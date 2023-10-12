package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

data class GetManagersModel(
    val `data`: Data,
    val message: Any,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String
):java.io.Serializable

data class Data(
    val listData: ListData
):java.io.Serializable

data class ListData(
    val aggregation: Any,
    val page: Int,
    val pivotData: Any,
    val records: String,
    val rows: List<Row>,
    val select: Boolean,
    val size: Int,
    val total: Int,
    val zc_extra: Any
):java.io.Serializable

data class Row(
    val email: String,
    val first_name: String,
    val last_name: String,
    val login_unique: String,
    val middle_name: String,
    val role: Role,
//    val status: Status,
    val uid: String
):java.io.Serializable

data class Role(
    val name: String,
    val uid: String
) :java.io.Serializable

//data class Status(
//    val icon: Any,
//    val name: String,
//    val other: Other,
//    val uid: String
//)

data class Other(
    val color: Any
):java.io.Serializable