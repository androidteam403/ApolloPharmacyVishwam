package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.SiteDto
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.data.network.SwachhListRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class SwachListViewModel : ViewModel() {

    var getpendingAndApprovedListResponse = MutableLiveData<GetpendingAndApprovedListResponse>()
    var siteLiveData = ArrayList<StoreListItem>()
    var command = LiveEvent<CmsCommand>()



    fun getPendingAndApprovedListApiCall(empiD: String, fromDate: String, toDate: String) {
        val state = MutableLiveData<State>()
        state.postValue(State.LOADING)
        val getpendingAndApprovedListRequest = GetpendingAndApprovedListRequest()
//        getpendingAndApprovedListRequest.empid = empiD
        getpendingAndApprovedListRequest.empid = Preferences.getValidatedEmpId()
        getpendingAndApprovedListRequest.fromdate = fromDate
        getpendingAndApprovedListRequest.todate = toDate
        getpendingAndApprovedListRequest.storeId = ""
        getpendingAndApprovedListRequest.startpageno = 0
        getpendingAndApprovedListRequest.endpageno = 100

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                SwachhListRepo.getPendingAndApprovedListRepo(getpendingAndApprovedListRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    when (response.value.status) {
                        true -> {
                            getpendingAndApprovedListResponse.value = response.value
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


    fun siteId() {
        val state = MutableLiveData<State>()
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
                                Gson().fromJson(
                                    BackShlash.removeSubString(res),
                                    SiteDto::class.java
                                )

                            if (reasonmasterV2Response.status) {
                                siteLiveData.clear()
                                reasonmasterV2Response.siteData?.listData?.rows?.map { siteLiveData.add(it) }
                                // getDepartment()
                                command.value = CmsCommand.ShowSiteInfo("")
                            } else {
                                command.value = CmsCommand.ShowToast(
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

    sealed class CmsCommand {

        data class ShowToast(val message: String) : CmsCommand()

        data class ShowSiteInfo(val message: String) : CmsCommand()
    }
}