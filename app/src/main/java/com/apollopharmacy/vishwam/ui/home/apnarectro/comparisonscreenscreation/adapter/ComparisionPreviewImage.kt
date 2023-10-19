package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

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
import java.util.ArrayList


class ComparisionPreviewImage(
    val mContext: Context,
    var posImageUrlList: MutableList<ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>?,
    val callBack: ComparisonScreenCreationCallBack,
    val uploadStage: String?,
    val mainImageUrlList: ArrayList<GetImageUrlsModelApnaResponse.Category>,
    val stage: String,
    val status: String,
    var imageUploaded: Boolean,
    var pos: Int

) : PagerAdapter() {
    var checkBoxClickedCount: Int = 0
    val colorGreen = ContextCompat.getColor(mContext, R.color.greenn)
    val colorGrey = ContextCompat.getColor(mContext, R.color.grey)
    var uploadPosition: Int = 0

    @SuppressLint("SuspiciousIndentation")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val activityPostRectroReviewScreenBinding: ComparisionPreviewImageBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.comparision_preview_image,
                container,
                false
            )

        if (!uploadStage.equals("reshootStage")) {

            activityPostRectroReviewScreenBinding.uploadCameraLayout.setOnClickListener {
                callBack.onClickCamera("", position, uploadPosition)
            }
        }





        activityPostRectroReviewScreenBinding.deleteIconPreRetro.setOnClickListener {
            callBack.onClickDelete(position)
        }
        activityPostRectroReviewScreenBinding.deleteIcon.setOnClickListener {
            callBack.onClickDelete(position)
        }
        if (status.contains("Approved")) {
            activityPostRectroReviewScreenBinding.imageStatus.setText("Approved")
            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(
                ViswamApp.context.getColor(
                    R.color.greenn
                )
            )

        }
        else if (status.contains("Pending")) {
            activityPostRectroReviewScreenBinding.imageStatus.setText("Pending")

            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(
                ViswamApp.context.getColor(
                    R.color.pending_color_for_apna
                )
            )

        }
        else {
            activityPostRectroReviewScreenBinding.imageStatus.setText("Reshoot")

            activityPostRectroReviewScreenBinding.imageStatus.setTextColor(
                ViswamApp.context.getColor(
                    R.color.color_red
                )
            )

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
                                Glide.with(mContext).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.imageOne)
                            }

//                            else {
//                                Glide.with(mContext).load(
//                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
//                                        .get(k).file
//                                )
//                                    .placeholder(R.drawable.thumbnail_image)
//                                    .into(activityPostRectroReviewScreenBinding.imageOne)
//
//                            }

                        }


                    }
                }


            }
        } 
        else if (stage.equals("isPostRetroStage")) {
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
                                View.GONE
                            activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                                View.GONE
                            activityPostRectroReviewScreenBinding.imageHeading1.text =
                                "Pre Retro Image"
                            if (!uploadStage.equals("reshootStage")&&mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).file!=null) {
                                activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                                    colorGreen
                                )
                                activityPostRectroReviewScreenBinding.deleteIcon.visibility = View.VISIBLE
                                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility =
                                    View.VISIBLE

                                Glide.with(mContext).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.imageOne)
                            } else {
                                Glide.with(mContext).load(
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
//                        activityPostRectroReviewScreenBinding.deleteIcon.visibility =
//                            View.VISIBLE
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                            View.GONE
                        activityPostRectroReviewScreenBinding.imageHeading2.text =
                            "Post Retro Image"
                        if (!uploadStage.equals("reshootStage")) {
                            Glide.with(mContext).load(
                                mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).file
                            )
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                            Glide.with(mContext).load(
                                mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).file
                            )
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                        } else {
                            Glide.with(mContext).load(
                                mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).file
                            )
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                            Glide.with(mContext).load(
                                mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).file
                            )
                                .placeholder(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)

                        }

                    }
                }
            }
        }
        else if (stage.equals("isAfterCompletionStage")) {
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

                            activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                                View.GONE
                            if (!uploadStage.equals("reshootStage")&& mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                    .get(k).file!=null) {
                                activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                                    colorGreen
                                )
                                activityPostRectroReviewScreenBinding.deleteIcon.visibility =
                                    View.VISIBLE
                                activityPostRectroReviewScreenBinding.uploadnowbutton.visibility =
                                    View.VISIBLE
                                Glide.with(mContext).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                                Glide.with(mContext).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.imageTwo)
                            } else {
                                Glide.with(mContext).load(
                                    mainImageUrlList.get(i).groupingImageUrlList!!.get(j)
                                        .get(k).file
                                )
                                    .placeholder(R.drawable.thumbnail_image)
                                    .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                                Glide.with(mContext).load(
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


        activityPostRectroReviewScreenBinding.preRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.blue)
                )

                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                updateImages(activityPostRectroReviewScreenBinding, position)
            } else {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.grey)

                )
            }
        }
        activityPostRectroReviewScreenBinding.postRectroCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.blue)
                )
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                updateImages(activityPostRectroReviewScreenBinding, position)
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.grey)
                )

            }
        }
        activityPostRectroReviewScreenBinding.afterCompletionCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.blue)
                )

                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                updateImages(activityPostRectroReviewScreenBinding, position)
            } else {
                activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.grey)
                )
            }
        }


        if (stage == "isPreRetroStage") {
            for (i in posImageUrlList!!.get(position).indices) {
                if (posImageUrlList!!.get(position).size == 1 && posImageUrlList!!.get(position)
                        .get(i).status!!.equals("2")
                ) {
                    activityPostRectroReviewScreenBinding.reshootCameraPreRetro.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.firstImageZoom.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )

                    lp.weight = .2f
                    activityPostRectroReviewScreenBinding.firstImageLayout.layoutParams = lp
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
                } else if (posImageUrlList!!.get(position).size == 1) {
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
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
                } else if (posImageUrlList!!.get(position).size == 2) {
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = false
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = false
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.GONE
                    checkBoxClickedCount = 2
                } else if (posImageUrlList!!.get(position).size == 3) {
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = true
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = true
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.GONE

                }
            }


        }
        else if (stage.equals("isPostRetroStage")) {
            for (i in posImageUrlList!!.get(position).indices) {

                if (posImageUrlList!!.get(position).size == 1 && uploadStage.equals("newUploadStage")) {
                    uploadPosition=i
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.GONE

                    activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                        "Upload Post Retro"
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
                } else if (posImageUrlList!!.get(position).size == 2 && posImageUrlList!!.get(
                        position
                    ).get(i).status.equals("2")
                ) {
                    uploadPosition=i

                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                        "Post Retro Image"
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
                } else if (posImageUrlList!!.get(position).size == 2) {
                    uploadPosition=i

                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = false
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = false
                    checkBoxClickedCount = 2
                } else if (posImageUrlList!!.get(position).size == 3) {
                    uploadPosition=i

                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = false
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.blue)
                    )

                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isEnabled = true
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isEnabled = true

                }
            }

        }
        else {

            for (i in posImageUrlList!!.get(position).indices) {

                if (posImageUrlList!!.get(position)
                        .size == 2 && uploadStage == "newUploadStage"
                ) {
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.grey)
                    )
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.blue)
                    )
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true

                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = false
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.GONE
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                        "Upload After Completion Retro"
                } else if (posImageUrlList!!.get(position)
                        .size == 3 && posImageUrlList!!.get(
                        position
                    ).get(i).status.equals("2")
                ) {
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.secondImageLayout.visibility =
                        View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadCameraLayout.visibility = View.GONE
                    activityPostRectroReviewScreenBinding.reshootCamera.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.uploadPostOrAfterImageText.text =
                        "After Completion Image"
                    activityPostRectroReviewScreenBinding.uploadnowbutton.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.postRectroCbLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.blue)
                    )
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.afterCompletionCbLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.blue)
                    )


                } else if (posImageUrlList!!.get(position).size == 3) {
                    activityPostRectroReviewScreenBinding.comparisonText.visibility = View.VISIBLE
                    activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.preRectroCbLayout.setBackgroundColor(
                        ContextCompat.getColor(mContext, R.color.blue)
                    )
                    activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked = true
                    activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked = false
                    checkBoxClickedCount = 2
                }


            }




            if (uploadStage.equals("reshootStage") && stage.equals("isAfterCompletionStage")) {


                if (activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                    if (!imageUploaded) {
                        activityPostRectroReviewScreenBinding.reshootCamera.visibility =
                            View.VISIBLE
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
        }

        if (posImageUrlList!!.get(position).size == 1) {
            for (i in posImageUrlList!!.get(position).indices) {
                activityPostRectroReviewScreenBinding.reshootCamera.setOnClickListener {
                    callBack.onClickCamera(posImageUrlList!!.get(position).get(i).imageid!!, i, 0)
                }
                activityPostRectroReviewScreenBinding.reshootCameraPreRetro.setOnClickListener {
                    callBack.onClickCamera(posImageUrlList!!.get(position).get(i).imageid!!, i, 0)
                }

                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                    if (posImageUrlList!!.get(position).get(i).url != null) {
                        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)
                                        .get(i).stage.equals("3")
                                ) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }

                        activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                            colorGrey
                        )

                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                    } else {
                        activityPostRectroReviewScreenBinding.uploadnowbutton.setOnClickListener {
                            callBack.onClickUpload(position, i)
                        }
                        activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                            colorGreen
                        )
                        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position).get(i).file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position).get(i).file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)
                                        .get(i).stage.equals("3")
                                ) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                    }

                }
            }

        }
        else if (posImageUrlList!!.get(position).size == 2) {
            for (i in posImageUrlList!!.get(position).indices) {
                activityPostRectroReviewScreenBinding.reshootCamera.setOnClickListener {
                    callBack.onClickCamera(posImageUrlList!!.get(position).get(i).imageid!!, i, 0)
                }
                activityPostRectroReviewScreenBinding.uploadnowbutton.setOnClickListener {
                    callBack.onClickUpload(position, i)
                }
                activityPostRectroReviewScreenBinding.reshootCameraPreRetro.setOnClickListener {
                    callBack.onClickCamera(posImageUrlList!!.get(position).get(i).imageid!!, i, 0)
                }
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
                if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                    if (posImageUrlList!!.get(position).get(i).url != null) {
                        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)
                                        .get(i).stage.equals("3")
                                ) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                    } else {
                        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position).get(i).file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position).get(i).file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)
                                        .get(i).stage.equals("3")
                                ) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                    }
                }

                if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    if (posImageUrlList!!.get(position).get(i).url != null) {
                        activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                            colorGrey
                        )
                        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)
                                        .get(i).stage.equals("3")
                                ) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    } else {
                        activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                            colorGreen
                        )
                        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position).get(i).file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position).get(i).file.toString(),
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)
                                        .get(i).stage.equals("3")
                                ) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    }

//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                }
            }
        }
        else if (posImageUrlList!!.get(position).size == 3) {
            for (i in posImageUrlList!!.get(position).indices) {
                activityPostRectroReviewScreenBinding.reshootCamera.setOnClickListener {
                    callBack.onClickCamera(posImageUrlList!!.get(position).get(i).imageid!!, i, 0)
                }
                activityPostRectroReviewScreenBinding.uploadnowbutton.setOnClickListener {
                    callBack.onClickUpload(position, i)
                }
                activityPostRectroReviewScreenBinding.reshootCameraPreRetro.setOnClickListener {
                    callBack.onClickCamera(posImageUrlList!!.get(position).get(i).imageid!!, i, 0)
                }
                updateCheckBoxImages(activityPostRectroReviewScreenBinding, position)

                if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                    activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                    activityPostRectroReviewScreenBinding.imageHeading2.text =
                        "Post Retro Image"
                    if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                        activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.firstImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }

                    }

                    if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                            for (i in posImageUrlList!!.get(position).indices) {
                                if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Pre")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("Post")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position).get(i).url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                } else if (posImageUrlList!!.get(position)
                                        .get(i).stage.equals("3")
                                ) {
                                    if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                            .contains("After")
                                    ) {
                                        PopUpWIndow(
                                            mContext,
                                            R.layout.layout_image_fullview,
                                            activityPostRectroReviewScreenBinding.secondImageZoom,
                                            posImageUrlList!!.get(position)[i].url,
                                            null,
                                            "",
                                            0
                                        )
                                    }
                                }
                            }
                        }

                    }

                } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                    activityPostRectroReviewScreenBinding.imageHeading1.text =
                        "Post Retro Image"
                    activityPostRectroReviewScreenBinding.imageHeading2.text =
                        "After Completion Image"
                    if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
                        if (posImageUrlList!!.get(position).get(i).url != null) {
                            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                                colorGrey
                            )
                            activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                                for (i in posImageUrlList!!.get(position).indices) {
                                    if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                                .contains("Pre")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.firstImageZoom,
                                                posImageUrlList!!.get(position)[i].url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                                .contains("Post")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.firstImageZoom,
                                                posImageUrlList!!.get(position).get(i).url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                                .contains("After")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.firstImageZoom,
                                                posImageUrlList!!.get(position)[i].url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    }
                                }
                            }
                            activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                                for (i in posImageUrlList!!.get(position).indices) {
                                    if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                                .contains("Pre")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.secondImageZoom,
                                                posImageUrlList!!.get(position)[i].url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                                .contains("Post")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.secondImageZoom,
                                                posImageUrlList!!.get(position).get(i).url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)
                                            .get(i).stage.equals("3")
                                    ) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                                .contains("After")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.secondImageZoom,
                                                posImageUrlList!!.get(position)[i].url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    }
                                }
                            }
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                        } else {
                            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                                colorGreen
                            )
                            activityPostRectroReviewScreenBinding.firstImageZoom.setOnClickListener {

                                for (i in posImageUrlList!!.get(position).indices) {
                                    if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                                .contains("Pre")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.firstImageZoom,
                                                posImageUrlList!!.get(position)[i].file.toString(),
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                                .contains("Post")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.firstImageZoom,
                                                posImageUrlList!!.get(position)
                                                    .get(i).file.toString(),
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)[i].stage.equals("3")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading1.text.toString()
                                                .contains("After")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.firstImageZoom,
                                                posImageUrlList!!.get(position)[i].url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    }
                                }
                            }
                            activityPostRectroReviewScreenBinding.secondImageZoom.setOnClickListener {

                                for (i in posImageUrlList!!.get(position).indices) {
                                    if (posImageUrlList!!.get(position)[i].stage.equals("1")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                                .contains("Pre")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.secondImageZoom,
                                                posImageUrlList!!.get(position)[i].file.toString(),
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)[i].stage.equals("2")) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                                .contains("Post")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.secondImageZoom,
                                                posImageUrlList!!.get(position)
                                                    .get(i).file.toString(),
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    } else if (posImageUrlList!!.get(position)
                                            .get(i).stage.equals("3")
                                    ) {
                                        if (activityPostRectroReviewScreenBinding.imageHeading2.text.toString()
                                                .contains("After")
                                        ) {
                                            PopUpWIndow(
                                                mContext,
                                                R.layout.layout_image_fullview,
                                                activityPostRectroReviewScreenBinding.secondImageZoom,
                                                posImageUrlList!!.get(position)[i].url,
                                                null,
                                                "",
                                                0
                                            )
                                        }
                                    }
                                }
                            }
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                        }

                    }


                    if (posImageUrlList!!.get(position)
                            .get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown
                    ) {

                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!![position].get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList!!.get(position).get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList!!.get(position).get(i).url != null) {
                            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                                colorGrey
                            )

                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        } else {
                            activityPostRectroReviewScreenBinding.uploadnowbutton.setBackgroundColor(
                                colorGreen
                            )

                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }


                    }
                } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                    activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                    activityPostRectroReviewScreenBinding.imageHeading2.text =
                        "After Completion Image"
                    if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }

                    if (posImageUrlList!!.get(position)
                            .get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown
                    ) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList!!.get(position).get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList!!.get(position).get(i).url != null) {
                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)

                        } else {
                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext)
                                .load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }

                    }
                } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {
                    if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                        activityPostRectroReviewScreenBinding.imageHeading1.text =
                            "Post Retro Image"
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }

                } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                    activityPostRectroReviewScreenBinding.imageHeading1.text =
                        "Post Retro Image"
                    if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }

                }
            }


        }

        container.addView(activityPostRectroReviewScreenBinding.root)

        return activityPostRectroReviewScreenBinding.root

    }

    override fun getCount(): Int {
        return posImageUrlList!!.size
    }

    fun updateCheckBoxImages(
        activityPostRectroReviewScreenBinding: ComparisionPreviewImageBinding,
        position: Int
    ) {
        for (i in posImageUrlList!!.get(position).indices) {
            if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                if (posImageUrlList!!.get(position).get(i).url != null) {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                } else {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }

            } else if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {

                if (posImageUrlList!!.get(position).get(i).url != null) {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                } else {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                }

            } else if (posImageUrlList!!.get(position).get(i).stage.equals("3")) {
                if (posImageUrlList!!.get(position).get(i).url != null) {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                } else {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                        .placeholder(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                }

            }
        }
    }


    fun updateImages(
        activityPostRectroReviewScreenBinding: ComparisionPreviewImageBinding,
        position: Int
    ) {
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



        if (posImageUrlList!!.get(position).size == 1) {

            activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
            for (i in posImageUrlList!!.get(position).indices) {
                if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }

        } else if (posImageUrlList!!.get(position).size == 2) {

            activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
            activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
            for (i in posImageUrlList!!.get(position).indices) {
                if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.imageOne)
                    Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                        .placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image)
                        .into(activityPostRectroReviewScreenBinding.preRetroCbImage)
                }
            }
            for (i in posImageUrlList!!.get(position).indices) {
                if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                    if (posImageUrlList!!.get(position).get(i).url != null) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    } else {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.postRetroCbImage)
                    }

//                    bottomStatusUpdate(posImageUrlList.get(i).status)
                }
            }
        } else if (posImageUrlList!!.get(position).size == 3) {
            updateCheckBoxImages(activityPostRectroReviewScreenBinding, position)

            if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "Post Retro Image"
                for (i in posImageUrlList!!.get(position).indices) {
                    if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList!!.get(position).indices) {
                    if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList!!.get(position).indices) {
                    if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList!!.get(position).indices) {

                    if (posImageUrlList!!.get(position)
                            .get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown
                    ) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position)[i].file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!!.get(position)[i].file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList!!.get(position).get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList!!.get(position).get(i).url != null) {
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        } else {
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                        }

                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked && activityPostRectroReviewScreenBinding.afterCompletionCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Pre Retro Image"
                activityPostRectroReviewScreenBinding.imageHeading2.text = "After Completion Image"
                for (i in posImageUrlList!!.get(position).indices) {
                    if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
                for (i in posImageUrlList!!.get(position).indices) {
                    if (posImageUrlList!!.get(position)
                            .get(i).stage.equals("3") && activityPostRectroReviewScreenBinding.deleteIcon.isShown
                    ) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageTwo)
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).file)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)
                    } else if (posImageUrlList!!.get(position).get(i).stage.equals("3")) {
//                        bottomStatusUpdate(posImageUrlList.get(i).status)
                        if (posImageUrlList!!.get(position).get(i).url != null) {
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.imageTwo)
                            Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                                .placeholder(R.drawable.thumbnail_image)
                                .error(R.drawable.thumbnail_image)
                                .into(activityPostRectroReviewScreenBinding.afterCompletionCbImage)

                        }


                    }
                }
            } else if (activityPostRectroReviewScreenBinding.preRectroCheckbox.isChecked) {
                for (i in posImageUrlList!!.get(position).indices) {
                    if (posImageUrlList!!.get(position).get(i).stage.equals("1")) {
                        activityPostRectroReviewScreenBinding.imageHeading1.text =
                            "Post Retro Image"
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            } else if (activityPostRectroReviewScreenBinding.postRectroCheckbox.isChecked) {
                activityPostRectroReviewScreenBinding.imageHeading1.text = "Post Retro Image"
                for (i in posImageUrlList!!.get(position).indices) {
                    if (posImageUrlList!!.get(position).get(i).stage.equals("2")) {
                        Glide.with(mContext).load(posImageUrlList!!.get(position).get(i).url)
                            .placeholder(R.drawable.thumbnail_image)
                            .error(R.drawable.thumbnail_image)
                            .into(activityPostRectroReviewScreenBinding.imageOne)
                    }
                }
            }
        }
    }


    override fun getItemPosition(`object`: Any): Int {
        super.getItemPosition(`object`)

        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}