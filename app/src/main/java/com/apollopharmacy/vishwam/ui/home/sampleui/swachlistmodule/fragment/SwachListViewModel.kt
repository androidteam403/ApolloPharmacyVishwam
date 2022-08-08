package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    fun getPendingAndApprovedListApiCall() {
        val getpendingAndApprovedListRequest = GetpendingAndApprovedListRequest()
        getpendingAndApprovedListRequest.empid = "APL49396"
        getpendingAndApprovedListRequest.fromdate = "2022-08-02"
        getpendingAndApprovedListRequest.todate = "2022-08-06"
        getpendingAndApprovedListRequest.storeId = "16001"
        getpendingAndApprovedListRequest.startpageno = 0
        getpendingAndApprovedListRequest.endpageno = 100

        val state = MutableLiveData<State>()

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