package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import android.widget.MediaController
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

interface ApnaNewPreviewCallBack {
    fun onSuccessgetSurveyDetails(value: SurveyDetailsList)
    fun onClick(value:Int,url:String)

    fun onFailuregetSurveyWiseDetails(value: SurveyDetailsList)
}