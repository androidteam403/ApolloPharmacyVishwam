package com.apollopharmacy.vishwam.ui.home.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.State

class HomeViewModel : ViewModel() {
    val state = MutableLiveData<State>()
}