package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.sampleswachui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachApiRepo
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.swachuploadfragment.CommandsNew
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SampleSwachViewModel : ViewModel(){
    val commands = LiveEvent<CommandsNew>()
    val state = MutableLiveData<State>()
    var swachhapolloModel = MutableLiveData<SwachModelResponse>()


    fun swachImagesRegisters() {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiRepo.swachImagesRegister()
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        swachhapolloModel.value = result.value
                    }

                    else {
                        state.value = State.ERROR
                        commands.value = CommandsNew.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            CommandsNew.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(CommandsNew.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(CommandsNew.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(CommandsNew.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }
}