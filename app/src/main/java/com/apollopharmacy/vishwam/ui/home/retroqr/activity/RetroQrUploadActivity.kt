package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrUploadBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter.UploadRackAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.ImageDto
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RetroQrUploadActivity : AppCompatActivity(), RetroQrUploadCallback {
    private lateinit var activityRetroQrUploadBinding: ActivityRetroQrUploadBinding
    private lateinit var viewModel: RetroQrUploadViewModel
    private lateinit var uploadRackAdapter: UploadRackAdapter
    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    var images = ArrayList<ImageDto>()
    var imagesList= ArrayList<StoreWiseRackDetails.StoreDetail>()

    var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrUploadBinding = DataBindingUtil.setContentView(this@RetroQrUploadActivity,
            R.layout.activity_retro_qr_upload)
        viewModel = ViewModelProvider(this)[RetroQrUploadViewModel::class.java]
        activityRetroQrUploadBinding.callback = this@RetroQrUploadActivity
        setUp()
    }

    private fun setUp() {
        viewModel.getStoreWiseRackDetails(this)
        // for demo
//        images.add(ImageDto(File(""), ""))
//        images.add(ImageDto(File(""), ""))
//        images.add(ImageDto(File(""), ""))
//        images.add(ImageDto(File(""), ""))
//        images.add(ImageDto(File(""), ""))
//        images.add(ImageDto(File(""), ""))
//        images.add(ImageDto(File(""), ""))
//        images.add(ImageDto(File(""), ""))


    }

    override fun onClickBackArrow() {
        finish()
    }

    override fun onClickSubmit() {
        if (updatedCount == imagesList.size) {



//            Toast.makeText(this@RetroQrUploadActivity, "Submitted Successfully", Toast.LENGTH_SHORT)
//                .show()
            finish()
        }
    }

    override fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails) {
        activityRetroQrUploadBinding.totalRackCount.text = storeWiseRackDetails.storeDetails!!.size.toString()
        imagesList= storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>

        uploadRackAdapter =
            UploadRackAdapter(this@RetroQrUploadActivity, this@RetroQrUploadActivity,
                imagesList
            )
        activityRetroQrUploadBinding.uploadRackRcv.adapter = uploadRackAdapter
        activityRetroQrUploadBinding.uploadRackRcv.layoutManager =
            LinearLayoutManager(this@RetroQrUploadActivity)    }
//    var file: File = File(filename)
    override fun onClickCameraIcon(position: Int) {
        this.position = position
        if (!checkPermission()) {
            askPermissions(100)
            return
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")
        compressedImageFileName = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context, ViswamApp.context.packageName + ".provider", imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }

    var updatedCount: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && imageFile != null) {
            var imageDto=ImageDto(imageFile as File,"")
            images.add(imageDto)
            imagesList[position].imageurl = (imageFile as File).toString()
            updatedCount++
            uploadRackAdapter.notifyItemChanged(position)
        }
        activityRetroQrUploadBinding.updatedCount.setText(updatedCount.toString())
        activityRetroQrUploadBinding.pendingCount.setText((images.size - updatedCount).toString())
        if (updatedCount == images.size) {
            activityRetroQrUploadBinding.lastUpdateLayout.visibility = View.VISIBLE
            activityRetroQrUploadBinding.lastUpdateDate.setText(LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd MMM, yyyy")))
            activityRetroQrUploadBinding.submitButton.setBackgroundColor(Color.parseColor("#209804"))
        } else {
            activityRetroQrUploadBinding.lastUpdateLayout.visibility = View.GONE
            activityRetroQrUploadBinding.submitButton.setBackgroundColor(Color.parseColor("#a1a1a1"))
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this@RetroQrUploadActivity, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrUploadActivity, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrUploadActivity, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrUploadActivity, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this@RetroQrUploadActivity, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrUploadActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrUploadActivity, Manifest.permission.CAMERA
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
}