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

class ApnaNewSurveyActivity : AppCompatActivity(), ApnaNewSurveyCallBack {

    lateinit var trafficStreetDialog: Dialog
    lateinit var trafficGeneratorDialog: Dialog
    lateinit var apartmentTypeDialog: Dialog
    lateinit var apnaSpecialityDialog: Dialog

    var selectedTrafficGeneratorItem = ArrayList<String>()

    var hospitalsList = ArrayList<HospitalData>()
    var apartmentsList = ArrayList<ApartmentData>()
    var locationList = ArrayList<LocationListResponse.Data.ListData.Row>()
    var trafficStreetData = ArrayList<TrafficStreetTypeResponse.Data.ListData.Row>()
    var trafficGeneratorData = ArrayList<TrafficGeneratorsResponse.Data.ListData.Row>()
    var apartmentTypeData = ArrayList<ApartmentTypeResponse.Data.ListData.Row>()
    var apnaSpecialityData = ArrayList<ApnaSpecialityResponse.Data.ListData.Row>()
    var parkingTypeList = ArrayList<ParkingTypeResponse.Data.ListData.Row>()

    private lateinit var activityApnaNewSurveyBinding: ActivityApnaNewSurveyBinding
    private lateinit var apnaNewSurveyViewModel: ApnaNewSurveyViewModel
    var currentPosition: Int = 0
    var imageList = ArrayList<Image>()

    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    var videoFile: File? = null

    lateinit var imageAdapter: ImageAdapter
    lateinit var trafficGeneratorsItemAdapter: TrafficGeneratorsItemAdapter
    lateinit var trafficStreetAdapter: TrafficStreetAdapter
    lateinit var trafficGeneratorAdapter: TrafficGeneratorAdapter
    lateinit var apartmentTypeAdapter: ApartmentTypeAdapter
    lateinit var apartmentTypeItemAdapter: ApartmentTypeItemAdapter
    lateinit var apnaSpecialityAdapter: ApnaSpecialityAdapter
    lateinit var hospitalsAdapter: HospitalsAdapter

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
        activityApnaNewSurveyBinding.addSitePhoto.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(100)
            } else {
                openCamera()
            }
        }

        imageAdapter = ImageAdapter(context, imageList, this)
        activityApnaNewSurveyBinding.sitePhotosRecyclerView.adapter = imageAdapter
        activityApnaNewSurveyBinding.sitePhotosRecyclerView.layoutManager =
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

        // Morning time dropdown
        activityApnaNewSurveyBinding.morningTimeSelect.setOnClickListener {
            val timePickerDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogTimePickerBinding = DataBindingUtil.inflate<DialogTimePickerBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_time_picker,
                null,
                false
            )
            timePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePickerDialog.setContentView(dialogTimePickerBinding.root)
            var timePicker = dialogTimePickerBinding.timePicker
            timePicker.setIs24HourView(false)
            dialogTimePickerBinding.ok.setOnClickListener {
                var hour = timePicker.hour
                var minute = timePicker.minute
                activityApnaNewSurveyBinding.morningTimeSelect.setText(hour.toString() + ":" + minute.toString())
                timePickerDialog.dismiss()
            }
            dialogTimePickerBinding.cancel.setOnClickListener {
                timePickerDialog.dismiss()
            }
            timePickerDialog.show()
        }

        // Evening time dropdown
        activityApnaNewSurveyBinding.eveningTimeSelect.setOnClickListener {

        }

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
            }
            1 -> {
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
            }
            2 -> {
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
            }
            3 -> {
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
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
            }
            else -> {
//                Toast.makeText(context, "No item available", Toast.LENGTH_SHORT).show()
            }
        }
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
            parkingTypeList = parkingTypeResponse.data!!.listData!!.rows as ArrayList<ParkingTypeResponse.Data.ListData.Row>
            activityApnaNewSurveyBinding.parkingRadioGroup.check(activityApnaNewSurveyBinding.yesRadioButton.id)
        }
    }

    override fun onFailureGetParkingTypeApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }
}