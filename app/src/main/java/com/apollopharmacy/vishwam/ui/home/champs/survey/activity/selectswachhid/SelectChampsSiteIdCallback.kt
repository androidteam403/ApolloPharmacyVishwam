package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid

import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface SelectChampsSiteIdCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(siteId: String,siteName:String)
    fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>)
    fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)
     fun onSuccessgetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse)
     fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse)
}