package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityPreviewLastImageBinding
import com.apollopharmacy.vishwam.databinding.DialogLastimagePreviewAlertBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.SaveAcceptAndReshootRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage.adapter.PreviewImageViewPager
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.util.Utlis

class PreviewLastImageActivity : AppCompatActivity(), PreviewLastImageCallback,
    ViewPager.OnPageChangeListener {
    lateinit var activityPreviewLastImageBinding: ActivityPreviewLastImageBinding
    private var pendingAndApproved: PendingAndApproved? = null
    private var getImageUrlsResponse: GetImageUrlsResponse? = null
    private var previewImageViewPager: PreviewImageViewPager? = null
    private var imageUrlsList = ArrayList<GetImageUrlsResponse.ImageUrl>()
    private lateinit var previewLastImageViewModel: PreviewLastImageViewModel
    private var isApiHit: Boolean = false
    private var currentPosition: Int = 0
    var ratingbar: RatingBar? = null
    var ratingforsubmit: String? = null
    private lateinit var dialog: Dialog
    private var overallStatus: String? = null
    var isAllapproved:Boolean?=false

    val PREVIEW_LAST_IMAGE_ACTIVITY: Int = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewLastImageBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview_last_image
        )
        previewLastImageViewModel = ViewModelProvider(this)[PreviewLastImageViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        activityPreviewLastImageBinding.callback = this
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
                imageUrl.isVerified = (j.status=="1"|| j.status=="2")
                imageUrl.categoryname = i.categoryname
                imageUrl.mainCategoryId = i.categoryid
                imageUrlsList.add(imageUrl)
            }
        }
        val imageUrl = GetImageUrlsResponse.ImageUrl()
        imageUrl.url = ""
        imageUrlsList.add(imageUrl)

        activityPreviewLastImageBinding.pendingAndApprovedModel = pendingAndApproved
        activityPreviewLastImageBinding.imageUrlModel = imageUrlsList.get(0)
        activityPreviewLastImageBinding.totalImages = "1/${imageUrlsList.size - 1}"

        if (imageUrlsList.get(0).status.equals("1")) {
            activityPreviewLastImageBinding.actionStatus = "1"
        } else if (imageUrlsList.get(0).status.equals("2")) {
            activityPreviewLastImageBinding.actionStatus = "2"
        } else {
            activityPreviewLastImageBinding.actionStatus = "0"
        }

        var isAllVerified = true
        for (i in imageUrlsList) {
            if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1)
                if (i.isVerified == false) {
                    isAllVerified = false
                }
        }
        activityPreviewLastImageBinding.isAllComplete = isAllVerified

        previewImageViewPager = PreviewImageViewPager(
            this, imageUrlsList, pendingAndApproved!!.swachhid
        )

        activityPreviewLastImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityPreviewLastImageBinding.previewImageViewpager.adapter = previewImageViewPager
        activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(0, true)
        saveAcceptandReshoot()
        getRatingResponse()

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        Toast.makeText(this, "onPageScrolled", Toast.LENGTH_SHORT).show()
    }

    override fun onPageSelected(position: Int) {
        if (position == imageUrlsList.size - 1) {
            var isAllVerified = true
            for (i in imageUrlsList) {
                if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                    if (i.isVerified == false) {
                        isAllVerified = false
                        activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(
                            imageUrlsList.indexOf(i), true
                        )
                        break
                    }
                }
            }
            if (isAllVerified) {
                currentPosition = position
                activityPreviewLastImageBinding.imageUrlModel = imageUrlsList.get(position)
                activityPreviewLastImageBinding.totalImages =
                    "${position + 1}/${imageUrlsList.size - 1}"
                previewImageViewPager?.notifyDataSetChanged()
            }
            activityPreviewLastImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
        } else {
            currentPosition = position
            if (currentPosition != imageUrlsList.size - 1) {
                activityPreviewLastImageBinding.imageUrlModel = imageUrlsList.get(position)
                activityPreviewLastImageBinding.totalImages =
                    "${position + 1}/${imageUrlsList.size - 1}"
                if (imageUrlsList.get(position).status.equals("1")) {
                    activityPreviewLastImageBinding.actionStatus = "1"
                } else if (imageUrlsList.get(position).status.equals("2")) {
                    activityPreviewLastImageBinding.actionStatus = "2"
                } else {
                    activityPreviewLastImageBinding.actionStatus = "0"
                }
            }
            activityPreviewLastImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
        }

    }

    override fun onPageScrollStateChanged(state: Int) {
//        Toast.makeText(this, "onPageScrollStateChanged", Toast.LENGTH_SHORT).show()
    }

    var pendingCountforValidation = 0
    private fun report() {
        var pendingCount = 0
        var acceptedCount = 0
        var reShootCount = 0

        var isAccepted = true
        var isReShoot = true
        var isPending = true

//        for (i in getImageUrlsResponse!!.categoryList!!) {
            for (j in imageUrlsList!!) {
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
//        }
        pendingCountforValidation = pendingCount

        if (isPending) {

            overallStatus = "0"


        } else if (isAccepted) {

            overallStatus = "1"
            isAllapproved =true

        } else if (isReShoot) {

            overallStatus = "2"

//            activityApproveListBinding.submitRating.visibility=View.VISIBLE
        } else {

            overallStatus = "3"


//            activityApproveListBinding.submitRating.visibility=View.VISIBLE
        }
    }


    override fun onClickReShoot() {
        when (imageUrlsList.size > currentPosition) {
            true -> {
                imageUrlsList.get(currentPosition).status = "2"
                imageUrlsList.get(currentPosition).isVerified = true
                var isAllVerified = true
                for (i in imageUrlsList) {
                    if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                        if (i.isVerified == false) {
                            isAllVerified = false
                        }
                    }
                }
                activityPreviewLastImageBinding.isAllComplete = isAllVerified
                if (currentPosition == imageUrlsList.size - 2) {
                    for (i in imageUrlsList) {
                        if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                            if (i.isVerified == false) {
                                activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(
                                    imageUrlsList.indexOf(i), true
                                )
                                break
                            }
                        }
                    }
                    if (isAllVerified) {
                        activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(
                            currentPosition + 1, true
                        )
                    }
                } else {
                    activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(
                        currentPosition + 1, true
                    )
                }
            }
        }
    }

    override fun onClickAccept() {
        when (imageUrlsList.size > currentPosition) {
            true -> {
                imageUrlsList.get(currentPosition).status = "1"
                imageUrlsList.get(currentPosition).isVerified = true
                var isAllVerified = true
                for (i in imageUrlsList) {
                    if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                        if (i.isVerified == false) {
                            isAllVerified = false
                        }
                    }
                }

                activityPreviewLastImageBinding.isAllComplete = isAllVerified


                if (currentPosition == imageUrlsList.size - 2) {
                    for (i in imageUrlsList) {
                        if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                            if (i.isVerified == false) {
                                activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(
                                    imageUrlsList.indexOf(i), true
                                )
                                break
                            }
                        }
                    }
                    if (isAllVerified) {
                        activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(
                            currentPosition + 1, true
                        )
                    }
                } else {
                    activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(
                        currentPosition + 1, true
                    )
                }
            }
        }

    }

    private fun saveAcceptandReshoot() {
        previewLastImageViewModel.saveAcceptAndReshootResponse.observeForever { saveAcceptAndReshootResponse ->
            report()
            Utlis.hideLoading()

            when (saveAcceptAndReshootResponse != null) {
                true -> {
                    when (saveAcceptAndReshootResponse.status == true) {

                        true -> {
                            isApiHit = true
                            if (isAllapproved!!) {


                                dialog = Dialog(this)
                                dialog.setContentView(R.layout.rating_review_dialog)
                                val comments = dialog.findViewById<EditText>(R.id.comment)
                                val submitButton =
                                    dialog.findViewById<LinearLayout>(R.id.submitforreview)
                                val closeButton =
                                    dialog.findViewById<ImageView>(R.id.close_dialogRating)
                                ratingbar = dialog.findViewById<RatingBar>(R.id.ratingBarDialog)


                                closeButton.setOnClickListener {
                                    dialog.dismiss()
                                }
                                ratingforsubmit = "4"
                                ratingbar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

                                    ratingforsubmit = rating.toString().substring(0, 1)

//          Toast.makeText(applicationContext, "Rating: $rating + Remarks: ${submitRating.reamrks} ", Toast.LENGTH_SHORT).show()
                                }

                                submitButton.setOnClickListener {
                                    if (comments.getText().toString() != null && comments.getText()
                                            .toString().trim() != ""
                                    ) {
                                        var submitRating = RatingModelRequest()
                                        submitRating.type = "REMARKS"
                                        submitRating.swachhid = pendingAndApproved?.swachhid
                                        submitRating.storeid = pendingAndApproved?.storeId
                                        submitRating.statusid = "1"
                                        submitRating.reamrks = comments.getText().toString()
                                        submitRating.rating = ratingforsubmit.toString()
                                        submitRating.userid = Preferences.getValidatedEmpId()
                                        Utlis.showLoading(this)
                                        previewLastImageViewModel.submitRatingBar(submitRating)
                                        dialog.dismiss()

                                    } else {
                                        Toast.makeText(applicationContext,
                                            "Please enter comments",
                                            Toast.LENGTH_SHORT).show()
                                    }

                                }


                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                dialog.show()

                            } else {
                                Toast.makeText(this,
                                    "Review has been completed",
                                    Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent()
                                imageUrlsList.removeAt(imageUrlsList.size - 1)
                                intent.putExtra("IMAGE_URLS_OBJECT", imageUrlsList)
                                intent.putExtra("isApiHit", isApiHit)
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }
                        }
                    }

                }
            }
        }
    }

    private fun getRatingResponse() {
        previewLastImageViewModel.ratingBarResponse.observeForever {
            Utlis.hideLoading()
            if (it.message == "success") {
                isApiHit = true
                Toast.makeText(this, "Review has been completed", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent()
                imageUrlsList.removeAt(imageUrlsList.size - 1)
                intent.putExtra("IMAGE_URLS_OBJECT", imageUrlsList)
                intent.putExtra("isApiHit", isApiHit)
                setResult(Activity.RESULT_OK, intent)
                finish()
//              Toast.makeText(getApplicationContext(), it.message, Toast.LENGTH_LONG).show();
            } else if (it.message == "RATINGS ALREADY SUBMITTED") {
                isApiHit = true
                Toast.makeText(getApplicationContext(),
                    "Rating is already submitted !",
                    Toast.LENGTH_LONG).show();
                val intent = Intent()
                imageUrlsList.removeAt(imageUrlsList.size - 1)
                intent.putExtra("IMAGE_URLS_OBJECT", imageUrlsList)
                intent.putExtra("isApiHit", isApiHit)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        }
    }

    override fun onClickCompleted() {
//        val intent = Intent()
//        for (i in imageUrlsList){
//            if (i.url.equals("DUMMY")){
//                imageUrlsList.remove(i)
//
//            }
//        }

        report()

        val saveAcceptAndReshootRequest = SaveAcceptAndReshootRequest()
        saveAcceptAndReshootRequest.type = ""
        saveAcceptAndReshootRequest.swachhid = pendingAndApproved!!.swachhid
        saveAcceptAndReshootRequest.storeid = pendingAndApproved!!.storeId
        saveAcceptAndReshootRequest.statusid = overallStatus
        saveAcceptAndReshootRequest.reamrks = ""
        saveAcceptAndReshootRequest.rating = ""
        saveAcceptAndReshootRequest.userid = Preferences.getValidatedEmpId()
        val imageUrlsLists = ArrayList<SaveAcceptAndReshootRequest.Imageurl>()
//        for (i in getImageUrlsResponse!!.categoryList!!) {
            for (j in imageUrlsList!!) {
                val imageUrl = SaveAcceptAndReshootRequest.Imageurl()
                imageUrl.imageid = j.imageid
                imageUrl.statusid = j.status
                imageUrl.remarks = ""
                imageUrlsLists.add(imageUrl)
            }
//        }

        saveAcceptAndReshootRequest.imageurls = imageUrlsLists
        Utlis.showLoading(this)
        previewLastImageViewModel.saveAccepetAndReshoot(saveAcceptAndReshootRequest)

//        imageUrlsList.removeAt(imageUrlsList.size - 1)
//        intent.putExtra("IMAGE_URLS_OBJECT", imageUrlsList)
//        intent.putExtra("isApiHit", isApiHit)
//        setResult(Activity.RESULT_OK, intent)
//        finish()
    }

    override fun onClickBack() {
        val imagesStatusAlertDialog = Dialog(this)
        val dialogLastimagePreviewAlertBinding: DialogLastimagePreviewAlertBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialog_lastimage_preview_alert, null, false
            )
        imagesStatusAlertDialog.setContentView(dialogLastimagePreviewAlertBinding.root)
//        imagesStatusAlertDialog.setCancelable(false)
        imagesStatusAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {
            imagesStatusAlertDialog.dismiss()
            finish()
        }
        dialogLastimagePreviewAlertBinding.cancelButton.setOnClickListener {
            imagesStatusAlertDialog.dismiss()
//            onClickCompleted()
            val intent = Intent()
            imageUrlsList.removeAt(imageUrlsList.size - 1)
            intent.putExtra("IMAGE_URLS_OBJECT", imageUrlsList)
            intent.putExtra("isApiHit", isApiHit)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        dialogLastimagePreviewAlertBinding.close.setOnClickListener {
            imagesStatusAlertDialog.dismiss()
        }
        imagesStatusAlertDialog.show()


//        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
//            onBackPressed()
//
//        }
//        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
//            dialog.dismiss()
//
//        }
//        val builder = AlertDialog.Builder(this)
//        with(builder) {
//            setMessage("Do you want to Save Preview Images")
//            setPositiveButton("ok", positiveButtonClick)
//            setNegativeButton("Cancel", negativeButtonClick)
//            show()
//        }

    }

    override fun onBackPressed() {
        onClickBack()
    }
}