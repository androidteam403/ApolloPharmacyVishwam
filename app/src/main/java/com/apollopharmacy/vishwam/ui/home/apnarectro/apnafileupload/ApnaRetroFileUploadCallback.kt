package com.apollopharmacy.vishwam.ui.home.apnarectro.apnafileupload

interface ApnaRetroFileUploadCallback {
    fun onFailureUpload(message: String)

    fun allFilesDownloaded(fileUploadModelList: List<ApnaRetroFileUploadModel>?)

    fun allFilesUploaded(fileUploadModelList: List<ApnaRetroFileUploadModel>?)
}