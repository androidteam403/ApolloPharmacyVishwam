package com.apollopharmacy.vishwam.data.model.discount

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetDiscountColorResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("trainingDetails")
    @Expose
    var trainingDetails: List<TrainingDetail>? = null
    fun withStatus(status: Boolean?): GetDiscountColorResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): GetDiscountColorResponse {
        this.message = message
        return this
    }

    fun withTrainingDetails(trainingDetails: List<TrainingDetail>?): GetDiscountColorResponse {
        this.trainingDetails = trainingDetails
        return this
    }

    inner class TrainingDetail : Serializable {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("length")
        @Expose
        var length: String? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null
        fun withId(id: Int?): TrainingDetail {
            this.id = id
            return this
        }

        fun withName(name: String?): TrainingDetail {
            this.name = name
            return this
        }

        fun withLength(length: String?): TrainingDetail {
            this.length = length
            return this
        }

        fun withType(type: String?): TrainingDetail {
            this.type = type
            return this
        }

        fun withCreatedDate(createdDate: String?): TrainingDetail {
            this.createdDate = createdDate
            return this
        }
    }
}