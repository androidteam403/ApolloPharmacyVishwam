package com.apollopharmacy.vishwam.ui.home.cashcloser

import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsResponse
import java.io.File

interface CashCloserFragmentCallback {
    fun addImage(siteId: String, imagePosition: Int)

    fun deleteImage(imagePosition: Int)

    fun previewImage(file: File, position: Int)

    fun headrItemClickListener(storeId: String, pos: Int)

    fun uploadClicked(siteId: String, position: Int)

    fun saveCashDepositDetails(
        siteid: String,
        imageurl: String,
        amount: String,
        remarks: String,
        dcid: String,
        createdBy: String,
    )

    fun onSuccessGetCashDepositDetailsApiCall(cashDepositDetailsResponse: CashDepositDetailsResponse)

    fun onFailureGetCashDepositDetailsApiCall(message: String)
}