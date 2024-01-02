package com.apollopharmacy.vishwam.ui.home.communityadvisor

import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServiceDetailsResponse

interface CommunityAdvisorFragmentCallback {

    fun onClickServicesTab()

    fun onClickCustomerInteractionTab()

    fun onClickServicesItems(serviceItem: HomeServiceDetailsResponse.Detlist)

    fun onSuccessHomeServiceDetails(homeServiceDetailsResponse: HomeServiceDetailsResponse)
    fun onFailureHomeServiceDetails(message: String)

    fun noListFound(count: Int)

}