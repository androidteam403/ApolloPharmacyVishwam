package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.model.GetEmailAddressModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsResponse
import com.apollopharmacy.vishwam.ui.rider.login.BackSlash
import com.apollopharmacy.vishwam.ui.rider.login.model.LoginResponse
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.ArrayList

class NewSurveyViewModel  : ViewModel() {

//    import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

    val commands = LiveEvent<Command>()
    var siteLiveData = ArrayList<StoreDetailsResponse.Row>()

    val state = MutableLiveData<State>()
    var getStoreDetailsChamps = MutableLiveData<StoreDetailsResponse>()
    var getEmailDetailsChamps = MutableLiveData<GetEmailAddressModelResponse>()
//    fun getStoreDetailsChamps(newSurveyCallback: NewSurveyCallback) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getStoreDetailsChamps();
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value!=null) {
//                        state.value = State.ERROR
//                        newSurveyCallback.onSuccessgetStoreDetails(result.value)
////                        getStoreDetailsChamps.value = result.value
//                    } else {
//                        state.value = State.ERROR
//                        newSurveyCallback.onFailuregetStoreDetails(result.value)
////                        commands.value = Command.ShowToast(result.value.message)
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



    @SuppressLint("SuspiciousIndentation")
    fun getProxySiteListResponse(newSurveyCallback: NewSurveyCallback) {
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

        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("")) {
                val  baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                break
            }

        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(
                    proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(
                        "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site/list/mobile-site-list",
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
                             resp= response.value.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)
                            val gson = Gson()
                            val type = object : TypeToken<StoreDetailsResponse>() {}.type

                            val storeListResponse = gson.fromJson<StoreDetailsResponse>(BackSlash.removeSubString(res), type)
                            if (storeListResponse.data!=null) {
                                newSurveyCallback.onSuccessgetStoreDetails(storeListResponse.data!!.listData!!.rows!!)

//                                getSurveyListResponse.value =
//                                    surveyListResponse
                            } else {
                                newSurveyCallback.onFailuregetStoreDetails(storeListResponse)

                            }

                        }
                        else{

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


    fun getProxyStoreWiseDetailResponse(newSurveyCallback: NewSurveyCallback, siteId: String) {
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

        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("")) {
                val  baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                break
            }

        }
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {

                RegistrationRepo.getDetails(
                    proxyBaseUrl,
                    proxyToken,
                    GetDetailsRequest(
                        "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site/select/get-store-users?site=${siteId}",
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
                            val res = BackShlash.removeBackSlashes(resp)
                            val storeWiseDetailListResponse = Gson().fromJson(
                                BackShlash.removeSubString(res),
                                GetStoreWiseDetailsResponse::class.java
                            )
                            if (storeWiseDetailListResponse.success) {
                                newSurveyCallback.onSuccessgetStoreWiseDetails(storeWiseDetailListResponse)

//                                getSurveyListResponse.value =
//                                    surveyListResponse
                            } else {
                                newSurveyCallback.onFailuregetStoreWiseDetails(storeWiseDetailListResponse)

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





//    fun getStoreWiseDetailsChampsApi(newSurveyCallback: NewSurveyCallback, empId: String) {
//        state.postValue(State.LOADING)
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                ChampsApiRepo.getStoreWiseDetailsChampsApi(empId)
//            }
//            when (result) {
//                is ApiResult.Success -> {
//                    if (result.value.status) {
//                        state.value = State.ERROR
//                        newSurveyCallback.onSuccessgetStoreWiseDetails(result.value)
//                    } else {
//                        state.value = State.ERROR
//                        commands.value = Command.ShowToast(result.value.message)
//                        newSurveyCallback.onFailuregetStoreWiseDetails(result.value)
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


    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}