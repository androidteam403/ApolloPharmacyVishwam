package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.*
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class ApnaNewSurveyActivity : AppCompatActivity(), ApnaNewSurveyCallBack,
    GoogleMap.OnMarkerDragListener {

    var selectedMarker: Marker? = null
    var errorMessage: String = "Please fill all mandatory fields"
    var length = 0.0
    var width = 0.0

    var surveyCreateRequest = SurveyCreateRequest()

    var location_uid: String = ""
    var state_uid: String = ""
    var city_uid: String = ""

    var imageUrls = ArrayList<ImageDto>()

    var isLocationDetailsCompleted: Boolean = false
    var isSiteSpecificationsCompleted: Boolean = false
    var isMarketInformationCompleted: Boolean = false
    var isCompetitorsDetailsCompleted: Boolean = false
    var isPopulationAndHousesCompleted: Boolean = false
    var isHospitalsCompleted: Boolean = false
    var isPhotosAndMediaCompleted: Boolean = false

    var isLocationDetailsInProgress: Boolean = true
    var isSiteSpecificationsInProgress: Boolean = false
    var isMarketInformationInProgress: Boolean = false
    var isCompetitorsDetailsInProgress: Boolean = false
    var isPopulationAndHousesInProgress: Boolean = false
    var isHospitalsInProgress: Boolean = false
    var isPhotosAndMediaInProgress: Boolean = false

    lateinit var cityListDialog: Dialog
    lateinit var stateListDialog: Dialog
    lateinit var locationListDialog: Dialog
    lateinit var trafficStreetDialog: Dialog
    lateinit var trafficGeneratorDialog: Dialog
    lateinit var apartmentTypeDialog: Dialog
    lateinit var apnaSpecialityDialog: Dialog
    lateinit var organisedDialog: Dialog
    lateinit var unorganisedDialog: Dialog
    lateinit var dimensionTypeDialog: Dialog
    lateinit var neighbouringLocationDialog: Dialog

    lateinit var morningFromTimePickerDialog: TimePickerDialog
    lateinit var morningToTimePickerDialog: TimePickerDialog
    lateinit var eveningFromTimePickerDialog: TimePickerDialog
    lateinit var eveningToTimePickerDialog: TimePickerDialog


    var cityList = ArrayList<CityListResponse.Data.ListData.Row>()
    var stateList = ArrayList<StateListResponse.Data.ListData.Row>()
    var chemistList = ArrayList<ChemistData>()
    var hospitalsList = ArrayList<HospitalData>()
    var apartmentsList = ArrayList<ApartmentData>()
    var neighbouringStoreList = ArrayList<NeighbouringStoreData>()
    var locationList = ArrayList<LocationListResponse.Data.ListData.Row>()
    var trafficStreetData = ArrayList<TrafficStreetTypeResponse.Data.ListData.Row>()
    var trafficGeneratorData = ArrayList<TrafficGeneratorsResponse.Data.ListData.Row>()
    var apartmentTypeData = ArrayList<ApartmentTypeResponse.Data.ListData.Row>()
    var apnaSpecialityData = ArrayList<ApnaSpecialityResponse.Data.ListData.Row>()
    var parkingTypeList = ArrayList<ParkingTypeResponse.Data.ListData.Row>()
    var dimensionTypeList = ArrayList<DimensionTypeResponse.Data.ListData.Row>()
    var neighbouringLocationList = ArrayList<NeighbouringLocationResponse.Data.ListData.Row>()
    var selectedTrafficGeneratorItem = ArrayList<String>()
//    var neighbouringStoreList = ArrayList<NeighbouringLocationResponse.Data.ListData.Row>()

    private lateinit var activityApnaNewSurveyBinding: ActivityApnaNewSurveyBinding
    private lateinit var apnaNewSurveyViewModel: ApnaNewSurveyViewModel
    var currentPosition: Int = 0
    var imageFileList = ArrayList<File>()

    var imageFile: File? = null
    var videoFile: File? = null
    private var compressedImageFileName: String? = null

    lateinit var cityItemAdapter: CityItemAdapter
    lateinit var stateItemAdapter: StateItemAdapter
    lateinit var locationListItemAdapter: LocationListItemAdapter
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
    lateinit var dimensionTypeAdapter: DimensionTypeAdapter
    lateinit var neighbouringLocationAdapter: NeighbouringLocationAdapter
    lateinit var neighbouringStoreAdapter: NeighbouringStoreAdapter

    val REQUEST_CODE_CAMERA = 2235211
    val REQUEST_CODE_VIDEO = 2156

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var client: FusedLocationProviderClient
    var map: GoogleMap? = null
    public fun getStartIntent(context: Context): Intent {
        val intent = Intent(context, ApnaNewSurveyActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        return intent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityApnaNewSurveyBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_apna_new_survey)

        apnaNewSurveyViewModel = ViewModelProvider(this)[ApnaNewSurveyViewModel::class.java]

        activityApnaNewSurveyBinding.scrollView.post(Runnable {
            activityApnaNewSurveyBinding.scrollView.scrollTo(0, 0)
        })

//        activityApnaNewSurveyBinding.scrollView.fullScroll(View.FOCUS_UP)

        setUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun setUp() {
        Utlis.showLoading(this@ApnaNewSurveyActivity)

        // Location list api call
        apnaNewSurveyViewModel.getLocationList(this@ApnaNewSurveyActivity)

        // State list api call
        apnaNewSurveyViewModel.getStateList(this@ApnaNewSurveyActivity)


        activityApnaNewSurveyBinding.backButton.setOnClickListener {
            val dialog = Dialog(this@ApnaNewSurveyActivity)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val apnaSurveyAlertDialogBinding =
                DataBindingUtil.inflate<ApnaSurveyAlertDialogBinding>(
                    LayoutInflater.from(this@ApnaNewSurveyActivity),
                    R.layout.apna_survey_alert_dialog,
                    null,
                    false
                )
            dialog.setContentView(apnaSurveyAlertDialogBinding.root)
            apnaSurveyAlertDialogBinding.yesButton.setOnClickListener {
                dialog.dismiss()
                finish()
            }
            apnaSurveyAlertDialogBinding.noButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.show()
        }
        supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(this@ApnaNewSurveyActivity)

        Dexter.withActivity(this@ApnaNewSurveyActivity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    getCurrentLocation()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?,
                ) {
                    token!!.continuePermissionRequest()
                }

            }).check()


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
            val dialogBinding: DialogQuickGoBinding? = DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialog_quick_go, null, false
            )
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
                    if (validateLocationDetails()) {
                        showNext(1)
                        customDialog.dismiss()
                    } else {
                        Toast.makeText(
                            this@ApnaNewSurveyActivity,
                            "Please fill location details first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                dialogBinding.marketInformation.setOnClickListener {
                    if (validateLocationDetails() && validateSiteSpecification()) {
                        showNext(2)
                        customDialog.dismiss()
                    } else {
                        Toast.makeText(
                            this@ApnaNewSurveyActivity,
                            "Please fill site specifications first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialogBinding.competitorsDetails.setOnClickListener {
                    if (validateLocationDetails() && validateSiteSpecification()) {
                        showNext(3)
                        customDialog.dismiss()
                    } else {
                        Toast.makeText(
                            this@ApnaNewSurveyActivity,
                            "Please fill market information first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialogBinding.populationAndHouses.setOnClickListener {
                    if (validateLocationDetails() && validateSiteSpecification()) {
                        showNext(4)
                        customDialog.dismiss()
                    } else {
                        Toast.makeText(
                            this@ApnaNewSurveyActivity,
                            "Please fill competitors details first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialogBinding.hospitals.setOnClickListener {
                    if (validateLocationDetails() && validateSiteSpecification()) {
                        showNext(5)
                        customDialog.dismiss()
                    } else {
                        Toast.makeText(
                            this@ApnaNewSurveyActivity,
                            "Please fill population and houses first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialogBinding.photosAndMedia.setOnClickListener {
                    if (validateLocationDetails() && validateSiteSpecification()) {
                        showNext(6)
                        customDialog.dismiss()
                    } else {
                        Toast.makeText(
                            this@ApnaNewSurveyActivity,
                            "Please fill hospitals details first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                // Location Details
                val location = activityApnaNewSurveyBinding.locationText.text.toString().trim()
                val city = activityApnaNewSurveyBinding.cityText.text.toString().trim()
                val state = activityApnaNewSurveyBinding.stateText.text.toString().trim()
                val pin = activityApnaNewSurveyBinding.pinText.text.toString().trim()
                val landMarks =
                    activityApnaNewSurveyBinding.nearByLandmarksText.text.toString().trim()
//                location.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && pin.isNotEmpty() && landMarks.isNotEmpty()
                if (isLocationDetailsCompleted) {
                    dialogBinding.locationDetailsCompleted.visibility = View.VISIBLE
                    dialogBinding.locationDetailsCount.visibility = View.GONE
                    dialogBinding.locationDetailsProgress.visibility = View.GONE
                    dialogBinding.locationDetails.setTextColor(Color.parseColor("#00a651"))
                    dialogBinding.locationDetailsView.visibility = View.GONE
                    dialogBinding.locationDetailsViewCompleted.visibility = View.VISIBLE
                } else if (isLocationDetailsInProgress) { // location.isNotEmpty() || city.isNotEmpty() || state.isNotEmpty() || pin.isNotEmpty() || landMarks.isNotEmpty()
                    dialogBinding.locationDetailsCount.visibility = View.GONE
                    dialogBinding.locationDetailsCompleted.visibility = View.GONE
                    dialogBinding.locationDetailsProgress.visibility = View.VISIBLE
                    dialogBinding.locationDetails.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.apna_project_actionbar_color
                        )
                    )
                } else {
                    dialogBinding.locationDetailsCount.visibility = View.VISIBLE
                    dialogBinding.locationDetailsCompleted.visibility = View.GONE
                    dialogBinding.locationDetailsProgress.visibility = View.GONE
                    dialogBinding.locationDetails.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

                // Site Specifications
                val length = activityApnaNewSurveyBinding.lengthText.text.toString().trim()
                val width = activityApnaNewSurveyBinding.widthText.text.toString().trim()
                val ceilingHeight =
                    activityApnaNewSurveyBinding.ceilingHeightText.text.toString().trim()
                val expectedRent =
                    activityApnaNewSurveyBinding.expectedRentText.text.toString().trim()
                val securityDeposit =
                    activityApnaNewSurveyBinding.securityDepositText.text.toString().trim()
//                length.isNotEmpty() && width.isNotEmpty() && ceilingHeight.isNotEmpty() && expectedRent.isNotEmpty() && securityDeposit.isNotEmpty()
                if (isSiteSpecificationsCompleted) {
                    dialogBinding.siteSpecificationsCount.visibility = View.GONE
                    dialogBinding.siteSpecifiactionProgress.visibility = View.GONE
                    dialogBinding.siteSpecificationCompleted.visibility = View.VISIBLE
                    dialogBinding.siteSpecification.setTextColor(Color.parseColor("#00a651"))
                    dialogBinding.siteSpecificationsView.visibility = View.GONE
                    dialogBinding.siteSpecificationsViewCompleted.visibility = View.VISIBLE
                } else if (isSiteSpecificationsInProgress) { // length.isNotEmpty() || width.isNotEmpty() || ceilingHeight.isNotEmpty() || expectedRent.isNotEmpty() || securityDeposit.isNotEmpty()
                    dialogBinding.siteSpecificationsCount.visibility = View.GONE
                    dialogBinding.siteSpecificationCompleted.visibility = View.GONE
                    dialogBinding.siteSpecifiactionProgress.visibility = View.VISIBLE
                    dialogBinding.siteSpecification.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.apna_project_actionbar_color
                        )
                    )
                } else {
                    dialogBinding.siteSpecificationsCount.visibility = View.VISIBLE
                    dialogBinding.siteSpecificationCompleted.visibility = View.GONE
                    dialogBinding.siteSpecifiactionProgress.visibility = View.GONE
                    dialogBinding.siteSpecification.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

                // market information
                val existingOutletName =
                    activityApnaNewSurveyBinding.existingOutletName.text.toString().trim()
//                val existingOutletAgeRadio =
//                    activityApnaNewSurveyBinding.ageOrSaleRadioGroup.checkedRadioButtonId
                val ageOrSale = activityApnaNewSurveyBinding.ageOrSaleText.text.toString().trim()
                val pharma = activityApnaNewSurveyBinding.pharmaText.text.toString().trim()
                val fmcg = activityApnaNewSurveyBinding.fmcgText.text.toString().trim()
                val surgicals = activityApnaNewSurveyBinding.surgicalsText.text.toString().trim()
                val areaDiscount =
                    activityApnaNewSurveyBinding.areaDiscountText.text.toString().trim()
                val comments =
                    activityApnaNewSurveyBinding.distributorsComments.text.toString().trim()
                val occupation = activityApnaNewSurveyBinding.occupationText.text.toString().trim()
                val serviceClass =
                    activityApnaNewSurveyBinding.serviceClassText.text.toString().trim()
                val businessClass =
                    activityApnaNewSurveyBinding.businessClassText.text.toString().trim()
//                existingOutletName.isNotEmpty() && existingOutletAgeRadio != -1 && ageOrSale.isNotEmpty() && pharma.isNotEmpty() && fmcg.isNotEmpty() && surgicals.isNotEmpty() && areaDiscount.isNotEmpty() && neighbouringStoreList.size > 0 && comments.isNotEmpty() && occupation.isNotEmpty() && serviceClass.isNotEmpty() && businessClass.isNotEmpty() && selectedTrafficGeneratorItem.size > 0
                if (isMarketInformationCompleted) {
                    dialogBinding.marketInformationCount.visibility = View.GONE
                    dialogBinding.marketInformationProgress.visibility = View.GONE
                    dialogBinding.marketInformationCompleted.visibility = View.VISIBLE
                    dialogBinding.marketInformation.setTextColor(Color.parseColor("#00a651"))
                    dialogBinding.marketInformationView.visibility = View.GONE
                    dialogBinding.marketInformationViewCompleted.visibility = View.VISIBLE
                } else if (isMarketInformationInProgress) { // existingOutletName.isNotEmpty() || existingOutletAgeRadio != -1 || ageOrSale.isNotEmpty() || pharma.isNotEmpty() || fmcg.isNotEmpty() || surgicals.isNotEmpty() || areaDiscount.isNotEmpty() || neighbouringStoreList.size > 0 || comments.isNotEmpty() || occupation.isNotEmpty() || serviceClass.isNotEmpty() || businessClass.isNotEmpty() || selectedTrafficGeneratorItem.size > 0
                    dialogBinding.marketInformationCount.visibility = View.GONE
                    dialogBinding.marketInformationCompleted.visibility = View.GONE
                    dialogBinding.marketInformationProgress.visibility = View.VISIBLE
                    dialogBinding.marketInformation.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.apna_project_actionbar_color
                        )
                    )
                } else {
                    dialogBinding.marketInformationCount.visibility = View.VISIBLE
                    dialogBinding.marketInformationCompleted.visibility = View.GONE
                    dialogBinding.marketInformationProgress.visibility = View.GONE
                    dialogBinding.marketInformation.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

                // competitors details
                val chemist = activityApnaNewSurveyBinding.chemistText.text.toString().trim()
                val organised = activityApnaNewSurveyBinding.organisedSelect.text.toString().trim()
                val organisedAvgSale =
                    activityApnaNewSurveyBinding.organisedAvgSaleText.text.toString().trim()
                val unorganised =
                    activityApnaNewSurveyBinding.unorganisedSelect.text.toString().trim()
                val unorganisedAvgSale =
                    activityApnaNewSurveyBinding.unorganisedAvgSaleText.text.toString().trim()
//                chemistList.size > 0
                if (isCompetitorsDetailsCompleted) {
                    dialogBinding.competitorsDetailsCount.visibility = View.GONE
                    dialogBinding.competitorsDetailsProgress.visibility = View.GONE
                    dialogBinding.competitorsDetailsCompleted.visibility = View.VISIBLE
                    dialogBinding.competitorsDetails.setTextColor(Color.parseColor("#00a651"))
                    dialogBinding.competitorsDetailsView.visibility = View.GONE
                    dialogBinding.competitorsDetailsViewCompleted.visibility = View.VISIBLE
                } else if (isCompetitorsDetailsInProgress) { // chemist.isNotEmpty() || organised.isNotEmpty() || organisedAvgSale.isNotEmpty() || unorganised.isNotEmpty() || unorganisedAvgSale.isNotEmpty()
                    dialogBinding.competitorsDetailsCount.visibility = View.GONE
                    dialogBinding.competitorsDetailsCompleted.visibility = View.GONE
                    dialogBinding.competitorsDetailsProgress.visibility = View.VISIBLE
                    dialogBinding.competitorsDetails.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.apna_project_actionbar_color
                        )
                    )
                } else {
                    dialogBinding.competitorsDetailsCount.visibility = View.VISIBLE
                    dialogBinding.competitorsDetailsCompleted.visibility = View.GONE
                    dialogBinding.competitorsDetailsProgress.visibility = View.GONE
                    dialogBinding.competitorsDetails.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

                // population and houses
                val apartmentType =
                    activityApnaNewSurveyBinding.apartmentTypeSelect.text.toString().trim()
                val noOfHouses = activityApnaNewSurveyBinding.noOfHousesText.text.toString().trim()
                val distance = activityApnaNewSurveyBinding.distanceText.text.toString().trim()
//                apartmentsList.size > 0
                if (isPopulationAndHousesCompleted) {
                    dialogBinding.populationAndHousesCount.visibility = View.GONE
                    dialogBinding.populationAndHousesProgress.visibility = View.GONE
                    dialogBinding.populationAndHousesCompleted.visibility = View.VISIBLE
                    dialogBinding.populationAndHouses.setTextColor(Color.parseColor("#00a651"))
                    dialogBinding.populationAndHousesView.visibility = View.GONE
                    dialogBinding.populationAndHousesViewCompleted.visibility = View.VISIBLE
                } else if (isPopulationAndHousesInProgress) { // apartmentType.isNotEmpty() || noOfHouses.isNotEmpty() || distance.isNotEmpty()
                    dialogBinding.populationAndHousesCount.visibility = View.GONE
                    dialogBinding.populationAndHousesCompleted.visibility = View.GONE
                    dialogBinding.populationAndHousesProgress.visibility = View.VISIBLE
                    dialogBinding.populationAndHouses.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.apna_project_actionbar_color
                        )
                    )
                } else {
                    dialogBinding.populationAndHousesCount.visibility = View.VISIBLE
                    dialogBinding.populationAndHousesCompleted.visibility = View.GONE
                    dialogBinding.populationAndHousesProgress.visibility = View.GONE
                    dialogBinding.populationAndHouses.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

                // hospitals
                val speciality =
                    activityApnaNewSurveyBinding.hospitalSpecialitySelect.text.toString().trim()
                val beds = activityApnaNewSurveyBinding.bedsText.text.toString().trim()
                val name = activityApnaNewSurveyBinding.hospitalNameText.text.toString().trim()
                val occupancy = activityApnaNewSurveyBinding.occupancyText.text.toString().trim()
                val noOfOpd = activityApnaNewSurveyBinding.noOfOpdText.text.toString().trim()
//                hospitalsList.size > 0
                if (isHospitalsCompleted) {
                    dialogBinding.hospitalsCount.visibility = View.GONE
                    dialogBinding.hospitalsProgress.visibility = View.GONE
                    dialogBinding.hospitalsCompleted.visibility = View.VISIBLE
                    dialogBinding.hospitals.setTextColor(Color.parseColor("#00a651"))
                    dialogBinding.hospitalsView.visibility = View.GONE
                    dialogBinding.hospitalsViewCompleted.visibility = View.VISIBLE
                } else if (isHospitalsInProgress) { // speciality.isNotEmpty() || beds.isNotEmpty() || name.isNotEmpty() || occupancy.isNotEmpty() || noOfOpd.isNotEmpty()
                    dialogBinding.hospitalsCount.visibility = View.GONE
                    dialogBinding.hospitalsCompleted.visibility = View.GONE
                    dialogBinding.hospitalsProgress.visibility = View.VISIBLE
                    dialogBinding.hospitals.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.apna_project_actionbar_color
                        )
                    )
                } else {
                    dialogBinding.hospitalsCount.visibility = View.VISIBLE
                    dialogBinding.hospitalsCompleted.visibility = View.GONE
                    dialogBinding.hospitalsProgress.visibility = View.GONE
                    dialogBinding.hospitals.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

                // photos and media
                if (isPhotosAndMediaCompleted) { // imageList.size == 5 && videoFile != null
                    dialogBinding.photosAndMediaCount.visibility = View.GONE
                    dialogBinding.photosAndMediaProgress.visibility = View.GONE
                    dialogBinding.photosAndMediaCompleted.visibility = View.VISIBLE
                    dialogBinding.photosAndMedia.setTextColor(Color.parseColor("#00a651"))
                } else if (isPhotosAndMediaInProgress) { // (imageList.size in 1..4) || videoFile != null
                    dialogBinding.photosAndMediaCount.visibility = View.GONE
                    dialogBinding.photosAndMediaCompleted.visibility = View.GONE
                    dialogBinding.photosAndMediaProgress.visibility = View.VISIBLE
                    dialogBinding.photosAndMedia.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.apna_project_actionbar_color
                        )
                    )
                } else {
                    dialogBinding.photosAndMediaCount.visibility = View.VISIBLE
                    dialogBinding.photosAndMediaCompleted.visibility = View.GONE
                    dialogBinding.photosAndMediaProgress.visibility = View.GONE
                    dialogBinding.photosAndMedia.setTextColor(Color.parseColor("#FFA9A9A9"))
                }

            }.show()
        }

        // preview
        activityApnaNewSurveyBinding.previewIcon.setOnClickListener {

            val siteImageMb = SurveyCreateRequest.SiteImageMb()
            val siteImages = ArrayList<SurveyCreateRequest.SiteImageMb.Image>()
            for (i in imageFileList.indices) {
                val image = SurveyCreateRequest.SiteImageMb.Image()
                image.url = imageFileList[i].absolutePath
                siteImages.add(image)
            }
            siteImageMb.images = siteImages
            surveyCreateRequest.siteImageMb = siteImageMb

            if (videoFile != null) {
                val videoMb = SurveyCreateRequest.VideoMb()
                val videos = ArrayList<SurveyCreateRequest.VideoMb.Video>()
                val video = SurveyCreateRequest.VideoMb.Video()
                video.url = videoFile!!.absolutePath
                videos.add(video)
                videoMb.video = videos
                surveyCreateRequest.videoMb = videoMb
            }

            val intent = Intent(this@ApnaNewSurveyActivity, ApnaSurveyPreviewActivity::class.java)
            intent.putExtra("SURVEY_REQUEST", surveyCreateRequest)
            startActivity(intent)
        }


        showNext(currentPosition)

        activityApnaNewSurveyBinding.next.setOnClickListener {
            if (validateLocationDetails()) {
                currentPosition++
                activityApnaNewSurveyBinding.next.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                showNext(currentPosition)
            } else {
                Toast.makeText(
                    this@ApnaNewSurveyActivity,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        activityApnaNewSurveyBinding.previous.setOnClickListener {
            currentPosition--
            activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE
            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.next.visibility = View.GONE
            showNext(currentPosition)
        }

        activityApnaNewSurveyBinding.nextBtn.setOnClickListener {
            if (currentPosition == 6) {
                showNext(currentPosition)
            } else if (currentPosition == 1 && validateSiteSpecification()) {
                currentPosition++
                showNext(currentPosition)
            } else {
                if (currentPosition == 1 && !validateSiteSpecification()) {
                    Toast.makeText(
                        this@ApnaNewSurveyActivity,
                        "Please fill all mandatory fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    currentPosition++
                    showNext(currentPosition)
                }
            }
        }

        activityApnaNewSurveyBinding.previousBtn.setOnClickListener {
            currentPosition--
            if (currentPosition == 0) {
                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE
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
                if (imageFileList.size == 5) {
                    Toast.makeText(
                        this@ApnaNewSurveyActivity,
                        "You are allowed to upload only five images",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    openCamera()
                }
            }
        }

        imageAdapter = ImageAdapter(this@ApnaNewSurveyActivity, imageFileList, this)
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
            val dialog = Dialog(this@ApnaNewSurveyActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val videoDeleteConfirmDialogBinding =
                DataBindingUtil.inflate<VideoDeleteConfirmDialogBinding>(
                    LayoutInflater.from(this@ApnaNewSurveyActivity),
                    R.layout.video_delete_confirm_dialog,
                    null,
                    false
                )
            dialog.setContentView(videoDeleteConfirmDialogBinding.root)

            videoDeleteConfirmDialogBinding.noButton.setOnClickListener {
                dialog.dismiss()
            }
            videoDeleteConfirmDialogBinding.yesButton.setOnClickListener {
                videoFile = null
                activityApnaNewSurveyBinding.beforeCaptureLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.afterCaptureLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.playVideo.visibility = View.GONE
                activityApnaNewSurveyBinding.deleteVideo.visibility = View.GONE
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.show()
        }

        // Video preview
        activityApnaNewSurveyBinding.playVideo.setOnClickListener {
            val intent = Intent(this@ApnaNewSurveyActivity, VideoPreviewActivity::class.java)
            intent.putExtra("VIDEO_URI", videoFile!!.absolutePath)
            startActivity(intent)
        }

        // Location Dropdown
        activityApnaNewSurveyBinding.locationText.setOnClickListener {
            locationListDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogLocationListBinding = DataBindingUtil.inflate<DialogLocationListBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_location_list,
                null,
                false
            )
            locationListDialog.setContentView(dialogLocationListBinding.root)
            locationListDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            locationListDialog.setCancelable(false)
            dialogLocationListBinding.closeDialog.setOnClickListener {
                locationListDialog.dismiss()
            }
            locationListItemAdapter = LocationListItemAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, locationList
            )
            dialogLocationListBinding.locationRcv.adapter = locationListItemAdapter
            dialogLocationListBinding.locationRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogLocationListBinding.searchLocationListText.addTextChangedListener(object :
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
                    filterLocationList(s.toString(), dialogLocationListBinding)
                }

            })
            locationListDialog.show()
        }

        // State Dropdown
        activityApnaNewSurveyBinding.stateText.setOnClickListener {
            stateListDialog = Dialog(this@ApnaNewSurveyActivity)
            val dialogStateListBinding = DataBindingUtil.inflate<DialogStateListBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_state_list,
                null,
                false
            )
            stateListDialog.setContentView(dialogStateListBinding.root)
            stateListDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            stateListDialog.setCancelable(false)
            dialogStateListBinding.closeDialog.setOnClickListener {
                stateListDialog.dismiss()
            }
            stateItemAdapter =
                StateItemAdapter(this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, stateList)
            dialogStateListBinding.stateRcv.adapter = stateItemAdapter
            dialogStateListBinding.stateRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)
            dialogStateListBinding.searchStateText.addTextChangedListener(object : TextWatcher {
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
                    filterStateList(s.toString(), dialogStateListBinding)
                }

            })
            stateListDialog.show()
        }

        // City dropdown
        activityApnaNewSurveyBinding.cityText.setOnClickListener {
            if (activityApnaNewSurveyBinding.stateText.text.toString().isNotEmpty()) {

                cityListDialog = Dialog(this@ApnaNewSurveyActivity)
                val dialogCityListBinding = DataBindingUtil.inflate<DialogCityListBinding>(
                    LayoutInflater.from(this@ApnaNewSurveyActivity),
                    R.layout.dialog_city_list,
                    null,
                    false
                )
                cityListDialog.setContentView(dialogCityListBinding.root)
                cityListDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                cityListDialog.setCancelable(false)
                dialogCityListBinding.closeDialog.setOnClickListener {
                    cityListDialog.dismiss()
                }
                cityItemAdapter = CityItemAdapter(
                    this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, cityList
                )
                dialogCityListBinding.cityRcv.adapter = cityItemAdapter
                dialogCityListBinding.cityRcv.layoutManager =
                    LinearLayoutManager(this@ApnaNewSurveyActivity)
                dialogCityListBinding.searchCityText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        filterCityList(s.toString(), dialogCityListBinding)
                    }

                })
                cityListDialog.show()

            } else {
                Toast.makeText(
                    this@ApnaNewSurveyActivity, "Please select state first", Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Traffic street dropdown
        activityApnaNewSurveyBinding.trafficStreetSelect.setOnClickListener {
            trafficStreetDialog =
                Dialog(this@ApnaNewSurveyActivity)//, android.R.style.Theme_Black_NoTitleBar_Fullscreen
            val dialogTrafficStreetBinding = DataBindingUtil.inflate<DialogTrafficStreetBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_traffic_street,
                null,
                false
            )
            trafficStreetDialog.setContentView(dialogTrafficStreetBinding.root)
            trafficStreetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            trafficStreetDialog.setCancelable(false)
            dialogTrafficStreetBinding.closeDialog.setOnClickListener {
                trafficStreetDialog.dismiss()
            }
            trafficStreetAdapter = TrafficStreetAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, trafficStreetData
            )
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

            trafficGeneratorAdapter = TrafficGeneratorAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, trafficGeneratorData
            )
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

            dialogTrafficGeneratorBinding.submit.setOnClickListener {
                trafficGeneratorDialog.dismiss()
                trafficGeneratorsItemAdapter = TrafficGeneratorsItemAdapter(
                    this@ApnaNewSurveyActivity,
                    this@ApnaNewSurveyActivity,
                    selectedTrafficGeneratorItem
                )
                activityApnaNewSurveyBinding.trafficGeneratorsRcv.adapter =
                    trafficGeneratorsItemAdapter
                activityApnaNewSurveyBinding.trafficGeneratorsRcv.layoutManager =
                    LinearLayoutManager(
                        this@ApnaNewSurveyActivity, LinearLayoutManager.HORIZONTAL, false
                    )
                trafficGeneratorsItemAdapter.notifyDataSetChanged()
            }
            trafficGeneratorDialog.show()
        }

        // Dimension Type Dropdown
        activityApnaNewSurveyBinding.dimensionTypeSelect.setOnClickListener {
            dimensionTypeDialog = Dialog(this@ApnaNewSurveyActivity)
            dimensionTypeDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dimensionTypeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val dialogDimensionTypeBinding = DataBindingUtil.inflate<DialogDimensionTypeBinding>(
                LayoutInflater.from(this@ApnaNewSurveyActivity),
                R.layout.dialog_dimension_type,
                null,
                false
            )
            dimensionTypeDialog.setContentView(dialogDimensionTypeBinding.root)
            dialogDimensionTypeBinding.closeDialog.setOnClickListener {
                dimensionTypeDialog.dismiss()
            }
            dimensionTypeAdapter = DimensionTypeAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, dimensionTypeList
            )
            dialogDimensionTypeBinding.dimensionTypeRcv.adapter = dimensionTypeAdapter
            dialogDimensionTypeBinding.dimensionTypeRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            dialogDimensionTypeBinding.searchDimensionTypeText.addTextChangedListener(object :
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
                    dimensionTypeFilter(s.toString(), dialogDimensionTypeBinding)
                }

            })

            dimensionTypeDialog.setCancelable(false)
            dimensionTypeDialog.show()
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

            apartmentTypeAdapter = ApartmentTypeAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, apartmentTypeData
            )
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
            val dialogApnaSpecialityBinding = DataBindingUtil.inflate<DialogApnaSpecialityBinding>(
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
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, apnaSpecialityData
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

        // Adding Neighbouring Store
        activityApnaNewSurveyBinding.neighbourAddBtn.setOnClickListener {
            val location =
                activityApnaNewSurveyBinding.neighbourLocationSelect.text.toString().trim()
            val store = activityApnaNewSurveyBinding.storeText.text.toString().trim()
            val rent = activityApnaNewSurveyBinding.rentText.text.toString().trim()
            val sales = activityApnaNewSurveyBinding.salesText.text.toString().trim()
            val sqFt = activityApnaNewSurveyBinding.sqFtText.text.toString().trim()

            neighbouringStoreList.add(
                NeighbouringStoreData(
                    location,
                    store,
                    rent,
                    sales,
                    sqFt
                )
            )
            neighbouringStoreAdapter = NeighbouringStoreAdapter(
                this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                neighbouringStoreList
            )
            activityApnaNewSurveyBinding.neighbouringStoreRcv.adapter = neighbouringStoreAdapter
            activityApnaNewSurveyBinding.neighbouringStoreRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            activityApnaNewSurveyBinding.neighbourLocationSelect.text!!.clear()
            activityApnaNewSurveyBinding.storeText.text!!.clear()
            activityApnaNewSurveyBinding.rentText.text!!.clear()
            activityApnaNewSurveyBinding.salesText.text!!.clear()
            activityApnaNewSurveyBinding.sqFtText.text!!.clear()
        }

        // Adding apartments data
        activityApnaNewSurveyBinding.apartmentsAddBtn.setOnClickListener {
            val apartments = activityApnaNewSurveyBinding.apartmentsOrColony.text.toString()
            val apartmentType = activityApnaNewSurveyBinding.apartmentTypeSelect.text.toString()
            val noOfHouses = activityApnaNewSurveyBinding.noOfHousesText.text.toString()
            val distance = activityApnaNewSurveyBinding.distanceText.text.toString()
            if (apartments.isNotEmpty() && apartmentType.isNotEmpty() && noOfHouses.isNotEmpty() && distance.isNotEmpty()) {
                apartmentsList.add(
                    ApartmentData(
                        apartments, apartmentType, noOfHouses.toInt(), distance.toInt()
                    )
                )
            }
            apartmentTypeItemAdapter = ApartmentTypeItemAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, apartmentsList
            )
            activityApnaNewSurveyBinding.apartmentsRecyclerView.adapter = apartmentTypeItemAdapter
            activityApnaNewSurveyBinding.apartmentsRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)
            apartmentTypeItemAdapter.notifyDataSetChanged()
            // clear
            activityApnaNewSurveyBinding.apartmentsOrColony.text!!.clear()
            activityApnaNewSurveyBinding.apartmentTypeSelect.text!!.clear()
            activityApnaNewSurveyBinding.noOfHousesText.text!!.clear()
            activityApnaNewSurveyBinding.distanceText.text!!.clear()
        }

        // Adding hospital data
        activityApnaNewSurveyBinding.hospitalAddBtn.setOnClickListener {
            val name = activityApnaNewSurveyBinding.hospitalNameText.text.toString()
            val speciality = activityApnaNewSurveyBinding.hospitalSpecialitySelect.text.toString()
            val beds = activityApnaNewSurveyBinding.bedsText.text.toString()
            val noOfOpd = activityApnaNewSurveyBinding.noOfOpdText.text.toString()
            val occupancy = activityApnaNewSurveyBinding.occupancyText.text.toString()

            if (name.isNotEmpty() && speciality.isNotEmpty() && beds.isNotEmpty() && noOfOpd.isNotEmpty() && occupancy.isNotEmpty()) {
                hospitalsList.add(
                    HospitalData(
                        name, beds.toInt(), speciality, noOfOpd.toInt(), occupancy.toInt()
                    )
                )
            }

            hospitalsAdapter = HospitalsAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, hospitalsList
            )
            activityApnaNewSurveyBinding.hospitalsRecyclerView.adapter = hospitalsAdapter
            activityApnaNewSurveyBinding.hospitalsRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)
            hospitalsAdapter.notifyDataSetChanged()

            // clear
            activityApnaNewSurveyBinding.hospitalNameText.text!!.clear()
            activityApnaNewSurveyBinding.hospitalSpecialitySelect.text!!.clear()
            activityApnaNewSurveyBinding.bedsText.text!!.clear()
            activityApnaNewSurveyBinding.noOfOpdText.text!!.clear()
            activityApnaNewSurveyBinding.occupancyText.text!!.clear()
        }

        // Adding chemist data
        activityApnaNewSurveyBinding.chemistAddBtn.setOnClickListener {
            val chemist = activityApnaNewSurveyBinding.chemistText.text.toString()
            val organised = activityApnaNewSurveyBinding.organisedSelect.text.toString()
            val organisedAvgSale = activityApnaNewSurveyBinding.organisedAvgSaleText.text.toString()
            val unorganised = activityApnaNewSurveyBinding.unorganisedSelect.text.toString()
            val unorganisedAvgSale =
                activityApnaNewSurveyBinding.unorganisedAvgSaleText.text.toString()

            if (chemist.isNotEmpty() && organised.isNotEmpty() && organisedAvgSale.isNotEmpty() && unorganised.isNotEmpty() && unorganisedAvgSale.isNotEmpty()) {
                chemistList.add(
                    ChemistData(
                        chemist, organised, organisedAvgSale, unorganised, unorganisedAvgSale
                    )
                )
            }
            chemistAdapter =
                ChemistAdapter(this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, chemistList)
            activityApnaNewSurveyBinding.chemistRecyclerView.adapter = chemistAdapter
            activityApnaNewSurveyBinding.chemistRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)

            // Clear
            activityApnaNewSurveyBinding.chemistText.text!!.clear()
            activityApnaNewSurveyBinding.organisedSelect.text!!.clear()
            activityApnaNewSurveyBinding.organisedAvgSaleText.text!!.clear()
            activityApnaNewSurveyBinding.unorganisedSelect.text!!.clear()
            activityApnaNewSurveyBinding.unorganisedAvgSaleText.text!!.clear()

            if (chemistList.size > 0) {
                activityApnaNewSurveyBinding.chemistTotalLayout.visibility = View.VISIBLE
                val totalOrganisedAvgSale =
                    chemistList.stream().map { it.organisedAvgSale }.mapToDouble { it.toDouble() }
                        .sum()
                activityApnaNewSurveyBinding.totalOrganisedText.text =
                    String.format("%.1f", totalOrganisedAvgSale)

                val totalUnorganisedAvgSale =
                    chemistList.stream().map { it.unorganisedAvgSale }.mapToDouble { it.toDouble() }
                        .sum()
                activityApnaNewSurveyBinding.totalUnorganisedText.text =
                    String.format("%.1f", totalUnorganisedAvgSale)
            } else {
                activityApnaNewSurveyBinding.chemistTotalLayout.visibility = View.GONE
            }
        }

        // Neighbouring Location Dropdown
        activityApnaNewSurveyBinding.neighbourLocationSelect.setOnClickListener {
            neighbouringLocationDialog = Dialog(this@ApnaNewSurveyActivity)
            neighbouringLocationDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogNeighbourLocationBinding =
                DataBindingUtil.inflate<DialogNeighbourLocationBinding>(
                    LayoutInflater.from(this@ApnaNewSurveyActivity),
                    R.layout.dialog_neighbour_location,
                    null,
                    false
                )
            neighbouringLocationDialog.setContentView(dialogNeighbourLocationBinding.root)
            dialogNeighbourLocationBinding.closeDialog.setOnClickListener {
                neighbouringLocationDialog.dismiss()
            }

            neighbouringLocationAdapter = NeighbouringLocationAdapter(
                this@ApnaNewSurveyActivity,
                this@ApnaNewSurveyActivity,
                neighbouringLocationList
            )
            dialogNeighbourLocationBinding.neighbouringLocationRcv.adapter =
                neighbouringLocationAdapter
            dialogNeighbourLocationBinding.neighbouringLocationRcv.layoutManager =
                LinearLayoutManager(this@ApnaNewSurveyActivity)
            dialogNeighbourLocationBinding.searchNeighbouringLocationText.addTextChangedListener(
                object :
                    TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        neighbouringLocationFilter(s.toString(), dialogNeighbourLocationBinding)
                    }
                })

            neighbouringLocationDialog.setCancelable(false)
            neighbouringLocationDialog.show()
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

            organisedAdapter = OrganisedAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, parkingTypeList
            )
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

            unOrganisedAdapter = UnOrganisedAdapter(
                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, parkingTypeList
            )
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


        var morningFromHour: Int = 0
        var morningFromMinute: Int = 0
        var morningToHour: Int = 0
        var morningToMinute: Int = 0

        var eveningFromHour: Int = 0
        var eveningFromMinute: Int = 0
        var eveningToHour: Int = 0
        var eveningToMinute: Int = 0

        // Morning from dropdown
        activityApnaNewSurveyBinding.morningFromSelect.setOnClickListener {
            if (activityApnaNewSurveyBinding.morningFromSelect.text.toString() != null && !activityApnaNewSurveyBinding.morningFromSelect.text.toString()
                    .isEmpty()
            ) {
                var existTime = activityApnaNewSurveyBinding.morningFromSelect.text.toString()
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                morningFromTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            morningFromHour = hourOfDay
                            morningFromMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)

                            if (activityApnaNewSurveyBinding.morningToSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.morningToSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if (morningFromHour > morningToHour && morningFromMinute > morningToMinute) {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                    activityApnaNewSurveyBinding.morningFromSelect.getText()!!.clear()
                                } else if (morningFromHour == morningToHour && morningFromMinute > morningToMinute) {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                    activityApnaNewSurveyBinding.morningFromSelect.getText()!!.clear()
                                } else if (morningFromHour > morningToHour && morningFromMinute == morningToMinute) {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                    activityApnaNewSurveyBinding.morningFromSelect.getText()!!.clear()
                                }
                                else {
                                    activityApnaNewSurveyBinding.morningFromSelect.setText(
                                        formattedTime)
                                }
                            } else {
                                activityApnaNewSurveyBinding.morningFromSelect.setText(formattedTime)
                            }
                        }
                    },
                    Integer.valueOf(existTime.substring(0, existTime.indexOf(":"))),
                    Integer.valueOf(existTime.substring(existTime.indexOf(":") + 1)),
                    true
                )
                morningFromTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                morningFromTimePickerDialog.show()
            } else {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                morningFromTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            morningFromHour = hourOfDay
                            morningFromMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)

                            if (activityApnaNewSurveyBinding.morningToSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.morningToSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if (morningFromHour > morningToHour && morningFromMinute > morningToMinute) {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                    activityApnaNewSurveyBinding.morningFromSelect.getText()!!.clear()
                                } else if (morningFromHour == morningToHour && morningFromMinute > morningToMinute) {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                    activityApnaNewSurveyBinding.morningFromSelect.getText()!!.clear()
                                } else if (morningFromHour > morningToHour && morningFromMinute == morningToMinute) {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                    activityApnaNewSurveyBinding.morningFromSelect.getText()!!.clear()
                                }
                                else {
                                    activityApnaNewSurveyBinding.morningFromSelect.setText(
                                        formattedTime)
                                }
                            } else {
                                activityApnaNewSurveyBinding.morningFromSelect.setText(formattedTime)
                            }
                        }
                    },
                    hour,
                    minute,
                    true
                )
                morningFromTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                morningFromTimePickerDialog.show()
            }
        }

        // Morning to dropdown
        activityApnaNewSurveyBinding.morningToSelect.setOnClickListener {
            if (activityApnaNewSurveyBinding.morningToSelect.text.toString() != null && !activityApnaNewSurveyBinding.morningToSelect.text.toString()
                    .isEmpty()
            ) {
                var existTime = activityApnaNewSurveyBinding.morningToSelect.text.toString()
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                morningToTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            morningToHour = hourOfDay
                            morningToMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)
                            if (activityApnaNewSurveyBinding.morningFromSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.morningFromSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if (morningToHour < morningFromHour && morningToMinute < morningFromMinute) {
                                    activityApnaNewSurveyBinding.morningToSelect.getText()!!.clear()
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()

                                } else if (morningToHour == morningFromHour && morningToMinute < morningFromMinute) {
                                    activityApnaNewSurveyBinding.morningToSelect.getText()!!.clear()
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                } else if (morningToHour < morningFromHour && morningToMinute == morningFromMinute) {
                                    activityApnaNewSurveyBinding.morningToSelect.getText()!!.clear()
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    activityApnaNewSurveyBinding.morningToSelect.setText(
                                        formattedTime)
                                }
                            } else {
                                activityApnaNewSurveyBinding.morningToSelect.setText(formattedTime)
                            }

                        }
                    },
                    Integer.valueOf(existTime.substring(0, existTime.indexOf(":"))),
                    Integer.valueOf(existTime.substring(existTime.indexOf(":") + 1)),
                    true
                )
                morningToTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                morningToTimePickerDialog.show()
            } else {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                morningToTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            morningToHour = hourOfDay
                            morningToMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)
                            if (activityApnaNewSurveyBinding.morningFromSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.morningFromSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if ((morningToHour > morningFromHour && morningToMinute > morningFromMinute) ||
                                    (morningToHour >= morningFromHour && morningToMinute > morningFromMinute)
                                ) {
                                    activityApnaNewSurveyBinding.morningToSelect.setText(
                                        formattedTime)
                                } else {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                activityApnaNewSurveyBinding.morningToSelect.setText(formattedTime)
                            }
                        }

                    },
                    hour,
                    minute,
                    true
                )
                morningToTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                morningToTimePickerDialog.show()
            }
        }

        // Evening from dropdown
        activityApnaNewSurveyBinding.eveningFromSelect.setOnClickListener {
            if (activityApnaNewSurveyBinding.eveningFromSelect.text.toString() != null && !activityApnaNewSurveyBinding.eveningFromSelect.text.toString()
                    .isEmpty()
            ) {
                var existTime = activityApnaNewSurveyBinding.eveningFromSelect.text.toString()
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                eveningFromTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            eveningFromHour = hourOfDay
                            eveningFromMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)
                            if (activityApnaNewSurveyBinding.eveningToSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.eveningToSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if ((eveningToHour > eveningFromHour && eveningToMinute > eveningFromMinute) ||
                                    (eveningToHour >= eveningFromHour && eveningToMinute > eveningFromMinute)) {
                                    activityApnaNewSurveyBinding.eveningFromSelect.setText(
                                        formattedTime)
                                } else {
                                    activityApnaNewSurveyBinding.eveningFromSelect.getText()!!.clear()
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                activityApnaNewSurveyBinding.eveningFromSelect.setText(formattedTime)
                            }

                        }
                    },
                    Integer.valueOf(existTime.substring(0, existTime.indexOf(":"))),
                    Integer.valueOf(existTime.substring(existTime.indexOf(":") + 1)),
                    true
                )
                eveningFromTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                eveningFromTimePickerDialog.show()
            } else {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                eveningFromTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            eveningFromHour = hourOfDay
                            eveningFromMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)
                            if (activityApnaNewSurveyBinding.eveningToSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.eveningToSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if ((eveningToHour > eveningFromHour && eveningToMinute > eveningFromMinute) ||
                                    (eveningToHour >= eveningFromHour && eveningToMinute > eveningFromMinute)) {
                                    activityApnaNewSurveyBinding.eveningFromSelect.setText(
                                        formattedTime)
                                } else {
                                    activityApnaNewSurveyBinding.eveningFromSelect.getText()!!.clear()
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                activityApnaNewSurveyBinding.eveningFromSelect.setText(formattedTime)
                            }
                        }

                    },
                    hour,
                    minute,
                    true
                )
                eveningFromTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                eveningFromTimePickerDialog.show()
            }
        }

        // Evening to dropdown
        activityApnaNewSurveyBinding.eveningToSelect.setOnClickListener {
            if (activityApnaNewSurveyBinding.eveningToSelect.text.toString() != null && !activityApnaNewSurveyBinding.eveningToSelect.text.toString()
                    .isEmpty()
            ) {
                var existTime = activityApnaNewSurveyBinding.eveningToSelect.text.toString()
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                eveningToTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            eveningToHour = hourOfDay
                            eveningToMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)
                            if (activityApnaNewSurveyBinding.eveningFromSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.eveningFromSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if ((eveningToHour > eveningFromHour && eveningToMinute > eveningFromMinute) ||
                                    (eveningToHour >= eveningFromHour && eveningToMinute > eveningFromMinute)
                                ) {
                                    activityApnaNewSurveyBinding.eveningToSelect.setText(
                                        formattedTime)
                                } else {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                activityApnaNewSurveyBinding.eveningToSelect.setText(formattedTime)
                            }

                        }
                    },
                    Integer.valueOf(existTime.substring(0, existTime.indexOf(":"))),
                    Integer.valueOf(existTime.substring(existTime.indexOf(":") + 1)),
                    true
                )
                eveningToTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                eveningToTimePickerDialog.show()
            } else {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                eveningToTimePickerDialog = TimePickerDialog(
                    this@ApnaNewSurveyActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            eveningToHour = hourOfDay
                            eveningToMinute = minute
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val formattedTime = simpleDateFormat.format(calendar.time)
                            if (activityApnaNewSurveyBinding.eveningFromSelect.text.toString() != null
                                && !activityApnaNewSurveyBinding.eveningFromSelect.text.toString()
                                    .isEmpty()
                            ) {
                                if ((eveningToHour > eveningFromHour && eveningToMinute > eveningFromMinute) ||
                                    (eveningToHour >= eveningFromHour && eveningToMinute > eveningFromMinute)
                                ) {
                                    activityApnaNewSurveyBinding.eveningToSelect.setText(
                                        formattedTime)
                                } else {
                                    Toast.makeText(this@ApnaNewSurveyActivity,
                                        "Invalid Time",
                                        Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                activityApnaNewSurveyBinding.eveningToSelect.setText(formattedTime)
                            }
                        }

                    },
                    hour,
                    minute,
                    true
                )
                eveningToTimePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                eveningToTimePickerDialog.show()
            }
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
                    activityApnaNewSurveyBinding.totalAreaText.getText()!!.clear()
                } else if (s.toString()
                        .isEmpty() && activityApnaNewSurveyBinding.widthText.text.toString()
                        .isNotEmpty()
                ) {
                    activityApnaNewSurveyBinding.totalAreaText.getText()!!.clear()
                } else {
                    activityApnaNewSurveyBinding.totalAreaText.getText()!!.clear()
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
                    activityApnaNewSurveyBinding.totalAreaText.getText()!!.clear()
                } else if (s.toString()
                        .isEmpty() && activityApnaNewSurveyBinding.lengthText.text.toString()
                        .isNotEmpty()
                ) {
                    activityApnaNewSurveyBinding.totalAreaText.getText()!!.clear()
                } else {
                    activityApnaNewSurveyBinding.totalAreaText.getText()!!.clear()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        activityApnaNewSurveyBinding.clear.setOnClickListener {
            activityApnaNewSurveyBinding.totalAreaText.setText("")
            activityApnaNewSurveyBinding.lengthText.text!!.clear()
            activityApnaNewSurveyBinding.widthText.text!!.clear()
        }

        // Append %
        activityApnaNewSurveyBinding.areaDiscountText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredText = s.toString()
                if (enteredText.isNotEmpty() && !enteredText.endsWith("%")) {
                    activityApnaNewSurveyBinding.areaDiscountText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.areaDiscountText.setText("$enteredText%")
                    activityApnaNewSurveyBinding.areaDiscountText.setSelection(
                        activityApnaNewSurveyBinding.areaDiscountText.text!!.length - 1
                    )
                    activityApnaNewSurveyBinding.areaDiscountText.addTextChangedListener(this)
                } else if (enteredText.endsWith("%") && enteredText.length > 1) {
                    activityApnaNewSurveyBinding.areaDiscountText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.areaDiscountText.setText(enteredText)
                    activityApnaNewSurveyBinding.areaDiscountText.setSelection(
                        activityApnaNewSurveyBinding.areaDiscountText.text!!.length - 1
                    )
                    activityApnaNewSurveyBinding.areaDiscountText.addTextChangedListener(this)
                } else if (enteredText.isEmpty() || activityApnaNewSurveyBinding.areaDiscountText.text.toString() == "%") {
                    activityApnaNewSurveyBinding.areaDiscountText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.areaDiscountText.text!!.clear()
                    activityApnaNewSurveyBinding.areaDiscountText.addTextChangedListener(this)
                }

                val pharma = activityApnaNewSurveyBinding.pharmaText.text.toString().trim()
                val fmcg = activityApnaNewSurveyBinding.fmcgText.text.toString().trim()
                val surgicals = activityApnaNewSurveyBinding.surgicalsText.text.toString().trim()
                val areaDiscount = s.toString().trim()

                var pharmaValue: Int = 0
                var fmcgValue: Int = 0
                var surgicalsValue: Int = 0
                var areaDiscountValue: Int = 0

                if (pharma.isEmpty()) {
                    pharmaValue = 0
                } else {
                    if (pharma.endsWith("%")) {
                        pharmaValue = pharma.substring(0, pharma.length - 1).toInt()
                    } else {
                        pharmaValue = pharma.toInt()
                    }
                }

                if (fmcg.isEmpty()) {
                    fmcgValue = 0
                } else {
                    if (fmcg.endsWith("%")) {
                        fmcgValue = fmcg.substring(0, fmcg.length - 1).toInt()
                    } else {
                        fmcgValue = fmcg.toInt()
                    }
                }

                if (surgicals.isEmpty()) {
                    surgicalsValue = 0
                } else {
                    if (surgicals.endsWith("%")) {
                        surgicalsValue = surgicals.substring(0, surgicals.length - 1).toInt()
                    } else {
                        surgicalsValue = surgicals.toInt()
                    }
                }

                if (areaDiscount.isEmpty()) {
                    areaDiscountValue = 0
                } else {
                    if (areaDiscount.endsWith("%")) {
                        areaDiscountValue =
                            areaDiscount.substring(0, areaDiscount.length - 1).toInt()
                    } else {
                        areaDiscountValue = areaDiscount.toInt()
                    }
                }
                val sum = pharmaValue + fmcgValue + surgicalsValue + areaDiscountValue
                if (sum > 100) {
                    activityApnaNewSurveyBinding.areaDiscountText.getText()!!.clear()
                    Toast.makeText(
                        this@ApnaNewSurveyActivity,
                        "Must be less than or equals to 100",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        activityApnaNewSurveyBinding.pharmaText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredText = s.toString()
                if (enteredText.isNotEmpty() && !enteredText.endsWith("%")) {
                    activityApnaNewSurveyBinding.pharmaText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.pharmaText.setText("$enteredText%")
                    activityApnaNewSurveyBinding.pharmaText.setSelection(
                        activityApnaNewSurveyBinding.pharmaText.text!!.length - 1
                    )
                    activityApnaNewSurveyBinding.pharmaText.addTextChangedListener(this)
                } else if (enteredText.endsWith("%") && enteredText.length > 1) {
                    activityApnaNewSurveyBinding.pharmaText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.pharmaText.setText(enteredText)
                    activityApnaNewSurveyBinding.pharmaText.setSelection(
                        activityApnaNewSurveyBinding.pharmaText.text!!.length - 1
                    )
                    activityApnaNewSurveyBinding.pharmaText.addTextChangedListener(this)
                } else if (enteredText.isEmpty() || activityApnaNewSurveyBinding.pharmaText.text.toString() == "%") {
                    activityApnaNewSurveyBinding.pharmaText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.pharmaText.text!!.clear()
                    activityApnaNewSurveyBinding.pharmaText.addTextChangedListener(this)
                }

                val pharma = s.toString().trim()
                val fmcg = activityApnaNewSurveyBinding.fmcgText.text.toString().trim()
                val surgicals = activityApnaNewSurveyBinding.surgicalsText.text.toString().trim()
                val areaDiscount =
                    activityApnaNewSurveyBinding.areaDiscountText.text.toString().trim()

                var pharmaValue: Int = 0
                var fmcgValue: Int = 0
                var surgicalsValue: Int = 0
                var areaDiscountValue: Int = 0

                if (pharma.isEmpty()) {
                    pharmaValue = 0
                } else {
                    if (pharma.endsWith("%")) {
                        pharmaValue = pharma.substring(0, pharma.length - 1).toInt()
                    } else {
                        pharmaValue = pharma.toInt()
                    }
                }

                if (fmcg.isEmpty()) {
                    fmcgValue = 0
                } else {
                    if (fmcg.endsWith("%")) {
                        fmcgValue = fmcg.substring(0, fmcg.length - 1).toInt()
                    } else {
                        fmcgValue = fmcg.toInt()
                    }
                }

                if (surgicals.isEmpty()) {
                    surgicalsValue = 0
                } else {
                    if (surgicals.endsWith("%")) {
                        surgicalsValue = surgicals.substring(0, surgicals.length - 1).toInt()
                    } else {
                        surgicalsValue = surgicals.toInt()
                    }
                }

                if (areaDiscount.isEmpty()) {
                    areaDiscountValue = 0
                } else {
                    if (areaDiscount.endsWith("%")) {
                        areaDiscountValue = areaDiscount.substring(0, areaDiscount.length - 1).toInt()
                    } else {
                        areaDiscountValue = areaDiscount.toInt()
                    }
                }

                val sum = pharmaValue + fmcgValue + surgicalsValue + areaDiscountValue
                if (sum > 100) {
                    activityApnaNewSurveyBinding.pharmaText.getText()!!.clear()
                    Toast.makeText(
                        this@ApnaNewSurveyActivity,
                        "Must be less than or equals to 100",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        activityApnaNewSurveyBinding.fmcgText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredText = s.toString()
                if (enteredText.isNotEmpty() && !enteredText.endsWith("%")) {
                    activityApnaNewSurveyBinding.fmcgText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.fmcgText.setText("$enteredText%")
                    activityApnaNewSurveyBinding.fmcgText.setSelection(activityApnaNewSurveyBinding.fmcgText.text!!.length - 1)
                    activityApnaNewSurveyBinding.fmcgText.addTextChangedListener(this)
                } else if (enteredText.endsWith("%") && enteredText.length > 1) {
                    activityApnaNewSurveyBinding.fmcgText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.fmcgText.setText(enteredText)
                    activityApnaNewSurveyBinding.fmcgText.setSelection(activityApnaNewSurveyBinding.fmcgText.text!!.length - 1)
                    activityApnaNewSurveyBinding.fmcgText.addTextChangedListener(this)
                } else if (enteredText.isEmpty() || activityApnaNewSurveyBinding.fmcgText.text.toString() == "%") {
                    activityApnaNewSurveyBinding.fmcgText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.fmcgText.text!!.clear()
                    activityApnaNewSurveyBinding.fmcgText.addTextChangedListener(this)
                }

                val pharma = activityApnaNewSurveyBinding.pharmaText.text.toString().trim()
                val fmcg = s.toString().trim()
                val surgicals = activityApnaNewSurveyBinding.surgicalsText.text.toString().trim()
                val areaDiscount =
                    activityApnaNewSurveyBinding.areaDiscountText.text.toString().trim()

                var pharmaValue: Int = 0
                var fmcgValue: Int = 0
                var surgicalsValue: Int = 0
                var areaDiscountValue: Int = 0

                if (pharma.isEmpty()) {
                    pharmaValue = 0
                } else {
                    if (pharma.endsWith("%")) {
                        pharmaValue = pharma.substring(0, pharma.length - 1).toInt()
                    } else {
                        pharmaValue = pharma.toInt()
                    }
                }

                if (fmcg.isEmpty()) {
                    fmcgValue = 0
                } else {
                    if (fmcg.endsWith("%")) {
                        fmcgValue = fmcg.substring(0, fmcg.length - 1).toInt()
                    } else {
                        fmcgValue = fmcg.toInt()
                    }
                }

                if (surgicals.isEmpty()) {
                    surgicalsValue = 0
                } else {
                    if (surgicals.endsWith("%")) {
                        surgicalsValue = surgicals.substring(0, surgicals.length - 1).toInt()
                    } else {
                        surgicalsValue = surgicals.toInt()
                    }
                }

                if (areaDiscount.isEmpty()) {
                    areaDiscountValue = 0
                } else {
                    if (areaDiscount.endsWith("%")) {
                        areaDiscountValue = areaDiscount.substring(0, areaDiscount.length - 1).toInt()
                    } else {
                        areaDiscountValue = areaDiscount.toInt()
                    }
                }
                val sum = pharmaValue + fmcgValue + surgicalsValue + areaDiscountValue
                if (sum > 100) {
                    activityApnaNewSurveyBinding.fmcgText.getText()!!.clear()
                    Toast.makeText(
                        this@ApnaNewSurveyActivity,
                        "Must be less than or equals to 100",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        activityApnaNewSurveyBinding.surgicalsText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredText = s.toString()
                if (enteredText.isNotEmpty() && !enteredText.endsWith("%")) {
                    activityApnaNewSurveyBinding.surgicalsText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.surgicalsText.setText("$enteredText%")
                    activityApnaNewSurveyBinding.surgicalsText.setSelection(
                        activityApnaNewSurveyBinding.surgicalsText.text!!.length - 1
                    )
                    activityApnaNewSurveyBinding.surgicalsText.addTextChangedListener(this)
                } else if (enteredText.endsWith("%") && enteredText.length > 1) {
                    activityApnaNewSurveyBinding.surgicalsText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.surgicalsText.setText(enteredText)
                    activityApnaNewSurveyBinding.surgicalsText.setSelection(
                        activityApnaNewSurveyBinding.surgicalsText.text!!.length - 1
                    )
                    activityApnaNewSurveyBinding.surgicalsText.addTextChangedListener(this)
                } else if (enteredText.isEmpty() || activityApnaNewSurveyBinding.surgicalsText.text.toString() == "%") {
                    activityApnaNewSurveyBinding.surgicalsText.removeTextChangedListener(this)
                    activityApnaNewSurveyBinding.surgicalsText.text!!.clear()
                    activityApnaNewSurveyBinding.surgicalsText.addTextChangedListener(this)
                }

                val pharma = activityApnaNewSurveyBinding.pharmaText.text.toString().trim()
                val fmcg = activityApnaNewSurveyBinding.fmcgText.text.toString().trim()
                val surgicals = s.toString().trim()
                val areaDiscount =
                    activityApnaNewSurveyBinding.areaDiscountText.text.toString().trim()

                var pharmaValue: Int = 0
                var fmcgValue: Int = 0
                var surgicalsValue: Int = 0
                var areaDiscountValue: Int = 0

                if (pharma.isEmpty()) {
                    pharmaValue = 0
                } else {
                    if (pharma.endsWith("%")) {
                        pharmaValue = pharma.substring(0, pharma.length - 1).toInt()
                    } else {
                        pharmaValue = pharma.toInt()
                    }
                }

                if (fmcg.isEmpty()) {
                    fmcgValue = 0
                } else {
                    if (fmcg.endsWith("%")) {
                        fmcgValue = fmcg.substring(0, fmcg.length - 1).toInt()
                    } else {
                        fmcgValue = fmcg.toInt()
                    }
                }

                if (surgicals.isEmpty()) {
                    surgicalsValue = 0
                } else {
                    if (surgicals.endsWith("%")) {
                        surgicalsValue = surgicals.substring(0, surgicals.length - 1).toInt()
                    } else {
                        surgicalsValue = surgicals.toInt()
                    }
                }

                if (areaDiscount.isEmpty()) {
                    areaDiscountValue = 0
                } else {
                    if (areaDiscount.endsWith("%")) {
                        areaDiscountValue =
                            areaDiscount.substring(0, areaDiscount.length - 1).toInt()
                    } else {
                        areaDiscountValue = areaDiscount.toInt()
                    }
                }
                val sum = pharmaValue + fmcgValue + surgicalsValue + areaDiscountValue
                if (sum > 100) {
                    activityApnaNewSurveyBinding.surgicalsText.getText()!!.clear()
                    Toast.makeText(
                        this@ApnaNewSurveyActivity,
                        "Must be less than or equals to 100",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // on click submit
        activityApnaNewSurveyBinding.submitBtn.setOnClickListener {
            if (validateLocationDetails() && validateSiteSpecification()) {
                surveyCreateRequest.employeeId = Preferences.getValidatedEmpId()

                val dialog = Dialog(this@ApnaNewSurveyActivity)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val createSurveyConfirmDialogBinding =
                    DataBindingUtil.inflate<CreateSurveyConfirmDialogBinding>(
                        LayoutInflater.from(
                            this@ApnaNewSurveyActivity
                        ), R.layout.create_survey_confirm_dialog, null, false
                    )
                dialog.setContentView(createSurveyConfirmDialogBinding.root)
                createSurveyConfirmDialogBinding.cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
                createSurveyConfirmDialogBinding.saveButton.setOnClickListener {
                    dialog.dismiss()
                    Utlis.showLoading(this@ApnaNewSurveyActivity)
                    if (imageFileList != null && imageFileList.size > 0) {
                        apnaNewSurveyViewModel.imagesListConnectToAzure(
                            imageFileList, this@ApnaNewSurveyActivity, surveyCreateRequest
                        )
                    } else if (videoFile != null) {
                        var videoFileList = ArrayList<File>()
                        videoFileList.add(videoFile!!)
                        apnaNewSurveyViewModel.videoListConnectToAzure(
                            videoFileList, this@ApnaNewSurveyActivity, surveyCreateRequest
                        )
                    } else {
                        apnaNewSurveyViewModel.createSurvey(
                            surveyCreateRequest, this@ApnaNewSurveyActivity
                        )
                    }
                }
                dialog.setCancelable(false)
                dialog.show()

            } else {
                Toast.makeText(
                    this@ApnaNewSurveyActivity,
                    "Please fill all mandatory fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun neighbouringLocationFilter(
        searchText: String,
        dialogNeighbourLocationBinding: DialogNeighbourLocationBinding?,
    ) {
        val filteredList = ArrayList<NeighbouringLocationResponse.Data.ListData.Row>()
        for (i in neighbouringLocationList.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(neighbouringLocationList)
            } else if (neighbouringLocationList[i].name!!.contains(searchText, true)) {
                filteredList.add(neighbouringLocationList[i])
            }
        }
        if (filteredList.size < 1) {
            dialogNeighbourLocationBinding!!.neighbouringLocationRcv.visibility = View.GONE
            dialogNeighbourLocationBinding.neighbouringLocationAvailable.visibility = View.VISIBLE
        } else {
            dialogNeighbourLocationBinding!!.neighbouringLocationRcv.visibility = View.VISIBLE
            dialogNeighbourLocationBinding.neighbouringLocationAvailable.visibility = View.GONE
        }
        neighbouringLocationAdapter.filter(filteredList)
    }

    private fun dimensionTypeFilter(
        searchText: String,
        dialogDimensionTypeBinding: DialogDimensionTypeBinding?,
    ) {
        val filteredList = ArrayList<DimensionTypeResponse.Data.ListData.Row>()
        for (i in dimensionTypeList.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(dimensionTypeList)
            } else if (dimensionTypeList[i].name!!.contains(searchText, true)) {
                filteredList.add(dimensionTypeList[i])
            }
        }
        if (filteredList.size < 1) {
            dialogDimensionTypeBinding!!.dimensionTypeRcv.visibility = View.GONE
            dialogDimensionTypeBinding.dimensionTypeAvailable.visibility = View.VISIBLE
        } else {
            dialogDimensionTypeBinding!!.dimensionTypeRcv.visibility = View.VISIBLE
            dialogDimensionTypeBinding.dimensionTypeAvailable.visibility = View.GONE
        }
        dimensionTypeAdapter.filter(filteredList)
    }

    // Current Location
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val task: Task<Location> = client.lastLocation
        task.addOnSuccessListener(object : OnSuccessListener<Location> {
            override fun onSuccess(location: Location?) {
                supportMapFragment.getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(map: GoogleMap) {
//                        map.setOnMarkerDragListener(this@ApnaNewSurveyActivity);
                        this@ApnaNewSurveyActivity.map = map
                        val latLang = LatLng(location!!.latitude, location.longitude)
                        activityApnaNewSurveyBinding.latitude.setText(location.latitude.toString())
                        activityApnaNewSurveyBinding.longitude.setText(location.longitude.toString())
                        val markerOption =
                            MarkerOptions().position(latLang).title("")
                        map.addMarker(markerOption)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 10F))
                    }
                })
            }
        })
    }

    private fun filterCityList(searchText: String, dialogCityListBinding: DialogCityListBinding?) {
        val filteredList = ArrayList<CityListResponse.Data.ListData.Row>()
        for (i in cityList.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(cityList)
            } else {
                if (cityList[i].name!!.contains(searchText, true)) {
                    filteredList.add(cityList[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogCityListBinding!!.cityRcv.visibility = View.GONE
            dialogCityListBinding.cityAvailable.visibility = View.VISIBLE
        } else {
            dialogCityListBinding!!.cityRcv.visibility = View.VISIBLE
            dialogCityListBinding.cityAvailable.visibility = View.GONE
        }
        cityItemAdapter.filter(filteredList)
    }

    private fun filterStateList(
        searchText: String,
        dialogStateListBinding: DialogStateListBinding?,
    ) {
        val filteredList = ArrayList<StateListResponse.Data.ListData.Row>()
        for (i in stateList.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(stateList)
            } else {
                if (stateList[i].name!!.contains(searchText, true)) {
                    filteredList.add(stateList[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogStateListBinding!!.stateRcv.visibility = View.GONE
            dialogStateListBinding.stateAvailable.visibility = View.VISIBLE
        } else {
            dialogStateListBinding!!.stateRcv.visibility = View.VISIBLE
            dialogStateListBinding.stateAvailable.visibility = View.GONE
        }
        stateItemAdapter.filter(filteredList)
    }

    private fun filterLocationList(
        searchText: String,
        dialogLocationListBinding: DialogLocationListBinding?,
    ) {
        val filteredList = ArrayList<LocationListResponse.Data.ListData.Row>()
        for (i in locationList.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(locationList)
            } else {
                if (locationList[i].name!!.contains(searchText, true)) {
                    filteredList.add(locationList[i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogLocationListBinding!!.locationRcv.visibility = View.GONE
            dialogLocationListBinding.locationAvailable.visibility = View.VISIBLE
        } else {
            dialogLocationListBinding!!.locationRcv.visibility = View.VISIBLE
            dialogLocationListBinding.locationAvailable.visibility = View.GONE
        }
        locationListItemAdapter.filter(filteredList)
    }

    private fun updateTotalArea(length: Double, width: Double) {
        activityApnaNewSurveyBinding.totalAreaText.setText(String.format("%.2f", (length * width)))
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
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)//context.cacheDir

        videoFile =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString(), "${System.currentTimeMillis()}.mp4"
            )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile))
        } else {
            val videoUri =
                FileProvider.getUriForFile(context, context.packageName + ".provider", videoFile!!)
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
            val photoUri =
                FileProvider.getUriForFile(context, context.packageName + ".provider", imageFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && imageFile != null) {
            imageFileList.add(imageFile!!)
            //  imageList.add(ImageDto(imageFile!!, ""))
            activityApnaNewSurveyBinding.totalSitePhoto.setText(imageFileList.size.toString())
            imageAdapter.notifyDataSetChanged()
        } else if (requestCode == REQUEST_CODE_VIDEO && resultCode == Activity.RESULT_OK && videoFile != null) {
            activityApnaNewSurveyBinding.beforeCaptureLayout.visibility = View.GONE
            activityApnaNewSurveyBinding.afterCaptureLayout.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.deleteVideo.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.playVideo.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.afterCapturedVideo.setVideoURI(Uri.parse(videoFile!!.absolutePath))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE

                this.currentPosition = 0
            }

            1 -> {

                activityApnaNewSurveyBinding.scrollView.post(Runnable {
                    activityApnaNewSurveyBinding.scrollView.scrollTo(0, 0)
                })

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

                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE

                isLocationDetailsCompleted = true
                isSiteSpecificationsInProgress = true

                activityApnaNewSurveyBinding.locationDetailsNum.visibility = View.GONE
                activityApnaNewSurveyBinding.locationDetailsText.visibility = View.GONE

                activityApnaNewSurveyBinding.locationDetailsNumCompleted.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsTextCompleted.visibility = View.VISIBLE


                activityApnaNewSurveyBinding.locationDetailsExpand.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );


                surveyCreateRequest.location2 =
                    activityApnaNewSurveyBinding.locationText.text.toString().trim()
                surveyCreateRequest.state2 =
                    activityApnaNewSurveyBinding.stateText.text.toString().trim()
                surveyCreateRequest.city2 =
                    activityApnaNewSurveyBinding.cityText.text.toString().trim()

                surveyCreateRequest.address =
                    activityApnaNewSurveyBinding.locationText.text.toString().trim()
                val location = SurveyCreateRequest.Location__1()
                location.uid = location_uid
                surveyCreateRequest.location = location

                val state = SurveyCreateRequest.State()
                state.uid = state_uid
                surveyCreateRequest.state = state

                val city = SurveyCreateRequest.City()
                city.uid = city_uid
                surveyCreateRequest.city = city

                surveyCreateRequest.pincode =
                    activityApnaNewSurveyBinding.pinText.text.toString().trim()
                surveyCreateRequest.landmarks =
                    activityApnaNewSurveyBinding.nearByLandmarksText.text.toString().trim()
                surveyCreateRequest.lat =
                    activityApnaNewSurveyBinding.latitude.text.toString().trim()
                surveyCreateRequest.long =
                    activityApnaNewSurveyBinding.longitude.text.toString().trim()

                this.currentPosition = 1
            }

            2 -> {

                activityApnaNewSurveyBinding.scrollView.post(Runnable {
                    activityApnaNewSurveyBinding.scrollView.scrollTo(0, 0)
                })

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

                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE

                isSiteSpecificationsCompleted = true
                isMarketInformationInProgress = true

                activityApnaNewSurveyBinding.siteSpecificationsNum.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationsText.visibility = View.GONE

                activityApnaNewSurveyBinding.siteSpecificationsNumCompleted.visibility =
                    View.VISIBLE
                activityApnaNewSurveyBinding.siteSpecificationsTextCompleted.visibility =
                    View.VISIBLE

                activityApnaNewSurveyBinding.siteSpecificationsExpand.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );

                surveyCreateRequest.dimensionType1 =
                    activityApnaNewSurveyBinding.dimensionTypeSelect.text.toString().trim()
                surveyCreateRequest.length =
                    activityApnaNewSurveyBinding.lengthText.text.toString().trim()
                surveyCreateRequest.width =
                    activityApnaNewSurveyBinding.widthText.text.toString().trim()
                surveyCreateRequest.ceilingHeight =
                    activityApnaNewSurveyBinding.ceilingHeightText.text.toString().trim()

                if (activityApnaNewSurveyBinding.totalAreaText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.totalArea =
                        activityApnaNewSurveyBinding.totalAreaText.text.toString().trim().toFloat()
                }
                val expectedRentRadioGroupId =
                    activityApnaNewSurveyBinding.expectedRentRadioGroup.checkedRadioButtonId
                val dimensionType = SurveyCreateRequest.DimensionType()
                dimensionType.uid =
                    findViewById<RadioButton>(expectedRentRadioGroupId).text.toString().trim()
                surveyCreateRequest.dimensionType = dimensionType
                if (activityApnaNewSurveyBinding.expectedRentText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.expectedRent =
                        activityApnaNewSurveyBinding.expectedRentText.text.toString().trim().toInt()
                }

                if (activityApnaNewSurveyBinding.securityDepositText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.securityDeposit =
                        activityApnaNewSurveyBinding.securityDepositText.text.toString().trim()
                            .toInt()
                }
                if (activityApnaNewSurveyBinding.toiletsAvailabilityRadioGroup.checkedRadioButtonId != -1) {
                    val toiletsAvailabilityRadioGroupId =
                        activityApnaNewSurveyBinding.toiletsAvailabilityRadioGroup.checkedRadioButtonId
                    val toiletsAvailability = SurveyCreateRequest.ToiletsAvailability()
                    toiletsAvailability.uid =
                        findViewById<RadioButton>(toiletsAvailabilityRadioGroupId).text.toString()
                            .trim()
                    surveyCreateRequest.toiletsAvailability = toiletsAvailability
                }
                surveyCreateRequest.buildingAge =
                    activityApnaNewSurveyBinding.ageOfTheBuildingText.text.toString().trim()

                val parkingRadioGroupId =
                    activityApnaNewSurveyBinding.parkingRadioGroup.checkedRadioButtonId
                val parking = SurveyCreateRequest.Parking()
                parking.uid = findViewById<RadioButton>(parkingRadioGroupId).text.toString().trim()
                surveyCreateRequest.parking = parking

                val trafficStreetType = SurveyCreateRequest.TrafficStreetType()
                trafficStreetType.uid =
                    activityApnaNewSurveyBinding.trafficStreetSelect.text.toString().trim()
                surveyCreateRequest.trafficStreetType = trafficStreetType

                if (activityApnaNewSurveyBinding.morningFromSelect.text.toString().isNotEmpty()) {
                    val morningFrom = LocalTime.parse(
                        activityApnaNewSurveyBinding.morningFromSelect.text.toString(),
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                    surveyCreateRequest.morningFrom =
                        morningFrom.format(DateTimeFormatter.ofPattern("HH:m:ss"))
                }

                if (activityApnaNewSurveyBinding.morningToSelect.text.toString().isNotEmpty()) {
                    val morningTo = LocalTime.parse(
                        activityApnaNewSurveyBinding.morningToSelect.text.toString(),
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                    surveyCreateRequest.morningTo =
                        morningTo.format(DateTimeFormatter.ofPattern("HH:m:ss"))
                }

                if (activityApnaNewSurveyBinding.eveningFromSelect.text.toString().isNotEmpty()) {
                    val eveningFrom = LocalTime.parse(
                        activityApnaNewSurveyBinding.eveningFromSelect.text.toString(),
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                    surveyCreateRequest.eveningFrom =
                        eveningFrom.format(DateTimeFormatter.ofPattern("HH:m:ss"))
                }

                if (activityApnaNewSurveyBinding.eveningToSelect.text.toString().isNotEmpty()) {
                    val eveningTo = LocalTime.parse(
                        activityApnaNewSurveyBinding.eveningToSelect.text.toString(),
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                    surveyCreateRequest.eveningTo =
                        eveningTo.format(DateTimeFormatter.ofPattern("HH:m:ss"))
                }

                surveyCreateRequest.trafficPatterns =
                    activityApnaNewSurveyBinding.presentTrafficPatterns.text.toString().trim()


                this.currentPosition = 2
            }

            3 -> {

                activityApnaNewSurveyBinding.scrollView.post(Runnable {
                    activityApnaNewSurveyBinding.scrollView.scrollTo(0, 0)
                })

                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE

                isMarketInformationCompleted = true
                isMarketInformationInProgress = false
                isCompetitorsDetailsInProgress = true

                activityApnaNewSurveyBinding.marketInformationNum.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationText.visibility = View.GONE

                activityApnaNewSurveyBinding.marketInformationNumCompleted.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.marketInformationTextCompleted.visibility =
                    View.VISIBLE

                activityApnaNewSurveyBinding.marketInformationExpand.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );


                surveyCreateRequest.extngOutletName =
                    activityApnaNewSurveyBinding.existingOutletName.text.toString().trim()

                if (activityApnaNewSurveyBinding.ageOrSaleText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.extngOutletAge =
                        activityApnaNewSurveyBinding.ageOrSaleText.text.toString().trim().toFloat()
                }

                if (activityApnaNewSurveyBinding.pharmaText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.csPharma =
                        activityApnaNewSurveyBinding.pharmaText.text.toString().substring(
                            0, activityApnaNewSurveyBinding.pharmaText.text.toString().length - 1
                        ).trim().toFloat()
                }

                if (activityApnaNewSurveyBinding.fmcgText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.csFmcg =
                        activityApnaNewSurveyBinding.fmcgText.text.toString().substring(
                            0, activityApnaNewSurveyBinding.fmcgText.text.toString().length - 1
                        ).trim().toFloat()
                }

                if (activityApnaNewSurveyBinding.surgicalsText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.csSurgicals =
                        activityApnaNewSurveyBinding.surgicalsText.text.toString().substring(
                            0, activityApnaNewSurveyBinding.surgicalsText.text.toString().length - 1
                        ).trim().toFloat()
                }

                if (activityApnaNewSurveyBinding.areaDiscountText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.areaDiscount =
                        activityApnaNewSurveyBinding.areaDiscountText.text.toString().substring(
                            0,
                            activityApnaNewSurveyBinding.areaDiscountText.text.toString().length - 1
                        ).trim().toFloat()
                }

                val neighbouringStores = ArrayList<SurveyCreateRequest.NeighboringStore>()
                for (i in neighbouringStoreList.indices) {
                    val neighboringStore = SurveyCreateRequest.NeighboringStore()
                    val location = SurveyCreateRequest.NeighboringStore.Location()
                    location.uid = neighbouringStoreList.get(i).location
                    neighboringStore.location = location
                    neighboringStore.store = neighbouringStoreList.get(i).store
                    if (neighbouringStoreList[i].rent.isNotEmpty() && neighbouringStoreList[i].sales.isNotEmpty() && neighbouringStoreList[i].sqFt.isNotEmpty()) {
                        neighboringStore.sales = neighbouringStoreList[i].sales.toFloat()
                        neighboringStore.sqft = neighbouringStoreList[i].sqFt.toFloat()
                        neighboringStore.rent = neighbouringStoreList[i].rent.toInt()
                    }
                    neighbouringStores.add(neighboringStore)
                }
                surveyCreateRequest.neighboringStore = neighbouringStores

                surveyCreateRequest.localDisbtsComments =
                    activityApnaNewSurveyBinding.distributorsComments.text.toString().trim()

                surveyCreateRequest.occupation =
                    activityApnaNewSurveyBinding.occupationText.text.toString().trim()

                if (activityApnaNewSurveyBinding.serviceClassText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.serviceClass =
                        activityApnaNewSurveyBinding.serviceClassText.text.toString().trim()
                            .toFloat()
                }

                if (activityApnaNewSurveyBinding.businessClassText.text.toString().isNotEmpty()) {
                    surveyCreateRequest.businessClass =
                        activityApnaNewSurveyBinding.businessClassText.text.toString().trim()
                            .toFloat()
                }

                val trafficGenerators = ArrayList<SurveyCreateRequest.TrafficGenerator>()
                for (i in selectedTrafficGeneratorItem.indices) {
                    val trafficGenerator = SurveyCreateRequest.TrafficGenerator()
                    trafficGenerator.uid = selectedTrafficGeneratorItem[i]
                    trafficGenerators.add(trafficGenerator)
                }
                surveyCreateRequest.trafficGenerator = trafficGenerators


                this.currentPosition = 3
            }

            4 -> {

                activityApnaNewSurveyBinding.scrollView.post(Runnable {
                    activityApnaNewSurveyBinding.scrollView.scrollTo(0, 0)
                })

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

                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE

                isCompetitorsDetailsCompleted = true
                isCompetitorsDetailsInProgress = false
                isPopulationAndHousesInProgress = true

                activityApnaNewSurveyBinding.competitorsDetailsText.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsDetailsNum.visibility = View.GONE

                activityApnaNewSurveyBinding.competitorsDetailsNumCompleted.visibility =
                    View.VISIBLE
                activityApnaNewSurveyBinding.competitorsDetailsTextCompleted.visibility =
                    View.VISIBLE

                activityApnaNewSurveyBinding.competitorsDetailsExpand.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );


                val chemist = ArrayList<SurveyCreateRequest.Chemist>()
                for (i in chemistList.indices) {
                    val chemistData = SurveyCreateRequest.Chemist()
                    val organised = SurveyCreateRequest.Chemist.Organised()
                    val unorganised = SurveyCreateRequest.Chemist.Unorganised()
                    chemistData.chemist = chemistList[i].chemist
                    organised.uid = chemistList[i].organised
                    chemistData.organised = organised
                    unorganised.uid = chemistList[i].unorganised
                    chemistData.unorganised = unorganised
                    chemistData.orgAvgSale = chemistList[i].organisedAvgSale.toInt()
                    chemistData.unorgAvgSale = chemistList[i].unorganisedAvgSale.toInt()
                    chemist.add(chemistData)
                }
                surveyCreateRequest.chemist = chemist

                this.currentPosition = 4
            }

            5 -> {

                activityApnaNewSurveyBinding.scrollView.post(Runnable {
                    activityApnaNewSurveyBinding.scrollView.scrollTo(0, 0)
                })

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

                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.next.visibility = View.GONE

                isPopulationAndHousesCompleted = true
                isPopulationAndHousesInProgress = false
                isHospitalsInProgress = true

                activityApnaNewSurveyBinding.populationAndHousesNum.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesText.visibility = View.GONE

                activityApnaNewSurveyBinding.populationAndHousesNumCompleted.visibility =
                    View.VISIBLE
                activityApnaNewSurveyBinding.populationAndHousesTextCompleted.visibility =
                    View.VISIBLE

                activityApnaNewSurveyBinding.populationAndHousesExpand.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );


                val apartments = ArrayList<SurveyCreateRequest.Apartment>()
                for (i in apartmentsList.indices) {
                    val apartment = SurveyCreateRequest.Apartment()
                    val type = SurveyCreateRequest.Apartment.Type()
                    type.uid = apartmentsList[i].apartmentType
                    apartment.type = type
                    apartment.apartments = apartmentsList[i].apartments
                    apartment.noHouses = apartmentsList[i].noOfHouses.toString()
                    apartment.distance = apartmentsList[i].distance
                    apartments.add(apartment)
                }
                surveyCreateRequest.apartments = apartments

                this.currentPosition = 5
            }

            6 -> {

                activityApnaNewSurveyBinding.scrollView.post(Runnable {
                    activityApnaNewSurveyBinding.scrollView.scrollTo(0, 0)
                })

                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previousSubmitBtn.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.next.visibility = View.GONE

                isHospitalsCompleted = true
                isHospitalsInProgress = false
                isPhotosAndMediaCompleted = true
//                isPhotosAndMediaInProgress = true

                activityApnaNewSurveyBinding.hospitalsText.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsNum.visibility = View.GONE

                activityApnaNewSurveyBinding.hospitalsNumCompleted.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.hospitalsTextCompleted.visibility = View.VISIBLE

                activityApnaNewSurveyBinding.hospitalsExpand.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                );


                val hospitals = ArrayList<SurveyCreateRequest.Hospital>()
                for (i in hospitalsList.indices) {
                    val hospital = SurveyCreateRequest.Hospital()
                    val speciality = SurveyCreateRequest.Hospital.Speciality()
                    speciality.uid = hospitalsList[i].speciality
                    hospital.speciality = speciality
                    hospital.hospitals = hospitalsList[i].hospitalName
                    hospital.beds = hospitalsList[i].beds.toString()
                    hospital.noOpd = hospitalsList[i].noOfOpd.toString()
                    hospital.occupancy = hospitalsList[i].occupancy.toString()
                        .substring(0, hospitalsList[i].occupancy.toString().length)
                    hospitals.add(hospital)
                }
                surveyCreateRequest.hospitals = hospitals

                this.currentPosition = 6
            }

            else -> {
            }
        }
    }

    private fun validateSiteSpecification(): Boolean {
        val dimensionType = activityApnaNewSurveyBinding.dimensionTypeSelect.text.toString().trim()
        val length = activityApnaNewSurveyBinding.lengthText.text.toString().trim()
        val width = activityApnaNewSurveyBinding.widthText.text.toString().trim()
        val ceilingHeight = activityApnaNewSurveyBinding.ceilingHeightText.text.toString().trim()
        val expectedRent = activityApnaNewSurveyBinding.expectedRentText.text.toString().trim()
        val securityDeposit =
            activityApnaNewSurveyBinding.securityDepositText.text.toString().trim()

        if (length.isEmpty()) {
            return false
        } else if (width.isEmpty()) {
            return false
        } else if (ceilingHeight.isEmpty()) {
            return false
        } else if (expectedRent.isEmpty()) {
            return false
        } else if (securityDeposit.isEmpty()) {
            return false
        } else if (dimensionType.isEmpty()) {
            return false
        }
        return true
    }

    private fun validateLocationDetails(): Boolean {
        val location = activityApnaNewSurveyBinding.locationText.text.toString().trim()
        val city = activityApnaNewSurveyBinding.cityText.text.toString().trim()
        val state = activityApnaNewSurveyBinding.stateText.text.toString().trim()
        val pin = activityApnaNewSurveyBinding.pinText.text.toString().trim()
        val landmarks = activityApnaNewSurveyBinding.nearByLandmarksText.text.toString().trim()

        if (location.isEmpty()) {
            errorMessage = "Please fill all mandatory fields"
            return false
        } else if (city.isEmpty()) {
            errorMessage = "Please fill all mandatory fields"
            return false
        } else if (state.isEmpty()) {
            errorMessage = "Please fill all mandatory fields"
            return false
        } else if (pin.isEmpty() || pin.length < 6) {
            errorMessage = "Please enter valid pincode"
            return false
        } else if (landmarks.isEmpty()) {
            errorMessage = "Please fill all mandatory fields"
            return false
        } else {
            errorMessage = "Please fill all mandatory fields"
            return true
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

    override fun onCityItemSelect(position: Int, item: String, uid: String) {
        city_uid = uid
        activityApnaNewSurveyBinding.cityText.setText(item)
        cityListDialog.dismiss()
    }

    override fun onOrganisedItemSelect(position: Int, item: String) {
        activityApnaNewSurveyBinding.organisedSelect.setText(item)
        organisedDialog.dismiss()
    }

    override fun onUnorganisedItemSelect(position: Int, item: String) {
        activityApnaNewSurveyBinding.unorganisedSelect.setText(item)
        unorganisedDialog.dismiss()
    }

    var selectedState = ""
    override fun onSelectState(position: Int, item: String, uid: String) {
        state_uid = uid
        activityApnaNewSurveyBinding.stateText.setText(item)
        stateListDialog.dismiss()

        if (selectedState != item) {
            selectedState = item
            activityApnaNewSurveyBinding.cityText.getText()!!.clear()
            Utlis.showLoading(this@ApnaNewSurveyActivity)
            apnaNewSurveyViewModel.getCityList(this@ApnaNewSurveyActivity, uid)
        }
//        Utlis.showLoading(this@ApnaNewSurveyActivity)
//        apnaNewSurveyViewModel.getCityList(this@ApnaNewSurveyActivity, uid)
    }

    override fun onLocationListItemSelect(position: Int, item: String, uid: String) {
        location_uid = uid
        activityApnaNewSurveyBinding.locationText.setText(item)
        locationListDialog.dismiss()
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

    override fun onClickTrafficGeneratorItemDelete(position: Int, deletedItem: String) {
        for (i in trafficGeneratorData.indices) {
            if (trafficGeneratorData[i].name.equals(deletedItem, true)) {
                trafficGeneratorData[i].isSelected = false
            }
        }
        selectedTrafficGeneratorItem.removeAt(position)
        trafficGeneratorsItemAdapter.notifyDataSetChanged()
        trafficGeneratorAdapter.notifyDataSetChanged()
    }

    override fun onClickNeighbouringStoreDelete(position: Int) {
        neighbouringStoreList.removeAt(position)
        neighbouringStoreAdapter.notifyDataSetChanged()
    }

    override fun onTrafficGeneratorItemSelect(position: Int, item: String, selected: Boolean?) {

        if (selected == true) {
            selectedTrafficGeneratorItem.add(item)
        } else if (selected == false) {
            selectedTrafficGeneratorItem.remove(item)
        }
    }

    override fun deleteSiteImage(position: Int, file: File) {
        imageFileList.removeAt(position)
        activityApnaNewSurveyBinding.totalSitePhoto.setText(imageFileList.size.toString())
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
        Glide.with(this@ApnaNewSurveyActivity).load(imageFile)
            .placeholder(R.drawable.placeholder_image).into(previewImageDialogBinding.previewImage)
        previewImageDialogBinding.close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onSuccessGetLocationListApiCall(locationListResponse: LocationListResponse) {
        Utlis.hideLoading()
        if (locationListResponse != null && locationListResponse.data != null && locationListResponse.data!!.listData!!.rows!!.size > 0) {
            locationList =
                locationListResponse.data!!.listData!!.rows as ArrayList<LocationListResponse.Data.ListData.Row>
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

//            val neighbouringStoreAdapter = NeighbouringStoreAdapter(
//                this@ApnaNewSurveyActivity, this@ApnaNewSurveyActivity, neighbouringLocationList
//            )
//            activityApnaNewSurveyBinding.neighbouringStoreRcv.adapter = neighbouringStoreAdapter
//            activityApnaNewSurveyBinding.neighbouringStoreRcv.layoutManager =
//                LinearLayoutManager(this@ApnaNewSurveyActivity)
        }
    }

    override fun onFailureGetNeighbouringLocationApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }


//    override fun onDataChanged(neighbouringList: ArrayList<NeighbouringLocationResponse.Data.ListData.Row>) {
//        neighbouringStoreList = neighbouringList
//    }

    override fun onSuccessConnectToAzure(images: ArrayList<ImageDto>) {
        imageUrls = images
    }

    override fun onFailureConnectToAzure(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetStateListApiCall(stateListResponse: StateListResponse) {
        Utlis.hideLoading()
        if (stateListResponse != null && stateListResponse.data!!.listData != null && stateListResponse.data!!.listData!!.rows!!.size > 0) {
            stateList =
                stateListResponse.data!!.listData!!.rows as ArrayList<StateListResponse.Data.ListData.Row>
        }
    }

    override fun onFailureGetStateListApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetCityListApiCall(cityListResponse: CityListResponse) {
        Utlis.hideLoading()
        if (cityListResponse != null && cityListResponse.data!!.listData != null && cityListResponse.data!!.listData!!.rows!!.size > 0) {
            cityList =
                cityListResponse.data!!.listData!!.rows as ArrayList<CityListResponse.Data.ListData.Row>
        }
    }

    override fun onFailureGetCityListApiCall(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessSurveyCreateApiCall(surveyCreateResponse: SurveyCreateResponse) {
        Utlis.hideLoading()
        if (surveyCreateResponse.success!!) {
            val dialog = Dialog(this@ApnaNewSurveyActivity)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val surveySavedConfirmDialogBinding =
                DataBindingUtil.inflate<SurveySavedConfirmDialogBinding>(
                    LayoutInflater.from(this@ApnaNewSurveyActivity),
                    R.layout.survey_saved_confirm_dialog,
                    null,
                    false
                )
            dialog.setContentView(surveySavedConfirmDialogBinding.root)
            surveySavedConfirmDialogBinding.uid.text =
                "Transaction Id Is: " + surveyCreateResponse.data!!.uid
            surveySavedConfirmDialogBinding.message.text = surveyCreateResponse.message
            surveySavedConfirmDialogBinding.okButton.setOnClickListener {
                dialog.dismiss()
                finish()
            }
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    override fun onFailureSurveyCreateApiCall(message: String) {
        Utlis.hideLoading()
        val dialog = Dialog(this@ApnaNewSurveyActivity)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val surveyFailedDialogBinding = DataBindingUtil.inflate<SurveyFailedDialogBinding>(
            LayoutInflater.from(this@ApnaNewSurveyActivity),
            R.layout.survey_failed_dialog,
            null,
            false
        )
        surveyFailedDialogBinding.errorMessage.text = message
        surveyFailedDialogBinding.okButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onSuccessImagesConnectAzure(
        imageUrlList: List<SurveyCreateRequest.SiteImageMb.Image>,
        surveyCreateRequest: SurveyCreateRequest,
    ) {
        var siteImageMb = SurveyCreateRequest.SiteImageMb()
        siteImageMb.images = imageUrlList
        surveyCreateRequest.siteImageMb = siteImageMb
        if (videoFile != null) {
            var videoFileList = ArrayList<File>()
            videoFileList.add(videoFile!!)
            apnaNewSurveyViewModel.videoListConnectToAzure(
                videoFileList, this@ApnaNewSurveyActivity, surveyCreateRequest
            )
        } else {
            apnaNewSurveyViewModel.createSurvey(
                surveyCreateRequest, this@ApnaNewSurveyActivity
            )
        }
    }

    override fun onFailureImageConnectAzure(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, "$message", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessVideoConnectAzure(
        videoUrlList: List<SurveyCreateRequest.VideoMb.Video>,
        surveyCreateRequest: SurveyCreateRequest,
    ) {
        var videoMb = SurveyCreateRequest.VideoMb()
        videoMb.video = videoUrlList
        surveyCreateRequest.videoMb = videoMb
        apnaNewSurveyViewModel.createSurvey(
            surveyCreateRequest, this@ApnaNewSurveyActivity
        )
    }

    override fun onFailureVideoConnectAzure(message: String) {
        Toast.makeText(this@ApnaNewSurveyActivity, "$message", Toast.LENGTH_SHORT).show()
    }

    override fun onSelectDimensionTypeItem(position: Int, item: String) {
        activityApnaNewSurveyBinding.dimensionTypeSelect.setText(item)
        dimensionTypeDialog.dismiss()
    }

    override fun onSelectNeighbourLocation(position: Int, item: String) {
        activityApnaNewSurveyBinding.neighbourLocationSelect.setText(item)
        neighbouringLocationDialog.dismiss()
    }

    override fun onBackPressed() {
        RESULT_OK
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onMarkerDrag(p0: Marker) {
    }

    override fun onMarkerDragEnd(p0: Marker) {
        if (map != null) {
            map!!.clear()
            val latLang = LatLng(p0!!.position.latitude, p0!!.position.latitude)
            activityApnaNewSurveyBinding.latitude.setText(latLang.latitude.toString())
            activityApnaNewSurveyBinding.longitude.setText(latLang.longitude.toString())
            val markerOption = MarkerOptions().position(latLang).title("").draggable(true)
            map!!.addMarker(markerOption)
            map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 10F))
        }
    }

    override fun onMarkerDragStart(p0: Marker) {
    }
}