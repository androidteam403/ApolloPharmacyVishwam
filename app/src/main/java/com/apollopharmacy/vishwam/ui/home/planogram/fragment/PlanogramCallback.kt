package com.apollopharmacy.vishwam.ui.home.planogram.fragment

import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.ListBySiteIdResponse

interface PlanogramCallback {

    fun onClickContinue()

    fun onClickSiteId()
    fun onSuccessPlanogramSiteIdList(siteIdResponse: ListBySiteIdResponse?)
    fun onFailurePlanogramSiteIdList(message: Any)
}