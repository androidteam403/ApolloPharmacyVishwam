package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ImageAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaNewPreviewCallBack
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.bumptech.glide.Glide

class PreviewImageAdapter(
    val mContext: Context,
    private val imagetData: ArrayList<SurveyDetailsList.Image>,
    val imageClicklistner: ApnaNewPreviewCallBack,

    ) : RecyclerView.Adapter<PreviewImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageAdapterLayoutBinding: ImageAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.image_adapter_layout,
            parent,
            false
        )
        return ViewHolder(imageAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = imagetData.get(position)
        Glide.with(ViswamApp.context).load(items.url)
            .placeholder(R.drawable.thumbnail_image)
            .into(holder.imageAdapterLayoutBinding.image)
        holder.imageAdapterLayoutBinding.eyeImageRes.setOnClickListener {
            imageClicklistner.onItemClick(position, items.url!!,"")
        }
    }

    override fun getItemCount(): Int {
        return imagetData.size
    }

    class ViewHolder(val imageAdapterLayoutBinding: ImageAdapterLayoutBinding) :
        RecyclerView.ViewHolder(imageAdapterLayoutBinding.root)
}