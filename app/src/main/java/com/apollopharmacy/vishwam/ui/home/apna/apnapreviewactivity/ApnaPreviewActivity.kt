package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaPreviewBinding
import com.apollopharmacy.vishwam.databinding.DialogVideoPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.videopreview.ApnaVideoPreviewActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ApnaPreviewActivity : AppCompatActivity(), ApnaNewPreviewCallBack {
    private lateinit var apnaPreviewActivityBinding: ActivityApnaPreviewBinding
    private lateinit var apnaNewPreviewViewModel: ApnaNewPreviewViewModel
    var adapter: PreviewChemistAdapter? = null
    var hospitalAdapter: PreviewHospitalAdapter? = null
    var apartmentAdapter: PreviewApartmentAdapter? = null
    var trafficAdapter: PreviewTrafficAdapter? = null
    var imageAdapter: PreviewImageAdapter? = null
    var videoAdapter: PreviewVideoAdapter? = null

    var neighbourAdapter: PreviewNeighbouringStoreAdapter? = null
    private var locationManager: LocationManager? = null
    private val GPS_REQUEST_CODE = 2
    var supportMapFragment: SupportMapFragment? = null
    var client: FusedLocationProviderClient? = null
    var map: GoogleMap? = null
    var geocoder: Geocoder? = null
    var mapUserLats: String? = null
    var videoList = ArrayList<String>()
    var videoMbList = ArrayList<SurveyDetailsList.VideoMb>()
    lateinit var approvedOrders: SurveyListResponse.Row

    var mapUserLangs: String? = null
    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apnaPreviewActivityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_apna_preview
        )

        apnaNewPreviewViewModel = ViewModelProvider(this)[ApnaNewPreviewViewModel::class.java]
        if (intent != null) {
            approvedOrders= (intent.getSerializableExtra("regionList") as SurveyListResponse.Row?)!!
        }
        setUp()
    }

    @RequiresApi(33)
    private fun setUp() {

        apnaNewPreviewViewModel.getApnaDetailsList(this, approvedOrders.uid!!)




        apnaPreviewActivityBinding.arrow.setOnClickListener {
            onBackPressed()
        }
        if (approvedOrders!=null){
            if (approvedOrders.status!=null) {
                if (approvedOrders.status!!.name.equals("null") && approvedOrders.status!!.name.isNullOrEmpty()) {
                    apnaPreviewActivityBinding.status.setText("-")

                } else {
                    apnaPreviewActivityBinding.status.setText(approvedOrders.status!!.name)

                }
            }
            apnaPreviewActivityBinding.storeId.setText(approvedOrders.surveyId)
            apnaPreviewActivityBinding.surveyby.setText("-")
            apnaPreviewActivityBinding.storeName.setText(approvedOrders.location!!.name + " , " + approvedOrders.city!!.name)
            apnaPreviewActivityBinding.surveystart.setText(approvedOrders.createdTime)
            apnaPreviewActivityBinding.surveyended.setText(approvedOrders.modifiedTime)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            try {
                val date1 = simpleDateFormat.parse(approvedOrders.createdTime)
                val date2 = simpleDateFormat.parse(approvedOrders.modifiedTime)
                printDifference(date1, date2)
                apnaPreviewActivityBinding.timeTaken.setText(printDifference(date1, date2))

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (approvedOrders.status!=null) {
                if (approvedOrders.status!!.other!=null) {

                    if (approvedOrders.status!!.other!!.color.toString()
                            .isNullOrEmpty() || approvedOrders.status!!.other!!.color.toString().equals(
                            "null")
                    ) {

                    } else {
                        apnaPreviewActivityBinding.status.setTextColor(Color.parseColor(
                            approvedOrders.status!!.other!!.color))

                    }
                }
            }

            if (approvedOrders.status!=null) {
                if (approvedOrders.status!!.icon!=null) {

                    if (approvedOrders.status!!.icon.equals("null") || approvedOrders.status!!.icon.isNullOrEmpty()) {

                    } else {
                        apnaPreviewActivityBinding.statusLayout.setBackgroundColor(Color.parseColor(
                            approvedOrders.status!!.icon))
                    }
                }
            }        }

        }

    override fun onSuccessgetSurveyDetails(value: SurveyDetailsList) {

        mapUserLats = value.data!!.lat
        mapUserLangs = value.data!!.long

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        client = LocationServices.getFusedLocationProviderClient(this)
        supportMapFragment!!.getMapAsync { googleMap ->

            val latLng = LatLng(mapUserLats!!.toDouble(), mapUserLangs!!.toDouble())

            val options =
                MarkerOptions().position(latLng).title("i")
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng,
                9f))
            googleMap.addMarker(options)
        }

        apnaPreviewActivityBinding.lattitude.setText(value.data!!.lat)
        apnaPreviewActivityBinding.longitude.setText(value.data!!.long)
        apnaPreviewActivityBinding.length.setText(value.data!!.length.toString())
        apnaPreviewActivityBinding.width.setText(value.data!!.width.toString())
        apnaPreviewActivityBinding.ceilingHeight.setText(value.data!!.ceilingHeight.toString())
        apnaPreviewActivityBinding.totalareasqft.setText(value.data!!.totalArea.toString())
        apnaPreviewActivityBinding.expectedrent.setText(value.data!!.expectedRent.toString())
        apnaPreviewActivityBinding.securitydeposit.setText(value.data!!.securityDeposit.toString())
        apnaPreviewActivityBinding.ageOfTheBuilding.setText(value.data!!.buildingAge.toString())
        apnaPreviewActivityBinding.parking.setText(value.data!!.parking!!.uid)
        apnaPreviewActivityBinding.expectedrentsrft.setText(value.data!!.expectedRent.toString())
        apnaPreviewActivityBinding.existingOutletName.setText(value.data!!.extngOutletName)
        apnaPreviewActivityBinding.pharma.setText(value.data!!.csPharma.toString())
        apnaPreviewActivityBinding.fmcg.setText(value.data!!.csFmcg.toString())
        apnaPreviewActivityBinding.surgicals.setText(value.data!!.csSurgicals.toString())
        apnaPreviewActivityBinding.areadiscount.setText(value.data!!.areaDiscount.toString())


        apnaPreviewActivityBinding.serviceClass.setText(value.data!!.serviceClass.toString())
        apnaPreviewActivityBinding.businessClass.setText(value.data!!.businessClass.toString())
        apnaPreviewActivityBinding.morning.setText(value.data!!.morningFrom.toString())
        apnaPreviewActivityBinding.evening.setText(value.data!!.eveningTo.toString())
        apnaPreviewActivityBinding.localdistubutorcomment.setText(value.data!!.localDisbtsComments.toString())

        neighbourAdapter = PreviewNeighbouringStoreAdapter(this,
            value.data!!.neighboringStore as ArrayList<SurveyDetailsList.NeighboringStore>)
        apnaPreviewActivityBinding.recyclerViewneighbour.adapter = neighbourAdapter

        apartmentAdapter = PreviewApartmentAdapter(this,
            value.data!!.apartments as ArrayList<SurveyDetailsList.Apartment>)
        apnaPreviewActivityBinding.recyclerViewapartmnet.adapter = apartmentAdapter
        adapter = PreviewChemistAdapter(this,
            value.data!!.chemist as ArrayList<SurveyDetailsList.Chemist>)
        apnaPreviewActivityBinding.recyclerViewchemist.adapter = adapter

        apnaPreviewActivityBinding.locationdetails.setText(value.data!!.state!!.name + ", " + value.data!!.city!!.name)

        trafficAdapter = PreviewTrafficAdapter(this,
            value.data!!.trafficGenerator as ArrayList<SurveyDetailsList.TrafficGenerator>)
        apnaPreviewActivityBinding.recyclerViewTraffic.adapter = trafficAdapter

        hospitalAdapter = PreviewHospitalAdapter(this,
            value.data!!.hospitals as ArrayList<SurveyDetailsList.Hospital>)
        apnaPreviewActivityBinding.recyclerViewhospital.adapter = hospitalAdapter
        imageAdapter = PreviewImageAdapter(this,
            value.data!!.siteImageMb!!.images as ArrayList<SurveyDetailsList.Image>)
        apnaPreviewActivityBinding.imageRecyclerView.adapter = imageAdapter
        videoMbList.add(value.data!!.videoMb!!)
        videoList.add("https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1")
        videoAdapter = PreviewVideoAdapter(this, videoMbList, videoList, this)
        apnaPreviewActivityBinding.videoRecyclerView.adapter = videoAdapter


    }

    override fun onClick(value: Int, url: String) {
        val i = Intent(this, ApnaVideoPreviewActivity::class.java)
        i.putExtra("activity", url)
        startActivityForResult(i, 210)



    }
    private fun printDifference(startDate: Date, endDate: Date): String {

        //milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        return "$elapsedHours:$elapsedMinutes:$elapsedSeconds"

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onFailuregetSurveyWiseDetails(value: SurveyDetailsList) {
    }


}