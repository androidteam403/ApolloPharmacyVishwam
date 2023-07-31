package com.apollopharmacy.vishwam.ui.home.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReasonWiseTicketCountByRoleResponse implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        @SerializedName("listData")
        @Expose
        private ListData listData;

        public ListData getListData() {
            return listData;
        }

        public void setListData(ListData listData) {
            this.listData = listData;
        }

        public class ListData implements Serializable {
            @SerializedName("records")
            @Expose
            private String records;
            @SerializedName("select")
            @Expose
            private Boolean select;
            @SerializedName("total")
            @Expose
            private Integer total;
            @SerializedName("page")
            @Expose
            private Integer page;
            @SerializedName("rows")
            @Expose
            private List<Row> rows;
            @SerializedName("zc_extra")
            @Expose
            private ZcExtra zcExtra;
            @SerializedName("pivotData")
            @Expose
            private Object pivotData;
            @SerializedName("aggregation")
            @Expose
            private Object aggregation;
            @SerializedName("size")
            @Expose
            private Integer size;

            public String getRecords() {
                return records;
            }

            public void setRecords(String records) {
                this.records = records;
            }

            public Boolean getSelect() {
                return select;
            }

            public void setSelect(Boolean select) {
                this.select = select;
            }

            public Integer getTotal() {
                return total;
            }

            public void setTotal(Integer total) {
                this.total = total;
            }

            public Integer getPage() {
                return page;
            }

            public void setPage(Integer page) {
                this.page = page;
            }

            public List<Row> getRows() {
                return rows;
            }

            public void setRows(List<Row> rows) {
                this.rows = rows;
            }

            public ZcExtra getZcExtra() {
                return zcExtra;
            }

            public void setZcExtra(ZcExtra zcExtra) {
                this.zcExtra = zcExtra;
            }

            public Object getPivotData() {
                return pivotData;
            }

            public void setPivotData(Object pivotData) {
                this.pivotData = pivotData;
            }

            public Object getAggregation() {
                return aggregation;
            }

            public void setAggregation(Object aggregation) {
                this.aggregation = aggregation;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public class Row implements Serializable {
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("Data is irrevalant")
                @Expose
                private Integer dataIsIrrevalant;
                @SerializedName("Brand Image")
                @Expose
                private Integer brandImage;
                @SerializedName("Offline billing amount mismatched")
                @Expose
                private Integer offlineBillingAmountMismatched;
                @SerializedName("Poor Customer Service Experiences")
                @Expose
                private Integer poorCustomerServiceExperiences;
                @SerializedName("Dates expire data mismatched")
                @Expose
                private Integer datesExpireDataMismatched;
                @SerializedName("sample")
                @Expose
                private Integer sample;
                @SerializedName("Acessories online sales")
                @Expose
                private Integer acessoriesOnlineSales;
                @SerializedName("Billing software not working properly")
                @Expose
                private Integer billingSoftwareNotWorkingProperly;
                @SerializedName("Digital Marketing")
                @Expose
                private Integer digitalMarketing;
                @SerializedName("Total")
                private Integer total;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Integer getDataIsIrrevalant() {
                    return dataIsIrrevalant;
                }

                public void setDataIsIrrevalant(Integer dataIsIrrevalant) {
                    this.dataIsIrrevalant = dataIsIrrevalant;
                }

                public Integer getBrandImage() {
                    return brandImage;
                }

                public void setBrandImage(Integer brandImage) {
                    this.brandImage = brandImage;
                }

                public Integer getOfflineBillingAmountMismatched() {
                    return offlineBillingAmountMismatched;
                }

                public void setOfflineBillingAmountMismatched(Integer offlineBillingAmountMismatched) {
                    this.offlineBillingAmountMismatched = offlineBillingAmountMismatched;
                }

                public Integer getPoorCustomerServiceExperiences() {
                    return poorCustomerServiceExperiences;
                }

                public void setPoorCustomerServiceExperiences(Integer poorCustomerServiceExperiences) {
                    this.poorCustomerServiceExperiences = poorCustomerServiceExperiences;
                }

                public Integer getDatesExpireDataMismatched() {
                    return datesExpireDataMismatched;
                }

                public void setDatesExpireDataMismatched(Integer datesExpireDataMismatched) {
                    this.datesExpireDataMismatched = datesExpireDataMismatched;
                }

                public Integer getSample() {
                    return sample;
                }

                public void setSample(Integer sample) {
                    this.sample = sample;
                }

                public Integer getAcessoriesOnlineSales() {
                    return acessoriesOnlineSales;
                }

                public void setAcessoriesOnlineSales(Integer acessoriesOnlineSales) {
                    this.acessoriesOnlineSales = acessoriesOnlineSales;
                }

                public Integer getBillingSoftwareNotWorkingProperly() {
                    return billingSoftwareNotWorkingProperly;
                }

                public void setBillingSoftwareNotWorkingProperly(Integer billingSoftwareNotWorkingProperly) {
                    this.billingSoftwareNotWorkingProperly = billingSoftwareNotWorkingProperly;
                }

                public Integer getDigitalMarketing() {
                    return digitalMarketing;
                }

                public void setDigitalMarketing(Integer digitalMarketing) {
                    this.digitalMarketing = digitalMarketing;
                }

                public Integer getTotal() {
                    return total;
                }

                public void setTotal(Integer total) {
                    this.total = total;
                }
            }

            public class ZcExtra implements Serializable {
                @SerializedName("data1")
                @Expose
                private List<Data1> data1 = new ArrayList<Data1>();
                @SerializedName("data2")
                @Expose
                private Integer data2;

                public List<Data1> getData1() {
                    return data1;
                }

                public void setData1(List<Data1> data1) {
                    this.data1 = data1;
                }

                public Integer getData2() {
                    return data2;
                }

                public void setData2(Integer data2) {
                    this.data2 = data2;
                }

                public class Data1 implements Serializable {
                    @SerializedName("name")
                    @Expose
                    private String name;
                    @SerializedName("code")
                    @Expose
                    private String code;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }
                }
            }
        }
    }
}