package com.apollopharmacy.vishwam.ui.home.apna.survey

import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse

interface ApnaSurveyCallback {
    fun onClick(position: Int, surveyListResponse: SurveyListResponse.Row)
    fun onSuccessgetSurveyDetails(value: SurveyListResponse)

}