package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

interface ApnaNewPreviewCallBack {
    fun onSuccessgetSurveyDetails(value: SurveyDetailsList)
    fun onClick(value: Int, url: String)
    fun onItemClick(
        position: Int,
        imagePath: String,
        name: String,
        imagetData: ArrayList<SurveyDetailsList.Image>,
    )

    fun onFailuregetSurveyWiseDetails(value: SurveyDetailsList)
}