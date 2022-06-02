package com.apollopharmacy.vishwam.data.model

data class ValidateResponse(
    val DeviceDetails: DeviceDetails? = null,
    val BUILDDETAILS: BUILDDETAILS? = null,
    val APIS: List<APISItem>,
    val message: String,
    val status: Boolean,
)

data class DeviceDetails(
    val SEQUENCENUMBER: String? = null,
    val PRESCRIPTION: Boolean? = null,
    val uHIDMAXLENGTH: String? = null,
    val CHANGEPASSWORD: Boolean? = null,
    val DELIVERYMODE: Any? = null,
    val ADDCUSTOMER: Any? = null,
    val UHID: Boolean? = null,
    val VENDORNAME: String? = null,
    val CUSTOMERREGISTRATION: Any? = null,
    val PAYMETTYPE: Any? = null,
)

data class APISItem(
    val TOKEN: String,
    val URL: String,
    val NAME: String? = null,
)

data class BUILDDETAILS(
    val FORCEDOWNLOAD: Boolean,
    val APOLLOIMAGE: Any? = null,
    val APPAVALIBALITY: Boolean,
    val APOLLOTEXT: Any? = null,
    val BUILDVERSION: String,
    val DOWNLOADURL: String,
    val VENDORIMAGE: Any? = null,
    val EMAILVALIDATION: Boolean? = null,
    val AVABILITYMESSAGE: String,
    val BUILDMESSAGE: String,
    val VENDORTEXT: Any? = null,
)

