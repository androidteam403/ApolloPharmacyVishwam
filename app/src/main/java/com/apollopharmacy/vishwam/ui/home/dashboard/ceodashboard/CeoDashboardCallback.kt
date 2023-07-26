package com.apollopharmacy.vishwam.ui.home.dashboard.ceodashboard

import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse

interface CeoDashboardCallback {

    fun onClickRightArrow(row: TicketCountsByStatusRoleResponse.Data.ListData.Row)

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

}