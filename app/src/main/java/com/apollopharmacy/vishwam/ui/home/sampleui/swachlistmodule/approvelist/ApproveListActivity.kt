package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityApproveListBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.adapter.ApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewImage.PreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage.PreviewLastImageActivity
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.util.Utlis

class ApproveListActivity : AppCompatActivity(), ApproveListcallback {
    private var pendingAndApproved: PendingAndApproved? = null
    private lateinit var activityApproveListBinding: ActivityApproveListBinding
    private lateinit var approveListViewModel: ApproveListViewModel
    private var approveListAdapter: ApproveListAdapter? = null
    private var getImageUrlsResponses = GetImageUrlsResponse()
    private var imageUrlsList = ArrayList<GetImageUrlsResponse.ImageUrl>()
    private var overallStatus: String? = null
    var ratingbar: RatingBar? = null
    var ratingforsubmit: String? = null
    private lateinit var dialog: Dialog
    var isApprovedAdapter:Boolean?=null
    var view: View? = null
    var isAllapproved:Boolean?=false
    val APPROVE_LIST_ACTIVITY = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApproveListBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_approve_list
        )
//        ratingbar = findViewById(R.id.ratingBar);
        approveListViewModel = ViewModelProvider(this)[ApproveListViewModel::class.java]
        setUp()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUp() {
        activityApproveListBinding.callback = this
        pendingAndApproved =
            intent.getSerializableExtra("PENDING_AND_APPROVED") as PendingAndApproved
        isApprovedAdapter =
            intent.getBooleanExtra("isApprovedAdapter", false) as Boolean

        if(isApprovedAdapter!!){
            activityApproveListBinding.startReviewButton.visibility = View.GONE
            activityApproveListBinding.submitRatingButton.visibility = View.VISIBLE
            activityApproveListBinding.viewformarginBottom.visibility=View.VISIBLE
        }



        when (pendingAndApproved != null) {
            true -> {
                activityApproveListBinding.model = pendingAndApproved
                Utlis.showLoading(this)
                approveListViewModel.getImageUrlsApiCall(pendingAndApproved!!)

            }
        }
        errorMessage()
        getImageUrlsApiResponse()
        saveAcceptandReshoot()
        getRatingResponse()


    }

    private fun getRatingResponse() {
        approveListViewModel.ratingBarResponse.observeForever {
            Utlis.hideLoading()
            if (it.message == "success") {
                Toast.makeText(this, "Review has been completed", Toast.LENGTH_SHORT)
                    .show()
                onBackPressed()
//              Toast.makeText(getApplicationContext(), it.message, Toast.LENGTH_LONG).show();
            }else{
//                Toast.makeText(getApplicationContext(), it.message, Toast.LENGTH_LONG).show();
            }

        }
    }

    private fun getImageUrlsApiResponse() {
        approveListViewModel.getImageUrlsResponse.observeForever { getImageUrlsResponse ->
            Utlis.hideLoading()
            getImageUrlsResponses = getImageUrlsResponse
            when (getImageUrlsResponse != null && getImageUrlsResponse.categoryList != null && getImageUrlsResponse.categoryList!!.size > 0) {
                true -> {
//                   getImageUrlsResponses.categoryList?.get(0)?.imageUrls?.get(0)?.status="0"
                    approveListAdapter =
                        ApproveListAdapter(this, getImageUrlsResponses.categoryList!!, this)
                    activityApproveListBinding.categoryListRecycler.layoutManager =
                        LinearLayoutManager(
                            this
                        )
                    activityApproveListBinding.categoryListRecycler.adapter =
                        approveListAdapter
                    report()

                    if (!pendingCountforValidation.equals(0) && !isApprovedAdapter!!) {
                        activityApproveListBinding.reshootButton.visibility = View.GONE
                        activityApproveListBinding.startReviewButton.visibility = View.VISIBLE
                        activityApproveListBinding.submitRatingButton.visibility=View.GONE
                        activityApproveListBinding.viewformarginBottom.visibility=View.VISIBLE
                    }
                }
            }
        }

    }

    private fun saveAcceptandReshoot() {
        approveListViewModel.saveAcceptAndReshootResponse.observeForever { saveAcceptAndReshootResponse ->
            Utlis.hideLoading()
            when (saveAcceptAndReshootResponse != null) {
                true -> {
                    when (saveAcceptAndReshootResponse.status == true) {
                        true -> {

                           if(isAllapproved!!){


                            dialog = Dialog(this)
                            dialog.setContentView(R.layout.rating_review_dialog)
                            val comments = dialog.findViewById<EditText>(R.id.comment)
                            val submitButton = dialog.findViewById<LinearLayout>(R.id.submitforreview)
                            val closeButton = dialog.findViewById<ImageView>(R.id.close_dialogRating)
                            ratingbar =  dialog.findViewById<RatingBar>(R.id.ratingBarDialog)


                            closeButton.setOnClickListener {
                                dialog.dismiss()
                            }
                            ratingforsubmit="4"
                            ratingbar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

                                ratingforsubmit = rating.toString().substring(0, 1)

//          Toast.makeText(applicationContext, "Rating: $rating + Remarks: ${submitRating.reamrks} ", Toast.LENGTH_SHORT).show()
                            }

                            submitButton.setOnClickListener {
                                if( comments.getText().toString()!=null &&  comments.getText().toString()!=""){
                                    var submitRating = RatingModelRequest()
                                    submitRating.type = "REMARKS"
                                    submitRating.swachhid = pendingAndApproved?.swachhid
                                    submitRating.storeid = pendingAndApproved?.storeId
                                    submitRating.statusid = "1"
                                    submitRating.reamrks = comments.getText().toString()
                                    submitRating.rating = ratingforsubmit.toString()
                                    submitRating.userid = Preferences.getValidatedEmpId()
                                    Utlis.showLoading(this)
                                    approveListViewModel.submitRatingBar(submitRating)
                                    dialog.dismiss()
                                    if(isApprovedAdapter!!){
                                        super.onBackPressed()
                                    }else{
                                        dialog.dismiss()
                                    }
                                }else{
                                    Toast.makeText(applicationContext, "Please enter comments", Toast.LENGTH_SHORT).show()
                                }

                            }


                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialog.show()

                        }else{
                               Toast.makeText(this, "Review has been completed", Toast.LENGTH_SHORT)
                                   .show()
                               onBackPressed()
                           }
                        }
                    }

                }
            }
        }
    }

    private fun errorMessage() {
        approveListViewModel.errorMessage.observeForever { errorMessage ->
            Utlis.hideLoading()
            if (errorMessage != null)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()

            }
        }
    }
        var pendingCountforValidation=0
    private fun report() {
        var pendingCount = 0
        var acceptedCount = 0
        var reShootCount = 0

        var isAccepted = true
        var isReShoot = true
        var isPending = true

        for (i in getImageUrlsResponses.categoryList!!) {
            for (j in i.imageUrls!!) {
                if (j.status.equals("0")) {
                    pendingCount++
                    isAccepted = false
                    isReShoot = false
                } else if (j.status.equals("1")) {
                    acceptedCount++
                    isReShoot = false
                    isPending = false
                } else if (j.status.equals("2")) {
                    reShootCount++
                    isAccepted = false
                    isPending = false
                }
            }
        }
        pendingCountforValidation = pendingCount
        activityApproveListBinding.pendingCount = "" + pendingCount
        activityApproveListBinding.acceptedCount = "" + acceptedCount
        activityApproveListBinding.reShootCount = "" + reShootCount




        if (isPending) {
            activityApproveListBinding.status = "0"
            overallStatus = "0"
            if(isApprovedAdapter!!){
                activityApproveListBinding.startReviewButton.visibility=View.GONE
                activityApproveListBinding.submitRatingButton.visibility=View.GONE
                activityApproveListBinding.viewformarginBottom.visibility=View.GONE
            }

        } else if (isAccepted) {
            activityApproveListBinding.status = "1"
            overallStatus = "1"
            isAllapproved =true
            if(isApprovedAdapter!!){
                activityApproveListBinding.startReviewButton.visibility=View.GONE
                activityApproveListBinding.submitRatingButton.visibility=View.VISIBLE
                activityApproveListBinding.viewformarginBottom.visibility=View.VISIBLE
            }

        } else if (isReShoot) {
            activityApproveListBinding.status = "2"
            overallStatus = "2"

            if(isApprovedAdapter!!){
                activityApproveListBinding.startReviewButton.visibility=View.GONE
                activityApproveListBinding.submitRatingButton.visibility=View.GONE
                activityApproveListBinding.viewformarginBottom.visibility=View.GONE
            }
//            activityApproveListBinding.submitRating.visibility=View.VISIBLE
        } else {
            activityApproveListBinding.status = "3"
            overallStatus = "3"
            if(isApprovedAdapter!!){
                activityApproveListBinding.submitRatingButton.visibility=View.GONE
                activityApproveListBinding.viewformarginBottom.visibility=View.GONE
                activityApproveListBinding.startReviewButton.visibility=View.GONE
            }

//            activityApproveListBinding.submitRating.visibility=View.VISIBLE
        }
    }
    var previewClicked: Boolean=false
    override fun onClickImage(
        position: Int,
        imagePath: String,
        viewClick: View,
        category: String,
        configPositionRes: Int,
    ) {
        previewClicked=true

//        PopUpWIndow(
//            ViswamApp.context, R.layout.layout_image_fullview, viewClick,
//            imagePath, null,category,position
//        )



        val intent = Intent(this, PreviewImageActivity::class.java)
        intent.putExtra("GET_IMAGE_URLS_RESPONSE", getImageUrlsResponses)
        intent.putExtra("GET_IMAGE_URLS_RESPONSE_LIST", getImageUrlsResponses.categoryList?.get(
            configPositionRes))
        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
        intent.putExtra("position", position)
        intent.putExtra("configPositionRes", configPositionRes)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//
    }

    override fun onClickSubmit() {
        val saveAcceptAndReshootRequest = SaveAcceptAndReshootRequest()
        saveAcceptAndReshootRequest.type=""
        saveAcceptAndReshootRequest.swachhid = pendingAndApproved!!.swachhid
        saveAcceptAndReshootRequest.storeid = pendingAndApproved!!.storeId
        saveAcceptAndReshootRequest.statusid = overallStatus
       saveAcceptAndReshootRequest.reamrks = ""
        saveAcceptAndReshootRequest.rating = ""
        saveAcceptAndReshootRequest.userid = Preferences.getValidatedEmpId()
        val imageUrlsList = ArrayList<SaveAcceptAndReshootRequest.Imageurl>()
        for (i in getImageUrlsResponses.categoryList!!) {
            for (j in i.imageUrls!!) {
                val imageUrl = SaveAcceptAndReshootRequest.Imageurl()
                imageUrl.imageid = j.imageid
                imageUrl.statusid = j.status
                imageUrl.remarks = ""
                imageUrlsList.add(imageUrl)
            }
        }

        saveAcceptAndReshootRequest.imageurls = imageUrlsList
        Utlis.showLoading(this)
        approveListViewModel.saveAccepetAndReshoot(saveAcceptAndReshootRequest)
    }

    override fun onClickBack() {
        onBackPressed()
    }

    override fun onClickStartReiew() {
        val intent = Intent(this, PreviewLastImageActivity::class.java)
        intent.putExtra("GET_IMAGE_URLS_RESPONSE", getImageUrlsResponses)
        intent.putExtra("PENDING_AND_APPROVED", pendingAndApproved)
        startActivityForResult(intent, PreviewLastImageActivity().PREVIEW_LAST_IMAGE_ACTIVITY)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onePendingStatus() {
//        activityApproveListBinding.commentsLayout.visibility = View.VISIBLE
       activityApproveListBinding.reshootButton.visibility = View.GONE
        activityApproveListBinding.startReviewButton.visibility = View.VISIBLE
        activityApproveListBinding.viewformarginBottom.visibility=View.VISIBLE
//        activityApproveListBinding.ratingBarLayout.visibility=View.VISIBLE
    }

    override fun onClickSubmitRatingReview() {
//        var submitRating = RatingModelRequest()
//        submitRating.type = "REMARKS"
//        submitRating.swachhid = pendingAndApproved?.swachhid
//        submitRating.storeid = Preferences.getSiteId()
//        submitRating.statusid = "1"
//        submitRating.reamrks = activityApproveListBinding.comment.getText().toString()
//        submitRating.rating = ratingforsubmit.toString()
//        submitRating.userid = Preferences.getValidatedEmpId()
//        Utlis.showLoading(this)
//        approveListViewModel.submitRatingBar(submitRating)
//        Toast.makeText(applicationContext, "Rating: $ratingforsubmit + Remarks: ${submitRating.reamrks} ", Toast.LENGTH_SHORT).show()
    }

    override fun onClickSubmitRatingButton() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.rating_review_dialog)
       val comments = dialog.findViewById<EditText>(R.id.comment)
        val submitButton = dialog.findViewById<LinearLayout>(R.id.submitforreview)
        val closeButton = dialog.findViewById<ImageView>(R.id.close_dialogRating)
        ratingbar =  dialog.findViewById<RatingBar>(R.id.ratingBarDialog)


        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        ratingforsubmit="4"
        ratingbar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            ratingforsubmit = rating.toString().substring(0, 1)

//          Toast.makeText(applicationContext, "Rating: $rating + Remarks: ${submitRating.reamrks} ", Toast.LENGTH_SHORT).show()
        }

        submitButton.setOnClickListener {
            if( comments.getText().toString()!=null &&  comments.getText().toString()!=""){
                var submitRating = RatingModelRequest()
                submitRating.type = "REMARKS"
                submitRating.swachhid = pendingAndApproved?.swachhid
                submitRating.storeid = pendingAndApproved?.storeId
                submitRating.statusid = "1"
                submitRating.reamrks = comments.getText().toString()
                submitRating.rating = ratingforsubmit.toString()
                submitRating.userid = Preferences.getValidatedEmpId()
                Utlis.showLoading(this)
                approveListViewModel.submitRatingBar(submitRating)
                dialog.dismiss()
                if(isApprovedAdapter!!){
                    super.onBackPressed()
                }else{
                    dialog.dismiss()
                }
            }else{
                Toast.makeText(applicationContext, "Please enter comments", Toast.LENGTH_SHORT).show()
            }

        }


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PreviewLastImageActivity().PREVIEW_LAST_IMAGE_ACTIVITY) {
                imageUrlsList =
                    data?.getSerializableExtra("IMAGE_URLS_OBJECT") as ArrayList<GetImageUrlsResponse.ImageUrl>

                val categoryList = ArrayList<GetImageUrlsResponse.Category>()
                for (i in imageUrlsList) {
                    var isAlreadyExist = -1
                    if (categoryList.size > 0) {
                        for (j in categoryList) {
                            if (j.categoryid!!.equals(i.mainCategoryId)) {
                                isAlreadyExist = categoryList.indexOf(j)
                                break
                            }
                        }
                    }
                    if (isAlreadyExist != -1) {
                        categoryList.get(isAlreadyExist).imageUrls!!.add(i)
                    } else {
                        val category = GetImageUrlsResponse.Category()
                        category.categoryid = i.mainCategoryId
                        category.categoryname = i.categoryname
                        val imageUrlListss = ArrayList<GetImageUrlsResponse.ImageUrl>()
                        imageUrlListss.add(i)
                        category.imageUrls = imageUrlListss
                        categoryList.add(category)
                    }
                }
                getImageUrlsResponses.categoryList = categoryList

                approveListAdapter =
                    ApproveListAdapter(this, getImageUrlsResponses.categoryList!!, this)
                activityApproveListBinding.categoryListRecycler.layoutManager =
                    LinearLayoutManager(
                        this
                    )
                activityApproveListBinding.categoryListRecycler.adapter =
                    approveListAdapter
                report()

                if(!isApprovedAdapter!! && pendingCountforValidation.equals(0)){
                    activityApproveListBinding.reshootButton.visibility = View.VISIBLE
                    activityApproveListBinding.startReviewButton.visibility = View.GONE
                    activityApproveListBinding.viewformarginBottom.visibility=View.VISIBLE

                }else{
                    activityApproveListBinding.reshootButton.visibility = View.GONE
                    activityApproveListBinding.startReviewButton.visibility = View.VISIBLE
                    activityApproveListBinding.viewformarginBottom.visibility=View.VISIBLE

                }

            }
        }
    }
}