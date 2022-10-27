package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class QcListsResponse : Serializable {
    @SerializedName("PENDINGLIST")
    @Expose
    var pendinglist: List<Pending>? = null

    @SerializedName("APPROVEDLIST")
    @Expose
    var approvedlist: List<Approved>? = null

    @SerializedName("REJECTEDLIST")
    @Expose
    var rejectedlist: List<Any>? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null
    fun withPendinglist(pendinglist: List<Pending>?): QcListsResponse {
        this.pendinglist = pendinglist
        return this
    }

    fun withApprovedlist(approvedlist: List<Approved>?): QcListsResponse {
        this.approvedlist = approvedlist
        return this
    }

    fun withRejectedlist(rejectedlist: List<Any>?): QcListsResponse {
        this.rejectedlist = rejectedlist
        return this
    }

    fun withStatus(status: Boolean?): QcListsResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): QcListsResponse {
        this.message = message
        return this
    }

    class Approved {
        @SerializedName("CUSTNAME")
        @Expose
        var custname: String? = null

        @SerializedName("OMSORDERNO")
        @Expose
        var omsorderno: String? = null

        @SerializedName("ORDERNO")
        @Expose
        var orderno: String? = null

        @SerializedName("MOBILENO")
        @Expose
        var mobileno: String? = null

        @SerializedName("STOREID")
        @Expose
        var storeid: String? = null

        @SerializedName("DC_CODE")
        @Expose
        var dcCode: String? = null

        @SerializedName("REQUESTEDDATE")
        @Expose
        var requesteddate: String? = null

        @SerializedName("QCFAILDATE")
        @Expose
        var qcfaildate: Any? = null

        @SerializedName("TRACKINSTATUSGDESCRIPTION")
        @Expose
        var trackinstatusgdescription: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null

        @SerializedName("USERID")
        @Expose
        var userid: String? = null

        @SerializedName("REMARKSDESC")
        @Expose
        var remarksdesc: String? = null

        @SerializedName("APPROVEDDATE")
        @Expose
        var approveddate: Any? = null

        @SerializedName("REJECTEDDATE")
        @Expose
        var rejecteddate: String? = null

        var position: String? = null

        fun setposition(pos: String) {
            position = pos
        }

        var isClick: Boolean = false

        fun setisClick(pos: Boolean) {
            isClick = pos
        }

        var order: String? = null

        fun setorder(pos: String) {
            order = pos
        }

        fun withCustname(custname: String?): Approved {
            this.custname = custname
            return this
        }

        fun withOmsorderno(omsorderno: String?): Approved {
            this.omsorderno = omsorderno
            return this
        }

        fun withOrderno(orderno: String?): Approved {
            this.orderno = orderno
            return this
        }

        fun withMobileno(mobileno: String?): Approved {
            this.mobileno = mobileno
            return this
        }

        fun withStoreid(storeid: String?): Approved {
            this.storeid = storeid
            return this
        }

        fun withDcCode(dcCode: String?): Approved {
            this.dcCode = dcCode
            return this
        }

        fun withRequesteddate(requesteddate: String?): Approved {
            this.requesteddate = requesteddate
            return this
        }

        fun withQcfaildate(qcfaildate: Any?): Approved {
            this.qcfaildate = qcfaildate
            return this
        }

        fun withTrackinstatusgdescription(trackinstatusgdescription: String?): Approved {
            this.trackinstatusgdescription = trackinstatusgdescription
            return this
        }

        fun withStatus(status: String?): Approved {
            this.status = status
            return this
        }

        fun withUserid(userid: String?): Approved {
            this.userid = userid
            return this
        }

        fun withRemarksdesc(remarksdesc: String?): Approved {
            this.remarksdesc = remarksdesc
            return this
        }

        fun withApproveddate(approveddate: Any?): Approved {
            this.approveddate = approveddate
            return this
        }

        fun withRejecteddate(rejecteddate: String?): Approved {
            this.rejecteddate = rejecteddate
            return this
        }
    }

    class Pending {
        @SerializedName("CUSTNAME")
        @Expose
        var custname: String? = null

        @SerializedName("OMSORDERNO")
        @Expose
        var omsorderno: String? = null

        @SerializedName("ORDERNO")
        @Expose
        var orderno: String? = null

        @SerializedName("MOBILENO")
        @Expose
        var mobileno: String? = null

        @SerializedName("STOREID")
        @Expose
        var storeid: String? = null

        @SerializedName("DC_CODE")
        @Expose
        var dcCode: String? = null

        @SerializedName("REQUESTEDDATE")
        @Expose
        var requesteddate: String? = null

        @SerializedName("QCFAILDATE")
        @Expose
        var qcfaildate: String? = null

        @SerializedName("TRACKINSTATUSGDESCRIPTION")
        @Expose
        var trackinstatusgdescription: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null

        @SerializedName("USERID")
        @Expose
        var userid: String? = null

        @SerializedName("REMARKSDESC")
        @Expose
        var remarksdesc: String? = null

        @SerializedName("APPROVEDDATE")
        @Expose
        var approveddate: String? = null

        @SerializedName("REJECTEDDATE")
        @Expose
        var rejecteddate: String? = null

        var isItemChecked: Boolean = false

        var isOrderExpanded: Boolean = false

        fun setisItemChecked(checked: Boolean) {
            isItemChecked = checked
        }

        var isClick: Boolean = false

        fun setisClick(pos: Boolean) {
            isClick = pos
        }

        fun withCustname(custname: String?): Pending {
            this.custname = custname
            return this
        }

        fun withOmsorderno(omsorderno: String?): Pending {
            this.omsorderno = omsorderno
            return this
        }

        fun withOrderno(orderno: String?): Pending {
            this.orderno = orderno
            return this
        }

        fun withMobileno(mobileno: String?): Pending {
            this.mobileno = mobileno
            return this
        }

        fun withStoreid(storeid: String?): Pending {
            this.storeid = storeid
            return this
        }

        fun withDcCode(dcCode: String?): Pending {
            this.dcCode = dcCode
            return this
        }

        fun withRequesteddate(requesteddate: String?): Pending {
            this.requesteddate = requesteddate
            return this
        }

        fun withQcfaildate(qcfaildate: String?): Pending {
            this.qcfaildate = qcfaildate
            return this
        }

        fun withTrackinstatusgdescription(trackinstatusgdescription: String?): Pending {
            this.trackinstatusgdescription = trackinstatusgdescription
            return this
        }

        fun withStatus(status: String?): Pending {
            this.status = status
            return this
        }

        fun withUserid(userid: String?): Pending {
            this.userid = userid
            return this
        }

        fun withRemarksdesc(remarksdesc: String?): Pending {
            this.remarksdesc = remarksdesc
            return this
        }

        fun withApproveddate(approveddate: String?): Pending {
            this.approveddate = approveddate
            return this
        }

        fun withRejecteddate(rejecteddate: String?): Pending {
            this.rejecteddate = rejecteddate
            return this
        }
    }
}