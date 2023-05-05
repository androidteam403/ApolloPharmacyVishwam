package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.ApnaSurveyFragment
import com.apollopharmacy.vishwam.ui.home.apna.survey.videopreview.ApnaVideoPreview
import com.apollopharmacy.vishwam.util.PopUpWIndow
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
    private var instance: ApnaSurveyFragment? = null

    var mapUserLangs: String? = null

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apnaPreviewActivityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_apna_preview
        )

        apnaNewPreviewViewModel = ViewModelProvider(this)[ApnaNewPreviewViewModel::class.java]

        apnaPreviewActivityBinding.arrow.setOnClickListener {
//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)
//            finish()
            onBackPressed()
        }
        setUp()
    }

    @RequiresApi(33)
    private fun setUp() {


        if (intent != null) {
            approvedOrders =
                (intent.getSerializableExtra("regionList") as SurveyListResponse.Row?)!!
            apnaNewPreviewViewModel.getApnaDetailsList(this, approvedOrders.uid!!)

        }

        if (approvedOrders != null) {
            if (approvedOrders.status != null) {
                if (approvedOrders.status!!.name.equals("null") && approvedOrders.status!!.name.isNullOrEmpty()) {
                    apnaPreviewActivityBinding.status.setText("-")

                } else {
                    apnaPreviewActivityBinding.status.setText(approvedOrders.status!!.name)

                }
            }
            apnaPreviewActivityBinding.status.setTextColor(Color.parseColor(approvedOrders.status!!.textColor!!))
            apnaPreviewActivityBinding.statusLayout.setBackgroundColor(Color.parseColor(
                approvedOrders.status!!.backgroundColor!!))

            apnaPreviewActivityBinding.storeId.setText(approvedOrders.surveyId)
            var fName = ""
            var lName = ""
            if (approvedOrders.createdId!!.firstName != null) {
                fName = approvedOrders.createdId!!.firstName!!
            }

            if (approvedOrders.createdId!!.lastName != null) {
                lName = ", ${approvedOrders.createdId!!.lastName!!}"
            }
            apnaPreviewActivityBinding.surveyby.setText("$fName$lName")

            var locationName = ""
            var cityName = ""
            if (approvedOrders.location!!.name != null) locationName =
                approvedOrders.location!!.name!!
            if (approvedOrders.city!!.name != null) cityName = ", ${approvedOrders.city!!.name}"

            apnaPreviewActivityBinding.storeName.setText("$locationName$cityName")

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val outputDateFormat = SimpleDateFormat("dd MMM, yyy hh:mm a")
            apnaPreviewActivityBinding.surveystart.setText(outputDateFormat.format(inputDateFormat.parse(
                approvedOrders.createdTime!!)!!))
            apnaPreviewActivityBinding.surveyended.setText(outputDateFormat.format(inputDateFormat.parse(
                approvedOrders.modifiedTime!!)!!))

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            try {
                val date1 = simpleDateFormat.parse(approvedOrders.createdTime)
                val date2 = simpleDateFormat.parse(approvedOrders.modifiedTime)
                printDifference(date1, date2)
                apnaPreviewActivityBinding.timeTaken.setText(printDifference(date1, date2))

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (approvedOrders.status != null) {
                if (approvedOrders.status!!.other != null) {

                    if (approvedOrders.status!!.other!!.color.toString()
                            .isNullOrEmpty() || approvedOrders.status!!.other!!.color.toString()
                            .equals(
                                "null")
                    ) {

                    } else {
                        apnaPreviewActivityBinding.status.setTextColor(Color.parseColor(
                            approvedOrders.status!!.other!!.color))

                    }
                }
            }

            if (approvedOrders.status != null) {
                if (approvedOrders.status!!.icon != null) {

                    if (approvedOrders.status!!.icon.equals("null") || approvedOrders.status!!.icon.isNullOrEmpty()) {

                    } else {
                        apnaPreviewActivityBinding.statusLayout.setBackgroundColor(Color.parseColor(
                            approvedOrders.status!!.icon))
                    }
                }
            }
        }

    }


    override fun onSuccessgetSurveyDetails(value: SurveyDetailsList) {
        apartmentAdapter = PreviewApartmentAdapter(this,
            value.data!!.apartments as ArrayList<SurveyDetailsList.Apartment>)
        apnaPreviewActivityBinding.recyclerViewapartmnet.adapter = apartmentAdapter
        adapter = PreviewChemistAdapter(this,
            value.data!!.chemist as ArrayList<SurveyDetailsList.Chemist>)
        apnaPreviewActivityBinding.recyclerViewchemist.adapter = adapter
        hospitalAdapter = PreviewHospitalAdapter(this,
            value.data!!.hospitals as ArrayList<SurveyDetailsList.Hospital>)
        apnaPreviewActivityBinding.recyclerViewhospital.adapter = hospitalAdapter
        if (value.data!!.siteImageMb != null && value.data!!.siteImageMb!!.images != null && value.data!!.siteImageMb!!.images!!.size > 0) {
            apnaPreviewActivityBinding.noPhotosAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.imageRecyclerView.visibility = View.VISIBLE
            imageAdapter = PreviewImageAdapter(this,
                value.data!!.siteImageMb!!.images as ArrayList<SurveyDetailsList.Image>, this)
            apnaPreviewActivityBinding.imageRecyclerView.adapter = imageAdapter
        } else {
            apnaPreviewActivityBinding.imageRecyclerView.visibility = View.GONE
            apnaPreviewActivityBinding.noPhotosAvailable.visibility = View.VISIBLE
        }
//        videoMbList.add(value.data!!.videoMb!!)
//        videoList.add("https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1")
        if (value.data!!.videoMb!! != null && value.data!!.videoMb!!.video != null && value.data!!.videoMb!!.video!!.size > 0) {

            apnaPreviewActivityBinding.noVideoAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.videoRecyclerView.visibility = View.VISIBLE
            videoAdapter = PreviewVideoAdapter(this, value.data!!.videoMb!!.video!!, this)
            apnaPreviewActivityBinding.videoRecyclerView.adapter = videoAdapter
            neighbourAdapter = PreviewNeighbouringStoreAdapter(this,
                value.data!!.neighboringStore as ArrayList<SurveyDetailsList.NeighboringStore>)
            apnaPreviewActivityBinding.recyclerViewneighbour.adapter = neighbourAdapter
        } else {
            apnaPreviewActivityBinding.videoRecyclerView.visibility = View.GONE
            apnaPreviewActivityBinding.noVideoAvailable.visibility = View.VISIBLE
        }
        apnaPreviewActivityBinding.locationdetails.setText(value.data!!.state!!.name + ", " + value.data!!.city!!.name)

        trafficAdapter = PreviewTrafficAdapter(this,
            value.data!!.trafficGenerator as ArrayList<SurveyDetailsList.TrafficGenerator>)
        apnaPreviewActivityBinding.recyclerViewTraffic.adapter = trafficAdapter
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

        if (value.data!!.ceilingHeight != null) {
            apnaPreviewActivityBinding.ceilingHeight.setText(value.data!!.ceilingHeight.toString())
        } else {
            apnaPreviewActivityBinding.ceilingHeight.setText("-")
        }

        if (value.data!!.totalArea != null) {
            apnaPreviewActivityBinding.totalareasqft.setText(value.data!!.totalArea.toString())
        } else {
            apnaPreviewActivityBinding.totalareasqft.setText("-")
        }

        if (value.data!!.expectedRent != null) {
            apnaPreviewActivityBinding.expectedrentsrft.setText(value.data!!.expectedRent.toString())
        } else {
            apnaPreviewActivityBinding.expectedrentsrft.setText("-")
        }

        if (value.data!!.securityDeposit != null) {
            apnaPreviewActivityBinding.securitydeposit.setText(value.data!!.securityDeposit.toString())
        } else {
            apnaPreviewActivityBinding.securitydeposit.setText("-")
        }

        if (value.data!!.buildingAge != null) {
            apnaPreviewActivityBinding.ageOfTheBuilding.setText(value.data!!.buildingAge.toString())
        } else {
            apnaPreviewActivityBinding.ageOfTheBuilding.setText("")
        }

        if (value.data!!.parking != null) {
            apnaPreviewActivityBinding.parking.setText(value.data!!.parking!!.uid)
        } else {
            apnaPreviewActivityBinding.parking.setText("-")
        }

        if (value.data!!.expectedRent != null) {
            apnaPreviewActivityBinding.expectedrentsrft.setText(value.data!!.expectedRent.toString())
        } else {
            apnaPreviewActivityBinding.expectedrentsrft.setText("-")
        }

        if (value.data!!.extngOutletName != null) {
            apnaPreviewActivityBinding.existingOutletName.setText(value.data!!.extngOutletName)
        } else {
            apnaPreviewActivityBinding.existingOutletName.setText("-")
        }

        if (value.data!!.csPharma != null) {
            apnaPreviewActivityBinding.pharma.setText(value.data!!.csPharma.toString())
        } else {
            apnaPreviewActivityBinding.pharma.setText("-")
        }

        if (value.data!!.csFmcg != null) {
            apnaPreviewActivityBinding.fmcg.setText(value.data!!.csFmcg.toString())
        } else {
            apnaPreviewActivityBinding.fmcg.setText("-")
        }

        if (value.data!!.csSurgicals != null) {
            apnaPreviewActivityBinding.surgicals.setText(value.data!!.csSurgicals.toString())
        } else {
            apnaPreviewActivityBinding.surgicals.setText("-")
        }

        if (value.data!!.areaDiscount != null) {
            apnaPreviewActivityBinding.areadiscount.setText(value.data!!.areaDiscount.toString())
        } else {
            apnaPreviewActivityBinding.areadiscount.setText("-")
        }

        if(value.data!!.serviceClass != null) {
            apnaPreviewActivityBinding.serviceClass.setText(value.data!!.serviceClass.toString())
        } else {
            apnaPreviewActivityBinding.serviceClass.setText("-")
        }
        if (value.data!!.businessClass != null) {
            apnaPreviewActivityBinding.businessClass.setText(value.data!!.businessClass.toString())
        } else {
            apnaPreviewActivityBinding.businessClass.setText("-")
        }

        if (value.data!!.morningFrom != null) {
            apnaPreviewActivityBinding.morning.setText(value.data!!.morningFrom.toString())
        } else {
            apnaPreviewActivityBinding.morning.setText("-")
        }
        if (value.data!!.eveningTo != null) {
            apnaPreviewActivityBinding.evening.setText(value.data!!.eveningTo.toString())
        } else {
            apnaPreviewActivityBinding.evening.setText("-")
        }
        if (value.data!!.localDisbtsComments != null) {
            apnaPreviewActivityBinding.localdistubutorcomment.setText(value.data!!.localDisbtsComments.toString())
        } else {
            apnaPreviewActivityBinding.localdistubutorcomment.setText("-")
        }
    }

    override fun onClick(value: Int, url: String) {
        val i = Intent(this, ApnaVideoPreview::class.java)
        i.putExtra("activity", url)
//        startActivityForResult(i, 210)
        startActivity(i)


    }

    override fun onItemClick(position: Int, imagePath: String, name: String) {
        PopUpWIndow(this,
            R.layout.layout_image_fullview,
            View(this),
            imagePath,
            null,
            name,
            position)

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
        return String.format(
            "%02d:%02d:%02d",
            elapsedHours, elapsedMinutes, elapsedSeconds)
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onFailuregetSurveyWiseDetails(value: SurveyDetailsList) {
    }


}