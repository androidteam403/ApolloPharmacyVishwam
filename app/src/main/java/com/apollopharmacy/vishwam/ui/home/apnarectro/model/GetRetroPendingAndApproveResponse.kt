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
        var retroSublist: List<Retro>? = null

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

        @SerializedName("APPROVED_BY")
        @Expose
        var approvedBy: String? = null

        @SerializedName("APPROVED_DATE")
        @Expose
        var approvedDate: String? = null

        @SerializedName("RESHOOT_BY")
        @Expose
        var reshootBy: String? = null

        @SerializedName("RESHOOT_DATE")
        @Expose
        var reshootDate: String? = null

        @SerializedName("PARTIALLY_APPROVED_BY")
        @Expose
        var partiallyApprovedBy: String? = null

        @SerializedName("PARTIALLY_APPROVED_DATE")
        @Expose
        var partiallyApprovedDate: String? = null

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

        fun withApprovedBy(approvedBy: String?): Retro {
            this.approvedBy = approvedBy
            return this
        }

        fun withApprovedDate(approvedDate: String?): Retro {
            this.approvedDate = approvedDate
            return this
        }

        fun withReshootBy(reshootBy: String?): Retro {
            this.reshootBy = reshootBy
            return this
        }

        fun withReshootDate(reshootDate: String?): Retro {
            this.reshootDate = reshootDate
            return this
        }

        fun withPartiallyApprovedBy(partiallyApprovedBy: String?): Retro {
            this.partiallyApprovedBy = partiallyApprovedBy
            return this
        }

        fun withPartiallyApprovedDate(partiallyApprovedDate: String?): Retro {
            this.partiallyApprovedDate = partiallyApprovedDate
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