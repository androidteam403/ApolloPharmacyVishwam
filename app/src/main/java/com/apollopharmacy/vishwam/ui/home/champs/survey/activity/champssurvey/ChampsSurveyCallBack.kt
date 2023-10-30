package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey

import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.model.TrainersEmailIdResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.model.*

interface ChampsSurveyCallBack {

    fun onClickBack()

    fun onClickCategory(get: String, position: Int)

    fun onClickSubmit()
    
    fun onClickSaveDraft()

    fun onClickPreview()

     fun onSuccessgetCategoryDetails(value: GetCategoryDetailsModelResponse)
     fun onFailuregetCategoryDetails(value: GetCategoryDetailsModelResponse)
    fun onSuccessgetTrainingDetails(value: GetTrainingAndColorDetailsModelResponse)
     fun onFailuregetTrainingDetails(value: GetTrainingAndColorDetailsModelResponse)
     fun onSuccessgetColorDetails(value: GetTrainingAndColorDetailsModelResponse)
     fun onSuccessSaveDetailsApi(value: SaveSurveyModelResponse, type: String)
     fun onFailureSaveDetailsApi(value: SaveSurveyModelResponse)
     fun onSuccessSurveyList(value: GetSurveyDetailsModelResponse)
     fun onFailureSurveyList(value: GetSurveyDetailsModelResponse)
     fun onSuccessGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
     fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse)
     fun onSuccessgetSubCategoryDetails(
         value: GetSubCategoryDetailsModelResponse,
         categoryName: String
     )
     fun onFailuregetSubCategoryDetails(value: GetSubCategoryDetailsModelResponse)
     fun onSuccessSaveUpdateApi(value: SaveUpdateResponse)
     fun onFailureSaveUpdateApi(value: SaveUpdateResponse)

     fun onClickDelete()

     fun onClickEyeIconForDropDown()
     fun onSuccessTrainerList(storeWiseDetailListResponse: TrainersEmailIdResponse?)
     fun onFailureTrainerList(storeWiseDetailListResponse: TrainersEmailIdResponse?)
}