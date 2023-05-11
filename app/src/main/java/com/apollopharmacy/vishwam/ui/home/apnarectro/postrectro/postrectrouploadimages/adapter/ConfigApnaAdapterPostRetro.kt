package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.AdapterConfigApnaBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.PostRetroUploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import java.util.stream.Collectors

class ConfigApnaAdapterPostRetro(
    private var apnaConfigList: MutableList<GetImageUrlsModelApnaResponse.Category>,
    private var configList: GetStoreWiseCatDetailsApnaResponse,
    private var context: PostRetroUploadImagesActivity,
    private val callbackInterface: ImagesUploadAdapterPostRetro.CallbackInterface,
    private var stage: String,
    private var categoryList: List<GetImageUrlsModelApnaResponse.Category>? = null,
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
       val category  =  configList.configlist?.get(position)
        val categoryGroup = categoryList!!.get(position)
        holder.adapterConfigApnaBinding.categoryNumber.text = categoryGroup!!.categoryid
        holder.adapterConfigApnaBinding.categoryName.text=categoryGroup.categoryname
        if(categoryGroup.groupingImageUrlList==null){
            var groupingImageUrlLists = ArrayList<ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>()
            var groupingImageUrlList: Map<Int, List<GetImageUrlsModelApnaResponse.Category.ImageUrl>> =
                categoryGroup.imageUrls!!.stream()
                    .collect(Collectors.groupingBy { w -> w.position })
//           getStorePendingApprovedList.getList.clear()


//        var getImageUrlListDummys =
//            java.util.ArrayList<java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>()
            for (entry in groupingImageUrlList.entries) {
                groupingImageUrlLists.addAll(listOf(entry.value as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>))
            }
//        groupingImageUrlLists!!.categoryList!!.get(i).groupingImageUrlList =
//            getImageUrlListDummys as List<MutableList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>?
            categoryGroup.groupingImageUrlList=groupingImageUrlLists
        }


        imagesUploadAdapter =
            ImagesUploadAdapterPostRetro(position, category!!.imageDataDto, callbackInterface, category.categoryName,
                apnaConfigList.get(position).groupingImageUrlList as MutableList<MutableList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>, stage,
                categoryGroup.groupingImageUrlList as MutableList<MutableList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>, categoryGroup!!.categoryid
            )
        holder.adapterConfigApnaBinding.uploadlayoutrecyclerview.layoutManager = LinearLayoutManager(
            ViswamApp.context,
            LinearLayoutManager.HORIZONTAL, false)
        holder.adapterConfigApnaBinding.uploadlayoutrecyclerview.adapter = imagesUploadAdapter
    }


    override fun getItemCount(): Int {
       return categoryList!!.size
    }

    class ViewHolder(val adapterConfigApnaBinding: AdapterConfigApnaBinding) :
        RecyclerView.ViewHolder(adapterConfigApnaBinding.root)

}