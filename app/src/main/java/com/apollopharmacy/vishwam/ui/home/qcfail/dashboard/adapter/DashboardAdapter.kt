package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.DashboardSitesBinding
import com.apollopharmacy.vishwam.databinding.QcDashboardSitesBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse

class DashboardAdapter(
    val mContext: Context,
    var pendingCountResponseList: ArrayList<PendingCountResponse.Pendingcount>,
) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val dashboardSitesBinding: DashboardSitesBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.dashboard_sites,
                parent,
                false
            )
        return ViewHolder(dashboardSitesBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = pendingCountResponseList.get(position)
        if (Preferences.getAppLevelDesignationQCFail().replace(" ", "").equals("EXECUTIVE", true)) {
            holder.dashboardSitesBinding.sitedid.setText(items.storeid)
            holder.dashboardSitesBinding.manager.setText("-")
            holder.dashboardSitesBinding.generalmanager.setText("-")
            holder.dashboardSitesBinding.executive.setText(items.executivecount.toString())
        }
        else if (Preferences.getAppLevelDesignationQCFail().replace(" ", "").equals("MANAGER", true)) {
            holder.dashboardSitesBinding.sitedid.setText(items.storeid)
            holder.dashboardSitesBinding.manager.setText(items.managercount.toString())
            holder.dashboardSitesBinding.generalmanager.setText("-")
            holder.dashboardSitesBinding.executive.setText("-")
        }
        else if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                .equals("GENERALMANAGER", true)
        ) {
            holder.dashboardSitesBinding.sitedid.setText(items.storeid)
            holder.dashboardSitesBinding.manager.setText("-")
            holder.dashboardSitesBinding.generalmanager.setText(items.generalmangercount.toString())
            holder.dashboardSitesBinding.executive.setText("-")
        }


    }


    override fun getItemCount(): Int {
        return pendingCountResponseList.size
    }

    class ViewHolder(val dashboardSitesBinding: DashboardSitesBinding) :
        RecyclerView.ViewHolder(dashboardSitesBinding.root)
}


