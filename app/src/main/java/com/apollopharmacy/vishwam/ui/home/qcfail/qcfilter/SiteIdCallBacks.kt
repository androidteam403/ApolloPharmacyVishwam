package com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter

interface SiteIdCallBacks {

    fun onClickSiteId(position: String?, position1: Int)
    fun onClickSubmit()
    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onClickCrossButton(selectsiteId: String, position: Int)
}