package com.apollopharmacy.vishwam.ui.home.greeting

import com.apollopharmacy.vishwam.ui.home.greeting.model.EmployeeWishesResponse

interface GreetingActivityCallback {
    fun onClickYourSignatureHere()
    fun onClickSend()
    fun onSuccessEmployeeWishesApiCAll(employeeWishesResponse: EmployeeWishesResponse)
}