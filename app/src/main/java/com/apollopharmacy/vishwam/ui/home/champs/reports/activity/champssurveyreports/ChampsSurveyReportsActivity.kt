package com.apollopharmacy.vishwam.ui.home.champs.reports.activity.champssurveyreports

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyReportsBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.util.Utils

class ChampsSurveyReportsActivity : AppCompatActivity(),ComplaintListCalendarDialog.DateSelected, ChampsSurveyReportsCallBack{
    var isFromDateSelected: Boolean = false
    private lateinit var activityChampsSurveyReportsBinding:ActivityChampsSurveyReportsBinding
    private lateinit var champsSurveyViewModel: ChampsSurveyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChampsSurveyReportsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_survey_reports

        )
        champsSurveyViewModel = ViewModelProvider(this)[ChampsSurveyViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        activityChampsSurveyReportsBinding.callback = this
       activityChampsSurveyReportsBinding.fromDateEdittext.setText(Utils.getCurrentDate())
        activityChampsSurveyReportsBinding.endDateEdittext.setText(Utils.getCurrentDate())
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

    override fun onClickView() {
        var fromDate = activityChampsSurveyReportsBinding.fromDateEdittext.text.toString()
        var toDate = activityChampsSurveyReportsBinding.endDateEdittext.text.toString()
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

    private fun openDateDialog(){
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(activityChampsSurveyReportsBinding.fromDateEdittext.text.toString(),
                    false,
                    activityChampsSurveyReportsBinding.fromDateEdittext.text.toString())
            }.show(supportFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(activityChampsSurveyReportsBinding.endDateEdittext.text.toString(),
                    true,
                    activityChampsSurveyReportsBinding.fromDateEdittext.text.toString())
            }.show(supportFragmentManager, "")
        }
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            activityChampsSurveyReportsBinding.fromDateEdittext.setText(showingDate)
            val fromDate =  activityChampsSurveyReportsBinding.fromDateEdittext.text.toString()
            val toDate =  activityChampsSurveyReportsBinding.endDateEdittext.text.toString()
            if (Utils.getDateDifference(fromDate, toDate) == 0) {
                activityChampsSurveyReportsBinding.endDateEdittext.setText(Utils.getCurrentDate())
            }
        } else {
            activityChampsSurveyReportsBinding.endDateEdittext.setText(showingDate)
        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {

    }


}