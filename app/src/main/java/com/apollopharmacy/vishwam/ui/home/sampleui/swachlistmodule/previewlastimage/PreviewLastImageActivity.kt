package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPreviewLastImageBinding
import com.apollopharmacy.vishwam.databinding.DialogLastimagePreviewAlertBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage.adapter.PreviewImageViewPager

class PreviewLastImageActivity : AppCompatActivity(), PreviewLastImageCallback,
    ViewPager.OnPageChangeListener {
    lateinit var activityPreviewLastImageBinding: ActivityPreviewLastImageBinding
    private var pendingAndApproved: PendingAndApproved? = null
    private var getImageUrlsResponse: GetImageUrlsResponse? = null
    private var previewImageViewPager: PreviewImageViewPager? = null
    private var imageUrlsList = ArrayList<GetImageUrlsResponse.ImageUrl>()
    private var currentPosition: Int = 0

    val PREVIEW_LAST_IMAGE_ACTIVITY: Int = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPreviewLastImageBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview_last_image
        )
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
                imageUrl.isVerified = j.isVerified
                imageUrl.categoryname = i.categoryname
                imageUrl.mainCategoryId = i.categoryid
                imageUrlsList.add(imageUrl)
            }
        }
        val imageUrl = GetImageUrlsResponse.ImageUrl()
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
            this, imageUrlsList
        )

        activityPreviewLastImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityPreviewLastImageBinding.previewImageViewpager.adapter = previewImageViewPager
        activityPreviewLastImageBinding.previewImageViewpager.setCurrentItem(0, true)


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
            if (isAllVerified){
                currentPosition = position
                activityPreviewLastImageBinding.imageUrlModel = imageUrlsList.get(position)
                activityPreviewLastImageBinding.totalImages = "${position + 1}/${imageUrlsList.size - 1}"
                previewImageViewPager?.notifyDataSetChanged()
            }
            activityPreviewLastImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
        }else{
            currentPosition = position
            if (currentPosition != imageUrlsList.size - 1) {
                activityPreviewLastImageBinding.imageUrlModel = imageUrlsList.get(position)
                activityPreviewLastImageBinding.totalImages = "${position + 1}/${imageUrlsList.size - 1}"
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

    override fun onClickCompleted() {
        val intent = Intent()
        imageUrlsList.removeAt(imageUrlsList.size - 1)
        intent.putExtra("IMAGE_URLS_OBJECT", imageUrlsList)
        setResult(Activity.RESULT_OK, intent)
        finish()
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
            onClickCompleted()
        }
        dialogLastimagePreviewAlertBinding.cancelButton.setOnClickListener {
            imagesStatusAlertDialog.dismiss()
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