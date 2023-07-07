package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid

import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsResponse

interface SelectChampsSiteIdCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(siteId: String,siteName:String)
    fun onSuccessgetStoreDetails(value: List<StoreDetailsResponse.Row>)
    fun onFailuregetStoreDetails(value: StoreDetailsResponse)
     fun onSuccessgetStoreWiseDetails(value: GetStoreWiseDetailsResponse)
     fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsResponse)
}