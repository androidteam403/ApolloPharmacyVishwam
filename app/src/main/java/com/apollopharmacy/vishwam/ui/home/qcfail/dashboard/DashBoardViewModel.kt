package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.QcApiRepo
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcRegionList
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashBoardViewModel: ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    val qcPendingCountList = MutableLiveData<PendingCountResponse>()




    fun getQcPendingList(empId: String,designation: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)


        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getqcPendingCountList(empId, designation)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        qcPendingCountList.value = result.value

                    } else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }




}