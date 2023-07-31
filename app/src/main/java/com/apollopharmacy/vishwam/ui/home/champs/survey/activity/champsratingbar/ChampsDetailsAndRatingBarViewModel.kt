package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzureChamps
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ChampsApiRepo
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ChampsDetailsAndRatingBarViewModel : ViewModel() {
    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    fun getSubCategoryDetailsChamps(champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSubCategoryDetailsChamps();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsDetailsandRatingBarCallBack.onSuccessgetSubCategoryDetails(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsDetailsandRatingBarCallBack.onFailuregetSubCategoryDetails(result.value)
                        commands.value = Command.ShowToast(result.value.message!!)
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

    fun getSubCategoryDetailsChampsApi(
        champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack,
        categoryName: String,
    ) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSubCategoryDetailsChampsApi(categoryName)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsDetailsandRatingBarCallBack.onSuccessgetSubCategoryDetails(result.value)

                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.message!!)
                        champsDetailsandRatingBarCallBack.onFailuregetSubCategoryDetails(result.value)
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

    fun connectToAzure(
        image: File?,
        champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack,
    ) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response = ConnectionAzureChamps.connectToAzur(
                image,
                Config.CONTAINER_NAME_CHAMPS,
                Config.STORAGE_CONNECTION_FOR_CCR_APP
            )
            champsDetailsandRatingBarCallBack.onSuccessImageIsUploadedInAzur(response)



//            commands.postValue(Command.ImageIsUploadedInAzur(response!!))
        }
    }

    fun getSurveyListByChampsID(champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack) {
        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ChampsApiRepo.getSurveyDetailsByChampsId();
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status!!) {
                        state.value = State.ERROR
                        champsDetailsandRatingBarCallBack.onSuccessGetSurveyDetailsByChampsId(result.value)
//                        getStoreDetailsChamps.value = result.value
                    } else {
                        state.value = State.ERROR
                        champsDetailsandRatingBarCallBack.onFailureGetSurveyDetailsByChampsId(result.value)
                        commands.value = Command.ShowToast(result.value.message!!)
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

    sealed class Command {
        data class ShowToast(val message: String) : Command()
        data class ImageIsUploadedInAzur(val filePath: String) : Command()
    }
}