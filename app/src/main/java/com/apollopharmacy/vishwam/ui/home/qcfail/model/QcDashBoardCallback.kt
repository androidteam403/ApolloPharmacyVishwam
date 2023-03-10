package com.apollopharmacy.vishwam.ui.home.qcfail.model

interface QcDashBoardCallback {
    fun onClick(position: Int, designation: String,empId:String,click: Boolean)
    fun notify(position: Int,click: Boolean)
    fun onClickManagerDashBoard(position: Int, designation: String,empId:String)
    fun onClickManagerHierarchy(position: Int, designation: String,empId:String)

    fun onClickExecutive(position: Int, designation: String,empId:String)

}