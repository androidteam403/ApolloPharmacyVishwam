package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterCaptureimagesChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarCallBack
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.PreviewActivityCallback
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.bumptech.glide.Glide

class ImagesDisplayChampsPreviewAdapter(
    private val context: Context,
    private val previewActivityCallback: PreviewActivityCallback,
    private val imageDataLists: MutableList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>?
) : RecyclerView.Adapter<ImagesDisplayChampsPreviewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterCaptureImagesSwachBinding: AdapterCaptureimagesChampsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_captureimages_champs,
                parent,
                false
            )
        return ViewHolder(adapterCaptureImagesSwachBinding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val champsUploadedImages = imageDataLists!!.get(position)

//            if (imageDataList!!.get(position).file != null) {
//                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
//                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.VISIBLE
//                Glide.with(context).load(imageDataList!!.get(position).file)
//                    .placeholder(R.drawable.placeholder_image)
//                    .into(holder.adapterCaptureImagesSwachBinding.uploadImageChamps)
//
//            } else

                if(imageDataLists!!.get(position).imageUrl!=null && !imageDataLists.get(position).imageUrl!!.isEmpty()){
                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.VISIBLE
                Glide.with(context).load(imageDataLists.get(position).imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.adapterCaptureImagesSwachBinding.uploadImageChamps)
                    holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.GONE
//                    if(status.equals("COMPLETED")){
//                        holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.GONE
//                    }else{
//                        holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.GONE
//                    }
            }
            else {
                    holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
                    holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.GONE
//                if(status.equals("COMPLETED")){
//                    holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
//                    holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.GONE
//                    holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.GONE
//                }else{
//                    holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.VISIBLE
//                    holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.GONE
//                    holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.GONE
//                }

            }


//        else {
//            val champsUploadedImages = imageUrls!!.get(position)
//
//            if (imageUrls != null && imageUrls.size > 0 && imageUrls.get(position)!="") {
//                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
//                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.VISIBLE
//                Glide.with(ViswamApp.context).load(imageUrls.get(position))
//                    .placeholder(R.drawable.placeholder_image)
//                    .into(holder.adapterCaptureImagesSwachBinding.uploadImageChamps)
//            } else {
//                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.VISIBLE
//                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.GONE
//            }
//
//        }


//        holder.adapterCaptureImagesSwachBinding.imagePlusIcon.setOnClickListener {
//            previewActivityCallback.onClickPlusIcon(position)
//        }
//        holder.adapterCaptureImagesSwachBinding.redTrash.setOnClickListener {
//            previewActivityCallback.onClickImageDelete(position)
//        }

        holder.adapterCaptureImagesSwachBinding.uploadImageChamps.setOnClickListener {
            previewActivityCallback.onClickImageView(it,imageDataLists!!.get(position).imageUrl);
        }


    }


    override fun getItemCount(): Int {
        return imageDataLists!!.size
//        if (!gettingImages) {
//
//        } else {
//            return imageUrls!!.size
//        }

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(val adapterCaptureImagesSwachBinding: AdapterCaptureimagesChampsBinding) :
        RecyclerView.ViewHolder(adapterCaptureImagesSwachBinding.root)
}

