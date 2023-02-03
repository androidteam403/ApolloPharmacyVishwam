package com.apollopharmacy.vishwam.ui.home.drugmodule

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.data.model.ImageFile
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.*
import com.apollopharmacy.vishwam.ui.home.IOnBackPressed
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.*
import com.apollopharmacy.vishwam.util.PopUpWIndow
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import me.echodev.resizer.Resizer
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Drug : BaseFragment<DrugFragmentViewModel, FragmentDrugBinding>(),
    ComplaintListCalendarDialog.DateSelected, ImagesListner, CalenderNew.DateSelected,
    DoctorSpecialityDialog.SelectDoctorDialogListner,
    ItemTypeDialog.ItemTypeDialogClickListner,
    SiteNewDialog.NewDialogSiteClickListner, SubmitDialog.AbstractDialogSubmitClickListner,
    Dialog.DialogClickListner, GstDialog.GstDialogClickListner,
    SubmitcomplaintDialog.AbstractDialogSubmitClickListner,
    DrugFragmentCallback,
    IOnBackPressed {

    lateinit var adapter: DrugImageRecyclerView
    lateinit var adapter1: DrugImageRecyclerView1
    lateinit var adapter2: DrugImageRecyclerView2
    lateinit var adapter3: DrugImageRecyclerView3


    var imageList = ArrayList<Image>()

    var frontImageList = ArrayList<Image>()
    var backImageList = ArrayList<Image>()
    var sideImageList = ArrayList<Image>()
    var billImageList = ArrayList<Image>()
    var selectedDoctorSpecialityUid: String? = ""
    var selectedItemTypeUid: String? = ""
    var newImageList = ArrayList<ImageFile>()
    var imagesList = ArrayList<DrugRequest.Image>()

    var imageFromCameraFile: File? = null
    var imageFromBackCameraFile: File? = null
    var imageFromSideCameraFile: File? = null
    var imageFromBillCameraFile: File? = null

    var imageFromGallery: File? = null
    var isFromDateSelected: Boolean = false
    lateinit var store: StoreListItem
    override val layoutRes: Int
        get() = R.layout.fragment_drug

    override fun retrieveViewModel(): DrugFragmentViewModel {
        return ViewModelProvider(this).get(DrugFragmentViewModel::class.java)
    }

    var itemTypeDropDownResponse: ItemTypeDropDownResponse? = null
    var doctorSpecialityDropDownREsponse: ItemTypeDropDownResponse? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {
        viewModel.itemTypeApi(this)
        viewModel.doctorSpecialityApi(this)
        showLoading()
        viewModel.getRemarksMasterList()
        viewModel.subCategories.observe(viewLifecycleOwner) {
            when (it) {
                is ArrayList<ReasonmasterV2Response.TicketSubCategory> -> {
                    hideLoading()
                }
            }
        }
        viewBinding.mrpp.setText("0")
        viewBinding.fromDateText.setText(Utlis.getCurrentDate("dd-MMM-yyyy").toString())
        viewBinding.toDateText.setText(Utlis.getCurrentDate("dd-MMM-yyyy").toString())
        viewBinding.batchNo.setText("12345")
        viewBinding.purchasePrice.setText("0")
//        viewBinding.selectDepartment.setText("0")
        viewBinding.packsize.setText("1")
        viewBinding.selectDepartment.setText("0")
        viewBinding.hsnCode.setText("0")
        viewBinding.barCode.setText("0")
        viewBinding.doctorname.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))

        viewBinding.batchNo.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        viewModel.commands.observe(viewLifecycleOwner, {
            when (it) {
                is DrugFragmentViewModel.Commands.ShowSiteInfo -> {
                    hideLoading()

                    SiteNewDialog().apply {

                        Preferences.setSiteIdList(Gson().toJson(viewModel.getSiteData()))
                        Preferences.setSiteIdListFetched(true)
                        arguments =
                            SiteNewDialog().generateParsedData(viewModel.getSiteData())
                    }.show(childFragmentManager, "")

                }
                else -> {}
            }
        })



        viewBinding.itemType.setOnClickListener {
            if (viewBinding.batchNo.text.toString().isEmpty()) {
                viewBinding.batchNo.setText("12345")
            }

            val gson = Gson()
            val itemTypeList = Preferences.getItemTypeListJson()


            val type = object : TypeToken<ItemTypeDropDownResponse>() {}.type
            this.itemTypeDropDownResponse =
                gson.fromJson<List<ItemTypeDropDownResponse>>(
                    itemTypeList,
                    type
                ) as ItemTypeDropDownResponse

            ItemTypeDialog().apply {
                arguments =
                    ItemTypeDialog().generateParsedData(itemTypeDropDownResponse!!.data!!.listData!!.rows)
            }.show(childFragmentManager, "")

        }
        viewBinding.doctorSpecialty.setOnClickListener {
            if (viewBinding.batchNo.text.toString().isEmpty()) {
                viewBinding.batchNo.setText("12345")
            }
            val gson = Gson()
            val doctorSpecialityList = Preferences.getDoctorSpecialityListJson()


            val type = object : TypeToken<ItemTypeDropDownResponse>() {}.type
            this.doctorSpecialityDropDownREsponse =
                gson.fromJson<List<ItemTypeDropDownResponse>>(
                    doctorSpecialityList,
                    type
                ) as ItemTypeDropDownResponse

            DoctorSpecialityDialog().apply {
                arguments =
                    DoctorSpecialityDialog().generateParsedData(doctorSpecialityDropDownREsponse!!.data!!.listData!!.rows)
            }.show(childFragmentManager, "")

        }


        viewBinding.selectCategory.setOnClickListener {
            if (viewBinding.batchNo.text.toString().isEmpty()) {
                viewBinding.batchNo.setText("12345")
            }
            Dialog().apply {
                arguments =
                        //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                    Dialog().generateParsedData(viewModel.uniqueSubCategoryList)
            }.show(childFragmentManager, "")


        }

        viewBinding.siteIdSelect.setOnClickListener {
            showLoading()
            viewModel.siteId()


        }


        viewBinding.packsize.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    if (viewBinding.packsize.text.toString() == null || viewBinding.packsize.text!!.isEmpty()) {
                        viewBinding.packsize.setText("1")
                    }
                }
            }
        })



        viewBinding.itemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (viewBinding.batchNo.text.toString().isEmpty()) {
                    viewBinding.batchNo.setText("12345")
                }
                if (s != null) {
                    if (s.length == 1) {
                        viewBinding.branchNameTextInput.error = null

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })




        viewBinding.batchNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1) {
                        viewBinding.BatchTextInput.error = null

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


        viewBinding.mrpp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1 && s.startsWith(".")) {
                        viewBinding.mrpp.setText("0.")
                        viewBinding.mrpp.setSelection(viewBinding.mrpp.text!!.length)
                    } else if (s.length > 1 && s.startsWith("0")) {
                        if (!s.startsWith("0.")) {
                            viewBinding.mrpp.setText(s.substring(1))
                            viewBinding.mrpp.setSelection(viewBinding.mrpp.text!!.length)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        viewBinding.purchasePrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1 && s.startsWith(".")) {
                        viewBinding.purchasePrice.setText("0.")
                        viewBinding.purchasePrice.setSelection(viewBinding.purchasePrice.text!!.length)
                    } else if (s.length > 1 && s.startsWith("0")) {
                        if (!s.startsWith("0.")) {
                            viewBinding.purchasePrice.setText(s.substring(1))
                            viewBinding.purchasePrice.setSelection(viewBinding.purchasePrice.text!!.length)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isEmpty()) {
                        viewBinding.purchasePrice.setText("0")
                        viewBinding.purchasePrice.setSelection(viewBinding.purchasePrice.text!!.length)
                    }
                }
            }

        })

        viewBinding.selectDepartment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1 && s.startsWith(".")) {
                        viewBinding.selectDepartment.setText("0.")
                        viewBinding.selectDepartment.setSelection(viewBinding.selectDepartment.text!!.length)
                    } else if (s.length > 1 && s.startsWith("0")) {
                        if (!s.startsWith("0.")) {
                            viewBinding.selectDepartment.setText(s.substring(1))
                            viewBinding.selectDepartment.setSelection(viewBinding.selectDepartment.text!!.length)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isEmpty()) {
                        viewBinding.selectDepartment.setText("0")
                        viewBinding.selectDepartment.setSelection(viewBinding.selectDepartment.text!!.length)
                    }
                }
            }

        })


        viewBinding.barCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length > 1 && s.startsWith("0")) {
                        viewBinding.barCode.setText(s.substring(1))
                        viewBinding.barCode.setSelection(viewBinding.barCode.text!!.length)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isEmpty()) {
                        viewBinding.barCode.setText("0")
                        viewBinding.barCode.setSelection(viewBinding.barCode.text!!.length)
                    }
                }
            }

        })
        viewBinding.description.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.gst.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })


        viewBinding.hsnCode.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.barCode.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.doctorSpecialtyLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.doctornameLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })



        viewBinding.expiredate.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.fromDate.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.selectRemarksText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.purchasePriceTextInput.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.mrp.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.packsize.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.reequiredQuantityLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.itemTypeLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.branchNameTextInput.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        viewBinding.selectCategoryText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
        viewBinding.siteId.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        if (viewBinding.batchNo.text.toString().isEmpty()) {
                            viewBinding.batchNo.setText("12345")
                        }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })





























        viewBinding.hsnCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (viewBinding.batchNo.text.toString().isEmpty()) {
                    viewBinding.batchNo.setText("12345")
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (viewBinding.batchNo.text.toString().isEmpty()) {
                    viewBinding.batchNo.setText("12345")
                }
                if (s != null) {
                    if (s.length > 1 && s.startsWith("0")) {
                        viewBinding.hsnCode.setText(s.substring(1))
                        viewBinding.hsnCode.setSelection(viewBinding.hsnCode.text!!.length)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isEmpty()) {
                        viewBinding.hsnCode.setText("0")
                        viewBinding.hsnCode.setSelection(viewBinding.hsnCode.text!!.length)
                    }
                }
            }

        })

        viewBinding.packsize.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1) {
                        viewBinding.pasckizel.error = null

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

//        viewBinding.selectDepartment.setOnClickListener {
//
//
//            GstDialog().apply {
//                arguments =
//                        //CustomDialog().generateParsedData(viewModel.getDepartmentData())
//                    GstDialog().generateParsedData(viewModel.getGst())
//            }.show(childFragmentManager, "")
//
//        }
        viewModel.responsenewcomplaintregistration.observe(viewLifecycleOwner, {
            hideLoading()
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            RefreshView()

            SubmitcomplaintDialog().apply {
                arguments =
                    SubmitcomplaintDialog().generateParsedData(it)
            }.show(childFragmentManager, "")
        })
        viewModel.drugList.observe(viewLifecycleOwner, {
            hideLoading()
            RefreshView()

            SubmitDialog().apply {
                arguments =
                    SubmitDialog().generateParsedData(it)
            }.show(childFragmentManager, "")

        })
        viewBinding.submit.setOnClickListener {


            if (validationCheck()) {
                showLoading()
                viewModel.connectToAzure(imageList)
            }
        }

        viewModel.commands.observe(viewLifecycleOwner) {
            when (it) {
                is DrugFragmentViewModel.Commands.ShowToast -> {
                    hideLoading()
                    showErrorMsg(it.message)
                }
                is DrugFragmentViewModel.Commands.DrugImagesUploadInAzur -> {
                    imagesList.clear()
                    var name: String = "FRONT"
                    for (i in it.filePath.indices) {
                        imagesList.add(
                            DrugRequest.Image(
                                name,
                                Base64.getEncoder()
                                    .encodeToString(it.filePath[i].base64Images.toByteArray()),
                                it.filePath[i].file.name,
                                "image/" + imageType(it.filePath[i].file).toString(),

                                it.filePath[i].file.length().toString(),
                                it.filePath[i].base64Images
                            )
                        )
                        if (imagesList.size == 1) {
                            name = "BACK"
                        } else if (imagesList.size == 2) {
                            name = "SIDE"
                        } else if (imagesList.size == 3) {
                            name = "BILL"
                        } else if (imagesList.size == 4) {
                            name = ""
                        }
                    }
                    var empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()
                    var employeeDetailsResponse: EmployeeDetailsResponse? = null
                    try {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        employeeDetailsResponse =
                            gson.fromJson<EmployeeDetailsResponse>(
                                empDetailsResponse,
                                EmployeeDetailsResponse::class.java
                            )
                    } catch (e: JsonParseException) {
                        e.printStackTrace()
                    }

                    viewModel.getDrugList(
                        DrugRequest(
                            viewBinding.siteIdSelect.text.toString(),
                            viewBinding.selectCategory.text.toString(),
                            viewBinding.itemName.text.toString(),
                            viewBinding.batchNo.text.toString(),
                            viewBinding.packsize.text.toString(),
                            viewBinding.mrpp.text.toString(),
                            viewBinding.purchasePrice.text.toString(),
                            viewBinding.fromDateText.text.toString(),
                            viewBinding.toDateText.text.toString(),
                            viewBinding.barCode.text.toString(),
                            viewBinding.hsnCode.text.toString(),
                            viewBinding.selectDepartment.text.toString(),
                            viewBinding.createdBy.text.toString(),
                            viewBinding.createdOn.text.toString(),
                            "0",
                            "0",
                            "",
                            viewBinding.selectRemarks.text.toString(),
                            viewBinding.createdBy.text.toString(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            selectedItemTypeUid,
                            "",
                            "",
                            viewBinding.requiredQuantity.text.toString(),
                            viewBinding.doctorname.text.toString(),
                            selectedDoctorSpecialityUid,
                            imagesList.distinct(),
                            viewBinding.descriptionText.text.toString(),
                            store, employeeDetailsResponse!!
                        )
                    )

                }
                else -> {}
            }

        }

        adapter3 = DrugImageRecyclerView3(billImageList, this)
        viewBinding.imageRecyclerView4.adapter = adapter3

        adapter = DrugImageRecyclerView(frontImageList, this)
        viewBinding.imageRecyclerView.adapter = adapter

        adapter1 = DrugImageRecyclerView1(backImageList, this)
        viewBinding.imageRecyclerView1.adapter = adapter1


        adapter2 = DrugImageRecyclerView2(sideImageList, this)
        viewBinding.imageRecyclerView2.adapter = adapter2












        viewBinding.fromDateText.setOnClickListener {
            isFromDateSelected = true
            openDateDialog()

        }

        viewBinding.toDateText.setOnClickListener {
            isFromDateSelected = true
            dateDialog()

        }



        viewBinding.addImage.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else
                cameraIntent()

        }

        viewBinding.addImage1.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else
                backCameraIntent()

        }

        viewBinding.addImage2.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else
                sideCameraIntent()
        }
        viewBinding.addImage3.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else
                billCameraIntent()

        }

        viewModel.fetchTransactionPOSDetails()
    }


    private fun cameraIntent() {
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
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)


    private fun backCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromBackCameraFile =
            File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromBackCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                imageFromBackCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


        startActivityForResult(intent, Config.REQUEST_BACK_CAMERA)
    }


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)


    private fun sideCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromSideCameraFile =
            File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromSideCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                imageFromSideCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Config.REQUEST_SIDE_CAMERA)
    }


    private fun billCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromBillCameraFile =
            File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromBillCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                imageFromBillCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, Config.REQUEST_BILL_CAMERA)
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
            Config.REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent()
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


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun rotateImage(bitmap: Bitmap) {
        var exifInterface: ExifInterface? = null
        try {
            exifInterface = imageFromCameraFile?.let { ExifInterface(it.absolutePath) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = exifInterface!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(270f)

            else -> {
            }
        }
        val rotatedBitMap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)


    }

    private fun setReducedSize(): Bitmap {

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFromCameraFile?.absolutePath, bmOptions)
        val camWidth = bmOptions.outWidth
        val camHeight = bmOptions.outHeight
        val scalefactor = Math.min(camWidth / 75, camHeight / 73)
        bmOptions.inSampleSize = scalefactor
        bmOptions.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(imageFromCameraFile?.absolutePath, bmOptions)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val matrix = Matrix()
        if (requestCode == Config.REQUEST_CODE_CAMERA) {
            if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {

                frontImageList.add(Image(compresImageSize(imageFromCameraFile!!), "", "Front"))
                imageList.add(Image(compresImageSize(imageFromCameraFile!!), "", ""))

//                rotateImage(setReducedSize())


                viewBinding.imageRecyclerView.visibility = View.VISIBLE
                viewBinding.addImage.visibility = View.GONE

                adapter.notifyAdapter(frontImageList)

            }
        } else if (requestCode == Config.REQUEST_BACK_CAMERA) {
            if (requestCode == Config.REQUEST_BACK_CAMERA && imageFromBackCameraFile != null && resultCode == Activity.RESULT_OK) {
                backImageList.add(Image(compresImageSize(imageFromBackCameraFile!!), "", "Back"))
                imageList.add(Image(compresImageSize(imageFromBackCameraFile!!), "", ""))

                viewBinding.imageRecyclerView1.visibility = View.VISIBLE
                viewBinding.addImage1.visibility = View.GONE

                adapter1.notifyAdapter(backImageList)
            }
        } else if (requestCode == Config.REQUEST_SIDE_CAMERA) {

            if (requestCode == Config.REQUEST_SIDE_CAMERA && imageFromSideCameraFile != null && resultCode == Activity.RESULT_OK) {

                sideImageList.add(Image(compresImageSize(imageFromSideCameraFile!!), "", "Side"))
                imageList.add(Image(compresImageSize(imageFromSideCameraFile!!), "", ""))

                viewBinding.imageRecyclerView2.visibility = View.VISIBLE
                viewBinding.addImage2.visibility = View.GONE



                adapter2.notifyAdapter(sideImageList)
            }

        } else if (requestCode == Config.REQUEST_BILL_CAMERA) {
            if (requestCode == Config.REQUEST_BILL_CAMERA && imageFromBillCameraFile != null && resultCode == Activity.RESULT_OK) {

                billImageList.add(Image(compresImageSize(imageFromBillCameraFile!!), "", "Bill"))
                imageList.add(Image(compresImageSize(imageFromBillCameraFile!!), "", ""))

                viewBinding.imageRecyclerView4.visibility = View.VISIBLE
                viewBinding.addImage3.visibility = View.GONE

                adapter3.notifyAdapter(billImageList)

            }
        }


    }

    private fun compresImageSize(imageFromCameraFile: File): File {
        val resizedImage = Resizer(requireContext())
            .setTargetLength(1080)
            .setQuality(100)
            .setOutputFormat("JPG")
            .setOutputDirPath(
                ViswamApp.context.cacheDir.toString()
            )
            .setSourceImage(imageFromCameraFile)
            .resizedFile
        return resizedImage
    }

    private fun showErrorMsg(errMsg: String?) {
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun validationCheck(): Boolean {
        val gst = viewBinding.selectDepartment.text.toString().trim()
        val categoryName = viewBinding.selectCategory.text.toString().trim()
        val itemName = viewBinding.itemName.text.toString().trim()
        val itemType = viewBinding.itemType.text.toString().trim()
        val requireQty = viewBinding.requiredQuantity.text.toString().trim()
        val doctorName = viewBinding.doctorname.text.toString().trim()
        val doctorSpeciality = viewBinding.doctorSpecialty.text.toString().trim()

        val manufDate = viewBinding.fromDateText.text.toString().trim()

        val createdBy = viewBinding.createdBy.text.toString().trim()
        val createdOn = viewBinding.createdOn.text.toString().trim()

        val remarks = viewBinding.selectRemarks.text.toString().trim()
        val expDate = viewBinding.toDateText.text.toString().trim()
        val batchNo = viewBinding.batchNo.text.toString().trim()
        val mrp = viewBinding.mrpp.text.toString().trim()
        val purchasePrice = viewBinding.purchasePrice.text.toString().trim()
        val hsnCode = viewBinding.hsnCode.text.toString().trim()
        val packsize = viewBinding.packsize.text.toString().trim()
        val site = viewBinding.siteIdSelect.text.toString().trim()
        val barCode = viewBinding.barCode.text.toString().trim()
        val location = viewBinding.loactionSelect.text.toString().trim()

        if (site.isEmpty()) {
            showErrorMsg(
                "Kindly select the Site"
            )
            return false
        } else if (location.isEmpty()) {
            showErrorMsg(
                "Please enter Location"
            )
            viewBinding.loactionSelect.requestFocus()
            return false
        } else if (createdBy.isEmpty()) {
            showErrorMsg(
                "Please enter CreatedBy"
            )
            viewBinding.createdInput.requestFocus()
            return false
        } else if (createdOn.isEmpty()) {
            showErrorMsg(
                "Please enter CreatedOn"
            )
            viewBinding.createdOnInput.requestFocus()
            return false
        } else if (categoryName.isEmpty()) {
            showErrorMsg(
                "Kindly select the Category Name "
            )
            return false
        } else if (itemName.isEmpty()) {
            showErrorMsg(
                "Please enter Item name"
            )
            viewBinding.itemName.requestFocus()
//            viewBinding.branchNameTextInput.error = ""
            return false
        } else if (itemType.isEmpty()) {
            showErrorMsg(
                "Kindly select the Item type"
            )

//            viewBinding.branchNameTextInput.error = ""
            return false
        } else if (requireQty.isEmpty()) {
            showErrorMsg(
                "Please enter Required quantity"
            )
            viewBinding.requiredQuantity.requestFocus()
//            viewBinding.branchNameTextInput.error = ""
            return false
        } else if (doctorName.isEmpty()) {
            showErrorMsg(
                "Please enter Doctor's name"
            )
            viewBinding.doctorname.requestFocus()
//            viewBinding.branchNameTextInput.error = ""
            return false
        } else if (doctorSpeciality.isEmpty()) {
            showErrorMsg(
                "Kindly select the Doctor speciality"
            )

//            viewBinding.branchNameTextInput.error = ""
            return false
        } else if (packsize.isEmpty()) {
            showErrorMsg(
                "Please enter Pack size"
            )
            viewBinding.pasckizel.requestFocus()
            return false
        } else if (mrp.isEmpty()) {
            showErrorMsg(
                "Please enter MRP"
            )
            viewBinding.MrpTextInput.requestFocus()
            return false
        } else if (mrp.isNotEmpty() && purchasePrice.isNotEmpty() && mrp.toDouble() < purchasePrice.toDouble()) {
            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_purchace_price_diff)
            )
            return false
        } else if (batchNo.isEmpty()) {
            showErrorMsg(
                "Please enter Batch number"
            )
            viewBinding.BatchTextInput.requestFocus()
            return false
        } else if (manufDate.isEmpty()) {
            showErrorMsg(
                "Please select Manufacturing date"
            )
            return false
        } else if (expDate.isEmpty()) {
            showErrorMsg(
                "Please select Expiry Date CreatedOn"
            )
            return false
        }

//        else if (barCode.isEmpty()) {
//            showErrorMsg(
//                "Please Enter Barcode"
//            )
//            viewBinding.barCodeL.requestFocus()
//            return false
//        }
//        else if (hsnCode.isEmpty()) {
//            viewBinding.hsnText.requestFocus()
//            showErrorMsg(
//                "Please Enter HSN code "
//            )
//            return false
//        }
        else if (!hsnCode.equals("0") && hsnCode.isNotEmpty() && hsnCode.length < 4) {
            viewBinding.hsnText.requestFocus()
            showErrorMsg(
                "Please Enter minimum 4 numbers in HSN Code"
            )
            return false
        }
//        else if (gst.isEmpty()) {
//            viewBinding.selectDepartment.requestFocus()
//            showErrorMsg(
//                "Please Enter GST"
//            )
//            return false
//        }
        else if (gst.isNotEmpty() && gst.toDouble() > 100) {
            viewBinding.selectDepartment.requestFocus()
            showErrorMsg(
                "Please Enter valid GST"
            )
            return false
        }
//        else if(frontImageList.isEmpty() ){
//            showErrorMsg(
//                "Please upload front Image"
//            )
//            return false
//        }else if(backImageList.isEmpty() ){
//            showErrorMsg(
//                "Please upload back Image"
//            )
//            return false
//        }
//        else if(sideImageList.isEmpty() ){
//            showErrorMsg(
//                "Please upload side Image"
//            )
//            return false
//        }else if(billImageList.isEmpty() ){
//            showErrorMsg(
//                "Please upload bill Image"
//            )
//            return false
//        }
        return true
    }

    override fun selectDepartment(departmentDto: ReasonmasterV2Response.TicketSubCategory) {
        viewModel.selectedSubCategory = departmentDto
        viewBinding.selectCategory.setText(departmentDto.name)
        viewBinding.selectCategoryText.error = null
    }

    @SuppressLint("ResourceType")
    override fun selectSite(departmentDto: StoreListItem) {
        store = departmentDto
        val simpleDateFormat = SimpleDateFormat("dd-MMM-YYYY")
        val currentDate: String = simpleDateFormat.format(Date())


        val site = viewBinding.siteIdSelect.text.toString().trim()

        viewBinding.siteIdSelect.setText(departmentDto.site + " - " + departmentDto.store_name)
        viewBinding.siteId.error = null

        viewBinding.dcTextInput.visibility = View.VISIBLE
        viewBinding.dcCode.visibility = View.VISIBLE

        viewBinding.dcCode.setText(
            "" + (departmentDto.dc_code?.code
                ?: String()) + " - " + departmentDto.dc_code?.name
        )
//        viewBinding.dcTextInput.setBoxBackgroundColorResource(R.color.cement)
//        viewBinding.createdInput.setBoxBackgroundColorResource(R.color.cement)
        viewBinding.createdOn.setText(currentDate)
//        viewBinding.createdOnInput.setBoxBackgroundColorResource(R.color.cement)
//        viewBinding.location.setBoxBackgroundColorResource(R.color.cement)

        viewBinding.createdBy.setText(Preferences.getToken())
        viewBinding.loactionSelect.setText(departmentDto.store_name)

        viewModel.getTicketstatus(departmentDto.site, viewModel.reasonData.data.department.uid)
    }

    fun imageType(pathname: File): String? {
        val name = pathname.name
        var ext: String? = null
        val i = name.lastIndexOf('.')
        if (i > 0 && i < name.length - 1) {
            ext = name.substring(i + 1).toLowerCase()
        }
        return ext
    }


    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.fromDateText.text.toString(),
                    false,
                    viewBinding.fromDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }


    fun dateDialog() {
        if (isFromDateSelected) {
            CalenderNew().apply {
                arguments = generateParsedData(
                    viewBinding.toDateText.text.toString(),
                    false,
                    viewBinding.toDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }


    fun RefreshView() {


        viewBinding.scrollView.fullScroll(ScrollView.FOCUS_UP)
//        viewBinding.selectDepartment.setText("")
        viewBinding.selectCategory.setText("")
        viewBinding.itemName.setText("")
//        viewBinding.fromDateText.setText("")
//        viewBinding.toDateText.setText("")
//        viewBinding.batchNo.setText("")
//        viewBinding.mrpp.setText("")
//        viewBinding.purchasePrice.setText("")
        viewBinding.hsnCode.setText("")
        viewBinding.itemType.setText("")
        viewBinding.doctorSpecialty.setText("")
        viewBinding.requiredQuantity.setText("")
        viewBinding.doctorname.setText("")


        viewBinding.siteIdSelect.setText("")
//        viewBinding.barCode.setText("")
        viewBinding.selectRemarks.setText("")
        viewBinding.createdOn.setText("")
        viewBinding.createdBy.setText("")
        viewBinding.descriptionText.setText("")
        viewBinding.loactionSelect.setText("")

        viewBinding.dcCode.setText(" ")
        viewBinding.dcCode.visibility = View.GONE
        viewBinding.dcTextInput.visibility = View.GONE

        viewBinding.dcTextInput.setBoxBackgroundColorResource(R.color.white)
        viewBinding.createdInput.setBoxBackgroundColorResource(R.color.white)

        viewBinding.createdOnInput.setBoxBackgroundColorResource(R.color.white)
        viewBinding.location.setBoxBackgroundColorResource(R.color.white)
        viewBinding.siteId.error = null
        viewBinding.selectCategoryText.error = null
        viewBinding.branchNameTextInput.error = null
//        viewBinding.fromDate.error = null
//        viewBinding.toDate.error = null
//        viewBinding.BatchTextInput.error = null
//        viewBinding.MrpTextInput.error = null
//        viewBinding.purchasePriceTextInput.error = null
//        viewBinding.hsnText.error = null
//        viewBinding.pasckizel.error = null
        viewBinding.location.error = null
        viewBinding.siteIdSelect.error = null
//        viewBinding.barCodeL.error = null
//        viewBinding.gst.error = null
        viewBinding.mrpp.setText("0")
        viewBinding.fromDateText.setText(Utlis.getCurrentDate("dd-MMM-yyyy").toString())
        viewBinding.toDateText.setText(Utlis.getCurrentDate("dd-MMM-yyyy").toString())
        viewBinding.batchNo.setText("12345")
        viewBinding.purchasePrice.setText("0")
//        viewBinding.selectDepartment.setText("0")
        viewBinding.packsize.setText("1")
        viewBinding.selectDepartment.setText("0")
        viewBinding.hsnCode.setText("0")
        viewBinding.barCode.setText("0")
        viewBinding.addImage.visibility = View.VISIBLE
        viewBinding.addImage1.visibility = View.VISIBLE
        viewBinding.addImage2.visibility = View.VISIBLE
        viewBinding.addImage3.visibility = View.VISIBLE

        imageList.clear()
        imagesList.clear()
        frontImageList.clear()
        backImageList.clear()
        sideImageList.clear()
        billImageList.clear()
        adapter.notifyAdapter(frontImageList)
        adapter1.notifyAdapter(backImageList)
        adapter2.notifyAdapter(sideImageList)
        adapter3.notifyAdapter(billImageList)

    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            viewBinding.fromDateText.setText(showingDate)
            viewBinding.fromDate.error = null

        }
//        else {
//            viewBinding.toDateText.setText(showingDate)
//            viewBinding.toDate.error = null
//        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
    }

    override fun selectGST(gst: String) {
        viewBinding.selectDepartment.setText(gst)
        viewBinding.gst.error = null
    }

    override fun confirmsavetheticket() {
        RefreshView()
    }


    fun popUpdialog() {


        val dialogBinding: LayoutErrorBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.layout_error,
                null,
                false
            )
        val customDialog = AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        if (dialogBinding != null) {
            dialogBinding.cancelButton.setOnClickListener {
                customDialog.dismiss()

            }

            dialogBinding.close.setOnClickListener {
                customDialog.dismiss()

            }

        }
    }


    override fun deleteImage(position: Int) {


        val dialogBinding: DialogDeleteBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_delete,
                null,
                false
            )
        val customDialog = AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {


            adapter.deleteImage(position)
            frontImageList.clear()
            viewBinding.addImage.visibility = View.VISIBLE
            viewBinding.imageRecyclerView.visibility = View.GONE

            adapter.notifyAdapter(frontImageList)
            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }

    }

    override fun backdeleteImage(position: Int) {
        val dialogBinding: DialogDeleteBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_delete,
                null,
                false
            )
        val customDialog = AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {


            adapter1.deleteImage(position)

            backImageList.clear()
            viewBinding.addImage1.visibility = View.VISIBLE
            viewBinding.imageRecyclerView1.visibility = View.GONE


            adapter1.notifyAdapter(backImageList)
            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun sidedeleteImage(position: Int) {
        val dialogBinding: DialogDeleteBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_delete,
                null,
                false
            )
        val customDialog = AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {


            adapter2.deleteImage(position)

            sideImageList.clear()
            viewBinding.addImage2.visibility = View.VISIBLE
            viewBinding.imageRecyclerView2.visibility = View.GONE

            adapter2.notifyAdapter(sideImageList)
            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun billdeleteImage(position: Int) {
        val dialogBinding: DialogDeleteBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_delete,
                null,
                false
            )
        val customDialog = AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {


            adapter3.deleteImage(position)
            billImageList.clear()
            viewBinding.addImage3.visibility = View.VISIBLE
            viewBinding.imageRecyclerView4.visibility = View.GONE

            adapter3.notifyAdapter(billImageList)
            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }


    override fun onItemClick(position: Int, imagePath: String, name: String) {
        PopUpWIndow(context, R.layout.layout_image_fullview, view, imagePath, null, name, position)
    }

    override fun selectedDate(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            viewBinding.toDateText.setText(showingDate)

        }
    }

    override fun onBackPressed(): Boolean {
        val gst = viewBinding.selectDepartment.text.toString().trim()
        val categoryName = viewBinding.selectCategory.text.toString().trim()
        val itemName = viewBinding.itemName.text.toString().trim()
        val manufDate = viewBinding.fromDateText.text.toString().trim()
        val remarks = viewBinding.selectRemarks.text.toString().trim()
        val expDate = viewBinding.toDateText.text.toString().trim()
        val batchNo = viewBinding.batchNo.text.toString().trim()
        val mrp = viewBinding.mrpp.text.toString().trim()
        val purchasePrice = viewBinding.purchasePrice.text.toString().trim()
        val hsnCode = viewBinding.hsnCode.text.toString().trim()
        val packsize = viewBinding.packsize.text.toString().trim()
        val site = viewBinding.siteIdSelect.text.toString().trim()
        val barCode = viewBinding.barCode.text.toString().trim()
        val description = viewBinding.descriptionText.text.toString().trim()

        return site.isNotEmpty() || categoryName.isNotEmpty() || itemName.isNotEmpty() || packsize.isNotEmpty() ||
                mrp.isNotEmpty() || purchasePrice != "0" || remarks.isNotEmpty() || batchNo.isNotEmpty() || manufDate.isNotEmpty() ||
                expDate.isNotEmpty() || barCode != "0" || hsnCode != "0" || gst != "0" || description.isNotEmpty() || imageList.isNotEmpty()
    }

    override fun selectDoctorSpecialiity(row: ItemTypeDropDownResponse.Rows) {
        viewBinding.doctorSpecialty.setText(row.name)
        selectedDoctorSpecialityUid = row.uid
    }

    override fun selectItemType(row: ItemTypeDropDownResponse.Rows) {
        viewBinding.itemType.setText(row.name)
        selectedItemTypeUid = row.uid
    }

    override fun onSuccessItemTypeApi(itemTypeDropDownResponse: ItemTypeDropDownResponse) {
//        this.itemTypeDropDownResponse = itemTypeDropDownResponse
        Preferences.setItemTypeList(Gson().toJson(itemTypeDropDownResponse))
//        val gson = Gson()
//        val itemTypeList = Preferences.getItemTypeListJson()
//
//
//
//        val type = object : TypeToken<ItemTypeDropDownResponse>() {}.type
//        this.itemTypeDropDownResponse =
//            gson.fromJson<List<ItemTypeDropDownResponse>>(itemTypeList, type) as ItemTypeDropDownResponse
        Preferences.setItemTypeListFetched(true)

    }

    override fun onSuccessDoctorSpecialityApi(doctorSpecialityDropDownResponse: ItemTypeDropDownResponse) {
//        this.doctorSpecialityDropDownREsponse = doctorSpecialityDropDownResponse

        Preferences.setDoctorSpecialityList(Gson().toJson(doctorSpecialityDropDownResponse))
//        val gson = Gson()
//        val doctorSpecialityList = Preferences.getDoctorSpecialityListJson()
//
//
//
//        val type = object : TypeToken<ItemTypeDropDownResponse>() {}.type
//        this.doctorSpecialityDropDownREsponse =
//            gson.fromJson<List<ItemTypeDropDownResponse>>(doctorSpecialityList, type) as ItemTypeDropDownResponse
        Preferences.setDoctorSpecialityListFetched(true)

    }
}


class DrugImageRecyclerView(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner,
) :
    SimpleRecyclerView<ImageviewDrugBinding, Image>(
        orderData,
        R.layout.imageview_drug
    ) {
    override fun bindItems(
        binding: ImageviewDrugBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageBitmap(BitmapFactory.decodeFile(items.file.absolutePath))
//
//            val myBitmap: Bitmap = BitmapFactory.decodeFile(items.file
//                .absolutePath)
//            binding.image.setImageBitmap(myBitmap)
//            binding.image.setRotation(90F)
//


//        Picasso.load(Uri.parse(image.getProductGalleryImage().toString()))
//            .error(R.drawable.placeholder_image).into(holder.adapterItemPartsBinding.image)
//        Picasso.get().load(items.file).into(binding.image)
//        binding.image.setImageURI(Uri.parse(items.file.toString()))

//        Glide.with(ViswamApp.context).load(items.file.toString())
//            .placeholder(R.drawable.thumbnail_image)
//            .into(binding.image)
        binding.image.setOnClickListener {
            items.file.toString()
                .let { it1 -> imageClicklistner.onItemClick(position, it1, "Front View") }
        }

        binding.deleteImage.setOnClickListener {
            imageClicklistner.deleteImage(position)
        }
    }

    fun notifyAdapter(userList: ArrayList<Image>) {
        this.orderData = userList
        notifyDataSetChanged()
    }

    fun deleteImage(position: Int) {
        orderData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, orderData.size)


    }
}


class DrugImageRecyclerView1(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner,
) :
    SimpleRecyclerView<ImageviewDrugBinding, Image>(
        orderData,
        R.layout.imageview_drug
    ) {
    override fun bindItems(
        binding: ImageviewDrugBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.image.setOnClickListener {
            items.file.toString()
                .let { it1 -> imageClicklistner.onItemClick(position, it1, "Back View") }
        }

        binding.deleteImage.setOnClickListener {
            imageClicklistner.backdeleteImage(position)
        }
    }

    fun notifyAdapter(userList: ArrayList<Image>) {
        this.orderData = userList
        notifyDataSetChanged()
    }

    fun deleteImage(position: Int) {
        orderData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, orderData.size)


    }
}


class DrugImageRecyclerView2(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner,
) :
    SimpleRecyclerView<ImageviewDrugBinding, Image>(
        orderData,
        R.layout.imageview_drug
    ) {
    override fun bindItems(
        binding: ImageviewDrugBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.image.setOnClickListener {
            items.file.toString()
                .let { it1 -> imageClicklistner.onItemClick(position, it1, "Side View") }
        }

        binding.deleteImage.setOnClickListener {
            imageClicklistner.sidedeleteImage(position)
        }
    }

    fun notifyAdapter(userList: ArrayList<Image>) {
        this.orderData = userList
        notifyDataSetChanged()
    }

    fun deleteImage(position: Int) {
        orderData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, orderData.size)


    }
}


class DrugImageRecyclerView3(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner,
) :
    SimpleRecyclerView<ImageviewDrugBinding, Image>(
        orderData,
        R.layout.imageview_drug
    ) {
    override fun bindItems(
        binding: ImageviewDrugBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.eyeImageRes.setOnClickListener {
            items.file.toString()
                .let { it1 -> imageClicklistner.onItemClick(position, it1, "Bill View") }
        }

        binding.deleteImage.setOnClickListener {
            imageClicklistner.billdeleteImage(position)
        }
    }

    fun notifyAdapter(userList: ArrayList<Image>) {
        this.orderData = userList
        notifyDataSetChanged()
    }

    fun deleteImage(position: Int) {
        orderData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, orderData.size)


    }
}


interface ImagesListner {

    fun deleteImage(position: Int)
    fun backdeleteImage(position: Int)
    fun sidedeleteImage(position: Int)
    fun billdeleteImage(position: Int)


    fun onItemClick(position: Int, imagePath: String, name: String)

}


