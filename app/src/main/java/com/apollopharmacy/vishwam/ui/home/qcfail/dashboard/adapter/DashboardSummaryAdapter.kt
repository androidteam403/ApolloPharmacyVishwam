package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DashboardSummaryBinding
import com.apollopharmacy.vishwam.databinding.SearchSiteBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import java.text.DecimalFormat

class DashboardSummaryAdapter(
    val mContext: Context,
    var dashBoardList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,


) :
    RecyclerView.Adapter<DashboardSummaryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val searchSiteBinding: DashboardSummaryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.dashboard_summary,
                parent,
                false
            )
        return ViewHolder(searchSiteBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = dashBoardList.get(position)

        if (items.designation?.replace(" ", "").equals("GENERALMANAGER", true)) {
            holder.searchSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)


        } else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
            holder.searchSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)

            holder.searchSiteBinding.generalmanagerLayout.setBackgroundColor(
                Color.parseColor(
                    "#606db3"
                )
            )
            holder.searchSiteBinding.rtoLayout.setBackgroundColor(
                Color.parseColor(
                    "#606db3"
                )
            )
        } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
            holder.searchSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)

            holder.searchSiteBinding.generalmanagerLayout.setBackgroundColor(
                Color.parseColor(
                    "#d48a2b"
                )
            )
            holder.searchSiteBinding.rtoLayout.setBackgroundColor(
                Color.parseColor(
                    "#d48a2b"
                )
            )
        }
        holder.searchSiteBinding.rtCount.setText(items.rtocount.toString())
        holder.searchSiteBinding.rtovalue.setText(DecimalFormat("#,###.00").format(items.rtoamount).toString())
        holder.searchSiteBinding.rrtoCount.setText(items.rrtocount.toString())
        holder.searchSiteBinding.rrtovalue.setText(DecimalFormat("#,###.00").format(items.rrtoamount).toString())








    }


    override fun getItemCount(): Int {
        return dashBoardList.size
    }

    class ViewHolder(val searchSiteBinding: DashboardSummaryBinding) :
        RecyclerView.ViewHolder(searchSiteBinding.root)



}



