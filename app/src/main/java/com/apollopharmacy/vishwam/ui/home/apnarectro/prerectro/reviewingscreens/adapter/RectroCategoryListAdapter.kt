package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.PreRetroPreviewLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.PreRetroReviewingCallback

class RectroCategoryListAdapter(
    val mContext: Context,
    var approveList: ArrayList<String>,
    val mclickistener: PreRetroReviewingCallback,

    ) :

    RecyclerView.Adapter<RectroCategoryListAdapter.ViewHolder>() {

    var adapter: RectroImagesListAdapter? = null

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
        holder.preRetroPreviewLayoutBinding.sno.setText((position+1).toString())
        holder.preRetroPreviewLayoutBinding.categoryName.setText(approvedOrders)
        adapter= RectroImagesListAdapter(mContext, approveList)
        holder.preRetroPreviewLayoutBinding.recyclerViewimages.adapter=adapter
        holder.itemView.setOnClickListener {
            mclickistener.onClickItemView(position,approvedOrders)

        }






    }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val preRetroPreviewLayoutBinding: PreRetroPreviewLayoutBinding) :
        RecyclerView.ViewHolder(preRetroPreviewLayoutBinding.root)

}