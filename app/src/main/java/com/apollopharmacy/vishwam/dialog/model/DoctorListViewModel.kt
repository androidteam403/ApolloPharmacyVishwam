package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.DoctorListResponse
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.SiteListResponse
import com.apollopharmacy.vishwam.util.Utils

class DoctorListViewModel : ViewModel() {
    val TAG = "SiteViewModel"
    var fixedArrayList = MutableLiveData<ArrayList<DoctorListResponse.Doctor>>()
    var orginalArrayList = ArrayList<DoctorListResponse.Doctor>()

    fun siteArrayList(siteArrayList: ArrayList<DoctorListResponse.Doctor>): ArrayList<DoctorListResponse.Doctor> {
        fixedArrayList.value = siteArrayList
        orginalArrayList = siteArrayList
        return siteArrayList
    }

    fun filterDataBySiteId(siteId: String) {
        Utils.printMessage(TAG, "Orginal Data :: " + orginalArrayList.toString())
        var data =
            orginalArrayList.filter { m ->
                m.doctorname!!.contains(siteId) || m.doctorname!!.contains(siteId.toUpperCase())
            } as ArrayList<DoctorListResponse.Doctor>
        Utils.printMessage(TAG, "Filter Data :: " + data.toString())
        fixedArrayList.value = data
    }
}