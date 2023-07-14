package com.apollopharmacy.vishwam.util.fileupload

interface FileUploadCallback {

    fun onFailureUpload(message: String)

    fun allFilesDownloaded(fileUploadModelList: List<FileUploadModel>?)

    fun allFilesUploaded(fileUploadModelList: List<FileUploadModel>?)
}