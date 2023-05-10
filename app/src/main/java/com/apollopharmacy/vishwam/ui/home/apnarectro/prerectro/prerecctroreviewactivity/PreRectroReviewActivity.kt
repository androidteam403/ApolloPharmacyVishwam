package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro.ApnaPreviewLastImageViewModel
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro.PreviewLastImageCallback
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import org.apache.commons.lang3.text.WordUtils

class PreRectroReviewActivity : AppCompatActivity(), PreviewLastImageCallback {
    var stage: String = ""
    private var retroId: String = ""
    private var store: String = ""
    var categoryName: String = ""
    var categorypos: Int = 0
    private var uploadBy: String = ""
    private var statusId: String = ""
    private var isApiHit: Boolean = false
    private lateinit var dialog: Dialog
    var ratingbar: RatingBar? = null
    private var isRatingApiHit: Boolean = false
    var ratingforsubmit: String? = null

    private var status: String = ""
    var url: String = ""
    public var imageUrlList = java.util.ArrayList<GetImageUrlResponse.Category>()
    var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()
    var saveRequestImageslist = java.util.ArrayList<SaveAcceptRequest.Imageurl>()
    private lateinit var previewLastImageViewModel: PreviewPreRetroViewModel

    var pos: Int = 0
    lateinit var activityPreRectroReviewScreenBinding: ActivityPreRectroReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPreRectroReviewScreenBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_pre_rectro_review
        )
        previewLastImageViewModel =
            ViewModelProvider(this)[PreviewPreRetroViewModel::class.java]

        if (intent != null) {
            retroId = intent.getStringExtra("retroId")!!
            status = intent.getStringExtra("status")!!
            stage = intent.getStringExtra("stage")!!
            uploadBy = intent.getStringExtra("uploadby")!!

            url = intent.getStringExtra("url")!!
            imageUrlList =
                intent.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>

            pos = intent.getIntExtra("position", 0)!!
            Log.e("position", pos.toString())

            categoryName = intent.getStringExtra("categoryName")!!
            categorypos = intent.getIntExtra("categoryPos", 0)!!
            store = intent.getStringExtra("store")!!
        }

        setUp()

    }

    private fun setUp() {

        activityPreRectroReviewScreenBinding.callback = this


        if(status.equals("Pending")){
            activityPreRectroReviewScreenBinding.totalImageandaction.visibility=View.VISIBLE
        }else{
            activityPreRectroReviewScreenBinding.totalImageandaction.visibility=View.GONE

        }

        activityPreRectroReviewScreenBinding.isLastPos =
            imageUrlsList.filter { it.isVerified == true }.size == imageUrlsList.size
        for (j in imageUrlList.indices) {
            if (imageUrlList.get(j).categoryname.equals(categoryName)) {
                if (imageUrlList.get(j).imageUrls!!.get(pos).isVerified!! && imageUrlList.get(j).imageUrls!!.get(
                        pos).status.equals("1")
                ) {

                    activityPreRectroReviewScreenBinding.accept.alpha = 0.5f

                } else {
                    activityPreRectroReviewScreenBinding.accept.alpha = 1f

                }
                if (imageUrlList.get(j).imageUrls!!.get(pos).isVerified!!&&imageUrlList.get(j).imageUrls!!.get(pos).status.equals("2")) {

                    activityPreRectroReviewScreenBinding.reshoot.alpha = 0.5f

                } else {
                    activityPreRectroReviewScreenBinding.reshoot.alpha = 1f

                }

            }
        }
        for (k in imageUrlList.indices) {
            for (l in imageUrlList.get(k).imageUrls!!.indices) {
                var imageUrlResponse = GetImageUrlResponse.ImageUrl()
                imageUrlResponse.isVerified = imageUrlList.get(k).imageUrls!!.get(l).isVerified
                imageUrlResponse.status = imageUrlList.get(k).imageUrls!!.get(l).status
                imageUrlResponse.retorautoid= imageUrlList.get(k).imageUrls!!.get(l).retorautoid
                imageUrlResponse.imageid= imageUrlList.get(k).imageUrls!!.get(l).imageid
                imageUrlResponse.categoryid= imageUrlList.get(k).imageUrls!!.get(l).categoryid

                imageUrlsList.add(imageUrlResponse)

            }


        }



        if (imageUrlsList.filter { it.status.equals("1") }.size==imageUrlsList.size){
            statusId="1"
        }
        else if (imageUrlsList.filter { it.status.equals("2") }.size==imageUrlsList.size) {
            statusId="2"
        }
        else if ((imageUrlsList.filter { it.status.equals("1") }.size + imageUrlsList.filter { it.status.equals("2") }.size)==imageUrlsList.size) {
            statusId="2"
        }

            activityPreRectroReviewScreenBinding.setRetroId(retroId)

        if (imageUrlsList != null) {
            activityPreRectroReviewScreenBinding.isLastPos =
                imageUrlsList.filter { it.isVerified == true }.size == imageUrlsList.size

            activityPreRectroReviewScreenBinding.totalImages = imageUrlsList.size.toString()
            activityPreRectroReviewScreenBinding.accepted =
                imageUrlsList.filter { it.status.equals("1") }.size.toString()
            activityPreRectroReviewScreenBinding.rejected =
                imageUrlsList.filter { it.status.equals("2") }.size.toString()

        }

        activityPreRectroReviewScreenBinding.categoryNumber.setText((categorypos + 1).toString())
        activityPreRectroReviewScreenBinding.categoryName.setText(categoryName)
        activityPreRectroReviewScreenBinding.status.setText(status)
        if (status.toLowerCase().contains("pen")) {
            activityPreRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        } else if (status.toLowerCase().contains("app")) {
            activityPreRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#39B54A"))
        } else if (status.toLowerCase().contains("res")) {
            activityPreRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        }
        activityPreRectroReviewScreenBinding.stage.setText(WordUtils.capitalizeFully(stage.replace(
            "-",
            " ")) + " Review")
        activityPreRectroReviewScreenBinding.retroId.setText(retroId)
        activityPreRectroReviewScreenBinding.store.setText(store)



        Glide.with(this).load(url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPreRectroReviewScreenBinding.image)


    }

    override fun onClick(position: Int, status: String) {
    }

    override fun onClickReShoot() {
        for (i in imageUrlList.indices) {
            for (j in imageUrlsList.indices){
                if (imageUrlList.get(i).categoryname.equals(categoryName)) {
                    imageUrlList.get(i).imageUrls!!.get(pos).setisVerified(true)
                    imageUrlList.get(i).imageUrls!!.get(pos).status = "2"
                    if (imageUrlList.get(i).imageUrls!!.get(pos).imageid.equals(imageUrlsList.get(j).imageid)){
                        imageUrlsList.get(j).setisVerified(true)
                        imageUrlsList.get(j).status="2"
                    }


                }

            }
        }




        activityPreRectroReviewScreenBinding.isLastPos =
            imageUrlsList.filter { it.isVerified == true }.size == imageUrlsList.size


        activityPreRectroReviewScreenBinding.totalImages = imageUrlsList.size.toString()
        activityPreRectroReviewScreenBinding.accepted =
            imageUrlsList.filter { it.status.equals("1") }.size.toString()
        activityPreRectroReviewScreenBinding.rejected =
            imageUrlsList.filter { it.status.equals("2") }.size.toString()
        for (j in imageUrlList.indices) {
            if (imageUrlList.get(j).categoryname.equals(categoryName)) {
                if (imageUrlList.get(j).imageUrls!!.get(pos).isVerified == true&&imageUrlList.get(j).imageUrls!!.get(pos).status.equals("2")) {



                    activityPreRectroReviewScreenBinding.reshoot.alpha = 0.5f
                } else {
                    activityPreRectroReviewScreenBinding.reshoot.alpha = 1f

                }
            }
        }

    }

    override fun onClickAccept() {

        for (i in imageUrlList.indices) {
            for (j in imageUrlsList.indices){
            if (imageUrlList.get(i).categoryname.equals(categoryName)) {
                imageUrlList.get(i).imageUrls!!.get(pos).setisVerified(true)
                imageUrlList.get(i).imageUrls!!.get(pos).status = "1"
                if (imageUrlList.get(i).imageUrls!!.get(pos).imageid.equals(imageUrlsList.get(j).imageid)){
                    imageUrlsList.get(j).setisVerified(true)
                    imageUrlsList.get(j).status="1"
                }


                }

            }
        }




        activityPreRectroReviewScreenBinding.isLastPos =
            imageUrlsList.filter { it.isVerified == true }.size == imageUrlsList.size


        activityPreRectroReviewScreenBinding.totalImages = imageUrlsList.size.toString()
        activityPreRectroReviewScreenBinding.accepted =
            imageUrlsList.filter { it.status.equals("1") }.size.toString()
        activityPreRectroReviewScreenBinding.rejected =
            imageUrlsList.filter { it.status.equals("2") }.size.toString()
        for (j in imageUrlList.indices) {
            if (imageUrlList.get(j).categoryname.equals(categoryName)) {
                if (imageUrlList.get(j).imageUrls!!.get(pos).isVerified == true&&imageUrlList.get(j).imageUrls!!.get(pos).status.equals("1")) {



                    activityPreRectroReviewScreenBinding.accept.alpha = 0.5f
                } else {
                    activityPreRectroReviewScreenBinding.accept.alpha = 1f

                }
            }
        }

    }

    override fun onClickCompleted() {
        for (i in imageUrlsList.indices) {
            var imageRequest = SaveAcceptRequest.Imageurl()
            imageRequest.statusid = imageUrlsList.get(i).status
            imageRequest.imageid = imageUrlsList.get(i).imageid
            saveRequestImageslist.add(imageRequest)

        }
        if (imageUrlsList.filter { it.status.equals("1") }.size==imageUrlsList.size){
            statusId="1"
        }
        else if (imageUrlsList.filter { it.status.equals("2") }.size==imageUrlsList.size) {
            statusId="2"
        }
        else if ((imageUrlsList.filter { it.status.equals("1") }.size + imageUrlsList.filter { it.status.equals("2") }.size)==imageUrlsList.size) {
            statusId="2"
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
        var imageRequest = SaveAcceptRequest()
        imageRequest.retroautoid = retroId
        imageRequest.type = ""
        imageRequest.stageid = "1"
        imageRequest.reamrks = ""
        imageRequest.statusid = statusId
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
        imagesStatusAlertDialog.getWindow()
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {
            finish()
            imagesStatusAlertDialog.dismiss()
        }
        dialogLastimagePreviewAlertBinding.cancelButton.setOnClickListener {

            val intent = Intent()
            intent.putExtra("imageUrlList", imageUrlList)
            intent.putExtra("imagesList",imageUrlsList)
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
        dialogLastimagePreviewAlertBinding.messege.setText(retroId+"\n"+"Reviewed Sucessfully")

        dialogLastimagePreviewAlertBinding.alertTitle.setText("Apna "+WordUtils.capitalizeFully(stage.replace("-",
            " ")) + " Review")

        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {

            isRatingApiHit = true

            val intent = Intent()
            intent.putExtra("imagesList", imageUrlsList)
            intent.putExtra("imageUrlList",imageUrlList)
            intent.putExtra("isApiHit", isApiHit)
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
        dialogLastimagePreviewAlertBinding.messege.setText(retroId+"\n"+"Reviewed Sucessfully")

        dialogLastimagePreviewAlertBinding.alertTitle.setText("Apna "+WordUtils.capitalizeFully(stage.replace("-",
            " ")) + " Review")

        dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {

            isRatingApiHit = true

            val intent = Intent()
            intent.putExtra("isApiHit", isApiHit)
            intent.putExtra("imagesList", imageUrlsList)
            intent.putExtra("imageUrlList",imageUrlList)
            intent.putExtra("ratingApi", isRatingApiHit)

            setResult(Activity.RESULT_OK, intent)
            finish()
            imagesStatusAlertDialog.dismiss()
        }

        imagesStatusAlertDialog.show()
    }

    override fun onSuccessSaveAcceptReshoot(value: SaveAcceptResponse) {
        isApiHit = true
        if (imageUrlsList.filter { it.status.equals("1") }.size == imageUrlsList.size) {


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
                    imageRequest.stageid = "1"
                    imageRequest.reamrks = comments.text.toString()
                    imageRequest.statusid = statusId
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
            imagesStatusAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogLastimagePreviewAlertBinding.messege.setText(retroId+"\n"+"Reviewed Sucessfully")

            dialogLastimagePreviewAlertBinding.alertTitle.setText("Apna "+WordUtils.capitalizeFully(stage.replace("-",
                " ")) + " Review")

            dialogLastimagePreviewAlertBinding.yesBtn.setOnClickListener {

                isRatingApiHit = true

                val intent = Intent()
                intent.putExtra("imagesList", imageUrlsList)
                intent.putExtra("imageUrlList",imageUrlList)
                intent.putExtra("isApiHit", isApiHit)
                intent.putExtra("ratingApi", isRatingApiHit)

                setResult(Activity.RESULT_OK, intent)
                finish()
                imagesStatusAlertDialog.dismiss()
            }

            imagesStatusAlertDialog.show()




        }
    }

    override fun onFailureSaveAcceptReshoot(value: SaveAcceptResponse) {
        Toast.makeText(this, value.message,Toast.LENGTH_LONG).show()
    }

    override fun onClickDelete() {
    }
}