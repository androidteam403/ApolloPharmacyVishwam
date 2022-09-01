package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.bumptech.glide.Glide
import java.io.File

class ImagesCardViewAdapter(
    private val configPosition: Int,
    private val imageDataDto: MutableList<SwachModelResponse.Config.ImgeDtcl>?,
    private val callbackInterface: CallbackInterface,
    private val configlist: List<SwachModelResponse.Config>?,
    private  val categoryName: String?
) : RecyclerView.Adapter<ImagesCardViewAdapter.ViewHolder>() {

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
        viewType: Int
    ): ImagesCardViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_captureimages_swach, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ImagesCardViewAdapter.ViewHolder, position: Int) {
        val SwachModelResponse = imageDataDto?.get(position)

        var myInt: Int = 1

        holder.uploadtextnumberS.text = "Image" +position.plus(myInt).toString()


        if(SwachModelResponse?.file!=null){
            holder.beforeCaptureLayout.visibility = View.GONE
            holder.afterCaptureLayout.visibility = View.VISIBLE
            Glide.with(ViswamApp.context).load(SwachModelResponse?.file.toString())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.afterCaptureImage)

//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
            holder.eyeImage.visibility = View.VISIBLE
            holder.redTrashLayout.visibility = View.VISIBLE
        }else{
            holder.afterCaptureImage.visibility = View.GONE
            holder.eyeImage.visibility = View.GONE
            holder.redTrashLayout.visibility = View.GONE
            holder.beforeCaptureLayout.visibility = View.VISIBLE
        }





        holder.plusIcon.setOnClickListener {
            //Set your codes about intent here
            callbackInterface.plusIconAddImage(configPosition,position)
        }

        holder.redTrashLayout.setOnClickListener{
            callbackInterface.deleteImageCallBack(configPosition, position)
        }


        holder.eyeImage.setOnClickListener{
            callbackInterface.capturedImageReview(configPosition,SwachModelResponse?.file, it, position, categoryName)
        }

    }


    override fun getItemCount(): Int {
        return imageDataDto?.size!!
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val beforeCaptureLayout: RelativeLayout = itemView.findViewById(R.id.beforecapturelayout)
        val afterCaptureLayout: RelativeLayout = itemView.findViewById(R.id.aftercapturelayout)
        val redTrashLayout: LinearLayout = itemView.findViewById(R.id.redTrash)
        val eyeImage: LinearLayout = itemView.findViewById(R.id.eyeImage)
        val uploadtextnumberS : TextView = itemView.findViewById(R.id.imageTextandNumber)
        val plusIcon : ImageView = itemView.findViewById(R.id.plusSysmbol)
        val afterCaptureImage: ImageView = itemView.findViewById(R.id.aftercapturedimage)

    }
}

