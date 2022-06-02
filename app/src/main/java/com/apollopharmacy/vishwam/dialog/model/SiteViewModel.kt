package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.util.Utils

class SiteViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<StoreListItem>>()
    var orginalArrayList = ArrayList<StoreListItem>()

    fun siteArrayList(siteArrayList: ArrayList<StoreListItem>): ArrayList<StoreListItem> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.sITEID!!.contains(siteId) || m.sITENAME!!.contains(siteId.toUpperCase())
            } as ArrayList<StoreListItem>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}