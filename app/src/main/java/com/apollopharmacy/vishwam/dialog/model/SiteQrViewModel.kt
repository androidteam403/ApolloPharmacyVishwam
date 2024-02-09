package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.util.Utils

class SiteQrViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<StoreDetailsModelResponse.Row>>()
    var orginalArrayList = ArrayList<StoreDetailsModelResponse.Row>()

    fun siteArrayListQr(siteArrayList: ArrayList<StoreDetailsModelResponse.Row>): ArrayList<StoreDetailsModelResponse.Row> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.site!!.contains(siteId) || m.site!!.contains(siteId.toUpperCase())
            } as ArrayList<StoreDetailsModelResponse.Row>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}