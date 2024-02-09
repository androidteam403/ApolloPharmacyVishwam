package com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ReviewRackLayoutBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.RetroQrUploadCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.util.Locale

class ReviewRackAdapter(
    var mContext: Context,
    var mCallback: RetroQrUploadCallback,
    var images: ArrayList<StoreWiseRackDetails.StoreDetail>,
    var isReview:Boolean
) : RecyclerView.Adapter<ReviewRackAdapter.ViewHolder>(), Filterable {

    class ViewHolder(val reviewRackLayoutBinding: ReviewRackLayoutBinding) :
        RecyclerView.ViewHolder(reviewRackLayoutBinding.root)

    var charString: String? = null
    private val imagesFilterList = ArrayList<StoreWiseRackDetails.StoreDetail>()

    var imagesListList = ArrayList<StoreWiseRackDetails.StoreDetail>()


    init {
        imagesListList = images
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val reviewRackLayoutBinding = DataBindingUtil.inflate<ReviewRackLayoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.review_rack_layout,
            parent,
            false
        )
        return ViewHolder(reviewRackLayoutBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var items = images.get(position)
        val rackCount = position + 1

        if (position==0){
            holder.reviewRackLayoutBinding.viewLine.visibility=View.GONE
        }
        else{
            holder.reviewRackLayoutBinding.viewLine.visibility=View.VISIBLE

        }
        holder.reviewRackLayoutBinding.category.setOnClickListener {
//            mCallback.onClickCategory(position,images)

        }
        if (isReview){
//            items.setcategoryIdQr(items.categoryid!!)
            holder.reviewRackLayoutBinding.uploadButton.visibility=View.GONE

        }
        else{
            holder.reviewRackLayoutBinding.uploadButton.visibility=View.VISIBLE

        }
//        else{
//
//        if (items.categoryname.isNullOrEmpty()){
//
//            holder.reviewRackLayoutBinding.category.setTextColor(Color.parseColor("#FF0000")) // Replace #FF0000 with your desired color code
//
//        }
//        else{
//            holder.reviewRackLayoutBinding.category.text = items.categoryname
//
////            holder.reviewRackLayoutBinding.category.text = items.categoryQr
//            holder.reviewRackLayoutBinding.category.setTextColor(Color.parseColor("#000000")) // Replace #FF0000 with your desired color code
//
//        }
//        }
        holder.reviewRackLayoutBinding.category.text = items.categoryname


        if (items.reviewimageurl!!.isNullOrEmpty()) {
            holder.reviewRackLayoutBinding.beforeCaptureLayout.visibility = View.VISIBLE
            holder.reviewRackLayoutBinding.afterCaptureLayout.visibility = View.GONE
            holder.reviewRackLayoutBinding.delete.visibility = View.GONE
            holder.reviewRackLayoutBinding.eyeImageRes.visibility = View.GONE

        } else {

            holder.reviewRackLayoutBinding.beforeCaptureLayout.visibility = View.GONE
            holder.reviewRackLayoutBinding.afterCaptureLayout.visibility = View.VISIBLE
            holder.reviewRackLayoutBinding.delete.visibility = View.VISIBLE

            holder.reviewRackLayoutBinding.eyeImageRes.visibility = View.VISIBLE

            Glide.with(ViswamApp.context).load(items.reviewimageurl.toString())
                .placeholder(R.drawable.thumbnail_image)
                .into(holder.reviewRackLayoutBinding.afterCapturedImage)
        }
        holder.reviewRackLayoutBinding.cameraIcon.setOnClickListener {
            mCallback.onClickCameraIcon(position, "review")
        }
        holder.reviewRackLayoutBinding.delete.setOnClickListener {
            mCallback.deleteImage(position)

        }
        holder.reviewRackLayoutBinding.eyeImageRes.setOnClickListener {
            mCallback.imageData(
                position,
                items.reviewimageurl!!,
                items.rackno!!,
                items.qrcode!!,
                holder.reviewRackLayoutBinding.eyeImageRes
            )
        }
//        holder.reviewRackLayoutBinding.compareIconLayout.setOnClickListener {
//            mCallback.onClickCompare(items.matchingPercentage!!,
//                RijndaelCipherEncryptDecrypt().decrypt(items.imageurl!!,"blobfilesload"), items.reviewimageurl!!, items.rackno!!
//            )
//        }
        holder.reviewRackLayoutBinding.compareIconLayout.setOnClickListener {
            mCallback.onClickCompare(
                items.matchingPercentage!!,
                items.imageurl!!, items.reviewimageurl!!, items.rackno!!
            )
        }



        holder.reviewRackLayoutBinding.uploadButton.setOnClickListener {
            mCallback.onUpload(position, items.rackno!!)
        }
        if (items.matchingPercentage.toString().isNullOrEmpty()) {
//            holder.reviewRackLayoutBinding.compareIconLayout.visibility = View.GONE
//            holder.reviewRackLayoutBinding.uploadButton.visibility = View.GONE

            holder.reviewRackLayoutBinding.matchingPercentageLayout.visibility = View.GONE
        } else {
//            holder.reviewRackLayoutBinding.compareIconLayout.visibility = View.VISIBLE
//            holder.reviewRackLayoutBinding.uploadButton.visibility = View.VISIBLE
            holder.reviewRackLayoutBinding.matchingPercentageLayout.visibility = View.VISIBLE
            val matchingPercentage = items.matchingPercentage!!.toInt()
            if (matchingPercentage >= 0 && matchingPercentage <= 70) {
                holder.reviewRackLayoutBinding.rating.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.round_rating_bar_red
                    )
                )
                holder.reviewRackLayoutBinding.matchingPercentage.setText(images.get(position).matchingPercentage + "%")
            } else if (matchingPercentage >= 90 && matchingPercentage <= 100) {

                holder.reviewRackLayoutBinding.rating.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.round_rating_bar_green
                    )
                )
                holder.reviewRackLayoutBinding.matchingPercentage.setText(items.matchingPercentage + "%")
            } else if (matchingPercentage >= 70 && matchingPercentage <= 90) {

                holder.reviewRackLayoutBinding.rating.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.round_rating_bar_orane
                    )
                )
                holder.reviewRackLayoutBinding.matchingPercentage.setText(items.matchingPercentage + "%")
            }
        }


    }

    override fun getItemCount(): Int {
        return images.size
    }

    private fun rotateImage(bitmap: Bitmap, file: File): Bitmap? {
        var exifInterface: ExifInterface? = null
        try {
            exifInterface = file.let { ExifInterface(it.absolutePath) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = exifInterface!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(270f)

            else -> {
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                charString = charSequence.toString()
                images = if (charString.equals("All") || charString!!.isEmpty()) {
                    imagesListList
                } else {
                    imagesFilterList.clear()
                    for (row in imagesListList) {
                        if (!imagesFilterList.contains(row) && row.categoryname!!.lowercase(Locale.getDefault())
                                .contains(
                                    charString!!.lowercase(
                                        Locale.getDefault()
                                    )
                                )
                        ) {
                            imagesFilterList.add(row)
                        }
                    }
                    imagesFilterList
                }
                val filterResults = FilterResults()
                filterResults.values = images
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (images != null && !images!!.isEmpty()) {
                    images = filterResults.values as ArrayList<StoreWiseRackDetails.StoreDetail>
                    try {
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.e("FullfilmentAdapter", e.message!!)
                    }
                } else {
                    notifyDataSetChanged()
                }
            }
        }
    }

}