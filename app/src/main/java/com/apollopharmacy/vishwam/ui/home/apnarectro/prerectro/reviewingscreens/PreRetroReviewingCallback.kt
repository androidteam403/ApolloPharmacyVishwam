package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens

interface PreRetroReviewingCallback {
     fun onClickItemView(position: Int, approvedOrders: String)

     fun onClickReview()
}