package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.reshootactivity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityReShootBinding
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.reshootactivity.adapters.ImagesCardViewAdapterRes
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.CommandsNeww
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.ReShootActivityViewModel
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.adapters.OnClickStatusClickAdapter
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.fileuploadswach.FileUploadSwach
import com.apollopharmacy.vishwam.util.fileuploadswach.FileUploadSwachCallback
import com.apollopharmacy.vishwam.util.fileuploadswach.FileUploadSwachModel
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import me.echodev.resizer.Resizer
import java.io.File
import java.text.SimpleDateFormat


class ReShootActivity : AppCompatActivity(), ImagesCardViewAdapterRes.CallbackInterface,
    OnClickStatusClickAdapter.CallbackInterfaceOnClick , FileUploadSwachCallback{
    lateinit var activityreShootBinding: ActivityReShootBinding
    lateinit var viewModel: ReShootActivityViewModel
    private var getImageUrlsList = ArrayList<GetImageUrlModelResponse>()
    var imageFromCameraFile: File? = null
    var uploadedCount: Int = 0
    private lateinit var dialog: Dialog
    var overallreshootcount: Int = 0
    var swachId: String? = null
    var urlPosition: Int = 0
    var configgpositionRes: Int = 0

    private lateinit var onClickStatusClickAdapter: OnClickStatusClickAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityreShootBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_re_shoot

        )
        viewModel = ViewModelProvider(this)[ReShootActivityViewModel::class.java]
        swachId = intent.getStringExtra("swachhid")
        val status = intent.getStringExtra("status")
        val approvedDate = intent.getStringExtra("approvedDate")
        val storeId = intent.getStringExtra("storeId")
        val uploadedDate = intent.getStringExtra("uploadedDate")
        val reshootDate = intent.getStringExtra("reshootDate")
        val partiallyApprovedDate = intent.getStringExtra("partiallyApprovedDate")

        activityreShootBinding.storeId.text = storeId
        activityreShootBinding.swachId.text = swachId



        if (status == "Approved") {

            if (uploadedDate != null && uploadedDate != "") {

                val strDate = uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
                activityreShootBinding.uploadedDate.text = dateNewFormat
            } else {
                activityreShootBinding.uploadedDate.text = "--"
            }
//            if (approvedDate != null && approvedDate != "") {
//
//                val strDate = approvedDate
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                val date = dateFormat.parse(strDate)
//                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
//                activityreShootBinding.uploadedDate.text = dateNewFormat
//            } else {
//                activityreShootBinding.uploadedDate.text = "--"
//            }
            activityreShootBinding.statusTop.text = status
            activityreShootBinding.statusTop.setTextColor(
                ContextCompat.getColor(
                    context, R.color.greenn
                )
            );
        } else if (status == "Partially Approved") {

            if (uploadedDate != null && uploadedDate != "") {

                val strDate = uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
                activityreShootBinding.uploadedDate.text = dateNewFormat
            } else {
                activityreShootBinding.uploadedDate.text = "--"
            }
//            if (partiallyApprovedDate != null && partiallyApprovedDate != "") {
//
//                val strDate = partiallyApprovedDate
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                val date = dateFormat.parse(strDate)
//                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
//                activityreShootBinding.uploadedDate.text = dateNewFormat
//            } else {
//                activityreShootBinding.uploadedDate.text = "--"
//            }
            activityreShootBinding.statusTop.text = status
            activityreShootBinding.statusTop.setTextColor(
                ContextCompat.getColor(
                    context, R.color.color_red
                )
            );
        } else if (status == "Reshoot") {

            if (uploadedDate != null && uploadedDate != "") {

                val strDate = uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
                activityreShootBinding.uploadedDate.text = dateNewFormat
            } else {
                activityreShootBinding.uploadedDate.text = "--"
            }
//            if (reshootDate != null && reshootDate != "") {
//
//                val strDate = reshootDate
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                val date = dateFormat.parse(strDate)
//                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
//                activityreShootBinding.uploadedDate.text = dateNewFormat
//            } else {
//                activityreShootBinding.uploadedDate.text = "--"
//            }
            activityreShootBinding.statusTop.text = status
            activityreShootBinding.statusTop.setTextColor(
                ContextCompat.getColor(
                    context, R.color.color_red
                )
            );
        } else if (status == "Pending") {
            if (uploadedDate != null && uploadedDate != "") {

                val strDate = uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)
                activityreShootBinding.uploadedDate.text = dateNewFormat
            } else {
                activityreShootBinding.uploadedDate.text = "--"
            }
            activityreShootBinding.statusTop.text = status
            activityreShootBinding.statusTop.setTextColor(
                ContextCompat.getColor(
                    context, R.color.material_amber_accent_700
                )
            );
        }


        var submit = GetImageUrlModelRequest()
        submit.storeid = Preferences.getSwachhSiteId()
        submit.swachhId = swachId

        showLoadingTemp(this)
        viewModel.getImageUrl(submit)

        viewModel.getImageUrlsList.observeForever {

            if (it != null && it.categoryList != null) {
                hideLoadingTemp()
                getImageUrlsList.add(it)

                if (it.remarks?.size!! > 0 && it.remarks?.get(0)?.remarks != "") {
                    activityreShootBinding.comments.text = it.remarks?.get(0)?.remarks
                }


//                getImageUrlsList.get(0).categoryList?.get(0)?.imageUrls?.get(0)?.status = "2"
//                getImageUrlsList.get(0).categoryList?.get(1)?.imageUrls?.get(0)?.status = "1"


                for (i in getImageUrlsList.get(0).categoryList?.indices!!) {
                    for (j in getImageUrlsList.get(0).categoryList!!.get(i).imageUrls?.indices!!) {
                        if (getImageUrlsList.get(0).categoryList!!.get(i).imageUrls?.get(j)?.status.equals(
                                "2"
                            )
                        ) {
                            overallreshootcount++
                        }
                    }
                }
                if (overallreshootcount > 0) {
                    activityreShootBinding.reshootButton.visibility = View.VISIBLE
                    activityreShootBinding.countOfIages.setText(uploadedCount.toString() + "" + "/" + overallreshootcount)
                    if (uploadedCount == overallreshootcount) {
                        activityreShootBinding.reshootButton.setBackgroundColor(Color.parseColor("#ed1c24"));
                    } else {
                        activityreShootBinding.reshootButton.setBackgroundColor(Color.parseColor("#a6a6a6"));
                    }

                } else {
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    params.setMargins(0, 0, 0, 0)
                    activityreShootBinding.scrollViewReShoot.setLayoutParams(params)
                }


                onClickStatusClickAdapter =
                    OnClickStatusClickAdapter(getImageUrlsList.get(0).categoryList, this, this)
                val layoutManager = LinearLayoutManager(context)
                activityreShootBinding.imageRecyclerViewRes.layoutManager = layoutManager
//                activityreShootBinding.imageRecyclerViewRes.itemAnimator =
//                    DefaultItemAnimator()
                activityreShootBinding.imageRecyclerViewRes.adapter = onClickStatusClickAdapter

            } else {
                hideLoadingTemp()
                Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT).show()
            }


        }



        activityreShootBinding.reshootButton.setOnClickListener {
            if (uploadedCount == overallreshootcount){
//                activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#00a651"));
                if (getImageUrlsList != null && getImageUrlsList.get(0).categoryList!!.size>0) {
                    var fileUploadModelList = java.util.ArrayList<FileUploadSwachModel>()

                    for(i in getImageUrlsList!!.get(0).categoryList!!!!) {
                        if (i.imageUrls != null) {
                            for (j in i.imageUrls!!) {
                                if(j.status.equals("2")){
                                    if (j.file != null) {
                                        var fileUploadModel = FileUploadSwachModel()
                                        fileUploadModel.file = j.file
                                        fileUploadModel.categoryName = i.categoryname
                                        fileUploadModel.reshootCategoryPos= i.categoryPosForUrl
                                        fileUploadModel.reshootUrlPos= j.imagePosForUrl
                                        fileUploadModelList.add(fileUploadModel)
                                    }
                                }


                            }
                        }
                    }

                    FileUploadSwach().uploadFiles(
                        context,
                        this,
                        fileUploadModelList
                    )
                }

            } else {
                Toast.makeText(applicationContext, "Please reshoot all Images", Toast.LENGTH_SHORT)
                    .show()
                Utlis.hideLoading()
            }
//            reshootButton()
        }

        viewModel.uploadSwachModelRes.observeForever {
            if (it != null) {
                Toast.makeText(context, "Images Uploaded Successfully", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }

//            viewModel.onUploadSwachRes(submit)
//            val intent = Intent(context, UploadNowButtonActivity::class.java)
//            startActivity(intent)


        }
        activityreShootBinding.backButton.setOnClickListener {
            super.onBackPressed()
        }

        viewModel.commands.observeForever {

            when (it) {
                is CommandsNeww.ImageIsUploadedInAzur -> {

//                    getImageUrlsList.removeAt(Integer.parseInt(getImageUrlsList.get(0).categoryList?.get(configgpositionRes)?.imageUrls?.get(
//                        urlPosition).toString()))


                    getImageUrlsList.get(0).categoryList?.get(configgpositionRes)?.imageUrls?.get(
                        urlPosition
                    )?.url = it.filePath
                    getImageUrlsList.get(0).categoryList?.get(configgpositionRes)?.imageUrls?.get(
                        urlPosition
                    )?.isReshootStatus = true
                    uploadedCount++
                    activityreShootBinding.countOfIages.setText(uploadedCount.toString() + "" + "/" + overallreshootcount)
                    if (uploadedCount == overallreshootcount) {
                        activityreShootBinding.reshootButton.setBackgroundColor(Color.parseColor("#ed1c24"));
                    } else {
                        activityreShootBinding.reshootButton.setBackgroundColor(Color.parseColor("#a6a6a6"));
                    }

                    onClickStatusClickAdapter.notifyDataSetChanged()
                    Utlis.hideLoading()
                }
                else -> {}
            }
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

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                context, context.packageName + ".provider", imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }


//        getImageUrlsList.removeAt(Integer.parseInt((getImageUrlsList[0].categoryList?.get(configgpositionRes)?.imageUrls?.get(urlPosition)).toString()))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }


    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
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
                    ).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()

        setResult(Activity.RESULT_OK, intent)
        finish()

//        exitDialog()

    }


    override fun onClickCamera(position: Int, configPositionRes: Int) {
        this.urlPosition = position
        this.configgpositionRes = configPositionRes

        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {
            openCamera()
        }


    }


    var configPositionDel: Int = 0
    var urlPositionDel: Int = 0
    override fun deleteImageCallBackRes(configPositionRes: Int, position: Int) {
        this.configPositionDel = configPositionRes
        this.urlPositionDel = position
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_camera_delete)
        val close = dialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            getImageUrlsList.get(0).categoryList?.get(configPositionDel)?.imageUrls?.get(
                urlPositionDel
            )?.url = ""
            getImageUrlsList.get(0).categoryList?.get(configPositionDel)?.imageUrls?.get(
                urlPositionDel
            )?.isReshootStatus = false
            onClickStatusClickAdapter.notifyDataSetChanged()
            uploadedCount--
            activityreShootBinding.countOfIages.setText(uploadedCount.toString() + "" + "/" + overallreshootcount)
            activityreShootBinding.reshootButton.setBackgroundColor(Color.parseColor("#a6a6a6"));

            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun capturedImageReviewRes(
        configPositionRes: Int,
        url: String?,
        view: View,
        position: Int,
        category: String,
    ) {
//        PopUpWIndow(
//            context, R.layout.layout_image_fullview, view,
//            url.toString(), null,category,position
//        )

        PopUpWIndow(
            context, R.layout.popup_imageview, view, url.toString(), null, category, position
        )


//
//        categoryName?.visibility=View.VISIBLE
//        categoryName?.text=getImageUrlsList.get(0).categoryList?.get(configPositionRes)?.categoryname

//        val categoryNae : TextView = findViewById(R.id.category_naePop)as TextView


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {


//            val exif: ExifInterface = ExifInterface(imageFromCameraFile!!) //Since API Level 5
//
//            val exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)


            val resizedImage =
                Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                    .setOutputDirPath(
                        ViswamApp.Companion.context.cacheDir.toString()
                    )

                    .setSourceImage(imageFromCameraFile).resizedFile

        getImageUrlsList.get(0).categoryList!!.get(configgpositionRes).imageUrls!!.get(urlPosition).file=resizedImage
            getImageUrlsList.get(0).categoryList!!.get(configgpositionRes).categoryPosForUrl=  configgpositionRes
            getImageUrlsList.get(0).categoryList!!.get(configgpositionRes).imageUrls!!.get(urlPosition).imagePosForUrl= urlPosition
            getImageUrlsList.get(0).categoryList?.get(configgpositionRes)?.imageUrls?.get(
                urlPosition
            )?.isReshootStatus = true
            uploadedCount++
            activityreShootBinding.countOfIages.setText(uploadedCount.toString() + "" + "/" + overallreshootcount)
            if (uploadedCount == overallreshootcount) {
                activityreShootBinding.reshootButton.setBackgroundColor(Color.parseColor("#ed1c24"));
            } else {
                activityreShootBinding.reshootButton.setBackgroundColor(Color.parseColor("#a6a6a6"));
            }
            onClickStatusClickAdapter.notifyDataSetChanged()
//            Utlis.showLoading(this)
////            viewModel.connectToAzure(imageFromCameraFile)
//
//            viewModel.connectToAzure(resizedImage)


        }

    }


    private fun reshootButton() {
        if (uploadedCount == overallreshootcount) {
            if (NetworkUtil.isNetworkConnected(context)) {
                var submit = OnUploadSwachModelRequest()
                submit.actionEvent = "RESHOOT"
                submit.storeid = Preferences.getSwachhSiteId()
                submit.userid = Preferences.getToken()
                submit.swachhIdRehoot = swachId
                submit.status = "0"

                var imageUrlsList = ArrayList<OnUploadSwachModelRequest.ImageUrl>()

                for (i in getImageUrlsList.get(0).categoryList?.indices!!) {
                    for (j in getImageUrlsList.get(0).categoryList!!.get(i).imageUrls!!.indices) {
                        if (getImageUrlsList.get(0).categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "2"
                            )
                        ) {
                            var imageUrl = submit.ImageUrl()
                            imageUrl.url =
                                getImageUrlsList.get(0).categoryList!!.get(i).imageUrls?.get(j)?.url
                            imageUrl.categoryid =
                                getImageUrlsList.get(0).categoryList!!.get(i).categoryid
                            imageUrl.imageId =
                                getImageUrlsList.get(0).categoryList!!.get(i).imageUrls?.get(j)?.imageid
                            imageUrlsList.add(imageUrl)
                        }
                    }

                }
                submit.imageUrls = imageUrlsList
                viewModel.onUploadSwachRes(submit)
            }
        } else {
            Toast.makeText(context, "Please re-shoot all the images", Toast.LENGTH_SHORT).show()
        }
    }

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

    override fun onFailureUpload(message: String) {
        Utlis.hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun allFilesDownloaded(fileUploadModelList: List<FileUploadSwachModel>) {
        for(i in fileUploadModelList){
            for(j in getImageUrlsList.get(0)!!.categoryList!!){
                if(i.categoryName.equals(j.categoryname)){
                    j.imageUrls!!.get(i.reshootUrlPos!!).url =  RijndaelCipherEncryptDecrypt().decrypt(i.fileDownloadResponse!!.referenceurl, "blobfilesload")
                }
            }
        }
        onClickStatusClickAdapter.notifyDataSetChanged()
        reshootButton()
    }

}