package com.apollopharmacy.vishwam.ui.home.apollosensing.activity

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SiteListResponse

interface ApolloSensingStoreCallback {
    fun noOrdersFound(size: Int)
    fun onItemClick(storeListItem: StoreListItem)
    fun onClickCancel()
    fun onSuccessSiteListApiCall(siteListResponse: SiteListResponse)
    fun onSiteListItemClick(item: SiteListResponse.Data.ListData.Row)
}