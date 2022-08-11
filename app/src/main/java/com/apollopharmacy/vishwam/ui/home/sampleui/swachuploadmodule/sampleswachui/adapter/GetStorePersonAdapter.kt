package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelResponse

class GetStorePersonAdapter(
    private var getStorePersonlist: List<GetStorePersonHistoryodelResponse.Get>?,
    private var callBackInterface: getStoreHistory
) :
    RecyclerView.Adapter<GetStorePersonAdapter.ViewHolder>() {


    interface getStoreHistory {
        fun onClickStatus(
            position: Int,
            swachhid: String?,
            status: String?,
            approvedDate: String?,
            storeId: String?,
            uploadedDate: String?,
            reshootDate: String?,
            partiallyApprovedDate: String?
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_getstorepersonhistory, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val getStorePerson = getStorePersonlist?.get(position)






        if (getStorePersonlist?.get(position)?.status.equals("APPROVED")) {
            holder.approvedLayout.visibility = View.VISIBLE
            holder.threeDots.visibility = View.VISIBLE
            if (getStorePerson?.approvedDate == "") {
                holder.onDetails.text = "--"
            } else {
                holder.onDetails.text = getStorePerson?.approvedDate
            }

            if (getStorePerson?.approvedBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.approvedBy
            }
        }
        else if (getStorePersonlist?.get(position)?.status.equals("PENDING")) {

            if (getStorePerson?.uploadedDate == "") {
                holder.onDetails.text = "--"
            } else {
                holder.onDetails.text = getStorePerson?.uploadedDate
            }

            if (getStorePerson?.uploadedBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.uploadedBy
            }
            holder.pendingpLayout.visibility = View.VISIBLE
        }
        else if (getStorePersonlist?.get(position)?.status.equals("RE-SHOOT")) {
            holder.reshootLayout.visibility = View.VISIBLE
            if (getStorePerson?.reshootDate == "") {
                holder.onDetails.text = "--"
            } else {
                holder.onDetails.text = getStorePerson?.reshootDate
            }

            if (getStorePerson?.reshootBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.reshootBy
            }
        }
        else if (getStorePersonlist?.get(position)?.status.equals("NOT UPDATED")) {
            holder.notUpdatedLayout.visibility = View.VISIBLE
        }
        else if (getStorePersonlist?.get(position)?.status.equals("PARTIALLY APPROVED")) {
            holder.partiallyApproved.visibility = View.VISIBLE

            if (getStorePerson?.partiallyApprovedDate == "") {
                holder.onDetails.text = "--"
            } else {
                holder.onDetails.text = getStorePerson?.partiallyApprovedDate
            }

            if (getStorePerson?.partiallyApprovedBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.partiallyApprovedBy
            }
        }

        holder.overAllLayout.setOnClickListener {
            callBackInterface.onClickStatus(
                position,
                getStorePerson?.swachhid,
                getStorePerson?.status,
                getStorePerson?.approvedDate,
                getStorePerson?.storeId,
                getStorePerson?.uploadedDate,
                getStorePerson?.reshootDate,
                getStorePerson?.partiallyApprovedDate,
            )
        }


    }

    override fun getItemCount(): Int {
        return getStorePersonlist?.size!!
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val onDetails: TextView = itemView.findViewById(R.id.date_details)
        val byDetails: TextView = itemView.findViewById(R.id.by_details)
        val approvedLayout: LinearLayout = itemView.findViewById(R.id.green_approved_layot)
        val reshootLayout: LinearLayout = itemView.findViewById(R.id.re_shoot_layout)
        val pendingpLayout: LinearLayout = itemView.findViewById(R.id.pending_layout)
        val notUpdatedLayout: LinearLayout = itemView.findViewById(R.id.not_updated_layout)
        val overAllLayout: LinearLayout = itemView.findViewById(R.id.overallLayout)
        val partiallyApproved: LinearLayout = itemView.findViewById(R.id.partially_approved_layout)
        val threeDots: LinearLayout = itemView.findViewById(R.id.three_dots)


    }
}