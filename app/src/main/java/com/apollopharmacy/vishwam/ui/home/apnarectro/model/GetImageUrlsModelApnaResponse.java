package com.apollopharmacy.vishwam.ui.home.apnarectro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



    public class GetImageUrlsModelApnaResponse implements Serializable
    {

        @SerializedName("MESSAGE")
        @Expose
        private String message;
        @SerializedName("STATUS")
        @Expose
        private Boolean status;
        @SerializedName("REMARKS")
        @Expose
        private List<Remark> remarks;
        @SerializedName("CATEGORY_LIST")
        @Expose
        private List<Category> categoryList;

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

        public List<Remark> getRemarks() {
            return remarks;
        }

        public void setRemarks(List<Remark> remarks) {
            this.remarks = remarks;
        }

        public class Remark implements Serializable
        {

            @SerializedName("RECID")
            @Expose
            private Integer recid;
            @SerializedName("RETRO_AUTO_ID")
            @Expose
            private String retroAutoId;
            @SerializedName("STORE_ID")
            @Expose
            private String storeId;
            @SerializedName("REMARKS")
            @Expose
            private String remarks;
            @SerializedName("RATING")
            @Expose
            private Integer rating;
            @SerializedName("CREATED_BY")
            @Expose
            private String createdBy;
            @SerializedName("CREATED_DATE")
            @Expose
            private String createdDate;
            @SerializedName("STATUS")
            @Expose
            private Object status;
            @SerializedName("STAGE")
            @Expose
            private Object stage;
            public Integer getRecid() {
                return recid;
            }

            public void setRecid(Integer recid) {
                this.recid = recid;
            }

            public String getRetroAutoId() {
                return retroAutoId;
            }

            public void setRetroAutoId(String retroAutoId) {
                this.retroAutoId = retroAutoId;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public Integer getRating() {
                return rating;
            }

            public void setRating(Integer rating) {
                this.rating = rating;
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

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
                this.status = status;
            }

            public Object getStage() {
                return stage;
            }

            public void setStage(Object stage) {
                this.stage = stage;
            }

        }

        public List<Category> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        public class Category implements Serializable
        {

            @SerializedName("CATEGORYID")
            @Expose
            private String categoryid;
            @SerializedName("CATEGORYNAME")
            @Expose
            private String categoryname;
            @SerializedName("IMAGE_URLS")
            @Expose
            private List<ImageUrl> imageUrls;

            private List<List<ImageUrl>> groupingImageUrlList;

            public List<List<ImageUrl>> getGroupingImageUrlList() {
                return groupingImageUrlList;
            }

            public void setGroupingImageUrlList(List<List<ImageUrl>> groupingImageUrlList) {
                this.groupingImageUrlList = groupingImageUrlList;
            }

            public String getCategoryid() {
                return categoryid;
            }

            public void setCategoryid(String categoryid) {
                this.categoryid = categoryid;
            }

            public String getCategoryname() {
                return categoryname;
            }

            public void setCategoryname(String categoryname) {
                this.categoryname = categoryname;
            }

            public List<ImageUrl> getImageUrls() {
                return imageUrls;
            }

            public void setImageUrls(List<ImageUrl> imageUrls) {
                this.imageUrls = imageUrls;
            }
            public class ImageUrl implements Serializable
            {

                @SerializedName("URL")
                @Expose
                private String url;
                @SerializedName("STATUS")
                @Expose
                private String status;
                @SerializedName("REMARKS")
                @Expose
                private Object remarks;
                @SerializedName("RETORAUTOID")
                @Expose
                private Integer retorautoid;
                @SerializedName("IMAGEID")
                @Expose
                private String imageid;
                @SerializedName("STAGE")
                @Expose
                private String stage;
                @SerializedName("CATEGORYID")
                @Expose
                private Integer categoryid;
                @SerializedName("POSITION")
                @Expose
                private Integer position;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Object getRemarks() {
                    return remarks;
                }

                public void setRemarks(Object remarks) {
                    this.remarks = remarks;
                }

                public Integer getRetorautoid() {
                    return retorautoid;
                }

                public void setRetorautoid(Integer retorautoid) {
                    this.retorautoid = retorautoid;
                }

                public String getImageid() {
                    return imageid;
                }

                public void setImageid(String imageid) {
                    this.imageid = imageid;
                }

                public String getStage() {
                    return stage;
                }

                public void setStage(String stage) {
                    this.stage = stage;
                }

                public Integer getCategoryid() {
                    return categoryid;
                }

                public void setCategoryid(Integer categoryid) {
                    this.categoryid = categoryid;
                }

                public Integer getPosition() {
                    return position;
                }

                public void setPosition(Integer position) {
                    this.position = position;
                }

            }

        }

    }
