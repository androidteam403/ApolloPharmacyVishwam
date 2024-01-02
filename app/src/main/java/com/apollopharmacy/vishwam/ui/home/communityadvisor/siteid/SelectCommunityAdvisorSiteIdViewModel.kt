package com.apollopharmacy.vishwam.ui.home.communityadvisor.siteid

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
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectCommunityAdvisorSiteIdViewModel : ViewModel() {
    val commands = LiveEvent<SelectCommunityAdvisorSiteIdViewModel.Command>()
    var siteLiveData = ArrayList<StoreDetailsModelResponse.Row>()
    val state = MutableLiveData<State>()
    var fixedArrayList = MutableLiveData<ArrayList<StoreDetailsModelResponse.Row>>()
    var getStoreDetailsChamps = MutableLiveData<StoreDetailsModelResponse>()
    var getEmailDetailsChamps = MutableLiveData<GetEmailAddressModelResponse>()


    fun getProxySiteListResponse(selectCommunityAdvisorSiteIdCallback: SelectCommunityAdvisorSiteIdCallback) {
        if (Preferences.isSiteIdListFetchedChamps()) {
            siteLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getSiteIdListJsonChamps()
            val type = object : TypeToken<List<StoreDetailsModelResponse.Row?>?>() {}.type

            this.siteLiveData = gson.fromJson<List<StoreDetailsModelResponse.Row>>(
                siteIdList,
                type
            ) as ArrayList<StoreDetailsModelResponse.Row>
//            commands.value = Command.ShowToast("")
            fixedArrayList.value = siteLiveData
        } else {

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
                            val resp: String = response.value.string()
                            if (resp != null) {
                                val res = BackShlash.removeBackSlashes(resp)
                                val storeListResponse = Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    StoreDetailsModelResponse::class.java
                                )
                                if (storeListResponse.success == true) {
                                    siteLiveData.clear()
                                    storeListResponse.data!!.listData!!.rows!!.map {
                                        siteLiveData.add(it)
                                    }
                                    fixedArrayList.value = siteLiveData
//                                    commands.value = Command.ShowToast("")
                                } else {
                                    selectCommunityAdvisorSiteIdCallback.onFailuregetStoreDetails(
                                        storeListResponse
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

    fun getSiteData(): ArrayList<StoreDetailsModelResponse.Row> {
        return siteLiveData
    }

    sealed class Command {
        data class ShowToast(val message: String) : Command()
    }
}

