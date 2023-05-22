package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
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
    private var stage: String,
    private var getImagesUrlList: MutableList<MutableList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>,
    private var categoryid: String?,
) : RecyclerView.Adapter<ImagesUploadAdapterPostRetro.ViewHolder>() {

    private var uploadedCount:Int=0
    private var approvedCount:Int=0
    private var reshootCount:Int=0
    private var pendingCount:Int=0

    interface CallbackInterface {

        fun onClickImageView(
            s: String,
            posImageUrlList: java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
            categoryName: String?,
            categoryid: String?,
            position: Int,
            configPosition: Int
        )
        fun onClickCameraIcon(
            configPosition: Int,
            uploadButtonPosition: Int,
            get: ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>
        )
        fun deleteImageCallBack(
            deleteImagePos: Int,
            position: Int,
            get: java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>
        )
        fun capturedImageReview(
            capturedImagepos: Int,
            capturedImage: File?,
            view: View,
            position: Int,
            categoryName: String?
        )

//         fun updateCount( pendingCount: Int, approvedCount: Int, reshootCount: Int)
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
        var posImageUrlList = getImagesUrlList.get(position)
        if (stage == "isPreRetroStage") {
            for (i in posImageUrlList) {
                if (i.stage.equals("1")) {
                    if (i.status.equals("0")) {
                        Glide.with(context).load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(
                                context,
                                R.color.material_amber_accent_700
                            )
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE

                    } else if (i.status.equals("1"))
                    {
                        Glide.with(context)
                            .load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(
                                context,
                                R.color.green
                            )

                    }
                    else if (i.status.equals("2")) {
                        if(i.file!=null){
                            Glide.with(context)
                                .load(i.file)
                                .placeholder(R.drawable.placeholder_image)
                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                            holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                                View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                                ContextCompat.getColorStateList(context, R.color.material_amber_accent_700)
                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 1.0f
                        }else{
                            Glide.with(context)
                                .load(i.url)
                                .placeholder(R.drawable.placeholder_image)
                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                            holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE
                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                                View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                                ContextCompat.getColorStateList(context, R.color.color_red)
                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                        }


                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE





                    }else if (i.status.equals("9")){
//newly update
                    }

                }
            }
        }
        else if (stage.equals("isPostRetroStage")) {
            var isPostCreate=true
            for (i in posImageUrlList) {
                if (i.stage.equals("2")) {
                    isPostCreate=false
                    if (i.status.equals("1")) {
                        Glide.with(context)
                            .load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(
                                context,
                                R.color.green
                            )

                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE

                    }
                    else if (i.status.equals("1")) {
//                    if(uploadStage){
                        Glide.with(context)
                            .load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(
                                context,
                                R.color.green
                            )


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
                    else if (i.status.equals("2")) {

                        if(i.file!=null){
                            Glide.with(context)
                                .load(i.file)
                                .placeholder(R.drawable.placeholder_image)
                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                            holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 1.0f
                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                                View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                                ContextCompat.getColorStateList(context, R.color.material_amber_accent_700)
                        }else{
                            Glide.with(context)
                                .load(i.url)
                                .placeholder(R.drawable.placeholder_image)
                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                            holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE
                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                                View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                                ContextCompat.getColorStateList(context, R.color.color_red)
                        }
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE



                    }else if (i.status.equals("9")){
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                        Glide.with(context)
                            .load(i.file)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(context, R.color.material_amber_accent_700)

                    }
                }
            }
            if(isPostCreate){
                for (i in posImageUrlList) {
                    if (i.stage.equals("1")) {
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                        Glide.with(context).load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
                        holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility=View.VISIBLE
                    }
                }

            }
        }
        else if (stage.equals("isAfterCompletionStage")) {
            var isPostCreate=true
            for (i in posImageUrlList) {
                if (i.stage.equals("3")) {
                    isPostCreate=false
                    if (i.status.equals("0")) {
                        Glide.with(context)
                            .load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(
                                context,
                                R.color.material_amber_accent_700
                            )
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE

                    }
                    else if (i.status.equals("1")) {
//                    if(uploadStage){
                        Glide.with(context)
                            .load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(
                                context,
                                R.color.green
                            )

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
                    else if (i.status.equals("2")) {

                        if(i.file!=null){
                            Glide.with(context)
                                .load(i.file)
                                .placeholder(R.drawable.placeholder_image)
                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                            holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                                View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 1.0f
                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                                ContextCompat.getColorStateList(context, R.color.material_amber_accent_700)
                        }else{
                            Glide.with(context)
                                .load(i.url)
                                .placeholder(R.drawable.placeholder_image)
                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                            holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.GONE
                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                                View.VISIBLE
                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                                ContextCompat.getColorStateList(context, R.color.color_red)
                        }

                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
                            View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
                            View.VISIBLE

                    }else if (i.status.equals("9")){
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                        Glide.with(context)
                            .load(i.file)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility=View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
                            View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
                            ContextCompat.getColorStateList(context, R.color.material_amber_accent_700)

                    }
                }
            }
            if(isPostCreate){
                //before uploading images
                for (i in posImageUrlList) {
                    if (i.stage.equals("2")) {
                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
                        Glide.with(context).load(i.url)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
                        holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.GONE
                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.VISIBLE
                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility=View.VISIBLE
                    }
                }

            }
        }



//        var myInt: Int = 1
//
//        holder.adapterImagesuploadApnaBinding.imageTextandNumber.text =
//            "Image" + position.plus(myInt).toString()
//        if (category!!.file != null) {
//            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
//            Glide.with(ViswamApp.context).load(category!!.file)
//                .placeholder(R.drawable.placeholder_image)
//                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 1.0f
////            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
//            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.VISIBLE
//        } else {
//
//
//            if (stage.equals("isPreRetroStage")) {
//
//                for (i in groupingImageUrlList.get(position)) {
//                    if (i.stage.equals("1")) {
//                        if (i.status.equals("0")) {
//                            Glide.with(ViswamApp.context).load(i.url)
//                                .placeholder(R.drawable.placeholder_image)
//                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                                View.GONE
//                            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                                View.VISIBLE
//                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
//                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                                View.VISIBLE
//                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
//                                ContextCompat.getColorStateList(
//                                    context,
//                                    R.color.material_amber_accent_700
//                                )
//
//                        } else if (i.status.equals("1")) {
//                            Glide.with(ViswamApp.context)
//                                .load(groupingImageUrlList.get(position).get(0).url)
//                                .placeholder(R.drawable.placeholder_image)
//                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                                View.GONE
//                            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                                View.VISIBLE
//                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
//                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                                View.VISIBLE
//
//                        } else if (i.status.equals("2")) {
//                            Glide.with(ViswamApp.context)
//                                .load(groupingImageUrlList.get(position).get(0).url)
//                                .placeholder(R.drawable.placeholder_image)
//                                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                            holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                                View.GONE
//                            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                                View.VISIBLE
//                            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility =
//                                View.VISIBLE
//                            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                                View.VISIBLE
//                            holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
//                                ContextCompat.getColorStateList(context, R.color.red)
//
//                        }
//                    }
//                }
//                else if (stage.equals("isPostRetroStage")) {
//                    if (groupingImageUrlList.get(position).get(0).status.equals("0")) {
//                        Glide.with(ViswamApp.context)
//                            .load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                            View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
//                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
//                            ContextCompat.getColorStateList(
//                                context,
//                                R.color.material_amber_accent_700
//                            )
//
//                    } else if (groupingImageUrlList.get(position).get(0).status.equals("1")) {
////                    if(uploadStage){
//                        Glide.with(ViswamApp.context)
//                            .load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                            View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                            View.VISIBLE
//
//                        //                    }else{
////                        Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
////                            .placeholder(R.drawable.placeholder_image)
////                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
////                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
////                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
////                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
////                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
////                    }
//
//                    } else if (groupingImageUrlList.get(position).get(0).status.equals("2")) {
//                        Glide.with(ViswamApp.context)
//                            .load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                            View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
//                            ContextCompat.getColorStateList(context, R.color.red)
//
//                    }
//
//
////
//
//                } else if (stage.equals("isAfterCompletionStage")) {
//
//                    if (groupingImageUrlList.get(position).get(0).status.equals("0")) {
//                        Glide.with(ViswamApp.context)
//                            .load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                            View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
//                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
//                            ContextCompat.getColorStateList(
//                                context,
//                                R.color.material_amber_accent_700
//                            )
//
//                    } else if (groupingImageUrlList.get(position).get(0).status.equals("1")) {
////                    if(uploadStage){
//                        Glide.with(ViswamApp.context)
//                            .load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                            View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                            View.VISIBLE
//
////                    }else{
////                        Glide.with(ViswamApp.context).load(groupingImageUrlList.get(position).get(0).url)
////                            .placeholder(R.drawable.placeholder_image)
////                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
////                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
////                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
////                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
////                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.GONE
////                    }
//
//                    } else if (groupingImageUrlList.get(position).get(0).status.equals("2")) {
//                        Glide.with(ViswamApp.context)
//                            .load(groupingImageUrlList.get(position).get(0).url)
//                            .placeholder(R.drawable.placeholder_image)
//                            .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
//                        holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha = 0.5f
//                        holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility =
//                            View.GONE
//                        holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility =
//                            View.VISIBLE
//                        holder.adapterImagesuploadApnaBinding.imageTick.imageTintList =
//                            ContextCompat.getColorStateList(context, R.color.red)
//                    }
//
//                }
//
//
////            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
////            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.VISIBLE
////            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
////            holder.adapterImagesuploadApnaBinding.aftercapturedimage.visibility = View.GONE
////            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
////            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.GONE
////            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.VISIBLE
////            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.GONE
//            }
//        holder.adapterImagesuploadApnaBinding.eyeImage.setOnClickListener {
//            uploadImagesCallback.onClickEyeImage()
//        }
            holder.adapterImagesuploadApnaBinding.aftercapturedimage.setOnClickListener {
                if (stage.equals("isPreRetroStage")) {
                    callbackInterface.onClickImageView(
                        "isPreRetroStage",
                        posImageUrlList as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
                        categoryName,
                        categoryid,
                        position,
                        configPosition
                    )
                } else if (stage.equals("isPostRetroStage")) {
                    callbackInterface.onClickImageView(
                        "isPostRetroStage",
                        posImageUrlList as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
                        categoryName,
                        categoryid,
                        position,
                        configPosition
                    )
                } else if (stage.equals("isAfterCompletionStage")) {
                    callbackInterface.onClickImageView(
                        "isAfterCompletionStage",
                        posImageUrlList as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
                        categoryName,
                        categoryid,
                        position,
                        configPosition
                    )
                }

            }

            holder.adapterImagesuploadApnaBinding.cameraIcon.setOnClickListener {
                callbackInterface.onClickCameraIcon(
                    configPosition,
                    position,
                    getImagesUrlList.get(position) as ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>
                )
            }
            holder.adapterImagesuploadApnaBinding.redTrash.setOnClickListener {
                callbackInterface.deleteImageCallBack(configPosition, position,
                    getImagesUrlList.get(position) as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>
                )
            }
////        callbackInterface.updateCount(pendingCount, approvedCount,reshootCount)
//        }
    }

    override fun getItemCount(): Int {
        return getImagesUrlList?.size!!
    }
    class ViewHolder(val adapterImagesuploadApnaBinding: AdapterImagesuploadApnaBinding) :
        RecyclerView.ViewHolder(adapterImagesuploadApnaBinding.root)
}