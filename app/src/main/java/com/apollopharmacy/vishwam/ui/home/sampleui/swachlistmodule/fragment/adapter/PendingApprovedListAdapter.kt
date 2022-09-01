package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPendingApprovedListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.SwachhListCallback
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved

class PendingApprovedListAdapter(
    val mContext: Context?,
    val pendingApprovedList: ArrayList<PendingAndApproved>,
    val mCallback: SwachhListCallback,
    val empName: String,
) :
    RecyclerView.Adapter<PendingApprovedListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val pendingApprovedListBinding: AdapterPendingApprovedListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.adapter_pending_approved_list,
                parent,
                false
            )
        return ViewHolder(pendingApprovedListBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            pendingApprovedList.get(position).uploadedBy?.toUpperCase()
            pendingApprovedList.get(position).empName= empName
            holder.pendingApprovedListBinding.model = pendingApprovedList.get(position)

//        if( pendingApprovedList.get(position).uploadedDate!="") {
//            val strDate = pendingApprovedList.get(position).uploadedDate
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
//            val date = dateFormat.parse(strDate)
//            val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
//            pendingApprovedList.get(position).uploadedDate = dateNewFormat.toString()
//        }

//        if( pendingApprovedList.get(position).reshootDate!=""){
//            val strDate2 = pendingApprovedList.get(position).reshootDate
//            val dateFormat2 = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
//            val date2 = dateFormat2.parse(strDate2)
//            val dateNewFormat2 = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date2)
//            pendingApprovedList.get(position).reshootDate = dateNewFormat2.toString()
//
//        }
//        if( pendingApprovedList.get(position).approvedDate!="") {
//            val strDate3 = pendingApprovedList.get(position).approvedDate
//            val dateFormat3 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//             val date3 = dateFormat3.parse(strDate3)
//            val dateNewFormat3 = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date3)
//            pendingApprovedList.get(position).approvedDate = dateNewFormat3.toString()
//
//        }
//
//        if(pendingApprovedList.get(position).partiallyApprovedDate!=""){
//            val strDate4 = pendingApprovedList.get(position).partiallyApprovedDate
//            val dateFormat4 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            val date4 = dateFormat4.parse(strDate4)
//            val dateNewFormat4 = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date4)
//            pendingApprovedList.get(position).partiallyApprovedDate = dateNewFormat4.toString()
//        }



        holder.pendingApprovedListBinding.cardview
            .setOnClickListener {
                mCallback.onClickUpdate(
                    pendingApprovedList.get(position)
                )
            }

    }


    override fun getItemCount(): Int {
        return pendingApprovedList.size
    }

    class ViewHolder(val pendingApprovedListBinding: AdapterPendingApprovedListBinding) :
        RecyclerView.ViewHolder(pendingApprovedListBinding.root)
}