package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PreRetroPreviewLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.ApprovalReviewCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.PreRetroReviewingCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import java.util.stream.Collectors

class ApprovalCategoryListAdapter(
    val mContext: Context,
    var approveList: List<GetImageUrlResponse.Category>,
    var stage: String,
    val mclickistener: ApprovalReviewCallback,

    ) :

    RecyclerView.Adapter<ApprovalCategoryListAdapter.ViewHolder>() {

    var adapter: ApprovalImagesListAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val preRetroPreviewLayoutBinding: PreRetroPreviewLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.pre_retro_preview_layout,
                parent,
                false
            )
        return ViewHolder(preRetroPreviewLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)

        holder.preRetroPreviewLayoutBinding.sno.setText((position + 1).toString())
        holder.preRetroPreviewLayoutBinding.categoryName.setText(approvedOrders.categoryname)


        for (i in approvedOrders.groupByImageUrlList!!.indices) {
approvedOrders.groupByImageUrlList!!.get(0).get(0).seturl("https://pharmtest.blob.core.windows.net/cms//data/user/0/com.apollopharmacy.vishwam/cache/1682509724450.jpg")

        }

        adapter = ApprovalImagesListAdapter(mContext,stage,
            approvedOrders.groupByImageUrlList!!)
        holder.preRetroPreviewLayoutBinding.recyclerViewimages.adapter = adapter

//        holder.itemView.setOnClickListener {
//            mclickistener.onClickItemView(position,approvedOrders)
//
//        }


    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val preRetroPreviewLayoutBinding: PreRetroPreviewLayoutBinding) :
        RecyclerView.ViewHolder(preRetroPreviewLayoutBinding.root)

}