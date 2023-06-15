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
        s: String,
        approvedby: String?,
        approvedDate: String?,
        partiallyApprovedBy: String?,
        partiallyApprovedDate: String?,
        reshootDate: String?,
        reshootBy: String?,
        retroStage: String?

    )
    fun onClickPreRetroPending(s: String)
    fun onClickPostRetroPending(
        stage: String,
        status: String,
        retroid: String,
        uploadedOn: String,
        uploadedBy: String,
        toString: String,
        s: String,
        approvedby: String?,
        approvedDate: String?,
        partiallyApprovedBy: String?,
        partiallyApprovedDate: String?,
        reshootBy: String?,
        reshootDate: String?,
        retroStage: String?
    )
     fun onSuccessgetStorePendingApprovedApiCall(value: GetStorePendingAndApprovedListRes)

     fun onClickSearch()

}