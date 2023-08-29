package com.apollopharmacy.vishwam.ui.home.planogram.siteid

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
import com.apollopharmacy.vishwam.databinding.ActivitySelectPlanogramSiteidBinding
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.planogram.siteid.adapter.SiteIdListPlanogramAdapter
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.google.gson.Gson

class SelectPlanogramSiteIDActivity : AppCompatActivity(), SelectPlanogramSiteIdCallback {
    lateinit var activitySelectPlanogramSiteidBinding: ActivitySelectPlanogramSiteidBinding
    lateinit var viewModel: SelectPlangramSiteIdViewModel
    private var siteIDListAdapter: SiteIdListPlanogramAdapter? = null
    var siteDataList = ArrayList<StoreDetailsModelResponse.Row>()
    var isSiteIdEmpty: Boolean = false
    private lateinit var dialog: Dialog

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectPlanogramSiteidBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_planogram_siteid
        )
        viewModel = ViewModelProvider(this)[SelectPlangramSiteIdViewModel::class.java]
        activitySelectPlanogramSiteidBinding.callback = this
        showLoading(this)
        viewModel.getProxySiteListResponse(this)
        viewModel.fixedArrayList.observeForever {
            siteDataList = it
            siteIDListAdapter =
                SiteIdListPlanogramAdapter(applicationContext, siteDataList, this)
            activitySelectPlanogramSiteidBinding.fieldRecyclerView.adapter = siteIDListAdapter
            hideLoading()

        }

        viewModel.commands.observeForever {
            when (it) {
                is SelectPlangramSiteIdViewModel.Command.ShowToast -> {
                    hideLoading()
                    Preferences.setSiteIdListPlanogram(Gson().toJson(viewModel.getSiteData()))
                    Preferences.setSiteIdListFetchedPlanogram(true)
                }


                else -> {}
            }

        }
        searchByFulfilmentId()
    }


    private fun searchByFulfilmentId() {
        activitySelectPlanogramSiteidBinding.searchSiteText.addTextChangedListener(object :
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
        if (!Preferences.getPlanogramSiteId().isEmpty()) {
            isSiteIdEmpty = false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            isSiteIdEmpty = true
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

        if (!Preferences.getPlanogramSiteId().isEmpty()) {
            isSiteIdEmpty = false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            isSiteIdEmpty = true
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    override fun noOrdersFound(size: Int) {
        if (size > 0) {
            activitySelectPlanogramSiteidBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectPlanogramSiteidBinding.noOrderFoundText.setVisibility(View.VISIBLE)
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
//            Preferences.setSwachhSiteId(storeListItem.siteid!!)
            siteId = site.site
            this.siteName = site.storeName;
            Preferences.setPlanogramSiteId(siteId!!)
            Preferences.setPlanogramSiteName(siteName!!)
            Preferences.setPlanogramSiteState(site.state!!.name!!.toString())
            Preferences.setPlanogramSiteCity(site.city!!)


//            Preferences.setExecutiveName(site.)
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()


            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.show()

//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)
//            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }


    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
        Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }


}