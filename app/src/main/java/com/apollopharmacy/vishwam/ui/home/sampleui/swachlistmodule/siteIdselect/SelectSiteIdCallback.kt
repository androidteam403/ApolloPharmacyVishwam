package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.siteIdselect

import com.apollopharmacy.vishwam.data.model.cms.StoreListItem

interface SelectSiteIdCallback {

    fun onClickSiteId(site: StoreListItem)
    fun onClickSubmit()
    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onClickCrossButton(selectsiteId: String, position: Int)
}