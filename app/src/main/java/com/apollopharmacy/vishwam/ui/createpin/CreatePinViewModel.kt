package com.apollopharmacy.vishwam.ui.createpin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.MPinRequest
import com.apollopharmacy.vishwam.data.model.MPinResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePinViewModel : ViewModel() {

    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()

    fun checkMPinLogin(mPinRequest: MPinRequest) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                LoginRepo.checkMPinDetails(mPinRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.Status) {
                        state.value = State.ERROR
                        commands.value = Command.NavigateTo(result.value)
                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.Message)
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
    data class NavigateTo(
        val value: MPinResponse,
    ) : Command()
}