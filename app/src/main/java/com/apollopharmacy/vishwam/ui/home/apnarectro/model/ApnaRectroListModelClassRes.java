package com.apollopharmacy.vishwam.ui.home.apnarectro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ApnaRectroListModelClassRes {

    public class Example implements Serializable
    {

        @SerializedName("MESSAGE")
        @Expose
        private String message;
        @SerializedName("STATUS")
        @Expose
        private Boolean status;
        @SerializedName("GET_APNA_RETRO_LIST")
        @Expose
        private List<GetApnaRetro> getApnaRetroList;

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

        public List<GetApnaRetro> getGetApnaRetroList() {
            return getApnaRetroList;
        }

        public void setGetApnaRetroList(List<GetApnaRetro> getApnaRetroList) {
            this.getApnaRetroList = getApnaRetroList;
        }

        public class GetApnaRetro implements Serializable
        {

            @SerializedName("APNA_RETRO_ID")
            @Expose
            private String apnaRetroId;
            @SerializedName("STORE_ID")
            @Expose
            private String storeId;
            @SerializedName("STAGE")
            @Expose
            private String stage;
            @SerializedName("PRE_RETRO")
            @Expose
            private PreRetro preRetro;
            @SerializedName("POST_RETRO")
            @Expose
            private PostRetro postRetro;
            @SerializedName("AFTER_COMPLETION")
            @Expose
            private AfterCompletion afterCompletion;

            public String getApnaRetroId() {
                return apnaRetroId;
            }

            public void setApnaRetroId(String apnaRetroId) {
                this.apnaRetroId = apnaRetroId;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getStage() {
                return stage;
            }

            public void setStage(String stage) {
                this.stage = stage;
            }

            public PreRetro getPreRetro() {
                return preRetro;
            }

            public void setPreRetro(PreRetro preRetro) {
                this.preRetro = preRetro;
            }

            public PostRetro getPostRetro() {
                return postRetro;
            }

            public void setPostRetro(PostRetro postRetro) {
                this.postRetro = postRetro;
            }

            public AfterCompletion getAfterCompletion() {
                return afterCompletion;
            }

            public void setAfterCompletion(AfterCompletion afterCompletion) {
                this.afterCompletion = afterCompletion;
            }
            public class AfterCompletion implements Serializable
            {

                @SerializedName("AFTER_COMPLETION_UPLOADED_BY")
                @Expose
                private String afterCompletionUploadedBy;
                @SerializedName("AFTER_COMPLETION_APPROVED_BY")
                @Expose
                private String afterCompletionApprovedBy;
                @SerializedName("AFTER_COMPLETION_RESHOOT_BY")
                @Expose
                private String afterCompletionReshootBy;
                @SerializedName("AFTER_COMPLETION_PARTIALLY_APPROVED_BY")
                @Expose
                private String afterCompletionPartiallyApprovedBy;
                @SerializedName("AFTER_COMPLETION_UPLOADED_DATE")
                @Expose
                private String afterCompletionUploadedDate;
                @SerializedName("AFTER_COMPLETION_APPROVED_DATE")
                @Expose
                private String afterCompletionApprovedDate;
                @SerializedName("AFTER_COMPLETION_RESHOOT_DATE")
                @Expose
                private String afterCompletionReshootDate;
                @SerializedName("AFTER_COMPLETION_PARTIALLY_APPROVED_DATE")
                @Expose
                private String afterCompletionPartiallyApprovedDate;
                @SerializedName("AFTER_RETRO_STATUS")
                @Expose
                private String afterRetroStatus;

                public String getAfterCompletionUploadedBy() {
                    return afterCompletionUploadedBy;
                }

                public void setAfterCompletionUploadedBy(String afterCompletionUploadedBy) {
                    this.afterCompletionUploadedBy = afterCompletionUploadedBy;
                }

                public String getAfterCompletionApprovedBy() {
                    return afterCompletionApprovedBy;
                }

                public void setAfterCompletionApprovedBy(String afterCompletionApprovedBy) {
                    this.afterCompletionApprovedBy = afterCompletionApprovedBy;
                }

                public String getAfterCompletionReshootBy() {
                    return afterCompletionReshootBy;
                }

                public void setAfterCompletionReshootBy(String afterCompletionReshootBy) {
                    this.afterCompletionReshootBy = afterCompletionReshootBy;
                }

                public String getAfterCompletionPartiallyApprovedBy() {
                    return afterCompletionPartiallyApprovedBy;
                }

                public void setAfterCompletionPartiallyApprovedBy(String afterCompletionPartiallyApprovedBy) {
                    this.afterCompletionPartiallyApprovedBy = afterCompletionPartiallyApprovedBy;
                }

                public String getAfterCompletionUploadedDate() {
                    return afterCompletionUploadedDate;
                }

                public void setAfterCompletionUploadedDate(String afterCompletionUploadedDate) {
                    this.afterCompletionUploadedDate = afterCompletionUploadedDate;
                }

                public String getAfterCompletionApprovedDate() {
                    return afterCompletionApprovedDate;
                }

                public void setAfterCompletionApprovedDate(String afterCompletionApprovedDate) {
                    this.afterCompletionApprovedDate = afterCompletionApprovedDate;
                }

                public String getAfterCompletionReshootDate() {
                    return afterCompletionReshootDate;
                }

                public void setAfterCompletionReshootDate(String afterCompletionReshootDate) {
                    this.afterCompletionReshootDate = afterCompletionReshootDate;
                }

                public String getAfterCompletionPartiallyApprovedDate() {
                    return afterCompletionPartiallyApprovedDate;
                }

                public void setAfterCompletionPartiallyApprovedDate(String afterCompletionPartiallyApprovedDate) {
                    this.afterCompletionPartiallyApprovedDate = afterCompletionPartiallyApprovedDate;
                }

                public String getAfterRetroStatus() {
                    return afterRetroStatus;
                }

                public void setAfterRetroStatus(String afterRetroStatus) {
                    this.afterRetroStatus = afterRetroStatus;
                }

            }




            public class PostRetro implements Serializable
            {

                @SerializedName("POST_RETRO_UPLOADED_BY")
                @Expose
                private String postRetroUploadedBy;
                @SerializedName("POST_RETRO_APPROVED_BY")
                @Expose
                private String postRetroApprovedBy;
                @SerializedName("POST_RETRO_RESHOOT_BY")
                @Expose
                private String postRetroReshootBy;
                @SerializedName("POST_RETRO_PARTIALLY_APPROVED_BY")
                @Expose
                private String postRetroPartiallyApprovedBy;
                @SerializedName("POST_RETRO_UPLOADED_DATE")
                @Expose
                private String postRetroUploadedDate;
                @SerializedName("POST_RETRO_APPROVED_DATE")
                @Expose
                private String postRetroApprovedDate;
                @SerializedName("POST_RETRO_RESHOOT_DATE")
                @Expose
                private String postRetroReshootDate;
                @SerializedName("POST_RETRO_PARTIALLY_APPROVED_DATE")
                @Expose
                private String postRetroPartiallyApprovedDate;
                @SerializedName("POST_RETRO_STATUS")
                @Expose
                private String postRetroStatus;

                public String getPostRetroUploadedBy() {
                    return postRetroUploadedBy;
                }

                public void setPostRetroUploadedBy(String postRetroUploadedBy) {
                    this.postRetroUploadedBy = postRetroUploadedBy;
                }

                public String getPostRetroApprovedBy() {
                    return postRetroApprovedBy;
                }

                public void setPostRetroApprovedBy(String postRetroApprovedBy) {
                    this.postRetroApprovedBy = postRetroApprovedBy;
                }

                public String getPostRetroReshootBy() {
                    return postRetroReshootBy;
                }

                public void setPostRetroReshootBy(String postRetroReshootBy) {
                    this.postRetroReshootBy = postRetroReshootBy;
                }

                public String getPostRetroPartiallyApprovedBy() {
                    return postRetroPartiallyApprovedBy;
                }

                public void setPostRetroPartiallyApprovedBy(String postRetroPartiallyApprovedBy) {
                    this.postRetroPartiallyApprovedBy = postRetroPartiallyApprovedBy;
                }

                public String getPostRetroUploadedDate() {
                    return postRetroUploadedDate;
                }

                public void setPostRetroUploadedDate(String postRetroUploadedDate) {
                    this.postRetroUploadedDate = postRetroUploadedDate;
                }

                public String getPostRetroApprovedDate() {
                    return postRetroApprovedDate;
                }

                public void setPostRetroApprovedDate(String postRetroApprovedDate) {
                    this.postRetroApprovedDate = postRetroApprovedDate;
                }

                public String getPostRetroReshootDate() {
                    return postRetroReshootDate;
                }

                public void setPostRetroReshootDate(String postRetroReshootDate) {
                    this.postRetroReshootDate = postRetroReshootDate;
                }

                public String getPostRetroPartiallyApprovedDate() {
                    return postRetroPartiallyApprovedDate;
                }

                public void setPostRetroPartiallyApprovedDate(String postRetroPartiallyApprovedDate) {
                    this.postRetroPartiallyApprovedDate = postRetroPartiallyApprovedDate;
                }

                public String getPostRetroStatus() {
                    return postRetroStatus;
                }

                public void setPostRetroStatus(String postRetroStatus) {
                    this.postRetroStatus = postRetroStatus;
                }

            }

            public class PreRetro implements Serializable
            {

                @SerializedName("PRE_RETRO_UPLOADED_BY")
                @Expose
                private String preRetroUploadedBy;
                @SerializedName("PRE_RETRO_APPROVED_BY")
                @Expose
                private String preRetroApprovedBy;
                @SerializedName("PRE_RETRO_RESHOOT_BY")
                @Expose
                private String preRetroReshootBy;
                @SerializedName("PRE_RETRO_PARTIALLY_APPROVED_BY")
                @Expose
                private String preRetroPartiallyApprovedBy;
                @SerializedName("PRE_RETRO_UPLOADED_DATE")
                @Expose
                private String preRetroUploadedDate;
                @SerializedName("PRE_RETRO_APPROVED_DATE")
                @Expose
                private String preRetroApprovedDate;
                @SerializedName("PRE_RETRO_RESHOOT_DATE")
                @Expose
                private String preRetroReshootDate;
                @SerializedName("PRE_RETRO_PARTIALLY_APPROVED_DATE")
                @Expose
                private String preRetroPartiallyApprovedDate;
                @SerializedName("PRE_RETRO_STATUS")
                @Expose
                private String preRetroStatus;

                public String getPreRetroUploadedBy() {
                    return preRetroUploadedBy;
                }

                public void setPreRetroUploadedBy(String preRetroUploadedBy) {
                    this.preRetroUploadedBy = preRetroUploadedBy;
                }

                public String getPreRetroApprovedBy() {
                    return preRetroApprovedBy;
                }

                public void setPreRetroApprovedBy(String preRetroApprovedBy) {
                    this.preRetroApprovedBy = preRetroApprovedBy;
                }

                public String getPreRetroReshootBy() {
                    return preRetroReshootBy;
                }

                public void setPreRetroReshootBy(String preRetroReshootBy) {
                    this.preRetroReshootBy = preRetroReshootBy;
                }

                public String getPreRetroPartiallyApprovedBy() {
                    return preRetroPartiallyApprovedBy;
                }

                public void setPreRetroPartiallyApprovedBy(String preRetroPartiallyApprovedBy) {
                    this.preRetroPartiallyApprovedBy = preRetroPartiallyApprovedBy;
                }

                public String getPreRetroUploadedDate() {
                    return preRetroUploadedDate;
                }

                public void setPreRetroUploadedDate(String preRetroUploadedDate) {
                    this.preRetroUploadedDate = preRetroUploadedDate;
                }

                public String getPreRetroApprovedDate() {
                    return preRetroApprovedDate;
                }

                public void setPreRetroApprovedDate(String preRetroApprovedDate) {
                    this.preRetroApprovedDate = preRetroApprovedDate;
                }

                public String getPreRetroReshootDate() {
                    return preRetroReshootDate;
                }

                public void setPreRetroReshootDate(String preRetroReshootDate) {
                    this.preRetroReshootDate = preRetroReshootDate;
                }

                public String getPreRetroPartiallyApprovedDate() {
                    return preRetroPartiallyApprovedDate;
                }

                public void setPreRetroPartiallyApprovedDate(String preRetroPartiallyApprovedDate) {
                    this.preRetroPartiallyApprovedDate = preRetroPartiallyApprovedDate;
                }

                public String getPreRetroStatus() {
                    return preRetroStatus;
                }

                public void setPreRetroStatus(String preRetroStatus) {
                    this.preRetroStatus = preRetroStatus;
                }

            }

        }

    }


}
