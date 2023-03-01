package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails

interface SurveyDetailsCallback {

    fun onClickBack()

    fun onClickStartChampsSurvey()

    fun onClickPlusRec()

    fun deleteEmailAddressRec(get: String)

    fun deleteEmailAddressCC(get: String)

    fun onClickPlusCC()
}