package com.apollopharmacy.vishwam.ui.home.apna.activity.model

class PopulationData {
    var apartments: List<Apartment>? = null

    class Apartment {
        var type: String? = null
        var apartmentName: String? = null
        var noOfHouses: String? = null
        var distance: String? = null
    }
}