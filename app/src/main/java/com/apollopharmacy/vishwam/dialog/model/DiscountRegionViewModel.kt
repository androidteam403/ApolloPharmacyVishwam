package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.util.Utils

class DiscountRegionViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<PendingOrder.PENDINGLISTItem>>()
    var orginalArrayList = ArrayList<PendingOrder.PENDINGLISTItem>()

    fun siteArrayList(siteArrayList: ArrayList<PendingOrder.PENDINGLISTItem>): ArrayList<PendingOrder.PENDINGLISTItem> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.DCCODE!!.contains(siteId) || m.DCNAME!!.contains(siteId.toUpperCase())
            } as ArrayList<PendingOrder.PENDINGLISTItem>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}