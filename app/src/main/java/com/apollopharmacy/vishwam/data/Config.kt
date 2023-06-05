package com.apollopharmacy.vishwam.data

import java.util.Date

object Config {
    var STORAGE_CONNECTION_FOR_CCR_APP =
        "DefaultEndpointsProtocol=https;" + "AccountName=pharmtest;" + "AccountKey=dhJpbROM1e6MzzjAHPXyP52+w1U+cN2DQKnqwc77Uwp6LkIH/9k2hHktS3zpfJPCEQdL2jcqYdANTdEox+Fiww=="
    var CONTAINER_NAME = "cms"
    var CONTAINER_NAME_CHAMPS = "champs"
    val PROBLEM_SINCE_DAYS = 30
    var URL_MAIN =
        "https://online.apollopharmacy.org/CPOS/APOLLO/" //"http://lms.apollopharmacy.org:8033/CPOS/APOLLO/"
    var DEVICE_ID = "34.87.87.09.909"
    var KEY = "2039" //Producation : 2034 OTHER : 2027 UAT : 2039
    var TYPE_REJECT = "REJECT"
    var TYPE_ACCEPT = "ACCEPT"
    var ENCRIPTION_KEY = "discount"
    var REQUEST_CODE_CAMERA = 2234243
    var REQUEST_BACK_CAMERA = 2234241
    var REQUEST_SIDE_CAMERA = 2234240
    var REQUEST_BILL_CAMERA = 2234242
    var REQUEST_CODE_PRODUCT_FRONT_CAMERA = 2234244
    val CAMERA_PERMISSION_CODE = 100
    val STORAGE_PERMISSION_CODE = 101


    var APP_ENC_KEY = "Madhan"
    var VALIDATE_VENDOR_TOKEN = "JzCBMp8NNovOPRM4z3FP8GKjNz8XG3Tp"
    var VALIDATEVENDOR_ENCRIPTION_KEY = "globevendor"
    var IS_LOG_ENABLED = true
    val VISWAM_PREFERENCE = "VISWAM_PREFERENCE"
    val ATTENDANCE_API_HEADER = "h72genrSSNFivOi/cfiX3A=="
    val SWACHH_LIST_KEY = "h72genrSSNFivOi/cfiX3A=="
    val SWACHH_IMAGE_KEY = "h72genrSSNFivOi/cfiX3A=="
    val SWACHAPI_HEADER = "h72genrSSNFivOi/cfiX3A=="
    val SWACHH_IMAGES_APPROVE = "h72genrSSNFivOi/cfiX3A=="
    val DRUG_KEY = " h72genrSSNFivOi/cfiX3A=="

    val CMS_GLOBAL_API = "https://viswam.apollopharmacy.org/apolloutil/api/Encryptiondecryption"
    val CMS_GLOABL_KEY = "wyDoHg0wG3U5O9sRWncACA=="

    val CMS_Tickets_List =
        "http://online.apollopharmacy.org:8085/CMS/trackingDataHandler/ticketsList"
    val CMS_List_Of_Departments =
        "http://online.apollopharmacy.org:8085/CMS/departmentCrtlr/listOfDepartments"
    val CMS_Registration =
        "http://online.apollopharmacy.org:8085/CMS/cmsRegistrarionCrtlr/registration"
    val CMS_Registered_Cmp_List =
        "http://online.apollopharmacy.org:8085/CMS/complientsListController/registeredComplientsList"
    val CMS_Update_Ack =
        "http://online.apollopharmacy.org:8085/CMS/acknowledgementController/updateAcknowledgementData"
    val CMS_Fetch_Code = "http://online.apollopharmacy.org:8085/CMS/articlecodeFetcher/fetchCode"
    val CMS_Get_Store_List =
        "https://online.apollopharmacy.org/DISCOUNT/Apollo/DiscountRequest/GetStorteList"
    val CMS_Save_Emp_Details =
        "https://online.apollopharmacy.org/DISCOUNT/Apollo/DiscountRequest/SaveEmployeeDetails"

//    val CMS_Registration= "http://172.16.2.250:8085/CMS/cmsRegistrarionCrtlr/registration"
//    val CMS_Registered_Cmp_List= "http://172.16.2.250:8085/CMS/complientsListController/registeredComplientsList"

    //made changes by naveen
    val EMPLOYEE_WISHES_CONTAINAER_NAME = "apselfisignature"//"ap_selfi_signature"
    const val EMPLOYEE_WISHES_STORAGE_CONNECTIONS =
        "DefaultEndpointsProtocol=https;" + "AccountName=pharmtest;" + "AccountKey=dhJpbROM1e6MzzjAHPXyP52+w1U+cN2DQKnqwc77Uwp6LkIH/9k2hHktS3zpfJPCEQdL2jcqYdANTdEox+Fiww=="
    var COMPLAINTLISTFETCHEDTIME: Date? = null

    val CASH_DEPOSIT_CONTAINAER_NAME = "cashclousr" // CASHCLOUSR
}