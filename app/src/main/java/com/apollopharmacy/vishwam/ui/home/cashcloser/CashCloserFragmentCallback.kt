package com.apollopharmacy.vishwam.ui.home.cashcloser

import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsResponse
import java.io.File

interface CashCloserFragmentCallback {
    fun addImage(siteId: String, imagePosition: Int, imageState: Int)

    fun deleteImage(position: Int, imageState: Int)

    fun previewImage(file: String, position: Int)

    fun headrItemClickListener(storeId: String, pos: Int)

    fun onClickUpload(
        siteid: String,
        imageurl: File?,
        imageurlTwo: File?,
        amount: String,
        remarks: String,
        dcid: String,
        createdBy: String,
    )

    fun onSuccessGetCashDepositDetailsApiCall(cashDepositDetailsResponse: CashDepositDetailsResponse)

    fun onFailureGetCashDepositDetailsApiCall(message: String)

    fun onSuccessSaveCashDepositDetailsApiCall(cashDepositDetailsResponse: CashDepositDetailsResponse)

    fun onFailureSaveCashDepositDetailsApiCall(message: String)
}