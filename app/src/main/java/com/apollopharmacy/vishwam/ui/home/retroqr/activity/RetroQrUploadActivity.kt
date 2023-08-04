package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
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
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import me.echodev.resizer.Resizer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import java.io.File
import java.lang.Math.abs
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RetroQrUploadActivity : AppCompatActivity(), RetroQrUploadCallback,
    RackDialog.GstDialogClickListner, RetroQrFileUploadCallback {
    private lateinit var activityRetroQrUploadBinding: ActivityRetroQrUploadBinding
    private lateinit var viewModel: RetroQrUploadViewModel
    private lateinit var uploadRackAdapter: UploadRackAdapter
    private lateinit var reviewRackAdapter: ReviewRackAdapter
    private var activity: String = ""
    var updated: Int = 0
    private lateinit var cameraDialog: Dialog

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


        viewModel = ViewModelProvider(this)[RetroQrUploadViewModel::class.java]
        activityRetroQrUploadBinding.callback = this@RetroQrUploadActivity
        setUp()
    }

    private fun setUp() {
        showLoading(this)
        OpenCVLoader.initDebug()

        viewModel.getStoreWiseRackDetails(this)



        activityRetroQrUploadBinding.siteId.setText(Preferences.getQrSiteId())
        activityRetroQrUploadBinding.siteName.setText(Preferences.getQrSiteName())

        if (intent != null) {
//            activity = intent.getStringExtra("activity")!!

            activityRetroQrUploadBinding.rackArrow.setOnClickListener {
                RackDialog().apply {
                    arguments = RackDialog().generateParsedData(rackList)
                }.show(supportFragmentManager, "")
            }


        }




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

                    var fileUploadModel = RetroQrFileUploadModel()
                    fileUploadModel.file = File(imagesList[i].imageurl)
                    fileUploadModel.rackNo = imagesList[i].rackno
                    fileUploadModel.qrCode = imagesList[i].qrcode
                    fileUploadModelList.add(fileUploadModel)
                }
            }


//
            showLoading(this)

            RetroQrFileUpload().uploadFiles(
                this, this, fileUploadModelList
            )


//            finish()
        }
    }

    override fun onClickSubmitReview() {
        if (reviewImagesList.size - updated == 0) {


            var fileUploadModelList = ArrayList<RetroQrFileUploadModel>()
            for (i in reviewImagesList.indices) {
                if (reviewImagesList[i].reviewimageurl.isNullOrEmpty()
                ) {

                } else {


                    var fileUploadModel = RetroQrFileUploadModel()
                    fileUploadModel.file = File(reviewImagesList[i].reviewimageurl)
                    fileUploadModel.rackNo = reviewImagesList[i].rackno
                    fileUploadModel.qrCode = reviewImagesList[i].qrcode
                    fileUploadModelList.add(fileUploadModel)
                }
            }


//
            showLoading(this)

            RetroQrFileUpload().uploadFiles(
                this, this, fileUploadModelList
            )


//                finish()
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
        finish()
        Toast.makeText(this@RetroQrUploadActivity, message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }

    override fun onFailureUploadImagesApiCall(message: String) {
        Toast.makeText(this@RetroQrUploadActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFailureStoreWiseRackResponse(message: String) {
        hideLoading()
        onBackPressed()
        Toast.makeText(this@RetroQrUploadActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails) {

        rackList.clear()
        rackList.add("All")
        activityRetroQrUploadBinding.rackNum.setText("All")
        for (i in storeWiseRackDetails.storeDetails!!.indices) {
            rackList.add(storeWiseRackDetails.storeDetails!!.get(i).rackno!!)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rackList)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        activityRetroQrUploadBinding.spinner.adapter = adapter

        storeDetailsList =
            storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>

        reviewImagesList =
            storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>



        activityRetroQrUploadBinding.totalRacks.setText(reviewImagesList.size.toString())

        activityRetroQrUploadBinding.pending.setText((reviewImagesList.size - updated).toString())
        activityRetroQrUploadBinding.updated.setText((updated).toString())


        updatedCount = storeWiseRackDetails.storeDetails!!.filter {
            it.imageurl.toString().contains(".")
        }.size
        activityRetroQrUploadBinding.totalRackCount.text =
            storeWiseRackDetails.storeDetails!!.size.toString()
        imagesList =
            storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>


        if (imagesList.filter { it.imageurl!!.isNotEmpty() }.size > 0) {
            activity = "review"

            activityRetroQrUploadBinding.uploadRackRcv.visibility = View.VISIBLE
            activityRetroQrUploadBinding.reviewRack.visibility = View.VISIBLE
            activityRetroQrUploadBinding.uploadsubmitLayout.visibility = View.GONE
            activityRetroQrUploadBinding.reviewSubmitLayout.visibility = View.VISIBLE
            activityRetroQrUploadBinding.headername.setText("RETRO QR REVIEW")

        } else {
            activity = "upload"

            activityRetroQrUploadBinding.headername.setText("RETRO QR UPLOAD")

            activityRetroQrUploadBinding.uploadRackRcv.visibility = View.VISIBLE
            activityRetroQrUploadBinding.reviewRack.visibility = View.GONE
            activityRetroQrUploadBinding.uploadsubmitLayout.visibility = View.VISIBLE
            activityRetroQrUploadBinding.reviewSubmitLayout.visibility = View.GONE


        }




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
                    val newWidth = 640 // Desired width for the new Bitmap

                    val newHeight = 480 // Desired height for the new Bitmap

                    val resizedBitmap1 = resizeBitmap(bitmap1, newWidth, newHeight)
                    val resizedBitmap2 = resizeBitmap(bitmap2, newWidth, newHeight)
                    val threshold = 21

                    val matchingPercentage: Double = calculateSimilarityPercentage(
                        bitmap1!!,
                        bitmap2!!,threshold
                    )

                    reviewImagesList.get(position).matchingPercentage =
                        matchingPercentage.toInt().toString()

                    runOnUiThread {
                        reviewRackAdapter.notifyDataSetChanged()
                        if (reviewImagesList.isNotEmpty()) {
                            activityRetroQrUploadBinding.redtext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() >= 0 && it.matchingPercentage!!.toInt() < 71 }.size).toString())
                            activityRetroQrUploadBinding.orangetext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() > 70 && it.matchingPercentage!!.toInt() < 91 }.size).toString())
                            activityRetroQrUploadBinding.greentext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() > 90 && it.matchingPercentage!!.toInt() < 101 }.size).toString())


                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    fun resizeBitmap(originalBitmap: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        return Bitmap.createScaledBitmap(originalBitmap!!, newWidth, newHeight, false)
    }

    override fun deleteImage(position: Int) {

        cameraDialog = Dialog(this)
        cameraDialog.setContentView(R.layout.dialog_camera_delete)
        val close = cameraDialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            cameraDialog.dismiss()
        }
        val ok = cameraDialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            reviewImagesList.get(position).setmatchingPercentage("")
            reviewImagesList.get(position).setreviewimageurl("")
            updated--
            activityRetroQrUploadBinding.updated.setText(updated.toString())
            activityRetroQrUploadBinding.pending.setText((reviewImagesList.size - updated).toString())

            if (reviewImagesList.size - updated == 0) {
                activityRetroQrUploadBinding.submitButtonReview.setBackgroundColor(
                    Color.parseColor(
                        "#209804"
                    )
                )

            } else {
                activityRetroQrUploadBinding.submitButtonReview.setBackgroundColor(
                    Color.parseColor(
                        "#a1a1a1"
                    )
                )

            }
            if (reviewImagesList.isNotEmpty()) {

                if (reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() >= 0 && it.matchingPercentage!!.toInt() < 71 }.size > 0) {
                    activityRetroQrUploadBinding.redtext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() >= 0 && it.matchingPercentage!!.toInt() < 71 }.size).toString())

                } else {
                    activityRetroQrUploadBinding.redtext.setText("0")

                }


                if (reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() > 70 && it.matchingPercentage!!.toInt() < 91 }.size > 0) {
                    activityRetroQrUploadBinding.orangetext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() > 70 && it.matchingPercentage!!.toInt() < 91 }.size).toString())

                } else {
                    activityRetroQrUploadBinding.orangetext.setText("0")
                }

                if (reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() > 90 && it.matchingPercentage!!.toInt() < 101 }.size > 0) {
                    activityRetroQrUploadBinding.greentext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty() && it.matchingPercentage!!.toInt() > 90 && it.matchingPercentage!!.toInt() < 101 }.size).toString())

                } else {
                    activityRetroQrUploadBinding.greentext.setText("0")
                }


            }

            reviewRackAdapter.notifyDataSetChanged()

            cameraDialog.dismiss()

        }

        cameraDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cameraDialog.show()

    }

    override fun onUpload(position: Int, rackNo: String) {
        var fileUploadModelList = ArrayList<RetroQrFileUploadModel>()
        for (i in reviewImagesList.indices) {
            if (reviewImagesList[i].reviewimageurl.isNullOrEmpty()
            ) {

            } else {

                val resizedImage = Resizer(context).setTargetLength(1080).setQuality(100)
                    .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                    .setOutputDirPath(
                        ViswamApp.Companion.context.cacheDir.toString()
                    ).setSourceImage(File(reviewImagesList[i].reviewimageurl)).resizedFile
                var fileUploadModel = RetroQrFileUploadModel()
                fileUploadModel.file = resizedImage
                fileUploadModel.rackNo = reviewImagesList[i].rackno
                fileUploadModel.qrCode = reviewImagesList[i].qrcode
                fileUploadModelList.add(fileUploadModel)
            }
        }


//
        showLoading(this)

        RetroQrFileUpload().uploadFiles(
            this, this, fileUploadModelList
        )


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

            val resizedImage = Resizer(this)
                .setTargetLength(1080)
                .setQuality(100)
                .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                .setOutputDirPath(
                    context.cacheDir.toString()
                )

                .setSourceImage(imageFile)
                .resizedFile

//            images.get(position).image= imageFile as File


            if (activity.equals("review")) {
                updated++
                activityRetroQrUploadBinding.updated.setText(updated.toString())
                activityRetroQrUploadBinding.pending.setText((reviewImagesList.size - updated).toString())


            }

            if (adapterName.equals("upload")) {
                imagesList[position].imageurl = (resizedImage as File).toString()
                updatedCount++
                uploadRackAdapter.notifyItemChanged(position)
            } else if (adapterName.equals("review")) {
                reviewImagesList[position].reviewimageurl = (resizedImage as File).toString()

//onClickCompare(
//                    position,
//                    File(reviewImagesList.get(position).reviewimageurl),
//                    RijndaelCipherEncryptDecrypt().decrypt(reviewImagesList.get(position).imageurl!!,"blobfilesload"),
//                )
                onClickCompare(
                    position,
                    File(reviewImagesList.get(position).reviewimageurl),
                    reviewImagesList.get(position).imageurl!!,
                )

                reviewRackAdapter.notifyItemChanged(position)

            }
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




    fun calculateColorDifference(color1: Int, color2: Int, thresholdAlpha: Int): Int {
        // Calculate the color difference between two pixels
        val alpha1 = android.graphics.Color.alpha(color1)
        val r1 = android.graphics.Color.red(color1)
        val g1 = android.graphics.Color.green(color1)
        val b1 = android.graphics.Color.blue(color1)

        val alpha2 = android.graphics.Color.alpha(color2)
        val r2 = android.graphics.Color.red(color2)
        val g2 = android.graphics.Color.green(color2)
        val b2 = android.graphics.Color.blue(color2)

        val alphaDifference = if (alpha1 > thresholdAlpha || alpha2 > thresholdAlpha) {
            Math.abs(alpha1 - alpha2)
        } else {
            0 // Ignore transparency if both colors are fully transparent
        }

        return alphaDifference + Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2)
    }

    fun calculateSimilarityPercentage(bitmap1: Bitmap, bitmap2: Bitmap, threshold: Int): Double {
        // Ensure both bitmaps have the same dimensions
        if (bitmap1.width != bitmap2.width || bitmap1.height != bitmap2.height) {
            return 0.0
        }

        // Calculate the total color difference
        var totalColorDifference = 0
        for (x in 0 until bitmap1.width) {
            for (y in 0 until bitmap1.height) {
                totalColorDifference += calculateColorDifference(bitmap1.getPixel(x, y), bitmap2.getPixel(x, y), threshold)
            }
        }

        // Calculate similarity percentage (average color difference)
        val totalPixels = bitmap1.width * bitmap1.height
        val averageColorDifference = totalColorDifference.toDouble() / totalPixels

        // Normalize the average color difference to be within the range [0, 1]
        val normalizedSimilarity = 1.0 - (averageColorDifference / 255.0)

        return normalizedSimilarity * 100.0
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

            showLoading(this)
            viewModel.saveImageUrlsApiCall(
                saveImageUrlsRequest, this
            )

        }
    }


    override fun allFilesUploaded(fileUploadModelList: List<RetroQrFileUploadModel>?) {

//       if (fileUploadModelList != null && fileUploadModelList.size > 0) {
//            val saveImageUrlsRequest = QrSaveImageUrlsRequest()
//            saveImageUrlsRequest.storeid =
//                Preferences.getApolloSensingStoreId() //Preferences.getSiteId()
//            saveImageUrlsRequest.userid = Preferences.getValidatedEmpId()
//            val base64ImageList = ArrayList<QrSaveImageUrlsRequest.StoreDetail>()
//            for (i in fileUploadModelList) {
//                val base64Image = QrSaveImageUrlsRequest.StoreDetail()
//                base64Image.imageurl = i.sensingFileUploadResponse!!.referenceurl
//                base64Image.qrcode = ""
//                base64Image.rackno = i.rackNo
//                base64ImageList.add(base64Image)
//            }
//            saveImageUrlsRequest.storeDetails = base64ImageList
//            viewModel.saveImageUrlsApiCall(
//                saveImageUrlsRequest, this
//            )
//
//        }


    }

    override fun selectGST(gst: String) {
        activityRetroQrUploadBinding.rackNum.setText(gst)
        if (gst.isNullOrEmpty()) {
            uploadRackAdapter.getFilter().filter("")
            reviewRackAdapter.getFilter().filter("")
        } else {
            uploadRackAdapter.getFilter().filter(gst)
            reviewRackAdapter.getFilter().filter(gst)
        }

    }
}