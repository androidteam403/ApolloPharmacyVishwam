package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity

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
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityUploadNowButtonBinding
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity.adapter.ConfigAdapterSwach
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity.adapter.ImagesCardViewAdapter
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.apollopharmacy.vishwam.util.Utlis
import java.io.File

class UploadNowButtonActivity : AppCompatActivity(), ImagesCardViewAdapter.CallbackInterface {
    lateinit var activityUploadNowButtonBinding: ActivityUploadNowButtonBinding
    private var swacchApolloList = ArrayList<SwachModelResponse>()
    lateinit var viewModel: UploadNowButtonViewModel
    private lateinit var configListAdapter: ConfigAdapterSwach
    var imageFromCameraFile: File? = null
    private lateinit var dialog: Dialog
    public var allImagesUploadedSwach: Boolean = false
    private var overallImageCount: Int = 0
    private var uploadedImageCount: Int = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadNowButtonBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_upload_now_button
        )
        viewModel = ViewModelProvider(this)[UploadNowButtonViewModel::class.java]
        activityUploadNowButtonBinding.imageCountSwach.text=  uploadedImageCount.toString()+ "/" + overallImageCount

        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            Utlis.hideKeyPad(this)
            viewModel.swachImagesRegisters()
        } else {
            context?.let {
                Toast.makeText(
                    it,
                    ViswamApp.context.resources?.getString(R.string.label_network_error)
                        .toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }


        viewModel.swachhapolloModel.observeForever {

            if (it != null && it.status ?: null == true) {
                swacchApolloList.add(it)

                for ((index, value) in it.configlist!!.withIndex()) {
                    val countUpload = value.categoryImageUploadCount?.toInt()
                    var dtcl_list = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
                    for (count in 1..countUpload!!) {
                        overallImageCount++
                        dtcl_list.add(SwachModelResponse.Config.ImgeDtcl(null, count, "", 0))

                    }
                    swacchApolloList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

                }
                activityUploadNowButtonBinding.imageCountSwach.text= uploadedImageCount.toString() + "/" + overallImageCount
                configListAdapter =
                    ConfigAdapterSwach(it, swacchApolloList, this)
                val layoutManager = LinearLayoutManager(context)
                activityUploadNowButtonBinding.recyclerViewimageswach.layoutManager = layoutManager
                activityUploadNowButtonBinding.recyclerViewimageswach.itemAnimator =
                    DefaultItemAnimator()
                activityUploadNowButtonBinding.recyclerViewimageswach.adapter = configListAdapter


            }
            Utlis.hideLoading()
        }

        viewModel.commandsNewSwach.observeForever({
            configListAdapter.notifyDataSetChanged()
            Utlis.hideLoading()
            when (it) {
                is CommandsNewSwach.ImageIsUploadedInAzur -> {
                    for (i in swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto!!.indices) {
                        if (swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto?.get(
                                i
                            )?.positionLoop?.equals(it.filePath.positionLoop)!!
                        ) {
                            val imageDtcl = SwachModelResponse.Config.ImgeDtcl(
                                it.filePath.file,
                                it.filePath.integerButtonCount,
                                it.filePath.base64Images,
                                it.filePath.positionLoop
                            )
                            swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto?.set(
                                i,
                                imageDtcl
                            )


                            break
                        }

                    }


                }
            }
        })


        activityUploadNowButtonBinding.uploadnowbutton.setOnClickListener {
            updateButtonValidation()
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
            activityUploadNowButtonBinding.imageCountSwach.text= uploadedImageCount.toString() + "/" + overallImageCount
            activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#a6a6a6"));
            configListAdapter.notifyDataSetChanged()
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
        position: Int
    ) {
        PhotoPopupWindow(
            context, R.layout.layout_image_fullview, view,
            capturedImage.toString(), null
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
//            var capture: File? = null

//            frontview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))


            swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                uploadPosition
            )?.file = imageFromCameraFile
            configListAdapter.notifyDataSetChanged()
            swacchApolloList.get(0).configlist!!.get(configPosition).imageUploaded = true
            swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                uploadPosition
            )?.positionLoop = uploadPosition
            uploadedImageCount++
            activityUploadNowButtonBinding.imageCountSwach.text= uploadedImageCount.toString() + "/" + overallImageCount
            checkAllImagesUploaded()


        }
        Utlis.showLoading(this)
        viewModel.connectToAzure(
            swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                uploadPosition
            )
        )
        configListAdapter.notifyDataSetChanged()
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

            if (uploadedImageCount==overallImageCount) {
                activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#00a651"));
            } else {
                activityUploadNowButtonBinding.uploadnowbutton.setBackgroundColor(Color.parseColor("#a6a6a6"));
            }

        }
    }

    private fun updateButtonValidation() {
        if (uploadedImageCount == overallImageCount) {
            Toast.makeText(applicationContext, "UPLOADED", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Please upload all Images", Toast.LENGTH_SHORT)
                .show()
        }
    }
}