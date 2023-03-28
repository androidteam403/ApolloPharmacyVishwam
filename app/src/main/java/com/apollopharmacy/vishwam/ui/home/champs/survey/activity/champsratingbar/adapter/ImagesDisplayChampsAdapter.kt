package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.AdapterCaptureimagesChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarCallBack
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.net.URL

class ImagesDisplayChampsAdapter(
    private val imageDataList: MutableList<ChampsDetailsandRatingBarActivity.ImagesData>?,
    private val context: Context,
    private val champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack,
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val champsUploadedImages = imageDataList!!.get(position)

            if (imageDataList!!.get(position).file != null) {
                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.VISIBLE
                Glide.with(ViswamApp.context).load(imageDataList!!.get(position).file)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.adapterCaptureImagesSwachBinding.uploadImageChamps)

            } else if(imageDataList.get(position).imageUrl!=null && !imageDataList.get(position).imageUrl.isEmpty()){
                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.GONE
                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.VISIBLE
                Glide.with(ViswamApp.context).load(imageDataList!!.get(position).imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.adapterCaptureImagesSwachBinding.uploadImageChamps)
            }
            else {
                holder.adapterCaptureImagesSwachBinding.imagePlusIcon.visibility = View.VISIBLE
                holder.adapterCaptureImagesSwachBinding.uploadImageLayout1.visibility = View.GONE
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


    }


    override fun getItemCount(): Int {
        return imageDataList!!.size
//        if (!gettingImages) {
//
//        } else {
//            return imageUrls!!.size
//        }

    }


    class ViewHolder(val adapterCaptureImagesSwachBinding: AdapterCaptureimagesChampsBinding) :
        RecyclerView.ViewHolder(adapterCaptureImagesSwachBinding.root)
}

