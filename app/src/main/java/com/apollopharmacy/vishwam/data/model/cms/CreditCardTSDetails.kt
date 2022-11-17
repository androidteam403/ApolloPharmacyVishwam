package com.apollopharmacy.vishwam.data.model.cms

data class CreditCardTSDetails(
    val `data`: CCData,
    val message: Any,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String,
    var position: Int
)

data class CCData(
    val category: CCCategory,
    val cluster: CCCluster,
    val created_id: CCCreatedId,
    val created_time: String,
    val date_of_occurrence: String,
    val department: CCDepartment,
    val executive: CCExecutive,
    val level: CCLevel,
    val ou: CCOu,
    val platform: CCPlatform,
    val reason: CCReason,
    val region: CCRegion,
    val site: CCSite,
    val site_phone_no: String,
    val status: CCStatus,
    val store_explanation: Any,
    val subcategory: CCSubcategory,
    val ticket_id: String,
    val ticket_it: CCTicketIt,
    val ticket_type: CCTicketTypeX,
    val uid: String
)

data class CCCategory(
    val code: String,
    val name: String,
    val uid: String
)

data class CCCluster(
    val code: String,
    val name: String,
    val uid: String
)

data class CCCreatedId(
    val email: String,
    val first_name: String,
    val last_name: Any,
    val login_unique: String,
    val middle_name: String,
    val phone: Any,
    val uid: String
)

data class CCDepartment(
    val code: String,
    val name: String,
    val uid: String
)

data class CCExecutive(
    val email: String,
    val first_name: String,
    val last_name: Any,
    val login_unique: String,
    val phone: String,
    val uid: String
)

data class CCLevel(
    val code: String,
    val name: String,
    val uid: String
)

data class CCOu(
    val code: String,
    val name: String,
    val uid: String
)

data class CCPlatform(
    val icon: Any,
    val name: String,
    val other: Other,
    val uid: String
)

data class CCReason(
    val code: String,
    val name: String,
    val uid: String,
    val sub_workflow: SubWorkflow,
    val reason_dept: List<ReasonDept>
)

data class ReasonDept(
    val uid: String,
    val department: Department
)

data class SubWorkflow(
    val code: String,
    val name: String,
    val uid: String,
)

data class CCRegion(
    val code: String,
    val name: String,
    val uid: String
)

data class CCSite(
    val phone_no: String,
    val site: String,
    val site_type: CCSiteType,
    val store_name: String,
    val uid: String
)

data class CCStatus(
    val background_color: String,
    val code: String,
    val name: String,
    val text_color: String,
    val uid: String
)

data class CCSubcategory(
    val code: String,
    val name: String,
    val uid: String
)

data class CCTicketIt(
    val acted_by: CCActedBy,
    val acted_on: Any,
    val approval_code: String,
    val asset_tag_id: Any,
    val bill_amount: Int,
    val bill_number: String,
    val branch_mail_id: Any,
    val status: CCStatusX,
    val ticket_type: CCTicketType,
    val tid: CCTid,
    val transaction_id: String,
    val uid: String
)

data class CCTicketTypeX(
    val code: String,
    val name: String,
    val uid: String
)

data class CCOther(
    val color: Any
)

data class CCSiteType(
    val icon: Any,
    val name: String,
    val other: Other,
    val uid: String
)

data class CCActedBy(
    val email: String,
    val first_name: String,
    val last_name: String,
    val login_unique: String,
    val middle_name: String,
    val phone: String,
    val uid: String
)

data class CCStatusX(
    val icon: Any,
    val name: Any,
    val other: CCOther,
    val uid: String
)

data class CCTicketType(
    val icon: Any,
    val name: Any,
    val other: CCOther,
    val uid: Any
)

data class CCTid(
    val tid: String,
    val uid: String
)