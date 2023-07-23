package com.apollopharmacy.vishwam.ui.home.retroqr.activity

import android.view.View
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails
import java.io.File

interface RetroQrUploadCallback {
    fun onClickBackArrow()
    fun onClickSubmit()

    fun imageData(position: Int, imageUrl: String, rackNo: String, qrCode: String,view:View)

    fun onSuccessUploadImagesApiCall(message: String)
    fun onFailureUploadImagesApiCall(message: String)

    fun onSuccessgetStoreWiseRackResponse(storeWiseRackDetails: StoreWiseRackDetails)
    fun onClickCameraIcon(position: Int,adapter:String)
    fun onClickCompare(matchingPercentage: String, firstImage: String, secondImage: String,rackNo:String)

    fun deleteImage(position: Int)

}