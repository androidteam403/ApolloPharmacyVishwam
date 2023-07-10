package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsResponse

interface NewSurveyCallback {

    fun onClickSearch()

   fun onClickCardView()

    fun onClickCloseIcon()
    fun onSuccessgetStoreDetails(value: List<StoreDetailsResponse.Row>)
     fun onFailuregetStoreDetails(value: StoreDetailsResponse)
     fun onSuccessgetStoreWiseDetails(value: GetStoreWiseDetailsResponse)
     fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsResponse)
}