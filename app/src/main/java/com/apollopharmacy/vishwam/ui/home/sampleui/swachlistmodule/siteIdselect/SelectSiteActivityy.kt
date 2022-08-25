package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.siteIdselect

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.ActivitySelectSiteActivityyBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.siteIdselect.adapter.SiteIdAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.siteIdselect.adapter.SiteIdDisplay
import com.apollopharmacy.vishwam.util.Utlis
import java.util.*

class SelectSiteActivityy : AppCompatActivity(), SelectSiteIdCallback {
    var selectsiteIdList = ArrayList<String>()
    lateinit var activitySelectSiteActivityBinding: ActivitySelectSiteActivityyBinding
    lateinit var viewModel: SelectSiteActivityViewModel
    lateinit var siteData: ArrayList<StoreListItem>
    private lateinit var siteIdAdapter: SiteIdAdapter
    private lateinit var dialog: Dialog
    private lateinit var siteIdDisplay: SiteIdDisplay
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
//        Utlis.showLoading(context)

        Utlis.showLoading(this)
        viewModel.siteId()

//        viewModel.siteLiveData.observeForever {
//            if(it!=null){
//                siteLiveDataList.add(it)
//            }


        viewModel.fixedArrayList.observeForever {
//            siteData.add(it)
            siteIdAdapter =
                SiteIdAdapter(applicationContext, it, this, selectsiteIdList, tickMarkDelete)
            val layoutManager = LinearLayoutManager(ViswamApp.context)
            activitySelectSiteActivityBinding.fieldRecyclerView.adapter = siteIdAdapter
//            Utlis.hideLoading()
            Utlis.hideLoading()
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
                        siteIdAdapter.getFilter()?.filter(editable)
                    }
                } else {
                    if (siteIdAdapter != null) {
                        siteIdAdapter.getFilter()?.filter("")
                    }
                }
            }
        })
    }




    override fun onClickSiteId(selectedSiteId: StoreListItem) {
        var isSameId: Boolean = false
        if(selectsiteIdList.size>0 && selectsiteIdList!=null){
            for (i in selectsiteIdList.indices) {
                if (selectedSiteId.site.equals(selectsiteIdList.get(i))) {

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
            if (!isSameId) {
                selectsiteIdList.add(selectedSiteId.site!!)
//                Toast.makeText(applicationContext,
//                    "SiteId: " + selectedSiteId.site + " selected",
//                    Toast.LENGTH_SHORT).show()
            }
        }else {
            selectsiteIdList.add(selectedSiteId.site!!)
//            Toast.makeText(applicationContext,
//                "SiteId: " + selectedSiteId.site + " selected",
//                Toast.LENGTH_SHORT).show()

        }

        siteIdDisplay =
            SiteIdDisplay(applicationContext, selectsiteIdList, this)
        val layoutManager = LinearLayoutManager(ViswamApp.context)
        activitySelectSiteActivityBinding.storeIdsRecyclerView.adapter = siteIdDisplay


    }



    override fun onClickSubmit() {
        if(selectsiteIdList.size==0){
            Toast.makeText(applicationContext, "Please select siteId's", Toast.LENGTH_SHORT).show()
        }
         else if(selectsiteIdList.size!=null && selectsiteIdList.size>0 && selectsiteIdList.size<=10){
            val returnIntent = Intent()
            returnIntent.putStringArrayListExtra("selectsiteIdList", selectsiteIdList)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }



    }

    override fun onClickCancel() {
       super.onBackPressed()
    }

    override fun noOrdersFound(size: Int) {
        if (size > 0) {
            activitySelectSiteActivityBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectSiteActivityBinding.noOrderFoundText.setVisibility(View.VISIBLE)
        }
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
            tickMarkDelete=true

            siteIdDisplay?.notifyDataSetChanged()
            siteIdAdapter.notifyDataSetChanged()

        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


}
