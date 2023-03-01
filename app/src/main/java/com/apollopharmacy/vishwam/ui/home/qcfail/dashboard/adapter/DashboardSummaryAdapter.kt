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

            holder.searchSiteBinding.logo.setImageResource(R.drawable.qc_manager)
            holder.searchSiteBinding.generalmanagerLayout.setBackgroundColor(Color.parseColor("#636fc1"))
            holder.searchSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#7e88c7"))
        } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
            holder.searchSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)

            holder.searchSiteBinding.logo.setImageResource(R.drawable.qc_executive)
            holder.searchSiteBinding.generalmanagerLayout.setBackgroundColor(Color.parseColor("#f4a841"))
            holder.searchSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#f6b968"))

        }
        holder.searchSiteBinding.rtocounts.setText(items.rtocount.toString())
        holder.searchSiteBinding.rtovalues.setText(DecimalFormat("#,###.00").format(items.rtoamount).toString())
        holder.searchSiteBinding.rrrtocounts.setText(items.rrtocount.toString())
        holder.searchSiteBinding.rrrtovalues.setText(DecimalFormat("#,###.00").format(items.rrtoamount).toString())








    }


    override fun getItemCount(): Int {
        return dashBoardList.size
    }

    class ViewHolder(val searchSiteBinding: DashboardSummaryBinding) :
        RecyclerView.ViewHolder(searchSiteBinding.root)



}



