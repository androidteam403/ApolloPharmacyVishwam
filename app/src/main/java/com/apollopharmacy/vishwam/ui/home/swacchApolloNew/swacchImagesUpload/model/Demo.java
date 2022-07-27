package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Demo implements Serializable {

    @SerializedName("ACTION_EVENT")
    @Expose
    private String actionEvent;
    @SerializedName("STORE_ID")
    @Expose
    private String storeId;
    @SerializedName("USERID_ID")
    @Expose
    private String useridId;
    @SerializedName("CATEGORY_ID")
    @Expose
    private String categoryId;
    @SerializedName("IMAGE_URLS")
    @Expose
    private List<ImageUrl> imageUrls = null;

    public String getActionEvent() {
        return actionEvent;
    }

    public void setActionEvent(String actionEvent) {
        this.actionEvent = actionEvent;
    }

    public Demo withActionEvent(String actionEvent) {
        this.actionEvent = actionEvent;
        return this;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Demo withStoreId(String storeId) {
        this.storeId = storeId;
        return this;
    }

    public String getUseridId() {
        return useridId;
    }

    public void setUseridId(String useridId) {
        this.useridId = useridId;
    }

    public Demo withUseridId(String useridId) {
        this.useridId = useridId;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Demo withCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public List<ImageUrl> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageUrl> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Demo withImageUrls(List<ImageUrl> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }


    public class ImageUrl implements Serializable {

        @SerializedName("URL")
        @Expose
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ImageUrl withUrl(String url) {
            this.url = url;
            return this;
        }

    }
}
