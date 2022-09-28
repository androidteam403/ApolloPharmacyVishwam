package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPendingApprovedListBinding
import com.apollopharmacy.vishwam.databinding.AdapterPendingListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.SwachhListCallback
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved

class PendingListAdapter(
    val context: Context?,
    val pendingList: ArrayList<PendingAndApproved>,
    val mCallback: SwachhListCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_DATA = 0;
        private const val VIEW_TYPE_PROGRESS = 1;
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = pendingList?.get(position)
        //if data is load, returns PROGRESSBAR viewtype.
        return if (viewtype?.swachhid.isNullOrEmpty()) {
            VIEW_TYPE_PROGRESS
        } else VIEW_TYPE_DATA

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return when (viewType) {
           VIEW_TYPE_DATA -> {//inflates row layout
               val pendingListBinding: AdapterPendingListBinding =
                   DataBindingUtil.inflate(
                       LayoutInflater.from(context),
                       R.layout.adapter_pending_list,
                       parent,
                       false
                   )
                return DataViewHolder(pendingListBinding)
            }
            VIEW_TYPE_PROGRESS -> {//inflates progressbar layout
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.progressbar, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }

//        val pendingListBinding: AdapterPendingListBinding =
//            DataBindingUtil.inflate(
//                LayoutInflater.from(context),
//                R.layout.adapter_pending_list,
//                parent,
//                false
//            )
//        return ViewHolder(pendingListBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is DataViewHolder) {
            holder.bind(getItem(position))
        }


    }

    inner class DataViewHolder(val pendingListBinding: AdapterPendingListBinding) :
        RecyclerView.ViewHolder(pendingListBinding.root) {
        fun bind(items: PendingAndApproved) {
            bindItems(pendingListBinding, items, adapterPosition)
        }
    }
    fun getData(): ArrayList<PendingAndApproved> {
        return pendingList
    }

    fun getItem(position: Int): PendingAndApproved {
        return pendingList[position]
    }

    override fun getItemCount(): Int {
        return pendingList.size
    }
    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun bindItems(
        binding: AdapterPendingListBinding,
        items: PendingAndApproved,
        position: Int,

        ) {
        if(pendingList.get(position).swachhid!=null){


            var upperCaseUploadedBy: String = pendingList.get(position).uploadedBy!!

            pendingList.get(position).uploadedBy = upperCaseUploadedBy.toUpperCase()

//        pendingList.get(position).empName = empName
            binding.model = pendingList.get(position)
//
//        if( pendingList.get(position).uploadedDate!="") {
//            val strDate = pendingList.get(position).uploadedDate
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
//            val date = dateFormat.parse(strDate)
//            val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
//            pendingList.get(position).uploadedDate = dateNewFormat.toString()
//        }

            val isApprovedAdapter:Boolean=false
            binding.cardview
                .setOnClickListener {
                    mCallback.onClickUpdate(
                        pendingList.get(position),
                        isApprovedAdapter
                    )
                }

            binding.reviewButton
                .setOnClickListener {
                    mCallback.onClickReview(pendingList.get(position).swachhid,
                        pendingList.get(position).storeId)
                }


        }
    }

//    class ViewHolder(val pendingListBinding: AdapterPendingListBinding) :
//        RecyclerView.ViewHolder(pendingListBinding.root) {
//
//    }


}