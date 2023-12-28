package com.apollopharmacy.vishwam.ui.home.communityadvisor.servicescustomerinteraction

import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.GetServicesCustomerResponse
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServicesSaveDetailsResponse

interface ServicesCustomerCallback {
    fun onSuccessGetServicesCustomerResponse(getServicesCustomerResponse: GetServicesCustomerResponse)
    fun onFailureGetServicesCustomerResponse(message: String)

    fun onSuccessGetHomeServicesSaveDetailsResponse(homeServicesSaveDetailsResponse: HomeServicesSaveDetailsResponse)
    fun onFailureGetHomeServicesSaveDetailsResponse(message: String)
    fun onItemClick(listServices: GetServicesCustomerResponse.ListServices)
}