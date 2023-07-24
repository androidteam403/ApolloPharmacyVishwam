package com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityImageComparisonBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagepreview.RetroImagePreviewActivity
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageComparisonActivity : AppCompatActivity(), ImageComparisonCallback {
    private lateinit var activityImageComparisonBinding: ActivityImageComparisonBinding
    private var firstImage: String = ""
    private var secondImage: String = ""
    private var matchingPercentage: String = ""
    private var rackNo: String = ""
    private var storeId: String = ""
    var bitmap1: Bitmap? = null
    var bitmap2: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityImageComparisonBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_image_comparison)
        activityImageComparisonBinding.callback=this

        OpenCVLoader.initDebug()

        setUp()
    }

    @SuppressLint("SetTextI18n")
    private fun setUp() {

        if (intent != null) {
            firstImage = intent.getStringExtra("firstimage")!!
            secondImage = intent.getStringExtra("secondimage")!!
            rackNo = intent.getStringExtra("rackNo")!!
            matchingPercentage = intent.getStringExtra("matchingPercentage")!!

            activityImageComparisonBinding.matchingPercentage.setText(matchingPercentage)

            val thread = Thread {
                try {
                    if (secondImage.isNotEmpty()) {
                        val client = OkHttpClient()
                        val request = Request.Builder().url(firstImage.toString()).build()
                        val response = client.newCall(request).execute()
                        val inputStream = response.body!!.byteStream()
                         bitmap1 = BitmapFactory.decodeStream(inputStream)
                         bitmap2 = BitmapFactory.decodeFile(secondImage)
                        runOnUiThread {
                            activityImageComparisonBinding.beforeAfterSlider.setBeforeImage(BitmapDrawable(resources,
                                bitmap2)).setAfterImage(BitmapDrawable(resources, bitmap1))
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
//


            activityImageComparisonBinding.firstImage.setOnClickListener {
                val intent = Intent(applicationContext, RetroImagePreviewActivity::class.java)
                intent.putExtra("firstimage", firstImage)
                intent.putExtra("secondimage", secondImage)
                intent.putExtra("rackNo", rackNo)
                intent.putExtra("matchingPercentage", matchingPercentage)
                startActivity(intent)
            }


            activityImageComparisonBinding.secondImage.setOnClickListener {
                val intent = Intent(applicationContext, RetroImagePreviewActivity::class.java)
                intent.putExtra("firstimage", secondImage)
                intent.putExtra("secondimage", secondImage)
                intent.putExtra("rackNo", rackNo)
                intent.putExtra("matchingPercentage", matchingPercentage)
                startActivity(intent)
            }
            activityImageComparisonBinding.rackno.setText(rackNo)
            activityImageComparisonBinding.siteId.setText(Preferences.getQrSiteId() + " , " + Preferences.getQrSiteName())

            activityImageComparisonBinding.ok.setOnClickListener {
                finish()
            }

//
//            Glide.with(this).load(firstImage)
//                .placeholder(R.drawable.thumbnail_image)
//                .into(activityImageComparisonBinding.firstimage)
//            Glide.with(this).load(secondImage)
//                .placeholder(R.drawable.thumbnail_image)
//                .into(activityImageComparisonBinding.secondimage)
        }


    }


    override fun onClickBack() {
        finish()
    }

    override fun onClickSubmit() {
        finish()
    }
}