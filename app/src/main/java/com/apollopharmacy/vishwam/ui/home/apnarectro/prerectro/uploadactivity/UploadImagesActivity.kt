package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity

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
import androidx.annotation.RequiresApi
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
import com.apollopharmacy.vishwam.databinding.ActivityUploadImagesBinding
import com.apollopharmacy.vishwam.databinding.DialogForImageUploadBinding
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImagesUrlsRequest
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen.PostRectroReviewScreen
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreRectroReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter.ConfigApnaAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter.ImagesUploadAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIDActivity
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import me.echodev.resizer.Resizer
import java.io.File

class UploadImagesActivity : AppCompatActivity(), UploadImagesCallback, ImagesUploadAdapter.CallbackInterface{
    lateinit var activityUploadImagesBinding: ActivityUploadImagesBinding
    private lateinit var uploadImagesViewModel: UploadImagesViewModel
    private var configApnaAdapter: ConfigApnaAdapter? = null
    private lateinit var dialog: Dialog
    private var fragmentName:String =""
    private var fileNameForCompressedImage: String? = null
    private lateinit var cameraDialog: Dialog
    var imageFromCameraFile: File? = null
    var pos:Int=0
    private var apnaConfigList = ArrayList<GetStoreWiseCatDetailsApnaResponse>()
    private var uploadedImageCount: Int = 0
    private var overallImageCount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadImagesBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_upload_images
        )

        setUp()


    }

    private fun setUp() {
        activityUploadImagesBinding.callback=this
        uploadImagesViewModel = ViewModelProvider(this)[UploadImagesViewModel::class.java]
        fragmentName = intent.getStringExtra("fragmentName")!!
//        Toast.makeText(applicationContext,""+fragmentName, Toast.LENGTH_SHORT).show()
//        activityUploadImagesBinding.storeId.text=Preferences.getApnaSiteId()
        activityUploadImagesBinding.incharge.text=Preferences.getToken()
        activityUploadImagesBinding.storeName.text=Preferences.getApnaSiteName()

        activityUploadImagesBinding.storeId.text=Preferences.getApnaSiteId()
        activityUploadImagesBinding.uploadedCount.text= uploadedImageCount.toString()
        activityUploadImagesBinding.overAllCount.text = "/" +overallImageCount.toString()

//        configLst!!.add(ImgeDtcl(null, "Signage"))
//        configLst!!.add(ImgeDtcl(null, "Front glass facade left and right"))
//        configLst!!.add(ImgeDtcl(null, "Merchadising of rack FMCG rack left and right"))
//        configLst!!.add(ImgeDtcl(null, "Service desk covering system"))
//        configLst!!.add(ImgeDtcl(null, "Pharma rack left and right"))

        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            uploadImagesViewModel.getStoreWiseDetailsApna(this)

        } else {
            Toast.makeText(
                ViswamApp.context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }






    }


    class ImgeDtcl(var file: File?,  var categoryName: String)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickUpload() {

        Utlis.showLoading(this)
        updateButtonValidation()

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

    override fun onClickBackIcon() {
       onClickBack()
    }


    override fun onSuccessGetStoreWiseDetails(getStoreWiseResponse: GetStoreWiseCatDetailsApnaResponse) {
        if(getStoreWiseResponse!=null && getStoreWiseResponse.message=="success"){
            activityUploadImagesBinding.noOrdersFound.visibility = View.GONE
            apnaConfigList.add(getStoreWiseResponse)
            for ((index, value) in getStoreWiseResponse.configlist!!.withIndex()) {
                val countUpload = value.categoryImageUploadCount?.toInt()
                var dtcl_list = ArrayList<GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl>()
                for (count in 1..countUpload!!) {
                    overallImageCount++
                    dtcl_list.add(GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl(null, count, "", 0, "",""))

                }
                apnaConfigList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

            }

            activityUploadImagesBinding.uploadedCount.text= uploadedImageCount.toString()
            activityUploadImagesBinding.overAllCount.text = "/" +overallImageCount.toString()



            configApnaAdapter =
                ConfigApnaAdapter(getStoreWiseResponse, apnaConfigList, this, this)
            val layoutManager = LinearLayoutManager(ViswamApp.context)
            activityUploadImagesBinding.categoryNameApnaRecyclerView.layoutManager = layoutManager
            activityUploadImagesBinding.categoryNameApnaRecyclerView.itemAnimator =
                DefaultItemAnimator()
            activityUploadImagesBinding.categoryNameApnaRecyclerView.adapter = configApnaAdapter

            Utlis.hideLoading()
        }else{
            activityUploadImagesBinding.noOrdersFound.visibility = View.VISIBLE
            Toast.makeText(applicationContext, "Store ID not Available", Toast.LENGTH_SHORT)
                .show()
            super.onBackPressed()
            Utlis.hideLoading()
        }

    }

    override fun onFailureStoreWiseDetails(value: GetStoreWiseCatDetailsApnaResponse) {
        activityUploadImagesBinding.noOrdersFound.visibility = View.VISIBLE
        Toast.makeText(applicationContext, ""+value.message, Toast.LENGTH_SHORT).show()

        Utlis.hideLoading()
    }

    override fun onClickCancel() {
       onClickBack()
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
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            val ok = dialog.findViewById<RelativeLayout>(R.id.ok_apna)
            ok.setOnClickListener {
                dialog.dismiss()
//                super.onBackPressed()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }else{
            Toast.makeText(applicationContext, ""+saveImageUrlsResponse.message, Toast.LENGTH_SHORT).show()
            Utlis.hideLoading()
        }

    }

    override fun onFailureSaveImageUrlsApi(saveImageUrlsResponse: SaveImageUrlsResponse) {
        Toast.makeText(applicationContext, ""+saveImageUrlsResponse.message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
    }

    private fun uploadApi() {
//        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//            Utlis.showLoading(this)
            var submit = SaveImagesUrlsRequest()
            submit.actionEvent = "SUBMIT"
            submit.storeid =Preferences.getApnaSiteId()
            submit.userid = Preferences.getToken()
            submit.stage="1"
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
            uploadImagesViewModel.onUploadImagesApna(submit, this)

//        }
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


            configApnaAdapter?.notifyDataSetChanged()
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
    private fun checkAllImagesUploaded() {
        activityUploadImagesBinding.uploadedCount.text= uploadedImageCount.toString()
        activityUploadImagesBinding.overAllCount.text = "/" +overallImageCount.toString()
        if (apnaConfigList.get(0).configlist != null) {
            for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
                for ((index1, value1) in apnaConfigList.get(0).configlist?.get(index)?.imageDataDto?.withIndex()!!) {
                    apnaConfigList.get(0).configlist!!.get(index).imageUploaded = apnaConfigList.get(0).configlist!!.get(index).imageDataDto?.get(index1)?.file != null
                }

            }
        }


        var allImagesUploaded: Boolean = true
        for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
            if (apnaConfigList.get(0).configlist!!.get(index).imageUploaded == false) {
                allImagesUploaded = false
            }

            if (uploadedImageCount == overallImageCount) {
                activityUploadImagesBinding.warningLayout.visibility=View.GONE
                activityUploadImagesBinding.completedMessage.visibility=View.VISIBLE
                activityUploadImagesBinding.uploadnowbutton.background = (resources.getDrawable(R.drawable.greenbackground_for_buttons))
                activityUploadImagesBinding.uploadedCount.setTextColor(getColor(R.color.dark_green))
            } else {
                activityUploadImagesBinding.warningLayout.visibility=View.VISIBLE
                activityUploadImagesBinding.completedMessage.visibility=View.GONE
                activityUploadImagesBinding.uploadedCount.setTextColor(getColor(R.color.red))
                activityUploadImagesBinding.uploadnowbutton.background = (resources.getDrawable(R.drawable.ashbackgrounf_for_buttons))
            }

        }
    }
    var configPosition: Int = 0
    var uploadPosition: Int = 0
    override fun plusIconAddImage(configPos: Int, uploadPos: Int) {
        this.configPosition = configPos
        this.uploadPosition = uploadPos
        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()

        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateButtonValidation() {
        if (uploadedImageCount == overallImageCount) {
            activityUploadImagesBinding.uploadnowbutton.background = (resources.getDrawable(R.drawable.greenbackground_for_buttons))


//            for (i in swacchApolloList.get(0).configlist?.indices!!) {
            uploadImagesViewModel.connectToAzure(
                apnaConfigList, this, false
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
            configApnaAdapter?.notifyDataSetChanged()
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

}