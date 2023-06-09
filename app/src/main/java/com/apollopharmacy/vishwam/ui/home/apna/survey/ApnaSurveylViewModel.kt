package com.apollopharmacy.vishwam.ui.home.apna.survey

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApnaSurveylViewModel : ViewModel() {
    val command = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var getSurveyListResponse = MutableLiveData<SurveyListResponse>()

    fun getApnaSurveyList(apnaSurveyCallback: ApnaSurveyCallback) {

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
        var baseUrl =
            "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/apna_project_survey/list/project-survey-list-for-mobile?employee_id=${Preferences.getValidatedEmpId()}"//admin

        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("")) {
                baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                break
            }

        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(baseUrl,
                        "GET",
                        "The",
                        "",
                        ""))


            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val surveyListResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                SurveyListResponse::class.java)
                            if (surveyListResponse.success == true) {
                                apnaSurveyCallback.onSuccessgetSurveyDetails(surveyListResponse)
                                getSurveyListResponse.value =
                                    surveyListResponse
                            } else {
                                apnaSurveyCallback.onFailuregetSurveyDetails(surveyListResponse)
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


}