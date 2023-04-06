package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse

class ConfigAdapterSwach(
    private val configLst: SwachModelResponse,
    private val frontview_fileArrayList: ArrayList<SwachModelResponse>,
    private val callbackInterface: ImagesCardViewAdapter.CallbackInterface,
    private val context: Context,
) :
    RecyclerView.Adapter<ConfigAdapterSwach.ViewHolder>() {

    private lateinit var imagesCardViewAdapter: ImagesCardViewAdapter


    interface ConfigPosition {
        fun ConfigPositions(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_config_adapter_swach, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val SwachModelResponse = configLst.configlist?.get(position)

        holder.textView.text = SwachModelResponse?.categoryName

        imagesCardViewAdapter =
            ImagesCardViewAdapter(position, SwachModelResponse?.imageDataDto, callbackInterface, configLst.configlist, SwachModelResponse?.categoryName)
        holder.imageRecyclerView.layoutManager = LinearLayoutManager(
            ViswamApp.context,
            LinearLayoutManager.HORIZONTAL, false)
        holder.imageRecyclerView.adapter = imagesCardViewAdapter

    }


    override fun getItemCount(): Int {
        return configLst.configlist!!.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.categoryNameSwach)
        val imageRecyclerView: RecyclerView = itemView.findViewById(R.id.uploadlayoutrecyclerview)


    }


}