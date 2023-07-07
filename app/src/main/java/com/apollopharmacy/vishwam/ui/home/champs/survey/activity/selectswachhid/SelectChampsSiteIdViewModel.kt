package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.SiteDto
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyCallback
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyViewModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.model.GetEmailAddressModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.SelectSiteActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SelectChampsSiteIdViewModel : ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var getStoreDetailsChamps = MutableLiveData<StoreDetailsModelResponse>()
    var getEmailDetailsChamps = MutableLiveData<GetEmailAddressModelResponse>()
//    fun siteId() {
//        if (Preferences.isSiteIdListFetched()) {
//            siteLiveData.clear()
//            val gson = Gson()
//            val siteIdList = Preferences.getSiteIdListJson()
//            val type = object : TypeToken<List<StoreListItem?>?>() {}.type
//
//            this.siteLiveData =
//                gson.fromJson<List<StoreListItem>>(siteIdList, type) as ArrayList<StoreListItem>
//            command.value = SelectSiteActivityViewModel.CmsCommandSelectSiteId.ShowSiteInfo("")
//            fixedArrayList.value = siteLiveData
//        } else {
//            val url = Preferences.getApi()
//            val data = Gson().fromJson(url, ValidateResponse::class.java)
//
//            var baseUrL = ""
//            var token = ""
//            for (i in data.APIS.indices) {
//                if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
//                    baseUrL = data.APIS[i].URL
//                    token = data.APIS[i].TOKEN
//                    break
//                }
//            }
//            for (i in data.APIS.indices) {
//                if (data.APIS[i].NAME.equals("CMS GETSITELIST")) {
//                    val baseUrl = data.APIS[i].URL
//                    viewModelScope.launch {
//                        state.value = State.SUCCESS
//                        val response = withContext(Dispatchers.IO) {
//                            RegistrationRepo.getDetails(
//                                baseUrL,token,
//                                GetDetailsRequest(
//                                    baseUrl,
//                                    "GET",
//                                    "The",
//                                    "",
//                                    ""
//                                )
//                            )
////                        RegistrationRepo.selectSiteId(token, baseUrl)
//                        }
//                        when (response) {
//                            is ApiResult.Success -> {
//                                state.value = State.ERROR
//                                val resp: String = response.value.string()
//                                val res = BackShlash.removeBackSlashes(resp)
//                                val reasonmasterV2Response =
//                                    Gson().fromJson(BackShlash.removeSubString(res),
//                                        SiteDto::class.java
//                                    )
//
//                                if (reasonmasterV2Response.status) {
//                                    siteLiveData.clear()
//                                    reasonmasterV2Response.siteData?.listData?.rows?.map {
//                                        siteLiveData.add(it)
//                                        fixedArrayList.value = siteLiveData
//                                    }
//                                    // getDepartment()
//                                    command.value =
//                                        SelectSiteActivityViewModel.CmsCommandSelectSiteId.ShowSiteInfo(
//                                            "")
//                                } else {
//                                    command.value =
//                                        SelectSiteActivityViewModel.CmsCommandSelectSiteId.ShowToast(
//                                            reasonmasterV2Response.message.toString()
//                                        )
//                                }
//                            }
//                            is ApiResult.GenericError -> {
//                                state.value = State.ERROR
//                            }
//                            is ApiResult.NetworkError -> {
//                                state.value = State.ERROR
//                            }
//                            is ApiResult.UnknownError -> {
//                                state.value = State.ERROR
//                            }
//                            is ApiResult.UnknownHostException -> {
//                                state.value = State.ERROR
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//    fun getSiteData(): ArrayList<StoreListItem> {
//        return siteLiveData
//    }

    fun getStoreDetailsChamps(selectChampsSiteIdCallBack: SelectChampsSiteIdCallback) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getStoreDetailsChamps();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR
                        selectChampsSiteIdCallBack.onSuccessgetStoreDetails(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        selectChampsSiteIdCallBack.onFailuregetStoreDetails(result.value)
                        commands.value = Command.ShowToast(result.value.message)
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


    fun getStoreDetailsChampsApi(selectChampsSiteIdCallBack: SelectChampsSiteIdCallback) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getStoreDetailsChampsApi()
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status) {
                        state.value = State.ERROR
                        selectChampsSiteIdCallBack.onSuccessgetStoreDetails(result.value)
                    } else {
                        state.value = State.ERROR
                        selectChampsSiteIdCallBack.onFailuregetStoreDetails(result.value)
                        commands.value = Command.ShowToast(result.value.message)
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
    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}