package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Getqcfailpendinghistoryforhierarchy : Serializable {
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
    var isDataFetched: Boolean = false

    fun setisDataFetched(pos: Boolean) {
        isDataFetched = pos
    }
    var emplId: String? = null
    fun setemplId(empId: String) {
        emplId = empId
    }
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("PENDINGCOUNT")
    @Expose
    var pendingcount: List<Pendingcount>? = null
    fun withStatus(status: Boolean?): Getqcfailpendinghistoryforhierarchy {
        this.status = status
        return this
    }

    fun withMessage(message: String?): Getqcfailpendinghistoryforhierarchy {
        this.message = message
        return this
    }

    fun withPendingcount(pendingcount: List<Pendingcount>?): Getqcfailpendinghistoryforhierarchy {
        this.pendingcount = pendingcount
        return this
    }

    public class Pendingcount : Serializable {

        var isClick: Boolean = false

        fun setisClick(pos: Boolean) {
            isClick = pos
        }
        var isSearchClick: Boolean = false

        fun setiisSearchClick(pos: Boolean) {
            isSearchClick = pos
        }
        var isexecutiveClick: Boolean = false

        fun setisexecutiveClick(pos: Boolean) {
            isexecutiveClick = pos
        }
        var searchexecutiveClick: Boolean = false

        fun setsearchexecutiveClick(pos: Boolean) {
            searchexecutiveClick = pos
        }
        @SerializedName("EMPID")
        @Expose
        var empid: String? = null

        @SerializedName("DESIGNATION")
        @Expose
        var designation: String? = null

        @SerializedName("SITEID")
        @Expose
        var siteid: String? = null

        @SerializedName("PENDINGCOUNT")
        @Expose
        var pendingcount: Int? = null

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


        fun withEmpid(empid: String?): Pendingcount {
            this.empid = empid
            return this
        }

        fun withDesignation(designation: String?): Pendingcount {
            this.designation = designation
            return this
        }

        fun withSiteid(siteid: String?): Pendingcount {
            this.siteid = siteid
            return this
        }

        fun withPendingcount(pendingcount: Int?): Pendingcount {
            this.pendingcount = pendingcount
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