package com.apollopharmacy.vishwam.ui.home.apna.activity.model

class HospitalsData {
    var hospitals: List<Hospital>? = null

    class Hospital {
        var speciality: String? = null
        var beds: String? = null
        var name: String? = null
        var occupancy: String? = null
        var noOfOpd: String? = null
    }
}