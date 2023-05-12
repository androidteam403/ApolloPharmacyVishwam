package com.apollopharmacy.vishwam.ui.otpview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.DeviceDeRegResponse
import com.apollopharmacy.vishwam.data.model.ValidateOtpRequest
import com.apollopharmacy.vishwam.data.model.ValidateOtpResponse
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ValidateOtpRepo
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpViewModel : ViewModel() {

    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var validateOtpModel = MutableLiveData<ValidateOtpResponse>()
    var deviceDeRegModel = MutableLiveData<DeviceDeRegResponse>()

    fun checkEmpAvailability(validateOtpRequest: ValidateOtpRequest) {
        state.postValue(State.LOADING)

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("OTP LOGIN VISWAMAPP")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ValidateOtpRepo.validateEmpWithOtp(
                    baseUrl,
                    token,
                    validateOtpRequest
                )//Config.ATTENDANCE_API_HEADER
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.STATUS) {
                        state.value = State.ERROR
                        Preferences.setEmpPhoneNumber(result.value.MOBILENO)
                        validateOtpModel.value = result.value
                    } else {
                        state.value = State.ERROR
                        println(41)
                        println(result.value)
                        println(43)
                        validateOtpModel.value = result.value
                        commands.value = Command.ShowToast(result.value.MESSAGE)
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

    fun deRegisterDevice(empId: String) {
        state.postValue(State.LOADING)
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("DEACTIVATE")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ValidateOtpRepo.deviceDeRegister(baseUrl, token, empId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.Status) {
                        state.value = State.ERROR
                        deviceDeRegModel.value = result.value
                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.Message)
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
}

sealed class Command {
    data class ShowToast(val message: String) : Command()
}