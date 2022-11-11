package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class InventoryAcceptrejectModel(
    val reject_comment: String?,
    val comment: String?,
    val item_status: ItemStatus,
    val site: String,
    val uid: String,
    val employee_id: String,
    val status: String,
    val ticket_id: String,
    val action: String?,
)

data class ItemStatus(
    val uid: String
)

data class Ticket(
    val uid: String
)