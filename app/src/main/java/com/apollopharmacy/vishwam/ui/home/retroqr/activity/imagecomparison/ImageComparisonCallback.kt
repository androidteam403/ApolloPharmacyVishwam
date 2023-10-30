package com.apollopharmacy.vishwam.ui.home.retroqr.activity.imagecomparison

interface ImageComparisonCallback {
    fun onClickBack()
    fun onClickSubmit()
    fun onClickOk()
    fun onSuccessUploadImagesApiCall(message: String)
    fun onFailureUploadImagesApiCall(message: String)
}