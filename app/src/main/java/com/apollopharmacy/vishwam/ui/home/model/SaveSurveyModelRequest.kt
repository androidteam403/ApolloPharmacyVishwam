package com.apollopharmacy.vishwam.ui.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveSurveyModelRequest : Serializable {
    @SerializedName("headerDetails")
    @Expose
    var headerDetails: HeaderDetails? = null

    @SerializedName("categoryDetails")
    @Expose
    var categoryDetails: CategoryDetails? = null

    class HeaderDetails : Serializable {
        @SerializedName("state")
        @Expose
        var state: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("store_id")
        @Expose
        var storeId: String? = null

        @SerializedName("date_of_visit")
        @Expose
        var dateOfVisit: String? = null



        @SerializedName("email_id_of_trainer")
        @Expose
        var emailIdOfTrainer: String? = null

        @SerializedName("email_id_of_executive")
        @Expose
        var emailIdOfExecutive: String? = null

        @SerializedName("email_id_of_manager")
        @Expose
        var emailIdOfManager: String? = null

        @SerializedName("email_id_of_regional_head")
        @Expose
        var emailIdOfRegionalHead: String? = null

        @SerializedName("email_id_of_recipients")
        @Expose
        var emailIdOfRecipients: String? = null

        @SerializedName("email_id_of_cc")
        @Expose
        var emailIdOfCc: String? = null

        @SerializedName("techinal_details")
        @Expose
        var techinalDetails: String? = null

        @SerializedName("soft_skills")
        @Expose
        var softSkills: String? = null

        @SerializedName("other_training")
        @Expose
        var otherTraining: String? = null

        @SerializedName("issues_to_be_resolved")
        @Expose
        var issuesToBeResolved: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null

        @SerializedName("created_by")
        @Expose
        var createdBy: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("champ_auto_id")
        @Expose
        var champAutoId: String? = null

        var site_name:String?=null

    }

    class CategoryDetails : Serializable {
        @SerializedName("appearance_store")
        @Expose
        var appearanceStore: String? = null

        @SerializedName("offer_display")
        @Expose
        var offerDisplay: String? = null

        @SerializedName("store_frontage")
        @Expose
        var storeFrontage: String? = null

        @SerializedName("grooming_staff")
        @Expose
        var groomingStaff: String? = null

        @SerializedName("cleanliness_images")
        @Expose
        var cleanlinessImages: String? = null

        @SerializedName("greeting_customers")
        @Expose
        var greetingCustomers: String? = null

        @SerializedName("customer_engagement")
        @Expose
        var customerEngagement: String? = null

        @SerializedName("customer_handling")
        @Expose
        var customerHandling: String? = null

        @SerializedName("reminder_calls")
        @Expose
        var reminderCalls: String? = null

        @SerializedName("hospitality_images")
        @Expose
        var hospitalityImages: String? = null

        @SerializedName("billing_skus_dispensed")
        @Expose
        var billingSkusDispensed: String? = null

        @SerializedName("interpretation_recheck_prescription")
        @Expose
        var interpretationRecheckPrescription: String? = null

        @SerializedName("bank_deposits")
        @Expose
        var bankDeposits: String? = null

        @SerializedName("expiry_fifo_policy")
        @Expose
        var expiryFifoPolicy: String? = null

        @SerializedName("rs_check_internal_auditing")
        @Expose
        var rsCheckInternalAuditing: String? = null

        @SerializedName("one_apollo_dr_connect")
        @Expose
        var oneApolloDrConnect: String? = null

        @SerializedName("cash_checking_every_2_hours")
        @Expose
        var cashCheckingEvery2Hours: String? = null

        @SerializedName("accuracy_images")
        @Expose
        var accuracyImages: String? = null

        @SerializedName("stock_arrangement_refrigerator")
        @Expose
        var stockArrangementRefrigerator: String? = null

        @SerializedName("ac_working_condition")
        @Expose
        var acWorkingCondition: String? = null

        @SerializedName("lighting")
        @Expose
        var lighting: String? = null

        @SerializedName("planogram")
        @Expose
        var planogram: String? = null

        @SerializedName("licenses_renewal")
        @Expose
        var licensesRenewal: String? = null

        @SerializedName("biometric")
        @Expose
        var biometric: String? = null

        @SerializedName("maintenance_hd_register")
        @Expose
        var maintenanceHdRegister: String? = null

        @SerializedName("duty_rosters_allotment")
        @Expose
        var dutyRostersAllotment: String? = null

        @SerializedName("internet")
        @Expose
        var internet: String? = null

        @SerializedName("swiping_machine_working")
        @Expose
        var swipingMachineWorking: String? = null

        @SerializedName("the_cc_cameras_working")
        @Expose
        var theCcCamerasWorking: String? = null

        @SerializedName("printers_working_condition")
        @Expose
        var printersWorkingCondition: String? = null

        @SerializedName("maintenance_images")
        @Expose
        var maintenanceImages: String? = null

        @SerializedName("availability_stock_good")
        @Expose
        var availabilityStockGood: String? = null

        @SerializedName("substitution_offered_regularly")
        @Expose
        var substitutionOfferedRegularly: String? = null

        @SerializedName("service_recovery_done_90")
        @Expose
        var serviceRecoveryDone90: String? = null

        @SerializedName("bounce_tracking")
        @Expose
        var bounceTracking: String? = null

        @SerializedName("products_images")
        @Expose
        var productsImages: String? = null

        @SerializedName("speed_service_5_to_10_minutes")
        @Expose
        var speedService5To10Minutes: String? = null

        @SerializedName("home_delivery_commitment_fulfilled_time")
        @Expose
        var homeDeliveryCommitmentFulfilledTime: String? = null

        @SerializedName("sales_promotion")
        @Expose
        var salesPromotion: String? = null

        @SerializedName("speed_service_sales_promotion_images")
        @Expose
        var speedServiceSalesPromotionImages: String? = null


    }
}