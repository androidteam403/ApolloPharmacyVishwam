package com.apollopharmacy.vishwam.ui.home.apnarectro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GetStorePendingAndApprovedListRes implements Serializable {

    @SerializedName("MESSAGE")
    @Expose
    private String message;
    @SerializedName("STATUS")
    @Expose
    private Boolean status;
    @SerializedName("getList")
    @Expose
    private List<Get> getList;

    private List<List<Get>> groupByRetrodList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Get> getGetList() {
        return getList;
    }

    public void setGetList(List<Get> getList) {
        this.getList = getList;
    }

    public List<List<Get>> getGroupByRetrodList() {
        return groupByRetrodList;
    }

    public void setGroupByRetrodList(List<List<Get>> groupByRetrodList) {
        this.groupByRetrodList = groupByRetrodList;
    }


    public static class Get implements Serializable {

        @SerializedName("RETROID")
        @Expose
        private String retroid;
        @SerializedName("STORE")
        @Expose
        private String store;
        @SerializedName("UPLOADED_BY")
        @Expose
        private String uploadedBy;
        @SerializedName("UPLOADED_DATE")
        @Expose
        private String uploadedDate;
        @SerializedName("APPROVED_BY")
        @Expose
        private String approvedBy;
        @SerializedName("APPROVED_DATE")
        @Expose
        private String approvedDate;
        @SerializedName("RESHOOT_BY")
        @Expose
        private String reshootBy;
        @SerializedName("RESHOOT_DATE")
        @Expose
        private String reshootDate;
        @SerializedName("PARTIALLY_APPROVED_BY")
        @Expose
        private String partiallyApprovedBy;
        @SerializedName("PARTIALLY_APPROVED_DATE")
        @Expose
        private String partiallyApprovedDate;
        @SerializedName("STATUS")
        @Expose
        private String status;
        @SerializedName("STAGE")
        @Expose
        private String stage;
        @SerializedName("HIERARCHYSTATUS")
        @Expose
        private String hierarchystatus;


        public String getRetroid() {
            return retroid;
        }

        public void setRetroid(String retroid) {
            this.retroid = retroid;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public String getUploadedBy() {
            return uploadedBy;
        }

        public void setUploadedBy(String uploadedBy) {
            this.uploadedBy = uploadedBy;
        }

        public String getUploadedDate() {
            return uploadedDate;
        }

        public void setUploadedDate(String uploadedDate) {
            this.uploadedDate = uploadedDate;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public String getApprovedDate() {
            return approvedDate;
        }

        public void setApprovedDate(String approvedDate) {
            this.approvedDate = approvedDate;
        }

        public String getReshootBy() {
            return reshootBy;
        }

        public void setReshootBy(String reshootBy) {
            this.reshootBy = reshootBy;
        }

        public String getReshootDate() {
            return reshootDate;
        }

        public void setReshootDate(String reshootDate) {
            this.reshootDate = reshootDate;
        }

        public String getPartiallyApprovedBy() {
            return partiallyApprovedBy;
        }

        public void setPartiallyApprovedBy(String partiallyApprovedBy) {
            this.partiallyApprovedBy = partiallyApprovedBy;
        }

        public String getPartiallyApprovedDate() {
            return partiallyApprovedDate;
        }

        public void setPartiallyApprovedDate(String partiallyApprovedDate) {
            this.partiallyApprovedDate = partiallyApprovedDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getHierarchystatus() {
            return hierarchystatus;
        }

        public void setHierarchystatus(String hierarchystatus) {
            this.hierarchystatus = hierarchystatus;
        }

    }

}




