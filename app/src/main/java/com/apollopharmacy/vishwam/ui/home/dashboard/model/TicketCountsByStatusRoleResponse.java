package com.apollopharmacy.vishwam.ui.home.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class TicketCountsByStatusRoleResponse implements Serializable {

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

    public String empId;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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
                @SerializedName("employee_id")
                @Expose
                private String employeeid;

                @SerializedName("role_code")
                @Expose
                private String roleCode;
                @SerializedName("role_name")
                @Expose
                private String role_name;

                @SerializedName("total")
                @Expose
                private Integer total;
                @SerializedName("closed")
                @Expose
                private Integer closed;
                @SerializedName("rejected")
                @Expose
                private Integer rejected;
                @SerializedName("less_than_2")
                @Expose
                private Integer lessThan2;
                @SerializedName("3_to_8")
                @Expose
                private Integer _3To8;
                @SerializedName("greater_than_8")
                @Expose
                private Integer greaterThan8;
                @SerializedName("pending")
                @Expose
                private Integer pending;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getEmployeeid() {
                    return employeeid;
                }

                public void setEmployeeid(String employeeid) {
                    this.employeeid = employeeid;
                }

                public String getRoleCode() {
                    return roleCode;
                }

                public void setRoleCode(String roleCode) {
                    this.roleCode = roleCode;
                }

                public String getRole_name() {
                    return role_name;
                }

                public void setRole_name(String role_name) {
                    this.role_name = role_name;
                }

                public Integer getTotal() {
                    return total;
                }

                public void setTotal(Integer total) {
                    this.total = total;
                }

                public Integer getClosed() {
                    return closed;
                }

                public void setClosed(Integer closed) {
                    this.closed = closed;
                }

                public Integer getRejected() {
                    return rejected;
                }

                public void setRejected(Integer rejected) {
                    this.rejected = rejected;
                }

                public Integer getLessThan2() {
                    return lessThan2;
                }

                public void setLessThan2(Integer lessThan2) {
                    this.lessThan2 = lessThan2;
                }

                public Integer get3To8() {
                    return _3To8;
                }

                public void set3To8(Integer _3To8) {
                    this._3To8 = _3To8;
                }

                public Integer getGreaterThan8() {
                    return greaterThan8;
                }

                public void setGreaterThan8(Integer greaterThan8) {
                    this.greaterThan8 = greaterThan8;
                }

                public Integer getPending() {
                    return pending;
                }

                public void setPending(Integer pending) {
                    this.pending = pending;
                }

            }

            public class ZcExtra implements Serializable {

                @SerializedName("data1")
                @Expose
                private Integer data1;

                public Integer getData1() {
                    return data1;
                }

                public void setData1(Integer data1) {
                    this.data1 = data1;
                }

            }

        }

    }

}












