package com.apollopharmacy.vishwam.ui.home.apna.survey.videopreview

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaVideoPreviewBinding

class ApnaVideoPreview : AppCompatActivity() {
    lateinit var activityVideopreviewBinding: ActivityApnaVideoPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityVideopreviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_apna_video_preview)
        setUp()
    }

    private fun setUp() {

        activityVideopreviewBinding.arrow.setOnClickListener {
            onBackPressed()
        }
        if (intent != null) {
            val mediaController = MediaController(this)

            val videoUrl = intent.getStringExtra("activity")
            val uri = Uri.parse(videoUrl)
            activityVideopreviewBinding.videoView.setVideoURI(uri)
            mediaController.setAnchorView(activityVideopreviewBinding.videoView)

            activityVideopreviewBinding.videoView.setMediaController(mediaController)
            activityVideopreviewBinding.videoView.requestFocus()
            activityVideopreviewBinding.videoView.start()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}