package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse

interface ApprovalReviewCallback {
     fun onClickItemView(position: Int, approvedOrders:ArrayList<List<GetImageUrlResponse.ImageUrl>>?,categoryPosition: Int,categoryName:String,url:String,statusPos:String)
     fun onSuccessImageUrlList(value:GetImageUrlResponse,categoryList:List<GetImageUrlResponse.Category>,retroId:String)
     fun onFailureImageUrlList(value: GetImageUrlResponse)
     fun onClickReview()
     fun onSuccessSaveAcceptReshoot(value: SaveAcceptResponse)
     fun onFailureSaveAcceptReshoot(value: SaveAcceptResponse)

     fun onClickBackIcon()
}