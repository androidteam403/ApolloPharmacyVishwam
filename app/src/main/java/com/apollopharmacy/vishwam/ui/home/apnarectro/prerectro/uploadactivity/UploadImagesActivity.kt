package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityUploadImagesBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen.PostRectroReviewScreen
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreRectroReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter.ConfigApnaAdapter
import me.echodev.resizer.Resizer
import java.io.File

class UploadImagesActivity : AppCompatActivity(), UploadImagesCallback{
    lateinit var activityUploadImagesBinding: ActivityUploadImagesBinding
    private var configApnaAdapter: ConfigApnaAdapter? = null
    private lateinit var dialog: Dialog
    private var fragmentName:String =""
    private var fileNameForCompressedImage: String? = null
    private lateinit var cameraDialog: Dialog
    var imageFromCameraFile: File? = null
    var pos:Int=0
    var configLst = ArrayList<ImgeDtcl>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadImagesBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_upload_images
        )

        setUp()


    }

    private fun setUp() {
        activityUploadImagesBinding.callback=this
        fragmentName = intent.getStringExtra("fragmentName")!!
        Toast.makeText(applicationContext,""+fragmentName, Toast.LENGTH_SHORT).show()

        configLst!!.add(ImgeDtcl(null, "Signage"))
        configLst!!.add(ImgeDtcl(null, "Front glass facade left and right"))
        configLst!!.add(ImgeDtcl(null, "Merchadising of rack FMCG rack left and right"))
        configLst!!.add(ImgeDtcl(null, "Service desk covering system"))
        configLst!!.add(ImgeDtcl(null, "Pharma rack left and right"))




        configApnaAdapter =
            ConfigApnaAdapter(configLst, this, this)
        val layoutManager = LinearLayoutManager(ViswamApp.context)
        activityUploadImagesBinding.categoryNameApnaRecyclerView.layoutManager = layoutManager
        activityUploadImagesBinding.categoryNameApnaRecyclerView.itemAnimator =
            DefaultItemAnimator()
        activityUploadImagesBinding.categoryNameApnaRecyclerView.adapter = configApnaAdapter
    }


    class ImgeDtcl(var file: File?,  var categoryName: String)

    override fun onClickUpload() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_onsuccessupload_apna)
        val close = dialog.findViewById<LinearLayout>(R.id.close_apna)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<RelativeLayout>(R.id.ok_apna)
        ok.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onClickEyeImage() {
        if(fragmentName.equals("fromPreRectro")){
            val intent = Intent(applicationContext, PreRectroReviewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }else{
            val intent = Intent(applicationContext, PostRectroReviewScreen::class.java)
            intent.putExtra("fragmentName", fragmentName)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

    }

    override fun onClickDeleteImage(deletPos: Int) {
        cameraDialog = Dialog(this)
        cameraDialog.setContentView(R.layout.dialog_camera_delete)
        val close = cameraDialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            cameraDialog.dismiss()
        }
        val ok = cameraDialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            configLst.get(deletPos).file=null
            configApnaAdapter!!.notifyDataSetChanged()
            cameraDialog.dismiss()
        }

        cameraDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cameraDialog.show()

    }
    override fun onClickPlusIcon(position: Int) {
        pos=position
        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()

        }
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile =
            File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")
        fileNameForCompressedImage = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
//            var capture: File? = null
            imageFromCameraFile?.length()
            val resizedImage = Resizer(this)
                .setTargetLength(1080)
                .setQuality(100)
                .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                .setOutputDirPath(
                    ViswamApp.Companion.context.cacheDir.toString()
                )

                .setSourceImage(imageFromCameraFile)
                .resizedFile

//            val fileSizeInBytesC: Long = imageFromCameraFile!!.length()

//          val fileSizeInKBC = fileSizeInBytesC / 1024
//
//           val fileSizeInMBC = fileSizeInKBC / 1024
//            val path: String =
//                Environment.getExternalStorageDirectory().toString() + "/CameraImages/exampleswach.jpg"
//            val file = File(path)
//            val outputFileUri = Uri.fromFile(file)
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
//
//            startActivityForResult(intent, imageFromCameraFile)

//            createImageFile()

//          saveToInternalStorage(imageFromCameraFile)


//        val compressedImageFile =  Compressor(this).compressToFile(imageFromCameraFile);
//
//           val  compressedImageBitmap =  Compressor(this).compressToBitmap(imageFromCameraFile);

//          val compressedImage =  Compressor(this)
//                .setMaxWidth(640)
//                .setMaxHeight(480)
//                .setQuality(100)
//                .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                .compressToFile(imageFromCameraFile);


//            val resizedImage = Resizer(this)
//                .setTargetLength(1080)
//                .setQuality(100)
//                .setOutputFormat("JPG")
////                .setOutputFilename(fileNameForCompressedImage)
//                .setOutputDirPath(
//                    ViswamApp.Companion.context.cacheDir.toString()
//                )
//
//                .setSourceImage(imageFromCameraFile)
//                .resizedFile
            // Environment.getExternalStoragePublicDirectory(
            //                        Environment.DIRECTORY_PICTURES
            //                    ).getAbsolutePath()
// Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_PICTURES
//            val fileSizeInBytes: Long = resizedImage.length()
//

//            val fileSizeInKB = fileSizeInBytes / 1024
//
//            val fileSizeInMB = fileSizeInKB / 1024

            if (resizedImage != null) {
              configLst.get(pos).file  = resizedImage// resizedImage
            }

//


            configApnaAdapter?.notifyDataSetChanged()

//            Utlis.showLoading(this)
//            viewModel.connectToAzure(
//                swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
//                    uploadPosition
//                )
//            )


        }


    }

}