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

    public GetStorePendingAndApprovedListRes withMessage(String message) {
        this.message = message;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public GetStorePendingAndApprovedListRes withStatus(Boolean status) {
        this.status = status;
        return this;
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
    public GetStorePendingAndApprovedListRes withGetList(List<Get> getList) {
        this.getList = getList;
        return this;
    }


    public class Get implements Serializable {

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
        @SerializedName("EXECUTIVE_APPROVED_BY")
        @Expose
        private Object executiveApprovedBy;
        @SerializedName("EXECUTIVE_APPROVED_DATE")
        @Expose
        private Object executiveApprovedDate;
        @SerializedName("EXECUTIVE_RESHOOT_BY")
        @Expose
        private Object executiveReshootBy;
        @SerializedName("EXECUTIVE_RESHOOT_DATE")
        @Expose
        private Object executiveReshootDate;
        @SerializedName("MANAGER_APPROVED_BY")
        @Expose
        private Object managerApprovedBy;
        @SerializedName("MANAGER_APPROVED_DATE")
        @Expose
        private Object managerApprovedDate;
        @SerializedName("MANAGER_RESHOOT_BY")
        @Expose
        private Object managerReshootBy;
        @SerializedName("MANAGER_RESHOOT_DATE")
        @Expose
        private Object managerReshootDate;
        @SerializedName("GM_APPROVED_BY")
        @Expose
        private Object gmApprovedBy;
        @SerializedName("GM_APPROVED_DATE")
        @Expose
        private Object gmApprovedDate;
        @SerializedName("GM_RESHOOT_BY")
        @Expose
        private Object gmReshootBy;
        @SerializedName("GM_RESHOOT_DATE")
        @Expose
        private Object gmReshootDate;
        @SerializedName("CEO_APPROVED_BY")
        @Expose
        private Object ceoApprovedBy;
        @SerializedName("CEO_APPROVED_DATE")
        @Expose
        private Object ceoApprovedDate;
        @SerializedName("CEO_RESHOOT_BY")
        @Expose
        private Object ceoReshootBy;
        @SerializedName("CEO_RESHOOT_DATE")
        @Expose
        private Object ceoReshootDate;
        @SerializedName("STATUS")
        @Expose
        private String status;
        @SerializedName("STAGE")
        @Expose
        private String stage;
        @SerializedName("HIERARCHYSTATUS")
        @Expose
        private Object hierarchystatus;

        public String getRetroid() {
            return retroid;
        }

        public void setRetroid(String retroid) {
            this.retroid = retroid;
        }

        public Get withRetroid(String retroid) {
            this.retroid = retroid;
            return this;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public Get withStore(String store) {
            this.store = store;
            return this;
        }

        public String getUploadedBy() {
            return uploadedBy;
        }

        public void setUploadedBy(String uploadedBy) {
            this.uploadedBy = uploadedBy;
        }

        public Get withUploadedBy(String uploadedBy) {
            this.uploadedBy = uploadedBy;
            return this;
        }

        public String getUploadedDate() {
            return uploadedDate;
        }

        public void setUploadedDate(String uploadedDate) {
            this.uploadedDate = uploadedDate;
        }

        public Get withUploadedDate(String uploadedDate) {
            this.uploadedDate = uploadedDate;
            return this;
        }

        public Object getExecutiveApprovedBy() {
            return executiveApprovedBy;
        }

        public void setExecutiveApprovedBy(Object executiveApprovedBy) {
            this.executiveApprovedBy = executiveApprovedBy;
        }

        public Get withExecutiveApprovedBy(Object executiveApprovedBy) {
            this.executiveApprovedBy = executiveApprovedBy;
            return this;
        }

        public Object getExecutiveApprovedDate() {
            return executiveApprovedDate;
        }

        public void setExecutiveApprovedDate(Object executiveApprovedDate) {
            this.executiveApprovedDate = executiveApprovedDate;
        }

        public Get withExecutiveApprovedDate(Object executiveApprovedDate) {
            this.executiveApprovedDate = executiveApprovedDate;
            return this;
        }

        public Object getExecutiveReshootBy() {
            return executiveReshootBy;
        }

        public void setExecutiveReshootBy(Object executiveReshootBy) {
            this.executiveReshootBy = executiveReshootBy;
        }

        public Get withExecutiveReshootBy(Object executiveReshootBy) {
            this.executiveReshootBy = executiveReshootBy;
            return this;
        }

        public Object getExecutiveReshootDate() {
            return executiveReshootDate;
        }

        public void setExecutiveReshootDate(Object executiveReshootDate) {
            this.executiveReshootDate = executiveReshootDate;
        }

        public Get withExecutiveReshootDate(Object executiveReshootDate) {
            this.executiveReshootDate = executiveReshootDate;
            return this;
        }

        public Object getManagerApprovedBy() {
            return managerApprovedBy;
        }

        public void setManagerApprovedBy(Object managerApprovedBy) {
            this.managerApprovedBy = managerApprovedBy;
        }

        public Get withManagerApprovedBy(Object managerApprovedBy) {
            this.managerApprovedBy = managerApprovedBy;
            return this;
        }

        public Object getManagerApprovedDate() {
            return managerApprovedDate;
        }

        public void setManagerApprovedDate(Object managerApprovedDate) {
            this.managerApprovedDate = managerApprovedDate;
        }

        public Get withManagerApprovedDate(Object managerApprovedDate) {
            this.managerApprovedDate = managerApprovedDate;
            return this;
        }

        public Object getManagerReshootBy() {
            return managerReshootBy;
        }

        public void setManagerReshootBy(Object managerReshootBy) {
            this.managerReshootBy = managerReshootBy;
        }

        public Get withManagerReshootBy(Object managerReshootBy) {
            this.managerReshootBy = managerReshootBy;
            return this;
        }

        public Object getManagerReshootDate() {
            return managerReshootDate;
        }

        public void setManagerReshootDate(Object managerReshootDate) {
            this.managerReshootDate = managerReshootDate;
        }

        public Get withManagerReshootDate(Object managerReshootDate) {
            this.managerReshootDate = managerReshootDate;
            return this;
        }

        public Object getGmApprovedBy() {
            return gmApprovedBy;
        }

        public void setGmApprovedBy(Object gmApprovedBy) {
            this.gmApprovedBy = gmApprovedBy;
        }

        public Get withGmApprovedBy(Object gmApprovedBy) {
            this.gmApprovedBy = gmApprovedBy;
            return this;
        }

        public Object getGmApprovedDate() {
            return gmApprovedDate;
        }

        public void setGmApprovedDate(Object gmApprovedDate) {
            this.gmApprovedDate = gmApprovedDate;
        }

        public Get withGmApprovedDate(Object gmApprovedDate) {
            this.gmApprovedDate = gmApprovedDate;
            return this;
        }

        public Object getGmReshootBy() {
            return gmReshootBy;
        }

        public void setGmReshootBy(Object gmReshootBy) {
            this.gmReshootBy = gmReshootBy;
        }

        public Get withGmReshootBy(Object gmReshootBy) {
            this.gmReshootBy = gmReshootBy;
            return this;
        }

        public Object getGmReshootDate() {
            return gmReshootDate;
        }

        public void setGmReshootDate(Object gmReshootDate) {
            this.gmReshootDate = gmReshootDate;
        }

        public Get withGmReshootDate(Object gmReshootDate) {
            this.gmReshootDate = gmReshootDate;
            return this;
        }

        public Object getCeoApprovedBy() {
            return ceoApprovedBy;
        }

        public void setCeoApprovedBy(Object ceoApprovedBy) {
            this.ceoApprovedBy = ceoApprovedBy;
        }

        public Get withCeoApprovedBy(Object ceoApprovedBy) {
            this.ceoApprovedBy = ceoApprovedBy;
            return this;
        }

        public Object getCeoApprovedDate() {
            return ceoApprovedDate;
        }

        public void setCeoApprovedDate(Object ceoApprovedDate) {
            this.ceoApprovedDate = ceoApprovedDate;
        }

        public Get withCeoApprovedDate(Object ceoApprovedDate) {
            this.ceoApprovedDate = ceoApprovedDate;
            return this;
        }

        public Object getCeoReshootBy() {
            return ceoReshootBy;
        }

        public void setCeoReshootBy(Object ceoReshootBy) {
            this.ceoReshootBy = ceoReshootBy;
        }

        public Get withCeoReshootBy(Object ceoReshootBy) {
            this.ceoReshootBy = ceoReshootBy;
            return this;
        }

        public Object getCeoReshootDate() {
            return ceoReshootDate;
        }

        public void setCeoReshootDate(Object ceoReshootDate) {
            this.ceoReshootDate = ceoReshootDate;
        }

        public Get withCeoReshootDate(Object ceoReshootDate) {
            this.ceoReshootDate = ceoReshootDate;
            return this;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Get withStatus(String status) {
            this.status = status;
            return this;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public Get withStage(String stage) {
            this.stage = stage;
            return this;
        }

        public Object getHierarchystatus() {
            return hierarchystatus;
        }

        public void setHierarchystatus(Object hierarchystatus) {
            this.hierarchystatus = hierarchystatus;
        }

        public Get withHierarchystatus(Object hierarchystatus) {
            this.hierarchystatus = hierarchystatus;
            return this;
        }
    }
}


