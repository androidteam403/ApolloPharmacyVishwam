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
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    var updated: Int = 0

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



        activityRetroQrUploadBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                uploadRackAdapter.getFilter().filter(selectedItem)
                reviewRackAdapter.getFilter().filter(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                uploadRackAdapter.getFilter().filter("")
                reviewRackAdapter.getFilter().filter("")               }
        }

        activityRetroQrUploadBinding.siteId.setText(Preferences.getQrSiteId())
        activityRetroQrUploadBinding.siteName.setText(Preferences.getQrSiteName())

        if (intent != null) {
//            activity = intent.getStringExtra("activity")!!



        }


        // Set a listener to handle item selection





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
            showLoading(this)

            RetroQrFileUpload().uploadFiles(
                context, this, fileUploadModelList
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
                    context, this, fileUploadModelList
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

    override fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails) {

        rackList.clear()
        rackList.add("All")

        for(i in storeWiseRackDetails.storeDetails!!.indices){
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

        activityRetroQrUploadBinding.pending.setText((reviewImagesList.size-updated).toString())
        activityRetroQrUploadBinding.updated.setText((updated).toString())


        updatedCount = storeWiseRackDetails.storeDetails!!.filter {
            it.imageurl.toString().contains(".")
        }.size
        activityRetroQrUploadBinding.totalRackCount.text =
            storeWiseRackDetails.storeDetails!!.size.toString()
        imagesList =
            storeWiseRackDetails.storeDetails as ArrayList<StoreWiseRackDetails.StoreDetail>


        if (imagesList.filter { it.imageurl!!.isNotEmpty() }.size>0) {
            activity="review"

            activityRetroQrUploadBinding.uploadRackRcv.visibility = View.VISIBLE
            activityRetroQrUploadBinding.reviewRack.visibility = View.VISIBLE
            activityRetroQrUploadBinding.uploadsubmitLayout.visibility = View.GONE
            activityRetroQrUploadBinding.reviewSubmitLayout.visibility = View.VISIBLE
            activityRetroQrUploadBinding.headername.setText("RETRO QR REVIEW")

        }
        else {
            activity="upload"

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
                    val newWidth = 200 // Desired width for the new Bitmap

                    val newHeight = 150 // Desired height for the new Bitmap


// Resize the originalBitmap

// Resize the originalBitmap
                    val resizedBitmap1 = resizeBitmap(bitmap1, newWidth, newHeight)
                    val resizedBitmap2 = resizeBitmap(bitmap2, newWidth, newHeight)

                    val matchingPercentage: Double = calculateMatchingPercentage(resizedBitmap1!!,
                        resizedBitmap2!!
                    )

                    reviewImagesList.get(position).matchingPercentage =matchingPercentage.toInt().toString()

                    runOnUiThread {
                        reviewRackAdapter.notifyDataSetChanged()
                        if (reviewImagesList.isNotEmpty()){
                            activityRetroQrUploadBinding.redtext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>=0&& it.matchingPercentage!!.toInt()<71 }.size).toString())
                            activityRetroQrUploadBinding.orangetext.setText((reviewImagesList.filter {  it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>70&& it.matchingPercentage!!.toInt()<91 }.size).toString())
                            activityRetroQrUploadBinding.greentext.setText((reviewImagesList.filter {  it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>90&& it.matchingPercentage!!.toInt()<101 }.size).toString())


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

        reviewImagesList.get(position).setmatchingPercentage("")
        reviewImagesList.get(position).setreviewimageurl("")
        updated--
        activityRetroQrUploadBinding.updated.setText(updated.toString())
        activityRetroQrUploadBinding.pending.setText((reviewImagesList.size - updated).toString())

        if (reviewImagesList.size - updated == 0) {
            activityRetroQrUploadBinding.submitButtonReview.setBackgroundColor(Color.parseColor("#209804"))

        } else {
            activityRetroQrUploadBinding.submitButtonReview.setBackgroundColor(Color.parseColor("#a1a1a1"))

        }
        if (reviewImagesList.isNotEmpty()){

         if (reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>=0&& it.matchingPercentage!!.toInt()<71 }.size>0){
             activityRetroQrUploadBinding.redtext.setText((reviewImagesList.filter { it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>=0&& it.matchingPercentage!!.toInt()<71 }.size).toString())

         }
            else
         {
             activityRetroQrUploadBinding.redtext.setText("0")

         }


            if (reviewImagesList.filter {  it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>70&& it.matchingPercentage!!.toInt()<91 }.size>0){
                activityRetroQrUploadBinding.orangetext.setText((reviewImagesList.filter {  it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>70&& it.matchingPercentage!!.toInt()<91 }.size).toString())

            }
            else{
                activityRetroQrUploadBinding.orangetext.setText("0")
            }

            if (reviewImagesList.filter {  it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>90&& it.matchingPercentage!!.toInt()<101 }.size>0){
                activityRetroQrUploadBinding.greentext.setText((reviewImagesList.filter {  it.matchingPercentage!!.isNotEmpty()&&it.matchingPercentage!!.toInt()>90&& it.matchingPercentage!!.toInt()<101 }.size).toString())

            }else{
                activityRetroQrUploadBinding.greentext.setText("0")
            }



        }

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


            if (activity.equals("review")) {
                updated++
                activityRetroQrUploadBinding.updated.setText(updated.toString())
                activityRetroQrUploadBinding.pending.setText((reviewImagesList.size - updated).toString())


            }

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


    fun calculateMatchingPercentage(
        bitmap1: Bitmap,
        bitmap2: Bitmap,
    ): Double {
        val width1 = bitmap1.width
        val height1 = bitmap1.height
        val width2 = bitmap2.width
        val height2 = bitmap2.height
        require(!(width1 != width2 || height1 != height2)) { "Bitmaps must have the same dimensions." }
        val pixels1 = IntArray(width1 * height1)
        val pixels2 = IntArray(width2 * height2)
        bitmap1.getPixels(pixels1, 0, width1, 0, 0, width1, height1)
        bitmap2.getPixels(pixels2, 0, width2, 0, 0, width2, height2)
        var matchingPixels = 0
        for (i in pixels1.indices) {
            if (pixels1[i] == pixels2[i]) {
                matchingPixels++
            }
        }
        return matchingPixels.toDouble() / pixels1.size * 100
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
            saveImageUrlsRequest.storeid ="16001"
               //Preferences.getSiteId()
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

    }
}