package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.AdapterConfigApnaBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesCallback

class ConfigApnaAdapter(
    private var configList: ArrayList<UploadImagesActivity.ImgeDtcl>,
    private var context: Context,
    private var uploadImagesCallback: UploadImagesCallback
) :
    RecyclerView.Adapter<ConfigApnaAdapter.ViewHolder>()  {
    private lateinit var imagesUploadAdapter: ImagesUploadAdapter



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterConfigApnaBinding: AdapterConfigApnaBinding=
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_config_apna,
                parent,
                false
            )
        return ViewHolder(adapterConfigApnaBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category  = configList.get(position)
        holder.adapterConfigApnaBinding.categoryNumber.text = (position+1).toString()
        holder.adapterConfigApnaBinding.categoryName.text=configList.get(position).categoryName

        imagesUploadAdapter =
            ImagesUploadAdapter(configList, position, context, uploadImagesCallback)
        holder.adapterConfigApnaBinding.uploadlayoutrecyclerview.layoutManager = LinearLayoutManager(
            ViswamApp.context,
            LinearLayoutManager.HORIZONTAL, false)
        holder.adapterConfigApnaBinding.uploadlayoutrecyclerview.adapter = imagesUploadAdapter
    }


    override fun getItemCount(): Int {
       return configList.size
    }

    class ViewHolder(val adapterConfigApnaBinding: AdapterConfigApnaBinding) :
        RecyclerView.ViewHolder(adapterConfigApnaBinding.root)

}