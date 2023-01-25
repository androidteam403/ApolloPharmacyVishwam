package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
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


        holder.searchSiteBinding.rtocounts.setText(items.rtocount.toString())
        holder.searchSiteBinding.rtovalues.setText(DecimalFormat("#,###.00").format(items.rtoamount).toString())
        holder.searchSiteBinding.rrrtocounts.setText(items.rrtocount.toString())
        holder.searchSiteBinding.rrrtovalues.setText(DecimalFormat("#,###.00").format(items.rtoamount).toString())








    }


    override fun getItemCount(): Int {
        return dashBoardList.size
    }

    class ViewHolder(val searchSiteBinding: DashboardSummaryBinding) :
        RecyclerView.ViewHolder(searchSiteBinding.root)



}



