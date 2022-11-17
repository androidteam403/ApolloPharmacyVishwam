package com.apollopharmacy.vishwam.dialog.model

data class TransactionPOSModel(
    val `data`: Data,
    val message: String,
    val success: Boolean,
    val zcServerDateTime: String,
    val zcServerHost: String,
    val zcServerIp: String
)

data class Data(
    val listData: ListData
)

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
)

data class Row(
    val site: Site,
    val tid: String,
    val uid: String
)

data class Site(
    val site: String,
    val uid: String
)