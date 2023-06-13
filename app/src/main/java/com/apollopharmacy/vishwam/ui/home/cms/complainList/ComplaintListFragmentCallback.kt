package com.apollopharmacy.vishwam.ui.home.cms.complainList

import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubworkflowConfigDetailsResponse

interface ComplaintListFragmentCallback {

    fun onSuccessSubWorkflowAcceptApiCall()
    fun onSuccessSubWorkflowRejectApiCall()

    fun onSuccessSubworkflowConfigDetailsApi(
        subworkflowConfigDetailsResponse: SubworkflowConfigDetailsResponse,
        position: Int,
    )

    fun onFailureSubworkflowConfigDetailsApi(message: String)
}