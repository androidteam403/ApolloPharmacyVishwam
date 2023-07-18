package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterDashboardCeoBinding
import com.apollopharmacy.vishwam.databinding.AdapterDetailsDashboardBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.adapter.DashboardAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.model.ReasonWiseTicketCountByRoleResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.adapter.SiteIdDisplayAdapter
import java.util.ArrayList

class DashboardDetailsAdapter(
    private var context: Context,
    var reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountByRoleResponse,
) : RecyclerView.Adapter<DashboardDetailsAdapter.ViewHolder>() {
    var horizantalCategoryAdapter: HorizantalCategoryAdapter? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DashboardDetailsAdapter.ViewHolder {
        val adapterDashboardDetailsAdapterBinding =
            DataBindingUtil.inflate<AdapterDetailsDashboardBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_details_dashboard,
                parent,
                false
            )
        return DashboardDetailsAdapter.ViewHolder(adapterDashboardDetailsAdapterBinding)
    }

    override fun getItemCount(): Int {
        return reasonWiseTicketCountByRoleResponse.data.listData.rows.size
    }

    override fun onBindViewHolder(holder: DashboardDetailsAdapter.ViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.adapterDashboardDetailsAdapterBinding.overallLayout.background =
                (context.getDrawable(R.drawable.ash_background_qc))
        } else {

            holder.adapterDashboardDetailsAdapterBinding.overallLayout.background =
                (context.getDrawable(R.drawable.background_for_white))
        }

        holder.adapterDashboardDetailsAdapterBinding.store.text =
            reasonWiseTicketCountByRoleResponse.data.listData.rows[position].name
        holder.adapterDashboardDetailsAdapterBinding.total.text =
            reasonWiseTicketCountByRoleResponse.data.listData.rows[position].total.toString()

        horizantalCategoryAdapter =
            HorizantalCategoryAdapter(context,
                reasonWiseTicketCountByRoleResponse.data.listData.rows as ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.Row>,
                reasonWiseTicketCountByRoleResponse.data.listData.zcExtra.data1 as ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.ZcExtra.Data1>)
        holder.adapterDashboardDetailsAdapterBinding.childRecyclerView.layoutManager =
            LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
        holder.adapterDashboardDetailsAdapterBinding.childRecyclerView.adapter =
            horizantalCategoryAdapter

//        holder.adapterDashboardDetailsAdapterBinding.childRecyclerView.adapter =
//            horizantalCategoryAdapter
    }

    class ViewHolder(var adapterDashboardDetailsAdapterBinding: AdapterDetailsDashboardBinding) :
        RecyclerView.ViewHolder(adapterDashboardDetailsAdapterBinding.root) {}
}