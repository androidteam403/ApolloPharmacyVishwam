package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
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
import com.apollopharmacy.vishwam.databinding.DialogRackQrCodePrintBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter.ReviewRackAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter.UploadRackAdapter
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison.ImageComparisonActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.ImageDto
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.QrSaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.RackDialog
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.printpreview.PrintPreviewActivity
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUpload
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUploadCallback
import com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr.RetroQrFileUploadModel
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.apollopharmacy.vishwam.util.PopUpWIndowQr
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.apollopharmacy.vishwam.util.bluetooth.BarcodeEncoder
import com.apollopharmacy.vishwam.util.bluetooth.manager.BluetoothManager
import com.apollopharmacy.vishwam.util.bluetooth.manager.PrintfTSPLManager
import com.apollopharmacy.vishwam.util.rijndaelcipher.RijndaelCipherEncryptDecrypt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import me.echodev.resizer.Resizer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
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
    private lateinit var printRackAdapter: PrintRackAdapter
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

    private val ACTIVITY_BARCODESCANNER_DETAILS_CODE = 151
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
        PopUpWIndowQr(context, R.layout.layout_image_fullview, view, imageUrl, null, rackNo, position)

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
        intent.putExtra("activity", "upload")

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
                    val newWidth = 1920 // Desired width for the new Bitmap

                    val newHeight = 1200 // Desired height for the new Bitmap

                    val resizedBitmap1 = resizeBitmap(bitmap1, newWidth, newHeight)
                    val resizedBitmap2 = resizeBitmap(bitmap2, newWidth, newHeight)
                    val threshold = 21

                    val matchingPercentage: Double =
                        calculateMatchingPercentage(resizedBitmap1!!, resizedBitmap2!!)

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

    override fun onClickQrCodePrint() {
        if (!reviewImagesList.isNullOrEmpty()) {
            selectRackPrint()
        }
        /*if (!reviewImagesList.isNullOrEmpty()) {
            if (!BluetoothManager.getInstance(this@RetroQrUploadActivity).isConnect()) {
                val dialogView =
                    Dialog(this@RetroQrUploadActivity) // R.style.Theme_AppCompat_DayNight_NoActionBar
                val connectPrinterBinding: DialogConnectPrinterBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(this@RetroQrUploadActivity),
                    R.layout.dialog_connect_printer,
                    null,
                    false
                )
                dialogView.setContentView(connectPrinterBinding.getRoot())
                dialogView.setCancelable(false)
                dialogView.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                connectPrinterBinding.dialogButtonOK.setOnClickListener { view ->
                    dialogView.dismiss()
                    var intent = Intent(this@RetroQrUploadActivity, BluetoothActivity::class.java)
                    startActivityForResult(
                        intent,
                        ACTIVITY_BARCODESCANNER_DETAILS_CODE
                    )
                }
                connectPrinterBinding.dialogButtonNO.setOnClickListener { view -> dialogView.dismiss() }
                //            connectPrinterBinding.dialogButtonNot.setOnClickListener(view -> dialogView.dismiss());
                dialogView.show()

                //Toast.makeText(getContext(), "Please connect Bluetooth first", Toast.LENGTH_SHORT).show();
                // startActivityForResult(BluetoothActivity.getStartIntent(getContext()), ACTIVITY_BARCODESCANNER_DETAILS_CODE);
                // overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                // return;
            } else {
                generatebarcode()
            }
        }*/
    }

    fun generatebarcode() {
        if (!BluetoothManager.getInstance(this@RetroQrUploadActivity).isConnect) {
            Toast.makeText(
                this@RetroQrUploadActivity,
                "Your printer is disconnected. Please connect to Printer by clicking on Reprint Barcode",
                Toast.LENGTH_LONG
            ).show()
        } else {
            selectRackPrint()
        }
    }

    private fun qrCode(content: String): Bitmap? {
//        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)
//        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
//        val content = "Rack 1\n16001"
        try {
            val hints: MutableMap<EncodeHintType, Any?> = HashMap()
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 170, 170, hints)
            val barcodeEncoder = BarcodeEncoder()
            val qrCodeBitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            return qrCodeBitmap
            // Overlay the QR code with the logo
            /* val logoBitmap = BitmapFactory.decodeResource(
                 resources, R.drawable.fifteen_ppp
             )
             return overlayBitmap(qrCodeBitmap, logoBitmap)!!*/
//            qrCodeImageView.setImageBitmap(combinedBitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }
    }

    private fun overlayBitmap(qrCodeBitmap: Bitmap, logoBitmap: Bitmap): Bitmap? {
        val qrCodeWidth = qrCodeBitmap.width
        val qrCodeHeight = qrCodeBitmap.height
        val logoWidth = logoBitmap.width
        val logoHeight = logoBitmap.height

        // Calculate the position to center the logo on the QR code
        val xPos = (qrCodeWidth - logoWidth) / 2
        val yPos = (qrCodeHeight - logoHeight) / 2

        // Create a new bitmap with the QR code
        val combinedBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, qrCodeBitmap.config)

        // Create a canvas for drawing
        val canvas = Canvas(combinedBitmap)
        canvas.drawBitmap(qrCodeBitmap, 0f, 0f, null)
        canvas.drawBitmap(logoBitmap, xPos.toFloat(), yPos.toFloat(), null)
        return combinedBitmap
    }

    var isAllChecked = false
    var dialogRackQrCodePrintBinding: DialogRackQrCodePrintBinding? = null
    fun selectRackPrint() {
        val dialog = Dialog(this@RetroQrUploadActivity)
        dialogRackQrCodePrintBinding = DataBindingUtil.inflate<DialogRackQrCodePrintBinding>(
            LayoutInflater.from(this@RetroQrUploadActivity),
            R.layout.dialog_rack_qr_code_print,
            null,
            false
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogRackQrCodePrintBinding!!.root)

//        isBluetoothConned(dialogRackQrCodePrintBinding!!)
        dialog.setCancelable(false)
        dialogRackQrCodePrintBinding!!.close.setOnClickListener {
            dialog.dismiss()
        }
        if (isAllChecked) {
            dialogRackQrCodePrintBinding!!.allCheckedIcon.setImageResource(R.drawable.retroqr_green_check_mark_icon)
        } else {
            dialogRackQrCodePrintBinding!!.allCheckedIcon.setImageResource(R.drawable.qc_checkbox)
        }


        printRackAdapter = PrintRackAdapter(this@RetroQrUploadActivity, reviewImagesList)
        dialogRackQrCodePrintBinding!!.printRackRecycler.adapter = printRackAdapter
        dialogRackQrCodePrintBinding!!.printRackRecycler.layoutManager =
            LinearLayoutManager(this@RetroQrUploadActivity)
        dialogRackQrCodePrintBinding!!.checkUncheckAllRacks.setOnClickListener {
            isAllChecked = !isAllChecked
            if (isAllChecked) {
                dialogRackQrCodePrintBinding!!.allCheckedIcon.setImageResource(R.drawable.retroqr_green_check_mark_icon)
            } else {
                dialogRackQrCodePrintBinding!!.allCheckedIcon.setImageResource(R.drawable.qc_checkbox)
            }
            for (i in reviewImagesList) {
                i.isRackSelected = isAllChecked
            }
            printRackAdapter.notifyDataSetChanged()
        }
        dialogRackQrCodePrintBinding!!.preview.setOnClickListener {
            dialog.dismiss()
            generateQrCodewithText(
                20,
                10,
                "M",
                7,
                0
            )

            /* val intent = Intent(this@RetroQrUploadActivity, PrintPreviewActivity::class.java)
             val bundle = Bundle()
             bundle.putSerializable("STORE_WISE_RACK_DETAILS", reviewImagesList)
             intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
             intent.putExtras(bundle)
             startActivity(intent)


             val instance: PrintfTSPLManager =
                 PrintfTSPLManager.getInstance(this@RetroQrUploadActivity)
             instance.clearCanvas()
             instance.initCanvas(90, 23)
             instance.setDirection(0)
             for (i in reviewImagesList) {
                 if (i.isRackSelected) {
                     //Print barcode
 //            instance.printBarCode(20, 10, "128", 130, 2, 2, 0, refnumber);
                     //Print QR code
 //            instance.printQrCode(20, 10, "M", 7, 0, contentttt);
                     instance.printQrCode(
                         20,
                         10,
                         "M",
                         7,
                         0,
                         "${i.rackno}-${Preferences.getQrSiteId()}"
                     )
                     instance.beginPrintf(1)
                 }
             }*/
        }
        dialog.show()
    }

    fun generateQrCodewithText(x: Int, y: Int, level: String, width: Int, rotation: Int) {
        var selectedRackList = ArrayList<StoreWiseRackDetails.StoreDetail>()

        for (i in reviewImagesList) {
            if (i.isRackSelected) {
                val content = "${i.rackno}-${Preferences.getQrSiteId()}"
                val bStream = ByteArrayOutputStream()
                qrCode(content)!!.compress(Bitmap.CompressFormat.PNG, 100, bStream)
                val byteArray = bStream.toByteArray()
                i.byteArray = byteArray
                selectedRackList.add(i)
            }
        }
        if (selectedRackList.size > 0) {
            val intent = Intent(this@RetroQrUploadActivity, PrintPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("STORE_WISE_RACK_DETAILS", selectedRackList)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            intent.putExtras(bundle)
            startActivity(intent)
        }else{
            Toast.makeText(this@RetroQrUploadActivity, "Please select racks to print", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickPrintIcon() {
        val dialog = Dialog(this@RetroQrUploadActivity)
        val dialogRackQrCodePrintBinding =
            DataBindingUtil.inflate<DialogRackQrCodePrintBinding>(
                LayoutInflater.from(this@RetroQrUploadActivity),
                R.layout.dialog_rack_qr_code_print,
                null,
                false
            )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogRackQrCodePrintBinding.root)
        dialog.setCancelable(false)
        dialogRackQrCodePrintBinding.close.setOnClickListener {
            dialog.dismiss()
        }
        printRackAdapter = PrintRackAdapter(this@RetroQrUploadActivity, reviewImagesList)
        dialogRackQrCodePrintBinding.printRackRecycler.adapter = printRackAdapter
        dialogRackQrCodePrintBinding.printRackRecycler.layoutManager =
            LinearLayoutManager(this@RetroQrUploadActivity)


        dialogRackQrCodePrintBinding.preview.setOnClickListener {
            val instance: PrintfTSPLManager =
                PrintfTSPLManager.getInstance(this@RetroQrUploadActivity)
            instance.clearCanvas()
            instance.initCanvas(90, 23)
            instance.setDirection(0)
            for (i in reviewImagesList) {
                if (i.isRackSelected) {
                    instance.printQrCode(
                        20,
                        10,
                        "M",
                        7,
                        0,
                        "${i.rackno}-${Preferences.getQrSiteId()}"
                    )
                    instance.beginPrintf(1)
                }
            }
        }
        dialog.show()

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
        val alpha1 = Color.alpha(color1)
        val r1 = Color.red(color1)
        val g1 = Color.green(color1)
        val b1 = Color.blue(color1)

        val alpha2 = Color.alpha(color2)
        val r2 = Color.red(color2)
        val g2 = Color.green(color2)
        val b2 = Color.blue(color2)

        val alphaDifference = if (alpha1 > thresholdAlpha || alpha2 > thresholdAlpha) {
            abs(alpha1 - alpha2)
        } else {
            0 // Ignore transparency if both colors are fully transparent
        }

        return alphaDifference + abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2)
    }

    fun calculateSimilarityPercentage(
        bitmap1: Bitmap,
        bitmap2: Bitmap,
        threshold: Int
    ): Double {
        // Ensure both bitmaps have the same dimensions
        if (bitmap1.width != bitmap2.width || bitmap1.height != bitmap2.height) {
            return 0.0
        }

        // Calculate the total color difference
        var totalColorDifference = 0
        for (x in 0 until bitmap1.width) {
            for (y in 0 until bitmap1.height) {
                totalColorDifference += calculateColorDifference(
                    bitmap1.getPixel(x, y),
                    bitmap2.getPixel(x, y),
                    threshold
                )
            }
        }

        // Calculate similarity percentage (average color difference)
        val totalPixels = bitmap1.width * bitmap1.height
        val averageColorDifference = totalColorDifference.toDouble() / totalPixels

        // Normalize the average color difference to be within the range [0, 1]
        val normalizedSimilarity = 1.0 - (averageColorDifference / 255.0)

        return normalizedSimilarity * 100.0
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
        return matchingPercentage
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

    var handlerBluetoothConnection = Handler()
    var runnableBluetoothConnection = Runnable {
        if (dialogRackQrCodePrintBinding != null) {
            if (BluetoothManager.getInstance(this).isConnect) {
                dialogRackQrCodePrintBinding!!.bluetoothIndicator.setBackgroundResource(R.drawable.green_background)
                dialogRackQrCodePrintBinding!!.connectedPrinterStatus.text =
                    "Connected to the printer"
//                dialogRackQrCodePrintBinding!!.reconnect.visibility = View.GONE
            } else {
                dialogRackQrCodePrintBinding!!.bluetoothIndicator.setBackgroundResource(R.drawable.red_background)
                dialogRackQrCodePrintBinding!!.connectedPrinterStatus.text =
                    "Disconnected to the printer"
//                dialogRackQrCodePrintBinding!!.reconnect.visibility = View.VISIBLE
            }
        }
        startPostDelay()
    }

    fun startPostDelay() {
        handlerBluetoothConnection.removeCallbacks(runnableBluetoothConnection)
        handlerBluetoothConnection.postDelayed(runnableBluetoothConnection, 500)
    }

    override fun onResume() {
        handlerBluetoothConnection.removeCallbacks(runnableBluetoothConnection)
        handlerBluetoothConnection.postDelayed(runnableBluetoothConnection, 500)
        super.onResume()
    }

    override fun onPause() {
        handlerBluetoothConnection.removeCallbacks(runnableBluetoothConnection)
        super.onPause()
    }
    fun isBluetoothConned(dialogRackQrCodePrintBinding: DialogRackQrCodePrintBinding) {
        if (dialogRackQrCodePrintBinding != null) {
            if (BluetoothManager.getInstance(this).isConnect) {
                dialogRackQrCodePrintBinding.bluetoothIndicator.setBackgroundResource(R.drawable.green_background)
                dialogRackQrCodePrintBinding.connectedPrinterStatus.text =
                    "Connected to the printer"
//                dialogRackQrCodePrintBinding.reconnect.visibility = View.GONE
            } else {
                dialogRackQrCodePrintBinding.bluetoothIndicator.setBackgroundResource(R.drawable.red_background)
                dialogRackQrCodePrintBinding.connectedPrinterStatus.text =
                    "Disconnected to the printer"
//                dialogRackQrCodePrintBinding.reconnect.visibility = View.VISIBLE
            }
        }
        handlerBluetoothConnection.removeCallbacks(runnableBluetoothConnection)
        handlerBluetoothConnection.postDelayed(runnableBluetoothConnection, 500)

    }
}