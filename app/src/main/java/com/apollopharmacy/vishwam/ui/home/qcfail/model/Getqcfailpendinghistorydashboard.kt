package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import java.io.Serializable

class Getqcfailpendinghistorydashboard : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null
    var designation: String? = null
    fun setdesignation(desig: String) {
        designation = desig
    }

    var employeId: String? = null
    fun setemployeId(empId: String) {
        employeId = empId
    }


    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("PENDINGCOUNT")
    @Expose
    var pendingcount: List<Pendingcount>? = null
    fun withStatus(status: Boolean?): Getqcfailpendinghistorydashboard {
        this.status = status
        return this
    }

    fun withMessage(message: String?): Getqcfailpendinghistorydashboard {
        this.message = message
        return this
    }

    fun withPendingcount(pendingcount: List<Pendingcount>?): Getqcfailpendinghistorydashboard {
        this.pendingcount = pendingcount
        return this
    }

    public class Pendingcount : Serializable {
        @SerializedName("EMPID")
        @Expose
        var empid: String? = null

        @SerializedName("DESIGNATION")
        @Expose
        var designation: String? = null

        @SerializedName("RTOCOUNT")
        @Expose
        var rtocount: Int? = null

        @SerializedName("RTOAMOUNT")
        @Expose
        var rtoamount: Double? = null

        @SerializedName("RRTOCOUNT")
        @Expose
        var rrtocount: Int? = null

        @SerializedName("RRTOAMOUNT")
        @Expose
        var rrtoamount: Double? = null

        var isClick: Boolean = false

        fun setisClick(pos: Boolean) {
            isClick = pos
        }
        var fetched: Boolean = false
        fun setfetched(empId: Boolean) {
            fetched = empId
        }

        var isSearchClick: Boolean = false

        fun setisSearchClick(pos: Boolean) {
            isSearchClick = pos
        }
        fun withEmpid(empid: String?): Pendingcount {
            this.empid = empid
            return this
        }

        fun withDesignation(designation: String?): Pendingcount {
            this.designation = designation
            return this
        }

        fun withRtocount(rtocount: Int?): Pendingcount {
            this.rtocount = rtocount
            return this
        }

        fun withRtoamount(rtoamount: Double?): Pendingcount {
            this.rtoamount = rtoamount
            return this
        }

        fun withRrtocount(rrtocount: Int?): Pendingcount {
            this.rrtocount = rrtocount
            return this
        }

        fun withRrtoamount(rrtoamount: Double?): Pendingcount {
            this.rrtoamount = rrtoamount
            return this
        }
    }
}