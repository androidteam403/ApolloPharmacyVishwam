package com.apollopharmacy.vishwam.ui.home.retroqr.selectqrretrositeid

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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivitySelectQrSiteBinding
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.retroqr.selectqrretrositeid.adapter.QrSiteIdListAdapter
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.google.gson.Gson

class SelectRetroQrSiteIDActivity : AppCompatActivity(), SelectQrRetroSiteIdCallback {
    lateinit var activitySelectQrSiteBinding: ActivitySelectQrSiteBinding
    lateinit var viewModel: SelectQrRetroSiteIdViewModel
    private var siteIDListAdapter: QrSiteIdListAdapter? = null
    var siteDataList = ArrayList<StoreDetailsModelResponse.Row>()
    var isSiteIdEmpty: Boolean = false
    private lateinit var dialog: Dialog

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectQrSiteBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_qr_site
        )
        viewModel = ViewModelProvider(this)[SelectQrRetroSiteIdViewModel::class.java]
        activitySelectQrSiteBinding.callback = this
        showLoading(this)
        viewModel.getProxySiteListResponse(this)
        viewModel.fixedArrayList.observeForever {
            siteDataList = it
            siteIDListAdapter =
                QrSiteIdListAdapter(applicationContext, siteDataList, this)
            activitySelectQrSiteBinding.fieldRecyclerView.adapter = siteIDListAdapter
            hideLoading()

        }

        viewModel.commands.observeForever {
            when (it) {
                is SelectQrRetroSiteIdViewModel.Command.ShowToast -> {
                    hideLoading()
                    Preferences.setQrSiteIdList(Gson().toJson(viewModel.getSiteData()))
                    Preferences.setSiteIdListFetchedQrRetro(true)
                }


                else -> {}
            }

        }
        searchByFulfilmentId()
    }


    private fun searchByFulfilmentId() {
        activitySelectQrSiteBinding.searchSiteText.addTextChangedListener(object :
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
        if (!Preferences.getQrSiteId().isEmpty()) {
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

        if (!Preferences.getQrSiteId().isEmpty()) {
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
            activitySelectQrSiteBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectQrSiteBinding.noOrderFoundText.setVisibility(View.VISIBLE)
        }
    }

    var siteId: String? = ""
    override fun onItemClick(site: String, siteName: String) {


        dialog = Dialog(this)
        dialog.setContentView(R.layout.change_siteid)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val close = dialog.findViewById<TextView>(R.id.no_btnSiteChange)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnSiteChange)
        ok.setOnClickListener {
            dialog.dismiss()
//            Preferences.setSwachhSiteId(storeListItem.siteid!!)
            siteId = site
            Preferences.setQrSiteId(site)
            Preferences.setQrSiteName(siteName)
            val intent = Intent()
            intent.putExtra("siteId", site)
            intent.putExtra("sitename", siteName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        dialog.show()
    }

    override fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>) {

        hideLoading()

    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
//    Toast.makeText(applicationContext,""+value.message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }


}