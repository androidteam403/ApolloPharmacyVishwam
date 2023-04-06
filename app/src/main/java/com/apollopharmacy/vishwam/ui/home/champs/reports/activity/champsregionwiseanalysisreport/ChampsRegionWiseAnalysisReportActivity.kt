package com.apollopharmacy.vishwam.ui.home.champs.reports.activity.champsregionwiseanalysisreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityChampsRegionWiseAnalysisReportBinding
import com.apollopharmacy.vishwam.databinding.ActivityChampsSummaryReportsBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.ui.home.champs.reports.activity.champssummaryreports.ChampsSummaryReportsViewModel
import com.apollopharmacy.vishwam.ui.home.champs.reports.activity.champssurveyreports.ChampsSurveyViewModel
import com.apollopharmacy.vishwam.util.Utils

class ChampsRegionWiseAnalysisReportActivity : AppCompatActivity(), ComplaintListCalendarDialog.DateSelected, ChampsRegionWiseReportCallback {
    var isFromDateSelected: Boolean = false
    private lateinit var activityChampsRegionWiseAnalysisReportBinding: ActivityChampsRegionWiseAnalysisReportBinding
    private lateinit var champsRegionWiseReportsViewModel: ChampsSummaryReportsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_champs_region_wise_analysis_report)

        activityChampsRegionWiseAnalysisReportBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_region_wise_analysis_report

        )
        champsRegionWiseReportsViewModel = ViewModelProvider(this)[ChampsSummaryReportsViewModel::class.java]
        setUp()
    }

    private fun setUp() {
       activityChampsRegionWiseAnalysisReportBinding.callback=this
        activityChampsRegionWiseAnalysisReportBinding.fromDateEdittext.setText(Utils.getCurrentDate())
        activityChampsRegionWiseAnalysisReportBinding.endDateEdittext.setText(Utils.getCurrentDate())
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            activityChampsRegionWiseAnalysisReportBinding.fromDateEdittext.setText(showingDate)
            val fromDate =  activityChampsRegionWiseAnalysisReportBinding.fromDateEdittext.text.toString()
            val toDate =  activityChampsRegionWiseAnalysisReportBinding.endDateEdittext.text.toString()
            if (Utils.getDateDifference(fromDate, toDate) == 0) {
                activityChampsRegionWiseAnalysisReportBinding.endDateEdittext.setText(Utils.getCurrentDate())
            }
        } else {
            activityChampsRegionWiseAnalysisReportBinding.endDateEdittext.setText(showingDate)
        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {

    }

    override fun onClickBack() {
        super.onBackPressed()
    }

    override fun onClickView() {
        var fromDate = activityChampsRegionWiseAnalysisReportBinding.fromDateEdittext.text.toString()
        var toDate = activityChampsRegionWiseAnalysisReportBinding.endDateEdittext.text.toString()
        if (Utils.getDateDifference(fromDate, toDate) > 0) {
//            if (!viewBinding.pullToRefresh.isRefreshing)
//                Utlis.showLoading(requireContext())
//            isLoadMoreAvailable = true
//            callAPI(1)
        } else {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.label_check_dates),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClickEndDate() {
        isFromDateSelected = false
        openDateDialog()
    }

    override fun onClickStartDate() {
        isFromDateSelected = true
        openDateDialog()
    }

    private fun openDateDialog(){
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(activityChampsRegionWiseAnalysisReportBinding.fromDateEdittext.text.toString(),
                    false,
                    activityChampsRegionWiseAnalysisReportBinding.fromDateEdittext.text.toString())
            }.show(supportFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(activityChampsRegionWiseAnalysisReportBinding.endDateEdittext.text.toString(),
                    true,
                    activityChampsRegionWiseAnalysisReportBinding.fromDateEdittext.text.toString())
            }.show(supportFragmentManager, "")
        }
    }
}