package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import android.annotation.SuppressLint
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
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.model.GetEmailAddressModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class NewSurveyViewModel : ViewModel() {


    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var getStoreDetailsChamps = MutableLiveData<StoreDetailsModelResponse>()
    var getEmailDetailsChamps = MutableLiveData<GetEmailAddressModelResponse>()
    fun getStoreDetailsChamps(newSurveyCallback: NewSurveyCallback) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getStoreDetailsChamps();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.success == true) {
                        state.value = State.ERROR
                        newSurveyCallback.onSuccessgetStoreDetails(result.value.data!!.listData!!.rows!!)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        newSurveyCallback.onFailuregetStoreDetails(result.value)
                        commands.value = Command.ShowToast(result.value.message.toString())
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


    @SuppressLint("SuspiciousIndentation")
    fun getStoreDetailsChampsApi(newSurveyCallback: NewSurveyCallback, applicationContext: Context) {
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

        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS GETSITELIST")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }

        }//"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site/list/mobile-site-list"
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
                        var resp: String? = ""

                        try {
                            resp = response.value.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            try {
                                val res = BackShlash.removeBackSlashes(resp)
                                val gson = Gson()
                                val type = object : TypeToken<StoreDetailsModelResponse>() {}.type

                                val storeListResponse = gson.fromJson<StoreDetailsModelResponse>(
                                    BackShlash.removeSubString(res), type
                                )
                                if (storeListResponse.data != null) {
                                    newSurveyCallback.onSuccessgetStoreDetails(storeListResponse.data!!.listData!!.rows!!)

//                                getSurveyListResponse.value =
//                                    surveyListResponse
                                } else {
                                    newSurveyCallback.onFailuregetStoreDetails(storeListResponse)

                                }
                            } catch (e: Exception) {
                                Log.e("API Error", "Received HTML response")
                                Toast.makeText(applicationContext, "Please try again later", Toast.LENGTH_SHORT).show()
                                newSurveyCallback.onFailureUat();

                                // Handle parsing error, e.g., show an error message to the user
                            }


                        } else {

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


    fun getStoreWiseDetailsChampsApi(
        newSurveyCallback: NewSurveyCallback,
        siteId: String,
        applicationContext: Context
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
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CHMP GET STORE USERS")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }

        }//https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site/select/get-store-users?
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(
                    proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(
                        "${baseUrl}site=${siteId}",
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
                                val storeWiseDetailListResponse = Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    GetStoreWiseDetailsModelResponse::class.java
                                )
                                if (storeWiseDetailListResponse.success) {
                                    newSurveyCallback.onSuccessgetStoreWiseDetails(
                                        storeWiseDetailListResponse
                                    )

//                                getSurveyListResponse.value =
//                                    surveyListResponse
                                } else {
                                    newSurveyCallback.onFailuregetStoreWiseDetails(
                                        storeWiseDetailListResponse
                                    )

                                }
                            } catch (e: Exception) {
                                Log.e("API Error", "Received HTML response")
                                Toast.makeText(applicationContext, "Please try again later", Toast.LENGTH_SHORT).show()

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


    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}