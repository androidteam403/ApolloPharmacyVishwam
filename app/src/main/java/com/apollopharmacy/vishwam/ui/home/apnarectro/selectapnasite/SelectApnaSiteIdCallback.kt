package com.apollopharmacy.vishwam.ui.home.apnarectro.selectapnasite

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem

interface SelectApnaSiteIdCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(storeListItem: StoreListItem)
}