 package com.apollopharmacy.vishwam.ui.home.champs.reports.activity.champssummaryreports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityChampsSummaryReportsBinding
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyBinding
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyReportsBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurveyactivity.ChampsSurveyViewModel
import com.apollopharmacy.vishwam.util.Utils

class ChampsSummaryReportsActivity : AppCompatActivity(), ChampsSummaryReportsCallBack,ComplaintListCalendarDialog.DateSelected {
    var isFromDateSelected: Boolean = false
    private lateinit var activityChampsSummaryReportsBinding: ActivityChampsSummaryReportsBinding
    private lateinit var champsSummaryReportsViewModel: ChampsSummaryReportsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_champs_summary_reports)
        activityChampsSummaryReportsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_summary_reports

        )
        champsSummaryReportsViewModel = ViewModelProvider(this)[ChampsSummaryReportsViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        activityChampsSummaryReportsBinding.callback = this
        activityChampsSummaryReportsBinding.fromDateEdittext.setText(Utils.getCurrentDate())
        activityChampsSummaryReportsBinding.endDateEdittext.setText(Utils.getCurrentDate())
    }

    override fun onClickBack() {
      super.onBackPressed()
    }

    override fun onClickStartDate() {
        isFromDateSelected = true
        openDateDialog()
    }

    override fun onClickEndDate() {
        isFromDateSelected = false
        openDateDialog()
    }

    private fun openDateDialog(){
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(activityChampsSummaryReportsBinding.fromDateEdittext.text.toString(),
                    false,
                    activityChampsSummaryReportsBinding.fromDateEdittext.text.toString())
            }.show(supportFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(activityChampsSummaryReportsBinding.endDateEdittext.text.toString(),
                    true,
                    activityChampsSummaryReportsBinding.fromDateEdittext.text.toString())
            }.show(supportFragmentManager, "")
        }
    }

    override fun onClickView() {
        var fromDate = activityChampsSummaryReportsBinding.fromDateEdittext.text.toString()
        var toDate = activityChampsSummaryReportsBinding.endDateEdittext.text.toString()
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

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            activityChampsSummaryReportsBinding.fromDateEdittext.setText(showingDate)
            val fromDate =  activityChampsSummaryReportsBinding.fromDateEdittext.text.toString()
            val toDate =  activityChampsSummaryReportsBinding.endDateEdittext.text.toString()
            if (Utils.getDateDifference(fromDate, toDate) == 0) {
                activityChampsSummaryReportsBinding.endDateEdittext.setText(Utils.getCurrentDate())
            }
        } else {
            activityChampsSummaryReportsBinding.endDateEdittext.setText(showingDate)
        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
        TODO("Not yet implemented")
    }
}