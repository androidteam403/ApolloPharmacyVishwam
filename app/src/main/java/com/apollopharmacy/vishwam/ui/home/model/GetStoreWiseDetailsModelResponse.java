package com.apollopharmacy.vishwam.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetStoreWiseDetailsModelResponse implements Serializable {

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

    public GetStoreWiseDetailsModelResponse withMessage(Object message) {
        this.message = message;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public GetStoreWiseDetailsModelResponse withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public GetStoreWiseDetailsModelResponse withData(Data data) {
        this.data = data;
        return this;
    }


    public class Data implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("site")
        @Expose
        private String site;
        @SerializedName("executive")
        @Expose
        private Executive executive;
        @SerializedName("hod")
        @Expose
        private Hod hod;
        @SerializedName("manager")
        @Expose
        private Manager manager;
        @SerializedName("region_head")
        @Expose
        private RegionHead regionHead;
        @SerializedName("region")
        @Expose
        private Region region;
        @SerializedName("site_mngr_exec")
        @Expose
        private List<SiteMngrExec> siteMngrExec;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Data withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public Data withSite(String site) {
            this.site = site;
            return this;
        }

        public Executive getExecutive() {
            return executive;
        }

        public void setExecutive(Executive executive) {
            this.executive = executive;
        }

        public Data withExecutive(Executive executive) {
            this.executive = executive;
            return this;
        }

        public Hod getHod() {
            return hod;
        }

        public void setHod(Hod hod) {
            this.hod = hod;
        }

        public Data withHod(Hod hod) {
            this.hod = hod;
            return this;
        }

        public Manager getManager() {
            return manager;
        }

        public void setManager(Manager manager) {
            this.manager = manager;
        }

        public Data withManager(Manager manager) {
            this.manager = manager;
            return this;
        }

        public RegionHead getRegionHead() {
            return regionHead;
        }

        public void setRegionHead(RegionHead regionHead) {
            this.regionHead = regionHead;
        }

        public Data withRegionHead(RegionHead regionHead) {
            this.regionHead = regionHead;
            return this;
        }

        public List<SiteMngrExec> getSiteMngrExec() {
            return siteMngrExec;
        }

        public void setSiteMngrExec(List<SiteMngrExec> siteMngrExec) {
            this.siteMngrExec = siteMngrExec;
        }

        public Data withSiteMngrExec(List<SiteMngrExec> siteMngrExec) {
            this.siteMngrExec = siteMngrExec;
            return this;
        }

    }

    public class Executive implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("role")
        @Expose
        private Role role;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Executive withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Executive withEmail(String email) {
            this.email = email;
            return this;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Executive withRole(Role role) {
            this.role = role;
            return this;
        }

    }


    public class Hod implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("role")
        @Expose
        private Role__1 role;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Hod withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Hod withEmail(String email) {
            this.email = email;
            return this;
        }

        public Role__1 getRole() {
            return role;
        }

        public void setRole(Role__1 role) {
            this.role = role;
        }

        public Hod withRole(Role__1 role) {
            this.role = role;
            return this;
        }

    }

    public class Manager implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("role")
        @Expose
        private Role__2 role;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Manager withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Manager withEmail(String email) {
            this.email = email;
            return this;
        }

        public Role__2 getRole() {
            return role;
        }

        public void setRole(Role__2 role) {
            this.role = role;
        }

        public Manager withRole(Role__2 role) {
            this.role = role;
            return this;
        }

    }


    public class Region implements Serializable
    {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("code")
        @Expose
        private String code;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    public class RegionHead implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("role")
        @Expose
        private Role__3 role;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public RegionHead withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public RegionHead withEmail(String email) {
            this.email = email;
            return this;
        }

        public Role__3 getRole() {
            return role;
        }

        public void setRole(Role__3 role) {
            this.role = role;
        }

        public RegionHead withRole(Role__3 role) {
            this.role = role;
            return this;
        }

    }

    public class Role implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Role withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Role withName(String name) {
            this.name = name;
            return this;
        }

    }

    public class Role__1 implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Role__1 withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Role__1 withName(String name) {
            this.name = name;
            return this;
        }

    }

    public class Role__2 implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Role__2 withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Role__2 withName(String name) {
            this.name = name;
            return this;
        }

    }

    public class Role__3 implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Role__3 withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Role__3 withName(String name) {
            this.name = name;
            return this;
        }

    }

    public class Role__4 implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Role__4 withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Role__4 withName(String name) {
            this.name = name;
            return this;
        }

    }

    public class SiteMngrExec implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("role")
        @Expose
        private Role__4 role;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public SiteMngrExec withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public SiteMngrExec withEmail(String email) {
            this.email = email;
            return this;
        }

        public Role__4 getRole() {
            return role;
        }

        public void setRole(Role__4 role) {
            this.role = role;
        }

        public SiteMngrExec withRole(Role__4 role) {
            this.role = role;
            return this;
        }

    }
}

