package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateResponse(
    @SerializedName("DeviceDetails") @Expose val DeviceDetails: DeviceDetails? = null,

    @SerializedName("BUILDDETAILS") @Expose val BUILDDETAILS: BUILDDETAILS? = null,

    @SerializedName("APIS") @Expose val APIS: List<APISItem>,

    @SerializedName("message") @Expose val message: String,

    @SerializedName("status") @Expose val status: Boolean,
)

data class DeviceDetails(
    @SerializedName("SEQUENCENUMBER") @Expose val SEQUENCENUMBER: String? = null,

    @SerializedName("PRESCRIPTION") @Expose val PRESCRIPTION: Boolean? = null,

    @SerializedName("uHIDMAXLENGTH") @Expose val uHIDMAXLENGTH: String? = null,

    @SerializedName("CHANGEPASSWORD") @Expose val CHANGEPASSWORD: Boolean? = null,

    @SerializedName("DELIVERYMODE") @Expose val DELIVERYMODE: Any? = null,

    @SerializedName("ADDCUSTOMER") @Expose val ADDCUSTOMER: Any? = null,

    @SerializedName("UHID") @Expose val UHID: Boolean? = null,

    @SerializedName("VENDORNAME") @Expose val VENDORNAME: String? = null,

    @SerializedName("CUSTOMERREGISTRATION") @Expose val CUSTOMERREGISTRATION: Any? = null,

    @SerializedName("PAYMETTYPE") @Expose val PAYMETTYPE: Any? = null,
)

data class APISItem(
    @SerializedName("TOKEN") @Expose val TOKEN: String,

    @SerializedName("URL") @Expose var URL: String,

    @SerializedName("NAME") @Expose val NAME: String? = null,
)

data class BUILDDETAILS(

    @SerializedName("FORCEDOWNLOAD") @Expose val FORCEDOWNLOAD: Boolean,

    @SerializedName("APOLLOIMAGE") @Expose val APOLLOIMAGE: Any? = null,

    @SerializedName("APPAVALIBALITY") @Expose val APPAVALIBALITY: Boolean,

    @SerializedName("APOLLOTEXT") @Expose val APOLLOTEXT: Any? = null,

    @SerializedName("BUILDVERSION") @Expose val BUILDVERSION: String,

    @SerializedName("DOWNLOADURL") @Expose val DOWNLOADURL: String,

    @SerializedName("VENDORIMAGE") @Expose val VENDORIMAGE: Any? = null,

    @SerializedName("EMAILVALIDATION") @Expose val EMAILVALIDATION: Boolean? = null,

    @SerializedName("AVABILITYMESSAGE") @Expose val AVABILITYMESSAGE: String,

    @SerializedName("BUILDMESSAGE") @Expose val BUILDMESSAGE: String,

    @SerializedName("VENDORTEXT") @Expose val VENDORTEXT: Any? = null,
)


//package com.apollopharmacy.vishwam.data.model
//
//data class ValidateResponse(
//    val DeviceDetails: DeviceDetails? = null,
//    val BUILDDETAILS: BUILDDETAILS? = null,
//    val APIS: List<APISItem>,
//    val message: String,
//    val status: Boolean,
//)
//
//data class DeviceDetails(
//    val SEQUENCENUMBER: String? = null,
//    val PRESCRIPTION: Boolean? = null,
//    val uHIDMAXLENGTH: String? = null,
//    val CHANGEPASSWORD: Boolean? = null,
//    val DELIVERYMODE: Any? = null,
//    val ADDCUSTOMER: Any? = null,
//    val UHID: Boolean? = null,
//    val VENDORNAME: String? = null,
//    val CUSTOMERREGISTRATION: Any? = null,
//    val PAYMETTYPE: Any? = null,
//)
//
//data class APISItem(
//    val TOKEN: String,
//    val URL: String,
//    val NAME: String? = null,
//)
//
//data class BUILDDETAILS(
//    val FORCEDOWNLOAD: Boolean,
//    val APOLLOIMAGE: Any? = null,
//    val APPAVALIBALITY: Boolean,
//    val APOLLOTEXT: Any? = null,
//    val BUILDVERSION: String,
//    val DOWNLOADURL: String,
//    val VENDORIMAGE: Any? = null,
//    val EMAILVALIDATION: Boolean? = null,
//    val AVABILITYMESSAGE: String,
//    val BUILDMESSAGE: String,
//    val VENDORTEXT: Any? = null,
//)
//
