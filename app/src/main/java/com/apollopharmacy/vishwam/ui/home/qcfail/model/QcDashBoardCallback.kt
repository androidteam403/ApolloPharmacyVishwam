package com.apollopharmacy.vishwam.ui.home.qcfail.model

import java.util.ArrayList

interface QcDashBoardCallback {
    fun onClick(position: Int, designation: String,empId:String,click: Boolean)
    fun notify(position: Int,click: Boolean)
    fun onClickManagerDashBoard(position: Int, designation: String,empId:String)
    fun onClickManagerHierarchy(position: Int, hierarchyList:ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)
    fun onClickExecutiveHierarchy(position: Int, hierarchyList:ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)

    fun onClickExecutive(position: Int, designation: String,empId:String)
    fun onSuccessDashboardHierarchyResponse(value: Getqcfailpendinghistoryforhierarchy)
    fun onSuccessDashboardHistoryResponse(value:Getqcfailpendinghistorydashboard )

    fun onSearchClick(position: Int,list:ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>)
}