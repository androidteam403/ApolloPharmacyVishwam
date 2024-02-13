package com.apollopharmacy.vishwam.ui.home.communityadvisor.siteid

import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface SelectCommunityAdvisorSiteIdCallback {
    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(siteId: StoreDetailsModelResponse.Row)
    fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)
    fun onSuccessgetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse)
    fun onFailureUat()

}