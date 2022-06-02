package com.apollopharmacy.vishwam.ui.home.menu.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.State

class NotificationViewModel : ViewModel() {
    val state = MutableLiveData<State>()

}