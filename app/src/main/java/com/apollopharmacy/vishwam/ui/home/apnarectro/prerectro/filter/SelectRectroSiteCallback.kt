package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.filter

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem

interface SelectRectroSiteCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)

    fun onItemClick(storeListItem: StoreListItem)
}