package com.apollopharmacy.vishwam.ui.home.planogram.activity

import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse

interface PlanogramActivityCallback {

    fun onClickBack()

    fun onClickSubmit()

    fun onClickAreasToFocusOn()

    fun onClickCategoriesToFocusOn()

    fun showTillTop()

    fun onSuccessPlanogramSurveyQuestionsListApiCall(planogramSurveyQuestionsListResponse: PlanogramSurveyQuestionsListResponse)

    fun onFailurePlanogramSurveyQuestionsListApiCall(message: String)
     fun checkAreasToFocusOn(
         questionsList: ArrayList<PlanogramSurveyQuestionsListResponse.Questions>,
         categoryPosition: Int
     )
     fun checkCategoriesToFocusOn(
         questionsList: ArrayList<PlanogramSurveyQuestionsListResponse.Questions>,
         categoryPosition: Int
     )

     fun onSuccessSaveUpdateApi(saveUpdateRequestJsonResponse: PlanogramSaveUpdateResponse?)
    abstract fun onFailureSaveUpdateApi(saveUpdateRequestJsonResponse: PlanogramSaveUpdateResponse?)
    fun caluclateScore()
}