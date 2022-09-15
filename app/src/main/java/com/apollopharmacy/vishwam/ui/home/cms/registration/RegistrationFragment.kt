package com.apollopharmacy.vishwam.ui.home.cms.registration

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.apollopharmacy.eposmobileapp.ui.dashboard.ConfirmSiteDialog
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config.REQUEST_CODE_CAMERA
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.databinding.FragmentRegistrationBinding
import com.apollopharmacy.vishwam.databinding.ViewImageItemBinding
import com.apollopharmacy.vishwam.dialog.*
import com.apollopharmacy.vishwam.dialog.AcknowledgementDialog.Companion.KEY_DATA_ACK
import com.apollopharmacy.vishwam.dialog.CategoryDialog.Companion.KEY_DATA_SUBCATEGORY
import com.apollopharmacy.vishwam.dialog.CustomDialog.Companion.KEY_DATA
import com.apollopharmacy.vishwam.ui.home.MainActivity.isSuperAdmin
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RegistrationFragment : BaseFragment<RegistrationViewModel, FragmentRegistrationBinding>(),
    CustomDialog.AbstractDialogClickListner, CategoryDialog.SubCategoryDialogClickListner,
    SubCategoryDialog.SubSubCategoryDialogClickListner, CalendarDialog.DateSelected,
    ImageClickListner, SiteDialog.AbstractDialogSiteClickListner,
    SubmitcomplaintDialog.AbstractDialogSubmitClickListner,
    ConfirmSiteDialog.OnSiteClickListener, ReasonsDialog.ReasonsDialogClickListner,
    SearchArticleCodeDialog.SearchArticleDialogClickListner, CalendarFutureDate.DateSelectedFuture {

    private var statusInventory: String? = null
    lateinit var userData: LoginDetails
    lateinit var storeData: LoginDetails.StoreData
    private var fileArrayList = ArrayList<ImageDataDto>()
    lateinit var adapter: ImageRecyclerView
    private var imagesArrayListSend = ArrayList<SubmitNewV2Response.PrescriptionImagesItem>()
    var imageFromCameraFile: File? = null
    private var categoryListSelected = ArrayList<DepartmentV2Response.CategoriesItem>()
    private var subCategoryListSelected = ArrayList<DepartmentV2Response.SubcategoryItem>()
    private var departmentId: String? = null
    private var maintanceArrayList = arrayListOf<String>()
    private var storeInfo: String = ""
    private var dcInfo: String = ""
    val TAG = "RegistrationFragment"


    var platformUid: String? = null
    var deptuid: String? = null
    var categoryuid: String? = null
    var subcategoryuid: String? = null
    var reasonuid: String? = null
    lateinit var reasonSla: ArrayList<ReasonmasterV2Response.Reason_SLA>
    var siteid: String? = null

    var dynamicsiteid: String? = null

    var cmsloginresponse: ResponseCMSLogin.Data? = null
    var ticketstatusapiresponse: ResponseTicktResolvedapi? = null
    var ticketratingapiresponse: ResponseticketRatingApi.Data? = null

    private var NewimagesArrayListSend = ArrayList<RequestNewComplaintRegistration.Image>()

    private var reasoncategoryListSelected = ArrayList<ReasonmasterV2Response.TicketCategory>()
    private var reasonssubCategoryListSelected =
        ArrayList<ReasonmasterV2Response.TicketSubCategory>()

    private var reasonsListSelected = ArrayList<ReasonmasterV2Response.Row>()
    var repo : LoginDetails? = null
    override val layoutRes: Int
        get() = R.layout.fragment_registration

    override fun retrieveViewModel(): RegistrationViewModel {
        return RegistrationViewModel()
    }

    override fun setup() {
        viewBinding.registration = viewModel
        userData = LoginRepo.getProfile()!!

        platformUid = "mobile"
//        if (isSuperAdmin) {
//            showLoading()
//            viewModel.siteId()
//        } else {
            if (userData.STOREDETAILS.get(0).IsSelectedStore) {
                viewBinding.departmentLayout.visibility = View.VISIBLE
                val storeItem = StoreListItem(
                    userData.STOREDETAILS.get(0).SITENAME,
                    userData.STOREDETAILS.get(0).STATEID,
                    userData.STOREDETAILS.get(0).DCNAME,
                    userData.STOREDETAILS.get(0).SITEID,
                    userData.STOREDETAILS.get(0).DC
                )
                Preferences.saveSiteId(userData.STOREDETAILS.get(0).SITEID)
                var storedata = StoreData(
                    userData.STOREDETAILS.get(0).SITEID,
                    userData.STOREDETAILS.get(0).SITENAME,
                    userData.STOREDETAILS.get(0).DCNAME,
                    userData.STOREDETAILS.get(0).STATEID,
                    userData.STOREDETAILS.get(0).DC
                )
                LoginRepo.saveStoreData(storedata)


                // viewModel.getListOfPendingAcknowledgement(storeItem)
                viewModel.getSelectedStoreDetails(storeItem)
                storeInfo =
                    userData.STOREDETAILS.get(0).SITEID + " - " + userData.STOREDETAILS.get(0).SITENAME
                dcInfo =
                    userData.STOREDETAILS.get(0).DC + " - " + userData.STOREDETAILS.get(0).DCNAME
                viewBinding.siteIdSelect.setText(storeInfo)
                viewBinding.branchName.setText(dcInfo)

                val c = Calendar.getInstance().time
                println("Current time => $c")
                val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                var formattedDate = df.format(c);
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

        showLoading()
        // viewModel.getDepartment()
        viewModel.getRemarksMasterList()
//            else {
//                if (Preferences.getSiteId().isEmpty()) {
//                    showLoading()
//                    viewModel.siteId()
//                } else {
//                    updateSiteId()
//                }
//            }
//        }

        /*  if(dynamicsiteid) {
              dynamicsiteid =
          }*/
        adapter = ImageRecyclerView(fileArrayList, this)
        viewBinding.imageRecyclerView.adapter = adapter
        viewBinding.selectDepartment.setOnClickListener {
            CustomDialog().apply {
                arguments =
                        //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                    CustomDialog().generateParsedDatafromreasons(viewModel.getdepartmrntsformreasonslist())
            }.show(childFragmentManager, "")
        }
        viewBinding.siteIdSelect.setOnClickListener {
            showLoading()
            viewModel.siteId()
            /*if (viewBinding.siteIdSelect.text.isNullOrEmpty()) {
                SiteDialog().apply {
                    arguments =
                        SiteDialog().generateParsedData(viewModel.getSiteData())
                }.show(childFragmentManager, "")
            }*/
        }
        viewModel.command.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CmsCommand.VisibleLayout -> {
                    viewBinding.problemLayout.visibility = View.VISIBLE
                }
                is CmsCommand.InVisibleLayout -> {
                    viewBinding.problemLayout.visibility = View.GONE
                }
            }
        })

        //select category form Reasons list api..........
        viewBinding.selectCategory.setOnClickListener {
            if (reasoncategoryListSelected.size == 0) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_no_category_available),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                for (item in reasoncategoryListSelected) {
                    maintanceArrayList.add(item.name.toString())
                }
                CategoryDialog().apply {
                    arguments = CategoryDialog().generateParsedData(
                        reasoncategoryListSelected,
                        KEY_DATA_SUBCATEGORY
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
                )
                    .show()
            } else {
                ReasonsDialog().apply {
                    arguments = ReasonsDialog().generateParsedData(
                        reasonsListSelected, KEY_DATA
                    )
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
                )
                    .show()
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
            hideLoading();


        })


        //ticket status api response..................
        viewModel.tisketstatusresponse.observe(viewLifecycleOwner, {
            var ticketstatus: ResponseTicktResolvedapi
            ticketstatus = it

            if (!ticketstatus.success) {
                ticketstatusapiresponse = ticketstatus;

                /*var cmsLogin = RequestCMSLogin()
                  cmsLogin.appUserName = "admin"
                  cmsLogin.appPassword = "Cms#1234"
                  viewModel.getCMSLoginApi(cmsLogin)
                  viewModel.getTicketRatingApi()*/


                  AcknowledgementDialog().apply {
                      arguments = AcknowledgementDialog()
                          .generateParsedDataNew(ticketstatus.data, KEY_DATA_ACK)
                  }
                      .show(childFragmentManager, "")
            }

        })

        //close ticket Response........
        viewModel.cmsticketclosingapiresponse.observe(viewLifecycleOwner, {

            /*   SubmitcomplaintDialog().apply {
                   arguments =
                       SubmitcomplaintDialog().generateParsedData(it)
               }.show(childFragmentManager, "")*/
        })

        /* viewBinding.selectCategory.setOnClickListener {
             if (categoryListSelected.size == 0) {
                 Toast.makeText(
                     requireContext(),
                     context?.resources?.getString(R.string.label_no_category_available),
                     Toast.LENGTH_SHORT
                 )
                     .show()
             } else {
                 for (item in categoryListSelected) {
                     maintanceArrayList.add(item.categoryName.toString())
                 }
                 CategoryDialog().apply {
                     arguments = CategoryDialog().generateParsedData(
                         categoryListSelected,
                         KEY_DATA_SUBCATEGORY
                     )
                 }.show(childFragmentManager, "")
             }
         }*/
        /*viewBinding.selectSubCategory.setOnClickListener {
            if (subCategoryListSelected.size == 0) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_no_sub_category_available),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                SubCategoryDialog().apply {
                    arguments = SubCategoryDialog().generateParsedData(
                        subCategoryListSelected, KEY_DATA
                    )
                }.show(childFragmentManager, "")
            }
        }*/
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
                    openCamera()
            }
        }

//        viewBinding.dateOfProblem.setOnClickListener { openDateDialog() }
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
                val description = viewBinding.descriptionText.text.toString().trim()
                var currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                var problemDate =
                    Utils.dateofoccurence(viewBinding.dateOfProblem.text.toString()) + " " + currentTime;
                showLoading()
                /* val newimageurl="https://www.google.com/imgres?imgurl=https%3A%2F%2Fmedia.clickoncare.com%2Fcatalog%2Fproduct%2Fcache%2F1%2Fimage%2F720x%2F9df78eab33525d08d6e5fb8d27136e95%2Fa%2Fl%2Faloeera-juice-concentrate-900ml_1-3420805279.jpg&imgrefurl=https%3A%2F%2Fwww.clickoncarecom%2Fapollo-noni-with-aloevera-juice-concentrate-900ml&tbnid=2AhQmWnajeRtrMvet=10CAYQMyhqahcKEwiY44vq0N72AhUAAAAAHQAAAAAQAg..i&docid=CaFCjtUPqEw3bM&w=720&h=720&q=apollo%20product%20image&ved=0CAYQMyhqahcKEwiY44vq0N72AhUAAAAAHQAAAAAQAg"
                 val newimageurl1=  RequestNewComplaintRegistration.Image(newimageurl.toString())
                 NewimagesArrayListSend.add(newimageurl1)*/
                // viewModel.connectToAzure(fileArrayList, statusInventory!!)

                /* statusInventory = "NOTBATCH"
                 viewModel.connectToAzure(fileArrayList, statusInventory!!)

                 if (fileArrayList.isNullOrEmpty()) {
                     viewModel.submitNewcomplaintregApi(RequestNewComplaintRegistration(
                        "RH18344",
                         problemDate,
                         description,
                         RequestNewComplaintRegistration.Platform(platformUid!!),
                         RequestNewComplaintRegistration.Category(categoryuid!!),
                         RequestNewComplaintRegistration.Department(deptuid!!),
                         RequestNewComplaintRegistration.Site(Preferences.getSiteId()),
                         RequestNewComplaintRegistration.Reason(reasonuid!!),
                         RequestNewComplaintRegistration.Subcategory(subcategoryuid!!),
                         RequestNewComplaintRegistration.ProblemImages(NewimagesArrayListSend)
                     )
                     )
                 }
                 else {
                     statusInventory = "NOTBATCH"
                     viewModel.connectToAzure(fileArrayList, statusInventory!!)
                 }*/
                if (statusInventory.equals("NEWBATCH")) {
                    viewModel.connectToAzure(fileArrayList, statusInventory!!)
                } else if (statusInventory.equals("MAINTENANCE")) {
                    if (fileArrayList.isNullOrEmpty()) {
                        /* viewModel.submitApi(
                             SubmitNewV2Response(
                                 "",
                                 imagesArrayListSend,
                                 "${description},${viewBinding.selectCategory.text.toString()},${viewBinding.selectSubCategory.text.toString()}",
                                 userData.EMPNAME,
                                 "",
                                 "",
                                 problemDate,
                                 "",
                                 departmentId,
                                 "",
                                 "",
                                 userData.EMPID
                             )*/
                        viewModel.submitNewcomplaintregApi(RequestNewComplaintRegistration(
                            //"RH18344",
                            userData.EMPID,
                            problemDate,
                            description,
                            RequestNewComplaintRegistration.Platform(platformUid!!),
                            RequestNewComplaintRegistration.Category(categoryuid!!),
                            RequestNewComplaintRegistration.Department(deptuid!!),
                            //RequestNewComplaintRegistration.Site("12067"),
                            RequestNewComplaintRegistration.Site(Preferences.getSiteId()),
                            RequestNewComplaintRegistration.Reason(reasonuid!!,reasonSla!!),
                            RequestNewComplaintRegistration.Subcategory(subcategoryuid!!),
                            RequestNewComplaintRegistration.ProblemImages(NewimagesArrayListSend)
                        )
                        )
                    } else {
                        viewModel.connectToAzure(fileArrayList, statusInventory!!)
                    }
                } else {
                    Utils.printMessage(TAG, "Department = Non inventory")
                    if (fileArrayList.isNullOrEmpty()) {
                        /* viewModel.submitApi(
                             SubmitNewV2Response(
                                 "",
                                 imagesArrayListSend,
                                 description,
                                 userData.EMPNAME,
                                 "",
                                 "",
                                 problemDate,
                                 "",
                                 departmentId,
                                 "",
                                 "",
                                 userData.EMPID
                             )*/

                        viewModel.submitNewcomplaintregApi(RequestNewComplaintRegistration(
                            //"RH18344",
                            userData.EMPID,
                            problemDate,
                            description,
                            RequestNewComplaintRegistration.Platform(platformUid!!),
                            RequestNewComplaintRegistration.Category(categoryuid!!),
                            RequestNewComplaintRegistration.Department(deptuid!!),
                            // RequestNewComplaintRegistration.Site("12067"),
                            RequestNewComplaintRegistration.Site(Preferences.getSiteId()),
                            RequestNewComplaintRegistration.Reason(reasonuid!!,reasonSla!!),
                            RequestNewComplaintRegistration.Subcategory(subcategoryuid!!),
                            RequestNewComplaintRegistration.ProblemImages(NewimagesArrayListSend)
                        )
                        )
                    } else {
                        viewModel.connectToAzure(fileArrayList, statusInventory!!)
                    }
                }
            }
        }

        viewModel.responsenewcomplaintregistration.observe(viewLifecycleOwner, {
            hideLoading()
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            RefreshView()
            repo?.let { it1 -> LoginRepo.saveProfile(it1,LoginRepo.getPassword()) }
            SubmitcomplaintDialog().apply {
                arguments =
                    SubmitcomplaintDialog().generateParsedData(it)
            }.show(childFragmentManager, "")
        })
        viewModel.command.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CmsCommand.RefreshPageOnSuccess -> {
                    //Show Gif image here
                    /* hideLoading()
                     Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                     RefreshView()

                     SubmitcomplaintDialog().apply {
                         arguments =
                             SubmitcomplaintDialog().generateParsedData(it)
                     }.show(childFragmentManager, "")*/


                }
                is CmsCommand.ImageIsUploadedInAzur -> {
                    for (i in it.filePath.indices) {
                        imagesArrayListSend.add(SubmitNewV2Response.PrescriptionImagesItem(it.filePath[i].base64Images))
                        NewimagesArrayListSend.add(RequestNewComplaintRegistration.Image(it.filePath[i].base64Images))

                    }
                    if (it.tag.equals("NOTBATCH")) {
                        Utils.printMessage(TAG, "Inventory Submit == Non Inventory")
                        Utils.printMessage(TAG, "Image : " + imagesArrayListSend.get(0).toString())
                        /*viewModel.submitRequestWithImages(
                            SubmitNewV2Response(
                                "",
                                imagesArrayListSend,
                                viewBinding.descriptionText.text.toString().trim(),
                                userData.EMPNAME,
                                "",
                                "",
                                viewBinding.dateOfProblem.text.toString(),
                                "",
                                departmentId,
                                "",
                                "",
                                userData.EMPID
                            ), statusInventory!!
                        )*/

                        val description = viewBinding.descriptionText.text.toString().trim()
                        var currentTime =
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        val problemDate =
                            Utils.dateofoccurence(viewBinding.dateOfProblem.text.toString()) + " " + currentTime
                        viewModel.submitNewcomplaintregApi(RequestNewComplaintRegistration(
                            // "RH18344",
                            userData.EMPID,
                            problemDate,
                            description,
                            RequestNewComplaintRegistration.Platform(platformUid!!),
                            RequestNewComplaintRegistration.Category(categoryuid!!),
                            RequestNewComplaintRegistration.Department(deptuid!!),
                            // RequestNewComplaintRegistration.Site("12067"),
                            RequestNewComplaintRegistration.Site(Preferences.getSiteId()),
                            RequestNewComplaintRegistration.Reason(reasonuid!!,reasonSla!!),
                            RequestNewComplaintRegistration.Subcategory(subcategoryuid!!),
                            RequestNewComplaintRegistration.ProblemImages(NewimagesArrayListSend)
                        )
                        )
                    } else if (it.tag.equals("MAINTENANCE")) {
                        Utils.printMessage(TAG, "Inventory Submit == MAINTENANCE")
                        /* val description = viewBinding.descriptionText.text.toString().trim()
                         val category = viewBinding.selectCategory.text.toString()
                         val subCategory = viewBinding.selectSubCategory.text.toString()
                        viewModel.submitRequestWithImages(
                             SubmitNewV2Response(
                                 "",
                                 imagesArrayListSend,
                                 "${description},${category},${subCategory}",
                                 userData.EMPNAME,
                                 "",
                                 "",
                                 viewBinding.dateOfProblem.text.toString(),
                                 "",
                                 departmentId,
                                 "",
                                 "",
                                 userData.EMPID
                             ), statusInventory!!
                         )*/
                        val description = viewBinding.descriptionText.text.toString().trim()
                        var currentTime =
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                        val problemDate =
                            Utils.dateofoccurence(viewBinding.dateOfProblem.text.toString()) + " " + currentTime
                        viewModel.submitNewcomplaintregApi(RequestNewComplaintRegistration(
                            //"RH18344",
                            userData.EMPID,
                            problemDate,
                            description,
                            RequestNewComplaintRegistration.Platform(platformUid!!),
                            RequestNewComplaintRegistration.Category(categoryuid!!),
                            RequestNewComplaintRegistration.Department(deptuid!!),
                            // RequestNewComplaintRegistration.Site("12067"),
                            RequestNewComplaintRegistration.Site(Preferences.getSiteId()),
                            RequestNewComplaintRegistration.Reason(reasonuid!!,reasonSla!!),
                            RequestNewComplaintRegistration.Subcategory(subcategoryuid!!),
                            RequestNewComplaintRegistration.ProblemImages(NewimagesArrayListSend)
                        )
                        )

                    } else {
                        Utils.printMessage(TAG, "Inventory Submit :: Inventory")
                        val getMrpCost = viewBinding.mrpEditText.text.toString().trim()
                        val getPurchaseCoset =
                            viewBinding.purchasePriseEdit.text.toString().trim()
                        val batchNo = viewBinding.batchText.text.toString().trim()
                        val articleCode = viewBinding.articleCode.text.toString().trim()
                        val expiredate = viewBinding.expireDateExpire.text.toString().trim()
                        val description =
                            "BATCH, PROBLEM IN $articleCode WITH BATCH $batchNo HAVING PURCHASE PRICE OF $getPurchaseCoset AND MRP OF $getMrpCost HAVING EXPIRY OF $expiredate"
                        /* viewModel.submitRequestWithImages(
                            SubmitNewV2Response(
                                "",
                                imagesArrayListSend,
                                description,
                                userData.EMPNAME,
                                "",
                                "",
                                viewBinding.dateOfProblem.text.toString(),
                                "",
                                departmentId,
                                articleCode,
                                batchNo,
                                userData.EMPID
                            ), statusInventory!!
                        )*/

                        // val description = viewBinding.descriptionText.text.toString().trim()
                        var currentTime =
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        val problemDate =
                            Utils.dateofoccurence(viewBinding.dateOfProblem.text.toString()) + " " + currentTime
                        viewModel.submitNewcomplaintregApi(RequestNewComplaintRegistration(
                            // "RH18344",
                            userData.EMPID,
                            problemDate,
                            description,
                            RequestNewComplaintRegistration.Platform(platformUid!!),
                            RequestNewComplaintRegistration.Category(categoryuid!!),
                            RequestNewComplaintRegistration.Department(deptuid!!),
                            //RequestNewComplaintRegistration.Site("12067"),
                            RequestNewComplaintRegistration.Site(Preferences.getSiteId()),
                            RequestNewComplaintRegistration.Reason(reasonuid!!,reasonSla!!),
                            RequestNewComplaintRegistration.Subcategory(subcategoryuid!!),
                            RequestNewComplaintRegistration.ProblemImages(NewimagesArrayListSend)
                        )
                        )
                    }
                }
                is CmsCommand.ShowToast -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is CmsCommand.CheckValidatedUserWithSiteID -> {
                    hideLoading()
                    onSuccessUserWithSiteID(it.slectedStoreItem)
                    /*Toast.makeText(
                        requireContext(),
                        context?.resources?.getString(R.string.label_registration_success),
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
                is CmsCommand.ShowSiteInfo -> {
                    hideLoading()
                    SiteDialog().apply {
                        arguments =
                            SiteDialog().generateParsedData(viewModel.getSiteData())
                    }.show(childFragmentManager, "")
                }
                is CmsCommand.SuccessDeptList -> {
                    hideLoading()
                }
            }
        })
        viewModel.pendingListLiveData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (it.status.equals("true")) {
                if (it.data.size != 0) {
                    AcknowledgementDialog().apply {
                        arguments = AcknowledgementDialog()
                            .generateParsedData(it.data, KEY_DATA_ACK)
                    }
                        .show(childFragmentManager, "")
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
                            viewBinding.mrpEditText.getText().toString()
                                .replace(".", "")
                        )
                        viewBinding.mrpEditText.setSelection(
                            viewBinding.mrpEditText.getText().toString().length
                        )
                    }
                } else {
                    if (text.contains(".") && text.indexOf(".") != text.length - 1 && text[text.length - 1].toString() == ".") {
                        viewBinding.mrpEditText.setText(
                            text.substring(
                                0,
                                text.length - 1
                            )
                        )
                        viewBinding.mrpEditText.setSelection(
                            viewBinding.mrpEditText.getText().toString().length
                        )
                    }
                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                        viewBinding.mrpEditText.setText(
                            text.substring(
                                0,
                                text.length - 1
                            )
                        )
                        viewBinding.mrpEditText.setSelection(
                            viewBinding.mrpEditText.getText().toString().length
                        )
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
                            viewBinding.purchasePriseEdit.getText().toString()
                                .replace(".", "")
                        )
                        viewBinding.purchasePriseEdit.setSelection(
                            viewBinding.purchasePriseEdit.getText().toString().length
                        )
                    }
                } else {
                    if (text.contains(".") && text.indexOf(".") != text.length - 1 && text[text.length - 1].toString() == ".") {
                        viewBinding.purchasePriseEdit.setText(
                            text.substring(
                                0,
                                text.length - 1
                            )
                        )
                        viewBinding.purchasePriseEdit.setSelection(
                            viewBinding.purchasePriseEdit.getText().toString().length
                        )
                    }
                    if (text.contains(".") && text.substring(text.indexOf(".") + 1).length > 2) {
                        viewBinding.purchasePriseEdit.setText(
                            text.substring(
                                0,
                                text.length - 1
                            )
                        )
                        viewBinding.purchasePriseEdit.setSelection(
                            viewBinding.purchasePriseEdit.getText().toString().length
                        )
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }
/*
    public  fun getcmslogindetails():ResponseCMSLogin?
    {
        return cmsloginresponse
    }

    public  fun getticketratingdeatails():ResponseticketRatingApi?
    {
        return ticketratingapiresponse
    }

    public  fun  getticketstatusdetails():ResponseTicktResolvedapi?
    {
        return ticketstatusapiresponse
    }*/

    private fun validationCheck(): Boolean {
        val departmentName = viewBinding.selectDepartment.text.toString().trim()
        val categoryName = viewBinding.selectCategory.text.toString().trim()
        val subCategoryName = viewBinding.selectSubCategory.text.toString().trim()
        val problemDate = viewBinding.dateOfProblem.text.toString()
        val description = viewBinding.descriptionText.text.toString().trim()
        val articleCode = viewBinding.articleCode.text.toString().trim()
        val batchNumber = viewBinding.batchText.text.toString().trim()
        val mrpPrice = viewBinding.mrpEditText.text.toString().trim()
        val purchasePrice = viewBinding.purchasePriseEdit.text.toString().trim()
        val expiryDate = viewBinding.expireDateExpire.text.toString().trim()
        val reason = viewBinding.selectRemarks.text.toString().trim()



        if (statusInventory.equals("NEWBATCH")) {
            if (articleCode.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_article_code)
                )
                return false
            } else if (batchNumber.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_batch_number)
                )
                return false
            } else if (mrpPrice.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_enter_mrp)
                )
                return false
            } else if (purchasePrice.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_enter_purchase_price)
                )
                return false
            } else if (purchasePrice.toDouble() > mrpPrice.toDouble()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_price_diff)
                )
                return false
            } else if (expiryDate.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_expiry_date)
                )
                return false
            } else if (fileArrayList.size < 2) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_upload_image)
                )
                return false
            }
        } else if (departmentName.equals("MAINTENANCE")) {
            if (categoryName.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_category)
                )
                return false
            } else if (subCategoryName.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_sub_category)
                )
                return false
            } else if (reason.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_reason)
                )
                return false
            } else if (problemDate.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_problem_since)
                )
                return false
            } else if (description.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_enter_description)
                )
                return false
            }

        } else {
            if (departmentName.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_department)
                )
                return false
            } else if (categoryName.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_category)
                )
                return false
            } else if (subCategoryName.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_sub_category)
                )
                return false
            } else if (reason.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_select_reason)
                )
                return false
            } else if (problemDate.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_problem_since)
                )
                return false
            } else if (description.isEmpty()) {
                showErrorMsg(
                    context?.resources?.getString(R.string.err_msg_enter_description)
                )
                return false
            }

        }
        return true;
    }

    private fun updateSiteId() {
        storeData = RegistrationRepo.getStoreInfo()!!
        val storeItem = StoreListItem(
            storeData.SITENAME,
            storeData.STATEID,
            storeData.DCNAME,
            storeData.SITEID,
            storeData.DC
        )

        var storedata = StoreData(
            storeData.SITEID,
            storeData.SITENAME,
            storeData.DCNAME,
            storeData.STATEID,
            storeData.DC
        )
        LoginRepo.saveStoreData(storedata)
        // viewModel.getListOfPendingAcknowledgement(storeItem)

//        viewModel.getRemarksMasterList()
        viewModel.getSelectedStoreDetails(storeItem)
        storeInfo =
            storeData.SITEID + " - " + storeData.SITENAME
        dcInfo = storeData.DC + " - " + storeData.DCNAME
        viewBinding.siteIdSelect.setText(storeInfo)
        viewBinding.departmentLayout.visibility = View.VISIBLE
        viewBinding.branchName.setText(dcInfo)
        // viewModel.getDepartment()
        viewBinding.siteIdSelect.setOnClickListener {
            Toast.makeText(
                context,
                context?.resources?.getString(R.string.label_site_change_alert),
                Toast.LENGTH_SHORT
            ).show()
        }
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

    /* override fun selectDepartment(departmentDto: DepartmentV2Response.DepartmentListItem) {
         viewBinding.inventoryMessageForCamera.visibility = View.GONE
         viewBinding.date.visibility = View.VISIBLE
         viewBinding.description.visibility = View.VISIBLE
         viewBinding.newBatchLayout.visibility = View.GONE
         departmentId = departmentDto.departmentKey
         statusInventory = "NOTBATCH"
         viewBinding.selectDepartment.setText(departmentDto.departmentName)
         categoryListSelected.clear()
         subCategoryListSelected.clear()
         viewBinding.selectCategory.setText("")
         if (departmentDto.categories.size != 0) {
             departmentDto.categories.map { categoryListSelected.add(it) }
             viewBinding.selectCategoryText.visibility = View.VISIBLE
             viewBinding.selectSubCategoryText.visibility = View.GONE
         } else {
             viewBinding.selectCategoryText.visibility = View.GONE
             viewBinding.selectSubCategoryText.visibility = View.GONE
         }
     }*/

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


        deptuid = departmentDto.uid
        //Ticket status Api calling function................

        viewModel.getTicketstatus(Preferences.getSiteId(), deptuid);


        var cateorylist = departmentDto.uid?.let { viewModel.getCategoriesfromReasons(it) }
        reasoncategoryListSelected.clear();
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

    /*override fun selectCategory(departmentDto: DepartmentV2Response.CategoriesItem) {
        viewBinding.selectCategory.setText(departmentDto.categoryName)
        viewBinding.selectSubCategoryText.visibility = View.GONE
        subCategoryListSelected.clear()
        if (departmentDto.categoryName.equals("New Batch")) {
            statusInventory = "NEWBATCH"
            viewBinding.description.visibility = View.GONE
            viewBinding.newBatchLayout.visibility = View.VISIBLE
            viewBinding.date.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.VISIBLE
        } else {
            for (i in maintanceArrayList.indices) {
                if (maintanceArrayList[i].equals(departmentDto.categoryName)) {
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
        }
        viewBinding.selectSubCategory.setText("")
        departmentDto.subcategory.map { subCategoryListSelected.add(it) }
        if (departmentDto.subcategory.size != 0)
            viewBinding.selectSubCategoryText.visibility = View.VISIBLE
        else
            viewBinding.selectSubCategoryText.visibility = View.GONE
    }*/

    /* override fun selectSubCategory(departmentDto: DepartmentV2Response.SubcategoryItem) {
         viewBinding.selectSubCategory.setText(departmentDto.subCategoryName)
     }*/

    //select category from Reasons List.......
    override fun selectCategory(departmentDto: ReasonmasterV2Response.TicketCategory) {
        viewBinding.selectCategory.setText(departmentDto.name)
        viewBinding.selectSubCategoryText.visibility = View.GONE
        viewBinding.selectRemarksText.visibility = View.GONE

        subCategoryListSelected.clear()
        if (departmentDto.name.equals("New Batch")) {
            statusInventory = "NEWBATCH"
            viewBinding.description.visibility = View.GONE
            viewBinding.newBatchLayout.visibility = View.VISIBLE
            viewBinding.date.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.VISIBLE
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
        }
        viewBinding.selectSubCategory.setText("")
        viewBinding.selectRemarks.setText("")
        reasonssubCategoryListSelected.clear()
        reasonsListSelected.clear()
        categoryuid = departmentDto.uid
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
        // reasonuid = departmentDto.uid?.let { viewModel.getreasonlist(it) }
        var reasonlist = departmentDto.uid?.let { viewModel.getreasonlist(it) }
        reasonsListSelected.clear()
        if (reasonlist != null) {
            if (reasonlist.size != 0) {
                reasonlist.map { reasonsListSelected.add(it) }
                //  viewBinding.selectSubCategoryText.visibility = View.VISIBLE
                viewBinding.selectRemarksText.visibility = View.VISIBLE
            } else {
                // viewBinding.selectSubCategoryText.visibility = View.GONE
                viewBinding.selectRemarksText.visibility = View.GONE
            }
        }
        viewBinding.selectSubCategory.setText(departmentDto.name)
        viewBinding.selectRemarks.setText("")
    }

    override fun selectReasons(departmentDto: ReasonmasterV2Response.Row) {
        /* var reasonlist=departmentDto.uid?.let { viewModel.getreasonlist(it) }
        reasonsListSelected.clear()
        if(reasonlist != null)
        {
            if (reasonlist.size != 0) {
                reasonlist.map { reasonsListSelected.add(it) }
                //  viewBinding.selectSubCategoryText.visibility = View.VISIBLE
                viewBinding.selectRemarksText.visibility = View.VISIBLE
            }
            else {
                // viewBinding.selectSubCategoryText.visibility = View.GONE
                viewBinding.selectRemarksText.visibility = View.GONE
            }
        }*/
        viewBinding.selectRemarks.setText(departmentDto.name)
        reasonuid = departmentDto.uid
        reasonSla = departmentDto.reason_sla
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
        fileArrayList.clear()
        imagesArrayListSend.clear()
        adapter.notifyAdapter(fileArrayList)

        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        var formattedDate = df.format(c);
        viewBinding.dateOfProblem.setText(formattedDate)

        viewBinding.newBatchLayout.visibility = View.GONE
        viewBinding.departmentLayout.visibility = View.VISIBLE
        viewBinding.selectDepartmentText.visibility = View.VISIBLE
        viewBinding.selectCategoryText.visibility = View.GONE
        viewBinding.selectSubCategoryText.visibility = View.GONE
        viewBinding.selectRemarksText.visibility = View.GONE
        viewBinding.date.visibility = View.VISIBLE
        viewBinding.description.visibility = View.VISIBLE
//        viewBinding.siteIdSelect.setText(storeInfo)
//        viewBinding.branchName.setText(dcInfo)
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
            REQUEST_CODE_CAMERA -> {
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
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
            fileArrayList.add(ImageDataDto(imageFromCameraFile!!, ""))
            adapter.notifyAdapter(fileArrayList)
        }
    }

    override fun deleteImage(position: Int) {
        adapter.deleteImage(position)
    }

    @SuppressLint("ResourceType")
    override fun confirmsavetheticket() {
        /* val fragment: Fragment = ComplainListFragment()
         val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
         val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.fragment_container, fragment)
         fragmentTransaction.addToBackStack(null)
         fragmentTransaction.commit()*/


        RefreshView()


    }

    override fun selectSite(departmentDto: StoreListItem) {
        if (isSuperAdmin) {
            onSuccessUserWithSiteID(departmentDto)
        } else {
            ConfirmSiteDialog().apply {
                arguments =
                    ConfirmSiteDialog().generateParsedData(departmentDto)
            }.show(childFragmentManager, "")
        }
    }

    override fun selectSubCategory(articleData: ArticleCodeResponse.DataItem) {
        clearArticleEditText()
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

    override fun confirmSite(departmentDto: StoreListItem) {
        showLoading()
        Preferences.saveSiteId(departmentDto.site!!)
        RegistrationRepo.saveStoreInfo(departmentDto)
        viewModel.registerUserWithSiteID(
            UserSiteIDRegReqModel(
                userData.EMPID,
                departmentDto.site
            ), departmentDto
        )

        var storedata = StoreData(
            departmentDto.site,
            departmentDto.store_name,
            departmentDto.dc_code?.name,
            departmentDto.site,
            departmentDto.dc_code?.code
        )
//        LoginRepo.saveStoreData(storedata)


    }

    override fun cancelledSite() {
        /* SiteDialog().apply {
             arguments =
                 SiteDialog().generateParsedData(viewModel.getSiteData())
         }.show(childFragmentManager, "")*/
    }

    @SuppressLint("SetTextI18n")
    fun onSuccessUserWithSiteID(selectedStoreItem: StoreListItem) {
        hideLoading()
        RefreshView()
        if (selectedStoreItem.site.isNullOrEmpty()) {
            viewBinding.departmentLayout.visibility = View.GONE
            viewBinding.newBatchLayout.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.GONE
        } else {
            viewBinding.newBatchLayout.visibility = View.GONE
            viewBinding.inventoryMessageForCamera.visibility = View.GONE
            Preferences.saveSiteId(selectedStoreItem.site!!)
            RegistrationRepo.saveStoreInfo(selectedStoreItem)
            if (!isSuperAdmin) {
                showLoading()
            }
            // viewModel.getListOfPendingAcknowledgement(selectedStoreItem)
//            viewModel.getRemarksMasterList()
            viewModel.getSelectedStoreDetails(selectedStoreItem)
            viewBinding.departmentLayout.visibility = View.VISIBLE
           val store =  LoginDetails.StoreData(
               selectedStoreItem.site.toString(),
               selectedStoreItem.store_name.toString(),
               selectedStoreItem.dc_code?.name.toString(),
               selectedStoreItem.site.toString(),
               selectedStoreItem.dc_code?.code.toString(),
                true)
            repo = LoginRepo.getProfile()
            repo?.STOREDETAILS?.clear()
            repo?.STOREDETAILS?.add(store)
            if (repo != null) {
//                LoginRepo.saveProfile(repo,LoginRepo.getPassword())
                Log.e("Saved prif data", Gson().toJson(repo))
                viewBinding.siteIdSelect.setText(selectedStoreItem.site + " - " + selectedStoreItem.store_name)
                viewBinding.branchName.setText(selectedStoreItem.dc_code?.code + " - " + selectedStoreItem.dc_code?.name)
            }

        }
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

interface ImageClickListner {
    fun deleteImage(position: Int)
}
