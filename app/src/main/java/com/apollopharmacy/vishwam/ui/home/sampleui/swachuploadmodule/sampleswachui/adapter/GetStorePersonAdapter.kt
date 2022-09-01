package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelResponse
import java.text.SimpleDateFormat

class GetStorePersonAdapter(
    private var getStorePersonlist: List<GetStorePersonHistoryodelResponse.Get>?,
    private var callBackInterface: getStoreHistory,
    private var empname: String
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


        holder.swachId.text=getStorePerson?.swachhid
        getStorePersonlist?.get(position)?.uploadedBy?.toUpperCase()
//        getStorePersonlist?.get(position).empName= empName





        if (getStorePersonlist?.get(position)?.status.equals("APPROVED")) {
            holder.approvedLayout.visibility = View.VISIBLE
            holder.threeDots.visibility = View.VISIBLE
            if (getStorePerson?.approvedDate == "") {
                holder.onDetails.text = "--"
            } else {


                val strDate = getStorePerson?.approvedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.onDetails.text = dateNewFormat.toString()




                holder.onDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.uploadedBy
                holder.empName.text = " - " + empname
            }
        }
        else if (getStorePersonlist?.get(position)?.status.equals("PENDING")) {

            if (getStorePerson?.uploadedDate == "") {
                holder.onDetails.text = "--"
            } else {

                val strDate = getStorePerson?.uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.onDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.uploadedBy
                holder.empName.text = " - " +empname
            }
            holder.pendingpLayout.visibility = View.VISIBLE
        }
        else if (getStorePersonlist?.get(position)?.status.equals("RESHOOT")) {
            holder.reshootLayout.visibility = View.VISIBLE
            if (getStorePerson?.uploadedDate == "") {
                holder.onDetails.text = "--"
            } else {

                val strDate =  getStorePerson?.uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.onDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.uploadedBy
                holder.empName.text = " - " +empname
            }
        }
        else if (getStorePersonlist?.get(position)?.status.equals("NOT UPDATED")) {
            holder.notUpdatedLayout.visibility = View.VISIBLE
        }
        else if (getStorePersonlist?.get(position)?.status.equals("PARTIALLY APPROVED")) {
            holder.partiallyApproved.visibility = View.VISIBLE

            if (getStorePerson?.uploadedDate == "") {
                holder.onDetails.text = "--"
            } else {
                val strDate =  getStorePerson?.uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.onDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                holder.byDetails.text = "--"
            } else {
                holder.byDetails.text = getStorePerson?.uploadedBy
                holder.empName.text = " - " +empname
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
        val swachId: TextView = itemView.findViewById(R.id.swachidgetStore)
        val empName: TextView = itemView.findViewById(R.id.empNamegetStore)


    }
}