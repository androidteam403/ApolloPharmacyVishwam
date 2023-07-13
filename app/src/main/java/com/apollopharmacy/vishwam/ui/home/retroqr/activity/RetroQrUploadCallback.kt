package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails

interface RetroQrUploadCallback {
    fun onClickBackArrow()
    fun onClickSubmit()

    fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails)
    fun onClickCameraIcon(position: Int)
}