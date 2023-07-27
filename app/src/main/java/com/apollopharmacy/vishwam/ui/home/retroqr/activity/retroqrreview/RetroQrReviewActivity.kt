package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrReviewBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.adapter.ImagesAdapterRetroReviewQr
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrreview.model.ImageData
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.File

class RetroQrReviewActivity : AppCompatActivity(), RetroQrReviewCallback {
    private lateinit var activityRetroQrReviewBinding: ActivityRetroQrReviewBinding
    private lateinit var viewModel: RetroQrReviewViewModel
    private lateinit var imagesAdapterRetroReviewQr: ImagesAdapterRetroReviewQr
    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    var position: Int = 0
    var imageData = ImageData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrReviewBinding = DataBindingUtil.setContentView(this@RetroQrReviewActivity,
            R.layout.activity_retro_qr_review)
        activityRetroQrReviewBinding.callback = this@RetroQrReviewActivity
        viewModel = ViewModelProvider(this)[RetroQrReviewViewModel::class.java]
        OpenCVLoader.initDebug()
        setUp()
    }

    private fun setUp() {
        val images = ArrayList<ImageData.Image>()
        val reviewImages = ArrayList<ImageData.ReviewImage>()
        for (i in 1..5) {
            val image = ImageData.Image()
            image.file = File("")
            image.matchingPercentage = ""
            images.add(image)
        }
        imageData.images = images

        val reviewImage1 = ImageData.ReviewImage()
        reviewImage1.url ="https://pharmteststorage.blob.core.windows.net/test/vendor/SENSING/1689831004658.jpg?sv=2022-11-02&se=9999-12-31T23:59:59Z&sr=b&sp=r&sig=oggBsfDrGAbdYs6e0jHStYvzA0m6iFT5ws2XdnqKZ9I%3D"
        val reviewImage2 = ImageData.ReviewImage()
        reviewImage2.url =
            "https://pharmtest.blob.core.windows.net/cms//data/user/0/com.apollopharmacy.vishwam/cache/1688709499851.jpg"

        val reviewImage3 = ImageData.ReviewImage()
        reviewImage3.url =
            "https://pharmtest.blob.core.windows.net/cms//data/user/0/com.apollopharmacy.vishwam/cache/1688549945770.jpg"

        val reviewImage4 = ImageData.ReviewImage()
        reviewImage4.url =
            "https://pharmtest.blob.core.windows.net/cms//data/user/0/com.apollopharmacy.vishwam/cache/1688549950792.jpg"

        val reviewImage5 = ImageData.ReviewImage()
        reviewImage5.url =
            "https://pharmtest.blob.core.windows.net/cms//data/user/0/com.apollopharmacy.vishwam/cache/1688709499851.jpg"

        reviewImages.add(reviewImage1)
        reviewImages.add(reviewImage2)
        reviewImages.add(reviewImage3)
        reviewImages.add(reviewImage4)
        reviewImages.add(reviewImage5)
        imageData.reviewImages = reviewImages

        activityRetroQrReviewBinding.totalRacks.setText(images.size.toString())
        imagesAdapterRetroReviewQr =
            ImagesAdapterRetroReviewQr(this@RetroQrReviewActivity,
                this@RetroQrReviewActivity,
                imageData)
        activityRetroQrReviewBinding.rackRecyclerView.adapter = imagesAdapterRetroReviewQr
        activityRetroQrReviewBinding.rackRecyclerView.layoutManager =
            LinearLayoutManager(this@RetroQrReviewActivity, LinearLayoutManager.VERTICAL, false)
    }

    override fun onClickCamera(position: Int) {
        this.position = position
        if (!checkPermission()) {
            askPermissions(100)
            return
        } else {
            openCamera()
        }
    }

    override fun onClickDelete(position: Int) {
        val images = imageData.images
        images!![position].file = File("")
        updated--
        count--
        activityRetroQrReviewBinding.updated.setText(updated.toString())
        activityRetroQrReviewBinding.pending.setText((images.size - updated).toString())
        imageData.images!![position].matchingPercentage = ""

        if (count == 5) {
            activityRetroQrReviewBinding.submitButton.setBackgroundColor(Color.parseColor(
                "#209804"))
        } else {
            activityRetroQrReviewBinding.submitButton.setBackgroundColor(Color.parseColor(
                "#a1a1a1"))
        }
        imagesAdapterRetroReviewQr.notifyDataSetChanged()
    }

    var count: Int = 0
    override fun onClickCompare(position: Int, file: File?, url: String?) {
        val thread = Thread {
            try {
                if (file!!.path.isNotEmpty()) {
                    val client = OkHttpClient()
                    val request = Request.Builder().url(url.toString()).build()
                    val response = client.newCall(request).execute()
                    val inputStream = response.body!!.byteStream()
                    val bitmap1 = BitmapFactory.decodeStream(inputStream)
                    val bitmap2 = BitmapFactory.decodeFile(file.absolutePath)
                    val matchingPercentage: String = calculateMatchingPercentage(bitmap1, bitmap2)
                    imageData.images!![position].matchingPercentage =
                        matchingPercentage.substringBefore(".")
                    count++
                    runOnUiThread {
                        imagesAdapterRetroReviewQr.notifyDataSetChanged()
                    }
                    if (count == 5) {
                        activityRetroQrReviewBinding.submitButton.setBackgroundColor(Color.parseColor(
                            "#209804"))
                    } else {
                        activityRetroQrReviewBinding.submitButton.setBackgroundColor(Color.parseColor(
                            "#a1a1a1"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    override fun onClickSubmit() {
        if (count == 5) {
            Toast.makeText(this@RetroQrReviewActivity, "Submitted Successfully", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun calculateMatchingPercentage(bitmap1: Bitmap?, bitmap2: Bitmap?): String {
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
        return matchingPercentage.toString()
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

    var updated: Int = 0
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && imageFile != null) {
            val images = imageData.images
            images!![position].file = imageFile
            updated++
            imagesAdapterRetroReviewQr.notifyItemChanged(position)
        }
        activityRetroQrReviewBinding.updated.setText(updated.toString())
        activityRetroQrReviewBinding.pending.setText((imageData.images!!.size - updated).toString())
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this@RetroQrReviewActivity, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrReviewActivity, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrReviewActivity, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrReviewActivity, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this@RetroQrReviewActivity, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrReviewActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@RetroQrReviewActivity, Manifest.permission.CAMERA
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