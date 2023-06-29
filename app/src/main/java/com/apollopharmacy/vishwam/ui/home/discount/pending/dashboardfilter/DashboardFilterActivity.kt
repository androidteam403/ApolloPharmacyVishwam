package com.apollopharmacy.vishwam.ui.home.discount.pending.dashboardfilter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.ActivityDashboardFilterBinding
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DiscountRegionDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DiscountSiteDialog
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcCalender
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcCalenderToDate
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcRegionList
import com.apollopharmacy.vishwam.util.Utlis

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



        activityDashboardFilterBinding.toDateText.setText(Preferences.getDiscountToDate())
        activityDashboardFilterBinding.fromDateText.setText(Preferences.getDiscountFromDate())
        activityDashboardFilterBinding.regionIdSelect.setText(Preferences.getDiscountRegion())
        activityDashboardFilterBinding.siteIdSelect.setText(Preferences.getDiscountSite())

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
            val intent = Intent()
            Preferences.setDiscountFromDate("")
            Preferences.setDiscountToDate("")
            Preferences.setDiscountSite("")
            Preferences.setDiscountRegion("")
            intent.putExtra("reset", "reset")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }



        activityDashboardFilterBinding.applybutoon.setOnClickListener {
            if (fromDateDiscount.isNullOrEmpty()) {
                Toast.makeText(context, "Mandatory Fields Should not be  Empty", Toast.LENGTH_LONG)
                    .show()

            } else if (fromDateDiscount.isNullOrEmpty()) {

                Toast.makeText(context, "Mandatory Fields Should not be  Empty", Toast.LENGTH_LONG)
                    .show()

            } else {
                val intent = Intent()
                Preferences.setDiscountFromDate(fromDateDiscount)
                Preferences.setDiscountToDate(toDateDiscount)
                Preferences.setDiscountSite(siteId)
                Preferences.setDiscountRegion(dcCode)
                intent.putExtra("fromDate", fromDateDiscount)
                intent.putExtra("toDate", toDateDiscount)
                intent.putExtra("siteId", siteId)
                intent.putExtra("dcCode", dcCode)
                intent.putExtra("reset", "")
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
        fromDateDiscount = fromDate
        if (toDateDiscount.isEmpty()) {
            activityDashboardFilterBinding.fromDateText.setText(fromDate)

        } else if (Utlis.filterDateFormate(toDateDiscount)
                .after(Utlis.filterDateFormate(fromDateDiscount)) || Utlis.filterDateFormate(
                fromDateDiscount
            ).equals(Utlis.filterDateFormate(toDateDiscount))
        ) {

            activityDashboardFilterBinding.fromDateText.setText(fromDate)

        } else {
            Toast.makeText(context, "From Date should not be After To Date ", Toast.LENGTH_LONG).show()

        }


    }

    override fun toDate(dateSelected: String, showingDate: String) {
        toDateDiscount = dateSelected
        if (fromDateDiscount.isNullOrEmpty()) {
            activityDashboardFilterBinding.toDateText.setText(dateSelected)

        } else {

            if (Utlis.filterDateFormate(fromDateDiscount)
                    .before(Utlis.filterDateFormate(toDateDiscount)) || Utlis.filterDateFormate(
                    fromDateDiscount
                ).equals(Utlis.filterDateFormate(toDateDiscount))
            ) {
                activityDashboardFilterBinding.toDateText.setText(dateSelected)
            } else {
                Toast.makeText(context, "To Date Should not be After From Date ", Toast.LENGTH_LONG)
                    .show()
            }
        }


    }

    override fun selectSite(departmentDto: PendingOrder.PENDINGLISTItem) {
        activityDashboardFilterBinding.siteIdSelect.setText(departmentDto.STORE)
        siteId = departmentDto.STORE

    }

    override fun selectRegion(departmentDto: PendingOrder.PENDINGLISTItem) {
        activityDashboardFilterBinding.regionIdSelect.setText(departmentDto.DCCODE)
        dcCode = departmentDto.DCCODE
    }


}