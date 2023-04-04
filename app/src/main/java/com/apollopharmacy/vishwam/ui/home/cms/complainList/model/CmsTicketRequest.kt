package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CmsTicketRequest : Serializable {
    @SerializedName("uid")
    @Expose
    var uid: String? = null

    @SerializedName("ticket")
    @Expose
    var ticket: Ticket? = null
    fun withUid(uid: String?): CmsTicketRequest {
        this.uid = uid
        return this
    }

    fun withTicket(ticket: Ticket?): CmsTicketRequest {
        this.ticket = ticket
        return this
    }

    public class Ticket : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): Ticket {
            this.uid = uid
            return this
        }
    }
}