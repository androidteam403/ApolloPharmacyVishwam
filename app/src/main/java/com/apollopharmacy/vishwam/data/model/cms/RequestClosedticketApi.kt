package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RequestClosedticketApi(
    @field:SerializedName("feedback")
    var feedback: Feedback,

    @field:SerializedName("comment")
    var comment: String? = null,

    @field:SerializedName("uid")
    var uid: String? = null,

    @field:SerializedName("status")
    var status: Status,

    @field:SerializedName("action")
    var action: Action,

    @field:SerializedName("level")
    var level: Level,

    @field:SerializedName("ticket_id")
    var ticket_id: String? = null,

    @field:SerializedName("user")
    var user: User,

    @field:SerializedName("action_name")
    var action_name: String? = null,

    @field:SerializedName("session_user")
    var session_user: SessionUser,

    @field:SerializedName("site")
    var site: Site,
) : Serializable {

    data class Feedback(
        @field:SerializedName("rating")
        var rating: Rating,
    ) : Serializable

    data class Rating(

        @field:SerializedName("uid")
        var uid: String? = null,

        ) : Serializable

    data class Status(
        @field:SerializedName("uid")
        var uid: String? = null,

        @field:SerializedName("code")
        var code: String? = null,

    ) : Serializable

    data class Action(
        @field:SerializedName("uid")
        var uid: String? = null,

        @field:SerializedName("code")
        var code: String? = null,
    ) : Serializable

    data class Level(

        @field:SerializedName("uid")
        var uid: String? = null,

        ) : Serializable

    data class User(
        @field:SerializedName("uid")
        var uid: String? = null,
    ) : Serializable

    data class SessionUser(
        @field:SerializedName("uid")
        var uid: String? = null,

        @field:SerializedName("name")
        var name: String? = null,

        @field:SerializedName("login_unique")
        var login_unique: String? = null,
    ) : Serializable

    data class Site(
        @field:SerializedName("uid")
        var uid: String? = null,
    ) : Serializable


}