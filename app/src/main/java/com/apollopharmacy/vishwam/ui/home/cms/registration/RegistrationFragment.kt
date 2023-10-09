package com.apollopharmacy.vishwam.ui.home.cms.registration

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.eposmobileapp.ui.dashboard.ConfirmSiteDialog
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Config.REQUEST_CODE_CAMERA
import com.apollopharmacy.vishwam.data.Config.REQUEST_CODE_PRODUCT_FRONT_CAMERA
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.*
import com.apollopharmacy.vishwam.dialog.AcknowledgementDialog.Companion.KEY_DATA_ACK
import com.apollopharmacy.vishwam.dialog.CategoryDialog.Companion.KEY_DATA_SUBCATEGORY
import com.apollopharmacy.vishwam.dialog.CustomDialog.Companion.KEY_DATA
import com.apollopharmacy.vishwam.dialog.model.Row
import com.apollopharmacy.vishwam.ui.home.IOnBackPressed
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivity.isSuperAdmin
import com.apollopharmacy.vishwam.ui.home.cms.cmsfileupload.CmsFileUpload
import com.apollopharmacy.vishwam.ui.home.cms.cmsfileupload.CmsFileUploadCallback
import com.apollopharmacy.vishwam.ui.home.cms.cmsfileupload.CmsFileUploadModel
import com.apollopharmacy.vishwam.ui.home.cms.registration.adapter.AllowDuplicateStCreationAdapter
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FetchItemModel
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.UpdateUserDefaultSiteRequest
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.signaturepad.NetworkUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import me.echodev.resizer.Resizer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class RegistrationFragment : BaseFragment<RegistrationViewModel, FragmentRegistrationBinding>(),
    CustomDialog.AbstractDialogClickListner, CategoryDialog.SubCategoryDialogClickListner,
    SubCategoryDialog.SubSubCategoryDialogClickListner, CalendarDialog.DateSelected,
    ImageClickListner, SiteDialog.AbstractDialogSiteClickListner,
    SubmitcomplaintDialog.AbstractDialogSubmitClickListner, ConfirmSiteDialog.OnSiteClickListener,
    ReasonsDialog.ReasonsDialogClickListner,
    SearchArticleCodeDialog.SearchArticleDialogClickListner, CalendarFutureDate.DateSelectedFuture,
    OnTransactionPOSSelectedListnier, IOnBackPressed, CmsFileUploadCallback {

    private var statusInventory: String? = null
    lateinit var userData: LoginDetails
    lateinit var storeData: LoginDetails.StoreData
    private var fileArrayList = ArrayList<ImageDataDto>()
    private var InventoryfileArrayList = ArrayList<ImageDataDto>()
    lateinit var dialog: Dialog
    lateinit var adapter: ImageRecyclerView
    private var imagesArrayListSend = ArrayList<SubmitNewV2Response.PrescriptionImagesItem>()
    var imageFromCameraFile: File? = null
    var frontImageFile: File? = null
    var backImageFile: File? = null
    var otherImageFile: File? = null
    private var categoryListSelected = ArrayList<DepartmentV2Response.CategoriesItem>()
    private var subCategoryListSelected = ArrayList<DepartmentV2Response.SubcategoryItem>()
    private var departmentId: String? = null
    private var maintanceArrayList = arrayListOf<String>()
    private var storeInfo: String = ""
    private var dcInfo: String = ""
    val TAG = "RegistrationFragment"


    var platformUid: String? = null
    var deptuid: String? = null
    var deptCode: String? = null
    var categoryuid: String? = null
    var subcategoryuid: String? = null
    var reasonuid: String? = null
    lateinit var reasonSla: ArrayList<ReasonmasterV2Response.Reason_SLA>
    var siteid: String? = null
    var imagesFilledCount = 0
    var clickedCamera = 0
    var notFrontView = false

    var dynamicsiteid: String? = null

    var cmsloginresponse: ResponseCMSLogin.Data? = null
    var ticketstatusapiresponse: ResponseTicktResolvedapi? = null
    var ticketratingapiresponse: ResponseticketRatingApi.Data? = null

    private var NewimagesArrayListSend = ArrayList<RequestNewComplaintRegistration.Image>()

    private var reasoncategoryListSelected = ArrayList<ReasonmasterV2Response.TicketCategory>()
    private var reasonssubCategoryListSelected =
        ArrayList<ReasonmasterV2Response.TicketSubCategory>()

    private var reasonsListSelected = ArrayList<ReasonmasterV2Response.Row>()

    private var ticketInventoryItems =
        ArrayList<RequestSaveUpdateComplaintRegistration.TicketInventoryItem>()

    private var ticketCodeBatch = RequestSaveUpdateComplaintRegistration.CodeBatch()
    var employeeDetailsResponse: EmployeeDetailsResponse? = null
    lateinit var selectedCategory: ReasonmasterV2Response.TicketCategory
    lateinit var selectedSubCategory: ReasonmasterV2Response.TicketSubCategory
    lateinit var selectedReasonDto: ReasonmasterV2Response.Row

    var isBackPressAllow = ""
    lateinit var reasonmasterV2Response: ReasonmasterV2Response

    var siteUid = ""
    override val layoutRes: Int
        get() = R.layout.fragment_registration

    override fun retrieveViewModel(): RegistrationViewModel {
        return RegistrationViewModel()
    }

    override fun setup() {
        viewBinding.registration = viewModel
        userData = LoginRepo.getProfile()!!

        platformUid = "mobile"

        var empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()

        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(
                empDetailsResponse, EmployeeDetailsResponse::class.java
            )
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }

//        if (employeeDetailsResponse != null && employeeDetailsResponse!!.data != null && employeeDetailsResponse!!.data!!.role != null && employeeDetailsResponse!!.data!!.role!!.code.equals(
//                "store_supervisor")
//        ) {
        if (employeeDetailsResponse != null && employeeDetailsResponse!!.data != null && employeeDetailsResponse!!.data != null && employeeDetailsResponse!!.data!!.site != null && employeeDetailsResponse!!.data!!.site!!.site != null) {
            viewBinding.departmentLayout.visibility = View.VISIBLE
            val storeItem = StoreListItem(
                employeeDetailsResponse!!.data!!.site!!.storeName,
                employeeDetailsResponse!!.data!!.site!!.state!!.code,
                employeeDetailsResponse!!.data!!.site!!.dcCode!!.name,
                employeeDetailsResponse!!.data!!.site!!.site,
                employeeDetailsResponse!!.data!!.site!!.dcCode!!.code,
            )
            siteUid = employeeDetailsResponse!!.data!!.site!!.uid!!
            Preferences.setRegistrationSiteId(employeeDetailsResponse!!.data!!.site!!.site.toString())
            Preferences.saveSiteId(employeeDetailsResponse!!.data!!.site!!.site.toString())

//            Preferences.saveSiteId(userData.STOREDETAILS.get(0).SITEID)

            var storedata = StoreData(
                employeeDetailsResponse!!.data!!.site!!.site,
                employeeDetailsResponse!!.data!!.site!!.storeName,
                employeeDetailsResponse!!.data!!.site!!.dcCode!!.name,
                employeeDetailsResponse!!.data!!.site!!.state!!.code,
                employeeDetailsResponse!!.data!!.site!!.dcCode!!.code
            )
            LoginRepo.saveStoreData(storedata)

            showLoading()

            viewModel.getSelectedStoreDetails(storeItem)

            if (employeeDetailsResponse!!.data!!.site!!.site != null) {
                storeInfo =
                    employeeDetailsResponse!!.data!!.site!!.site + " - " + employeeDetailsResponse!!.data!!.site!!.storeName
            }
            if (employeeDetailsResponse!!.data!!.site!!.dcCode!!.code != null) {
                dcInfo =
                    employeeDetailsResponse!!.data!!.site!!.dcCode!!.code + " - " + employeeDetailsResponse!!.data!!.site!!.dcCode!!.name
            }
            viewBinding.siteIdSelect.setText(storeInfo)
            viewBinding.branchName.setText(dcInfo)

            val c = Calendar.getInstance().time
            println("Current time => $c")
            val df = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            var formattedDate = df.format(c)
            viewBinding.dateOfProblem.setText(formattedDate)
            viewBinding.dateOfProblem.isEnabled = false


            viewBinding.siteIdSelect.setOnClickListener {
                Toast.makeText(
                    context,
                    context?.resources?.getString(R.string.label_site_change_alert),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

//        }
//        else {
//            if (userData != null && userData.STOREDETAILS != null && userData.STOREDETAILS.size > 0) {
//                if (userData.STOREDETAILS.get(0).IsSelectedStore) {
//                    viewBinding.departmentLayout.visibility = View.VISIBLE
//                    val storeItem = StoreListItem(userData.STOREDETAILS.get(0).SITENAME,
//                        userData.STOREDETAILS.get(0).STATEID,
//                        userData.STOREDETAILS.get(0).DCNAME,
//                        userData.STOREDETAILS.get(0).SITEID,
//                        userData.STOREDETAILS.get(0).DC)
//                    Preferences.saveSiteId(userData.STOREDETAILS.get(0).SITEID)
//                    var storedata = StoreData(userData.STOREDETAILS.get(0).SITEID,
//                        userData.STOREDETAILS.get(0).SITENAME,
//                        userData.STOREDETAILS.get(0).DCNAME,
//                        userData.STOREDETAILS.get(0).STATEID,
//                        userData.STOREDETAILS.get(0).DC)
//                    LoginRepo.saveStoreData(storedata)
//
//                    showLoading()
//
//                    viewModel.getSelectedStoreDetails(storeItem)
//                    if (userData.STOREDETAILS.get(0).SITEID != null) {
//                        storeInfo =
//                            userData.STOREDETAILS.get(0).SITEID + " - " + userData.STOREDETAILS.get(
//                                0).SITENAME
//                    }
//                    if (userData.STOREDETAILS.get(0).DC != null) dcInfo =
//                        userData.STOREDETAILS.get(0).DC + " - " + userData.STOREDETAILS.get(0).DCNAME
//                    viewBinding.siteIdSelect.setText(storeInfo)
//                    viewBinding.branchName.setText(dcInfo)
//
//                    val c = Calendar.getInstance().time
//                    println("Current time => $c")
//                    val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
//                    var formattedDate = df.format(c)
//                    viewBinding.dateOfProblem.setText(formattedDate)
//                    viewBinding.dateOfProblem.isEnabled = false
//
//
//                    viewBinding.siteIdSelect.setOnClickListener {
//                        Toast.makeText(context,
//                            context?.resources?.getString(R.string.label_site_change_alert),
//                            Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
        showLoading()
        if (Preferences.isReasonIdListFetched()) {
            hideLoading()
            viewModel.getRemarksMasterList()
        } else {
            viewModel.getRemarksMasterList()

        }


        adapter = ImageRecyclerView(fileArrayList, this)
        viewBinding.imageRecyclerView.adapter = adapter
        viewBinding.selectDepartment.setOnClickListener {
            if (viewBinding.siteIdSelect.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please select Site Id", Toast.LENGTH_SHORT).show()
            } else {
                CustomDialog().apply {
                    arguments =
                        CustomDialog().generateParsedDatafromreasons(viewModel.getdepartmrntsformreasonslist())
                }.show(childFragmentManager, "")
            }
        }
        viewBinding.siteIdSelect.setOnClickListener {
            showLoading()
            viewModel.siteId()
        }
        viewModel.command.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CmsCommand.VisibleLayout -> {
                    viewBinding.problemLayout.visibility = View.VISIBLE
                }

                is CmsCommand.InVisibleLayout -> {
                    viewBinding.problemLayout.visibility = View.GONE
                }

                else -> {}
            }
        })

        //select category form Reasons list api..........
        viewBinding.selectCategory.setOnClickListener {
            if (reasoncategoryListSelected.size == 0) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_no_category_available),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                for (item in reasoncategoryListSelected) {
                    maintanceArrayList.add(item.name.toString())
                }
                CategoryDialog().apply {
                    arguments = CategoryDialog().generateParsedData(
                        reasoncategoryListSelected, KEY_DATA_SUBCATEGORY
                    )
                }.show(childFragmentManager, "")
            }
        }

        //select reason list....................
        viewBinding.selectRemarks.setOnClickListener {
            if (reasonsListSelected.size == 0) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_no_reasons_available),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ReasonsDialog().apply {
                    arguments = ReasonsDialog().generateParsedData(reasonsListSelected, KEY_DATA)
                }.show(childFragmentManager, "")
            }
        }

        //select subcategory form Reasons list api..........
        viewBinding.selectSubCategory.setOnClickListener {
            if (reasonssubCategoryListSelected.size == 0) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_no_sub_category_available),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                SubCategoryDialog().apply {
                    arguments = SubCategoryDialog().generateParsedData(
                        reasonssubCategoryListSelected, KEY_DATA
                    )
                }.show(childFragmentManager, "")
            }
        }

        //select reason list....................
        viewModel.reasonlistapiresponse.observe(viewLifecycleOwner, {
            hideLoading()
        })


        //ticket status api response..................
        viewModel.tisketstatusresponse.observe(viewLifecycleOwner, {
            var ticketstatus: ResponseTicktResolvedapi
            ticketstatus = it
            if (!ticketstatus.success) {
                ticketstatusapiresponse = ticketstatus
                if ((ticketstatusapiresponse!!.data!!.have_subworkflow == null || ticketstatusapiresponse!!.data!!.allow_manual_ticket_closure!!.isNullOrEmpty() || ticketstatusapiresponse!!.data!!.have_subworkflow == false) && (ticketstatusapiresponse!!.data!!.allow_manual_ticket_closure == null || ticketstatusapiresponse!!.data!!.allow_manual_ticket_closure!! == "Yes")) {
                    AcknowledgementDialog().apply {
                        arguments = AcknowledgementDialog().generateParsedDataNew(
                            ticketstatus.data, KEY_DATA_ACK
                        )
                    }.show(childFragmentManager, "")
                } else {
                    var dialog = Dialog(requireContext())
                    val dialogHavedubworkflowTicketresolvedBinding =
                        DataBindingUtil.inflate<DialogHavedubworkflowTicketresolvedBinding>(
                            LayoutInflater.from(requireContext()),
                            R.layout.dialog_havedubworkflow_ticketresolved,
                            null,
                            false
                        )
                    dialog.setContentView(dialogHavedubworkflowTicketresolvedBinding.root)
                    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    if (ticketstatusapiresponse!!.data != null && ticketstatusapiresponse!!.data!!.override_ticket_creation != null && ticketstatusapiresponse!!.data!!.override_ticket_creation.equals(
                            "No"
                        )
                    ) {
                        dialog.setCancelable(false)
                        dialogHavedubworkflowTicketresolvedBinding.message.text =
                            "To this selected Department & Store, this ticket has been resolved. Please close the ticket and create new one."
                    }
                    var override_ticket_creation: Boolean = false
                    if (ticketstatusapiresponse!!.data != null && ticketstatusapiresponse!!.data!!.override_ticket_creation != null && ticketstatusapiresponse!!.data!!.override_ticket_creation!!.isEmpty() && ticketstatusapiresponse!!.data!!.override_ticket_creation.equals(
                            "No"
                        )
                    ) {
                        override_ticket_creation = true
                    } else {
                        override_ticket_creation = false
                    }

                    dialogHavedubworkflowTicketresolvedBinding.ticketNo.setText(
                        ticketstatusapiresponse!!.data.ticket_id
                    )
                    if (ticketstatusapiresponse!!.data.ticket_created_time != null) {
                        dialogHavedubworkflowTicketresolvedBinding.regDate.setText(
                            "${Utlis.convertCmsDate(ticketstatusapiresponse!!.data.ticket_created_time.toString())}"
                        )
                    }
                    dialogHavedubworkflowTicketresolvedBinding.dilogaClose.setOnClickListener {
                        if (ticketstatusapiresponse!!.data != null && ticketstatusapiresponse!!.data!!.override_ticket_creation != null && !ticketstatusapiresponse!!.data!!.override_ticket_creation!!.isEmpty() && ticketstatusapiresponse!!.data!!.override_ticket_creation.equals(
                                "No"
                            )
                        ) {
                            isBackPressAllow = "GO_BACK"
                            dialog!!.dismiss()
                            MainActivity.mInstance.onBackPressed()
                        } else {
                            dialog!!.dismiss()
                        }
                    }
                    dialog.show()

                }
            }
        })

        //close ticket Response........
        viewModel.transactionPOSDetails.observe(viewLifecycleOwner, {
            SearchTransactionPOSDialog(it).apply { }.show(childFragmentManager, "")
        })


        viewBinding.addImage.setOnClickListener {
            if (fileArrayList.size == 2) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_upload_image_limit),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!checkPermission()) {
                    askPermissions(REQUEST_CODE_CAMERA)
                    return@setOnClickListener
                } else
//                    openCamera()
                    showOption(0)
            }
        }

        viewBinding.productImageView.productOtherImagePreview.setOnClickListener {
            showOption(3)
//            openCameraForFrontImage(3)
        }
        viewBinding.productImageView.productBackImagePreview.setOnClickListener {
            showOption(2)
//            openCameraForFrontImage(2)
        }
        viewBinding.productImageView.productFrontImagePreview.setOnClickListener {
            showOption(1)
//            openCameraForFrontImage(1)
        }
        viewBinding.productImageView.frontImageDelete.setOnClickListener {
            frontImageFile = null
            viewBinding.productImageView.frontImageDelete.visibility = View.GONE
            viewBinding.productImageView.productFrontImagePreview.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_capture_image
                )
            )
            imagesFilledCount--
        }
        viewBinding.productImageView.backImageDelete.setOnClickListener {
            backImageFile = null
            viewBinding.productImageView.backImageDelete.visibility = View.GONE
            viewBinding.productImageView.productBackImagePreview.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_capture_image
                )
            )
            imagesFilledCount--
        }
        viewBinding.productImageView.otherImageDelete.setOnClickListener {
            otherImageFile = null
            viewBinding.productImageView.otherImageDelete.visibility = View.GONE
            viewBinding.productImageView.productOtherImagePreview.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_capture_image
                )
            )
            imagesFilledCount--
        }
        viewBinding.dateOfProblem.setOnClickListener { openDateDialog() }
        viewBinding.articleCode.setOnClickListener {
            SearchArticleCodeDialog().apply { }.show(childFragmentManager, "")
        }
        viewBinding.expireDateExpire.setOnClickListener {
            CalendarFutureDate().let {
                it.setTargetFragment(this, 1)
                it.show(requireFragmentManager().beginTransaction(), "")
            }
        }
        viewBinding.submit.setOnClickListener {
            if (validationCheck()) {
                submitClick()
            }
        }

        viewModel.responsenewcomplaintregistration.observe(viewLifecycleOwner, {
            hideLoading()
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            RefreshView()

            SubmitcomplaintDialog().apply {
                arguments = SubmitcomplaintDialog().generateParsedData(it)
            }.show(childFragmentManager, "")
        })

        viewModel.command.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CmsCommand.ResonListforMobile -> {
                    reasonmasterV2Response = it.reasonmasterV2Response
                }

                is CmsCommand.RefreshPageOnSuccess -> {

                }

                is CmsCommand.ImageIsUploadedInAzur -> {
                    //saveTicketApi(it)
                }

                is CmsCommand.ShowToast -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is CmsCommand.CheckValidatedUserWithSiteID -> {
                    hideLoading()
                    onSuccessUserWithSiteID(it.slectedStoreItem)
                }

                is CmsCommand.ShowSiteInfo -> {
                    hideLoading()
                    SiteDialog().apply {

                        Preferences.setSiteIdList(Gson().toJson(viewModel.getSiteData()))
                        Preferences.setSiteIdListFetched(true)
                        arguments = SiteDialog().generateParsedData(viewModel.getSiteData())
                    }.show(childFragmentManager, "")
                }

                is CmsCommand.SuccessDeptList -> {
                    hideLoading()
                }

                else -> {}
            }
        })

        viewModel.pendingListLiveData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (it.status.equals("true")) {
                if (it.data.size != 0) {
                    AcknowledgementDialog().apply {
                        arguments =
                            AcknowledgementDialog().generateParsedData(it.data, KEY_DATA_ACK)
                    }.show(childFragmentManager, "")
                }
            }
        })

        viewBinding.mrpEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int,
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val text = charSequence.toString()
                if (charSequence.length <= 1) {
                    if (text.contains(".") && text.indexOf(".") == 0) {
                        viewBinding.mrpEditText.setText(
                            viewBinding.mrpEditText.text.toString().replace(".", "")
                        )
                        viewBinding.mrpEditText.setSelection(viewBinding.mrpEditText.text.toString().length)
                    }
                } else {
                    if (text.contains(".") && text.indexOf(".") != text.length - 1 && text[text.length - 1].toString() == ".") {
                        viewBinding.mrpEditText.setText(text.substring(0, text.length - 1))
                        viewBinding.mrpEditText.setSelection(viewBinding.mrpEditText.text.toString().length)
                    }
                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                        viewBinding.mrpEditText.setText(text.substring(0, text.length - 1))
                        viewBinding.mrpEditText.setSelection(viewBinding.mrpEditText.text.toString().length)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        viewBinding.purchasePriseEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int,
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val text = charSequence.toString()
                if (charSequence.length <= 1) {
                    if (text.contains(".") && text.indexOf(".") == 0) {
                        viewBinding.purchasePriseEdit.setText(
                            viewBinding.purchasePriseEdit.text.toString().replace(".", "")
                        )
                        viewBinding.purchasePriseEdit.setSelection(viewBinding.purchasePriseEdit.text.toString().length)
                    }
                } else {
                    if (text.contains(".") && text.indexOf(".") != text.length - 1 && text[text.length - 1].toString() == ".") {
                        viewBinding.purchasePriseEdit.setText(text.substring(0, text.length - 1))
                        viewBinding.purchasePriseEdit.setSelection(viewBinding.purchasePriseEdit.text.toString().length)
                    }
                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                        viewBinding.purchasePriseEdit.setText(text.substring(0, text.length - 1))
                        viewBinding.purchasePriseEdit.setSelection(viewBinding.purchasePriseEdit.text.toString().length)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
        viewBinding.oldmrpEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int,
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val text = charSequence.toString()
                if (charSequence.length <= 1) {
                    if (text.contains(".") && text.indexOf(".") == 0) {
                        viewBinding.oldmrpEditText.setText(
                            viewBinding.oldmrpEditText.text.toString().replace(".", "")
                        )
                        viewBinding.oldmrpEditText.setSelection(viewBinding.oldmrpEditText.text.toString().length)
                    }
                } else {
                    if (text.contains(".") && text.indexOf(".") != text.length - 1 && text[text.length - 1].toString() == ".") {
                        viewBinding.oldmrpEditText.setText(text.substring(0, text.length - 1))
                        viewBinding.oldmrpEditText.setSelection(viewBinding.oldmrpEditText.text.toString().length)
                    }
                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                        viewBinding.oldmrpEditText.setText(text.substring(0, text.length - 1))
                        viewBinding.oldmrpEditText.setSelection(viewBinding.oldmrpEditText.text.toString().length)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        viewBinding.newMrpEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int,
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val text = charSequence.toString()
                if (charSequence.length <= 1) {
                    if (text.contains(".") && text.indexOf(".") == 0) {
                        viewBinding.newMrpEdit.setText(
                            viewBinding.newMrpEdit.text.toString().replace(".", "")
                        )
                        viewBinding.newMrpEdit.setSelection(viewBinding.newMrpEdit.text.toString().length)
                    }
                } else {
                    if (text.contains(".") && text.indexOf(".") != text.length - 1 && text[text.length - 1].toString() == ".") {
                        viewBinding.newMrpEdit.setText(text.substring(0, text.length - 1))
                        viewBinding.newMrpEdit.setSelection(viewBinding.newMrpEdit.text.toString().length)
                    }
                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                        viewBinding.newMrpEdit.setText(text.substring(0, text.length - 1))
                        viewBinding.newMrpEdit.setSelection(viewBinding.newMrpEdit.text.toString().length)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        viewBinding.batchText.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        viewBinding.transactionDetailsLayout.approvalCodeEdit.setFilters(
            arrayOf<InputFilter>(
                InputFilter.AllCaps()
            )
        )
        viewBinding.transactionDetailsLayout.tidEdit.setOnClickListener {
            viewModel.fetchTransactionPOSDetails(viewModel.tisketstatusresponse.value!!.data.uid)
        }
        siteTicketbyReasonSuccess()
    }


    private fun validationCheck(): Boolean {
        val departmentName = viewBinding.selectDepartment.text.toString().trim()
        val categoryName = viewBinding.selectCategory.text.toString().trim()
        val subCategoryName = viewBinding.selectSubCategory.text.toString().trim()
        val problemDate = viewBinding.dateOfProblem.text.toString()
        val description = viewBinding.descriptionText.text.toString().trim()
        val articleCode = viewBinding.articleCode.text.toString().trim()
        val batchNumber = viewBinding.batchText.text.toString().trim()
        val mrpPrice = viewBinding.mrpEditText.text.toString().trim()
        val oldmrpPrice = viewBinding.oldmrpEditText.text.toString().trim()
        val newMrpPrice = viewBinding.newMrpEdit.text.toString().trim()
        val purchasePrice = viewBinding.purchasePriseEdit.text.toString().trim()
        val expiryDate = viewBinding.expireDateExpire.text.toString().trim()
        val reason = viewBinding.selectRemarks.text.toString().trim()


        if (departmentName.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_select_department))
            return false
        } else if (categoryName.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_select_category))
            return false
        } else if (subCategoryName.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_select_sub_category))
            return false
        } else if (reason.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_select_reason))
            return false
        } else if (problemDate.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_problem_since))
            return false
        } else if (statusInventory.equals("NEWBATCH") || statusInventory.equals("MRP Change Request") || statusInventory.equals(
                "New Batch Request"
            )
        ) {
            if (articleCode.isEmpty()) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_select_article_code))
                return false
            } else if (batchNumber.isEmpty()) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_select_batch_number))
                return false
            } else if (batchNumber.length < 3) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_select_batch_number_length))
                return false
            } else if (expiryDate.isEmpty()) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_select_expiry_date))
                return false
            } else if (purchasePrice.isEmpty()) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_purchase_price))
                return false
            } else if (purchasePrice.isNotEmpty() && purchasePrice.equals("0")) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_purchase_price_))
                return false
            } else if (mrpPrice.isEmpty() && statusInventory.equals("NEWBATCH")) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_mrp))
                return false
            } else if (mrpPrice.isNotEmpty() && statusInventory.equals("NEWBATCH") && mrpPrice.equals(
                    "0"
                )
            ) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_error_mrp))
                return false
            } else if (oldmrpPrice.isEmpty() && statusInventory.equals("MRP Change Request")) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_old_mrp))
                return false
            } else if (oldmrpPrice.isNotEmpty() && statusInventory.equals("MRP Change Request") && oldmrpPrice.equals(
                    "0"
                )
            ) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_error_old_mrp_))
                return false
            } else if (purchasePrice.isNotEmpty()) {
                if (statusInventory.equals("MRP Change Request")) {
                    if (newMrpPrice.isEmpty()) {
                        showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_new_mrp))
                        return false
                    } else if (oldmrpPrice.toDouble() < purchasePrice.toDouble() || newMrpPrice.toDouble() < purchasePrice.toDouble()) {
                        showErrorMsg(context?.resources?.getString(R.string.err_msg_purchace_price_old_new))
                        return false
                    } else if (!newMrpPrice.isEmpty() && !oldmrpPrice.isEmpty() && newMrpPrice.toDouble() == oldmrpPrice.toDouble()) {
                        showErrorMsg(context?.resources?.getString(R.string.err_msg_old_new_price_should_not_same))
                        return false
                    }
                } else if (statusInventory.equals("NEWBATCH")) {
                    if (mrpPrice.toDouble() < purchasePrice.toDouble()) {
                        showErrorMsg(context?.resources?.getString(R.string.err_msg_purchace_price_diff))
                        return false
                    }
                } else {
                    if (purchasePrice.toDouble() > mrpPrice.toDouble()) {
                        showErrorMsg(context?.resources?.getString(R.string.err_msg_price_diff))
                        return false
                    }
                }
            }
            if (frontImageFile == null) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_upload_front_image))
                return false
            }
            if (backImageFile == null) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_upload_back_image))
                return false
            }

        } else if (selectedCategory.name.equals("POS") && selectedSubCategory.name.equals("Credit Card(CC) Bill") && selectedReasonDto.code.equals(
                "asb_not_completed"
            )
        ) {
            if (viewBinding.transactionDetailsLayout.tidEdit.text.toString().trim().isEmpty()) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_select_TID))
                return false
            } else if (viewBinding.transactionDetailsLayout.billNumberEdit.text.toString().trim()
                    .isEmpty()
            ) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_bill_number))
                return false
            } else if (viewBinding.transactionDetailsLayout.transactionIdEdit.text.toString().trim()
                    .isEmpty()
            ) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_trn_id))
                return false
            } else if (viewBinding.transactionDetailsLayout.approvalCodeEdit.text.toString().trim()
                    .isEmpty()
            ) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_approval_code))
                return false
            } else if (viewBinding.transactionDetailsLayout.billAmountEdit.text.toString().trim()
                    .isEmpty()
            ) {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_bill_amount))
                return false
            } else if (fileArrayList.isEmpty()) {
                showErrorMsg(context?.resources?.getString(R.string.error_upload_image_limit))
                return false
            }
        }
        if (description.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_description))
            return false
        }

        return true
    }


    private fun showErrorMsg(errMsg: String?) {
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
    }

    fun openDateDialog() {
        CalendarDialog().let {
            it.setTargetFragment(this, 1)
            it.show(requireFragmentManager().beginTransaction(), "")
        }
    }


    //new Changed code for Nrew Response.......
    override fun selectDepartment(departmentDto: ReasonmasterV2Response.Department) {
        viewBinding.inventoryMessageForCamera.visibility = View.GONE
        viewBinding.date.visibility = View.VISIBLE
        viewBinding.description.visibility = View.VISIBLE
        viewBinding.newBatchLayout.visibility = View.GONE
        departmentId = departmentDto.uid
        statusInventory = "NOTBATCH"
        viewBinding.selectDepartment.setText(departmentDto.name)
        categoryListSelected.clear()
        subCategoryListSelected.clear()
        viewBinding.selectCategory.setText("")
        viewBinding.selectSubCategory.setText("")
        viewBinding.selectRemarks.setText("")
        clearTransactionCCView()

        deptuid = departmentDto.uid
        deptCode = departmentDto.code
//        if(departmentDto.code.equals("IN")) {
//            viewBinding.captureUploadLayout.visibility = View.GONE
//        }else{
//            viewBinding.captureUploadLayout.visibility = View.VISIBLE
//        }
        //Ticket status Api calling function................

        viewModel.getTicketstatus(Preferences.getSiteId(), deptuid)


        var cateorylist = departmentDto.uid?.let { viewModel.getCategoriesfromReasons(it) }
        reasoncategoryListSelected.clear()
        if (cateorylist != null) {
            if (cateorylist.size != 0) {
                cateorylist.map { reasoncategoryListSelected.add(it) }
                viewBinding.selectCategoryText.visibility = View.VISIBLE
                viewBinding.selectSubCategoryText.visibility = View.GONE
                viewBinding.selectRemarksText.visibility = View.GONE
            } else {
                viewBinding.selectCategoryText.visibility = View.GONE
                viewBinding.selectSubCategoryText.visibility = View.GONE
                viewBinding.selectRemarksText.visibility = View.GONE

            }
        }
    }


    //select category from Reasons List.......
    override fun selectCategory(departmentDto: ReasonmasterV2Response.TicketCategory) {
        viewBinding.selectCategory.setText(departmentDto.name)
        viewBinding.selectSubCategoryText.visibility = View.GONE
        viewBinding.selectRemarksText.visibility = View.GONE
        viewBinding.titleName.text = resources.getString(R.string.label_upload_image)
        viewBinding.transactionDetailsLayout.transactionDetails.visibility = View.GONE
        subCategoryListSelected.clear()
        if (departmentDto.name.equals("New Batch")) {
            statusInventory = "NEWBATCH"
            viewBinding.description.visibility = View.VISIBLE
            viewBinding.newBatchLayout.visibility = View.VISIBLE
            viewBinding.date.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.GONE
            viewBinding.mrp.visibility = View.VISIBLE
            viewBinding.oldMrp.visibility = View.GONE
            viewBinding.newMrpInputLayout.visibility = View.GONE
            viewBinding.captureUploadLayout.visibility = View.GONE
        } else if (departmentDto.name.equals("MRP Change Request")) {
            statusInventory = "MRP Change Request"
            viewBinding.description.visibility = View.VISIBLE
            viewBinding.newBatchLayout.visibility = View.VISIBLE
            viewBinding.date.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.GONE
            viewBinding.oldMrp.visibility = View.VISIBLE
            viewBinding.mrp.visibility = View.GONE
            viewBinding.newMrpInputLayout.visibility = View.VISIBLE
            viewBinding.captureUploadLayout.visibility = View.GONE
        } else {
            for (i in maintanceArrayList.indices) {
                if (maintanceArrayList[i].equals(departmentDto.name)) {
                    statusInventory = "MAINTENANCE"
                    break
                } else {
                    statusInventory = "NOTBATCH"
                }
            }
            viewBinding.date.visibility = View.VISIBLE
            viewBinding.newBatchLayout.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.GONE
            viewBinding.description.visibility = View.VISIBLE
            viewBinding.captureUploadLayout.visibility = View.VISIBLE
        }
        viewBinding.selectSubCategory.setText("")
        viewBinding.selectRemarks.setText("")
        clearTransactionCCView()
        reasonssubCategoryListSelected.clear()
        reasonsListSelected.clear()
        categoryuid = departmentDto.uid
        selectedCategory = departmentDto
        var subcateorylist = departmentDto.uid?.let { viewModel.getSubCategoriesfromReasons(it) }
        if (subcateorylist != null) {
            subcateorylist.map {
                reasonssubCategoryListSelected.add(it)
            }
        }
        if (subcateorylist != null) {
            if (subcateorylist.size != 0) {
                viewBinding.selectSubCategoryText.visibility = View.VISIBLE
                viewBinding.selectRemarksText.visibility = View.VISIBLE
            } else {
                viewBinding.selectSubCategoryText.visibility = View.GONE
                viewBinding.selectRemarksText.visibility = View.GONE
            }

        }
    }

    override fun selectSubCategory(departmentDto: ReasonmasterV2Response.TicketSubCategory) {
        subcategoryuid = departmentDto.uid
        selectedSubCategory = departmentDto
        var reasonlist = departmentDto.uid?.let { viewModel.getreasonlist(it) }
        reasonsListSelected.clear()
        if (reasonlist != null) {
            if (reasonlist.size != 0) {
                reasonlist.map { reasonsListSelected.add(it) }
                viewBinding.selectRemarksText.visibility = View.VISIBLE
            } else {
                viewBinding.selectRemarksText.visibility = View.GONE
            }
        }
        viewBinding.selectSubCategory.setText(departmentDto.name)
        viewBinding.selectRemarks.setText("")
        clearTransactionCCView()
    }

    override fun selectReasons(departmentDto: ReasonmasterV2Response.Row) {
        viewBinding.selectRemarks.setText(departmentDto.name)
        reasonuid = departmentDto.uid
        reasonSla = departmentDto.reason_sla
        selectedReasonDto = departmentDto
        if (selectedCategory.name.equals("POS") && selectedSubCategory.name.equals("Credit Card(CC) Bill") && departmentDto.code.equals(
                "asb_not_completed"
            )
        ) {
            statusInventory = "POS"
            viewBinding.transactionDetailsLayout.transactionDetails.visibility = View.VISIBLE
            viewBinding.titleName.text = "${resources.getString(R.string.label_upload_image)} *"

        }
        if (departmentDto != null && departmentDto!!.allow_duplicate_st_creation != null && departmentDto!!.allow_duplicate_st_creation!!.uid != null && departmentDto!!.allow_duplicate_st_creation!!.uid!!.equals(
                "No"
            )
        ) {
            if (NetworkUtils.isNetworkConnected(requireContext())) {
                showLoading()
                viewModel.siteTicketbyReason(
                    siteUid,
                    departmentDto!!.uid!!,
                    departmentDto!!.allow_duplicate_st_creation!!.uid!!
                )
            } else {
                Toast.makeText(
                    requireContext(), "Check your internet connection", Toast.LENGTH_SHORT
                ).show()
            }
        }

        /*if (reasonmasterV2Response != null
            && reasonmasterV2Response!!.data != null
            && reasonmasterV2Response!!.data!!.)*/
    }


    private fun clearTransactionCCView() {
        imagesArrayListSend.clear()
        NewimagesArrayListSend.clear()
        viewBinding.transactionDetailsLayout.transactionIdEdit.setText("")
        viewBinding.transactionDetailsLayout.tidEdit.setText("")
        viewBinding.transactionDetailsLayout.billAmountEdit.setText("")
        viewBinding.transactionDetailsLayout.approvalCodeEdit.setText("")
        viewBinding.transactionDetailsLayout.billNumberEdit.setText("")
        viewBinding.titleName.text = resources.getString(R.string.label_upload_image)
        viewBinding.transactionDetailsLayout.transactionDetails.visibility = View.GONE
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        viewBinding.dateOfProblem.setText(showingDate)
    }

    fun RefreshView() {
        statusInventory = "NOTBATCH"
        viewBinding.selectDepartment.setText("")
        viewBinding.selectCategory.setText("")
        viewBinding.selectSubCategory.setText("")
        viewBinding.selectRemarks.setText("")
        viewBinding.dateOfProblem.setText("")
        viewBinding.descriptionText.setText("")
        viewBinding.mrpEditText.setText("")
        viewBinding.purchasePriseEdit.setText("")
        viewBinding.batchText.setText("")
        viewBinding.articleCode.setText("")
        viewBinding.expireDateExpire.setText("")
        viewBinding.articleCode.setText("")
        viewBinding.barcodeEdt.setText("")
        viewBinding.expireDateExpire.setText("")
        viewBinding.purchasePriseEdit.setText("")
        viewBinding.oldmrpEditText.setText("")
        viewBinding.newMrpEdit.setText("")
        fileArrayList.clear()
        InventoryfileArrayList.clear()
        imagesArrayListSend.clear()
        adapter.notifyAdapter(fileArrayList)
        frontImageFile = null
        backImageFile = null
        otherImageFile = null


        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        var formattedDate = df.format(c)
        viewBinding.dateOfProblem.setText(formattedDate)

        viewBinding.newBatchLayout.visibility = View.GONE
        viewBinding.departmentLayout.visibility = View.VISIBLE
        viewBinding.selectDepartmentText.visibility = View.VISIBLE
        viewBinding.selectCategoryText.visibility = View.GONE
        viewBinding.selectSubCategoryText.visibility = View.GONE
        viewBinding.selectRemarksText.visibility = View.GONE
        viewBinding.date.visibility = View.VISIBLE
        viewBinding.description.visibility = View.VISIBLE
        viewBinding.productImageView.frontImageDelete.visibility = View.GONE
        viewBinding.productImageView.backImageDelete.visibility = View.GONE
        viewBinding.productImageView.otherImageDelete.visibility = View.GONE
        viewBinding.productImageView.productFrontImagePreview.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_capture_image
            )
        )
        viewBinding.productImageView.productBackImagePreview.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_capture_image
            )
        )
        viewBinding.productImageView.productOtherImagePreview.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_capture_image
            )
        )
        clearTransactionCCView()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        context?.resources?.getString(R.string.label_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            REQUEST_CODE_PRODUCT_FRONT_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraForFrontImage(1)
                } else {
                    Toast.makeText(
                        requireContext(),
                        context?.resources?.getString(R.string.label_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                requireContext(), requireContext().packageName + ".provider", imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    private fun openCameraForFrontImage(imgType: Int) {
        if (!checkPermission()) {
            askPermissions(imgType)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (imgType == 1) {
                frontImageFile =
                    File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(frontImageFile))
                } else {
                    val photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName + ".provider",
                        frontImageFile!!
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
            } else if (imgType == 2) {
                backImageFile = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(backImageFile))
                } else {
                    val photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName + ".provider",
                        backImageFile!!
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
            } else if (imgType == 3) {
                otherImageFile =
                    File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(otherImageFile))
                } else {
                    val photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName + ".provider",
                        otherImageFile!!
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, imgType)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dialog.dismiss()
        if (requestCode == 1 || requestCode == 2 || requestCode == 3 || requestCode == Config.REQUEST_CODE_GALLERY) {
            if (requestCode == Config.REQUEST_CODE_GALLERY) {
                dialog.dismiss()
                if (data!!.clipData != null) {
                    val images = data!!.clipData
                    if (images != null) {
                        if (images!!.itemCount <= 3) {
                            if (!notFrontView) {
                                if ((imagesFilledCount == 0 && images.itemCount == 3)
                                    || (imagesFilledCount == 1 && images.itemCount == 2)
                                    || (imagesFilledCount == 2 && images.itemCount == 1)
                                ) {
                                    for (i in 0 until images.itemCount) {
                                        var imagePath =
                                            getRealPathFromURI(
                                                requireContext(),
                                                images.getItemAt(i).uri
                                            )
                                        var imageFileGallery: File? = File(imagePath)
                                        val imageBase64 =
                                            encodeImage(imageFileGallery!!.absolutePath)
                                        val resizedImage =
                                            Resizer(requireContext()).setTargetLength(1080)
                                                .setQuality(100)
                                                .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                                                .setOutputDirPath(
                                                    ViswamApp.Companion.context.cacheDir.toString()
                                                )

                                                .setSourceImage(imageFileGallery).resizedFile

//
                                        if (frontImageFile == null) {
                                            viewBinding.productImageView.productFrontImagePreview.setImageURI(
                                                Uri.fromFile(
                                                    resizedImage
                                                )
                                            )
                                            viewBinding.productImageView.frontImageDelete.visibility =
                                                View.VISIBLE
                                            frontImageFile = resizedImage
                                            imagesFilledCount++
                                        } else if (backImageFile == null) {
                                            viewBinding.productImageView.productBackImagePreview.setImageURI(
                                                Uri.fromFile(
                                                    resizedImage
                                                )
                                            )
                                            viewBinding.productImageView.backImageDelete.visibility =
                                                View.VISIBLE
                                            backImageFile = resizedImage
                                            imagesFilledCount++
                                        } else if (otherImageFile == null) {
                                            viewBinding.productImageView.productOtherImagePreview.setImageURI(
                                                Uri.fromFile(
                                                    resizedImage
                                                )
                                            )
                                            viewBinding.productImageView.otherImageDelete.visibility =
                                                View.VISIBLE
                                            otherImageFile = resizedImage
                                            imagesFilledCount++
                                        }

                                    }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "You are allowed to upload only three images",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                if ((fileArrayList.size == 0 && images.itemCount == 2)
                                    || (fileArrayList.size == 1 && images.itemCount == 1)
                                ) {
                                    for (i in 0 until images.itemCount) {
                                        var imagePath =
                                            getRealPathFromURI(
                                                requireContext(),
                                                images.getItemAt(i).uri
                                            )
                                        var imageFileGallery: File? = File(imagePath)
                                        val imageBase64 =
                                            encodeImage(imageFileGallery!!.absolutePath)
                                        val resizedImage =
                                            Resizer(requireContext()).setTargetLength(1080)
                                                .setQuality(100)
                                                .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                                                .setOutputDirPath(
                                                    ViswamApp.Companion.context.cacheDir.toString()
                                                )

                                                .setSourceImage(imageFileGallery).resizedFile
                                        fileArrayList.add(ImageDataDto(resizedImage, ""))
//           imagesFilledCount++
                                        adapter.notifyAdapter(fileArrayList)
                                    }

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "You are allowed to upload only two images",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }


                        } else {
                            if(!notFrontView){
                                Toast.makeText(
                                    requireContext(),
                                    "You are allowed to upload only three images",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                Toast.makeText(
                                    requireContext(),
                                    "You are allowed to upload only two images",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                } else {
                    dialog.dismiss()
                    val uri = data.data
                    var imagePath = getRealPathFromURI(requireContext(), uri!!)
                    var imageFileGallery: File? = File(imagePath)
                    val resizedImage =
                        Resizer(requireContext()).setTargetLength(1080).setQuality(100)
                            .setOutputFormat("JPG")
//                .setOutputFilename(fileNameForCompressedImage)
                            .setOutputDirPath(
                                ViswamApp.Companion.context.cacheDir.toString()
                            )

                            .setSourceImage(imageFileGallery).resizedFile
                    if (!notFrontView) {
                        if (clickedCamera == 1 && frontImageFile == null) {
                            viewBinding.productImageView.productFrontImagePreview.setImageURI(
                                Uri.fromFile(
                                    resizedImage
                                )
                            )
                            viewBinding.productImageView.frontImageDelete.visibility = View.VISIBLE
                            frontImageFile = resizedImage
                            imagesFilledCount++
                        } else if (clickedCamera == 2 && backImageFile == null) {
                            viewBinding.productImageView.productBackImagePreview.setImageURI(
                                Uri.fromFile(
                                    resizedImage
                                )
                            )
                            viewBinding.productImageView.backImageDelete.visibility = View.VISIBLE
                            backImageFile = resizedImage
                            imagesFilledCount++
                        } else if (clickedCamera == 3 && otherImageFile == null) {
                            viewBinding.productImageView.productOtherImagePreview.setImageURI(
                                Uri.fromFile(
                                    resizedImage
                                )
                            )
                            viewBinding.productImageView.otherImageDelete.visibility = View.VISIBLE
                            otherImageFile = resizedImage
                            imagesFilledCount++
                        }
                    } else {
                        if (fileArrayList.size <= 1) {
                            fileArrayList.add(ImageDataDto(resizedImage, ""))
//           imagesFilledCount++
                            adapter.notifyAdapter(fileArrayList)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "You are allowed to upload only two images",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }


                }

            } else if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    1 -> {
                        viewBinding.productImageView.productFrontImagePreview.setImageURI(
                            Uri.fromFile(
                                frontImageFile
                            )
                        )
                        imagesFilledCount++
                        viewBinding.productImageView.frontImageDelete.visibility = View.VISIBLE
                        frontImageFile = compresImageSize(frontImageFile!!)
                    }

                    2 -> {
                        viewBinding.productImageView.productBackImagePreview.setImageURI(
                            Uri.fromFile(
                                backImageFile
                            )
                        )
                        imagesFilledCount++
                        viewBinding.productImageView.backImageDelete.visibility = View.VISIBLE
                        backImageFile = compresImageSize(backImageFile!!)
                    }

                    3 -> {
                        viewBinding.productImageView.productOtherImagePreview.setImageURI(
                            Uri.fromFile(
                                otherImageFile
                            )
                        )
                        imagesFilledCount++
                        viewBinding.productImageView.otherImageDelete.visibility = View.VISIBLE
                        otherImageFile = compresImageSize(otherImageFile!!)
                    }
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                when (requestCode) {
                    1 -> {
                        frontImageFile = null
                    }

                    2 -> {
                        backImageFile = null
                    }

                    3 -> {
                        otherImageFile = null
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
            fileArrayList.add(ImageDataDto(compresImageSize(imageFromCameraFile!!), ""))
//           imagesFilledCount++
            adapter.notifyAdapter(fileArrayList)
        }
    }

    private fun compresImageSize(imageFromCameraFile: File): File {
        val resizedImage =
            Resizer(requireContext()).setTargetLength(1080).setQuality(100).setOutputFormat("JPG")
                .setOutputDirPath(ViswamApp.Companion.context.cacheDir.toString())
                .setSourceImage(imageFromCameraFile).resizedFile
        return resizedImage
    }

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
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
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
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context, uri, null, null
                )
            }

            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(
                uri, projection, null, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
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
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

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

    override fun deleteImage(position: Int) {
        imagesArrayListSend.clear()
        NewimagesArrayListSend.clear()
        fileArrayList.removeAt(position)
        adapter.deleteImage(position)
    }

    @SuppressLint("ResourceType")
    override fun confirmsavetheticket() {
        RefreshView()
        if (employeeDetailsResponse != null

            && employeeDetailsResponse!!.data != null && employeeDetailsResponse!!.data!!.role != null && employeeDetailsResponse!!.data!!.role!!.code.equals(
                "store_supervisor"
            )
        ) {
            if (selectedSiteId != null) {
                showLoading()
                var updateUserDefaultSiteRequest = UpdateUserDefaultSiteRequest()
                updateUserDefaultSiteRequest.empId = Preferences.getValidatedEmpId()
                updateUserDefaultSiteRequest.site = selectedSiteId!!.site
                viewModel.updateDefaultSiteIdApiCall(updateUserDefaultSiteRequest)
            }
        }
        viewModel.updateUserDefaultSiteResponseMutable.observe(viewLifecycleOwner) {
            hideLoading()
            var empDetailsResponses = Preferences.getEmployeeDetailsResponseJson()
            var employeeDetailsResponsess: EmployeeDetailsResponse? = null
            try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                employeeDetailsResponsess = gson.fromJson<EmployeeDetailsResponse>(
                    empDetailsResponses, EmployeeDetailsResponse::class.java
                )
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
            employeeDetailsResponsess!!.data!!.site!!.site = selectedSiteId!!.site
            employeeDetailsResponsess.data!!.site!!.storeName = selectedSiteId!!.store_name
            employeeDetailsResponsess.data!!.site!!.dcCode!!.name = selectedSiteId!!.dc_code!!.name
            employeeDetailsResponsess.data!!.site!!.state!!.code = selectedSiteId!!.sTATEID
            employeeDetailsResponsess.data!!.site!!.dcCode!!.code = selectedSiteId!!.dc_code!!.code
            Preferences.storeEmployeeDetailsResponseJson(Gson().toJson(employeeDetailsResponsess))
            var empDetailsResponsezz = Preferences.getEmployeeDetailsResponseJson()
            try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(
                    empDetailsResponsezz, EmployeeDetailsResponse::class.java
                )
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
            Preferences.saveSiteId(selectedSiteId!!.site!!)
            RegistrationRepo.saveStoreInfo(selectedSiteId!!)
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun selectSite(departmentDto: StoreListItem) {
        if (isSuperAdmin) {
            onSuccessUserWithSiteID(departmentDto)
        } else {
            ConfirmSiteDialog().apply {
                arguments = ConfirmSiteDialog().generateParsedData(departmentDto)
            }.show(childFragmentManager, "")
        }
    }

    override fun selectSubCategory(articleData: FetchItemModel.Rows) {
        clearArticleEditText()
        viewModel.inventoryCategotyItem = articleData
        viewBinding.articleCode.setText(articleData.artCodeName)
    }

    override fun selectedFutureDateTo(dateSelected: String, showingDate: String) {
        viewBinding.expireDateExpire.setText(showingDate)
    }

    fun clearArticleEditText() {
        viewBinding.articleCode.setText("")
        viewBinding.batchText.setText("")
        viewBinding.mrpEditText.setText("")
        viewBinding.purchasePriseEdit.setText("")
        viewBinding.expireDateExpire.setText("")
        fileArrayList.clear()
        imagesArrayListSend.clear()
        adapter.notifyAdapter(fileArrayList)
    }

    private var selectedSiteId: StoreListItem? = null
    override fun confirmSite(departmentDto: StoreListItem) {
        showLoading()
        if (employeeDetailsResponse != null && employeeDetailsResponse!!.data != null && employeeDetailsResponse!!.data!!.role != null && employeeDetailsResponse!!.data!!.role!!.code.equals(
                "store_supervisor"
            )
        ) {
            selectedSiteId = departmentDto

            viewModel.registerUserWithSiteID(
                UserSiteIDRegReqModel(
                    userData.EMPID, departmentDto.site
                ), departmentDto
            )
        } else {
            Preferences.saveSiteId(departmentDto.site!!)
            RegistrationRepo.saveStoreInfo(departmentDto)
            viewModel.registerUserWithSiteID(
                UserSiteIDRegReqModel(
                    userData.EMPID, departmentDto.site
                ), departmentDto
            )

            var storedata = StoreData(
                departmentDto.site,
                departmentDto.store_name,
                departmentDto.dc_code?.name,
                departmentDto.site,
                departmentDto.dc_code?.code
            )
            LoginRepo.saveStoreData(storedata)
        }
    }

    override fun cancelledSite() {

    }

    @SuppressLint("SetTextI18n")
    fun onSuccessUserWithSiteID(selectedStoreItem: StoreListItem) {
        siteUid = selectedStoreItem!!.uid!!
        hideLoading()
        RefreshView()
        if (selectedStoreItem.site.isNullOrEmpty()) {
            viewBinding.departmentLayout.visibility = View.GONE
            viewBinding.newBatchLayout.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.GONE
        } else {
            viewBinding.newBatchLayout.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.GONE
            Preferences.saveSiteId(selectedStoreItem.site)
            RegistrationRepo.saveStoreInfo(selectedStoreItem)
//            if (!isSuperAdmin) {
//                showLoading()
//            }
//            viewModel.getRemarksMasterList()
            viewModel.getSelectedStoreDetails(selectedStoreItem)
            viewBinding.departmentLayout.visibility = View.VISIBLE
            val store = LoginDetails.StoreData(
                selectedStoreItem.site.toString(),
                selectedStoreItem.store_name.toString(),
                selectedStoreItem.dc_code?.name.toString(),
                selectedStoreItem.site.toString(),
                selectedStoreItem.dc_code?.code.toString(),
                true
            )
            val repo = LoginRepo.getProfile()
            repo?.STOREDETAILS?.clear()
            repo?.STOREDETAILS?.add(store)
            if (repo != null) {
                LoginRepo.saveProfile(repo, LoginRepo.getPassword())
                Log.e("Saved prif data", Gson().toJson(repo))
                viewBinding.siteIdSelect.setText(selectedStoreItem.site + " - " + selectedStoreItem.store_name)
                viewBinding.branchName.setText(selectedStoreItem.dc_code?.code + " - " + selectedStoreItem.dc_code?.name)
            }

        }
    }

    lateinit var posTid: Row
    override fun onSelectedPOSTid(data: Row) {
        posTid = data
        viewBinding.transactionDetailsLayout.tidEdit.setText(posTid.tid)
    }

    private fun submitClick() {
        val description = viewBinding.descriptionText.text.toString().trim()
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(Date())
        val problemDate =
            Utils.dateofoccurence(viewBinding.dateOfProblem.text.toString()) + " " + currentTime
        showLoading()
//        val storeId: String = if (employeeDetailsResponse != null
//            && employeeDetailsResponse!!.data != null
//            && employeeDetailsResponse!!.data!!.role != null
//            && employeeDetailsResponse!!.data!!.role!!.code.equals("store_supervisor")
//        ) {
//            employeeDetailsResponse!!.data!!.site!!.site.toString()
//        } else {
//            Preferences.getSiteId()
//        }
        val storeId = Preferences.getSiteId()
        if (statusInventory.equals("MRP Change Request") || statusInventory.equals("NEWBATCH")) {
            InventoryfileArrayList.clear()
            if (frontImageFile != null) InventoryfileArrayList.add(
                ImageDataDto(
                    frontImageFile!!, ""
                )
            )
            if (backImageFile != null) InventoryfileArrayList.add(ImageDataDto(backImageFile!!, ""))
            if (otherImageFile != null) InventoryfileArrayList.add(
                ImageDataDto(
                    otherImageFile!!, ""
                )
            )

            var cmsFileUploadModelList = ArrayList<CmsFileUploadModel>()
            for (i in InventoryfileArrayList) {
                var cmsFileUploadModel = CmsFileUploadModel()
                cmsFileUploadModel.file = i.file
                cmsFileUploadModelList.add(cmsFileUploadModel)
            }

            CmsFileUpload().uploadFiles(
                requireContext(),
                this@RegistrationFragment,
                cmsFileUploadModelList,
                statusInventory!!
            )
            //viewModel.connectToAzure(InventoryfileArrayList, statusInventory!!)
        } else {
            if (fileArrayList.isEmpty()) {
                val ticketIt: Ticket_it? =
                    if (selectedCategory.name.equals("POS") && selectedSubCategory.name.equals("Credit Card(CC) Bill") && selectedReasonDto.code.equals(
                            "asb_not_completed"
                        )
                    ) {
                        Ticket_it(
                            posTid,
                            viewBinding.transactionDetailsLayout.billNumberEdit.text.toString(),
                            viewBinding.transactionDetailsLayout.transactionIdEdit.text.toString(),
                            viewBinding.transactionDetailsLayout.approvalCodeEdit.text.toString(),
                            viewBinding.transactionDetailsLayout.billAmountEdit.text.toString()
                                .toDouble()
                        )
                    } else {
                        null
                    }
                callSubmitNewComplaintRegApi(description, problemDate, storeId, ticketIt)
            } else {
                var cmsFileUploadModelList = ArrayList<CmsFileUploadModel>()
                for (i in fileArrayList) {
                    var cmsFileUploadModel = CmsFileUploadModel()
                    cmsFileUploadModel.file = i.file
                    cmsFileUploadModelList.add(cmsFileUploadModel)
                }

                CmsFileUpload().uploadFiles(
                    requireContext(),
                    this@RegistrationFragment,
                    cmsFileUploadModelList,
                    statusInventory!!
                )

                //viewModel.connectToAzure(fileArrayList, statusInventory!!)
            }
        }
    }

    private fun saveTicketApi(
        cmsFileUploadModelList: List<CmsFileUploadModel>?,
        tag: String,
    ) {//cmsCommand: CmsCommand.ImageIsUploadedInAzur
//        val storeId: String = if (employeeDetailsResponse != null
//            && employeeDetailsResponse!!.data != null
//            && employeeDetailsResponse!!.data!!.role != null
//            && employeeDetailsResponse!!.data!!.role!!.code.equals("store_supervisor")
//        ) {
//            employeeDetailsResponse!!.data!!.site!!.site.toString()
//        } else {
        val storeId = Preferences.getSiteId()
//        }
        val description = viewBinding.descriptionText.text.toString().trim()
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(Date())
        val problemDate =
            Utils.dateofoccurence(viewBinding.dateOfProblem.text.toString()) + " " + currentTime
        var newPrice = 0.0
        var oldMrp = 0.0
        var mrp = 0.0
        if (tag.equals("MRP Change Request")) {
            newPrice = viewBinding.newMrpEdit.text.toString().toDouble()
            oldMrp = viewBinding.oldmrpEditText.text.toString().toDouble()
        } else if (tag.equals("NEWBATCH")) {
            mrp = viewBinding.mrpEditText.text.toString().toDouble()
        }
        if (tag.equals("MRP Change Request") || tag.equals("NEWBATCH")) {
            val codeBatch: RequestSaveUpdateComplaintRegistration.CodeBatch =
                if (tag.equals("MRP Change Request")) {
                    RequestSaveUpdateComplaintRegistration.CodeBatch("mrp_change", "MRP Change")
                } else {
                    RequestSaveUpdateComplaintRegistration.CodeBatch("new_Batch", "New Batch")
                }
            ticketInventoryItems.clear()
            ticketInventoryItems.add(
                RequestSaveUpdateComplaintRegistration.TicketInventoryItem(

                    cmsFileUploadModelList!!.get(0).fileDownloadResponse!!.decryptedUrl,
                    cmsFileUploadModelList!!.get(1).fileDownloadResponse!!.decryptedUrl,
                    if (cmsFileUploadModelList.size > 2) cmsFileUploadModelList.get(2).fileDownloadResponse!!.decryptedUrl else "",
                    viewModel.inventoryCategotyItem,
                    viewModel.inventoryCategotyItem.artcode,
                    viewBinding.batchText.text.toString(),
                    viewBinding.barcodeEdt.text.toString(),
                    Utils.dateofoccurence(viewBinding.expireDateExpire.text.toString()) + " " + currentTime,
                    viewBinding.purchasePriseEdit.text.toString().toDouble(),
                    oldMrp,
                    newPrice,
                    mrp,
                    1
                )
            )

            viewModel.submitTicketInventorySaveUpdate(
                RequestSaveUpdateComplaintRegistration(
                    userData.EMPID,
                    problemDate,
                    description,
                    RequestSaveUpdateComplaintRegistration.Platform(platformUid!!),
                    RequestSaveUpdateComplaintRegistration.Category(categoryuid!!),
                    RequestSaveUpdateComplaintRegistration.Department(deptuid!!, deptCode!!),
                    RequestSaveUpdateComplaintRegistration.Site(
                        viewModel.tisketstatusresponse.value!!.data.uid,
                        storeId,
                        viewModel.tisketstatusresponse.value!!.data.store_name
                    ),
                    RequestSaveUpdateComplaintRegistration.Reason(reasonuid!!, reasonSla),
                    RequestSaveUpdateComplaintRegistration.Subcategory(subcategoryuid!!),
                    RequestSaveUpdateComplaintRegistration.TicketInventory(
                        ticketInventoryItems, null, codeBatch
                    ),
                    RequestSaveUpdateComplaintRegistration.TicketType(
                        "64D9D9BE4A621E9C13A2C73404646655", "store", "store"
                    ),
                    viewModel.tisketstatusresponse.value!!.data.region,
                    viewModel.tisketstatusresponse.value!!.data.cluster,
                    viewModel.tisketstatusresponse.value!!.data.phone_no,
                    viewModel.tisketstatusresponse.value!!.data.executive,
                    viewModel.tisketstatusresponse.value!!.data.manager,
                    viewModel.tisketstatusresponse.value!!.data.region_head,
                )
            )
        } else {
            for (i in cmsFileUploadModelList!!.indices) {
                imagesArrayListSend.add(
                    SubmitNewV2Response.PrescriptionImagesItem(
                        cmsFileUploadModelList.get(i).fileDownloadResponse!!.decryptedUrl
                    )
                )
                NewimagesArrayListSend.add(
                    RequestNewComplaintRegistration.Image(
                        cmsFileUploadModelList.get(i).fileDownloadResponse!!.decryptedUrl
                    )
                )
            }
            val ticketIt: Ticket_it? =
                if (selectedCategory.name.equals("POS") && selectedSubCategory.name.equals("Credit Card(CC) Bill") && selectedReasonDto.code.equals(
                        "asb_not_completed"
                    )
                ) {
                    Ticket_it(
                        posTid,
                        viewBinding.transactionDetailsLayout.billNumberEdit.text.toString(),
                        viewBinding.transactionDetailsLayout.transactionIdEdit.text.toString(),
                        viewBinding.transactionDetailsLayout.approvalCodeEdit.text.toString(),
                        viewBinding.transactionDetailsLayout.billAmountEdit.text.toString()
                            .toDouble()
                    )
                } else {
                    null
                }
            callSubmitNewComplaintRegApi(description, problemDate, storeId, ticketIt)
        }
    }

    /*
        private fun saveTicketApi(
            cmsFileUploadModelList: List<CmsFileUploadModel>?,
            tag: String,
        ) {//cmsCommand: CmsCommand.ImageIsUploadedInAzur
    //        val storeId: String = if (employeeDetailsResponse != null
    //            && employeeDetailsResponse!!.data != null
    //            && employeeDetailsResponse!!.data!!.role != null
    //            && employeeDetailsResponse!!.data!!.role!!.code.equals("store_supervisor")
    //        ) {
    //            employeeDetailsResponse!!.data!!.site!!.site.toString()
    //        } else {
            val storeId = Preferences.getSiteId()
    //        }
            val description = viewBinding.descriptionText.text.toString().trim()
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val problemDate =
                Utils.dateofoccurence(viewBinding.dateOfProblem.text.toString()) + " " + currentTime
            var newPrice = 0.0
            var oldMrp = 0.0
            var mrp = 0.0
            if (cmsCommand.tag.equals("MRP Change Request")) {
                newPrice = viewBinding.newMrpEdit.text.toString().toDouble()
                oldMrp = viewBinding.oldmrpEditText.text.toString().toDouble()
            } else if (cmsCommand.tag.equals("NEWBATCH")) {
                mrp = viewBinding.mrpEditText.text.toString().toDouble()
            }
            if (cmsCommand.tag.equals("MRP Change Request") || cmsCommand.tag.equals("NEWBATCH")) {
                val codeBatch: RequestSaveUpdateComplaintRegistration.CodeBatch =
                    if (cmsCommand.tag.equals("MRP Change Request")) {
                        RequestSaveUpdateComplaintRegistration.CodeBatch("mrp_change", "MRP Change")
                    } else {
                        RequestSaveUpdateComplaintRegistration.CodeBatch("new_Batch", "New Batch")
                    }
                ticketInventoryItems.clear()
                ticketInventoryItems.add(
                    RequestSaveUpdateComplaintRegistration.TicketInventoryItem(
                        cmsCommand.filePath[0].base64Images,
                        cmsCommand.filePath[1].base64Images,
                        if (cmsCommand.filePath.size > 2) cmsCommand.filePath[2].base64Images else "",
                        viewModel.inventoryCategotyItem,
                        viewModel.inventoryCategotyItem.artcode,
                        viewBinding.batchText.text.toString(),
                        viewBinding.barcodeEdt.text.toString(),
                        Utils.dateofoccurence(viewBinding.expireDateExpire.text.toString()) + " " + currentTime,
                        viewBinding.purchasePriseEdit.text.toString().toDouble(),
                        oldMrp,
                        newPrice,
                        mrp,
                        1
                    )
                )

                viewModel.submitTicketInventorySaveUpdate(
                    RequestSaveUpdateComplaintRegistration(
                        userData.EMPID,
                        problemDate,
                        description,
                        RequestSaveUpdateComplaintRegistration.Platform(platformUid!!),
                        RequestSaveUpdateComplaintRegistration.Category(categoryuid!!),
                        RequestSaveUpdateComplaintRegistration.Department(deptuid!!, deptCode!!),
                        RequestSaveUpdateComplaintRegistration.Site(
                            viewModel.tisketstatusresponse.value!!.data.uid,
                            storeId,
                            viewModel.tisketstatusresponse.value!!.data.store_name
                        ),
                        RequestSaveUpdateComplaintRegistration.Reason(reasonuid!!, reasonSla),
                        RequestSaveUpdateComplaintRegistration.Subcategory(subcategoryuid!!),
                        RequestSaveUpdateComplaintRegistration.TicketInventory(
                            ticketInventoryItems, null, codeBatch
                        ),
                        RequestSaveUpdateComplaintRegistration.TicketType(
                            "64D9D9BE4A621E9C13A2C73404646655", "store", "store"
                        ),
                        viewModel.tisketstatusresponse.value!!.data.region,
                        viewModel.tisketstatusresponse.value!!.data.cluster,
                        viewModel.tisketstatusresponse.value!!.data.phone_no,
                        viewModel.tisketstatusresponse.value!!.data.executive,
                        viewModel.tisketstatusresponse.value!!.data.manager,
                        viewModel.tisketstatusresponse.value!!.data.region_head,
                    )
                )
            } else {
                for (i in cmsCommand.filePath.indices) {
                    imagesArrayListSend.add(SubmitNewV2Response.PrescriptionImagesItem(cmsCommand.filePath[i].base64Images))
                    NewimagesArrayListSend.add(RequestNewComplaintRegistration.Image(cmsCommand.filePath[i].base64Images))
                }
                val ticketIt: Ticket_it? =
                    if (selectedCategory.name.equals("POS") && selectedSubCategory.name.equals("Credit Card(CC) Bill") && selectedReasonDto.code.equals(
                            "asb_not_completed"
                        )
                    ) {
                        Ticket_it(
                            posTid,
                            viewBinding.transactionDetailsLayout.billNumberEdit.text.toString(),
                            viewBinding.transactionDetailsLayout.transactionIdEdit.text.toString(),
                            viewBinding.transactionDetailsLayout.approvalCodeEdit.text.toString(),
                            viewBinding.transactionDetailsLayout.billAmountEdit.text.toString()
                                .toDouble()
                        )
                    } else {
                        null
                    }
                callSubmitNewComplaintRegApi(description, problemDate, storeId, ticketIt)
            }
        }
    */
    fun siteTicketbyReasonSuccess() {
        viewModel.siteTicketbyReasonResponseLive.observe(viewLifecycleOwner) {
            hideLoading()
            if (it.data!!.listData!!.records!!.toInt() > 0) {
                var allowDuplicateStCreationDialog = Dialog(requireContext())
                var dialogAllowDuplicateStCreationBinding =
                    DataBindingUtil.inflate<DialogAllowDuplicateStCreationBinding>(
                        LayoutInflater.from(requireContext()),
                        R.layout.dialog_allow_duplicate_st_creation,
                        null,
                        false
                    )
                allowDuplicateStCreationDialog.setContentView(dialogAllowDuplicateStCreationBinding.root)
                allowDuplicateStCreationDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                allowDuplicateStCreationDialog.setCancelable(false)
                if (it.data != null && it.data!!.listData != null && it.data!!.listData!!.rows != null && it.data!!.listData!!.rows!!.size > 0) {
                    var allowDuplicateStCreationAdapter =
                        AllowDuplicateStCreationAdapter(it.data!!.listData!!.rows!!)
                    var mLayoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    dialogAllowDuplicateStCreationBinding.ticketListRecyclerView.layoutManager =
                        mLayoutManager
                    dialogAllowDuplicateStCreationBinding.ticketListRecyclerView.adapter =
                        allowDuplicateStCreationAdapter
                }
                dialogAllowDuplicateStCreationBinding.dilogaClose.setOnClickListener {
                    allowDuplicateStCreationDialog.dismiss()
                    isBackPressAllow = "GO_BACK"
                    MainActivity.mInstance.onBackPressed()
                }
                allowDuplicateStCreationDialog.show()
            }
        }
    }

    private fun callSubmitNewComplaintRegApi(
        description: String,
        problemDate: String,
        storeId: String,
        ticketIt: Ticket_it?,
    ) {
        viewModel.submitNewcomplaintregApi(
            RequestNewComplaintRegistration(
                userData.EMPID,
                problemDate,
                description,
                RequestNewComplaintRegistration.Platform(platformUid!!),
                RequestNewComplaintRegistration.Category(
                    selectedCategory.uid!!, selectedCategory.code
                ),
                RequestNewComplaintRegistration.Department(deptuid!!, deptCode),
                RequestNewComplaintRegistration.Site(storeId),
                RequestNewComplaintRegistration.Reason(reasonuid!!, reasonSla),
                RequestNewComplaintRegistration.Subcategory(subcategoryuid!!),
                RequestNewComplaintRegistration.ProblemImages(NewimagesArrayListSend.distinct()),
                ticketIt
            )
        )
    }

    override fun onBackPressed(): Boolean {
        if (isBackPressAllow.equals("GO_BACK")) {
            return false
        } else if (viewBinding.selectDepartment.text.toString()
                .isNotEmpty() || viewBinding.descriptionText.text.toString()
                .isNotEmpty() || fileArrayList.isNotEmpty()
        ) {
            return true
        }
        return false
    }

    override fun onFailureUpload(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun allFilesDownloaded(
        cmsFileUploadModelList: List<CmsFileUploadModel>?,
        tag: String,
    ) {
        saveTicketApi(cmsFileUploadModelList, tag)
    }

    override fun allFilesUploaded(cmsfileUploadModelList: List<CmsFileUploadModel>?, tag: String) {

    }

    private fun showOption(num: Int) {
        clickedCamera = num
        if (num == 0) {
            notFrontView = true
        }
        dialog = Dialog(requireContext())
//        val window: Window = myDialog.getWindow()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val chooseImageOptionLayoutBinding =
            DataBindingUtil.inflate<ChooseImageOptionLayoutBinding>(
                LayoutInflater.from(requireContext()),
                R.layout.choose_image_option_layout,
                null,
                false
            )
        dialog.setContentView(chooseImageOptionLayoutBinding.root)
        chooseImageOptionLayoutBinding.cancel.setOnClickListener {
            dialog.dismiss()
        }
        // Open camera
        chooseImageOptionLayoutBinding.camera.setOnClickListener {

            if (num == 0) {
                openCamera()
            } else {
                openCameraForFrontImage(num)
            }
        }
        // Open gallery
        chooseImageOptionLayoutBinding.gallery.setOnClickListener {
            openGallery()
        }

        dialog.setCancelable(false)
        dialog.show()


//        ImagePicker.with(this@ApolloSensingFragment)
//            .crop()
//            .start(Config.REQUEST_CODE_CAMERA)

//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")
//        compressedImageFileName = "${System.currentTimeMillis()}.jpg"
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
//        } else {
//            val photoUri = FileProvider.getUriForFile(
//                ViswamApp.context, ViswamApp.context.packageName + ".provider", imageFile!!
//            )
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"), Config.REQUEST_CODE_GALLERY
        )
    }
}


class ImageRecyclerView(
    var orderData: ArrayList<ImageDataDto>,
    val imageClicklistner: ImageClickListner,
) : SimpleRecyclerView<ViewImageItemBinding, ImageDataDto>(orderData, R.layout.view_image_item) {
    override fun bindItems(
        binding: ViewImageItemBinding,
        items: ImageDataDto,
        position: Int,
    ) {
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
//        orderData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, orderData.size)
    }
}

interface ImageClickListner {
    fun deleteImage(position: Int)
}
