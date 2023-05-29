package com.apollopharmacy.vishwam.ui.home.discount.pending.dashboardfilter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.ActivityDashboardFilterBinding
import com.apollopharmacy.vishwam.databinding.ActivityQcFilterBinding
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DiscountRegionDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DiscountSiteDialog
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFilterActivity : AppCompatActivity(), QcCalender.DateSelected,
    DiscountSiteDialog.NewDialogSiteClickListner,
    QcCalenderToDate.DateSelected, DiscountRegionDialog.NewDialogSiteClickListner {
    lateinit var activityDashboardFilterBinding: ActivityDashboardFilterBinding

    var storeList = ArrayList<PendingOrder.PENDINGLISTItem>()


    var selectsiteIdList = ArrayList<String>()


    var regionList = ArrayList<QcRegionList.Store>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDashboardFilterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_dashboard_filter)
        if (intent != null) {
            storeList =
                intent.getSerializableExtra("storeList") as ArrayList<PendingOrder.PENDINGLISTItem>

        }
        activityDashboardFilterBinding.fromDateText.setOnClickListener {
            QcCalender().apply {
                arguments = generateParsedData(
                    activityDashboardFilterBinding.toDateText.text.toString(),
                    false,
                    activityDashboardFilterBinding.toDateText.text.toString()
                )
            }.show(supportFragmentManager, "")


        }
        activityDashboardFilterBinding.toDateText.setOnClickListener {
            QcCalenderToDate().apply {
                arguments = generateParsedData(
                    activityDashboardFilterBinding.toDateText.text.toString(),
                    false,
                    activityDashboardFilterBinding.toDateText.text.toString()
                )
            }.show(supportFragmentManager, "")
        }

        activityDashboardFilterBinding.siteIdSelect.setOnClickListener {
            DiscountSiteDialog().apply {
                arguments = DiscountSiteDialog().generateParsedData(storeList)
            }.show(supportFragmentManager, "")

        }
        activityDashboardFilterBinding.regionIdSelect.setOnClickListener {
            DiscountRegionDialog().apply {
                arguments = DiscountRegionDialog().generateParsedData(storeList)
            }.show(supportFragmentManager, "")

        }


    }

    override fun fromDate(fromDate: String, showingDate: String) {
        activityDashboardFilterBinding.fromDateText.setText(fromDate)
    }

    override fun toDate(dateSelected: String, showingDate: String) {
        activityDashboardFilterBinding.toDateText.setText(dateSelected)

    }

    override fun selectSite(departmentDto: PendingOrder.PENDINGLISTItem) {
        activityDashboardFilterBinding.siteIdSelect.setText(departmentDto.STORENAME)
    }

    override fun selectRegion(departmentDto: PendingOrder.PENDINGLISTItem) {
        activityDashboardFilterBinding.regionIdSelect.setText(departmentDto.DCNAME)    }


}