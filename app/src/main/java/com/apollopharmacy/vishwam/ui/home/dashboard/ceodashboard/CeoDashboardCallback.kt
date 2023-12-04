package com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard

import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse

interface CeoDashboardCallback {

    fun onClickRightArrow(row: TicketCountsByStatusRoleResponse.Data.ListData.Row)
    fun onClickEmployee(employee:String,roleCode:String)

    fun onSuccessgetTicketListByCountApi(value: TicketCountsByStatusRoleResponse)

    fun onFailuregetTicketListByCountApi(value: TicketCountsByStatusRoleResponse)

    fun onClickStores()

    fun onClickClosed()

    fun onClickLesssThanTwo()

    fun onClickThreetoEight()

    fun onClickGreaterThanEight()

    fun onClickRejected()

    fun onClickPending()

    fun onClickTotal()

    fun onClickFromDate()

    fun onClickToDate()

    fun onClickApplyDate()

    fun onCLickRowtoShowTickets(row: TicketCountsByStatusRoleResponse.Data.ListData.Row)

}