package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class ApproveRejectListRequest : Serializable {
    @SerializedName("EMPID")
    @Expose
    var empid: String? = null

    @SerializedName("FROMDATE")
    @Expose
    var fromdate: String? = null

    @SerializedName("TODATE")
    @Expose
    var todate: String? = null

    @SerializedName("STARTPAGENO")
    @Expose
    var startpageno: Int? = null

    @SerializedName("ENDPAGENO")
    @Expose
    var endpageno: Int? = null

    constructor(
        empid: String?,
        fromdate: String?,
        todate: String?,
        startpageno: Int?,
        endpageno: Int?
    ) {
        this.empid = empid
        this.fromdate = fromdate
        this.todate = todate
        this.startpageno = startpageno
        this.endpageno = endpageno
    }

    fun withEmpid(empid: String?): ApproveRejectListRequest {
        this.empid = empid
        return this
    }

    fun withFromdate(fromdate: String?): ApproveRejectListRequest {
        this.fromdate = fromdate
        return this
    }

    fun withTodate(todate: String?): ApproveRejectListRequest {
        this.todate = todate
        return this
    }

    fun withStartpageno(startpageno: Int?): ApproveRejectListRequest {
        this.startpageno = startpageno
        return this
    }

    fun withEndpageno(endpageno: Int?): ApproveRejectListRequest {
        this.endpageno = endpageno
        return this
    }
}