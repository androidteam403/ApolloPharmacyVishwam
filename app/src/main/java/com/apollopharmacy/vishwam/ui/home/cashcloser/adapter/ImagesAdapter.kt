package com.apollopharmacy.vishwam.ui.home.cashcloser.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.CaptureImagesLayoutBinding
import com.apollopharmacy.vishwam.ui.home.cashcloser.CashCloserFragmentCallback
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.ImageData
import com.bumptech.glide.Glide
import java.text.FieldPosition

class ImagesAdapter(
    val mContext: Context,
    var images: ArrayList<ImageData>,
    val mCallback: CashCloserFragmentCallback,
) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val captureImagesLayoutBinding: CaptureImagesLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.capture_images_layout,
                parent,
                false
            )
        return ViewHolder(captureImagesLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
var imageItem  = images.get(position)
        if (imageItem.file != null) {
            holder.captureImagesLayoutBinding.beforecapturelayout.visibility = View.GONE
            holder.captureImagesLayoutBinding.aftercapturelayout.visibility = View.VISIBLE
            holder.captureImagesLayoutBinding.eyeImage.visibility = View.VISIBLE
            holder.captureImagesLayoutBinding.redTrash.visibility = View.VISIBLE

            Glide
                .with(mContext)
                .load(images[position].file.toString())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.captureImagesLayoutBinding.aftercapturedimage)
        } else {
            holder.captureImagesLayoutBinding.eyeImage.visibility = View.GONE
            holder.captureImagesLayoutBinding.redTrash.visibility = View.GONE
            holder.captureImagesLayoutBinding.eyeImage.visibility = View.GONE
            holder.captureImagesLayoutBinding.beforecapturelayout.visibility = View.VISIBLE
        }

        holder.captureImagesLayoutBinding.plusSysmbol.setOnClickListener {
            mCallback.addImage(position)
        }

        holder.captureImagesLayoutBinding.redTrash.setOnClickListener {
            mCallback.deleteImage(position)
        }

        holder.captureImagesLayoutBinding.eyeImage.setOnClickListener {
            mCallback.previewImage(images[position].file!!, position)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    class ViewHolder(val captureImagesLayoutBinding: CaptureImagesLayoutBinding) :
        RecyclerView.ViewHolder(captureImagesLayoutBinding.root)
}