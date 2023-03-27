package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DoctorListResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("SITELIST")
    @Expose
    var sitelist: Any? = null

    @SerializedName("DOCTORLIST")
    @Expose
    var doctorlist: List<Doctor>? = null
    fun withStatus(status: Boolean?): DoctorListResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): DoctorListResponse {
        this.message = message
        return this
    }

    fun withSitelist(sitelist: Any?): DoctorListResponse {
        this.sitelist = sitelist
        return this
    }

    fun withDoctorlist(doctorlist: List<Doctor>?): DoctorListResponse {
        this.doctorlist = doctorlist
        return this
    }

     class Doctor : Serializable {
        @SerializedName("DOCTORNAME")
        @Expose
        var doctorname: String? = null

        @SerializedName("SPECIALITY")
        @Expose
        var speciality: String? = null
        fun withDoctorname(doctorname: String?): Doctor {
            this.doctorname = doctorname
            return this
        }

        fun withSpeciality(speciality: String?): Doctor {
            this.speciality = speciality
            return this
        }
    }
}