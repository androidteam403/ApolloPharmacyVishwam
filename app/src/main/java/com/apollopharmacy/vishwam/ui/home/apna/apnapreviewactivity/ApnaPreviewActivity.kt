package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    var toiletsAvailable: String = ""
    var parkingAvailable: String = ""
    var trafficType: String = ""

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
            apnaPreviewActivityBinding.statusLayout.setBackgroundColor(
                Color.parseColor(
                    approvedOrders.status!!.backgroundColor!!
                )
            )

//            apnaPreviewActivityBinding.storeId.setText(approvedOrders.surveyId)
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
            apnaPreviewActivityBinding.surveystart.setText(
                outputDateFormat.format(
                    inputDateFormat.parse(
                        approvedOrders.createdTime!!
                    )!!
                )
            )
            apnaPreviewActivityBinding.surveyended.setText(
                outputDateFormat.format(
                    inputDateFormat.parse(
                        approvedOrders.modifiedTime!!
                    )!!
                )
            )

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
                                "null"
                            )
                    ) {

                    } else {
                        apnaPreviewActivityBinding.status.setTextColor(
                            Color.parseColor(
                                approvedOrders.status!!.other!!.color
                            )
                        )

                    }
                }
            }

            if (approvedOrders.status != null) {
                if (approvedOrders.status!!.icon != null) {

                    if (approvedOrders.status!!.icon.equals("null") || approvedOrders.status!!.icon.isNullOrEmpty()) {

                    } else {
                        apnaPreviewActivityBinding.statusLayout.setBackgroundColor(
                            Color.parseColor(
                                approvedOrders.status!!.icon
                            )
                        )
                    }
                }
            }
        }

    }


    @SuppressLint("SetTextI18n")
    override fun onSuccessgetSurveyDetails(value: SurveyDetailsList) {
        apnaPreviewActivityBinding.storeId.setText(value.data!!.id)
        if (value.data!!.apartments != null && value.data!!.apartments!!.size > 0) {
            apnaPreviewActivityBinding.recyclerViewapartmnet.visibility = View.VISIBLE
            apnaPreviewActivityBinding.apartmentsNotFound.visibility = View.GONE
            apartmentAdapter = PreviewApartmentAdapter(
                this,
                value.data!!.apartments as ArrayList<SurveyDetailsList.Apartment>
            )
            apnaPreviewActivityBinding.recyclerViewapartmnet.adapter = apartmentAdapter
        } else {
            apnaPreviewActivityBinding.recyclerViewapartmnet.visibility = View.GONE
            apnaPreviewActivityBinding.apartmentsNotFound.visibility = View.VISIBLE
        }

        if (value.data!!.chemist != null && value.data!!.chemist!!.size > 0) {
            apnaPreviewActivityBinding.recyclerViewchemist.visibility = View.VISIBLE
            apnaPreviewActivityBinding.chemistTotal.visibility = View.VISIBLE
            apnaPreviewActivityBinding.chemistNotFound.visibility = View.GONE
            adapter = PreviewChemistAdapter(
                this,
                value.data!!.chemist as ArrayList<SurveyDetailsList.Chemist>
            )
            apnaPreviewActivityBinding.recyclerViewchemist.adapter = adapter

            val totalOrg =
                value.data!!.chemist!!.stream().map { it.orgAvgSale }.mapToInt { it!!.toInt() }
                    .sum()
            val totalUnorg =
                value.data!!.chemist!!.stream().map { it.unorgAvgSale }.mapToInt { it!!.toInt() }
                    .sum()
            val total = totalOrg + totalUnorg

            apnaPreviewActivityBinding.organized.setText(totalOrg.toString())
            apnaPreviewActivityBinding.unorganized.setText(totalUnorg.toString())
            apnaPreviewActivityBinding.total.setText(total.toString())
        } else {
            apnaPreviewActivityBinding.recyclerViewchemist.visibility = View.GONE
            apnaPreviewActivityBinding.chemistTotal.visibility = View.GONE
            apnaPreviewActivityBinding.chemistNotFound.visibility = View.VISIBLE
        }

        if (value.data!!.hospitals != null && value.data!!.hospitals!!.size > 0) {
            apnaPreviewActivityBinding.recyclerViewhospital.visibility = View.VISIBLE
            apnaPreviewActivityBinding.hospitalsNotFound.visibility = View.GONE
            hospitalAdapter = PreviewHospitalAdapter(
                this,
                value.data!!.hospitals as ArrayList<SurveyDetailsList.Hospital>
            )
            apnaPreviewActivityBinding.recyclerViewhospital.adapter = hospitalAdapter
        } else {
            apnaPreviewActivityBinding.recyclerViewhospital.visibility = View.GONE
            apnaPreviewActivityBinding.hospitalsNotFound.visibility = View.VISIBLE
        }

        if (value.data!!.siteImageMb != null && value.data!!.siteImageMb!!.images != null && value.data!!.siteImageMb!!.images!!.size > 0) {
            apnaPreviewActivityBinding.noPhotosAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.imageRecyclerView.visibility = View.VISIBLE
            imageAdapter = PreviewImageAdapter(
                this,
                value.data!!.siteImageMb!!.images as ArrayList<SurveyDetailsList.Image>, this
            )
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
        } else {
            apnaPreviewActivityBinding.videoRecyclerView.visibility = View.GONE
            apnaPreviewActivityBinding.noVideoAvailable.visibility = View.VISIBLE
        }

        if (value.data!!.neighboringStore != null && value.data!!.neighboringStore!!.size > 0) {
            apnaPreviewActivityBinding.recyclerViewneighbour.visibility = View.VISIBLE
            apnaPreviewActivityBinding.neighbouringStoreNotFound.visibility = View.GONE
            neighbourAdapter = PreviewNeighbouringStoreAdapter(
                this,
                value.data!!.neighboringStore as ArrayList<SurveyDetailsList.NeighboringStore>
            )
            apnaPreviewActivityBinding.recyclerViewneighbour.adapter = neighbourAdapter
        } else {
            apnaPreviewActivityBinding.recyclerViewneighbour.visibility = View.GONE
            apnaPreviewActivityBinding.neighbouringStoreNotFound.visibility = View.VISIBLE
        }

        var location = ""
        var state = ""
        var city = ""
        var pin = ""
        var landmarks = ""

        if (value.data!!.location != null) {
            if (value.data!!.location!!.name != null) {
                if (value.data!!.location!!.name.toString().isNotEmpty()) {
                    if (!value.data!!.location!!.name.toString().equals("null", true)) {
                        location = value.data!!.location!!.name.toString()
                    } else {
                        location = "-"
                    }
                } else {
                    location = "-"
                }
            } else {
                location = "-"
            }
        } else {
            location = "-"
        }

        if (value.data!!.city != null) {
            if (value.data!!.city!!.name != null) {
                if (value.data!!.city!!.name.toString().isNotEmpty()) {
                    if (!value.data!!.city!!.name.toString().equals("null", true)) {
                        city = value.data!!.city!!.name.toString()
                    } else {
                        city = "-"
                    }
                } else {
                    city = "-"
                }
            } else {
                city = "-"
            }
        } else {
            city = "-"
        }

        if (value.data!!.state != null) {
            if (value.data!!.state!!.name != null) {
                if (value.data!!.state!!.name.toString().isNotEmpty()) {
                    if (!value.data!!.state!!.name.toString().equals("null", true)) {
                        state = value.data!!.state!!.name.toString()
                    } else {
                        state = "-"
                    }
                } else {
                    state = "-"
                }
            } else {
                state = "-"
            }
        } else {
            state = "-"
        }

        if (value.data!!.pincode != null) {
            if (value.data!!.pincode.toString().isNotEmpty()) {
                if (!value.data!!.pincode.toString().equals("null", true)) {
                    pin = value.data!!.pincode.toString()
                } else {
                    pin = "-"
                }
            } else {
                pin = "-"
            }
        } else {
            pin = "-"
        }

        if (value.data!!.landmarks != null) {
            if (value.data!!.landmarks.toString().isNotEmpty()) {
                landmarks = value.data!!.landmarks.toString()
            } else {
                landmarks = "-"
            }
        } else {
            landmarks = "-"
        }

        apnaPreviewActivityBinding.locationdetails.setText(
            location + "," + landmarks + "" + "\n" + city + "" + "\n" + state + "-" + pin
        )

        if (value.data!!.trafficGenerator != null && value.data!!.trafficGenerator!!.size > 0) {
            apnaPreviewActivityBinding.recyclerViewTraffic.visibility = View.VISIBLE
            apnaPreviewActivityBinding.trafficGeneratorsNotFound.visibility = View.GONE
            trafficAdapter = PreviewTrafficAdapter(
                this,
                value.data!!.trafficGenerator as ArrayList<SurveyDetailsList.TrafficGenerator>
            )
            apnaPreviewActivityBinding.recyclerViewTraffic.adapter = trafficAdapter
        } else {
            apnaPreviewActivityBinding.recyclerViewTraffic.visibility = View.GONE
            apnaPreviewActivityBinding.trafficGeneratorsNotFound.visibility = View.VISIBLE
        }

        if (value.data!!.lat != null) {
            if (!value.data!!.lat!!.isEmpty()) {
                mapUserLats = value.data!!.lat
            } else {
                mapUserLats = "0"
            }
        } else {
            mapUserLats = "0"
        }

        if (value.data!!.long != null) {
            if (!value.data!!.long!!.isEmpty()) {
                mapUserLangs = value.data!!.long
            } else {
                mapUserLangs = "0"
            }
        } else {
            mapUserLangs = "0"
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        client = LocationServices.getFusedLocationProviderClient(this)
        supportMapFragment!!.getMapAsync { googleMap ->

            val latLng = LatLng(mapUserLats!!.toDouble(), mapUserLangs!!.toDouble())

            val options =
                MarkerOptions().position(latLng).title("i")
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    15f
                )
            )
            googleMap.addMarker(options)
        }

        if (value.data!!.lat != null) {
            if (!value.data!!.lat!!.isEmpty()) {
                apnaPreviewActivityBinding.lattitude.setText(value.data!!.lat)
            } else {
                apnaPreviewActivityBinding.lattitude.setText("-")
            }
        } else {
            apnaPreviewActivityBinding.lattitude.setText("-")
        }

        if (value.data!!.long != null) {
            if (!value.data!!.long!!.isEmpty()) {
                apnaPreviewActivityBinding.longitude.setText(value.data!!.long)
            } else {
                apnaPreviewActivityBinding.longitude.setText("-")
            }
        } else {
            apnaPreviewActivityBinding.longitude.setText("-")
        }
        if (value!!.data!!.dimensionType != null && value!!.data!!.dimensionType != null && value!!.data!!.dimensionType!!.name != null && !value!!.data!!.dimensionType!!.name!!.isEmpty()) {
            apnaPreviewActivityBinding.dimensionType.setText(value!!.data!!.dimensionType!!.name!!)
        } else {
            apnaPreviewActivityBinding.dimensionType.setText("-")

        }
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

        if (value.data!!.toiletsAvailability != null) {
            if (value.data!!.toiletsAvailability!!.name != null) {
                if (value.data!!.toiletsAvailability!!.name.toString().isNotEmpty()) {
                    if (!value.data!!.toiletsAvailability!!.name.toString().equals("null", true)) {
                        apnaPreviewActivityBinding.toiletsAvailability.setText(value.data!!.toiletsAvailability!!.name.toString())
                        toiletsAvailable = value.data!!.toiletsAvailability!!.name.toString()
                    } else {
                        apnaPreviewActivityBinding.toiletsAvailability.setText("-")
                    }
                } else {
                    apnaPreviewActivityBinding.toiletsAvailability.setText("-")
                }
            } else {
                apnaPreviewActivityBinding.toiletsAvailability.setText("-")
            }
        } else {
            apnaPreviewActivityBinding.toiletsAvailability.setText("-")
        }

        if (value.data!!.buildingAge != null && value.data!!.buildingAge != 0.0) {
            var ageofBuilding = value.data!!.buildingAge.toString()
            if (ageofBuilding!!.contains(".")) {
                apnaPreviewActivityBinding.ageOfTheBuilding.setText(
                    "${
                        ageofBuilding.substring(
                            0,
                            ageofBuilding.indexOf(".")
                        )
                    } years ${ageofBuilding.substring(ageofBuilding.indexOf(".") + 1)} months"
                )
            } else {
                apnaPreviewActivityBinding.ageOfTheBuilding.setText("${ageofBuilding} Years")
            }
        } else {
            apnaPreviewActivityBinding.ageOfTheBuilding.setText("")
        }

        if (value.data!!.parking != null) {
            apnaPreviewActivityBinding.parking.setText(value.data!!.parking!!.name)
            parkingAvailable = value.data!!.parking!!.name.toString()
        } else {
            apnaPreviewActivityBinding.parking.setText("-")
        }

        if (value.data!!.trafficStreetType != null) {
            if (value.data!!.trafficStreetType!!.name != null) {
                if (value.data!!.trafficStreetType!!.name.toString().isNotEmpty()) {
                    apnaPreviewActivityBinding.trafficStreetType.setText(value.data!!.trafficStreetType!!.name.toString())
                    trafficType = value.data!!.trafficStreetType!!.name.toString()
                } else {
                    apnaPreviewActivityBinding.trafficStreetType.setText("-")
                }
            } else {
                apnaPreviewActivityBinding.trafficStreetType.setText("-")
            }
        } else {
            apnaPreviewActivityBinding.trafficStreetType.setText("-")
        }

        if (value.data!!.expectedRent != null) {
            apnaPreviewActivityBinding.expectedrentsrft.setText(value.data!!.expectedRent.toString())
        } else {
            apnaPreviewActivityBinding.expectedrentsrft.setText("-")
        }

//        if (value.data!!.extngOutletName != null) {
//            apnaPreviewActivityBinding.existingOutletName.setText(value.data!!.extngOutletName)
//        } else {
//            apnaPreviewActivityBinding.existingOutletName.setText("-")
//        }

        if (value.data!!.eoSiteId != null) {
            apnaPreviewActivityBinding.existingOutletSiteId.setText(value.data!!.eoSiteId)
        } else {
            apnaPreviewActivityBinding.existingOutletSiteId.setText("-")
        }

        if (value.data!!.eoSiteName != null) {
            apnaPreviewActivityBinding.existingOutletSiteName.setText(value.data!!.eoSiteName)
        } else {
            apnaPreviewActivityBinding.existingOutletSiteName.setText("-")
        }

        if (value.data!!.extngOutletAge != null) {
            if (value.data!!.extngOutletAge.toString().isNotEmpty()) {
                if (!value.data!!.extngOutletAge.toString().equals("null", true)) {
                    apnaPreviewActivityBinding.existingOutletAge.setText(value.data!!.extngOutletAge.toString())
                } else {
                    apnaPreviewActivityBinding.existingOutletAge.setText("-")
                }
            } else {
                apnaPreviewActivityBinding.existingOutletAge.setText("-")
            }
        } else {
            apnaPreviewActivityBinding.existingOutletAge.setText("-")
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

        if (value.data!!.serviceClass != null) {
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
            apnaPreviewActivityBinding.morningFrom.setText(value.data!!.morningFrom.toString())
        } else {
            apnaPreviewActivityBinding.morningFrom.setText("-")
        }

        if (value.data!!.morningTo != null) {
            apnaPreviewActivityBinding.morningTo.setText(value.data!!.morningTo.toString())
        } else {
            apnaPreviewActivityBinding.morningTo.setText("-")
        }

        if (value.data!!.eveningFrom != null) {
            apnaPreviewActivityBinding.eveningFrom.setText(value.data!!.eveningFrom.toString())
        } else {
            apnaPreviewActivityBinding.eveningFrom.setText("-")
        }

        if (value.data!!.eveningTo != null) {
            apnaPreviewActivityBinding.eveningTo.setText(value.data!!.eveningTo.toString())
        } else {
            apnaPreviewActivityBinding.eveningTo.setText("-")
        }

        if (value.data!!.trafficPatterns != null && value.data!!.trafficPatterns!!.isNotEmpty()) {
            apnaPreviewActivityBinding.presentTrafficPatterns.setText(value.data!!.trafficPatterns)
        } else {
            apnaPreviewActivityBinding.presentTrafficPatterns.setText("-")
        }

        if (value.data!!.localDisbtsComments != null) {
            apnaPreviewActivityBinding.localdistubutorcomment.setText(value.data!!.localDisbtsComments.toString())
        } else {
            apnaPreviewActivityBinding.localdistubutorcomment.setText("-")
        }

        // Key Features
        if (toiletsAvailable.equals("yes", true)) {
            apnaPreviewActivityBinding.toiletsAvailable.visibility = View.VISIBLE
            apnaPreviewActivityBinding.toiletsNotAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.toiletsText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.black)
            )
        } else if (toiletsAvailable.equals("No", true)) {
            apnaPreviewActivityBinding.toiletsAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.toiletsNotAvailable.visibility = View.VISIBLE
            apnaPreviewActivityBinding.toiletsText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.grey)
            )
        } else {
            apnaPreviewActivityBinding.toiletsAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.toiletsNotAvailable.visibility = View.VISIBLE
            apnaPreviewActivityBinding.toiletsText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.grey)
            )
        }

        if (parkingAvailable.equals("Yes", true)) {
            apnaPreviewActivityBinding.parkingAvailable.visibility = View.VISIBLE
            apnaPreviewActivityBinding.parkingNotAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.parkingText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.black)
            )
        } else if (parkingAvailable.equals("No", true)) {
            apnaPreviewActivityBinding.parkingAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.parkingNotAvailable.visibility = View.VISIBLE
            apnaPreviewActivityBinding.parkingText.setTextColor(
                ContextCompat.getColor(applicationContext, R.color.grey)
            )
        }

        if (trafficType.equals("Low", true)) {
            apnaPreviewActivityBinding.trafficLow.visibility = View.VISIBLE
            apnaPreviewActivityBinding.trafficMedium.visibility = View.GONE
            apnaPreviewActivityBinding.trafficHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficVeryHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficNotAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.trafficText.setText("Traffic Low")
        } else if (trafficType.equals("Medium", true)) {
            apnaPreviewActivityBinding.trafficLow.visibility = View.GONE
            apnaPreviewActivityBinding.trafficMedium.visibility = View.VISIBLE
            apnaPreviewActivityBinding.trafficHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficVeryHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficNotAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.trafficText.setText("Traffic Medium")
        } else if (trafficType.equals("High", true)) {
            apnaPreviewActivityBinding.trafficLow.visibility = View.GONE
            apnaPreviewActivityBinding.trafficMedium.visibility = View.GONE
            apnaPreviewActivityBinding.trafficHigh.visibility = View.VISIBLE
            apnaPreviewActivityBinding.trafficVeryHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficNotAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.trafficText.setText("Traffic High")
        } else if (trafficType.equals("V.High", true)) {
            apnaPreviewActivityBinding.trafficLow.visibility = View.GONE
            apnaPreviewActivityBinding.trafficMedium.visibility = View.GONE
            apnaPreviewActivityBinding.trafficHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficNotAvailable.visibility = View.GONE
            apnaPreviewActivityBinding.trafficVeryHigh.visibility = View.VISIBLE
            apnaPreviewActivityBinding.trafficText.setText("Traffic V.High")
        } else {
            apnaPreviewActivityBinding.trafficLow.visibility = View.GONE
            apnaPreviewActivityBinding.trafficMedium.visibility = View.GONE
            apnaPreviewActivityBinding.trafficHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficVeryHigh.visibility = View.GONE
            apnaPreviewActivityBinding.trafficNotAvailable.visibility = View.VISIBLE
            apnaPreviewActivityBinding.trafficText.setText("Traffic")
            apnaPreviewActivityBinding.trafficText.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.grey
                )
            )
        }

        // Category Sale
        if (value.data!!.csPharma != null) {
            apnaPreviewActivityBinding.pharmaAvailableLayout.visibility = View.VISIBLE
            apnaPreviewActivityBinding.pharmaNotAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.pharmaValue.setText(
                value.data!!.csPharma!!.toString()
                    .substringBefore('.') + "%"
            )
        } else {
            apnaPreviewActivityBinding.pharmaAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.pharmaNotAvailableLayout.visibility = View.VISIBLE
        }

        if (value.data!!.csFmcg != null) {
            apnaPreviewActivityBinding.fmcgAvailableLayout.visibility = View.VISIBLE
            apnaPreviewActivityBinding.fmcgNotAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.fmcgValue.setText(
                value.data!!.csFmcg!!.toString()
                    .substringBefore('.') + "%"
            )
        } else {
            apnaPreviewActivityBinding.fmcgAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.fmcgNotAvailableLayout.visibility = View.VISIBLE
        }

        if (value.data!!.csSurgicals != null) {
            apnaPreviewActivityBinding.surgicalsAvailableLayout.visibility = View.VISIBLE
            apnaPreviewActivityBinding.surgicalsNotAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.surgicalsValue.setText(
                value.data!!.csSurgicals!!.toString()
                    .substringBefore('.') + "%"
            )
        } else {
            apnaPreviewActivityBinding.surgicalsAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.surgicalsNotAvailableLayout.visibility = View.VISIBLE
        }

        if (value.data!!.areaDiscount != null) {
            apnaPreviewActivityBinding.areaDiscountAvailableLayout.visibility = View.VISIBLE
            apnaPreviewActivityBinding.areaDiscountNotAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.areaDiscountValue.setText(
                value.data!!.areaDiscount!!.toString()
                    .substringBefore('.') + "%"
            )
        } else {
            apnaPreviewActivityBinding.areaDiscountAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.areaDiscountNotAvailableLayout.visibility =
                View.VISIBLE
        }
    }

    override fun onClick(value: Int, url: String) {
        val i = Intent(this, ApnaVideoPreview::class.java)
        i.putExtra("activity", url)
//        i.setType("video/mp4")
//        startActivityForResult(i, 210)
        startActivity(i)


    }

    override fun onItemClick(position: Int, imagePath: String, name: String) {
        PopUpWIndow(
            this,
            R.layout.layout_image_fullview,
            View(this),
            imagePath,
            null,
            name,
            position
        )

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
            elapsedHours, elapsedMinutes, elapsedSeconds
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onFailuregetSurveyWiseDetails(value: SurveyDetailsList) {
    }


}