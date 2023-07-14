package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails

interface RetroQrUploadCallback {
    fun onClickBackArrow()
    fun onClickSubmit()
    fun onSuccessUpload(message: String)

    fun onFailureUpload(message: String)
    fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails)
    fun onClickCameraIcon(position: Int)
}