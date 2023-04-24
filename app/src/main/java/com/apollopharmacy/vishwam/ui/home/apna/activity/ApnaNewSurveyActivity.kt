package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.*
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ApnaNewSurveyActivity : AppCompatActivity(), ApnaNewSurveyCallBack {
    var length = 0.0
    var width = 0.0

    var isLocationDetailsCompleted: Boolean = false
    var isSiteSpecificationsCompleted: Boolean = false
    var isMarketInformationCompleted: Boolean = false
    var isCompetitorsDetailsCompleted: Boolean = false
    var isPopulationAndHousesCompleted: Boolean = false
    var isHospitalsCompleted: Boolean = false
    var isPhotosAndMediaCompleted: Boolean = false

    lateinit var trafficStreetDialog: Dialog
    lateinit var trafficGeneratorDialog: Dialog
    lateinit var apartmentTypeDialog: Dialog
    lateinit var apnaSpecialityDialog: Dialog
    lateinit var organisedDialog: Dialog
    lateinit var unorganisedDialog: Dialog

    var selectedTrafficGeneratorItem = ArrayList<String>()

    var chemistList = ArrayList<ChemistData>()
    var hospitalsList = ArrayList<HospitalData>()
    var apartmentsList = ArrayList<ApartmentData>()
    var locationList = ArrayList<LocationListResponse.Data.ListData.Row>()
    var trafficStreetData = ArrayList<TrafficStreetTypeResponse.Data.ListData.Row>()
    var trafficGeneratorData = ArrayList<TrafficGeneratorsResponse.Data.ListData.Row>()
    var apartmentTypeData = ArrayList<ApartmentTypeResponse.Data.ListData.Row>()
    var apnaSpecialityData = ArrayList<ApnaSpecialityResponse.Data.ListData.Row>()
    var parkingTypeList = ArrayList<ParkingTypeResponse.Data.ListData.Row>()
    var dimensionTypeList = ArrayList<DimensionTypeResponse.Data.ListData.Row>()
    var neighbouringLocationList = ArrayList<NeighbouringLocationResponse.Data.ListData.Row>()

    private lateinit var activityApnaNewSurveyBinding: ActivityApnaNewSurveyBinding
    private lateinit var apnaNewSurveyViewModel: ApnaNewSurveyViewModel
    var currentPosition: Int = 0
    var imageList = ArrayList<Image>()

    var imageFile: File? = null
    var videoFile: File? = null
    private var compressedImageFileName: String? = null

    lateinit var imageAdapter: ImageAdapter
    lateinit var trafficGeneratorsItemAdapter: TrafficGeneratorsItemAdapter
    lateinit var trafficStreetAdapter: TrafficStreetAdapter
    lateinit var trafficGeneratorAdapter: TrafficGeneratorAdapter
    lateinit var apartmentTypeAdapter: ApartmentTypeAdapter
    lateinit var apartmentTypeItemAdapter: ApartmentTypeItemAdapter
    lateinit var apnaSpecialityAdapter: ApnaSpecialityAdapter
    lateinit var hospitalsAdapter: HospitalsAdapter
    lateinit var organisedAdapter: OrganisedAdapter
    lateinit var unOrganisedAdapter: UnOrganisedAdapter
    lateinit var chemistAdapter: ChemistAdapter

    val REQUEST_CODE_CAMERA = 2235211
    val REQUEST_CODE_VIDEO = 2156


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityApnaNewSurveyBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_apna_new_survey
        )

        apnaNewSurveyViewModel = ViewModelProvider(this)[ApnaNewSurveyViewModel::class.java]

        setUp()
    }

    @SuppressLint("SetTextI18n")
    private fun setUp() {

        // Location list api call
        apnaNewSurveyViewModel.getLocationList(this@ApnaNewSurveyActivity)

        activityApnaNewSurveyBinding.backButton.setOnClickListener {
            finish()
        }

        activityApnaNewSurveyBinding.locationDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.locationDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.locationDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.locationDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.locationDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.siteSpecificationsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.siteSpecificationsExtraData.isVisible) {
                activityApnaNewSurveyBinding.siteSpecificationsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.siteSpecificationsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.siteSpecificationsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.marketInformationExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.marketInformationExtraData.isVisible) {
                activityApnaNewSurveyBinding.marketInformationExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.marketInformationExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.marketInformationExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.competitorsDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.competitorsDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.competitorsDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.competitorsDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.competitorsDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.populationAndHousesExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.populationAndHousesExtraData.isVisible) {
                activityApnaNewSurveyBinding.populationAndHousesExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.populationAndHousesExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.populationAndHousesExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.hospitalsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.hospitalsExtraData.isVisible) {
                activityApnaNewSurveyBinding.hospitalsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.hospitalsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.hospitalsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.photoMediaExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.photoMediaExtraData.isVisible) {
                activityApnaNewSurveyBinding.photoMediaExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.photoMediaExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.photoMediaExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.photoMediaExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.filterIcon.setOnClickListener {
            val dialogBinding: DialogQuickGoBinding? =
                DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.dialog_quick_go,
                    null,
                    false)
            val customDialog = android.app.AlertDialog.Builder(this, 0).create()
            customDialog.apply {

                setView(dialogBinding?.root)
                setCancelable(false)
                dialogBinding!!.close.setOnClickListener {
                    dismiss()
                }
                dialogBinding.locationDetails.setOnClickListener {
                    showNext(0)
                    customDialog.dismiss()
                }
                dialogBinding.siteSpecification.setOnClickListener {
                    showNext(1)
                    customDialog.dismiss()
                }
                dialogBinding.marketInformation.setOnClickListener {
                    showNext(2)
                    customDialog.dismiss()
                }
                dialogBinding.competitorsDetails.setOnClickListener {
                    showNext(3)
                    customDialog.dismiss()
                }
                dialogBinding.populationAndHouses.setOnClickListener {
                    showNext(4)
                    customDialog.dismiss()
                }
                dialogBinding.hospitals.setOnClickListener {
                    showNext(5)
                    customDialog.dismiss()
                }
                dialogBinding.photosAndMedia.setOnClickListener {
                    showNext(6)
                    customDialog.dismiss()
                }

//                val location = activityApnaNewSurveyBinding.locationText.text.toString().trim()
//                val city = activityApnaNewSurveyBinding.cityText.text.toString().trim()
//                val state = activityApnaNewSurveyBinding.stateText.text.toString().trim()
//                val pin = activityApnaNewSurveyBinding.pinText.text.toString().trim()
//                val latitude = activityApnaNewSurveyBinding.latitude.text.toString().trim()
//                val longitude = activityApnaNewSurveyBinding.longitude.text.toString().trim()
//                if (location.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && pin.isNotEmpty() && latitude.isNotEmpty() && longitude.isNotEmpty()) {
//                    dialogBinding.locationDetailsCount.visibility = View.GONE
//                    dialogBinding.locationDetailsCompleted.visibility = View.VISIBLE
//                    dialogBinding.locationDetailsProgress.visibility = View.GONE
//                    dialogBinding.locationDetails.setTextColor(Color.parseColor("#00a651"))
//                } else if (location.isNotEmpty() || city.isNotEmpty() || state.isNotEmpty() || pin.isNotEmpty() || latitude.isNotEmpty() || longitude.isNotEmpty()) {
//                    dialogBinding.locationDetailsCount.visibility = View.GONE
//                    dialogBinding.locationDetailsCompleted.visibility = View.GONE
//                    dialogBinding.locationDetailsProgress.visibility = View.VISIBLE
//                    dialogBinding.locationDetails.setTextColor(Color.parseColor("#f7931e"))
//                } else {
//                    dialogBinding.locationDetailsCount.visibility = View.VISIBLE
//                    dialogBinding.locationDetailsCompleted.visibility = View.GONE
//                    dialogBinding.locationDetailsProgress.visibility = View.GONE
//                    dialogBinding.locationDetails.setTextColor(Color.parseColor("#000000"))
//                }

            }.show()
        }


        showNext(currentPosition)

        activityApnaNewSurveyBinding.next.setOnClickListener {
            currentPosition++
            activityApnaNewSurveyBinding.next.visibility = View.GONE
            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
            showNext(currentPosition)
        }

        activityApnaNewSurveyBinding.previous.setOnClickListener {
            currentPosition--
            activityApnaNewSurveyBinding.previous.visibility = View.GONE
            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.next.visibility = View.GONE
            showNext(currentPosition)
        }

        activityApnaNewSurveyBinding.nextBtn.setOnClickListener {
            if (currentPosition == 6) {
                showNext(currentPosition)
            } else {
                currentPosition++
                showNext(currentPosition)
            }
        }

        activityApnaNewSurveyBinding.previousBtn.setOnClickListener {
            currentPosition--
            if (currentPosition == 0) {
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.next.visibility = View.VISIBLE
            }
            showNext(currentPosition)
        }

        // Capture site photos
        activityApnaNewSurveyBinding.addSiteImage.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(100)
            } else {
                if (imageList.size == 5) {
                    Toast.makeText(this@ApnaNewSurveyActivity,
                        "You are allowed to upload only five images",
                        Toast.LENGTH_SHORT).show()
                }
                openCamera()
            }
        }

        imageAdapter = ImageAdapter(context, imageList, this)
        activityApnaNewSurveyBinding.siteImagesRecyclerView.adapter = imageAdapter
        activityApnaNewSurveyBinding.siteImagesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Record Video
        activityApnaNewSurveyBinding.videoIcon.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(100)
            } else {
                recordVideo()
            }
        }
        // Delete Video
        activityApnaNewSurveyBinding.deleteVideo.setOnClickListener {
            videoFile = null
            activityApnaNewSurveyBinding.beforeCaptureLayout.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.afterCaptureLayout.visibility = View.GONE
            activityApnaNewSurveyBinding.playVideo.visibility = View.GONE
            activityApnaNewSurveyBinding.deleteVideo.visibility = View.GONE
        }

        // Video preview
        activityApnaNewSurveyBinding.playVideo.setOnClickListener {
            val intent = Intent(this@ApnaNewSurveyActivity, VideoPreviewActivity::class.java)
            intent.putExtra("VIDEO_URI", videoFile!!.absolutePath)
            startActivity(intent)

//            val dialog = Dialog(this@ApnaNewSurveyActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            val dialogVideoPreviewBinding: DialogVideoPreviewBinding = DataBindingUtil.inflate(
//                LayoutInflater.from(this),
//                R.layout.dialog_video_preview,
//                null,
//                false
//            )
//            dialog.setContentView(dialogVideoPreviewBinding.root)
//            val videoUri = Uri.parse(videoFile!!.absolutePath)
//            dialogVideoPreviewBinding.previewVideo.setVideoPath(videoUri.toString())
//
//            val mediaController = MediaController(this@ApnaNewSurveyActivity)
//            mediaController.setAnchorView(dialogVideoPreviewBinding.previewVideo)
//            dialogVideoPreviewBinding.previewVideo.setMediaController(mediaController)
//            dialogVideoPreviewBinding.previewVideo.requestFocus()
//            dialogVideoPreviewBinding.previewVideo.start()
//
//            dialogVideoPreviewBinding.close.setOnClickListener {
//                dialog.dismiss()
//            }
//            dialog.show()
        }

        // Traffic street dropdown
        activityApnaNewSurveyBinding.trafficStreetSelect.setOnClickListener {
            trafficStreetDialog =
                Dialog(this@ApnaNewSurveyActivity)//, android.R.style.Theme_Black_NoTitleBar_Fullscreen
            val dialogTrafficStreetBinding = DataBindingUtil.inflate<DialogTrafficStreetBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_traffic_street,
                null,
                false)
            trafficStreetDialog.setContentView(dialogTrafficStreetBinding.root)
            trafficStreetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            trafficStreetDialog.setCancelable(false)
            dialogTrafficStreetBinding.closeDialog.setOnClickListener {
                trafficStreetDialog.dismiss()
            }

            trafficStreetAdapter = TrafficStreetAdapter(this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                trafficStreetData)
            dialogTrafficStreetBinding.trafficStreetRcv.adapter = trafficStreetAdapter
            dialogTrafficStreetBinding.trafficStreetRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogTrafficStreetBinding.searchTrafficStreetText.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
//                    trafficStreetAdapter.filter(s.toString())
                    trafficStreetFilter(s.toString(), dialogTrafficStreetBinding)
                }
            })

            trafficStreetDialog.show()


//            TrafficStreetDialog().apply {
//
//            }.show(supportFragmentManager, "")
        }

        // Traffic Generator dropdown
        activityApnaNewSurveyBinding.trafficGeneratorSelect.setOnClickListener {
            trafficGeneratorDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogTrafficGeneratorBinding =
                DataBindingUtil.inflate<DialogTrafficGeneratorBinding>(
                    LayoutInflater.from(this@ApnaNewSurveyActivity),
                    R.layout.dialog_traffic_generator,
                    null,
                    false
                )
            trafficGeneratorDialog.setContentView(dialogTrafficGeneratorBinding.root)
            trafficGeneratorDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            trafficGeneratorDialog.setCancelable(false)
            dialogTrafficGeneratorBinding.closeDialog.setOnClickListener {
                trafficGeneratorDialog.dismiss()
            }

            trafficGeneratorAdapter =
                TrafficGeneratorAdapter(this@ApnaNewSurveyActivity,
                    this@ApnaNewSurveyActivity,
                    trafficGeneratorData)
            dialogTrafficGeneratorBinding.trafficGeneratorRcv.adapter = trafficGeneratorAdapter
            dialogTrafficGeneratorBinding.trafficGeneratorRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogTrafficGeneratorBinding.searchTrafficGeneratorText.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    trafficGeneratorFilter(s.toString(), dialogTrafficGeneratorBinding)
                }
            })
            trafficGeneratorDialog.show()
        }

        // Apartment type dropdown
        activityApnaNewSurveyBinding.apartmentTypeSelect.setOnClickListener {
            apartmentTypeDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogApartmentTypeBinding = DataBindingUtil.inflate<DialogApartmentTypeBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_apartment_type,
                null,
                false
            )

            apartmentTypeDialog.setContentView(dialogApartmentTypeBinding.root)
            apartmentTypeDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            apartmentTypeDialog.setCancelable(false)
            dialogApartmentTypeBinding.closeDialog.setOnClickListener {
                apartmentTypeDialog.dismiss()
            }

            apartmentTypeAdapter = ApartmentTypeAdapter(this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                apartmentTypeData)
            dialogApartmentTypeBinding.apartmentTypeRcv.adapter = apartmentTypeAdapter
            dialogApartmentTypeBinding.apartmentTypeRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogApartmentTypeBinding.searchApartmentTypeText.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    apartmentTypeFilter(s.toString(), dialogApartmentTypeBinding)
                }
            })
            apartmentTypeDialog.show()
        }

        // Hospital speciality dropdown
        activityApnaNewSurveyBinding.hospitalSpecialitySelect.setOnClickListener {
            apnaSpecialityDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogApnaSpecialityBinding =
                DataBindingUtil.inflate<DialogApnaSpecialityBinding>(
                    LayoutInflater.from(this@ApnaNewSurveyActivity),
                    R.layout.dialog_apna_speciality,
                    null,
                    false
                )
            apnaSpecialityDialog.setContentView(dialogApnaSpecialityBinding.root)
            apnaSpecialityDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            apnaSpecialityDialog.setCancelable(false)
            dialogApnaSpecialityBinding.closeDialog.setOnClickListener {
                apnaSpecialityDialog.dismiss()
            }

            apnaSpecialityAdapter = ApnaSpecialityAdapter(
                this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                apnaSpecialityData
            )
            dialogApnaSpecialityBinding.specialityRcv.adapter = apnaSpecialityAdapter
            dialogApnaSpecialityBinding.specialityRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogApnaSpecialityBinding.searchSpecialityText.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    apnaSpecialityFilter(s.toString(), dialogApnaSpecialityBinding)
                }

            })
            apnaSpecialityDialog.show()
        }

        // Adding apartments data
        activityApnaNewSurveyBinding.apartmentsAddBtn.setOnClickListener {
            val apartmentType = activityApnaNewSurveyBinding.apartmentTypeSelect.text
            val noOfHouses = activityApnaNewSurveyBinding.noOfHousesText.text
            val distance = activityApnaNewSurveyBinding.distanceText.text
            apartmentsList.add(ApartmentData(
                apartmentType.toString(),
                noOfHouses.toString().toInt(),
                distance.toString().toInt()
            ))
            apartmentTypeItemAdapter = ApartmentTypeItemAdapter(
                this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                apartmentsList
            )
            activityApnaNewSurveyBinding.apartmentsRecyclerView.adapter = apartmentTypeItemAdapter
            activityApnaNewSurveyBinding.apartmentsRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)
            apartmentTypeItemAdapter.notifyDataSetChanged()
        }

        // Adding hospital data
        activityApnaNewSurveyBinding.hospitalAddBtn.setOnClickListener {
            var name = activityApnaNewSurveyBinding.hospitalNameText.text.toString()
            var speciality = activityApnaNewSurveyBinding.hospitalSpecialitySelect.text.toString()
            var beds = activityApnaNewSurveyBinding.bedsText.text.toString()
            var noOfOpd = activityApnaNewSurveyBinding.noOfOpdText.text.toString()
            var occupancy = activityApnaNewSurveyBinding.occupancyText.text.toString()

            hospitalsList.add(HospitalData(
                name,
                beds.toInt(),
                speciality,
                noOfOpd.toInt(),
                occupancy.toInt()
            ))

            hospitalsAdapter = HospitalsAdapter(
                this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                hospitalsList
            )
            activityApnaNewSurveyBinding.hospitalsRecyclerView.adapter = hospitalsAdapter
            activityApnaNewSurveyBinding.hospitalsRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)
            hospitalsAdapter.notifyDataSetChanged()
        }

        // Adding chemist data
        activityApnaNewSurveyBinding.chemistAddBtn.setOnClickListener {
            val chemist = activityApnaNewSurveyBinding.chemistText.text.toString()
            val organised = activityApnaNewSurveyBinding.organisedSelect.text.toString()
            val organisedAvgSale = activityApnaNewSurveyBinding.organisedAvgSaleText.text.toString()
            val unorganised = activityApnaNewSurveyBinding.unorganisedSelect.text.toString()
            val unorganisedAvgSale =
                activityApnaNewSurveyBinding.unorganisedAvgSaleText.text.toString()

            chemistList.add(ChemistData(
                chemist,
                organised,
                organisedAvgSale,
                unorganised,
                unorganisedAvgSale
            ))
            chemistAdapter =
                ChemistAdapter(this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, chemistList)
            activityApnaNewSurveyBinding.chemistRecyclerView.adapter = chemistAdapter
            activityApnaNewSurveyBinding.chemistRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            if (chemistList.size > 0) {
                activityApnaNewSurveyBinding.chemistTotalLayout.visibility = View.VISIBLE
                val totalOrganisedAvgSale =
                    chemistList.stream().map { it.organisedAvgSale }.mapToDouble { it.toDouble() }
                        .sum()
                activityApnaNewSurveyBinding.totalOrganisedText.text =
                    String.format("%.1f", totalOrganisedAvgSale) + " Lac"

                val totalUnorganisedAvgSale =
                    chemistList.stream().map { it.unorganisedAvgSale }.mapToDouble { it.toDouble() }
                        .sum()
                activityApnaNewSurveyBinding.totalUnorganisedText.text =
                    String.format("%.1f", totalUnorganisedAvgSale) + " Lac"
            } else {
                activityApnaNewSurveyBinding.chemistTotalLayout.visibility = View.GONE
            }
        }

        // Organised Dropdown
        activityApnaNewSurveyBinding.organisedSelect.setOnClickListener {
            organisedDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogOrganisedBinding = DataBindingUtil.inflate<DialogOrganisedBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_organised,
                null,
                false
            )
            organisedDialog.setContentView(dialogOrganisedBinding.root)

            organisedDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            organisedDialog.setCancelable(false)
            dialogOrganisedBinding.closeDialog.setOnClickListener {
                organisedDialog.dismiss()
            }

            organisedAdapter = OrganisedAdapter(this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                parkingTypeList)
            dialogOrganisedBinding.organisedRcv.adapter = organisedAdapter
            dialogOrganisedBinding.organisedRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogOrganisedBinding.searchOrganisedText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    organisedFilter(s.toString(), dialogOrganisedBinding)
                }

            })
            organisedDialog.show()
        }

        // unorganised dropdown
        activityApnaNewSurveyBinding.unorganisedSelect.setOnClickListener {
            unorganisedDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogUnorganisedBinding = DataBindingUtil.inflate<DialogUnorganisedBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_unorganised,
                null,
                false
            )
            unorganisedDialog.setContentView(dialogUnorganisedBinding.root)

            unorganisedDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            unorganisedDialog.setCancelable(false)
            dialogUnorganisedBinding.closeDialog.setOnClickListener {
                unorganisedDialog.dismiss()
            }

            unOrganisedAdapter = UnOrganisedAdapter(this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                parkingTypeList)
            dialogUnorganisedBinding.unorganisedRcv.adapter = unOrganisedAdapter
            dialogUnorganisedBinding.unorganisedRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogUnorganisedBinding.searchUnOrganisedText.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    unorganisedFilter(s.toString(), dialogUnorganisedBinding)
                }

            })
            unorganisedDialog.show()
        }

        // Morning time dropdown
        activityApnaNewSurveyBinding.morningTimeSelect.setOnClickListener {
            val calendar = Calendar.getInstance()
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            var minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                this@ApnaNewSurveyActivity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        var simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        var formattedTime = simpleDateFormat.format(calendar.time)
                        activityApnaNewSurveyBinding.morningTimeSelect.setText(formattedTime)
                    }
                }, hour, minute, false
            )
            timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            timePickerDialog.show()
        }

        // Evening time dropdown
        activityApnaNewSurveyBinding.eveningTimeSelect.setOnClickListener {
            val calendar = Calendar.getInstance()
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            var minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                this@ApnaNewSurveyActivity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        var simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        var formattedTime = simpleDateFormat.format(calendar.time)
                        activityApnaNewSurveyBinding.eveningTimeSelect.setText(formattedTime)
                    }

                }, hour, minute, false
            )
            timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            timePickerDialog.show()
        }

        // Total area sq ft calculate
        activityApnaNewSurveyBinding.lengthText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString()
                        .isNotEmpty() && activityApnaNewSurveyBinding.widthText.text.toString()
                        .isNotEmpty()
                ) {
                    length = s.toString().toDouble()
                    width = activityApnaNewSurveyBinding.widthText.text.toString().toDouble()
                    updateTotalArea(length, width)
                } else if (s.toString()
                        .isNotEmpty() && activityApnaNewSurveyBinding.widthText.text.toString()
                        .isEmpty()
                ) {
                    activityApnaNewSurveyBinding.totalAreaText.text = s.toString()
                } else if (s.toString()
                        .isEmpty() && activityApnaNewSurveyBinding.widthText.text.toString()
                        .isNotEmpty()
                ) {
                    activityApnaNewSurveyBinding.totalAreaText.text =
                        activityApnaNewSurveyBinding.widthText.text.toString()
                } else {
                    activityApnaNewSurveyBinding.totalAreaText.text = "0.0"
                }
            }
        })

        activityApnaNewSurveyBinding.widthText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString()
                        .isNotEmpty() && activityApnaNewSurveyBinding.lengthText.text.toString()
                        .isNotEmpty()
                ) {
                    width = s.toString().toDouble()
                    length = activityApnaNewSurveyBinding.lengthText.text.toString().toDouble()
                    updateTotalArea(length, width)
                } else if (s.toString()
                        .isNotEmpty() && activityApnaNewSurveyBinding.lengthText.text.toString()
                        .isEmpty()
                ) {
                    activityApnaNewSurveyBinding.totalAreaText.text =
                        activityApnaNewSurveyBinding.widthText.text.toString()
                } else if (s.toString()
                        .isEmpty() && activityApnaNewSurveyBinding.lengthText.text.toString()
                        .isNotEmpty()
                ) {
                    activityApnaNewSurveyBinding.totalAreaText.text =
                        activityApnaNewSurveyBinding.lengthText.text.toString()
                } else {
                    activityApnaNewSurveyBinding.totalAreaText.text = "0.0"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        activityApnaNewSurveyBinding.clear.setOnClickListener {
            activityApnaNewSurveyBinding.totalAreaText.text = "0.0"
            activityApnaNewSurveyBinding.lengthText.text!!.clear()
            activityApnaNewSurveyBinding.widthText.text!!.clear()
        }
    }

    private fun updateTotalArea(length: Double, width: Double) {
        activityApnaNewSurveyBinding.totalAreaText.text = String.format("%.1f", (length * width))
    }

    private fun unorganisedFilter(
        searchText: String,
        dialogUnorganisedBinding: DialogUnorganisedBinding?,
    ) {
        val filteredList = ArrayList<ParkingTypeResponse.Data.ListData.Row>()
        for (i in parkingTypeList.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(parkingTypeList)
            } else {
                if (parkingTypeList[i].name!!.contains(searchText, true)) {
                    filteredList.add(parkingTypeList[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogUnorganisedBinding!!.unorganisedRcv.visibility = View.GONE
            dialogUnorganisedBinding.unorganisedAvailable.visibility = View.VISIBLE
        } else {
            dialogUnorganisedBinding!!.unorganisedRcv.visibility = View.VISIBLE
            dialogUnorganisedBinding.unorganisedAvailable.visibility = View.GONE
        }
        unOrganisedAdapter.filter(filteredList)
    }

    private fun organisedFilter(
        searchText: String,
        dialogOrganisedBinding: DialogOrganisedBinding?,
    ) {
        var filteredList = ArrayList<ParkingTypeResponse.Data.ListData.Row>()
        for (i in parkingTypeList.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(parkingTypeList)
            } else {
                if (parkingTypeList[i].name!!.contains(searchText, true)) {
                    filteredList.add(parkingTypeList[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogOrganisedBinding!!.organisedRcv.visibility = View.GONE
            dialogOrganisedBinding.organisedAvailable.visibility = View.VISIBLE
        } else {
            dialogOrganisedBinding!!.organisedRcv.visibility = View.VISIBLE
            dialogOrganisedBinding.organisedAvailable.visibility = View.GONE
        }
        organisedAdapter.filter(filteredList)
    }

    private fun apnaSpecialityFilter(
        searchText: String,
        dialogApnaSpecialityBinding: DialogApnaSpecialityBinding?,
    ) {
        val filteredList = ArrayList<ApnaSpecialityResponse.Data.ListData.Row>()
        for (i in apnaSpecialityData.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(apnaSpecialityData)
            } else {
                if (apnaSpecialityData[i].name!!.contains(searchText, true)) {
                    filteredList.add(apnaSpecialityData[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogApnaSpecialityBinding!!.specialityRcv.visibility = View.GONE
            dialogApnaSpecialityBinding.specialityAvailable.visibility = View.VISIBLE
        } else {
            dialogApnaSpecialityBinding!!.specialityRcv.visibility = View.VISIBLE
            dialogApnaSpecialityBinding.specialityAvailable.visibility = View.GONE
        }
        apnaSpecialityAdapter.filter(filteredList)
    }

    private fun apartmentTypeFilter(
        searchText: String,
        dialogApartmentTypeBinding: DialogApartmentTypeBinding?,
    ) {
        val filteredList = ArrayList<ApartmentTypeResponse.Data.ListData.Row>()
        for (i in apartmentTypeData.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(apartmentTypeData)
            } else {
                if (apartmentTypeData[i].name!!.contains(searchText, true)) {
                    filteredList.add(apartmentTypeData[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogApartmentTypeBinding!!.apartmentTypeRcv.visibility = View.GONE
            dialogApartmentTypeBinding.apartmentTypeAvailable.visibility = View.VISIBLE
        } else {
            dialogApartmentTypeBinding!!.apartmentTypeRcv.visibility = View.VISIBLE
            dialogApartmentTypeBinding.apartmentTypeAvailable.visibility = View.GONE
        }
        apartmentTypeAdapter.filter(filteredList)
    }

    private fun trafficGeneratorFilter(
        searchText: String,
        dialogTrafficGeneratorBinding: DialogTrafficGeneratorBinding,
    ) {
        val filteredList = ArrayList<TrafficGeneratorsResponse.Data.ListData.Row>()
        for (i in trafficGeneratorData.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(trafficGeneratorData)
            } else {
                if (trafficGeneratorData[i].name!!.contains(searchText, true)) {
                    filteredList.add(trafficGeneratorData[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogTrafficGeneratorBinding.trafficGeneratorRcv.visibility = View.GONE
            dialogTrafficGeneratorBinding.trafficGeneratorAvailable.visibility = View.VISIBLE
        } else {
            dialogTrafficGeneratorBinding.trafficGeneratorRcv.visibility = View.VISIBLE
            dialogTrafficGeneratorBinding.trafficGeneratorAvailable.visibility = View.GONE
        }
        trafficGeneratorAdapter.filter(filteredList)
    }

    private fun trafficStreetFilter(
        searchText: String,
        dialogTrafficStreetBinding: DialogTrafficStreetBinding,
    ) {
        val filteredList = ArrayList<TrafficStreetTypeResponse.Data.ListData.Row>()
        for (i in trafficStreetData.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(trafficStreetData)
            } else {
                if (trafficStreetData[i].name!!.contains(searchText, true)) {
                    filteredList.add(trafficStreetData[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogTrafficStreetBinding.trafficStreetRcv.visibility = View.GONE
            dialogTrafficStreetBinding.trafficStreetAvailable.visibility = View.VISIBLE
        } else {
            dialogTrafficStreetBinding.trafficStreetRcv.visibility = View.VISIBLE
            dialogTrafficStreetBinding.trafficStreetAvailable.visibility = View.GONE
        }
        trafficStreetAdapter.filter(filteredList)
    }

    private fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
        videoFile = File(context.cacheDir, "${System.currentTimeMillis()}.mp4")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile))
        } else {
            val videoUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                videoFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivityForResult(intent, REQUEST_CODE_VIDEO)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        compressedImageFileName = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && imageFile != null) {
            imageList.add(Image(imageFile!!, ""))
            imageAdapter.notifyDataSetChanged()
        } else if (requestCode == REQUEST_CODE_VIDEO && resultCode == Activity.RESULT_OK && videoFile != null) {
            activityApnaNewSurveyBinding.beforeCaptureLayout.visibility = View.GONE
            activityApnaNewSurveyBinding.afterCaptureLayout.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.deleteVideo.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.playVideo.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.afterCapturedVideo.setVideoURI(Uri.parse(videoFile!!.absolutePath))
        }
    }

    private fun showNext(currentPosition: Int) {
        when (currentPosition) {
            0 -> {
                if (locationList.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getLocationList(this@ApnaNewSurveyActivity)
                }
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.next.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.previous.visibility = View.GONE
                this.currentPosition = 0
            }
            1 -> {
                if (dimensionTypeList.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getDimensionType(this@ApnaNewSurveyActivity)
                }
                if (parkingTypeList.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getParkingType(this@ApnaNewSurveyActivity)
                }
                if (trafficStreetData.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getTrafficStreetType(this@ApnaNewSurveyActivity)
                }
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previous.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE
                this.currentPosition = 1
            }
            2 -> {
                if (neighbouringLocationList.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getNeighbouringLocation(this@ApnaNewSurveyActivity)
                }
                if (trafficGeneratorData.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getTrafficGeneratorsType(this@ApnaNewSurveyActivity)
                }
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previous.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE
                this.currentPosition = 2
            }
            3 -> {
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previous.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE
                this.currentPosition = 3
            }
            4 -> {
                if (apartmentTypeData.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getApartmentType(this@ApnaNewSurveyActivity)
                }
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previous.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE
                this.currentPosition = 4
            }
            5 -> {
                if (apnaSpecialityData.size == 0) {
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    apnaNewSurveyViewModel.getApnaSpeciality(this@ApnaNewSurveyActivity)
                }
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previous.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE
                this.currentPosition = 5
            }
            6 -> {
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previous.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.next.visibility = View.GONE
                this.currentPosition = 6
            }
            else -> {
            }
        }
    }

    private fun validateLocationDetails(): Boolean {
        val location = activityApnaNewSurveyBinding.locationText.text.toString().trim()
        val city = activityApnaNewSurveyBinding.cityText.text.toString().trim()
        val state = activityApnaNewSurveyBinding.stateText.text.toString().trim()
        val pin = activityApnaNewSurveyBinding.pinText.text.toString().trim()
        val landmarks = activityApnaNewSurveyBinding.nearByLandmarksText.text.toString().trim()

        if (location.isEmpty()) {
            return false
        } else if (city.isEmpty()) {
            return false
        } else if (state.isEmpty()) {
            return false
        } else if (pin.isEmpty() || pin.length < 6) {
            return false
        } else if (landmarks.isEmpty()) {
            return false
        }
        return true
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askPermissions(PermissonCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        }
    }

    override fun onOrganisedItemSelect(position: Int, item: String) {
        activityApnaNewSurveyBinding.organisedSelect.setText(item)
        organisedDialog.dismiss()
    }

    override fun onUnorganisedItemSelect(position: Int, item: String) {
        activityApnaNewSurveyBinding.unorganisedSelect.setText(item)
        unorganisedDialog.dismiss()
    }

    @SuppressLint("SetTextI18n")
    override fun onClickDeleteChemist(position: Int) {
        chemistList.removeAt(position)

        if (chemistList.size > 0) {
            activityApnaNewSurveyBinding.chemistTotalLayout.visibility = View.VISIBLE
            val totalOrganisedAvgSale =
                chemistList.stream().map { it.organisedAvgSale }.mapToDouble { it.toDouble() }.sum()
            activityApnaNewSurveyBinding.totalOrganisedText.text =
                String.format("%.3f", totalOrganisedAvgSale) + "Lac"

            val totalUnorganisedAvgSale =
                chemistList.stream().map { it.unorganisedAvgSale }.mapToDouble { it.toDouble() }
                    .sum()
            activityApnaNewSurveyBinding.totalUnorganisedText.text =
                String.format("%.3f", totalUnorganisedAvgSale) + "Lac"
        } else {
            activityApnaNewSurveyBinding.chemistTotalLayout.visibility = View.GONE
        }

        chemistAdapter.notifyDataSetChanged()
    }

    override fun onClickApartmentDelete(position: Int) {
        apartmentsList.removeAt(position)
        apartmentTypeItemAdapter.notifyDataSetChanged()
    }

    override fun onClickDeleteHospital(position: Int) {
        hospitalsList.removeAt(position)
        hospitalsAdapter.notifyDataSetChanged()
    }

    override fun onApartmentTypeItemSelect(position: Int, item: String) {
        activityApnaNewSurveyBinding.apartmentTypeSelect.setText(item)
        apartmentTypeDialog.dismiss()
    }

    override fun onTrafficStreetItemSelect(position: Int, item: String) {
        activityApnaNewSurveyBinding.trafficStreetSelect.setText(item)
        trafficStreetDialog.dismiss()
    }

    override fun onApnaSpecialityItemSelect(position: Int, item: String) {
        activityApnaNewSurveyBinding.hospitalSpecialitySelect.setText(item)
        apnaSpecialityDialog.dismiss()
    }

    override fun onClickTrafficGeneratorItemDelete(position: Int) {
        selectedTrafficGeneratorItem.removeAt(position)
        trafficGeneratorsItemAdapter.notifyDataSetChanged()
    }

    override fun onTrafficGeneratorItemSelect(position: Int, item: String) {
        trafficGeneratorDialog.dismiss()
        selectedTrafficGeneratorItem.add(item)
        trafficGeneratorsItemAdapter =
            TrafficGeneratorsItemAdapter(this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                selectedTrafficGeneratorItem)
        activityApnaNewSurveyBinding.trafficGeneratorsRcv.adapter = trafficGeneratorsItemAdapter
        activityApnaNewSurveyBinding.trafficGeneratorsRcv.layoutManager =
            LinearLayoutManager(this@ApnaNewSurveyActivity, LinearLayoutManager.HORIZONTAL, false)
        trafficGeneratorsItemAdapter.notifyDataSetChanged()
    }

    override fun deleteSiteImage(position: Int, file: File) {
        imageList.removeAt(position)
        imageAdapter.notifyDataSetChanged()
    }

    override fun previewImage(imageFile: File) {
        val dialog =
            Dialog(this@ApnaNewSurveyActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val previewImageDialogBinding: PreviewImageDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this@ApnaNewSurveyActivity),
            R.layout.preview_image_dialog,
            null,
            false
        )
        dialog.setContentView(previewImageDialogBinding.root)
        Glide
            .with(this@ApnaNewSurveyActivity)
            .load(imageFile)
            .placeholder(R.drawable.placeholder_image)
            .into(previewImageDialogBinding.previewImage)
        previewImageDialogBinding.close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onSuccessGetLocationListApiCall(locationListResponse: LocationListResponse) {
        Utlis.hideLoading()
        if (locationListResponse != null && locationListResponse.data != null && locationListResponse.data!!.listData!!.rows!!.size > 0) {
            for (i in locationListResponse.data!!.listData!!.rows!!.indices) {
                locationList =
                    locationListResponse.data!!.listData!!.rows as ArrayList<LocationListResponse.Data.ListData.Row>
                activityApnaNewSurveyBinding.cityText.setText(locationList[0].name)
                activityApnaNewSurveyBinding.stateText.setText(locationList[0].city!!.code)
            }
        }
    }

    override fun onFailureGetLocationListApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetTrafficStreetTypeApiCall(trafficStreetTypeResponse: TrafficStreetTypeResponse) {
        Utlis.hideLoading()
        if (trafficStreetTypeResponse != null && trafficStreetTypeResponse.data != null && trafficStreetTypeResponse.data!!.listData!!.rows!!.size > 0) {
            trafficStreetData =
                trafficStreetTypeResponse.data!!.listData!!.rows as ArrayList<TrafficStreetTypeResponse.Data.ListData.Row>
        }
    }

    override fun onFailureGetTrafficStreetTypeApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetTrafficGeneratorsTypeApiCall(trafficGeneratorsResponse: TrafficGeneratorsResponse) {
        Utlis.hideLoading()
        if (trafficGeneratorsResponse != null && trafficGeneratorsResponse.data != null && trafficGeneratorsResponse.data!!.listData!!.rows!!.size > 0) {
            trafficGeneratorData =
                trafficGeneratorsResponse.data!!.listData!!.rows as ArrayList<TrafficGeneratorsResponse.Data.ListData.Row>
        }
    }

    override fun onFailureGetTrafficGeneratorsTypeApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetApartmentTypeApiCall(apartmentTypeResponse: ApartmentTypeResponse) {
        Utlis.hideLoading()
        if (apartmentTypeResponse != null && apartmentTypeResponse.data != null && apartmentTypeResponse.data!!.listData!!.rows!!.size > 0) {
            apartmentTypeData =
                apartmentTypeResponse.data!!.listData!!.rows as ArrayList<ApartmentTypeResponse.Data.ListData.Row>
        }
    }

    override fun onFailureGetApartmentTypeApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetApnaSpecialityApiCall(apnaSpecialityResponse: ApnaSpecialityResponse) {
        Utlis.hideLoading()
        if (apnaSpecialityResponse != null && apnaSpecialityResponse.data != null && apnaSpecialityResponse.data!!.listData!!.rows!!.size > 0) {
            apnaSpecialityData =
                apnaSpecialityResponse.data!!.listData!!.rows as ArrayList<ApnaSpecialityResponse.Data.ListData.Row>
        }
    }

    override fun onFailureGetApnaSpecialityApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetParkingTypeApiCall(parkingTypeResponse: ParkingTypeResponse) {
        Utlis.hideLoading()
        if (parkingTypeResponse != null && parkingTypeResponse.data!!.listData != null && parkingTypeResponse.data!!.listData!!.rows!!.size > 0) {
            parkingTypeList =
                parkingTypeResponse.data!!.listData!!.rows as ArrayList<ParkingTypeResponse.Data.ListData.Row>
            activityApnaNewSurveyBinding.parkingRadioGroup.check(activityApnaNewSurveyBinding.yesRadioButton.id)
        }
    }

    override fun onFailureGetParkingTypeApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetDimensionTypeApiCall(dimensionTypeResponse: DimensionTypeResponse) {
        Utlis.hideLoading()
        if (dimensionTypeResponse != null && dimensionTypeResponse.data!!.listData != null && dimensionTypeResponse.data!!.listData!!.rows!!.size > 0) {
            dimensionTypeList =
                dimensionTypeResponse.data!!.listData!!.rows as ArrayList<DimensionTypeResponse.Data.ListData.Row>
            activityApnaNewSurveyBinding.expectedRentRadioGroup.check(activityApnaNewSurveyBinding.perSqFtRadioButton.id)
        }
    }

    override fun onFailureGetDimensionTypeApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetNeighbouringLocationApiCall(neighbouringLocationResponse: NeighbouringLocationResponse) {
        Utlis.hideLoading()
        if (neighbouringLocationResponse != null && neighbouringLocationResponse.data!!.listData != null && neighbouringLocationResponse.data!!.listData!!.rows!!.size > 0) {
            neighbouringLocationList =
                neighbouringLocationResponse.data!!.listData!!.rows as ArrayList<NeighbouringLocationResponse.Data.ListData.Row>

            val neighbouringStoreAdapter = NeighbouringStoreAdapter(
                this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                neighbouringLocationList
            )
            activityApnaNewSurveyBinding.neighbouringStoreRcv.adapter = neighbouringStoreAdapter
            activityApnaNewSurveyBinding.neighbouringStoreRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)
        }
    }

    override fun onFailureGetNeighbouringLocationApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }
}