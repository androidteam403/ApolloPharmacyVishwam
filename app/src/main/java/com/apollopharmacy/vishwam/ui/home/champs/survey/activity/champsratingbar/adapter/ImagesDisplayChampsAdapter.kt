package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.AdapterCaptureimagesChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarCallBack
import com.bumptech.glide.Glide
import java.io.File
import java.util.ArrayList

class ImagesDisplayChampsAdapter(
    private val  imageLayout: ArrayList<String>,
    private val configPosition: ArrayList<File>,
    private val context: Context,
    private val champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack,
) : RecyclerView.Adapter<ImagesDisplayChampsAdapter.ViewHolder>() {




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterCaptureImagesSwachBinding:AdapterCaptureimagesChampsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_captureimages_champs,
                parent,
                false
            )
        return ViewHolder(adapterCaptureImagesSwachBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champsUploadedImages = imageLayout.get(position)



        if(configPosition!=null){
//            Glide.with(ViswamApp.context).load(configPosition.toString())
//                .placeholder(R.drawable.placeholder_image)
//                .into(holder.adapterCaptureImagesSwachBinding.uploadImageHere)

//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
        }else{
//            holder.afterCaptureImage.visibility = View.GONE
//            holder.eyeImage.visibility = View.GONE
//            holder.redTrashLayout.visibility = View.GONE
//            holder.beforeCaptureLayout.visibility = View.VISIBLE
        }






    }


    override fun getItemCount(): Int {
        return configPosition?.size
    }


    class ViewHolder(val adapterCaptureImagesSwachBinding:AdapterCaptureimagesChampsBinding):
        RecyclerView.ViewHolder(adapterCaptureImagesSwachBinding.root)
}

