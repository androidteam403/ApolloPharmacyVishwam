package com.apollopharmacy.vishwam.ui.home.SwachhApollo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.State

class SwachhapolloModel : ViewModel()
{
    val state = MutableLiveData<State>()
}