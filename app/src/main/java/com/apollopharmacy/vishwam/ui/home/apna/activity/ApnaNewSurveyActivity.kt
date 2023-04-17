package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityApnaNewSurveyBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.SupportMapFragment

class ApnaNewSurveyActivity : AppCompatActivity() {
    private lateinit var activityApnaNewSurveyBinding: ActivityApnaNewSurveyBinding
    private lateinit var apnaNewSurveyViewModel: ApnaNewSurveyViewModel
    var displayedChild: Int = 0
    var childCount: Int = 0
    var currentPosition: Int = 0

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
        activityApnaNewSurveyBinding.backButton.setOnClickListener {
            finish()
        }

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

        // Site Specifications
        activityApnaNewSurveyBinding.siteSpecificationsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.siteSpecificationsExtraData.isVisible) {
                activityApnaNewSurveyBinding.siteSpecificationsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.siteSpecificationsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.siteSpecificationsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Market Information
        activityApnaNewSurveyBinding.marketInformationExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.marketInformationExtraData.isVisible) {
                activityApnaNewSurveyBinding.marketInformationExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.marketInformationExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.marketInformationExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Competitors Details
        activityApnaNewSurveyBinding.competitorsDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.competitorsDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.competitorsDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.competitorsDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.competitorsDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Population and Houses
        activityApnaNewSurveyBinding.populationAndHousesExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.populationAndHousesExtraData.isVisible) {
                activityApnaNewSurveyBinding.populationAndHousesExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.populationAndHousesExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.populationAndHousesExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Hospitals
        activityApnaNewSurveyBinding.hospitalsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.hospitalsExtraData.isVisible) {
                activityApnaNewSurveyBinding.hospitalsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.hospitalsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.hospitalsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Photos and Media
        activityApnaNewSurveyBinding.photoMediaExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.photoMediaExtraData.isVisible) {
                activityApnaNewSurveyBinding.photoMediaExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.photoMediaExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.photoMediaExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.photoMediaExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

//        showNext(currentPosition)

//        activityApnaNewSurveyBinding.next.setOnClickListener {
//            currentPosition++
//            activityApnaNewSurveyBinding.next.visibility = View.GONE
//            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
//            showNext(currentPosition)
//        }
//
//        activityApnaNewSurveyBinding.nextBtn.setOnClickListener {
//            if (currentPosition == 6) {
//                showNext(currentPosition)
//            } else {
//                currentPosition++
//                showNext(currentPosition)
//            }
//        }
//
//        activityApnaNewSurveyBinding.previousBtn.setOnClickListener {
//            currentPosition--
//            if (currentPosition == 0) {
//                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
//                activityApnaNewSurveyBinding.next.visibility = View.VISIBLE
//            }
//            showNext(currentPosition)
//        }
    }

//    private fun showNext(currentPosition: Int) {
//        when (currentPosition) {
//            0 -> {
//                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
//            }
//            1 -> {
//                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
//            }
//            2 -> {
//                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
//            }
//            3 -> {
//                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
//            }
//            4 -> {
//                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
//            }
//            5 -> {
//                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
//            }
//            6 -> {
//                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
//                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
//            }
//            else -> {
//                Toast.makeText(context, "No item available", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}
















//        displayedChild = activityApnaNewSurveyBinding.viewFlipper.displayedChild
//        childCount = activityApnaNewSurveyBinding.viewFlipper.childCount
//
//        activityApnaNewSurveyBinding.next.setOnClickListener {
//            activityApnaNewSurveyBinding.viewFlipper.showNext()
//            activityApnaNewSurveyBinding.next.visibility = View.GONE
//            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
//        }
//
//        activityApnaNewSurveyBinding.previousBtn.setOnClickListener {
//            displayedChild = activityApnaNewSurveyBinding.viewFlipper.displayedChild
//            childCount = activityApnaNewSurveyBinding.viewFlipper.childCount
//            if (displayedChild == 1) {
//                // First item is showing
//                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
//                activityApnaNewSurveyBinding.next.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.viewFlipper.showPrevious()
//            } else {
//                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
//                activityApnaNewSurveyBinding.next.visibility = View.GONE
//                activityApnaNewSurveyBinding.viewFlipper.showPrevious()
//            }
//        }
//
//        activityApnaNewSurveyBinding.nextBtn.setOnClickListener {
//            displayedChild = activityApnaNewSurveyBinding.viewFlipper.displayedChild
//            childCount = activityApnaNewSurveyBinding.viewFlipper.childCount
//            if (displayedChild == childCount - 2) {
//                // last item is showing
//                activityApnaNewSurveyBinding.nextBtn.visibility = View.GONE
//            } else {
//                activityApnaNewSurveyBinding.viewFlipper.showNext()
//                activityApnaNewSurveyBinding.nextBtn.visibility = View.VISIBLE
//            }
//        }