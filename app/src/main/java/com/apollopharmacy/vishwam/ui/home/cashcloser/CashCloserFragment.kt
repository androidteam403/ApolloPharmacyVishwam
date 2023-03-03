package com.apollopharmacy.vishwam.ui.home.cashcloser

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.FragmentCashCloserBinding
import com.apollopharmacy.vishwam.databinding.PreviewImageDialogBinding
import com.apollopharmacy.vishwam.ui.home.cashcloser.adapter.CashCloserPendingAdapter
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashCloserList
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.ImageData
import com.bumptech.glide.Glide
import java.io.File


class CashCloserFragment : BaseFragment<CashCloserViewModel, FragmentCashCloserBinding>(),
    CashCloserFragmentCallback {
    var imageFile: File? = null
    private var compressedImageFileName: String? = null

    //    private var capturedImages = ArrayList<ImageData>()
    lateinit var cashCloserPendingAdapter: CashCloserPendingAdapter
    val list = ArrayList<CashCloserList>()


    override val layoutRes: Int
        get() = R.layout.fragment_cash_closer

    override fun retrieveViewModel(): CashCloserViewModel {
        return ViewModelProvider(this).get(CashCloserViewModel::class.java)
    }

    override fun setup() {
        var capturedImages1 = ArrayList<ImageData>()

        capturedImages1.add(ImageData(null, ""))
        capturedImages1.add(ImageData(null, ""))

        var capturedImages2 = ArrayList<ImageData>()

        capturedImages2.add(ImageData(null, ""))
        capturedImages2.add(ImageData(null, ""))

        var capturedImages3 = ArrayList<ImageData>()

        capturedImages3.add(ImageData(null, ""))
        capturedImages3.add(ImageData(null, ""))

        var capturedImages4 = ArrayList<ImageData>()

        capturedImages4.add(ImageData(null, ""))
        capturedImages4.add(ImageData(null, ""))

        list.add(CashCloserList("16001", "2023-01-03", "Pending", capturedImages1))
        list.add(CashCloserList("16291", "2023-01-03", "Pending", capturedImages2))
        list.add(CashCloserList("16002", "2023-01-03", "Pending", capturedImages3))
        list.add(CashCloserList("16290", "2023-01-03", "Pending", capturedImages4))

        cashCloserPendingAdapter =
            CashCloserPendingAdapter(requireContext(), list, this)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerViewCashCloser.adapter = cashCloserPendingAdapter
        viewBinding.recyclerViewCashCloser.layoutManager = linearLayoutManager


    }

    var imagePosition: Int = 0

    override fun addImage(imagePosition: Int) {
        this.imagePosition = imagePosition
        if (!checkPermission()) {
            askPermissions(100)
            return
        } else {
            openCamera()
        }
    }

    override fun deleteImage(imagePosition: Int) {
    }

    override fun previewImage(file: File, position: Int) {
        var dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var previewImageDialogBinding: PreviewImageDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.preview_image_dialog,
            null,
            false
        )
        dialog.setContentView(previewImageDialogBinding.root)
        Glide
            .with(requireContext())
            .load(file.toString())
            .placeholder(R.drawable.placeholder_image)
            .into(previewImageDialogBinding.previewImage)
        previewImageDialogBinding.close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    var expandedItemSiteId: String = ""
    override fun headrItemClickListener(storeId: String, pos: Int) {
        expandedItemSiteId = storeId
        for (i in list) {
            if (list.indexOf(i) == pos) {
                i.setIsExpanded(!i.isExpanded)
            } else {
                i.setIsExpanded(false)
            }
        }
        cashCloserPendingAdapter.notifyDataSetChanged()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")
        compressedImageFileName = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFile != null && resultCode == Activity.RESULT_OK) {
            for (i in list) {
                if (i.siteId.equals(expandedItemSiteId)) {
                    i.imageList!!.get(imagePosition).file = imageFile!!
                }
            }
            cashCloserPendingAdapter!!.notifyDataSetChanged()
        }
    }


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
}