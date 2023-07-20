package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.view.View
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails

interface RetroQrUploadCallback {
    fun onClickBackArrow()
    fun onClickSubmit()

    fun imageData(position: Int, imageUrl: String, rackNo: String, qrCode: String,view:View)

    fun onSuccessUploadImagesApiCall(message: String)
    fun onFailureUploadImagesApiCall(message: String)

    fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails)
    fun onClickCameraIcon(position: Int)
}