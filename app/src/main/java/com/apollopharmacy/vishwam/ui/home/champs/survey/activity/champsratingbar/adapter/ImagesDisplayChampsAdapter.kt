package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter

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
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.bumptech.glide.Glide

class ImagesDisplayChampsAdapter(
    private val context: Context,
    private val champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack,
    private var imageDataLists: MutableList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>?,
    private val status: String
) : RecyclerView.Adapter<ImagesDisplayChampsAdapter.ViewHolder>() {


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
                if((imageDataLists!!.get(position).imageUrl!=null && !imageDataLists!!.get(position).imageUrl!!.isEmpty()) ||
                    imageDataLists!!.get(position).file!=null){
                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.VISIBLE
                    if(imageDataLists!!.get(position).imageUrl!=null && !imageDataLists!!.get(position).imageUrl!!.isEmpty()){
                        Glide.with(context).load(imageDataLists!!.get(position).imageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterCaptureImagesSwachBinding.uploadImageChamps)
                    }else{
                        Glide.with(context).load(imageDataLists!!.get(position).file)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.adapterCaptureImagesSwachBinding.uploadImageChamps)
                    }
                    if(status.equals("COMPLETED")){
                        holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.GONE
                    }else{
                        holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.VISIBLE
                    }
            }
            else {
                if(status.equals("COMPLETED")){
                    holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
                    holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.GONE
                    holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.GONE
                }else{
                    holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.VISIBLE
                    holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.GONE
                    holder.adapterCaptureImagesSwachBinding.redTrash.visibility=View.VISIBLE
                }

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


        holder.adapterCaptureImagesSwachBinding.imagePlusIcon.setOnClickListener {
            champsDetailsandRatingBarCallBack.onClickPlusIcon(position)
        }
        holder.adapterCaptureImagesSwachBinding.redTrash.setOnClickListener {
            champsDetailsandRatingBarCallBack.onClickImageDelete(position)
        }

        holder.adapterCaptureImagesSwachBinding.uploadImageChamps.setOnClickListener {
            champsDetailsandRatingBarCallBack.onClickImageView(it,imageDataLists , position);
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

