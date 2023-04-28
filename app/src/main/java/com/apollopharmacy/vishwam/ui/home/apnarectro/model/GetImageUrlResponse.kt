package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetImageUrlResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("REMARKS")
    @Expose
    var remarks: List<Remark>? = null

    @SerializedName("CATEGORY_LIST")
    @Expose
    var categoryList: List<Category>? = null
    var retroId: String? = null
    fun setretroId(retroid: String) {
        retroId = retroid
    }
    fun withMessage(message: String?): GetImageUrlResponse {
        this.message = message
        return this
    }

    fun withStatus(status: Boolean?): GetImageUrlResponse {
        this.status = status
        return this
    }

    fun withRemarks(remarks: List<Remark>?): GetImageUrlResponse {
        this.remarks = remarks
        return this
    }

    fun withCategoryList(categoryList: List<Category>?): GetImageUrlResponse {
        this.categoryList = categoryList
        return this
    }

    public class Category : Serializable {
        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: String? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        var categoryname: String? = null

        @SerializedName("IMAGE_URLS")
        @Expose
        var imageUrls: List<ImageUrl>? = null



        var groupByImageUrlList: List<List<ImageUrl>>? = null

        fun withCategoryid(categoryid: String?): Category {
            this.categoryid = categoryid
            return this
        }

        fun withCategoryname(categoryname: String?): Category {
            this.categoryname = categoryname
            return this
        }

        fun withImageUrls(imageUrls: List<ImageUrl>?): Category {
            this.imageUrls = imageUrls
            return this
        }
    }

    public class ImageUrl : Serializable {
        @SerializedName("URL")
        @Expose
        var url: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: Any? = null

        @SerializedName("RETORAUTOID")
        @Expose
        var retorautoid: Int? = null

        @SerializedName("IMAGEID")
        @Expose
        var imageid: String? = null

        @SerializedName("STAGE")
        @Expose
        var stage: String? = null

        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: Int? = null

        @SerializedName("POSITION")
        @Expose
        var position: Int? = null
        fun seturl(urL: String) {
            url = urL
        }
        fun withUrl(url: String?): ImageUrl {
            this.url = url
            return this
        }

        fun withStatus(status: String?): ImageUrl {
            this.status = status
            return this
        }

        fun withRemarks(remarks: Any?): ImageUrl {
            this.remarks = remarks
            return this
        }

        fun withRetorautoid(retorautoid: Int?): ImageUrl {
            this.retorautoid = retorautoid
            return this
        }

        fun withImageid(imageid: String?): ImageUrl {
            this.imageid = imageid
            return this
        }

        fun withStage(stage: String?): ImageUrl {
            this.stage = stage
            return this
        }

        fun withCategoryid(categoryid: Int?): ImageUrl {
            this.categoryid = categoryid
            return this
        }

        fun withPosition(position: Int?): ImageUrl {
            this.position = position
            return this
        }
        var isVerified: Boolean? = false
        fun setisVerified(pos: Boolean) {
            isVerified = pos
        }

    }

    inner class Remark : Serializable {
        @SerializedName("RECID")
        @Expose
        var recid: Int? = null

        @SerializedName("RETRO_AUTO_ID")
        @Expose
        var retroAutoId: String? = null

        @SerializedName("STORE_ID")
        @Expose
        var storeId: String? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: String? = null

        @SerializedName("RATING")
        @Expose
        var rating: Int? = null

        @SerializedName("CREATED_BY")
        @Expose
        var createdBy: String? = null

        @SerializedName("CREATED_DATE")
        @Expose
        var createdDate: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: Any? = null

        @SerializedName("STAGE")
        @Expose
        var stage: Any? = null
        fun withRecid(recid: Int?): Remark {
            this.recid = recid
            return this
        }

        fun withRetroAutoId(retroAutoId: String?): Remark {
            this.retroAutoId = retroAutoId
            return this
        }

        fun withStoreId(storeId: String?): Remark {
            this.storeId = storeId
            return this
        }

        fun withRemarks(remarks: String?): Remark {
            this.remarks = remarks
            return this
        }

        fun withRating(rating: Int?): Remark {
            this.rating = rating
            return this
        }

        fun withCreatedBy(createdBy: String?): Remark {
            this.createdBy = createdBy
            return this
        }

        fun withCreatedDate(createdDate: String?): Remark {
            this.createdDate = createdDate
            return this
        }

        fun withStatus(status: Any?): Remark {
            this.status = status
            return this
        }

        fun withStage(stage: Any?): Remark {
            this.stage = stage
            return this
        }
    }
}