package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityVideoPreviewsBinding

class VideoPreviewActivity : AppCompatActivity() {
    public lateinit var activityVideoPreviewBinding: ActivityVideoPreviewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVideoPreviewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_video_previews)


        val intent = intent
        val videoPath = intent.getStringExtra("VIDEO_URI")
        activityVideoPreviewBinding.previewVideo.setVideoPath(videoPath.toString())
        val mediaController = MediaController(this@VideoPreviewActivity)
        mediaController.setAnchorView(activityVideoPreviewBinding.previewVideo)
        activityVideoPreviewBinding.previewVideo.setMediaController(mediaController)
        activityVideoPreviewBinding.previewVideo.requestFocus()
        activityVideoPreviewBinding.previewVideo.start()

        activityVideoPreviewBinding.close.setOnClickListener {
            finish()
        }
    }
}