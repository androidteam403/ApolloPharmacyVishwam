package com.apollopharmacy.vishwam.ui.home.retroqr

import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface RetroQrFragmentCallback {
    fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>)
    fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)
}