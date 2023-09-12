package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QcItemListResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null
    var orderno: String? = null
    fun setorderno(order: String) {
        orderno = order
    }

    @SerializedName("ITEMLIST")
    @Expose
    var itemlist: List<Item>? = null
    fun withStatus(status: Boolean?): QcItemListResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): QcItemListResponse {
        this.message = message
        return this
    }

    fun withItemlist(itemlist: List<Item>?): QcItemListResponse {
        this.itemlist = itemlist
        return this
    }

    class Item :Serializable{
        @SerializedName("RECID")
        @Expose
        var recid: Int? = null

        @SerializedName("STOREID")
        @Expose
        var storeid: Any? = null

        @SerializedName("ORDERNO")
        @Expose
        var orderno: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: Any? = null

        @SerializedName("ITEMID")
        @Expose
        var itemid: String? = null

        @SerializedName("ITEMNAME")
        @Expose
        var itemname: String? = null

        @SerializedName("CATEGORY")
        @Expose
        var category: String? = null

        @SerializedName("QTY")
        @Expose
        var qty: Int? = 0

        @SerializedName("PRICE")
        @Expose
        var price: Double? = 0.0

        @SerializedName("DISCAMOUNT")
        @Expose
        var discamount: Double? = 0.0

        @SerializedName("CREATEDDATE")
        @Expose
        var createddate: String? = null

        @SerializedName("AUTOID")
        @Expose
        var autoid: Any? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: String? = null

        @SerializedName("MODIFIEDBY")
        @Expose
        var modifiedby: Any? = null

        @SerializedName("MODIFIEDDATE")
        @Expose
        var modifieddate: Any? = null

        @SerializedName("OMSRETURNLINERECID")
        @Expose
        var omsreturnlinerecid: Any? = null

        @SerializedName("PASSQTY")
        @Expose
        var passqty: Any? = null

        @SerializedName("FAILQTY")
        @Expose
        var failqty: Any? = null

        @SerializedName("APPROVEDQTY")
        @Expose
        var approvedqty: Int? = 0

        @SerializedName("DISCPERCENTAGE")
        @Expose
        var discpercentage: Any? = null

        @SerializedName("NETAMOUNT")
        @Expose
        var netamount: Any? = null

        @SerializedName("IMAGEURLS")
        @Expose
        var imageurls: Any? = null

        var reason: String? = null

        var isClick: Boolean = false

        fun setisClick(pos: Boolean) {
            isClick = pos
        }
        fun withRecid(recid: Int?): Item {
            this.recid = recid
            return this
        }

        fun withStoreid(storeid: Any?): Item {
            this.storeid = storeid
            return this
        }

        fun withOrderno(orderno: String?): Item {
            this.orderno = orderno
            return this
        }

        fun withStatus(status: Any?): Item {
            this.status = status
            return this
        }

        fun withItemid(itemid: String?): Item {
            this.itemid = itemid
            return this
        }

        fun withItemname(itemname: String?): Item {
            this.itemname = itemname
            return this
        }

        fun withCategory(category: String?): Item {
            this.category = category
            return this
        }

        fun withQty(qty: Int?): Item {
            this.qty = qty
            return this
        }

        fun withPrice(price: Double?): Item {
            this.price = price
            return this
        }

        fun withDiscamount(discamount: Double?): Item {
            this.discamount = discamount
            return this
        }

        fun withCreateddate(createddate: String?): Item {
            this.createddate = createddate
            return this
        }

        fun withAutoid(autoid: Any?): Item {
            this.autoid = autoid
            return this
        }

        fun withRemarks(remarks: String?): Item {
            this.remarks = remarks
            return this
        }

        fun withModifiedby(modifiedby: Any?): Item {
            this.modifiedby = modifiedby
            return this
        }

        fun withModifieddate(modifieddate: Any?): Item {
            this.modifieddate = modifieddate
            return this
        }

        fun withOmsreturnlinerecid(omsreturnlinerecid: Any?): Item {
            this.omsreturnlinerecid = omsreturnlinerecid
            return this
        }

        fun withPassqty(passqty: Any?): Item {
            this.passqty = passqty
            return this
        }

        fun withFailqty(failqty: Any?): Item {
            this.failqty = failqty
            return this
        }

        fun withApprovedqty(approvedqty: Int?): Item {
            this.approvedqty = approvedqty
            return this
        }

        fun withDiscpercentage(discpercentage: Any?): Item {
            this.discpercentage = discpercentage
            return this
        }

        fun withNetamount(netamount: Any?): Item {
            this.netamount = netamount
            return this
        }

        fun withImageurls(imageurls: Any?): Item {
            this.imageurls = imageurls
            return this
        }
    }
}