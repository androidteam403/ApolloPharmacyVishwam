package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.selectswachhid

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
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.siteIdselect.SelectSiteActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SelectSwachhSiteIdViewModel : ViewModel() {
    var siteLiveData = ArrayList<StoreListItem>()
    var command = LiveEvent<SelectSiteActivityViewModel.CmsCommandSelectSiteId>()
    val state = MutableLiveData<State>()
    var fixedArrayList = MutableLiveData<ArrayList<StoreListItem>>()
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
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("CMS GETSITELIST")) {
                    val baseUrl = data.APIS[i].URL
                    val token = data.APIS[i].TOKEN
                    viewModelScope.launch {
                        state.value = State.SUCCESS
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.getDetails(
                                "h72genrSSNFivOi/cfiX3A==",
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
                                    Gson().fromJson(BackShlash.removeSubString(res),
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
                                            "")
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
    fun getSiteData(): ArrayList<StoreListItem> {
        return siteLiveData
    }
}