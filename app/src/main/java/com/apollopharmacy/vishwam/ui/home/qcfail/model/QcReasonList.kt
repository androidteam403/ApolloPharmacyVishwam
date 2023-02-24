package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QcReasonList : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("REMARKSLIST")
    @Expose
    var remarkslist: List<Remarks>? = null
    fun withStatus(status: Boolean?): QcReasonList {
        this.status = status
        return this
    }

    fun withMessage(message: String?): QcReasonList {
        this.message = message
        return this
    }

    fun withRemarkslist(remarkslist: List<Remarks>?): QcReasonList {
        this.remarkslist = remarkslist
        return this
    }

    inner class Remarks {
        @SerializedName("REASONCODE")
        @Expose
        var reasoncode: String? = null

        @SerializedName("REASONBASE")
        @Expose
        var reasonbase: String? = null

        @SerializedName("REASONDESC")
        @Expose
        var reasondesc: String? = null
        fun withReasoncode(reasoncode: String?): Remarks {
            this.reasoncode = reasoncode
            return this
        }

        fun withReasonbase(reasonbase: String?): Remarks {
            this.reasonbase = reasonbase
            return this
        }

        fun withReasondesc(reasondesc: String?): Remarks {
            this.reasondesc = reasondesc
            return this
        }
    }
}