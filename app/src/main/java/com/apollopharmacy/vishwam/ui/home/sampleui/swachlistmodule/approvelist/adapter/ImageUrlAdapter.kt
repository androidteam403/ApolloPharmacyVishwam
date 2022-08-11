package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.adapter

import android.content.Context
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.AdapterImageUrlsBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.ApproveListcallback
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.bumptech.glide.Glide

class ImageUrlAdapter(
    val mContext: Context,
    val imageUrlsList: List<GetImageUrlsResponse.ImageUrl>,
    val approveListcallback: ApproveListcallback,
    val isApproved: Boolean
) : RecyclerView.Adapter<ImageUrlAdapter.ViewHolder>() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageUrlAdapter.ViewHolder {
        val adapterImageUrlsBinding: AdapterImageUrlsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_image_urls, parent, false
        )
        return ViewHolder(adapterImageUrlsBinding)
    }

    override fun onBindViewHolder(holder: ImageUrlAdapter.ViewHolder, position: Int) {
        var view: View? = null

        val imageUrl = imageUrlsList.get(position)
        holder.adapterImageUrlsBinding.isApproved = isApproved
        Glide.with(mContext).load(imageUrl.url)
            .error(R.drawable.placeholder_image)
            .into(holder.adapterImageUrlsBinding.image)
        holder.adapterImageUrlsBinding.image.setOnClickListener {


            imageUrl.url?.let { it1 -> approveListcallback.onClickImage(position, it1) }
        }


    }

    override fun getItemCount(): Int {
        return imageUrlsList.size
    }




    class ViewHolder(val adapterImageUrlsBinding: AdapterImageUrlsBinding) :
        RecyclerView.ViewHolder(adapterImageUrlsBinding.root)
}
