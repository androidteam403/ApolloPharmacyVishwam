package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzureSwacch
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachApiRepo
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadNowButtonViewModel : ViewModel() {

    val commandsNewSwach = LiveEvent<CommandsNewSwach>()
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
                        commandsNewSwach.value = CommandsNewSwach.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commandsNewSwach.postValue(
                        result.error?.let {
                            CommandsNewSwach.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commandsNewSwach.postValue(CommandsNewSwach.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commandsNewSwach.postValue(CommandsNewSwach.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commandsNewSwach.postValue(CommandsNewSwach.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun connectToAzure(image: SwachModelResponse.Config.ImgeDtcl?) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ConnectionAzureSwacch.connectToAzur(image,
                    Config.CONTAINER_NAME,
                    Config.STORAGE_CONNECTION_FOR_CCR_APP
                )

            commandsNewSwach.postValue(CommandsNewSwach.ImageIsUploadedInAzur(response))
        }
    }

}

sealed class CommandsNewSwach {
    data class ShowToast(val message: String?) : CommandsNewSwach()
    data class ImageIsUploadedInAzur(val filePath: SwachModelResponse.Config.ImgeDtcl) :
        CommandsNewSwach()
}