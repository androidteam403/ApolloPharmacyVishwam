package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaSurveyPreviewBinding
import com.apollopharmacy.vishwam.databinding.ApnaPreviewQuickGoDialogBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.XYMarkerView
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.stream.Collectors

class ApnaSurveyPreviewActivity : AppCompatActivity(), ApnaSurveyPreviewCallback {

    var toiletsAvailable: String = ""
    var parkingAvailable: String = ""
    var trafficType: String = ""

    lateinit var activityApnaSurveyPreviewBinding: ActivityApnaSurveyPreviewBinding
    var surveyCreateRequest = SurveyCreateRequest()
    var trafficGenerators = ArrayList<SurveyCreateRequest.TrafficGenerator>()
    var neighboringStores = ArrayList<SurveyCreateRequest.NeighboringStore>()
    var chemist = ArrayList<SurveyCreateRequest.Chemist>()
    var apartments = ArrayList<SurveyCreateRequest.Apartment>()
    var hospitals = ArrayList<SurveyCreateRequest.Hospital>()
    var images = ArrayList<SurveyCreateRequest.SiteImageMb.Image>()
    var videos = ArrayList<SurveyCreateRequest.VideoMb.Video>()

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var client: FusedLocationProviderClient

    var neighborEntries = ArrayList<BarEntry>()
    var competitorsEntries = ArrayList<Entry>()

    //    var apartmentsEntryOne = ArrayList<BarEntry>()
    var apartmentsEntries = ArrayList<BarEntry>()
    var hospitalsEntries = ArrayList<BarEntry>()

    //    var hospitalsEntryTwo = ArrayList<BarEntry>()
    var sales = ArrayList<Float>()
    var stores = ArrayList<String>()
    var avgSales = ArrayList<Float>()
    var chemists = ArrayList<String>()
    var noOfHouses = ArrayList<Float>()
    var apartmentNames = ArrayList<String>()
    var beds = ArrayList<Float>()
    var hospitalNames = ArrayList<String>()
    var stringValuesList = java.util.ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityApnaSurveyPreviewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_apna_survey_preview)
        setUp()
    }

    @SuppressLint("SetTextI18n")
    private fun setUp() {

        if (intent != null) {
            surveyCreateRequest =
                intent.getSerializableExtra("SURVEY_REQUEST") as SurveyCreateRequest
        }

        activityApnaSurveyPreviewBinding.backArrow.setOnClickListener {
            finish()
        }
        activityApnaSurveyPreviewBinding.scrollTop.setOnClickListener {
            activityApnaSurveyPreviewBinding.scrollView.fullScroll(View.FOCUS_UP)
        }
        activityApnaSurveyPreviewBinding.quickGoIcon.setOnClickListener {
            val apnaPreviewQuickGoDialogBinding: ApnaPreviewQuickGoDialogBinding? =
                DataBindingUtil.inflate(
                    LayoutInflater.from(this), R.layout.apna_preview_quick_go_dialog, null, false
                )
            val customDialog = android.app.AlertDialog.Builder(this, 0).create()
            customDialog.apply {

                setView(apnaPreviewQuickGoDialogBinding?.root)
                setCancelable(false)
                apnaPreviewQuickGoDialogBinding!!.close.setOnClickListener {
                    dismiss()
                }
                apnaPreviewQuickGoDialogBinding.locationDetails.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(activityApnaSurveyPreviewBinding.scrollView,
                        activityApnaSurveyPreviewBinding.locationDetailsLayout)
                }
                apnaPreviewQuickGoDialogBinding.siteSpecification.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(activityApnaSurveyPreviewBinding.scrollView,
                        activityApnaSurveyPreviewBinding.siteSpecificationsLayout)
                }
                apnaPreviewQuickGoDialogBinding.marketInformation.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(activityApnaSurveyPreviewBinding.scrollView,
                        activityApnaSurveyPreviewBinding.marketInformationLayout)
                }
                apnaPreviewQuickGoDialogBinding.competitorsDetails.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(activityApnaSurveyPreviewBinding.scrollView,
                        activityApnaSurveyPreviewBinding.competitorsDetailsLayout)
                }
                apnaPreviewQuickGoDialogBinding.populationAndHouses.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(activityApnaSurveyPreviewBinding.scrollView,
                        activityApnaSurveyPreviewBinding.populationAndHousesLayout)
                }
                apnaPreviewQuickGoDialogBinding.hospitals.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(activityApnaSurveyPreviewBinding.scrollView,
                        activityApnaSurveyPreviewBinding.hospitalsLayout)
                }
                apnaPreviewQuickGoDialogBinding.photosAndMedia.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(activityApnaSurveyPreviewBinding.scrollView,
                        activityApnaSurveyPreviewBinding.photosAndMediaLayout)
                }
            }.show()
        }

        // Neighboring store graph
//        setNeighborChartValues()
//        setupNeighborChart()

        // Competitors Details graph
//        setCompetitorsValues()
//        setupCompetitorsChart()

        // Apartments graph
//        setApartmentsValuesOne()
//        setApartmentsValuesTwo()
//        setupApartmentsChart()

        // Hospitals graph
//        setHospitalsValuesOne()
//        setHospitalsValues()
//        setupHospitalsChart()

        // Location Details
        val lat = surveyCreateRequest.lat
        val long = surveyCreateRequest.long
        var region = ""
        var landMarks = ""
        var city = ""
        var state = ""
        var pin = ""

//        if (surveyCreateRequest.location2 != null) {
//            location = surveyCreateRequest.location2.toString()
//        } else {
//            location = "-"
//        }
        if (surveyCreateRequest.region != null) {
            if (surveyCreateRequest.region!!.name != null) {
                if (surveyCreateRequest.region!!.name!!.toString().isNotEmpty()) {
                    region = surveyCreateRequest.region!!.name!!.toString()
                } else {
                    region = "-"
                }
            } else {
                region = "-"
            }
        } else {
            region = "-"
        }
        if (surveyCreateRequest.landmarks != null) {
            landMarks = surveyCreateRequest.landmarks.toString()
        } else {
            landMarks = "-"
        }
        if (surveyCreateRequest.city != null) {
            city = surveyCreateRequest.city.toString()
        } else {
            city = "-"
        }
        if (surveyCreateRequest.state != null) {
            state = surveyCreateRequest.state.toString()
        } else {
            state = "-"
        }
        if (surveyCreateRequest.pincode != null) {
            pin = surveyCreateRequest.pincode.toString()
        } else {
            pin = "-"
        }

        activityApnaSurveyPreviewBinding.locationDetails.setText(
            "$region,$landMarks,$city,$state-$pin"
        )
//        if (lat != null) {
//            activityApnaSurveyPreviewBinding.lattitude.setText(lat)
//        } else {
//            activityApnaSurveyPreviewBinding.lattitude.setText("-")
//        }
//        if (long != null) {
//            activityApnaSurveyPreviewBinding.longitude.setText(long)
//        } else {
//            activityApnaSurveyPreviewBinding.longitude.setText("-")
//        }

        supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(this@ApnaSurveyPreviewActivity)

        Dexter.withActivity(this@ApnaSurveyPreviewActivity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    getCurrentLocation(lat, long)
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

        // Site Specifications
//        activityApnaSurveyPreviewBinding.dimensionTypeSelect.setText(surveyCreateRequest.dimensionType1)
        if (surveyCreateRequest.dimensionType != null && surveyCreateRequest.dimensionType!!.name != null) {
            activityApnaSurveyPreviewBinding.dimensionType.setText("(" + surveyCreateRequest.dimensionType!!.name + "): ")
            activityApnaSurveyPreviewBinding.totalAreaDimensionType.setText("(" + surveyCreateRequest.dimensionType!!.name + "): ")
            activityApnaSurveyPreviewBinding.expectedRentUnit.setText(surveyCreateRequest.dimensionType!!.name)
            activityApnaSurveyPreviewBinding.securityDepositUnit.setText(surveyCreateRequest.dimensionType!!.name)
        } else {
            activityApnaSurveyPreviewBinding.dimensionType.setText("(-): ")
            activityApnaSurveyPreviewBinding.totalAreaDimensionType.setText("(-): ")
            activityApnaSurveyPreviewBinding.expectedRentUnit.setText("-")
            activityApnaSurveyPreviewBinding.securityDepositUnit.setText("-")
        }
        if (surveyCreateRequest.length != null) {
            activityApnaSurveyPreviewBinding.length.setText(surveyCreateRequest.length)
        } else {
            activityApnaSurveyPreviewBinding.length.setText("-")
        }
        if (surveyCreateRequest.width != null) {
            activityApnaSurveyPreviewBinding.width.setText(surveyCreateRequest.width)
        } else {
            activityApnaSurveyPreviewBinding.width.setText("-")
        }
        if (surveyCreateRequest.ceilingHeight != null) {
            activityApnaSurveyPreviewBinding.ceilingHeight.setText(surveyCreateRequest.ceilingHeight)
        } else {
            activityApnaSurveyPreviewBinding.ceilingHeight.setText("-")
        }
        if (surveyCreateRequest.totalArea != null) {
            activityApnaSurveyPreviewBinding.totalArea.setText(surveyCreateRequest.totalArea.toString())
        } else {
            activityApnaSurveyPreviewBinding.totalArea.setText("-")
        }
        if (surveyCreateRequest.expectedRent != null) {
            val decimalFormat = DecimalFormat("##,##,##0")
            val formattedNumber =
                decimalFormat.format(surveyCreateRequest.expectedRent.toString().toLong())
            activityApnaSurveyPreviewBinding.expectedRent.setText(formattedNumber)
//            activityApnaSurveyPreviewBinding.expectedRent.setText(surveyCreateRequest.expectedRent.toString())
        } else {
            activityApnaSurveyPreviewBinding.expectedRent.setText("-")
        }

        if (surveyCreateRequest.securityDeposit != null) {
            val decimalFormat = DecimalFormat("##,##,##0")
            val formattedNumber =
                decimalFormat.format(surveyCreateRequest.securityDeposit.toString().toLong())
            activityApnaSurveyPreviewBinding.securityDeposit.setText(formattedNumber)
        } else {
            activityApnaSurveyPreviewBinding.securityDeposit.setText("-")
        }

        if (surveyCreateRequest.toiletsAvailability != null) {
            val toiletsAvailability: SurveyCreateRequest.ToiletsAvailability =
                surveyCreateRequest.toiletsAvailability!!
            if (toiletsAvailability.uid!!.isNotEmpty()) {
                toiletsAvailable = toiletsAvailability.uid.toString()
//                activityApnaSurveyPreviewBinding.toiletsAvailability.setText(toiletsAvailability.uid)
            } else {
//                activityApnaSurveyPreviewBinding.toiletsAvailability.setText("-")
            }
        } else {
//            activityApnaSurveyPreviewBinding.toiletsAvailability.setText("-")
        }

//        if (surveyCreateRequest.dimensionType != null) {
//            val dimensionType: SurveyCreateRequest.DimensionType =
//                surveyCreateRequest.dimensionType!!
//            if (dimensionType.uid!!.isNotEmpty()) {
//                if (dimensionType.uid.equals("per sq.ft", true)) {
//                    activityApnaSurveyPreviewBinding.expectedRentRadioGroup.check(R.id.perSqFtRadioButton)
//                } else {
//                    activityApnaSurveyPreviewBinding.expectedRentRadioGroup.check(R.id.inchRadioButton)
//                }
//            }
//        }

        if (surveyCreateRequest.buildingAge != null && surveyCreateRequest.buildingAge != "") {

            var ageofBuilding = surveyCreateRequest.buildingAge
            if (ageofBuilding!!.contains(".")) {
                activityApnaSurveyPreviewBinding.ageOfTheBuilding.setText(
                    "${
                        ageofBuilding.substring(
                            0,
                            ageofBuilding.indexOf(".")
                        )
                    } years ${ageofBuilding.substring(ageofBuilding.indexOf(".") + 1)} months"
                )
            } else {
                activityApnaSurveyPreviewBinding.ageOfTheBuilding.setText("${surveyCreateRequest.buildingAge} Years")
            }
        } else {
            activityApnaSurveyPreviewBinding.ageOfTheBuilding.setText("-")
        }

        if (surveyCreateRequest.parking != null) {
            val parking: SurveyCreateRequest.Parking = surveyCreateRequest.parking!!
            if (parking.uid!!.isNotEmpty()) {
                parkingAvailable = parking.uid.toString()
//                activityApnaSurveyPreviewBinding.parking.setText(parking.uid)
            } else {
//                activityApnaSurveyPreviewBinding.parking.setText("-")
            }
        } else {
//            activityApnaSurveyPreviewBinding.parking.setText("-")
        }

        if (surveyCreateRequest.trafficStreetType != null && surveyCreateRequest.trafficStreetType!!.uid != "") {
            trafficType = surveyCreateRequest.trafficStreetType!!.uid.toString()
//            activityApnaSurveyPreviewBinding.trafficStreetType.setText(surveyCreateRequest.trafficStreetType!!.uid)
        } else {
//            activityApnaSurveyPreviewBinding.trafficStreetType.setText("-")
        }

        val inputDateFormat = SimpleDateFormat("HH:mm:ss")
        val outputDateFormat = SimpleDateFormat("HH:mm")

        if (surveyCreateRequest.morningFrom != null) {
            activityApnaSurveyPreviewBinding.morningFrom.setText(
                outputDateFormat.format(inputDateFormat.parse(surveyCreateRequest.morningFrom!!)!!)
            )
        } else {
            activityApnaSurveyPreviewBinding.morningFrom.setText("-")
        }

        if (surveyCreateRequest.morningTo != null) {
            activityApnaSurveyPreviewBinding.morningTo.setText(
                outputDateFormat.format(inputDateFormat.parse(surveyCreateRequest.morningTo!!)!!)
            )
        } else {
            activityApnaSurveyPreviewBinding.morningTo.setText("-")
        }

        if (surveyCreateRequest.eveningFrom != null) {
            activityApnaSurveyPreviewBinding.eveningFrom.setText(
                outputDateFormat.format(inputDateFormat.parse(surveyCreateRequest.eveningFrom!!)!!)
            )
        } else {
            activityApnaSurveyPreviewBinding.eveningFrom.setText("-")
        }

        if (surveyCreateRequest.eveningTo != null) {
            activityApnaSurveyPreviewBinding.eveningTo.setText(
                outputDateFormat.format(inputDateFormat.parse(surveyCreateRequest.eveningTo!!)!!)
            )
        } else {
            activityApnaSurveyPreviewBinding.eveningTo.setText("-")
        }
        if (surveyCreateRequest.trafficPatterns != null && surveyCreateRequest.trafficPatterns != "") {
            activityApnaSurveyPreviewBinding.presentTrafficPatterns.setText(surveyCreateRequest.trafficPatterns)
        } else {
            activityApnaSurveyPreviewBinding.presentTrafficPatterns.setText("-")
        }

        // Market information
        if (surveyCreateRequest.eoSiteId != null && surveyCreateRequest.eoSiteId != "") {
            activityApnaSurveyPreviewBinding.existingOutletSiteId.setText(surveyCreateRequest.eoSiteId)
        } else {
            activityApnaSurveyPreviewBinding.existingOutletSiteId.setText("-")
        }

        if (surveyCreateRequest.eoSiteName != null && surveyCreateRequest.eoSiteName != "") {
            activityApnaSurveyPreviewBinding.existingOutletSiteName.setText(surveyCreateRequest.eoSiteName)
        } else {
            activityApnaSurveyPreviewBinding.existingOutletSiteName.setText("-")
        }

        if (surveyCreateRequest.extngOutletAge != null) {
            if (surveyCreateRequest.extngOutletAge!! > 0) {
                activityApnaSurveyPreviewBinding.existingOutletAge.setText(surveyCreateRequest.extngOutletAge.toString())
            } else {
                activityApnaSurveyPreviewBinding.existingOutletAge.setText("-")
            }
        } else {
            activityApnaSurveyPreviewBinding.existingOutletAge.setText("-")
        }

//        if (surveyCreateRequest.csPharma != null) {
//            activityApnaSurveyPreviewBinding.pharma.setText(surveyCreateRequest.csPharma.toString())
//        } else {
//            activityApnaSurveyPreviewBinding.pharma.setText("-")
//        }

//        if (surveyCreateRequest.csFmcg != null) {
//            activityApnaSurveyPreviewBinding.fmcg.setText(surveyCreateRequest.csFmcg.toString())
//        } else {
//            activityApnaSurveyPreviewBinding.fmcg.setText("-")
//        }

//        if (surveyCreateRequest.csSurgicals != null) {
//            activityApnaSurveyPreviewBinding.surgicals.setText(surveyCreateRequest.csSurgicals.toString())
//        } else {
//            activityApnaSurveyPreviewBinding.surgicals.setText("-")
//        }

//        if (surveyCreateRequest.areaDiscount != null) {
//            activityApnaSurveyPreviewBinding.areaDiscount.setText(surveyCreateRequest.areaDiscount.toString())
//        } else {
//            activityApnaSurveyPreviewBinding.areaDiscount.setText("-")
//        }

        if (surveyCreateRequest.neighboringStore != null) {
            if (surveyCreateRequest.neighboringStore!!.size > 0) {
                activityApnaSurveyPreviewBinding.neighborStoreHeader.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.neighbouringStoreNotFound.visibility = View.GONE
                neighboringStores =
                    surveyCreateRequest.neighboringStore as ArrayList<SurveyCreateRequest.NeighboringStore>

                val filteredList: ArrayList<SurveyCreateRequest.NeighboringStore> =
                    neighboringStores.stream().filter { i ->
                        i.store != null || i.rent != null || i.sales != null || i.sqft != null
                    }
                        .collect(Collectors.toList()) as ArrayList<SurveyCreateRequest.NeighboringStore>

                val neighbouringStorePreviewAdapter = NeighbouringStorePreviewAdapter(
                    this@ApnaSurveyPreviewActivity, filteredList
                )
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.adapter =
                    neighbouringStorePreviewAdapter
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)

                sales = neighboringStores.map { it.sales } as ArrayList<Float>
                stores = neighboringStores.map { it.store } as ArrayList<String>
                setNeighborChartValues()
                setupNeighborChart()
            } else {
//                activityApnaSurveyPreviewBinding.neighborStoreHeader.visibility = View.GONE
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.neighbouringStoreNotFound.visibility = View.VISIBLE
            }
        } else {
//            activityApnaSurveyPreviewBinding.neighborStoreHeader.visibility = View.GONE
            activityApnaSurveyPreviewBinding.neighbourRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.neighbouringStoreNotFound.visibility = View.VISIBLE
        }

        if (surveyCreateRequest.localDisbtsComments != null && surveyCreateRequest.localDisbtsComments != "") {
            activityApnaSurveyPreviewBinding.localDistributorsComment.setText(surveyCreateRequest.localDisbtsComments)
        } else {
            activityApnaSurveyPreviewBinding.localDistributorsComment.setText("-")
        }

        if (surveyCreateRequest.occupation != null && surveyCreateRequest.occupation != "") {
            activityApnaSurveyPreviewBinding.occupation.setText(surveyCreateRequest.occupation)
        } else {
            activityApnaSurveyPreviewBinding.occupation.setText("-")
        }

        if (surveyCreateRequest.serviceClass != null) {
            val decimalFormat = DecimalFormat("##,##,##0")
            val formattedNumber = decimalFormat.format(surveyCreateRequest.serviceClass!!.toLong())
            activityApnaSurveyPreviewBinding.serviceClass.setText(formattedNumber)
        } else {
            activityApnaSurveyPreviewBinding.serviceClass.setText("-")
        }

        if (surveyCreateRequest.businessClass != null) {
            val decimalFormat = DecimalFormat("##,##,##0")
            val formattedNumber = decimalFormat.format(surveyCreateRequest.businessClass!!.toLong())
            activityApnaSurveyPreviewBinding.businessClass.setText(formattedNumber)
        } else {
            activityApnaSurveyPreviewBinding.businessClass.setText("-")
        }

        if (surveyCreateRequest.trafficGenerator != null) {
            if (surveyCreateRequest.trafficGenerator!!.size > 0) {
                activityApnaSurveyPreviewBinding.trafficGeneratorRecyclerView.visibility =
                    View.VISIBLE
                activityApnaSurveyPreviewBinding.trafficGeneratorsNotFound.visibility = View.GONE
                trafficGenerators =
                    surveyCreateRequest.trafficGenerator as ArrayList<SurveyCreateRequest.TrafficGenerator>
                val trafficGeneratorPreviewAdapter = TrafficGeneratorPreviewAdapter(
                    this@ApnaSurveyPreviewActivity, trafficGenerators
                )
                activityApnaSurveyPreviewBinding.trafficGeneratorRecyclerView.adapter =
                    trafficGeneratorPreviewAdapter
                activityApnaSurveyPreviewBinding.trafficGeneratorRecyclerView.layoutManager =
                    LinearLayoutManager(
                        this, LinearLayoutManager.HORIZONTAL, false
                    )
            } else {
                activityApnaSurveyPreviewBinding.trafficGeneratorRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.trafficGeneratorsNotFound.visibility = View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.trafficGeneratorRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficGeneratorsNotFound.visibility = View.VISIBLE
        }

        // Competitors details
        if (surveyCreateRequest.chemist != null) {
            if (surveyCreateRequest.chemist!!.size > 0) {
//                activityApnaSurveyPreviewBinding.chemistHeader.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.chemistTotal.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.chemistListNotFound.visibility = View.GONE
                chemist = surveyCreateRequest.chemist as ArrayList<SurveyCreateRequest.Chemist>
                val competitorsDetailsPreviewAdapter = CompetitorsDetailsPreviewAdapter(
                    this@ApnaSurveyPreviewActivity, chemist
                )
                activityApnaSurveyPreviewBinding.chemistRecyclerView.adapter =
                    competitorsDetailsPreviewAdapter
                activityApnaSurveyPreviewBinding.chemistRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)
                chemists = chemist.map { it.chemist.toString() } as ArrayList<String>

                val totalOrgAvgSale =
                    chemist.stream().map { it.orgAvgSale }.mapToInt { it!!.toInt() }.sum()
                val totalUnorgAvgSale =
                    chemist.stream().map { it.unorgAvgSale }.mapToInt { it!!.toInt() }.sum()
                val total = totalOrgAvgSale + totalUnorgAvgSale

                activityApnaSurveyPreviewBinding.totalOrganized.setText(
                    DecimalFormat("##,##,##0").format(totalOrgAvgSale.toLong())
                )
                activityApnaSurveyPreviewBinding.totalUnorganized.setText(
                    DecimalFormat("##,##,##0").format(totalUnorgAvgSale.toLong())
                )
                activityApnaSurveyPreviewBinding.total.setText(
                    DecimalFormat("##,##,##0").format(total.toLong())
                )

                avgSales = chemist.map { it.orgAvgSale!!.toFloat() } as ArrayList<Float>
                setCompetitorsValues()
                setupCompetitorsChart()
            } else {
//                activityApnaSurveyPreviewBinding.chemistHeader.visibility = View.GONE
                activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.chemistListNotFound.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.chemistTotal.visibility = View.GONE
            }
        } else {
//            activityApnaSurveyPreviewBinding.chemistHeader.visibility = View.GONE
            activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.chemistListNotFound.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.chemistTotal.visibility = View.GONE
        }

        // Population and houses
        if (surveyCreateRequest.apartments != null) {
            if (surveyCreateRequest.apartments!!.size > 0) {
//                activityApnaSurveyPreviewBinding.apartmentsHeader.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.apartmentsListNotFound.visibility = View.GONE
                apartments =
                    surveyCreateRequest.apartments as ArrayList<SurveyCreateRequest.Apartment>
                val apartmentsPreviewAdapter = ApartmentsPreviewAdapter(
                    this@ApnaSurveyPreviewActivity, apartments
                )
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.adapter =
                    apartmentsPreviewAdapter
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)

                noOfHouses = apartments.map { it.noHouses!!.toInt().toFloat() } as ArrayList<Float>
                apartmentNames = apartments.map { it.apartments.toString() } as ArrayList<String>
                setApartmentsValues()
                setupApartmentsChart()
            } else {
//                activityApnaSurveyPreviewBinding.apartmentsHeader.visibility = View.GONE
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.apartmentsListNotFound.visibility = View.VISIBLE
            }
        } else {
//            activityApnaSurveyPreviewBinding.apartmentsHeader.visibility = View.GONE
            activityApnaSurveyPreviewBinding.apartmnetRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.apartmentsListNotFound.visibility = View.VISIBLE
        }

        // Hospitals
        if (surveyCreateRequest.hospitals != null) {
            if (surveyCreateRequest.hospitals!!.size > 0) {
//                activityApnaSurveyPreviewBinding.hospitalsHeader.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.hospitalListNotFound.visibility = View.GONE
                hospitals = surveyCreateRequest.hospitals as ArrayList<SurveyCreateRequest.Hospital>
                val hospitalsPreviewAdapter = HospitalsPreviewAdapter(
                    this@ApnaSurveyPreviewActivity, hospitals
                )
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.adapter =
                    hospitalsPreviewAdapter
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)

                beds = hospitals.map { it.beds!!.toInt().toFloat() } as ArrayList<Float>
                hospitalNames = hospitals.map { it.hospitals.toString() } as ArrayList<String>
                setHospitalsValues()
                setupHospitalsChart()
            } else {
//                activityApnaSurveyPreviewBinding.hospitalsHeader.visibility = View.GONE
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.hospitalListNotFound.visibility = View.VISIBLE
            }
        } else {
//            activityApnaSurveyPreviewBinding.hospitalsHeader.visibility = View.GONE
            activityApnaSurveyPreviewBinding.hospitalRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.hospitalListNotFound.visibility = View.VISIBLE
        }

        // photos and media
        if (surveyCreateRequest.siteImageMb != null) {
            if (surveyCreateRequest.siteImageMb!!.images!!.size > 0) {
                activityApnaSurveyPreviewBinding.imageRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.noPhotosAvailable.visibility = View.GONE
                images =
                    surveyCreateRequest.siteImageMb!!.images as ArrayList<SurveyCreateRequest.SiteImageMb.Image>
                val imagePreviewAdapter = ImagePreviewAdapter(
                    this@ApnaSurveyPreviewActivity, this@ApnaSurveyPreviewActivity, images
                )
                activityApnaSurveyPreviewBinding.imageRecyclerView.adapter = imagePreviewAdapter
                activityApnaSurveyPreviewBinding.imageRecyclerView.layoutManager =
                    LinearLayoutManager(
                        this@ApnaSurveyPreviewActivity, LinearLayoutManager.HORIZONTAL, false
                    )
            } else {
                activityApnaSurveyPreviewBinding.imageRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.noPhotosAvailable.visibility = View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.imageRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.noPhotosAvailable.visibility = View.VISIBLE
        }

        // video
        if (surveyCreateRequest.videoMb != null) {
            if (surveyCreateRequest.videoMb!!.video!!.size > 0) {
                activityApnaSurveyPreviewBinding.videoRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.noVideoAvailable.visibility = View.GONE
                videos =
                    surveyCreateRequest.videoMb!!.video as ArrayList<SurveyCreateRequest.VideoMb.Video>
                val videoPreviewAdapter = VideoPreviewAdapter(
                    this@ApnaSurveyPreviewActivity, this@ApnaSurveyPreviewActivity, videos
                )
                activityApnaSurveyPreviewBinding.videoRecyclerView.adapter = videoPreviewAdapter
                activityApnaSurveyPreviewBinding.videoRecyclerView.layoutManager =
                    LinearLayoutManager(
                        this@ApnaSurveyPreviewActivity, LinearLayoutManager.HORIZONTAL, false
                    )
            } else {
                activityApnaSurveyPreviewBinding.videoRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.noVideoAvailable.visibility = View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.videoRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.noVideoAvailable.visibility = View.VISIBLE
        }

        if (surveyCreateRequest.apolloEmployee != null) {
            if (surveyCreateRequest.apolloEmployee!!.uid != null) {
                if (surveyCreateRequest.apolloEmployee!!.uid!!.isNotEmpty()) {
                    activityApnaSurveyPreviewBinding.apolloEmployee.setText(surveyCreateRequest.apolloEmployee!!.uid!!.toString())
                } else {
                    activityApnaSurveyPreviewBinding.apolloEmployee.setText("-")
                }
            } else {
                activityApnaSurveyPreviewBinding.apolloEmployee.setText("-")
            }
        } else {
            activityApnaSurveyPreviewBinding.apolloEmployee.setText("-")
        }

        // Key Features
        if (toiletsAvailable.equals("yes", true)) {
            activityApnaSurveyPreviewBinding.toiletsAvailable.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.toiletsNotAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.toiletsText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.black)
            )
        } else if (toiletsAvailable.equals("No", true)) {
            activityApnaSurveyPreviewBinding.toiletsAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.toiletsNotAvailable.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.toiletsText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.grey)
            )
        }

        if (parkingAvailable.equals("Yes", true)) {
            activityApnaSurveyPreviewBinding.parkingAvailable.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.parkingNotAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.parkingText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.black)
            )
        } else if (parkingAvailable.equals("No", true)) {
            activityApnaSurveyPreviewBinding.parkingAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.parkingNotAvailable.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.parkingText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.grey)
            )
        }

        if (trafficType.equals("Low", true)) {
            activityApnaSurveyPreviewBinding.trafficLow.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.trafficMedium.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficVeryHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficNotAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficText.setText("Traffic Low")
        } else if (trafficType.equals("Medium", true)) {
            activityApnaSurveyPreviewBinding.trafficLow.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficMedium.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.trafficHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficVeryHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficNotAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficText.setText("Traffic Medium")
        } else if (trafficType.equals("High", true)) {
            activityApnaSurveyPreviewBinding.trafficLow.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficMedium.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficHigh.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.trafficVeryHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficNotAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficText.setText("Traffic High")
        } else if (trafficType.equals("V_High", true)) {
            activityApnaSurveyPreviewBinding.trafficLow.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficMedium.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficVeryHigh.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.trafficNotAvailable.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficText.setText("Traffic V.High")
        } else {
            activityApnaSurveyPreviewBinding.trafficLow.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficMedium.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficVeryHigh.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficNotAvailable.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.trafficText.setText("Traffic")
        }

        // Category Sale
        if (surveyCreateRequest.csPharma != null) {
            if (surveyCreateRequest.csPharma!! > 0) {
                activityApnaSurveyPreviewBinding.pharmaAvailableLayout.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.pharmaNotAvailableLayout.visibility = View.GONE
                activityApnaSurveyPreviewBinding.pharmaValue.setText(
                    surveyCreateRequest.csPharma!!.toString().substringBefore('.') + "%"
                )
            } else {
                activityApnaSurveyPreviewBinding.pharmaAvailableLayout.visibility = View.GONE
                activityApnaSurveyPreviewBinding.pharmaNotAvailableLayout.visibility = View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.pharmaAvailableLayout.visibility = View.GONE
            activityApnaSurveyPreviewBinding.pharmaNotAvailableLayout.visibility = View.VISIBLE
        }

        if (surveyCreateRequest.csFmcg != null) {
            if (surveyCreateRequest.csFmcg!! > 0) {
                activityApnaSurveyPreviewBinding.fmcgAvailableLayout.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.fmcgNotAvailableLayout.visibility = View.GONE
                activityApnaSurveyPreviewBinding.fmcgValue.setText(
                    surveyCreateRequest.csFmcg!!.toString().substringBefore('.') + "%"
                )
            } else {
                activityApnaSurveyPreviewBinding.fmcgAvailableLayout.visibility = View.GONE
                activityApnaSurveyPreviewBinding.fmcgNotAvailableLayout.visibility = View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.fmcgAvailableLayout.visibility = View.GONE
            activityApnaSurveyPreviewBinding.fmcgNotAvailableLayout.visibility = View.VISIBLE
        }

        if (surveyCreateRequest.csSurgicals != null) {
            if (surveyCreateRequest.csSurgicals!! > 0) {
                activityApnaSurveyPreviewBinding.surgicalsAvailableLayout.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.surgicalsNotAvailableLayout.visibility = View.GONE
                activityApnaSurveyPreviewBinding.surgicalsValue.setText(
                    surveyCreateRequest.csSurgicals!!.toString().substringBefore('.') + "%"
                )
            } else {
                activityApnaSurveyPreviewBinding.surgicalsAvailableLayout.visibility = View.GONE
                activityApnaSurveyPreviewBinding.surgicalsNotAvailableLayout.visibility =
                    View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.surgicalsAvailableLayout.visibility = View.GONE
            activityApnaSurveyPreviewBinding.surgicalsNotAvailableLayout.visibility = View.VISIBLE
        }

        if (surveyCreateRequest.areaDiscount != null) {
            if (surveyCreateRequest.areaDiscount!! > 0) {
                activityApnaSurveyPreviewBinding.areaDiscountAvailableLayout.visibility =
                    View.VISIBLE
                activityApnaSurveyPreviewBinding.areaDiscountNotAvailableLayout.visibility =
                    View.GONE
                activityApnaSurveyPreviewBinding.areaDiscountValue.setText(
                    surveyCreateRequest.areaDiscount!!.toString().substringBefore('.') + "%"
                )
            } else {
                activityApnaSurveyPreviewBinding.areaDiscountAvailableLayout.visibility = View.GONE
                activityApnaSurveyPreviewBinding.areaDiscountNotAvailableLayout.visibility =
                    View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.areaDiscountAvailableLayout.visibility = View.GONE
            activityApnaSurveyPreviewBinding.areaDiscountNotAvailableLayout.visibility =
                View.VISIBLE
        }
    }

    private fun scrollToView(scrollView: ScrollView, view: View) {
        val childOffset = Point()
        getDeepChildOffset(scrollView, view.parent, view, childOffset)
        scrollView.smoothScrollTo(0, childOffset.y)
    }

    private fun getDeepChildOffset(
        mainParent: ViewGroup,
        parent: ViewParent,
        child: View,
        accumulatedOffset: Point,
    ) {
        val parentGroup = parent as ViewGroup
        accumulatedOffset.x += child.left
        accumulatedOffset.y += child.top
        if (parentGroup == mainParent) {
            return
        }
        getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
    }

    private fun setupHospitalsChart() {
        val barDataSet = BarDataSet(hospitalsEntries, "")
        barDataSet.isHighlightEnabled = true
        val barData = BarData(barDataSet)
        activityApnaSurveyPreviewBinding.hospitalsChart.data = barData

        activityApnaSurveyPreviewBinding.hospitalsChart.setDragEnabled(true)
        activityApnaSurveyPreviewBinding.hospitalsChart.setScaleEnabled(true);
        activityApnaSurveyPreviewBinding.hospitalsChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaSurveyPreviewActivity,
                    IndexAxisValueFormatter(stringValuesList))
                mv.chartView = activityApnaSurveyPreviewBinding.hospitalsChart // For bounds control

                activityApnaSurveyPreviewBinding.hospitalsChart.marker = mv

            }

            override fun onNothingSelected() {

            }
        })

        barDataSet.setColor(Color.parseColor("#07559d"))
        barData.setDrawValues(false)
        if (beds.size > 2) {
            barData.barWidth = 0.5f
        } else {
            barData.barWidth = 0.1f
        }

//        activityApnaSurveyPreviewBinding.hospitalsChart.axisLeft.isEnabled = false
        activityApnaSurveyPreviewBinding.hospitalsChart.axisRight.isEnabled = false
        activityApnaSurveyPreviewBinding.hospitalsChart.xAxis.isEnabled = false

        activityApnaSurveyPreviewBinding.hospitalsChart.axisLeft.setLabelCount(5, true)
        activityApnaSurveyPreviewBinding.hospitalsChart.axisLeft.axisMinimum = 0f
        activityApnaSurveyPreviewBinding.hospitalsChart.axisLeft.axisMaximum = beds.max()

//        activityApnaSurveyPreviewBinding.hospitalsChart.xAxis.valueFormatter =
//            IndexAxisValueFormatter(
//                listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
//            )
//        activityApnaSurveyPreviewBinding.hospitalsChart.xAxis.setCenterAxisLabels(true)
//        activityApnaSurveyPreviewBinding.hospitalsChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        activityApnaSurveyPreviewBinding.hospitalsChart.xAxis.isGranularityEnabled = true
//        activityApnaSurveyPreviewBinding.hospitalsChart.xAxis.granularity = 1f
//        activityApnaSurveyPreviewBinding.hospitalsChart.xAxis.axisMinimum = 0f
//        activityApnaSurveyPreviewBinding.hospitalsChart.groupBars(0f, 0.5f, 0.1f)

//        activityApnaSurveyPreviewBinding.hospitalsChart.setDragEnabled(true)
//        activityApnaSurveyPreviewBinding.hospitalsChart.setVisibleXRangeMaximum(2f)


        // removing outer line
        activityApnaSurveyPreviewBinding.hospitalsChart.getAxisRight().setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.hospitalsChart.getAxisLeft().setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.hospitalsChart.getXAxis().setDrawAxisLine(false)
        // removing grid line
        activityApnaSurveyPreviewBinding.hospitalsChart.getAxisRight().setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.hospitalsChart.getAxisLeft().setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.hospitalsChart.getXAxis().setDrawGridLines(false)

        activityApnaSurveyPreviewBinding.hospitalsChart.description.isEnabled = false
        activityApnaSurveyPreviewBinding.hospitalsChart.legend.isEnabled = false
        activityApnaSurveyPreviewBinding.hospitalsChart.setTouchEnabled(true)
        activityApnaSurveyPreviewBinding.hospitalsChart.invalidate()
    }

//    private fun setHospitalsValuesTwo() {
//        hospitalsEntryTwo.add(BarEntry(1f, 6f))
//        hospitalsEntryTwo.add(BarEntry(2f, 3f))
//        hospitalsEntryTwo.add(BarEntry(3f, 5f))
//        hospitalsEntryTwo.add(BarEntry(4f, 4f))
//        hospitalsEntryTwo.add(BarEntry(5f, 5f))
//        hospitalsEntryTwo.add(BarEntry(6f, 4f))
//    }

    private fun setHospitalsValues() {
        for (i in beds.indices) {
            hospitalsEntries.add(BarEntry(i.toFloat(), beds.get(i), hospitalNames.get(i)))
        }
//        hospitalsEntryOne.add(BarEntry(1f, 5f))
//        hospitalsEntryOne.add(BarEntry(2f, 2f))
//        hospitalsEntryOne.add(BarEntry(3f, 4f))
//        hospitalsEntryOne.add(BarEntry(4f, 5f))
//        hospitalsEntryOne.add(BarEntry(5f, 3f))
//        hospitalsEntryOne.add(BarEntry(6f, 5f))
    }

    private fun setupApartmentsChart() {
        val barDataSet = BarDataSet(apartmentsEntries, "")
        barDataSet.isHighlightEnabled = true
        val barData = BarData(barDataSet)
        activityApnaSurveyPreviewBinding.apartmentsChart.data = barData

        activityApnaSurveyPreviewBinding.apartmentsChart.setDragEnabled(true)
        activityApnaSurveyPreviewBinding.apartmentsChart.setScaleEnabled(true);
        activityApnaSurveyPreviewBinding.apartmentsChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaSurveyPreviewActivity,
                    IndexAxisValueFormatter(stringValuesList))
                mv.chartView =
                    activityApnaSurveyPreviewBinding.apartmentsChart // For bounds control

                activityApnaSurveyPreviewBinding.apartmentsChart.marker = mv
            }

            override fun onNothingSelected() {

            }

        })

        barDataSet.setColor(Color.parseColor("#00aa9e"))
        barData.setDrawValues(false)
        if (noOfHouses.size > 2) {
            barData.barWidth = 0.5f
        } else {
            barData.barWidth = 0.1f
        }

//        activityApnaSurveyPreviewBinding.apartmentsChart.axisLeft.isEnabled = false
        activityApnaSurveyPreviewBinding.apartmentsChart.axisRight.isEnabled = false
        activityApnaSurveyPreviewBinding.apartmentsChart.xAxis.isEnabled = false

        activityApnaSurveyPreviewBinding.apartmentsChart.axisLeft.setLabelCount(5, true)
        activityApnaSurveyPreviewBinding.apartmentsChart.axisLeft.axisMinimum = 0f
        activityApnaSurveyPreviewBinding.apartmentsChart.axisLeft.axisMaximum = noOfHouses.max()

//        activityApnaSurveyPreviewBinding.apartmentsChart.xAxis.valueFormatter =
//            IndexAxisValueFormatter(
//                listOf("Apartments", "Houses")
//            )
//        activityApnaSurveyPreviewBinding.apartmentsChart.xAxis.setCenterAxisLabels(true)
//        activityApnaSurveyPreviewBinding.apartmentsChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        activityApnaSurveyPreviewBinding.apartmentsChart.xAxis.isGranularityEnabled = true
//        activityApnaSurveyPreviewBinding.apartmentsChart.xAxis.granularity = 1f
//        activityApnaSurveyPreviewBinding.apartmentsChart.xAxis.axisMinimum = 0f
//        activityApnaSurveyPreviewBinding.apartmentsChart.groupBars(0f, 0.5f, 0.1f)


        // removing outer line
        activityApnaSurveyPreviewBinding.apartmentsChart.getAxisRight().setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.apartmentsChart.getAxisLeft().setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.apartmentsChart.getXAxis().setDrawAxisLine(false)
        // removing grid line
        activityApnaSurveyPreviewBinding.apartmentsChart.getAxisRight().setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.apartmentsChart.getAxisLeft().setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.apartmentsChart.getXAxis().setDrawGridLines(false)

        activityApnaSurveyPreviewBinding.apartmentsChart.description.isEnabled = false
        activityApnaSurveyPreviewBinding.apartmentsChart.legend.isEnabled = false
        activityApnaSurveyPreviewBinding.apartmentsChart.setTouchEnabled(true)
        activityApnaSurveyPreviewBinding.apartmentsChart.invalidate()
    }

    private fun setApartmentsValues() {
        for (i in noOfHouses.indices) {
            apartmentsEntries.add(BarEntry(i.toFloat(), noOfHouses.get(i), apartmentNames.get(i)))
        }
//        apartmentsEntryTwo.add(BarEntry(1f, 6f))
//        apartmentsEntryTwo.add(BarEntry(2f, 5f))
    }

//    private fun setApartmentsValuesOne() {
//        apartmentsEntryOne.add(BarEntry(1f, 5f))
//        apartmentsEntryOne.add(BarEntry(2f, 4f))
//    }

    private fun setupCompetitorsChart() {
        val lineDataSet = LineDataSet(competitorsEntries, "")
        val lineData = LineData(lineDataSet)
        lineDataSet.isHighlightEnabled = true
        activityApnaSurveyPreviewBinding.competitorsChart.data = lineData

        activityApnaSurveyPreviewBinding.competitorsChart.setDragEnabled(true)
        activityApnaSurveyPreviewBinding.competitorsChart.setScaleEnabled(true);
        activityApnaSurveyPreviewBinding.competitorsChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaSurveyPreviewActivity,
                    IndexAxisValueFormatter(stringValuesList))
                mv.chartView =
                    activityApnaSurveyPreviewBinding.competitorsChart // For bounds control

                activityApnaSurveyPreviewBinding.competitorsChart.marker = mv
            }

            override fun onNothingSelected() {

            }
        })

        // Line width
        lineDataSet.lineWidth = 2f
        // Line color
        lineDataSet.setColor(Color.parseColor("#56a35f"))
        // Remove circles from the line
        lineDataSet.setDrawCircles(false)
        // Remove values above the line
        lineDataSet.setDrawValues(false)

        activityApnaSurveyPreviewBinding.competitorsChart.axisRight.isEnabled = false
        activityApnaSurveyPreviewBinding.competitorsChart.xAxis.isEnabled = false

        activityApnaSurveyPreviewBinding.competitorsChart.getAxisLeft().setLabelCount(5, true)
        activityApnaSurveyPreviewBinding.competitorsChart.getAxisLeft().setAxisMinimum(0f)
        activityApnaSurveyPreviewBinding.competitorsChart.getAxisLeft()
            .setAxisMaximum(avgSales.max())

//        activityApnaSurveyPreviewBinding.competitorsChart.xAxis.position =
//            XAxis.XAxisPosition.BOTTOM
//        activityApnaSurveyPreviewBinding.competitorsChart.xAxis.setCenterAxisLabels(true)
//        activityApnaSurveyPreviewBinding.competitorsChart.xAxis.valueFormatter =
//            IndexAxisValueFormatter(
//                listOf("Jan", "Feb", "Mar")
//            )

        activityApnaSurveyPreviewBinding.competitorsChart.description.isEnabled = false
        activityApnaSurveyPreviewBinding.competitorsChart.legend.isEnabled = false

        // removing outer line
        activityApnaSurveyPreviewBinding.competitorsChart.getAxisRight().setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.competitorsChart.getAxisLeft().setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.competitorsChart.getXAxis().setDrawAxisLine(false)
        // removing grid line
        activityApnaSurveyPreviewBinding.competitorsChart.getAxisRight().setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.competitorsChart.getAxisLeft().setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.competitorsChart.getXAxis().setDrawGridLines(false)

        activityApnaSurveyPreviewBinding.competitorsChart.setTouchEnabled(true)
        activityApnaSurveyPreviewBinding.competitorsChart.invalidate()
    }

    private fun setCompetitorsValues() {
        for (i in avgSales.indices) {
            competitorsEntries.add(Entry(i.toFloat(), avgSales.get(i), chemists.get(i)))
        }
//        competitorsEntries.add(Entry(0f, 5f))
//        competitorsEntries.add(Entry(1f, 7f))
//        competitorsEntries.add(Entry(2f, 8f))
    }

    private fun setupNeighborChart() {
        val barDataSet = BarDataSet(neighborEntries, "")
        val barData = BarData(barDataSet)
        barDataSet.isHighlightEnabled = true
        activityApnaSurveyPreviewBinding.neighborChart.data = barData

        activityApnaSurveyPreviewBinding.neighborChart.setDragEnabled(true)
        activityApnaSurveyPreviewBinding.neighborChart.setScaleEnabled(true);
        activityApnaSurveyPreviewBinding.neighborChart.setDrawValueAboveBar(true)
        activityApnaSurveyPreviewBinding.neighborChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaSurveyPreviewActivity,
                    IndexAxisValueFormatter(stringValuesList))
                mv.chartView = activityApnaSurveyPreviewBinding.neighborChart // For bounds control

                activityApnaSurveyPreviewBinding.neighborChart.marker = mv
            }

            override fun onNothingSelected() {
            }
        })

        // Set bar colors
        barDataSet.colors = listOf(
            Color.parseColor("#f7941d"),
            Color.parseColor("#8dc73e"),
            Color.parseColor("#03a99e"),
            Color.parseColor("#00b0ee")
        )

        // Remove values top of the bar
        barDataSet.setDrawValues(false)
        // Bar width
        if (sales.size > 2) {
            barData.barWidth = 0.5f
        } else {
            barData.barWidth = 0.1f
        }

        activityApnaSurveyPreviewBinding.neighborChart.axisRight.isEnabled = false
        activityApnaSurveyPreviewBinding.neighborChart.xAxis.isEnabled = false

        // Set y axis values
        activityApnaSurveyPreviewBinding.neighborChart.getAxisLeft().setLabelCount(5, true)
        activityApnaSurveyPreviewBinding.neighborChart.getAxisLeft().setAxisMinimum(0f)
        activityApnaSurveyPreviewBinding.neighborChart.getAxisLeft().setAxisMaximum(sales.max())
        // Disable touch
        activityApnaSurveyPreviewBinding.neighborChart.setTouchEnabled(true)
        // Remove description
        activityApnaSurveyPreviewBinding.neighborChart.description.isEnabled = false
        // Remove legend
        activityApnaSurveyPreviewBinding.neighborChart.legend.isEnabled = false
        // Remove outer line
        activityApnaSurveyPreviewBinding.neighborChart.axisRight.setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.neighborChart.axisLeft.setDrawAxisLine(false)
        activityApnaSurveyPreviewBinding.neighborChart.xAxis.setDrawAxisLine(false)
        // Remove grid line
        activityApnaSurveyPreviewBinding.neighborChart.axisRight.setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.neighborChart.axisLeft.setDrawGridLines(false)
        activityApnaSurveyPreviewBinding.neighborChart.xAxis.setDrawGridLines(false)

        activityApnaSurveyPreviewBinding.neighborChart.invalidate()
    }

    private fun setNeighborChartValues() {
        for (i in sales.indices) {
            neighborEntries.add(BarEntry(i.toFloat(), sales.get(i), stores.get(i)))
        }
//        neighborEntries.add(BarEntry(1f, 18f))
//        neighborEntries.add(BarEntry(2f, 13f))
//        neighborEntries.add(BarEntry(3f, 11f))
//        neighborEntries.add(BarEntry(4f, 5f))
    }

    private fun getCurrentLocation(lat: String?, long: String?) {
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
                        if (lat != null && long != null) {
                            val latLang = LatLng(lat.toDouble(), long.toDouble())
                            val markerOption = MarkerOptions().position(latLang).title("")
                            map.addMarker(markerOption)
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15F))
                        } else {
                        }
                    }
                })
            }
        })
    }

    override fun onClick(
        imageUrl: String,
        position: Int,
        data: ArrayList<SurveyCreateRequest.SiteImageMb.Image>,
    ) {

        val intent = Intent(this@ApnaSurveyPreviewActivity, ApnaImagePreviewActivity::class.java)
        intent.putExtra("IMAGE_LIST", data)
        intent.putExtra("CURRENT_POSITION", position)
        startActivity(intent)

//        val dialog = Dialog(
//            this@ApnaSurveyPreviewActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen
//        )
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        val previewImageDialogBinding = DataBindingUtil.inflate<PreviewImageDialogBinding>(
//            LayoutInflater.from(this@ApnaSurveyPreviewActivity),
//            R.layout.preview_image_dialog,
//            null,
//            false
//        )
//        dialog.setContentView(previewImageDialogBinding.root)
//        Glide.with(this@ApnaSurveyPreviewActivity).load(imageUrl)
//            .placeholder(R.drawable.placeholder_image).into(previewImageDialogBinding.previewImage)
//        previewImageDialogBinding.close.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.setCancelable(false)
//        dialog.show()
    }

    override fun onClickPlay(video: String) {
        val intent = Intent(this@ApnaSurveyPreviewActivity, VideoPreviewActivity::class.java)
        intent.putExtra("VIDEO_URI", video)
        startActivity(intent)
    }
}