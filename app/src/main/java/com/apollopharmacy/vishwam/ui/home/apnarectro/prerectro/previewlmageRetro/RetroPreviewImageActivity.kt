package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPreviewRetroImageBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter.RetroPreviewImage

class RetroPreviewImageActivity : AppCompatActivity(), PreviewLastImageCallback,
    ViewPager.OnPageChangeListener {
    lateinit var activityPreviewImageBinding: ActivityPreviewRetroImageBinding
    private var previewImageAdapter: RetroPreviewImage? = null
    var list = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewImageBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview_retro_image
        )
        setUp()
    }

    private fun setUp() {
        list.add("https://askapollopr.blob.core.windows.net/sampleimage/ReturnImage_3857527_20230403_154049.jpg")
        previewImageAdapter= RetroPreviewImage(this,this,list)

        activityPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityPreviewImageBinding.previewImageViewpager.adapter = previewImageAdapter
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onClick(position: Int, status: String) {
    }
}
