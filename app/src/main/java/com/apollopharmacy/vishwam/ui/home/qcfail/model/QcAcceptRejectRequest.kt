package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable



class QcAcceptRejectRequest:Serializable {
    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("REMARKS")
    @Expose
    var remarks: String? = null

    @SerializedName("REMARKSCODE")
    @Expose
    var remarkscode: String? = null

    @SerializedName("orders")
    @Expose
    var orders: List<Order>? = null

    constructor(type: String?, remarks: String?, remarkscode: String?, orders: List<Order>?) {
        this.type = type
        this.remarks = remarks
        this.remarkscode = remarkscode
        this.orders = orders
    }

    fun withType(type: String?): QcAcceptRejectRequest {
        this.type = type
        return this
    }

    fun withRemarks(remarks: String?): QcAcceptRejectRequest {
        this.remarks = remarks
        return this
    }

    fun withRemarkscode(remarkscode: String?): QcAcceptRejectRequest {
        this.remarkscode = remarkscode
        return this
    }

    fun withOrders(orders: List<Order>?): QcAcceptRejectRequest {
        this.orders = orders
        return this
    }

     class Item {
        @SerializedName("QTY")
        @Expose
        var qty: Int? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: String? = null

        @SerializedName("ITEMID")
        @Expose
        var itemid: String? = null
        fun withQty(qty: Int?): Item {
            this.qty = qty
            return this
        }

        fun withRemarks(remarks: String?): Item {
            this.remarks = remarks
            return this
        }

        fun withItemid(itemid: String?): Item {
            this.itemid = itemid
            return this
        }
    }

     class Order {
        @SerializedName("ORDERNO")
        @Expose
        var orderno: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null

        @SerializedName("DESIGNATION")
        @Expose
        var designation: String? = null

        @SerializedName("EMPID")
        @Expose
        var empid: String? = null

        @SerializedName("STOREID")
        @Expose
        var storeid: String? = null

        @SerializedName("ITEMS")
        @Expose
        var items: List<Item>? = null

         constructor(
             orderno: String?,
             status: String?,
             designation: String?,
             empid: String?,
             storeid: String?,
             items: List<Item>?,
         ) {
             this.orderno = orderno
             this.status = status
             this.designation = designation
             this.empid = empid
             this.storeid = storeid
             this.items = items
         }

         fun withOrderno(orderno: String?): Order {
            this.orderno = orderno
            return this
        }

        fun withStatus(status: String?): Order {
            this.status = status
            return this
        }

        fun withDesignation(designation: String?): Order {
            this.designation = designation
            return this
        }

        fun withEmpid(empid: String?): Order {
            this.empid = empid
            return this
        }

        fun withStoreid(storeid: String?): Order {
            this.storeid = storeid
            return this
        }

        fun withItems(items: List<Item>?): Order {
            this.items = items
            return this
        }
    }
}