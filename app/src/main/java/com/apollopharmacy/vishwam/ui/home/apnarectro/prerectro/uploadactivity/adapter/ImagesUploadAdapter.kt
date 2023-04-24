package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.AdapterImagesuploadApnaBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesCallback
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.bumptech.glide.Glide
import java.io.File

class ImagesUploadAdapter(
    private var configPosition: Int,
    private var imageDataDto: MutableList<GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl>?,
    private var callbackInterface: CallbackInterface,
    private var categoryName: String?
) : RecyclerView.Adapter<ImagesUploadAdapter.ViewHolder>() {

    interface CallbackInterface {
        fun plusIconAddImage(configPosition: Int, uploadButtonPosition: Int)
        fun deleteImageCallBack(deleteImagePos: Int, position: Int)
        fun capturedImageReview(
            capturedImagepos: Int,
            capturedImage: File?,
            view: View,
            position: Int,
            categoryName: String?
        )
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImagesUploadAdapter.ViewHolder {
        val adapterImagesuploadApnaBinding: AdapterImagesuploadApnaBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_imagesupload_apna,
                parent,
                false
            )
        return ViewHolder(adapterImagesuploadApnaBinding)
    }

    override fun onBindViewHolder(holder: ImagesUploadAdapter.ViewHolder, position: Int) {
        val category  =  imageDataDto?.get(position)
        var myInt: Int = 1

        holder.adapterImagesuploadApnaBinding.imageTextandNumber.text = "Image" +position.plus(myInt).toString()
        if(category!!.file!=null){
            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
            Glide.with(ViswamApp.context).load(category.file)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)

//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.GONE
        }else{
//            Glide.with(ViswamApp.context).load(R.drawable.capture)
//                .placeholder(R.drawable.placeholder_image)
//                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE

//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
//            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
            holder.adapterImagesuploadApnaBinding.aftercapturedimage.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.VISIBLE
            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.GONE
        }
//        holder.adapterImagesuploadApnaBinding.eyeImage.setOnClickListener {
//            uploadImagesCallback.onClickEyeImage()
//        }
        holder.adapterImagesuploadApnaBinding.plusSysmbol.setOnClickListener {
            callbackInterface.plusIconAddImage(configPosition,position)
        }
        holder.adapterImagesuploadApnaBinding.redTrash.setOnClickListener {
            callbackInterface.deleteImageCallBack(configPosition, position)
        }
    }

    override fun getItemCount(): Int {
        return imageDataDto?.size!!
    }
    class ViewHolder(val adapterImagesuploadApnaBinding: AdapterImagesuploadApnaBinding) :
        RecyclerView.ViewHolder(adapterImagesuploadApnaBinding.root)
}