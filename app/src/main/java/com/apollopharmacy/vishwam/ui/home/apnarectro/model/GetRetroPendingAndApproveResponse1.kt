package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetRetroPendingAndApproveResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("RETROLIST")
    @Expose
    var retrolist: List<Retro>? = null
    var groupByRetrodList: List<List<Retro>>? = null
    fun withMessage(message: String?): GetRetroPendingAndApproveResponse {
        this.message = message
        return this
    }

    fun withStatus(status: Boolean?): GetRetroPendingAndApproveResponse {
        this.status = status
        return this
    }

    fun withRetrolist(retrolist: List<Retro>?): GetRetroPendingAndApproveResponse {
        this.retrolist = retrolist
        return this
    }

    inner class Retro : Serializable {
        @SerializedName("RETROID")
        @Expose
        var retroid: String? = null

        @SerializedName("STORE")
        @Expose
        var store: String? = null

        @SerializedName("UPLOADED_BY")
        @Expose
        var uploadedBy: String? = null

        @SerializedName("UPLOADED_DATE")
        @Expose
        var uploadedDate: String? = null

        @SerializedName("EXECUTIVE_APPROVED_BY")
        @Expose
        var executiveApprovedBy: String? = null

        @SerializedName("EXECUTIVE_APPROVED_DATE")
        @Expose
        var executiveApprovedDate: String? = null

        @SerializedName("EXECUTIVE_RESHOOT_BY")
        @Expose
        var executiveReshootBy: String? = null

        @SerializedName("EXECUTIVE_RESHOOT_DATE")
        @Expose
        var executiveReshootDate: String? = null

        @SerializedName("MANAGER_APPROVED_BY")
        @Expose
        var managerApprovedBy: String? = null

        @SerializedName("MANAGER_APPROVED_DATE")
        @Expose
        var managerApprovedDate: String? = null

        @SerializedName("MANAGER_RESHOOT_BY")
        @Expose
        var managerReshootBy: Any? = null

        @SerializedName("MANAGER_RESHOOT_DATE")
        @Expose
        var managerReshootDate: Any? = null

        @SerializedName("GM_APPROVED_BY")
        @Expose
        var gmApprovedBy: String? = null

        @SerializedName("GM_APPROVED_DATE")
        @Expose
        var gmApprovedDate: String? = null

        @SerializedName("GM_RESHOOT_BY")
        @Expose
        var gmReshootBy: Any? = null

        @SerializedName("GM_RESHOOT_DATE")
        @Expose
        var gmReshootDate: String? = null

        @SerializedName("CEO_APPROVED_BY")
        @Expose
        var ceoApprovedBy: Any? = null

        @SerializedName("CEO_APPROVED_DATE")
        @Expose
        var ceoApprovedDate: Any? = null

        @SerializedName("CEO_RESHOOT_BY")
        @Expose
        var ceoReshootBy: Any? = null

        @SerializedName("CEO_RESHOOT_DATE")
        @Expose
        var ceoReshootDate: Any? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null

        @SerializedName("STAGE")
        @Expose
        var stage: String? = null

        @SerializedName("HIERARCHYSTATUS")
        @Expose
        var hierarchystatus: String? = null
        fun withRetroid(retroid: String?): Retro {
            this.retroid = retroid
            return this
        }

        fun withStore(store: String?): Retro {
            this.store = store
            return this
        }

        fun withUploadedBy(uploadedBy: String?): Retro {
            this.uploadedBy = uploadedBy
            return this
        }

        fun withUploadedDate(uploadedDate: String?): Retro {
            this.uploadedDate = uploadedDate
            return this
        }

        fun withExecutiveApprovedBy(executiveApprovedBy: String?): Retro {
            this.executiveApprovedBy = executiveApprovedBy
            return this
        }

        fun withExecutiveApprovedDate(executiveApprovedDate: String?): Retro {
            this.executiveApprovedDate = executiveApprovedDate
            return this
        }

        fun withExecutiveReshootBy(executiveReshootBy: String?): Retro {
            this.executiveReshootBy = executiveReshootBy
            return this
        }

        fun withExecutiveReshootDate(executiveReshootDate: String?): Retro {
            this.executiveReshootDate = executiveReshootDate
            return this
        }

        fun withManagerApprovedBy(managerApprovedBy: String?): Retro {
            this.managerApprovedBy = managerApprovedBy
            return this
        }

        fun withManagerApprovedDate(managerApprovedDate: String?): Retro {
            this.managerApprovedDate = managerApprovedDate
            return this
        }

        fun withManagerReshootBy(managerReshootBy: Any?): Retro {
            this.managerReshootBy = managerReshootBy
            return this
        }

        fun withManagerReshootDate(managerReshootDate: Any?): Retro {
            this.managerReshootDate = managerReshootDate
            return this
        }

        fun withGmApprovedBy(gmApprovedBy: String?): Retro {
            this.gmApprovedBy = gmApprovedBy
            return this
        }

        fun withGmApprovedDate(gmApprovedDate: String?): Retro {
            this.gmApprovedDate = gmApprovedDate
            return this
        }

        fun withGmReshootBy(gmReshootBy: Any?): Retro {
            this.gmReshootBy = gmReshootBy
            return this
        }

        fun withGmReshootDate(gmReshootDate: String?): Retro {
            this.gmReshootDate = gmReshootDate
            return this
        }

        fun withCeoApprovedBy(ceoApprovedBy: Any?): Retro {
            this.ceoApprovedBy = ceoApprovedBy
            return this
        }

        fun withCeoApprovedDate(ceoApprovedDate: Any?): Retro {
            this.ceoApprovedDate = ceoApprovedDate
            return this
        }

        fun withCeoReshootBy(ceoReshootBy: Any?): Retro {
            this.ceoReshootBy = ceoReshootBy
            return this
        }

        fun withCeoReshootDate(ceoReshootDate: Any?): Retro {
            this.ceoReshootDate = ceoReshootDate
            return this
        }

        fun withStatus(status: String?): Retro {
            this.status = status
            return this
        }

        fun withStage(stage: String?): Retro {
            this.stage = stage
            return this
        }

        fun withHierarchystatus(hierarchystatus: String?): Retro {
            this.hierarchystatus = hierarchystatus
            return this
        }
    }
}