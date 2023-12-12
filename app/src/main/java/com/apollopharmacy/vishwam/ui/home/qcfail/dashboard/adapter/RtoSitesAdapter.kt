package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.RtoSitesBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.NumberFormat
import java.util.*

var pendingCountResponseListList = ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()


class RtoSitesAdapter(
    val mContext: Context,
    var pendingCountResponseList: ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,
    qcDashBoardCallback: QcDashBoardCallback,
    ) :
    RecyclerView.Adapter<RtoSitesAdapter.ViewHolder>(), Filterable {
    var charString: String? = ""
    var pendingCountResponseFilterList =
        ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()

init {
    pendingCountResponseListList=pendingCountResponseList
}
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




        if (items.siteid.toString().isNullOrEmpty()) {
            holder.dashboardSitesBinding.parentLayout.visibility = View.GONE
        } else {
            holder.dashboardSitesBinding.parentLayout.visibility = View.VISIBLE

        }

        if (items.siteid.isNullOrEmpty()) {
            holder.dashboardSitesBinding.siteIdS.setText("-")

        } else {
            holder.dashboardSitesBinding.siteIdS.setText(items.siteid)

        }

        if (items.rtocount.toString().isNullOrEmpty() || items.rtocount!!.equals("null")) {
            holder.dashboardSitesBinding.rtCount.setText("-")

        } else {
            holder.dashboardSitesBinding.rtCount.setText(items.rtocount.toString())

        }
        if (items.rtoamount.toString().isNullOrEmpty() || items.rtoamount!!.equals("null")) {
            holder.dashboardSitesBinding.rtValue.setText("-")

        } else {
            holder.dashboardSitesBinding.rtValue.setText(
                NumberFormat.getNumberInstance(Locale.US).format(items.rtoamount).toString()
            )


        }
        if (items.rrtocount.toString().isNullOrEmpty() || items.rrtocount!!.equals("null")) {
            holder.dashboardSitesBinding.rrtoCount.setText("-")

        } else {
            holder.dashboardSitesBinding.rrtoCount.setText(items.rrtocount.toString())

        }
        if (items.rrtoamount.toString().isNullOrEmpty() || items.rrtoamount!!.equals("null")) {
            holder.dashboardSitesBinding.rrtoValue.setText("-")

        } else {


            holder.dashboardSitesBinding.rrtoValue.setText(
                NumberFormat.getNumberInstance(Locale.US).format(items.rrtoamount).toString()
            )

        }


    }


    override fun getItemCount(): Int {
        return pendingCountResponseList.size
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults? {
                charString = charSequence.toString()
                if (charString!!.isEmpty()) {
                    pendingCountResponseList = pendingCountResponseListList
                } else {
                    pendingCountResponseFilterList.clear()
                    for (row in pendingCountResponseListList) {
                        if (!pendingCountResponseFilterList.contains(row) && row.siteid!!.toLowerCase()
                                .contains(charString!!.toLowerCase())
                        ) {
                            pendingCountResponseFilterList.add(row)
                        }
                    }
                    pendingCountResponseList = pendingCountResponseFilterList
                }
                val filterResults = FilterResults()
                filterResults.values = pendingCountResponseList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            protected override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults,
            ) {
                if (pendingCountResponseList != null && !pendingCountResponseList.isEmpty()) {
                    pendingCountResponseList =
                        filterResults.values as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                    try {
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                    }
                } else {
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ViewHolder(val dashboardSitesBinding: RtoSitesBinding) :
        RecyclerView.ViewHolder(dashboardSitesBinding.root)
}


