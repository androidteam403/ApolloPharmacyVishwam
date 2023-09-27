package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.view.View
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails

interface RetroQrUploadCallback {
    fun onClickBackArrow()
    fun onClickSubmit()
    fun onClickSubmitReview()

    fun imageData(position: Int, imageUrl: String, rackNo: String, qrCode: String, view: View)

    fun onSuccessUploadImagesApiCall(message: String)
    fun onFailureUploadImagesApiCall(message: String)
    fun onFailureStoreWiseRackResponse(message: String)

    fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails)







    fun onClickCameraIcon(position: Int, adapter: String)
    fun onClickCompare(
        matchingPercentage: String,
        firstImage: String,
        secondImage: String,
        rackNo: String,
    )

    fun deleteImage(position: Int)

    fun onUpload(position: Int, rackNo: String)

    fun onClickQrCodePrint()
}