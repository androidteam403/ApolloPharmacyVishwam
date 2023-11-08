package com.apollopharmacy.vishwam.ui.home.dashboard.managerdashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterDashboardCeoBinding
import com.apollopharmacy.vishwam.databinding.AdapterManagerDashboardBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.adapter.DashboardAdapter

class ManagerAdapter: RecyclerView.Adapter<ManagerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterManagerDashboardBinding =
            DataBindingUtil.inflate<AdapterManagerDashboardBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_manager_dashboard,
                parent,
                false)
        return ViewHolder(adapterManagerDashboardBinding)
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class ViewHolder(var adapterManagerDashboardBinding: AdapterManagerDashboardBinding) :
        RecyclerView.ViewHolder(adapterManagerDashboardBinding.root) {}
}