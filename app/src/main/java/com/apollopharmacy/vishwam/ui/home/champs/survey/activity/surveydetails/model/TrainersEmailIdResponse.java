package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


    public class TrainersEmailIdResponse implements Serializable
    {

        @SerializedName("message")
        @Expose
        private Object message;
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("data")
        @Expose
        private Data data;

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
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

        public class Data implements Serializable
        {

            @SerializedName("listData")
            @Expose
            private ListData listData;

            public ListData getListData() {
                return listData;
            }

            public void setListData(ListData listData) {
                this.listData = listData;
            }
            public class ListData implements Serializable
            {

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
                private Object zcExtra;
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

                public class Row implements Serializable
                {

                    @SerializedName("uid")
                    @Expose
                    private String uid;
                    @SerializedName("region")
                    @Expose
                    private Region region;
                    @SerializedName("trainer_email")
                    @Expose
                    private List<TrainerEmail> trainerEmail;

                    public String getUid() {
                        return uid;
                    }

                    public void setUid(String uid) {
                        this.uid = uid;
                    }

                    public Region getRegion() {
                        return region;
                    }

                    public void setRegion(Region region) {
                        this.region = region;
                    }

                    public class Region implements Serializable
                    {

                        @SerializedName("uid")
                        @Expose
                        private String uid;
                        @SerializedName("name")
                        @Expose
                        private String name;
                        private final static long serialVersionUID = 9218066914037698040L;

                        public String getUid() {
                            return uid;
                        }

                        public void setUid(String uid) {
                            this.uid = uid;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                    }


                    public List<TrainerEmail> getTrainerEmail() {
                        return trainerEmail;
                    }

                    public void setTrainerEmail(List<TrainerEmail> trainerEmail) {
                        this.trainerEmail = trainerEmail;
                    }
                    public class TrainerEmail implements Serializable
                    {

                        @SerializedName("uid")
                        @Expose
                        private String uid;
                        @SerializedName("email")
                        @Expose
                        private String email;

                        public String getUid() {
                            return uid;
                        }

                        public void setUid(String uid) {
                            this.uid = uid;
                        }

                        public String getEmail() {
                            return email;
                        }

                        public void setEmail(String email) {
                            this.email = email;
                        }

                    }

                }

                public Object getZcExtra() {
                    return zcExtra;
                }

                public void setZcExtra(Object zcExtra) {
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

            }


        }


    }


