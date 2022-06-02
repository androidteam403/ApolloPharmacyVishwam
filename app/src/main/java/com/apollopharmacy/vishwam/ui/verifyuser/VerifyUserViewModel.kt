package com.apollopharmacy.vishwam.ui.verifyuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.State

class VerifyUserViewModel : ViewModel() {
    val state = MutableLiveData<State>()
}

sealed class Command {
    data class ShowToast(val message: String) : Command()
}