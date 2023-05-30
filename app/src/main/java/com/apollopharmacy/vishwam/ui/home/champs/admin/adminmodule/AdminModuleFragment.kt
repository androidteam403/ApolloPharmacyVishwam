package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityAdminModuleBinding
import com.apollopharmacy.vishwam.databinding.DialogEditRangeChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.adapter.GetCategoryDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.SaveCategoryConfigurationDetailsRequest
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.SaveCategoryConfigurationDetailsResponse

class AdminModuleFragment : BaseFragment<AdminModuleViewModel, ActivityAdminModuleBinding>(),
    AdminModuleCallBack {
    var isRotated: Boolean = false
    private lateinit var dialogEditRangeChampsBinding: DialogEditRangeChampsBinding


    // written by naveen
    var categoryDetailsList: ArrayList<GetCategoryDetailsResponse.CategoryDetails>? = null
    var getCategoryDetailsAdapter: GetCategoryDetailsAdapter? = null
    var subCategoryDetailsList: List<GetSubCategoryDetailsResponse.SubCategoryDetails>? = null
    var subCategoryDetailsListList =
        ArrayList<ArrayList<GetSubCategoryDetailsResponse.SubCategoryDetails>>()


    override val layoutRes: Int
        get() = R.layout.activity_admin_module

    override fun retrieveViewModel(): AdminModuleViewModel {
        return ViewModelProvider(this).get(AdminModuleViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        var prevValue = viewBinding.seekbar1.progress

        viewBinding.seekbar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                var updatedValue = viewBinding.seekbar1.progress
                var orangeValue = prevValue - updatedValue

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        showLoading()
        viewModel.getCategoryDetailsApiCall(this@AdminModuleFragment)
    }

    override fun onClickExpand() {
        if (!isRotated) {
            viewBinding.arrowImage.rotation = 450F
            viewBinding.cleanlinessExpandLayout.visibility = View.VISIBLE
            isRotated = true
        } else {
            viewBinding.arrowImage.rotation = 0F
            viewBinding.cleanlinessExpandLayout.visibility = View.GONE
            isRotated = false
        }

    }

    override fun onClickEdit() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_edit_range_champs,
            null,
            false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if (dialogEditRangeChampsBinding.enterPoints.text.toString() > "5") {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else if (dialogEditRangeChampsBinding.enterPoints.text.toString() <= "0") {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints * 2
                viewBinding.seekbar1.progress = (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditOverall(
        categoryDetails: GetCategoryDetailsResponse.CategoryDetails,
        sumOfSubCategoryMaxRatings: Double, categoryPos: String,
    ) {
        if (categoryDetails != null) {
            val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
            dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_edit_range_champs,
                null,
                false)
            editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
            if (editBoxDialog.getWindow() != null) {
                editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            editBoxDialog.setCancelable(false)
            dialogEditRangeChampsBinding.enterPoints.setText(categoryDetails.rating)
            dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->
                var sumOfAllCategoriesRating = 0.0
                for (i in categoryDetailsList!!) {
                    if (categoryDetailsList!!.indexOf(i) == (categoryPos.toInt() - 1)) {
                        sumOfAllCategoriesRating =
                            sumOfAllCategoriesRating + dialogEditRangeChampsBinding.enterPoints.text.toString()
                                .toDouble()
                    } else {
                        sumOfAllCategoriesRating = sumOfAllCategoriesRating + i.rating!!.toDouble()
                    }
                }
                if (sumOfAllCategoriesRating <= 100) {
                    if (dialogEditRangeChampsBinding.enterPoints.text.toString()
                            .toDouble() >= categoryDetails.sumOfSubCategoryRating
                    ) {
                        categoryDetails.rating =
                            dialogEditRangeChampsBinding.enterPoints.text.toString()
                        editBoxDialog.dismiss()
                        getCategoryDetailsAdapter!!.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context,
                            "Maximum rating should not less than sum of all sub categories rating.",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context,
                        "Sum of categories rating should less than or equal to 100.",
                        Toast.LENGTH_SHORT).show()
                }
            }
            dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
                editBoxDialog.dismiss()
            }
            editBoxDialog.show()
        }
    }

    override fun onClickEditOverallHospitality() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_edit_range_champs,
            null,
            false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
        dialogEditRangeChampsBinding.headingForDialog.text = "HOSPITALITY"
        dialogEditRangeChampsBinding.enterPoints.setText(viewBinding.overallHospitality.text.toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) > 15) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) < 0) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                viewBinding.overallHospitality.text =
                    dialogEditRangeChampsBinding.enterPoints.text.toString()
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }

        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub1() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_edit_range_champs,
            null,
            false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Overall Appearance of the Store"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar1.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) > 5) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) < 0) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints * 2
                viewBinding.seekbar1.progress = (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub2() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_edit_range_champs,
            null,
            false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Offer Display"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar2.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) > 2.5) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) < 0) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints * 2
                viewBinding.seekbar2.progress = (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub3() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_edit_range_champs,
            null,
            false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Store Frontage"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar3.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) > 2.5) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) < 0) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints * 2
                viewBinding.seekbar3.progress = (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onClickEditSub4() {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_edit_range_champs,
            null,
            false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)
        dialogEditRangeChampsBinding.headingForDialog.text = "Grooming of the staff"
        dialogEditRangeChampsBinding.enterPoints.setText(getConvertedValue(viewBinding.seekbar4.progress).toString())

        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->

            if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) > 2.5) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else if ((dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat()) < 0) {
                Toast.makeText(context,
                    "Please enter a value with in the range",
                    Toast.LENGTH_SHORT).show()
            } else {
                val enterPoints =
                    (dialogEditRangeChampsBinding.enterPoints.text.toString().toFloat())
                val mulPoints = enterPoints * 2
                viewBinding.seekbar4.progress = (mulPoints.toInt())
                editBoxDialog.dismiss()
            }
//            val simpleSeekBar: SeekBar = (R.id.seekbar1) as SeekBar

        }




        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }


        editBoxDialog.show()
    }

    override fun onSuccessGetCategoryDetailsApiCall(getCategoryDetailsResponse: GetCategoryDetailsResponse) {
        hideLoading()
        if (getCategoryDetailsResponse.categoryDetails != null && getCategoryDetailsResponse.categoryDetails!!.size > 0) {
            categoryDetailsList =
                getCategoryDetailsResponse.categoryDetails as ArrayList<GetCategoryDetailsResponse.CategoryDetails>?
            getCategoryDetailsAdapter = GetCategoryDetailsAdapter(categoryDetailsList!!,
                context,
                this@AdminModuleFragment,
                subCategoryDetailsList)
            var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewBinding.getCategoryDetailsRecyclerView.layoutManager = layoutManager
            viewBinding.getCategoryDetailsRecyclerView.adapter = getCategoryDetailsAdapter
        }
    }

    override fun onFailureGetCategoryDetailsApiCall(message: String) {
        hideLoading()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickSubCategoryDetailsItem(categoryDetails: GetCategoryDetailsResponse.CategoryDetails) {
        if (subCategoryDetailsListList != null && subCategoryDetailsListList.size > 0) {
            var isSubCategoryDetailsPresent = false
            for (i in subCategoryDetailsListList) {
                for (j in subCategoryDetailsListList.get(subCategoryDetailsListList.indexOf(i))) {
                    if (j.categoryName!!.equals(categoryDetails.categoryName)) {
                        isSubCategoryDetailsPresent = true
                        subCategoryDetailsList =
                            subCategoryDetailsListList.get(subCategoryDetailsListList.indexOf(i))
                        break
                    }
                }
            }

            if (isSubCategoryDetailsPresent) {
                if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
                    var itemPos: Int = -1
                    for (i in categoryDetailsList!!) {
                        if (i.categoryName.equals(categoryDetails.categoryName)) {
                            i.isItemExpanded = !i.isItemExpanded!!
                            itemPos = categoryDetailsList!!.indexOf(i)
                            break
                        }
                    }
                    getCategoryDetailsAdapter!!.subCategoryDetailsList = subCategoryDetailsList
                    getCategoryDetailsAdapter!!.notifyItemChanged(itemPos)
                }
            } else {
                showLoading()
                viewModel.getSubCategoryDetailsApiCall(this@AdminModuleFragment,
                    categoryDetails.categoryName!!)
            }
        } else {
            showLoading()
            viewModel.getSubCategoryDetailsApiCall(this@AdminModuleFragment,
                categoryDetails.categoryName!!)
        }

    }

    override fun onSuccessGetSubCategoryDetailsApiCall(
        getSubCategoryDetailsResponse: GetSubCategoryDetailsResponse,
        categoryName: String,
    ) {
        hideLoading()
        if (getSubCategoryDetailsResponse.subCategoryDetails != null && getSubCategoryDetailsResponse.subCategoryDetails!!.size > 0) {
            subCategoryDetailsList = getSubCategoryDetailsResponse.subCategoryDetails!!
            subCategoryDetailsListList.add(subCategoryDetailsList!! as ArrayList<GetSubCategoryDetailsResponse.SubCategoryDetails>)
            if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
                var itemPos: Int = -1
                for (i in categoryDetailsList!!) {
                    if (i.categoryName.equals(categoryName)) {
                        i.isItemExpanded = !i.isItemExpanded!!
                        itemPos = categoryDetailsList!!.indexOf(i)
                        break
                    }
                }
                getCategoryDetailsAdapter!!.subCategoryDetailsList = subCategoryDetailsList
                getCategoryDetailsAdapter!!.notifyItemChanged(itemPos)
            }

        }
    }

    override fun onFailureGetSubCategoryDetailsApiCall(message: String) {
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }

    override fun onClickSubmit() {
        var saveCategoryConfigurationDetailsRequest = SaveCategoryConfigurationDetailsRequest()
        var categoryDetails = ArrayList<SaveCategoryConfigurationDetailsRequest.CategoryDetail>()
        if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
            for (i in categoryDetailsList!!) {
                var categoryDetail = SaveCategoryConfigurationDetailsRequest.CategoryDetail()
                categoryDetail.categoryName = i.categoryName
                categoryDetail.rating = i.rating
                categoryDetail.modifiedBy = Preferences.getValidatedEmpId()
                categoryDetails.add(categoryDetail)
            }
        }
        saveCategoryConfigurationDetailsRequest.categoryDetails = categoryDetails
        var subCategoryDetails =
            ArrayList<SaveCategoryConfigurationDetailsRequest.SubCategoryDetail>()
        if (subCategoryDetailsListList != null && subCategoryDetailsListList.size > 0) {
            for (j in subCategoryDetailsListList) {
                if (j != null && j.size > 0) {
                    for (k in j) {
                        var subCategoryDetail =
                            SaveCategoryConfigurationDetailsRequest.SubCategoryDetail()
                        subCategoryDetail.categoryName = k.categoryName
                        subCategoryDetail.subCategoryName = k.subCategoryName
                        subCategoryDetail.rating = k.rating
                        subCategoryDetail.modifiedBy = Preferences.getValidatedEmpId()
                        subCategoryDetails.add(subCategoryDetail)
                    }
                }
            }
        }
        saveCategoryConfigurationDetailsRequest.subCategoryDetails = subCategoryDetails
        showLoading()
        viewModel.saveCategoryConfigurationDetailsApiCall(this@AdminModuleFragment,
            saveCategoryConfigurationDetailsRequest)
    }

    override fun onClickEditSubCategoryRatingRange(
        subCategoryDetails: GetSubCategoryDetailsResponse.SubCategoryDetails,
        categoryPosition: Int,
    ) {
        val editBoxDialog = context?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.dialog_edit_range_champs,
            null,
            false)
        editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
        if (editBoxDialog.getWindow() != null) {
            editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        editBoxDialog.setCancelable(false)
        dialogEditRangeChampsBinding.headingForDialog.text = subCategoryDetails.subCategoryName
        dialogEditRangeChampsBinding.enterPoints.setText(subCategoryDetails.rating)
        dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->
            var overalRating = categoryDetailsList!!.get(categoryPosition).rating!!.toInt()
            var sumOfSubCategoryRating = 0.0
            var subCategoryDetailsListTemp =
                ArrayList<GetSubCategoryDetailsResponse.SubCategoryDetails>()
            for (i in subCategoryDetailsListList) {
                for (j in i) {
                    if (j.categoryName.equals(subCategoryDetails.categoryName)) {
                        subCategoryDetailsListTemp = i
                        break
                    }
                }
            }

            for (i in subCategoryDetailsListTemp) {
                if (i.subCategoryName.equals(subCategoryDetails.subCategoryName)) {
                    sumOfSubCategoryRating =
                        sumOfSubCategoryRating + dialogEditRangeChampsBinding.enterPoints.text.toString()
                            .toDouble()
                } else {
                    sumOfSubCategoryRating = sumOfSubCategoryRating + i.rating!!.toDouble()
                }
            }

            if (sumOfSubCategoryRating <= overalRating) {
                subCategoryDetails.rating = dialogEditRangeChampsBinding.enterPoints.text.toString()
                editBoxDialog.dismiss()
                getCategoryDetailsAdapter!!.notifyItemChanged(categoryPosition)
            } else {
                Toast.makeText(context,
                    "Sum of sub categories rating should less than overall rating.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
            editBoxDialog.dismiss()
        }
        editBoxDialog.show()
    }

    override fun onSuccessSaveCategoryConfigurationDetailsApiCAll(
        saveCategoryConfigurationDetailsResponse: SaveCategoryConfigurationDetailsResponse,
    ) {
        hideLoading()
        Toast.makeText(context,
            saveCategoryConfigurationDetailsResponse.message,
            Toast.LENGTH_SHORT).show()

    }

    override fun onFailureSaveCategoryConfigurationDetails(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
}