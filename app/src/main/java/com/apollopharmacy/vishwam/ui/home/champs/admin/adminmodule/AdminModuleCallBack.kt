package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule

import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.SaveCategoryConfigurationDetailsResponse

interface AdminModuleCallBack {

    fun onClickExpand()

    fun onClickEdit()

    fun onClickEditOverall(categoryDetails: GetCategoryDetailsResponse.CategoryDetails)

    fun onClickEditOverallHospitality()

    fun onClickEditSub1()

    fun onClickEditSub2()

    fun onClickEditSub3()

    fun onClickEditSub4()


    // created by naveen
    fun onSuccessGetCategoryDetailsApiCall(getCategoryDetailsResponse: GetCategoryDetailsResponse)

    fun onFailureGetCategoryDetailsApiCall(message: String)

    fun onClickSubCategoryDetailsItem(categoryDetails: GetCategoryDetailsResponse.CategoryDetails)

    fun onSuccessGetSubCategoryDetailsApiCall(
        getSubCategoryDetailsResponse: GetSubCategoryDetailsResponse,
        categoryName: String,
    )

    fun onFailureGetSubCategoryDetailsApiCall(message: String)

    fun onClickSubmit()

    fun onClickEditSubCategoryRatingRange(
        subCategoryDetails: GetSubCategoryDetailsResponse.SubCategoryDetails,
        categoryPosition: Int,
    )

    fun onSuccessSaveCategoryConfigurationDetailsApiCAll(saveCategoryConfigurationDetailsResponse: SaveCategoryConfigurationDetailsResponse)
    fun onFailureSaveCategoryConfigurationDetails(message: String)
}