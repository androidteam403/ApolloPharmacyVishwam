package com.apollopharmacy.vishwam.ui.home.cashcloser.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File

class CashDepositDetailsResponse {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("CASHDEPOSIT")
    @Expose
    var cashdeposit: List<Cashdeposit>? = null

    class Cashdeposit {
        @SerializedName("CLOSINGDATE")
        @Expose
        var closingdate: String? = null

        @SerializedName("SITEID")
        @Expose
        var siteid: String? = null

        @SerializedName("IMAGEURL")
        @Expose
        var imageurl: String? = null

        @SerializedName("AMOUNT")
        @Expose
        var amount: String? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: String? = null

        @SerializedName("DCID")
        @Expose
        var dcid: String? = null

        var isExpanded: Boolean = false

        var imagePath: File? = null

        var imagePathTwo: File? = null

        var isUploaded: Boolean? = false

        var amountEdit: Int? = 0

        fun setIsExpanded(expanded: Boolean) {
            isExpanded = expanded
        }

        fun setImageUrl(imageurl: String) {
            this.imageurl = imageurl
        }

        var isClicked: Boolean? = false

        fun setIsClicked(clicked: Boolean) {
            isClicked = clicked
        }

    }
}