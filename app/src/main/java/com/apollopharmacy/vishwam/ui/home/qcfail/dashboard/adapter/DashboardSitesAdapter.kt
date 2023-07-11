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
    var getStorePendingApprovedListDummys:
    Map<String, List<PendingCountResponse.Pendingcount>>,
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


        for (m in getStorePendingApprovedListDummys.entries) {
            holder.dashboardSitesBinding.storeid.setText(m.key)
            for ( j in m.value.indices){
                    holder.dashboardSitesBinding.empid.setText(m.value[j].empid)

                    if (m.value[j].ordertype.equals("REVERSE RETURN")) {
                        holder.dashboardSitesBinding.rtcount.setText(m.value[j].pendingcount.toString())

                    } else if (m.value[j].ordertype.equals("FORWARD RETURN")) {
                        holder.dashboardSitesBinding.frcount.setText(m.value[j].pendingcount.toString())

                    }

            }






        }



    }


    override fun getItemCount(): Int {
        return getStorePendingApprovedListDummys.size
    }

    class ViewHolder(val dashboardSitesBinding: DashboardSitesBinding) :
        RecyclerView.ViewHolder(dashboardSitesBinding.root)
}


