package com.apollopharmacy.vishwam.ui.home.apollosensing.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.ActivityApolloSensingStoreBinding
import com.apollopharmacy.vishwam.ui.home.apollosensing.activity.adapter.SiteIdAdapter
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis

class ApolloSensingStoreActivity : AppCompatActivity(), ApolloSensingStoreCallback {
    lateinit var activityApolloSensingStoreBinding: ActivityApolloSensingStoreBinding
    lateinit var viewModel: ApolloSensingStoreViewModel
    var siteDataList = ArrayList<StoreListItem>()
    lateinit var siteIdAdapter: SiteIdAdapter
    private lateinit var dialog: Dialog
    var isSiteIdEmpty:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApolloSensingStoreBinding =
            DataBindingUtil.setContentView(this@ApolloSensingStoreActivity,
                R.layout.activity_apollo_sensing_store)
        viewModel =
            ViewModelProvider(this)[ApolloSensingStoreViewModel::class.java]
        activityApolloSensingStoreBinding.callback = this
        Utlis.showLoading(this)
        viewModel.siteId()
        onSuccessSiteIdLIst()
    }

    private fun onSuccessSiteIdLIst() {
        viewModel.fixedArrayList.observeForever {
            siteDataList = it
            siteIdAdapter = SiteIdAdapter(
                applicationContext,
                siteDataList,
                this@ApolloSensingStoreActivity
            )
            activityApolloSensingStoreBinding.siteRecyclerView.adapter = siteIdAdapter
            Utlis.hideLoading()
        }
        searchBySiteId()
    }

    private fun searchBySiteId() {
        activityApolloSensingStoreBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 2) {
                    if (siteIdAdapter != null) {
                        siteIdAdapter.getFilter()!!.filter(s)
                    }
                } else {
                    if (siteIdAdapter != null) {
                        siteIdAdapter.getFilter()!!.filter("")
                    }
                }
            }

        })
    }

    override fun noOrdersFound(size: Int) {
        if (size > 0) {
            activityApolloSensingStoreBinding.noOrderFoundText.setVisibility(View.GONE)
        } else {
            activityApolloSensingStoreBinding.noOrderFoundText.setVisibility(View.VISIBLE)
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
            Preferences.setApolloSensingStoreId(storeListItem.site!!)
            if(storeListItem.store_name!=null){
                Preferences.setApolloSensingStoreName(storeListItem.store_name)
            }else{
                Preferences.setApolloSensingStoreName("")
            }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onClickCancel() {
        if(!Preferences.getApolloSensingStoreId().isEmpty()){
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
        if(!Preferences.getApolloSensingStoreId().isEmpty()){
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
}