package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityUploadImagesPostretroBinding
import com.apollopharmacy.vishwam.databinding.DialogForImageUploadBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.*
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen.PostRectroReviewScreen
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreRectroReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter.ConfigApnaAdapterPostRetro
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter.ImagesUploadAdapterPostRetro
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import me.echodev.resizer.Resizer
import java.io.File
import java.util.stream.Collectors

class PostRetroUploadImagesActivity : AppCompatActivity(), PostRetroUploadImagesCallback, ImagesUploadAdapterPostRetro.CallbackInterface{
    lateinit var activityUploadImagesPostRetroBinding: ActivityUploadImagesPostretroBinding
    private var configApnaAdapterPostRetro: ConfigApnaAdapterPostRetro? = null
    private var postRetroUploadImagesViewModel: PostRetroUploadImagesViewModel?=null
    private lateinit var dialog: Dialog
    private var fragmentName:String =""
    private var retroid:String =""
    private var uploadedOn:String =""
    private var uploadedBy:String =""
    private var storeId:String=""
    private var stage: String =""
    private var fileNameForCompressedImage: String? = null
//    var configLst = ArrayList<ImgeDtcl>()
    var pos:Int=0
    var imageFromCameraFile: File? = null
    private var uploadedImageCount: Int = 0
    private var uploadStage: String?=null
    private var overallImageCount: Int = 0
    private var apnaConfigList = ArrayList<GetStoreWiseCatDetailsApnaResponse>()
    private lateinit var cameraDialog: Dialog
    private var getImageUrlsLists: GetImageUrlsModelApnaResponse?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadImagesPostRetroBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_upload_images_postretro
        )

        setUp()


    }

    private fun setUp() {
        activityUploadImagesPostRetroBinding.callback=this
        postRetroUploadImagesViewModel = ViewModelProvider(this)[PostRetroUploadImagesViewModel::class.java]
        fragmentName = intent.getStringExtra("fragmentName")!!
        retroid = intent.getStringExtra("retroid")!!
        stage = intent.getStringExtra("stage")!!
        uploadedOn = intent.getStringExtra("uploadedOn")!!
        uploadedBy = intent.getStringExtra("uploadedBy")!!
        storeId=intent.getStringExtra("storeId")!!
        uploadStage=intent.getStringExtra("uploadStage")!!


        if(stage.equals("isPreRetroStage")){
            if(uploadStage.equals("newUploadStage")){
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(context.getColor(R.color.white))
//            activityUploadImagesPostRetroBinding.preRetroPendingLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.lightt_blue))
                activityUploadImagesPostRetroBinding.reviewName.setText("Pre Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId
            }else if(uploadStage.equals("approvedStage")){
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(context.getColor(R.color.white))
//            activityUploadImagesPostRetroBinding.preRetroPendingLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.lightt_blue))
                activityUploadImagesPostRetroBinding.reviewName.setText("Pre Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.reportLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId

            }else if(uploadStage.equals("reshootStage")){
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(context.getColor(R.color.white))
//            activityUploadImagesPostRetroBinding.preRetroPendingLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.lightt_blue))
                activityUploadImagesPostRetroBinding.reviewName.setText("Pre Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility=View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId

            }

        }
        else if(stage.equals("isPostRetroStage")){
            if(uploadStage.equals("newUploadStage")){
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.reviewName.setText("Post Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.reportLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId
            }else if(uploadStage.equals("approvedStage")){
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.reviewName.setText("Post Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.reportLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId
            }else if(uploadStage.equals("reshootStage")){
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(context.getColor(R.color.white))
//            activityUploadImagesPostRetroBinding.preRetroPendingLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.lightt_blue))
                activityUploadImagesPostRetroBinding.reviewName.setText("Post Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility=View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId

            }

        }else{
            if(uploadStage.equals("newUploadStage")){
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.reviewName.setText("After Completion Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId
            }else if(uploadStage.equals("approvedStage")){
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.reviewName.setText("After Completion Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId
            }else if(uploadStage.equals("reshootStage")){
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(context.getColor(R.color.white))
//            activityUploadImagesPostRetroBinding.preRetroPendingLayout.setBackgroundColor(context.getColor(R.color.white))
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(context.getColor(R.color.lightt_blue))
                activityUploadImagesPostRetroBinding.reviewName.setText("After Completion Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility=View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility=View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility=View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text=uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text=uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text=storeId

            }

        }

        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            postRetroUploadImagesViewModel!!.getStoreWiseDetailsApna(this)

        } else {
            Toast.makeText(
                ViswamApp.context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }



    }


    class ImgeDtcl(var file: File?,  var categoryName: String, var postRetroUploaded: Boolean)

    override fun onClickUpload() {

        Utlis.showLoading(this)
        updateButtonValidation()
    }

    override fun onClickCancel() {
        onClickBack()
    }
    override fun onBackPressed() {
        onClickBack()
    }
    fun onClickBack() {
        if (uploadedImageCount > 0) {
            val imagesStatusAlertDialog = Dialog(this)
            val dialogBackPressedAllert: DialogForImageUploadBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(this), R.layout.dialog_for_image_upload, null, false
                )
            imagesStatusAlertDialog.setContentView(dialogBackPressedAllert.root)
//        dialogBackPressedAllert.alertTitle.visibility = View.GONE
//        dialogBackPressedAllert.messege.text = "Do you want to exit?"
//       imagesStatusAlertDialog.setCancelable(false)
            imagesStatusAlertDialog.getWindow()
                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBackPressedAllert.yesBtn.setOnClickListener {
                imagesStatusAlertDialog.dismiss()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            dialogBackPressedAllert.cancelButton.setOnClickListener {
                imagesStatusAlertDialog.dismiss()
            }
            dialogBackPressedAllert.close.setOnClickListener {
                imagesStatusAlertDialog.dismiss()
            }
            imagesStatusAlertDialog.show()
        } else {
            super.onBackPressed()
        }
    }

    private fun updateButtonValidation() {
        if (uploadedImageCount == overallImageCount) {
            activityUploadImagesPostRetroBinding.uploadnowbutton.background = (resources.getDrawable(R.drawable.greenbackground_for_buttons))


//            for (i in swacchApolloList.get(0).configlist?.indices!!) {
            postRetroUploadImagesViewModel!!.connectToAzure(
                apnaConfigList, this
            )

//            }


        } else {
            Toast.makeText(applicationContext, "Please upload all Images", Toast.LENGTH_SHORT)
                .show()
            Utlis.hideLoading()
        }
    }
    var configPositionDel: Int = 0
    var uploadPositionDel: Int = 0
    override fun deleteImageCallBack(configPosDelete: Int, deleteImagePosDelete: Int) {
        this.configPositionDel = configPosDelete
        this.uploadPositionDel = deleteImagePosDelete
        cameraDialog = Dialog(this)
        cameraDialog.setContentView(R.layout.dialog_camera_delete)
        val close = cameraDialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            cameraDialog.dismiss()
        }
        val ok = cameraDialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            apnaConfigList.get(0).configlist?.get(configPosDelete)?.imageDataDto?.get(
                deleteImagePosDelete
            )?.file =
                null
            apnaConfigList.get(0).configlist!!.get(configPosDelete).imageUploaded = false
            uploadedImageCount--
            configApnaAdapterPostRetro?.notifyDataSetChanged()
            checkAllImagesUploaded()
            cameraDialog.dismiss()
        }

        cameraDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cameraDialog.show()
    }

    override fun capturedImageReview(
        capturedImagepos: Int,
        capturedImage: File?,
        view: View,
        position: Int,
        categoryName: String?,
    ) {
        TODO("Not yet implemented")
    }

    private fun checkAllImagesUploaded() {
        activityUploadImagesPostRetroBinding.uploadedCount.text= uploadedImageCount.toString()
        activityUploadImagesPostRetroBinding.overAllCount.text = "/" +overallImageCount.toString()
        if (apnaConfigList.get(0).configlist != null) {
            for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
                for ((index1, value1) in apnaConfigList.get(0).configlist?.get(index)?.imageDataDto?.withIndex()!!) {
                    if (apnaConfigList.get(0).configlist!!.get(index).imageDataDto?.get(index1)?.file != null) {
                        apnaConfigList.get(0).configlist!!.get(index).imageUploaded = true
                    } else {
                        apnaConfigList.get(0).configlist!!.get(index).imageUploaded = false
                    }
                }

            }
        }


        var allImagesUploaded: Boolean = true
        for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
            if (apnaConfigList.get(0).configlist!!.get(index).imageUploaded == false) {
                allImagesUploaded = false
            }

            if (uploadedImageCount == overallImageCount) {
                activityUploadImagesPostRetroBinding.warningLayout.visibility= View.GONE
                activityUploadImagesPostRetroBinding.completedMessage.visibility= View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.background = (resources.getDrawable(R.drawable.greenbackground_for_buttons))
                activityUploadImagesPostRetroBinding.uploadedCount.setTextColor(getColor(R.color.dark_green))
            } else {
                activityUploadImagesPostRetroBinding.warningLayout.visibility= View.VISIBLE
                activityUploadImagesPostRetroBinding.completedMessage.visibility= View.GONE
                activityUploadImagesPostRetroBinding.uploadedCount.setTextColor(getColor(R.color.red))
                activityUploadImagesPostRetroBinding.uploadnowbutton.background = (resources.getDrawable(R.drawable.ashbackgrounf_for_buttons))
            }

        }
    }

    override fun onClickEyeImage() {
        if(fragmentName.equals("fromPreRectro")){
            val intent = Intent(applicationContext, PreRectroReviewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }else{
            val intent = Intent(applicationContext, PostRectroReviewScreen::class.java)
            intent.putExtra("fragmentName", fragmentName)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

    }

    override fun onClickImageView() {
        TODO("Not yet implemented")
    }

    override fun onClickImageView(stage: String) {
        if(stage.equals("isPreRetroStage")){
            val intent = Intent(applicationContext, PreRectroReviewActivity::class.java)
            intent.putExtra("fragmentName", fragmentName)
            intent.putExtra("stage", stage)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }else{
            val intent = Intent(applicationContext, PostRectroReviewScreen::class.java)
            intent.putExtra("fragmentName", fragmentName)
            intent.putExtra("stage", stage)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

    }

    override fun onSuccessImageIsUploadedInAzur(response: ArrayList<GetStoreWiseCatDetailsApnaResponse>) {
        apnaConfigList=response

        uploadApi()
    }

    override fun onSuccessSaveImageUrlsApi(saveImageUrlsResponse: SaveImageUrlsResponse) {
        if(saveImageUrlsResponse!=null && saveImageUrlsResponse.status==true){
            Utlis.hideLoading()
            dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_onsuccessupload_apna)
            val close = dialog.findViewById<LinearLayout>(R.id.close_apna)
            val textMessage = dialog.findViewById<TextView>(R.id.transaction_id_apna)
            textMessage.text = "Pre Retro is Submitted for Review \n Transaction id is: " + saveImageUrlsResponse.retroid
            close.setOnClickListener {
                dialog.dismiss()
                super.onBackPressed()
            }
            val ok = dialog.findViewById<RelativeLayout>(R.id.ok_apna)
            ok.setOnClickListener {
                dialog.dismiss()
                super.onBackPressed()
            }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }else{
            Toast.makeText(context, ""+saveImageUrlsResponse.message, Toast.LENGTH_SHORT).show()
            Utlis.hideLoading()
        }
    }

    override fun onFailureSaveImageUrlsApi(saveImageUrlsResponse: SaveImageUrlsResponse) {
        Toast.makeText(context, ""+saveImageUrlsResponse.message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
    }

    override fun onSuccessGetStoreWiseDetails(getStoreWiseResponse: GetStoreWiseCatDetailsApnaResponse) {
        if(getStoreWiseResponse!=null && getStoreWiseResponse.message=="success"){
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.GONE
            apnaConfigList.add(getStoreWiseResponse)
            for ((index, value) in getStoreWiseResponse.configlist!!.withIndex()) {
                val countUpload = value.categoryImageUploadCount?.toInt()
                var dtcl_list = ArrayList<GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl>()
                for (count in 1..countUpload!!) {
                    overallImageCount++
                    dtcl_list.add(GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl(null, count, "", 0))

                }
                apnaConfigList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

            }

            activityUploadImagesPostRetroBinding.uploadedCount.text= uploadedImageCount.toString()
            activityUploadImagesPostRetroBinding.overAllCount.text = "/" +overallImageCount.toString()
            if (NetworkUtil.isNetworkConnected(this)) {
                Utlis.showLoading(this)
                var submit = GetImageUrlsModelApnaRequest()
                submit.storeid = "16001"
                submit.retroId = "APRET1600120230428081840"

                postRetroUploadImagesViewModel!!.getImageUrl(submit, this)

            } else {
                Toast.makeText(
                    ViswamApp.context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
//            configApnaAdapterPostRetro =
//                ConfigApnaAdapterPostRetro(getImageUrlsLists!!.categoryList, apnaConfigList.get(0), this, this, stage)
//            val layoutManager = LinearLayoutManager(ViswamApp.context)
//            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.layoutManager = layoutManager
//            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.itemAnimator =
//                DefaultItemAnimator()
//            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.adapter = configApnaAdapterPostRetro
//
//            Utlis.hideLoading()
        }
        else{
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.VISIBLE
            Toast.makeText(context, "Store ID not Available", Toast.LENGTH_SHORT)
                .show()
            super.onBackPressed()
            Utlis.hideLoading()
        }


    }

    override fun onFailureStoreWiseDetails(value: GetStoreWiseCatDetailsApnaResponse) {
        activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.VISIBLE
        Toast.makeText(context, ""+value.message, Toast.LENGTH_SHORT).show()

        Utlis.hideLoading()
    }

    override fun onSuccessImageUrlsList(getImageUrlsList: GetImageUrlsModelApnaResponse, retroId: String) {
        if(getImageUrlsList!=null && getImageUrlsList.status.equals(true)){
            getImageUrlsLists=getImageUrlsList
            Utlis.hideLoading()
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.visibility=View.VISIBLE
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility=View.GONE






            for(i in getImageUrlsList.categoryList.indices){
                val retroIdsGroupedList: Map<Int, List<GetImageUrlsModelApnaResponse.Category.ImageUrl>> =
                    getImageUrlsList.categoryList.get(i).imageUrls.stream().collect(Collectors.groupingBy { w -> w.position })
//           getStorePendingApprovedList.getList.clear()
                Toast.makeText(context, ""+retroIdsGroupedList.size, Toast.LENGTH_SHORT).show()

                var getImageUrlListDummys =
                    java.util.ArrayList<java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>()
                for (entry in retroIdsGroupedList.entries) {
                    getImageUrlListDummys.addAll(listOf(entry.value as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>))
                }
                getImageUrlsLists!!.categoryList.get(i).groupingImageUrlList= getImageUrlListDummys as List<MutableList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>?

            }

            configApnaAdapterPostRetro =
                ConfigApnaAdapterPostRetro(
                    getImageUrlsLists!!.categoryList,
                    apnaConfigList.get(0),
                    this,
                    this,
                    stage
                )
            val layoutManager = LinearLayoutManager(ViswamApp.context)
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.layoutManager = layoutManager
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.itemAnimator =
                DefaultItemAnimator()
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.adapter = configApnaAdapterPostRetro

        }
        else{
            hideLoading()
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.visibility=View.GONE
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility=View.VISIBLE
        }
       }

    override fun onFailureImageUrlsList(value: GetImageUrlsModelApnaResponse) {

    }

    override fun onClickReshoot() {

    }

    private fun uploadApi() {
//        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//            Utlis.showLoading(this)
        var submit = SaveImagesUrlsRequest()
        submit.actionEvent = "SUBMIT"
        submit.storeid = Preferences.getSwachhSiteId()
        submit.userid = Preferences.getValidatedEmpId()
        if(stage.equals("isPostRetroStage")){
            submit.stage="2"
        }else{
            submit.stage="3"
        }

        var imageUrlsList = java.util.ArrayList<SaveImagesUrlsRequest.ImageUrl>()

        for (i in apnaConfigList.get(0).configlist!!.indices) {
            for (j in apnaConfigList.get(0).configlist!!.get(i).imageDataDto!!.indices) {
                var imageUrl = submit.ImageUrl()
                imageUrl.url =
                    apnaConfigList.get(0).configlist!!.get(i).imageDataDto?.get(j)?.base64Images
                imageUrl.categoryid = apnaConfigList.get(0).configlist!!.get(i).categoryId
                imageUrl.position=j
                imageUrlsList.add(imageUrl)
            }

        }
        submit.imageUrls = imageUrlsList
        postRetroUploadImagesViewModel!!.onUploadImagesApna(submit, this)

//        }
    }

    var configPosition: Int = 0
    var uploadPosition: Int = 0
    override fun onClickCameraIcon(configPosition: Int, uploadButtonPosition: Int) {
        this.configPosition = configPosition
        this.uploadPosition = uploadButtonPosition

        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()

        }
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
            if (resizedImage != null) {
                apnaConfigList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                    uploadPosition
                )?.file = resizedImage// resizedImage
            }

//


            configApnaAdapterPostRetro?.notifyDataSetChanged()
            apnaConfigList.get(0).configlist!!.get(configPosition).imageUploaded = true
            apnaConfigList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                uploadPosition
            )?.positionLoop = uploadPosition
            uploadedImageCount++

            checkAllImagesUploaded()

//            Utlis.showLoading(this)
//            viewModel.connectToAzure(
//                swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
//                    uploadPosition
//                )
//            )


        }


    }

}