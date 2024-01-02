package com.apollopharmacy.vishwam.ui.home.apnarectro.approval

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImageUrlsResponse

interface PreRectroApprovalCallback {
    fun onClick(position: Int, subPos:Int,status: List<List<GetRetroPendingAndApproveResponse.Retro>>?,approvePendingList:ArrayList<GetRetroPendingAndApproveResponse.Retro>,newStatus:String)
    fun onSuccessRetroApprovalList(value: GetRetroPendingAndApproveResponse)
    fun onFailureRetroApprovalList(value: GetRetroPendingAndApproveResponse)


}