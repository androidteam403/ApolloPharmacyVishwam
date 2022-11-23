package com.apollopharmacy.vishwam.ui.home.qcfail.model

interface QcListsCallback {
    fun orderno(position: Int, orderno: String)
    fun notify(position: Int, orderno: String)
    fun accept(
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
        omsOrderno:String,
    )

    fun reject(
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
        omsOrderno:String,

        )

    fun imageData(position: Int, orderno: String, itemName: String, imageUrl: String)

    fun isChecked(
        array: List<QcListsResponse.Pending>,
        position: Int,
        pending: QcListsResponse.Pending,
    )
}