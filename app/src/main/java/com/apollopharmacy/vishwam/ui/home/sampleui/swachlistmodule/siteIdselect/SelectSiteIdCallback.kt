package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.siteIdselect

interface SelectSiteIdCallback {

    fun onClickSiteId(position: String?, position1: Int)
    fun onClickSubmit()
    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onClickCrossButton(selectsiteId: String, position: Int)
}