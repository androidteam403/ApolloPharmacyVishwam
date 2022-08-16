package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reshootactivity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse
import com.bumptech.glide.Glide

class ImagesCardViewAdapterRes(
    private var configPositionRes: Int,
    private var imageUrlsList: List<GetImageUrlModelResponse.Category.ImageUrl>?,
    private var callbackInterface: CallbackInterface
) : RecyclerView.Adapter<ImagesCardViewAdapterRes.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesCardViewAdapterRes.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_reshootactivity_iagesview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesCardViewAdapterRes.ViewHolder, position: Int) {
        var imageUrls = imageUrlsList?.get(position)


        if(imageUrls?.url!="IMAGE1" && imageUrls?.url!="IMAGE2" && imageUrls?.url!="") {
            Glide.with(context).load(imageUrls?.url).into(holder.iageView)
        }else{
            Glide.with(context).load(R.drawable.placeholder_image).into(holder.iageView)
        }

        if(imageUrlsList?.get(position)?.status.equals("2")) {
            holder.eyeImage.visibility = View.GONE
            holder.camera.visibility = View.VISIBLE
        }

        if(imageUrls?.isReshootStatus == true){
            holder.camera.visibility = View.GONE
            holder.eyeImage.visibility = View.VISIBLE
            holder.redTrashLayout.visibility = View.VISIBLE
        }else if(imageUrls?.isReshootStatus == false){
            holder.camera.visibility = View.VISIBLE
            holder.eyeImage.visibility = View.GONE
            holder.redTrashLayout.visibility = View.GONE
        }
//
//        if(imageUrlsList?.get(position)?.status.equals("0")){
//            holder.camera.visibility = View.GONE
//            holder.eyeImage.visibility = View.VISIBLE
//            holder.redTrashLayout.visibility = View.GONE
//        }




        holder.redTrashLayout.setOnClickListener{
            callbackInterface.deleteImageCallBackRes(configPositionRes, position)
        }


        holder.eyeImage.setOnClickListener{
            callbackInterface.capturedImageReviewRes(configPositionRes,imageUrls?.url, it, position)
        }






        holder.camera.setOnClickListener {
            callbackInterface.onClickCamera(position, configPositionRes)
        }









    }

    override fun getItemCount(): Int {
        return imageUrlsList?.size!!
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val camera: LinearLayout = itemView.findViewById(R.id.cameraButtonRes)
        val iageView : ImageView = itemView.findViewById(R.id.imageViewRes)
        val redTrashLayout: LinearLayout = itemView.findViewById(R.id.redTrashRes)
        val eyeImage: LinearLayout = itemView.findViewById(R.id.eyeImageRes)

    }

    interface CallbackInterface {

        fun onClickCamera(position: Int, configPositionRes: Int)
        fun deleteImageCallBackRes(configPositionRes: Int, position: Int)
        fun capturedImageReviewRes(configPositionRes: Int, url: String?, view: View, position: Int)
    }
}