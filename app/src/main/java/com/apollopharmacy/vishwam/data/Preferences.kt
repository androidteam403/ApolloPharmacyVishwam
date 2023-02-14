package com.apollopharmacy.vishwam.data

import android.content.Context
import android.content.SharedPreferences
import com.apollopharmacy.vishwam.data.Config.VISWAM_PREFERENCE
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.google.gson.GsonBuilder

object Preferences {

    private val sharedPreferences: SharedPreferences =
        ViswamApp.context.getSharedPreferences(VISWAM_PREFERENCE, Context.MODE_PRIVATE)

    private const val KEY_SAVING_TOKEN = "KEY_USER_ID"
    private const val PREF_KEY_LOGIN_JSON = "PREF_KEY_LOGIN_JSON"
    private const val KEY_APP_LEVEL_DESIGNATION = "KEY_APP_LEVEL_DESIGNATION"
    private const val KEY_FROM_DATE = "KEY_FROM_DATE"
    private const val KEY_TO_DATE = "KEY_TO_DATE"

    private const val KEY_QC_REGION_ID = "KEY_QC_REGION_ID"


    private const val KEY_QC_SITE_ID = "KEY_QC_SITE_ID"

    private const val KEY_SITE_DETAILS = "KEY_SITE_DETAILS"
    private const val PREF_KEY_SITE_ID_LIST = "KEY_SITE_ID_LIST"
    private const val PREF_KEY_SITE_ID_LIST_QCFAIL = "PREF_KEY_SITE_ID_LIST_QCFAIL"
    private const val PREF_KEY_REGION_ID_LIST_QCFAIL = "PREF_KEY_REGION_ID_LIST_QCFAIL"
    private const val PREF_SITE_ID_FETCHED = "PREF_SITE_ID_FETCHED"
    private const val PREF_REASON_ID_FETCHED = "PREF_REASON_ID_FETCHED"
    private const val PREF_ITEMTYPE_FETCHED = "PREF_ITEMTYPE_FETCHED"
    private const val PREF_ITEMTYPE_LIST_FETCHED = "PREF_ITEMTYPE_LIST_FETCHED"
    private const val PREF_DOCTORSPECIALITY_FETCHED = "PREF_DOCTORSPECIALITY_FETCHED"
    private const val PREF_DOCTORSPECIALITYLIST_FETCHED = "PREF_DOCTORSPECIALITYLIST_FETCHED"
    private const val PREF_REASON_LIST = "PREF_REASON_LIST"
    private const val PREF_REASON_DEPTARTMENT_LIST = "PREF_REASON_DEPTARTMENT_LIST"
    private const val PREF_REASON_OBJECT = "PREF_REASON_OBJECT"

    private const val PREF_SITE_ID_FETCHED_QC_FAIL = "PREF_SITE_ID_FETCHED_QC_FAIL"
    private const val PREF_REGION_ID_FETCHED_QC_FAIL = "PREF_REGION_ID_FETCHED_QC_FAIL"

    private const val PREF_KEY_EMP_DETAILS_JSON = "PREF_KEY_EMP_DETAILS_JSON"
    private const val PREF_KEY_EMP_DETAILS_JSON_NEW_DRUG = "PREF_KEY_EMP_DETAILS_JSON_NEW_DRUG"
    private const val APP_LEVEL_DESIGNATION_SWACH = "APP_LEVEL_DESIGNATION_SWACH"
    private const val APP_LEVEL_DESIGNATION_QC_FAIL = "APP_LEVEL_DESIGNATION_QC_FAIL"
    private const val EMPLOYEE_ROLE_NEW_DRUG_REQUEST = "EMPLOYEE_ROLE_NEW_DRUG_REQUEST"
    private const val PREF_KEY_RESPONSE_NEW_TICKET_LIST_JSON =
        "PREF_KEY_RESPONSE_NEW_TICKET_LIST_JSON"

    fun savingToken(userId: String) {
        sharedPreferences.edit().putString(KEY_SAVING_TOKEN, userId).apply()
    }

    fun getToken(): String {
        return sharedPreferences.getString(KEY_SAVING_TOKEN, "")!!
    }

    fun savingStoreData(storedata: String) {
        sharedPreferences.edit().putString(KEY_SITE_DETAILS, storedata).apply()
    }

    fun getStoreData(): String {
        return sharedPreferences.getString(KEY_SITE_DETAILS, "")!!
    }

    fun signOutUser() {
        sharedPreferences.edit().remove(KEY_SAVING_TOKEN).apply()
    }

    fun storeLoginJson(loginDetails: String) {
        sharedPreferences.edit().putString(PREF_KEY_LOGIN_JSON, loginDetails).apply()
    }

    fun getLoginJson(): String {
        return sharedPreferences.getString(PREF_KEY_LOGIN_JSON, "")!!
//        val storeString = sharedPreferences.getString(PREF_KEY_LOGIN_JSON, "")
//        return try {
//            Gson().fromJson(storeString, LoginDetails::class.java)
//        } catch (e: JsonParseException) {
//            e.printStackTrace()
//            return null
//        }
    }

    //------------------------ saving designation ------------------------------------

    private const val KEY_DESIGNATION = "KEY_DESIGNATION"
    fun saveDesignation(userDesignation: String) {
        sharedPreferences.edit().putString(KEY_DESIGNATION, userDesignation).apply()
    }

    fun getDesignation(): String {
        return sharedPreferences.getString(KEY_DESIGNATION, "")!!
    }


    private const val KEY_APP_DESIGNATION = "KEY_APP_DESIGNATION"
    fun saveAppDesignation(userDesignation: String) {
        sharedPreferences.edit().putString(KEY_APP_DESIGNATION, userDesignation).apply()
    }

    fun getAppDesignation(): String {
        return sharedPreferences.getString(KEY_APP_DESIGNATION, "")!!
    }

    //------------------ saving profileData ------------------------------

    private const val KEY_PROFILE = "KEY_PROFILE"
    fun saveProfile(userProfile: String) {
        sharedPreferences.edit().putString(KEY_PROFILE, userProfile).apply()
    }

    fun getProfile(): String {
        return sharedPreferences.getString(KEY_PROFILE, "")!!
    }

    fun savePassword(password: String) {
        sharedPreferences.edit().putString(KEY_USER_PASSWORD, password).apply()
    }

    fun getUserPassword(): String {
        return sharedPreferences.getString(KEY_USER_PASSWORD, "")!!
    }

    //-------------------- get Api details -----------------------

    private const val KEY_API = "KEY_API"
    private const val SWACH_API = "SWACH_API"

    private const val KEY_SITE_ID = "site_id"
    private const val KEY_SITE_INFORMATION = "site_information"
    private const val KEY_GLOBAL_RESPONSE = "KEY_GLOBAL_RESPONSE"
    private const val PREF_KEY_USER_VALIDATED = "PREF_KEY_USER_VALIDATED"
    private const val PREF_KEY_VALIDATED_EMP = "PREF_KEY_VALIDATED_EMP"
    private const val PREF_KEY_PIN_CREATED = "PREF_KEY_PIN_CREATED"
    private const val PREF_KEY_FCM_UPDATED = "PREF_KEY_FCM_UPDATED"
    private const val KEY_LOGIN_DATE = "KEY_LOGIN_DATE"
    private const val KEY_COMPANY = "KEY_COMPANY"
    private const val KEY_USER_PASSWORD = "KEY_PASSWORD"
    private const val UPLOADED_DATE_DAY_WISE = ""
    private const val EMPLOYEE_ROLE = ""

    private const val KEY_SWACHH_SITEID = "KEY_SWACHH_SITEID"

    private const val KEY_REGISTRATION_SITE_ID = "KEY_REGISTRATION_SITE_ID"

    private const val KEY_EMP_MOBILE_NUMBER = "KEY_EMP_MOBILE_NUMBER"


    fun saveApi(apiItems: String) {
        sharedPreferences.edit().putString(KEY_API, apiItems).apply()
    }

    fun getApi(): String {
        return sharedPreferences.getString(KEY_API, "")!!
    }


    fun saveSwachhApi(apiItems: String) {
        sharedPreferences.edit().putString(KEY_API, apiItems).apply()
    }

    fun getSwachhApi(): String {
        return sharedPreferences.getString(KEY_API, "")!!
    }


    fun saveSiteId(siteId: String) {
        sharedPreferences.edit().putString(KEY_SITE_ID, siteId).apply()
    }

    fun getSiteId(): String {
        return sharedPreferences.getString(KEY_SITE_ID, "")!!
    }

    fun saveSiteInformation(siteInformation: String) {
        sharedPreferences.edit().putString(KEY_SITE_INFORMATION, siteInformation).apply()
    }

    fun getSiteInformation(): String {
        return sharedPreferences.getString(KEY_SITE_INFORMATION, "")!!
    }

    fun saveGlobalResponse(apiItems: String) {
        sharedPreferences.edit().putString(KEY_GLOBAL_RESPONSE, apiItems).apply()
    }

    fun getGlobalResponse(): String {
        return sharedPreferences.getString(KEY_GLOBAL_RESPONSE, "")!!
    }

    fun setIsUserValidated(validateSuccess: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_KEY_USER_VALIDATED, validateSuccess).apply()
    }

    fun getIsUserValidated(): Boolean {
        return sharedPreferences.getBoolean(PREF_KEY_USER_VALIDATED, false)
    }

    fun setValidatedEmpId(siteInformation: String) {
        sharedPreferences.edit().putString(PREF_KEY_VALIDATED_EMP, siteInformation).apply()
    }

    fun getValidatedEmpId(): String {
        return sharedPreferences.getString(PREF_KEY_VALIDATED_EMP, "")!!
    }

    fun setIsPinCreated(createdPin: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_KEY_PIN_CREATED, createdPin).apply()
    }

    fun getIsPinCreated(): Boolean {
        return sharedPreferences.getBoolean(PREF_KEY_PIN_CREATED, false)
    }

    fun setFCMKeyUpdated(createdPin: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_KEY_FCM_UPDATED, createdPin).apply()
    }

    fun getIsFCMKeyUpdated(): Boolean {
        return sharedPreferences.getBoolean(PREF_KEY_FCM_UPDATED, false)
    }

    fun setLoginDate(loginDate: String) {
        sharedPreferences.edit().putString(KEY_LOGIN_DATE, loginDate).apply()
    }

    fun getLoginDate(): String {
        return sharedPreferences.getString(KEY_LOGIN_DATE, "")!!
    }

    fun setCompany(company: String) {
        sharedPreferences.edit().putString(KEY_COMPANY, company).apply()
    }

    fun getCompany(): String {
        return sharedPreferences.getString(KEY_COMPANY, "")!!
    }

    fun setUploadedDateDayWise(company: String) {
        sharedPreferences.edit().putString(UPLOADED_DATE_DAY_WISE, company).apply()
    }

    fun getUploadedDateDayWise(): String {
        return sharedPreferences.getString(UPLOADED_DATE_DAY_WISE, "")!!
    }

    fun setSiteIdListFetched(isSiteIdListFetched: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_SITE_ID_FETCHED, isSiteIdListFetched).apply()
    }

    fun isSiteIdListFetched(): Boolean {
        return sharedPreferences.getBoolean(PREF_SITE_ID_FETCHED, false)
    }


    fun setItemTypeListFetched(isItemTypeListFetched: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_ITEMTYPE_FETCHED, isItemTypeListFetched).apply()
    }

    fun isItemTypeListFetched(): Boolean {
        return sharedPreferences.getBoolean(PREF_ITEMTYPE_FETCHED, false)
    }

    fun setDoctorSpecialityListFetched(isItemTypeListFetched: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_DOCTORSPECIALITY_FETCHED, isItemTypeListFetched)
            .apply()
    }

    fun isDoctorSpecialityListFetched(): Boolean {
        return sharedPreferences.getBoolean(PREF_DOCTORSPECIALITY_FETCHED, false)
    }

    fun setReasonListFetched(isReasonIdListFetched: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_REASON_ID_FETCHED, isReasonIdListFetched).apply()
    }

    fun isReasonIdListFetched(): Boolean {
        return sharedPreferences.getBoolean(PREF_REASON_ID_FETCHED, false)
    }

    fun setSiteIdListFetchedQcFail(isSiteIdListFetchedQcfail: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_SITE_ID_FETCHED_QC_FAIL, isSiteIdListFetchedQcfail)
            .apply()
    }

    fun isSiteIdListFetchedQcFail(): Boolean {
        return sharedPreferences.getBoolean(PREF_SITE_ID_FETCHED_QC_FAIL, false)
    }


    fun setRegionIdListFetchedQcFail(isRegionIdListFetchedQcfail: Boolean) {
        sharedPreferences.edit()
            .putBoolean(PREF_REGION_ID_FETCHED_QC_FAIL, isRegionIdListFetchedQcfail).apply()
    }

    fun isRegionIdListFetchedQcFail(): Boolean {
        return sharedPreferences.getBoolean(PREF_REGION_ID_FETCHED_QC_FAIL, false)
    }

    fun setSiteIdList(siteIdList: String) {
        sharedPreferences.edit().putString(PREF_KEY_SITE_ID_LIST, siteIdList).apply()
    }

    fun getSiteIdListJson(): String {
        return sharedPreferences.getString(PREF_KEY_SITE_ID_LIST, "")!!
    }

    fun setItemTypeList(siteIdList: String) {
        sharedPreferences.edit().putString(PREF_ITEMTYPE_LIST_FETCHED, siteIdList).apply()
    }

    fun getItemTypeListJson(): String {
        return sharedPreferences.getString(PREF_ITEMTYPE_LIST_FETCHED, "")!!
    }

    fun setDoctorSpecialityList(siteIdList: String) {
        sharedPreferences.edit().putString(PREF_DOCTORSPECIALITYLIST_FETCHED, siteIdList).apply()
    }

    fun getDoctorSpecialityListJson(): String {
        return sharedPreferences.getString(PREF_DOCTORSPECIALITYLIST_FETCHED, "")!!
    }

    fun setReasonIdList(siteIdList: String) {
        sharedPreferences.edit().putString(PREF_REASON_LIST, siteIdList).apply()
    }

    fun getReasonIdListJson(): String {
        return sharedPreferences.getString(PREF_REASON_LIST, "")!!
    }

    fun setReasonIdObject(siteIdList: String) {
        sharedPreferences.edit().putString(PREF_REASON_OBJECT, siteIdList).apply()
    }

    fun getReasonIdObjectJson(): String {
        return sharedPreferences.getString(PREF_REASON_OBJECT, "")!!
    }

    fun setReasondDepartmentIdList(siteIdList: String) {
        sharedPreferences.edit().putString(PREF_REASON_DEPTARTMENT_LIST, siteIdList).apply()
    }

    fun getReasondDepartmentIdList(): String {
        return sharedPreferences.getString(PREF_REASON_DEPTARTMENT_LIST, "")!!
    }


    fun setReasonsListFetched(isSiteIdListFetchedQcfail: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_SITE_ID_FETCHED_QC_FAIL, isSiteIdListFetchedQcfail)
            .apply()
    }

    fun setSiteIdListQcFail(siteIdListQcFail: String) {
        sharedPreferences.edit().putString(PREF_KEY_SITE_ID_LIST_QCFAIL, siteIdListQcFail).apply()
    }

    fun getSiteIdListJsonQcFail(): String {
        return sharedPreferences.getString(PREF_KEY_SITE_ID_LIST_QCFAIL, "")!!
    }

    fun setRegionIdListQcFail(regionIdListQcFail: String) {
        sharedPreferences.edit().putString(PREF_KEY_REGION_ID_LIST_QCFAIL, regionIdListQcFail)
            .apply()
    }

    fun getRegionIdListJsonQcFail(): String {
        return sharedPreferences.getString(PREF_KEY_REGION_ID_LIST_QCFAIL, "")!!
    }


    fun setEmployeeRoleUid(role: String) {
        sharedPreferences.edit().putString(EMPLOYEE_ROLE, role).apply()
    }

    fun getEmployeeRoleUid(): String {
        return sharedPreferences.getString(EMPLOYEE_ROLE, "")!!
    }

    fun setEmployeeRoleUidNewDrugRequest(role: String) {
        sharedPreferences.edit().putString(EMPLOYEE_ROLE_NEW_DRUG_REQUEST, role).apply()
    }

    fun getEmployeeRoleUidNewDrugRequest(): String {
        return sharedPreferences.getString(EMPLOYEE_ROLE_NEW_DRUG_REQUEST, "")!!
    }

    fun setAppLevelDesignationSwach(role: String) {
        sharedPreferences.edit().putString(APP_LEVEL_DESIGNATION_SWACH, role).apply()
    }

    fun getAppLevelDesignationSwach(): String {
        return sharedPreferences.getString(APP_LEVEL_DESIGNATION_SWACH, "")!!
    }

    fun setAppLevelDesignationQCFail(role: String) {
        sharedPreferences.edit().putString(APP_LEVEL_DESIGNATION_QC_FAIL, role).apply()
    }

    fun getAppLevelDesignationQCFail(): String {
        return sharedPreferences.getString(APP_LEVEL_DESIGNATION_QC_FAIL, "")!!
    }


    fun setSwachhSiteId(swachhSiteId: String) {
        sharedPreferences.edit().putString(KEY_SWACHH_SITEID, swachhSiteId).apply()
    }

    fun getSwachhSiteId(): String {
        return sharedPreferences.getString(KEY_SWACHH_SITEID, "")!!
    }

    fun storeEmployeeDetailsResponseJson(employeeDetailsResponse: String) {
        sharedPreferences.edit().putString(PREF_KEY_EMP_DETAILS_JSON, employeeDetailsResponse)
            .apply()
    }

    fun getEmployeeDetailsResponseJson(): String {
        return sharedPreferences.getString(PREF_KEY_EMP_DETAILS_JSON, "")!!

    }


    fun storeEmployeeDetailsResponseJsonNewDrug(employeeDetailsResponse: String) {
        sharedPreferences.edit()
            .putString(PREF_KEY_EMP_DETAILS_JSON_NEW_DRUG, employeeDetailsResponse).apply()
    }

    fun getEmployeeDetailsResponseJsonNewDrug(): String {
        return sharedPreferences.getString(PREF_KEY_EMP_DETAILS_JSON_NEW_DRUG, "")!!

    }


    fun setAppLevelDesignation(siteIdList: String) {
        sharedPreferences.edit().putString(KEY_APP_LEVEL_DESIGNATION, siteIdList).apply()
    }


    fun getAppLevelDesignation(): String {
        return sharedPreferences.getString(KEY_APP_LEVEL_DESIGNATION, "")!!
    }
//        val storeString = sharedPreferences.getString(PREF_KEY_LOGIN_JSON, "")
//        return try {
//            Gson().fromJson(storeString, LoginDetails::class.java)
//        } catch (e: JsonParseException) {
//            e.printStackTrace()
//            return null
//        }


    fun setQcFromDate(siteIdList: String) {
        sharedPreferences.edit().putString(KEY_FROM_DATE, siteIdList).apply()
    }


    fun getQcFromDate(): String {
        return sharedPreferences.getString(KEY_FROM_DATE, "")!!
    }

    fun setQcToDate(siteIdList: String) {
        sharedPreferences.edit().putString(KEY_TO_DATE, siteIdList).apply()
    }


    fun getQcToDate(): String {
        return sharedPreferences.getString(KEY_TO_DATE, "")!!
    }

    fun setQcSite(siteIdList: String) {
        sharedPreferences.edit().putString(KEY_QC_SITE_ID, siteIdList).apply()
    }


    fun getQcSite(): String {
        return sharedPreferences.getString(KEY_QC_SITE_ID, "")!!
    }


    fun setQcRegion(siteIdList: String) {
        sharedPreferences.edit().putString(KEY_QC_REGION_ID, siteIdList).apply()
    }


    fun getQcRegion(): String {
        return sharedPreferences.getString(KEY_QC_REGION_ID, "")!!
    }


    fun setRegistrationSiteId(siteId: String) {
        sharedPreferences.edit().putString(KEY_REGISTRATION_SITE_ID, siteId).apply()
    }

    fun getRegistrationSiteId(): String {
        return sharedPreferences.getString(KEY_REGISTRATION_SITE_ID, "")!!
    }

    fun setEmpPhoneNumber(siteId: String) {
        sharedPreferences.edit().putString(KEY_EMP_MOBILE_NUMBER, siteId).apply()
    }

    fun getEmpPhoneNumber(): String {
        return sharedPreferences.getString(KEY_EMP_MOBILE_NUMBER, "")!!
    }

    fun setResponseNewTicketlist(responseNewTicketlistJson: String) {
        sharedPreferences.edit()
            .putString(PREF_KEY_RESPONSE_NEW_TICKET_LIST_JSON, responseNewTicketlistJson).apply()
    }

    fun getResponseNewTicketlist(): String {
            return sharedPreferences.getString(PREF_KEY_RESPONSE_NEW_TICKET_LIST_JSON, "")!!
    }
}