package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ApproveRejectListResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("getPendingList")
    @Expose
    var getPendingList: List<GetPending>? = null

    @SerializedName("getApprovedList")
    @Expose
    var getApprovedList: List<GetApproved>? = null
    fun withMessage(message: String?): ApproveRejectListResponse {
        this.message = message
        return this
    }

    fun withStatus(status: Boolean?): ApproveRejectListResponse {
        this.status = status
        return this
    }

    fun withGetPendingList(getPendingList: List<GetPending>?): ApproveRejectListResponse {
        this.getPendingList = getPendingList
        return this
    }

    fun withGetApprovedList(getApprovedList: List<GetApproved>?): ApproveRejectListResponse {
        this.getApprovedList = getApprovedList
        return this
    }

    inner class GetApproved : Serializable {
        @SerializedName("CATEGORY_AUOT_ID")
        @Expose
        var categoryAuotId: String? = null

        @SerializedName("CATEGORY_ID")
        @Expose
        var categoryId: Int? = null
        var isExpanded:Boolean = false

        @SerializedName("STORE_ID")
        @Expose
        var storeId: String? = null

        @SerializedName("CATEGORY_NAME")
        @Expose
        var categoryName: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null

        @SerializedName("APPROVED_BY")
        @Expose
        var approvedBy: String? = null



        var imageUrls: List<LineImagesResponse.ImageUrl>? = null

        @SerializedName("REJECTED_BY")
        @Expose
        var rejectedBy: String? = null

        @SerializedName("APPROVED_DATE")
        @Expose
        var approvedDate: String? = null

        @SerializedName("REJECTED_DATE")
        @Expose
        var rejectedDate: String? = null
        fun withCategoryAuotId(categoryAuotId: String?): GetApproved {
            this.categoryAuotId = categoryAuotId
            return this
        }

        fun withCategoryId(categoryId: Int?): GetApproved {
            this.categoryId = categoryId
            return this
        }

        fun withStoreId(storeId: String?): GetApproved {
            this.storeId = storeId
            return this
        }

        fun withCategoryName(categoryName: String?): GetApproved {
            this.categoryName = categoryName
            return this
        }

        fun withStatus(status: String?): GetApproved {
            this.status = status
            return this
        }

        fun withApprovedBy(approvedBy: String?): GetApproved {
            this.approvedBy = approvedBy
            return this
        }

        fun withRejectedBy(rejectedBy: String?): GetApproved {
            this.rejectedBy = rejectedBy
            return this
        }

        fun withApprovedDate(approvedDate: String?): GetApproved {
            this.approvedDate = approvedDate
            return this
        }

        fun withRejectedDate(rejectedDate: String?): GetApproved {
            this.rejectedDate = rejectedDate
            return this
        }
        inner class ImageUrl : Serializable {
            @SerializedName("URL")
            @Expose
            var url: String? = null
            fun withUrl(url: String?): ImageUrl {
                this.url = url
                return this
            }
        }
    }

    inner class GetPending : Serializable {
        @SerializedName("CATEGORY_AUOT_ID")
        @Expose
        var categoryAuotId: String? = null

        @SerializedName("CATEGORY_ID")
        @Expose
        var categoryId: Int? = null

        @SerializedName("STORE_ID")
        @Expose
        var storeId: String? = null

        @SerializedName("CATEGORY_NAME")
        @Expose
        var categoryName: String? = null
        var isExpanded:Boolean = false

        @SerializedName("STATUS")
        @Expose
        var status: String? = null
        var imageUrls: List<LineImagesResponse.ImageUrl>? = null

        @SerializedName("APPROVED_BY")
        @Expose
        var approvedBy: String? = null

        @SerializedName("REJECTED_BY")
        @Expose
        var rejectedBy: String? = null

        @SerializedName("APPROVED_DATE")
        @Expose
        var approvedDate: String? = null

        @SerializedName("REJECTED_DATE")
        @Expose
        var rejectedDate: String? = null
        fun withCategoryAuotId(categoryAuotId: String?): GetPending {
            this.categoryAuotId = categoryAuotId
            return this
        }

        fun withCategoryId(categoryId: Int?): GetPending {
            this.categoryId = categoryId
            return this
        }

        fun withStoreId(storeId: String?): GetPending {
            this.storeId = storeId
            return this
        }

        fun withCategoryName(categoryName: String?): GetPending {
            this.categoryName = categoryName
            return this
        }

        fun withStatus(status: String?): GetPending {
            this.status = status
            return this
        }

        fun withApprovedBy(approvedBy: String?): GetPending {
            this.approvedBy = approvedBy
            return this
        }

        fun withRejectedBy(rejectedBy: String?): GetPending {
            this.rejectedBy = rejectedBy
            return this
        }

        fun withApprovedDate(approvedDate: String?): GetPending {
            this.approvedDate = approvedDate
            return this
        }

        fun withRejectedDate(rejectedDate: String?): GetPending {
            this.rejectedDate = rejectedDate
            return this
        }
    }
    inner class ImageUrl : Serializable {
        @SerializedName("URL")
        @Expose
        var url: String? = null
        fun withUrl(url: String?): ImageUrl {
            this.url = url
            return this
        }
    }
}