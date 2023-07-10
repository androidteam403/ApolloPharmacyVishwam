package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterQrRetroReviewBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.RetroQrReviewCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.model.ImageData
import com.bumptech.glide.Glide


class ImagesAdapterRetroReviewQr(
    var mContext: Context,
    var callback: RetroQrReviewCallback,
    var imageData: ImageData,
) : RecyclerView.Adapter<ImagesAdapterRetroReviewQr.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterQrRetroReviewBinding =
            DataBindingUtil.inflate<AdapterQrRetroReviewBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_qr_retro_review,
                parent,
                false)
        return ViewHolder(adapterQrRetroReviewBinding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterQrRetroReviewBinding.rackNum.setText("Rack " + (position + 1))

        Glide.with(mContext)
            .load(imageData.reviewImages!![position].url)
            .into(holder.adapterQrRetroReviewBinding.aftercapturedimage2)

        if (imageData.images!![position].file!!.path.isNotEmpty()) {
            holder.adapterQrRetroReviewBinding.beforeCaptureLayout.visibility = View.GONE
            holder.adapterQrRetroReviewBinding.afterCaptureLayout.visibility = View.VISIBLE
            holder.adapterQrRetroReviewBinding.afterCapturedImage.setImageBitmap(BitmapFactory.decodeFile(
                imageData.images!![position].file!!.absolutePath))
        } else {
            holder.adapterQrRetroReviewBinding.beforeCaptureLayout.visibility = View.VISIBLE
            holder.adapterQrRetroReviewBinding.afterCaptureLayout.visibility = View.GONE
        }

        if (imageData.images!![position].matchingPercentage!!.isEmpty()) {
            holder.adapterQrRetroReviewBinding.compareIconLayout.visibility = View.VISIBLE
            holder.adapterQrRetroReviewBinding.matchingPercentageLayout.visibility = View.GONE
        } else {
            holder.adapterQrRetroReviewBinding.compareIconLayout.visibility = View.GONE
            holder.adapterQrRetroReviewBinding.matchingPercentageLayout.visibility = View.VISIBLE
            val matchingPercentage = imageData.images!![position].matchingPercentage!!.toInt()
            if (matchingPercentage in 0..10) {
                holder.adapterQrRetroReviewBinding.rating.setBackgroundDrawable(ContextCompat.getDrawable(
                    mContext,
                    R.drawable.round_rating_bar_red))
                holder.adapterQrRetroReviewBinding.matchingPercentage.setText(imageData.images!![position].matchingPercentage + "%")
            } else {
                holder.adapterQrRetroReviewBinding.rating.setBackgroundDrawable(ContextCompat.getDrawable(
                    mContext,
                    R.drawable.round_rating_bar_green))
                holder.adapterQrRetroReviewBinding.matchingPercentage.setText(imageData.images!![position].matchingPercentage + "%")
            }
        }

        holder.adapterQrRetroReviewBinding.cameraIcon.setOnClickListener {
            callback.onClickCamera(position)
        }
        holder.adapterQrRetroReviewBinding.delete.setOnClickListener {
            callback.onClickDelete(position)
        }
        holder.adapterQrRetroReviewBinding.compareIcon.setOnClickListener {
            callback.onClickCompare(position,
                imageData.images!![position].file,
                imageData.reviewImages!![position].url)
        }
    }

    override fun getItemCount(): Int {
        return 5;
    }

    class ViewHolder(var adapterQrRetroReviewBinding: AdapterQrRetroReviewBinding) :
        RecyclerView.ViewHolder(adapterQrRetroReviewBinding.root) {}

}