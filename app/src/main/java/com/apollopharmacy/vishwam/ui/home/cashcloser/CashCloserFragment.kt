package com.apollopharmacy.vishwam.ui.home.cashcloser

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
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
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.CashDepositConfirmDialogBinding
import com.apollopharmacy.vishwam.databinding.FragmentCashCloserBinding
import com.apollopharmacy.vishwam.databinding.PreviewImageDialogBinding
import com.apollopharmacy.vishwam.ui.home.cashcloser.adapter.CashCloserPendingAdapter
import com.apollopharmacy.vishwam.ui.home.cashcloser.cashdepositbolbstorage.CashDepositBlobStorage
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashCloserList
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsRequest
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsResponse
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.ImageData
import com.apollopharmacy.vishwam.ui.home.greeting.model.EmployeeWishesRequest
import com.apollopharmacy.vishwam.ui.home.greeting.wishesblobstorage.EmployeeWishesBlobStorage
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_getstorepersonhistory.*
import java.io.File
import java.lang.reflect.InvocationTargetException


class CashCloserFragment : BaseFragment<CashCloserViewModel, FragmentCashCloserBinding>(),
    CashCloserFragmentCallback {
    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    lateinit var cashCloserPendingAdapter: CashCloserPendingAdapter

    private var cashDepositDetailsList = ArrayList<CashDepositDetailsResponse.Cashdeposit>()

    override val layoutRes: Int
        get() = R.layout.fragment_cash_closer

    override fun retrieveViewModel(): CashCloserViewModel {
        return ViewModelProvider(this).get(CashCloserViewModel::class.java)
    }

    override fun setup() {
        showLoading()

        viewModel.getCashDepositDetails("14068", this@CashCloserFragment)
    }

    var imagePosition: Int = 0
    var siteId: String = ""
    var imageState: Int = 0
    override fun addImage(siteId: String, imagePosition: Int, imageState: Int) {
        this.imagePosition = imagePosition
        this.siteId = siteId
        this.imageState = imageState
        if (!checkPermission()) {
            askPermissions(100)
            return
        } else {
            openCamera()
        }
    }

    override fun deleteImage(position: Int, imageState: Int) {

        for (i in cashDepositDetailsList) {
            if (cashDepositDetailsList.indexOf(i) == position) {
                if (imageState == 1) {
                    i.imagePath = null
                } else if (imageState == 2) {
                    i.imagePathTwo = null
                }
            }
        }
        cashCloserPendingAdapter.notifyDataSetChanged()
    }

    override fun previewImage(file: String, position: Int) {
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
            .load(file)
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
        for (i in cashDepositDetailsList) {
            if (cashDepositDetailsList.indexOf(i) == pos) {
                i.setIsExpanded(!i.isExpanded)
            } else {
                i.setIsExpanded(false)
            }
        }
        cashCloserPendingAdapter.notifyDataSetChanged()
    }

    var uploadedItemSiteId: String = ""

    override fun onClickUpload(
        siteid: String,
        imageurl: File?,
        imageurlTwo: File?,
        amount: String,
        remarks: String,
        dcid: String,
        createdBy: String,
    ) {
        showLoading()

        var capturedImageUrl: String? = null
        var capturedImageUrlTwo: String? = null
        var url: String? = null
        uploadedItemSiteId = siteId

        val thread = Thread {
            try {

                if (imageurl != null && imageurlTwo != null) {
                    capturedImageUrl = CashDepositBlobStorage.captureImageBlobStorage(imageurl,
                        "${System.currentTimeMillis()}.jpg")

                    capturedImageUrlTwo =
                        CashDepositBlobStorage.captureImageBlobStorage(imageurlTwo,
                            "${System.currentTimeMillis()}.jpg")

                    url = "$capturedImageUrl,$capturedImageUrlTwo"
                } else if (imageurl != null) {
                    capturedImageUrl = CashDepositBlobStorage.captureImageBlobStorage(imageurl,
                        "${System.currentTimeMillis()}.jpg")
                    url = capturedImageUrl
                } else {
                    capturedImageUrlTwo =
                        CashDepositBlobStorage.captureImageBlobStorage(imageurlTwo!!,
                            "${System.currentTimeMillis()}.jpg")
                    url = capturedImageUrlTwo
                }

                val cashDepositDetailsRequest = CashDepositDetailsRequest()
                cashDepositDetailsRequest.siteid = siteid
                cashDepositDetailsRequest.imageurl = url
                cashDepositDetailsRequest.amount = amount
                cashDepositDetailsRequest.remarks = remarks
                cashDepositDetailsRequest.dcid = dcid
                cashDepositDetailsRequest.createdby = createdBy
                viewModel.saveCashDepositDetails(cashDepositDetailsRequest, this@CashCloserFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    override fun onSuccessGetCashDepositDetailsApiCall(cashDepositDetailsResponse: CashDepositDetailsResponse) {
        hideLoading()
        if (cashDepositDetailsResponse != null && cashDepositDetailsResponse.cashdeposit != null && cashDepositDetailsResponse.cashdeposit!!.size > 0) {
            cashDepositDetailsList =
                cashDepositDetailsResponse.cashdeposit as ArrayList<CashDepositDetailsResponse.Cashdeposit>

            cashCloserPendingAdapter = CashCloserPendingAdapter(
                requireContext(),
                cashDepositDetailsList,
                this
            )
            val linearLayoutManager = LinearLayoutManager(requireContext())
            viewBinding.recyclerViewCashCloser.adapter = cashCloserPendingAdapter
            viewBinding.recyclerViewCashCloser.layoutManager = linearLayoutManager
        }
    }

    override fun onFailureGetCashDepositDetailsApiCall(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessSaveCashDepositDetailsApiCall(cashDepositDetailsResponse: CashDepositDetailsResponse) {
        hideLoading()

        if (cashDepositDetailsResponse.status == true) {
            val dialog = Dialog(requireContext())
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val cashDepositConfirmDialog: CashDepositConfirmDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.cash_deposit_confirm_dialog,
                null,
                false
            )
            dialog.setContentView(cashDepositConfirmDialog.root)
            cashDepositConfirmDialog.okButton.setOnClickListener {
            viewModel.getCashDepositDetails("14068", this@CashCloserFragment)
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    override fun onFailureSaveCashDepositDetailsApiCall(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

            if (imageState == 1) {
                cashDepositDetailsList.get(imagePosition).imagePath = imageFile
            } else {
                cashDepositDetailsList.get(imagePosition).imagePathTwo = imageFile
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
