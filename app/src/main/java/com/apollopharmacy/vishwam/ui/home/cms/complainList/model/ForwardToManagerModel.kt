package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class ForwardToManagerModel(
    val forward_comment: String,
    val manager: Manager,
    val ticket: FMTicket,
    val uid: String,
    val employee_id: String
):java.io.Serializable

data class Manager(
    val uid: String
):java.io.Serializable

data class FMTicket(
    val uid: String
):java.io.Serializable