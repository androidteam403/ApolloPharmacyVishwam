package com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter

import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcStoreList

interface QcFilterSiteCallBack {

    fun getSiteIdList(storelist: List<QcStoreList.Store>?)
}