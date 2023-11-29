package com.apollopharmacy.vishwam.ui.home.help

import com.apollopharmacy.vishwam.ui.home.help.model.HelpResponseModel

interface HelpActivityCallback {
    fun onclickBackArrow()
    fun noModuleFound(size: Int)
    fun onSuccessHelpDetails(storeWiseDetailListResponse: HelpResponseModel?)
    fun onFailureHelpDetails(storeWiseDetailListResponse: HelpResponseModel?)
}