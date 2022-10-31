package com.apollopharmacy.vishwam.data.model.cms

data class InventoryDetailsModel(
    val `data`: Data,
    val message: Any,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String,
    var position: Int
)

data class Data(
    val category: Category,
    val cluster: Cluster,
    val created_id: CreatedId,
    val date_of_occurrence: String,
    val department: Department,
    val ou: Ou,
    val platform: Platform,
    val reason: Reason,
    val region: Region,
    val site: Site,
    val status: Status,
    val subcategory: Subcategory,
    val ticket_id: String,
    val ticket_inventory: TicketInventory,
    val ticket_type: TicketType,
    val uid: String
)

data class Category(
    val code: String,
    val name: String,
    val uid: String
)

data class Cluster(
    val code: String,
    val name: String,
    val uid: String
)

data class CreatedId(
    val email: Any,
    val first_name: String,
    val last_name: Any,
    val login_unique: String,
    val middle_name: Any,
    val phone: Any,
    val uid: String
)

data class Department(
    val allocation_type: AllocationType,
    val code: String,
    val name: String,
    val uid: String
)

data class Ou(
    val code: Any,
    val name: Any,
    val organization: Organization,
    val state: State,
    val uid: Any
)

data class Platform(
    val icon: Any,
    val name: String,
    val other: Other,
    val uid: String
)

data class Reason(
    val code: String,
    val name: String,
    val uid: String
)

data class Region(
    val code: String,
    val name: String,
    val uid: String
)

data class Site(
    val hod: Hod,
    val ou: OuX,
    val phone_no: String,
    val region: RegionX,
    val site: String,
    val site_type: SiteType,
    val store_name: String,
    val uid: String,
    val manager: Manager
)

data class Status(
    val background_color: String,
    val code: String,
    val name: String,
    val text_color: String,
    val uid: String
)

data class Subcategory(
    val code: String,
    val name: String,
    val uid: String
)

data class TicketInventory(
    val code_batch: CodeBatch,
    val ticket_inventory_item: List<TicketInventoryItem>,
    val uid: String
)

data class TicketType(
    val icon: Any,
    val name: Any,
    val other: Other,
    val uid: Any
)

data class AllocationType(
    val icon: Any,
    val name: String,
    val other: Other,
    val uid: String
)

data class Other(
    val color: Any
)

data class Organization(
    val code: Any,
    val uid: Any
)

data class State(
    val name: Any,
    val uid: Any
)

data class Hod(
    val first_name: String,
    val uid: String
)

data class OuX(
    val code: String,
    val name: String,
    val organization: OrganizationX,
    val state: StateX,
    val uid: String
)

data class RegionX(
    val name: String,
    val uid: String
)

data class SiteType(
    val icon: Any,
    val name: String,
    val other: Other,
    val uid: String
)

data class OrganizationX(
    val code: String,
    val uid: String
)

data class StateX(
    val name: String,
    val uid: String
)

data class CodeBatch(
    val icon: Any,
    val name: String,
    val other: Other,
    val uid: String
)

data class TicketInventoryItem(
    val acted_by: ActedBy,
    val acted_on: Any,
    val back_image: List<Any>,
    val back_img_blob: String,
    val barcode: String,
    val batch_no: String,
    val expiry_date: String,
    val front_image: List<Any>,
    val front_img_blob: String,
    val item_code: String,
    val item_status: ItemStatus,
    val manager: Manager,
    val mfg_date: Any,
    val mrp: String,
    val new_mrp: Double,
    val old_mrp: Double,
    val other_image: List<Any>,
    val other_img_blob: String ,
    val purchase_rate: Double,
    val uid: String
)

data class ActedBy(
    val first_name: Any,
    val uid: Any
)

data class ItemStatus(
    val icon: Any,
    val name: Any,
    val other: Other,
    val uid: Any
)

data class Manager(
    val first_name: String,
    val uid: String
)