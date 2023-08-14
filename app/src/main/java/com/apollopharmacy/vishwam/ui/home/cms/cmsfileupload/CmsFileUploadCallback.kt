package com.apollopharmacy.vishwam.ui.home.cms.cmsfileupload

interface CmsFileUploadCallback {
    fun onFailureUpload(message: String)

    fun allFilesDownloaded(cmsFileUploadModelList: List<CmsFileUploadModel>?, tag: String)

    fun allFilesUploaded(cmsfileUploadModelList: List<CmsFileUploadModel>?, tag: String)
}