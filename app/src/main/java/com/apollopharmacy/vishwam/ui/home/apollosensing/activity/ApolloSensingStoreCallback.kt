package com.apollopharmacy.vishwam.ui.home.apollosensing.activity

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem

interface ApolloSensingStoreCallback {
    fun noOrdersFound(size: Int)
    fun onItemClick(storeListItem: StoreListItem)
    fun onClickCancel()
}