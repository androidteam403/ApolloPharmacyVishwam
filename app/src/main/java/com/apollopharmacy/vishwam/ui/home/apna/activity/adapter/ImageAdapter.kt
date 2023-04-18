package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterSitePhotoBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.Image

class ImageAdapter(
    val mContext: Context,
    private val images: ArrayList<Image>,
    val mCallback: ApnaNewSurveyCallBack,
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterSitePhotoBinding: AdapterSitePhotoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.adapter_site_photo,
            parent,
            false
        )
        return ViewHolder(adapterSitePhotoBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterSitePhotoBinding.afterCapturedImage.setImageBitmap(BitmapFactory.decodeFile(
            images[position].file.absolutePath))

        holder.adapterSitePhotoBinding.deleteImage.setOnClickListener {
            mCallback.deleteSiteImage(position, images[position].file)
        }

        holder.adapterSitePhotoBinding.eyeImage.setOnClickListener {
            mCallback.previewImage(images[position].file)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(val adapterSitePhotoBinding: AdapterSitePhotoBinding) :
        RecyclerView.ViewHolder(adapterSitePhotoBinding.root)
}