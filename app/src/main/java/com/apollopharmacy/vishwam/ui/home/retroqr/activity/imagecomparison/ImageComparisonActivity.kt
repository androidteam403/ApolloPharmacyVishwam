package com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityImageComparisonBinding

class ImageComparisonActivity : AppCompatActivity(), ImageComparisonCallback {
    private lateinit var activityImageComparisonBinding: ActivityImageComparisonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityImageComparisonBinding =
            DataBindingUtil.setContentView(this@ImageComparisonActivity,
                R.layout.activity_image_comparison)
        activityImageComparisonBinding.callback = this@ImageComparisonActivity
    }

    override fun onClickBack() {
        finish()
    }

    override fun onClickClose() {
        finish()
    }
}