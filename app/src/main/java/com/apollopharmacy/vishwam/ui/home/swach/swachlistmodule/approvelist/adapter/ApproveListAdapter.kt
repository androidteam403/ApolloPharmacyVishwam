package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterApproveListBinding
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.ApproveListcallback
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.GetImageUrlsResponse

class ApproveListAdapter(
    val mContext: Context,
    val categoryList: List<GetImageUrlsResponse.Category>,
    val approveListcallback: ApproveListcallback
) :
    RecyclerView.Adapter<ApproveListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterApproveListBinding: AdapterApproveListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.adapter_approve_list,
                parent,
                false
            )
        return ViewHolder(adapterApproveListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList.get(position)
        holder.adapterApproveListBinding.model = category
        var isAccepted = true
        var isReShoot = true
        var isPending = true
        var approvedCount = 0
        for (i in category.imageUrls!!) {
            if (i.status.equals("0")) {
                isAccepted = false
                isReShoot = false
            } else if (i.status.equals("1")) {
                isReShoot = false
                isPending = false
                approvedCount++
            } else if (i.status.equals("2")) {
                isAccepted = false
                isPending = false
            }
        }
        holder.adapterApproveListBinding.approvedAndTotalCount =
            "$approvedCount/${category.imageUrls!!.size}"
        if (isPending) {
            holder.adapterApproveListBinding.status = "0"
            approveListcallback.onePendingStatus()
        } else if (isAccepted) {
            holder.adapterApproveListBinding.status = "1"
        } else if (isReShoot) {
            holder.adapterApproveListBinding.status = "2"
        } else {
            holder.adapterApproveListBinding.status = "3"
        }



        if ((category.imageUrls != null && category.imageUrls!!.size > 0) == true) {
            val imageUrlAdapter =
                ImageUrlAdapter(mContext, category.imageUrls!!, approveListcallback, categoryList,position,isAccepted)
            holder.adapterApproveListBinding.imageUrlsRecycler.layoutManager =
                LinearLayoutManager(
                    mContext, LinearLayoutManager.HORIZONTAL, false
                )
            holder.adapterApproveListBinding.imageUrlsRecycler.adapter =
                imageUrlAdapter
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(val adapterApproveListBinding: AdapterApproveListBinding) :
        RecyclerView.ViewHolder(adapterApproveListBinding.root)
}