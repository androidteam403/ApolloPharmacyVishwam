package com.apollopharmacy.vishwam.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


    public class GetSurevyDetailsByChampsIdResponse implements Serializable
    {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("headerDetails")
        @Expose
        private HeaderDetails headerDetails;
        @SerializedName("categoryDetails")
        @Expose
        private CategoryDetails categoryDetails;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public HeaderDetails getHeaderDetails() {
            return headerDetails;
        }

        public void setHeaderDetails(HeaderDetails headerDetails) {
            this.headerDetails = headerDetails;
        }
        public class HeaderDetails implements Serializable
        {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("champ_auto_id")
            @Expose
            private String champAutoId;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("store_id")
            @Expose
            private String storeId;
            @SerializedName("date_of_visit")
            @Expose
            private String dateOfVisit;
            @SerializedName("email_id_of_trainer")
            @Expose
            private String emailIdOfTrainer;
            @SerializedName("email_id_of_executive")
            @Expose
            private String emailIdOfExecutive;
            @SerializedName("email_id_of_manager")
            @Expose
            private String emailIdOfManager;
            @SerializedName("email_id_of_regional_head")
            @Expose
            private String emailIdOfRegionalHead;
            @SerializedName("email_id_of_recipients")
            @Expose
            private String emailIdOfRecipients;
            @SerializedName("email_id_of_cc")
            @Expose
            private String emailIdOfCc;
            @SerializedName("techinal_details")
            @Expose
            private String techinalDetails;
            @SerializedName("soft_skills")
            @Expose
            private String softSkills;
            @SerializedName("other_training")
            @Expose
            private String otherTraining;
            @SerializedName("issues_to_be_resolved")
            @Expose
            private String issuesToBeResolved;
            @SerializedName("total")
            @Expose
            private String total;
            @SerializedName("created_by")
            @Expose
            private String createdBy;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("status")
            @Expose
            private String status;
            private final static long serialVersionUID = -9019231342443753728L;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getChampAutoId() {
                return champAutoId;
            }

            public void setChampAutoId(String champAutoId) {
                this.champAutoId = champAutoId;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getDateOfVisit() {
                return dateOfVisit;
            }

            public void setDateOfVisit(String dateOfVisit) {
                this.dateOfVisit = dateOfVisit;
            }

            public String getEmailIdOfTrainer() {
                return emailIdOfTrainer;
            }

            public void setEmailIdOfTrainer(String emailIdOfTrainer) {
                this.emailIdOfTrainer = emailIdOfTrainer;
            }

            public String getEmailIdOfExecutive() {
                return emailIdOfExecutive;
            }

            public void setEmailIdOfExecutive(String emailIdOfExecutive) {
                this.emailIdOfExecutive = emailIdOfExecutive;
            }

            public String getEmailIdOfManager() {
                return emailIdOfManager;
            }

            public void setEmailIdOfManager(String emailIdOfManager) {
                this.emailIdOfManager = emailIdOfManager;
            }

            public String getEmailIdOfRegionalHead() {
                return emailIdOfRegionalHead;
            }

            public void setEmailIdOfRegionalHead(String emailIdOfRegionalHead) {
                this.emailIdOfRegionalHead = emailIdOfRegionalHead;
            }

            public String getEmailIdOfRecipients() {
                return emailIdOfRecipients;
            }

            public void setEmailIdOfRecipients(String emailIdOfRecipients) {
                this.emailIdOfRecipients = emailIdOfRecipients;
            }

            public String getEmailIdOfCc() {
                return emailIdOfCc;
            }

            public void setEmailIdOfCc(String emailIdOfCc) {
                this.emailIdOfCc = emailIdOfCc;
            }

            public String getTechinalDetails() {
                return techinalDetails;
            }

            public void setTechinalDetails(String techinalDetails) {
                this.techinalDetails = techinalDetails;
            }

            public String getSoftSkills() {
                return softSkills;
            }

            public void setSoftSkills(String softSkills) {
                this.softSkills = softSkills;
            }

            public String getOtherTraining() {
                return otherTraining;
            }

            public void setOtherTraining(String otherTraining) {
                this.otherTraining = otherTraining;
            }

            public String getIssuesToBeResolved() {
                return issuesToBeResolved;
            }

            public void setIssuesToBeResolved(String issuesToBeResolved) {
                this.issuesToBeResolved = issuesToBeResolved;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

        }

        public CategoryDetails getCategoryDetails() {
            return categoryDetails;
        }

        public void setCategoryDetails(CategoryDetails categoryDetails) {
            this.categoryDetails = categoryDetails;
        }
        public class CategoryDetails implements Serializable
        {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("champ_auto_id")
            @Expose
            private String champAutoId;
            @SerializedName("type")
            @Expose
            private Object type;
            @SerializedName("appearance_store")
            @Expose
            private String appearanceStore;
            @SerializedName("offer_display")
            @Expose
            private String offerDisplay;
            @SerializedName("store_frontage")
            @Expose
            private String storeFrontage;
            @SerializedName("grooming_staff")
            @Expose
            private String groomingStaff;
            @SerializedName("cleanliness_images")
            @Expose
            private String cleanlinessImages;
            @SerializedName("greeting_customers")
            @Expose
            private String greetingCustomers;
            @SerializedName("customer_engagement")
            @Expose
            private String customerEngagement;
            @SerializedName("customer_handling")
            @Expose
            private String customerHandling;
            @SerializedName("reminder_calls")
            @Expose
            private String reminderCalls;
            @SerializedName("hospitality_images")
            @Expose
            private String hospitalityImages;
            @SerializedName("billing_skus_dispensed")
            @Expose
            private String billingSkusDispensed;
            @SerializedName("interpretation_recheck_prescription")
            @Expose
            private String interpretationRecheckPrescription;
            @SerializedName("bank_deposits")
            @Expose
            private String bankDeposits;
            @SerializedName("expiry_fifo_policy")
            @Expose
            private String expiryFifoPolicy;
            @SerializedName("rs_check_internal_auditing")
            @Expose
            private String rsCheckInternalAuditing;
            @SerializedName("one_apollo_dr_connect")
            @Expose
            private String oneApolloDrConnect;
            @SerializedName("cash_checking_every_2_hours")
            @Expose
            private String cashCheckingEvery2Hours;
            @SerializedName("accuracy_images")
            @Expose
            private String accuracyImages;
            @SerializedName("stock_arrangement_refrigerator")
            @Expose
            private String stockArrangementRefrigerator;
            @SerializedName("ac_working_condition")
            @Expose
            private String acWorkingCondition;
            @SerializedName("lighting")
            @Expose
            private String lighting;
            @SerializedName("planogram")
            @Expose
            private String planogram;
            @SerializedName("licenses_renewal")
            @Expose
            private String licensesRenewal;
            @SerializedName("biometric")
            @Expose
            private String biometric;
            @SerializedName("maintenance_hd_register")
            @Expose
            private String maintenanceHdRegister;
            @SerializedName("duty_rosters_allotment")
            @Expose
            private String dutyRostersAllotment;
            @SerializedName("internet")
            @Expose
            private String internet;
            @SerializedName("swiping_machine_working")
            @Expose
            private String swipingMachineWorking;
            @SerializedName("the_cc_cameras_working")
            @Expose
            private String theCcCamerasWorking;
            @SerializedName("printers_working_condition")
            @Expose
            private String printersWorkingCondition;
            @SerializedName("maintenance_images")
            @Expose
            private String maintenanceImages;
            @SerializedName("availability_stock_good")
            @Expose
            private String availabilityStockGood;
            @SerializedName("substitution_offered_regularly")
            @Expose
            private String substitutionOfferedRegularly;
            @SerializedName("service_recovery_done_90")
            @Expose
            private String serviceRecoveryDone90;
            @SerializedName("bounce_tracking")
            @Expose
            private String bounceTracking;
            @SerializedName("products_images")
            @Expose
            private String productsImages;
            @SerializedName("speed_service_5_to_10_minutes")
            @Expose
            private String speedService5To10Minutes;
            @SerializedName("home_delivery_commitment_fulfilled_time")
            @Expose
            private String homeDeliveryCommitmentFulfilledTime;
            @SerializedName("sales_promotion")
            @Expose
            private String salesPromotion;
            @SerializedName("speed_service_sales_promotion_images")
            @Expose
            private String speedServiceSalesPromotionImages;
            private final static long serialVersionUID = 1238255494308058104L;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getChampAutoId() {
                return champAutoId;
            }

            public void setChampAutoId(String champAutoId) {
                this.champAutoId = champAutoId;
            }

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }

            public String getAppearanceStore() {
                return appearanceStore;
            }

            public void setAppearanceStore(String appearanceStore) {
                this.appearanceStore = appearanceStore;
            }

            public String getOfferDisplay() {
                return offerDisplay;
            }

            public void setOfferDisplay(String offerDisplay) {
                this.offerDisplay = offerDisplay;
            }

            public String getStoreFrontage() {
                return storeFrontage;
            }

            public void setStoreFrontage(String storeFrontage) {
                this.storeFrontage = storeFrontage;
            }

            public String getGroomingStaff() {
                return groomingStaff;
            }

            public void setGroomingStaff(String groomingStaff) {
                this.groomingStaff = groomingStaff;
            }

            public String getCleanlinessImages() {
                return cleanlinessImages;
            }

            public void setCleanlinessImages(String cleanlinessImages) {
                this.cleanlinessImages = cleanlinessImages;
            }

            public String getGreetingCustomers() {
                return greetingCustomers;
            }

            public void setGreetingCustomers(String greetingCustomers) {
                this.greetingCustomers = greetingCustomers;
            }

            public String getCustomerEngagement() {
                return customerEngagement;
            }

            public void setCustomerEngagement(String customerEngagement) {
                this.customerEngagement = customerEngagement;
            }

            public String getCustomerHandling() {
                return customerHandling;
            }

            public void setCustomerHandling(String customerHandling) {
                this.customerHandling = customerHandling;
            }

            public String getReminderCalls() {
                return reminderCalls;
            }

            public void setReminderCalls(String reminderCalls) {
                this.reminderCalls = reminderCalls;
            }

            public String getHospitalityImages() {
                return hospitalityImages;
            }

            public void setHospitalityImages(String hospitalityImages) {
                this.hospitalityImages = hospitalityImages;
            }

            public String getBillingSkusDispensed() {
                return billingSkusDispensed;
            }

            public void setBillingSkusDispensed(String billingSkusDispensed) {
                this.billingSkusDispensed = billingSkusDispensed;
            }

            public String getInterpretationRecheckPrescription() {
                return interpretationRecheckPrescription;
            }

            public void setInterpretationRecheckPrescription(String interpretationRecheckPrescription) {
                this.interpretationRecheckPrescription = interpretationRecheckPrescription;
            }

            public String getBankDeposits() {
                return bankDeposits;
            }

            public void setBankDeposits(String bankDeposits) {
                this.bankDeposits = bankDeposits;
            }

            public String getExpiryFifoPolicy() {
                return expiryFifoPolicy;
            }

            public void setExpiryFifoPolicy(String expiryFifoPolicy) {
                this.expiryFifoPolicy = expiryFifoPolicy;
            }

            public String getRsCheckInternalAuditing() {
                return rsCheckInternalAuditing;
            }

            public void setRsCheckInternalAuditing(String rsCheckInternalAuditing) {
                this.rsCheckInternalAuditing = rsCheckInternalAuditing;
            }

            public String getOneApolloDrConnect() {
                return oneApolloDrConnect;
            }

            public void setOneApolloDrConnect(String oneApolloDrConnect) {
                this.oneApolloDrConnect = oneApolloDrConnect;
            }

            public String getCashCheckingEvery2Hours() {
                return cashCheckingEvery2Hours;
            }

            public void setCashCheckingEvery2Hours(String cashCheckingEvery2Hours) {
                this.cashCheckingEvery2Hours = cashCheckingEvery2Hours;
            }

            public String getAccuracyImages() {
                return accuracyImages;
            }

            public void setAccuracyImages(String accuracyImages) {
                this.accuracyImages = accuracyImages;
            }

            public String getStockArrangementRefrigerator() {
                return stockArrangementRefrigerator;
            }

            public void setStockArrangementRefrigerator(String stockArrangementRefrigerator) {
                this.stockArrangementRefrigerator = stockArrangementRefrigerator;
            }

            public String getAcWorkingCondition() {
                return acWorkingCondition;
            }

            public void setAcWorkingCondition(String acWorkingCondition) {
                this.acWorkingCondition = acWorkingCondition;
            }

            public String getLighting() {
                return lighting;
            }

            public void setLighting(String lighting) {
                this.lighting = lighting;
            }

            public String getPlanogram() {
                return planogram;
            }

            public void setPlanogram(String planogram) {
                this.planogram = planogram;
            }

            public String getLicensesRenewal() {
                return licensesRenewal;
            }

            public void setLicensesRenewal(String licensesRenewal) {
                this.licensesRenewal = licensesRenewal;
            }

            public String getBiometric() {
                return biometric;
            }

            public void setBiometric(String biometric) {
                this.biometric = biometric;
            }

            public String getMaintenanceHdRegister() {
                return maintenanceHdRegister;
            }

            public void setMaintenanceHdRegister(String maintenanceHdRegister) {
                this.maintenanceHdRegister = maintenanceHdRegister;
            }

            public String getDutyRostersAllotment() {
                return dutyRostersAllotment;
            }

            public void setDutyRostersAllotment(String dutyRostersAllotment) {
                this.dutyRostersAllotment = dutyRostersAllotment;
            }

            public String getInternet() {
                return internet;
            }

            public void setInternet(String internet) {
                this.internet = internet;
            }

            public String getSwipingMachineWorking() {
                return swipingMachineWorking;
            }

            public void setSwipingMachineWorking(String swipingMachineWorking) {
                this.swipingMachineWorking = swipingMachineWorking;
            }

            public String getTheCcCamerasWorking() {
                return theCcCamerasWorking;
            }

            public void setTheCcCamerasWorking(String theCcCamerasWorking) {
                this.theCcCamerasWorking = theCcCamerasWorking;
            }

            public String getPrintersWorkingCondition() {
                return printersWorkingCondition;
            }

            public void setPrintersWorkingCondition(String printersWorkingCondition) {
                this.printersWorkingCondition = printersWorkingCondition;
            }

            public String getMaintenanceImages() {
                return maintenanceImages;
            }

            public void setMaintenanceImages(String maintenanceImages) {
                this.maintenanceImages = maintenanceImages;
            }

            public String getAvailabilityStockGood() {
                return availabilityStockGood;
            }

            public void setAvailabilityStockGood(String availabilityStockGood) {
                this.availabilityStockGood = availabilityStockGood;
            }

            public String getSubstitutionOfferedRegularly() {
                return substitutionOfferedRegularly;
            }

            public void setSubstitutionOfferedRegularly(String substitutionOfferedRegularly) {
                this.substitutionOfferedRegularly = substitutionOfferedRegularly;
            }

            public String getServiceRecoveryDone90() {
                return serviceRecoveryDone90;
            }

            public void setServiceRecoveryDone90(String serviceRecoveryDone90) {
                this.serviceRecoveryDone90 = serviceRecoveryDone90;
            }

            public String getBounceTracking() {
                return bounceTracking;
            }

            public void setBounceTracking(String bounceTracking) {
                this.bounceTracking = bounceTracking;
            }

            public String getProductsImages() {
                return productsImages;
            }

            public void setProductsImages(String productsImages) {
                this.productsImages = productsImages;
            }

            public String getSpeedService5To10Minutes() {
                return speedService5To10Minutes;
            }

            public void setSpeedService5To10Minutes(String speedService5To10Minutes) {
                this.speedService5To10Minutes = speedService5To10Minutes;
            }

            public String getHomeDeliveryCommitmentFulfilledTime() {
                return homeDeliveryCommitmentFulfilledTime;
            }

            public void setHomeDeliveryCommitmentFulfilledTime(String homeDeliveryCommitmentFulfilledTime) {
                this.homeDeliveryCommitmentFulfilledTime = homeDeliveryCommitmentFulfilledTime;
            }

            public String getSalesPromotion() {
                return salesPromotion;
            }

            public void setSalesPromotion(String salesPromotion) {
                this.salesPromotion = salesPromotion;
            }

            public String getSpeedServiceSalesPromotionImages() {
                return speedServiceSalesPromotionImages;
            }

            public void setSpeedServiceSalesPromotionImages(String speedServiceSalesPromotionImages) {
                this.speedServiceSalesPromotionImages = speedServiceSalesPromotionImages;
            }

        }

    }







