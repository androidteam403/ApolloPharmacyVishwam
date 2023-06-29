package com.apollopharmacy.vishwam.ui.home.apollosensing.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ImageDeleteConfirmDialogBinding
import com.apollopharmacy.vishwam.databinding.LayoutPrescriptionImageBinding
import com.apollopharmacy.vishwam.ui.home.apollosensing.ApolloSensingFragmentCallback
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.ImageDto
import java.io.File
import java.io.IOException

class PrescriptionImageAdapter(
    var mContext: Context,
    var mCallback: ApolloSensingFragmentCallback,
    var images: ArrayList<ImageDto>,
) : RecyclerView.Adapter<PrescriptionImageAdapter.ViewHolder>() {

    class ViewHolder(val layoutPrescriptionImageBinding: LayoutPrescriptionImageBinding) :
        RecyclerView.ViewHolder(layoutPrescriptionImageBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutPrescriptionImageBinding =
            DataBindingUtil.inflate<LayoutPrescriptionImageBinding>(
                LayoutInflater.from(mContext),
                R.layout.layout_prescription_image,
                parent,
                false
            )
        return ViewHolder(layoutPrescriptionImageBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.layoutPrescriptionImageBinding.image.setImageBitmap(
//            BitmapFactory.decodeFile(images[position].file.absolutePath)
//        )

        holder.layoutPrescriptionImageBinding.image.setImageBitmap(
            rotateImage(
                BitmapFactory.decodeFile(images[position].file.absolutePath),
                images[position].file
            )
        )

        holder.layoutPrescriptionImageBinding.deleteImage.setOnClickListener {
            showDialog(position, images[position].file)
        }
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

    private fun showDialog(position: Int, file: File) {
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
            mCallback.deleteImage(position, file)
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return images.size
    }
}