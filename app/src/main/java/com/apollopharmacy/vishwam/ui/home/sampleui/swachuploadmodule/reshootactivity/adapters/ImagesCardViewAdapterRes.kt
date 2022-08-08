package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reshootactivity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse

class ImagesCardViewAdapterRes(
    private var position: Int,
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

        holder.camera.setOnClickListener {
            callbackInterface.onClickCamera(position)
        }


    }

    override fun getItemCount(): Int {
        return imageUrlsList?.size!!
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val camera: LinearLayout = itemView.findViewById(R.id.cameraButtonRes)


    }

    interface CallbackInterface {

        fun onClickCamera(position: Int)
    }
}