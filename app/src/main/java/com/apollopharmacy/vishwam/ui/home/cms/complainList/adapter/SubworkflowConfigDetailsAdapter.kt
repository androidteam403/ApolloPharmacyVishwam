package com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterSubworkflowConfigDetailsActionsBinding
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubworkflowConfigDetailsResponse

class SubworkflowConfigDetailsAdapter(
    var context: Context,
    var rowsList: ArrayList<SubworkflowConfigDetailsResponse.Rows>,
) :
    RecyclerView.Adapter<SubworkflowConfigDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubworkflowConfigDetailsAdapter.ViewHolder {
        val adapterSubworkflowConfigDetailsActionsBinding =
            DataBindingUtil.inflate<AdapterSubworkflowConfigDetailsActionsBinding>(
                LayoutInflater.from(context),
                R.layout.adapter_subworkflow_config_details_actions,
                parent,
                false
            )
        return SubworkflowConfigDetailsAdapter.ViewHolder(
            adapterSubworkflowConfigDetailsActionsBinding
        )
    }

    override fun onBindViewHolder(
        holder: SubworkflowConfigDetailsAdapter.ViewHolder,
        position: Int,
    ) {
        val row = rowsList.get(position)
        holder.adapterSubworkflowConfigDetailsActionsBinding.actionBtn.text =
            "${row.action!!.action}"
        holder.adapterSubworkflowConfigDetailsActionsBinding.actionBtn.setBackgroundColor(
            Color.parseColor(
                row.action!!.backgroundColor!!
            )
        )
        holder.adapterSubworkflowConfigDetailsActionsBinding.actionBtn.setTextColor(
            Color.parseColor(
                row.action!!.textColor!!
            )
        )
    }

    override fun getItemCount(): Int {
        return rowsList.size
    }

    class ViewHolder(var adapterSubworkflowConfigDetailsActionsBinding: AdapterSubworkflowConfigDetailsActionsBinding) :
        RecyclerView.ViewHolder(adapterSubworkflowConfigDetailsActionsBinding.root)
}