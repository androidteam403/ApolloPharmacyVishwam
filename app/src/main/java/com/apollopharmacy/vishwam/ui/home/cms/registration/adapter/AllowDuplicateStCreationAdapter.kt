package com.apollopharmacy.vishwam.ui.home.cms.registration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterAllowDuplicateStCreationBinding
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.SiteTicketbyReasonResponse

class AllowDuplicateStCreationAdapter(var rows: ArrayList<SiteTicketbyReasonResponse.Rows>?) :
    RecyclerView.Adapter<AllowDuplicateStCreationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllowDuplicateStCreationAdapter.ViewHolder {
        var adapterAllowDuplicateStCreationBinding =
            DataBindingUtil.inflate<AdapterAllowDuplicateStCreationBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_allow_duplicate_st_creation,
                parent,
                false
            )
        return AllowDuplicateStCreationAdapter.ViewHolder(adapterAllowDuplicateStCreationBinding)
    }

    override fun onBindViewHolder(
        holder: AllowDuplicateStCreationAdapter.ViewHolder,
        position: Int
    ) {
        val row = rows!!.get(position)
        holder.adapterAllowDuplicateStCreationBinding.ticketIdWithStatus.text =
            "${row.ticketId} (${row.status!!.name})"
    }

    override fun getItemCount(): Int {
        return rows!!.size
    }

    class ViewHolder(val adapterAllowDuplicateStCreationBinding: AdapterAllowDuplicateStCreationBinding) :
        RecyclerView.ViewHolder(adapterAllowDuplicateStCreationBinding.root)
}