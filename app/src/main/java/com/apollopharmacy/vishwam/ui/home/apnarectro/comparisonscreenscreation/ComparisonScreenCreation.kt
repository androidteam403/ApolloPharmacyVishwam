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
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityComparisonScreenBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter.ComparisionPreviewImage
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.bumptech.glide.Glide
import me.echodev.resizer.Resizer
import okhttp3.internal.notify
import java.io.File


class ComparisonScreenCreation : AppCompatActivity(), ComparisonScreenCreationCallBack,
    ViewPager.OnPageChangeListener {
    var stage: String = ""
    var retroid: String = ""
    var categoryName: String = ""
    var categoryid: String = ""
    var storeId: String = ""
    var uploadPosition: Int = 0
    var uploadSubPosition: Int = 0

    var checkBoxClickedCount: Int = 0
    private var uploadStage: String = ""
    private var fileNameForCompressedImage: String? = null
    var imageFromCameraFile: File? = null
    var imageClickedPos: Int? = null
    private lateinit var cameraDialog: Dialog
    var categoryGroupResponse: GetImageUrlsModelApnaResponse.Category? = null
    var imageUploaded = false
    private var previewImageAdapter: ComparisionPreviewImage? = null
    private var mainImageUrlList = ArrayList<GetImageUrlsModelApnaResponse.Category>()
    var status: String = ""
    var tempImageUrlList: MutableList<ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>? =
        mutableListOf()
    var posImageId: String = ""

    private var posImageUrlList =
        ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>()
    var imageId: String = ""
    var isLastPage = false

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
        activityPostRectroReviewScreenBinding.callback = this
        activityPostRectroReviewScreenBinding.previewImageViewpager.addOnPageChangeListener(
            this
        )
        if (intent != null) {
            stage = intent.getStringExtra("stage")!!
            status = intent.getStringExtra("status")!!
            storeId = intent.getStringExtra("store")!!
            retroid = intent.getStringExtra("retroid")!!
            mainImageUrlList =
                intent.getSerializableExtra("mainList") as ArrayList<GetImageUrlsModelApnaResponse.Category>
            posImageUrlList =
                intent.getSerializableExtra("posImageUrlList") as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>
            categoryName = intent.getStringExtra("categoryName")!!
            categoryid = intent.getStringExtra("categoryid")!!
            categoryGroupResponse =
                intent.getSerializableExtra("categoryResponse") as GetImageUrlsModelApnaResponse.Category?
            uploadStage = intent.getStringExtra("uploadStage")!!
            imageClickedPos = intent.getIntExtra("imageClickedPos", 0)!!
            activityPostRectroReviewScreenBinding.storeId.setText(
                storeId.split("-").get(0) + "-" + storeId.split("-").get(1)
            )

            mainImageUrlList.forEach { category ->
                category.groupingImageUrlList?.let { tempImageUrlList?.addAll(it) }
            }
        }

        activityPostRectroReviewScreenBinding.imageStatus.setText(status)


        if (status.contains("Approved")) {
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(context.getColor(R.color.greenn))

        } else if (status.contains("Pending")) {
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(context.getColor(R.color.pending_color_for_apna))

        } else {
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(context.getColor(R.color.color_red))

        }






        for (i in mainImageUrlList.indices) {
            for (j in mainImageUrlList.get(i).groupingImageUrlList!!.indices) {
                for (k in mainImageUrlList.get(i).groupingImageUrlList!!.get(j).indices) {
                    if (categoryName.equals(mainImageUrlList.get(i).categoryname)) {
                        if (mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                .get(k).imageid.isNullOrEmpty()
                        ) {

                        } else {
                            posImageId = mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                .get(k).imageid!!

                        }


                    }


                }
            }
        }
        for (k in tempImageUrlList!!.indices) {
            for (l in tempImageUrlList!!.get(k).indices) {
                if (posImageId.equals(tempImageUrlList!!.get(k).get(l).imageid)) {

                }
            }
        }
// Ensure that tempImageUrlList is not null before using it
        tempImageUrlList?.let {
            previewImageAdapter = ComparisionPreviewImage(
                this, it,
                this, uploadStage, mainImageUrlList, stage, status, imageUploaded,
                imageClickedPos!!
            )

            activityPostRectroReviewScreenBinding.previewImageViewpager.adapter =
                previewImageAdapter
            activityPostRectroReviewScreenBinding.previewImageViewpager.setCurrentItem(
                imageClickedPos!!,
                true
            )
        } ?: run {
            // Handle the case when tempImageUrlList is null
        }














        if (stage == "isPreRetroStage") {
            activityPostRectroReviewScreenBinding.reviewName.setText("Pre Retro Review")
            if (posImageUrlList.size == 1 && posImageUrlList.get(0).status!!.equals("2")) {
                activityPostRectroReviewScreenBinding.reshootCameraPreRetro.visibility =
                    View.VISIBLE
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                activityPostRectroReviewScreenBinding.firstImageZoom.visibility = View.GONE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                lp.weight = .2f
                activityPostRectroReviewScreenBinding.firstImageLayout.layoutParams = lp
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.GONE
            } else if (posImageUrlList.size == 1) {
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                activityPostRectroReviewScreenBinding.firstImageZoom.visibility = View.GONE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                lp.weight = .2f
                activityPostRectroReviewScreenBinding.firstImageLayout.layoutParams = lp
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.GONE
            } else if (posImageUrlList.size == 2) {
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = false
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.GONE
                checkBoxClickedCount = 2
            } else if (posImageUrlList.size == 3) {
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = true
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.GONE

            }

        } else if (stage.equals("isPostRetroStage")) {
            activityPostRectroReviewScreenBinding.reviewName.setText("Post Retro Review")

            if (posImageUrlList.size == 1 && uploadStage.equals("newUploadStage")) {
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.GONE

                activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                    "Upload Post Retro"
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
            } else if (posImageUrlList.size == 2 && posImageUrlList.get(1).status.equals("2")) {
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                    "Post Retro Image"
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
            } else if (posImageUrlList.size == 2) {
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = false
                checkBoxClickedCount = 2
            } else if (posImageUrlList.size == 3) {
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    resources.getColor(
                        R.color.blue
                    )
                )
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = true

            }
        } else {
            activityPostRectroReviewScreenBinding.reviewName.text = "After Completion Review"
            if (posImageUrlList.size == 2 && uploadStage == "newUploadStage") {
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    resources.getColor(R.color.grey)
                )
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                    "Upload After Completion Retro"
            } else if (posImageUrlList.size == 3 && posImageUrlList.get(2).status.equals("2")) {
                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                    View.VISIBLE
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.GONE
                activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                    "After Completion Image"
                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    resources.getColor(R.color.grey)
                )
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    resources.getColor(R.color.blue)
                )

            } else if (posImageUrlList.size == 3) {
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.reviewName.setText("After Completion Review")
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    resources.getColor(R.color.blue)
                )
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                checkBoxClickedCount = 2
            }


        }
        activityPostRectroReviewScreenBinding.retroId.text = retroid
        activityPostRectroReviewScreenBinding.categoryName.text = categoryName
        activityPostRectroReviewScreenBinding.categoryNumber.text = categoryid
//        activityPostRectroReviewScreenBinding.storeId.text=storeIdFromRemarks

        activityPostRectroReviewScreenBinding.imageOne.setImageView2(
            activityPostRectroReviewScreenBinding.imageTwo
        )
        activityPostRectroReviewScreenBinding.imageTwo.setImageView1(
            activityPostRectroReviewScreenBinding.imageOne
        )

        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)

        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

            for (i in posImageUrlList.indices) {
                if (posImageUrlList[i].stage.equals("1")) {
                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                            .contains("Pre")
                    ) {
                        PopUpWIndow(
                            this,
                            R.layout.layout_image_fullview,
                            activityPostRectroReviewScreenBinding.firstImageZoom,
                            posImageUrlList[i].url,
                            null,
                            "",
                            0
                        )
                    }
                } else if (posImageUrlList[i].stage.equals("2")) {
                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                            .contains("Post")
                    ) {
                        PopUpWIndow(
                            this,
                            R.layout.layout_image_fullview,
                            activityPostRectroReviewScreenBinding.firstImageZoom,
                            posImageUrlList.get(i).url,
                            null,
                            "",
                            0
                        )
                    }
                } else if (posImageUrlList.get(i).stage.equals("3")) {
                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                            .contains("After")
                    ) {
                        PopUpWIndow(
                            this,
                            R.layout.layout_image_fullview,
                            activityPostRectroReviewScreenBinding.firstImageZoom,
                            posImageUrlList[i].url,
                            null,
                            "",
                            0
                        )
                    }
                }
            }
        }
        activityPostRectroReviewScreenBinding.preRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    resources.getColor(
                        R.color.blue
                    )
                )
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                updateImages()
            } else {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    resources.getColor(
                        R.color.grey
                    )
                )
            }
        }
        activityPostRectroReviewScreenBinding.postRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    resources.getColor(R.color.blue)
                )
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                updateImages()
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    resources.getColor(R.color.grey)
                )

            }
        }
        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    resources.getColor(R.color.blue)
                )
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                updateImages()
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    resources.getColor(R.color.grey)
                )
            }
        }
        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

            for (i in posImageUrlList.indices) {
                if (posImageUrlList[i].stage.equals("1")) {
                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                            .contains("Pre")
                    ) {
                        PopUpWIndow(
                            this,
                            R.layout.layout_image_fullview,
                            activityPostRectroReviewScreenBinding.secondImageZoom,
                            posImageUrlList[i].url,
                            null,
                            "",
                            0
                        )
                    }
                } else if (posImageUrlList[i].stage.equals("2")) {
                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                            .contains("Post")
                    ) {
                        PopUpWIndow(
                            this,
                            R.layout.layout_image_fullview,
                            activityPostRectroReviewScreenBinding.secondImageZoom,
                            posImageUrlList.get(i).url,
                            null,
                            "",
                            0
                        )
                    }
                } else if (posImageUrlList.get(i).stage.equals("3")) {
                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                            .contains("After")
                    ) {
                        PopUpWIndow(
                            this,
                            R.layout.layout_image_fullview,
                            activityPostRectroReviewScreenBinding.secondImageZoom,
                            posImageUrlList[i].url,
                            null,
                            "",
                            0
                        )
                    }
                }
            }
        }
    }

    fun updateImages() {
        if (uploadStage.equals("reshootStage") && stage.equals("isAfterCompletionStage")) {
            if (activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                if (!imageUploaded) {
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.deleteIcon.visibility = View.GONE
                } else {
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.deleteIcon.visibility = View.VISIBLE
                }

            } else {
                activityPostRectroReviewScreenBinding.deleteIcon.visibility = View.GONE
                activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.GONE
            }
        }



        if (posImageUrlList.size == 1) {

            activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
            for (i in posImageUrlList.indices) {
                if (posImageUrlList.get(i).stage.equals("1")) {
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }

        } else if (posImageUrlList.size == 2) {

            activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
            activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
            for (i in posImageUrlList.indices) {
                if (posImageUrlList.get(i).stage.equals("1")) {
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }
            for (i in posImageUrlList.indices) {
                if (posImageUrlList.get(i).stage.equals("2")) {
                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    if (posImageUrlList.get(i).url != null) {
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    } else {
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
        } else if (posImageUrlList.size == 3) {
            updateCheckBoxImages()

            if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {

                    if (posImageUrlList.get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown) {
                        Glide.with(this).load(posImageUrlList[i].file)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(this).load(posImageUrlList[i].file)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList.get(i).stage.equals("3")) {
                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList.get(i).url != null) {
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }

                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown) {
                        Glide.with(this).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(this).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList.get(i).stage.equals("3")) {
                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList.get(i).url != null) {
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(this).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)

                        }


                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        activityPostRectroReviewScreenBinding.imageHeading1.text =
                            "Post Retro Image"
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Glide.with(this).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            }
        }
    }

    fun updateCheckBoxImages() {
        for (i in posImageUrlList.indices) {
            if (posImageUrlList.get(i).stage.equals("1")) {
                if (posImageUrlList.get(i).url != null) {
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                } else {
                    Glide.with(this).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }

            } else if (posImageUrlList.get(i).stage.equals("2")) {
                if (posImageUrlList.get(i).url != null) {
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                } else {
                    Glide.with(this).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                }

            } else if (posImageUrlList.get(i).stage.equals("3")) {
                if (posImageUrlList.get(i).url != null) {
                    Glide.with(this).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                } else {
                    Glide.with(this).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                }

            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun bottomStatusUpdate(status: String?) {
//        if (status.equals("0")) {
//            activityPostRectroReviewScreenBinding.imageStatus.text = "Pending"
//            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(getResources().getColor(R.color.material_amber_accent_700))
//        } else if (status.equals("1")) {
//            activityPostRectroReviewScreenBinding.imageStatus.text = "Approved"
//            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(getResources().getColor(R.color.dark_green))
//        } else if (status.equals("2")) {
//            activityPostRectroReviewScreenBinding.imageStatus.text = "Reshoot"
//            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(getResources().getColor(R.color.color_red))
//
//        }

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

    override fun onClickCamera(imageId: String, position: Int, subPos: Int) {
        uploadPosition = position
        uploadSubPosition = subPos
        this.imageId = imageId
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
    override fun onClickUpload(pos: Int, subPos: Int) {

        if (tempImageUrlList!!.get(pos).get(subPos).file != null) {
            for (i in mainImageUrlList.indices) {
                for (j in mainImageUrlList[i].groupingImageUrlList!!.indices) {
                    for (k in mainImageUrlList[i].groupingImageUrlList!![j].indices) {
                        if (tempImageUrlList!![pos].get(subPos).imageid.equals(
                                mainImageUrlList[i].groupingImageUrlList!![j].get(
                                    k
                                ).imageid
                            ) && !mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                .get(k).imageUploadStatusUpdate
                        ) {
                            mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                .get(k).imageUploadStatusUpdate = true
                        }

                    }
                }
            }
            val intent = Intent()
            intent.putExtra("imageUrlWithData", imageUrlWithData)
            intent.putExtra("mainImageUrlList", mainImageUrlList)
            intent.putExtra("stage", tempImageUrlList!!.get(pos).get(subPos).stage)


            if (uploadStage.equals("reshootStage")) {
                intent.putExtra("posImageUrlList", posImageUrlList)
                intent.putExtra("fileForReshoot", fileForReshoot)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(context, "Please upload the image", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onClickDelete(position: Int) {
        var file: String = ""
        cameraDialog = Dialog(this)
        cameraDialog.setContentView(R.layout.dialog_camera_delete)
        val close = cameraDialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            cameraDialog.dismiss()
        }
        val ok = cameraDialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {

            var pos = -1

            for (i in tempImageUrlList!!.get(position).indices) {
                if (tempImageUrlList!!.get(position).get(i).status.equals("9")) {
                    file = tempImageUrlList!!.get(position).get(i).file.toString()
                }
            }

            for (k in mainImageUrlList.indices) {
                for (l in mainImageUrlList.get(k).groupingImageUrlList!!.indices) {
                    for (m in mainImageUrlList.get(k).groupingImageUrlList!!.get(l).indices) {
                        if (mainImageUrlList.get(k).groupingImageUrlList!!.get(l).get(m).file!=null&&file.equals(mainImageUrlList.get(k).groupingImageUrlList!!.get(l).get(m).file.toString())){
                            mainImageUrlList.get(k).groupingImageUrlList!!.get(l).removeAt(m)

                            synchronized(mainImageUrlList.get(k).groupingImageUrlList!!.get(l)!!) {
                                mainImageUrlList.get(k).groupingImageUrlList!!.get(l)!!
                                    .notify()
                                previewImageAdapter?.notifyDataSetChanged()

                            }
                        }

                    }
                }
            }




//            for (n in tempImageUrlList!!.get(position).indices) {
//
//            for (k in mainImageUrlList.indices) {
//                for (l in mainImageUrlList.get(k).groupingImageUrlList!!.indices) {
//                    for (m in mainImageUrlList.get(k).groupingImageUrlList!!.get(l).indices) {
//
//                            if (tempImageUrlList!!.get(position).get(n).file != null&&mainImageUrlList.get(k).groupingImageUrlList!!.get(l).get(m).file!=null) {
//
//                                    if (mainImageUrlList.get(k).groupingImageUrlList!!.get(l).get(m).status.equals("9")) {
//                                        mainImageUrlList.get(k).groupingImageUrlList!!.get(l).removeAt(m)
//
//                                        synchronized(mainImageUrlList.get(k).groupingImageUrlList!!.get(l)!!) {
//                                            mainImageUrlList.get(k).groupingImageUrlList!!.get(l)!!
//                                                .notify()
//                                            previewImageAdapter?.notifyDataSetChanged()
//
//                                        }
//
//                                    }
//
//
//
//                            }
//
//                        }
//
//
//                    }
//                }
//            }


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
            imageUploaded = false
            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                resources.getColor(
                    R.color.grey
                )
            )

            if (stage.equals("isPostRetroStage")) {
                if (uploadStage.equals("reshootStage")) {
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageTwo)
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = false
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = false
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.VISIBLE
                } else {
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = false
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.GONE

                }
            } else if (stage.equals("isAfterCompletionStage")) {
                if (uploadStage.equals("reshootStage")) {
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    Glide.with(this).load(posImageUrlList.get(stagePos).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageTwo)
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.VISIBLE
                    if (activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                            View.VISIBLE
                    } else {
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.GONE
                    }

                } else {
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.GONE

                }
            }

            cameraDialog.dismiss()
        }

        cameraDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cameraDialog.show()
    }

    override fun onClickDeleteforPreRetro(position: Int) {
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile =
            File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        fileNameForCompressedImage = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)

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

    var stagePos = -1
    var fileForReshoot: File? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
            imageFromCameraFile?.length()
            val resizedImage = Resizer(this)
                .setTargetLength(1080)
                .setQuality(100)
                .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                .setOutputDirPath(
                    context.cacheDir.toString()
                )

                .setSourceImage(imageFromCameraFile)
                .resizedFile
            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                resources.getColor(
                    R.color.dark_green
                )
            )
            var imageUrl = GetImageUrlsModelApnaResponse.Category.ImageUrl()

//            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_LONG).show()
            imageUploaded = true
            activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
            if (uploadStage.equals("reshootStage")) {
                for (k in mainImageUrlList.indices) {
                    for (l in mainImageUrlList.get(k).groupingImageUrlList!!.indices) {
                        for (m in mainImageUrlList.get(k).groupingImageUrlList!!.get(l)!!.indices) {
                            if (imageId.equals(
                                    mainImageUrlList!!.get(k).groupingImageUrlList!!.get(l)
                                        .get(m).imageid
                                )
                            ) {
                                mainImageUrlList.get(k).groupingImageUrlList!!.get(l).get(m).file =
                                    resizedImage
                                mainImageUrlList.get(k).groupingImageUrlList!!.get(l).get(m).url =
                                    null

                            }


                        }
                    }

                }
                for (i in tempImageUrlList!!.indices) {
                    for (j in tempImageUrlList!!.get(i).indices) {

                        if (imageId.equals(tempImageUrlList!!.get(i).get(j).imageid)) {
                            tempImageUrlList!!.get(i).get(j).file = resizedImage

                            imageUploaded = true
                            tempImageUrlList!!.get(i).get(j).url = null
                            previewImageAdapter?.notifyDataSetChanged()
                            activityPostRectroReviewScreenBinding.previewImageViewpager.setCurrentItem(
                                i,
                                true
                            )
                            activityPostRectroReviewScreenBinding.previewImageViewpager.adapter =
                                previewImageAdapter
                        }

                    }

                }
            } else {
                imageUrl.file = resizedImage!!
                imageUrl.status = "9"
                if (stage.equals("isPreRetroStage")) {
                    imageUrl.stage = "1"
                    stagePos = 0
                } else if (stage.equals("isPostRetroStage")) {
                    imageUrl.stage = "2"
                    stagePos = 1
                } else {
                    imageUrl.stage = "3"
                    stagePos = 2
                }

                for (k in mainImageUrlList.indices) {
                    for (l in mainImageUrlList.get(k).groupingImageUrlList!!.indices) {

                        for (m in mainImageUrlList.get(k).groupingImageUrlList!!.get(l).indices) {
                            if (tempImageUrlList!!.get(uploadPosition)
                                    .get(uploadSubPosition).imageid!!.equals(
                                        mainImageUrlList.get(k).groupingImageUrlList!!.get(
                                            l
                                        ).get(m).imageid
                                    )
                            ) {

                                imageUrl.categoryid = mainImageUrlList.get(k).categoryid!!.toInt()
                                imageUrl.position =
                                    mainImageUrlList.get(k).groupingImageUrlList!!.get(l)
                                        .get(m).position
                                imageUrlWithData = imageUrl
                                mainImageUrlList.get(k).groupingImageUrlList!!.get(l)!!
                                    .add(imageUrl)
                                synchronized(
                                    mainImageUrlList.get(k).groupingImageUrlList!!.get(
                                        l
                                    )!!
                                ) {
                                    mainImageUrlList.get(k).groupingImageUrlList!!.get(l)!!
                                        .notify()
                                    previewImageAdapter?.notifyDataSetChanged()

                                }
                            }
                        }
                    }

                }


            }


//            if (uploadStage.equals("reshootStage"))
//            {
//                if (stage.equals("isPreRetroStage")) {
//                    posImageUrlList.get(0).file = resizedImage
//                    mainImageUrlList.get(pos).groupingImageUrlList!![imageClickedPos!!].get(0).file =
//                        resizedImage
//
////                    posImageUrlList.get(0).url=null
//                    stagePos = 0
//                } else if (stage.equals("isPostRetroStage")) {
//                    posImageUrlList.get(1).file = resizedImage
//                    mainImageUrlList.get(pos).groupingImageUrlList!![imageClickedPos!!].get(1).file =
//                        resizedImage
//
////                    posImageUrlList.get(1).url=null
//                    stagePos = 1
//                } else {
//                    posImageUrlList.get(2).file = resizedImage
//                    mainImageUrlList.get(pos).groupingImageUrlList!![imageClickedPos!!].get(2).file =
//                        resizedImage
//
////                    posImageUrlList.get(2).url=null
//                    stagePos = 2
//                }
//                fileForReshoot = resizedImage
//
//            }
//            else {
//
//                imageUrl.file = resizedImage!!
//                imageUrl.status = "9"
//                if (stage.equals("isPreRetroStage")) {
//                    imageUrl.stage = "1"
//                    stagePos = 0
//                } else if (stage.equals("isPostRetroStage")) {
//                    imageUrl.stage = "2"
//                    stagePos = 1
//                } else {
//                    imageUrl.stage = "3"
//                    stagePos = 2
//                }
//                imageUrl.categoryid = categoryid.toInt()
//                imageUrl.position = imageClickedPos
//                imageUrlWithData = imageUrl
//                mainImageUrlList.get(pos).groupingImageUrlList!![imageClickedPos!!].add(imageUrl)
//
//                posImageUrlList!!.add(imageUrl)
//
//                previewImageAdapter!!.notifyDataSetChanged()
//
//                activityPostRectroReviewScreenBinding.previewImageViewpager.setCurrentItem(0, true)
//
//                synchronized(mainImageUrlList.get(pos).groupingImageUrlList!!) {
//                    mainImageUrlList.get(pos).groupingImageUrlList!!.notify()
//
//                }
//            }

            if (stage.equals("isPreRetroStage")) {
                for (i in mainImageUrlList.indices) {
                    for (j in mainImageUrlList.get(i).groupingImageUrlList!!.indices) {
                        for (k in mainImageUrlList.get(i).groupingImageUrlList!!.get(j).indices) {


                            if (mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).stage.equals("2")
                            ) {
                                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled =
                                    false
                                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled =
                                    false
                                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.firstImageLayout.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.deleteIcon.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.imageHeading1.text =
                                    "Pre Retro Image"
                                if (!uploadStage.equals("reshootStage")) {
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.imageOne)
                                } else {
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.imageOne)

                                }

                            }


                        }
                    }


                }
            } else if (stage.equals("isPostRetroStage")) {
                for (i in mainImageUrlList.indices) {
                    for (j in mainImageUrlList.get(i).groupingImageUrlList!!.indices) {
                        for (k in mainImageUrlList.get(i).groupingImageUrlList!!.get(j).indices) {


                            if (mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).stage.equals("2")
                            ) {
                                activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled =
                                    false
                                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled =
                                    false
                                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.firstImageLayout.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.deleteIcon.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.imageHeading1.text =
                                    "Pre Retro Image"
                                if (!uploadStage.equals("reshootStage")) {
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.imageOne)
                                } else {
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.imageOne)

                                }

                            }


                            activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility =
                                View.VISIBLE
                            activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                                View.GONE
                            activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled =
                                false
                            activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled =
                                false
                            activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                                View.GONE
                            activityPostRectroReviewScreenBinding.secondImageLayout.visibility =
                                View.VISIBLE
                            activityPostRectroReviewScreenBinding.deleteIcon.visibility =
                                View.VISIBLE
                            activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                                View.GONE
                            activityPostRectroReviewScreenBinding.imageHeading2.text =
                                "Post Retro Image"
                            if (!uploadStage.equals("reshootStage")) {
                                Glide.with(this).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                                Glide.with(this).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.imageTwo)
                            } else {
                                Glide.with(this).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                                Glide.with(this).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.imageTwo)

                            }

                        }
                    }
                }
            } else if (stage.equals("isAfterCompletionStage")) {
                for (i in mainImageUrlList.indices) {
                    for (j in mainImageUrlList.get(i).groupingImageUrlList!!.indices) {
                        for (k in mainImageUrlList.get(i).groupingImageUrlList!!.get(j).indices) {
                            if (mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).stage.equals("3")
                            ) {
                                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked =
                                    true
                                activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                                    View.GONE
                                activityPostRectroReviewScreenBinding.secondImageLayout.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.deleteIcon.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                                    View.GONE
                                if (!uploadStage.equals("reshootStage")) {
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.imageTwo)
                                } else {
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                                    Glide.with(this).load(
                                        mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                            .get(k).file
                                    )
                                        .placeholder(R.drawable.thumbnail_image)
                                        .into(activityPostRectroReviewScreenBinding.imageTwo)

                                }

                                activityPostRectroReviewScreenBinding.imageHeading2.text =
                                    "After Completion Image"
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {


    }


    override fun onPageSelected(position: Int) {
        if (position == 0 || position == 1) {
            activityPostRectroReviewScreenBinding.categoryNumber.setText("1")

            activityPostRectroReviewScreenBinding.categoryName.setText("Signage")
        } else {
            activityPostRectroReviewScreenBinding.categoryNumber.setText("2")

            activityPostRectroReviewScreenBinding.categoryName.setText("Front Glass Facade Left and Right")

        }

    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    private var currentIndex = 0

    private fun performFunctionAfterLastPage() {
        val listSize = mainImageUrlList.size
        val categoryNameList = mainImageUrlList.map { it.categoryname }

        // Iterate over the list
        for (i in 0 until listSize) {
            val index = (currentIndex + i) % listSize
            val currentItem = mainImageUrlList[index]

            if (currentItem.categoryname == categoryName) {
                val nextIndex = (index + 1) % listSize
                if (mainImageUrlList[nextIndex].groupingImageUrlList!!.isNotEmpty()) {
                    categoryName = mainImageUrlList[nextIndex].categoryname!!
//                    previewImageAdapter = ComparisionPreviewImage(
//                        this, mainImageUrlList[nextIndex].groupingImageUrlList,
//                        this, uploadStage, mainImageUrlList, stage, status, imageUploaded,
//                        imageClickedPos!!
//                    )

                    activityPostRectroReviewScreenBinding.previewImageViewpager.adapter =
                        previewImageAdapter
                    activityPostRectroReviewScreenBinding.previewImageViewpager.setCurrentItem(
                        0,
                        true
                    )

                    // Update the current index
                    currentIndex = nextIndex
                    break // Exit the loop after performing the operation
                }
            }
        }
    }

}
