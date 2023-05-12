package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class ChangeManagerRequest(
    val forward_comment: String,
    val manager: ChangeManager,
    val old_manager: OldManager,
    val site: ChangeSite,
    val uid: String,
    val employee_id: String,
    val ticket: CCTicket
)

data class  CCTicket(
    val uid: String
)
data class ChangeManager(
    val uid: String
)

data class OldManager(
    val uid: String
)

data class ChangeSite(
    val site: String,
    val uid: String
)