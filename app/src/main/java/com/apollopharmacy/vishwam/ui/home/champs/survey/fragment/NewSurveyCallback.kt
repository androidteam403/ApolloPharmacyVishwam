package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseEmpIdResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface NewSurveyCallback {

    fun onClickSearch()

   fun onClickCardView()

    fun onClickCloseIcon()

    fun onClickSiteIdIcon()
    fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>)
     fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)

    fun onSuccessgetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse)
    fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse)

    fun onClickBack()
    fun onFailureUat()
}