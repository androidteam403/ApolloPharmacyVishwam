package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse

interface PostRetroUploadImagesCallback {
    fun onClickUpload()
    fun onClickCancel()
    fun onClickEyeImage()
    fun onClickImageView()
    fun onSuccessImageIsUploadedInAzur(response: ArrayList<GetStoreWiseCatDetailsApnaResponse>)
     fun onSuccessSaveImageUrlsApi(value: SaveImageUrlsResponse)
     fun onFailureSaveImageUrlsApi(value: SaveImageUrlsResponse)
     fun onSuccessGetStoreWiseDetails(value: GetStoreWiseCatDetailsApnaResponse)
     fun onFailureStoreWiseDetails(value: GetStoreWiseCatDetailsApnaResponse)
     fun onSuccessImageUrlsList(value: GetImageUrlsModelApnaResponse, retroId: String)
     fun onFailureImageUrlsList(value: GetImageUrlsModelApnaResponse)

     fun onClickReshoot()

}