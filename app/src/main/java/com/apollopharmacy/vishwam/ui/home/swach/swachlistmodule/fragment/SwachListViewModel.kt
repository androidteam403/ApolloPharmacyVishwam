package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachhListRepo
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.GetpendingAndApprovedListRequest
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwachListViewModel : ViewModel() {

    var getpendingAndApprovedListResponse = MutableLiveData<GetpendingAndApprovedListResponse>()


    fun getPendingAndApprovedListApiCall(
        empiD: String,
        fromDate: String,
        toDate: String,
        selectedSiteids: String?,
        startPage: Int,
        endPageNum: Int,
    ) {
        val state = MutableLiveData<State>()
        state.postValue(State.LOADING)
        val getpendingAndApprovedListRequest = GetpendingAndApprovedListRequest()
//        getpendingAndApprovedListRequest.empid = empiD
        getpendingAndApprovedListRequest.empid = Preferences.getValidatedEmpId()
        getpendingAndApprovedListRequest.fromdate = fromDate
        getpendingAndApprovedListRequest.todate = toDate
        getpendingAndApprovedListRequest.startpageno = startPage
        getpendingAndApprovedListRequest.endpageno = endPageNum
        if (selectedSiteids != null && selectedSiteids.length > 0) {
            getpendingAndApprovedListRequest.storeId = selectedSiteids
        } else {
            getpendingAndApprovedListRequest.storeId = ""
        }

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SW PENDING APPROVED LIST")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }


//        getpendingAndApprovedListRequest.startpageno = 0
//        getpendingAndApprovedListRequest.endpageno = 100

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                SwachhListRepo.getPendingAndApprovedListRepo(baseUrl,
                    token,
                    getpendingAndApprovedListRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    when (response.value.status) {
                        true -> {
                            getpendingAndApprovedListResponse.value = response.value
                        }
                        else -> {}
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