package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner

import com.apollopharmacy.vishwam.ui.home.model.GetImageByRackResponse

interface RetroQrScannerCallback {
    fun onSuccessGetImageUrlApiCall(imageByRackResponse: GetImageByRackResponse)
    fun onFailureGetImageUrlApiCall(message: String)
}