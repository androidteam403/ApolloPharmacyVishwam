package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner

import com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner.model.ScannerResponse

interface RetroQrScannerCallback {
    fun onSuccessGetImageUrlApiCall(scannerResponse: ScannerResponse)
    fun onFailureGetImageUrlApiCall(message: String)
}