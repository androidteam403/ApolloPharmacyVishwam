package com.apollopharmacy.vishwam.ui.home.cashcloser.model

class CashCloserList {
    var siteId: String? = null
    var date: String? = null
    var status: String? = null
    var isExpanded: Boolean = false
    var isUploaded: Boolean = false
    var amount: String? = null
    var imageList: List<ImageData>? = null

    constructor(
        siteId: String?,
        date: String?,
        status: String?,
        imageList: List<ImageData>?,
        amount: String?,
    ) {
        this.siteId = siteId
        this.date = date
        this.status = status
        this.imageList = imageList
        this.amount = amount
    }

    fun setIsExpanded(expanded: Boolean) {
        isExpanded = expanded
    }

    fun setIsUploaded(uploaded: Boolean) {
        isUploaded = uploaded
    }




    fun setSiteid(siteId: String?): CashCloserList {
        this.siteId = siteId
        return this
    }

    fun setDate(date: String?): CashCloserList {
        this.date = date
        return this
    }

    fun setStatus(status: String?): CashCloserList {
        this.status = status
        return this
    }
}