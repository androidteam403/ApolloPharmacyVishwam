package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.PendingAndApproved

interface SwachhListCallback {
    fun onClickUpdate(pendingAndApproved: PendingAndApproved, isApprovedAdapter: Boolean)
    fun onClickFromDate()
    fun onClickToDate()
    fun onClickSearch()
    fun onClickApproved()
    fun onClickPending()
    fun onClickCrossButton(get: String, position: Int)
    fun onClickReview(swachhid: String?, storeId: String?)
}