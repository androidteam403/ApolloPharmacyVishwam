package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.apnasiteIdselect

interface ApnaSelectSiteIdCallback {

    fun onClickSiteId(position: String?, position1: Int)
    fun onClickSubmit()
    fun onClickCancel()
    fun noOrdersFound(size: Int)
    fun onClickCrossButton(selectsiteId: String, position: Int)
    fun onClickCompleted()
}