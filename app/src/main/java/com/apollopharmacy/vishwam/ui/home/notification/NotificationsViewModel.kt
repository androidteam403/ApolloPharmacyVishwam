package com.apollopharmacy.vishwam.ui.home.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.model.TrainersEmailIdResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    var command = LiveEvent<CmsCommand>()
    fun getNotificationDetailsApi(
        notificationsActivityCallback: NotificationsActivityCallback,
        page: Int,
        rows: Int
    ) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var proxyBaseUrl = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyBaseUrl = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }
        var baseUrl = "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/vishwam_notifications/list/vishwam-notifications-list-for-mobile?page=1&rows=10&zcFetchListTotal=true"
        var token = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("RT PENDING APPROVED LIST")) {
//                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
//                break
//            }
//        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(
                    proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(
                        baseUrl,
                        "GET",
                        "The",
                        "",
                        ""
                    )
                )


            }

//            val response = withContext(Dispatchers.IO) {
//                ApnaRectroApiRepo.getNotificationList(
//                    baseUrl
//                )

//            }
            when (response) {

                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val storeWiseDetailListResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                NotificationModelResponse::class.java
                            )
                            if (storeWiseDetailListResponse.success!!) {
                                notificationsActivityCallback.onSuccessNotificationDetails(storeWiseDetailListResponse)

//                                getSurveyListResponse.value =
//                                    surveyListResponse
                            } else {
                                notificationsActivityCallback.onFailureNotificationDetails(storeWiseDetailListResponse)

                            }

                        }

                    } else {
                    }

//                    if (response.value.success ?: null == true) {
//                        state.value = State.ERROR
//                        notificationsActivityCallback.onSuccessNotificationDetails(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        notificationsActivityCallback.onFailureNotificationDetails(response.value)
//                    }
                }

                is ApiResult.GenericError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(response.error?.let {
                        CmsCommand.ShowToast(it)
                    })
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(CmsCommand.ShowToast("Network Error"))
                    state.value = State.ERROR

                }

                is ApiResult.UnknownError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(CmsCommand.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR

                }

                else -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(CmsCommand.ShowToast("Something went wrong, please try again later"))

                    state.value = State.ERROR
                }
            }
        }

    }
    sealed class CmsCommand {
        data class ShowToast(val message: String) : CmsCommand()
    }

}