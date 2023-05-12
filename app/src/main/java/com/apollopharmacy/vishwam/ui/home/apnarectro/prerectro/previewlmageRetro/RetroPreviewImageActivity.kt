package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter.RetroPreviewImage
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.previewlastimage.PreviewLastImageViewModel
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.RatingModelRequest
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import java.util.stream.Collectors

class RetroPreviewImageActivity : AppCompatActivity(), PreviewLastImageCallback,
    ViewPager.OnPageChangeListener {
    lateinit var activityPreviewImageBinding: ActivityPreviewRetroImageBinding
    private var previewImageAdapter: RetroPreviewImage? = null
    var list = ArrayList<String>()
    var categoryAndImageslist = ArrayList<String>()
    private lateinit var previewLastImageViewModel: ApnaPreviewLastImageViewModel
    private var currentPosition: Int = 0
    private var apiStatus: String = ""
    private var stage: String = ""
    private var apiStage: String = ""
    private var isApiHit: Boolean = false
    private var isRatingApiHit: Boolean = false
    var applist = ArrayList<GetImageUrlResponse.ImageUrl>()
    var reshootList = ArrayList<GetImageUrlResponse.ImageUrl>()
    var pendingList = ArrayList<GetImageUrlResponse.ImageUrl>()
    private var overallStatus: String? = null
    var isAllapproved: Boolean? = false
    public var imageUrlList = java.util.ArrayList<GetImageUrlResponse.Category>()
    private var store: String = ""
    private var retroId: String = ""
    private var uploaddate: String = ""
    private var uploadBy: String = ""
    private lateinit var dialog: Dialog
    var ratingbar: RatingBar? = null
    var ratingforsubmit: String? = null
    private var getImageUrlsResponse: GetImageUrlResponse? = null
    var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()
    var categorysList = ArrayList<GetImageUrlResponse.Category>()
    var saveRequestImageslist = java.util.ArrayList<SaveAcceptRequest.Imageurl>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewImageBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview_retro_image
        )
        activityPreviewImageBinding.callback = this
        previewLastImageViewModel =
            ViewModelProvider(this)[ApnaPreviewLastImageViewModel::class.java]

        if (intent != null) {

            getImageUrlsResponse =
                intent.getSerializableExtra("GET_IMAGE_URLS_RESPONSE") as GetImageUrlResponse?
            list =
                getIntent().getExtras()?.getStringArrayList("imageList")!!
            store = intent.getStringExtra("store")!!
            retroId = intent.getStringExtra("retroId")!!
            stage = intent.getStringExtra("stage")!!

            uploaddate = intent.getStringExtra("uploaddate")!!
            uploadBy = intent.getStringExtra("uploadby")!!
            imageUrlList =
                intent.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>
        }
        setUp()
    }

    private fun setUp() {

        activityPreviewImageBinding.stageName.setText(WordUtils.capitalizeFully(stage.replace("-",
            " ")) + " Review")

        activityPreviewImageBinding.storeId.setText(store)
        activityPreviewImageBinding.categoty.setText(imageUrlList.get(currentPosition).categoryname)
        activityPreviewImageBinding.uploadedDate.setText(uploaddate)
        if (stage.toLowerCase().contains("pre")) {
            apiStage = "1"

        } else if (stage.toLowerCase().contains("post")) {
            apiStage = "2"

        } else if (stage.toLowerCase().contains("aft")) {
            apiStage = "3"

        }

        for (i in imageUrlList.indices) {
            for (j in imageUrlList[i].imageUrls!!.indices) {
                val imageUrl = GetImageUrlResponse.ImageUrl()

                if (stage.toLowerCase()
                        .contains("pre") && imageUrlList[i].imageUrls!!.get(j).stage.equals("1")
                ) {
                    imageUrl.url = imageUrlList[i].imageUrls!!.get(j).url
                    imageUrl.status = imageUrlList[i].imageUrls!!.get(j).status
                    imageUrl.remarks = imageUrlList[i].imageUrls!!.get(j).remarks
                    imageUrl.stage = imageUrlList[i].imageUrls!!.get(j).stage
                    imageUrl.categoryid = imageUrlList[i].imageUrls!!.get(j).categoryid
                    imageUrl.imageid = imageUrlList[i].imageUrls!!.get(j).imageid
                    imageUrl.isVerified = imageUrlList[i].imageUrls!!.get(j).isVerified

                    imageUrl.retorautoid = imageUrlList[i].imageUrls!!.get(j).retorautoid
                    imageUrlsList.add(imageUrl)
                } else if (stage.toLowerCase()
                        .contains("pos") && imageUrlList[i].imageUrls!!.get(j).stage.equals("2")
                ) {
                    imageUrl.url = imageUrlList[i].imageUrls!!.get(j).url
                    imageUrl.status = imageUrlList[i].imageUrls!!.get(j).status
                    imageUrl.remarks = imageUrlList[i].imageUrls!!.get(j).remarks
                    imageUrl.stage = imageUrlList[i].imageUrls!!.get(j).stage
                    imageUrl.categoryid = imageUrlList[i].imageUrls!!.get(j).categoryid
                    imageUrl.imageid = imageUrlList[i].imageUrls!!.get(j).imageid
                    imageUrl.isVerified = imageUrlList[i].imageUrls!!.get(j).isVerified

                    imageUrl.retorautoid = imageUrlList[i].imageUrls!!.get(j).retorautoid
                    imageUrlsList.add(imageUrl)
                } else if (stage.toLowerCase()
                        .contains("aft") && imageUrlList[i].imageUrls!!.get(j).stage.equals("3")
                ) {
                    imageUrl.url = imageUrlList[i].imageUrls!!.get(j).url
                    imageUrl.status = imageUrlList[i].imageUrls!!.get(j).status
                    imageUrl.remarks = imageUrlList[i].imageUrls!!.get(j).remarks
                    imageUrl.stage = imageUrlList[i].imageUrls!!.get(j).stage
                    imageUrl.categoryid = imageUrlList[i].imageUrls!!.get(j).categoryid
                    imageUrl.imageid = imageUrlList[i].imageUrls!!.get(j).imageid
                    imageUrl.isVerified = imageUrlList[i].imageUrls!!.get(j).isVerified

                    imageUrl.retorautoid = imageUrlList[i].imageUrls!!.get(j).retorautoid
                    imageUrlsList.add(imageUrl)
                }

            }
        }
//


        if (imageUrlsList.get(currentPosition).isVerified == true) {
            if (imageUrlsList.get(currentPosition).status.equals("1")) {
                activityPreviewImageBinding.accept.alpha = 0.5f

            } else {
                activityPreviewImageBinding.accept.alpha = 1f

            }
            if (imageUrlsList.get(currentPosition).status.equals("2")) {
                activityPreviewImageBinding.reshoot.alpha = 0.5f

            } else {
                activityPreviewImageBinding.reshoot.alpha = 1f

            }
        }

        if (imageUrlsList.isNotEmpty()) {
            activityPreviewImageBinding.totalimages.setText("( " + (currentPosition + 1 / imageUrlsList.size + 1).toString() + "/" + imageUrlsList.size.toString() + " )")

        }
        previewImageAdapter = RetroPreviewImage(this, imageUrlsList, retroId, stage)

        activityPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityPreviewImageBinding.previewImageViewpager.adapter = previewImageAdapter
        activityPreviewImageBinding.previewImageViewpager.setCurrentItem(0, true)

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        for (i in imageUrlList.indices) {
            for (j in imageUrlList[i].imageUrls!!.indices) {
                if (imageUrlList[i].imageUrls!![j].imageid.equals(imageUrlsList.get(position).imageid)) {
                    activityPreviewImageBinding.categoty.setText(imageUrlList.get(i).categoryname)

                }

            }
        }


        currentPosition = position

        previewImageAdapter?.notifyDataSetChanged()


        applist = imageUrlsList.stream()
            .filter { imageUrlsList: GetImageUrlResponse.ImageUrl -> imageUrlsList.status.equals("1") }
            .collect(Collectors.toList()) as ArrayList<GetImageUrlResponse.ImageUrl>
        reshootList = imageUrlsList.stream()
            .filter { imageUrlsList: GetImageUrlResponse.ImageUrl -> imageUrlsList.status.equals("2") }
            .collect(Collectors.toList()) as ArrayList<GetImageUrlResponse.ImageUrl>
        pendingList = imageUrlsList.stream()
            .filter { imageUrlsList: GetImageUrlResponse.ImageUrl -> imageUrlsList.status.equals("0") }
            .collect(Collectors.toList()) as ArrayList<GetImageUrlResponse.ImageUrl>


        if (currentPosition == imageUrlsList.size - 1) {
            if (imageUrlsList[imageUrlsList.size - 1].isVerified == true) {

                if (applist.size == imageUrlsList.size || reshootList.size == imageUrlsList.size || applist.size + reshootList.size == imageUrlsList.size) {
                    activityPreviewImageBinding.isLastPos =
                        currentPosition == imageUrlsList.size - 1
                } else {
                    activityPreviewImageBinding.isLastPos = false
                }

            }

        } else {
            activityPreviewImageBinding.isLastPos = false
        }


        if (imageUrlsList.get(position).status.equals("1")) {
            activityPreviewImageBinding.accept.alpha = 0.5f

        } else {
            activityPreviewImageBinding.accept.alpha = 1f

        }
        if (imageUrlsList.get(position).status.equals("2")) {
            activityPreviewImageBinding.reshoot.alpha = 0.5f

        } else {
            activityPreviewImageBinding.reshoot.alpha = 1f

        }



        activityPreviewImageBinding.totalimages.setText("( " + (position + 1 / imageUrlsList.size + 1).toString() + "/" + imageUrlsList.size.toString() + " )")
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onClick(position: Int, status: String) {

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

                activityPreviewImageBinding.isAllComplete = isAllVerified


                if (currentPosition == imageUrlsList.size - 2) {
                    for (i in imageUrlsList) {
                        if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                            if (i.isVerified == false) {
                                activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
                                    imageUrlsList.indexOf(i), true
                                )
                                break
                            }
                        }
                    }
                    if (isAllVerified) {
                        activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
                            currentPosition + 1, true
                        )
                    }
                } else {
                    activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
                        currentPosition + 1, true
                    )
                }
            }
            else -> {
            }
        }



        if (currentPosition == imageUrlsList.size - 1) {
            if (imageUrlsList[imageUrlsList.size - 1].isVerified == true) {
                activityPreviewImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
            }

        } else {
            activityPreviewImageBinding.isLastPos = false
        }
        previewImageAdapter!!.notifyDataSetChanged()


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

                activityPreviewImageBinding.isAllComplete = isAllVerified


                if (currentPosition == imageUrlsList.size - 2) {
                    for (i in imageUrlsList) {
                        if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                            if (i.isVerified == false) {
                                activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
                                    imageUrlsList.indexOf(i), true
                                )
                                break
                            }
                        }
                    }
                    if (isAllVerified) {
                        activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
                            currentPosition + 1, true
                        )
                    }
                } else {
                    activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
                        currentPosition + 1, true
                    )
                }
            }
            else -> {
            }
        }



        if (currentPosition == imageUrlsList.size - 1) {
            if (imageUrlsList[imageUrlsList.size - 1].isVerified == true) {
                activityPreviewImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
            }

        } else {
            activityPreviewImageBinding.isLastPos = false
        }
        previewImageAdapter!!.notifyDataSetChanged()

    }

    override fun onClickCompleted() {

        for (i in imageUrlsList.indices) {
            var imageRequest = SaveAcceptRequest.Imageurl()
            imageRequest.statusid = imageUrlsList.get(i).status
            imageRequest.imageid = imageUrlsList.get(i).imageid
            saveRequestImageslist.add(imageRequest)

        }
        for (i in imageUrlList.indices) {
            for (j in imageUrlList[i].imageUrls!!.indices) {
                for (k in imageUrlsList.indices) {
                    if (imageUrlList[i].imageUrls!![j].imageid.equals(imageUrlsList.get(k).imageid)) {
                        if (imageUrlsList.get(k).isVerified == true && imageUrlsList.get(k).status.equals(
                                "1")
                        ) {

                            imageUrlList.get(i).imageUrls!!.get(j).status = "1"

                            imageUrlList.get(i).imageUrls!!.get(j).setisVerified(true)
                        } else if (imageUrlsList.get(k).isVerified == true && imageUrlsList.get(
                                k).status.equals("2")
                        ) {


                            imageUrlList.get(i).imageUrls!!.get(j).status = "2"
                            imageUrlList.get(i).imageUrls!!.get(j)
                                .setisVerified(true)
                        }
                    }

                }

            }
        }



        applist = imageUrlsList.stream()
            .filter { imageUrlsList: GetImageUrlResponse.ImageUrl -> imageUrlsList.status.equals("1") }
            .collect(Collectors.toList()) as ArrayList<GetImageUrlResponse.ImageUrl>
        reshootList = imageUrlsList.stream()
            .filter { imageUrlsList: GetImageUrlResponse.ImageUrl -> imageUrlsList.status.equals("2") }
            .collect(Collectors.toList()) as ArrayList<GetImageUrlResponse.ImageUrl>
        pendingList = imageUrlsList.stream()
            .filter { imageUrlsList: GetImageUrlResponse.ImageUrl -> imageUrlsList.status.equals("0") }
            .collect(Collectors.toList()) as ArrayList<GetImageUrlResponse.ImageUrl>


        if (pendingList.size == 0 && imageUrlsList.size == applist.size) {
            apiStatus = "1"
        } else if (pendingList.size == 0 && imageUrlsList.size == reshootList.size) {
            apiStatus = "2"
        } else if (pendingList.size == 0 && imageUrlsList.size == reshootList.size + applist.size) {
            apiStatus = "2"
        }


        var imageRequest = SaveAcceptRequest()
        imageRequest.retroautoid = retroId
        imageRequest.type = ""
        imageRequest.stageid = apiStage
        imageRequest.reamrks = ""
        imageRequest.statusid = apiStatus
        imageRequest.storeid = store
        imageRequest.userid = uploadBy
        imageRequest.rating = ""
        imageRequest.imageurls = saveRequestImageslist

        previewLastImageViewModel.saveAccepetAndReshoot(imageRequest, this)

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
            finish()
            imagesStatusAlertDialog.dismiss()
        }
        dialogLastimagePreviewAlertBinding.cancelButton.setOnClickListener {

            val intent = Intent()
            intent.putExtra("mainImagesList", imageUrlsList)
            setResult(Activity.RESULT_OK, intent)
            finish()
            imagesStatusAlertDialog.dismiss()

        }
        imagesStatusAlertDialog.show()

    }

    override fun onSuccessRatingResponse(value: SaveAcceptResponse) {
        val imagesStatusAlertDialog = Dialog(this)
        val dialogLastimagePreviewAlertBinding: DialogOkAlertBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialog_ok_alert, null, false
            )
        imagesStatusAlertDialog.setContentView(dialogLastimagePreviewAlertBinding.root)
//        imagesStatusAlertDialog.setCancelable(false)
        imagesStatusAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLastimagePreviewAlertBinding.messege.setText(retroId + "\n" + "Reviewed Sucessfully")

        dialogLastimagePreviewAlertBinding.alertTitle.setText("Apna " + WordUtils.capitalizeFully(
            stage.replace("-",
                " ")) + " Review")

        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {

            isRatingApiHit = true

            val intent = Intent()
            intent.putExtra("mainImagesList", imageUrlsList)
            intent.putExtra("ratingApi", isRatingApiHit)
            setResult(Activity.RESULT_OK, intent)
            finish()
            imagesStatusAlertDialog.dismiss()
        }

        imagesStatusAlertDialog.show()


    }

    override fun onFailureRatingResponse(value: SaveAcceptResponse) {
        Utlis.hideLoading()
        Toast.makeText(applicationContext, value.message, Toast.LENGTH_SHORT).show()
        val imagesStatusAlertDialog = Dialog(this)
        val dialogLastimagePreviewAlertBinding: DialogOkAlertBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialog_ok_alert, null, false
            )
        imagesStatusAlertDialog.setContentView(dialogLastimagePreviewAlertBinding.root)
//        imagesStatusAlertDialog.setCancelable(false)
        imagesStatusAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLastimagePreviewAlertBinding.messege.setText(retroId + "\n" + "Reviewed Sucessfully")

        dialogLastimagePreviewAlertBinding.alertTitle.setText("Apna " + WordUtils.capitalizeFully(
            stage.replace("-",
                " ")) + " Review")

        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {

            isRatingApiHit = true

            val intent = Intent()
            intent.putExtra("mainImagesList", imageUrlsList)
            intent.putExtra("isApiHit", isApiHit)
            setResult(Activity.RESULT_OK, intent)
            finish()
            imagesStatusAlertDialog.dismiss()
        }

        imagesStatusAlertDialog.show()
    }

    override fun onSuccessSaveAcceptReshoot(value: SaveAcceptResponse) {
        isApiHit = true
        if (applist.size == imageUrlsList.size) {


            dialog = Dialog(this)
            dialog.setContentView(R.layout.rating_review_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
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
                    var imageRequest = SaveAcceptRequest()
                    imageRequest.retroautoid = retroId
                    imageRequest.type = "REMARKS"
                    imageRequest.stageid = apiStage
                    imageRequest.reamrks = comments.text.toString()
                    imageRequest.statusid = apiStatus
                    imageRequest.storeid = store
                    imageRequest.userid = uploadBy
                    imageRequest.rating = ratingforsubmit.toString()

                    Utlis.showLoading(this)
                    previewLastImageViewModel.getRatingResponse(imageRequest,
                        this
                    )
                    dialog.dismiss()

                } else {
                    Toast.makeText(applicationContext,
                        "Please enter comments",
                        Toast.LENGTH_SHORT).show()
                }


            }
        } else {

            val imagesStatusAlertDialog = Dialog(this)
            val dialogLastimagePreviewAlertBinding: DialogOkAlertBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(this), R.layout.dialog_ok_alert, null, false
                )
            imagesStatusAlertDialog.setContentView(dialogLastimagePreviewAlertBinding.root)
//        imagesStatusAlertDialog.setCancelable(false)
            imagesStatusAlertDialog.getWindow()
                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogLastimagePreviewAlertBinding.messege.setText(retroId + "\n" + "Reviewed Sucessfully")

            dialogLastimagePreviewAlertBinding.alertTitle.setText("Apna " + WordUtils.capitalizeFully(
                stage.replace("-",
                    " ")) + " Review")

            dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {

                isRatingApiHit = true

                val intent = Intent()
                intent.putExtra("mainImagesList", imageUrlsList)
                intent.putExtra("isApiHit", isApiHit)
                setResult(Activity.RESULT_OK, intent)
                finish()
                imagesStatusAlertDialog.dismiss()
            }

            imagesStatusAlertDialog.show()


        }
    }


    override fun onFailureSaveAcceptReshoot(value: SaveAcceptResponse) {


        Toast.makeText(this, value.message, Toast.LENGTH_LONG).show()


    }

    override fun onClickDelete() {


        val imagesStatusAlertDialog = Dialog(this)
        val dialogLastimagePreviewAlertBinding: DialogDeletePreviewAlertBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialog_delete_preview_alert, null, false
            )
        imagesStatusAlertDialog.setContentView(dialogLastimagePreviewAlertBinding.root)
//        imagesStatusAlertDialog.setCancelable(false)
        imagesStatusAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {
            if (imageUrlsList.get(currentPosition).status.equals("1")) {
                activityPreviewImageBinding.accept.alpha = 1f
            } else if (imageUrlsList.get(currentPosition).status.equals("2")) {
                activityPreviewImageBinding.reshoot.alpha = 1f
            }
            when (imageUrlsList.size > currentPosition) {
                true -> {
                    imageUrlsList.get(currentPosition).status = "0"

                    imageUrlsList.get(currentPosition).isVerified = true
                    var isAllVerified = true
                    for (i in imageUrlsList) {
                        if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                            if (i.isVerified == false) {
                                isAllVerified = false
                            }
                        }
                    }

                    activityPreviewImageBinding.isAllComplete = isAllVerified
                }

                else -> {}
            }
            previewImageAdapter!!.notifyDataSetChanged()
            imagesStatusAlertDialog.dismiss()


//                if (currentPosition == imageUrlsList.size - 2) {
//                    for (i in imageUrlsList) {
//                        if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
//                            if (i.isVerified == false) {
//                                activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
//                                    imageUrlsList.indexOf(i), true
//                                )
//                                break
//                            }
//                        }
//                    }
//                    if (isAllVerified) {
//                        activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
//                            currentPosition + 1, true
//                        )
//                    }
//                } else {
//                    activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
//                        currentPosition + 1, true
//                    )
//                }
//            }
//            else -> {
//            }
        }


        dialogLastimagePreviewAlertBinding.cancelButton.setOnClickListener {
            imagesStatusAlertDialog.dismiss()
        }

        imagesStatusAlertDialog.show()

    }


    override fun onBackPressed() {
        onClickBack()
    }

}
