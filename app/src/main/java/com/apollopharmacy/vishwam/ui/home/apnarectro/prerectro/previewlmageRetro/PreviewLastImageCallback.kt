package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.previewlmageRetro

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.SaveAcceptResponse

interface PreviewLastImageCallback {
    fun onClick(position: Int, status: String)
    fun onClickReShoot()
    fun onClickAccept()
    fun onClickCompleted()
    fun onClickBack()

    fun onClickDelete()

}