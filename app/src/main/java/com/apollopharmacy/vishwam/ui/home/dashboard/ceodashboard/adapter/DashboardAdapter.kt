package com.apollopharmacy.vishwam.ui.home.dashboard.adapter

import android.view.LayoutInflater
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
        holder.adapterDashboardCeoBinding.pending.setText(response.pending.toString())
        holder.adapterDashboardCeoBinding.totalQc.setText(response.total.toString())
        holder.adapterDashboardCeoBinding.threeToEight.setText(response.get3To8().toString())

        holder.adapterDashboardCeoBinding.name.setOnClickListener {
            if (!response.employeeid.isNullOrEmpty()){
                ceoDashboardCallback.onClickEmployee(response.employeeid)

            }
        }

        holder.adapterDashboardCeoBinding.rightArrowQc.setOnClickListener {
            ceoDashboardCallback.onClickRightArrow(response)
        }
    }

    override fun getItemCount(): Int {
        return ticketCountsByStatsuRoleResponse.size
    }

    class ViewHolder(var adapterDashboardCeoBinding: AdapterDashboardCeoBinding) :
        RecyclerView.ViewHolder(adapterDashboardCeoBinding.root) {}
}