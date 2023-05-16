package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaSurveyPreviewBinding
import com.apollopharmacy.vishwam.databinding.PreviewImageDialogBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import com.bumptech.glide.Glide
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

        // Location Details
        val lat = surveyCreateRequest.lat
        val long = surveyCreateRequest.long
        var location = ""
        var landMarks = ""
        var city = ""
        var state = ""
        var pin = ""

        if (surveyCreateRequest.location2 != null) {
            location = surveyCreateRequest.location2.toString()
        } else {
            location = "-"
        }
        if (surveyCreateRequest.landmarks != null) {
            landMarks = surveyCreateRequest.landmarks.toString()
        } else {
            landMarks = "-"
        }
        if (surveyCreateRequest.city2 != null) {
            city = surveyCreateRequest.city2.toString()
        } else {
            city = "-"
        }
        if (surveyCreateRequest.state2 != null) {
            state = surveyCreateRequest.state2.toString()
        } else {
            state = "-"
        }
        if (surveyCreateRequest.pincode != null) {
            pin = surveyCreateRequest.pincode.toString()
        } else {
            pin = "-"
        }

        activityApnaSurveyPreviewBinding.locationdetails.setText(
            location + "," + landMarks + "," + "\n" + city + "," + "\n" + state + "-" + pin
        )
        if (lat != null) {
            activityApnaSurveyPreviewBinding.lattitude.setText(lat)
        } else {
            activityApnaSurveyPreviewBinding.lattitude.setText("-")
        }
        if (long != null) {
            activityApnaSurveyPreviewBinding.longitude.setText(long)
        } else {
            activityApnaSurveyPreviewBinding.longitude.setText("-")
        }

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
            activityApnaSurveyPreviewBinding.expectedRent.setText(surveyCreateRequest.expectedRent.toString())
        } else {
            activityApnaSurveyPreviewBinding.expectedRent.setText("-")
        }

        if (surveyCreateRequest.securityDeposit != null) {
            activityApnaSurveyPreviewBinding.securityDeposit.setText(surveyCreateRequest.securityDeposit.toString())
        } else {
            activityApnaSurveyPreviewBinding.securityDeposit.setText("-")
        }

        if (surveyCreateRequest.toiletsAvailability != null) {
            val toiletsAvailability: SurveyCreateRequest.ToiletsAvailability =
                surveyCreateRequest.toiletsAvailability!!
            if (toiletsAvailability.uid!!.isNotEmpty()) {
                toiletsAvailable = toiletsAvailability.uid.toString()
                activityApnaSurveyPreviewBinding.toiletsAvailability.setText(toiletsAvailability.uid)
            } else {
                activityApnaSurveyPreviewBinding.toiletsAvailability.setText("-")
            }
        } else {
            activityApnaSurveyPreviewBinding.toiletsAvailability.setText("-")
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
            activityApnaSurveyPreviewBinding.ageOfTheBuilding.setText(surveyCreateRequest.buildingAge)
        } else {
            activityApnaSurveyPreviewBinding.ageOfTheBuilding.setText("-")
        }

        if (surveyCreateRequest.parking != null) {
            val parking: SurveyCreateRequest.Parking = surveyCreateRequest.parking!!
            if (parking.uid!!.isNotEmpty()) {
                parkingAvailable = parking.uid.toString()
                activityApnaSurveyPreviewBinding.parking.setText(parking.uid)
            } else {
                activityApnaSurveyPreviewBinding.parking.setText("-")
            }
        } else {
            activityApnaSurveyPreviewBinding.parking.setText("-")
        }

        if (surveyCreateRequest.trafficStreetType != null && surveyCreateRequest.trafficStreetType!!.uid != "") {
            trafficType = surveyCreateRequest.trafficStreetType!!.uid.toString()
            activityApnaSurveyPreviewBinding.trafficStreetType.setText(surveyCreateRequest.trafficStreetType!!.uid)
        } else {
            activityApnaSurveyPreviewBinding.trafficStreetType.setText("-")
        }

        val inputDateFormat = SimpleDateFormat("HH:mm:ss")
        val outputDateFormat = SimpleDateFormat("HH:mm:ss")

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
        if (surveyCreateRequest.extngOutletName != null && surveyCreateRequest.extngOutletName != "") {
            activityApnaSurveyPreviewBinding.existingOutletName.setText(surveyCreateRequest.extngOutletName)
        } else {
            activityApnaSurveyPreviewBinding.existingOutletName.setText("-")
        }

        if (surveyCreateRequest.extngOutletAge != null) {
            activityApnaSurveyPreviewBinding.existingOutletAge.setText(surveyCreateRequest.extngOutletAge.toString())
        } else {
            activityApnaSurveyPreviewBinding.existingOutletAge.setText("-")
        }

        if (surveyCreateRequest.csPharma != null) {
            activityApnaSurveyPreviewBinding.pharma.setText(surveyCreateRequest.csPharma.toString())
        } else {
            activityApnaSurveyPreviewBinding.pharma.setText("-")
        }

        if (surveyCreateRequest.csFmcg != null) {
            activityApnaSurveyPreviewBinding.fmcg.setText(surveyCreateRequest.csFmcg.toString())
        } else {
            activityApnaSurveyPreviewBinding.fmcg.setText("-")
        }

        if (surveyCreateRequest.csSurgicals != null) {
            activityApnaSurveyPreviewBinding.surgicals.setText(surveyCreateRequest.csSurgicals.toString())
        } else {
            activityApnaSurveyPreviewBinding.surgicals.setText("-")
        }

        if (surveyCreateRequest.areaDiscount != null) {
            activityApnaSurveyPreviewBinding.areaDiscount.setText(surveyCreateRequest.areaDiscount.toString())
        } else {
            activityApnaSurveyPreviewBinding.areaDiscount.setText("-")
        }

        if (surveyCreateRequest.neighboringStore != null) {
            if (surveyCreateRequest.neighboringStore!!.size > 0) {
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.neighbouringStoreNotFound.visibility =
                    View.GONE
                neighboringStores = surveyCreateRequest.neighboringStore as ArrayList<SurveyCreateRequest.NeighboringStore>

                val filteredList: ArrayList<SurveyCreateRequest.NeighboringStore> = neighboringStores.stream().filter {
                    i -> i.store != null || i.rent != null || i.sales != null || i.sqft != null
                }.collect(Collectors.toList()) as ArrayList<SurveyCreateRequest.NeighboringStore>

                val neighbouringStorePreviewAdapter = NeighbouringStorePreviewAdapter(
                    this@ApnaSurveyPreviewActivity,
                    filteredList
                )
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.adapter =
                    neighbouringStorePreviewAdapter
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)
            } else {
                activityApnaSurveyPreviewBinding.neighbourRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.neighbouringStoreNotFound.visibility =
                    View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.neighbourRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.neighbouringStoreNotFound.visibility =
                View.VISIBLE
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
            activityApnaSurveyPreviewBinding.serviceClass.setText(surveyCreateRequest.serviceClass.toString())
        } else {
            activityApnaSurveyPreviewBinding.serviceClass.setText("-")
        }

        if (surveyCreateRequest.businessClass != null) {
            activityApnaSurveyPreviewBinding.businessClass.setText(surveyCreateRequest.businessClass.toString())
        } else {
            activityApnaSurveyPreviewBinding.businessClass.setText("-")
        }

        if (surveyCreateRequest.trafficGenerator != null) {
            if (surveyCreateRequest.trafficGenerator!!.size > 0) {
                activityApnaSurveyPreviewBinding.trafficGeneratorRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.trafficGeneratorsNotFound.visibility = View.GONE
                trafficGenerators =
                    surveyCreateRequest.trafficGenerator as ArrayList<SurveyCreateRequest.TrafficGenerator>
                val trafficGeneratorPreviewAdapter = TrafficGeneratorPreviewAdapter(
                    this@ApnaSurveyPreviewActivity,
                    trafficGenerators
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
                activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.chemistTotal.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.chemistListNotFound.visibility = View.GONE
                chemist = surveyCreateRequest.chemist as ArrayList<SurveyCreateRequest.Chemist>
                val competitorsDetailsPreviewAdapter = CompetitorsDetailsPreviewAdapter(
                    this@ApnaSurveyPreviewActivity,
                    chemist
                )
                activityApnaSurveyPreviewBinding.chemistRecyclerView.adapter =
                    competitorsDetailsPreviewAdapter
                activityApnaSurveyPreviewBinding.chemistRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)

                val totalOrgAvgSale = chemist.stream().map { it.orgAvgSale }.mapToInt { it!!.toInt() }.sum()
                val totalUnorgAvgSale = chemist.stream().map { it.unorgAvgSale }.mapToInt { it!!.toInt() }.sum()
                activityApnaSurveyPreviewBinding.totalOrganized.setText(totalOrgAvgSale.toString())
                activityApnaSurveyPreviewBinding.totalUnorganized.setText(totalUnorgAvgSale.toString())

            } else {
                activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.chemistListNotFound.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.chemistTotal.visibility = View.GONE
            }
        } else {
            activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.chemistListNotFound.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.chemistTotal.visibility = View.GONE
        }

        // Population and houses
        if (surveyCreateRequest.apartments != null) {
            if (surveyCreateRequest.apartments!!.size > 0) {
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.apartmentsListNotFound.visibility = View.GONE
                apartments =
                    surveyCreateRequest.apartments as ArrayList<SurveyCreateRequest.Apartment>
                val apartmentsPreviewAdapter = ApartmentsPreviewAdapter(
                    this@ApnaSurveyPreviewActivity,
                    apartments
                )
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.adapter =
                    apartmentsPreviewAdapter
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)
            } else {
                activityApnaSurveyPreviewBinding.apartmnetRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.apartmentsListNotFound.visibility = View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.apartmnetRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.apartmentsListNotFound.visibility = View.VISIBLE
        }

       // Hospitals
        if (surveyCreateRequest.hospitals != null) {
            if (surveyCreateRequest.hospitals!!.size > 0) {
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.visibility = View.VISIBLE
                activityApnaSurveyPreviewBinding.hospitalListNotFound.visibility = View.GONE
                hospitals = surveyCreateRequest.hospitals as ArrayList<SurveyCreateRequest.Hospital>
                val hospitalsPreviewAdapter = HospitalsPreviewAdapter(
                    this@ApnaSurveyPreviewActivity,
                    hospitals
                )
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.adapter =
                    hospitalsPreviewAdapter
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.layoutManager =
                    LinearLayoutManager(this@ApnaSurveyPreviewActivity)
            } else {
                activityApnaSurveyPreviewBinding.hospitalRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.hospitalListNotFound.visibility = View.VISIBLE
            }
        } else {
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
                   this@ApnaSurveyPreviewActivity,
                   this@ApnaSurveyPreviewActivity,
                   images
               )
               activityApnaSurveyPreviewBinding.imageRecyclerView.adapter =
                   imagePreviewAdapter
               activityApnaSurveyPreviewBinding.imageRecyclerView.layoutManager =
                   LinearLayoutManager(
                       this@ApnaSurveyPreviewActivity,
                       LinearLayoutManager.HORIZONTAL,
                       false
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
                    this@ApnaSurveyPreviewActivity,
                    this@ApnaSurveyPreviewActivity,
                    videos
                )
                activityApnaSurveyPreviewBinding.videoRecyclerView.adapter =
                    videoPreviewAdapter
                activityApnaSurveyPreviewBinding.videoRecyclerView.layoutManager =
                    LinearLayoutManager(
                        this@ApnaSurveyPreviewActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
            } else {
                activityApnaSurveyPreviewBinding.videoRecyclerView.visibility = View.GONE
                activityApnaSurveyPreviewBinding.noVideoAvailable.visibility = View.VISIBLE
            }
        } else {
            activityApnaSurveyPreviewBinding.videoRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.noVideoAvailable.visibility = View.VISIBLE
        }
    }

    private fun getCurrentLocation(lat: String?, long: String?) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
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
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 10F))
                        } else {
                        }
                    }
                })
            }
        })
    }

    override fun onClick(imageUrl: String) {
        val dialog = Dialog(this@ApnaSurveyPreviewActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val previewImageDialogBinding = DataBindingUtil.inflate<PreviewImageDialogBinding>(
            LayoutInflater.from(this@ApnaSurveyPreviewActivity),
            R.layout.preview_image_dialog,
            null,
            false
        )
        dialog.setContentView(previewImageDialogBinding.root)
        Glide.with(this@ApnaSurveyPreviewActivity).load(imageUrl)
            .placeholder(R.drawable.placeholder_image).into(previewImageDialogBinding.previewImage)
        previewImageDialogBinding.close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onClickPlay(video: String) {
        val intent = Intent(this@ApnaSurveyPreviewActivity, VideoPreviewActivity::class.java)
        intent.putExtra("VIDEO_URI", video)
        startActivity(intent)
    }
}