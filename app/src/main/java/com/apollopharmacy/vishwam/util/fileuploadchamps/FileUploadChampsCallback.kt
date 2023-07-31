package com.apollopharmacy.vishwam.util.fileuploadchamps

interface FileUploadChampsCallback {

    fun onFailureUpload(message: String)

    fun allFilesUploaded(fileUploadModelList: List<FileUploadChampsModel>)
    fun allFilesDownloaded(fileUploadModelList: List<FileUploadChampsModel>)
}