package com.apollopharmacy.vishwam.ui.home.planogram.siteid

import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

interface SelectPlanogramSiteIdCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(siteId: StoreDetailsModelResponse.Row)
    fun onFailuregetStoreDetails(value: StoreDetailsModelResponse)
    fun onFailureUat()

}