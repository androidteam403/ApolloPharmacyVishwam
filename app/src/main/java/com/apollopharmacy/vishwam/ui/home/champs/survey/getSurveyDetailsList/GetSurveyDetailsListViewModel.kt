package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsViewModel
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.model.TrainersEmailIdResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.GlobalConfigurationResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.notification.NotificationsActivityCallback
import com.apollopharmacy.vishwam.ui.home.notification.NotificationsViewModel
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetSurveyDetailsListViewModel:ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
//    fun getSurveyListApi(
//        getSurveyDetailsListCallback: GetSurveyDetailsListCallback,
//        startDate: String,
//        endDate: String,
//        id: String
//    ) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getSurveyDetailsApi(startDate, endDate, id);
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status!!) {
//                        state.value = State.ERROR
//                        getSurveyDetailsListCallback.onSuccessSurveyList(result.value)
////                        getStoreDetailsChamps.value = result.value
//                    } else {
//                        state.value = State.ERROR
//                        getSurveyDetailsListCallback.onFailureSurveyList(result.value)
//                        commands.value = Command.ShowToast(result.value.message!!)
//                    }
//                }
//                is ApiResult.GenericError -> {
//                    commands.postValue(result.error?.let {
//                        Command.ShowToast(it)
//                    })
//                    state.value = State.ERROR
//                }
//                is ApiResult.NetworkError -> {
//                    commands.postValue(Command.ShowToast("Network Error"))
//                    state.value = State.ERROR
//                }
//                is ApiResult.UnknownError -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//                else -> {
//                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
//                    state.value = State.ERROR
//                }
//            }
//        }
//    }

    fun getSurveyListApi(
        surveyDetailsCallback: GetSurveyDetailsListCallback,
        fromDate: String,
        toDate: String,
        validatedEmpId: String
    ) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP SURVEY DETAILS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetailsApi(baseUrl, token,fromDate, toDate, validatedEmpId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        surveyDetailsCallback.onSuccessSurveyList(result.value)
                    } else {
                        surveyDetailsCallback.onFailureSurveyList(result.value)
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message!!)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun getGlobalConfigApi(
        getSurveyDetailsListCallback: GetSurveyDetailsListCallback
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
        var baseUrl = "https://apis.v35.apollodev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/vishwam_global_config/list/vishwam-global-config-list-for-select"
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
//                ApnaRectroApiRepo.getGlobalConfig(
//                    baseUrl
//                )
//
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
                                GlobalConfigurationResponse::class.java
                            )
                            if (storeWiseDetailListResponse.success!!) {
                                getSurveyDetailsListCallback.onSuccessGlobalConfigDetails(storeWiseDetailListResponse)

//                                getSurveyListResponse.value =
//                                    surveyListResponse
                            } else {
                                getSurveyDetailsListCallback.onSuccessGlobalConfigDetails(storeWiseDetailListResponse)

                            }

                        }

                    } else {
                    }
//                    if (response.value.success ?: null == true) {
//                        state.value = State.ERROR
//                        getSurveyDetailsListCallback.onSuccessGlobalConfigDetails(response.value)
//                    } else {
//                        state.value = State.ERROR
//                        getSurveyDetailsListCallback.onFailureGlobalConfigDetails(response.value)
//                    }
                }

                is ApiResult.GenericError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    commands.postValue(response.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }

                is ApiResult.NetworkError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR

                }

                is ApiResult.UnknownError -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR

                }

                else -> {
//                    notificationsActivityCallback.onFailureNotificationDetail()
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))

                    state.value = State.ERROR
                }
            }
        }

    }
    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}