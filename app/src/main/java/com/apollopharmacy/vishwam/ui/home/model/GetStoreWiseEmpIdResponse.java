package com.apollopharmacy.vishwam.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class GetStoreWiseEmpIdResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("StoreWiseDetails")
    @Expose
    private StoreWiseDetails storeWiseDetails;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StoreWiseDetails getStoreWiseDetails() {
        return storeWiseDetails;
    }

    public void setStoreWiseDetails(StoreWiseDetails storeWiseDetails) {
        this.storeWiseDetails = storeWiseDetails;
    }
    public class StoreWiseDetails implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("store_id")
        @Expose
        private String storeId;
        @SerializedName("executive_id")
        @Expose
        private String executiveId;
        @SerializedName("executive_name")
        @Expose
        private String executiveName;
        @SerializedName("executive_email")
        @Expose
        private String executiveEmail;
        @SerializedName("manager_id")
        @Expose
        private String managerId;
        @SerializedName("manager_name")
        @Expose
        private String managerName;
        @SerializedName("manager_email")
        @Expose
        private String managerEmail;
        @SerializedName("reagional_head_id")
        @Expose
        private String reagionalHeadId;
        @SerializedName("reagional_head_name")
        @Expose
        private String reagionalHeadName;
        @SerializedName("reagional_head_email")
        @Expose
        private String reagionalHeadEmail;
        @SerializedName("trainer_id")
        @Expose
        private String trainerId;
        @SerializedName("trainer_name")
        @Expose
        private String trainerName;
        @SerializedName("trainer_email")
        @Expose
        private String trainerEmail;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("created_date")
        @Expose
        private String createdDate;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getExecutiveId() {
            return executiveId;
        }

        public void setExecutiveId(String executiveId) {
            this.executiveId = executiveId;
        }

        public String getExecutiveName() {
            return executiveName;
        }

        public void setExecutiveName(String executiveName) {
            this.executiveName = executiveName;
        }

        public String getExecutiveEmail() {
            return executiveEmail;
        }

        public void setExecutiveEmail(String executiveEmail) {
            this.executiveEmail = executiveEmail;
        }

        public String getManagerId() {
            return managerId;
        }

        public void setManagerId(String managerId) {
            this.managerId = managerId;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

        public String getManagerEmail() {
            return managerEmail;
        }

        public void setManagerEmail(String managerEmail) {
            this.managerEmail = managerEmail;
        }

        public String getReagionalHeadId() {
            return reagionalHeadId;
        }

        public void setReagionalHeadId(String reagionalHeadId) {
            this.reagionalHeadId = reagionalHeadId;
        }

        public String getReagionalHeadName() {
            return reagionalHeadName;
        }

        public void setReagionalHeadName(String reagionalHeadName) {
            this.reagionalHeadName = reagionalHeadName;
        }

        public String getReagionalHeadEmail() {
            return reagionalHeadEmail;
        }

        public void setReagionalHeadEmail(String reagionalHeadEmail) {
            this.reagionalHeadEmail = reagionalHeadEmail;
        }

        public String getTrainerId() {
            return trainerId;
        }

        public void setTrainerId(String trainerId) {
            this.trainerId = trainerId;
        }

        public String getTrainerName() {
            return trainerName;
        }

        public void setTrainerName(String trainerName) {
            this.trainerName = trainerName;
        }

        public String getTrainerEmail() {
            return trainerEmail;
        }

        public void setTrainerEmail(String trainerEmail) {
            this.trainerEmail = trainerEmail;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }
}




