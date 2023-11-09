package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import android.view.View
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurevyDetailsByChampsIdResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurveyDetailsByChampsIdResponsee

interface ChampsDetailsandRatingBarCallBack {

    fun onClickBack()
    fun onClickSubmit()
    fun onSuccessgetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse)
    fun onFailuregetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse)
    fun onClickSeekBar(progress: Float, subCatPos: Int)
    fun onSuccessImageIsUploadedInAzur(response: String?)
    fun onClickOpenGallery()
    fun onSuccessGetSurveyDetailsByChampsId(value: GetSurveyDetailsByChampsIdResponsee)
    fun onFailureGetSurveyDetailsByChampsId(value: GetSurveyDetailsByChampsIdResponsee)
    fun onClickPlusIcon(position: Int)
    fun onClickImageDelete(position: Int)
    fun onClickImageView(
        view: View,
        file: MutableList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>?,
        position: Int
    )
}