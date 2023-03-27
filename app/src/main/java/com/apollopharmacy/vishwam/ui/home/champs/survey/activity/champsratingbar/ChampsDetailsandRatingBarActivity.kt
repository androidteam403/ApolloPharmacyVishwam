package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityChampsDetailsandRatingBarBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter.ImagesDisplayChampsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter.SubCategoryAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurevyDetailsByChampsIdResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import me.echodev.resizer.Resizer
import java.io.File
import java.io.IOException
import java.nio.file.Files


class ChampsDetailsandRatingBarActivity : AppCompatActivity(), ChampsDetailsandRatingBarCallBack {
    private var getCategoryName: String? = ""
    private lateinit var activityChampsDetailsandRatingBarBinding: ActivityChampsDetailsandRatingBarBinding
    private lateinit var champsDetailsAndRatingBarViewModel: ChampsDetailsAndRatingBarViewModel
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private var imagesDisplayChampsAdapter: ImagesDisplayChampsAdapter? = null
    var imageFromCameraFile: File? = null
    private var fileNameForCompressedImage: String? = null
    private var getSubCategoryDetailss: GetSubCategoryDetailsModelResponse? = null
    private var imagesList = ArrayList<File>()
    private var imageLayout = ArrayList<String>()
    private var camera1Clicked: Int = 0
    private var redTrashClicked: Int = 0
    private lateinit var dialog: Dialog
    private var decrementMb1: Long = 0
    private var decrementMb2: Long = 0
    private var decrementMb3: Long = 0
    private var getCategoryAndSubCategoryDetails: GetCategoryDetailsModelResponse? = null
    private var categoryPosition: Int = 0
    private var isPending: Boolean = false
    private var threeFilesUploaded : Boolean=false
    private var  imageFiles = ArrayList<File>()
    private var uploadedFromGallery: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_champs_detailsand_rating_bar)

        activityChampsDetailsandRatingBarBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_detailsand_rating_bar

        )
        champsDetailsAndRatingBarViewModel =
            ViewModelProvider(this)[ChampsDetailsAndRatingBarViewModel::class.java]
        setUp()
    }


    private fun setUp() {
        activityChampsDetailsandRatingBarBinding.callback = this
        activityChampsDetailsandRatingBarBinding.mbUsedText.text =
            sumOfThreePics.toString() + " MB Used"
        getCategoryName =
            intent.getStringExtra("categoryName")
        getCategoryAndSubCategoryDetails =
            intent.getSerializableExtra("getCategoryAndSubCategoryDetails") as GetCategoryDetailsModelResponse?
        categoryPosition = intent.getIntExtra("position", 0)
        isPending = intent.getBooleanExtra("isPending", isPending)
//        Toast.makeText(applicationContext, "" + getCategoryName, Toast.LENGTH_SHORT).show()
        activityChampsDetailsandRatingBarBinding.categoryName.setText(getCategoryName)
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null) {
            activityChampsDetailsandRatingBarBinding.overallSum.text =
                getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).rating
        }
        activityChampsDetailsandRatingBarBinding.mbUsedText.text =
            getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).sumOfThreePicsinMb.toString() + " KB Used"
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails!!.get(
                categoryPosition
            ).sumOfSubCategoryRating != null
        ) {
            activityChampsDetailsandRatingBarBinding.sumOfrating.text =
                getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).sumOfSubCategoryRating.toString()
        }
        activityChampsDetailsandRatingBarBinding.categoryPos.text =
            (categoryPosition + 1).toString()
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            champsDetailsAndRatingBarViewModel.getSubCategoryDetailsChamps(this)

        } else {
            Toast.makeText(
                ViswamApp.context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (isPending) {
            if (NetworkUtil.isNetworkConnected(this)) {
                Utlis.showLoading(this)
                champsDetailsAndRatingBarViewModel.getSurveyListByChampsID(this)

            } else {
                Toast.makeText(
                    ViswamApp.context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }



        retrievingImages()
        LoadRecyclerView()


//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            champsDetailsAndRatingBarViewModel.getSubCategoryDetailsChampsApi(
//                this,
//                getCategoryName!!
//            )
//
//        } else {
//            Toast.makeText(
//                ViswamApp.context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }

    }

    override fun onClickBack() {
        super.onBackPressed()
    }

    override fun onClickSaveDraft() {

    }

    override fun onClickSubmit() {
        getCategoryAndSubCategoryDetails?.emailDetails?.get(categoryPosition)!!.sumOfThreePicsinMb =
            sumOfThreePics.toString()
        if(imageFiles!=null && imageFiles.size>0){
            for(i in imageFiles.indices){
                champsDetailsAndRatingBarViewModel.connectToAzure(imageFiles.get(i), this)
            }
        }

        calucateSumOfSubCategory()
        val intent = Intent()
        intent.putExtra("getCategoryAndSubCategoryDetails", getCategoryAndSubCategoryDetails)
        intent.putExtra("categoryPosition", categoryPosition)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onClickImageUpload3() {
        onClickCamera()
        camera1Clicked = 3
    }

    override fun onClickImageUpload2() {
        onClickCamera()
        camera1Clicked = 2
    }

    override fun onClickImageUpload1() {
        onClickCamera()
        camera1Clicked = 1
    }

    override fun onClickRedTrashDelete1() {
        redTrashClicked = 1
        onClickDelete()
    }

    override fun onClickRedTrashDelete2() {
        redTrashClicked = 2
        onClickDelete()
    }

    override fun onClickRedTrashDelete3() {
        redTrashClicked = 3
        onClickDelete()
    }

    private fun onClickCamera() {
        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()

        }
    }

    private fun onClickDelete() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_camera_delete)
        val close = dialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            if (redTrashClicked == 1) {
                activityChampsDetailsandRatingBarBinding.uploadImageLayout1.visibility = View.GONE
                activityChampsDetailsandRatingBarBinding.image1.visibility = View.VISIBLE
//                imageFiles.removeAt(0)
                var afterDec = sumOfThreePics - decrementMb1
                sumOfThreePics = afterDec
                activityChampsDetailsandRatingBarBinding.uploadImagesProgressBar.progress =
                    sumOfThreePics.toInt()
                activityChampsDetailsandRatingBarBinding.mbUsedText.text =
                    sumOfThreePics.toString() + " KB Used"
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        categoryPosition
                    )?.imageUrls != null
                ) {
                    getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.imageUrls!!.removeAt(
                        0
                    )
                }

            } else if (redTrashClicked == 2) {
                activityChampsDetailsandRatingBarBinding.uploadImageLayout2.visibility = View.GONE
                activityChampsDetailsandRatingBarBinding.image2.visibility = View.VISIBLE
//                imageFiles.removeAt(1)
                var afterDec = sumOfThreePics - decrementMb2
                sumOfThreePics = afterDec
                activityChampsDetailsandRatingBarBinding.uploadImagesProgressBar.progress =
                    sumOfThreePics.toInt()
                activityChampsDetailsandRatingBarBinding.mbUsedText.text =
                    sumOfThreePics.toString() + " KB Used"
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        categoryPosition
                    )?.imageUrls != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        categoryPosition
                    )?.imageUrls!!.size > 0
                ) {
                    getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.imageUrls!!.removeAt(
                        1
                    )
                }
            } else {
                activityChampsDetailsandRatingBarBinding.uploadImageLayout3.visibility = View.GONE
                activityChampsDetailsandRatingBarBinding.image3.visibility = View.VISIBLE
//                imageFiles.removeAt(2)
                var afterDec = sumOfThreePics - decrementMb3
                sumOfThreePics = afterDec
                activityChampsDetailsandRatingBarBinding.uploadImagesProgressBar.progress =
                    sumOfThreePics.toInt()
                activityChampsDetailsandRatingBarBinding.mbUsedText.text =
                    sumOfThreePics.toString() + " KB Used"
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        categoryPosition
                    )?.imageUrls != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        categoryPosition
                    )?.imageUrls!!.size > 1
                ) {
                    getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.imageUrls!!.removeAt(
                        2
                    )
                }
            }
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Config.REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(
                        ViswamApp.context,
                        ViswamApp.context?.resources?.getString(R.string.label_permission_denied),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun LoadRecyclerView() {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails?.emailDetails != null && getCategoryAndSubCategoryDetails?.emailDetails?.get(
                categoryPosition
            )?.subCategoryDetails != null
        ) {
            subCategoryAdapter =
                SubCategoryAdapter(
                    getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!,
                    applicationContext,
                    this
                )
            activityChampsDetailsandRatingBarBinding.categoryRecyclerView.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityChampsDetailsandRatingBarBinding.categoryRecyclerView.setAdapter(
                subCategoryAdapter
            )
        }


    }

    private fun retrievingImages() {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null) {
            for (i in getCategoryAndSubCategoryDetails!!.emailDetails!!.indices) {
                if (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).categoryName.equals(
                        getCategoryName)) {
                    if (getCategoryAndSubCategoryDetails?.emailDetails?.get(i)?.imageUrls != null && getCategoryAndSubCategoryDetails?.emailDetails?.get(
                            i
                        )!!.imageUrls?.size!! > 0 && getCategoryAndSubCategoryDetails?.emailDetails?.get(
                            i
                        )!!.imageUrls?.get(0) != null && !getCategoryAndSubCategoryDetails?.emailDetails?.get(
                            i
                        )!!.imageUrls?.get(0)?.isEmpty()!!
                    ) {
                        activityChampsDetailsandRatingBarBinding.image1.visibility = View.GONE
                        activityChampsDetailsandRatingBarBinding.uploadImageLayout1.visibility =
                            View.VISIBLE
                        Glide.with(applicationContext).load(
                            getCategoryAndSubCategoryDetails?.emailDetails?.get(i)?.imageUrls!!.get(
                                0
                            )
                        ).into(activityChampsDetailsandRatingBarBinding.uploadImageChamps1);
                    }

                    if (getCategoryAndSubCategoryDetails?.emailDetails?.get(i)?.imageUrls != null && getCategoryAndSubCategoryDetails!!.emailDetails!!.get(
                            i
                        ).imageUrls!!.size > 1 && getCategoryAndSubCategoryDetails?.emailDetails?.get(
                            i
                        )!!.imageUrls?.get(1) != null && !getCategoryAndSubCategoryDetails?.emailDetails?.get(
                            i
                        )!!.imageUrls?.get(1)?.isEmpty()!!
                    ) {
                        activityChampsDetailsandRatingBarBinding.image2.visibility = View.GONE
                        activityChampsDetailsandRatingBarBinding.uploadImageLayout2.visibility =
                            View.VISIBLE
                        Glide.with(applicationContext).load(
                            getCategoryAndSubCategoryDetails?.emailDetails?.get(i)?.imageUrls!!.get(
                                1
                            )
                        ).into(activityChampsDetailsandRatingBarBinding.uploadImageChamps2);
                    }

                    if (getCategoryAndSubCategoryDetails?.emailDetails?.get(i)?.imageUrls != null && getCategoryAndSubCategoryDetails!!.emailDetails!!.get(
                            i
                        ).imageUrls!!.size > 2 && getCategoryAndSubCategoryDetails?.emailDetails?.get(
                            i
                        )!!.imageUrls?.get(2) != null && !getCategoryAndSubCategoryDetails?.emailDetails?.get(
                            i
                        )!!.imageUrls?.get(2)?.isEmpty()!!
                    ) {
                        activityChampsDetailsandRatingBarBinding.image3.visibility = View.GONE
                        activityChampsDetailsandRatingBarBinding.uploadImageLayout3.visibility =
                            View.VISIBLE
                        Glide.with(applicationContext).load(
                            getCategoryAndSubCategoryDetails?.emailDetails?.get(i)?.imageUrls!!.get(
                                2
                            )
                        ).into(activityChampsDetailsandRatingBarBinding.uploadImageChamps3);
                    }


                }
            }
        }
    }

    private fun calucateSumOfSubCategory() {
        var sumOfRange: Float = 0f
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                categoryPosition
            )?.subCategoryDetails != null
        ) {
            for (i in getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!.indices) {
                if (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                        i
                    ).givenRating != null
                ) {
                    var indiviualRange: Float =
                        (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                            i
                        ).givenRating)!!.toFloat()
                    sumOfRange = indiviualRange + sumOfRange
                    getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.sumOfSubCategoryRating =
                        sumOfRange

                }
            }
            activityChampsDetailsandRatingBarBinding.sumOfrating.text =
                getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).sumOfSubCategoryRating.toString()
            subCategoryAdapter!!.notifyDataSetChanged()

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

    var imageUri: Uri? = null
    var sumOfThreePics: Long = 0
    var imageData: ArrayList<File>? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
            var capture: File? = null
//            val resizedImage =
//             Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
////                .setOutputFilename(fileNameForCompressedImage)
//                    .setOutputDirPath(
//                        ViswamApp.Companion.context.cacheDir.toString()
//                    )
//
//                    .setSourceImage(imageFromCameraFile).resizedFile
            val resizedImage =
                Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                    .setOutputDirPath(
                        ViswamApp.context.cacheDir.toString()
                    )

                    .setSourceImage(imageFromCameraFile).resizedFile


            val fileSizeInBytesC: Long = resizedImage!!.length()

            val fileSizeInKBC = fileSizeInBytesC / 1024

            val fileSizeInMBC = fileSizeInKBC / 1024


            var mbOfPic: Long = sumOfThreePics + fileSizeInKBC
            sumOfThreePics = mbOfPic
            activityChampsDetailsandRatingBarBinding.uploadImagesProgressBar.progress =
                sumOfThreePics.toInt()
            activityChampsDetailsandRatingBarBinding.mbUsedText.text =
                sumOfThreePics.toString() + " KB Used"
            if (camera1Clicked == 1) {
                uploadedFromGallery=1
                activityChampsDetailsandRatingBarBinding.image1.visibility = View.GONE
                activityChampsDetailsandRatingBarBinding.uploadImageLayout1.visibility =
                    View.VISIBLE
                Glide.with(ViswamApp.context).load(resizedImage)
                    .placeholder(R.drawable.placeholder_image)
                    .into(activityChampsDetailsandRatingBarBinding.uploadImageChamps1)
                decrementMb1 = fileSizeInMBC

            } else if (camera1Clicked == 2) {
                uploadedFromGallery=2
                activityChampsDetailsandRatingBarBinding.image2.visibility = View.GONE
                activityChampsDetailsandRatingBarBinding.uploadImageLayout2.visibility =
                    View.VISIBLE
                Glide.with(ViswamApp.context).load(resizedImage)
                    .placeholder(R.drawable.placeholder_image)
                    .into(activityChampsDetailsandRatingBarBinding.uploadImageChamps2)
                decrementMb2 = fileSizeInMBC
            } else {
                uploadedFromGallery=3
                activityChampsDetailsandRatingBarBinding.image3.visibility = View.GONE
                activityChampsDetailsandRatingBarBinding.uploadImageLayout3.visibility =
                    View.VISIBLE
                Glide.with(ViswamApp.context).load(resizedImage)
                    .placeholder(R.drawable.placeholder_image)
                    .into(activityChampsDetailsandRatingBarBinding.uploadImageChamps3)
                decrementMb3 = fileSizeInMBC
            }

            Utlis.showLoading(this)
            champsDetailsAndRatingBarViewModel.connectToAzure(resizedImage, this)

        }
        else if (resultCode == RESULT_OK && requestCode == 999) {
            if (data != null) {
                try {
                    imageUri = data.getData()


                    var path = getRealPathFromURI(imageUri)

//                    var file: File = imageUri!!.toFile()
                    var file: File = File(imageUri!!.getPath());

                    var files: File = File(path);


//                    var fileSplit = files.toPath().toString().split(":")
                    val resizedImage =
                        Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                            .setOutputDirPath(
                                ViswamApp.context.cacheDir.toString()
                            )

                            .setSourceImage(files).resizedFile


                    val fileSizeInBytesC: Long = resizedImage.length()

                    val fileSizeInKBC = fileSizeInBytesC / 1024

                    val fileSizeInMBC = fileSizeInKBC / 1024


                    var mbOfPic: Long = sumOfThreePics + fileSizeInKBC
                    sumOfThreePics = mbOfPic
                    activityChampsDetailsandRatingBarBinding.uploadImagesProgressBar.progress =
                        sumOfThreePics.toInt()
                    activityChampsDetailsandRatingBarBinding.mbUsedText.text =
                        sumOfThreePics.toString() + " KB Used"
                    if (uploadedFromGallery!=1 && uploadedFromGallery==2 && imageFiles.size<3) {
                        uploadedFromGallery=3
                        activityChampsDetailsandRatingBarBinding.image3.visibility = View.GONE
                        activityChampsDetailsandRatingBarBinding.uploadImageLayout3.visibility =
                            View.VISIBLE
                        Glide.with(ViswamApp.context).load(resizedImage)
                            .placeholder(R.drawable.placeholder_image)
                            .into(activityChampsDetailsandRatingBarBinding.uploadImageChamps3)
//                        activityChampsDetailsandRatingBarBinding.uploadImageChamps3.setImageURI(imageUri)
                        decrementMb3 = fileSizeInMBC
                        imageFiles.add(resizedImage)
                        disableUploadOption()

                    } else if (uploadedFromGallery == 1 && uploadedFromGallery!=3 && imageFiles.size<3) {
                        uploadedFromGallery=2
                        activityChampsDetailsandRatingBarBinding.image2.visibility = View.GONE
                        activityChampsDetailsandRatingBarBinding.uploadImageLayout2.visibility =
                            View.VISIBLE
                        Glide.with(ViswamApp.context).load(resizedImage)
                            .placeholder(R.drawable.placeholder_image)
                            .into(activityChampsDetailsandRatingBarBinding.uploadImageChamps2)
//                        activityChampsDetailsandRatingBarBinding.uploadImageChamps2.setImageURI(imageUri)
                        decrementMb2 = fileSizeInMBC
                        imageFiles.add(resizedImage)
                        disableUploadOption()
                    } else {
                        if(imageFiles.size<3){
                            uploadedFromGallery=1
//                        activityChampsDetailsandRatingBarBinding.uploadImageChamps1.setImageURI(file)
                            activityChampsDetailsandRatingBarBinding.image1.visibility = View.GONE
                            activityChampsDetailsandRatingBarBinding.uploadImageLayout1.visibility =
                                View.VISIBLE
                            Glide.with(ViswamApp.context).load(resizedImage)
//                            .placeholder(R.drawable.placeholder_image)
                                .into(activityChampsDetailsandRatingBarBinding.uploadImageChamps1)
//                        activityChampsDetailsandRatingBarBinding.uploadImageChamps2.setImageURI(imageUri)
                            decrementMb1 = fileSizeInMBC
                            imageFiles.add(resizedImage)
                            disableUploadOption()
                        }

                    }

//                    Utlis.showLoading(this)

//                        var bitmap =
//                            MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), data.getData())

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        else if (resultCode === RESULT_CANCELED) {
            Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_SHORT).show()
        }


    }

    fun disableUploadOption(){
        if(imageFiles!=null && imageFiles.size>0){
            if(imageFiles.size==3){
                activityChampsDetailsandRatingBarBinding.openGallery.visibility=View.GONE
                activityChampsDetailsandRatingBarBinding.openGalleryAsh.visibility=View.VISIBLE
            }else{
                activityChampsDetailsandRatingBarBinding.openGallery.visibility=View.VISIBLE
                activityChampsDetailsandRatingBarBinding.openGalleryAsh.visibility=View.GONE
            }
        }
    }
    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onSuccessgetSubCategoryDetails(getSubCategoryDetails: GetSubCategoryDetailsModelResponse) {
        getSubCategoryDetailss = getSubCategoryDetails
        if (getSubCategoryDetails != null && getSubCategoryDetails.subCategoryDetails != null && getSubCategoryDetails.subCategoryDetails!!.size != null) {

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null) {
                for (i in getCategoryAndSubCategoryDetails!!.emailDetails!!.indices) {
                    if (getCategoryName.equals(
                            getCategoryAndSubCategoryDetails!!.emailDetails!!.get(
                                i
                            ).categoryName
                        )
                    ) {
//                        categoryPosition = i
                        if (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).subCategoryDetails == null) {
                            getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).subCategoryDetails =
                                getSubCategoryDetails.subCategoryDetails
                        }
                    }
                }
            }
            subCategoryAdapter =
                SubCategoryAdapter(
                    getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!,
                    applicationContext,
                    this
                )
            activityChampsDetailsandRatingBarBinding.categoryRecyclerView.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityChampsDetailsandRatingBarBinding.categoryRecyclerView.setAdapter(
                subCategoryAdapter
            )


        }
        Utlis.hideLoading()
    }

    override fun onFailuregetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse) {
        if (value != null && value.message != null) {
            Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        }

        Utlis.hideLoading()
    }

    override fun onClickSeekBar(
        progress: Float,
        subCategoryPosition: Int,
    ) {
        getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!.get(
            subCategoryPosition
        ).givenRating =
            progress
        calucateSumOfSubCategory()

    }

    var submit = ArrayList<String>()
    override fun onSuccessImageIsUploadedInAzur(response: String?) {

        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null
            && getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.imageUrls != null
        ) {
            getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.imageUrls!!.add(
                response!!
            )
        } else {
            submit.add(response!!)
            getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.imageUrls =
                submit
        }


        Utlis.hideLoading()
    }

    override fun onClickOpenGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 999)
    }

    override fun onSuccessGetSurveyDetailsByChampsId(getSurveyDetailsByChapmpsId: GetSurevyDetailsByChampsIdResponse) {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                0
            )?.subCategoryDetails != null
        ) {
            getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(0).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore).toFloat()
            getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(1).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.offerDisplay).toFloat()
            getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(2).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.storeFrontage).toFloat()
            getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(3).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.groomingStaff).toFloat()
            subCategoryAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse) {
        if (value != null && value.message != null) {
            Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
}