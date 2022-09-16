package com.apollopharmacy.vishwam.ui.home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.State
import com.hadilq.liveevent.LiveEvent

class HomeViewModel : ViewModel() {
    val state = MutableLiveData<State>()

    var command = LiveEvent<CmsCommand>()




    sealed class CmsCommand {

        data class ShowToast(val message: String) : CmsCommand()

    }
}

