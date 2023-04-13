package com.apollopharmacy.vishwam.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class GetTrainingAndColorDetailsModelResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("trainingDetails")
    @Expose
    private List<TrainingDetail> trainingDetails;


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

    public List<TrainingDetail> getTrainingDetails() {
        return trainingDetails;
    }

    public void setTrainingDetails(List<TrainingDetail> trainingDetails) {
        this.trainingDetails = trainingDetails;
    }

    public class TrainingDetail implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("length")
        @Expose
        private String length;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        private final static long serialVersionUID = 2035931023349423433L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }

}


