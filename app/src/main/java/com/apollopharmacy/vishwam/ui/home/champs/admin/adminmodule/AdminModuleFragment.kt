package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityAdminModuleBinding
import com.apollopharmacy.vishwam.databinding.ActivityPreviewBinding
import com.apollopharmacy.vishwam.databinding.DialogEditRangeChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.adapter.GetCategoryDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.SaveCategoryConfigurationDetailsRequest
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.SaveCategoryConfigurationDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.PreviewActivityViewModel
import com.apollopharmacy.vishwam.util.Utlis

class AdminModuleFragment : AppCompatActivity(),
    AdminModuleCallBack {
    var isRotated: Boolean = false
    private lateinit var activityAdminModuleBinding: ActivityAdminModuleBinding
    private lateinit var adminModuleViewModel: AdminModuleViewModel
    private lateinit var dialogEditRangeChampsBinding: DialogEditRangeChampsBinding


    // written by naveen
    var categoryDetailsList: ArrayList<GetCategoryDetailsResponse.CategoryDetails>? = null
    var getCategoryDetailsAdapter: GetCategoryDetailsAdapter? = null
    var subCategoryDetailsList: List<GetSubCategoryDetailsResponse.SubCategoryDetails>? = null
    var subCategoryDetailsListList =
        ArrayList<ArrayList<GetSubCategoryDetailsResponse.SubCategoryDetails>>()
    var sumEquals100: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_preview)

        activityAdminModuleBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_admin_module
        )

        adminModuleViewModel = ViewModelProvider(this)[AdminModuleViewModel::class.java]
        setup()

    }


    fun setup() {
        activityAdminModuleBinding.callback = this
        var prevValue = activityAdminModuleBinding.seekbar1.progress

        activityAdminModuleBinding.seekbar1.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                var updatedValue = activityAdminModuleBinding.seekbar1.progress
                var orangeValue = prevValue - updatedValue

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        Utlis.showLoading(this)
        adminModuleViewModel.getCategoryDetailsApiCall(this@AdminModuleFragment)
    }

    override fun onClickExpand() {
        if (!isRotated) {
            activityAdminModuleBinding.arrowImage.rotation = 450F
            activityAdminModuleBinding.cleanlinessExpandLayout.visibility = View.VISIBLE
            isRotated = true
        } else {
            activityAdminModuleBinding.arrowImage.rotation = 0F
            activityAdminModuleBinding.cleanlinessExpandLayout.visibility = View.GONE
            isRotated = false
        }

    }

    override fun onClickBack() {
        super.onBackPressed()
    }


    var categoryPosForUpdate: String? = ""
    override fun onClickEditOverall(
        categoryDetails: GetCategoryDetailsResponse.CategoryDetails,
        sumOfSubCategoryMaxRatings: Double, categoryPos: String, categoryName: String,
    ) {
        if (categoryDetails != null) {
            categoryPosForUpdate = categoryPos
            val editBoxDialog =
                applicationContext?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
            dialogEditRangeChampsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.dialog_edit_range_champs,
                null,
                false
            )
            editBoxDialog!!.setContentView(dialogEditRangeChampsBinding.getRoot())
            if (editBoxDialog.getWindow() != null) {
                editBoxDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            editBoxDialog.setCancelable(false)
            dialogEditRangeChampsBinding.enterPoints.setText(categoryDetails.sumOfSubCategoryRating.toString())
            dialogEditRangeChampsBinding.headingForDialog.setText(categoryName)
            dialogEditRangeChampsBinding.continueChamps.setOnClickListener { view ->
                var sumOfAllCategoriesRating = 0.0
                for (i in categoryDetailsList!!.indices) {
                    if (categoryDetailsList!!.get(i).categoryName == categoryName) {
                        sumOfAllCategoriesRating =
                            sumOfAllCategoriesRating + dialogEditRangeChampsBinding.enterPoints.text.toString()
                                .toDouble()
                    } else {
                        sumOfAllCategoriesRating =
                            sumOfAllCategoriesRating + categoryDetailsList!!.get(i).rating!!.toDouble()
                    }
                }
                if (sumOfAllCategoriesRating <= 100) {
                    if (dialogEditRangeChampsBinding.enterPoints.text.toString()
                            .toDouble() >= categoryDetails.sumOfSubCategoryRating
                    ) {

                        categoryDetails.sumOfSubCategoryRating =
                            (dialogEditRangeChampsBinding.enterPoints.text.toString()).toDouble()

                        editBoxDialog.dismiss()
                        getCategoryDetailsAdapter!!.notifyDataSetChanged()
//                        getCategoryDetailsAdapter!!.getEditBoxValue( (dialogEditRangeChampsBinding.enterPoints.text.toString()).toDouble())
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Maximum rating should not less than sum of all sub categories rating.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Sum of categories rating should less than or equal to 100.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dialogEditRangeChampsBinding.closeAddressDialog.setOnClickListener { view ->
                editBoxDialog.dismiss()
            }
            editBoxDialog.show()
        }
    }


    override fun onSuccessGetCategoryDetailsApiCall(getCategoryDetailsResponse: GetCategoryDetailsResponse) {
        Utlis.hideLoading()
        if (getCategoryDetailsResponse.categoryDetails != null && getCategoryDetailsResponse.categoryDetails!!.size > 0) {

            categoryDetailsList =
                getCategoryDetailsResponse.categoryDetails as ArrayList<GetCategoryDetailsResponse.CategoryDetails>?

            Utlis.showLoading(this)
            subCategoryApiHitting()
//            for (i in categoryDetailsList!!) {
//
//
//                viewModel.getSubCategoryDetailsApiCall(
//                    this@AdminModuleFragment,
//                    i.categoryName!!
//                )
//            }
//            getCategoryDetailsAdapter = GetCategoryDetailsAdapter(categoryDetailsList!!,
//                context,
//                this@AdminModuleFragment,
//                 categoryPosForUpdate)
//            var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            viewBinding.getCategoryDetailsRecyclerView.layoutManager = layoutManager
//            viewBinding.getCategoryDetailsRecyclerView.adapter = getCategoryDetailsAdapter
        }
    }

    fun subCategoryApiHitting() {

        var isAllSubCategoriesAvailable = true
        var categoryName = ""
        for (i in categoryDetailsList!!) {
//            showLoading()
            if (!i.isHavingSubCategoryDetails) {
                isAllSubCategoriesAvailable = false
                categoryName = i.categoryName!!
                break
            }

        }

        if (isAllSubCategoriesAvailable) {
            getCategoryDetailsAdapter = GetCategoryDetailsAdapter(
                categoryDetailsList!!,
                applicationContext,
                this@AdminModuleFragment,
                categoryPosForUpdate
            )
            var layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            activityAdminModuleBinding.getCategoryDetailsRecyclerView.layoutManager = layoutManager
            activityAdminModuleBinding.getCategoryDetailsRecyclerView.adapter =
                getCategoryDetailsAdapter
            onValidateTotalSum(categoryDetailsList)

            Utlis.hideLoading()
        } else {
            adminModuleViewModel.getSubCategoryDetailsApiCall(
                this@AdminModuleFragment,
                categoryName
            )
        }


    }

    override fun onFailureGetCategoryDetailsApiCall(message: String) {
        Utlis.hideLoading()
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickSubCategoryDetailsItem(
        categoryDetails: GetCategoryDetailsResponse.CategoryDetails,
        itemPos: Int,
    ) {

        if (categoryDetails != null && categoryDetails.subCategoryDetailsList != null) {
            if (getCategoryDetailsAdapter != null) {
                categoryDetails.isItemExpanded = !categoryDetails.isItemExpanded!!
                getCategoryDetailsAdapter!!.notifyItemChanged(itemPos)
            }
        } else {
            Utlis.showLoading(this)
            adminModuleViewModel.getSubCategoryDetailsApiCall(
                this@AdminModuleFragment,
                categoryDetails.categoryName!!
            )
        }
//        if (subCategoryDetailsListList != null && subCategoryDetailsListList.size > 0) {
//            var isSubCategoryDetailsPresent = false
//            for (i in subCategoryDetailsListList) {
//                for (j in subCategoryDetailsListList.get(subCategoryDetailsListList.indexOf(i))) {
//                    if (j.categoryName!!.equals(categoryDetails.categoryName)) {
//                        isSubCategoryDetailsPresent = true
//                        subCategoryDetailsList =
//                            subCategoryDetailsListList.get(subCategoryDetailsListList.indexOf(i))
//                        break
//                    }
//                }
//            }
//
//            if (isSubCategoryDetailsPresent) {
//                if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
//                    var itemPos: Int = -1
//                    for (i in categoryDetailsList!!) {
//                        if (i.categoryName.equals(categoryDetails.categoryName)) {
//                            i.isItemExpanded = !i.isItemExpanded!!
//                            itemPos = categoryDetailsList!!.indexOf(i)
//                            break
//                        }
//                    }
////                    getCategoryDetailsAdapter!!.subCategoryDetailsList = subCategoryDetailsList
//                    getCategoryDetailsAdapter!!.notifyItemChanged(itemPos)
//                }
//            } else {
//                showLoading()
//                viewModel.getSubCategoryDetailsApiCall(this@AdminModuleFragment,
//                    categoryDetails.categoryName!!)
//            }
//        } else {
//            showLoading()
//            viewModel.getSubCategoryDetailsApiCall(this@AdminModuleFragment,
//                categoryDetails.categoryName!!)
//        }

    }

    override fun onSuccessGetSubCategoryDetailsApiCall(
        getSubCategoryDetailsResponse: GetSubCategoryDetailsResponse,
        categoryName: String,
    ) {

        if (getSubCategoryDetailsResponse.subCategoryDetails != null && getSubCategoryDetailsResponse.subCategoryDetails!!.size > 0) {

            for (i in categoryDetailsList!!.indices) {
                if (categoryDetailsList!!.get(i).categoryName.equals(categoryName)) {
                    categoryDetailsList!!.get(i).subCategoryDetailsList =
                        getSubCategoryDetailsResponse.subCategoryDetails
                    categoryDetailsList!!.get(i).isHavingSubCategoryDetails = true
                }
            }
//            if(isAllSubCategoriesAvailable){
//                getCategoryDetailsAdapter = GetCategoryDetailsAdapter(
//                    categoryDetailsList!!,
//                    context,
//                    this@AdminModuleFragment,
//                    categoryPosForUpdate
//                )
//                var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//                viewBinding.getCategoryDetailsRecyclerView.layoutManager = layoutManager
//                viewBinding.getCategoryDetailsRecyclerView.adapter = getCategoryDetailsAdapter
//                onValidateTotalSum(categoryDetailsList)
//
//                hideLoading()
//            }else{
            subCategoryApiHitting()
//            }

            //            subCategoryDetailsList = getSubCategoryDetailsResponse.subCategoryDetails!!
//            subCategoryDetailsListList.add(subCategoryDetailsList!! as ArrayList<GetSubCategoryDetailsResponse.SubCategoryDetails>)
//            if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
//                var itemPos: Int = -1
//                for (i in categoryDetailsList!!) {
//                    if (i.categoryName.equals(categoryName)) {
//                        i.isItemExpanded = !i.isItemExpanded!!
//                        itemPos = categoryDetailsList!!.indexOf(i)
//                        break
//                    }
//                }
//                getCategoryDetailsAdapter!!.subCategoryDetailsList = subCategoryDetailsList
//                getCategoryDetailsAdapter!!.notifyItemChanged(itemPos)
//            }


//            if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
//                var itemPos: Int = -1
//                for (i in categoryDetailsList!!) {
//                    if (i.categoryName.equals(categoryName)) {
//                        i.isItemExpanded = !i.isItemExpanded!!
//                        itemPos = categoryDetailsList!!.indexOf(i)
//                        break
//                    }
//                }
//               categoryDetailsList!!.get(itemPos).subCategoryDetailsList = getSubCategoryDetailsResponse.subCategoryDetails
////                getCategoryDetailsAdapter!!.subCategoryDetailsList = subCategoryDetailsList
////                categoryDetailsList!!.get(itemPos).isItemExpanded=!categoryDetailsList!!.get(itemPos).isItemExpanded!!
//                getCategoryDetailsAdapter!!.notifyItemChanged(itemPos)
//            }
        }
    }

    override fun onFailureGetSubCategoryDetailsApiCall(message: String) {
        Toast.makeText(applicationContext, "" + message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
    }

    override fun onClickSubmit() {
        if (sumEquals100) {


            var saveCategoryConfigurationDetailsRequest = SaveCategoryConfigurationDetailsRequest()
            var categoryDetails =
                ArrayList<SaveCategoryConfigurationDetailsRequest.CategoryDetail>()
            if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
                for (i in categoryDetailsList!!) {
                    var categoryDetail = SaveCategoryConfigurationDetailsRequest.CategoryDetail()
                    categoryDetail.categoryName = i.categoryName
                    categoryDetail.rating = i.sumOfSubCategoryRating.toString()
                    categoryDetail.modifiedBy = Preferences.getValidatedEmpId()
                    categoryDetails.add(categoryDetail)
                }
            }
            saveCategoryConfigurationDetailsRequest.categoryDetails = categoryDetails
            var subCategoryDetails =
                ArrayList<SaveCategoryConfigurationDetailsRequest.SubCategoryDetail>()
            if (categoryDetailsList != null && categoryDetailsList!!.size > 0) {
                for (i in categoryDetailsList!!) {
                    if (i.subCategoryDetailsList != null && i.subCategoryDetailsList!!.size > 0) {
                        for (j in i.subCategoryDetailsList!!) {
                            if (j != null) {
//                            for (k in j) {
                                var subCategoryDetail =
                                    SaveCategoryConfigurationDetailsRequest.SubCategoryDetail()
                                subCategoryDetail.categoryName = j.categoryName
                                subCategoryDetail.subCategoryName = j.subCategoryName
                                subCategoryDetail.rating = (j.rating!!.toFloat()).toString()
                                subCategoryDetail.modifiedBy = Preferences.getValidatedEmpId()
                                subCategoryDetails.add(subCategoryDetail)
//                            }
                            }
                        }
                    }
                }
            }

            saveCategoryConfigurationDetailsRequest.subCategoryDetails = subCategoryDetails
            Utlis.showLoading(this)
            adminModuleViewModel.saveCategoryConfigurationDetailsApiCall(
                this@AdminModuleFragment,
                saveCategoryConfigurationDetailsRequest
            )
        } else {
            Toast.makeText(
                applicationContext,
                "Sum of category rating values should be equal to 100",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClickEditSubCategoryRatingRange(
        subCategoryDetails: GetSubCategoryDetailsResponse.SubCategoryDetails,
        categoryPosition: Int,
    ) {
        val editBoxDialog = applicationContext?.let { Dialog(it, R.style.fadeinandoutcustomDialog) }
        dialogEditRangeChampsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(applicationContext),
            R.layout.dialog_edit_range_champs,
            null,
            false
        )
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
                Toast.makeText(
                    applicationContext,
                    "Sum of sub categories rating should less than overall rating.",
                    Toast.LENGTH_SHORT
                ).show()
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
       Utlis.hideLoading()
        Toast.makeText(
            applicationContext, "Rating Modified Successfully.",
            Toast.LENGTH_SHORT
        ).show()
        onBackPressed()
//saveCategoryConfigurationDetailsResponse.message
    }

    override fun onFailureSaveCategoryConfigurationDetails(message: String) {
       Utlis.hideLoading()
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onValidateTotalSum(getCategoryDetails: List<GetCategoryDetailsResponse.CategoryDetails>?) {
        var sumOfCategoryMaxRating = 0.0

        for (i in getCategoryDetails!!) {

            sumOfCategoryMaxRating = sumOfCategoryMaxRating + i.sumOfSubCategoryRating!!.toDouble()

        }
        if (sumOfCategoryMaxRating == 100.0) {
            sumEquals100 = true
            activityAdminModuleBinding.submitButtnAdmin.setBackgroundColor(applicationContext.getColor(R.color.vishwam_actionbar_color))
        } else {
            sumEquals100 = false
            activityAdminModuleBinding.submitButtnAdmin.setBackgroundColor(applicationContext.getColor(R.color.grey))
        }
    }


    private fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
}