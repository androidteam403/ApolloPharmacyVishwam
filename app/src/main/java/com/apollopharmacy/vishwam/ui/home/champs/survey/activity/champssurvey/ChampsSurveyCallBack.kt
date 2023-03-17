package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey

import com.apollopharmacy.vishwam.ui.home.model.*

interface ChampsSurveyCallBack {

    fun onClickBack()

    fun onClickCategory(get: String, position: Int)

    fun onClickSubmit()
    
    fun onClickSaveDraft()

     fun onSuccessgetCategoryDetails(value: GetCategoryDetailsModelResponse)
     fun onFailuregetCategoryDetails(value: GetCategoryDetailsModelResponse)
    fun onSuccessgetTrainingDetails(value: GetTrainingAndColorDetailsModelResponse)
     fun onFailuregetTrainingDetails(value: GetTrainingAndColorDetailsModelResponse)
     fun onSuccessgetColorDetails(value: GetTrainingAndColorDetailsModelResponse)
     fun onSuccessSaveDetailsApi(value: SaveSurveyModelResponse)
     fun onFailureSaveDetailsApi(value: SaveSurveyModelResponse)
     fun onSuccessSurveyList(value: GetSurveyDetailsModelResponse)
     fun onFailureSurveyList(value: GetSurveyDetailsModelResponse)
     fun onSuccessGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
     fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
}