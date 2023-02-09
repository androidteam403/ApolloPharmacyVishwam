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

    constructor(uid: String?, ticket: Ticket?) {
        this.uid = uid
        this.ticket = ticket
    }

    fun withUid(uid: String?): CmsTicketRequest {
        this.uid = uid
        return this
    }

    fun withTicket(ticket: Ticket?): CmsTicketRequest {
        this.ticket = ticket
        return this
    }

     class Ticket : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

         constructor(uid: String?) {
             this.uid = uid
         }

         fun withUid(uid: String?): Ticket {
            this.uid = uid
            return this
        }
    }
}