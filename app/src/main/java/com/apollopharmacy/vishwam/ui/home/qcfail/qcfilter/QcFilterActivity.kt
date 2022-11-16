package com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter

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
import com.apollopharmacy.vishwam.databinding.ActivityQcFilterBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.util.Utlis
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

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
    private var fragment: String = ""
    private var qcDate: String = ""
    private var qcfDate: String = ""

    var selectsiteIdList = ArrayList<String>()
    var getregionList = ArrayList<QcStoreList.Store>()
    var regionList = ArrayList<QcRegionList.Store>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQcFilterBinding = DataBindingUtil.setContentView(this, R.layout.activity_qc_filter)
        viewModel = ViewModelProvider(this)[QcSiteActivityViewModel::class.java]
        viewModel.getQcRegionList()
        viewModel.getQcStoreist()



        activityQcFilterBinding.toDateText.setText(Preferences.getQcToDate())
        activityQcFilterBinding.fromDateText.setText(Preferences.getQcFromDate())
        activityQcFilterBinding.regionIdSelect.setText(Preferences.getQcRegion())
        activityQcFilterBinding.siteIdSelect.setText(Preferences.getQcSite())

//        var fromDate = String()
//        var currentDate = String()
//
//        currentDate = SimpleDateFormat.format(Date())
//
//        val cal = Calendar.getInstance()
//        cal.add(Calendar.DATE, -7)
//        fromDate = simpleDateFormat.format(cal.time)
//        if (intent != null) {
//            fragment =
//                getIntent().getExtras()?.getString("activity")!!
//        }
//        if (fragment.equals("1")){
//            activityQcFilterBinding.fromDateText.setText("01-Apr-2019")
//            activityQcFilterBinding.toDateText.setText(Utlis.getCurrentDate("dd-MMM-yyyy")!!)
//
//        }else  if (fragment.equals("2")){
//            activityQcFilterBinding.fromDateText.setText(Utlis.getDateSevenDaysEarlier("dd-MMM-yyyy"))
//            activityQcFilterBinding.toDateText.setText( Utlis.getCurrentDate("dd-MMM-yyyy")!!)
//        }
//        else  if (fragment.equals("3")){
//            activityQcFilterBinding.fromDateText.setText(Utlis.getDateSevenDaysEarlier("dd-MMM-yyyy"))
//            activityQcFilterBinding.toDateText.setText( Utlis.getCurrentDate("dd-MMM-yyyy")!!)
//        }
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
            Preferences.setQcFromDate("")
            Preferences.setQcToDate("")
            Preferences.setQcSite("")
            Preferences.setQcRegion("")
            intent.putExtra("reset", "reset")
            setResult(Activity.RESULT_OK, intent)
            finish()

        }



        activityQcFilterBinding.applybutoon.setOnClickListener {

            if(fromQcDate.isNullOrEmpty()&&toDate.isNullOrEmpty()&&regionId.isNullOrEmpty()&&siteId.isNullOrEmpty()){
                Toast.makeText(context, "All Fields Should not be  Empty", Toast.LENGTH_LONG).show()

            }else{
                val intent = Intent()
                Preferences.setQcFromDate(fromQcDate)
                Preferences.setQcToDate(toDate)
                Preferences.setQcSite(siteId)
                Preferences.setQcRegion(regionId)
                intent.putExtra("regionId", regionId.replace(" ", ""))
                intent.putExtra("siteId", siteId.replace(" ", ""))
                intent.putExtra("fromQcDate", fromQcDate)
                intent.putExtra("toDate", toDate)
                intent.putExtra("apply", "apply")

                setResult(Activity.RESULT_OK, intent)
                finish()
            }


        }

        activityQcFilterBinding.cancel.setOnClickListener {
//            val intent = Intent()
//
//            intent.putExtra("reset", "reset")
//            setResult(Activity.RESULT_OK, intent)
//            finish()
            onBackPressed()
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


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun fromDate(fromDate: String, showingDate: String) {

        activityQcFilterBinding.fromDateText.setText(fromDate)
        fromQcDate = fromDate
//        qcDate=fromQcDate+30*1000*60*60*24
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
                val cal = Calendar.getInstance()
        cal.time=sdf.parse(fromQcDate)

        cal.add(Calendar.DATE, +30)
        val sdf1 = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        qcDate=sdf1.format(cal.time)
var date1 = cal.time
        val cal1 = Calendar.getInstance()
        var  date2 = cal1.time

        if (date1.before(date2)){
            qcDate=sdf.format(cal.time)
        }else{
            qcDate= Utlis.getCurrentDate("dd-MMM-yyyy").toString()
        }
//
//        if(qcDate<= LocalDate.now().minusDays(30).toString()){
//            qcDate=sdf.format(cal.time)
//
//        }else{
//            qcDate= Utlis.getCurrentDate("dd-MMM-yyyy").toString()
//
//        }
//
//
////        Date date = new Date();
////        String todate = sdf.format(date);
//        cal.add(Integer.parseInt(activityQcFilterBinding.fromDateText.text.toString()),30)
//        val todate1 = cal.time
        activityQcFilterBinding.toDateText.setText(qcDate)
        toDate = activityQcFilterBinding.toDateText.text.toString()

    }

    override fun toDate(dateSelected: String, showingDate: String) {
        activityQcFilterBinding.toDateText.setText(dateSelected)
        toDate = dateSelected

    }

    fun getDatethirtyDays(pattern: String?): String? {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())

//        Date date = new Date();
//        String todate = sdf.format(date);
        val cal = Calendar.getInstance()
//        cal.add(Calendar.DATE,-30)
        val todate1 = cal.time
        return sdf.format(todate1)
    }

}