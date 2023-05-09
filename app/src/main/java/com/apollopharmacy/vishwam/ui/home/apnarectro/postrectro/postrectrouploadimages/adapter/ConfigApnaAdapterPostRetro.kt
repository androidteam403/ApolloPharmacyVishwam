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
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.PostRetroUploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.PostRetroUploadImagesCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesCallback

class ConfigApnaAdapterPostRetro(
    private var configList: ArrayList<PostRetroUploadImagesActivity.ImgeDtcl>,
    private var context: Context,
    private var uploadImagesCallback: PostRetroUploadImagesCallback
) :
    RecyclerView.Adapter<ConfigApnaAdapterPostRetro.ViewHolder>()  {
    private lateinit var imagesUploadAdapter: ImagesUploadAdapterPostRetro



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
            ImagesUploadAdapterPostRetro(configList, position, context, uploadImagesCallback)
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