package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApprovalActivityPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter.ApprovalCategoryListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen.PostRectroReviewScreen
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreRectroReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro.ApnaRetroPreviewLastImageActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro.RetroPreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.previewlastimage.PreviewLastImageActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

class ApprovalPreviewActivity : AppCompatActivity(), ApprovalReviewCallback {
    lateinit var activityPreviewBinding: ApprovalActivityPreviewBinding
    private var stage: String = ""
    private var status: String = ""
    private var store: String = ""
    public var imageList = ArrayList<String>()
    public var pendingList = ArrayList<String>()
    public var reshootList = ArrayList<String>()
    public var approveList = ArrayList<String>()
    public var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()

    public var imageUrlList = ArrayList<GetImageUrlResponse.Category>()
    private var getImageUrlsResponses = GetImageUrlResponse()
    private var uploadDate: String = ""

    private var retroId: String = ""
    var adapter: ApprovalCategoryListAdapter? = null
    lateinit var viewModel: ApprovalPreviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewBinding =
            DataBindingUtil.setContentView(this, R.layout.approval_activity_preview)
        viewModel = ViewModelProvider(this)[ApprovalPreviewViewModel::class.java]

        setUp()
    }

    @SuppressLint("ResourceType")
    private fun setUp() {

        activityPreviewBinding.callback = this
        stage = intent.getStringExtra("stage")!!
        retroId = intent.getStringExtra("retroId")!!
        status = intent.getStringExtra("status")!!
        store = intent.getStringExtra("site")!!
        var imageUrlRequest = GetImageUrlRequest()
        imageUrlRequest.retroId = retroId
        imageUrlRequest.storeid = store
//        imageUrlRequest.retroId = "APRET1400120230426171928"
//        imageUrlRequest.storeid = "14001"
        viewModel.getRectroApprovalList(imageUrlRequest, this)

        activityPreviewBinding.storeId.setText(store)

        if (status.toLowerCase().contains("pen")) {
            activityPreviewBinding.review.visibility = View.VISIBLE
            activityPreviewBinding.submitButton.backgroundTintList =
                ContextCompat.getColorStateList(
                    this,
                    R.color.greenn)

        } else if (status.toLowerCase().contains("app")) {
            activityPreviewBinding.review.visibility = View.GONE

            activityPreviewBinding.submitButton.backgroundTintList =
                ContextCompat.getColorStateList(
                    this,
                    R.color.grey)

        } else if (status.toLowerCase().contains("res")) {
            activityPreviewBinding.review.visibility = View.GONE

            activityPreviewBinding.submitButton.backgroundTintList =
                ContextCompat.getColorStateList(
                    this,
                    R.color.grey)

        }

    }

    override fun onClickItemView(position: Int, status: String) {
        if (stage.equals("isPreRetroStage")) {
            val intent = Intent(this, PreRectroReviewActivity::class.java)
            intent.putExtra("stage", stage)
            startActivity(intent)
        } else {
            val intent = Intent(this, PostRectroReviewScreen::class.java)
            intent.putExtra("stage", stage)
            startActivity(intent)
        }
    }

    override fun onSuccessImageUrlList(
        value: GetImageUrlResponse,
        categoryList: List<GetImageUrlResponse.Category>, retroId: String,
    ) {
        getImageUrlsResponses = value
        value.setretroId(retroId)
        imageUrlList = categoryList as ArrayList<GetImageUrlResponse.Category>

        var retroIdsGroupedList: Map<Int, List<GetImageUrlResponse.ImageUrl>>? = null
        for (i in categoryList.indices) {
            retroIdsGroupedList =
                categoryList.get(i).imageUrls!!.stream()
                    .collect(Collectors.groupingBy { w -> w.position })


//           getStorePendingApprovedList.getList.clear()

            var getStorePendingApprovedListDummys =
                ArrayList<ArrayList<GetImageUrlResponse.ImageUrl>>()

            for (entry in retroIdsGroupedList!!.entries) {
                getStorePendingApprovedListDummys.addAll(listOf(entry.value as java.util.ArrayList<GetImageUrlResponse.ImageUrl>))
            }

            categoryList.get(i).groupByImageUrlList =
                getStorePendingApprovedListDummys as List<MutableList<GetImageUrlResponse.ImageUrl>>?

        }

        for (j in categoryList!!.indices) {
            for (k in categoryList[j].imageUrls!!.indices) {
                if (retroId.equals(value.retroId)) {
                    imageList.add(categoryList.get(j).imageUrls!!.get(k).url!!)
                    if (categoryList.get(j).imageUrls!!.get(k).status.equals("0")) {
                        pendingList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                    } else if (categoryList.get(j).imageUrls!!.get(k).status.equals("1")) {
                        approveList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                    } else if (categoryList.get(j).imageUrls!!.get(k).status.equals("2")) {
                        reshootList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                    }

                }


            }
        }


        activityPreviewBinding.upload.setText(imageList.size.toString())
        activityPreviewBinding.pending.setText(pendingList.size.toString())
        activityPreviewBinding.reshoot.setText(reshootList.size.toString())
        activityPreviewBinding.accept.setText(approveList.size.toString())

        for (i in value.remarks!!.indices) {
            activityPreviewBinding.storeId.setText(value.remarks!![i].storeId)
            val frmt = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            val date = frmt.parse(value.remarks!![i].createdDate)
            val newFrmt = SimpleDateFormat("dd MMM, yyy - hh:mm a").format(date)
            activityPreviewBinding.uploadon.setText(newFrmt)
            activityPreviewBinding.comments.setText(value.remarks!![i].remarks)
            activityPreviewBinding.uploadby.setText(value.remarks!![i].createdBy)

        }




        adapter = ApprovalCategoryListAdapter(this, imageUrlList, stage, this)
        activityPreviewBinding.recyclerViewcategories.adapter = adapter
    }

    override fun onFailureImageUrlList(value: GetImageUrlResponse) {
    }

    override fun onClickReview() {
        val intent = Intent(this, RetroPreviewImageActivity::class.java)
        intent.putExtra("stage", stage)
        intent.putExtra("store", store)
        intent.putExtra("imageUrlList", imageUrlList)
        intent.putExtra("GET_IMAGE_URL", getImageUrlsResponses)
        intent.putExtra("retroId", retroId)
        intent.putExtra("uploaddate", activityPreviewBinding.uploadon.text.toString())

        intent.putStringArrayListExtra("imageList", imageList)
        startActivityForResult(intent, 210)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 210) {


                imageUrlsList =
                    data?.getSerializableExtra("mainImagesList") as ArrayList<GetImageUrlResponse.ImageUrl>

                for (i in imageUrlList.indices) {
                    for (j in imageUrlList[i].imageUrls!!.indices) {
                        for (k in imageUrlsList.indices){
                            if (imageUrlList[i].imageUrls!![j].imageid.equals(imageUrlsList.get(k).imageid)) {
                                if (imageUrlsList.get(k).isVerified == true){
                                    imageUrlList.get(i).imageUrls!!.get(j).status="1"
                                    imageUrlList.get(i).imageUrls!!.get(j).setisVerified(true)
                                }
                            }

                        }

                    }
                }

//                for (j in imageUrlList!!.indices) {
//                    for (k in imageUrlList[j].imageUrls!!.indices) {
//                        if (retroId.equals(value.retroId)) {
//                            imageList.add(categoryList.get(j).imageUrls!!.get(k).url!!)
//                            if (categoryList.get(j).imageUrls!!.get(k).status.equals("0")) {
//                                pendingList.add(categoryList.get(j).imageUrls!!.get(k).status!!)
//
//                            } else if (categoryList.get(j).imageUrls!!.get(k).status.equals("1")) {
//                                approveList.add(categoryList.get(j).imageUrls!!.get(k).status!!)
//
//                            } else if (categoryList.get(j).imageUrls!!.get(k).status.equals("2")) {
//                                reshootList.add(categoryList.get(j).imageUrls!!.get(k).status!!)
//
//                            }
//
//                        }
//
//
//                    }
//                }

                adapter = ApprovalCategoryListAdapter(this, imageUrlList, stage, this)
                activityPreviewBinding.recyclerViewcategories.adapter = adapter
            }


        }
    }
}
