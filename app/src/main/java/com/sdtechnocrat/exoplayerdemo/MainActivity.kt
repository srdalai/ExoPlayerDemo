package com.sdtechnocrat.exoplayerdemo

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.SubtitleConfiguration
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.CueGroup
import com.google.android.exoplayer2.ui.CaptionStyleCompat
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.MimeTypes
import com.google.common.collect.ImmutableList
import com.sdtechnocrat.exoplayerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val videoUrl = "https://d3rhd7hxja3fsq.cloudfront.net/LhOvpm3DVIqnFf99TC9ijZxWoa6P9Iuw/3D28C83C85544F0D9517DD2E7C7EEDAA/vl/0058555b017e4692b8f08d4f4d3f3517/videos/stream.mpd"
        const val subtitleUri = "https://d3rhd7hxja3fsq.cloudfront.net/LhOvpm3DVIqnFf99TC9ijZxWoa6P9Iuw/3D28C83C85544F0D9517DD2E7C7EEDAA/vl/0058555b017e4692b8f08d4f4d3f3517/subtitle/ae4d9104e38e43cbc65de3d5935a5fa5/Selena Gomez - Lose You To Love Me (Official Music Video).eng.eng-f0474412712d643232951179597df2ce.vtt"
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val subConfig = SubtitleConfiguration.Builder(Uri.parse(subtitleUri))
            .setMimeType(MimeTypes.TEXT_VTT)
            .setLanguage("English")
            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
            .build();

        val mediaItem = MediaItem.Builder()
            .setUri(videoUrl)
            .setSubtitleConfigurations(ImmutableList.of(subConfig))
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        player.seekTo(TimeUnit.SECONDS.toMillis(60))

        player.addAnalyticsListener(EventLogger())

        configureSubtitleView()

        player.addListener(PlayerListener())
    }

    private fun configureSubtitleView() {
        val textColor = Color.parseColor("#ffffff")
        val outlineColor = Color.parseColor("#000000")
        val backgroundColor = Color.parseColor("#000000")
        //val subtitleTypeface = ResourcesCompat.getFont(this, R.font.regular_font)
        val style = CaptionStyleCompat(
            textColor,
            backgroundColor, Color.TRANSPARENT,
            CaptionStyleCompat.EDGE_TYPE_NONE,
            outlineColor, /*subtitleTypeface*/null
        )
        binding.playerView.subtitleView?.let {
            it.setStyle(style)
            it.setViewType(SubtitleView.VIEW_TYPE_CANVAS)
            //it.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * 1f);
            //it.setBottomPaddingFraction(SubtitleView.DEFAULT_BOTTOM_PADDING_FRACTION * 20f)
            it.setBottomPaddingFraction(1f)
            it.setApplyEmbeddedStyles(true)
            it.setApplyEmbeddedFontSizes(true)
            it.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        }


        //setSubtitleMargin()
    }

    inner class PlayerListener: Player.Listener {
        override fun onCues(cues: MutableList<Cue>) {
            super.onCues(cues)
        }

        override fun onCues(cueGroup: CueGroup) {
            super.onCues(cueGroup)
            for (cue in cueGroup.cues) {
                //Log.d(TAG, cue.text.toString())
                val modifiedCueText = cue.text.toString() + "\n"
                val spannedCueText = SpannableString(modifiedCueText)
                spannedCueText.setSpan(SubBackgroundSpan(this@MainActivity), 0, spannedCueText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.textViewSub.text = spannedCueText
            }
        }
    }
}