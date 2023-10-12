package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class ChangeManagerRequest(
    val forward_comment: String,
    val manager: ChangeManager,
    val old_manager: OldManager,
    val site: ChangeSite,
    val uid: String,
    val employee_id: String,
    val ticket: CCTicket
):java.io.Serializable

data class  CCTicket(
    val uid: String
):java.io.Serializable
data class ChangeManager(
    val uid: String
):java.io.Serializable

data class OldManager(
    val uid: String
):java.io.Serializable

data class ChangeSite(
    val site: String,
    val uid: String
):java.io.Serializable