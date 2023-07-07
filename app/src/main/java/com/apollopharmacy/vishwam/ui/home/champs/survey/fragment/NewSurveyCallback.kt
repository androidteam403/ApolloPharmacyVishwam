package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseEmpIdResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface NewSurveyCallback {

    fun onClickSearch()

   fun onClickCardView()

    fun onClickCloseIcon()
    fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>)
     fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)
}