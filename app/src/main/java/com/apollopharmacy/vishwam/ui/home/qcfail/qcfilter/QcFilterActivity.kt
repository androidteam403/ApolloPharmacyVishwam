package com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityQcFilterBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.util.Utlis
import org.apache.commons.lang3.StringUtils

class QcFilterActivity : AppCompatActivity(), QcSiteDialog.NewDialogSiteClickListner,
    QcRegionDialog.NewDialogSiteClickListner, QcCalender.DateSelected,
    QcCalenderToDate.DateSelected {
    lateinit var activityQcFilterBinding: ActivityQcFilterBinding
    lateinit var viewModel: QcSiteActivityViewModel
    var storeList = ArrayList<QcStoreList.Store>()
    private var fromQcDate: String = ""
    private var toDate: String = ""
    private var siteId: String = ""
    private var regionId: String = ""

    var selectsiteIdList = ArrayList<String>()
    var getregionList = ArrayList<QcStoreList.Store>()
    var regionList = ArrayList<QcRegionList.Store>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQcFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_qc_filter)
        viewModel = ViewModelProvider(this)[QcSiteActivityViewModel::class.java]
        viewModel.getQcRegionList()
        viewModel.getQcStoreist()
        viewModel.qcStoreList.observeForever {
            Utlis.hideLoading()
            if (!it.storelist.isNullOrEmpty()) {

                for (i in it.storelist!!) {
                    val items = QcStoreList.Store()
                    items.siteid = i.siteid
                    items.sitename = i.sitename

                    storeList.add(items)

                }

            }
        }
        viewModel.qcRegionLists.observeForever {

            if (!it.storelist.isNullOrEmpty()) {

                for (i in it.storelist!!) {
                    val items = QcRegionList.Store()
                    items.siteid = i.siteid
                    items.sitename = i.sitename

                    regionList.add(items)

                }

            }
        }



        activityQcFilterBinding.reset.setOnClickListener {
            val intent = Intent()

            intent.putExtra("reset", "reset")
            setResult(Activity.RESULT_OK, intent)
            finish()
//
//            activityQcFilterBinding.regionIdSelect.setText("")
//            activityQcFilterBinding.siteIdSelect.setText("")
//            activityQcFilterBinding.fromDateText.setText("")
//            activityQcFilterBinding.toDateText.setText("")
//            viewModel.getQcRegionList()
//            viewModel.getQcStoreist()
//            Utlis.showLoading(this)
        }



        activityQcFilterBinding.applybutoon.setOnClickListener {
//            selectsiteIdList.add(fromQcDate)
//            selectsiteIdList.add(toDate)
//            selectsiteIdList.add(siteId)
//            selectsiteIdList.add(regionId)
            val intent = Intent()
            intent.putExtra("regionId", regionId.replace(" ", ""))
            intent.putExtra("siteId", siteId.replace(" ", ""))
            intent.putExtra("fromQcDate", fromQcDate)
            intent.putExtra("toDate", toDate)
            intent.putExtra("apply", "apply")

            setResult(Activity.RESULT_OK, intent)
            finish()

        }

        activityQcFilterBinding.cancel.setOnClickListener {
            finish()
        }
        activityQcFilterBinding.fromDateText.setOnClickListener {
            QcCalender().apply {
                arguments = generateParsedData(
                    activityQcFilterBinding.toDateText.text.toString(),
                    false,
                    activityQcFilterBinding.toDateText.text.toString()
                )
            }.show(supportFragmentManager, "")


        }


        activityQcFilterBinding.toDateText.setOnClickListener {
            QcCalenderToDate().apply {
                arguments = generateParsedData(
                    activityQcFilterBinding.toDateText.text.toString(),
                    false,
                    activityQcFilterBinding.toDateText.text.toString()
                )
            }.show(supportFragmentManager, "")
        }


        activityQcFilterBinding.regionIdSelect.setOnClickListener {
            QcRegionDialog().apply {
                arguments = QcRegionDialog().generateParsedData(viewModel.getRegionData())
            }.show(supportFragmentManager, "")
        }

        activityQcFilterBinding.siteIdSelect.setOnClickListener {


            QcSiteDialog().apply {
                arguments =
                        //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                    QcSiteDialog().generateParsedData(viewModel.getSiteData())
            }.show(supportFragmentManager, "")
        }
    }

    override fun onselectMultipleSitesStore(list: ArrayList<String>, position: Int) {
        activityQcFilterBinding.siteIdSelect.setText((StringUtils.substring(list.toString(),
            1,
            list.toString().length - 1)))
//        for (i in list.indices){
//            siteId +=list.get(i)+ ","
//        }

        siteId = activityQcFilterBinding.siteIdSelect.text.toString()


    }


    override fun selectSite(departmentDto: QcStoreList.Store) {
//        activityQcFilterBinding.siteIdSelect.setText(departmentDto.siteid)
//        selectsiteIdList.add(departmentDto.siteid.toString())


    }

    override fun onselectMultipleSites(list: ArrayList<String>, position: Int) {
        activityQcFilterBinding.regionIdSelect.setText(StringUtils.substring(list.toString(),
            1,
            list.toString().length - 1))
        regionId = activityQcFilterBinding.regionIdSelect.text.toString()

//        regionId = list.toString()

//        if (list[position].isClick){
//            activityQcFilterBinding.regionIdSelect.setText(list[position].siteid)
//        }
    }

    override fun selectSite(regionId: QcRegionList.Store) {
//        activityQcFilterBinding.regionIdSelect.setText(regionId.siteid)
//        selectsiteIdList.add(regionId.siteid.toString())
    }


    override fun fromDate(fromDate: String, showingDate: String) {
        activityQcFilterBinding.fromDateText.setText(fromDate)
        fromQcDate = fromDate
    }

    override fun toDate(dateSelected: String, showingDate: String) {
        activityQcFilterBinding.toDateText.setText(dateSelected)
        toDate = dateSelected

    }


}