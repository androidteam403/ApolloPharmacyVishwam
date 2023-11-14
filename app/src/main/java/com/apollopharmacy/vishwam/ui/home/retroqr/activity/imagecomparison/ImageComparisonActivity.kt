package com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityImageComparisonBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.retroqr.RetroQrFragment
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagepreview.RetroImagePreviewActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.QrSaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner.RetroQrScannerActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUpload
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUploadCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUploadModel
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import java.io.File

class ImageComparisonActivity : AppCompatActivity(), ImageComparisonCallback,
    RetroQrFileUploadCallback,MainActivityCallback {
    private lateinit var activityImageComparisonBinding: ActivityImageComparisonBinding
    private var firstImage: String = ""
    private var secondImage: String = ""
    private var matchingPercentage: String = ""
    private var rackNo: String = ""
    private var storeId: String = ""
    private var activity: String = ""

    var bitmap1: Bitmap? = null
    private lateinit var viewModel: ImageComparisionViewModel

    var bitmap2: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityImageComparisonBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_image_comparison)
        activityImageComparisonBinding.callback = this
        viewModel = ViewModelProvider(this)[ImageComparisionViewModel::class.java]

        OpenCVLoader.initDebug()

        setUp()
    }

    @SuppressLint("SetTextI18n")
    private fun setUp() {
        MainActivity.mInstance.mainActivityCallback=this

        if (intent != null) {
            firstImage = intent.getStringExtra("firstimage")!!
            secondImage = intent.getStringExtra("secondimage")!!
            rackNo = intent.getStringExtra("rackNo")!!
            activity = intent.getStringExtra("activity")!!

            matchingPercentage = intent.getStringExtra("matchingPercentage")!!
            if (activity.equals("upload")) {
                activityImageComparisonBinding.submit.visibility = View.GONE
                activityImageComparisonBinding.ok.visibility = View.VISIBLE
                activityImageComparisonBinding.homeLayout.visibility=View.VISIBLE


            } else if (activity.equals("preview")) {
                activityImageComparisonBinding.homeLayout.visibility=View.GONE
                activityImageComparisonBinding.submit.visibility = View.VISIBLE
                activityImageComparisonBinding.ok.visibility = View.GONE

            }

            if (matchingPercentage.toInt() >= 0 && matchingPercentage.toInt() <= 70) {
                activityImageComparisonBinding.matchingcolor.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.round_rating_bar_red
                    )
                )
            } else if (matchingPercentage.toInt() >= 90 && matchingPercentage.toInt() <= 100) {

                activityImageComparisonBinding.matchingcolor.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.round_rating_bar_green
                    )
                )
            } else if (matchingPercentage.toInt() >= 70 && matchingPercentage.toInt() <= 90) {

                activityImageComparisonBinding.matchingcolor.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.round_rating_bar_orane
                    )
                )
            }

            activityImageComparisonBinding.matchingPercentage.setText(matchingPercentage + " %")

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
                            activityImageComparisonBinding.beforeAfterSlider.setBeforeImage(
                                BitmapDrawable(
                                    resources,
                                    bitmap2
                                )
                            ).setAfterImage(BitmapDrawable(resources, bitmap1))


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
                intent.putExtra("activity", "comparision")

                intent.putExtra("rackNo", rackNo)
                intent.putExtra("matchingPercentage", matchingPercentage)
                startActivity(intent)
            }


            activityImageComparisonBinding.secondImage.setOnClickListener {
                val intent = Intent(applicationContext, RetroImagePreviewActivity::class.java)
                intent.putExtra("firstimage", secondImage)
                intent.putExtra("secondimage", secondImage)
                intent.putExtra("rackNo", rackNo)
                intent.putExtra("activity", "comparision")

                intent.putExtra("matchingPercentage", matchingPercentage)
                startActivity(intent)
            }
            activityImageComparisonBinding.rackno.setText(rackNo)
            activityImageComparisonBinding.siteId.setText(Preferences.getQrSiteId() + " , " + Preferences.getQrSiteName())


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


        var fileUploadModelList = ArrayList<RetroQrFileUploadModel>()


        var fileUploadModel = RetroQrFileUploadModel()
        fileUploadModel.file = File(secondImage)
        fileUploadModel.rackNo = rackNo.split("-").get(1)
        fileUploadModel.qrCode = ""
        fileUploadModelList.add(fileUploadModel)


//
        Utlis.showLoading(this)

        RetroQrFileUpload().uploadFiles(
            this, this, fileUploadModelList
        )
    }

    override fun onClickOk() {
        finish()
    }

    override fun onSuccessUploadImagesApiCall(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onFailureUploadImagesApiCall(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    override fun onFailureUpload(message: String) {
    }

    override fun allFilesDownloaded(fileUploadModelList: List<RetroQrFileUploadModel>?) {
        if (fileUploadModelList != null && fileUploadModelList.size > 0) {
            val saveImageUrlsRequest = QrSaveImageUrlsRequest()
            saveImageUrlsRequest.storeid = Preferences.getQrSiteId()
            saveImageUrlsRequest.userid = Preferences.getValidatedEmpId()
            val base64ImageList = ArrayList<QrSaveImageUrlsRequest.StoreDetail>()
            for (i in fileUploadModelList) {
                val base64Image = QrSaveImageUrlsRequest.StoreDetail()
                base64Image.imageurl = RijndaelCipherEncryptDecrypt().decrypt(
                    i.fileDownloadResponse!!.referenceurl,
                    RijndaelCipherEncryptDecrypt().key
                )
                base64Image.qrcode = ""
                base64Image.rackno = i.rackNo
                base64ImageList.add(base64Image)
            }
            saveImageUrlsRequest.storeDetails = base64ImageList

            Utlis.showLoading(this)
            viewModel.saveImageUrlsApiCall(
                saveImageUrlsRequest, this
            )

        }
    }

    override fun allFilesUploaded(fileUploadModelList: List<RetroQrFileUploadModel>?) {
        TODO("Not yet implemented")
    }

    override fun onClickFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onSelectApprovedFragment(listSize: String?) {
        TODO("Not yet implemented")
    }

    override fun onSelectRejectedFragment() {
        TODO("Not yet implemented")
    }

    override fun onSelectPendingFragment() {
        TODO("Not yet implemented")
    }

    override fun onClickSpinnerLayout() {
        TODO("Not yet implemented")
    }

    override fun onClickSubmenuItem(
        menuName: String?,
        submenus: java.util.ArrayList<MenuModel>?,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onclickHelpIcon() {
        TODO("Not yet implemented")
    }
}