package com.apollopharmacy.vishwam.ui.home.drugmodule

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.*
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugRequest
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.GstDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.SiteNewDialog
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.SubmitDialog
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.bumptech.glide.Glide
import okhttp3.internal.notify
import java.io.File
import java.io.FileFilter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Drug() : BaseFragment<DrugFragmentViewModel, FragmentDrugBinding>(),
    ComplaintListCalendarDialog.DateSelected, ImagesListner,
    SiteNewDialog.NewDialogSiteClickListner, SubmitDialog.AbstractDialogSubmitClickListner,
    Dialog.DialogClickListner, GstDialog.GstDialogClickListner {

    lateinit var adapter: DrugImageRecyclerView

    var imageList = ArrayList<Image>();


    var imagesList = ArrayList<DrugRequest.Image>()

    var imageFromCameraFile: File? = null
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


        adapter = DrugImageRecyclerView(imageList, this)
        viewBinding.imageRecyclerView.adapter = adapter










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
                cameraIntent()

        }

        viewBinding.addImage2.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else
                cameraIntent()

        }
        viewBinding.addImage3.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else
                cameraIntent()

        }


    }


    private fun cameraIntent() {
        if (imageList.size <= 3) {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
            imageList.add(Image(imageFromCameraFile!!, "", ""))
            adapter.notifyAdapter(imageList)

            if (imageList.size == 4) {
                viewBinding.addImage.visibility = View.GONE
                viewBinding.addImage1.visibility = View.GONE
                viewBinding.addImage2.visibility = View.GONE
                viewBinding.addImage3.visibility = View.GONE

            }

        }
    }


    private fun showErrorMsg(errMsg: String?) {
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
    }


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
            viewBinding.siteIdSelect.requestFocus()
            viewBinding.siteIdSelect.error = "Please Select Site id"
            showErrorMsg(context?.resources?.getString(R.string.err_msg_select_site))
            return false

        } else if (categoryName.isEmpty()) {
            viewBinding.selectCategory.requestFocus()
            viewBinding.selectCategory.error = "Please Select Category "

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_cat)
            )
            return false
        } else if (itemName.isEmpty()) {
            viewBinding.itemName.requestFocus()
            viewBinding.itemName.error = "Please Enter Item Name"

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_nam)
            )
            return false
        } else if (manufDate.isEmpty()) {
            viewBinding.fromDateText.requestFocus()
            viewBinding.fromDateText.error = "Please Select Date"
            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_mnfDate)
            )
            return false
        } else if (expDate.isEmpty()) {
            viewBinding.toDateText.requestFocus()
            viewBinding.toDateText.error = "Please Select Date"
            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_expiry_date)
            )
            return false
        }
//
        else if (batchNo.isEmpty()) {
            viewBinding.batchNo.requestFocus()

            viewBinding.batchNo.error = "Please Enter Batch No"

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_batc)
            )
            return false
        } else if (mrp.isEmpty()) {
            viewBinding.mrpp.requestFocus()
            viewBinding.mrpp.error = "Please Enter Mrp Price"

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_mrp)
            )
            return false
        } else if (purchasePrice.isEmpty()) {
            viewBinding.purchasePrice.requestFocus()

            viewBinding.purchasePrice.error = "Please Enter Purchase Price"

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_purprice)
            )
            return false
        } else if (barCode.isEmpty()) {
            viewBinding.barCode.requestFocus()

            viewBinding.barCode.error = "Please Enter Barcode No"

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_bar)
            )
            return false
        } else if (hsnCode.isEmpty()) {
            viewBinding.hsnCode.requestFocus()

            viewBinding.hsnCode.error = "Please Enter HSN Code"

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_hsn)
            )
            return false
        } else if (gst.isEmpty()) {
            viewBinding.selectDepartment.requestFocus()
            viewBinding.selectDepartment.error = "Please Select Gst"


            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_gst)
            )
            return false
        } else if (packsize.isEmpty()) {
            viewBinding.packsize.requestFocus()

            viewBinding.packsize.error = "Please Enter Pack Size"

            showErrorMsg(
                context?.resources?.getString(R.string.err_msg_select_packsize)
            )
            return false

        } else {
            if (imageList.size <= 3) {

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
        viewBinding.selectCategory.error = null
    }

    override fun selectSite(departmentDto: StoreListItem) {

        viewBinding.siteIdSelect.setText(departmentDto.site + "," + departmentDto.store_name)
        viewBinding.siteIdSelect.error = null
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
        adapter.notifyAdapter(imageList)
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            viewBinding.fromDateText.setText(showingDate)
            viewBinding.fromDateText.error = null

        } else {
            viewBinding.toDateText.setText(showingDate)
            viewBinding.toDateText.error = null
        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
    }

    override fun selectGST(gst: String) {
        viewBinding.selectDepartment.setText(gst)
        viewBinding.selectDepartment.error = null
    }

    override fun confirmsavetheticket() {
        RefreshView()
    }

    override fun deleteImage(position: Int) {



        val dialogBinding: DialogDeleteBinding? =
            DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dialog_delete, null, false)
        val customDialog = AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {
            if (imageList.size < 5) {
                viewBinding.addImage.visibility = View.VISIBLE
                viewBinding.addImage1.visibility = View.VISIBLE
                viewBinding.addImage2.visibility = View.VISIBLE
                viewBinding.addImage3.visibility = View.VISIBLE
            }
            adapter.deleteImage(position)
            adapter.notifyAdapter(imageList)
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


interface ImagesListner {

    fun deleteImage(position: Int)

    fun onItemClick(position: Int, imagePath: String)

}


