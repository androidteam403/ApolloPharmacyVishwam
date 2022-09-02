package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewImage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPreviewImageBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewImage.adapter.PreviewImgViewPagerAdapter


class PreviewImageActivity: AppCompatActivity(), PreviewImageCallback , ViewPager.OnPageChangeListener{

    lateinit var activityPreviewImageBinding: ActivityPreviewImageBinding
    private var previewImgViewPagerAdapter: PreviewImgViewPagerAdapter? = null
    private var imageUrlsList = ArrayList<GetImageUrlsResponse.ImageUrl>()
    private var getImageUrlsResponse: GetImageUrlsResponse? = null
    private var pendingAndApproved: PendingAndApproved? = null
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewImageBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview_image
        )
        setUp()
    }

    private fun setUp() {
        activityPreviewImageBinding.callback = this
        activityPreviewImageBinding.countCategoryPreview.text= "1"

        getImageUrlsResponse =
            intent.getSerializableExtra("GET_IMAGE_URLS_RESPONSE") as GetImageUrlsResponse
        pendingAndApproved =
            intent.getSerializableExtra("PENDING_AND_APPROVED") as PendingAndApproved

        for (i in getImageUrlsResponse!!.categoryList!!) {
            for (j in i.imageUrls!!) {
                val imageUrl = GetImageUrlsResponse.ImageUrl()
                imageUrl.url = j.url
                imageUrl.status = j.status
                imageUrl.remarks = j.remarks
                imageUrl.categoryid = j.categoryid
                imageUrl.imageid = j.imageid
                imageUrl.isVerified = j.isVerified
                imageUrl.categoryname = i.categoryname
                imageUrl.mainCategoryId = i.categoryid
                imageUrlsList.add(imageUrl)
            }
        }

        activityPreviewImageBinding.imageUrlModel = imageUrlsList.get(0)
        activityPreviewImageBinding.pendingAndApprovedModel = pendingAndApproved

        previewImgViewPagerAdapter = PreviewImgViewPagerAdapter(
            this, imageUrlsList)

        activityPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityPreviewImageBinding.previewImageViewpager.adapter = previewImgViewPagerAdapter
        activityPreviewImageBinding.previewImageViewpager.setCurrentItem(0, true)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

        activityPreviewImageBinding.imageUrlModel = imageUrlsList.get(position)
        var myInt : Int =1

        activityPreviewImageBinding.countCategoryPreview.text= (position+myInt).toString()
        previewImgViewPagerAdapter?.notifyDataSetChanged()


//        if (position == imageUrlsList.size - 1) {
////            var isAllVerified = true
//            for (i in imageUrlsList) {
//                if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
//                    if (i.isVerified == false) {
////                        isAllVerified = false
//                        activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
//                            imageUrlsList.indexOf(i), true
//                        )
//                        break
//                    }
//                }
//            }
////            if (isAllVerified){
////                currentPosition = position
////                activityPreviewImageBinding.imageUrlModel = imageUrlsList.get(position)
//////                activityPreviewImageBinding.totalImages = "${position + 1}/${imageUrlsList.size - 1}"
////            }
////            activityPreviewImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
//
//        }else{
//            currentPosition = position
//            if (currentPosition != imageUrlsList.size - 1) {
//                activityPreviewImageBinding.imageUrlModel = imageUrlsList.get(position)
////                activityPreviewImageBinding.totalImages = "${position + 1}/${imageUrlsList.size - 1}"
////                if (imageUrlsList.get(position).status.equals("1")) {
////                    activityPreviewImageBinding.actionStatus = "1"
////                } else if (imageUrlsList.get(position).status.equals("2")) {
////                    activityPreviewImageBinding.actionStatus = "2"
////                } else {
////                    activityPreviewImageBinding.actionStatus = "0"
////                }
//            }
////            activityPreviewImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
//        }

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onClickBack() {
       super.onBackPressed()
    }
}