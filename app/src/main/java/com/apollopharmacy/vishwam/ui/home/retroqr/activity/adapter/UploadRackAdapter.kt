package com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.UploadRackLayoutBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.RetroQrUploadCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.ImageDto
import java.io.File
import java.io.IOException
import java.util.ArrayList

class UploadRackAdapter(
    var mContext: Context,
    var mCallback: RetroQrUploadCallback,
    var images: ArrayList<ImageDto>,
) : RecyclerView.Adapter<UploadRackAdapter.ViewHolder>() {

    class ViewHolder(val uploadRackLayoutBinding: UploadRackLayoutBinding) :
        RecyclerView.ViewHolder(uploadRackLayoutBinding.root)

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
        val rackCount = position + 1
        holder.uploadRackLayoutBinding.rackCount.text = "Rack $rackCount"

        if (images.get(position).image.path.isNotEmpty()) {
            holder.uploadRackLayoutBinding.beforeCaptureLayout.visibility = View.GONE
            holder.uploadRackLayoutBinding.afterCaptureLayout.visibility = View.VISIBLE
            holder.uploadRackLayoutBinding.afterCapturedImage.setImageBitmap(rotateImage(
                BitmapFactory.decodeFile(images.get(position).image.absolutePath),
                images.get(position).image))
        } else {
            holder.uploadRackLayoutBinding.beforeCaptureLayout.visibility = View.VISIBLE
            holder.uploadRackLayoutBinding.afterCaptureLayout.visibility = View.GONE
        }
        holder.uploadRackLayoutBinding.camera.setOnClickListener {
            mCallback.onClickCameraIcon(position)
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
}