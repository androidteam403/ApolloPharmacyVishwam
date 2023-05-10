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
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesCallback
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.adapter.ImagesCardViewAdapter
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import java.util.ArrayList

class ConfigApnaAdapter(
    private var configList: GetStoreWiseCatDetailsApnaResponse,
    private var configListDup: ArrayList<GetStoreWiseCatDetailsApnaResponse>,
    private val callbackInterface: ImagesUploadAdapter.CallbackInterface,
    private var context: Context
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
        val category  = configList.configlist?.get(position)
        holder.adapterConfigApnaBinding.categoryNumber.text = category!!.categoryId
        holder.adapterConfigApnaBinding.categoryName.text=category.categoryName

        imagesUploadAdapter =
            ImagesUploadAdapter(position, category.imageDataDto, callbackInterface, category.categoryName)
        holder.adapterConfigApnaBinding.uploadlayoutrecyclerview.layoutManager = LinearLayoutManager(
            ViswamApp.context,
            LinearLayoutManager.HORIZONTAL, false)
        holder.adapterConfigApnaBinding.uploadlayoutrecyclerview.adapter = imagesUploadAdapter
    }


    override fun getItemCount(): Int {
        return configList.configlist!!.size
    }

    class ViewHolder(val adapterConfigApnaBinding: AdapterConfigApnaBinding) :
        RecyclerView.ViewHolder(adapterConfigApnaBinding.root)

}