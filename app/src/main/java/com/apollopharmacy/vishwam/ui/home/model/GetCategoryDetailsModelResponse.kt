package com.apollopharmacy.vishwam.ui.home.model

import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SensingFileUploadResponse
import com.apollopharmacy.vishwam.ui.home.champs.SampleModel
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarActivity
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse
import com.apollopharmacy.vishwam.util.fileupload.FileDownloadResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

 class GetCategoryDetailsModelResponse : Serializable {
     @SerializedName("status")
     @Expose
     var status: Boolean? = null

     @SerializedName("message")
     @Expose
     var message: String? = null

    var storeIdP:String?=""

    var addressP:String?=""

    var issuedOnP:String?=""

    var storeNameP:String?=""

    var storeCityP:String?=""

    var storeStateP:String?=""

    var technicalDetails: String?=""

    var technicalText:String?=""

    var softSkills: String?=""

    var softSkillsText:String?=""

    var otherTraining: String?=""

    var otherTrainingText:String?=""

    var issuesToBeResolved: String?=""

    var issuesToBeResolvedText:String?=""

    var totalProgressP:Float?=0f



     @SerializedName("CategoryDetails")
     @Expose
     var categoryDetails: List<CategoryDetail>? = null


     class CategoryDetail : Serializable{
        @SerializedName("id")
        @Expose
        var id: Int? = null

         @SerializedName("champ_auto_id")
         @Expose
         var champ_auto_id: String? = null

         @SerializedName("type")
         @Expose
         var type: String? = null

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null


         @SerializedName("modified_by")
         @Expose
         var modifiedBy: Any? = null

         @SerializedName("modified_date")
         @Expose
         var modifiedDate: Any? = null

        var sumOfThreePicsinMb:String?=null


        var sumOfSubCategoryRating: Float? = 0f

         var clickedSubmit: Boolean? = false

         var imageUrls: ArrayList<String>?=null

         var   imageDataLists: MutableList<ImagesDatas>?=null

         class ImagesDatas  : Serializable{
             var file: File?=null
             var imageUrl: String?=""
             var sensingUploadUrlFilled:Boolean=false
             var imageFilled: Boolean=false
             var sensingFileUploadResponse: SensingFileUploadResponse? = null
             var fileDownloadResponse: FileDownloadResponse? = null

         }




        var subCategoryDetails: List<GetSubCategoryDetailsModelResponse.SubCategoryDetail>? = null

        inner class SubCategoryDetail : Serializable {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("sub_category_name")
            @Expose
            var subCategoryName: String? = null

            @SerializedName("category_name")
            @Expose
            var categoryName: String? = null

            @SerializedName("rating")
            @Expose
            var rating: String? = null

            @SerializedName("created_date")
            @Expose
            var createdDate: String? = null

            var givenRating: Float? = null

            }


//        inner class ImgeDtcl(var file: File?)

    }
}