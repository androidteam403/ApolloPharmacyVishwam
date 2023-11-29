package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DashboardSitesBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse

class DashboardSitesAdapter(
    val mContext: Context,
    var pendingCountResponseList: ArrayList<PendingCountResponse.Pendingcount>,
    var distintPendingCountResponseList: ArrayList<PendingCountResponse.Pendingcount>,

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

        val items = distintPendingCountResponseList.get(position)
        holder.dashboardSitesBinding.storeid.setText(items.siteid!!.split("-").get(0))
        holder.dashboardSitesBinding.storeName.setText(items.siteid!!.split("-").get(1))
        if (items.designation.equals("EXECUTIVE")){

            holder.dashboardSitesBinding.rtcount.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.pendency_orange))
            holder.dashboardSitesBinding.frcount.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.pendency_orange))


        }
        if (items.designation.equals("MANAGER")){
            holder.dashboardSitesBinding.rtcount.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.pendency_voilet))
            holder.dashboardSitesBinding.frcount.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.pendency_voilet))

        }

        if (items.designation!!.replace(" ","").equals("GENERALMANAGER")){
            holder.dashboardSitesBinding.rtcount.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.pendency_green))
            holder.dashboardSitesBinding.frcount.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.pendency_green))

        }
//        holder.dashboardSitesBinding.empid.setText(items.empid)

//        holder.dashboardSitesBinding.empid.setText(items.empid!!.split("-").get(0))
//        holder.dashboardSitesBinding.empName.setText(items.empid!!.split("-").get(1))

        for (m in pendingCountResponseList.indices) {
            if (items.siteid.equals(pendingCountResponseList.get(m).siteid)) {

                if (pendingCountResponseList.get(m).ordertype.equals("REVERSE RETURN")) {
                    holder.dashboardSitesBinding.rtcount.setText(pendingCountResponseList[m].pendingcount.toString())

                } else if (pendingCountResponseList[m].ordertype.equals("FORWARD RETURN")) {
                    holder.dashboardSitesBinding.frcount.setText(pendingCountResponseList[m].pendingcount.toString())

                }
            }

            }


        }


        override fun getItemCount(): Int {
            return distintPendingCountResponseList.size
        }

        class ViewHolder(val dashboardSitesBinding: DashboardSitesBinding) :
            RecyclerView.ViewHolder(dashboardSitesBinding.root)
    }


