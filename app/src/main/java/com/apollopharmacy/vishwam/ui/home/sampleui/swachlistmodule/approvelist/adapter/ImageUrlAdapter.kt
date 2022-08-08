package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterImageUrlsBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.ApproveListcallback
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.bumptech.glide.Glide

class ImageUrlAdapter(
    val mContext: Context,
    val imageUrlsList: List<GetImageUrlsResponse.ImageUrl>,
    val approveListcallback: ApproveListcallback
) : RecyclerView.Adapter<ImageUrlAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageUrlAdapter.ViewHolder {
        val adapterImageUrlsBinding: AdapterImageUrlsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_image_urls, parent, false
        )
        return ViewHolder(adapterImageUrlsBinding)
    }

    override fun onBindViewHolder(holder: ImageUrlAdapter.ViewHolder, position: Int) {
        val imageUrl = imageUrlsList.get(position)
        Glide.with(mContext).load(imageUrl.url)
            .error(R.drawable.placeholder_image)
            .into(holder.adapterImageUrlsBinding.image)
    }

    override fun getItemCount(): Int {
        return imageUrlsList.size
    }

    class ViewHolder(val adapterImageUrlsBinding: AdapterImageUrlsBinding) :
        RecyclerView.ViewHolder(adapterImageUrlsBinding.root)
}