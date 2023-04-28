//package com.apollopharmacy.vishwam.ui.home.apnarectro.approval
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.viewpager.widget.ViewPager
//import com.apollopharmacy.vishwam.R
//import com.apollopharmacy.vishwam.databinding.ActivityPreviewRetroImageBinding
//import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter.RetroPreviewImage
//import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
//import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.GetImageUrlsResponse
//
//class RetroPreviewImageActivity : AppCompatActivity(), PreviewLastImageCallback,
//        ViewPager.OnPageChangeListener {
//        lateinit var activityPreviewImageBinding: ActivityPreviewRetroImageBinding
//private var previewImageAdapter: RetroPreviewImage? = null
//        var list = ArrayList<String>()
//public var getImageUrlsResponseList: GetImageUrlResponse = null
//        var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()
//
//public var imageUrlList= java.util.ArrayList<GetImageUrlResponse.Category>()
//        var position: Int = 0
//private var store: String = ""
//
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        activityPreviewImageBinding = DataBindingUtil.setContentView(
//        this,
//        R.layout.activity_preview_retro_image
//        )
//
//        if (intent != null) {
//        getImageUrlsResponseList =
//        intent.getSerializableExtra("GET_IMAGE_URLS_RESPONSE") as GetImageUrlResponse
//        list =
//        getIntent().getExtras()?.getStringArrayList("imageList")!!
//        store = intent.getStringExtra("store")!!
//
//        imageUrlList= intent.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>
//        }
//        setUp()
//        }
//
//private fun setUp() {
////        list.add("https://askapollopr.blob.core.windows.net/sampleimage/ReturnImage_3857527_20230403_154049.jpg")
//
//        activityPreviewImageBinding.totalimages.setText("( "+(position+1/list.size+1).toString()+"/"+list.size.toString()+" )")
//        activityPreviewImageBinding.storeId.setText(store)
//        activityPreviewImageBinding.categoryname.setText(imageUrlList.get(position).categoryname)
//
//        previewImageAdapter= RetroPreviewImage(this,this,list)
//
//
//        for (j in getImageUrlsResponseList!!.imageUrls!!) {
//
//        val imageUrl = GetImageUrlResponse.ImageUrl()
//        imageUrl.url = j.url
//        imageUrl.status = j.status
//        imageUrl.remarks = j.remarks
//        imageUrl.retorautoid = j.retorautoid
//        imageUrl.imageid = j.imageid
//        imageUrl.position=j.position
//        imageUrl.categoryid=j.categoryid
//
//        imageUrlsList.add(imageUrl)
//        }
//
//        activityPreviewImageBinding.categoryname.setText(imageUrlsList.get(0))
//
//        activityPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
//        activityPreviewImageBinding.previewImageViewpager.adapter = previewImageAdapter
//        }
//
//        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        }
//
//        override fun onPageSelected(position: Int) {
//        if (imageUrlList.size>position){
//        activityPreviewImageBinding.categoryname.setText(imageUrlList.get(position).categoryname)
//
//        }
//        if (position==0){
//        activityPreviewImageBinding.categoryname.setText(imageUrlList.get(0).categoryname)
//
//        }
//        activityPreviewImageBinding.categoryname.setText(imageUrlList.get(position).categoryname)
//
////        activityPreviewImageBinding.categorycount.setText((position+1/ imageUrlList.size))
//
////        for (i in imageUrlList.indices) {
////
////            activityPreviewImageBinding.categorycount.setText((position+1/ imageUrlList.get(i).imageUrls!!.size+1).toString())
////
////            for (j in imageUrlList[i].imageUrls!!.indices){
////
////                activityPreviewImageBinding.imagename.setText((position+1/ imageUrlList.get(i).imageUrls!!.size+1).toString())
////
////            }
////        }
//        activityPreviewImageBinding.totalimages.setText("( "+(position+1/list.size+1).toString()+"/"+list.size.toString()+" )")
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//        }
//
//        override fun onClick(position: Int, status: String) {
//        }
//        }
