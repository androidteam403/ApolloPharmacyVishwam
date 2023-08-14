package com.apollopharmacy.vishwam.ui.home.planogram.activity

import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse

interface PlanogramActivityCallback {

    fun onClickBack()

    fun onClickSubmit()

    fun onClickAreasToFocusOn()

    fun onClickCategoriesToFocusOn()

    fun showTillTop()

    fun onSuccessPlanogramSurveyQuestionsListApiCall(planogramSurveyQuestionsListResponse: PlanogramSurveyQuestionsListResponse)

    fun onFailurePlanogramSurveyQuestionsListApiCall(message: String)
}