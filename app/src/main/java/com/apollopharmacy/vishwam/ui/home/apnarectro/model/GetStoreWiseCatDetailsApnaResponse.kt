package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class GetStoreWiseCatDetailsApnaResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("CONFIGLIST")
    @Expose
    var configlist: List<Config>? = null

    class Config : Serializable {
        @SerializedName("CATEGORY_ID")
        @Expose
        var categoryId: String? = null

        @SerializedName("STORE_ID")
        @Expose
        var storeId: String? = null

        @SerializedName("STORE_NAME")
        @Expose
        var storeName: String? = null

        var imageUploaded: Boolean? = null

        @SerializedName("CATEGORY_NAME")
        @Expose
        var categoryName: String? = null

        @SerializedName("CATEGORY_IMAGE_URL")
        @Expose
        var categoryImageUrl: String? = null

        @SerializedName("CATEGORY_IMAGE_UPLOAD_COUNT")
        @Expose
        var categoryImageUploadCount: String? = null

        var imageDataDto: MutableList<ImgeDtcl>? = null

        class ImgeDtcl(var file: File?, var integerButtonCount: Int,  var base64Images: String, var positionLoop: Int, var imageStatus:String, var imageId:String)


    }

}
