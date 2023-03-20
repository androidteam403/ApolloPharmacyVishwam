package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurevyDetailsByChampsIdResponse

interface ChampsDetailsandRatingBarCallBack {

    fun onClickBack()

    fun onClickSaveDraft()

    fun onClickSubmit()

    fun onClickImageUpload3()

    fun onClickImageUpload2()

    fun onClickImageUpload1()

    fun onClickRedTrashDelete1()

    fun onClickRedTrashDelete2()

    fun onClickRedTrashDelete3()
    fun onSuccessgetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse)
    fun onFailuregetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse)
     fun onClickSeekBar(progress: Float, subCatPos: Int)
     fun onSuccessImageIsUploadedInAzur(response: String?)

     fun onClickOpenGallery()
     fun onSuccessGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
     fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
}