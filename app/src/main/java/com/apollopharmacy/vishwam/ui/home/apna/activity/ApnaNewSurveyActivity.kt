package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityApnaNewSurveyBinding

class ApnaNewSurveyActivity : AppCompatActivity() {
    private lateinit var activityApnaNewSurveyBinding: ActivityApnaNewSurveyBinding
    private lateinit var apnaNewSurveyViewModel: ApnaNewSurveyViewModel
    var isLocationDetailsVisible = true
    var isShopDetailsVisible = false
    var isRentalDetailsVisible = false
    var isExistingBusinessDetailsVisible = false
    var isChemistDetailsVisible = false
    var isApartmentsDetailsVisible = false
    var isHospitalDetailsVisible = false
    var isSitePhotoDetailsVisible = false
    var isVideoDetailsVisible = false
    var isMapLocationDetailsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityApnaNewSurveyBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_apna_new_survey
        )

        apnaNewSurveyViewModel = ViewModelProvider(this)[ApnaNewSurveyViewModel::class.java]

        setUp()
    }

    private fun setUp() {

        // Location Details Layout
        activityApnaNewSurveyBinding.locationDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.locationDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.locationDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.locationDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.locationDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Shop Details
        activityApnaNewSurveyBinding.shopDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.shopDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.shopDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.shopDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.shopDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.shopDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Rental Details
        activityApnaNewSurveyBinding.rentalDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.rentalDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.rentalDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.rentalDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.rentalDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.rentalDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Existing Business Details
        activityApnaNewSurveyBinding.existingBusinessDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.existingBusinessDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.existingBusinessDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.existingBusinessDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.existingBusinessDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.existingBusinessDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }



        activityApnaNewSurveyBinding.next.setOnClickListener {
//            activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
//            activityApnaNewSurveyBinding.shopDetailsLayout.visibility = View.VISIBLE

//            activityApnaNewSurveyBinding.viewFlipper.showNext()

            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.next.visibility = View.GONE
        }



        activityApnaNewSurveyBinding.previousBtn.setOnClickListener {
//            activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.VISIBLE
//            activityApnaNewSurveyBinding.shopDetailsLayout.visibility = View.GONE

//            activityApnaNewSurveyBinding.viewFlipper.showPrevious()

            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
            activityApnaNewSurveyBinding.next.visibility = View.VISIBLE
        }
    }
}