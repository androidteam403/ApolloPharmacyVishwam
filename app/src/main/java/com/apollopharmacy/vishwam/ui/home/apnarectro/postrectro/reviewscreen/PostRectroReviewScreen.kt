package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityPostRectroReviewScreenBinding
import com.apollopharmacy.vishwam.databinding.DialogLastimagePreviewAlertBinding
import com.apollopharmacy.vishwam.databinding.DialogOkAlertBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreviewPreRetroViewModel
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro.PreviewLastImageCallback
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import org.apache.commons.lang3.text.WordUtils


class PostRectroReviewScreen : AppCompatActivity(), PreviewLastImageCallback {
    var stage: String = ""
    private var retroId: String = ""
    private var store: String = ""
    var categoryName: String = ""
    var categorypos: Int = 0
    private var status: String = ""
    var isAfterCompletion: Boolean = false
    var isPreRetro: Boolean = false
    var isPostRetro: Boolean = false
    var stagePosition: String = ""

    private var uploadBy: String = ""
    private var statusId: String = ""
    private var isApiHit: Boolean = false
    private lateinit var dialog: Dialog
    var ratingbar: RatingBar? = null
    var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()
    var imageUrlMainList = ArrayList<GetImageUrlResponse.Category>()
    private lateinit var previewLastImageViewModel: PreviewPostRetroViewModel
    var saveRequestImageslist = java.util.ArrayList<SaveAcceptRequest.Imageurl>()

    private var isRatingApiHit: Boolean = false
    var ratingforsubmit: String? = null
    public var imageUrlList = ArrayList<List<GetImageUrlResponse.ImageUrl>>()
    var pos: Int = 0

    lateinit var activityPostRectroReviewScreenBinding: ActivityPostRectroReviewScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPostRectroReviewScreenBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_post_rectro_review_screen)
        previewLastImageViewModel =
            ViewModelProvider(this)[PreviewPostRetroViewModel::class.java]
        if (intent != null) {
            retroId = intent.getStringExtra("retroId")!!
            status = intent.getStringExtra("status")!!

            pos = intent.getIntExtra("position", 0)!!
            imageUrlMainList =
                intent.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>
            uploadBy = intent.getStringExtra("uploadby")!!
            stage = intent.getStringExtra("stage")!!

            categoryName = intent.getStringExtra("categoryName")!!
            categorypos = intent.getIntExtra("categoryPos", 0)!!
            store = intent.getStringExtra("store")!!
            imageUrlList =
                intent.getSerializableExtra("imageList") as ArrayList<List<GetImageUrlResponse.ImageUrl>>


        }


        activityPostRectroReviewScreenBinding.retroId.setText(retroId)

        if (stage.toLowerCase().contains("pos")) {
            stagePosition = "2"
        } else if (stage.toLowerCase().contains("aft")) {
            stagePosition = "3"

        }
        else if (stage.toLowerCase().contains("pre")) {
            stagePosition = "1"

        }
        for (k in imageUrlMainList.indices) {
            imageUrlMainList.get(k).imageUrls!!.filter { it.stage.equals(stagePosition) }
        }
        imageUrlsList.filter { it.stage.equals(stagePosition) }

        for (k in imageUrlMainList.indices) {
            for (l in imageUrlMainList.get(k).imageUrls!!.indices) {
                var imageUrlResponse = GetImageUrlResponse.ImageUrl()
                imageUrlResponse.isVerified = imageUrlMainList.get(k).imageUrls!!.get(l).isVerified
                imageUrlResponse.status = imageUrlMainList.get(k).imageUrls!!.get(l).status
                imageUrlResponse.retorautoid =
                    imageUrlMainList.get(k).imageUrls!!.get(l).retorautoid
                imageUrlResponse.imageid = imageUrlMainList.get(k).imageUrls!!.get(l).imageid
                imageUrlResponse.stage = imageUrlMainList.get(k).imageUrls!!.get(l).stage

                imageUrlResponse.categoryid = imageUrlMainList.get(k).imageUrls!!.get(l).categoryid

                imageUrlsList.add(imageUrlResponse)

            }


        }


        setUp()
    }

    var handler = Handler()
    var runnable = Runnable {

        activityPostRectroReviewScreenBinding.imageOne.dispatchTouchEvent(
            MotionEvent.obtain(
                0,
                0,
                MotionEvent.ACTION_DOWN,
                100F,
                100F,
                0.5f,
                5F,
                0,
                1F,
                1F,
                0,
                0
            )
        )
        activityPostRectroReviewScreenBinding.imageTwo.dispatchTouchEvent(
            MotionEvent.obtain(
                0,
                0,
                MotionEvent.ACTION_DOWN,
                100F,
                100F,
                0.5f,
                5F,
                0,
                1F,
                1F,
                0,
                0
            )
        )
    }

    private fun setUp() {
        activityPostRectroReviewScreenBinding.callback = this

        if (status.equals("0")){
            activityPostRectroReviewScreenBinding.totalImagesandaction.visibility=View.VISIBLE

        }else{
            activityPostRectroReviewScreenBinding.totalImagesandaction.visibility=View.GONE

        }

        if (imageUrlList.get(0).size==3&&imageUrlList.get(0).filter { it.stage!!.contains("3") }.isNotEmpty()){
            Glide.with(this).load(imageUrlList.get(pos).get(2).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.afterCompletionCbLayoutImage)
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility=View.VISIBLE

        }else  if (imageUrlList.get(0).size==2&&imageUrlList.get(0).filter { it.stage!!.contains("2") }.isNotEmpty()){
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility=View.GONE

        }
        for (j in imageUrlMainList.indices) {
            if (imageUrlMainList.get(j).categoryname.equals(categoryName)) {

                for (l in imageUrlMainList.get(j).imageUrls!!.indices) {

                    if (imageUrlMainList.get(j).imageUrls!!.get(l).stage!!.contains(stagePosition)) {


                        if (imageUrlMainList.get(j).imageUrls!!.filter {
                                it.stage.equals(stagePosition) }.get(pos).isVerified!! && imageUrlMainList.get(j).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(pos).status.equals("1")) {

                            activityPostRectroReviewScreenBinding.accept.alpha = 0.5f

                        } else {
                            activityPostRectroReviewScreenBinding.accept.alpha = 1f

                        }
                        if (imageUrlMainList.get(j).imageUrls!!.filter {
                                it.stage.equals(stagePosition)
                            }
                                .get(pos).isVerified!! && imageUrlMainList.get(j).imageUrls!!.filter {
                                it.stage.equals(stagePosition)
                            }.get(pos).status.equals("2")) {

                            activityPostRectroReviewScreenBinding.reshoot.alpha = 0.5f

                        } else {
                            activityPostRectroReviewScreenBinding.reshoot.alpha = 1f

                        }

                    }
                }
            }
        }



        activityPostRectroReviewScreenBinding.backArrow.setOnClickListener {
            onBackPressed()
        }

        activityPostRectroReviewScreenBinding.categoryNumber.setText((categorypos + 1).toString())
        activityPostRectroReviewScreenBinding.categoryName.setText(categoryName)
        if (status.equals("0")){
            activityPostRectroReviewScreenBinding.status.setText("Pending")

        }
        else   if (status.equals("1")){
            activityPostRectroReviewScreenBinding.status.setText("Approved")

        }
        else   if (status.equals("2")){
            activityPostRectroReviewScreenBinding.status.setText("Reshoot")

        }
        if (status!!.toLowerCase().contains("0")) {
            activityPostRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        } else if (status!!.toLowerCase().contains("1")) {
            activityPostRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#39B54A"))
        } else if (status!!.toLowerCase().contains("2")) {
            activityPostRectroReviewScreenBinding.status.setTextColor(Color.parseColor(
                "#f26522"))
        }
        activityPostRectroReviewScreenBinding.reviewName.setText(WordUtils.capitalizeFully(stage.replace(
            "-",
            " ")) + " Review")

        activityPostRectroReviewScreenBinding.retroId.setText(retroId)
        activityPostRectroReviewScreenBinding.storeId.setText(store)
        if (imageUrlList.get(pos).get(0).stage.equals("1")) {
            activityPostRectroReviewScreenBinding.imageOneStage.setText("Pre Retro Image")
        }
        if(imageUrlList.get(pos).get(1).stage!!.contains("2")){
            if (imageUrlList.get(pos).get(1).stage.equals("2")) {
                activityPostRectroReviewScreenBinding.imageTwoStage.setText("Post Retro Image")
            }
        }

        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

            if (activityPostRectroReviewScreenBinding.imageOneStage.text.toString().contains("Pre")){
                PopUpWIndow(this, R.layout.layout_image_fullview, activityPostRectroReviewScreenBinding.firstImageZoom, imageUrlList[pos][0].url, null, "", 0)
            }
            else   if (activityPostRectroReviewScreenBinding.imageOneStage.text.toString().contains("Post")) {
                PopUpWIndow(this, R.layout.layout_image_fullview, activityPostRectroReviewScreenBinding.firstImageZoom, imageUrlList[pos][1].url, null, "", 0)
            }

            else   if (activityPostRectroReviewScreenBinding.imageOneStage.text.toString().contains("After")) {

                PopUpWIndow(this, R.layout.layout_image_fullview, activityPostRectroReviewScreenBinding.firstImageZoom, imageUrlList[pos][2].url, null, "", 0)
            }

        }
        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {
            if (activityPostRectroReviewScreenBinding.imageTwoStage.text.toString().contains("Pre")){
                PopUpWIndow(context, R.layout.layout_image_fullview, activityPostRectroReviewScreenBinding.secondImageZoom, imageUrlList[pos][0].url, null, "", 0)
            }
            else   if (activityPostRectroReviewScreenBinding.imageTwoStage.text.toString().contains("Post")) {

                PopUpWIndow(context, R.layout.layout_image_fullview, activityPostRectroReviewScreenBinding.secondImageZoom, imageUrlList[pos][1].url, null, "", 0)
            }

            else   if (activityPostRectroReviewScreenBinding.imageTwoStage.text.toString().contains("After")) {

                PopUpWIndow(this, R.layout.layout_image_fullview, activityPostRectroReviewScreenBinding.secondImageZoom, imageUrlList[pos][2].url, null, "", 0)
            }        }

        Glide.with(this).load(imageUrlList.get(pos).get(0).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.imageOne)


        Glide.with(this).load(imageUrlList.get(pos).get(1).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.imageTwo)


        Glide.with(this).load(imageUrlList[pos].get(0).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.preRectroCbLayoutImage)

        Glide.with(this).load(imageUrlList[pos].get(1).url)
            .placeholder(R.drawable.thumbnail_image)
            .into(activityPostRectroReviewScreenBinding.postRectroCbLayoutImage)
        if (isAfterCompletion) {

            if(activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked==false){
                activityPostRectroReviewScreenBinding.imageTwoStage.setText("After Completion  Image")
                activityPostRectroReviewScreenBinding.imageOneStage.setText("Pre Retro Image")
            }
            else{
                activityPostRectroReviewScreenBinding.imageTwoStage.setText("After Completion  Image")
                activityPostRectroReviewScreenBinding.imageOneStage.setText("Post Retro Image")
            }

            Glide.with(this).load(imageUrlList.get(pos).get(1).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.imageOne)

            Glide.with(this).load(imageUrlList.get(pos).get(2).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.imageTwo)
        }

        activityPostRectroReviewScreenBinding.setRetroId(retroId)
        activityPostRectroReviewScreenBinding.totalImages = imageUrlsList.filter { it.stage.equals(stagePosition) }.size.toString()
        activityPostRectroReviewScreenBinding.accepted =
            imageUrlsList.filter{it.stage.equals(stagePosition)}.filter { it.status.equals("1") }.size.toString()
        activityPostRectroReviewScreenBinding.rejected =
            imageUrlsList.filter{it.stage.equals(stagePosition)}.filter { it.status.equals("2") }.size.toString()

        activityPostRectroReviewScreenBinding.isLastPos =
            imageUrlsList.filter { it.stage.equals(stagePosition)&&it.isVerified == true }.size == imageUrlsList.filter { it.stage.equals(stagePosition) }.size


        if (isPostRetro) {
            activityPostRectroReviewScreenBinding.imageTwoStage.setText("Post Retro Image")

            Glide.with(this).load(imageUrlList.get(pos).get(0).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.imageOne)
            Glide.with(this).load(imageUrlList.get(pos).get(1).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.imageTwo)
        }
        if (isPreRetro) {
            if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked==false){
                activityPostRectroReviewScreenBinding.imageOneStage.setText("Pre Retro Image")
                activityPostRectroReviewScreenBinding.imageTwoStage.setText("After Completion  Image")

            }
            else {
                activityPostRectroReviewScreenBinding.imageOneStage.setText("Pre Retro Image")
            }

            Glide.with(this).load(imageUrlList.get(pos).get(0).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.imageOne)
            Glide.with(this).load(imageUrlList.get(pos).get(2).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.imageTwo)
        }

        if (imageUrlList.size==1) {
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE

        } else if (imageUrlList.get(0).size==3) {
            Glide.with(this).load(imageUrlList.get(pos).get(2).url)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityPostRectroReviewScreenBinding.afterCompletionCbLayoutImage)
            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.VISIBLE


        }





        activityPostRectroReviewScreenBinding.imageOne.setImageView2(
            activityPostRectroReviewScreenBinding.imageTwo
        )
        activityPostRectroReviewScreenBinding.imageTwo.setImageView1(
            activityPostRectroReviewScreenBinding.imageOne
        )






        activityPostRectroReviewScreenBinding.preRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (imageUrlList.get(0).size==3) {

                if (activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked == false) {
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true

                } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {

                    if (isChecked) {

                        activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                        activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                            resources.getColor(
                                R.color.blue))
                        activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                        activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                            resources.getColor(R.color.grey))

                        isPostRetro = false
                        isAfterCompletion = false
                        isPreRetro = true
                        setUp()


                    }
                } else {
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                }


            }

        }
        activityPostRectroReviewScreenBinding.postRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (imageUrlList.get(0).size==3) {


                if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked == false) {
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true

                } else if (activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {

                    if (isChecked) {


                        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                            false
                        activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                        activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                            resources.getColor(R.color.blue))
                        activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                            resources.getColor(R.color.grey))
                        isAfterCompletion = false
                        isPreRetro = false
                        isPostRetro == true

                        setUp()


                    }
                } else {

                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                }
            }

        }
        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (imageUrlList.get(0).size==3) {
                if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked == false && activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {
                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                        true
                }


                if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {

                    if (isChecked) {

                        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                            true
                        activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                            resources.getColor(R.color.blue))

                        activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                        activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                            resources.getColor(R.color.grey))
                        isPostRetro = false
                        isPreRetro = false
                        isAfterCompletion = true
                        setUp()

                    }
                } else {
                    if (isChecked == false)
                        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                            true
                }

            }
        }




        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)

    }


    override fun onResume() {

        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)
        super.onResume()
    }

    override fun onPause() {

        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun onClick(position: Int, status: String) {

    }

    override fun onClickReShoot() {

        for (i in imageUrlMainList.indices) {
            for (j in imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }) {
                for (l in imageUrlsList.filter { it.stage.equals(stagePosition) }.indices) {

                    if (imageUrlMainList.get(i).categoryname.equals(categoryName)) {
                        if (imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(pos).stage.equals(stagePosition)) {
                            imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(pos).setisVerified(true)
                            imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(pos).status = "2"

                            if ( imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(pos).imageid.equals(imageUrlsList.filter { it.stage.equals(stagePosition) }.get(l).imageid)){
                                imageUrlsList.filter { it.stage.equals(stagePosition) }.get(l).setisVerified(true)
                                imageUrlsList.filter { it.stage.equals(stagePosition) }.get(l).status="2"
                            }
                        }
                    }
                }

            }
        }

        activityPostRectroReviewScreenBinding.setRetroId(retroId)


        activityPostRectroReviewScreenBinding.isLastPos =
            imageUrlsList.filter { it.stage.equals(stagePosition)&&it.isVerified == true }.size == imageUrlsList.filter { it.stage.equals(stagePosition) }.size


        activityPostRectroReviewScreenBinding.totalImages = imageUrlsList.filter { it.stage.equals(stagePosition) }.size.toString()
        activityPostRectroReviewScreenBinding.accepted =
            imageUrlsList.filter{it.stage.equals(stagePosition)}.filter { it.status.equals("1") }.size.toString()
        activityPostRectroReviewScreenBinding.rejected =
            imageUrlsList.filter{it.stage.equals(stagePosition)}.filter { it.status.equals("2") }.size.toString()
        for (j in imageUrlMainList.indices) {
            if (imageUrlMainList.get(j).categoryname.equals(categoryName)) {

                if (imageUrlMainList.get(j).imageUrls!!.filter { it.stage.equals(stagePosition) }!!.get(pos).isVerified == true && imageUrlMainList.get(
                        j).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(pos).status.equals("2")
                ) {


                    activityPostRectroReviewScreenBinding.reshoot.alpha = 0.5f
                } else {
                    activityPostRectroReviewScreenBinding.reshoot.alpha = 1f

                }
            }
        }


    }

    override fun onClickAccept() {
        for (i in imageUrlMainList.indices) {
            for (j in imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }) {
                for (l in imageUrlsList.filter { it.stage.equals(stagePosition) }.indices) {

                    if (imageUrlMainList.get(i).categoryname.equals(categoryName)) {
                        if (imageUrlMainList.get(i).imageUrls!!.filter {
                                it.stage.equals(stagePosition)
                            }.get(pos).stage.equals(stagePosition)) {
                            imageUrlMainList.get(i).imageUrls!!.filter {
                                it.stage.equals(stagePosition)
                            }.get(pos).setisVerified(true)
                            imageUrlMainList.get(i).imageUrls!!.filter {
                                it.stage.equals(stagePosition)
                            }.get(pos).status = "1"

                            if ( imageUrlMainList.get(i).imageUrls!!.filter {
                                    it.stage.equals(stagePosition)
                                }.get(pos).imageid.equals(imageUrlsList.filter { it.stage.equals(stagePosition) }.get(l).imageid)){
                                imageUrlsList.filter { it.stage.equals(stagePosition) }.get(l).setisVerified(true)
                                imageUrlsList.filter { it.stage.equals(stagePosition) }.get(l).status="1"
                            }
                        }
                    }
                }

            }
        }


        activityPostRectroReviewScreenBinding.setRetroId(retroId)


        activityPostRectroReviewScreenBinding.isLastPos =
            imageUrlsList.filter { it.stage.equals(stagePosition)&&it.isVerified == true }.size == imageUrlsList.filter { it.stage.equals(stagePosition) }.size


        activityPostRectroReviewScreenBinding.totalImages = imageUrlsList.filter { it.stage.equals(stagePosition) }.size.toString()
        activityPostRectroReviewScreenBinding.accepted =
            imageUrlsList.filter{it.stage.equals(stagePosition)}.filter { it.status.equals("1") }.size.toString()
        activityPostRectroReviewScreenBinding.rejected =
            imageUrlsList.filter{it.stage.equals(stagePosition)}.filter { it.status.equals("2") }.size.toString()
        for (j in imageUrlMainList.indices) {
            if (imageUrlMainList.get(j).categoryname.equals(categoryName)) {

                if (imageUrlMainList.get(j).imageUrls!!.filter { it.stage.equals(stagePosition) }!!.get(pos).isVerified == true && imageUrlMainList.get(
                        j).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(pos).status.equals("1")
                ) {


                    activityPostRectroReviewScreenBinding.accept.alpha = 0.5f
                } else {
                    activityPostRectroReviewScreenBinding.accept.alpha = 1f

                }
            }
        }
    }


    override fun onClickCompleted() {
        for (i in imageUrlsList.filter { it.stage.equals(stagePosition) }.indices) {
            var imageRequest = SaveAcceptRequest.Imageurl()
            imageRequest.statusid = imageUrlsList.filter { it.stage.equals(stagePosition)}.get(i).status
            imageRequest.imageid = imageUrlsList.filter { it.stage.equals(stagePosition)}.get(i).imageid
            saveRequestImageslist.add(imageRequest)

        }
        if (imageUrlsList.filter { it.stage.equals(stagePosition) }.filter { it.status.equals("1") }.size == imageUrlsList.filter { it.stage.equals(stagePosition) }.size) {
            statusId = "1"
        } else if (imageUrlsList.filter { it.stage.equals(stagePosition) }.filter { it.status.equals("2") }.size == imageUrlsList.filter { it.stage.equals(stagePosition) }.size) {
            statusId = "2"
        } else if ((imageUrlsList.filter { it.stage.equals(stagePosition) }.filter { it.status.equals("1") }.size + imageUrlsList.filter { it.stage.equals(stagePosition) }.filter {
                it.status.equals("2")
            }.size) == imageUrlsList.size) {
            statusId = "2"
        }

        for (i in imageUrlMainList.indices) {
            for (j in imageUrlMainList[i].imageUrls!!.filter { it.stage.equals(stagePosition) }!!.indices) {
                for (k in imageUrlsList.filter { it.stage.equals(stagePosition) }.indices) {
                    if (imageUrlMainList[i].imageUrls!!.filter { it.stage.equals(stagePosition) }[j].imageid.equals(imageUrlsList.get(k).imageid)) {
                        if (imageUrlsList.filter { it.stage.equals(stagePosition) }.get(k).isVerified == true && imageUrlsList.filter { it.stage.equals(stagePosition) }.get(k).status.equals(
                                "1")
                        ) {

                            imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(j).status = "1"

                            imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(j).setisVerified(true)
                        } else if (imageUrlsList.filter { it.stage.equals(stagePosition) }.get(k).isVerified == true && imageUrlsList.filter { it.stage.equals(stagePosition) }.get(
                                k).status.equals("2")
                        ) {


                            imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(j).status = "2"
                            imageUrlMainList.get(i).imageUrls!!.filter { it.stage.equals(stagePosition) }.get(j)
                                .setisVerified(true)
                        }
                    }

                }

            }
        }
        var imageRequest = SaveAcceptRequest()
        imageRequest.retroautoid =retroId
        imageRequest.type = ""
        imageRequest.stageid =stagePosition
        imageRequest.reamrks = ""
        imageRequest.statusid = statusId
        imageRequest.storeid = store
        imageRequest.userid = Preferences.getToken()
        imageRequest.rating = ""
        imageRequest.imageurls = saveRequestImageslist.distinctBy { it.imageid }

        previewLastImageViewModel.saveAccepetAndReshoot(imageRequest, this)


    }

    override fun onClickBack() {

        if (status.equals("0")) {

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
                intent.putExtra("imageUrlList", imageUrlMainList)
                intent.putExtra("imagesList", imageUrlsList)
                intent.putExtra("stagePosition", stagePosition)
                intent.putExtra("ratingApi", isRatingApiHit)
                intent.putExtra("isApiHit", isApiHit)

                setResult(Activity.RESULT_OK, intent)
                finish()
                imagesStatusAlertDialog.dismiss()

            }
            imagesStatusAlertDialog.show()
        }
        else{
            onBackPressed()
        }

    }

    override fun onSuccessRatingResponse(value: SaveAcceptResponse) {
        Utlis.hideLoading()

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
            intent.putExtra("imageUrlList", imageUrlMainList)
            intent.putExtra("isApiHit", isApiHit)
            intent.putExtra("stagePosition", stagePosition)

            intent.putExtra("ratingApi", isRatingApiHit)
            intent.putExtra("imagesList", imageUrlsList)
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
            intent.putExtra("imageUrlList", imageUrlMainList)
            intent.putExtra("isApiHit", isApiHit)
            intent.putExtra("ratingApi", isRatingApiHit)
            intent.putExtra("imagesList", imageUrlsList)
            intent.putExtra("stagePosition", stagePosition)

            setResult(Activity.RESULT_OK, intent)
            finish()
            imagesStatusAlertDialog.dismiss()
        }

        imagesStatusAlertDialog.show()

    }

    override fun onSuccessSaveAcceptReshoot(value: SaveAcceptResponse) {
        isApiHit = true
        if (imageUrlsList.filter { it.stage.equals(stagePosition) }.filter { it.status.equals("1") }.size == imageUrlsList.filter { it.stage.equals(stagePosition) }.size) {


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
                    imageRequest.retroautoid = "retroId"
                    imageRequest.type = "REMARKS"
                    imageRequest.stageid =stagePosition
                    imageRequest.reamrks = comments.text.toString()
                    imageRequest.statusid = statusId
                    imageRequest.storeid = store
                    imageRequest.userid = Preferences.getToken()
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
                intent.putExtra("imageUrlList", imageUrlMainList)
                intent.putExtra("isApiHit", isApiHit)
                intent.putExtra("ratingApi", isRatingApiHit)
                intent.putExtra("imagesList", imageUrlsList)
                intent.putExtra("stagePosition", stagePosition)

                setResult(Activity.RESULT_OK, intent)
                finish()
                imagesStatusAlertDialog.dismiss()
            }

            imagesStatusAlertDialog.show()


        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onFailureSaveAcceptReshoot(value: SaveAcceptResponse) {
        Toast.makeText(applicationContext, value.message,  Toast.LENGTH_SHORT).show()
    }

    override fun onClickDelete() {
    }
}
