package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.SwachModelResponse
import java.io.File

class UploadButtonAdapter(
    private val configPosition: Int,
    private val configLst: MutableList<SwachModelResponse.Config.ImgeDtcl>?,
    private val callbackInterface: CallbackInterface,
    private val SwachModelResponseList: List<SwachModelResponse.Config>?
) :
    RecyclerView.Adapter<UploadButtonAdapter.ViewHolder>() {


    interface CallbackInterface {
        fun passResultCallback(configPosition: Int, uploadButtonPosition: Int)
        fun deleteImageCallBack(deleteImagePos: Int, position: Int)
        fun capturedImageReview(capturedImagepos: Int, capturedImage: File?, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_upload_button, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val SwachModelResponse = configLst?.get(position)

        var myInt: Int = 1

        if(SwachModelResponse?.file!=null){
            holder.capturedImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
        }


        holder.uploadTextNumber.text = position.plus(myInt).toString()

        if(SwachModelResponse?.file == null){
            holder.capturedImage.setImageURI(null)
            holder.previewLayout.visibility = View.GONE
        }else{
            holder.capturedImagesGoneLayout.visibility = View.VISIBLE
            holder.capturedImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
            holder.previewLayout.visibility = View.VISIBLE
        }




        holder.linearLayout.setOnClickListener {
            //Set your codes about intent here
            callbackInterface.passResultCallback(configPosition,position)
        }

        holder.deleteImage.setOnClickListener{
            callbackInterface.deleteImageCallBack(configPosition, position)
        }


        holder.previewLayout.setOnClickListener{
            callbackInterface.capturedImageReview(configPosition,SwachModelResponse?.file, position)
        }

    }

    override fun getItemCount(): Int {
        return configLst?.size!!
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val uploadTextNumber: TextView = itemView.findViewById(R.id.uploadtextnumberS)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.uploadswacImages)
        val capturedImagesGoneLayout: RelativeLayout = itemView.findViewById(R.id.capturedImagesGoneLayout)
        val capturedImage: ImageView = itemView.findViewById(R.id.capturedImageeGone)
        val deleteImage: ImageView = itemView.findViewById(R.id.deleteImageeGone)
        val previewLayout: LinearLayout = itemView.findViewById(R.id.previewLayout)


    }

}