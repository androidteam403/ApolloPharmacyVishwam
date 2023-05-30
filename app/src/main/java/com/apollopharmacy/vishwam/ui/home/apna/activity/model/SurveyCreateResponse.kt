package com.apollopharmacy.vishwam.ui.home.apna.activity.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class SurveyCreateResponse: Serializable {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data: Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("survey_id")
        @Expose
        var surveyId: String? = null

        @SerializedName("isUpdate")
        @Expose
        var isUpdate: Boolean? = null

        @SerializedName("isExecSuccess")
        @Expose
        var isExecSuccess: Boolean? = null

        @SerializedName("zcDebugLogs")
        @Expose
        var zcDebugLogs: ZcDebugLogs? = null

        class ZcDebugLogs: Serializable {
            @SerializedName("1")
            @Expose
            var x1: List<X1>? = null

            class X1: Serializable {
                @SerializedName("context")
                @Expose
                var context: String? = null

                @SerializedName("time")
                @Expose
                var time: String? = null

                @SerializedName("place")
                @Expose
                var place: String? = null

                @SerializedName("log")
                @Expose
                var log: String? = null
            }
        }
    }
}



//class SurveyCreateResponse {
//    @SerializedName("message")
//    @Expose
//    var message: String? = null
//
//    @SerializedName("success")
//    @Expose
//    var success: Boolean? = null
//
//    @SerializedName("data")
//    @Expose
//    var data: Data? = null
//
//    class Data {
//        @SerializedName("uid")
//        @Expose
//        var uid: String? = null
//
//        @SerializedName("isUpdate")
//        @Expose
//        var isUpdate: Boolean? = null
//
//        @SerializedName("survey_id")
//        @Expose
//        var surveyId: String? = null
//    }
//}