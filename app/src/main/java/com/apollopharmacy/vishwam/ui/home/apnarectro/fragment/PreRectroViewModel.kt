package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.ApnaRectroApiRepo
import com.apollopharmacy.vishwam.data.network.SwachApiiRepo
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.SampleSwachUiCallback
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.SampleSwachViewModel
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreRectroViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    val commands = LiveEvent<CommandsApnaFrag>()


    sealed class CommandsApnaFrag {
        data class ShowToast(val message: String?) : CommandsApnaFrag()
    }
}