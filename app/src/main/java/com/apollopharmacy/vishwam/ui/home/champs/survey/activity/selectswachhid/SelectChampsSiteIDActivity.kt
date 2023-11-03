package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid

import android.annotation.SuppressLint
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
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.adapter.SiteIdListChampsAdapter
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.google.gson.Gson

class SelectChampsSiteIDActivity : AppCompatActivity(), SelectChampsSiteIdCallback {
    lateinit var activitySelectChampsSiteidBinding: ActivitySelectChampsSiteidBinding
    lateinit var viewModel: SelectChampsSiteIdViewModel
    private var siteIDListAdapter: SiteIdListChampsAdapter? = null
    var siteDataList = ArrayList<StoreDetailsModelResponse.Row>()
    var isSiteIdEmpty: Boolean = false
    private lateinit var dialog: Dialog
    public var storeIdSelected = ""
    var siteId: String? = ""
    var siteName: String? = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectChampsSiteidBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_champs_siteid
        )
        viewModel = ViewModelProvider(this)[SelectChampsSiteIdViewModel::class.java]
        activitySelectChampsSiteidBinding.callback = this
        showLoading(this)
        viewModel.getProxySiteListResponse(this)
        viewModel.fixedArrayList.observeForever {
            siteDataList = it
            siteIDListAdapter =
                SiteIdListChampsAdapter(applicationContext, siteDataList, this)
            activitySelectChampsSiteidBinding.fieldRecyclerView.adapter = siteIDListAdapter
            hideLoading()

        }

        viewModel.commands.observeForever {
            when (it) {
                is SelectChampsSiteIdViewModel.Command.ShowToast -> {
                    hideLoading()
                    Preferences.setSiteIdListChamps(Gson().toJson(viewModel.getSiteData()))
                    Preferences.setSiteIdListFetchedChamps(true)
                }


                else -> {}
            }

        }
        searchByFulfilmentId()
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
        Utlis.hideKeyPad(this)
        if (!siteId!!.isEmpty()) {
            isSiteIdEmpty = false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            intent.putExtra("siteId", siteId)
            intent.putExtra("siteName", siteName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            isSiteIdEmpty = true
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            intent.putExtra("siteId", siteId)
            intent.putExtra("siteName", siteName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
//        val intent = Intent()
//        setResult(Activity.RESULT_OK, intent)
//        finish()

        if (!siteId!!.isEmpty()) {
            isSiteIdEmpty = false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            intent.putExtra("siteId", siteId)
            intent.putExtra("siteName", siteName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            isSiteIdEmpty = true
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            intent.putExtra("siteId", siteId)
            intent.putExtra("siteName", siteName)
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



    override fun onItemClick(site: String, siteName: String) {


        dialog = Dialog(this)
        dialog.setContentView(R.layout.change_siteid)
        val close = dialog.findViewById<TextView>(R.id.no_btnSiteChange)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnSiteChange)
        ok.setOnClickListener {
            dialog.dismiss()
//            Preferences.setSwachhSiteId(storeListItem.siteid!!)
            siteId = site
            this.siteName = siteName;
            Preferences.setApnaSiteName(siteName)


            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                showLoading(this)
                viewModel.getProxyStoreWiseDetailResponse(
                    this,
                    site
                )
            } else {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }


            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.show()

//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)
//            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    override fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>) {
//        siteIDListAdapter =
//            SiteIdListChampsAdapter(applicationContext,
//                value as ArrayList<StoreDetailsModelResponse.Row>, this)
////            activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
//        activitySelectChampsSiteidBinding.fieldRecyclerView.adapter = siteIDListAdapter
        hideLoading()

    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
//    Toast.makeText(applicationContext,""+value.message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }

    override fun onSuccessgetStoreWiseDetails(getStoreWiseDetailsResponses: GetStoreWiseDetailsModelResponse) {
        if (getStoreWiseDetailsResponses != null && getStoreWiseDetailsResponses.success && getStoreWiseDetailsResponses.data.executive != null) {
//            viewBinding.emailId.setText(getStoreWiseDetailsResponses.storeWiseDetails.executiveEmail)
            Preferences.setApnaSite(siteId!!)
            Preferences.setChampsSiteName(siteName!!)
            val intent = Intent()
            intent.putExtra("siteId", siteId)
            intent.putExtra("siteName", siteName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(applicationContext, "No data found", Toast.LENGTH_SHORT).show()
        }
        hideLoading()
    }

    override fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse) {
        Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }
}