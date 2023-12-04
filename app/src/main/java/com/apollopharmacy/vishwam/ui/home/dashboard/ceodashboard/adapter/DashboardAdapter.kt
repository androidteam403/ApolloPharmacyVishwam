package com.apollopharmacy.vishwam.ui.home.dashboard.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterDashboardCeoBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard.CeoDashboardCallback
import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse

class DashboardAdapter(
    var ceoDashboardCallback: CeoDashboardCallback,
    var ticketCountsByStatsuRoleResponse: List<TicketCountsByStatusRoleResponse.Data.ListData.Row>,
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterDashboardCeoBinding =
            DataBindingUtil.inflate<AdapterDashboardCeoBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_dashboard_ceo,
                parent,
                false
            )
        return ViewHolder(adapterDashboardCeoBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var response = ticketCountsByStatsuRoleResponse.get(position)
        holder.adapterDashboardCeoBinding.name.setText(response.name)
        holder.adapterDashboardCeoBinding.lessThanTwo.setText(response.lessThan2.toString())
        holder.adapterDashboardCeoBinding.greaterThanEight.setText(response.greaterThan8.toString())
        holder.adapterDashboardCeoBinding.closed.setText(response.closed.toString())
        holder.adapterDashboardCeoBinding.rejected.setText(response.rejected.toString())
        if (response.employeeid.isNullOrEmpty()) {
            holder.adapterDashboardCeoBinding.rightArrowQc.visibility = View.GONE
        } else {
            holder.adapterDashboardCeoBinding.rightArrowQc.visibility = View.VISIBLE
        }

        var pending = "${response.lessThan2 + response.get3To8() + response.greaterThan8}"
        holder.adapterDashboardCeoBinding.pending.setText(pending)
//        holder.adapterDashboardCeoBinding.pending.setText(response.pending.toString())

        val total = "${response.closed + response.rejected + pending.toInt()}"
        holder.adapterDashboardCeoBinding.totalQc.setText(total)
//        holder.adapterDashboardCeoBinding.totalQc.setText(response.total.toString())
        holder.adapterDashboardCeoBinding.threeToEight.setText(response.get3To8().toString())
        if (!response.storeId.isNullOrEmpty()) {
            holder.adapterDashboardCeoBinding.name.setTextColor(Color.parseColor("#000000"))
        } else {
            holder.adapterDashboardCeoBinding.name.setTextColor(Color.parseColor("#005EFF"))

        }

        holder.adapterDashboardCeoBinding.name.setOnClickListener {
            if (!response.employeeid.isNullOrEmpty()) {
                if (!response.roleCode.isNullOrEmpty()) {
                    if (response.storeId.isNullOrEmpty()) {
                        ceoDashboardCallback.onClickEmployee(response.employeeid, response.roleCode)
                    }
                }
            }
        }

        holder.adapterDashboardCeoBinding.rightArrowQc.setOnClickListener {
//            if (!response.roleCode.isNullOrEmpty()) {
            ceoDashboardCallback.onClickRightArrow(response)
//            }
        }
        holder.adapterDashboardCeoBinding.totalArrowLayout.setOnClickListener {
//            if (!response.roleCode.isNullOrEmpty()) {
            ceoDashboardCallback.onClickRightArrow(response)
//            }
        }

        //callbacks for the row item click to show tickets list
        holder.adapterDashboardCeoBinding.closedLayout.setOnClickListener {
            ceoDashboardCallback.onCLickRowtoShowTickets(response)
        }
        holder.adapterDashboardCeoBinding.lessthanTwoLayout.setOnClickListener {
            ceoDashboardCallback.onCLickRowtoShowTickets(response)
        }
        holder.adapterDashboardCeoBinding.threetoEightLayout.setOnClickListener {
            ceoDashboardCallback.onCLickRowtoShowTickets(response)
        }
        holder.adapterDashboardCeoBinding.greaterthanEightLayout.setOnClickListener {
            ceoDashboardCallback.onCLickRowtoShowTickets(response)
        }
        holder.adapterDashboardCeoBinding.rejectedLayout.setOnClickListener {
            ceoDashboardCallback.onCLickRowtoShowTickets(response)
        }
        holder.adapterDashboardCeoBinding.pendingLayout.setOnClickListener {
            ceoDashboardCallback.onCLickRowtoShowTickets(response)
        }
        holder.adapterDashboardCeoBinding.totalLayout.setOnClickListener {
            ceoDashboardCallback.onCLickRowtoShowTickets(response)
        }
    }

    override fun getItemCount(): Int {
        return ticketCountsByStatsuRoleResponse.size
    }

    class ViewHolder(var adapterDashboardCeoBinding: AdapterDashboardCeoBinding) :
        RecyclerView.ViewHolder(adapterDashboardCeoBinding.root) {}
}