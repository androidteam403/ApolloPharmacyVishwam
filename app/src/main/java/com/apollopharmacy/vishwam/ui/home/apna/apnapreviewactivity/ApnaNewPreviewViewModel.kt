package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApnaNewPreviewViewModel : ViewModel() {
    val command = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var getSurveyListResponse = MutableLiveData<SurveyDetailsList>()

    fun getApnaDetailsList(apnaNewPreviewCallBack: ApnaNewPreviewCallBack, uid: String) {

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
            if (data.APIS[i].NAME.equals("APNA SURVEY SELECT DETAILS")) {
                dynamicUrl = data.APIS[i].URL
                dynamicToken = data.APIS[i].TOKEN
                break
            }
        }
        // https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/apna_project_survey/select/?
        var baseUrl = dynamicUrl
        baseUrl = baseUrl + "uid=$uid"//EA4EED0B2C4D12CE92047C715E78DCB6
//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("")) {
//                baseUrl =
////                    "https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollocms/api/ticket_touch_point/list/?"
//                    data.APIS[i].URL
//                //val token = data.APIS[i].TOKEN
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
                                val surveyDetailsList = Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    SurveyDetailsList::class.java
                                )
                                if (surveyDetailsList.success) {
                                    apnaNewPreviewCallBack.onSuccessgetSurveyDetails(
                                        surveyDetailsList
                                    )
                                    getSurveyListResponse.value =
                                        surveyDetailsList


                                } else {
                                    state.value = State.ERROR
                                    apnaNewPreviewCallBack.onFailuregetSurveyWiseDetails(
                                        getSurveyListResponse.value!!
                                    )

                                }
                            } catch (e: Exception) {
                                Log.e("API Error", "Received HTML response")
//                                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show()

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