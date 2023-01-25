package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterStoreidsDisplayBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.SwachhListCallback
import java.util.ArrayList

class SiteIdDisplayAdapter(
    val context: Context?,
    val selectsiteIdList: ArrayList<String>, val mCallback: SwachhListCallback,
) : RecyclerView.Adapter<SiteIdDisplayAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterStoreidsDisplayBinding: AdapterStoreidsDisplayBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_storeids_display,
                parent,
                false
            )
        return ViewHolder(adapterStoreidsDisplayBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectsiteId = selectsiteIdList.get(position)
        holder.adapterStoreidsDisplayBinding.siteIdS.text = selectsiteId
        holder.adapterStoreidsDisplayBinding.crossButton.setOnClickListener {
            mCallback.onClickCrossButton(selectsiteId, position)
        }
    }


    override fun getItemCount(): Int {
        return selectsiteIdList.size
    }

    class ViewHolder(val adapterStoreidsDisplayBinding: AdapterStoreidsDisplayBinding) :
        RecyclerView.ViewHolder(adapterStoreidsDisplayBinding.root)
}