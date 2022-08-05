package com.apollopharmacy.vishwam.ui.sampleui.swachlistmodule.swachlistscreen3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApproveListBinding
import com.apollopharmacy.vishwam.databinding.ActivityPreviewLastImageBinding

class PreviewLastImageActivity : AppCompatActivity() {
    lateinit var activityPreviewLastImageBinding: ActivityPreviewLastImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewLastImageBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview_last_image
        )
    }
}