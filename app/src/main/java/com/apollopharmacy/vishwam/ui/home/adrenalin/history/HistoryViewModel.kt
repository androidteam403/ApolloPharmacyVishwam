package com.apollopharmacy.vishwam.ui.home.adrenalin.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.attendance.AttendanceHistoryRes
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.AttendanceRepo
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel : ViewModel() {
    var historyData = MutableLiveData<ArrayList<AttendanceHistoryRes>>()
    var historyCommand = LiveEvent<HistoryCommand>()
    val state = MutableLiveData<State>()
    val TAG = "HistoryViewModel"

    fun getAttendanceHistory(empId: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCE HISTORY")) {
                val token = data.APIS[i].TOKEN
                val baseUrl = data.APIS[i].URL
                state.value = State.SUCCESS
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.getAttendanceHistory(token, baseUrl, empId)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            Utils.printMessage(TAG, "History List :: " + response.value.toString())
                            historyData.value = response.value
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            historyCommand.value =
                                HistoryCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            historyCommand.value = HistoryCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            historyCommand.value =
                                HistoryCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            historyCommand.value =
                                HistoryCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }
}

sealed class HistoryCommand {
    data class ShowToast(val message: String) : HistoryCommand()
}
