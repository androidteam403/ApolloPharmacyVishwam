package com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityQcFilterBinding
import com.apollopharmacy.vishwam.dialog.CalenderNew
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*

class QcFilterActivity : AppCompatActivity(), QcSiteDialog.NewDialogSiteClickListner,
    QcRegionDialog.NewDialogSiteClickListner, QcCalender.DateSelected,
    QcCalenderToDate.DateSelected {
    lateinit var activityQcFilterBinding: ActivityQcFilterBinding
    lateinit var viewModel: QcSiteActivityViewModel
    var storeList = ArrayList<QcStoreList.Store>()
    var selectsiteIdList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQcFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_qc_filter)

//        window.setBackgroundDrawable( ColorDrawable(0))
//        supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[QcSiteActivityViewModel::class.java]
        viewModel.getQcRegionList()
        viewModel.getQcStoreist()
        var isFromDateSelected: Boolean = false
        viewModel.qcStoreList.observeForever {
            if (!it.storelist.isNullOrEmpty()) {

                for (i in it.storelist!!) {
                    val items = QcStoreList.Store()
                    items.siteid = i.siteid
                    items.sitename = i.sitename

                    storeList.add(items)

                }

            }
        }


        activityQcFilterBinding.submit.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
            setResult(Activity.RESULT_OK, returnIntent)
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


    override fun selectSite(departmentDto: QcStoreList.Store) {
        activityQcFilterBinding.siteIdSelect.setText(departmentDto.siteid)
        selectsiteIdList.add(departmentDto.siteid.toString())


    }

    override fun selectSite(regionId: QcRegionList.Store) {
        activityQcFilterBinding.regionIdSelect.setText(regionId.siteid)
        selectsiteIdList.add(regionId.siteid.toString())
    }


    override fun fromDate(fromDate: String, showingDate: String) {
        activityQcFilterBinding.fromDateText.setText(fromDate)
        selectsiteIdList.add(fromDate)
    }

    override fun toDate(dateSelected: String, showingDate: String) {
        activityQcFilterBinding.toDateText.setText(dateSelected)
        selectsiteIdList.add(dateSelected)

    }


}