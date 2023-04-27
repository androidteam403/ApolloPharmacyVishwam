package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListRes


interface PreRectroCallback {

    fun onClickContinue()
    fun onClickCardView()
    fun onClickPreRetroPending(s: String)
    fun onClickPostRetroPending(s: String, postRetostatus: String, toString: String)
     fun onSuccessgetStorePendingApprovedApiCall(value: GetStorePendingAndApprovedListRes)

}