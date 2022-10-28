package com.apollopharmacy.vishwam.ui.home.cms.registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FetchItemModel {


    @Expose
    @SerializedName("zcServerHost")
    private String zcServerHost;
    @Expose
    @SerializedName("zcServerIp")
    private String zcServerIp;
    @Expose
    @SerializedName("zcServerDateTime")
    private String zcServerDateTime;
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("success")
    private boolean success;

    public String getZcServerHost() {
        return zcServerHost;
    }

    public String getZcServerIp() {
        return zcServerIp;
    }

    public String getZcServerDateTime() {
        return zcServerDateTime;
    }

    public Data getData() {
        return data;
    }

    public boolean getSuccess() {
        return success;
    }

    public static class Data {
        @Expose
        @SerializedName("listData")
        private ListData listData;

        public ListData getListData() {
            return listData;
        }
    }

    public static class ListData {
        @Expose
        @SerializedName("size")
        private int size;
        @Expose
        @SerializedName("rows")
        private ArrayList<Rows> rows;
        @Expose
        @SerializedName("page")
        private int page;
        @Expose
        @SerializedName("total")
        private int total;
        @Expose
        @SerializedName("select")
        private boolean select;
        @Expose
        @SerializedName("records")
        private String records;

        public int getSize() {
            return size;
        }

        public ArrayList<Rows> getRows() {
            return rows;
        }

        public int getPage() {
            return page;
        }

        public int getTotal() {
            return total;
        }

        public boolean getSelect() {
            return select;
        }

        public String getRecords() {
            return records;
        }
    }

    public static class Rows {
        @Expose
        @SerializedName("artCodeName")
        private String artCodeName;
        @Expose
        @SerializedName("artcode")
        private String artcode;

        public String getArtCodeName() {
            return artCodeName;
        }

        public String getArtcode() {
            return artcode;
        }
    }
}
