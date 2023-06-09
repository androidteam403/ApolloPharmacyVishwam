package com.apollopharmacy.vishwam.ui.home.apna.activity

import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

interface ApnaSurveyPreviewCallback {
    fun onClick(
        imageUrl: String,
        position: Int,
        data: ArrayList<SurveyCreateRequest.SiteImageMb.Image>,
    )

    fun onClickPlay(video: String)
}