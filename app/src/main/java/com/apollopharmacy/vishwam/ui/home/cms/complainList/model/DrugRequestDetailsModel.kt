package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import java.io.Serializable

data class DrugRequestDetailsModel(
    val back_mb: String,
    val barcode: String,
    val batch_no: String,
    val bill: List<Any>,
    val bill_mb: String,
    val expiry_date: String,
    val front: List<Any>,
    val front_mb: String,
    val gst: String,
    val hsn_code: String,
    val item_name: String,
    val manufacturing_date: String,
    val mrp: Double,
    val pack_size: Int,
    val purchase_price: Double,
    val reference_no: String,
    val remarks: String,
    val side: List<Any>,
    val side_mb: String,
    val doctor_specialty: DoctoSpecialty,
    val doctors_name: String,
    val item_type: ItemType,
    val required_quantity: Int,
    val uid: String,
) : Serializable {
    data class DoctoSpecialty(
        val uid: String,
        val name: String,
    ) : Serializable

    data class ItemType(
        val uid: String,
        val name: String,
    ) : Serializable
}




