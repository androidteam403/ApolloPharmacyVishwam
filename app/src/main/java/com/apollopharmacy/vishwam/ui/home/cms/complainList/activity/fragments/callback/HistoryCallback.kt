package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.callback

import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.ComplaintsListDetailsCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.*

interface HistoryCallback {

    fun onSuccessSubWorkflowAcceptApiCall()

    fun onSuccessSubWorkflowRejectApiCall()

    fun onSuccessUsersListforSubworkflow(
        ticketData: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
        request: UserListForSubworkflowResponse?,
        historyCallback: HistoryCallback
    )

    fun onSuccessActionUpdate(
        request: TicketSubworkflowActionUpdateResponse?,
        row: SubworkflowConfigDetailsResponse.Rows,
        remark: String,
        data1: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback
    )

    fun onSuccessSubworkflowConfigDetailsApi(responseSubworkflowConfigDetailsResponse: SubworkflowConfigDetailsResponse, position: Int)

    fun onFailureSubworkflowConfigDetailsApi(message: String)

    fun onClickTicketResolveHis(orderDataWp: ResponseNewTicketlist.Row, orderData: ArrayList<ResponseNewTicketlist.Row>, position: Int, historyCallback: HistoryCallback)

    fun onClickCCAccept(data: TicketData, orderData: ArrayList<ResponseNewTicketlist.Row>, position: Int, historyCallback: HistoryCallback)

    fun onClickCCReject(data: TicketData, orderData: ArrayList<ResponseNewTicketlist.Row>, position: Int, historyCallback: HistoryCallback)

    fun onClickForwardToFinance(cmsTicketRequest: CmsTicketRequest, orderData: ArrayList<ResponseNewTicketlist.Row>, position: Int, historyCallback: HistoryCallback)

    fun onClickInventoryAccept(orderDataWp: ResponseNewTicketlist.Row, position: Int, orderData: ArrayList<ResponseNewTicketlist.Row>, historyCallback: HistoryCallback)

    fun onClickInventoryReject(orderDataWp: ResponseNewTicketlist.Row, position: Int, orderData: ArrayList<ResponseNewTicketlist.Row>, historyCallback: HistoryCallback)

    fun onClickForwardToManager(orderDataWp: ResponseNewTicketlist.Row, historyCallback: HistoryCallback)

    fun onClickForwardChangeManager(orderDataWp: ResponseNewTicketlist.Row, position: Int, orderData: ArrayList<ResponseNewTicketlist.Row>, historyCallback: HistoryCallback)

    fun onClickTicketClose(orderDataWp: ResponseNewTicketlist.Row, orderData: ArrayList<ResponseNewTicketlist.Row>, position: Int, historyCallback: HistoryCallback)

    fun onClickTicketReopen(orderDataWp: ResponseNewTicketlist.Row, orderData: ArrayList<ResponseNewTicketlist.Row>, position: Int, historyCallback: HistoryCallback)

    fun onClickAction(data: TicketData, responseList: ArrayList<ResponseNewTicketlist.Row>, positionHeader: Int, row: SubworkflowConfigDetailsResponse.Rows, historyCallback: HistoryCallback)

    fun onSuccessBack(complaintsListDetailsCallback: ComplaintsListDetailsCallback)


//    fun onClickTicketResolveHistory()


}