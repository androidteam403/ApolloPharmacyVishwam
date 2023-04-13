package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse

class CapturedImagesListAdapter(
    private val configPosition: Int,
    private val frontview_fileArrayList: MutableList<SwachModelResponse.Config.ImgeDtcl>?
) :
    RecyclerView.Adapter<CapturedImagesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_captured_image_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val CapturedImages = frontview_fileArrayList?.get(position)
        if(CapturedImages?.file == null){
            holder.capturedImage.setImageURI(null)
            holder.capturedImage.visibility = View.GONE
        }else{
            holder.capturedImage.setImageURI(Uri.fromFile(CapturedImages?.file))
        }


    }

    override fun getItemCount(): Int {
        return frontview_fileArrayList?.size!!
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val capturedImage: ImageView = itemView.findViewById(R.id.capturedImagee)

        val deleteImage: ImageView = itemView.findViewById(R.id.deleteImagee)


    }

}