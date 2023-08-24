package com.apollopharmacy.vishwam.ui.home.qcfail.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.util.Utils

class QcRegionViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<QcRegionList.Store>>()
    var orginalArrayList = ArrayList<QcRegionList.Store>()

    fun qcRegionArrayList(siteArrayList: ArrayList<QcRegionList.Store>): ArrayList<QcRegionList.Store> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: $orginalArrayList")
        var data =
            orginalArrayList.filter { m ->
                m.siteid!!.contains(siteId) || m.siteid!!.contains(siteId.toUpperCase())
            } as ArrayList<QcRegionList.Store>
        Utils.printMessage(TAG, "Filter Data :: $data")
        fixedArrayList.value = data
    }
}