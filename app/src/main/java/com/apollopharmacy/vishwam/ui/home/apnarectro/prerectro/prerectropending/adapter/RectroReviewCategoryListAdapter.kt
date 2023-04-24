package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerectropending.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PreRetroReviewLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerectropending.PreRetroReviewCallback

class RectroReviewCategoryListAdapter(
    val mContext: Context,
    var approveList: ArrayList<String>,
    val listner: PreRetroReviewCallback,

    ) :

    RecyclerView.Adapter<RectroReviewCategoryListAdapter.ViewHolder>() {

    var adapter: RectroImagesListAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val preRetroPreviewLayoutBinding: PreRetroReviewLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.pre_retro_review_layout,
                parent,
                false
            )
        return ViewHolder(preRetroPreviewLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)
        holder.preRetroPreviewLayoutBinding.sno.setText((position+1).toString())
        holder.preRetroPreviewLayoutBinding.categoryName.setText(approvedOrders)
        adapter= RectroImagesListAdapter(mContext, approveList)
        holder.preRetroPreviewLayoutBinding.recyclerViewimages.adapter=adapter
        holder.itemView.setOnClickListener {
            listner.onClick(position,approvedOrders)

        }






    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val preRetroPreviewLayoutBinding: PreRetroReviewLayoutBinding) :
        RecyclerView.ViewHolder(preRetroPreviewLayoutBinding.root)

}