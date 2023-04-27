package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response

data class DrugReason(
    val `data`: Data,
    val message: String,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String
)

data class Data(
    val code: String,
    val department: Department,
    val name: String,
    val reason_sla: ArrayList<ReasonmasterV2Response.Reason_SLA>?,
    val ticket_category: TicketCategory,
    val ticket_sub_category: TicketSubCategory,
    val uid: String
)

data class Department(
    val code: String,
    val name: String,
    val uid: String
)



data class TicketCategory(
    val code: String,
    val name: String,
    val uid: String
)

data class TicketSubCategory(
    val name: String,
    val uid: String
)

data class BbhTatMins(
    val icon: String,
    val name: String,
    val other: Other,
    val uid: String
)

data class BhTatMins(
    val icon: String,
    val name: String,
    val other: Other,
    val uid: String
)

data class DefaultTatMins(
    val icon: String,
    val name: String,
    val other: Other,
    val uid: String
)

data class EsacltnNeeded(
    val icon: String,
    val name: String,
    val other: Other,
    val uid: String
)

data class Priority(
    val icon: String,
    val name: String,
    val other: Other,
    val uid: String
)

data class SlaStatus(
    val icon: String,
    val name: String,
    val other: Other,
    val uid: String
)

data class Other(
    val color: String
)