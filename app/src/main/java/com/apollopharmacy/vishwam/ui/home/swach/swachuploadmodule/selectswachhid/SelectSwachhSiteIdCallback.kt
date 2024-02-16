package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem

interface SelectSwachhSiteIdCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(storeListItem: StoreListItem)
    fun onFailureUat()
}