package com.apollopharmacy.vishwam.ui.home.greeting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.GreetingActivityRepo
import com.apollopharmacy.vishwam.ui.home.greeting.model.EmployeeWishesRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GreetingViewModel : ViewModel() {

    fun employeeWishesApiCall(
        employeeWishesRequest: EmployeeWishesRequest,
        greetingActivityCallback: GreetingActivityCallback
    ) {
        val state = MutableLiveData<State>()

        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                GreetingActivityRepo.employeeWishesApiCAll(
                    "http://online.apollopharmacy.org:51/ADAPP/EMP.SVC/EmployeeWishes",
                    employeeWishesRequest
                )
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response.value != null) {
                        greetingActivityCallback.onSuccessEmployeeWishesApiCAll(response.value)
                    } else {
                    }
                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
    }
}