package com.apollopharmacy.vishwam.ui.home.retroqr.fileuploadqr


interface RetroQrFileUploadCallback {

    fun onFailureUpload(message: String)

    fun allFilesDownloaded(fileUploadModelList: List<RetroQrFileUploadModel>?)

    fun allFilesUploaded(fileUploadModelList: List<RetroQrFileUploadModel>?)
}