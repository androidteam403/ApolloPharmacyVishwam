package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterSitePhotoBinding
import com.apollopharmacy.vishwam.databinding.ImageDeleteConfirmDialogBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import java.io.File

class ImageAdapter(
    val mContext: Context,
    private val imageFileList: ArrayList<File>,
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
            imageFileList[position].absolutePath))

        holder.adapterSitePhotoBinding.deleteImage.setOnClickListener {
            openConfirmDialog(position, imageFileList[position])
        }

        holder.adapterSitePhotoBinding.eyeImage.setOnClickListener {
            mCallback.previewImage(imageFileList[position])
        }
    }

    private fun openConfirmDialog(imagePosition: Int, file: File) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imageDeleteConfirmDialogBinding =
            DataBindingUtil.inflate<ImageDeleteConfirmDialogBinding>(
                LayoutInflater.from(mContext),
                R.layout.image_delete_confirm_dialog,
                null,
                false
            )
        dialog.setContentView(imageDeleteConfirmDialogBinding.root)
        imageDeleteConfirmDialogBinding.noButton.setOnClickListener {
            dialog.dismiss()
        }
        imageDeleteConfirmDialogBinding.yesButton.setOnClickListener {
            mCallback.deleteSiteImage(imagePosition, file)
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return imageFileList.size
    }

    class ViewHolder(val adapterSitePhotoBinding: AdapterSitePhotoBinding) :
        RecyclerView.ViewHolder(adapterSitePhotoBinding.root)
}