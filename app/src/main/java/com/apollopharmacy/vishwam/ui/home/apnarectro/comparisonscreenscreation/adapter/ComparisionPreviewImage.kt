package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ComparisionPreviewImageBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.comparisonscreenscreation.ComparisonScreenCreationCallBack
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.util.ArrayList


class ComparisionPreviewImage(
    val mContext: Context,
    val posImageUrlList: ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
    val callBack: ComparisonScreenCreationCallBack,
    val uploadStage: String?,
    val stage: String,
    val status: String,

    var imageUploaded: Boolean

) :
    PagerAdapter() {
    var checkBoxClickedCount: Int = 0

    @SuppressLint("SuspiciousIndentation")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val activityPostRectroReviewScreenBinding: ComparisionPreviewImageBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.comparision_preview_image,
                container,
                false
            )


        activityPostRectroReviewScreenBinding.reshootCamera.setOnClickListener {
            callBack.onClickCamera()
        }
        activityPostRectroReviewScreenBinding.reshootCameraPreRetro.setOnClickListener {
            callBack.onClickCamera()
        }

        activityPostRectroReviewScreenBinding.uploadnowbutton.setOnClickListener {
            callBack.onClickUpload()
        }
        activityPostRectroReviewScreenBinding.deleteIconPreRetro.setOnClickListener {
            callBack.onClickDelete()
        }
        activityPostRectroReviewScreenBinding.deleteIcon.setOnClickListener {
            callBack.onClickDelete()
        }
        if (status.contains("Approved")) {
            activityPostRectroReviewScreenBinding.imageStatus.setText("Approved")
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(ViswamApp.context.getColor(R.color.greenn))

        } else if (status.contains("Pending")) {
            activityPostRectroReviewScreenBinding.imageStatus.setText("Pending")

            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(ViswamApp.context.getColor(R.color.pending_color_for_apna))

        } else {
            activityPostRectroReviewScreenBinding.imageStatus.setText("Reshoot")

            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(ViswamApp.context.getColor(R.color.color_red))

        }
        activityPostRectroReviewScreenBinding.imageOne.setImageView2(
            activityPostRectroReviewScreenBinding.imageTwo
        )
        activityPostRectroReviewScreenBinding.imageTwo.setImageView1(
            activityPostRectroReviewScreenBinding.imageOne
        )
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

        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 200)

        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

            for (i in posImageUrlList.indices) {
                if (posImageUrlList[i].stage.equals("1")) {
                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                            .contains("Pre")
                    ) {
                        PopUpWIndow(
                            mContext,
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
                            mContext,
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
                            mContext,
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
                    Color.parseColor(
                        R.color.blue.toString()
                    )
                )
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                updateImages(activityPostRectroReviewScreenBinding)
            } else {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    Color.parseColor(
                        R.color.grey.toString()
                    )
                )
            }
        }
        activityPostRectroReviewScreenBinding.postRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    Color.parseColor(R.color.blue.toString())
                )
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                updateImages(activityPostRectroReviewScreenBinding)
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    Color.parseColor(R.color.grey.toString())
                )

            }
        }
        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    Color.parseColor(R.color.blue.toString())
                )
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                updateImages(activityPostRectroReviewScreenBinding)
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    Color.parseColor(R.color.grey.toString())
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
                            mContext,
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
                            mContext,
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
                            mContext,
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


        if (stage == "isPreRetroStage") {
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
                    Color.parseColor(
                        R.color.blue.toString()
                    )
                )
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = true

            }
        } else {
            if (posImageUrlList.size == 2 && uploadStage == "newUploadStage") {
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    Color.parseColor(R.color.grey.toString())
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
                    Color.parseColor(R.color.grey.toString())
                )
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    Color.parseColor(R.color.blue.toString())
                )


            } else if (posImageUrlList.size == 3) {
                activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    Color.parseColor(R.color.blue.toString())
                )
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                checkBoxClickedCount = 2
            }


        }




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
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }

        } else if (posImageUrlList.size == 2) {

            activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
            activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
            for (i in posImageUrlList.indices) {
                if (posImageUrlList.get(i).stage.equals("1")) {
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }
            for (i in posImageUrlList.indices) {
                if (posImageUrlList.get(i).stage.equals("2")) {
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    if (posImageUrlList.get(i).url != null) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    } else {
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    }

//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                }
            }
        } else if (posImageUrlList.size == 3) {
            updateCheckBoxImages(activityPostRectroReviewScreenBinding)

            if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {

                    if (posImageUrlList.get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown) {
                        Picasso.with(mContext).load(posImageUrlList[i].file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList[i].file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList.get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList.get(i).url != null) {
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }

                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList.get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList.get(i).url != null) {
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)

                        }


                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        activityPostRectroReviewScreenBinding.imageHeading1.text =
                            "Post Retro Image"
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            }
        }









        container.addView(activityPostRectroReviewScreenBinding.root)

        return activityPostRectroReviewScreenBinding.root
    }


    override fun getCount(): Int {
        return posImageUrlList.size
    }

    fun updateCheckBoxImages(activityPostRectroReviewScreenBinding: ComparisionPreviewImageBinding) {
        for (i in posImageUrlList.indices) {
            if (posImageUrlList.get(i).stage.equals("1")) {
                if (posImageUrlList.get(i).url != null) {
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                } else {
                    Picasso.with(mContext).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }

            } else if (posImageUrlList.get(i).stage.equals("2")) {
                if (posImageUrlList.get(i).url != null) {
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                } else {
                    Picasso.with(mContext).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                }

            } else if (posImageUrlList.get(i).stage.equals("3")) {
                if (posImageUrlList.get(i).url != null) {
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                } else {
                    Picasso.with(mContext).load(posImageUrlList.get(i).file)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                }

            }
        }
    }

    fun updateImages(activityPostRectroReviewScreenBinding: ComparisionPreviewImageBinding) {
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
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }

        } else if (posImageUrlList.size == 2) {

            activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
            activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
            for (i in posImageUrlList.indices) {
                if (posImageUrlList.get(i).stage.equals("1")) {
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
                    Picasso.with(mContext).load(posImageUrlList.get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }
            for (i in posImageUrlList.indices) {
                if (posImageUrlList.get(i).stage.equals("2")) {
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    if (posImageUrlList.get(i).url != null) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    } else {
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    }

//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                }
            }
        } else if (posImageUrlList.size == 3) {
            updateCheckBoxImages(activityPostRectroReviewScreenBinding)

            if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {

                    if (posImageUrlList.get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown) {
                        Picasso.with(mContext).load(posImageUrlList[i].file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList[i].file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList.get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList.get(i).url != null) {
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }

                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Picasso.with(mContext).load(posImageUrlList.get(i).file)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList.get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList.get(i).url != null) {
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Picasso.with(mContext).load(posImageUrlList.get(i).url)
                                .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)

                        }


                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("1")) {
                        activityPostRectroReviewScreenBinding.imageHeading1.text =
                            "Post Retro Image"
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                for (i in posImageUrlList.indices) {
                    if (posImageUrlList.get(i).stage.equals("2")) {
                        Picasso.with(mContext).load(posImageUrlList.get(i).url)
                            .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            }
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
        //super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}