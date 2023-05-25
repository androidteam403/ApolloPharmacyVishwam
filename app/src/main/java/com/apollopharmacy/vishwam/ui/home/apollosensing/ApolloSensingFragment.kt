package com.apollopharmacy.vishwam.ui.home.apollosensing

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.provider.MediaStore
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
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentApolloSensingBinding
import com.apollopharmacy.vishwam.databinding.LinkSendConfirmDialogBinding
import com.apollopharmacy.vishwam.databinding.PrescriptionUploadedConfirmDialogBinding
import com.apollopharmacy.vishwam.ui.home.apollosensing.adapter.PrescriptionImageAdapter
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.ImageDto
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class ApolloSensingFragment : BaseFragment<ApolloSensingViewModel, FragmentApolloSensingBinding>(),
    ApolloSensingFragmentCallback {
    var employeeName: String = ""
    lateinit var prescriptionImageAdapter: PrescriptionImageAdapter
    var isOtpVerified: Boolean = false
    var isPrescriptionUpload: Boolean = false
    var countDownTime: Long = 30000
    lateinit var countDownTimer: CountDownTimer
    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    var prescriptionImageList = ArrayList<ImageDto>()
    var storeData = ArrayList<LoginDetails.StoreData>()
    var otp = "-1"
    override val layoutRes: Int
        get() = R.layout.fragment_apollo_sensing

    override fun retrieveViewModel(): ApolloSensingViewModel {
        return ViewModelProvider(this).get(ApolloSensingViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this@ApolloSensingFragment
        val userData = LoginRepo.getProfile()
        if (userData != null) {
            employeeName = userData.EMPNAME
            storeData = userData.STOREDETAILS
        }

        viewBinding.employeeId.setText(Preferences.getToken())
        viewBinding.employeeName.setText(employeeName)

        if (storeData.size > 0) {
            viewBinding.storeId.setText(storeData.get(0).SITEID)
            viewBinding.storeName.setText(storeData.get(0).SITENAME)
            viewBinding.storeLocation.setText(storeData.get(0).DCNAME)
        }

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
            if (phoneNumber.isNotEmpty() && name.isNotEmpty()) {
                if (phoneNumber.length == 10) {
                    if (!checkPermission()) {
                        askPermissions(100)
                        return@setOnClickListener
                    } else {
                        openCamera()
                    }
                } else {
                    Toast.makeText(
                        requireContext(), "Please enter valid phone number", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(), "Please enter phone number and name", Toast.LENGTH_SHORT
                ).show()
            }
        }

//        viewBinding.uploadPrescriptionBtn.setOnClickListener {
//            if (isPrescriptionUpload) {
//                openDialog()
//            } else {
//            }
//        }
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
        } else if (customerName.isEmpty()) {
            viewBinding.name.requestFocus()
            Toast.makeText(context, "Customer name should not be empty.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    private fun startTimer() {
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
            }
        }
        countDownTimer.start()
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
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFile != null && resultCode == Activity.RESULT_OK) {
            val imageBase64 = encodeImage(imageFile!!.absolutePath)
            prescriptionImageList.add(ImageDto(imageFile!!, imageBase64!!))
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
            isOtpVerified = true
            viewBinding.sendOtpBtn.visibility = View.GONE
            viewBinding.otpVerificationLayout.visibility = View.VISIBLE
            viewBinding.verifiedSuccessfullyLayout.visibility = View.VISIBLE
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
            startTimer()
        } else {
            showConfirmDialog()
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
                    customerName, customerPhoneNumber, this@ApolloSensingFragment
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

    override fun onClickUploadPrescription() {
        if (validateUploadPrescription()) {
            if (NetworkUtil.isNetworkConnected(requireContext())) {
                showLoading()
                val saveImageUrlsRequest = SaveImageUrlsRequest()
                saveImageUrlsRequest.siteId = Preferences.getSiteId()
                saveImageUrlsRequest.type = "STORE"
                saveImageUrlsRequest.requestedBy = Preferences.getValidatedEmpId()
                val base64ImageList = ArrayList<SaveImageUrlsRequest.Base64Image>()
                for (i in prescriptionImageList) {
                    val base64Image = SaveImageUrlsRequest.Base64Image()
                    base64Image.base64Image = i.base64Images
                    base64ImageList.add(base64Image)
                }
                saveImageUrlsRequest.base64ImageList = base64ImageList


                retrieveViewModel().saveImageUrlsApiCall(
                    saveImageUrlsRequest, this@ApolloSensingFragment
                )
            }
        }

    }

    override fun onSuccessUploadPrescriptionApiCall(message: String) {
        hideLoading()
        openDialog()
    }

    override fun onFailureUploadPrescriptionApiCall(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickBacktoMainScreen() {
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
    }

    override fun onClickBacktoMainScreenPrescription() {
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
}