package com.apollopharmacy.vishwam.ui.home.cashcloser.model

class CashCloserList {
    var siteId: String? = null
    var date: String? = null
    var status: String? = null
    var isExpanded: Boolean = false
    var imageList: List<ImageData>? = null

    constructor(
        siteId: String?,
        date: String?,
        status: String?,
        imageList: List<ImageData>?,
    ) {
        this.siteId = siteId
        this.date = date
        this.status = status
        this.imageList = imageList
    }

    fun setIsExpanded(expanded: Boolean) {
        isExpanded = expanded
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