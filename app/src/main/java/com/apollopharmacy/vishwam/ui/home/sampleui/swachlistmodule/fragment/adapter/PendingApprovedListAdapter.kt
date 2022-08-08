package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPendingApprovedListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.SwachhListCallback
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved

class PendingApprovedListAdapter(
    val mContext: Context?,
    val pendingApprovedList: ArrayList<PendingAndApproved>,
    val mCallback: SwachhListCallback
) :
    RecyclerView.Adapter<PendingApprovedListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val pendingApprovedListBinding: AdapterPendingApprovedListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.adapter_pending_approved_list,
                parent,
                false
            )
        return ViewHolder(pendingApprovedListBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pendingApprovedListBinding.model = pendingApprovedList.get(position)
        holder.pendingApprovedListBinding.updateButton.setOnClickListener {
            mCallback.onClickUpdate(
                pendingApprovedList.get(position)
            )
        }

    }


    override fun getItemCount(): Int {
        return pendingApprovedList.size
    }

    class ViewHolder(val pendingApprovedListBinding: AdapterPendingApprovedListBinding) :
        RecyclerView.ViewHolder(pendingApprovedListBinding.root)
}