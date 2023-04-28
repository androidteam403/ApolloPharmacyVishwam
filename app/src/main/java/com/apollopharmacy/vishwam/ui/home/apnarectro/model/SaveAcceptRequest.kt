package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveAcceptRequest : Serializable {
    @SerializedName("TYPE")
    @Expose
    var type: String? = null

    @SerializedName("RETROAUTOID")
    @Expose
    var retroautoid: String? = null

    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("STATUSID")
    @Expose
    var statusid: String? = null

    @SerializedName("STAGEID")
    @Expose
    var stageid: String? = null

    @SerializedName("REAMRKS")
    @Expose
    var reamrks: String? = null

    @SerializedName("RATING")
    @Expose
    var rating: String? = null

    @SerializedName("USERID")
    @Expose
    var userid: String? = null

    @SerializedName("IMAGEURLS")
    @Expose
    var imageurls: List<Imageurl>? = null
    fun withType(type: String?): SaveAcceptRequest {
        this.type = type
        return this
    }

    fun withRetroautoid(retroautoid: String?): SaveAcceptRequest {
        this.retroautoid = retroautoid
        return this
    }

    fun withStoreid(storeid: String?): SaveAcceptRequest {
        this.storeid = storeid
        return this
    }

    fun withStatusid(statusid: String?): SaveAcceptRequest {
        this.statusid = statusid
        return this
    }

    fun withStageid(stageid: String?): SaveAcceptRequest {
        this.stageid = stageid
        return this
    }

    fun withReamrks(reamrks: String?): SaveAcceptRequest {
        this.reamrks = reamrks
        return this
    }

    fun withRating(rating: String?): SaveAcceptRequest {
        this.rating = rating
        return this
    }

    fun withUserid(userid: String?): SaveAcceptRequest {
        this.userid = userid
        return this
    }

    fun withImageurls(imageurls: List<Imageurl>?): SaveAcceptRequest {
        this.imageurls = imageurls
        return this
    }

    public class Imageurl : Serializable {
        @SerializedName("IMAGEID")
        @Expose
        var imageid: String? = null

        @SerializedName("STATUSID")
        @Expose
        var statusid: String? = null
        fun withImageid(imageid: String?): Imageurl {
            this.imageid = imageid
            return this
        }

        fun withStatusid(statusid: String?): Imageurl {
            this.statusid = statusid
            return this
        }
    }
}