package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface NewSurveyCallback {

    fun onClickSearch()

    fun onClickCardView()

    fun onClickCloseIcon()
    fun onSuccessgetStoreDetails(value: StoreDetailsModelResponse)
     fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)
     fun onSuccessgetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse)
     fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse)
}