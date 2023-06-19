package com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.databinding.AdapterSubworkflowConfigDetailsActionsBinding
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ImageClickListener
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubworkflowConfigDetailsResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketData


class SubworkflowConfigDetailsAdapter(
    var context: Context,
    val imageClickListener: ImageClickListener,
    var rowsList: ArrayList<SubworkflowConfigDetailsResponse.Rows>,
    val data: TicketData,
    val responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
    val positionHeader: Int,
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
        holder.adapterSubworkflowConfigDetailsActionsBinding.row = row
        holder.adapterSubworkflowConfigDetailsActionsBinding.actionBtn.text =
            "${row.action!!.name}"
        val colorInt = Color.parseColor(row.action!!.backgroundColor!!)
        ViewCompat.setBackgroundTintList(
            holder.adapterSubworkflowConfigDetailsActionsBinding.actionBtn,
            ColorStateList.valueOf(colorInt)
        )
        holder.adapterSubworkflowConfigDetailsActionsBinding.actionBtn.setTextColor(
            Color.parseColor(
                row.action!!.textColor!!
            )
        )

        holder.itemView.setOnClickListener {
            imageClickListener.onClickAction(
                data,
                responseList,
                positionHeader,
                row
            )
        }
    }

    override fun getItemCount(): Int {
        return rowsList.size
    }

    class ViewHolder(var adapterSubworkflowConfigDetailsActionsBinding: AdapterSubworkflowConfigDetailsActionsBinding) :
        RecyclerView.ViewHolder(adapterSubworkflowConfigDetailsActionsBinding.root)
}