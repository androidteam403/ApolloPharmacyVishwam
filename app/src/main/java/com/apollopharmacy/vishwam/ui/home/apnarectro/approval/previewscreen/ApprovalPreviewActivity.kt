package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApprovalActivityPreviewBinding
import com.apollopharmacy.vishwam.databinding.DialogLastimagePreviewAlertBinding
import com.apollopharmacy.vishwam.databinding.DialogReviewAlertBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter.ApprovalCategoryListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter.TimeLineListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.*
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen.PostRectroReviewScreen
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreRectroReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro.RetroPreviewImageActivity
import com.apollopharmacy.vishwam.util.Utlis
import org.apache.commons.lang3.text.WordUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class ApprovalPreviewActivity : AppCompatActivity(), ApprovalReviewCallback {
    lateinit var activityPreviewBinding: ApprovalActivityPreviewBinding
    private var stage: String = ""
    private var status: String = ""
    private var store: String = ""
    private var apiStatus: String = ""
    private var apiStage: String = ""
    var isApiHit: Boolean = false
    var isRatingApiHit: Boolean = false
    var stagePosition: String = ""

    public var imageList = ArrayList<String>()
    public var pendingList = ArrayList<String>()
    public var reshootList = ArrayList<String>()
    public var approveList = ArrayList<String>()
    public var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()
    var saveRequestImageslist = ArrayList<SaveAcceptRequest.Imageurl>()
    public var approveResponseList = ArrayList<GetRetroPendingAndApproveResponse.Retro>()
    public var imageUrlList = java.util.ArrayList<GetImageUrlResponse.Category>()
    public var imageUrlsListReview = ArrayList<GetImageUrlResponse.ImageUrl>()

    private var getImageUrlsResponses = GetImageUrlResponse()
    private var uploadDate: String = ""
    var timelineAdapter: TimeLineListAdapter? = null

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
        approveResponseList =
            intent.getSerializableExtra("approvePendingList") as ArrayList<GetRetroPendingAndApproveResponse.Retro>
        var imageUrlRequest = GetImageUrlRequest()
        imageUrlRequest.retroId = retroId
        imageUrlRequest.storeid = store
//        imageUrlRequest.retroId = "APRET1400120230426171928"
//        imageUrlRequest.storeid = "14001"
        viewModel.getRectroApprovalList(imageUrlRequest, this)

        activityPreviewBinding.storeId.setText(store)
        activityPreviewBinding.stage.setText(WordUtils.capitalizeFully(stage.replace("-",
            " ")) + " Preview")
        activityPreviewBinding.retroId.setText(retroId)

        if (status.toLowerCase().contains("pen")) {
            activityPreviewBinding.review.visibility = View.VISIBLE


        } else if (status.toLowerCase().contains("app")) {
            activityPreviewBinding.review.visibility = View.GONE


        } else if (status.toLowerCase().contains("res")) {
            activityPreviewBinding.review.visibility = View.GONE


        }


        activityPreviewBinding.arrow.setOnClickListener {
            activityPreviewBinding.timeLineRecycleview.visibility = View.VISIBLE
            activityPreviewBinding.downarrow.visibility = View.VISIBLE
            activityPreviewBinding.arrow.visibility = View.GONE
        }
        activityPreviewBinding.downarrow.setOnClickListener {
            activityPreviewBinding.timeLineRecycleview.visibility = View.GONE
            activityPreviewBinding.downarrow.visibility = View.GONE
            activityPreviewBinding.arrow.visibility = View.VISIBLE
        }

        if (approveResponseList != null) {
            timelineAdapter =
                TimeLineListAdapter(this, approveResponseList.filter { it.retroid.equals(retroId) })
            activityPreviewBinding.timeLineRecycleview.adapter = timelineAdapter
        }

        activityPreviewBinding.backArrow.setOnClickListener {
            onBackPressed()
        }


    }


    override fun onClickItemView(
        position: Int,
        approvedOrders: ArrayList<List<GetImageUrlResponse.ImageUrl>>?,
        categoryPosition: Int,
        categoryName: String,
        url: String,
        statusPos:String
    ) {
        var tempImageList = ArrayList<GetImageUrlResponse.ImageUrl>()
        for (i in imageUrlList.indices) {
            for (j in imageUrlList.get(i).imageUrls!!.indices) {
                var imageResponse = GetImageUrlResponse.ImageUrl()
                imageResponse.imageid= imageUrlList.get(i).imageUrls!!.get(j).imageid
                imageResponse.stage = imageUrlList.get(i).imageUrls!!.get(j).stage
                tempImageList.add(imageResponse)
            }

        }

        if (tempImageList.distinctBy { it.imageid }.filter { it.stage.equals("1") }.size==tempImageList.distinctBy { it.imageid }.size ) {
            val intent = Intent(this, PreRectroReviewActivity::class.java)
            intent.putExtra("stage", stage)
            intent.putExtra("retroId", retroId)
            intent.putExtra("store", store)
            intent.putExtra("url", url)
            intent.putExtra("imageUrlList", imageUrlList)
            intent.putExtra("uploadby", activityPreviewBinding.uploadby.text.toString())

            intent.putExtra("categoryPos", categoryPosition)
            intent.putExtra("categoryName", categoryName)
            intent.putExtra("status", statusPos)
            intent.putExtra("position", position)
            startActivityForResult(intent, 235)
        } else if (approvedOrders!!.size==2||approvedOrders.size==3) {


            val intent = Intent(this, PostRectroReviewScreen::class.java)
            intent.putExtra("stage", stage)
            intent.putExtra("retroId", retroId)
            intent.putExtra("store", store)
            intent.putExtra("categoryPos", categoryPosition)
            intent.putExtra("categoryName", categoryName)
            intent.putExtra("status", statusPos)
            intent.putExtra("imageUrlList", imageUrlList)
            intent.putExtra("uploadby", activityPreviewBinding.uploadby.text.toString())

            intent.putExtra("imageList", approvedOrders)

            intent.putExtra("position", position)
            startActivityForResult(intent, 241)
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
                    imageList.add(categoryList.get(j).imageUrls!!.get(k).url.toString()!!)
                    if (categoryList.get(j).imageUrls!!.get(k).status.equals("0")) {
                        if (stage.toLowerCase()
                                .contains("pre") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "1"))
                        ) {
                            pendingList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        } else if (stage.toLowerCase()
                                .contains("pos") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "2"))
                        ) {

                            pendingList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        } else if (stage.toLowerCase()
                                .contains("aft") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "3"))
                        ) {
                            pendingList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        }

                    } else if (categoryList.get(j).imageUrls!!.get(k).status.equals("1")) {

                        if (stage.toLowerCase()
                                .contains("pre") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "1"))
                        ) {
                            approveList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        } else if (stage.toLowerCase()
                                .contains("pos") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "2"))
                        ) {
                            approveList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        } else if (stage.toLowerCase()
                                .contains("aft") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "3"))
                        ) {
                            approveList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        }


                    } else if (categoryList.get(j).imageUrls!!.get(k).status.equals("2")) {


                        if (stage.toLowerCase()
                                .contains("pre") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "1"))
                        ) {
                            reshootList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        } else if (stage.toLowerCase()
                                .contains("pos") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "2"))
                        ) {
                            reshootList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        } else if (stage.toLowerCase()
                                .contains("aft") && (categoryList.get(j).imageUrls!!.get(k).stage!!.equals(
                                "3"))
                        ) {
                            reshootList.add(categoryList.get(j).imageUrls!!.get(k).status!!)

                        }

                    }

                }


            }
        }


        activityPreviewBinding.upload.setText((pendingList.size + reshootList.size + approveList.size).toString())
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



        if (imageUrlList != null) {
            adapter = ApprovalCategoryListAdapter(this, imageUrlList, stage, this)
            activityPreviewBinding.recyclerViewcategories.adapter = adapter
        }
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
        intent.putExtra("uploadby", activityPreviewBinding.uploadby.text.toString())
        intent.putExtra("stage", stage)

        intent.putStringArrayListExtra("imageList", imageList)
        startActivityForResult(intent, 210)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

    }

    override fun onSuccessSaveAcceptReshoot(value: SaveAcceptResponse) {
        Utlis.hideLoading()

        val imagesStatusAlertDialog = Dialog(this)
        val dialogLastimagePreviewAlertBinding: DialogReviewAlertBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialog_review_alert, null, false
            )
        imagesStatusAlertDialog.setContentView(dialogLastimagePreviewAlertBinding.root)
//        imagesStatusAlertDialog.setCancelable(false)
        imagesStatusAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {


            val intent = Intent()
            intent.putExtra("isApiHit", true)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

        imagesStatusAlertDialog.show()


        Toast.makeText(getApplicationContext(),
            value.message,
            Toast.LENGTH_LONG).show();
    }

    override fun onFailureSaveAcceptReshoot(value: SaveAcceptResponse) {
        Utlis.hideLoading()
        Toast.makeText(getApplicationContext(),
            value.message,
            Toast.LENGTH_LONG).show();
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("isApiHit", isApiHit)
        intent.putExtra("isRatingApiHit", isRatingApiHit)

        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 210) {

                isApiHit = data?.getBooleanExtra("isApiHit", false) as Boolean

                isRatingApiHit = data?.getBooleanExtra("ratingApi", false) as Boolean
                if (isRatingApiHit) {
                    onBackPressed()
                }

                imageUrlsList =
                    data?.getSerializableExtra("mainImagesList") as ArrayList<GetImageUrlResponse.ImageUrl>
                if (isApiHit) {
                    val intent = Intent()
                    intent.putExtra("isApiHit", isApiHit)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                for (i in imageUrlList.indices) {
                    for (j in imageUrlList[i].imageUrls!!.indices) {
                        for (k in imageUrlsList.indices) {
                            if (imageUrlList[i].imageUrls!![j].imageid.equals(imageUrlsList.get(k).imageid)) {
                                if (imageUrlsList.get(k).isVerified == true && imageUrlsList.get(k).status.equals(
                                        "1")
                                ) {

                                    imageUrlList.get(i).imageUrls!!.get(j).status = "1"
                                    apiStatus = "1"

                                    imageUrlList.get(i).imageUrls!!.get(j).setisVerified(true)
                                } else if (imageUrlsList.get(k).isVerified == true && imageUrlsList.get(
                                        k).status.equals("2")
                                ) {

                                    apiStatus = "2"

                                    imageUrlList.get(i).imageUrls!!.get(j).status = "2"
                                    imageUrlList.get(i).imageUrls!!.get(j)
                                        .setisVerified(true)
                                } else if (imageUrlsList.get(k).isVerified == true && imageUrlsList.get(
                                        k).status.equals("0")
                                ) {
                                    imageUrlList.get(i).imageUrls!!.get(j).status = "0"
                                    apiStatus = "0"

                                    imageUrlList.get(i).imageUrls!!.get(j).setisVerified(false)

                                    imageUrlList.get(i).imageUrls!!.get(j)
                                        .setisVerified(false)
                                }
                            }

                        }

                    }
                }



                for (i in imageUrlsList.indices) {
                    var imageRequest = SaveAcceptRequest.Imageurl()
                    imageRequest.statusid = imageUrlsList.get(i).status
                    imageRequest.imageid = imageUrlsList.get(i).imageid
                    saveRequestImageslist.add(imageRequest)

                }
                val retroIdsGroupedList: Map<String, List<GetImageUrlResponse.ImageUrl>> =
                    imageUrlsList.stream().collect(Collectors.groupingBy { w -> w.status })

                if (retroIdsGroupedList.containsKey("0")) {
                    activityPreviewBinding.pending.setText(retroIdsGroupedList.get("0")!!.size.toString())
                } else {
                    activityPreviewBinding.pending.setText("0")

                }
                if (retroIdsGroupedList.containsKey("1")) {
                    activityPreviewBinding.accept.setText(retroIdsGroupedList.get("1")!!.size.toString())
                } else {
                    activityPreviewBinding.accept.setText("0")

                }
                if (retroIdsGroupedList.containsKey("2")) {
                    activityPreviewBinding.reshoot.setText(retroIdsGroupedList.get("2")!!.size.toString())
                } else {
                    activityPreviewBinding.reshoot.setText("0")

                }
                adapter = ApprovalCategoryListAdapter(this, imageUrlList, stage, this)
                activityPreviewBinding.recyclerViewcategories.adapter = adapter
            }
            if (requestCode == 241) {
                isApiHit = data?.getBooleanExtra("isApiHit", false) as Boolean
                stagePosition = data?.getStringExtra("stagePosition")!!
                isRatingApiHit = data?.getBooleanExtra("ratingApi", false) as Boolean

                imageUrlsListReview =
                    data!!.getSerializableExtra("imagesList") as java.util.ArrayList<GetImageUrlResponse.ImageUrl>

                imageUrlList =
                    data!!.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>




                if (isRatingApiHit || isApiHit) {
                    onBackPressed()
                }

                val retroIdsGroupedList: Map<String, List<GetImageUrlResponse.ImageUrl>> =
                    imageUrlsListReview.filter { it.stage.equals(stagePosition) }.stream()
                        .collect(Collectors.groupingBy { w -> w.status })

                if (retroIdsGroupedList.containsKey("0")) {
                    activityPreviewBinding.pending.setText(retroIdsGroupedList.get("0")!!.size.toString())
                } else {
                    activityPreviewBinding.pending.setText("0")

                }
                if (retroIdsGroupedList.containsKey("1")) {
                    activityPreviewBinding.accept.setText(retroIdsGroupedList.get("1")!!.size.toString())
                } else {
                    activityPreviewBinding.accept.setText("0")

                }
                if (retroIdsGroupedList.containsKey("2")) {
                    activityPreviewBinding.reshoot.setText(retroIdsGroupedList.get("2")!!.size.toString())
                } else {
                    activityPreviewBinding.reshoot.setText("0")

                }

                if (imageUrlList != null) {
                    adapter = ApprovalCategoryListAdapter(this, imageUrlList, stage, this)
                    activityPreviewBinding.recyclerViewcategories.adapter = adapter
                }
            }

            if (requestCode == 235) {
                isApiHit = data?.getBooleanExtra("isApiHit", false) as Boolean

                isRatingApiHit = data?.getBooleanExtra("ratingApi", false) as Boolean
                imageUrlsListReview =
                    data!!.getSerializableExtra("imagesList") as java.util.ArrayList<GetImageUrlResponse.ImageUrl>

                imageUrlList =
                    data!!.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>

                if (isRatingApiHit || isApiHit) {
                    onBackPressed()
                }


                val retroIdsGroupedList: Map<String, List<GetImageUrlResponse.ImageUrl>> =
                    imageUrlsListReview.stream().collect(Collectors.groupingBy { w -> w.status })

                if (retroIdsGroupedList.containsKey("0")) {
                    activityPreviewBinding.pending.setText(retroIdsGroupedList.get("0")!!.size.toString())
                } else {
                    activityPreviewBinding.pending.setText("0")

                }
                if (retroIdsGroupedList.containsKey("1")) {
                    activityPreviewBinding.accept.setText(retroIdsGroupedList.get("1")!!.size.toString())
                } else {
                    activityPreviewBinding.accept.setText("0")

                }
                if (retroIdsGroupedList.containsKey("2")) {
                    activityPreviewBinding.reshoot.setText(retroIdsGroupedList.get("2")!!.size.toString())
                } else {
                    activityPreviewBinding.reshoot.setText("0")

                }

                if (imageUrlList != null) {
                    adapter = ApprovalCategoryListAdapter(this, imageUrlList, stage, this)
                    activityPreviewBinding.recyclerViewcategories.adapter = adapter
                }


            }

        }
    }
}
