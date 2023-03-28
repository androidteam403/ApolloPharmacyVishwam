package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurevyDetailsByChampsIdResponse

interface ChampsDetailsandRatingBarCallBack {

    fun onClickBack()
    fun onClickSubmit()
    fun onSuccessgetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse)
    fun onFailuregetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse)
    fun onClickSeekBar(progress: Float, subCatPos: Int)
    fun onSuccessImageIsUploadedInAzur(response: String?)
    fun onClickOpenGallery()
    fun onSuccessGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
    fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
    fun onClickPlusIcon(position: Int)
    fun onClickImageDelete(position: Int)
}