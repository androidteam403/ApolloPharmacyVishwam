package com.apollopharmacy.vishwam.ui.home.planogram.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.PlanogramRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramDetailsListResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.CommandsNewSwachImp
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlanogramActivityViewModel : ViewModel() {

    val state = MutableLiveData<State>()
    val commands = LiveEvent<Command>()

    fun planogramDetailListApi(planogramActivityCallback: PlanogramActivityCallback, uid: String) {
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

        var baseUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/planogram/select/planogram-details-for-mobile?"
        var token = ""
        /* for (i in data.APIS.indices) {
             if (data.APIS[i].NAME.equals("")) {
                 baseUrl = data.APIS[i].URL
                 token = data.APIS[i].TOKEN
                 break
             }
         }*/

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                PlanogramRepo.planogramSurveyQuestionsListApiCAll(
                    baseUrL,
                    token1,
                    GetDetailsRequest(baseUrl + "uid=${uid}", "GET", "{}", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val planogramDetailsListResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    PlanogramDetailsListResponse::class.java
                                )
                            if (planogramDetailsListResponse.success!!) {
                                planogramActivityCallback.onSuccessPlanogramDetailListApiCall(
                                    planogramDetailsListResponse
                                )
                            } else {
                                planogramActivityCallback.onFailurePlanogramSurveyQuestionsListApiCall(
                                    planogramDetailsListResponse.message!!.toString()
                                )
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

                else -> {}
            }
        }
    }

    fun planogramSurveyQuestionsListApi(planogramActivityCallback: PlanogramActivityCallback) {

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

        var baseUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/planogram/list/planogram-survey-questions-list"
        var token = ""
        /* for (i in data.APIS.indices) {
             if (data.APIS[i].NAME.equals("")) {
                 baseUrl = data.APIS[i].URL
                 token = data.APIS[i].TOKEN
                 break
             }
         }*/

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                PlanogramRepo.planogramSurveyQuestionsListApiCAll(
                    baseUrL,
                    token1,
                    GetDetailsRequest(baseUrl, "GET", "{}", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val planogramSurveyQuestionsListResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    PlanogramSurveyQuestionsListResponse::class.java
                                )
                            if (planogramSurveyQuestionsListResponse.success!!) {
                                planogramActivityCallback.onSuccessPlanogramSurveyQuestionsListApiCall(
                                    planogramSurveyQuestionsListResponse
                                )
                            } else {
                                planogramActivityCallback.onFailurePlanogramSurveyQuestionsListApiCall(
                                    planogramSurveyQuestionsListResponse.message!!
                                )
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

                else -> {}
            }
        }
    }

    fun planogramSaveUpdateApi(
        planogramActivityCallback: PlanogramActivityCallback,
        planogramSaveUpdateRequest: PlanogramSaveUpdateRequest,
    ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)

        var baseUrLProxy = ""
        var tokenProxy = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrLProxy = data.APIS[i].URL
                tokenProxy = data.APIS[i].TOKEN
                break
            }
        }

        var baseUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/planogram/save-update/planogram-save-update"
        var token = ""
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS SAVE UPDATE")) {
//                baseUrl = data.APIS[i].URL
//                token = data.APIS[i].TOKEN
//                break
//            }
//        }
        val saveUpdateRequestJson = Gson().toJson(planogramSaveUpdateRequest)
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrLProxy,
                    tokenProxy,
                    GetDetailsRequest(baseUrl, "POST", saveUpdateRequestJson, "", "")
                )
//                ChampsApiRepo.saveUpdateApi(baseUrl, saveUpdateRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val saveUpdateRequestJsonResponse =
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    PlanogramSaveUpdateResponse::class.java
                                )
                            planogramActivityCallback.onSuccessSaveUpdateApi(
                                saveUpdateRequestJsonResponse
                            )
                            if (saveUpdateRequestJsonResponse.success ?: null == false) {
                                state.value = State.ERROR
                                CommandsNewSwachImp.ShowToast(saveUpdateRequestJsonResponse.message)
                                planogramActivityCallback.onFailureSaveUpdateApi(
                                    saveUpdateRequestJsonResponse
                                )
                            }
                        }
                    }

                }

                is ApiResult.GenericError -> {
                    commands.postValue(response.error?.let {
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

    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}