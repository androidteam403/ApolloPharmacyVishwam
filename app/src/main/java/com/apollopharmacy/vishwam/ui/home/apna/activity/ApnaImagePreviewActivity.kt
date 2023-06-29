package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaImagePreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.adapter.SurveyImagePreviewAdapter
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class ApnaImagePreviewActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    lateinit var activityApnaImagePreviewBinding: ActivityApnaImagePreviewBinding
    lateinit var surveyImagePreviewAdapter: SurveyImagePreviewAdapter
    var images = ArrayList<SurveyCreateRequest.SiteImageMb.Image>()
    var currentPosition: Int = 0
    var totalImages: Int = 0
    var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApnaImagePreviewBinding =
            DataBindingUtil.setContentView(this@ApnaImagePreviewActivity,
                R.layout.activity_apna_image_preview)

        if (intent != null) {
            images =
                intent.getSerializableExtra("IMAGE_LIST") as ArrayList<SurveyCreateRequest.SiteImageMb.Image>
            currentPosition = intent.getIntExtra("CURRENT_POSITION", -1)
        }
        totalImages = images.size
        if (position == 0) {
            activityApnaImagePreviewBinding.previousArrow.visibility = View.GONE
        }
        activityApnaImagePreviewBinding.back.setOnClickListener {
            finish()
        }
        if (images.size == 1) {
            activityApnaImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / images.size).toString() + " / ")
            activityApnaImagePreviewBinding.totalImages.setText(images.size.toString() + ")")
            activityApnaImagePreviewBinding.nextArrow.visibility = View.GONE
        } else {
            activityApnaImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / images.size + 1).toString() + " / ")
            activityApnaImagePreviewBinding.totalImages.setText(images.size.toString() + ")")
        }

        surveyImagePreviewAdapter =
            SurveyImagePreviewAdapter(this@ApnaImagePreviewActivity, currentPosition, images)
        activityApnaImagePreviewBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityApnaImagePreviewBinding.previewImageViewpager.adapter = surveyImagePreviewAdapter
        activityApnaImagePreviewBinding.previewImageViewpager.setCurrentItem(currentPosition, true)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (position == 0) {
            activityApnaImagePreviewBinding.previousArrow.visibility = View.GONE
        } else {
            activityApnaImagePreviewBinding.previousArrow.visibility = View.VISIBLE
        }
        if (position == images.size - 1) {
            activityApnaImagePreviewBinding.nextArrow.visibility = View.GONE
        } else {
            activityApnaImagePreviewBinding.nextArrow.visibility = View.VISIBLE
        }
        activityApnaImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / images.size + 1).toString() + " / ")
        activityApnaImagePreviewBinding.totalImages.setText(images.size.toString() + ")")
    }

    override fun onPageScrollStateChanged(state: Int) {
    }
}