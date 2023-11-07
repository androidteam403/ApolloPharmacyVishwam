package com.apollopharmacy.vishwam.ui.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveSurveyModelRequestt : Serializable {
    @SerializedName("categoryDetails")
    @Expose
    var categoryDetails: List<CategoryDetail>? = null

    @SerializedName("ImageDetails")
    @Expose
    var imageDetails: List<ImageDetail>? = null

    @SerializedName("headerDetails")
    @Expose
    var headerDetails: HeaderDetails? = null

     class CategoryDetail : Serializable {
        @SerializedName("CATEGORY_ID")
        @Expose
        var categoryId: String? = null

        @SerializedName("SUBCATEGORY_ID")
        @Expose
        var subcategoryId: String? = null

        @SerializedName("VALUE")
        @Expose
        var value: String? = null
    }

     class ImageDetail : Serializable {
        @SerializedName("CATEGORY_ID")
        @Expose
        var categoryId: String? = null

        @SerializedName("IMAGE_URL")
        @Expose
        var imageUrl: String? = null
    }

     class HeaderDetails : Serializable {
        @SerializedName("champ_auto_id")
        @Expose
        var champAutoId: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("created_by")
        @Expose
        var createdBy: String? = null

        @SerializedName("date_of_visit")
        @Expose
        var dateOfVisit: String? = null

        @SerializedName("email_id_of_cc")
        @Expose
        var emailIdOfCc: String? = null

        @SerializedName("email_id_of_executive")
        @Expose
        var emailIdOfExecutive: String? = null

        @SerializedName("email_id_of_manager")
        @Expose
        var emailIdOfManager: String? = null

        @SerializedName("email_id_of_recipients")
        @Expose
        var emailIdOfRecipients: String? = null

        @SerializedName("email_id_of_regional_head")
        @Expose
        var emailIdOfRegionalHead: String? = null

        @SerializedName("email_id_of_trainer")
        @Expose
        var emailIdOfTrainer: String? = null

        @SerializedName("issues_to_be_resolved")
        @Expose
        var issuesToBeResolved: String? = null

        @SerializedName("other_training")
        @Expose
        var otherTraining: String? = null

        @SerializedName("soft_skills")
        @Expose
        var softSkills: String? = null

        @SerializedName("state")
        @Expose
        var state: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("store_id")
        @Expose
        var storeId: String? = null

        @SerializedName("techinal_details")
        @Expose
        var techinalDetails: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null

    }
}