package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterSitePhotoPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaSurveyPreviewCallback
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class ImagePreviewAdapter(
    val mContext: Context,
    val imageClickListener: ApnaSurveyPreviewCallback,
    var data: ArrayList<SurveyCreateRequest.SiteImageMb.Image>,
) : RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterSitePhotoPreviewBinding: AdapterSitePhotoPreviewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.adapter_site_photo_preview,
            parent,
            false
        )
        return ViewHolder(adapterSitePhotoPreviewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterSitePhotoPreviewBinding.image.setImageBitmap(BitmapFactory.decodeFile(
            data[position].url))

        holder.adapterSitePhotoPreviewBinding.eyeImageRes.setOnClickListener {
            imageClickListener.onClick(data[position].url.toString())
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val adapterSitePhotoPreviewBinding: AdapterSitePhotoPreviewBinding) :
        RecyclerView.ViewHolder(adapterSitePhotoPreviewBinding.root)
}