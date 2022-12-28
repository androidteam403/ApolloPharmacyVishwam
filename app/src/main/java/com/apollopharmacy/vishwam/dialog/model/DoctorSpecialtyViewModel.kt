package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DoctorSpecialtyViewModel : ViewModel() {
    var fixedArrayList = MutableLiveData<ArrayList<String>>()
    var orginalArrayList = ArrayList<String>()

    fun specialtyArrayList(specialtyArrayList: ArrayList<String>): ArrayList<String> {
        fixedArrayList.value = specialtyArrayList
        orginalArrayList = specialtyArrayList
        return specialtyArrayList
    }

    fun filterSpecialty(specialty: String) {
        var data =
            orginalArrayList.filter { i ->
                i.contains(specialty) || i.contains(specialty.toUpperCase())
            } as ArrayList<String>
        fixedArrayList.value = data
    }
}