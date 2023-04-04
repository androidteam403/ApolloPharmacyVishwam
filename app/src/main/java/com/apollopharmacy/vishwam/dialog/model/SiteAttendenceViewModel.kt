package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.SiteListResponse
import com.apollopharmacy.vishwam.util.Utils

class SiteAttendenceViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<SiteListResponse.Site>>()
    var orginalArrayList = ArrayList<SiteListResponse.Site>()

    fun siteArrayList(siteArrayList: ArrayList<SiteListResponse.Site>): ArrayList<SiteListResponse.Site> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.siteid!!.contains(siteId) || m.sitename!!.contains(siteId.toUpperCase())
            } as ArrayList<SiteListResponse.Site>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}