package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DashboardSitesBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse

class DashboardSitesAdapter(
    val mContext: Context,
    var pendingCountResponseList: ArrayList<PendingCountResponse.Pendingcount>,
) :
    RecyclerView.Adapter<DashboardSitesAdapter.ViewHolder>() {

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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = pendingCountResponseList.get(position)
        if (items.ordertype.equals("REVERSE RETURN")){
            holder.dashboardSitesBinding.rtcount.setText(items.pendingcount.toString())
            holder.dashboardSitesBinding.frcount.setText("-")

        }
        else  if (items.ordertype.equals("FORWARD RETURN")){
            holder.dashboardSitesBinding.frcount.setText(items.pendingcount.toString())
            holder.dashboardSitesBinding.rtcount.setText("-")

        }

            holder.dashboardSitesBinding.storeid.setText(items.siteid)
        holder.dashboardSitesBinding.empid.setText(items.empid)
    }


    override fun getItemCount(): Int {
        return pendingCountResponseList.size
    }

    class ViewHolder(val dashboardSitesBinding: DashboardSitesBinding) :
        RecyclerView.ViewHolder(dashboardSitesBinding.root)
}


