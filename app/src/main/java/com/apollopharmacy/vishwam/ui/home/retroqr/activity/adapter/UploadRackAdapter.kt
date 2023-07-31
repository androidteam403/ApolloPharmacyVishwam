package com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.UploadRackLayoutBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.RetroQrUploadCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.ImageDto
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException
import java.util.Locale
import kotlin.collections.ArrayList

class UploadRackAdapter(
    var mContext: Context,
    var mCallback: RetroQrUploadCallback,
    var images: ArrayList<StoreWiseRackDetails.StoreDetail>,
) : RecyclerView.Adapter<UploadRackAdapter.ViewHolder>(),Filterable {

    class ViewHolder(val uploadRackLayoutBinding: UploadRackLayoutBinding) :
        RecyclerView.ViewHolder(uploadRackLayoutBinding.root)
    var charString: String? = null
    private val imagesFilterList=ArrayList<StoreWiseRackDetails.StoreDetail>()

    var imagesListList= ArrayList<StoreWiseRackDetails.StoreDetail>()


    init {
        imagesListList = images
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val uploadRackLayoutBinding = DataBindingUtil.inflate<UploadRackLayoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.upload_rack_layout,
            parent,
            false
        )
        return ViewHolder(uploadRackLayoutBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var items=images.get(position)
        val rackCount = position + 1
        holder.uploadRackLayoutBinding.rackCount.text = items.rackno

//        if (items.imageurl!!.contains(".")|| items.imageurl!!.isNullOrEmpty()) {
        if ( items.imageurl!!.isNotEmpty()) {
            holder.uploadRackLayoutBinding.beforeCaptureLayout.visibility = View.GONE
            holder.uploadRackLayoutBinding.afterCaptureLayout.visibility = View.VISIBLE
//            holder.uploadRackLayoutBinding.eyeImage.visibility = View.VISIBLE

//            Glide.with(ViswamApp.context).load(RijndaelCipherEncryptDecrypt().decrypt(items.imageurl,"blobfilesload"))
//                .placeholder(R.drawable.thumbnail_image)
//                .into(holder.uploadRackLayoutBinding.afterCapturedImage)
            Glide.with(ViswamApp.context).load(items.imageurl)
                .placeholder(R.drawable.thumbnail_image)
                .into(holder.uploadRackLayoutBinding.afterCapturedImage)

            holder.uploadRackLayoutBinding.eyeImage.setOnClickListener {
                mCallback.imageData(position, items.imageurl!!, items.rackno!!, items.qrcode!!,holder.uploadRackLayoutBinding.eyeImage)
            }


        } else {
            holder.uploadRackLayoutBinding.beforeCaptureLayout.visibility = View.VISIBLE
            holder.uploadRackLayoutBinding.afterCaptureLayout.visibility = View.GONE
//            holder.uploadRackLayoutBinding.eyeImage.visibility = View.GONE

//            holder.uploadRackLayoutBinding.afterCapturedImage.setImageBitmap(rotateImage(
//                BitmapFactory.decodeFile(items.image.absolutePath),
//                items.image))
        }
        holder.uploadRackLayoutBinding.camera.setOnClickListener {
            mCallback.onClickCameraIcon(position,"upload")
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
                images = if (charString.equals("All")||charString!!.isEmpty()) {
                    imagesListList
                } else {
                    imagesFilterList.clear()
                    for (row in imagesListList) {
                        if (!imagesFilterList.contains(row) && row.rackno!!.lowercase(Locale.getDefault()).contains(
                                charString!!.lowercase(
                                Locale.getDefault()))) {
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