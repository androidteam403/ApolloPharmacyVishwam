package com.apollopharmacy.vishwam.ui.home.qcfail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class Dummy {


    @SerializedName("STATUS")
    @Expose
    private Boolean status;
    @SerializedName("MESSAGE")
    @Expose
    private String message;
    @SerializedName("ITEMLIST")
    @Expose
    private List<Item> itemlist = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Dummy withStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Dummy withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<Item> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<Item> itemlist) {
        this.itemlist = itemlist;
    }

    public Dummy withItemlist(List<Item> itemlist) {
        this.itemlist = itemlist;
        return this;
    }


    public class Item {

        @SerializedName("RECID")
        @Expose
        private Integer recid;
        @SerializedName("STOREID")
        @Expose
        private Object storeid;
        @SerializedName("ORDERNO")
        @Expose
        private Object orderno;
        @SerializedName("STATUS")
        @Expose
        private Object status;
        @SerializedName("ITEMID")
        @Expose
        private String itemid;
        @SerializedName("ITEMNAME")
        @Expose
        private String itemname;
        @SerializedName("CATEGORY")
        @Expose
        private String category;
        @SerializedName("QTY")
        @Expose
        private Object qty;
        @SerializedName("PRICE")
        @Expose
        private Double price;
        @SerializedName("DISCAMOUNT")
        @Expose
        private Double discamount;
        @SerializedName("CREATEDDATE")
        @Expose
        private String createddate;
        @SerializedName("AUTOID")
        @Expose
        private Object autoid;
        @SerializedName("REMARKS")
        @Expose
        private Object remarks;
        @SerializedName("MODIFIEDBY")
        @Expose
        private Object modifiedby;
        @SerializedName("MODIFIEDDATE")
        @Expose
        private Object modifieddate;
        @SerializedName("OMSRETURNLINERECID")
        @Expose
        private Object omsreturnlinerecid;
        @SerializedName("PASSQTY")
        @Expose
        private Object passqty;
        @SerializedName("FAILQTY")
        @Expose
        private Object failqty;
        @SerializedName("APPROVEDQTY")
        @Expose
        private Object approvedqty;
        @SerializedName("DISCPERCENTAGE")
        @Expose
        private Object discpercentage;
        @SerializedName("NETAMOUNT")
        @Expose
        private Object netamount;
        @SerializedName("IMAGEURLS")
        @Expose
        private Object imageurls;

        public Integer getRecid() {
            return recid;
        }

        public void setRecid(Integer recid) {
            this.recid = recid;
        }

        public Item withRecid(Integer recid) {
            this.recid = recid;
            return this;
        }

        public Object getStoreid() {
            return storeid;
        }

        public void setStoreid(Object storeid) {
            this.storeid = storeid;
        }

        public Item withStoreid(Object storeid) {
            this.storeid = storeid;
            return this;
        }

        public Object getOrderno() {
            return orderno;
        }

        public void setOrderno(Object orderno) {
            this.orderno = orderno;
        }

        public Item withOrderno(Object orderno) {
            this.orderno = orderno;
            return this;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Item withStatus(Object status) {
            this.status = status;
            return this;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public Item withItemid(String itemid) {
            this.itemid = itemid;
            return this;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public Item withItemname(String itemname) {
            this.itemname = itemname;
            return this;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Item withCategory(String category) {
            this.category = category;
            return this;
        }

        public Object getQty() {
            return qty;
        }

        public void setQty(Object qty) {
            this.qty = qty;
        }

        public Item withQty(Object qty) {
            this.qty = qty;
            return this;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Item withPrice(Double price) {
            this.price = price;
            return this;
        }

        public Double getDiscamount() {
            return discamount;
        }

        public void setDiscamount(Double discamount) {
            this.discamount = discamount;
        }

        public Item withDiscamount(Double discamount) {
            this.discamount = discamount;
            return this;
        }

        public String getCreateddate() {
            return createddate;
        }

        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }

        public Item withCreateddate(String createddate) {
            this.createddate = createddate;
            return this;
        }

        public Object getAutoid() {
            return autoid;
        }

        public void setAutoid(Object autoid) {
            this.autoid = autoid;
        }

        public Item withAutoid(Object autoid) {
            this.autoid = autoid;
            return this;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Item withRemarks(Object remarks) {
            this.remarks = remarks;
            return this;
        }

        public Object getModifiedby() {
            return modifiedby;
        }

        public void setModifiedby(Object modifiedby) {
            this.modifiedby = modifiedby;
        }

        public Item withModifiedby(Object modifiedby) {
            this.modifiedby = modifiedby;
            return this;
        }

        public Object getModifieddate() {
            return modifieddate;
        }

        public void setModifieddate(Object modifieddate) {
            this.modifieddate = modifieddate;
        }

        public Item withModifieddate(Object modifieddate) {
            this.modifieddate = modifieddate;
            return this;
        }

        public Object getOmsreturnlinerecid() {
            return omsreturnlinerecid;
        }

        public void setOmsreturnlinerecid(Object omsreturnlinerecid) {
            this.omsreturnlinerecid = omsreturnlinerecid;
        }

        public Item withOmsreturnlinerecid(Object omsreturnlinerecid) {
            this.omsreturnlinerecid = omsreturnlinerecid;
            return this;
        }

        public Object getPassqty() {
            return passqty;
        }

        public void setPassqty(Object passqty) {
            this.passqty = passqty;
        }

        public Item withPassqty(Object passqty) {
            this.passqty = passqty;
            return this;
        }

        public Object getFailqty() {
            return failqty;
        }

        public void setFailqty(Object failqty) {
            this.failqty = failqty;
        }

        public Item withFailqty(Object failqty) {
            this.failqty = failqty;
            return this;
        }

        public Object getApprovedqty() {
            return approvedqty;
        }

        public void setApprovedqty(Object approvedqty) {
            this.approvedqty = approvedqty;
        }

        public Item withApprovedqty(Object approvedqty) {
            this.approvedqty = approvedqty;
            return this;
        }

        public Object getDiscpercentage() {
            return discpercentage;
        }

        public void setDiscpercentage(Object discpercentage) {
            this.discpercentage = discpercentage;
        }

        public Item withDiscpercentage(Object discpercentage) {
            this.discpercentage = discpercentage;
            return this;
        }

        public Object getNetamount() {
            return netamount;
        }

        public void setNetamount(Object netamount) {
            this.netamount = netamount;
        }

        public Item withNetamount(Object netamount) {
            this.netamount = netamount;
            return this;
        }

        public Object getImageurls() {
            return imageurls;
        }

        public void setImageurls(Object imageurls) {
            this.imageurls = imageurls;
        }

        public Item withImageurls(Object imageurls) {
            this.imageurls = imageurls;
            return this;
        }

    }
}
