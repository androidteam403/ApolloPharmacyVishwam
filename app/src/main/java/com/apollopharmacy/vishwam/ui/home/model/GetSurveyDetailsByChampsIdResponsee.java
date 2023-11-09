package com.apollopharmacy.vishwam.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

    public class GetSurveyDetailsByChampsIdResponsee implements Serializable {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("headerDetails")
        @Expose
        private HeaderDetails headerDetails;
        @SerializedName("categoryDetails")
        @Expose
        private List<CategoryDetail> categoryDetails;
        @SerializedName("ImageDetails")
        @Expose
        private List<ImageDetail> imageDetails;

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

        public HeaderDetails getHeaderDetails() {
            return headerDetails;
        }

        public void setHeaderDetails(HeaderDetails headerDetails) {
            this.headerDetails = headerDetails;
        }

        public class HeaderDetails implements Serializable {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("champ_auto_id")
            @Expose
            private String champAutoId;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("store_id")
            @Expose
            private String storeId;
            @SerializedName("date_of_visit")
            @Expose
            private String dateOfVisit;
            @SerializedName("email_id_of_trainer")
            @Expose
            private String emailIdOfTrainer;
            @SerializedName("email_id_of_executive")
            @Expose
            private String emailIdOfExecutive;
            @SerializedName("email_id_of_manager")
            @Expose
            private String emailIdOfManager;
            @SerializedName("email_id_of_regional_head")
            @Expose
            private String emailIdOfRegionalHead;
            @SerializedName("email_id_of_recipients")
            @Expose
            private String emailIdOfRecipients;
            @SerializedName("email_id_of_cc")
            @Expose
            private String emailIdOfCc;
            @SerializedName("techinal_details")
            @Expose
            private String techinalDetails;
            @SerializedName("soft_skills")
            @Expose
            private String softSkills;
            @SerializedName("other_training")
            @Expose
            private String otherTraining;
            @SerializedName("issues_to_be_resolved")
            @Expose
            private String issuesToBeResolved;
            @SerializedName("total")
            @Expose
            private String total;
            @SerializedName("created_by")
            @Expose
            private String createdBy;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("site_name")
            @Expose
            private Object siteName;
            private final static long serialVersionUID = -8177522428201081838L;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getChampAutoId() {
                return champAutoId;
            }

            public void setChampAutoId(String champAutoId) {
                this.champAutoId = champAutoId;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getDateOfVisit() {
                return dateOfVisit;
            }

            public void setDateOfVisit(String dateOfVisit) {
                this.dateOfVisit = dateOfVisit;
            }

            public String getEmailIdOfTrainer() {
                return emailIdOfTrainer;
            }

            public void setEmailIdOfTrainer(String emailIdOfTrainer) {
                this.emailIdOfTrainer = emailIdOfTrainer;
            }

            public String getEmailIdOfExecutive() {
                return emailIdOfExecutive;
            }

            public void setEmailIdOfExecutive(String emailIdOfExecutive) {
                this.emailIdOfExecutive = emailIdOfExecutive;
            }

            public String getEmailIdOfManager() {
                return emailIdOfManager;
            }

            public void setEmailIdOfManager(String emailIdOfManager) {
                this.emailIdOfManager = emailIdOfManager;
            }

            public String getEmailIdOfRegionalHead() {
                return emailIdOfRegionalHead;
            }

            public void setEmailIdOfRegionalHead(String emailIdOfRegionalHead) {
                this.emailIdOfRegionalHead = emailIdOfRegionalHead;
            }

            public String getEmailIdOfRecipients() {
                return emailIdOfRecipients;
            }

            public void setEmailIdOfRecipients(String emailIdOfRecipients) {
                this.emailIdOfRecipients = emailIdOfRecipients;
            }

            public String getEmailIdOfCc() {
                return emailIdOfCc;
            }

            public void setEmailIdOfCc(String emailIdOfCc) {
                this.emailIdOfCc = emailIdOfCc;
            }

            public String getTechinalDetails() {
                return techinalDetails;
            }

            public void setTechinalDetails(String techinalDetails) {
                this.techinalDetails = techinalDetails;
            }

            public String getSoftSkills() {
                return softSkills;
            }

            public void setSoftSkills(String softSkills) {
                this.softSkills = softSkills;
            }

            public String getOtherTraining() {
                return otherTraining;
            }

            public void setOtherTraining(String otherTraining) {
                this.otherTraining = otherTraining;
            }

            public String getIssuesToBeResolved() {
                return issuesToBeResolved;
            }

            public void setIssuesToBeResolved(String issuesToBeResolved) {
                this.issuesToBeResolved = issuesToBeResolved;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Object getSiteName() {
                return siteName;
            }

            public void setSiteName(Object siteName) {
                this.siteName = siteName;
            }

        }

        public List<CategoryDetail> getCategoryDetails() {
            return categoryDetails;
        }

        public void setCategoryDetails(List<CategoryDetail> categoryDetails) {
            this.categoryDetails = categoryDetails;
        }

        public class CategoryDetail implements Serializable {

            @SerializedName("RECID")
            @Expose
            private Integer recid;
            @SerializedName("CHAMPS_ID")
            @Expose
            private String champsId;
            @SerializedName("CATEGORY_ID")
            @Expose
            private Integer categoryId;
            @SerializedName("SUBCATEGORY_ID")
            @Expose
            private Integer subcategoryId;
            @SerializedName("VALUE")
            @Expose
            private Double value;

            public Integer getRecid() {
                return recid;
            }

            public void setRecid(Integer recid) {
                this.recid = recid;
            }

            public String getChampsId() {
                return champsId;
            }

            public void setChampsId(String champsId) {
                this.champsId = champsId;
            }

            public Integer getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(Integer categoryId) {
                this.categoryId = categoryId;
            }

            public Integer getSubcategoryId() {
                return subcategoryId;
            }

            public void setSubcategoryId(Integer subcategoryId) {
                this.subcategoryId = subcategoryId;
            }

            public Double getValue() {
                return value;
            }

            public void setValue(Double value) {
                this.value = value;
            }

        }

        public List<ImageDetail> getImageDetails() {
            return imageDetails;
        }

        public void setImageDetails(List<ImageDetail> imageDetails) {
            this.imageDetails = imageDetails;
        }

        public class ImageDetail implements Serializable {

            @SerializedName("RECID")
            @Expose
            private Integer recid;
            @SerializedName("CHAMPS_ID")
            @Expose
            private String champsId;
            @SerializedName("CATEGORY_ID")
            @Expose
            private Integer categoryId;
            @SerializedName("IMAGE_URL")
            @Expose
            private String imageUrl;
            private final static long serialVersionUID = -5990378643898644835L;

            public Integer getRecid() {
                return recid;
            }

            public void setRecid(Integer recid) {
                this.recid = recid;
            }

            public String getChampsId() {
                return champsId;
            }

            public void setChampsId(String champsId) {
                this.champsId = champsId;
            }

            public Integer getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(Integer categoryId) {
                this.categoryId = categoryId;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

        }
    }

