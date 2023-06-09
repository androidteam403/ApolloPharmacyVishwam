package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityApnaPreviewBinding
import com.apollopharmacy.vishwam.databinding.ApnaPreviewQuickGoDialogBinding
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter.*
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.ApnaSurveyFragment
import com.apollopharmacy.vishwam.ui.home.apna.survey.videopreview.ApnaVideoPreview
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.apollopharmacy.vishwam.util.Utlis
import com.github.mikephil.charting.charts.BarChart
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
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ApnaPreviewActivity : AppCompatActivity(), ApnaNewPreviewCallBack {
    var toiletsAvailable: String = ""
    var parkingAvailable: String = ""
    var trafficType: String = ""
    var surveyId: String = ""

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
    var currentLocationClient: FusedLocationProviderClient? = null
    var map: GoogleMap? = null
    var geocoder: Geocoder? = null
    var mapUserLats: String? = null
    var videoList = ArrayList<String>()
    var videoMbList = ArrayList<SurveyDetailsList.VideoMb>()
    lateinit var approvedOrders: SurveyListResponse.Row
    private var instance: ApnaSurveyFragment? = null

    var mapUserLangs: String? = null

    var neighborEntries = ArrayList<BarEntry>()
    var competitorsEntries = ArrayList<Entry>()
    var apartmentsEntries = ArrayList<BarEntry>()
    var stringValuesList = ArrayList<String>()

    //    var apartmentsEntryTwo = ArrayList<BarEntry>()
    var hospitalsEntries = ArrayList<BarEntry>()

    //    var hospitalsEntryTwo = ArrayList<BarEntry>()
    var sales = ArrayList<Float>()
    var stores = ArrayList<String>()
    var chemist =  ArrayList<String>()
    var apartments = ArrayList<String>()
    var avgSales = ArrayList<Float>()
    var noOfHouses = ArrayList<Float>()
    var beds = ArrayList<Float>()
    var hospitals = ArrayList<String>()

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


        apnaPreviewActivityBinding.scrollTop.setOnClickListener {
            apnaPreviewActivityBinding.scrollView.post {
                apnaPreviewActivityBinding.scrollView.fullScroll(View.FOCUS_UP)
            }
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
            if (approvedOrders.status != null) {
                if (approvedOrders.status!!.name != null) {
                    if (approvedOrders.status!!.name.toString()
                            .isNotEmpty() && !approvedOrders.status!!.name.toString().equals("null")
                    ) {
                        if (approvedOrders.status!!.name.toString().equals("New", true)) {
                            apnaPreviewActivityBinding.statusLayout.setBackgroundColor(
                                ContextCompat.getColor(this@ApnaPreviewActivity,
                                    R.color.apna_project_actionbar_color)
                            )
                        } else {
                            apnaPreviewActivityBinding.statusLayout.setBackgroundColor(Color.parseColor(
                                approvedOrders.status!!.backgroundColor))
                        }
                    } else {
                    }
                } else {
                }
            }
//            apnaPreviewActivityBinding.statusLayout.setBackgroundColor(
//                Color.parseColor(
//                    approvedOrders.status!!.backgroundColor!!
//                )
//            )

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
            if(approvedOrders.location!=null){
                if (approvedOrders.location!!.name != null) locationName =
                    approvedOrders.location!!.name!!
            }
//            if(approvedOrders.city!=null) {
//                if (approvedOrders.city!!.name != null) cityName = ", ${approvedOrders.city!!.name}"
//            }
            apnaPreviewActivityBinding.storeName.setText("$locationName$cityName")

//            if (approvedOrders.location!!.name != null) locationName =
//                approvedOrders.location!!.name!!
//            if (approvedOrders.city!!.name != null) cityName = ", ${approvedOrders.city!!.name}"

//            apnaPreviewActivityBinding.location.setText("$locationName$cityName")

            apnaPreviewActivityBinding.location.setText("-")
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val outputDateFormat = SimpleDateFormat("dd MMM, yyy")
            apnaPreviewActivityBinding.surveystart.setText(
                outputDateFormat.format(
                    inputDateFormat.parse(
                        approvedOrders.createdTime!!
                    )!!
                )
            )
//            apnaPreviewActivityBinding.surveyended.setText(
//                outputDateFormat.format(
//                    inputDateFormat.parse(
//                        approvedOrders.modifiedTime!!
//                    )!!
//                )
//            )


            if (approvedOrders.status != null) {
                if (approvedOrders.status!!.name != null) {
                    if (approvedOrders.status!!.name.toString()
                            .isNotEmpty() && !approvedOrders.status!!.name.toString().equals("null")
                    ) {
                        if (approvedOrders.status!!.name.toString().equals("Approved", true)) {
                            apnaPreviewActivityBinding.surveyEndedLayout.visibility = View.VISIBLE
                            apnaPreviewActivityBinding.surveyended.setText(outputDateFormat.format(
                                inputDateFormat.parse(approvedOrders.modifiedTime!!)!!))
                        } else {
                            apnaPreviewActivityBinding.surveyEndedLayout.visibility = View.GONE
                        }
                    } else {
                        apnaPreviewActivityBinding.surveyEndedLayout.visibility = View.GONE
                    }
                } else {
                    apnaPreviewActivityBinding.surveyEndedLayout.visibility = View.GONE
                }
            }

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            try {
                val date1 = simpleDateFormat.parse(approvedOrders.createdTime)
                val date2 = simpleDateFormat.parse(approvedOrders.modifiedTime)
                printDifference(date1, date2)
//                apnaPreviewActivityBinding.timeTaken.setText(printDifference(date1, date2))

                if (approvedOrders.status != null) {
                    if (approvedOrders.status!!.name != null) {
                        if (approvedOrders.status!!.name.toString()
                                .isNotEmpty() && !approvedOrders.status!!.name.toString()
                                .equals("null")
                        ) {
                            if (approvedOrders.status!!.name.toString().equals("Approved", true)) {
                                apnaPreviewActivityBinding.timeTakenLayout.visibility = View.VISIBLE
                                apnaPreviewActivityBinding.timeTaken.setText(printDifference(date1,
                                    date2))
                            } else {
                                apnaPreviewActivityBinding.timeTakenLayout.visibility = View.GONE
                            }
                        } else {
                            apnaPreviewActivityBinding.timeTakenLayout.visibility = View.GONE
                        }
                    } else {
                        apnaPreviewActivityBinding.timeTakenLayout.visibility = View.GONE
                    }
                }

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

//            if (approvedOrders.status != null) {
//                if (approvedOrders.status!!.icon != null) {
//
//                    if (approvedOrders.status!!.icon.equals("null") || approvedOrders.status!!.icon.isNullOrEmpty()) {
//
//                    } else {
//                        apnaPreviewActivityBinding.statusLayout.setBackgroundColor(
//                            Color.parseColor(
//                                approvedOrders.status!!.icon
//                            )
//                        )
//                    }
//                }
//            }
        }

        currentLocationClient = LocationServices.getFusedLocationProviderClient(this)

        apnaPreviewActivityBinding.quickGoIcon.setOnClickListener {
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
                    scrollToView(apnaPreviewActivityBinding.scrollView,
                        apnaPreviewActivityBinding.locationDetailsLayout)
                }
                apnaPreviewQuickGoDialogBinding.siteSpecification.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(apnaPreviewActivityBinding.scrollView,
                        apnaPreviewActivityBinding.siteSpecificationsLayout)
                }
                apnaPreviewQuickGoDialogBinding.marketInformation.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(apnaPreviewActivityBinding.scrollView,
                        apnaPreviewActivityBinding.marketInformationLayout)
                }
                apnaPreviewQuickGoDialogBinding.competitorsDetails.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(apnaPreviewActivityBinding.scrollView,
                        apnaPreviewActivityBinding.competitorsDetailsLayout)
                }
                apnaPreviewQuickGoDialogBinding.populationAndHouses.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(apnaPreviewActivityBinding.scrollView,
                        apnaPreviewActivityBinding.populationAndHousesLayout)
                }
                apnaPreviewQuickGoDialogBinding.hospitals.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(apnaPreviewActivityBinding.scrollView,
                        apnaPreviewActivityBinding.hospitalsLayout)
                }
                apnaPreviewQuickGoDialogBinding.photosAndMedia.setOnClickListener {
                    customDialog.dismiss()
                    scrollToView(apnaPreviewActivityBinding.scrollView,
                        apnaPreviewActivityBinding.photosAndMediaLayout)
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
//        setApartmentsValues()
//        setupApartmentsChart()
//        setApartmentsValuesTwo()

        // Hospitals graph
//        setHospitalsValuesOne()
//        setHospitalsValuesTwo()
//        setupHospitalsChart()
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
        barDataSet.isHighlightEnabled=true
        val barData = BarData(barDataSet)
        apnaPreviewActivityBinding.hospitalsChart.data = barData
        apnaPreviewActivityBinding.hospitalsChart.setDragEnabled(true)
        apnaPreviewActivityBinding.hospitalsChart.setScaleEnabled(true);
        apnaPreviewActivityBinding.hospitalsChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaPreviewActivity, IndexAxisValueFormatter(stringValuesList))
                mv.chartView = apnaPreviewActivityBinding.hospitalsChart // For bounds control

                apnaPreviewActivityBinding.hospitalsChart.marker = mv

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

//        apnaPreviewActivityBinding.hospitalsChart.axisLeft.isEnabled = false
        apnaPreviewActivityBinding.hospitalsChart.axisRight.isEnabled = false
        apnaPreviewActivityBinding.hospitalsChart.xAxis.isEnabled = false

        apnaPreviewActivityBinding.hospitalsChart.getAxisLeft().setLabelCount(5, true)
        apnaPreviewActivityBinding.hospitalsChart.getAxisLeft().setAxisMinimum(0f)
        apnaPreviewActivityBinding.hospitalsChart.getAxisLeft().setAxisMaximum(beds.max())

//        apnaPreviewActivityBinding.hospitalsChart.xAxis.valueFormatter =
//            IndexAxisValueFormatter(
//                listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
//            )
//        apnaPreviewActivityBinding.hospitalsChart.xAxis.setCenterAxisLabels(true)
//        apnaPreviewActivityBinding.hospitalsChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        apnaPreviewActivityBinding.hospitalsChart.xAxis.isGranularityEnabled = true
//        apnaPreviewActivityBinding.hospitalsChart.xAxis.granularity = 1f
//        apnaPreviewActivityBinding.hospitalsChart.xAxis.axisMinimum = 0f
//        apnaPreviewActivityBinding.hospitalsChart.groupBars(0f, 0.5f, 0.1f)

//        activityApnaSurveyPreviewBinding.hospitalsChart.setDragEnabled(true)
//        activityApnaSurveyPreviewBinding.hospitalsChart.setVisibleXRangeMaximum(2f)


        // removing outer line
        apnaPreviewActivityBinding.hospitalsChart.getAxisRight().setDrawAxisLine(false)
        apnaPreviewActivityBinding.hospitalsChart.getAxisLeft().setDrawAxisLine(false)
        apnaPreviewActivityBinding.hospitalsChart.getXAxis().setDrawAxisLine(false)
        // removing grid line
        apnaPreviewActivityBinding.hospitalsChart.getAxisRight().setDrawGridLines(false)
        apnaPreviewActivityBinding.hospitalsChart.getAxisLeft().setDrawGridLines(false)
        apnaPreviewActivityBinding.hospitalsChart.getXAxis().setDrawGridLines(false)

        apnaPreviewActivityBinding.hospitalsChart.description.isEnabled = false
        apnaPreviewActivityBinding.hospitalsChart.legend.isEnabled = false
        apnaPreviewActivityBinding.hospitalsChart.setTouchEnabled(true)
        apnaPreviewActivityBinding.hospitalsChart.invalidate()
    }

    private fun setHospitalsValues() {
        for (i in beds.indices) {
            hospitalsEntries.add(BarEntry(i.toFloat(), beds.get(i), hospitals.get(i).toString()))
        }
//        hospitalsEntryTwo.add(BarEntry(1f, 6f))
//        hospitalsEntryTwo.add(BarEntry(2f, 3f))
//        hospitalsEntryTwo.add(BarEntry(3f, 5f))
//        hospitalsEntryTwo.add(BarEntry(4f, 4f))
//        hospitalsEntryTwo.add(BarEntry(5f, 5f))
//        hospitalsEntryTwo.add(BarEntry(6f, 4f))
    }

//    private fun setHospitalsValuesOne() {
//        hospitalsEntryOne.add(BarEntry(1f, 5f))
//        hospitalsEntryOne.add(BarEntry(2f, 2f))
//        hospitalsEntryOne.add(BarEntry(3f, 4f))
//        hospitalsEntryOne.add(BarEntry(4f, 5f))
//        hospitalsEntryOne.add(BarEntry(5f, 3f))
//        hospitalsEntryOne.add(BarEntry(6f, 5f))
//    }

    private fun setupApartmentsChart() {
        val barDataSet = BarDataSet(apartmentsEntries, "")
        barDataSet.isHighlightEnabled=true
        val barData = BarData(barDataSet)
        apnaPreviewActivityBinding.apartmentsChart.data = barData
        apnaPreviewActivityBinding.apartmentsChart.setDragEnabled(true)
        apnaPreviewActivityBinding.apartmentsChart.setScaleEnabled(true);
        apnaPreviewActivityBinding.apartmentsChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaPreviewActivity, IndexAxisValueFormatter(stringValuesList))
                mv.chartView = apnaPreviewActivityBinding.apartmentsChart // For bounds control

                apnaPreviewActivityBinding.apartmentsChart.marker = mv
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

//        apnaPreviewActivityBinding.apartmentsChart.axisLeft.isEnabled = false
        apnaPreviewActivityBinding.apartmentsChart.axisRight.isEnabled = false
        apnaPreviewActivityBinding.apartmentsChart.xAxis.isEnabled = false

        apnaPreviewActivityBinding.apartmentsChart.getAxisLeft().setLabelCount(5, true)
        apnaPreviewActivityBinding.apartmentsChart.getAxisLeft().setAxisMinimum(0f)
        apnaPreviewActivityBinding.apartmentsChart.getAxisLeft().setAxisMaximum(noOfHouses.max())

//        apnaPreviewActivityBinding.apartmentsChart.xAxis.valueFormatter =
//            IndexAxisValueFormatter(
//                listOf("Apartments", "Houses")
//            )
//        apnaPreviewActivityBinding.apartmentsChart.xAxis.setCenterAxisLabels(true)
//        apnaPreviewActivityBinding.apartmentsChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        apnaPreviewActivityBinding.apartmentsChart.xAxis.isGranularityEnabled = true
//        apnaPreviewActivityBinding.apartmentsChart.xAxis.granularity = 1f
//        apnaPreviewActivityBinding.apartmentsChart.xAxis.axisMinimum = 0f
//        apnaPreviewActivityBinding.apartmentsChart.groupBars(0f, 0.5f, 0.1f)


        // removing outer line
        apnaPreviewActivityBinding.apartmentsChart.getAxisRight().setDrawAxisLine(false)
        apnaPreviewActivityBinding.apartmentsChart.getAxisLeft().setDrawAxisLine(false)
        apnaPreviewActivityBinding.apartmentsChart.getXAxis().setDrawAxisLine(false)
        // removing grid line
        apnaPreviewActivityBinding.apartmentsChart.getAxisRight().setDrawGridLines(false)
        apnaPreviewActivityBinding.apartmentsChart.getAxisLeft().setDrawGridLines(false)
        apnaPreviewActivityBinding.apartmentsChart.getXAxis().setDrawGridLines(false)

        apnaPreviewActivityBinding.apartmentsChart.description.isEnabled = false
        apnaPreviewActivityBinding.apartmentsChart.legend.isEnabled = false
        apnaPreviewActivityBinding.apartmentsChart.setTouchEnabled(true)
        apnaPreviewActivityBinding.apartmentsChart.invalidate()
    }

//    private fun setApartmentsValuesTwo() {
//        apartmentsEntryTwo.add(BarEntry(1f, 6f))
//        apartmentsEntryTwo.add(BarEntry(2f, 5f))
//    }

    private fun setApartmentsValues() {
        for (i in noOfHouses.indices) {
            apartmentsEntries.add(BarEntry(i.toFloat(), noOfHouses.get(i), apartments.get(i).toString()))
        }
//        apartmentsEntries.add(BarEntry(1f, 5f))
//        apartmentsEntries.add(BarEntry(2f, 4f))
    }

    private fun setupCompetitorsChart() {
        val lineDataSet = LineDataSet(competitorsEntries, "")
        lineDataSet.isHighlightEnabled=true
        val lineData = LineData(lineDataSet)
        apnaPreviewActivityBinding.competitorsChart.data = lineData
        apnaPreviewActivityBinding.competitorsChart.setDragEnabled(true)
        apnaPreviewActivityBinding.competitorsChart.setScaleEnabled(true);
        apnaPreviewActivityBinding.competitorsChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaPreviewActivity, IndexAxisValueFormatter(stringValuesList))
                mv.chartView = apnaPreviewActivityBinding.competitorsChart // For bounds control

                apnaPreviewActivityBinding.competitorsChart.marker = mv
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

        apnaPreviewActivityBinding.competitorsChart.axisRight.isEnabled = false

        apnaPreviewActivityBinding.competitorsChart.getAxisLeft().setLabelCount(5, true)
        apnaPreviewActivityBinding.competitorsChart.getAxisLeft().setAxisMinimum(0f)
        apnaPreviewActivityBinding.competitorsChart.getAxisLeft().setAxisMaximum(avgSales.max())

        apnaPreviewActivityBinding.competitorsChart.xAxis.isEnabled = false
//        apnaPreviewActivityBinding.competitorsChart.xAxis.position =
//            XAxis.XAxisPosition.BOTTOM
//        apnaPreviewActivityBinding.competitorsChart.xAxis.setCenterAxisLabels(true)
//        apnaPreviewActivityBinding.competitorsChart.xAxis.valueFormatter =
//            IndexAxisValueFormatter(
//                listOf("Jan", "Feb", "Mar")
//            )

        apnaPreviewActivityBinding.competitorsChart.description.isEnabled = false
        apnaPreviewActivityBinding.competitorsChart.legend.isEnabled = false

        // removing outer line
        apnaPreviewActivityBinding.competitorsChart.getAxisRight().setDrawAxisLine(false)
        apnaPreviewActivityBinding.competitorsChart.getAxisLeft().setDrawAxisLine(false)
        apnaPreviewActivityBinding.competitorsChart.getXAxis().setDrawAxisLine(false)
        // removing grid line
        apnaPreviewActivityBinding.competitorsChart.getAxisRight().setDrawGridLines(false)
        apnaPreviewActivityBinding.competitorsChart.getAxisLeft().setDrawGridLines(false)
        apnaPreviewActivityBinding.competitorsChart.getXAxis().setDrawGridLines(false)

        apnaPreviewActivityBinding.competitorsChart.setTouchEnabled(true)
        apnaPreviewActivityBinding.competitorsChart.invalidate()
    }

    private fun setCompetitorsValues() {
        for (i in avgSales.indices) {
            competitorsEntries.add(Entry(i.toFloat(), avgSales.get(i), chemist.get(i).toString()))
        }
//        competitorsEntries.add(Entry(0f, 5f))
//        competitorsEntries.add(Entry(1f, 7f))
//        competitorsEntries.add(Entry(2f, 8f))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNeighborChart() {
        val barDataSet = BarDataSet(neighborEntries, "")
        barDataSet.isHighlightEnabled=true
        val barData = BarData(barDataSet)
        apnaPreviewActivityBinding.neighborChart.setDragEnabled(true)
        apnaPreviewActivityBinding.neighborChart.setScaleEnabled(true);
        apnaPreviewActivityBinding.neighborChart.data= barData
        apnaPreviewActivityBinding.neighborChart.setDrawValueAboveBar(true)
        apnaPreviewActivityBinding.neighborChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                apnaPreviewActivityBinding.neighborChart.tooltipText=e!!.y.toString()
//               Toast.makeText(this@ApnaPreviewActivity, "test", Toast.LENGTH_SHORT).show()
                stringValuesList.add("test")
                val mv = XYMarkerView(this@ApnaPreviewActivity, IndexAxisValueFormatter(stringValuesList))
                mv.chartView = apnaPreviewActivityBinding.neighborChart // For bounds control

                apnaPreviewActivityBinding.neighborChart.marker = mv
            }

            override fun onNothingSelected() {

            }

        })


//        apnaPreviewActivityBinding.neighborChart.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN ->{
//                        apnaPreviewActivityBinding.neighborChart.tooltipText = barDataSet.values.get(0).y.toString()
//                    }
//
//                }
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })




        // Set bar colors
        for (i in sales.indices) {
            barDataSet.colors = listOf(
                Color.parseColor("#f7941d"),
                Color.parseColor("#8dc73e"),
                Color.parseColor("#03a99e"),
                Color.parseColor("#00b0ee")
            )
        }
//        barDataSet.colors = listOf(
//            Color.parseColor("#f7941d"),
//            Color.parseColor("#8dc73e"),
//            Color.parseColor("#03a99e"),
//            Color.parseColor("#00b0ee")
//        )

        // Remove values top of the bar
        barDataSet.setDrawValues(false)
        // Bar width
        if (sales.size > 2) {
            barData.barWidth = 0.5f
        } else {
            barData.barWidth = 0.1f
        }

        apnaPreviewActivityBinding.neighborChart.axisRight.isEnabled = false
        apnaPreviewActivityBinding.neighborChart.xAxis.isEnabled = false
//        apnaPreviewActivityBinding.neighborChart.isDragEnabled = true
//        apnaPreviewActivityBinding.neighborChart.setVisibleXRangeMaximum(3f)
        // Set x axis values
//        apnaPreviewActivityBinding.neighborChart.xAxis.isEnabled = true
//        apnaPreviewActivityBinding.neighborChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        apnaPreviewActivityBinding.neighborChart.xAxis.valueFormatter = IndexAxisValueFormatter(stores)
//        apnaPreviewActivityBinding.neighborChart.xAxis.textSize = 0.5f
        // Set y axis values
        apnaPreviewActivityBinding.neighborChart.getAxisLeft().setLabelCount(5, true)
        apnaPreviewActivityBinding.neighborChart.getAxisLeft().setAxisMinimum(0f)
        apnaPreviewActivityBinding.neighborChart.getAxisLeft().setAxisMaximum(sales.max())
        // Disable touch
        apnaPreviewActivityBinding.neighborChart.setTouchEnabled(true)
        // Remove description
        apnaPreviewActivityBinding.neighborChart.description.isEnabled = false
        // Remove legend
        apnaPreviewActivityBinding.neighborChart.legend.isEnabled = false
        // Remove outer line
        apnaPreviewActivityBinding.neighborChart.axisRight.setDrawAxisLine(false)
        apnaPreviewActivityBinding.neighborChart.axisLeft.setDrawAxisLine(false)
        apnaPreviewActivityBinding.neighborChart.xAxis.setDrawAxisLine(false)
        // Remove grid line
        apnaPreviewActivityBinding.neighborChart.axisRight.setDrawGridLines(false)
        apnaPreviewActivityBinding.neighborChart.axisLeft.setDrawGridLines(false)
        apnaPreviewActivityBinding.neighborChart.xAxis.setDrawGridLines(false)

        apnaPreviewActivityBinding.neighborChart.invalidate()
    }

    private fun setNeighborChartValues() {
        for (i in sales.indices) {
            neighborEntries.add(BarEntry(i.toFloat(), sales.get(i), stores.get(i).toString()))
        }
    //        neighborEntries.add(BarEntry(1f, 18f))
//        neighborEntries.add(BarEntry(2f, 13f))
//        neighborEntries.add(BarEntry(3f, 11f))
//        neighborEntries.add(BarEntry(4f, 5f))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onSuccessgetSurveyDetails(value: SurveyDetailsList) {
        if (value.data!!.id != null) {
            apnaPreviewActivityBinding.surveyId.setText(value.data!!.id)
        } else {
            apnaPreviewActivityBinding.surveyId.setText("-")
        }
        if (value.data!!.id != null) {
            if (!value.data!!.id.equals("null")) {
                surveyId = value.data!!.id.toString()
            }
        }
        if (value.data!!.apartments != null && value.data!!.apartments!!.size > 0) {
            apnaPreviewActivityBinding.apartmentsHeader.visibility = View.VISIBLE
            apnaPreviewActivityBinding.recyclerViewapartmnet.visibility = View.VISIBLE
            apnaPreviewActivityBinding.apartmentsNotFound.visibility = View.GONE
            apartmentAdapter = PreviewApartmentAdapter(
                this,
                value.data!!.apartments as ArrayList<SurveyDetailsList.Apartment>
            )
            apnaPreviewActivityBinding.recyclerViewapartmnet.adapter = apartmentAdapter

            noOfHouses =
                value.data!!.apartments!!.map { it.noHouses!!.toFloat() } as ArrayList<Float>
            apartments = value.data!!.apartments!!.map { it.apartments!!.toString() } as ArrayList<String>
            setApartmentsValues()
            setupApartmentsChart()
        } else {
            apnaPreviewActivityBinding.apartmentsHeader.visibility = View.GONE
            apnaPreviewActivityBinding.recyclerViewapartmnet.visibility = View.GONE
            apnaPreviewActivityBinding.apartmentsNotFound.visibility = View.VISIBLE
        }

        if (value.data!!.chemist != null && value.data!!.chemist!!.size > 0) {
            apnaPreviewActivityBinding.chemistHeader.visibility = View.VISIBLE
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

            chemist = value.data!!.chemist!!.map { it.chemist.toString() } as ArrayList<String>

            val total = totalOrg + totalUnorg

            apnaPreviewActivityBinding.organized.setText("\u20B9" + DecimalFormat("##,##,##0").format(
                totalOrg.toLong()))
            apnaPreviewActivityBinding.unorganized.setText("\u20B9" + DecimalFormat("##,##,##0").format(
                totalUnorg.toLong()))
            apnaPreviewActivityBinding.total.setText("\u20B9" + DecimalFormat("##,##,##0").format(
                total.toLong()))

            avgSales = value.data!!.chemist!!.map { it.orgAvgSale!!.toFloat() } as ArrayList<Float>
            setCompetitorsValues()
            setupCompetitorsChart()
        } else {
            apnaPreviewActivityBinding.chemistHeader.visibility = View.GONE
            apnaPreviewActivityBinding.recyclerViewchemist.visibility = View.GONE
            apnaPreviewActivityBinding.chemistTotal.visibility = View.GONE
            apnaPreviewActivityBinding.chemistNotFound.visibility = View.VISIBLE
        }

        if (value.data!!.hospitals != null && value.data!!.hospitals!!.size > 0) {
            apnaPreviewActivityBinding.hospitalsHeader.visibility = View.VISIBLE
            apnaPreviewActivityBinding.recyclerViewhospital.visibility = View.VISIBLE
            apnaPreviewActivityBinding.hospitalsNotFound.visibility = View.GONE
            hospitalAdapter = PreviewHospitalAdapter(
                this,
                value.data!!.hospitals as ArrayList<SurveyDetailsList.Hospital>
            )
            apnaPreviewActivityBinding.recyclerViewhospital.adapter = hospitalAdapter

            beds = value.data!!.hospitals!!.map { it.beds!!.toFloat() } as ArrayList<Float>
            hospitals = value.data!!.hospitals!!.map { it.hospitals } as ArrayList<String>
            setHospitalsValues()
            setupHospitalsChart()
        } else {
            apnaPreviewActivityBinding.hospitalsHeader.visibility = View.GONE
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
            apnaPreviewActivityBinding.neighborStoreHeader.visibility = View.VISIBLE
            neighbourAdapter = PreviewNeighbouringStoreAdapter(
                this,
                value.data!!.neighboringStore as ArrayList<SurveyDetailsList.NeighboringStore>
            )
            apnaPreviewActivityBinding.recyclerViewneighbour.adapter = neighbourAdapter

            sales = value.data!!.neighboringStore!!.map { it.sales } as ArrayList<Float>
            stores = value.data!!.neighboringStore!!.map { it.store } as ArrayList<String>
            setNeighborChartValues()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupNeighborChart()
            }
        } else {
            apnaPreviewActivityBinding.recyclerViewneighbour.visibility = View.GONE
            apnaPreviewActivityBinding.neighborStoreHeader.visibility = View.GONE
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
//
//        if (value.data!!.city != null) {
//            if (value.data!!.city!!.name != null) {
//                if (value.data!!.city!!.name.toString().isNotEmpty()) {
//                    if (!value.data!!.city!!.name.toString().equals("null", true)) {
//                        city = value.data!!.city!!.name.toString()
//                    } else {
//                        city = "-"
//                    }
//                } else {
//                    city = "-"
//                }
//            } else {
//                city = "-"
//            }
//        }
//        else {
//            city = "-"
//        }
//
//        if (value.data!!.state != null) {
//            if (value.data!!.state!!.name != null) {
//                if (value.data!!.state!!.name.toString().isNotEmpty()) {
//                    if (!value.data!!.state!!.name.toString().equals("null", true)) {
//                        state = value.data!!.state!!.name.toString()
//                    } else {
//                        state = "-"
//                    }
//                } else {
//                    state = "-"
//                }
//            } else {
//                state = "-"
//            }
//        } else {
//            state = "-"
//        }

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
            "$location,$landmarks,$city,$state-$pin"
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

            if (mapUserLats.toString().toDouble() > 0) {
                if (mapUserLangs.toString().toDouble() > 0) {
                    val latLng = LatLng(mapUserLats!!.toDouble(), mapUserLangs!!.toDouble())

                    val options = MarkerOptions().position(latLng).title("i")
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng,
                            15f
                        )
                    )
                    googleMap.addMarker(options)
                } else {
                    getCurrentLocation()
                }
            } else {
                getCurrentLocation()
            }

//            val latLng = LatLng(mapUserLats!!.toDouble(), mapUserLangs!!.toDouble())
//
//            val options = MarkerOptions().position(latLng).title("i")
//            googleMap.animateCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    latLng,
//                    15f
//                )
//            )
//            googleMap.addMarker(options)
        }

//        if (value.data!!.lat != null) {
//            if (!value.data!!.lat!!.isEmpty()) {
//                apnaPreviewActivityBinding.lattitude.setText(value.data!!.lat)
//            } else {
//                apnaPreviewActivityBinding.lattitude.setText("-")
//            }
//        } else {
//            apnaPreviewActivityBinding.lattitude.setText("-")
//        }

//        if (value.data!!.long != null) {
//            if (!value.data!!.long!!.isEmpty()) {
//                apnaPreviewActivityBinding.longitude.setText(value.data!!.long)
//            } else {
//                apnaPreviewActivityBinding.longitude.setText("-")
//            }
//        } else {
//            apnaPreviewActivityBinding.longitude.setText("-")
//        }

        if (value.data!!.dimensionType != null && value.data!!.dimensionType != null && value.data!!.dimensionType!!.name != null && !value.data!!.dimensionType!!.name!!.isEmpty()) {
            apnaPreviewActivityBinding.dimensionType.setText(
                "(" + value.data!!.dimensionType!!.name!! + "): "
            )
            apnaPreviewActivityBinding.totalAreaDimensionType.setText(
                "(" + value.data!!.dimensionType!!.name!! + "): "
            )
        } else {
            apnaPreviewActivityBinding.dimensionType.setText("(-): ")
            apnaPreviewActivityBinding.totalAreaDimensionType.setText("(-): ")
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
            apnaPreviewActivityBinding.expectedrentsrft.setText(
                DecimalFormat("##,##,##0").format(value.data!!.expectedRent!!.toLong())
            )
        } else {
            apnaPreviewActivityBinding.expectedrentsrft.setText("-")
        }

        if (value.data!!.securityDeposit != null) {
            apnaPreviewActivityBinding.securitydeposit.setText(
                DecimalFormat("##,##,##0").format(value.data!!.securityDeposit!!.toLong())
            )
        } else {
            apnaPreviewActivityBinding.securitydeposit.setText("-")
        }

        if (value.data!!.toiletsAvailability != null) {
            if (value.data!!.toiletsAvailability!!.name != null) {
                if (value.data!!.toiletsAvailability!!.name.toString().isNotEmpty()) {
                    if (!value.data!!.toiletsAvailability!!.name.toString().equals("null", true)) {
//                        apnaPreviewActivityBinding.toiletsAvailability.setText(value.data!!.toiletsAvailability!!.name.toString())
                        toiletsAvailable = value.data!!.toiletsAvailability!!.name.toString()
                    } else {
//                        apnaPreviewActivityBinding.toiletsAvailability.setText("-")
                    }
                } else {
//                    apnaPreviewActivityBinding.toiletsAvailability.setText("-")
                }
            } else {
//                apnaPreviewActivityBinding.toiletsAvailability.setText("-")
            }
        } else {
//            apnaPreviewActivityBinding.toiletsAvailability.setText("-")
        }

        if (value.data!!.buildingAge != null && value.data!!.buildingAge != 0.0) {
            val ageofBuilding = value.data!!.buildingAge.toString()
            if (ageofBuilding.contains(".")) {
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
//            apnaPreviewActivityBinding.parking.setText(value.data!!.parking!!.name)
            parkingAvailable = value.data!!.parking!!.name.toString()
        } else {
//            apnaPreviewActivityBinding.parking.setText("-")
        }

        if (value.data!!.trafficStreetType != null) {
            if (value.data!!.trafficStreetType!!.name != null) {
                if (value.data!!.trafficStreetType!!.name.toString().isNotEmpty()) {
//                    apnaPreviewActivityBinding.trafficStreetType.setText(value.data!!.trafficStreetType!!.name.toString())
                    trafficType = value.data!!.trafficStreetType!!.name.toString()
                } else {
//                    apnaPreviewActivityBinding.trafficStreetType.setText("-")
                }
            } else {
//                apnaPreviewActivityBinding.trafficStreetType.setText("-")
            }
        } else {
//            apnaPreviewActivityBinding.trafficStreetType.setText("-")
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

//        if (value.data!!.csPharma != null) {
//            apnaPreviewActivityBinding.pharma.setText(value.data!!.csPharma.toString())
//        } else {
//            apnaPreviewActivityBinding.pharma.setText("-")
//        }

//        if (value.data!!.csFmcg != null) {
//            apnaPreviewActivityBinding.fmcg.setText(value.data!!.csFmcg.toString())
//        } else {
//            apnaPreviewActivityBinding.fmcg.setText("-")
//        }

//        if (value.data!!.csSurgicals != null) {
//            apnaPreviewActivityBinding.surgicals.setText(value.data!!.csSurgicals.toString())
//        } else {
//            apnaPreviewActivityBinding.surgicals.setText("-")
//        }

//        if (value.data!!.areaDiscount != null) {
//            apnaPreviewActivityBinding.areadiscount.setText(value.data!!.areaDiscount.toString())
//        } else {
//            apnaPreviewActivityBinding.areadiscount.setText("-")
//        }

        if (value.data!!.serviceClass != null) {
            apnaPreviewActivityBinding.serviceClass.setText(
                DecimalFormat("##,##,##0").format(value.data!!.serviceClass!!.toLong())
            )
        } else {
            apnaPreviewActivityBinding.serviceClass.setText("-")
        }
        if (value.data!!.businessClass != null) {
            apnaPreviewActivityBinding.businessClass.setText(
                DecimalFormat("##,##,##0").format(value.data!!.businessClass!!.toLong())
            )
        } else {
            apnaPreviewActivityBinding.businessClass.setText("-")
        }

        val inputDateFormat = SimpleDateFormat("HH:mm:ss")
        val outputDateFormat = SimpleDateFormat("HH:mm")
        if (value.data!!.morningFrom != null) {
            apnaPreviewActivityBinding.morningFrom.setText(outputDateFormat.format(inputDateFormat.parse(
                value.data!!.morningFrom!!)!!))
        } else {
            apnaPreviewActivityBinding.morningFrom.setText("-")
        }

        if (value.data!!.morningTo != null) {
            apnaPreviewActivityBinding.morningTo.setText(
                outputDateFormat.format(inputDateFormat.parse(
                    value.data!!.morningTo!!)!!)
            )
        } else {
            apnaPreviewActivityBinding.morningTo.setText("-")
        }

        if (value.data!!.eveningFrom != null) {
            apnaPreviewActivityBinding.eveningFrom.setText(
                outputDateFormat.format(inputDateFormat.parse(
                    value.data!!.eveningFrom!!)!!)
            )
        } else {
            apnaPreviewActivityBinding.eveningFrom.setText("-")
        }

        if (value.data!!.eveningTo != null) {
            apnaPreviewActivityBinding.eveningTo.setText(
                outputDateFormat.format(inputDateFormat.parse(
                    value.data!!.eveningTo!!)!!)
            )
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
            if (value.data!!.csPharma!! > 0) {
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
        } else {
            apnaPreviewActivityBinding.pharmaAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.pharmaNotAvailableLayout.visibility = View.VISIBLE
        }

        if (value.data!!.csFmcg != null) {
            if (value.data!!.csFmcg!! > 0) {
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
        } else {
            apnaPreviewActivityBinding.fmcgAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.fmcgNotAvailableLayout.visibility = View.VISIBLE
        }

        if (value.data!!.csSurgicals != null) {
            if (value.data!!.csSurgicals!! > 0) {
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
        } else {
            apnaPreviewActivityBinding.surgicalsAvailableLayout.visibility = View.GONE
            apnaPreviewActivityBinding.surgicalsNotAvailableLayout.visibility = View.VISIBLE
        }

        if (value.data!!.areaDiscount != null) {
            if (value.data!!.areaDiscount!! > 0) {
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

    override fun onItemClick(
        position: Int,
        imagePath: String,
        name: String,
        imagetData: ArrayList<SurveyDetailsList.Image>,
    ) {

        val intent = Intent(this@ApnaPreviewActivity, ImagePreviewActivity::class.java)
        intent.putExtra("IMAGES", imagetData)
        intent.putExtra("IMAGE_POSITION", position)
        intent.putExtra("SURVEY_ID", surveyId)
        startActivity(intent)

//        PopUpWIndow(
//            this,
//            R.layout.layout_image_fullview,
//            View(this),
//            imagePath,
//            null,
//            name,
//            position
//        )
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
        Utlis.hideDialog()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val task: Task<Location> = currentLocationClient!!.lastLocation
        task.addOnSuccessListener(object : OnSuccessListener<Location> {
            override fun onSuccess(location: Location?) {
                supportMapFragment!!.getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(map: GoogleMap) {
//                        map.setOnMarkerDragListener(this@ApnaPreviewActivity)
                        this@ApnaPreviewActivity.map = map
                        val latLang = LatLng(location!!.latitude, location.longitude)
//                        activityApnaNewSurveyBinding.latitude.setText(location.latitude.toString())
//                        activityApnaNewSurveyBinding.longitude.setText(location.longitude.toString())

//                        if (selectedMarker != null) {
//                            selectedMarker!!.remove()
//                        }

                        val markerOption =
                            MarkerOptions().position(latLang).title("").draggable(true)
                        map.addMarker(markerOption)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15F))
                    }
                })
            }
        })
    }
}