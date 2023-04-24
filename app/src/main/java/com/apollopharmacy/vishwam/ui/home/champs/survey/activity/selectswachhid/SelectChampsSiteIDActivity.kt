package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid

import android.app.Activity
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
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivitySelectChampsSiteidBinding
import com.apollopharmacy.vishwam.databinding.ActivitySelectSwachhSiteidBinding
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.adapter.SiteIdListChampsAdapter
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis

class SelectChampsSiteIDActivity : AppCompatActivity(), SelectChampsSiteIdCallback {
    lateinit var activitySelectChampsSiteidBinding: ActivitySelectChampsSiteidBinding
    lateinit var viewModel: SelectChampsSiteIdViewModel
    private var siteIDListAdapter: SiteIdListChampsAdapter? = null
    var siteDataList = ArrayList<StoreDetailsModelResponse.StoreDetail>()
    var isSiteIdEmpty:Boolean=false
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectChampsSiteidBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_champs_siteid
        )
        viewModel = ViewModelProvider(this)[SelectChampsSiteIdViewModel::class.java]
        activitySelectChampsSiteidBinding.callback = this
        Utlis.showLoading(this)
        viewModel.getStoreDetailsChamps(this)
//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            viewModel.getStoreDetailsChampsApi(this)
//        }
//        else {
//            Toast.makeText(
//                applicationContext,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }
    }

    private fun searchByFulfilmentId() {
        activitySelectChampsSiteidBinding.searchSiteText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length >= 2) {
                    if (siteIDListAdapter != null) {
                        siteIDListAdapter!!.getFilter()?.filter(editable)
                    }
                } else {
                    if (siteIDListAdapter != null) {
                        siteIDListAdapter!!.getFilter()?.filter("")
                    }
                }
            }
        })
    }

    override fun onClickCancel() {
        if(!Preferences.getSwachhSiteId().isEmpty()){
            isSiteIdEmpty=false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }else{
            isSiteIdEmpty=true
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
//        val intent = Intent()
//        setResult(Activity.RESULT_OK, intent)
//        finish()

        if(!Preferences.getSwachhSiteId().isEmpty()){
            isSiteIdEmpty=false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }else{
            isSiteIdEmpty=true
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    override fun noOrdersFound(size: Int) {
        if (size > 0) {
            activitySelectChampsSiteidBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectChampsSiteidBinding.noOrderFoundText.setVisibility(View.VISIBLE)
        }
    }

    override fun onItemClick(storeListItem: StoreDetailsModelResponse.StoreDetail) {


        dialog = Dialog(this)
        dialog.setContentView(R.layout.change_siteid)
        val close = dialog.findViewById<TextView>(R.id.no_btnSiteChange)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnSiteChange)
        ok.setOnClickListener {
            dialog.dismiss()
            Preferences.setSwachhSiteId(storeListItem.siteid!!)
            Preferences.setSwachSiteName(storeListItem.sitename!!)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    override fun onSuccessgetStoreDetails(value: StoreDetailsModelResponse) {
        siteIDListAdapter =
            SiteIdListChampsAdapter(applicationContext, value.storeDetails, this)
//            activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
        activitySelectChampsSiteidBinding.fieldRecyclerView.adapter = siteIDListAdapter
        Utlis.hideLoading()

        searchByFulfilmentId()
    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
    Toast.makeText(applicationContext,""+value.message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
    }
}