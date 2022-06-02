package com.apollopharmacy.vishwam.ui.home.menu.notificationwebview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.State

class NotificationWebViewModel : ViewModel() {
    val state = MutableLiveData<State>()

}