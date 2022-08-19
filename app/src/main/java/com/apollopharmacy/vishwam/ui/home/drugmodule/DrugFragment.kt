package com.apollopharmacy.vishwam.ui.home.drugmodule

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.data.model.ImageFile
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.*
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugRequest
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.GstDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.SiteNewDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.SubmitDialog
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Drug() : BaseFragment<DrugFragmentViewModel, FragmentDrugBinding>(),
    ComplaintListCalendarDialog.DateSelected, ImagesListner,
    SiteNewDialog.NewDialogSiteClickListner, SubmitDialog.AbstractDialogSubmitClickListner,
    Dialog.DialogClickListner, GstDialog.GstDialogClickListner {

    lateinit var adapter: DrugImageRecyclerView
    lateinit var adapter1: DrugImageRecyclerView1
    lateinit var adapter2: DrugImageRecyclerView2
    lateinit var adapter3: DrugImageRecyclerView3


    var imageList = ArrayList<Image>();

    var frontImageList = ArrayList<Image>();
    var backImageList = ArrayList<Image>();
    var sideImageList = ArrayList<Image>();
    var billImageList = ArrayList<Image>();


    var newImageList = ArrayList<ImageFile>()

    var imagesList = ArrayList<DrugRequest.Image>()

    var imageFromCameraFile: File? = null
    var imageFromBackCameraFile: File? = null
    var imageFromSideCameraFile: File? = null
    var imageFromBillCameraFile: File? = null

    var imageFromGallery: File? = null

    var isFromDateSelected: Boolean = false

    override val layoutRes: Int
        get() = R.layout.fragment_drug

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun retrieveViewModel(): DrugFragmentViewModel {
        return ViewModelProvider(this).get(DrugFragmentViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {


        viewModel.siteId()
        viewBinding.selectCategory.setOnClickListener {
            Dialog().apply {
                arguments =
                        //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                    Dialog().generateParsedData(viewModel.getNames())
            }.show(childFragmentManager, "")


        }

        viewBinding.siteIdSelect.setOnClickListener {
            if (viewModel.getSiteData().isEmpty()) {
                viewModel.siteId()


            } else {
                SiteNewDialog().apply {
                    arguments =
                        SiteNewDialog().generateParsedData(viewModel.getSiteData())
                }.show(childFragmentManager, "")
            }
        }






        viewBinding.itemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
                    if (s.length == 1) {
                        viewBinding.MrpTextInput.error = null

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


        viewBinding.purchasePrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1) {
                        viewBinding.purchasePriceTextInput.error = null

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })


        viewBinding.barCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1) {
                        viewBinding.barCodeL.error = null

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })



        viewBinding.hsnCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length == 1) {
                        viewBinding.hsnText.error = null

                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
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













        viewBinding.selectDepartment.setOnClickListener {


            GstDialog().apply {
                arguments =
                        //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                    GstDialog().generateParsedData(viewModel.getGst())
            }.show(childFragmentManager, "")

        }


        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDate: String = simpleDateFormat.format(Date())




        viewBinding.submit.setOnClickListener {
            var name: String = "FRONT"

            if (validationCheck()) {
                showLoading()

                viewModel.drugList.observe(viewLifecycleOwner, {
                    hideLoading()
                    RefreshView()

                    SubmitDialog().apply {
                        arguments =
                            SubmitDialog().generateParsedData(it)
                    }.show(childFragmentManager, "")

                })



                viewModel.connectToAzure(imageList)
                viewModel.commands.observe(viewLifecycleOwner) {
                    when (it) {
                        is DrugFragmentViewModel.Commands.DrugImagesUploadInAzur -> {
                            for (i in it.filePath.indices) {
                                imagesList.add(
                                    DrugRequest.Image(
                                        name,
                                        Base64.getEncoder()
                                            .encodeToString(it.filePath[i].base64Images.toByteArray()),
                                        it.filePath[i].file.name,
                                        "image/" + imageType(it.filePath[i].file).toString(),

                                        it.filePath[i].file.length().toString()
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
                        }
                    }


                    viewModel.getDrugList(
                        DrugRequest(
                            viewBinding.siteIdSelect.getText().toString(),
                            viewBinding.selectCategory.getText().toString(),
                            viewBinding.itemName.getText().toString(),
                            viewBinding.batchNo.getText().toString(),
                            viewBinding.packsize.getText().toString(),
                            viewBinding.mrpp.getText().toString(),
                            viewBinding.purchasePrice.getText().toString(),
                            viewBinding.fromDateText.getText().toString(),
                            viewBinding.toDateText.getText().toString(),
                            viewBinding.barCode.getText().toString(),
                            viewBinding.hsnCode.getText().toString(),
                            viewBinding.selectDepartment.getText().toString(),
                            Preferences.getSiteId(),
                            currentDate,
                            "0",
                            "0",


                            "",
                            "",
                            Preferences.getToken(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",

                            "",
                            "",
                            "",
                            "",
                            imagesList
                        )
                    )


//            if (validationCheck()){

                }
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
            isFromDateSelected = false
            openDateDialog()

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


    }


    private fun cameraIntent() {
        if (frontImageList.size < 1) {
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
        } else {
            Toast.makeText(
                requireContext(),
                "You Already Uploaded All Images",
                Toast.LENGTH_SHORT
            )
                .show()
        }


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
    }


    private fun backCameraIntent() {
        if (imageList.size <= 3) {
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
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            startActivityForResult(intent, Config.REQUEST_BACK_CAMERA)
        } else {
            Toast.makeText(
                requireContext(),
                "You Already Uploaded All Images",
                Toast.LENGTH_SHORT
            )
                .show()
        }


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
    }


    private fun sideCameraIntent() {
        if (imageList.size <= 3) {
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
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, Config.REQUEST_SIDE_CAMERA)
        } else {
            Toast.makeText(
                requireContext(),
                "You Already Uploaded All Images",
                Toast.LENGTH_SHORT
            )
                .show()
        }


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
    }


    private fun billCameraIntent() {
        if (imageList.size <= 3) {
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
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, Config.REQUEST_BILL_CAMERA)
        } else {
            Toast.makeText(
                requireContext(),
                "You Already Uploaded All Images",
                Toast.LENGTH_SHORT
            )
                .show()
        }


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
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


    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)


    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==Config.REQUEST_CODE_CAMERA) {
            if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
                frontImageList.add(Image(imageFromCameraFile!!, "", "Front"))
                imageList.add(Image(imageFromCameraFile!!,"",""))

                    viewBinding.imageRecyclerView.visibility = View.VISIBLE
                    viewBinding.addImage.visibility = View.GONE

                adapter.notifyAdapter(frontImageList)

            }
        }
        else if (requestCode == Config.REQUEST_BACK_CAMERA) {
            if (requestCode == Config.REQUEST_BACK_CAMERA && imageFromBackCameraFile != null && resultCode == Activity.RESULT_OK) {
                backImageList.add(Image(imageFromBackCameraFile!!, "", "Back"))
                imageList.add(Image(imageFromBackCameraFile!!,"",""))

                viewBinding.imageRecyclerView1.visibility = View.VISIBLE
                viewBinding.addImage1.visibility = View.GONE

                adapter1.notifyAdapter(backImageList)
            }
        } else if (requestCode == Config.REQUEST_SIDE_CAMERA ) {

            if (requestCode == Config.REQUEST_SIDE_CAMERA && imageFromSideCameraFile != null && resultCode == Activity.RESULT_OK) {

                sideImageList.add(Image(imageFromSideCameraFile!!, "", "Side"))
                imageList.add(Image(imageFromSideCameraFile!!,"",""))

                viewBinding.imageRecyclerView2.visibility = View.VISIBLE
                viewBinding.addImage2.visibility = View.GONE



                adapter2.notifyAdapter(sideImageList)
            }

        } else if (requestCode == Config.REQUEST_BILL_CAMERA){
            if (requestCode == Config.REQUEST_BILL_CAMERA && imageFromBillCameraFile != null && resultCode == Activity.RESULT_OK) {

                billImageList.add(Image(imageFromBillCameraFile!!, "", "Bill"))
                imageList.add(Image(imageFromBillCameraFile!!,"",""))

                viewBinding.imageRecyclerView4.visibility = View.VISIBLE
                viewBinding.addImage3.visibility = View.GONE

                adapter3.notifyAdapter(billImageList)

            }
        }


    }


    private fun showErrorMsg(errMsg: String?) {
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun validationCheck(): Boolean {
        val gst = viewBinding.selectDepartment.text.toString().trim()
        val categoryName = viewBinding.selectCategory.text.toString().trim()
        val itemName = viewBinding.itemName.text.toString().trim()
        val manufDate = viewBinding.fromDateText.text.toString().trim()
        val expDate = viewBinding.toDateText.text.toString().trim()
        val batchNo = viewBinding.batchNo.text.toString().trim()
        val mrp = viewBinding.mrpp.text.toString().trim()
        val purchasePrice = viewBinding.purchasePrice.text.toString().trim()
        val hsnCode = viewBinding.hsnCode.text.toString().trim()
        val packsize = viewBinding.packsize.text.toString().trim()
        val site = viewBinding.siteIdSelect.text.toString().trim()
        val barCode = viewBinding.barCode.text.toString().trim()

        if (site.isEmpty()) {

            showErrorMsg(
                "Please Select Site id"
            )
            return false

        } else if (categoryName.isEmpty()) {

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_cat)
            )
            return false
        } else if (itemName.isEmpty()) {
            viewBinding.itemName.requestFocus()
            viewBinding.branchNameTextInput.error = "Please Enter Item Name"


            return false
        } else if (manufDate.isEmpty()) {

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_mnfDate)
            )
            return false
        } else if (expDate.isEmpty()) {


            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_expiry_date)
            )
            return false
        }
//
        else if (batchNo.isEmpty()) {
            viewBinding.BatchTextInput.requestFocus()

            viewBinding.BatchTextInput.error = "Please Enter Batch No"


            return false
        } else if (mrp.isEmpty()) {
            viewBinding.MrpTextInput.requestFocus()
            viewBinding.MrpTextInput.error = "Please Enter Mrp Price"


            return false
        } else if (purchasePrice.isEmpty()) {
            viewBinding.purchasePriceTextInput.requestFocus()

            viewBinding.purchasePriceTextInput.error = "Please Enter Purchase Price"

            return false
        } else if (barCode.isEmpty()) {
            viewBinding.barCodeL.requestFocus()

            viewBinding.barCodeL.error = "Please Enter Barcode No"


            return false
        } else if (hsnCode.isEmpty()) {
            viewBinding.hsnText.requestFocus()

            viewBinding.hsnText.error = "Please Enter HSN Code"
            return false
        } else if (gst.isEmpty()) {


            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_gst)
            )
            return false
        } else if (packsize.isEmpty()) {
            viewBinding.pasckizel.requestFocus()

            viewBinding.pasckizel.error = "Please Enter Pack Size"

            return false

        } else {
            if (frontImageList.isEmpty()||backImageList.isEmpty()|| billImageList.isEmpty()||sideImageList.isEmpty()) {

                showErrorMsg(
                    "Please Upload All Images"
                )
                return false
            }
        }
        return true
    }


    override fun selectDepartment(departmentDto: String) {
        viewBinding.selectCategory.setText(departmentDto)
        viewBinding.selectCategoryText.error = null
    }

    override fun selectSite(departmentDto: StoreListItem) {

        viewBinding.siteIdSelect.setText(departmentDto.site + "," + departmentDto.store_name)
        viewBinding.siteId.error = null
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
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    viewBinding.toDateText.text.toString(),
                    false,
                    viewBinding.toDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        }
    }

    fun RefreshView() {


        viewBinding.selectDepartment.setText("")
        viewBinding.selectCategory.setText("")
        viewBinding.itemName.setText("")
        viewBinding.fromDateText.setText("")
        viewBinding.toDateText.setText("")
        viewBinding.batchNo.setText("")
        viewBinding.mrpp.setText("")
        viewBinding.purchasePrice.setText("")
        viewBinding.hsnCode.setText("")
        viewBinding.packsize.setText("")
        viewBinding.siteIdSelect.setText("")
        viewBinding.barCode.setText("")


        viewBinding.siteId.error = null
        viewBinding.selectCategoryText.error = null
        viewBinding.branchNameTextInput.error = null
        viewBinding.fromDate.error = null
        viewBinding.toDate.error = null
        viewBinding.BatchTextInput.error = null
        viewBinding.MrpTextInput.error = null
        viewBinding.purchasePriceTextInput.error = null
        viewBinding.hsnText.error = null
        viewBinding.pasckizel.error = null
        viewBinding.siteIdSelect.error = null
        viewBinding.barCodeL.error = null
        viewBinding.gst.error = null
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

    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            viewBinding.fromDateText.setText(showingDate)
            viewBinding.fromDate.error = null

        } else {
            viewBinding.toDateText.setText(showingDate)
            viewBinding.toDate.error = null
        }
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


    override fun onItemClick(position: Int, imagePath: String) {
        PhotoPopupWindow(context, R.layout.layout_image_fullview, view, imagePath, null)
    }
}


class DrugImageRecyclerView(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner
) :
    SimpleRecyclerView<ViewimageBinding, Image>(
        orderData,
        R.layout.viewimage
    ) {
    override fun bindItems(
        binding: ViewimageBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.image.setOnClickListener {
            items.file.toString()?.let { it1 -> imageClicklistner.onItemClick(position, it1) }
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
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderData.size)


    }
}


class DrugImageRecyclerView1(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner
) :
    SimpleRecyclerView<ViewimageBinding, Image>(
        orderData,
        R.layout.viewimage
    ) {
    override fun bindItems(
        binding: ViewimageBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.image.setOnClickListener {
            items.file.toString()?.let { it1 -> imageClicklistner.onItemClick(position, it1) }
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
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderData.size)


    }
}


class DrugImageRecyclerView2(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner
) :
    SimpleRecyclerView<ViewimageBinding, Image>(
        orderData,
        R.layout.viewimage
    ) {
    override fun bindItems(
        binding: ViewimageBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.image.setOnClickListener {
            items.file.toString()?.let { it1 -> imageClicklistner.onItemClick(position, it1) }
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
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderData.size)


    }
}


class DrugImageRecyclerView3(
    var orderData: ArrayList<Image>,
    val imageClicklistner: ImagesListner
) :
    SimpleRecyclerView<ViewimageBinding, Image>(
        orderData,
        R.layout.viewimage
    ) {
    override fun bindItems(
        binding: ViewimageBinding,
        items: Image,
        position: Int,
    ) {

        binding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(binding.image)
        binding.image.setOnClickListener {
            items.file.toString()?.let { it1 -> imageClicklistner.onItemClick(position, it1) }
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
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderData.size)


    }
}


interface ImagesListner {

    fun deleteImage(position: Int)
    fun backdeleteImage(position: Int)
    fun sidedeleteImage(position: Int)
    fun billdeleteImage(position: Int)


    fun onItemClick(position: Int, imagePath: String)

}


