package com.apollopharmacy.vishwam.data.model.discount

data class FilterDiscountRequest(
    val EMPID: String,
    val FROMDATE: String,
    val TODATE: String,
    val TYPE: String,
)

