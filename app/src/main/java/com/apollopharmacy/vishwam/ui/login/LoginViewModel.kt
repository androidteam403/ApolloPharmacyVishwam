package com.apollopharmacy.vishwam.ui.login

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.*
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()

    fun checkLogin(loginRequest: LoginRequest) {
        if (loginRequest.EMPID.isEmpty()) {
            commands.postValue(Command.ShowToast("Please Enter User ID"))
        } else if (loginRequest.PASSWORD.isEmpty()) {
            commands.postValue(Command.ShowToast("Please Enter Password"))
        } else if(loginRequest.COMPANY.isEmpty()){
            commands.postValue(Command.ShowToast("Please Select company"))
        } else{
            state.postValue(State.LOADING)
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("DISCOUNT LOGIN")) {
                    val loginUrl = data.APIS[i].URL
                    viewModelScope.launch {
                        val result = withContext(Dispatchers.IO) {
                            LoginRepo.checkLoginUse(loginRequest, loginUrl)
                        }
                        when (result) {
                            is ApiResult.Success -> {
                                if (result.value.STATUS) {
                                    state.value = State.ERROR
                                    commands.postValue(Command.ShowToast("Successfully login"))
                                    Preferences.saveSiteId(result.value.STOREDETAILS.get(0).SITEID)
                                    LoginRepo.saveProfile(result.value,loginRequest.PASSWORD)
                                    Preferences.savingToken(result.value.EMPID)
                                    Preferences.storeLoginJson(Gson().toJson(result.value))
                                    Preferences.setLoginDate(Utils.getCurrentDate())
                                    commands.value = Command.NavigateTo(result.value)
                                } else {
                                    state.value = State.ERROR
                                    commands.value = Command.ShowToast(result.value.MESSAGE)
                                }
                            }
                            is ApiResult.GenericError -> {
                                commands.postValue(
                                    result.error?.let {
                                        Command.ShowToast(it)
                                    }
                                )
                                state.value = State.ERROR
                            }
                            is ApiResult.NetworkError -> {
                                commands.postValue(Command.ShowToast("Network Error"))
                                state.value = State.ERROR
                            }
                            is ApiResult.UnknownError -> {
                                commands.postValue(Command.ShowToast("An error has occurred"))
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
        }
    }

    fun checkMPinLogin(mPinRequest: MPinRequest) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                LoginRepo.checkMPinDetails(mPinRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    commands.value = Command.MpinValidation(result.value)
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                           Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("An error has occurred"))
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
    data class NavigateTo(
        val value: LoginDetails,
    ) : Command()
    data class MpinValidation(
        val value: MPinResponse,
    ) : Command()
    data class ShowButtonSheet(
        val fragment: Class<out BottomSheetDialogFragment>,
        val arguments: Bundle,
    ) :
        Command()
}