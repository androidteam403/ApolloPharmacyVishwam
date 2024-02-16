package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.filter

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
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.ActivitySelectRectroSiteidBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.filter.adapter.SiteIdAdapterRectro
import com.apollopharmacy.vishwam.util.Utlis

class SelectPreRectroSiteActivity : AppCompatActivity(), SelectRectroSiteCallback {
    lateinit var activitySelectSwachhSiteidBinding: ActivitySelectRectroSiteidBinding
    lateinit var viewModel: SelectRectroSiteViewModel
    private var siteIDListAdapter: SiteIdAdapterRectro? = null
    var siteDataList = ArrayList<StoreListItem>()
    var isSiteIdEmpty: Boolean = false
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectSwachhSiteidBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_rectro_siteid
        )
        viewModel = ViewModelProvider(this)[SelectRectroSiteViewModel::class.java]
        activitySelectSwachhSiteidBinding.callback = this
        viewModel.siteId(applicationContext)
        onSuccessSiteIdLIst()
    }

    fun onSuccessSiteIdLIst() {
        viewModel.fixedArrayList.observeForever {
            siteDataList = it



            Preferences.setSiteRetroListFetched(true)

            siteIDListAdapter =
                SiteIdAdapterRectro(applicationContext, siteDataList, this)
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
        if (!Preferences.getRectroSiteId().isEmpty()) {
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
            activitySelectSwachhSiteidBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activitySelectSwachhSiteidBinding.noOrderFoundText.setVisibility(View.VISIBLE)
        }
    }

    override fun onBackPressed() {

        if (!Preferences.getSwachhSiteId().isEmpty()) {
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


    override fun onItemClick(storeListItem: StoreListItem) {


        dialog = Dialog(this)
        dialog.setContentView(R.layout.change_siteid)
        val close = dialog.findViewById<TextView>(R.id.no_btnSiteChange)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnSiteChange)
        ok.setOnClickListener {
            dialog.dismiss()
            Preferences.setApnaRetroSite(storeListItem.site!!)
            Toast.makeText(this,storeListItem.site!!,Toast.LENGTH_LONG).show()
            Preferences.setRectroSiteId(storeListItem.site!!)
            Preferences.setRectroSiteName(storeListItem.store_name!!)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }
}