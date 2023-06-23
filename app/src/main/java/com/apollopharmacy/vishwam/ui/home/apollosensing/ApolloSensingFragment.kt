package com.apollopharmacy.vishwam.ui.home.apollosensing

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.apollosensing.activity.ApolloSensingStoreActivity
import com.apollopharmacy.vishwam.ui.home.apollosensing.adapter.PrescriptionImageAdapter
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.CheckScreenStatusResponse
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.ImageDto
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsResponse
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.UpdateUserDefaultSiteRequest
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.UpdateUserDefaultSiteResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import me.echodev.resizer.Resizer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class ApolloSensingFragment : BaseFragment<ApolloSensingViewModel, FragmentApolloSensingBinding>(),
    ApolloSensingFragmentCallback, MainActivityCallback {
    var isSendLinkApiCall: Boolean = false
    var isSiteIdEmpty: Boolean = false
    var employeeName: String = ""
    lateinit var prescriptionImageAdapter: PrescriptionImageAdapter
    var isOtpVerified: Boolean = false
    var isPrescriptionUpload: Boolean = false
    var countDownTime: Long = 60000
    var countDownTimer: CountDownTimer? = null
    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    var prescriptionImageList = ArrayList<ImageDto>()
    var storeData = ArrayList<LoginDetails.StoreData>()
    var otp = "-1"
    lateinit var dialog: Dialog
    var siteId: String = ""
    var errorMessage: String = ""
    override val layoutRes: Int
        get() = R.layout.fragment_apollo_sensing

    override fun retrieveViewModel(): ApolloSensingViewModel {
        return ViewModelProvider(this).get(ApolloSensingViewModel::class.java)
    }

    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this
        viewBinding.callback = this@ApolloSensingFragment

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
            viewModel.checkScreenStatus(this@ApolloSensingFragment)
        }

    }

    fun setUpNew() {
        val empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()
        var employeeDetailsResponse: EmployeeDetailsResponse? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(
                empDetailsResponse,
                EmployeeDetailsResponse::class.java
            )

        } catch (e: JsonParseException) {
            e.printStackTrace()
        }

        if (employeeDetailsResponse != null && employeeDetailsResponse.data != null && employeeDetailsResponse.data!!.role != null) {
            if (employeeDetailsResponse!!.data!!.role!!.name!!.equals("Store Supervisor", true)) {
                val site = employeeDetailsResponse.data!!.site!!.site
                val storeName = employeeDetailsResponse.data!!.site!!.storeName
                if ((site != null && storeName != null) && (site.isNotEmpty() && storeName.isNotEmpty())) {
                    siteId = site
                    viewBinding.storeId.setText(site)
                    viewBinding.storeName.setText(storeName)
                    MainActivity.mInstance.siteIdIcon.visibility = View.GONE
                } else if (Preferences.getApolloSensingStoreId().isEmpty()) {
                    showLoading()
                    val intent = Intent(context, ApolloSensingStoreActivity::class.java)
                    startActivityForResult(intent, 571)
                } else {
                    siteId = Preferences.getApolloSensingStoreId()
                    viewBinding.storeId.setText(Preferences.getApolloSensingStoreId())
                    viewBinding.storeName.setText(Preferences.getApolloSensingStoreName())
                }
            } else if (Preferences.getApolloSensingStoreId().isEmpty()) {
                showLoading()
                val intent = Intent(context, ApolloSensingStoreActivity::class.java)
                startActivityForResult(intent, 571)
            } else {
                siteId = Preferences.getApolloSensingStoreId()
                viewBinding.storeId.setText(Preferences.getApolloSensingStoreId())
                viewBinding.storeName.setText(Preferences.getApolloSensingStoreName())
            }
        } else if (Preferences.getApolloSensingStoreId().isEmpty()) {
            showLoading()
            val intent = Intent(context, ApolloSensingStoreActivity::class.java)
            startActivityForResult(intent, 571)
        } else {
            siteId = Preferences.getApolloSensingStoreId()
            viewBinding.storeId.setText(Preferences.getApolloSensingStoreId())
            viewBinding.storeName.setText(Preferences.getApolloSensingStoreName())
        }
        val userData = LoginRepo.getProfile()
        otpValidation()
        if (userData != null) {
            employeeName = userData.EMPNAME
            storeData = userData.STOREDETAILS
        }

        viewBinding.employeeId.setText(Preferences.getToken())
        viewBinding.employeeName.setText(employeeName)

//        if (storeData.size > 0) {
//            viewBinding.storeId.setText(storeData.get(0).SITEID)
//            viewBinding.storeName.setText(storeData.get(0).SITENAME)
//            viewBinding.storeLocation.setText(storeData.get(0).DCNAME)
//        }

        viewBinding.sendLink.setOnClickListener {
            viewBinding.uploadCustomerPrescriptionLayout.visibility = View.GONE
            viewBinding.customerPrescriptionLayout.visibility = View.VISIBLE
            viewBinding.sendLinkBtn.visibility = View.VISIBLE
        }

        viewBinding.sendOtpBtn.setOnClickListener {
            if (validateSendLinkCustomerDetails()) {
                val sendGlobalSmsRequest = SendGlobalSmsRequest()
                sendGlobalSmsRequest.sourceFor = "SENSING"
                sendGlobalSmsRequest.type = "OTP"
                sendGlobalSmsRequest.mobileNo =
                    viewBinding.customerPhoneNumber.text.toString().trim()
                sendGlobalSmsRequest.link = ""
                sendGlobalSmsRequest.customerName = viewBinding.name.text.toString().trim()
                if (NetworkUtil.isNetworkConnected(requireContext())) {
                    showLoading()
                    Utlis.hideKeyPad(context as Activity)
                    retrieveViewModel().sendGlobalSmsApiCall(
                        "OTP", sendGlobalSmsRequest, this@ApolloSensingFragment
                    )
                }


//                isOtpVerified = true
//                viewBinding.sendOtpBtn.visibility = View.GONE
//                viewBinding.otpVerificationLayout.visibility = View.VISIBLE
//                viewBinding.verifiedSuccessfullyLayout.visibility = View.VISIBLE
//                viewBinding.sendLinkBtn.setBackgroundColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.greenn
//                    )
//                )
//                viewBinding.sendLinkText.setTextColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.white
//                    )
//                )
//                startTimer()
            }
        }

//        viewBinding.sendLinkBtn.setOnClickListener {
//            if (isOtpVerified) {
//                showConfirmDialog()
//            } else {
//            }
//        }

        viewBinding.takePhoto.setOnClickListener {
            viewBinding.uploadCustomerPrescriptionLayout.visibility = View.GONE
            viewBinding.uploadPrescriptionLayout.visibility = View.VISIBLE
            viewBinding.uploadPrescriptionBtn.visibility = View.VISIBLE
        }

        if (prescriptionImageList.size == 0) {
            viewBinding.prescriptionImgRcvLayout.gravity = Gravity.CENTER_HORIZONTAL
        }

        viewBinding.addMorePrescription.setOnClickListener {
            val phoneNumber = viewBinding.phoneNumber.text.toString().trim()
            val name = viewBinding.custName.text.toString().trim()
            if (validateCustomerDetails(phoneNumber, name)) {
                if (!checkPermission()) {
                    askPermissions(100)
                    return@setOnClickListener
                } else {
                    if (prescriptionImageList.size == 2) {
                        Toast.makeText(
                            requireContext(),
                            "You are allowed to upload only two prescriptions",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        showOption()
                    }
                }
            } else {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }

//            val phoneNumber = viewBinding.phoneNumber.text.toString().trim()
//            val name = viewBinding.custName.text.toString().trim()
//            if (phoneNumber.isNotEmpty() && name.isNotEmpty()) {
//                if (phoneNumber.length == 10 && !phoneNumber.equals("0000000000")) {
//                    if (!checkPermission()) {
//                        askPermissions(100)
//                        return@setOnClickListener
//                    } else {
////                        openCamera()
//                        if (prescriptionImageList.size == 10) {
//                            Toast.makeText(
//                                requireContext(),
//                                "You are allowed to upload only ten prescriptions",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            showOption()
//                        }
//                    }
//                } else {
//                    Toast.makeText(
//                        requireContext(), "Please enter valid phone number", Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                Toast.makeText(
//                    requireContext(), "Please enter phone number and name", Toast.LENGTH_SHORT
//                ).show()
//            }
        }

//        viewBinding.uploadPrescriptionBtn.setOnClickListener {
//            if (isPrescriptionUpload) {
//                openDialog()
//            } else {
//            }
//        }
    }

    private fun validateCustomerDetails(phoneNumber: String, name: String): Boolean {
        if (phoneNumber.isEmpty()) {
            viewBinding.phoneNumber.requestFocus()
            errorMessage = "Customer phone number should not be empty."
            return false
        } else if (phoneNumber.length < 10) {
            viewBinding.phoneNumber.requestFocus()
            errorMessage = "Customer phone number must be 10 digits."
            return false
        } else if (phoneNumber.equals("0000000000")) {
            viewBinding.phoneNumber.requestFocus()
            errorMessage = "Customer phone number should not contain all digits zero."
            return false
        } else if (name.isEmpty()) {
            viewBinding.custName.requestFocus()
            errorMessage = "Name should not be empty."
            return false
        } else {
            errorMessage = ""
            return true
        }
    }

    fun stopTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

    fun validateSendLinkCustomerDetails(): Boolean {
        var customerPhoneNumber = viewBinding.customerPhoneNumber.text.toString().trim()
        var customerName = viewBinding.name.text.toString().trim()
        if (customerPhoneNumber.isEmpty()) {
            viewBinding.customerPhoneNumber.requestFocus()
            Toast.makeText(
                context, "Customer phone number should not be empty.", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (customerPhoneNumber.length != 10) {
            viewBinding.customerPhoneNumber.requestFocus()
            Toast.makeText(context, "Customer phone number must be 10 digits.", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (customerPhoneNumber.equals("0000000000")) {
            viewBinding.customerPhoneNumber.requestFocus()
            Toast.makeText(
                context,
                "Customer phone number should not contain all digits zero.",
                Toast.LENGTH_SHORT
            )
                .show()
            return false
        } else if (customerName.isEmpty()) {
            viewBinding.name.requestFocus()
            Toast.makeText(context, "Customer name should not be empty.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    private fun startTimer() {
        viewBinding.resendOtp.visibility = View.GONE
        countDownTimer = object : CountDownTimer(countDownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingTime = millisUntilFinished / 1000
                val timeFormat = String.format("%02d:%02d", remainingTime / 60, remainingTime % 60)
                viewBinding.timer.setText(timeFormat)
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                otp = "-1"
                viewBinding.timer.setText("00:00")
                viewBinding.resendOtp.visibility = View.VISIBLE
                viewBinding.sendLinkBtn.setBackgroundColor(Color.parseColor("#efefef"))
                viewBinding.sendLinkText.setTextColor(Color.parseColor("#b5b5b5"))
                viewBinding.verifiedSuccessfullyLayout.visibility = View.GONE
                viewBinding.otpView.getText()!!.clear()
            }
        }
        countDownTimer!!.start()
    }

    private fun openDialog() {
        val dialog = Dialog(requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val prescriptionUploadedConfirmDialogBinding =
            DataBindingUtil.inflate<PrescriptionUploadedConfirmDialogBinding>(
                LayoutInflater.from(requireContext()),
                R.layout.prescription_uploaded_confirm_dialog,
                null,
                false
            )
        dialog.setContentView(prescriptionUploadedConfirmDialogBinding.root)

        prescriptionUploadedConfirmDialogBinding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        prescriptionUploadedConfirmDialogBinding.okButton.setOnClickListener {
            viewBinding.phoneNumber.getText()!!.clear()
            viewBinding.custName.getText()!!.clear()
            viewBinding.prescriptionImgRcvLayout.gravity = Gravity.CENTER_HORIZONTAL
            viewBinding.prescriptionImgRcv.visibility = View.GONE
            viewBinding.uploadPrescriptionLayout.visibility = View.GONE
            viewBinding.uploadPrescriptionBtn.visibility = View.GONE
            viewBinding.uploadCustomerPrescriptionLayout.visibility = View.VISIBLE
            viewBinding.uploadYourPrescriptionLayout.visibility = View.GONE
            viewBinding.uploadPrescriptionBtn.setBackgroundColor(Color.parseColor("#efefef"))
            viewBinding.uploadPrescriptionText.setTextColor(Color.parseColor("#b5b5b5"))
            prescriptionImageList.clear()
            isPrescriptionUpload = false
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showOption() {
        dialog = Dialog(requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val chooseImageOptionLayoutBinding =
            DataBindingUtil.inflate<ChooseImageOptionLayoutBinding>(
                LayoutInflater.from(requireContext()),
                R.layout.choose_image_option_layout,
                null,
                false
            )
        dialog.setContentView(chooseImageOptionLayoutBinding.root)
        chooseImageOptionLayoutBinding.cancel.setOnClickListener {
            dialog.dismiss()
        }
        // Open camera
        chooseImageOptionLayoutBinding.camera.setOnClickListener {
            openCamera()
        }
        // Open gallery
        chooseImageOptionLayoutBinding.gallery.setOnClickListener {
            openGallery()
        }

        dialog.setCancelable(false)
        dialog.show()


//        ImagePicker.with(this@ApolloSensingFragment)
//            .crop()
//            .start(Config.REQUEST_CODE_CAMERA)

//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")
//        compressedImageFileName = "${System.currentTimeMillis()}.jpg"
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
//        } else {
//            val photoUri = FileProvider.getUriForFile(
//                ViswamApp.context, ViswamApp.context.packageName + ".provider", imageFile!!
//            )
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            Config.REQUEST_CODE_GALLERY
        )
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

    fun encodeImage(path: String): String? {
        val imagefile = File(path)
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imagefile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bm = BitmapFactory.decodeStream(fis)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        //Base64.de
        return android.util.Base64.encodeToString(b, android.util.Base64.NO_WRAP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFile != null && resultCode == Activity.RESULT_OK) {
//            val imageBase64 = encodeImage(imageFile!!.absolutePath)
//            prescriptionImageList.add(ImageDto(imageFile!!, imageBase64!!))
//        }
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                isSiteIdEmpty = data.getBooleanExtra("isSiteIdEmpty", isSiteIdEmpty)
            }

            if (requestCode == Config.REQUEST_CODE_CAMERA && imageFile != null) {
                val imageBase64 = encodeImage(imageFile!!.absolutePath)
                prescriptionImageList.add(ImageDto(imageFile!!, imageBase64!!))
                viewBinding.imageCount!!.setText(prescriptionImageList.size.toString())
                dialog.dismiss()
            } else if (requestCode == Config.REQUEST_CODE_GALLERY) {
                dialog.dismiss()
                val images = data!!.clipData
                if (images != null) {
                    if (images!!.itemCount <= 2) {
                        for (i in 0 until images.itemCount) {
                            var imagePath =
                                getRealPathFromURI(requireContext(), images.getItemAt(i).uri)
                            var imageFileGallery: File? = File(imagePath)
                            val imageBase64 = encodeImage(imageFileGallery!!.absolutePath)
                            if (prescriptionImageList.size < 2) {
                                prescriptionImageList.add(
                                    ImageDto(
                                        imageFileGallery!!,
                                        imageBase64!!
                                    )
                                )
                                viewBinding.imageCount!!.setText(prescriptionImageList.size.toString())
                            } else {
                                break
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "You are allowed to upload only two prescription",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val uri = data.data
                    var imagePath = getRealPathFromURI(requireContext(), uri!!)
                    var imageFileGallery: File? = File(imagePath)
                    val imageBase64 = encodeImage(imageFileGallery!!.absolutePath)
                    prescriptionImageList.add(ImageDto(imageFileGallery!!, imageBase64!!))
                    viewBinding.imageCount!!.setText(prescriptionImageList.size.toString())
                }
            } else if (requestCode == 571) {
                if (isSiteIdEmpty) {
                    MainActivity.mInstance.onBackPressed()
                    hideLoading()
                } else {
                    siteId = Preferences.getApolloSensingStoreId()
                    viewBinding.storeId.setText(Preferences.getApolloSensingStoreId())
                    viewBinding.storeName.setText(Preferences.getApolloSensingStoreName())

                    viewBinding.customerPhoneNumber.getText()!!.clear()
                    viewBinding.name.getText()!!.clear()
                    viewBinding.otpView.getText()!!.clear()
                    viewBinding.uploadCustomerPrescriptionLayout.visibility = View.VISIBLE
                    viewBinding.sendOtpBtn.visibility = View.VISIBLE
                    viewBinding.customerPrescriptionLayout.visibility = View.GONE
                    viewBinding.sendLinkBtn.visibility = View.GONE
                    viewBinding.otpVerificationLayout.visibility = View.GONE
                    viewBinding.verifiedSuccessfullyLayout.visibility = View.GONE
                    viewBinding.sendLinkBtn.setBackgroundColor(Color.parseColor("#efefef"))
                    viewBinding.sendLinkText.setTextColor(Color.parseColor("#b5b5b5"))

                    viewBinding.phoneNumber.getText()!!.clear()
                    viewBinding.custName.getText()!!.clear()
                    viewBinding.prescriptionImgRcvLayout.gravity = Gravity.CENTER_HORIZONTAL
                    viewBinding.prescriptionImgRcv.visibility = View.GONE
                    viewBinding.uploadPrescriptionLayout.visibility = View.GONE
                    viewBinding.uploadPrescriptionBtn.visibility = View.GONE
                    viewBinding.uploadCustomerPrescriptionLayout.visibility = View.VISIBLE
                    viewBinding.uploadYourPrescriptionLayout.visibility = View.GONE
                    viewBinding.uploadPrescriptionBtn.setBackgroundColor(Color.parseColor("#efefef"))
                    viewBinding.uploadPrescriptionText.setTextColor(Color.parseColor("#b5b5b5"))
                    prescriptionImageList.clear()
                    isPrescriptionUpload = false
                    hideLoading()
                }
            }
        }
        if (prescriptionImageList.size > 0) {
            viewBinding.prescriptionImgRcvLayout.gravity = Gravity.START
            viewBinding.prescriptionImgRcv.visibility = View.VISIBLE
            viewBinding.uploadYourPrescriptionLayout.visibility = View.VISIBLE
            viewBinding.uploadPrescriptionBtn.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.greenn
                )
            )
            viewBinding.uploadPrescriptionText.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.white
                )
            )
            isPrescriptionUpload = true
            prescriptionImageAdapter = PrescriptionImageAdapter(
                requireContext(), this@ApolloSensingFragment, prescriptionImageList
            )
            viewBinding.prescriptionImgRcv.adapter = prescriptionImageAdapter
            viewBinding.prescriptionImgRcv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/" + split[1]
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    }

                    isDownloadsDocument(uri) -> {
                        val fileName = getFilePath(context, uri)
                        if (fileName != null) {
                            return Environment.getExternalStorageDirectory()
                                .toString() + "/Download/" + fileName
                        }
                        var id = DocumentsContract.getDocumentId(uri)
                        if (id.startsWith("raw:")) {
                            id = id.replaceFirst("raw:".toRegex(), "")
                            val file = File(id)
                            if (file.exists()) return id
                        }
                        val contentUri =
                            ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                java.lang.Long.valueOf(id)
                            )
                        return getDataColumn(context, contentUri, null, null)
                    }

                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }

                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }

                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                }
            }

            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            }

            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(
                uri, projection, null, null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
//    private fun compressImageSize(imageFile: File): File {
//        val compressedImage = Resizer(requireContext())
//            .setTargetLength(1080)
//            .setQuality(100)
//            .setOutputFormat("JPG")
//            .setOutputDirPath(ViswamApp.context.cacheDir.toString())
//            .setSourceImage(imageFile)
//            .resizedFile
//        return compressedImage
//    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
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

    private fun showConfirmDialog() {
        val dialog = Dialog(requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val linkSendConfirmDialogBinding = DataBindingUtil.inflate<LinkSendConfirmDialogBinding>(
            LayoutInflater.from(requireContext()), R.layout.link_send_confirm_dialog, null, false
        )
        dialog.setContentView(linkSendConfirmDialogBinding.root)
        linkSendConfirmDialogBinding.customerPhoneNumber.text =
            viewBinding.customerPhoneNumber.text.toString().trim()
        linkSendConfirmDialogBinding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        linkSendConfirmDialogBinding.okButton.setOnClickListener {
            stopTimer()
            viewBinding.customerPhoneNumber.getText()!!.clear()
            viewBinding.name.getText()!!.clear()
            viewBinding.otpView.getText()!!.clear()
            viewBinding.uploadCustomerPrescriptionLayout.visibility = View.VISIBLE
            viewBinding.sendOtpBtn.visibility = View.VISIBLE
            viewBinding.customerPrescriptionLayout.visibility = View.GONE
            viewBinding.sendLinkBtn.visibility = View.GONE
            viewBinding.otpVerificationLayout.visibility = View.GONE
            viewBinding.verifiedSuccessfullyLayout.visibility = View.GONE
            viewBinding.sendLinkBtn.setBackgroundColor(Color.parseColor("#efefef"))
            viewBinding.sendLinkText.setTextColor(Color.parseColor("#b5b5b5"))
            dialog.dismiss()
            isOtpVerified = false
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun deleteImage(position: Int, file: File) {
        prescriptionImageList.removeAt(position)
        viewBinding.imageCount!!.setText(prescriptionImageList.size.toString())
        if (prescriptionImageList.size < 1) {
            viewBinding.prescriptionImgRcvLayout.gravity = Gravity.CENTER_HORIZONTAL
            viewBinding.prescriptionImgRcv.visibility = View.GONE
            viewBinding.uploadYourPrescriptionLayout.visibility = View.GONE
            viewBinding.uploadPrescriptionBtn.setBackgroundColor(Color.parseColor("#efefef"))
            viewBinding.uploadPrescriptionText.setTextColor(Color.parseColor("#b5b5b5"))
            isPrescriptionUpload = false
        }
        prescriptionImageAdapter.notifyDataSetChanged()
    }

    override fun onSuccessSendGlobalSms(
        sendGlobalSmsResponse: SendGlobalSmsResponse,
        type: String,
    ) {
        hideLoading()
        if (type.equals("OTP")) {
            otp = sendGlobalSmsResponse.otp!!
            viewBinding.otpView.text!!.clear()
            isOtpVerified = true
            viewBinding.sendOtpBtn.visibility = View.GONE
            viewBinding.otpVerificationLayout.visibility = View.VISIBLE
//            viewBinding.verifiedSuccessfullyLayout.visibility = View.VISIBLE
//            viewBinding.sendLinkBtn.setBackgroundColor(
//                ContextCompat.getColor(
//                    requireContext(), R.color.greenn
//                )
//            )
//            viewBinding.sendLinkText.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(), R.color.white
//                )
//            )
            startTimer()
        } else {
//            showConfirmDialog()
            isSendLinkApiCall = true
            val updateUserDefaultSiteRequest = UpdateUserDefaultSiteRequest()
            updateUserDefaultSiteRequest.empId = Preferences.getToken()
            updateUserDefaultSiteRequest.site = siteId
            retrieveViewModel().updateDefaultSiteIdApiCall(
                updateUserDefaultSiteRequest,
                this@ApolloSensingFragment
            )
        }
    }

    override fun onFailureSendGlobalSms(
        sendGlobalSmsResponse: SendGlobalSmsResponse,
        type: String,
    ) {
        hideLoading()
        if (sendGlobalSmsResponse.message != null) Toast.makeText(
            context, sendGlobalSmsResponse.message, Toast.LENGTH_SHORT
        ).show()
    }

    override fun onClickResend() {
        if (validateSendLinkCustomerDetails()) {
            val sendGlobalSmsRequest = SendGlobalSmsRequest()
            sendGlobalSmsRequest.sourceFor = "SENSING"
            sendGlobalSmsRequest.type = "OTP"
            sendGlobalSmsRequest.mobileNo = viewBinding.customerPhoneNumber.text.toString().trim()
            sendGlobalSmsRequest.link = ""
            sendGlobalSmsRequest.customerName = viewBinding.name.text.toString().trim()

            if (NetworkUtil.isNetworkConnected(requireContext())) {
                showLoading()
                Utlis.hideKeyPad(context as Activity)
                retrieveViewModel().sendGlobalSmsApiCall(
                    "OTP", sendGlobalSmsRequest, this@ApolloSensingFragment
                )
            }
        }
    }

    override fun onClickSendLinkBtn() {
        if (otp.equals(viewBinding.otpView.text.toString())) {
            if (NetworkUtil.isNetworkConnected(requireContext())) {
                showLoading()
                var customerPhoneNumber = viewBinding.customerPhoneNumber.text.toString().trim()
                var customerName = viewBinding.name.text.toString().trim()
                retrieveViewModel().getLinkApiCall(
                    customerName,
                    customerPhoneNumber,
                    this@ApolloSensingFragment,
                    Preferences.getApolloSensingStoreId(),
                    Utlis.getCurrentTimeStampFormat()!!
                )
            }
        }
    }

    override fun onSuccessGetLinkApolloSensing(link: String) {
        val sendGlobalSmsRequest = SendGlobalSmsRequest()
        sendGlobalSmsRequest.sourceFor = "SENSING"
        sendGlobalSmsRequest.type = "LINK"
        sendGlobalSmsRequest.mobileNo = viewBinding.customerPhoneNumber.text.toString().trim()
        sendGlobalSmsRequest.link = link
        sendGlobalSmsRequest.customerName = viewBinding.name.text.toString().trim()

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
            Utlis.hideKeyPad(context as Activity)
            retrieveViewModel().sendGlobalSmsApiCall(
                "LINK", sendGlobalSmsRequest, this@ApolloSensingFragment
            )
        }
    }

    override fun onFailureGetLinkApolloSensing(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    var referenceUrls = ArrayList<String>()
    override fun onClickUploadPrescription() {
        if (validateUploadPrescription()) {
            if (NetworkUtil.isNetworkConnected(requireContext())) {
                /* val saveImageUrlsRequest = SaveImageUrlsRequest()
                 saveImageUrlsRequest.siteId =
                     Preferences.getApolloSensingStoreId() //Preferences.getSiteId()
                 saveImageUrlsRequest.type = "STORE"
                 saveImageUrlsRequest.requestedBy = Preferences.getValidatedEmpId()
                 saveImageUrlsRequest.customerName = viewBinding.custName.text.toString().trim()
                 saveImageUrlsRequest.mobNo = viewBinding.phoneNumber.text.toString().trim()
                 val base64ImageList = ArrayList<SaveImageUrlsRequest.Base64Image>()
                 for (i in prescriptionImageList) {
                     val base64Image = SaveImageUrlsRequest.Base64Image()
                     base64Image.base64Image = i.base64Images
                     base64ImageList.add(base64Image)
                 }
                 saveImageUrlsRequest.base64ImageList = base64ImageList*/


//                retrieveViewModel().saveImageUrlsApiCall(
//                    saveImageUrlsRequest, this@ApolloSensingFragment
//                )
//                var file: File? = null
//                if (prescriptionImageList != null && prescriptionImageList.size > 0) {
//                    file = prescriptionImageList.get(0).file
//                }
//                if (file != null) {
//                    showLoading()
//                    val resizedImage = Resizer(requireContext())
//                        .setTargetLength(1080)
//                        .setQuality(100)
//                        .setOutputFormat("JPG")
////                .setOutputFilename(fileNameForCompressedImage)
//                        .setOutputDirPath(
//                            ViswamApp.Companion.context.cacheDir.toString()
//                        )
//
//                        .setSourceImage(file)
//                        .resizedFile
//                    retrieveViewModel().sensingFileUpload(this@ApolloSensingFragment, resizedImage)
//                }

                if (prescriptionImageList != null && prescriptionImageList.size > 0) {
                    showLoading()
                    referenceUrls.clear()
                    uploadImages(prescriptionImageList, 0)
                }
            }
        }

    }


    fun uploadImages(prescriptionImageList: ArrayList<ImageDto>?, pos: Int) {
        if (prescriptionImageList != null && prescriptionImageList.size > 0) {
            var file: File? = null
            if (prescriptionImageList != null && prescriptionImageList.size > 0) {
                file = prescriptionImageList.get(pos).file
            }
            if (file != null) {
                showLoading()
                val resizedImage = Resizer(requireContext())
                    .setTargetLength(1080)
                    .setQuality(100)
                    .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                    .setOutputDirPath(
                        ViswamApp.Companion.context.cacheDir.toString()
                    )

                    .setSourceImage(file)
                    .resizedFile

                if (pos == 1 || prescriptionImageList.size == 1) {
                    retrieveViewModel().sensingFileUpload(
                        this@ApolloSensingFragment,
                        resizedImage,
                        true
                    )
                } else {
                    retrieveViewModel().sensingFileUpload(
                        this@ApolloSensingFragment,
                        resizedImage,
                        false
                    )
                }
            }
        }
    }

    override fun onSuccessUploadPrescriptionApiCall(message: String) {
        hideLoading()
//        openDialog()
        isSendLinkApiCall = false
        val updateUserDefaultSiteRequest = UpdateUserDefaultSiteRequest()
        updateUserDefaultSiteRequest.empId = Preferences.getToken()
        updateUserDefaultSiteRequest.site = siteId
        retrieveViewModel().updateDefaultSiteIdApiCall(
            updateUserDefaultSiteRequest,
            this@ApolloSensingFragment
        )
    }

    override fun onFailureUploadPrescriptionApiCall(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickBacktoMainScreen() {
        confirmationForResetLinkForm()
    }

    override fun onClickBacktoMainScreenPrescription() {
        confirmationForResetUploadPrescriptionForm()
    }

    override fun onSuccessUpdateDefaultSiteIdApiCall(updateUserDefaultSiteResponse: UpdateUserDefaultSiteResponse) {
        if (isSendLinkApiCall) {
            showConfirmDialog()
        } else {
            openDialog()
        }
    }

    override fun onSuccessCheckScreenStatusApiCall(checkScreenStatusResponse: CheckScreenStatusResponse) {
        hideLoading()
        if (checkScreenStatusResponse != null && checkScreenStatusResponse!!.status == true) {
            if (checkScreenStatusResponse.CUSTLINK!! == true && checkScreenStatusResponse.STORELINK!! == true) {
                viewBinding.sendLink.visibility = View.VISIBLE
                viewBinding.or.visibility = View.VISIBLE
                viewBinding.takePhoto.visibility = View.VISIBLE
            } else if (checkScreenStatusResponse.CUSTLINK!! == true) {
                viewBinding.sendLink.visibility = View.VISIBLE
                viewBinding.or.visibility = View.GONE
                viewBinding.takePhoto.visibility = View.GONE
            } else if (checkScreenStatusResponse.STORELINK!! == true) {
                viewBinding.sendLink.visibility = View.GONE
                viewBinding.or.visibility = View.GONE
                viewBinding.takePhoto.visibility = View.VISIBLE
            }
        }
        setUpNew()
    }

    override fun onFailureCheckScreenStatusApiCall(message: String) {
        hideLoading()
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessSensingFileUploadApiCall(
        sensingFileUploadResponse: SensingFileUploadResponse,
        isLastImage: Boolean,
    ) {
        Toast.makeText(requireContext(), sensingFileUploadResponse.message, Toast.LENGTH_SHORT)
            .show()
        referenceUrls.add(sensingFileUploadResponse.referenceurl!!)
        if (isLastImage) {
            val saveImageUrlsRequest = SaveImageUrlsRequest()
            saveImageUrlsRequest.siteId =
                Preferences.getApolloSensingStoreId() //Preferences.getSiteId()
            saveImageUrlsRequest.type = "STORE"
            saveImageUrlsRequest.requestedBy = Preferences.getValidatedEmpId()
            saveImageUrlsRequest.customerName = viewBinding.custName.text.toString().trim()
            saveImageUrlsRequest.mobNo = viewBinding.phoneNumber.text.toString().trim()
            val base64ImageList = ArrayList<SaveImageUrlsRequest.Base64Image>()
            for (i in referenceUrls) {
                val base64Image = SaveImageUrlsRequest.Base64Image()
                base64Image.base64Image = i
                base64ImageList.add(base64Image)
            }
            saveImageUrlsRequest.base64ImageList = base64ImageList
            retrieveViewModel().saveImageUrlsApiCall(
                saveImageUrlsRequest, this@ApolloSensingFragment
            )
        } else {
            uploadImages(prescriptionImageList, 1)
        }

    }

    override fun onFailureSensingFileUploadApiCall(sensingFileUploadResponse: SensingFileUploadResponse) {
        hideLoading()
        Toast.makeText(requireContext(), sensingFileUploadResponse.message, Toast.LENGTH_SHORT)
            .show()
    }

    fun validateUploadPrescription(): Boolean {
        var customerPhoneNumber = viewBinding.phoneNumber.text.toString().trim()
        var customerName = viewBinding.custName.text.toString().trim()
        if (customerPhoneNumber.isEmpty()) {
            viewBinding.phoneNumber.requestFocus()
            Toast.makeText(
                context, "Customer phone number should not be empty.", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (customerPhoneNumber.length != 10) {
            viewBinding.phoneNumber.requestFocus()
            Toast.makeText(context, "Customer phone number must be 10 digits.", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (customerPhoneNumber.equals("0000000000")) {
            viewBinding.phoneNumber.requestFocus()
            Toast.makeText(
                context,
                "Customer phone number should not contain all digits zero.",
                Toast.LENGTH_SHORT
            )
                .show()
            return false
        } else if (customerName.isEmpty()) {
            viewBinding.custName.requestFocus()
            Toast.makeText(context, "Customer name should not be empty.", Toast.LENGTH_SHORT).show()
            return false
        } else if (prescriptionImageList == null || prescriptionImageList.size == 0) {
            Toast.makeText(context, "No prescriptions available.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun confirmationForResetLinkForm() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogResetLinkSendFormBinding =
            DataBindingUtil.inflate<DialogResetLinkSendFormBinding>(
                LayoutInflater.from(context), R.layout.dialog_reset_link_send_form, null, false
            )
        dialog.setContentView(dialogResetLinkSendFormBinding.root)
        dialogResetLinkSendFormBinding.noButton.setOnClickListener {
            dialog.dismiss()
        }
        dialogResetLinkSendFormBinding.yesButton.setOnClickListener {
            stopTimer()
            viewBinding.customerPhoneNumber.getText()!!.clear()
            viewBinding.name.getText()!!.clear()
            viewBinding.otpView.getText()!!.clear()
            viewBinding.uploadCustomerPrescriptionLayout.visibility = View.VISIBLE
            viewBinding.sendOtpBtn.visibility = View.VISIBLE
            viewBinding.customerPrescriptionLayout.visibility = View.GONE
            viewBinding.sendLinkBtn.visibility = View.GONE
            viewBinding.otpVerificationLayout.visibility = View.GONE
            viewBinding.verifiedSuccessfullyLayout.visibility = View.GONE
            viewBinding.sendLinkBtn.setBackgroundColor(Color.parseColor("#efefef"))
            viewBinding.sendLinkText.setTextColor(Color.parseColor("#b5b5b5"))
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun confirmationForResetUploadPrescriptionForm() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogResetUploadPrescriptionFormBinding =
            DataBindingUtil.inflate<DialogResetPrescriptionUploadFormBinding>(
                LayoutInflater.from(context),
                R.layout.dialog_reset_prescription_upload_form,
                null,
                false
            )
        dialog.setContentView(dialogResetUploadPrescriptionFormBinding.root)
        dialogResetUploadPrescriptionFormBinding.noButton.setOnClickListener {
            dialog.dismiss()
        }
        dialogResetUploadPrescriptionFormBinding.yesButton.setOnClickListener {
            viewBinding.phoneNumber.getText()!!.clear()
            viewBinding.custName.getText()!!.clear()
            viewBinding.prescriptionImgRcvLayout.gravity = Gravity.CENTER_HORIZONTAL
            viewBinding.prescriptionImgRcv.visibility = View.GONE
            viewBinding.uploadPrescriptionLayout.visibility = View.GONE
            viewBinding.uploadPrescriptionBtn.visibility = View.GONE
            viewBinding.uploadCustomerPrescriptionLayout.visibility = View.VISIBLE
            viewBinding.uploadYourPrescriptionLayout.visibility = View.GONE
            viewBinding.uploadPrescriptionBtn.setBackgroundColor(Color.parseColor("#efefef"))
            viewBinding.uploadPrescriptionText.setTextColor(Color.parseColor("#b5b5b5"))
            prescriptionImageList.clear()
            isPrescriptionUpload = false
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun otpValidation() {
        viewBinding.otpView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int,
            ) {
                if (s != "") {
                    //do your work here
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int,
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s != null && s.toString().length == 6) {
                    if (s.toString().equals(otp)) {
                        Utlis.hideKeyPad(context as Activity)
                        viewBinding.sendLinkBtn.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.greenn
                            )
                        )
                        viewBinding.sendLinkText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.white
                            )
                        )
                        viewBinding.verifiedSuccessfullyLayout.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                        viewBinding.otpView.text!!.clear()
                        viewBinding.sendLinkBtn.setBackgroundColor(Color.parseColor("#efefef"))
                        viewBinding.sendLinkText.setTextColor(Color.parseColor("#b5b5b5"))
                    }
                } else {
                    viewBinding.sendLinkBtn.setBackgroundColor(Color.parseColor("#efefef"))
                    viewBinding.sendLinkText.setTextColor(Color.parseColor("#b5b5b5"))
                    viewBinding.verifiedSuccessfullyLayout.visibility = View.GONE
                }
            }
        })
    }

    override fun onClickFilterIcon() {
    }

    override fun onClickSiteIdIcon() {
        val intent = Intent(context, ApolloSensingStoreActivity::class.java)
        startActivityForResult(intent, 571)
    }

    override fun onClickQcFilterIcon() {
    }
}