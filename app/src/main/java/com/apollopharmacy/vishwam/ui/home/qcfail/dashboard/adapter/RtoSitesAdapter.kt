package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.accounts.AccountManagerCallback
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
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
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

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






      if (items.siteid.toString().isNullOrEmpty()){
          holder.dashboardSitesBinding.parentLayout.visibility=View.GONE
      }else{
          holder.dashboardSitesBinding.parentLayout.visibility=View.VISIBLE

      }

        if (items.siteid.isNullOrEmpty()){
            holder.dashboardSitesBinding.siteIdS.setText("-")

        }else{
            holder.dashboardSitesBinding.siteIdS.setText(items.siteid)

        }

        if (items.rtocount.toString().isNullOrEmpty()|| items.rtocount!!.equals("null")){
            holder.dashboardSitesBinding.rtocounts.setText("-")

        }else{
            holder.dashboardSitesBinding.rtocounts.setText(items.rtocount.toString())

        }
        if (items.rtoamount.toString().isNullOrEmpty()|| items.rtoamount!!.equals("null")){
            holder.dashboardSitesBinding.rtovalues.setText("-")

        }else{
            holder.dashboardSitesBinding.rtovalues.setText( NumberFormat.getNumberInstance(Locale.US).format(items.rtoamount).toString())


        }
        if (items.rrtocount.toString().isNullOrEmpty()|| items.rrtocount!!.equals("null")){
            holder.dashboardSitesBinding.rrtocounts.setText("-")

        }else{
            holder.dashboardSitesBinding.rrtocounts.setText(items.rrtocount.toString())

        }
        if (items.rrtoamount.toString().isNullOrEmpty()|| items.rrtoamount!!.equals("null")){
            holder.dashboardSitesBinding.rrtovalues.setText("-")

        }else{


            holder.dashboardSitesBinding.rrtovalues.setText( NumberFormat.getNumberInstance(Locale.US).format(items.rrtoamount).toString())

        }


    }


    override fun getItemCount(): Int {
        return pendingCountResponseList.size
    }

    class ViewHolder(val dashboardSitesBinding: RtoSitesBinding) :
        RecyclerView.ViewHolder(dashboardSitesBinding.root)
}


