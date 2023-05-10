package com.apollopharmacy.vishwam.ui.home.apna.activity.model

class CompetitorsDetailsData {
    var chemists: List<Chemist>? = null

    class Chemist {
        var chemist: String? = null
        var organised: String? = null
        var organisedAvgSale: String? = null
        var unorganised: String? = null
        var unOrganisedAvgSale: String? = null
    }
}