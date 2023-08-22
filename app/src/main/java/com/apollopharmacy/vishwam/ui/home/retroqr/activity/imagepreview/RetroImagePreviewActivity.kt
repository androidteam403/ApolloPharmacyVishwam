package com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagepreview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityRetroImagePreviewBinding
import com.apollopharmacy.vishwam.ui.home.model.GetImageByRackResponse
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison.ImageComparisonActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner.model.ScannerResponse
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.IOException

class RetroImagePreviewActivity : AppCompatActivity(), RetroImagePreviewCallback {
    private lateinit var activityRetroImagePreviewBinding: ActivityRetroImagePreviewBinding
    lateinit var scannerResponse: ScannerResponse
    var imageFile: File? = null
    var matchingPercentages: Double = 0.0
    private var firstImage: String = ""
    private var secondImage: String = ""
    private var matchingPercentage: String = ""
    private var rackNo: String = ""
    private var compressedImageFileName: String? = null
   var imageByRackResponse= GetImageByRackResponse()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroImagePreviewBinding =
            DataBindingUtil.setContentView(this@RetroImagePreviewActivity,
                R.layout.activity_retro_image_preview)
        activityRetroImagePreviewBinding.callback = this@RetroImagePreviewActivity
        setUp()
    }

    private fun setUp() {
        OpenCVLoader.initDebug()

        if (intent != null&&intent.getStringExtra("activity").equals("comparision")) {
            firstImage = intent.getStringExtra("firstimage")!!
            secondImage = intent.getStringExtra("secondimage")!!
            rackNo = intent.getStringExtra("rackNo")!!
            matchingPercentage = intent.getStringExtra("matchingPercentage")!!

            activityRetroImagePreviewBinding.cameraIcon.visibility= View.GONE


            Glide.with(this).load(firstImage)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityRetroImagePreviewBinding.image)

            activityRetroImagePreviewBinding.rack.setText(rackNo)

//            scannerResponse = intent.getSerializableExtra("SCANNER_RESPONSE") as ScannerResponse
        }


        if (intent != null&&intent.getStringExtra("activity").equals("scanner")) {
            rackNo = intent.getStringExtra("rackNo")!!

            imageByRackResponse= intent.getSerializableExtra("SCANNER_RESPONSE") as GetImageByRackResponse
            Glide.with(this).load(imageByRackResponse.imageurl)
                .placeholder(R.drawable.thumbnail_image)
                .into(activityRetroImagePreviewBinding.image)

            activityRetroImagePreviewBinding.cameraIcon.visibility= View.VISIBLE
            activityRetroImagePreviewBinding.rack.setText("RACK3")

        }
    }
    private fun calculateMatchingPercentage(bitmap1: Bitmap?, bitmap2: Bitmap?): Double {
        val firstImage = Mat()
        val secondImage = Mat()
        // Convert bitmap to Mat object
        Utils.bitmapToMat(bitmap1, firstImage)
        Utils.bitmapToMat(bitmap2, secondImage)
        // Convert images to grayscale
        Imgproc.cvtColor(firstImage, firstImage, Imgproc.COLOR_BGR2GRAY)
        Imgproc.cvtColor(secondImage, secondImage, Imgproc.COLOR_BGR2GRAY)
        // Calculate matching percentage
        val result = Mat()
        Imgproc.matchTemplate(firstImage, secondImage, result, Imgproc.TM_CCOEFF_NORMED)
        val minMaxLoc = Core.minMaxLoc(result)
        val maxVal = minMaxLoc.maxVal
        var matchingPercentage: Double = maxVal * 100
        if (matchingPercentage < 0) {
            matchingPercentage = 0.0
        }

        val intent = Intent(this@RetroImagePreviewActivity, ImageComparisonActivity::class.java)
        intent.putExtra("firstimage", imageByRackResponse.imageurl)
        intent.putExtra("secondimage", imageFile!!.absolutePath)
        intent.putExtra("rackNo", rackNo)
        intent.putExtra("activity","preview")
        intent.putExtra("matchingPercentage", matchingPercentage.toInt().toString())
        Utlis.hideLoading()

        startActivityForResult(intent, 210)
        return matchingPercentage
    }

    override fun onClickCameraIcon() {
        if (!checkPermission()) {
            askPermissions(100)
            return
        } else {
            openCamera()
        }
    }

    override fun onClickBack() {
        finish()
    }

    override fun onClickTick() {
//        if (imageFile != null) {
//
//
//
//        } else {
//            Toast.makeText(this@RetroImagePreviewActivity,
//                "Please capture image",
//                Toast.LENGTH_SHORT).show()
//        }
    }
    fun resizeBitmap(originalBitmap: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        return Bitmap.createScaledBitmap(originalBitmap!!, newWidth, newHeight, false)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && imageFile != null) {
//            imageByRackResponse.imageurl = imageFile.toString()
//            activityRetroImagePreviewBinding.image.setImageBitmap(
//                rotateImage(BitmapFactory.decodeFile(imageFile!!.absolutePath), imageFile!!)
//            )

            if (resultCode==210){
                if (resultCode == Activity.RESULT_OK) {
                    finish()
                }
            }


            runOnUiThread {
                // Show a loading dialog on the main UI thread
                com.apollopharmacy.vishwam.util.Utils.showLoadingDialog(this@RetroImagePreviewActivity) // Assuming RetroQrScannerActivity is the current activity
            }
            val thread = Thread {
                try {
                    if (imageFile!!.path.isNotEmpty()) {
                        val client = OkHttpClient()
                        val request = Request.Builder().url(imageByRackResponse.imageurl.toString()).build()
                        val response = client.newCall(request).execute()
                        val inputStream = response.body!!.byteStream()

                        val bitmap1 = BitmapFactory.decodeStream(inputStream)
                        val bitmap2 = BitmapFactory.decodeFile(imageFile!!.absolutePath)
                        val newWidth = 1920 // Desired width for the new Bitmap

                        val newHeight = 1200 // Desired height for the new Bitmap

                        val resizedBitmap1 = resizeBitmap(bitmap1, newWidth, newHeight)
                        val resizedBitmap2 = resizeBitmap(bitmap2, newWidth, newHeight)
                        val threshold = 21

                      matchingPercentages =
                            calculateMatchingPercentage(resizedBitmap1!!, resizedBitmap2!!)
//                        Toast.makeText(this,matchingPercentages.toString(),Toast.LENGTH_LONG).show()






                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()



        }
    }

    private fun rotateImage(bitmap: Bitmap, file: File): Bitmap? {
        var exifInterface: ExifInterface? = null
        try {
            exifInterface = file.let { ExifInterface(it.absolutePath) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = exifInterface!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(270f)

            else -> {
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this@RetroImagePreviewActivity, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroImagePreviewActivity, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroImagePreviewActivity, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroImagePreviewActivity, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this@RetroImagePreviewActivity, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroImagePreviewActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroImagePreviewActivity, Manifest.permission.CAMERA
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