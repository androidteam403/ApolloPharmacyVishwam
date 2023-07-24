package com.apollopharmacy.vishwam.ui.home.retroqr.activity

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
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityRetroQrUploadBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter.ReviewRackAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter.UploadRackAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison.ImageComparisonActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.ImageDto
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.QrSaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.RackDialog
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUpload
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUploadCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUploadModel
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import me.echodev.resizer.Resizer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfDMatch
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.DescriptorMatcher
import org.opencv.features2d.ORB
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RetroQrUploadActivity : AppCompatActivity(), RetroQrUploadCallback,
    RackDialog.GstDialogClickListner, RetroQrFileUploadCallback {
    private lateinit var activityRetroQrUploadBinding: ActivityRetroQrUploadBinding
    private lateinit var viewModel: RetroQrUploadViewModel
    private lateinit var uploadRackAdapter: UploadRackAdapter
    private lateinit var reviewRackAdapter: ReviewRackAdapter
    private var activity: String = ""

    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    var images = ArrayList<ImageDto>()
    var rackList = ArrayList<String>()

    var imagesList = ArrayList<StoreWiseRackDetails.StoreDetail>()
    var reviewImagesList = ArrayList<StoreWiseRackDetails.StoreDetail>()
    var adapterName: String? = null
    var storeDetailsList = ArrayList<StoreWiseRackDetails.StoreDetail>()

    var updatedCount: Int = 0

    var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRetroQrUploadBinding = DataBindingUtil.setContentView(
            this@RetroQrUploadActivity, R.layout.activity_retro_qr_upload
        )

        var images = StoreWiseRackDetails.StoreDetail()
        images.rackno = "Rack1"
        images.imageurl =
            "https://pharmteststorage.blob.core.windows.net/test/vendor/SENSING/1690122201966.jpg?sv=2022-11-02&se=9999-12-31T23:59:59Z&sr=b&sp=r&sig=2hGVSwwTE7yfFRZWeDBx45OeatnWIWaeIMLGiGeUaxs%3D"
        images.reviewimageurl = ""
        imagesList.add(images)

        var images1 = StoreWiseRackDetails.StoreDetail()
        images1.rackno = "Rack2"
        images1.imageurl =
            "https://pharmteststorage.blob.core.windows.net/test/vendor/SENSING/1690122209961.jpg?sv=2022-11-02&se=9999-12-31T23:59:59Z&sr=b&sp=r&sig=0TWtci2yReiP9cFZR6Gsw4n97UkgFyBd9qiNhHVcYyY%3D"
        images1.reviewimageurl = ""

        imagesList.add(images1)
        reviewImagesList.add(images1)
        reviewImagesList.add(images)

        viewModel = ViewModelProvider(this)[RetroQrUploadViewModel::class.java]
        activityRetroQrUploadBinding.callback = this@RetroQrUploadActivity
        setUp()
    }

    private fun setUp() {
//        showLoading(context)
        OpenCVLoader.initDebug()

//        viewModel.getStoreWiseRackDetails(this)

        activityRetroQrUploadBinding.siteId.setText(Preferences.getQrSiteId())
        activityRetroQrUploadBinding.siteName.setText(Preferences.getQrSiteName())

        if (intent != null) {
            activity = intent.getStringExtra("activity")!!
            if (activity.equals("upload")) {
                activityRetroQrUploadBinding.uploadRackRcv.visibility = View.VISIBLE
                activityRetroQrUploadBinding.reviewRack.visibility = View.GONE
                activityRetroQrUploadBinding.uploadsubmitLayout.visibility = View.VISIBLE
                activityRetroQrUploadBinding.reviewSubmitLayout.visibility = View.GONE


            } else if (activity.equals("review")) {
                activityRetroQrUploadBinding.uploadRackRcv.visibility = View.VISIBLE
                activityRetroQrUploadBinding.reviewRack.visibility = View.VISIBLE
                activityRetroQrUploadBinding.uploadsubmitLayout.visibility = View.GONE
                activityRetroQrUploadBinding.reviewSubmitLayout.visibility = View.VISIBLE

            }


        }

        activityRetroQrUploadBinding.rackNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length >= 2) {
                    if (uploadRackAdapter != null) {
                        uploadRackAdapter.getFilter().filter(editable)
                        reviewRackAdapter.getFilter().filter(editable)

                    }
                } else if (activityRetroQrUploadBinding.rackNumber.getText().toString().equals("")) {
                    if (uploadRackAdapter != null) {
                        uploadRackAdapter.getFilter().filter("")
                        reviewRackAdapter.getFilter().filter("")

                    }
                } else {
                    if (uploadRackAdapter != null) {
                        uploadRackAdapter.getFilter().filter("")
                        reviewRackAdapter.getFilter().filter("")

                    }
                }
            }
        })


        activityRetroQrUploadBinding.rackArrow.setOnClickListener {

            for (i in imagesList.indices) {
                rackList.add(imagesList.get(i).rackno!!)
            }

            RackDialog().apply {
                arguments = RackDialog().generateParsedData(rackList)
            }.show(supportFragmentManager, "")
        }




        updatedCount = imagesList!!.filter {
            it.imageurl.toString().contains(".")
        }.size
        activityRetroQrUploadBinding.totalRackCount.text = imagesList!!.size.toString()


        activityRetroQrUploadBinding.updatedCount.setText(updatedCount.toString())
        activityRetroQrUploadBinding.pendingCount.setText((imagesList.size - updatedCount).toString())
        activityRetroQrUploadBinding.totalRacks.text = imagesList.size.toString()
        activityRetroQrUploadBinding.updated.text =
            reviewImagesList.filter { it.reviewimageurl!!.isNotEmpty() }.size.toString()
        activityRetroQrUploadBinding.pending.text =
            (reviewImagesList.size - (reviewImagesList.filter { it.reviewimageurl!!.isNotEmpty() }.size)).toString()

        activityRetroQrUploadBinding.totalRackCount.text = imagesList.size.toString()

        if (imagesList.isNotEmpty()) {

            uploadRackAdapter = UploadRackAdapter(
                this@RetroQrUploadActivity, this@RetroQrUploadActivity, imagesList
            )
            activityRetroQrUploadBinding.uploadRackRcv.adapter = uploadRackAdapter
            activityRetroQrUploadBinding.uploadRackRcv.layoutManager =
                LinearLayoutManager(this@RetroQrUploadActivity)
        }

        reviewRackAdapter = ReviewRackAdapter(
            this@RetroQrUploadActivity, this@RetroQrUploadActivity, reviewImagesList
        )
        activityRetroQrUploadBinding.reviewRack.adapter = reviewRackAdapter
        activityRetroQrUploadBinding.reviewRack.layoutManager =
            LinearLayoutManager(this@RetroQrUploadActivity)


    }

    override fun onClickBackArrow() {
        finish()
    }

    override fun onClickSubmit() {
        if (updatedCount == imagesList.size) {

            var fileUploadModelList = ArrayList<RetroQrFileUploadModel>()
            for (i in imagesList.indices) {
                if (imagesList[i].imageurl.toString()
                        .contains(".") && imagesList[i].imageurl.toString().contains("SENSING")
                ) {

                } else {

                    val resizedImage = Resizer(context).setTargetLength(1080).setQuality(100)
                        .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                        .setOutputDirPath(
                            ViswamApp.Companion.context.cacheDir.toString()
                        ).setSourceImage(File(imagesList[i].imageurl)).resizedFile
                    var fileUploadModel = RetroQrFileUploadModel()
                    fileUploadModel.file = resizedImage
                    fileUploadModel.rackNo = imagesList[i].rackno
                    fileUploadModel.qrCode = imagesList[i].qrcode
                    fileUploadModelList.add(fileUploadModel)
                }
            }


//
            showLoading(context)

            RetroQrFileUpload().uploadFiles(
                context, this, fileUploadModelList
            )




            finish()
        }
    }

    override fun imageData(
        position: Int,
        imageUrl: String,
        rackNo: String,
        qrCode: String,
        view: View,
    ) {
        PopUpWIndow(context, R.layout.layout_image_fullview, view, imageUrl, null, rackNo, position)

    }

    override fun onSuccessUploadImagesApiCall(message: String) {
        Toast.makeText(this@RetroQrUploadActivity, message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }

    override fun onFailureUploadImagesApiCall(message: String) {
        Toast.makeText(this@RetroQrUploadActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails) {
        storeDetailsList =
            storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>

        reviewImagesList =
            storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>

        updatedCount = storeWiseRackDetails.storeDetails!!.filter {
            it.imageurl.toString().contains(".")
        }.size
        activityRetroQrUploadBinding.totalRackCount.text =
            storeWiseRackDetails.storeDetails!!.size.toString()
        imagesList =
            storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>

        activityRetroQrUploadBinding.updatedCount.setText(updatedCount.toString())
        activityRetroQrUploadBinding.pendingCount.setText((imagesList.size - updatedCount).toString())

        activityRetroQrUploadBinding.totalRackCount.text = imagesList.size.toString()

        uploadRackAdapter = UploadRackAdapter(
            this@RetroQrUploadActivity, this@RetroQrUploadActivity, imagesList
        )
        activityRetroQrUploadBinding.uploadRackRcv.adapter = uploadRackAdapter
        activityRetroQrUploadBinding.uploadRackRcv.layoutManager =
            LinearLayoutManager(this@RetroQrUploadActivity)


        reviewRackAdapter = ReviewRackAdapter(
            this@RetroQrUploadActivity, this@RetroQrUploadActivity, reviewImagesList
        )
        activityRetroQrUploadBinding.reviewRack.adapter = reviewRackAdapter
        activityRetroQrUploadBinding.reviewRack.layoutManager =
            LinearLayoutManager(this@RetroQrUploadActivity)
        hideLoading()


    }

    //    var file: File = File(filename)
    override fun onClickCameraIcon(position: Int, adapter: String) {
        this.position = position
        adapterName = adapter
        if (!checkPermission()) {
            askPermissions(100)
            return
        } else {
            openCamera()
        }
    }

    override fun onClickCompare(
        matchingPercentage: String,
        firstImage: String,
        secondImage: String,
        rackNo: String,
    ) {
        val intent = Intent(applicationContext, ImageComparisonActivity::class.java)
        intent.putExtra("firstimage", firstImage)
        intent.putExtra("secondimage", secondImage)
        intent.putExtra("rackNo", rackNo)
        intent.putExtra("matchingPercentage", matchingPercentage)
        startActivity(intent)
    }

    fun onClickCompare(position: Int, file: File?, url: String?) {
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
                    reviewImagesList.get(position).matchingPercentage =
                        matchingPercentage.substringBefore(".")
                    runOnUiThread {
                        reviewRackAdapter.notifyDataSetChanged()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }



    override fun deleteImage(position: Int) {
        reviewImagesList.get(position).setmatchingPercentage("")
        reviewImagesList.get(position).setreviewimageurl("")
        reviewRackAdapter.notifyDataSetChanged()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        compressedImageFileName = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                context, context.packageName + ".provider", imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && imageFile != null) {
            var imageDto = ImageDto(imageFile as File, "")
//            images.get(position).image= imageFile as File

            if (adapterName.equals("upload")) {
                imagesList[position].imageurl = (imageFile as File).toString()
                updatedCount++
                uploadRackAdapter.notifyItemChanged(position)
            } else if (adapterName.equals("review")) {
                reviewImagesList[position].reviewimageurl = (imageFile as File).toString()


                onClickCompare(
                    position,
                    File(reviewImagesList.get(position).reviewimageurl),
                    reviewImagesList.get(position).imageurl
                )

                reviewRackAdapter.notifyItemChanged(position)

            }
        }

        activityRetroQrUploadBinding.updated.text =
            reviewImagesList.filter { it.reviewimageurl!!.isNotEmpty() }.size.toString()
        if (reviewImagesList.filter { it.reviewimageurl!!.isNotEmpty() }.size > 0) {
            activityRetroQrUploadBinding.pending.text =
                (reviewImagesList.size - (reviewImagesList.filter { it.reviewimageurl!!.isNotEmpty() }.size)).toString()
        }

        if (reviewImagesList.filter { it.reviewimageurl!!.isEmpty() }.size == 0) {
            activityRetroQrUploadBinding.submitButtonReview.setBackgroundColor(Color.parseColor("#209804"))

        } else {
            activityRetroQrUploadBinding.submitButtonReview.setBackgroundColor(Color.parseColor("#a1a1a1"))

        }
        activityRetroQrUploadBinding.updatedCount.setText(updatedCount.toString())
        activityRetroQrUploadBinding.pendingCount.setText((imagesList.size - updatedCount).toString())
        if (updatedCount == imagesList.size) {
            activityRetroQrUploadBinding.lastUpdateLayout.visibility = View.VISIBLE
            activityRetroQrUploadBinding.lastUpdateDate.setText(
                LocalDate.now().format(
                    DateTimeFormatter.ofPattern("dd MMM, yyyy")
                )
            )
            activityRetroQrUploadBinding.submitButton.setBackgroundColor(Color.parseColor("#209804"))
        } else {
            activityRetroQrUploadBinding.lastUpdateLayout.visibility = View.GONE
            activityRetroQrUploadBinding.submitButton.setBackgroundColor(Color.parseColor("#a1a1a1"))
        }
    }


    private fun calculateMatchingPercentage(bitmap1: Bitmap?, bitmap2: Bitmap?): String {

        val firstImage = Mat()
        val secondImage = Mat()
        // Convert bitmap to Mat object
        Utils.bitmapToMat(bitmap1, firstImage)
        Utils.bitmapToMat(bitmap2, secondImage)
        // Detect keypoints and compute descriptors using ORB
        val orb = ORB.create()
        val keypoints1 = MatOfKeyPoint()
        val keypoints2 = MatOfKeyPoint()
        val descriptors1 = Mat()
        val descriptors2 = Mat()

        orb.detectAndCompute(firstImage, Mat(), keypoints1, descriptors1)
        orb.detectAndCompute(secondImage, Mat(), keypoints2, descriptors2)

        // Match descriptors

        // Match descriptors
        val matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING)
        val matches = MatOfDMatch()
        matcher.match(descriptors1, descriptors2, matches)

        // Calculate the percentage of similarity
        val totalMatches = matches.rows().toDouble()
        var goodMatches = 0.0
        val thresholdDistance = 0.7 // Adjust this threshold to control the matching sensitivity


        var i = 0
        while (i < totalMatches) {
            if (matches.toArray()[i].distance < thresholdDistance) {
                goodMatches++
            }
            i++
        }

        val percentageSimilarity = goodMatches / totalMatches * 100











        return percentageSimilarity.toString()
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

    override fun onFailureUpload(message: String) {
    }

    override fun allFilesDownloaded(fileUploadModelList: List<RetroQrFileUploadModel>?) {
        if (fileUploadModelList != null && fileUploadModelList.size > 0) {
            val saveImageUrlsRequest = QrSaveImageUrlsRequest()
            saveImageUrlsRequest.storeid =
                Preferences.getApolloSensingStoreId() //Preferences.getSiteId()
            saveImageUrlsRequest.userid = Preferences.getValidatedEmpId()
            val base64ImageList = ArrayList<QrSaveImageUrlsRequest.StoreDetail>()
            for (i in fileUploadModelList) {
                val base64Image = QrSaveImageUrlsRequest.StoreDetail()
                base64Image.imageurl = i.fileDownloadResponse!!.referenceurl
                base64Image.qrcode = ""
                base64Image.rackno = i.rackNo
                base64ImageList.add(base64Image)
            }
            saveImageUrlsRequest.storeDetails = base64ImageList
            viewModel.saveImageUrlsApiCall(
                saveImageUrlsRequest, this
            )

        }
    }





    override fun allFilesUploaded(fileUploadModelList: List<RetroQrFileUploadModel>?) {

//            vendor/SENSING/1689678494093.jpg
//            vendor/SENSING/1689678498958.jpg
//            vendor/SENSING/1689678506217.jpg
//            viewModel.saveImageUrlsApiCall(
//                saveImageUrlsRequest,this
//            )


    }

    override fun selectGST(gst: String) {
        activityRetroQrUploadBinding.rackNumber.setText(gst)

    }
}