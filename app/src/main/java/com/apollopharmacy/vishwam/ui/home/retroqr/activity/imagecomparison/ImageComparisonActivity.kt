package com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityImageComparisonBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagepreview.RetroImagePreviewActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader

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

            if (matchingPercentage.toInt() >=0 && matchingPercentage.toInt()<=70) {
                activityImageComparisonBinding.matchingcolor.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.round_rating_bar_red))
            } else  if (matchingPercentage.toInt() >=90 && matchingPercentage.toInt()<=100) {

                activityImageComparisonBinding.matchingcolor.setBackgroundDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.round_rating_bar_green))
            }


            else  if (matchingPercentage.toInt() >=70 && matchingPercentage.toInt()<=90) {

                activityImageComparisonBinding.matchingcolor.setBackgroundDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.round_rating_bar_orane))
            }

            activityImageComparisonBinding.matchingPercentage.setText(matchingPercentage+" %")

            val thread = Thread {
                try {
                    if (secondImage.isNotEmpty()) {
                        val client = OkHttpClient()
                        val request = Request.Builder().url(firstImage).build()
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

////
//            Glide.with(this).load(firstImage)
//                .placeholder(R.drawable.thumbnail_image)
//                .into(activityImageComparisonBinding.firstimageview)
//            Glide.with(this).load(secondImage)
//                .placeholder(R.drawable.thumbnail_image)
//                .into(activityImageComparisonBinding.secondimageview)
        }


    }


    override fun onClickBack() {
        finish()
    }

    override fun onClickSubmit() {
        finish()
    }
}