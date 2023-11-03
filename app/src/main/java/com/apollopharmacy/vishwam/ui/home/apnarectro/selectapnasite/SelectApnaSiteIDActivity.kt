package com.apollopharmacy.vishwam.ui.home.apnarectro.selectapnasite

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
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.ActivitySelectApnaSiteidBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.selectapnasite.adapter.ApnaSiteIdListAdapter
import com.apollopharmacy.vishwam.util.Utlis

class SelectApnaSiteIDActivity : AppCompatActivity(), SelectApnaSiteIdCallback {
    lateinit var activitySelectSwachhSiteidBinding: ActivitySelectApnaSiteidBinding
    lateinit var viewModel: SelectApnaSiteIdViewModel
    private var siteIDListAdapter: ApnaSiteIdListAdapter? = null
    var siteDataList = ArrayList<StoreListItem>()
    var isSiteIdEmpty:Boolean=false
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectSwachhSiteidBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_select_apna_siteid
        )
        viewModel = ViewModelProvider(this)[SelectApnaSiteIdViewModel::class.java]
        activitySelectSwachhSiteidBinding.callback = this
        Utlis.showLoading(this)
        viewModel.siteId()
        onSuccessSiteIdLIst()
    }

    fun onSuccessSiteIdLIst() {
        viewModel.fixedArrayList.observeForever {
            siteDataList = it



            siteIDListAdapter =
                ApnaSiteIdListAdapter(applicationContext, siteDataList, this)
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
        Utlis.hideKeyPad(this)
        if(!Preferences.getApnaSiteId().isEmpty()){
            isSiteIdEmpty=false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        else{
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

        if(!Preferences.getApnaSiteId().isEmpty()){
            isSiteIdEmpty=false
            val intent = Intent()
            intent.putExtra("isSiteIdEmpty", isSiteIdEmpty)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        else{
            isSiteIdEmpty=true
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
            Preferences.setApnaSiteName(storeListItem.store_name!!)

            Preferences.setApnaSite(storeListItem.site!!)
            if(storeListItem.store_name!=null){
                Preferences.setRectroSiteName(storeListItem.store_name!!)

            }else{
                Preferences.setRectroSiteName("")

            }

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