package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class WorkFlowUpdateModel(
    val action: Action,
    val action_name: String,
    val department: Department,
    val level: Level,
    val next_level: NextLevel,
    val session_user: SessionUser?,
    val site: Site,
    val sla_time: String,
    val status: Status,
    val ticket_id: String?,
    val ticket_owner: TicketOwner,
    var uid: String,
    val user: User,
    val visibility_to_site_user: String,
    val acted_by: String

)

data class Action(
    val code: String?,
    val uid: String?
)

data class Department(
    val code: String?,
    val uid: String?
)

data class Level(
    val code: String?,
    val uid: String?
)

data class NextLevel(
    val uid: String?
)

data class SessionUser(
    val login_unique: String?,
    val name: String?,
    val uid: String?
)

data class Site(
    val uid: String?
)

data class Status(
    val code: String?,
    val uid: String?
)

data class TicketOwner(
    val uid: String?
)

data class User(
    val name: String?,
    val uid: String?
)