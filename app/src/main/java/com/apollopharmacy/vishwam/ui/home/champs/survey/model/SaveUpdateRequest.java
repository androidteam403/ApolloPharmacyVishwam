package com.apollopharmacy.vishwam.ui.home.champs.survey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SaveUpdateRequest implements Serializable {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("champs_id")
    @Expose
    private String champsId;


    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("add_recp_email")
    @Expose
    private String add_recp_email;

    @SerializedName("cc_email")
    @Expose
    private String cc_email;

    @SerializedName("trainer_email")
    @Expose
    private String trainerEmail;
    @SerializedName("trainer_id")
    @Expose
    private String trainerId;

    @SerializedName("employee_id")
    @Expose
    private String employeeId;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @SerializedName("trainer_name")
    @Expose
    private String trainerName;
    @SerializedName("trimp_other")
    @Expose
    private String trimpOther;
    @SerializedName("trimp_soft_skill")
    @Expose
    private String trimpSoftSkill;
    @SerializedName("trimp_soft_skills")
    @Expose
    private String trimpSoftSkills;
    @SerializedName("trimp_tech")
    @Expose
    private String trimpTech;
    @SerializedName("store_id")
    @Expose
    private String store;
    @SerializedName("total_score")
    @Expose
    private String totalScore;

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    @SerializedName("cms_champs_survey_qa")
    @Expose
    private List<CmsChampsSurveyQa> cmsChampsSurveyQa;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChampsId() {
        return champsId;
    }


    public void setChampsId(String champsId) {
        this.champsId = champsId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdd_recp_email() {
        return add_recp_email;
    }

    public void setAdd_recp_email(String add_recp_email) {
        this.add_recp_email = add_recp_email;
    }

    public String getCc_email() {
        return cc_email;
    }

    public void setCc_email(String cc_email) {
        this.cc_email = cc_email;
    }

    public String getTrainerEmail() {
        return trainerEmail;
    }

    public void setTrainerEmail(String trainerEmail) {
        this.trainerEmail = trainerEmail;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrimpOther() {
        return trimpOther;
    }

    public void setTrimpOther(String trimpOther) {
        this.trimpOther = trimpOther;
    }

    public String getTrimpSoftSkill() {
        return trimpSoftSkill;
    }

    public void setTrimpSoftSkill(String trimpSoftSkill) {
        this.trimpSoftSkill = trimpSoftSkill;
    }

    public String getTrimpSoftSkills() {
        return trimpSoftSkills;
    }

    public void setTrimpSoftSkills(String trimpSoftSkills) {
        this.trimpSoftSkills = trimpSoftSkills;
    }

    public String getTrimpTech() {
        return trimpTech;
    }

    public void setTrimpTech(String trimpTech) {
        this.trimpTech = trimpTech;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public List<CmsChampsSurveyQa> getCmsChampsSurveyQa() {
        return cmsChampsSurveyQa;
    }

    public void setCmsChampsSurveyQa(List<CmsChampsSurveyQa> cmsChampsSurveyQa) {
        this.cmsChampsSurveyQa = cmsChampsSurveyQa;
    }

    public static class CmsChampsSurveyQa implements Serializable {

        @SerializedName("answer_image")
        @Expose
        private AnswerImage answerImage;
        @SerializedName("answer")
        @Expose
        private String answer;
        @SerializedName("answer_type")
        @Expose
        private String answerType;
        @SerializedName("category_name")
        @Expose
        private String categoryName;

        public String getMaxScore() {
            return maxScore;
        }

        public void setMaxScore(String maxScore) {
            this.maxScore = maxScore;
        }

        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("max_score")
        @Expose
        private String maxScore;

        public AnswerImage getAnswerImage() {
            return answerImage;
        }

        public void setAnswerImage(AnswerImage answerImage) {
            this.answerImage = answerImage;
        }

        public static class AnswerImage implements Serializable {

            @SerializedName("images")
            @Expose
            private List<Image> images;

            public List<Image> getImages() {
                return images;
            }

            public void setImages(List<Image> images) {
                this.images = images;
            }

            public class Image implements Serializable {

                @SerializedName("url")
                @Expose
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

            }

        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAnswerType() {
            return answerType;
        }

        public void setAnswerType(String answerType) {
            this.answerType = answerType;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

    }
}



