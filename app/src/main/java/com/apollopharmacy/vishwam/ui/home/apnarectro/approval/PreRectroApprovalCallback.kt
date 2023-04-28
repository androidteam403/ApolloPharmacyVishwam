package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImageUrlsResponse

interface PreRectroApprovalCallback {
    fun onClick(position: Int, status: GetRetroPendingAndApproveResponse.Retro)
    fun onSuccessRetroApprovalList(value: GetRetroPendingAndApproveResponse)
    fun onFailureRetroApprovalList(value: GetRetroPendingAndApproveResponse)
}