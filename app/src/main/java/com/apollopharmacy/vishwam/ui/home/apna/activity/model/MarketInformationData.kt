package com.apollopharmacy.vishwam.ui.home.apna.activity.model

class MarketInformationData {
    var existingOutletName: String? = null
    var existingOutletAge: String? = null
    var pharma: String? = null
    var fmcg: String? = null
    var surgicals: String? = null
    var areaDiscount: String? = null
    var neighboringStoreList: List<NeighboringStore>? = null
    var comments: String? = null
    var occupation: String? = null
    var serviceClass: String? = null
    var businessClass: String? = null
    var trafficGenerators: List<String>? = null

    class NeighboringStore {
        var location: String? = null
        var store: String? = null
        var rent: String? = null
        var sale: String? = null
        var sqFt: String? = null
    }
}