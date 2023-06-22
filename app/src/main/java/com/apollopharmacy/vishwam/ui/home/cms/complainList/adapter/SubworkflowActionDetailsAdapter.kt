package com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.databinding.AdapterSubworkflowActionDetailsBinding

class SubworkflowActionDetailsAdapter(
    var context: Context,
    var ticketSubworkflowHistory: ArrayList<ResponseNewTicketlist.TicketSubworkflowHistory>,
) :
    RecyclerView.Adapter<SubworkflowActionDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubworkflowActionDetailsAdapter.ViewHolder {
        val adapterSubworkflowActionDetailsBinding =
            DataBindingUtil.inflate<AdapterSubworkflowActionDetailsBinding>(
                LayoutInflater.from(context),
                R.layout.adapter_subworkflow_action_details,
                parent,
                false
            )
        return SubworkflowActionDetailsAdapter.ViewHolder(
            adapterSubworkflowActionDetailsBinding
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: SubworkflowActionDetailsAdapter.ViewHolder,
        position: Int,
    ) {
        val row = ticketSubworkflowHistory.get(position)
        holder.adapterSubworkflowActionDetailsBinding.subworkflowActionDetailsCount.text =
            "${position + 1}."
        var actionDetails = ""
        if (row.createdId != null && row.createdId!!.firstName != null) {
            actionDetails = row.createdId!!.firstName!!
        }
        if (row.createdId != null && row.createdId!!.middleName != null) {
            actionDetails = "${actionDetails}  ${row.createdId!!.middleName!!}"
        }
        if (row.createdId != null && row.createdId!!.lastName != null) {
            actionDetails = "${actionDetails}  ${row.createdId!!.lastName!!}"
        }
        if (row.createdId != null && row.createdId!!.loginUnique != null) {
            actionDetails = "${actionDetails}  (${row.createdId!!.loginUnique!!}"
        }
        if (row.role != null && row.role!!.name != null) {
            actionDetails = "${actionDetails} - ${row.role!!.name!!}"
        }
        if (row.department != null && row.department!!.name != null) {
            actionDetails = "${actionDetails} - ${row.department!!.name})"
        }
        if (row.action != null && row.action!!.action != null) {
            actionDetails = "${actionDetails} ${row.action!!.action!!}."
        }
        if (row.ticket_status != null && row.ticket_status!!.name != null) {
            actionDetails = "${actionDetails}\nTicket Status: ${row.ticket_status!!.name!!}."
        }
        holder.adapterSubworkflowActionDetailsBinding.subworkflowActionDetails.setText(actionDetails)
    }

    override fun getItemCount(): Int {
        return ticketSubworkflowHistory.size
    }

    class ViewHolder(var adapterSubworkflowActionDetailsBinding: AdapterSubworkflowActionDetailsBinding) :
        RecyclerView.ViewHolder(adapterSubworkflowActionDetailsBinding.root)
}