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
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlanogramActivityViewModel : ViewModel() {

    val state = MutableLiveData<State>()

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
            }
        }
    }
}