package com.apollopharmacy.vishwam.ui.home.apollosensing.activity

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
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SiteListResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.SelectSiteActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApolloSensingStoreViewModel : ViewModel() {
    var siteLiveData = ArrayList<StoreListItem>()
    var command = LiveEvent<SelectSiteActivityViewModel.CmsCommandSelectSiteId>()
    val state = MutableLiveData<State>()
    var fixedArrayList = MutableLiveData<ArrayList<StoreListItem>>()

    fun siteList(empId: String, callback: ApolloSensingStoreCallback) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrL = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                baseUrL = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        var siteListUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SEN SITELIST")) {
                siteListUrl = data.APIS[i].URL
                break
            }
        }


//        val siteListUrl =
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/site/list/site-list-for-upload-apna-retro?"
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(
                    baseUrL,
                    token,
                    GetDetailsRequest(siteListUrl + "emp_id=" + empId, "GET", "The", "", "")
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    val resp: String = response.value.string()
                    val res = BackShlash.removeBackSlashes(resp)
                    val siteListResponse = Gson().fromJson(
                        BackShlash.removeSubString(res),
                        SiteListResponse::class.java
                    )
                    if (siteListResponse.success == true) {
                        callback.onSuccessSiteListApiCall(siteListResponse)
                    } else {

                    }
                }

                else -> {}
            }
        }
    }

    fun siteId() {
        if (Preferences.isSiteIdListFetched()) {
            siteLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getSiteIdListJson()
            val type = object : TypeToken<List<StoreListItem?>?>() {}.type

            this.siteLiveData =
                gson.fromJson<List<StoreListItem>>(siteIdList, type) as ArrayList<StoreListItem>
            command.value = SelectSiteActivityViewModel.CmsCommandSelectSiteId.ShowSiteInfo("")
            fixedArrayList.value = siteLiveData
        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var baseUrL = ""
            var token = ""
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                    baseUrL = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS GETSITELIST")) {
                    val baseUrl = data.APIS[i].URL
                    viewModelScope.launch {
                        state.value = State.SUCCESS
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.getDetails(
                                baseUrL, token,
                                GetDetailsRequest(
                                    baseUrl,
                                    "GET",
                                    "The",
                                    "",
                                    ""
                                )
                            )
//                        RegistrationRepo.selectSiteId(token, baseUrl)
                        }
                        when (response) {
                            is ApiResult.Success -> {
                                state.value = State.ERROR
                                val resp: String = response.value.string()
                                val res = BackShlash.removeBackSlashes(resp)
                                val reasonmasterV2Response =
                                    Gson().fromJson(
                                        BackShlash.removeSubString(res),
                                        SiteDto::class.java
                                    )

                                if (reasonmasterV2Response.status) {
                                    siteLiveData.clear()
                                    reasonmasterV2Response.siteData?.listData?.rows?.map {
                                        siteLiveData.add(it)
                                        fixedArrayList.value = siteLiveData
                                    }
                                    // getDepartment()
                                    command.value =
                                        SelectSiteActivityViewModel.CmsCommandSelectSiteId.ShowSiteInfo(
                                            ""
                                        )
                                } else {
                                    command.value =
                                        SelectSiteActivityViewModel.CmsCommandSelectSiteId.ShowToast(
                                            reasonmasterV2Response.message.toString()
                                        )
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
        }
    }

    fun getSiteData(): java.util.ArrayList<StoreListItem> {
        return siteLiveData
    }
}