package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid

import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface SelectChampsSiteIdCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(storeListItem: StoreDetailsModelResponse.StoreDetail)
    fun onSuccessgetStoreDetails(value: StoreDetailsModelResponse)
    fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)
}