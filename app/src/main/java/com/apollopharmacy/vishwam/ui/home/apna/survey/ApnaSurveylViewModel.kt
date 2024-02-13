package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import java.net.URLEncoder

class ApnaSurveylViewModel : ViewModel() {
    val command = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var getSurveyListResponse = MutableLiveData<SurveyListResponse>()

    fun getApnaSurveyList(
        apnaSurveyCallback: ApnaSurveyCallback,
        pageNo: String,
        rows: String,
        searchQuary: String,
        status: String,
        isSearch: Boolean,
        context: Context?,
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
        var dynamicUrl = ""
        var dynamicToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("APNA SURVEY LIST")) {
                dynamicUrl = data.APIS[i].URL
                dynamicToken = data.APIS[i].TOKEN
                break
            }
        }

        val new = if (status.contains("new")) "new" else ""
        val inprogress = if (status.contains("inprogress")) "in_progress" else ""
        val rejected = if (status.contains("rejected")) "rejected" else ""
        val approved = if (status.contains("approved")) "approved" else ""
        val cancelled = if (status.contains("cancelled")) "cancelled" else ""

        //Dev base url : https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/

        // https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/apna_project_survey/list/project-survey-list-for-mobile?
        var baseUrl = dynamicUrl
      //id
        baseUrl =
            baseUrl + "employee_id=${Preferences.getValidatedEmpId()}&page=${pageNo}&rows=${rows}" +

                    if (isSearch) {
                        "&survey_search=$searchQuary&"
                    } else {
                        "&"
                    } + "${
                URLEncoder.encode("status[0]", "utf-8")
            }=${new}&${
                URLEncoder.encode("status[1]", "utf-8")
            }=${inprogress}&${
                URLEncoder.encode("status[2]", "utf-8")
            }=${rejected}&${
                URLEncoder.encode("status[3]", "utf-8")
            }=${approved}&${
                URLEncoder.encode("status[4]", "utf-8")
            }=${cancelled}"



//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("")) {
//                baseUrl = data.APIS[i].URL
//                val token = data.APIS[i].TOKEN
//                break
//            }
//
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
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            try {
                                val res = BackShlash.removeBackSlashes(resp)
                                val surveyListResponse = Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    SurveyListResponse::class.java
                                )
                                if (surveyListResponse.success == true) {
                                    apnaSurveyCallback.onSuccessgetSurveyDetails(surveyListResponse)
//                                getSurveyListResponse.value =
//                                    surveyListResponse
                                } else {
                                    apnaSurveyCallback.onFailureGetSurveyDetails(surveyListResponse.message.toString())
                                    apnaSurveyCallback.onFailuregetSurveyDetails(surveyListResponse)
                                }
                            } catch (e: Exception) {
                                Log.e("API Error", "Received HTML response")
                                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show()
                                apnaSurveyCallback.onFailureUat();
                                // Handle parsing error, e.g., show an error message to the user
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