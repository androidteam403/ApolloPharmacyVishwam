package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterCategoryDashboardBinding
import com.apollopharmacy.vishwam.databinding.AdapterDetailsDashboardBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.model.ReasonWiseTicketCountByRoleResponse
import java.util.ArrayList

class DashboardCategoryAdapter(var categoryList: ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.ZcExtra.Data1>) :
    RecyclerView.Adapter<DashboardCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DashboardCategoryAdapter.ViewHolder {
        val adapterCategoryDashboardBinding =
            DataBindingUtil.inflate<AdapterCategoryDashboardBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_category_dashboard,
                parent,
                false)
        return ViewHolder(adapterCategoryDashboardBinding)
    }

    override fun onBindViewHolder(holder: DashboardCategoryAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 2
    }

    class ViewHolder(var adapterCategoryDashboardBinding: AdapterCategoryDashboardBinding) :
        RecyclerView.ViewHolder(adapterCategoryDashboardBinding.root) {}
}