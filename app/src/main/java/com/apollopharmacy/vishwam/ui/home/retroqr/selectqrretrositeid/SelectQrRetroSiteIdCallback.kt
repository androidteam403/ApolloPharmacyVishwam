package com.apollopharmacy.vishwam.ui.home.retroqr.selectqrretrositeid

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface SelectQrRetroSiteIdCallback {
    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(siteId: String,siteName:String)
    fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>)
    fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)


}