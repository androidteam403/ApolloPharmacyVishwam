package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.MovieModel
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.SwachModelResponse

class ConfigListAdapter(private val configLst: ArrayList<MovieModel>) :
    RecyclerView.Adapter<ConfigListAdapter.ViewHolder>() {

     var onItemClick: ((MovieModel) -> Unit)?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_uploadswachh_images, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val SwachModelResponse = configLst.get(position)

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageResource(ItemsViewModel.image)
//
//        // sets the text to the textview from our itemHolder class
        holder.textView.text = SwachModelResponse.getTitle()
        holder.imageView.setOnClickListener {
            onItemClick?.invoke(SwachModelResponse)
        }

    }

    override fun getItemCount(): Int {
        return configLst.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val imageView: LinearLayout = itemView.findViewById(R.id.uploadswacImages)
        val textView: TextView = itemView.findViewById(R.id.categoryname)

    }

}