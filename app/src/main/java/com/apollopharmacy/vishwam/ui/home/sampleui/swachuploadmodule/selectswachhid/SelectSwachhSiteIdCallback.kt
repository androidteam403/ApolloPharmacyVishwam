package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.selectswachhid

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem

interface SelectSwachhSiteIdCallback {

    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onItemClick(storeListItem: StoreListItem)
}