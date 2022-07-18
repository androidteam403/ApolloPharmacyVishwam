package com.apollopharmacy.vishwam.ui.home.SwachhApollo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.SwachApiRepo
import com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model.SwachModelResponse
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class SwachhapolloModel : ViewModel()
{
    val commands = LiveEvent<Commands>()
    val state = MutableLiveData<State>()
    var swachhapolloModel = MutableLiveData<SwachModelResponse.Config>()
    var uniqueCategoryList = MutableLiveData<SwachModelResponse.Config>()

    fun swachImagesRegister() {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SwachApiRepo.swachImagesRegister()
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        swachhapolloModel.value = result.value.Config()



                    } else {
                        state.value = State.ERROR
                        commands.value = Commands.ShowToast(result.value.message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            Commands.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Commands.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Commands.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Commands.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

}

sealed class Commands {
    data class ShowToast(val message: String?) : Commands()
}