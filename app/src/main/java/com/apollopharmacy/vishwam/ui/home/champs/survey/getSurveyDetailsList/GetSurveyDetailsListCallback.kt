package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import com.apollopharmacy.vishwam.ui.home.champs.survey.model.GlobalConfigurationResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurveyDetailsModelResponse

interface GetSurveyDetailsListCallback {
     fun onSuccessSurveyList(value: GetSurveyDetailsModelResponse)
     fun onFailureSurveyList(value: GetSurveyDetailsModelResponse)
     fun onClickCardView(
         status: String?,
         champsRefernceId: String?,
         sitename: String,
         toString: String
     )
     fun onClickPlusIcon()

     fun onClickback()

     fun onClickFilterIcon()
     fun onSuccessGlobalConfigDetails(value: GlobalConfigurationResponse)
     fun onFailureGlobalConfigDetails(value: GlobalConfigurationResponse)
}