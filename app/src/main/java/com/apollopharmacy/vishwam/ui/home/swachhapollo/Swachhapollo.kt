package com.apollopharmacy.vishwam.ui.home.swachhapollo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.databinding.FragmentSwachhapolloBinding
import com.apollopharmacy.vishwam.databinding.ViewImageItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import java.io.File

class Swachhapollo() : BaseFragment<SwachhapolloModel, FragmentSwachhapolloBinding>(),
    ImageClickListner {

    private var frontview_fileArrayList = ArrayList<ImageDataDto>()
    private var desktopview_fileArrayList = ArrayList<ImageDataDto>()
    private var racksimagesview_fileArrayList = ArrayList<ImageDataDto>()
    private var frontcounterview_fileArrayList = ArrayList<ImageDataDto>()
    private var gandolaimagesview_fileArrayList = ArrayList<ImageDataDto>()
    private var otherimagesview_fileArrayList = ArrayList<ImageDataDto>()

    private var uploadimagevalidationcount= ArrayList<ArrayList<ImageDataDto>>()


    lateinit var adapter: ImageRecyclerView
    lateinit var desktopview_adapter: ImageRecyclerView
    lateinit var racksimagesview_adapter: ImageRecyclerView
    lateinit var frontcounterview_adapter: ImageRecyclerView
    lateinit var gandolaimagesview_adapter: ImageRecyclerView
    lateinit var otherimagesview_adapter: ImageRecyclerView


    var imageFromCameraFile: File? = null
    var categorycount: Int = 0

    override val layoutRes: Int
        get() = R.layout.fragment_swachhapollo

    override fun retrieveViewModel(): SwachhapolloModel {
        return ViewModelProvider(this).get(SwachhapolloModel::class.java)
    }

    override fun setup() {
        adapter = ImageRecyclerView(frontview_fileArrayList,
            this)
        desktopview_adapter = ImageRecyclerView(desktopview_fileArrayList,
            this)
        racksimagesview_adapter=ImageRecyclerView(racksimagesview_fileArrayList,
            this)
        frontcounterview_adapter=ImageRecyclerView(frontcounterview_fileArrayList,
            this)
        gandolaimagesview_adapter=ImageRecyclerView(gandolaimagesview_fileArrayList,
            this)
        otherimagesview_adapter=ImageRecyclerView(otherimagesview_fileArrayList,
            this)


        uploadimagevalidationcount.add(frontview_fileArrayList)
        uploadimagevalidationcount.add(desktopview_fileArrayList)
        uploadimagevalidationcount.add(racksimagesview_fileArrayList)
        uploadimagevalidationcount.add(frontcounterview_fileArrayList)
        uploadimagevalidationcount.add(gandolaimagesview_fileArrayList)
        uploadimagevalidationcount.add(otherimagesview_fileArrayList)

       //front view Layout..............
        viewBinding.imageRecyclerView.adapter = adapter
        viewBinding.addImage.setOnClickListener {
            if (frontview_fileArrayList.size == 2) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!checkPermission()) {
                    askPermissions(Config.REQUEST_CODE_CAMERA)
                    return@setOnClickListener
                } else
                    openCamera()
            }
        }

        //Desktop  Layout...........
        viewBinding.desktopimageRecyclerView.adapter = desktopview_adapter
        viewBinding.desktopaddImage.setOnClickListener {
            if (desktopview_fileArrayList.size == 2) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!checkPermission()) {
                    askPermissions(Config.REQUEST_CODE_CAMERA)
                    return@setOnClickListener
                } else
                    openCamera()
            }
        }

       //Racks Image Layout..........
        viewBinding.racksImageRecyclerView.adapter = racksimagesview_adapter
        viewBinding.racksAddImage.setOnClickListener {
            if (racksimagesview_fileArrayList.size == 2) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!checkPermission()) {
                    askPermissions(Config.REQUEST_CODE_CAMERA)
                    return@setOnClickListener
                } else
                    openCamera()
            }
        }


        //frontcounter Layout......
        viewBinding.frontcounterImageRecyclerView.adapter = frontcounterview_adapter
        viewBinding.frontcounterAddImage.setOnClickListener {
            if (frontcounterview_fileArrayList.size == 2) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!checkPermission()) {
                    askPermissions(Config.REQUEST_CODE_CAMERA)
                    return@setOnClickListener
                } else
                    openCamera()
            }
        }

        //Gandola Image Layout.......
        viewBinding.gandolaImageRecyclerView.adapter = gandolaimagesview_adapter
        viewBinding.gandolaAddImage.setOnClickListener {
            if (gandolaimagesview_fileArrayList.size == 2) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!checkPermission()) {
                    askPermissions(Config.REQUEST_CODE_CAMERA)
                    return@setOnClickListener
                } else
                    openCamera()
            }
        }

        //Other Image Layout.................
        viewBinding.othersImageRecyclerView.adapter = otherimagesview_adapter
        viewBinding.othersAddImage.setOnClickListener {
            if (otherimagesview_fileArrayList.size == 2) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!checkPermission()) {
                    askPermissions(Config.REQUEST_CODE_CAMERA)
                    return@setOnClickListener
                } else
                    openCamera()
            }
        }

        viewBinding.leftArrow.setOnClickListener {
            if(categorycount > 0)
            {
                categorycount--;
                layoutvisiblility(categorycount)
            }

        }

        viewBinding.rightArrow.setOnClickListener {
            var temparray=ArrayList<ImageDataDto>();
            temparray=uploadimagevalidationcount.get(categorycount)
            if(temparray.size > 1 )
            {
                categorycount++
                layoutvisiblility(categorycount)
            }
            else{
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.validate_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
             // viewBinding.desktopLayoutId2.visibility = View.VISIBLE
            //viewBinding.frontviewLayoutId1.visibility = View.GONE }
    }

    private fun layoutvisiblility(categorycount:Int)
    {
        when (categorycount) {
            0 -> {
                viewBinding.frontviewLayoutId1.visibility = View.VISIBLE
                viewBinding.desktopLayoutId2.visibility = View.GONE
                viewBinding.rackesimagesLayoutId3.visibility = View.GONE
                viewBinding.frontcounterLayoutId4.visibility = View.GONE
                viewBinding.gandolaimagesLayoutId5.visibility = View.GONE
                viewBinding.othersLayoutId6.visibility = View.GONE
            }
            1 -> {
                viewBinding.frontviewLayoutId1.visibility = View.GONE
                viewBinding.desktopLayoutId2.visibility = View.VISIBLE
                viewBinding.rackesimagesLayoutId3.visibility = View.GONE
                viewBinding.frontcounterLayoutId4.visibility = View.GONE
                viewBinding.gandolaimagesLayoutId5.visibility = View.GONE
                viewBinding.othersLayoutId6.visibility = View.GONE
            }
            2 -> {
                viewBinding.frontviewLayoutId1.visibility = View.GONE
                viewBinding.desktopLayoutId2.visibility = View.GONE
                viewBinding.rackesimagesLayoutId3.visibility = View.VISIBLE
                viewBinding.frontcounterLayoutId4.visibility = View.GONE
                viewBinding.gandolaimagesLayoutId5.visibility = View.GONE
                viewBinding.othersLayoutId6.visibility = View.GONE
            }
            3 -> {
                viewBinding.frontviewLayoutId1.visibility = View.GONE
                viewBinding.desktopLayoutId2.visibility = View.GONE
                viewBinding.rackesimagesLayoutId3.visibility = View.GONE
                viewBinding.frontcounterLayoutId4.visibility = View.VISIBLE
                viewBinding.gandolaimagesLayoutId5.visibility = View.GONE
                viewBinding.othersLayoutId6.visibility = View.GONE
            }
            4 -> {
                viewBinding.frontviewLayoutId1.visibility = View.GONE
                viewBinding.desktopLayoutId2.visibility = View.GONE
                viewBinding.rackesimagesLayoutId3.visibility = View.GONE
                viewBinding.frontcounterLayoutId4.visibility = View.GONE
                viewBinding.gandolaimagesLayoutId5.visibility = View.VISIBLE
                viewBinding.othersLayoutId6.visibility = View.GONE
            }
            5 -> {
                viewBinding.frontviewLayoutId1.visibility = View.GONE
                viewBinding.desktopLayoutId2.visibility = View.GONE
                viewBinding.rackesimagesLayoutId3.visibility = View.GONE
                viewBinding.frontcounterLayoutId4.visibility = View.GONE
                viewBinding.gandolaimagesLayoutId5.visibility = View.GONE
                viewBinding.othersLayoutId6.visibility = View.VISIBLE
            }

        }

    }


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
            if(categorycount == 0)
            {
                frontview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
                adapter.notifyAdapter(frontview_fileArrayList)
            }
            else if(categorycount == 1)
            {
                desktopview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
                desktopview_adapter.notifyAdapter(desktopview_fileArrayList)
            }
            else if(categorycount == 2)
            {
                racksimagesview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
                racksimagesview_adapter.notifyAdapter(racksimagesview_fileArrayList)
            }
            else if(categorycount == 3)
            {
                frontcounterview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
                frontcounterview_adapter.notifyAdapter(frontcounterview_fileArrayList)
            }
            else if(categorycount == 4)
            {
                gandolaimagesview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
                gandolaimagesview_adapter.notifyAdapter(gandolaimagesview_fileArrayList)
            }
            else
            {
                otherimagesview_fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
                otherimagesview_adapter.notifyAdapter(otherimagesview_fileArrayList)
            }

        }
    }


    override fun deleteImage(position: Int) {
        if(categorycount == 0) {
            adapter.deleteImage(position)
        }
        else if(categorycount==1)
        {
           desktopview_adapter.deleteImage(position)
        }
        else if(categorycount == 2)
        {
            racksimagesview_adapter.deleteImage(position)
        }
        else if(categorycount == 3)
        {
            frontcounterview_adapter.deleteImage(position)
        }
        else if(categorycount == 4)
        {
            gandolaimagesview_adapter.deleteImage(position)
        }
        else
        {
            otherimagesview_adapter.deleteImage(position)
        }
    }

    class ImageRecyclerView(
        var orderData: ArrayList<ImageDataDto>,
        val imageClicklistner: ImageClickListner,
    ) :
        SimpleRecyclerView<ViewImageItemBinding, ImageDataDto>(
            orderData,
            R.layout.view_image_item
        ) {
        override fun bindItems(
            binding: ViewImageItemBinding,
            items: ImageDataDto,
            position: Int,
        ) {
            //Uri.fromFile(imageFromCameraFile)
            binding.image.setImageURI(Uri.fromFile(items.file))
            binding.deleteImage.setOnClickListener {
                imageClicklistner.deleteImage(position)
            }
        }

        fun notifyAdapter(userList: ArrayList<ImageDataDto>) {
            this.orderData = userList
            notifyDataSetChanged()
        }

        fun deleteImage(position: Int) {
            orderData.removeAt(position)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orderData.size)
        }
    }
}

interface ImageClickListner {
    fun deleteImage(position: Int)
}