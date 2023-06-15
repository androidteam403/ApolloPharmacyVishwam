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
    var fromDateDiscount: String = ""
    var siteId: String = ""
    var dcCode: String = ""
    var toDateDiscount: String = ""
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

        activityDashboardFilterBinding.reset.setOnClickListener {
            activityDashboardFilterBinding.siteIdSelect.setText("")
            activityDashboardFilterBinding.regionIdSelect.setText("")
            activityDashboardFilterBinding.toDateText.setText("")
            activityDashboardFilterBinding.fromDateText.setText("")

        }



        activityDashboardFilterBinding.applybutoon.setOnClickListener {
            if (fromDateDiscount.isNullOrEmpty()){
                Toast.makeText(context, "Mandatory Fields Should not be  Empty", Toast.LENGTH_LONG).show()

            } else if(fromDateDiscount.isNullOrEmpty() ){

                Toast.makeText(context, "Mandatory Fields Should not be  Empty", Toast.LENGTH_LONG).show()

            }

            else {
                val intent = Intent()
                intent.putExtra("fromDate", fromDateDiscount)
                intent.putExtra("toDate", toDateDiscount)
                intent.putExtra("siteId", siteId)
                intent.putExtra("dcCode", dcCode)
                intent.putExtra("apply", "apply")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        activityDashboardFilterBinding.cancel.setOnClickListener {

            onBackPressed()
        }


        activityDashboardFilterBinding.siteIdSelect.setOnClickListener {
            DiscountSiteDialog().apply {
                arguments =
                    DiscountSiteDialog().generateParsedData(storeList.distinctBy { it.STORE.isNotEmpty() } as ArrayList<PendingOrder.PENDINGLISTItem>)
            }.show(supportFragmentManager, "")

        }
        activityDashboardFilterBinding.regionIdSelect.setOnClickListener {
            DiscountRegionDialog().apply {
                arguments =
                    DiscountRegionDialog().generateParsedData(storeList.distinctBy { it.DCCODE.isNotEmpty() } as ArrayList<PendingOrder.PENDINGLISTItem>)
            }.show(supportFragmentManager, "")

        }


    }

    override fun fromDate(fromDate: String, showingDate: String) {
        activityDashboardFilterBinding.fromDateText.setText(fromDate)
        fromDateDiscount = fromDate
    }

    override fun toDate(dateSelected: String, showingDate: String) {
        activityDashboardFilterBinding.toDateText.setText(dateSelected)
        toDateDiscount = dateSelected

    }

    override fun selectSite(departmentDto: PendingOrder.PENDINGLISTItem) {
        activityDashboardFilterBinding.siteIdSelect.setText(departmentDto.STORE)
        siteId=departmentDto.STORE

    }

    override fun selectRegion(departmentDto: PendingOrder.PENDINGLISTItem) {
        activityDashboardFilterBinding.regionIdSelect.setText(departmentDto.DCCODE)
        dcCode=departmentDto.DCCODE
    }


}