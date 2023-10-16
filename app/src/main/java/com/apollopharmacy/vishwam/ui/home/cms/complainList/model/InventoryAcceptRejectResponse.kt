package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class InventoryAcceptRejectResponse(
    val `data`: AcceptRejectData,
    val message: String,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String
):java.io.Serializable

data class AcceptRejectData(
    val isExecSuccess: Boolean,
    val isUpdate: Boolean,
    val res: String,
    val uid: String,
    val errors: ArrayList<Errors>
):java.io.Serializable

data class Errors(
    val msg: String
):java.io.Serializable