package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse

class ImagesCardViewAdapterRes(
    private var position: Int,
    private var imageUrlsList: List<GetImageUrlModelResponse.Category.ImageUrl>?,
   private var callbackInterface: CallbackInterface
) : RecyclerView.Adapter<ImagesCardViewAdapterRes.ViewHolder>() {



    interface CallbackInterface {
        fun onClickCamera(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_reshootactivity_iagesview, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var imageUrlsResponse = imageUrlsList?.get(position)

        if(imageUrlsList?.get(position)?.url!=null){
//            holder.imageViewRes.setImageURI(Uri.fromFile(imageUrlsResponse.url))
        }



        if(imageUrlsResponse?.status.equals("2")){
            holder.cameraButtonRed.visibility = View.VISIBLE
        }else{
            holder.cameraButtonRed.visibility = View.GONE
        }


        holder.cameraButtonRed.setOnClickListener {
            callbackInterface.onClickCamera(position)
        }

    }

    override fun getItemCount(): Int {
        return imageUrlsList?.size!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageViewRes : ImageView = itemView.findViewById(R.id.imageViewRes)
        val cameraButtonRed : LinearLayout = itemView.findViewById(R.id.cameraButtonRes)
//        val approvedLayout : LinearLayout = itemView.findViewById(R.id.approved_layoutRes)
//        val reshootLayout : LinearLayout = itemView.findViewById(R.id.reshoot_layoutRes)
//        val partialLayout : LinearLayout = itemView.findViewById(R.id.partially_approved_res)
    }


}