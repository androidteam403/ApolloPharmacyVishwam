package com.apollopharmacy.vishwam.ui.home.greeting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.ActivityGreetingBinding
import com.apollopharmacy.vishwam.databinding.DialogImagePreviewBinding
import com.apollopharmacy.vishwam.databinding.DialogWishCharmainSignatureBinding
import com.apollopharmacy.vishwam.databinding.DialogWishesImagePreviewBinding
import com.apollopharmacy.vishwam.ui.home.greeting.model.EmployeeWishesRequest
import com.apollopharmacy.vishwam.ui.home.greeting.model.EmployeeWishesResponse
import com.apollopharmacy.vishwam.ui.home.greeting.wishesblobstorage.EmployeeWishesBlobStorage
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import me.echodev.resizer.Resizer
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class GreetingActivity : AppCompatActivity(), GreetingActivityCallback {
    lateinit var activityGreetingBinding: ActivityGreetingBinding
    private val pic_id = 123

    private var employeeImagePath: File? = null
    private var fileNameForCompressedImage: String? = null

    private var employeeSignaturePath: File? = null
    lateinit var greetingViewModel: GreetingViewModel

    private var writeYourText: String? = "Write your Text here"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGreetingBinding = DataBindingUtil.setContentView(this, R.layout.activity_greeting)
        greetingViewModel = ViewModelProvider(this)[GreetingViewModel::class.java]

        activityGreetingBinding.callback = this;

        activityGreetingBinding.camera.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else {
                cameraIntent()
            }

        }



        activityGreetingBinding.text.setOnClickListener {
            val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog.setCancelable(false)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_write_text)

            dialog.show()
            var button: Button
            var clearText: ImageView
            clearText = dialog.findViewById(R.id.cleartext)
            var descriptionText: TextView
            descriptionText = dialog.findViewById(R.id.descriptionText)
            button = dialog.findViewById(R.id.continue_btn)

//            descriptionText.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))

            clearText.setOnClickListener {
                dialog.dismiss()
            }

            button.setOnClickListener {
                if (descriptionText.text.toString().isNullOrEmpty()) {
                    Toast.makeText(
                        this@GreetingActivity, "Your text should not be empty", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    writeYourText = descriptionText.text.toString()
                    activityGreetingBinding.text.setText(descriptionText.text.toString())
                    dialog.dismiss()
                }
            }


        }

        activityGreetingBinding.preview.setOnClickListener {
            if (writeYourText!!.equals("Write your Text here")) {
                Toast.makeText(this@GreetingActivity, "Please enter text", Toast.LENGTH_SHORT)
                    .show()
            } else if (employeeImagePath == null) {
                Toast.makeText(
                    this@GreetingActivity, "Please upload your picture", Toast.LENGTH_SHORT
                ).show()
            } else if (employeeSignaturePath == null) {
                Toast.makeText(
                    this@GreetingActivity, "Please give Your signature", Toast.LENGTH_SHORT
                ).show()
            } else {
                val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                val dialogImagePreviewBinding = DataBindingUtil.inflate<DialogImagePreviewBinding>(
                    LayoutInflater.from(this@GreetingActivity),
                    R.layout.dialog_image_preview,
                    null,
                    false
                )
                dialog.setCancelable(false)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(dialogImagePreviewBinding.root)
                dialogImagePreviewBinding.text.text = writeYourText
                Glide.with(this).load(employeeImagePath).into(dialogImagePreviewBinding.camera)
                Glide.with(this).load(employeeSignaturePath)
                    .into(dialogImagePreviewBinding.signatureImage)
                val userData = LoginRepo.getProfile()
                if (userData != null) {
                    dialogImagePreviewBinding.employeeName.text = userData.EMPNAME
                }

                dialogImagePreviewBinding.camera.setOnClickListener {
                    showImagePreview(true)
                }
                dialogImagePreviewBinding.signatureImage.setOnClickListener {
                    showImagePreview(false)
                }
                dialogImagePreviewBinding.dismissDialog.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()

            }
        }
    }

    private fun cameraIntent() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivityForResult(intent, pic_id)


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)


//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, pic_id);

        val filename = "${System.currentTimeMillis()}.jpg"
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        employeeImagePath = image
           // File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(employeeImagePath))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                employeeImagePath!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, pic_id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Match the request 'pic id with requestCode
        if (resultCode != RESULT_CANCELED && employeeImagePath != null) {
            if (requestCode == pic_id) {
//                // BitMap is data structure of image file which store the image in memory
//                val photo = data!!.extras!!["data"] as Bitmap?
//                // Set the image in imageview for display
                val userData = LoginRepo.getProfile()
                if (userData != null) {
                    activityGreetingBinding.employeeName.text = userData.EMPNAME
                }
//                storeBitmapEmplyeeImage(photo)


//                employeeImagePath?.length()
//                val resizedImage = Resizer(this)
//                    .setTargetLength(1080)
//                    .setQuality(100)
//                    .setOutputFormat("JPG")
////                .setOutputFilename(fileNameForCompressedImage)
//                    .setOutputDirPath(
//                        ViswamApp.Companion.context.cacheDir.toString()
//                    )
//
//                    .setSourceImage(employeeImagePath)
//                    .resizedFile

                Glide.with(this).load(employeeImagePath).into(activityGreetingBinding.camera)
                activityGreetingBinding.employeeName.visibility = View.VISIBLE
            }
        }
    }

    private fun storeBitmapEmplyeeImage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        //   "${Preferences.getValidatedEmpId()}_p.jpg" //"${System.currentTimeMillis()}.jpg"


        var fos: OutputStream? = null
        var filePath: File? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                filePath = image
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
            filePath = image

        }
        fos?.use {
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, it)
//            Toast.makeText(this@GreetingActivity, "Saved to Photos", Toast.LENGTH_LONG).show()
            it.close()
        }
        if (filePath!!.exists()) {
            employeeImagePath = filePath
            Glide.with(this).load(filePath).into(activityGreetingBinding.camera)
            activityGreetingBinding.employeeName.visibility = View.VISIBLE
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
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

    private val multiplePermissionId = 1
    private val permissionsReqList: ArrayList<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayListOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }
    private var dialogWishCharmainSignatureBinding: DialogWishCharmainSignatureBinding? = null
    private var signatureDialog: Dialog? = null
    override fun onClickYourSignatureHere() {
        signatureDialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        signatureDialog!!.setCancelable(false)
        signatureDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        signatureDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        dialogWishCharmainSignatureBinding =
            DataBindingUtil.inflate<DialogWishCharmainSignatureBinding>(
                LayoutInflater.from(this), R.layout.dialog_wish_charmain_signature, null, false
            )
        signatureDialog!!.setContentView(dialogWishCharmainSignatureBinding!!.root)

        val vto = dialogWishCharmainSignatureBinding!!.drawView.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                dialogWishCharmainSignatureBinding!!.drawView.viewTreeObserver.removeOnGlobalLayoutListener(
                    this
                )
                val width = dialogWishCharmainSignatureBinding!!.drawView.measuredWidth
                val height = dialogWishCharmainSignatureBinding!!.drawView.measuredHeight
                dialogWishCharmainSignatureBinding!!.drawView.init(height, width)
            }
        })

        dialogWishCharmainSignatureBinding!!.clearSignature.setOnClickListener {
            if (dialogWishCharmainSignatureBinding!!.drawView.isTouch()) {
                dialogWishCharmainSignatureBinding!!.drawView.clear()
            } else {
                Toast.makeText(this@GreetingActivity, "Signature is required!!", Toast.LENGTH_LONG)
                    .show()

            }
        }
        dialogWishCharmainSignatureBinding!!.continueBtn.setOnClickListener {
            if (dialogWishCharmainSignatureBinding!!.drawView.isTouch()) {
                if (checkMultipleRequestPermissions()) {
                    dialogWishCharmainSignatureBinding!!.drawView.save()
                        ?.let { it1 -> storeBitmap(it1) }
                }
            } else {
                Toast.makeText(this@GreetingActivity, "Signature is required!!", Toast.LENGTH_LONG)
                    .show()
            }
        }

        dialogWishCharmainSignatureBinding!!.dialogDismissIcon.setOnClickListener { signatureDialog!!.dismiss() }
        signatureDialog!!.show()


    }

    private fun storeBitmap(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        //  "${Preferences.getValidatedEmpId()}_s.jpg"//"${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var filePath: File? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                filePath = image

            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
            filePath = image

        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.close()
        }
        if (filePath != null) if (filePath!!.exists()) {
            employeeSignaturePath = filePath
            Glide.with(this).load(filePath).into(activityGreetingBinding.signatureImage)
            activityGreetingBinding.yourSignatureHereLayout.visibility = View.GONE
            activityGreetingBinding.signatureImage.visibility = View.VISIBLE
            signatureDialog!!.dismiss()
        }
    }

    private fun checkMultipleRequestPermissions(): Boolean {
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        for (p in permissionsReqList) {
            val result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, listPermissionsNeeded.toTypedArray(), multiplePermissionId
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    dialogWishCharmainSignatureBinding!!.drawView.save()
                        ?.let { it1 -> storeBitmap(it1) }
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this, permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this, permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }
                    if (someDenied) {
                        settingActivityOpen(this)
                    } else {
                        showDialogOK(this) { _: DialogInterface?, which: Int ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> checkMultipleRequestPermissions()
                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun settingActivityOpen(activity: Activity) {
        Toast.makeText(
            activity, "Go to settings and enable permissions", Toast.LENGTH_LONG
        ).show()
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val packageName = activity.packageName
        i.data = Uri.parse("package:$packageName")
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        activity.startActivity(i)
    }

    private fun showDialogOK(activity: Activity, okListener: DialogInterface.OnClickListener) {
        Toast.makeText(this, "All Permissions are required for this app", Toast.LENGTH_SHORT).show()
    }

    override fun onClickSend() {
        if (writeYourText!!.equals("Write your Text here")) {
            Toast.makeText(this@GreetingActivity, "Please enter text", Toast.LENGTH_SHORT).show()
        } else if (employeeImagePath == null) {
            Toast.makeText(
                this@GreetingActivity, "Please upload your picture", Toast.LENGTH_SHORT
            ).show()
        } else if (employeeSignaturePath == null) {
            Toast.makeText(
                this@GreetingActivity, "Please give Your signature", Toast.LENGTH_SHORT
            ).show()
        } else {
            Utlis.showLoading(this@GreetingActivity)
            var imageUrl: String? = null
            var signatureUrl: String? = null

            val thread = Thread {
                try {
                    // Your code goes here
                    imageUrl = EmployeeWishesBlobStorage.imageBlobStorage(
                        employeeImagePath!!, "${Preferences.getValidatedEmpId()}_p.jpg"
                    )
                    signatureUrl = EmployeeWishesBlobStorage.imageBlobStorage(
                        employeeSignaturePath!!, "${Preferences.getValidatedEmpId()}_s.jpg"
                    )
                    val userData = LoginRepo.getProfile()
                    var employeeWishesRequest = EmployeeWishesRequest()
                    var empDetails = EmployeeWishesRequest.EmpDetails()
                    empDetails.EmpID = userData!!.EMPID
                    empDetails.EmpName = userData!!.EMPNAME
                    empDetails.ImageUrl = imageUrl
                    empDetails.SignatureUrl = signatureUrl
                    empDetails.Siteid = 0
                    empDetails.SiteName = ""
                    empDetails.Contents = writeYourText
                    empDetails.organization = Preferences.getCompany()
                    empDetails.Location = ""
                    empDetails.Mobilenumber = Preferences.getEmpPhoneNumber()
                    employeeWishesRequest.empdetails = empDetails
                    greetingViewModel.employeeWishesApiCall(
                        employeeWishesRequest, this@GreetingActivity
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
    }

    override fun onSuccessEmployeeWishesApiCAll(employeeWishesResponse: EmployeeWishesResponse) {
        Utlis.hideLoading()
        if (employeeWishesResponse.insertEMPResult != null) {
            if (employeeWishesResponse.insertEMPResult!!.Status == true) {
                activityGreetingBinding.previewSendLayout.visibility = View.GONE
                activityGreetingBinding.successLayout.text =
                    employeeWishesResponse.insertEMPResult!!.Message
                activityGreetingBinding.successLayout.visibility = View.VISIBLE
                successHandler.removeCallbacks(successRunnable)
                successHandler.postDelayed(successRunnable, 3000)

            }
            Toast.makeText(
                this@GreetingActivity,
                employeeWishesResponse.insertEMPResult!!.Message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    var successHandler = Handler()
    var successRunnable = Runnable {
        activityGreetingBinding.previewSendLayout.visibility = View.VISIBLE
        activityGreetingBinding.successLayout.visibility = View.GONE
    }

    override fun onPause() {
        successHandler.removeCallbacks(successRunnable)
        super.onPause()
    }

    fun showImagePreview(isUplodedImage: Boolean) {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val dialogWishesImagePreviewBinding =
            DataBindingUtil.inflate<DialogWishesImagePreviewBinding>(
                LayoutInflater.from(this@GreetingActivity),
                R.layout.dialog_wishes_image_preview,
                null,
                false
            )
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(dialogWishesImagePreviewBinding.root)

        if (isUplodedImage) {
            dialogWishesImagePreviewBinding.photo.text = "Your Photo"
            Glide.with(this).load(employeeImagePath)
                .into(dialogWishesImagePreviewBinding.photoPreview)
        } else {
            dialogWishesImagePreviewBinding.photo.text = "Your Signature"
            Glide.with(this).load(employeeSignaturePath)
                .into(dialogWishesImagePreviewBinding.photoPreview)
        }

        dialogWishesImagePreviewBinding.dialogDismissIcon.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}