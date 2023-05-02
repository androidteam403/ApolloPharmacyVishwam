package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaSurveyPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
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

class ApnaSurveyPreviewActivity : AppCompatActivity() {

    lateinit var activityApnaSurveyPreviewBinding: ActivityApnaSurveyPreviewBinding
    var surveyCreateRequest = SurveyCreateRequest()
    var trafficGenerators = ArrayList<SurveyCreateRequest.TrafficGenerator>()
    var neighboringStores = ArrayList<SurveyCreateRequest.NeighboringStore>()
    var chemist = ArrayList<SurveyCreateRequest.Chemist>()
    var apartments = ArrayList<SurveyCreateRequest.Apartment>()
    var hospitals = ArrayList<SurveyCreateRequest.Hospital>()
    var images = ArrayList<SurveyCreateRequest.SiteImageMb.Image>()

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var client: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityApnaSurveyPreviewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_apna_survey_preview)
        setUp()
    }

    private fun setUp() {

        if (intent != null) {
            surveyCreateRequest =
                intent.getSerializableExtra("SURVEY_REQUEST") as SurveyCreateRequest
        }

        activityApnaSurveyPreviewBinding.backButton.setOnClickListener {
            finish()
        }
        activityApnaSurveyPreviewBinding.closeIcon.setOnClickListener {
            finish()
        }

        // Location Details
        var lat = surveyCreateRequest.lat
        var long = surveyCreateRequest.long

        activityApnaSurveyPreviewBinding.locationText.setText(surveyCreateRequest.location2)
        activityApnaSurveyPreviewBinding.stateText.setText(surveyCreateRequest.state2)
        activityApnaSurveyPreviewBinding.cityText.setText(surveyCreateRequest.city2)
        activityApnaSurveyPreviewBinding.pinText.setText(surveyCreateRequest.pincode)
        activityApnaSurveyPreviewBinding.nearByLandmarksText.setText(surveyCreateRequest.landmarks)
        activityApnaSurveyPreviewBinding.latitude.setText(lat)
        activityApnaSurveyPreviewBinding.longitude.setText(long)

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
        activityApnaSurveyPreviewBinding.lengthText.setText(surveyCreateRequest.length)
        activityApnaSurveyPreviewBinding.widthText.setText(surveyCreateRequest.width)
        activityApnaSurveyPreviewBinding.ceilingHeightText.setText(surveyCreateRequest.ceilingHeight)

        if (surveyCreateRequest.expectedRent != null) {
            activityApnaSurveyPreviewBinding.expectedRentText.setText(surveyCreateRequest.expectedRent.toString())
        } else {
            activityApnaSurveyPreviewBinding.expectedRentText.setText("")
        }

        if (surveyCreateRequest.dimensionType != null) {
            val dimensionType: SurveyCreateRequest.DimensionType =
                surveyCreateRequest.dimensionType!!
            if (dimensionType.uid!!.isNotEmpty()) {
                if (dimensionType.uid.equals("per sq.ft", true)) {
                    activityApnaSurveyPreviewBinding.expectedRentRadioGroup.check(R.id.perSqFtRadioButton)
                } else {
                    activityApnaSurveyPreviewBinding.expectedRentRadioGroup.check(R.id.inchRadioButton)
                }
            }
        }

        if (surveyCreateRequest.securityDeposit != null) {
            activityApnaSurveyPreviewBinding.securityDepositText.setText(surveyCreateRequest.securityDeposit.toString())
        }

        if (surveyCreateRequest.toiletsAvailability != null) {
            val toiletsAvailability: SurveyCreateRequest.ToiletsAvailability =
                surveyCreateRequest.toiletsAvailability!!
            if (toiletsAvailability.uid!!.isNotEmpty()) {
                if (toiletsAvailability.uid!!.equals("yes", true)) {
                    activityApnaSurveyPreviewBinding.toiletsAvailabilityYes.isChecked = true
                } else {
                    activityApnaSurveyPreviewBinding.toiletsAvailabilityNo.isChecked = true
                }
            }
        }

        activityApnaSurveyPreviewBinding.ageOfTheBuildingText.setText(surveyCreateRequest.buildingAge)

        if (surveyCreateRequest.parking != null) {
            val parking: SurveyCreateRequest.Parking = surveyCreateRequest.parking!!
            if (parking.uid!!.isNotEmpty()) {
                if (parking.uid!!.equals("yes", true)) {
                    activityApnaSurveyPreviewBinding.yesRadioButton.isChecked = true
                } else {
                    activityApnaSurveyPreviewBinding.noRadioButton.isChecked = true
                }
            }
        }

        if (surveyCreateRequest.trafficStreetType != null) {
            activityApnaSurveyPreviewBinding.trafficStreetSelect.setText(surveyCreateRequest.trafficStreetType!!.uid)
        }
        activityApnaSurveyPreviewBinding.morningFromSelect.setText(surveyCreateRequest.morningFrom)
        activityApnaSurveyPreviewBinding.morningToSelect.setText(surveyCreateRequest.morningTo)
        activityApnaSurveyPreviewBinding.eveningFromSelect.setText(surveyCreateRequest.eveningFrom)
        activityApnaSurveyPreviewBinding.eveningToSelect.setText(surveyCreateRequest.eveningTo)
        activityApnaSurveyPreviewBinding.presentTrafficPatterns.setText(surveyCreateRequest.trafficPatterns)

        // Market information
        activityApnaSurveyPreviewBinding.existingOutletName.setText(surveyCreateRequest.extngOutletName)
        if (surveyCreateRequest.extngOutletAge != null) {
            activityApnaSurveyPreviewBinding.ageOrSaleText.setText(surveyCreateRequest.extngOutletAge.toString())
        } else {
            activityApnaSurveyPreviewBinding.ageOrSaleText.setText("")
        }

        if (surveyCreateRequest.csPharma != null) {
            activityApnaSurveyPreviewBinding.pharmaText.setText(surveyCreateRequest.csPharma.toString())
        } else {
            activityApnaSurveyPreviewBinding.pharmaText.setText("")
        }

        if (surveyCreateRequest.csFmcg != null) {
            activityApnaSurveyPreviewBinding.fmcgText.setText(surveyCreateRequest.csFmcg.toString())
        } else {
            activityApnaSurveyPreviewBinding.fmcgText.setText("")
        }

        if (surveyCreateRequest.csSurgicals != null) {
            activityApnaSurveyPreviewBinding.surgicalsText.setText(surveyCreateRequest.csSurgicals.toString())
        } else {
            activityApnaSurveyPreviewBinding.surgicalsText.setText("")
        }

        if (surveyCreateRequest.areaDiscount != null) {
            activityApnaSurveyPreviewBinding.areaDiscountText.setText(surveyCreateRequest.areaDiscount.toString())
        } else {
            activityApnaSurveyPreviewBinding.areaDiscountText.setText("")
        }
        activityApnaSurveyPreviewBinding.distributorsComments.setText(surveyCreateRequest.localDisbtsComments)

        if (surveyCreateRequest.serviceClass != null) {
            activityApnaSurveyPreviewBinding.serviceClassText.setText(surveyCreateRequest.serviceClass.toString())
        } else {
            activityApnaSurveyPreviewBinding.serviceClassText.setText("")
        }

        if (surveyCreateRequest.businessClass != null) {
            activityApnaSurveyPreviewBinding.businessClassText.setText(surveyCreateRequest.businessClass.toString())
        } else {
            activityApnaSurveyPreviewBinding.businessClassText.setText("")
        }

        if (surveyCreateRequest.neighboringStore != null) {
            activityApnaSurveyPreviewBinding.neighbouringStoreRcv.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.neighbouringStoreListNotFound.visibility = View.GONE
            neighboringStores =
                surveyCreateRequest.neighboringStore as ArrayList<SurveyCreateRequest.NeighboringStore>
            val neighbouringStorePreviewAdapter = NeighbouringStorePreviewAdapter(
                this@ApnaSurveyPreviewActivity,
                neighboringStores
            )
            activityApnaSurveyPreviewBinding.neighbouringStoreRcv.adapter =
                neighbouringStorePreviewAdapter
            activityApnaSurveyPreviewBinding.neighbouringStoreRcv.layoutManager =
                LinearLayoutManager(this@ApnaSurveyPreviewActivity)
        } else {
            activityApnaSurveyPreviewBinding.neighbouringStoreRcv.visibility = View.GONE
            activityApnaSurveyPreviewBinding.neighbouringStoreListNotFound.visibility =
                View.VISIBLE
        }

        if (surveyCreateRequest.trafficGenerator != null) {
            activityApnaSurveyPreviewBinding.trafficGeneratorsRcv.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.trafficGeneratorsNotFound.visibility = View.GONE
            trafficGenerators =
                surveyCreateRequest.trafficGenerator as ArrayList<SurveyCreateRequest.TrafficGenerator>
            val trafficGeneratorPreviewAdapter = TrafficGeneratorPreviewAdapter(
                this@ApnaSurveyPreviewActivity,
                trafficGenerators
            )
            activityApnaSurveyPreviewBinding.trafficGeneratorsRcv.adapter =
                trafficGeneratorPreviewAdapter
            activityApnaSurveyPreviewBinding.trafficGeneratorsRcv.layoutManager =
                LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false
                )
        } else {
            activityApnaSurveyPreviewBinding.trafficGeneratorsRcv.visibility = View.GONE
            activityApnaSurveyPreviewBinding.trafficGeneratorsNotFound.visibility = View.VISIBLE
        }

        // Competitors details
        if (surveyCreateRequest.chemist != null) {
            activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.VISIBLE
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
        } else {
            activityApnaSurveyPreviewBinding.chemistRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.chemistListNotFound.visibility = View.VISIBLE
        }

        // Population and houses
        if (surveyCreateRequest.apartments != null) {
            activityApnaSurveyPreviewBinding.apartmentsRecyclerView.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.apartmentListNotFound.visibility = View.GONE
            apartments = surveyCreateRequest.apartments as ArrayList<SurveyCreateRequest.Apartment>
            val apartmentsPreviewAdapter = ApartmentsPreviewAdapter(
                this@ApnaSurveyPreviewActivity,
                apartments
            )
            activityApnaSurveyPreviewBinding.apartmentsRecyclerView.adapter =
                apartmentsPreviewAdapter
            activityApnaSurveyPreviewBinding.apartmentsRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaSurveyPreviewActivity)
        } else {
            activityApnaSurveyPreviewBinding.apartmentsRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.apartmentListNotFound.visibility = View.VISIBLE
        }

        // Hospitals
        if (surveyCreateRequest.hospitals != null) {
            activityApnaSurveyPreviewBinding.hospitalsRecyclerView.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.hospitalsListNotFound.visibility = View.GONE
            hospitals = surveyCreateRequest.hospitals as ArrayList<SurveyCreateRequest.Hospital>
            val hospitalsPreviewAdapter = HospitalsPreviewAdapter(
                this@ApnaSurveyPreviewActivity,
                hospitals
            )
            activityApnaSurveyPreviewBinding.hospitalsRecyclerView.adapter = hospitalsPreviewAdapter
            activityApnaSurveyPreviewBinding.hospitalsRecyclerView.layoutManager =
                LinearLayoutManager(this@ApnaSurveyPreviewActivity)
        } else {
            activityApnaSurveyPreviewBinding.hospitalsRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.hospitalsListNotFound.visibility = View.VISIBLE
        }

        // photos and media
        if (surveyCreateRequest.siteImageMb!!.images!!.size > 0) {
            activityApnaSurveyPreviewBinding.siteImagesRecyclerView.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.noPhotoFound.visibility = View.GONE
            images =
                surveyCreateRequest.siteImageMb!!.images as ArrayList<SurveyCreateRequest.SiteImageMb.Image>
            val imagePreviewAdapter = ImagePreviewAdapter(
                this@ApnaSurveyPreviewActivity,
                images
            )
            activityApnaSurveyPreviewBinding.siteImagesRecyclerView.adapter =
                imagePreviewAdapter
            activityApnaSurveyPreviewBinding.siteImagesRecyclerView.layoutManager =
                LinearLayoutManager(
                    this@ApnaSurveyPreviewActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

        } else {
            activityApnaSurveyPreviewBinding.siteImagesRecyclerView.visibility = View.GONE
            activityApnaSurveyPreviewBinding.noPhotoFound.visibility = View.VISIBLE
        }

        if (surveyCreateRequest.videoMb != null) {
            activityApnaSurveyPreviewBinding.video.visibility = View.VISIBLE
            activityApnaSurveyPreviewBinding.videoNotFound.visibility = View.GONE
            if (surveyCreateRequest.videoMb!!.video != null) {
                val uri = Uri.parse(surveyCreateRequest.videoMb!!.video!![0].url)
                activityApnaSurveyPreviewBinding.afterCapturedVideo.setVideoURI(uri)
            }
        } else {
            activityApnaSurveyPreviewBinding.video.visibility = View.GONE
            activityApnaSurveyPreviewBinding.videoNotFound.visibility = View.VISIBLE
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
}