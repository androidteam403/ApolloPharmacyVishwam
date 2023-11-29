package com.apollopharmacy.vishwam.ui.home.help

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyViewModel
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.help.model.HelpResponseModel
import com.apollopharmacy.vishwam.ui.home.notification.NotificationsActivityCallback
import com.apollopharmacy.vishwam.ui.home.notification.NotificationsViewModel
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.CommandsNewSwachImp
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HelpActivityViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    var command = LiveEvent<NotificationsViewModel.CmsCommand>()
    fun getHelpDetailsApi(
        helpActivityCallback: HelpActivityCallback
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
        var baseUrl = "https://apis.v35.apollodev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/vishwam_help/list/vishwam-help-list-for-select"
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
                                HelpResponseModel::class.java
                            )
                            if (storeWiseDetailListResponse.success!!) {
                                helpActivityCallback.onSuccessHelpDetails(storeWiseDetailListResponse)

//                                getSurveyListResponse.value =
//                                    surveyListResponse
                            } else {
                                helpActivityCallback.onFailureHelpDetails(storeWiseDetailListResponse)

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
                        NotificationsViewModel.CmsCommand.ShowToast(it)
                    })
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(NotificationsViewModel.CmsCommand.ShowToast("Network Error"))
                    state.value = State.ERROR

                }

                is ApiResult.UnknownError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(NotificationsViewModel.CmsCommand.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR

                }

                else -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    command.postValue(NotificationsViewModel.CmsCommand.ShowToast("Something went wrong, please try again later"))

                    state.value = State.ERROR
                }
            }
        }

    }
    sealed class CmsCommand {
        data class ShowToast(val message: String) : CmsCommand()
    }
}