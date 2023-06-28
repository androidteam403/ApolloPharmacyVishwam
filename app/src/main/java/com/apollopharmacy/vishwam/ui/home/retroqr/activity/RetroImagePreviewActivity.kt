package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityRetroImagePreviewBinding

class RetroImagePreviewActivity : AppCompatActivity() {
    private lateinit var activityRetroImagePreviewBinding: ActivityRetroImagePreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroImagePreviewBinding =
            DataBindingUtil.setContentView(this@RetroImagePreviewActivity,
                R.layout.activity_retro_image_preview)
    }
}