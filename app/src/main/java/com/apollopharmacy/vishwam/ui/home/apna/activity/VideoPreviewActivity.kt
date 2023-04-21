package com.apollopharmacy.vishwam.ui.home.apna.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityVideoPreviewBinding

class VideoPreviewActivity : AppCompatActivity() {
    lateinit var activityVideoPreviewBinding: ActivityVideoPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVideoPreviewBinding = DataBindingUtil.setContentView(this@VideoPreviewActivity, R.layout.activity_video_preview)


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