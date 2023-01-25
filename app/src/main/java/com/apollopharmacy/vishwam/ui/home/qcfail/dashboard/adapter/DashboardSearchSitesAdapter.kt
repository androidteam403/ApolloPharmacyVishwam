package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.SearchSiteBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import java.text.DecimalFormat

class DashboardSearchSitesAdapter(
    val mContext: Context,
    var dashBoardList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,

    var pendingCountResponseList: ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,
) :
    RecyclerView.Adapter<DashboardSearchSitesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val searchSiteBinding: SearchSiteBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.search_site,
                parent,
                false
            )
        return ViewHolder(searchSiteBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = pendingCountResponseList.get(position)





        if (items.designation.equals("executive", true)) {
            holder.searchSiteBinding.executiveempId.setText(items.empid.toString())

        } else if (items.designation.equals("manager", true)) {
            holder.searchSiteBinding.managerempId.setText(items.empid.toString())

        } else if (items.designation?.replace(" ", "")
                .equals("generalmanager", true)
        ) {
            holder.searchSiteBinding.gmanagerempId.setText(items.empid.toString())

        }

        if(items.rtocount==null){
            holder.searchSiteBinding.rtocount.setText("-")

        }else{
            holder.searchSiteBinding.rtocount.setText(items.rtocount.toString())

        }
        if(items.rrtocount==null){
            holder.searchSiteBinding.rrtocount.setText("-")

        }else{
            holder.searchSiteBinding.rrtocount.setText(items.rrtocount.toString())

        }
        if(items.rtoamount==null){
            holder.searchSiteBinding.rtovalue.setText("-")

        }else{
            holder.searchSiteBinding.rtovalue.setText(DecimalFormat("#,###.00").format(items.rtoamount).toString())

        }
        if(items.rrtoamount==null){
            holder.searchSiteBinding.rrtovalue.setText("-")

        }else{
            holder.searchSiteBinding.rrtovalue.setText(DecimalFormat("#,###.00").format(items.rrtoamount).toString())

        }


    }


    override fun getItemCount(): Int {
        return pendingCountResponseList.size
    }

    class ViewHolder(val searchSiteBinding: SearchSiteBinding) :
        RecyclerView.ViewHolder(searchSiteBinding.root)



}



