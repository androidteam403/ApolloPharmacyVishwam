package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.accounts.AccountManagerCallback
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PendencySitesBinding
import com.apollopharmacy.vishwam.databinding.RtoPendencySitesBinding
import com.apollopharmacy.vishwam.databinding.RtoSitesBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.DecimalFormat

class RtoSitesAdapter(
    val mContext: Context,
    qcDashBoardCallback: QcDashBoardCallback,
    var pendingCountResponseList: ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,
) :
    RecyclerView.Adapter<RtoSitesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val dashboardSitesBinding: RtoSitesBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.rto_sites,
                parent,
                false
            )
        return ViewHolder(dashboardSitesBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = pendingCountResponseList.get(position)

        if (items.siteid.isNullOrEmpty()){
            holder.dashboardSitesBinding.siteIdS.setText("-")

        }else{
            holder.dashboardSitesBinding.siteIdS.setText(items.siteid)

        }
        holder.dashboardSitesBinding.rtocounts.setText(items.rtocount.toString())
        holder.dashboardSitesBinding.rtovalues.setText(DecimalFormat("#,###.00").format(items.rtoamount).toString())
        holder.dashboardSitesBinding.rrtocounts.setText(items.rrtocount.toString())
        holder.dashboardSitesBinding.rrtovalues.setText(DecimalFormat("#,###.00").format(items.rrtoamount).toString())

    }


    override fun getItemCount(): Int {
        return pendingCountResponseList.size
    }

    class ViewHolder(val dashboardSitesBinding: RtoSitesBinding) :
        RecyclerView.ViewHolder(dashboardSitesBinding.root)
}


