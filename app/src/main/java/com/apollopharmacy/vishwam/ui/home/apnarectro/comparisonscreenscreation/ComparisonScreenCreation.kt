package com.apollopharmacy.vishwam.ui.home.apnarectro.comparisonscreenscreation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityComparisonScreenBinding
import com.apollopharmacy.vishwam.databinding.ActivityPostRectroReviewScreenBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
import com.bumptech.glide.Glide
import java.io.File
import java.security.AccessController.checkPermission
import me.echodev.resizer.Resizer
import okhttp3.internal.notify


class ComparisonScreenCreation : AppCompatActivity(), ComparisonScreenCreationCallBack {
    var stage: String = ""
    var retroid: String = ""
    var categoryName:String=""
    var categoryid:String=""
    var storeIdFromRemarks:String=""
    var checkBoxClickedCount:Int=0
    private var uploadStage:String=""
    private var fileNameForCompressedImage: String? = null
    var imageFromCameraFile: File? = null
    var imageClickedPos:Int?=null
    private lateinit var cameraDialog: Dialog
    var imageUploaded=false


    public var posImageUrlList = java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>()
    var pos: Int = 0

    lateinit var activityPostRectroReviewScreenBinding: ActivityComparisonScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPostRectroReviewScreenBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_comparison_screen
        )
        setUp()
        updateImages()
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
        activityPostRectroReviewScreenBinding.callback=this
        if (intent != null) {
            stage = intent.getStringExtra("stage")!!
            retroid = intent.getStringExtra("retroid")!!
            posImageUrlList =
                intent.getSerializableExtra("posImageUrlList") as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>
            categoryName=intent.getStringExtra("categoryName")!!
            categoryid=intent.getStringExtra("categoryid")!!
            uploadStage = intent.getStringExtra("uploadStage")!!
            imageClickedPos= intent.getIntExtra("imageClickedPos", 0)!!
        }
        if (stage == "isPreRetroStage") {
            activityPostRectroReviewScreenBinding.reviewName.setText("Pre Retro Review")
            if(posImageUrlList.size==1){
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.GONE
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                lp.weight = 1f
                activityPostRectroReviewScreenBinding.firstImageLayout.layoutParams = lp
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility=View.GONE
            }
            else if(posImageUrlList.size==2){
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility=View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled=false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled=false
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility=View.GONE
                checkBoxClickedCount=2
            }
            else if(posImageUrlList.size==3){
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled=true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled=true
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility=View.GONE

            }

        } else if (stage.equals("isPostRetroStage")) {
            activityPostRectroReviewScreenBinding.reviewName.setText("Post Retro Review")

            if(posImageUrlList.size==1 && uploadStage.equals("newUploadStage")){
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.GONE
                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.VISIBLE
                activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE
                activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text="Upload Post Retro"
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility=View.VISIBLE
            }else if(posImageUrlList.size==2 && uploadStage.equals("reshootStage")){
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.GONE
                activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text="Post Retro Image"
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility=View.VISIBLE
            }
            else if(posImageUrlList.size==2){
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility=View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled=false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled=false
                checkBoxClickedCount=2
            }
            else if(posImageUrlList.size==3){
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled=true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled=true

            }
        } else {
            activityPostRectroReviewScreenBinding.reviewName.text = "After Completion Review"
            if(posImageUrlList.size==2 && uploadStage == "newUploadStage"){
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.GONE
                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility=View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text="Upload After Completion Retro"
            }else{
                if(posImageUrlList.size==3 && uploadStage == "reshootStage"){
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=false
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.GONE
                    if(activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked){
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.VISIBLE
                    }else{
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE
                    }
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text="After Completion Image"
                }else if(posImageUrlList.size==3){
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.reviewName.setText("After Completion Review")
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=false
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                    checkBoxClickedCount=2
                }

            }

        }
        activityPostRectroReviewScreenBinding.retroId.text = retroid
        activityPostRectroReviewScreenBinding.categoryName.text=categoryName
        activityPostRectroReviewScreenBinding.categoryNumber.text=categoryid
//        activityPostRectroReviewScreenBinding.storeId.text=storeIdFromRemarks

        activityPostRectroReviewScreenBinding.imageOne.setImageView2(
            activityPostRectroReviewScreenBinding.imageTwo
        )
        activityPostRectroReviewScreenBinding.imageTwo.setImageView1(
            activityPostRectroReviewScreenBinding.imageOne
        )

        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)



        activityPostRectroReviewScreenBinding.preRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.blue))
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                updateImages()
            } else {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
            }
        }
        activityPostRectroReviewScreenBinding.postRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(resources.getColor(R.color.blue))
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                updateImages()
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))

            }
        }
        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(resources.getColor(R.color.blue))
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                updateImages()
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=false
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
            }
        }

    }
    fun updateImages(){
        if(uploadStage.equals("reshootStage") && stage.equals("isAfterCompletionStage")){
            if(activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked){
                if(!imageUploaded){
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.deleteIcon.visibility=View.GONE
                }else{
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE
                    activityPostRectroReviewScreenBinding.deleteIcon.visibility=View.VISIBLE
                }

            } else{
                activityPostRectroReviewScreenBinding.deleteIcon.visibility=View.GONE
                activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE
            }
        }



        if (posImageUrlList.size == 1) {

            activityPostRectroReviewScreenBinding.imageHeading1.text="Pre Retro Image"
            for(i in posImageUrlList.indices){
                if(posImageUrlList.get(i).stage.equals("1") ){
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }

        }
        else if (posImageUrlList.size == 2) {

            activityPostRectroReviewScreenBinding.imageHeading1.text="Pre Retro Image"
            activityPostRectroReviewScreenBinding.imageHeading2.text="Post Retro Image"
            for(i in posImageUrlList.indices){
                if(posImageUrlList.get(i).stage.equals("1") ){
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }
            for(i in posImageUrlList.indices){
                if(posImageUrlList.get(i).stage.equals("2") ){
                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    if(posImageUrlList.get(i).url!=null){
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    }else{
                        Glide.with(this).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(this).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    }

                    bottomStatusUpdate(posImageUrlList.get(i).status)
                }
            }
        }
        else if (posImageUrlList.size == 3) {
            updateCheckBoxImages()

            if(activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked){
                activityPostRectroReviewScreenBinding.imageHeading1.text="Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text="Post Retro Image"
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("1") ){
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("2") ){
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                    }
                }
            }
            else if(activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked){
                activityPostRectroReviewScreenBinding.imageHeading1.text="Post Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text="After Completion Image"
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("2") ){
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("3") ){
                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if(posImageUrlList.get(i).url!=null){
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }else{
                            Glide.with(this).load(posImageUrlList.get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(this).load(posImageUrlList.get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }

                    }
                }
            }
            else if(activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked){
                activityPostRectroReviewScreenBinding.imageHeading1.text="Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text="After Completion Image"
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("1") ){
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("3") ){
                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if(posImageUrlList.get(i).url!=null){
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)

                        }else{
                            Glide.with(this).load(posImageUrlList.get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(this).load(posImageUrlList.get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }


                    }
                }
            }
            else if(activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked){
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("1") ){
                        activityPostRectroReviewScreenBinding.imageHeading1.text="Post Retro Image"
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            }
            else if(activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked){
                activityPostRectroReviewScreenBinding.imageHeading1.text="Post Retro Image"
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("2") ){
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            }
        }
    }

    fun updateCheckBoxImages(){
        for(i in posImageUrlList.indices){
            if(posImageUrlList.get(i).stage.equals("1") ){
                if(posImageUrlList.get(i).url!=null){
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }else{
                    Glide.with(this).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }

            }else if(posImageUrlList.get(i).stage.equals("2") ){
                if(posImageUrlList.get(i).url!=null){
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                }else{
                    Glide.with(this).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                }

            }  else if(posImageUrlList.get(i).stage.equals("3") ){
                if(posImageUrlList.get(i).url!=null){
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                }else{
                    Glide.with(this).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                }

            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun bottomStatusUpdate(status: String?) {
        if(status.equals("0")){
            activityPostRectroReviewScreenBinding.imageStatus.text="Pending"
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(getResources().getColor(R.color.material_amber_accent_700))
        }else if(status.equals("1")){
            activityPostRectroReviewScreenBinding.imageStatus.text="Approved"
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(getResources().getColor(R.color.dark_green))
        }else if(status.equals("2")){
            activityPostRectroReviewScreenBinding.imageStatus.text="Reshoot"
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(getResources().getColor(R.color.color_red))

        }

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

    override fun onClickCamera() {
        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()

        }
    }

    override fun onClickBack() {
        super.onBackPressed()
    }
    var imageUrlWithData = GetImageUrlsModelApnaResponse.Category.ImageUrl()
    override fun onClickUpload() {
        if(imageUploaded){
            val intent = Intent()
            intent.putExtra("imageUrlWithData", imageUrlWithData)
            if(uploadStage.equals("reshootStage")){
                intent.putExtra("posImageUrlList", posImageUrlList)
                intent.putExtra("fileForReshoot", fileForReshoot)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }else{
            Toast.makeText(context, "Please upload the image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickDelete() {
        cameraDialog = Dialog(this)
        cameraDialog.setContentView(R.layout.dialog_camera_delete)
        val close = cameraDialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            cameraDialog.dismiss()
        }
        val ok = cameraDialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {

            var pos = -1
            for (i in posImageUrlList) {
                if (stage.equals("isPreRetroStage")) {
                    if (i.stage.equals("1")) {
                        pos = posImageUrlList.indexOf(i)
                    }
                } else if (stage.equals("isPostRetroStage")) {
                    if (i.stage.equals("2")) {
                        pos = posImageUrlList.indexOf(i)
                    }

                } else {
                    if (i.stage.equals("3")) {
                        pos = posImageUrlList.indexOf(i)
                    }
                }
            }
            if (pos != -1) {
                if (posImageUrlList.get(pos).status.equals("9")) {
                    posImageUrlList.removeAt(pos)
                } else {
                    posImageUrlList.get(pos).file = null
                }
            }
            imageUploaded=false
            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(resources.getColor(R.color.grey))

            if(stage.equals("isPostRetroStage")){
                if(uploadStage.equals("reshootStage")){
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageTwo)
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.GONE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled=false
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled=false
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=true
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.VISIBLE
                }
                else{
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.GONE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled=false
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility=View.GONE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE

                }
            }else if(stage.equals("isAfterCompletionStage")){
                if(uploadStage.equals("reshootStage")){
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageTwo)
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.GONE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=false
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility=View.VISIBLE
                    if(activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked){
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.VISIBLE
                    }else{
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE
                    }

                }
                else{
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility=View.GONE
//                    activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(resources.getColor(R.color.grey))
//                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked=false
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.VISIBLE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.GONE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility=View.GONE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE

                }
            }

            cameraDialog.dismiss()
        }

        cameraDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cameraDialog.show()
    }

    override fun onClickDeleteforPreRetro() {
        TODO("Not yet implemented")
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile =
            File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")
        fileNameForCompressedImage = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askPermissions(PermissonCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        }
    }
    var stagePos=-1
    var fileForReshoot:File?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
//            var capture: File? = null
            imageFromCameraFile?.length()
            val resizedImage = Resizer(this)
                .setTargetLength(1080)
                .setQuality(100)
                .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                .setOutputDirPath(
                    ViswamApp.Companion.context.cacheDir.toString()
                )

                .setSourceImage(imageFromCameraFile)
                .resizedFile
            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(resources.getColor(R.color.dark_green))
//            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()
            imageUploaded=true

            var imageUrl = GetImageUrlsModelApnaResponse.Category.ImageUrl()
            if(uploadStage.equals("reshootStage")){
                if (stage.equals("isPreRetroStage")) {
                    posImageUrlList.get(0).file=resizedImage
//                    posImageUrlList.get(0).url=null
                    stagePos=0
                } else if (stage.equals("isPostRetroStage")) {
                    posImageUrlList.get(1).file=resizedImage
//                    posImageUrlList.get(1).url=null
                    stagePos=1
                } else {
                    posImageUrlList.get(2).file=resizedImage
//                    posImageUrlList.get(2).url=null
                    stagePos=2
                }
                fileForReshoot=resizedImage

            }else{

                imageUrl.file = resizedImage!!
                imageUrl.status = "9"
                if (stage.equals("isPreRetroStage")) {
                    imageUrl.stage = "1"
                    stagePos=0
                } else if (stage.equals("isPostRetroStage")) {
                    imageUrl.stage = "2"
                    stagePos=1
                } else {
                    imageUrl.stage = "3"
                    stagePos=2
                }
                imageUrl.categoryid = categoryid.toInt()
                imageUrl.position = imageClickedPos
                imageUrlWithData=imageUrl

                posImageUrlList!!.add(imageUrl)
                synchronized(posImageUrlList){
                    posImageUrlList.notify()
                }
            }



            if(stage.equals("isPostRetroStage")){
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("2")){
                        activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.VISIBLE
                        activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility=View.GONE
                        activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled=false
                        activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled=false
                        activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.GONE
                        activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.VISIBLE
                        activityPostRectroReviewScreenBinding.deleteIcon.visibility=View.VISIBLE
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE
                        activityPostRectroReviewScreenBinding.imageHeading2.text="Post Retro Image"
                        if(!uploadStage.equals("reshootStage")){
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                        }
                        else{
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)

                        }

                    }
                }
            }else if(stage.equals("isAfterCompletionStage")){
                for(i in posImageUrlList.indices){
                    if(posImageUrlList.get(i).stage.equals("3")){
                        activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.VISIBLE
                        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked=true
                        activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility=View.GONE
                        activityPostRectroReviewScreenBinding.secondImageLayout.visibility=View.VISIBLE
                        activityPostRectroReviewScreenBinding.deleteIcon.visibility=View.VISIBLE
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility=View.GONE
                        if(!uploadStage.equals("reshootStage")){
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                        }else{
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                            Glide.with(this).load(posImageUrlList.get(stagePos).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)

                        }

                        activityPostRectroReviewScreenBinding.imageHeading2.text="After Completion Image"
                    }
                }
            }


        }


    }
}
