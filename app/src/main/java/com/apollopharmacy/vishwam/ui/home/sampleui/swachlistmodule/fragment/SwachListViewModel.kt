package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachhListRepo
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListRequest
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model.GetpendingAndApprovedListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwachListViewModel : ViewModel() {

    var getpendingAndApprovedListResponse = MutableLiveData<GetpendingAndApprovedListResponse>()




    fun getPendingAndApprovedListApiCall(
        empiD: String,
        fromDate: String,
        toDate: String,
        selectedSiteids: String?
    ) {
        val state = MutableLiveData<State>()
        state.postValue(State.LOADING)
        val getpendingAndApprovedListRequest = GetpendingAndApprovedListRequest()
//        getpendingAndApprovedListRequest.empid = empiD
        getpendingAndApprovedListRequest.empid = Preferences.getValidatedEmpId()
        getpendingAndApprovedListRequest.fromdate = fromDate
        getpendingAndApprovedListRequest.todate = toDate
        getpendingAndApprovedListRequest.storeId = Preferences.getSiteId() + "," +selectedSiteids
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



}