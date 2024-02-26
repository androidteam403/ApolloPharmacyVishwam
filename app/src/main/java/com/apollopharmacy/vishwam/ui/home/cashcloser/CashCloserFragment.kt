package com.apollopharmacy.vishwam.ui.home.cashcloser

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
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
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
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.CashDepositConfirmDialogBinding
import com.apollopharmacy.vishwam.databinding.DialogCashDepositFilterBinding
import com.apollopharmacy.vishwam.databinding.FragmentCashCloserBinding
import com.apollopharmacy.vishwam.databinding.PreviewImageDialogBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.cashcloser.adapter.CashCloserPendingAdapter
import com.apollopharmacy.vishwam.ui.home.cashcloser.cashdepositbolbstorage.CashDepositBlobStorage
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsRequest
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsResponse
import com.apollopharmacy.vishwam.util.Utils
import com.bumptech.glide.Glide
import java.io.File


class CashCloserFragment : BaseFragment<CashCloserViewModel, FragmentCashCloserBinding>(),
    CashCloserCalenderDialog.DateSelected,
    CashCloserFragmentCallback {
    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    lateinit var cashCloserPendingAdapter: CashCloserPendingAdapter
    private var cashDepositDetailsList = ArrayList<CashDepositDetailsResponse.Cashdeposit>()
    lateinit var dialogCashDepositFilterBinding: DialogCashDepositFilterBinding
    var fromDate = Utils.getCurrentDate()
    var toDate = Utils.getCurrentDate()
    var isPendingListClicked = true
    var isCompletedListClicked = true
    var isFromDateSelected: Boolean = false
    var cashCloserList: String =
        "pendingList,completedList,selectAll"


    var isPendingGlobal: Boolean = true
    var isCompletedGlobal: Boolean = false
    var fromDateGlobal: String =
        Utils.minusThirtyDays(Utils.getCurrentDate())//Utils.getthirtyDaysBackDate()
    var toDateGlobal: String = Utils.getCurrentDate()

    var isPendingInternal: Boolean = true
    var isCompletedInternal: Boolean = false
    var fromDateInternal: String = Utils.getthirtyDaysBackDate()
    var toDateInternal: String = Utils.getCurrentDate()

    override val layoutRes: Int
        get() = R.layout.fragment_cash_closer

    override fun retrieveViewModel(): CashCloserViewModel {
        return ViewModelProvider(this).get(CashCloserViewModel::class.java)
    }

    override fun setup() {
        showLoading()
        viewModel.getCashDepositDetails(
            "14068",
            this@CashCloserFragment,
            fromDateGlobal, toDateGlobal,
            if (isPendingGlobal && isCompletedGlobal) {
                ""
            } else if (isPendingGlobal) {
                "PENDING"
            } else {
                "COMPLETED"
            },
        )
        //"2024-02-01", "2024-02-15", "PENDING"
        MainActivity.mInstance.filterIconApna.setOnClickListener {
            navigateToListActivity()
        }

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
        // navigateToListActivity()
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun navigateToListActivity() {
        isPendingInternal = isPendingGlobal
        isCompletedInternal = isCompletedGlobal
        fromDateInternal = fromDateGlobal
        toDateInternal = toDateGlobal


        val cashDepositFilterDialog =
            Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialogCashDepositFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_cash_deposit_filter,
            null,
            false
        )
        cashDepositFilterDialog.setContentView(dialogCashDepositFilterBinding.root)
        cashDepositFilterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogCashDepositFilterBinding.fromDateText.text = fromDateGlobal//fromDate
        dialogCashDepositFilterBinding.toDateText.text = toDateGlobal//toDate

        if (isPendingInternal && isCompletedInternal) {
            dialogCashDepositFilterBinding.selectAll.isChecked = true
        } else {
            dialogCashDepositFilterBinding.selectAll.isChecked = false
        }

        if (isPendingInternal) {
            dialogCashDepositFilterBinding.pendingList.background =
                requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
            dialogCashDepositFilterBinding.pendingList.setTextColor(
                requireContext().resources.getColor(
                    R.color.white_for_both
                )
            )
        } else {
            dialogCashDepositFilterBinding.pendingList.background =
                requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
            dialogCashDepositFilterBinding.pendingList.setTextColor(
                requireContext().resources.getColor(
                    R.color.greyyy
                )
            )
        }

        if (isCompletedInternal) {
            dialogCashDepositFilterBinding.completedList.background =
                requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
            dialogCashDepositFilterBinding.completedList.setTextColor(
                requireContext().resources.getColor(
                    R.color.white_for_both
                )
            )
        } else {
            dialogCashDepositFilterBinding.completedList.setTextColor(
                requireContext().resources.getColor(
                    R.color.greyyy
                )
            )
            dialogCashDepositFilterBinding.completedList.background =
                requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
        }


        dialogCashDepositFilterBinding!!.fromDateText.text =
            fromDateInternal//Utils.getCurrentDate()
        dialogCashDepositFilterBinding!!.toDateText.text = toDateInternal //Utils.getCurrentDate()
        /*this.cashCloserList = ""
        if (isPendingListClicked) {
            this.cashCloserList = "pendingList"
        }
        if (isCompletedListClicked) {
            if (this.cashCloserList.isEmpty()) {
                this.cashCloserList = "completedList"
            } else {
                this.cashCloserList = "${this.cashCloserList},completedList"
            }
        }
        if (dialogCashDepositFilterBinding.selectAll.isChecked) {
            this.cashCloserList = "pendingList,completedList,selectAll"
        }*/

        dialogCashDepositFilterBinding.fromDate.setOnClickListener {
            isFromDateSelected = true
            openDateDialog()
        }
        dialogCashDepositFilterBinding.toDate.setOnClickListener {
            isFromDateSelected = false
            openDateDialog()
        }


        submitButtonEnable(
            isPendingInternal,
            isCompletedInternal,
            dialogCashDepositFilterBinding
        )

        dialogCashDepositFilterBinding!!.pendingList.setOnClickListener {
            if (isPendingInternal) {
                isPendingInternal = false
                dialogCashDepositFilterBinding!!.pendingList.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogCashDepositFilterBinding!!.pendingList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.greyyy
                    )
                )
            } else {
                isPendingInternal = true
                dialogCashDepositFilterBinding!!.pendingList.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogCashDepositFilterBinding!!.pendingList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.white_for_both
                    )
                )
            }
            submitButtonEnable(
                isPendingInternal,
                isCompletedInternal,
                dialogCashDepositFilterBinding!!
            )
        }

        dialogCashDepositFilterBinding!!.completedList.setOnClickListener {
            if (isCompletedListClicked) {
                isCompletedListClicked = false
                dialogCashDepositFilterBinding!!.completedList.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogCashDepositFilterBinding!!.completedList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.greyyy
                    )
                )
                isCompletedInternal = false
            } else {
                isCompletedListClicked = true
                isCompletedInternal = true
                dialogCashDepositFilterBinding!!.completedList.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogCashDepositFilterBinding!!.completedList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.white_for_both
                    )
                )
            }
            submitButtonEnable(
                isPendingInternal,
                isCompletedInternal,
                dialogCashDepositFilterBinding!!
            )
        }
        dialogCashDepositFilterBinding!!.selectAllCheckboxLayout.setOnClickListener {
            dialogCashDepositFilterBinding!!.selectAll.isChecked =
                !dialogCashDepositFilterBinding!!.selectAll.isChecked
            if (dialogCashDepositFilterBinding!!.selectAll.isChecked) {

                isPendingInternal = true
                isCompletedInternal = true
                dialogCashDepositFilterBinding!!.pendingList.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogCashDepositFilterBinding!!.pendingList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.white_for_both
                    )
                )
                dialogCashDepositFilterBinding!!.completedList.background =
                    requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogCashDepositFilterBinding!!.completedList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.white_for_both
                    )
                )
            } else {
                isPendingInternal = false
                isCompletedInternal = false
                dialogCashDepositFilterBinding!!.pendingList.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogCashDepositFilterBinding!!.pendingList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.greyyy
                    )
                )
                dialogCashDepositFilterBinding!!.completedList.background =
                    requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogCashDepositFilterBinding!!.completedList.setTextColor(
                    requireContext().resources.getColor(
                        R.color.greyyy
                    )
                )
                dialogCashDepositFilterBinding!!.selectAll.isChecked = true
            }
            submitButtonEnable(
                isPendingInternal,
                isCompletedInternal,
                dialogCashDepositFilterBinding!!
            )
        }
        dialogCashDepositFilterBinding.clearAllFilters.setOnClickListener {
            isPendingInternal = true
            isCompletedInternal = false

            isPendingGlobal = isPendingInternal
            isCompletedGlobal = isCompletedInternal

            dialogCashDepositFilterBinding.fromDateText.text =
                Utils.minusThirtyDays(Utils.getCurrentDate())
            dialogCashDepositFilterBinding.toDateText.text = Utils.getCurrentDate()

            fromDateGlobal = dialogCashDepositFilterBinding.fromDateText.text.toString()
            toDateGlobal = dialogCashDepositFilterBinding.toDateText.text.toString()

            cashDepositFilterDialog.dismiss()
            showLoading()
            viewModel.getCashDepositDetails(
                "14068",
                this@CashCloserFragment,
                fromDateGlobal,
                toDateGlobal,
                if (isPendingGlobal && isCompletedGlobal) {
                    ""
                } else if (isPendingGlobal) {
                    "PENDING"
                } else {
                    "COMPLETED"
                },
            )
        }
        dialogCashDepositFilterBinding.submit.setOnClickListener {
            isPendingGlobal = isPendingInternal
            isCompletedGlobal = isCompletedInternal
            fromDateGlobal = dialogCashDepositFilterBinding.fromDateText.text.toString()
            toDateGlobal = dialogCashDepositFilterBinding.toDateText.text.toString()
            cashDepositFilterDialog.dismiss()
            showLoading()
            viewModel.getCashDepositDetails(
                "14068",
                this@CashCloserFragment,
                fromDateGlobal,
                toDateGlobal,
                if (isPendingGlobal && isCompletedGlobal) {
                    ""
                } else if (isPendingGlobal) {
                    "PENDING"
                } else {
                    "COMPLETED"
                },
            )
        }
        dialogCashDepositFilterBinding.closeDialog.setOnClickListener {
            cashDepositFilterDialog.dismiss()
        }
        cashDepositFilterDialog.show()
    }

    fun submitButtonEnable(
        isPendingListClicked: Boolean,
        isCompletedListClicked: Boolean,
        dialogCashDepositFilterBinding: DialogCashDepositFilterBinding,
    ) {
        if (!isPendingListClicked && !isCompletedListClicked) {
            dialogCashDepositFilterBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogCashDepositFilterBinding.isSubmitEnable = false
            dialogCashDepositFilterBinding.isSelectAllChecked = false
        } else if (isPendingListClicked && isCompletedListClicked) {
            dialogCashDepositFilterBinding.submit.setBackgroundResource(R.drawable.dark_blue_bg_for_btn)
            dialogCashDepositFilterBinding.isSubmitEnable = true
            dialogCashDepositFilterBinding.isSelectAllChecked = true
        } else {
            dialogCashDepositFilterBinding.submit.setBackgroundResource(R.drawable.dark_blue_bg_for_btn)
            dialogCashDepositFilterBinding.isSubmitEnable = true
            dialogCashDepositFilterBinding.isSelectAllChecked = false
        }
    }

    /*  fun submitClick() {
        var fromDate = viewBinding.fromDateText.text.toString()
        var toDate = viewBinding.toDateText.text.toString()
        if (Utils.getDateDifference(fromDate, toDate) > 0) {
            if (!viewBinding.pullToRefresh.isRefreshing) Utlis.showLoading(requireContext())
            isLoadMoreAvailable = true
            callAPI(1)
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_check_dates),
                Toast.LENGTH_SHORT
            ).show()
        }

    }*/
    fun openDateDialog() {
        if (isFromDateSelected) {
            CashCloserCalenderDialog().apply {
                arguments = generateParsedData(
                    dialogCashDepositFilterBinding!!.fromDateText.text.toString(),
                    false,
                    dialogCashDepositFilterBinding!!.fromDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        } else {
            CashCloserCalenderDialog().apply {
                arguments = generateParsedData(
                    dialogCashDepositFilterBinding!!.toDateText.text.toString(),
                    true,
                    dialogCashDepositFilterBinding!!.fromDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        }
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
                    capturedImageUrl = CashDepositBlobStorage.captureImageBlobStorage(
                        imageurl,
                        "${System.currentTimeMillis()}.jpg"
                    )

                    capturedImageUrlTwo =
                        CashDepositBlobStorage.captureImageBlobStorage(
                            imageurlTwo,
                            "${System.currentTimeMillis()}.jpg"
                        )

                    url = "$capturedImageUrl,$capturedImageUrlTwo"
                } else if (imageurl != null) {
                    capturedImageUrl = CashDepositBlobStorage.captureImageBlobStorage(
                        imageurl,
                        "${System.currentTimeMillis()}.jpg"
                    )
                    url = capturedImageUrl
                } else {
                    capturedImageUrlTwo =
                        CashDepositBlobStorage.captureImageBlobStorage(
                            imageurlTwo!!,
                            "${System.currentTimeMillis()}.jpg"
                        )
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
            cashDepositDetailsList.sortByDescending { it.closingdate }

            viewBinding.recyclerViewCashCloser.visibility = View.VISIBLE
            viewBinding.emptyList.visibility = View.GONE

            cashCloserPendingAdapter = CashCloserPendingAdapter(
                requireContext(),
                cashDepositDetailsList,
                this
            )
            val linearLayoutManager = LinearLayoutManager(requireContext())
            viewBinding.recyclerViewCashCloser.adapter = cashCloserPendingAdapter
            viewBinding.recyclerViewCashCloser.layoutManager = linearLayoutManager
        } else {
            viewBinding.recyclerViewCashCloser.visibility = View.GONE
            viewBinding.emptyList.visibility = View.VISIBLE
        }
    }

    override fun onFailureGetCashDepositDetailsApiCall(message: String) {
        hideLoading()
        viewBinding.recyclerViewCashCloser.visibility = View.GONE
        viewBinding.emptyList.visibility = View.VISIBLE
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                /*viewModel.getCashDepositDetails(
                    "14068",
                    this@CashCloserFragment,
                    "2024-02-01",
                    "2024-02-15",
                    "PENDING",
                )*/
                showLoading()
                viewModel.getCashDepositDetails(
                    "14068",
                    this@CashCloserFragment,
                    fromDateGlobal, toDateGlobal,
                    if (isPendingGlobal && isCompletedGlobal) {
                        ""
                    } else if (isPendingGlobal) {
                        "PENDING"
                    } else {
                        "COMPLETED"
                    },
                )
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

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            dialogCashDepositFilterBinding!!.fromDateText.setText(showingDate)
            var toDateInternal = Utils.plusThirtyDays(showingDate)
            if (Utils.getDateDifference(toDateInternal, Utils.getCurrentDate()) == 1) {
                dialogCashDepositFilterBinding!!.toDateText.setText(toDateInternal)//Utils.getCurrentDate()
            } else {
                dialogCashDepositFilterBinding!!.toDateText.setText(Utils.getCurrentDate())
            }
        } else {
            dialogCashDepositFilterBinding!!.toDateText.setText(showingDate)
            var fromDateInternal = Utils.minusThirtyDays(showingDate)
            dialogCashDepositFilterBinding!!.fromDateText.setText(fromDateInternal)
        }
        /*if (isFromDateSelected) {
            isFromDateSelected = false;
            dialogCashDepositFilterBinding!!.fromDateText.setText(showingDate);
            var toDateInternal = Utils.plusThirtyDays(showingDate);
            if (Utils.getDateDifference(toDateInternal, Utils.getCurrentDate()) < 30) {
                dialogCashDepositFilterBinding!!.toDateText.setText(Utils.getCurrentDate());
            } else {
                dialogCashDepositFilterBinding!!.toDateText.setText(toDateInternal);
            }
        } else {
            dialogCashDepositFilterBinding!!.toDateText.setText(showingDate);
            var fromDateInternal = Utils.plusThirtyDays(showingDate);
            dialogCashDepositFilterBinding!!.fromDateText.setText(fromDateInternal);
        }*/
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {

    }
}
