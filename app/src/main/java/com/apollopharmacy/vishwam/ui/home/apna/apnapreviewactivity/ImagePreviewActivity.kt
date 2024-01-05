package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityImagePreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter.ApnaImagePreviewAdapter
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class ImagePreviewActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    lateinit var activityImagePreviewBinding: ActivityImagePreviewBinding
    lateinit var apnaImagePreviewAdapter: ApnaImagePreviewAdapter
    var images = ArrayList<SurveyDetailsList.Image>()
    var currentPosition: Int = 0
    var totalImages: Int = 0
    var surveyId: String = ""
    var position: Int = 0
    var viewPagerCurrentPos: Int = 0

    var siteImageList = ArrayList<SurveyDetailsList.SiteImage>()
    var isMobileCreated = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityImagePreviewBinding = DataBindingUtil.setContentView(
            this@ImagePreviewActivity,
            R.layout.activity_image_preview
        )

        if (intent != null) {
            images = intent.getSerializableExtra("IMAGES") as ArrayList<SurveyDetailsList.Image>
            currentPosition = intent.getIntExtra("IMAGE_POSITION", -1)
            surveyId = intent.getStringExtra("SURVEY_ID").toString()

            siteImageList =
                intent.getSerializableExtra("IMAGE_WEBCREATED") as ArrayList<SurveyDetailsList.SiteImage>
            isMobileCreated = intent.getBooleanExtra("SOURCE", false);
        }
        totalImages = if (isMobileCreated) images.size else siteImageList.size

        if (position == 0) {
            activityImagePreviewBinding.previous.visibility = View.GONE
        }
        activityImagePreviewBinding.back.setOnClickListener {
            finish()
        }
        activityImagePreviewBinding.surveyId.setText(surveyId)
        if (if (isMobileCreated) images.size == 1 else siteImageList.size == 1) {
            if (isMobileCreated){
                activityImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / images.size).toString() + " / ")
                activityImagePreviewBinding.totalImages.setText(images.size.toString() + ")")
                activityImagePreviewBinding.next.visibility = View.GONE
            }else{
                activityImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / siteImageList.size).toString() + " / ")
                activityImagePreviewBinding.totalImages.setText(siteImageList.size.toString() + ")")
                activityImagePreviewBinding.next.visibility = View.GONE
            }

        } else {
            if (isMobileCreated){
                activityImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / images.size + 1).toString() + " / ")
                activityImagePreviewBinding.totalImages.setText(images.size.toString() + ")")
            }else{
                activityImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / siteImageList.size + 1).toString() + " / ")
                activityImagePreviewBinding.totalImages.setText(siteImageList.size.toString() + ")")
            }

        }

        apnaImagePreviewAdapter =
            ApnaImagePreviewAdapter(
                this@ImagePreviewActivity,
                images,
                currentPosition,
                isMobileCreated,
                siteImageList
            )
        activityImagePreviewBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityImagePreviewBinding.previewImageViewpager.adapter = apnaImagePreviewAdapter
        activityImagePreviewBinding.previewImageViewpager.setCurrentItem(currentPosition, true)
        nextPreviousListeners()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        viewPagerCurrentPos = position
        if (position == 0) {
            activityImagePreviewBinding.previous.visibility = View.GONE
        } else {
            activityImagePreviewBinding.previous.visibility = View.VISIBLE
        }
        if (if (isMobileCreated) (position == images.size - 1) else (position == siteImageList.size - 1)) {
            activityImagePreviewBinding.next.visibility = View.GONE
        } else {
            activityImagePreviewBinding.next.visibility = View.VISIBLE
        }
        if (isMobileCreated) {
            activityImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / images.size + 1).toString() + " / ")
            activityImagePreviewBinding.totalImages.setText(images.size.toString() + ")")
        }else{
            activityImagePreviewBinding.currentImage.setText("Total Images" + " ( " + (position + 1 / siteImageList.size + 1).toString() + " / ")
            activityImagePreviewBinding.totalImages.setText(siteImageList.size.toString() + ")")
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    fun nextPreviousListeners() {
        activityImagePreviewBinding.previous.setOnClickListener {
            activityImagePreviewBinding.previewImageViewpager.setCurrentItem(
                viewPagerCurrentPos - 1,
                true
            )

        }
        activityImagePreviewBinding.next.setOnClickListener {
            activityImagePreviewBinding.previewImageViewpager.setCurrentItem(
                viewPagerCurrentPos + 1,
                true
            )
        }
    }
}