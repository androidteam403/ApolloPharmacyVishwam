package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reshootactivity

import android.R.attr.bitmap
import android.R.attr.orientation
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.apollopharmacy.vishwam.databinding.ActivityReShootBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reshootactivity.adapters.ImagesCardViewAdapterRes
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.CommandsNeww
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.ReShootActivityViewModel
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.adapters.OnClickStatusClickAdapter
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import me.echodev.resizer.Resizer
import java.io.File
import java.text.SimpleDateFormat


class ReShootActivity : AppCompatActivity(), ImagesCardViewAdapterRes.CallbackInterface,
    OnClickStatusClickAdapter.CallbackInterfaceOnClick {
    lateinit var activityreShootBinding: ActivityReShootBinding
    lateinit var viewModel: ReShootActivityViewModel
    private var getImageUrlsList = ArrayList<GetImageUrlModelResponse>()
    var imageFromCameraFile: File? = null
    var uploadedCount: Int = 0
    private lateinit var dialog: Dialog
    var overallreshootcount: Int = 0


    private lateinit var onClickStatusClickAdapter: OnClickStatusClickAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityreShootBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_re_shoot

        )
        viewModel = ViewModelProvider(this)[ReShootActivityViewModel::class.java]
        val swachId = intent.getStringExtra("swachhid")
        val status = intent.getStringExtra("status")
        val approvedDate = intent.getStringExtra("approvedDate")
        val storeId = intent.getStringExtra("storeId")
        val uploadedDate = intent.getStringExtra("uploadedDate")
        val reshootDate = intent.getStringExtra("reshootDate")
        val partiallyApprovedDate = intent.getStringExtra("partiallyApprovedDate")

        activityreShootBinding.storeId.text = storeId









        if (status == "APPROVED") {

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
                    context,
                    R.color.greenn
                )
            );
        } else if (status == "PARTIALLY APPROVED") {

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
                    context,
                    R.color.material_amber_accent_700
                )
            );
        } else if (status == "RESHOOT") {

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
                    context,
                    R.color.color_red
                )
            );
        } else if (status == "PENDING") {
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
                    context,
                    R.color.sea_blue
                )
            );
        }


        var submit = GetImageUrlModelRequest()
        submit.storeid = storeId
        submit.swachhId = swachId
        Utlis.showLoading(this)
        viewModel.getImageUrl(submit)

        viewModel.getImageUrlsList.observeForever {
            if (it != null && it.categoryList != null) {
                getImageUrlsList.add(it)


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

                }


                onClickStatusClickAdapter =
                    OnClickStatusClickAdapter(getImageUrlsList.get(0).categoryList, this, this)
                val layoutManager = LinearLayoutManager(context)
                activityreShootBinding.imageRecyclerViewRes.layoutManager = layoutManager
                activityreShootBinding.imageRecyclerViewRes.itemAnimator =
                    DefaultItemAnimator()
                activityreShootBinding.imageRecyclerViewRes.adapter = onClickStatusClickAdapter
                Utlis.hideLoading()
            } else {

                Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT).show()
            }
        }



        activityreShootBinding.reshootButton.setOnClickListener {
            reshootButton()
        }

        viewModel.uploadSwachModelRes.observeForever {
            if (it != null) {
                Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                super.onBackPressed()
            }

//            viewModel.onUploadSwachRes(submit)
//            val intent = Intent(context, UploadNowButtonActivity::class.java)
//            startActivity(intent)


        }
        activityreShootBinding.backButton.setOnClickListener {
            onBackPressed()
        }

        viewModel.commands.observeForever({

            when (it) {
                is CommandsNeww.ImageIsUploadedInAzur -> {


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
            }
        })


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
        imageFromCameraFile =
            File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
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
        grantResults: IntArray
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

    override fun onBackPressed() {

        exitDialog()

    }

    var urlPosition: Int = 0
    var configgpositionRes: Int = 0
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
        position: Int
    ) {
        PhotoPopupWindow(
            context, R.layout.layout_image_fullview, view,
            url.toString(), null
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


            val exif: ExifInterface = ExifInterface(imageFromCameraFile!!) //Since API Level 5

            val exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)


       

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




            Utlis.showLoading(this)
            viewModel.connectToAzure(resizedImage)


        }

    }


    private fun reshootButton() {
        if (uploadedCount == overallreshootcount) {
            if (NetworkUtil.isNetworkConnected(context)) {
                var submit = OnUploadSwachModelRequest()
                submit.actionEvent = "RESHOOT"
                submit.storeid = Preferences.getSiteId()
                submit.userid = Preferences.getToken()
                var imageUrlsList = ArrayList<OnUploadSwachModelRequest.ImageUrl>()

                for (i in getImageUrlsList.get(0).categoryList?.indices!!) {
                    for (j in getImageUrlsList.get(0).categoryList!!.get(i).imageUrls!!.indices) {
                        var imageUrl = submit.ImageUrl()
                        imageUrl.url =
                            getImageUrlsList.get(0).categoryList!!.get(i).imageUrls?.get(j)?.url
                        imageUrl.categoryid =
                            getImageUrlsList.get(0).categoryList!!.get(i).categoryname
                        imageUrlsList.add(imageUrl)
                    }

                }
                submit.imageUrls = imageUrlsList
                viewModel.onUploadSwachRes(submit)
            }
        } else {
            Toast.makeText(context, "Please re-shoot all the images", Toast.LENGTH_SHORT).show()
        }
    }


}