package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import android.Manifest
import android.annotation.SuppressLint
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
import me.echodev.resizer.Resizer
import java.io.File
import java.io.IOException
import java.net.URL


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
    private var categoryPosition: Int = 0
    private var isPending: Boolean = false
    var imageDataList: MutableList<ImagesData>? = null
    var gettingImages = false


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
//        activityChampsDetailsandRatingBarBinding.mbUsedText.text =
//            sumOfThreePics.toString() + " MB Used"
        if (intent != null) {
            getCategoryName =
                intent.getStringExtra("categoryName")
            getCategoryAndSubCategoryDetails =
                intent.getSerializableExtra("getCategoryAndSubCategoryDetails") as GetCategoryDetailsModelResponse?
            categoryPosition = intent.getIntExtra("position", 0)
            isPending = intent.getBooleanExtra("isPending", isPending)

        }

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

        LoadRecyclerView()
        LoadImageRecyclerView()
        retrievingImages()


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
//        if (imageDataList != null && imageDataList!!.size > 0) {
//            for (i in imageDataList!!.indices) {
//                }
//        }

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

    @SuppressLint("SetTextI18n")
    private fun onClickDelete(deletePosition: Int, gettingImages: Boolean) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_camera_delete)
        val close = dialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            if (imageDataList != null && imageDataList!!.size > 0) {
                imageDataList!!.get(deletePosition).file = null
                imageDataList!!.get(deletePosition).imageUrl=""
            }
            imagesDisplayChampsAdapter!!.notifyDataSetChanged()
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

    class ImagesData(var file: File?, var imageUrl: String)

    private fun LoadImageRecyclerView() {
        gettingImages = false
        var dtcl_list = java.util.ArrayList<ImagesData>()
        dtcl_list.add(ImagesData(null, ""))
        dtcl_list.add(ImagesData(null, ""))
        dtcl_list.add(ImagesData(null, ""))

        imageDataList = dtcl_list


        imagesDisplayChampsAdapter =
            ImagesDisplayChampsAdapter(
                imageDataList,
                this,
                this
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
    var imageUrl: Uri? = null
    private fun retrievingImages() {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null) {
            if (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageUrls != null && getCategoryAndSubCategoryDetails!!.emailDetails!!.get(
                    categoryPosition
                ).imageUrls!!.size > 0
            ) {
                for(i in getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageUrls!!.indices){

                    for( j in imageDataList!!.indices){
                        if(imageDataList!!.get(j).imageUrl.equals("")){
                            imageDataList!!.get(j).imageUrl=getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).imageUrls!!.get(i)
                            break
                        }
                    }

                }
//                gettingImages = true
                imagesDisplayChampsAdapter =
                    ImagesDisplayChampsAdapter(
                        imageDataList,
                        this,
                        this
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

            for (i in imageDataList!!.indices) {
                if (imageDataList!!.get(i).file == null) {
                    imageDataList!!.get(i).file = resizedImage
                    break
                }
            }
            gettingImages = false
            imagesDisplayChampsAdapter!!.notifyDataSetChanged()
            disableUploadOption()
            champsDetailsAndRatingBarViewModel.connectToAzure(resizedImage, this)


        } else if (resultCode == RESULT_OK && requestCode == 999) {
            if (data != null) {
                try {
                    imageUri = data.getData()
                    var path = getRealPathFromURI(imageUri)
//                    var file: File = imageUri!!.toFile()
                    var file: File = File(imageUri!!.getPath()!!);
                    var files: File = File(path!!);
//                    var fileSplit = files.toPath().toString().split(":")
                    val resizedImage =
                        Resizer(this).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                            .setOutputDirPath(
                                ViswamApp.context.cacheDir.toString()
                            )

                            .setSourceImage(files).resizedFile

                    for (i in imageDataList!!.indices) {
                        if (imageDataList!!.get(i).file == null) {
                            imageDataList!!.get(i).file = resizedImage
                            break
                        }
                    }
                    gettingImages = false
                    imagesDisplayChampsAdapter!!.notifyDataSetChanged()
                    disableUploadOption()
                    champsDetailsAndRatingBarViewModel.connectToAzure(resizedImage, this)


                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (resultCode === RESULT_CANCELED) {
            Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_SHORT).show()
        }


    }

    fun disableUploadOption() {
        var imageFiles = ArrayList<File>()
        if (imageDataList != null) {
            for (i in imageDataList!!.indices) {
                if (imageDataList!!.get(i).file != null) {
                    imageFiles.add(imageDataList!!.get(i).file!!)
                }
            }
        }
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

    override fun onClickPlusIcon(position: Int) {
        onClickCamera()
    }

    override fun onClickImageDelete(position: Int) {
        onClickDelete(position, gettingImages)

    }


    private fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
}

