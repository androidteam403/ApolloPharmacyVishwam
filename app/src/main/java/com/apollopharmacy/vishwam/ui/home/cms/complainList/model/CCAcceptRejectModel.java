package com.apollopharmacy.vishwam.ui.home.cms.complainList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class CCAcceptRejectModel implements Serializable {


    @Expose
    @SerializedName("action")
    private String action;
    @Expose
    @SerializedName("ticket_id")
    private String ticket_id;
    @Expose
    @SerializedName("employee_id")
    private String employee_id;
    @Expose
    @SerializedName("pos_status")
    private String pos_status;
    @Expose
    @SerializedName("ticket_it")
    private Ticket_it ticket_it;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("uid")
    private String uid;
    @Expose
    @SerializedName("comment")
    private String comment;

    public void setAction(String action) {
        this.action = action;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public void setPos_status(String pos_status) {
        this.pos_status = pos_status;
    }

    public void setTicket_it(Ticket_it ticket_it) {
        this.ticket_it = ticket_it;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class Ticket_it implements Serializable {
        @Expose
        @SerializedName("status.uid")
        private String uid;

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
