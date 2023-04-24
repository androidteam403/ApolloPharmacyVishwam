package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity


import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse

interface UploadImagesCallback {
    fun onClickUpload()
    fun onClickEyeImage()
    fun onClickBackIcon()
    fun onSuccessGetStoreWiseDetails(value: GetStoreWiseCatDetailsApnaResponse)
    fun onFailureStoreWiseDetails(value: GetStoreWiseCatDetailsApnaResponse)
    fun onClickCancel()
    fun onSuccessImageIsUploadedInAzur(response: ArrayList<GetStoreWiseCatDetailsApnaResponse>)
     fun onSuccessSaveImageUrlsApi(value: SaveImageUrlsResponse)
     fun onFailureSaveImageUrlsApi(value: SaveImageUrlsResponse)
}