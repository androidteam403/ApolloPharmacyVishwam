package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetRetroPendingAndApproveResponse

interface PreRetroReviewingCallback {
     fun onClickItemView(position: Int, approvedOrders: String)
     fun onSuccessImageUrlList(value: GetImageUrlResponse)
     fun onFailureImageUrlList(value: GetImageUrlResponse)
     fun onClickReview()
}