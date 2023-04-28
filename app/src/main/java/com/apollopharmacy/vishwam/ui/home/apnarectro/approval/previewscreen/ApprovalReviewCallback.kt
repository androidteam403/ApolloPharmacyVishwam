package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse

interface ApprovalReviewCallback {
     fun onClickItemView(position: Int, approvedOrders: String)
     fun onSuccessImageUrlList(value:GetImageUrlResponse,categoryList:List<GetImageUrlResponse.Category>,retroId:String)
     fun onFailureImageUrlList(value: GetImageUrlResponse)
     fun onClickReview()
}