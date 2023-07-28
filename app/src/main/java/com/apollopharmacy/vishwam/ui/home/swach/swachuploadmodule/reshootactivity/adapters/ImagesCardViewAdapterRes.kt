package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.reshootactivity.adapters

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
    private var callbackInterface: CallbackInterface,
    private var categoryList: List<GetImageUrlModelResponse.Category>?

) : RecyclerView.Adapter<ImagesCardViewAdapterRes.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_reshootactivity_iagesview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pos:Int=0
        var conPos:Int=0
        var imageUrls = imageUrlsList?.get(position)
//        configPositionRes=1

//        if(imageUrls!!.status.equals("0")){
//            holder.imageStatus.setImageResource(R.drawable.clock_small)
//        }else if(imageUrls!!.status.equals("1")){
//            holder.imageStatus.setImageResource(R.drawable.approved_greenn)
//        }else if(imageUrls!!.status.equals("2")){
//            holder.imageStatus.setImageResource(R.drawable.action_reshoot)
//        }

        if((imageUrls?.file!=null && !imageUrls?.file!!.equals("")) || imageUrls?.url!=null) {
            if(imageUrls!!.file!=null){
                Glide.with(context).load(imageUrls?.file).into(holder.iageView)
            }else{
                Glide.with(context).load(imageUrls?.url).into(holder.iageView)
            }

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
            categoryList?.get(configPositionRes)?.let { it1 ->
                it1.categoryname?.let { it2 ->
                    callbackInterface.capturedImageReviewRes(configPositionRes,imageUrls?.url, it, position,
                        it2)
                }
            }
        }






        holder.camera.setOnClickListener {
            pos=position
            conPos=configPositionRes
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
//        val imageStatus: ImageView = itemView.findViewById(R.id.imageStatusReeshoot)

    }

    interface CallbackInterface {

        fun onClickCamera(position: Int, configPositionRes: Int)
        fun deleteImageCallBackRes(configPositionRes: Int, position: Int)
        fun capturedImageReviewRes(configPositionRes: Int, url: String?, view: View, position: Int,category:String)
    }
}