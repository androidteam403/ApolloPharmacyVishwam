package com.apollopharmacy.vishwam.ui.home.apollosensing

import android.text.Html
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApolloSensingRepo
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SaveImageUrlsRequest
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SendGlobalSmsRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.UpdateUserDefaultSiteRequest
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.UpdateUserDefaultSiteResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ApolloSensingViewModel : ViewModel() {


    val state = MutableLiveData<State>()

    fun checkScreenStatus(
        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrl = ""//"https://apsmtest.apollopharmacy.org:8443/SENSING/CheckScreenStatus"
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SEN ACCESS")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApolloSensingRepo.checkScreenStatus(
                    baseUrl
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    if (response.value.status == true) {
                        apolloSensingFragmentCallback.onSuccessCheckScreenStatusApiCall(response.value)
                    } else {
                        apolloSensingFragmentCallback.onFailureCheckScreenStatusApiCall(response.value.message!!)
                    }
                }

                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

    fun sendGlobalSmsApiCall(
        type: String,
        sendGlobalSmsRequest: SendGlobalSmsRequest,
        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
        isPrescriptionUploadFlow: Boolean,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrl = ""
        // "https://apsmtest.apollopharmacy.org:8443/GSMS/APOLLO/SMS/SendGlobalSms"//"https://172.16.103.116:8443/GSMS/APOLLO/SMS/SendGlobalSms"
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SEN GSMS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApolloSensingRepo.sendGlobalSmsApiCall(
                    baseUrl, token, sendGlobalSmsRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    if (response.value.status == true) {
                        apolloSensingFragmentCallback.onSuccessSendGlobalSms(
                            response.value, type, isPrescriptionUploadFlow
                        )
                    } else {
                        apolloSensingFragmentCallback.onFailureSendGlobalSms(
                            response.value, type, isPrescriptionUploadFlow
                        )
                    }
                }

                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

    fun getLinkApiCall(
        customerName: String,
        customerMobileNumber: String,
        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
        siteId: String,
        timeStamp: String,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        val devUrl = "http://dev.thresholdsoft.com/Apollo-sensing"
        val ipUrl = " http://172.16.103.116:8445"
        var baseUrl =
            "https://apmails.in/index.php?format=text&url=https://prescriptionsensing.apollopharmacy.in/?${customerName}_${customerMobileNumber}_${siteId}_${timeStamp}&encryption=true&campaign=sensing&code=APLSEN&secret=1980&key=3214"
//            "https://apmails.in/index.php?format=text&url=https://privilegecustomer.apollopharmacy.in/?${customerName}_${customerMobileNumber}_${siteId}_${timeStamp}&encryption=true&campaign=sensing&code=APLSEN&secret=1980&key=3214"
        /*"https://apmails.in/index.php?format=text&url=https://privilegecustomer.apollopharmacy.in/ap/sensing/?${customerName}_${customerMobileNumber}_${siteId}_${timeStamp}&encryption=true&campaign=sensing&code=APLSEN&secret=1980&key=3214"*/


        //  "https://t.zeroco.de/index.php?format=text&url=http://172.16.103.116:8445?${customerName}_${customerMobileNumber}_${siteId}_${timeStamp}"
        // "https://t.zeroco.de/index.php?url=http://dev.thresholdsoft.com/Apollo-sensing/?format=text&cusomer=$customerName&mobile=$customerMobileNumber&id=$timeStamp"
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("testt")) {
                baseUrl = data.APIS[i].URL
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApolloSensingRepo.getLinkApolloSensingApiCall(
                    baseUrl
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    val data = response.value.string()
                    if (data != null && data.isNotEmpty()) {
                        // String strToHtml = Html.fromHtml(htmlContentInStringFormat)
                        var strHtml: Spanned? = Html.fromHtml(data)
                        apolloSensingFragmentCallback.onSuccessGetLinkApolloSensing(strHtml.toString())
                    } else {
                        apolloSensingFragmentCallback.onFailureGetLinkApolloSensing("Something went wrong")
                    }

                }

                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

    fun saveImageUrlsApiCall(
        saveImageUrlsRequest: SaveImageUrlsRequest,
        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrl = ""
        //  "https://apsmtest.apollopharmacy.org:8443/SENSING/SaveSensingDetails" //"https://172.16.103.116:8443/SENSING/SaveSensingDetails"
        var baseToken = "" //"h72genrSSNFivOi/cfiX3A==" //"h72genrSSNFivOi/cfiX3A=="
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SEN SAVEDETAILS")) {
                baseUrl = data.APIS[i].URL
                baseToken = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                ApolloSensingRepo.saveImageUrlsApiCall(
                    baseUrl, baseToken, saveImageUrlsRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.SUCCESS
                    if (response.value.status == true) {
                        apolloSensingFragmentCallback.onSuccessUploadPrescriptionApiCall(response.value.message!!)
                    } else {
                        if (response.value.message != null) {
                            apolloSensingFragmentCallback.onFailureUploadPrescriptionApiCall(
                                response.value.message!!
                            )
                        } else {
                            apolloSensingFragmentCallback.onFailureUploadPrescriptionApiCall("Something went wrong.")
                        }
                    }
                }

                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

    fun updateDefaultSiteIdApiCall(
        updateUserDefaultSiteRequest: UpdateUserDefaultSiteRequest,
        callback: ApolloSensingFragmentCallback,
    ) {
        val updateUserDefaultSiteRequestJson = Gson().toJson(updateUserDefaultSiteRequest)
        val url = Preferences.getApi()

        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrL = ""
        var token1 = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token1 = data.APIS[i].TOKEN
                break
            }
        }
        //
        //https://apis.v35.dev.zeroco.de-////apollocms
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS update_user_default_site")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
//        "URL":"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/save-update/update-user-default-site","NAME":"CMS update_user_default_site","TOKEN":""},

//        val baseUrl: String =
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/save-update/update-user-default-site"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                SwachApiiRepo.updateSwachhDefaultSite(
                    baseUrL,
                    token1,
                    GetDetailsRequest(baseUrl, "POST", updateUserDefaultSiteRequestJson, "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val updateUserDefaultSiteResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                UpdateUserDefaultSiteResponse::class.java
                            )
                            if (updateUserDefaultSiteResponse.success!!) {
                                callback.onSuccessUpdateDefaultSiteIdApiCall(
                                    updateUserDefaultSiteResponse
                                )
//                                updateUserDefaultSiteResponseMutable.value =
//                                    updateUserDefaultSiteResponse

//                                updateSwachhDefaultSiteResponseModel.value =
//                                    updateSwachhDefaultSiteResponse
                            } else {

                            }
                        }
                    } else {
                    }
                }

                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }

                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }

//    fun sensingFileUpload(
//        apolloSensingFragmentCallback: ApolloSensingFragmentCallback,
//        file: File,
//        isLastImage: Boolean,
//    ) {
//        val url = Preferences.getApi()
//        val data = Gson().fromJson(url, ValidateResponse::class.java)
//
//        var sensingFileUploadRequest = SensingFileUploadRequest()
//        sensingFileUploadRequest.Filename = file
//
//        var baseUrl = "" //"https://blbext.apollopharmacy.org:3443/SENSING/Apollo/SensingFileUpload"
//        var token = "" //"9f15bdd0fcd5423190cHNK"
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("SEN BLOB")) {
//                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
//                break
//            }
//        }
//
//
//        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
//        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
//        viewModelScope.launch {
//            state.value = State.SUCCESS
//            val response = withContext(Dispatchers.IO) {
//                ApolloSensingRepo.sensingFileUpload(
//                    baseUrl, "multipart/form-data", token, fileToUpload
//                )
//            }
//            when (response) {
//                is ApiResult.Success -> {
//                    state.value = State.SUCCESS
//                    if (response.value.status == true) {
//                        apolloSensingFragmentCallback.onSuccessSensingFileUploadApiCall(
//                            response.value!!,
//                            isLastImage!!
//                        )
//                    } else {
//                        apolloSensingFragmentCallback.onFailureSensingFileUploadApiCall(response.value!!)
//                    }
//                }
//
//                is ApiResult.GenericError -> {
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.NetworkError -> {
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.UnknownError -> {
//                    state.value = State.ERROR
//                }
//
//                is ApiResult.UnknownHostException -> {
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

}