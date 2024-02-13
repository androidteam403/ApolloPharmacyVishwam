package com.apollopharmacy.vishwam.ui.home.communityadvisor.siteid

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
import com.apollopharmacy.vishwam.databinding.ActivitySelectCommunityAdvisorSiteidBinding
import com.apollopharmacy.vishwam.ui.home.communityadvisor.siteid.adapter.SiteIdListCommunityAdvisorAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.google.gson.Gson

class SelectCommunityAdvisorSiteIdActivity : AppCompatActivity(),
    SelectCommunityAdvisorSiteIdCallback {
    lateinit var activitySelectCommunityAdvisorSiteidBinding: ActivitySelectCommunityAdvisorSiteidBinding
    lateinit var viewModel: SelectCommunityAdvisorSiteIdViewModel
    private var siteIdListCommunityAdvisorAdapter: SiteIdListCommunityAdvisorAdapter? = null
    var siteDataList = ArrayList<StoreDetailsModelResponse.Row>()
    var isSiteIdEmpty: Boolean = false
    private lateinit var dialog: Dialog

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectCommunityAdvisorSiteidBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_community_advisor_siteid
        )
        viewModel = ViewModelProvider(this)[SelectCommunityAdvisorSiteIdViewModel::class.java]
        activitySelectCommunityAdvisorSiteidBinding.callback = this
        showLoading(this)
        viewModel.getProxySiteListResponse(this, applicationContext)
        viewModel.fixedArrayList.observeForever {
            siteDataList = it
            Preferences.setSiteIdListFetchedChamps(true)
            Preferences.setSiteIdListChamps(Gson().toJson(siteDataList))
            siteIdListCommunityAdvisorAdapter =
                SiteIdListCommunityAdvisorAdapter(
                    this@SelectCommunityAdvisorSiteIdActivity,
                    siteDataList,
                    this
                )
            activitySelectCommunityAdvisorSiteidBinding.fieldRecyclerView.adapter =
                siteIdListCommunityAdvisorAdapter
            Utlis.hideLoading()
        }

       /* viewModel.commands.observeForever {
            when (it) {
                is SelectCommunityAdvisorSiteIdViewModel.Command.ShowToast -> {
                    Utlis.hideLoading()
                    Preferences.setSiteIdListChamps(Gson().toJson(siteDataList))
                    Preferences.setSiteIdListFetchedChamps(true)
                }

                else -> {}
            }

        }*/
        searchByFulfilmentId()
    }

    private fun searchByFulfilmentId() {
        activitySelectCommunityAdvisorSiteidBinding.searchSiteText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length >= 2) {
                    if (siteIdListCommunityAdvisorAdapter != null) {
                        siteIdListCommunityAdvisorAdapter!!.getFilter()?.filter(editable)
                    }
                } else {
                    if (siteIdListCommunityAdvisorAdapter != null) {
                        siteIdListCommunityAdvisorAdapter!!.getFilter()?.filter("")
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
            //setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            isSiteIdEmpty = true
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            //  setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        if (!siteId!!.isEmpty()) {
            isSiteIdEmpty = false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            // setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            isSiteIdEmpty = true
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            //  setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun noOrdersFound(size: Int) {
        if (size > 0) {
            activitySelectCommunityAdvisorSiteidBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectCommunityAdvisorSiteidBinding.noOrderFoundText.setVisibility(View.VISIBLE)
        }
    }

    var siteId: String? = ""
    var siteName: String? = ""


    override fun onItemClick(site: StoreDetailsModelResponse.Row) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.change_siteid)
        val close = dialog.findViewById<TextView>(R.id.no_btnSiteChange)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnSiteChange)
        ok.setOnClickListener {
            dialog.dismiss()
            siteId = site.site
            this.siteName = site.storeName;
            Preferences.setCommunityAdvisorSiteId(siteId.orEmpty())
            Preferences.setCommunityAdvisorStoreName(siteName.orEmpty())
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
        Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
    }

    override fun onSuccessgetStoreWiseDetails(getStoreWiseDetailsResponses: GetStoreWiseDetailsModelResponse) {
        if (true && getStoreWiseDetailsResponses.success && getStoreWiseDetailsResponses.data.executive != null) {
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
        Utlis.hideLoading()
    }

    override fun onFailureUat() {
       hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }

}