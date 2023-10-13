package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class ChangeManagerResponse(
    val `data`: ChangeManagerData,
    val message: String,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String
):java.io.Serializable

data class ChangeManagerData(
    val isExecSuccess: Boolean,
    val isUpdate: Boolean,
    val uid: String
):java.io.Serializable