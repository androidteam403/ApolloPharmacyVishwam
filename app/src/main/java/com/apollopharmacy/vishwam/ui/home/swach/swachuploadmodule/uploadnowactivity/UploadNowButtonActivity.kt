package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
import com.apollopharmacy.vishwam.databinding.ActivityUploadNowButtonBinding
import com.apollopharmacy.vishwam.databinding.DialogForImageUploadBinding
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.adapter.ConfigAdapterSwach
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.adapter.ImagesCardViewAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.model.UpdateSwachhDefaultSiteRequest
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PopUpWIndow

import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import me.echodev.resizer.Resizer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UploadNowButtonActivity : AppCompatActivity(), ImagesCardViewAdapter.CallbackInterface {
    lateinit var activityUploadNowButtonBinding: ActivityUploadNowButtonBinding
    private var swacchApolloList = ArrayList<SwachModelResponse>()
    lateinit var viewModel: UploadNowButtonViewModel
    private var configListAdapter: ConfigAdapterSwach? = null
    var imageFromCameraFile: File? = null
    private lateinit var dialog: Dialog
    public var allImagesUploadedSwach: Boolean = false
    private var overallImageCount: Int = 0
    private var uploadedImageCount: Int = 0
    private var fileNameForCompressedImage: String? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadNowButtonBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_upload_now_button
        )
        viewModel = ViewModelProvider(this)[UploadNowButtonViewModel::class.java]

        activityUploadNowButtonBinding.storeId.text = Preferences.getSwachhSiteId()
        activityUploadNowButtonBinding.userId.text = Preferences.getToken()
        val sdf = SimpleDateFormat("dd MMM, yyyy")
        val currentDate = sdf.format(Date())

        activityUploadNowButtonBinding.todaysDate.text = currentDate
        activityUploadNowButtonBinding.imageCountSwach.text =
            uploadedImageCount.toString() + "/" + overallImageCount

//        if (NetworkUtil.isNetworkConnected(this)) {
        showLoadingTemp(this)
        viewModel.swachImagesRegisters()
//        } else {
//            context?.let {
//                Toast.makeText(
//                    it,
//                    ViswamApp.context.resources?.getString(R.string.label_network_error)
//                        .toString(),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//        }
        activityUploadNowButtonBinding.backButton.setOnClickListener {
            onClickBack()
        }



        viewModel.uploadSwachModel.observeForever {
            if (it != null && it.status == true) {
                // hideLoading()
                Toast.makeText(context, "Images Uploaded Successfully", Toast.LENGTH_SHORT).show()
                // onBackPressed()
                Preferences.setUploadedDateDayWise(currentDate)
                onSuccessUpdateSwachhDefualtSiteid()
                var updateSwachhDefaultSiteRequest = UpdateSwachhDefaultSiteRequest();
                updateSwachhDefaultSiteRequest.empId = Preferences.getValidatedEmpId()
                updateSwachhDefaultSiteRequest.site = Preferences.getSwachhSiteId()
                viewModel.updateSwachhSiteIdApiCall(updateSwachhDefaultSiteRequest)


            } else if (it != null && it.status == false && it.message == "ALREADY UPLAODED") {
                hideLoading()
                Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                Preferences.setUploadedDateDayWise(currentDate)
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
//                onBackPressed()
            } else {
                hideLoading()
                Toast.makeText(context, "Please try again!!", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.swachhapolloModel.observeForever {
            if (it != null && it.status ?: null == true) {
                hideLoadingTemp()
                swacchApolloList.add(it)
                activityUploadNowButtonBinding.noOrdersFound.visibility = View.GONE
                for ((index, value) in it.configlist!!.withIndex()) {
                    val countUpload = value.categoryImageUploadCount?.toInt()
                    var dtcl_list = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
                    for (count in 1..countUpload!!) {
                        overallImageCount++
                        dtcl_list.add(SwachModelResponse.Config.ImgeDtcl(null, count, "", 0))

                    }
                    swacchApolloList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

                }
                activityUploadNowButtonBinding.imageCountSwach.text =
                    uploadedImageCount.toString() + "/" + overallImageCount
                configListAdapter =
                    ConfigAdapterSwach(it, swacchApolloList, this, context)
                val layoutManager = LinearLayoutManager(context)
                activityUploadNowButtonBinding.recyclerViewimageswach.layoutManager = layoutManager
                activityUploadNowButtonBinding.recyclerViewimageswach.itemAnimator =
                    DefaultItemAnimator()
                activityUploadNowButtonBinding.recyclerViewimageswach.adapter = configListAdapter


            } else {
                activityUploadNowButtonBinding.noOrdersFound.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "Store ID not Available", Toast.LENGTH_SHORT)
                    .show()
                super.onBackPressed()
                hideLoadingTemp()
//                hideLoading()
            }
//            hideLoading()
        }
        var count: Int = 0
        viewModel.commandsNewSwach.observeForever({
            configListAdapter?.notifyDataSetChanged()
            hideLoading()
            when (it) {
                is CommandsNewSwachImp.ImageIsUploadedInAzur -> {

                    uploadApi()
//                    System.out.println(swac"test")
//                    for (i in swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto!!.indices) {
//                        for(i in it.filePath.indices){
//                            if (swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto?.get(
//                                    i
//                                )?.positionLoop?.equals(it.filePath.get(i).positionLoop)!!
//                            ) {
//                                val imageDtcl = SwachModelResponse.Config.ImgeDtcl(
//                                    it.filePath.get(i).file,
//                                    it.filePath.get(i).integerButtonCount,
//                                    it.filePath.get(i).base64Images,
//                                    it.filePath.get(i).positionLoop
//                                )
//                                swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto?.set(
//                                    i,
//                                    imageDtcl
//                                )
//
//
//                                break
//                            }
//                        }
//
//
//                    }
//


                }

            }
        })


        activityUploadNowButtonBinding.uploadnowbutton.setOnClickListener {
            showLoading(this)
            updateButtonValidation()

        }


    }

    private fun exitDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.exit_dialog)
        val close = dialog.findViewById<TextView>(R.id.no_btnExit)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnExit)
        ok.setOnClickListener {
            super.onBackPressed()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onBackPressed() {
        onClickBack()
//        val intent = Intent()
//        setResult(Activity.RESULT_OK, intent)
//        finish()
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
        }else{
            super.onBackPressed()
        }
    }

    private fun uploadApi() {

        if (NetworkUtil.isNetworkConnected(context)) {
            showLoading(this)
            var submit = OnUploadSwachModelRequest()
            submit.actionEvent = "SUBMIT"
            submit.storeid = Preferences.getSwachhSiteId()
            submit.userid = Preferences.getValidatedEmpId()
            var imageUrlsList = ArrayList<OnUploadSwachModelRequest.ImageUrl>()

            for (i in swacchApolloList.get(0).configlist!!.indices) {
                for (j in swacchApolloList.get(0).configlist!!.get(i).imageDataDto!!.indices) {
                    var imageUrl = submit.ImageUrl()
                    imageUrl.url =
                        swacchApolloList.get(0).configlist!!.get(i).imageDataDto?.get(j)?.base64Images
                    imageUrl.categoryid = swacchApolloList.get(0).configlist!!.get(i).categoryId
                    imageUrlsList.add(imageUrl)
                }

            }
            submit.imageUrls = imageUrlsList
            viewModel.onUploadSwach(submit)

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

    var configPositionDel: Int = 0
    var uploadPositionDel: Int = 0
    override fun deleteImageCallBack(configPosDelete: Int, deleteImagePosDelete: Int) {
        this.configPositionDel = configPosDelete
        this.uploadPositionDel = deleteImagePosDelete
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_camera_delete)
        val close = dialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            swacchApolloList.get(0).configlist?.get(configPosDelete)?.imageDataDto?.get(
                deleteImagePosDelete
            )?.file =
                null
            swacchApolloList.get(0).configlist!!.get(configPosDelete).imageUploaded = false
            uploadedImageCount--
            activityUploadNowButtonBinding.imageCountSwach.text =
                uploadedImageCount.toString() + "/" + overallImageCount
            activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#a6a6a6"));
            configListAdapter?.notifyDataSetChanged()
            checkAllImagesUploaded()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    override fun capturedImageReview(
        capturedImagepos: Int,
        capturedImage: File?,
        view: View,
        position: Int,
        categoryName: String?,
    ) {
//        PhotoPopupWindow(
//            context, R.layout.layout_image_fullview, view,
//            capturedImage.toString(), null
//        )

        PopUpWIndow(
            context, R.layout.layout_image_fullview, view,
            capturedImage.toString(), null, categoryName, position
        )

    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun askPermissions(PermissonCode: Int) {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ), PermissonCode
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Config.REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(
                        context,
                        context?.resources?.getString(R.string.label_permission_denied),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
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


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
    }


//    private fun openCamera() {
////        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////        val extStorageDirectory =
////            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
////                .toString()
////        imageFromCameraFile =
////            File(extStorageDirectory, "${System.currentTimeMillis()}.jpg")
////        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
////        } else {
////            val photoUri = FileProvider.getUriForFile(
////                context,
////                context.packageName + ".provider",
////                imageFromCameraFile!!
////            )
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
////        }
////        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
//
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        imageFromCameraFile =
//            File(ViswamApp.Companion.context.cacheDir, "${System.currentTimeMillis()}.jpg")
//        fileNameForCompressedImage = "${System.currentTimeMillis()}.jpg"
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
//        } else {
//            val photoUri = FileProvider.getUriForFile(
//                context,
//                context.packageName + ".provider",
//                imageFromCameraFile!!
//            )
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
//    }


    @RequiresApi(Build.VERSION_CODES.Q)
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

//            val fileSizeInBytesC: Long = imageFromCameraFile!!.length()

//          val fileSizeInKBC = fileSizeInBytesC / 1024
//
//           val fileSizeInMBC = fileSizeInKBC / 1024
//            val path: String =
//                Environment.getExternalStorageDirectory().toString() + "/CameraImages/exampleswach.jpg"
//            val file = File(path)
//            val outputFileUri = Uri.fromFile(file)
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
//
//            startActivityForResult(intent, imageFromCameraFile)

//            createImageFile()

//          saveToInternalStorage(imageFromCameraFile)


//        val compressedImageFile =  Compressor(this).compressToFile(imageFromCameraFile);
//
//           val  compressedImageBitmap =  Compressor(this).compressToBitmap(imageFromCameraFile);

//          val compressedImage =  Compressor(this)
//                .setMaxWidth(640)
//                .setMaxHeight(480)
//                .setQuality(100)
//                .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                .compressToFile(imageFromCameraFile);


//            val resizedImage = Resizer(this)
//                .setTargetLength(1080)
//                .setQuality(100)
//                .setOutputFormat("JPG")
////                .setOutputFilename(fileNameForCompressedImage)
//                .setOutputDirPath(
//                    ViswamApp.Companion.context.cacheDir.toString()
//                )
//
//                .setSourceImage(imageFromCameraFile)
//                .resizedFile
            // Environment.getExternalStoragePublicDirectory(
            //                        Environment.DIRECTORY_PICTURES
            //                    ).getAbsolutePath()
// Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES
//            val fileSizeInBytes: Long = resizedImage.length()
//

//            val fileSizeInKB = fileSizeInBytes / 1024
//
//            val fileSizeInMB = fileSizeInKB / 1024

            if (resizedImage != null) {
                swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                    uploadPosition
                )?.file = resizedImage// resizedImage
            }

//


            configListAdapter?.notifyDataSetChanged()
            swacchApolloList.get(0).configlist!!.get(configPosition).imageUploaded = true
            swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                uploadPosition
            )?.positionLoop = uploadPosition
            uploadedImageCount++
            activityUploadNowButtonBinding.imageCountSwach.text =
                uploadedImageCount.toString() + "/" + overallImageCount
            checkAllImagesUploaded()

//            Utlis.showLoading(this)
//            viewModel.connectToAzure(
//                swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
//                    uploadPosition
//                )
//            )
            configListAdapter?.notifyDataSetChanged()


        }


    }


    fun addImageToGallery(filePath: String?, context: Context) {

    }

    private fun checkAllImagesUploaded() {
        if (swacchApolloList.get(0).configlist != null) {
            for ((index, value) in swacchApolloList.get(0).configlist!!.withIndex()) {
                for ((index1, value1) in swacchApolloList.get(0).configlist?.get(index)?.imageDataDto?.withIndex()!!) {
                    if (swacchApolloList.get(0).configlist!!.get(index).imageDataDto?.get(index1)?.file != null) {
                        swacchApolloList.get(0).configlist!!.get(index).imageUploaded = true
                    } else {
                        swacchApolloList.get(0).configlist!!.get(index).imageUploaded = false
                    }
                }

            }
        }


        var allImagesUploaded: Boolean = true
        for ((index, value) in swacchApolloList.get(0).configlist!!.withIndex()) {
            if (swacchApolloList.get(0).configlist!!.get(index).imageUploaded == false) {
                allImagesUploaded = false
            }

            if (uploadedImageCount == overallImageCount) {
                activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#00a651"));
            } else {
                activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#a6a6a6"));
            }

        }
    }

    private fun updateButtonValidation() {
        if (uploadedImageCount == overallImageCount) {
            activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#00a651"));
//            Toast.makeText(applicationContext, "UPLOADED", Toast.LENGTH_SHORT).show()

//            for (i in swacchApolloList.get(0).configlist?.indices!!) {
            viewModel.connectToAzure(
                swacchApolloList)

//            }


        } else {
            Toast.makeText(applicationContext, "Please upload all Images", Toast.LENGTH_SHORT)
                .show()
            hideLoading()
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDir", MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }

//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val image = File.createTempFile(
//            imageFileName, /* prefix */
//            ".jpg", /* suffix */
//            storageDir      /* directory */
//        )
//
//        // Save a file: path for use with ACTION_VIEW intents
////        mCurrentPhotoPath = image.absolutePath
//        return image
//    }

    var mProgressDialogTemp: ProgressDialog? = null
    fun showLoadingTemp(context: Context) {
        hideLoadingTemp()
        mProgressDialogTemp = showLoadingDialogTemp(context)
    }

    fun hideLoadingTemp() {
        if (mProgressDialogTemp != null && mProgressDialogTemp!!.isShowing()) {
            mProgressDialogTemp!!.dismiss()
        }
    }

    fun showLoadingDialogTemp(context: Context?): ProgressDialog? {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    fun onSuccessUpdateSwachhDefualtSiteid() {
        viewModel.updateSwachhDefaultSiteResponseModel.observeForever {
            if (it != null && it.success ?: null == true) {
                hideLoading()
//                onBackPressed(

                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()


            }
        }
    }
}