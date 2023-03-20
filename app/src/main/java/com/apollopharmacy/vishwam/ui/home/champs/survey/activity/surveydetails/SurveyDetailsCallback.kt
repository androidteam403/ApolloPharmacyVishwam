package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails

import com.apollopharmacy.vishwam.ui.home.model.GetEmailAddressModelResponse

interface SurveyDetailsCallback {

    fun onClickBack()

    fun onClickStartChampsSurvey()

    fun onClickPlusRec()

    fun deleteEmailAddressRec(get: String)

    fun deleteEmailAddressCC(get: String)

    fun onClickPlusCC()
     fun onSuccessgetEmailDetails(value: GetEmailAddressModelResponse)
     fun onFailuregetEmailDetails(value: GetEmailAddressModelResponse)
     fun onSuccessgetEmailDetailsCC(value: GetEmailAddressModelResponse)
}