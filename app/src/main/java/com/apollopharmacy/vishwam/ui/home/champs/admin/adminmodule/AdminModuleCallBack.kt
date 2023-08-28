package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule

import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.SaveCategoryConfigurationDetailsResponse

interface AdminModuleCallBack {

    fun onClickExpand()

    fun onClickBack()


    fun onClickEditOverall(
        categoryDetails: GetCategoryDetailsResponse.CategoryDetails,
        sumOfSubCategoryMaxRatings: Double,
        categoryPos: String,
        categoryName:String
    )




    // created by naveen
    fun onSuccessGetCategoryDetailsApiCall(getCategoryDetailsResponse: GetCategoryDetailsResponse)

    fun onFailureGetCategoryDetailsApiCall(message: String)

    fun onClickSubCategoryDetailsItem(categoryDetails: GetCategoryDetailsResponse.CategoryDetails, itemPos:Int)

    fun onSuccessGetSubCategoryDetailsApiCall(
        getSubCategoryDetailsResponse: GetSubCategoryDetailsResponse,
        categoryName: String
    )

    fun onFailureGetSubCategoryDetailsApiCall(message: String)

    fun onClickSubmit()

    fun onClickEditSubCategoryRatingRange(
        subCategoryDetails: GetSubCategoryDetailsResponse.SubCategoryDetails,
        categoryPosition: Int,
    )

    fun onSuccessSaveCategoryConfigurationDetailsApiCAll(saveCategoryConfigurationDetailsResponse: SaveCategoryConfigurationDetailsResponse)
    fun onFailureSaveCategoryConfigurationDetails(message: String)
    fun onValidateTotalSum(getCategoryDetails: List<GetCategoryDetailsResponse.CategoryDetails>?)

}