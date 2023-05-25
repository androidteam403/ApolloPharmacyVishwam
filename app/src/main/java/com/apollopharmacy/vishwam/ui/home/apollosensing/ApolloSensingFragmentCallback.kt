package com.apollopharmacy.vishwam.ui.home.apollosensing

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsResponse
import java.io.File

interface ApolloSensingFragmentCallback {
    fun deleteImage(position: Int, file: File)

    fun onSuccessSendGlobalSms(sendGlobalSmsResponse: SendGlobalSmsResponse, type: String)

    fun onFailureSendGlobalSms(sendGlobalSmsResponse: SendGlobalSmsResponse, type: String)

    fun onClickResend()

    fun onClickSendLinkBtn()

    fun onSuccessGetLinkApolloSensing(link: String)

    fun onFailureGetLinkApolloSensing(message: String)

    fun onClickUploadPrescription()

    fun onSuccessUploadPrescriptionApiCall(message: String)

    fun onFailureUploadPrescriptionApiCall(message: String)
}