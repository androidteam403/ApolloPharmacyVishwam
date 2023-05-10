package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse

interface PreviewLastImageCallback {
    fun onClick(position: Int, status: String)
    fun onClickReShoot()
    fun onClickAccept()
    fun onClickCompleted()
    fun onClickBack()
    fun onSuccessRatingResponse(value: SaveAcceptResponse)
    fun onFailureRatingResponse(value: SaveAcceptResponse)

    fun onSuccessSaveAcceptReshoot(value: SaveAcceptResponse)
    fun onFailureSaveAcceptReshoot(value: SaveAcceptResponse)
    fun onClickDelete()

}