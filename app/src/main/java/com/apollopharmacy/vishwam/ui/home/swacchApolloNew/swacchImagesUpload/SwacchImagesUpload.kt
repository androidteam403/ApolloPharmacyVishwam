package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.databinding.FragmentSwacchImagesUploadBinding
import com.apollopharmacy.vishwam.databinding.ViewImageItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.SwachhApollo.ImageClickListner
import com.apollopharmacy.vishwam.ui.home.SwachhApollo.SwachhapolloModel
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.adapter.ConfigListAdapter
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.MovieModel
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.SwachModelResponse
import java.io.File

class SwacchImagesUpload : BaseFragment<SwachhapolloModel, FragmentSwacchImagesUploadBinding>(),
    ImageClickListner {
    var swachhapolloModel: SwachhapolloModel? = null
    lateinit var swachModelResponse: SwachModelResponse

    var imageFromCameraFile: File? = null
    private lateinit var configListAdapter: ConfigListAdapter
    private val movieList = ArrayList<MovieModel>()


    override val layoutRes: Int
        get() = R.layout.fragment_swacch_images_upload


    override fun retrieveViewModel(): SwachhapolloModel {
        return ViewModelProvider(this).get(SwachhapolloModel::class.java)

    }

    override fun setup() {
        viewModel.swachImagesRegister()



        configListAdapter = ConfigListAdapter(movieList)
        val layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerViewimageswach.layoutManager = layoutManager
        viewBinding.recyclerViewimageswach.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerViewimageswach.adapter = configListAdapter


        prepareMovieData()

        configListAdapter.onItemClick = {
            openCamera()
        }


    }

    private fun prepareMovieData() {
        var movie = MovieModel("FRONT VIEW")
        movieList.add(movie)
        movie = MovieModel("COMPUTER AND UPS")
        movieList.add(movie)
        movie = MovieModel("RACKS")
        movieList.add(movie)
        movie = MovieModel("FRONT COUNTER")
        movieList.add(movie)
        movie = MovieModel("UPLOAD PHARMACY GANDOLA DISPLAY")
        movieList.add(movie)
        configListAdapter.notifyDataSetChanged()
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

    override fun deleteImage(position: Int) {
        TODO("Not yet implemented")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {


        }
    }


}

interface ImageClickListner {
    fun deleteImage(position: Int)
}