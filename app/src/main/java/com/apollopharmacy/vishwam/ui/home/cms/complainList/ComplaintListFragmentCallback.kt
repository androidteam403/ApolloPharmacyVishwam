package com.apollopharmacy.vishwam.ui.home.cms.complainList

import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubworkflowConfigDetailsResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketData
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketSubworkflowActionUpdateResponse
import java.util.ArrayList

interface ComplaintListFragmentCallback {

    fun onSuccessSubWorkflowAcceptApiCall()
    fun onSuccessSubWorkflowRejectApiCall()

    fun onSuccessSubworkflowConfigDetailsApi(
        subworkflowConfigDetailsResponse: SubworkflowConfigDetailsResponse,
        position: Int,
    )

    fun onFailureSubworkflowConfigDetailsApi(message: String)

    fun onSuccessActionUpdate(
        ticketSubworkflowActionUpdateResponse: TicketSubworkflowActionUpdateResponse,
        row: SubworkflowConfigDetailsResponse.Rows,
        remark: String, data: TicketData, responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    )
}