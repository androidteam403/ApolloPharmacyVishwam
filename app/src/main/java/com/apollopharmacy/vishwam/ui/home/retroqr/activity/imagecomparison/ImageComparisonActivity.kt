package com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityImageComparisonBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner.model.ScannerResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class ImageComparisonActivity : AppCompatActivity(), ImageComparisonCallback {
    private lateinit var activityImageComparisonBinding: ActivityImageComparisonBinding
    lateinit var scannerResponse: ScannerResponse
    var bitmap1: Bitmap? = null
    var bitmap2: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityImageComparisonBinding =
            DataBindingUtil.setContentView(this@ImageComparisonActivity,
                R.layout.activity_image_comparison)
        activityImageComparisonBinding.callback = this@ImageComparisonActivity
        OpenCVLoader.initDebug()
        setUp()
    }

    @SuppressLint("SetTextI18n")
    private fun setUp() {
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        if (intent != null) {
            scannerResponse = intent.getSerializableExtra("SCANNER_RESPONSE") as ScannerResponse
        }
        val client = OkHttpClient()
        val request = Request.Builder().url(scannerResponse.imageUrl.toString()).build()
        val response = client.newCall(request).execute()
        val byteStream = response.body!!.byteStream()
        bitmap1 = BitmapFactory.decodeStream(byteStream)
        bitmap2 = BitmapFactory.decodeFile(scannerResponse.imageUrl1!!.absolutePath)

        activityImageComparisonBinding.beforeAfterSlider.setBeforeImage(BitmapDrawable(resources,
            bitmap2)).setAfterImage(BitmapDrawable(resources, bitmap1))

        val matchingPercentage: String = calculateMatchingPercentage(bitmap1, bitmap2)
        activityImageComparisonBinding.matchingPercentage.setText(matchingPercentage.substringBefore(".") + "%")
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

    override fun onClickBack() {
        finish()
    }

    override fun onClickSubmit() {
        finish()
    }
}