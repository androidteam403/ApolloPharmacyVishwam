package com.apollopharmacy.vishwam.ui.home.retroqr.activity.retroqrscanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RetroQrRepo
import com.apollopharmacy.vishwam.ui.login.Command
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RetroQrScannerViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    fun getImageUrl(url: String, callback: RetroQrScannerCallback) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)
            val result = withContext(Dispatchers.IO) {
                RetroQrRepo.getImageUrl(url)
            }
            when (result) {
                is ApiResult.Success -> {
                    callback.onSuccessGetImageUrlApiCall(result.value)
                }
                else -> {}
            }
        }
    }
}