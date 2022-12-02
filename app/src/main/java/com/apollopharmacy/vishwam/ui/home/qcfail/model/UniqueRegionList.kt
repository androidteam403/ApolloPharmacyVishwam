package com.apollopharmacy.vishwam.ui.home.qcfail.model

class UniqueRegionList {
    var siteid: String? = null

    fun getSiteId(): String? {
        return siteid
    }

    fun setSiteId(siteId: String?) {
        this.siteid = siteId
    }    var isClick: Boolean =false

    fun setisClick(pos: Boolean) {
        isClick = pos
    }
}