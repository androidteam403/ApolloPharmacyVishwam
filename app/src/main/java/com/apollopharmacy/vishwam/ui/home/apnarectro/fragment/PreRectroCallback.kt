package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListRes


interface PreRectroCallback {

    fun onClickContinue()
    fun onClickPreRetrPending(
        stage: String,
        status: String,
        retroid: String,
        uploadedOn: String,
        uploadedBy: String,
        toString: String,
        s: String
    )
    fun onClickPreRetroPending(s: String)
    fun onClickPostRetroPending(
        stage: String,
        status: String,
        retroid: String,
        uploadedOn: String,
        uploadedBy: String,
        toString: String,
        s: String
    )
     fun onSuccessgetStorePendingApprovedApiCall(value: GetStorePendingAndApprovedListRes)

}