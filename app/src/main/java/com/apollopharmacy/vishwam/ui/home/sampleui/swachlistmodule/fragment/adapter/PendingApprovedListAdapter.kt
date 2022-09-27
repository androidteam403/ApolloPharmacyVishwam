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
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelResponse

class PendingApprovedListAdapter(
    val mContext: Context?,
    val pendingApprovedList: ArrayList<PendingAndApproved>,
    val mCallback: SwachhListCallback,
) :
    RecyclerView.Adapter<PendingApprovedListAdapter.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1;
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = pendingApprovedList?.get(position)
        //if data is load, returns PROGRESSBAR viewtype.
        return if (viewtype?.swachhid.isNullOrEmpty()) {
            VIEW_TYPE_PROGRESS
        } else VIEW_TYPE_DATA

    }

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

        var upperCaseUploadedBy: String = pendingApprovedList.get(position).uploadedBy!!

        pendingApprovedList.get(position).uploadedBy = upperCaseUploadedBy.toUpperCase()
//        pendingApprovedList.get(position).empName = empName
        holder.pendingApprovedListBinding.model = pendingApprovedList.get(position)

//        if(pendingApprovedList.get(position).status.equals("APPROVED")){
//
//        }

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

        val isApprovedAdapter:Boolean=true
        holder.pendingApprovedListBinding.cardview
            .setOnClickListener {
                mCallback.onClickUpdate(
                    pendingApprovedList.get(position), isApprovedAdapter
                )
            }

        holder.pendingApprovedListBinding.reviewButton
            .setOnClickListener {
                mCallback.onClickReview( pendingApprovedList.get(position).swachhid, pendingApprovedList.get(position).storeId)
            }

    }
    fun getData(): ArrayList<PendingAndApproved> {
        return pendingApprovedList
    }

    fun getItem(position: Int): PendingAndApproved {
        return pendingApprovedList!![position]
    }

    override fun getItemCount(): Int {
        return pendingApprovedList.size
    }

    class ViewHolder(val pendingApprovedListBinding: AdapterPendingApprovedListBinding) :
        RecyclerView.ViewHolder(pendingApprovedListBinding.root)
}