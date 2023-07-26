package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity

import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model.ReasonWiseTicketCountbyRoleResponse

interface DashboardDetailsCallback {
    fun onSuccessGetReasonWiseTicketCountByRoleApiCall(reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountbyRoleResponse)
    fun onFailureGetReasonWiseTicketCountByRoleApiCall(reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountbyRoleResponse)
    fun onClickCategoryItem(
        data1: ReasonWiseTicketCountbyRoleResponse.Data1,
        pos: Int,
        isCameFromFixed: Boolean,
    )

    fun onClickBack()
}