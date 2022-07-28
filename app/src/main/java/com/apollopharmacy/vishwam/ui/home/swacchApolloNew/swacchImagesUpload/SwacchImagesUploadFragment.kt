package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.cms.SubmitNewV2Response
import com.apollopharmacy.vishwam.databinding.FragmentSwacchImagesUploadBinding

import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.adapter.ConfigListAdapter
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.adapter.UploadButtonAdapter
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.OnSubmitSwachModelRequest
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.SwachModelResponse.Config.ImgeDtcl
import com.apollopharmacy.vishwam.ui.home.swachhapollo.SwachhapolloModel
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import java.io.File
import kotlin.collections.ArrayList

class SwacchImagesUploadFragment :
    BaseFragment<SwachhapolloViewModel, FragmentSwacchImagesUploadBinding>(),
    UploadButtonAdapter.CallbackInterface {
    var swachhapolloModel: SwachhapolloModel? = null
    lateinit var swachModelResponse: SwachModelResponse

    private lateinit var capturedImagesList: MutableList<ImageDataDto>
    var imageFromCameraFile: File? = null
    private lateinit var configListAdapter: ConfigListAdapter
    private var swacchApolloList = ArrayList<SwachModelResponse>()
    private var imagesArrayListSend = ArrayList<SubmitNewV2Response.PrescriptionImagesItem>()

//    private var frontview_fileArrayList = ArrayList<ImageDataDto>()

    private var NewimagesArrayListSend = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
    private var uploadimagevalidationcount = ArrayList<ArrayList<ImageDataDto>>()


    override val layoutRes: Int
        get() = R.layout.fragment_swacch_images_upload


    override fun retrieveViewModel(): SwachhapolloViewModel {
        return ViewModelProvider(this).get(SwachhapolloViewModel::class.java)

    }

    override fun setup() {
        viewModel.swachImagesRegister()
        viewBinding.storeId.text = Preferences.getSiteId()

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        viewModel.swachhapolloModel.observeForever {
            if (it != null && it.status ?: null == true) {
                swacchApolloList.add(it)

                for ((index, value) in it.configlist!!.withIndex()) {
                    val countUpload = value.categoryImageUploadCount?.toInt()
                    var dtcl_list = ArrayList<ImgeDtcl>()
                    for (count in 1..countUpload!!) {

                        dtcl_list.add(ImgeDtcl(null, count, "", 0))

                    }
                    swacchApolloList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

                }

                configListAdapter =
                    ConfigListAdapter(it, this, swacchApolloList)
                val layoutManager = LinearLayoutManager(context)
                viewBinding.recyclerViewimageswach.layoutManager = layoutManager
                viewBinding.recyclerViewimageswach.itemAnimator = DefaultItemAnimator()
                viewBinding.recyclerViewimageswach.adapter = configListAdapter
                hideLoading()

            }
        }




        viewBinding.uploadButtonTop.setOnClickListener {

            Toast.makeText(context, "Please upload all Images", Toast.LENGTH_SHORT).show()
        }


        viewModel.commands.observe(viewLifecycleOwner, observer = {
            configListAdapter.notifyDataSetChanged()
            hideLoading()
            when (it) {

                is CommandsNew.ImageIsUploadedInAzur -> {
                    for (i in swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto!!.indices) {
                        if (swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto?.get(
                                i
                            )?.positionLoop?.equals(it.filePath.positionLoop)!!
                        ) {
                            val imageDtcl = ImgeDtcl(
                                it.filePath.file,
                                it.filePath.integerButtonCount,
                                it.filePath.base64Images,
                                it.filePath.positionLoop
                            )
                            swacchApolloList.get(0).configlist!!.get(configPosition).imageDataDto?.set(
                                i,
                                imageDtcl
                            )

                            break
                        }

                    }


                }


            }


        })



        viewBinding.uploadButtonTopYellow.setOnClickListener {
            if (NetworkUtil.isNetworkConnected(requireContext())) {
                showLoading()
                var submitList = ArrayList<OnSubmitSwachModelRequest>()
                for (i in swacchApolloList.get(0).configlist!!.indices) {
                    var submit = OnSubmitSwachModelRequest()
                    submit.actionEvent = "SUBMIT"
                    submit.storeId = Preferences.getSiteId()
                    submit.useridId = Preferences.getToken()
                    submit.categoryId = swacchApolloList.get(0).configlist!!.get(i).categoryId

                    var imageUrlsList = ArrayList<OnSubmitSwachModelRequest.ImageUrl>()
                    for (j in swacchApolloList.get(0).configlist!!.get(i).imageDataDto!!.indices) {
                        var imageUrl = submit.ImageUrl()

                        imageUrl.url =
                            swacchApolloList.get(0).configlist!!.get(i).imageDataDto?.get(j)?.base64Images
                        imageUrlsList.add(imageUrl)
                        submit.imageUrls = imageUrlsList


                    }

                    submitList.add(submit)

                }
                viewModel.onSubmitSwacch(ArrayList<OnSubmitSwachModelRequest>(submitList))
            }

        }

        viewModel.onSubmitSwachModel.observeForever {
            hideLoading()
            if (it != null && it.status == true) {
                Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }


//    private fun prepareMovieData() {
//        var movie = MovieModel("FRONT VIEW")
//        movieList.add(movie)
//        movie = MovieModel("COMPUTER AND UPS")
//        movieList.add(movie)
//        movie = MovieModel("RACKS")
//        movieList.add(movie)
//        movie = MovieModel("FRONT COUNTER")
//        movieList.add(movie)
//        movie = MovieModel("UPLOAD PHARMACY GANDOLA DISPLAY")
//        movieList.add(movie)
////        configListAdapter.notifyDataSetChanged()
//    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun askPermissions(PermissonCode: Int) {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ), PermissonCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            Config.REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        context?.resources?.getString(R.string.label_permission_denied),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile =
            File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }


    var configPosition: Int = 0
    var uploadPosition: Int = 0
    override fun passResultCallback(configPos: Int, uploadPos: Int) {
        this.configPosition = configPos
        this.uploadPosition = uploadPos
        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()
        }

    }


    override fun deleteImageCallBack(configPos: Int, deleteImagePos: Int) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_deleteimage)
        val yesBtn = dialog?.findViewById(R.id.yes_btn) as Button
        val noBtn = dialog.findViewById(R.id.no_btn) as Button
        yesBtn.setOnClickListener {
            swacchApolloList.get(0).configlist?.get(configPos)?.imageDataDto?.get(deleteImagePos)?.file =
                null
            swacchApolloList.get(0).configlist!!.get(configPos).imageUploaded = false
            configListAdapter.notifyDataSetChanged()

            checkAllImagesUploaded()
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()


    }

    override fun capturedImageReview(capturedImagepos: Int, capturedImage: File?, position: Int) {
        PhotoPopupWindow(
            context, R.layout.layout_image_fullview, view,
            capturedImage.toString(), null
        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
//            var capture: File? = null

//            frontview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))


            swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                uploadPosition
            )?.file = imageFromCameraFile
            configListAdapter.notifyDataSetChanged()
            swacchApolloList.get(0).configlist!!.get(configPosition).imageUploaded = true
            swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(
                uploadPosition
            )?.positionLoop = uploadPosition
            checkAllImagesUploaded()



        }
        showLoading()
        viewModel.connectToAzure(swacchApolloList.get(0).configlist?.get(configPosition)?.imageDataDto?.get(uploadPosition))
        configListAdapter.notifyDataSetChanged()
    }



    private fun checkAllImagesUploaded() {
        if (swacchApolloList.get(0).configlist != null) {
            for ((index, value) in swacchApolloList.get(0).configlist!!.withIndex()) {
                for ((index1, value1) in swacchApolloList.get(0).configlist?.get(index)?.imageDataDto?.withIndex()!!) {
                    if (swacchApolloList.get(0).configlist!!.get(index).imageDataDto?.get(index1)?.file != null) {
                        swacchApolloList.get(0).configlist!!.get(index).imageUploaded = true
                    } else {
                        swacchApolloList.get(0).configlist!!.get(index).imageUploaded = false
                    }
                }

            }
        }


        var allImagesUploaded: Boolean = true
        for ((index, value) in swacchApolloList.get(0).configlist!!.withIndex()) {
            if (swacchApolloList.get(0).configlist!!.get(index).imageUploaded == false) {
                allImagesUploaded = false
            }

            if (allImagesUploaded) {
                viewBinding.uploadButtonTop.visibility = View.GONE
                viewBinding.uploadButtonTopYellow.visibility = View.VISIBLE
            } else {
                viewBinding.uploadButtonTop.visibility = View.VISIBLE
                viewBinding.uploadButtonTopYellow.visibility = View.GONE
            }

        }
    }


}


