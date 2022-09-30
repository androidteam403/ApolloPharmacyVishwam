package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.selectswachhid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.ActivitySelectSwachhSiteidBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.selectswachhid.adapter.SiteIdListAdapter
import com.apollopharmacy.vishwam.util.Utlis

class SelectSwachhSiteIDActivity : AppCompatActivity(), SelectSwachhSiteIdCallback {
    lateinit var activitySelectSwachhSiteidBinding: ActivitySelectSwachhSiteidBinding
    lateinit var viewModel: SelectSwachhSiteIdViewModel
    private var siteIDListAdapter: SiteIdListAdapter? = null
    var siteDataList = ArrayList<StoreListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectSwachhSiteidBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_swachh_siteid
        )
        viewModel = ViewModelProvider(this)[SelectSwachhSiteIdViewModel::class.java]
        activitySelectSwachhSiteidBinding.callback = this
        Utlis.showLoading(this)
        viewModel.siteId()
        onSuccessSiteIdLIst()
    }

    fun onSuccessSiteIdLIst() {
        viewModel.fixedArrayList.observeForever {
            siteDataList = it



            siteIDListAdapter =
                SiteIdListAdapter(applicationContext, siteDataList, this)
//            activitySelectSiteActivityBinding.fieldRecyclerView.layoutManager = LinearLayoutManager(ViswamApp.context)
            activitySelectSwachhSiteidBinding.fieldRecyclerView.adapter = siteIDListAdapter
            Utlis.hideLoading()
        }
        searchByFulfilmentId()
    }

    private fun searchByFulfilmentId() {
        activitySelectSwachhSiteidBinding.searchSiteText.addTextChangedListener(object :
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

    }

    override fun onBackPressed() {

    }

    override fun noOrdersFound(size: Int) {
        if (size > 0) {
            activitySelectSwachhSiteidBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectSwachhSiteidBinding.noOrderFoundText.setVisibility(View.VISIBLE)
        }
    }

    override fun onItemClick(storeListItem: StoreListItem) {
        Preferences.setSwachhSiteId(storeListItem.site!!)
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}