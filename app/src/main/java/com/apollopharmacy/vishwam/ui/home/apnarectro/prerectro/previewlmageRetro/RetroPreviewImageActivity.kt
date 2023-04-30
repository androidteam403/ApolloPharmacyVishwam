package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPreviewRetroImageBinding
import com.apollopharmacy.vishwam.databinding.DialogLastimagePreviewAlertBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter.RetroPreviewImage
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.previewlastimage.PreviewLastImageViewModel
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils

class RetroPreviewImageActivity : AppCompatActivity(), PreviewLastImageCallback,
    ViewPager.OnPageChangeListener {
    lateinit var activityPreviewImageBinding: ActivityPreviewRetroImageBinding
    private var previewImageAdapter: RetroPreviewImage? = null
    var list = ArrayList<String>()
    var categoryAndImageslist = ArrayList<String>()
    private lateinit var previewLastImageViewModel: ApnaPreviewLastImageViewModel
    private var currentPosition: Int = 0

    public var imageUrlList = java.util.ArrayList<GetImageUrlResponse.Category>()
    var position: Int = 0
    private var store: String = ""
    private var retroId: String = ""
    private var uploaddate: String = ""

    private var getImageUrlsResponse: GetImageUrlResponse? = null
    var imageUrlsList = ArrayList<GetImageUrlResponse.ImageUrl>()
    var categorysList = ArrayList<GetImageUrlResponse.Category>()
    var acceptRejectData = SaveAcceptRequest()
    var imageUrlResponse = SaveAcceptRequest.Imageurl()


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
            uploaddate = intent.getStringExtra("uploaddate")!!
            imageUrlList =
                intent.getSerializableExtra("imageUrlList") as java.util.ArrayList<GetImageUrlResponse.Category>
        }
        setUp()
    }

    private fun setUp() {
//        list.add("https://askapollopr.blob.core.windows.net/sampleimage/ReturnImage_3857527_20230403_154049.jpg")

        activityPreviewImageBinding.totalimages.setText("( " + (position + 1 / list.size + 1).toString() + "/" + list.size.toString() + " )")
        activityPreviewImageBinding.storeId.setText(store)
        activityPreviewImageBinding.categoty.setText(imageUrlList.get(position).categoryname)
        activityPreviewImageBinding.uploadedDate.setText(uploaddate)


        for (i in imageUrlList.indices) {
            for (j in imageUrlList[i].imageUrls!!.indices) {
                val imageUrl = GetImageUrlResponse.ImageUrl()
                imageUrl.url = imageUrlList[i].imageUrls!!.get(j).url
                imageUrl.status = imageUrlList[i].imageUrls!!.get(j).status
                imageUrl.remarks = imageUrlList[i].imageUrls!!.get(j).remarks
                imageUrl.categoryid = imageUrlList[i].imageUrls!!.get(j).categoryid
                imageUrl.imageid = imageUrlList[i].imageUrls!!.get(j).imageid
                imageUrl.isVerified= imageUrlList[i].imageUrls!!.get(j).isVerified

                imageUrl.retorautoid = imageUrlList[i].imageUrls!!.get(j).retorautoid
                imageUrlsList.add(imageUrl)
            }
        }
//
        activityPreviewImageBinding.imagename.setText(imageUrlsList[position].imageid)
        activityPreviewImageBinding.categorycount.setText((position + 1).toString())

        if (imageUrlsList.get(position).isVerified == true) {
            if (imageUrlsList.get(position).status.equals("1")){
                activityPreviewImageBinding.accept.alpha = 0.5f

            }else{
                activityPreviewImageBinding.accept.alpha = 1f

            }
            if (imageUrlsList.get(position).status.equals("2")){
                activityPreviewImageBinding.reshoot.alpha = 0.5f

            }else{
                activityPreviewImageBinding.reshoot.alpha = 1f

            }
        }
        previewImageAdapter = RetroPreviewImage(this, this, imageUrlsList)

        activityPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityPreviewImageBinding.previewImageViewpager.adapter = previewImageAdapter


    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        currentPosition = position


        activityPreviewImageBinding.imagename.setText(imageUrlsList[position].imageid)
        for (i in imageUrlList.indices) {
            for (j in imageUrlList[i].imageUrls!!.indices) {
                if (imageUrlList[i].imageUrls!![j].imageid.equals(imageUrlsList.get(position).imageid)) {
                    activityPreviewImageBinding.categoty.setText(imageUrlList.get(i).categoryname)
                    activityPreviewImageBinding.categorycount.setText((i + 1).toString())
                    imageUrlResponse.imageid = imageUrlsList.get(i).imageid
                    imageUrlResponse.statusid = imageUrlsList.get(i).status
                }

            }
        }




        if (currentPosition == imageUrlsList.size - 1) {
            if (imageUrlsList[imageUrlsList.size - 1].isVerified == true) {
                activityPreviewImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
            }

        } else {
            activityPreviewImageBinding.isLastPos = false
        }


        if (imageUrlsList.get(currentPosition).isVerified == true) {
            if (imageUrlsList.get(currentPosition).status.equals("1")){
                activityPreviewImageBinding.accept.alpha = 0.5f

            }else{
                activityPreviewImageBinding.accept.alpha = 1f

            }
            if (imageUrlsList.get(currentPosition).status.equals("2")){
                activityPreviewImageBinding.reshoot.alpha = 0.5f

            }else{
                activityPreviewImageBinding.reshoot.alpha = 1f

            }
        }



        activityPreviewImageBinding.totalimages.setText("( " + (position + 1 / list.size + 1).toString() + "/" + list.size.toString() + " )")
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


    }

    override fun onClickCompleted() {

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

            val intent = Intent()
            intent.putExtra("mainImagesList", imageUrlsList)
            setResult(Activity.RESULT_OK, intent)
            finish()
            imagesStatusAlertDialog.dismiss()
            finish()
        }
        dialogLastimagePreviewAlertBinding.cancelButton.setOnClickListener {
            imagesStatusAlertDialog.dismiss()
        }
        imagesStatusAlertDialog.show()

    }

    override fun onClickDelete() {
        when (imageUrlsList.size > currentPosition) {
            true -> {
                imageUrlsList.get(currentPosition).status = "0"
                imageUrlsList.get(currentPosition).isVerified = false

                var isAllVerified = false
                for (i in imageUrlsList) {
                    if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                        if (i.isVerified == true) {
                            isAllVerified = true
                        }
                    }
                }

                activityPreviewImageBinding.isAllComplete = isAllVerified


                if (currentPosition == imageUrlsList.size - 2) {
                    for (i in imageUrlsList) {
                        if (imageUrlsList.indexOf(i) != imageUrlsList.size - 1) {
                            if (i.isVerified == true) {
                                activityPreviewImageBinding.previewImageViewpager.setCurrentItem(
                                    imageUrlsList.indexOf(i), true
                                )
                                break
                            }
                        }
                    }
                    if (isAllVerified == false) {
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
            if (imageUrlsList[imageUrlsList.size - 1].isVerified == false) {
                activityPreviewImageBinding.isLastPos = currentPosition == imageUrlsList.size - 1
            }

        } else {
            activityPreviewImageBinding.isLastPos = false
        }
    }


    override fun onBackPressed() {
        onClickBack()
    }

}
