package com.apollopharmacy.vishwam.ui.home.qcfail.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.util.Utils

class QcFailSiteViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<QcStoreList.Store>>()
    var orginalArrayList = ArrayList<QcStoreList.Store>()

    fun qcSiteArrayList(siteArrayList: ArrayList<QcStoreList.Store>): ArrayList<QcStoreList.Store> {
        orginalArrayList = siteArrayList
        fixedArrayList.value = siteArrayList

        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.siteid!!.contains(siteId) || m.siteid!!.contains(siteId.toUpperCase())
            } as ArrayList<QcStoreList.Store>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data


    }
}