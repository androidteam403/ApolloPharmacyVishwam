package com.apollopharmacy.vishwam.ui.home.qcfail.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.util.Utils

class QcRegionViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<UniqueRegionList>>()
    var orginalArrayList = ArrayList<UniqueRegionList>()

    fun qcRegionArrayList(siteArrayList: ArrayList<UniqueRegionList>): ArrayList<UniqueRegionList> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.siteid!!.contains(siteId) || m.siteid!!.contains(siteId.toUpperCase())
            } as ArrayList<UniqueRegionList>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}