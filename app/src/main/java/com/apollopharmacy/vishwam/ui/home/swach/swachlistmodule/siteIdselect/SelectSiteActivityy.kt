package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.ActivitySelectSiteActivityyBinding
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.adapter.SiteIdAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.adapter.SiteIdDisplay
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class SelectSiteActivityy : AppCompatActivity(), SelectSiteIdCallback {
    var selectsiteIdList = ArrayList<String>()
    lateinit var activitySelectSiteActivityBinding: ActivitySelectSiteActivityyBinding
    lateinit var viewModel: SelectSiteActivityViewModel
//    var siteDataList = ArrayList<ArrayList<StoreListItem>>()
    var siteDataList = ArrayList<StoreListItem>()

    private var siteIdAdapter: SiteIdAdapter?=null
    private lateinit var dialog: Dialog
    private var siteIdDisplay: SiteIdDisplay?=null
    private var tickMarkDelete: Boolean =false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectSiteActivityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_site_activityy
        )
        activitySelectSiteActivityBinding.callback = this
        viewModel = ViewModelProvider(this)[SelectSiteActivityViewModel::class.java]

        selectsiteIdList = intent.getStringArrayListExtra("selectsiteIdList")!!


        Utlis.showLoading(this)
        viewModel.siteId()

//        viewModel.siteLiveData.observeForever {
//            if(it!=null){
//                siteLiveDataList.add(it)
//            }

        if(selectsiteIdList!=null && selectsiteIdList.size>0){
            siteIdDisplay =
                SiteIdDisplay(applicationContext, selectsiteIdList, this)
//            activitySelectSiteActivityBinding.storeIdsRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
            activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay
        }



        viewModel.fixedArrayList.observeForever {
           siteDataList = it
                for(i in siteDataList.indices){
                    for(j in selectsiteIdList.indices){
                        if(selectsiteIdList.get(j).equals(siteDataList.get(i).site)){
                            siteDataList.get(i).isSelected=true
                        }
                    }
                }



            siteIdAdapter =
                SiteIdAdapter(applicationContext, siteDataList, this, selectsiteIdList)
//            activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
            activitySelectSiteActivityBinding.fieldRecyclerView.adapter = siteIdAdapter
//            Utlis.hideLoading()
            Utlis.hideLoading()



        }


        viewModel.command.observeForever {
            when (it) {
                is SelectSiteActivityViewModel.CmsCommandSelectSiteId.ShowSiteInfo -> {
                    Utlis.hideLoading()
                    Preferences.setSiteIdList(Gson().toJson(viewModel.getSiteData()))
                    Preferences.setSiteIdListFetched(true)
//                        arguments =
//                            SiteDialog().generateParsedData(viewModel.getSiteData())

                }
                else -> {}
            }
        }

        searchByFulfilmentId()
    }

    private fun searchByFulfilmentId() {
        activitySelectSiteActivityBinding.searchSiteText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length >= 2) {
                    if (siteIdAdapter != null) {
                        siteIdAdapter!!.getFilter()?.filter(editable)
                    }
                } else {
                    if (siteIdAdapter != null) {
                        siteIdAdapter!!.getFilter()?.filter("")
                    }
                }
            }
        })
    }




    override fun onClickSiteId(selectedSiteId: String?, position1: Int) {
        if (siteDataList != null && siteDataList.size>0) {

                var isSameId: Boolean = false
                for( i in selectsiteIdList.indices){
                    if(selectedSiteId?.equals(selectsiteIdList.get(i))!!){
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("You have already selected this site ID")

                        val alert = builder.create()
                        alert.show()

                        val timer = Timer()
                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                alert.dismiss()
                                timer.cancel()
                            }
                        }, 3000)
                        isSameId = true
                    }
                }
                if(!isSameId){
                    selectsiteIdList.add(selectedSiteId!!)
                    var pos: Int?=null
                    for(i in siteDataList.indices){
                        if(selectedSiteId.equals(siteDataList.get(i).site)){
                            pos = i
                        }
                    }
                    siteIdDisplay =
                            SiteIdDisplay(applicationContext, selectsiteIdList, this)
                       activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay

                    siteDataList.get(pos!!).isSelected = true
                    siteIdAdapter?.notifyDataSetChanged()
//                    siteIdAdapter =
//                    SiteIdAdapter(applicationContext, siteDataList, this, selectsiteIdList)
//                activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
//                activitySelectSiteActivityBinding.fieldRecyclerView.adapter = siteIdAdapter
//                siteIdAdapter?.notifyDataSetChanged()
                }




//            else{
//                siteDataList.get(position1).isSelected = true
//                if (selectsiteIdList!= null){
//                    if (!selectsiteIdList.contains(siteDataList.get(position1).site)){
//                        selectsiteIdList.add(siteDataList.get(position1).site!!)
//                        siteIdDisplay =
//                            SiteIdDisplay(applicationContext, selectsiteIdList, this)
////                        activitySelectSiteActivityBinding.storeIdsRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context, LinearLayoutManager.VERTICAL, false)
//                        activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay
//                    }
//                }
//
//                siteIdAdapter =
//                    SiteIdAdapter(applicationContext, siteDataList, this, selectsiteIdList)
//                activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
//                activitySelectSiteActivityBinding.fieldRecyclerView.adapter = siteIdAdapter
////                siteIdAdapter?.notifyDataSetChanged()
//            }


        }else if(siteDataList == null && siteDataList.size==0 ) {
            selectsiteIdList.add(selectedSiteId!!)
            var pos: Int?=null
            for(i in siteDataList.indices){
                if(selectedSiteId.equals(siteDataList.get(i).site)){
                    pos = i
                }
            }

            SiteIdDisplay(applicationContext, selectsiteIdList, this)
//                        activitySelectSiteActivityBinding.storeIdsRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context, LinearLayoutManager.VERTICAL, false)
            activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay

            siteDataList.get(pos!!).isSelected = true
            siteIdAdapter?.notifyDataSetChanged()

//            siteIdAdapter =
//                SiteIdAdapter(applicationContext, siteDataList, this, selectsiteIdList)
//            activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
//            activitySelectSiteActivityBinding.fieldRecyclerView.adapter = siteIdAdapter
        }
//        else if(selectedSiteId.equals(Preferences.getSiteId())){
//            val builder = AlertDialog.Builder(this)
//            builder.setMessage("This Site Id is already selected")
//
//            val alert = builder.create()
//            alert.show()
//
//            val timer = Timer()
//            timer.schedule(object : TimerTask() {
//                override fun run() {
//                    alert.dismiss()
//                    timer.cancel()
//                }
//            }, 3000)
//        }






//        var isSameId: Boolean = false
//        if(selectsiteIdList.size>0 && selectsiteIdList!=null){
//            for (i in selectsiteIdList.indices) {
//                if (selectedSiteId.equals(selectsiteIdList.get(i))) {
//
//                    val builder = AlertDialog.Builder(this)
//                    builder.setMessage("You have already selected this site ID")
//
//                    val alert = builder.create()
//                    alert.show()
//
//                    val timer = Timer()
//                    timer.schedule(object : TimerTask() {
//                        override fun run() {
//                            alert.dismiss()
//                            timer.cancel()
//                        }
//                    }, 3000)
//                    isSameId = true
//                }
//            }
//            if (!isSameId) {
//
//                if(selectedSiteId==siteDataList.get(positionTick).site){
//                    siteDataList.get(positionTick).tickmarkDelete=true
//                }else{
//                    siteDataList.get(positionTick).tickmarkDelete=false
//                }
//                selectsiteIdList.add(selectedSiteId!!)
//                siteIdDisplay =
//                    SiteIdDisplay(applicationContext, selectsiteIdList, this)
//                val layoutManager = LinearLayoutManager(ViswamApp.context)
//                activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay
////                Toast.makeText(applicationContext,
////                    "SiteId: " + selectedSiteId.site + " selected",
////                    Toast.LENGTH_SHORT).show()
//
//            }
//            siteIdAdapter =
//                SiteIdAdapter(applicationContext, siteDataList, this, selectsiteIdList)
//            activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
//            activitySelectSiteActivityBinding.fieldRecyclerView.adapter = siteIdAdapter
//        }else {
//            if(selectedSiteId==siteDataList.get(positionTick).site){
//                siteDataList.get(positionTick).tickmarkDelete=true
//            }else{
//                siteDataList.get(positionTick).tickmarkDelete=false
//            }
//            selectsiteIdList.add(selectedSiteId!!)
//
//            siteIdDisplay =
//                SiteIdDisplay(applicationContext, selectsiteIdList, this)
//            val layoutManager = LinearLayoutManager(ViswamApp.context)
//            activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay
////            Toast.makeText(applicationContext,
////                "SiteId: " + selectedSiteId.site + " selected",
////                Toast.LENGTH_SHORT).show()
//
//        }
//
//        siteIdAdapter =
//                    SiteIdAdapter(applicationContext, siteDataList, this, selectsiteIdList)
//                activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
//                activitySelectSiteActivityBinding.fieldRecyclerView.adapter = siteIdAdapter
////                siteIdAdapter?.notifyDataSetChanged()
//


    }



    override fun onClickSubmit() {
//        if(selectsiteIdList.size==0){
//            Toast.makeText(applicationContext, "Please select siteId's", Toast.LENGTH_SHORT).show()
//        }
//         else if(selectsiteIdList.size!=null && selectsiteIdList.size>0 && selectsiteIdList.size<=10){
//            val returnIntent = Intent()
//            returnIntent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
//        }



    }

    override fun onClickCancel() {
        Utlis.hideKeyPad(this)
//        if(selectsiteIdList.size==0){
//            val returnIntent = Intent()
//            returnIntent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
//        }
//        else if(selectsiteIdList.size!=null && selectsiteIdList.size>0 && selectsiteIdList.size<=10){
//            val returnIntent = Intent()
//            returnIntent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
//        }

       super.onBackPressed()
    }

    override fun noOrdersFound(size: Int) {
        if (size > 0) {
            activitySelectSiteActivityBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectSiteActivityBinding.noOrderFoundText.setVisibility(View.VISIBLE)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

//            val returnIntent = Intent()
//            returnIntent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
    }

    override fun onClickCrossButton(selectsiteId: String, position: Int) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.deletesite_dialog)
        val close = dialog.findViewById<TextView>(R.id.no_btnSite)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnSite)
        ok.setOnClickListener {
            dialog.dismiss()
            selectsiteIdList.remove(selectsiteId)
            var pos: Int?=null
            for(i in siteDataList.indices){
                if(selectsiteId.equals(siteDataList.get(i).site)){
                    pos = i
                }
            }

            SiteIdDisplay(applicationContext, selectsiteIdList, this)
//                        activitySelectSiteActivityBinding.storeIdsRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context, LinearLayoutManager.VERTICAL, false)
            activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay

            siteDataList.get(pos!!).isSelected = false
            siteIdAdapter?.notifyDataSetChanged()

        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onClickCompleted() {
        val returnIntent = Intent()
        returnIntent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


}
