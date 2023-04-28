package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.AdapterImagesuploadApnaBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.bumptech.glide.Glide
import java.io.File

class ImagesUploadAdapterPostRetro(
    private var configPosition: Int,
    private var imageDataDto: MutableList<GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl>?,
    private var callbackInterface: CallbackInterface,
    private var categoryName: String?,
    private var groupingImageUrlList: MutableList<MutableList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>,
    private var stage: String
) : RecyclerView.Adapter<ImagesUploadAdapterPostRetro.ViewHolder>() {

    interface CallbackInterface {

        fun onClickImageView(s: String)
        fun onClickCameraIcon(configPosition: Int, uploadButtonPosition: Int)
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
    ): ImagesUploadAdapterPostRetro.ViewHolder {
        val adapterImagesuploadApnaBinding: AdapterImagesuploadApnaBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_imagesupload_apna,
                parent,
                false
            )
        return ViewHolder(adapterImagesuploadApnaBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category  = imageDataDto?.get(position)
        var myInt: Int = 1

        holder.adapterImagesuploadApnaBinding.imageTextandNumber.text = "Image" +position.plus(myInt).toString()
        if(category!!.file!=null){
            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
            Glide.with(ViswamApp.context).load(category!!.file)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=1.0f
//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.VISIBLE
        }else{
            if(stage.equals("isPreRetroStage")){
                Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
                holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility=View.GONE
            }
            else if(stage.equals("isPostRetroStage") ){
                if(groupingImageUrlList.get(position).get(0).status.equals("0")){
                    Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
                        .placeholder(R.drawable.placeholder_image)
                        .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                    holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
                    holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                    holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                    holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                }
                else if(groupingImageUrlList.get(position).get(0).status.equals("1")){
//                    if(uploadStage){
                        Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
//                    }else{
//                        Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
//                    }

                }
                else if(groupingImageUrlList.get(position).get(0).status.equals("2")){
                    Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
                        .placeholder(R.drawable.placeholder_image)
                        .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                    holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
                    holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                    holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                    holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
                }
//

            }else if(stage.equals("isAfterCompletionStage")){

                if(groupingImageUrlList.get(position).get(0).status.equals("0")){
                    Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
                        .placeholder(R.drawable.placeholder_image)
                        .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                    holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
                    holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                    holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                    holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                }
                else if(groupingImageUrlList.get(position).get(0).status.equals("1")){
//                    if(uploadStage){
                        Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
//                    }else{
//                        Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
//                    }

                }
                else if(groupingImageUrlList.get(position).get(0).status.equals("2")){
                    Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
                        .placeholder(R.drawable.placeholder_image)
                        .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                    holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
                    holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                    holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                    holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
                }
//                if(groupingImageUrlList.get(position).get(0).stage.equals("3")){

//                }

            }


//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
//            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.aftercapturedimage.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.GONE
        }
//        holder.adapterImagesuploadApnaBinding.eyeImage.setOnClickListener {
//            uploadImagesCallback.onClickEyeImage()
//        }
        holder.adapterImagesuploadApnaBinding.aftercapturedimage.setOnClickListener {
            if(stage.equals("isPreRetroStage")){
                callbackInterface.onClickImageView("isPreRetroStage")
            }else if(stage.equals("isPostRetroStage")){
                callbackInterface.onClickImageView("isPostRetroStage")
            }else if(stage.equals("isAfterCompletionStage")){
                callbackInterface.onClickImageView("isAfterCompletionStage")
            }

        }

        holder.adapterImagesuploadApnaBinding.cameraIcon.setOnClickListener {
            callbackInterface.onClickCameraIcon(configPosition,position)
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