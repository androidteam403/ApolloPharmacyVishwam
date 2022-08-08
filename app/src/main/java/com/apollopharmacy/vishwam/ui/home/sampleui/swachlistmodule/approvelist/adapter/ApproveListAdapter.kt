package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterApproveListBinding

class ApproveListAdapter(val mContext: Context) :
    RecyclerView.Adapter<ApproveListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterApproveListBinding: AdapterApproveListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_approve_list, parent, false
        )
        return ViewHolder(adapterApproveListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 0
    }

    class ViewHolder(adapterApproveListBinding: AdapterApproveListBinding) :
        RecyclerView.ViewHolder(adapterApproveListBinding.root)
}