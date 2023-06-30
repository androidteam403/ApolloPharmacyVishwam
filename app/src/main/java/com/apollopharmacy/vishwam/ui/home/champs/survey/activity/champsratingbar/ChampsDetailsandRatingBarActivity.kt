package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
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
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityChampsDetailsandRatingBarBinding
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.ImageDto
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter.ImagesDisplayChampsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter.SubCategoryAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurevyDetailsByChampsIdResponse
import com.apollopharmacy.vishwam.util.Utlis
import me.echodev.resizer.Resizer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException


class ChampsDetailsandRatingBarActivity : AppCompatActivity(), ChampsDetailsandRatingBarCallBack {
    private var getCategoryName: String? = ""
    private lateinit var activityChampsDetailsandRatingBarBinding: ActivityChampsDetailsandRatingBarBinding
    private lateinit var champsDetailsAndRatingBarViewModel: ChampsDetailsAndRatingBarViewModel
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private var imagesDisplayChampsAdapter: ImagesDisplayChampsAdapter? = null
    var imageFromCameraFile: File? = null
    private var fileNameForCompressedImage: String? = null
    private var getSubCategoryDetailss: GetSubCategoryDetailsModelResponse? = null
    private lateinit var dialog: Dialog
    private var getCategoryAndSubCategoryDetails: GetCategoryDetailsModelResponse? = null
    private var champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack? = null
    private var categoryPosition: Int = 0
    private var status:String=""
    private var isPending: Boolean = false
    var thread: Thread?=null
    var isFromGallery:Boolean=false
    var imageDataList: MutableList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>? = null
    var dtcl_list = java.util.ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()
    var imageUploadedCount=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_champs_detailsand_rating_bar)

        activityChampsDetailsandRatingBarBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_detailsand_rating_bar

        )
        champsDetailsAndRatingBarViewModel =
            ViewModelProvider(this)[ChampsDetailsAndRatingBarViewModel::class.java]
//        champsDetailsandRatingBarCallBack = ChampsDetailsandRatingBarCallBack
        setUp()
    }


    private fun setUp() {
        activityChampsDetailsandRatingBarBinding.callback = this
//        activityChampsDetailsandRatingBarBinding.mbUsedText.text =
//            sumOfThreePics.toString() + " MB Used"
        if (intent != null) {
            getCategoryName =
                intent.getStringExtra("categoryName")
            getCategoryAndSubCategoryDetails =
                intent.getSerializableExtra("getCategoryAndSubCategoryDetails") as GetCategoryDetailsModelResponse?
            categoryPosition = intent.getIntExtra("position", 0)
            isPending = intent.getBooleanExtra("isPending", isPending)
            status= intent.getStringExtra("status")!!

        }
        if(status.equals("COMPLETED")){
            activityChampsDetailsandRatingBarBinding.cameraText.visibility=View.GONE
            activityChampsDetailsandRatingBarBinding.continueLayout.visibility=View.GONE
//            calucateSumOfSubCategory() 
        }else{
            activityChampsDetailsandRatingBarBinding.cameraText.visibility=View.VISIBLE
            activityChampsDetailsandRatingBarBinding.continueLayout.visibility=View.VISIBLE
        }
        activityChampsDetailsandRatingBarBinding.storeId.setText(getCategoryAndSubCategoryDetails?.storeIdP)
        activityChampsDetailsandRatingBarBinding.address.setText(getCategoryAndSubCategoryDetails?.addressP)
//        Toast.makeText(applicationContext, "" + getCategoryName, Toast.LENGTH_SHORT).show()
        activityChampsDetailsandRatingBarBinding.categoryName.setText(getCategoryName)
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
            activityChampsDetailsandRatingBarBinding.overallSum.text =
                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).rating
        }
         if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(
                categoryPosition
            ).sumOfSubCategoryRating != null
        ) {
            activityChampsDetailsandRatingBarBinding.sumOfrating.text =
                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).sumOfSubCategoryRating.toString()
        }
        activityChampsDetailsandRatingBarBinding.categoryPos.text =
            (categoryPosition + 1).toString()
//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            champsDetailsAndRatingBarViewModel.getSubCategoryDetailsChampsApi(this)
//
//        } else {
//            Toast.makeText(
//                ViswamApp.context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }
//        if (isPending) {
//            if (NetworkUtil.isNetworkConnected(this)) {
//                Utlis.showLoading(this)
//                champsDetailsAndRatingBarViewModel.getSurveyListByChampsID(this)
//
//            } else {
//                Toast.makeText(
//                    ViswamApp.context,
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//
//        }

        LoadRecyclerView()
        if((getCategoryAndSubCategoryDetails!=null && getCategoryAndSubCategoryDetails!!.categoryDetails!=null &&
                getCategoryAndSubCategoryDetails!!.categoryDetails?.get(categoryPosition)?.imageDataLists==null && !status.equals("COMPLETED") )||
                getCategoryAndSubCategoryDetails!!.categoryDetails?.get(categoryPosition)?.imageDataLists!!.size==0 && !status.equals("COMPLETED")){
            var image1=GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
            image1.imageFilled=false
            image1.file=null
            image1.imageUrl=""
            dtcl_list.add(image1)
            var image2=GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
            image2.imageFilled=false
            image2.file=null
            image2.imageUrl=""
            dtcl_list.add(image2)
            var image3=GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
            image3.imageFilled=false
            image3.file=null
            image3.imageUrl=""
            dtcl_list.add(image3)

            imageDataList = dtcl_list

            getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists = imageDataList
            imagesDisplayChampsAdapter =
                ImagesDisplayChampsAdapter(
                    this,
                    this,
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists,
                    status
                )
            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.layoutManager =
                LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL,
                    false
                )
            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.setAdapter(
                imagesDisplayChampsAdapter
            )
        }



        retrievingImages()
        disableUploadOption()


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


    override fun onClickSubmit() {
        getCategoryAndSubCategoryDetails?.categoryDetails?.get(categoryPosition)?.clickedSubmit = true

        for(i in getCategoryAndSubCategoryDetails?.categoryDetails?.get(categoryPosition)?.subCategoryDetails?.indices!!){
            if(  getCategoryAndSubCategoryDetails?.categoryDetails?.get(categoryPosition)?.subCategoryDetails!!.get(i).givenRating==null){
                getCategoryAndSubCategoryDetails?.categoryDetails?.get(categoryPosition)?.subCategoryDetails!!.get(i).givenRating=0.0f
            }
        }
        calucateSumOfSubCategory()
        val intent = Intent()
        intent.putExtra("getCategoryAndSubCategoryDetails", getCategoryAndSubCategoryDetails)
        intent.putExtra("categoryPosition", categoryPosition)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onClickCamera() {
        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()

        }
    }
    var imageUrlDeleted= false
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun onClickDelete(deletePosition: Int) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_camera_delete)
        val close = dialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists != null && getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.size > 0) {
                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(deletePosition).file = null
                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(deletePosition).imageUrl = ""
                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(deletePosition).imageFilled = false
                imageUploadedCount--
            }
            disableUploadOption()

            imagesDisplayChampsAdapter =
                ImagesDisplayChampsAdapter(
                    this,
                    this,
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists,
                    status
                )
            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.layoutManager =
                LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL,
                    false
                )
            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.setAdapter(
                imagesDisplayChampsAdapter
            )
            disableUploadOption()
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
            Config.REQUEST_CODE_GALLERY ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 999)
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
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails?.categoryDetails != null && getCategoryAndSubCategoryDetails?.categoryDetails?.get(
                categoryPosition
            )?.subCategoryDetails != null
        ) {
            subCategoryAdapter =
                SubCategoryAdapter(
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!,
                    applicationContext,
                    this, status
                )
            activityChampsDetailsandRatingBarBinding.categoryRecyclerView.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityChampsDetailsandRatingBarBinding.categoryRecyclerView.setAdapter(
                subCategoryAdapter
            )
        }


    }

    class ImagesData(var file: File?, var imageUrl: String, var imageFilled: Boolean)


    var imageUrl: Uri? = null
    private fun retrievingImages() {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
            if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists != null && getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(
                    categoryPosition
                ).imageDataLists!!.size > 0
            ) {
//                for (i in getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageUrls!!.indices) {
//
//                    for (j in imageDataList!!.indices) {
//                        if (!imageDataList!!.get(j).imageFilled!!) {
//                            imageDataList!!.get(j).imageUrl =
//                                getCategoryAndSubCategoryDetails!!.emailDetails!!.get(
//                                    categoryPosition
//                                ).imageUrls!!.get(i)
//                            imageDataList!!.get(j).imageFilled = true
//                            break
//                        }
//                    }
//
//                }
//                gettingImages = true
                imagesDisplayChampsAdapter =
                    ImagesDisplayChampsAdapter(
                        this,
                        this,
                        getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists,
                        status
                    )
                activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.layoutManager =
                    LinearLayoutManager(
                        this, LinearLayoutManager.HORIZONTAL,
                        false
                    )
                activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.setAdapter(
                    imagesDisplayChampsAdapter
                )
            }

        }
        disableUploadOption()
    }

    private fun calucateSumOfSubCategory() {
        var sumOfRange: Float = 0f
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                categoryPosition
            )?.subCategoryDetails != null
        ) {
            for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!.indices) {
                if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                        i
                    ).givenRating != null
                ) {

                    var indiviualRange: Float =
                        (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                            i
                        ).givenRating)!!.toFloat()
                    sumOfRange = indiviualRange + sumOfRange
                    getCategoryAndSubCategoryDetails!!.categoryDetails?.get(categoryPosition)?.sumOfSubCategoryRating =
                        sumOfRange

                }
            }
            activityChampsDetailsandRatingBarBinding.sumOfrating.text =
                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).sumOfSubCategoryRating.toString()
//            subCategoryAdapter!!.notifyDataSetChanged()

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

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {

            val resizedImage =
                Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                    .setOutputDirPath(
                        ViswamApp.context.cacheDir.toString()
                    )

                    .setSourceImage(imageFromCameraFile).resizedFile

            for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.indices) {
                if (!getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled!!) {
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).file = resizedImage
//                   getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled = true
                   imageUploadedCount++
                    break
                }
            }

//            getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageDataLists=imageDataList
//            imagesDisplayChampsAdapter =
//                ImagesDisplayChampsAdapter(
//                    imageDataList,
//                    this,
//                    this,
//                    getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageDataLists
//                )
//            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.layoutManager =
//                LinearLayoutManager(
//                    this, LinearLayoutManager.HORIZONTAL,
//                    false
//                )
//            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.setAdapter(
//                imagesDisplayChampsAdapter
//            )
//
            Utlis.showLoading(this)
            champsDetailsAndRatingBarViewModel.connectToAzure(resizedImage, this)


        } else if (resultCode == RESULT_OK && requestCode == 999) {

            val images = data!!.clipData
            if (images != null) {


                if ((images!!.itemCount ==1 && imageUploadedCount.equals(2))||
                    (images!!.itemCount ==2 && imageUploadedCount.equals(1)) ||
                    (images!!.itemCount ==3 && imageUploadedCount.equals(0))||
                    (images!!.itemCount <=3 && imageUploadedCount.equals(0)) ) {
                    if(images.itemCount>1){
                        isFromGallery=true
                    }else{
                        isFromGallery=false
                    }
                    for (i in 0 until images.itemCount) {
                        imageUploadedCount++
                        var imagePath =
                            getRealPathFromURI(applicationContext, images.getItemAt(i).uri)
                        var imageFileGallery: File? = File(imagePath)
//                        val imageBase64 = encodeImage(imageFileGallery!!.absolutePath)

                        for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.indices) {
                            if (!getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled!!) {
                                val resizedImage =
                                    Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                                        .setOutputDirPath(
                                            ViswamApp.context.cacheDir.toString()
                                        )

                                        .setSourceImage(imageFileGallery).resizedFile

                                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).file = resizedImage
//                   getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled = true
                                Utlis.showLoading(this)
                                champsDetailsAndRatingBarViewModel.connectToAzure(imageFileGallery, this)

                                break
                            }
                        }

                    }

                } else {
                    Toast.makeText(applicationContext,
                        "You are allowed to upload only 3 images",
                        Toast.LENGTH_SHORT).show()
                }
            } else {
                val uri = data.data
                var imagePath = getRealPathFromURI(applicationContext, uri!!)
                var imageFileGallery: File? = File(imagePath)
//                val imageBase64 = encodeImage(imageFileGallery!!.absolutePath)
//                prescriptionImageList.add(ImageDto(imageFileGallery!!, imageBase64!!))

                for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.indices) {
                    if (!getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled!!) {
                        val resizedImage =
                            Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                                .setOutputDirPath(
                                    ViswamApp.context.cacheDir.toString()
                                )

                                .setSourceImage(imageFileGallery).resizedFile
                        getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).file = resizedImage
//                   getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled = true
                       imageUploadedCount++
                        break
                    }
                }
                Utlis.showLoading(this)
                    champsDetailsAndRatingBarViewModel.connectToAzure(imageFileGallery, this)

            }
        }
//            if (data != null) {
//                try {
//                    imageUri = data.getData()
//                    var path = getRealPathFromURI(imageUri)
////                    var file: File = imageUri!!.toFile()
//                    var file: File = File(imageUri!!.getPath()!!);
//                    var files: File = File(path!!);
////                    var fileSplit = files.toPath().toString().split(":")
//                    val resizedImage =
//                        Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
////                .setOutputFilename(fileNameForCompressedImage)
//                            .setOutputDirPath(
//                                ViswamApp.context.cacheDir.toString()
//                            )
//
//                            .setSourceImage(files).resizedFile
//
//                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.indices) {
//                        if (!getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled!!) {
//                            getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).file = resizedImage
////                           getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled = true
//                            break
//                        }
//                    }
//                    disableUploadOption()
////                    getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageDataLists=imageDataList
////                    imagesDisplayChampsAdapter =
////                        ImagesDisplayChampsAdapter(
////                            imageDataList,
////                            this,
////                            this,
////                            getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageDataLists
////                        )
////                    activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.layoutManager =
////                        LinearLayoutManager(
////                            this, LinearLayoutManager.HORIZONTAL,
////                            false
////                        )
////                    activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.setAdapter(
////                        imagesDisplayChampsAdapter
////                    )
//                    Utlis.showLoading(this)
//                    champsDetailsAndRatingBarViewModel.connectToAzure(resizedImage, this)
//
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }

        else if (resultCode === RESULT_CANCELED) {
            Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_SHORT).show()
        }

        disableUploadOption()
    }



    fun disableUploadOption() {
        var imageFiles = ArrayList<Boolean>()
        if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists != null) {
            for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.indices) {
                if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).file!=null) {
                    imageFiles.add(getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled!!)
                }     else if(getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageUrl!=null &&
                        getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageUrl!="") {
                          imageFiles.add(getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists!!.get(i).imageFilled!!)
                }
            }
        }
        if(isFromGallery){
            activityChampsDetailsandRatingBarBinding.uploadImagesProgressBar.progress =
                imageUploadedCount
            activityChampsDetailsandRatingBarBinding.outOfThreeUploadedPhotostext.setText(
                imageUploadedCount.toString() + " " + "of 3 photos"
            )
            if (imageUploadedCount != null && imageUploadedCount > 0) {
                if (imageUploadedCount == 3) {
                    activityChampsDetailsandRatingBarBinding.openGallery.visibility = View.GONE
                    activityChampsDetailsandRatingBarBinding.openGalleryAsh.visibility = View.VISIBLE
                } else {
                    activityChampsDetailsandRatingBarBinding.openGallery.visibility = View.VISIBLE
                    activityChampsDetailsandRatingBarBinding.openGalleryAsh.visibility = View.GONE
                }
            }
        }else{
            activityChampsDetailsandRatingBarBinding.uploadImagesProgressBar.progress =
                imageFiles!!.size
            activityChampsDetailsandRatingBarBinding.outOfThreeUploadedPhotostext.setText(
                imageFiles!!.size.toString() + " " + "of 3 photos"
            )
            if (imageFiles != null && imageFiles.size > 0) {
                if (imageFiles.size == 3) {
                    activityChampsDetailsandRatingBarBinding.openGallery.visibility = View.GONE
                    activityChampsDetailsandRatingBarBinding.openGalleryAsh.visibility = View.VISIBLE
                } else {
                    activityChampsDetailsandRatingBarBinding.openGallery.visibility = View.VISIBLE
                    activityChampsDetailsandRatingBarBinding.openGalleryAsh.visibility = View.GONE
                }
            }
        }


    }


//    fun getRealPathFromURI(contentUri: Uri?): String? {
//        val proj = arrayOf(MediaStore.Audio.Media.DATA)
//        val cursor = managedQuery(contentUri, proj, null, null, null)
//        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//        cursor.moveToFirst()
//        return cursor.getString(column_index)
//    }

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
                            ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                                java.lang.Long.valueOf(id))
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
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context,
                    uri,
                    null,
                    null)
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

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

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
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
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
                null)
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
            cursor = context.contentResolver.query(uri, projection, null, null,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    override fun onSuccessgetSubCategoryDetails(getSubCategoryDetails: GetSubCategoryDetailsModelResponse) {
        getSubCategoryDetailss = getSubCategoryDetails
        if (getSubCategoryDetails != null && getSubCategoryDetails.subCategoryDetails != null && getSubCategoryDetails.subCategoryDetails!!.size != null) {

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
                for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.indices) {
                    if (getCategoryName.equals(
                            getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(
                                i
                            ).categoryName
                        )
                    ) {
//                        categoryPosition = i
                        if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).subCategoryDetails == null) {
                            getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).subCategoryDetails =
                                getSubCategoryDetails.subCategoryDetails
                        }
                    }
                }
            }
            subCategoryAdapter =
                SubCategoryAdapter(
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!,
                    applicationContext,
                    this,
                    status
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
        for( i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.indices){
            if(getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).categoryName!!.equals(getCategoryName)){
                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).subCategoryDetails!!.get(
                    subCategoryPosition
                ).givenRating =
                    progress
            }
        }

        calucateSumOfSubCategory()

    }

    var submit = ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()
    override fun onSuccessImageIsUploadedInAzur(response: String?) {


        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null
            && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(categoryPosition)?.imageDataLists != null
        ) {
            for( i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists?.indices!!){
                if(!getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists?.get(i)?.imageFilled!!){
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists?.get(i)?.imageUrl=response
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists?.get(i)?.imageFilled=true
                    break
                }
            }

        }

        runOnUiThread(Runnable {
            // Stuff that updates the UI
            imagesDisplayChampsAdapter =
                ImagesDisplayChampsAdapter(
                    applicationContext,
                   this,
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).imageDataLists, status
                )
            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.layoutManager =
                LinearLayoutManager(
                    applicationContext, LinearLayoutManager.HORIZONTAL,
                    false
                )
            activityChampsDetailsandRatingBarBinding.imagesDisplayRecyclerview.setAdapter(
                imagesDisplayChampsAdapter
            )

        })

        Utlis.hideLoading()
    }


    override fun onClickOpenGallery() {
        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_GALLERY)
            return
        }else{
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 999)
        }


//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(gallery, 999)
    }

    override fun onSuccessGetSurveyDetailsByChampsId(getSurveyDetailsByChapmpsId: GetSurevyDetailsByChampsIdResponse) {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                0
            )?.subCategoryDetails != null
        ) {
            getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(0).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore).toFloat()
            getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(1).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.offerDisplay).toFloat()
            getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(2).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.storeFrontage).toFloat()
            getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(3).givenRating =
                (getSurveyDetailsByChapmpsId.categoryDetails.groomingStaff).toFloat()
            subCategoryAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse) {
        if (value != null && value.message != null) {
            Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickPlusIcon(position: Int) {
        onClickCamera()
    }

    override fun onClickImageDelete(position: Int) {
        onClickDelete(position)

    }


    private fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
}

