package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class DrugRequest : Serializable {
    @SerializedName("DcIdOrSiteid")
    @Expose
    var dcIdOrSiteid: String? = null

    @SerializedName("Category")
    @Expose
    var category: String? = null

    @SerializedName("ItemName")
    @Expose
    var itemName: String? = null

    @SerializedName("Batch")
    @Expose
    var batch: String? = null

    @SerializedName("PackSize")
    @Expose
    var packSize: String? = null

    @SerializedName("Mrp")
    @Expose
    var mrp: String? = null

    @SerializedName("PurchasePrice")
    @Expose
    var purchasePrice: String? = null

    @SerializedName("ManufactureDate")
    @Expose
    var manufactureDate: String? = null

    @SerializedName("ExpiryDate")
    @Expose
    var expiryDate: String? = null

    @SerializedName("BarCode")
    @Expose
    var barCode: String? = null

    @SerializedName("HSNCode")
    @Expose
    var hSNCode: String? = null

    @SerializedName("Gst")
    @Expose
    var gst: String? = null

    @SerializedName("UserId")
    @Expose
    var userId: String? = null

    @SerializedName("ModifiedDate")
    @Expose
    var modifiedDate: String? = null

    @SerializedName("Status")
    @Expose
    var status: String? = null

    @SerializedName("Flag")
    @Expose
    var flag: String? = null

    @SerializedName("ArticleCode")
    @Expose
    var articleCode: String? = null

    @SerializedName("Remarks")
    @Expose
    var remarks: String? = null

    @SerializedName("CreatedBy")
    @Expose
    var createdBy: String? = null

    @SerializedName("Irqid")
    @Expose
    var irqid: String? = null

    @SerializedName("BrandName")
    @Expose
    var brandName: String? = null

    @SerializedName("ProdNameTypVarient")
    @Expose
    var prodNameTypVarient: String? = null

    @SerializedName("ReleaseFrom")
    @Expose
    var releaseFrom: String? = null

    @SerializedName("Combo")
    @Expose
    var combo: String? = null

    @SerializedName("OnlineSalesUOM")
    @Expose
    var onlineSalesUOM: String? = null

    @SerializedName("OnlineSalesUOMFactor")
    @Expose
    var onlineSalesUOMFactor: String? = null

    @SerializedName("ProductOrigin")
    @Expose
    var productOrigin: String? = null

    @SerializedName("ItemType")
    @Expose
    var itemType: String? = null

    @SerializedName("AirEnabled")
    @Expose
    var airEnabled: String? = null

    @SerializedName("Courierable")
    @Expose
    var courierable: String? = null


    @SerializedName("RequiredQuantity")
    @Expose
    var requiredQty: String? = null

    @SerializedName("DoctorName")
    @Expose
    var doctorName: String? = null

    @SerializedName("DoctorSpecialty")
    @Expose
    var doctorSpecialty: String? = null

    @SerializedName("Images")
    @Expose
    var images: List<Image>? = null

    var site: StoreListItem? = null
    var description: String? = null
    var employeeDetailsResponse: EmployeeDetailsResponse? = null

    constructor(
        dcIdOrSiteid: String?,
        category: String?,
        itemName: String?,
        batch: String?,
        packSize: String?,
        mrp: String?,
        purchasePrice: String?,
        manufactureDate: String?,
        expiryDate: String?,
        barCode: String?,
        hSNCode: String?,
        gst: String?,
        userId: String?,
        modifiedDate: String?,
        status: String?,
        flag: String?,
        articleCode: String?,
        remarks: String?,
        createdBy: String?,
        irqid: String?,
        brandName: String?,
        prodNameTypVarient: String?,
        releaseFrom: String?,
        combo: String?,
        onlineSalesUOM: String?,
        onlineSalesUOMFactor: String?,
        productOrigin: String?,
        itemType: String?,
        airEnabled: String?,
        courierable: String?,
        requiredQty:String?,
        doctorName:String?,
        doctorSpecialty:String?,
        images: List<Image>?,
        description: String?,
        site: StoreListItem,
        employeeDetailsResponse: EmployeeDetailsResponse
    ) {
        this.dcIdOrSiteid = dcIdOrSiteid
        this.category = category
        this.itemName = itemName
        this.batch = batch
        this.packSize = packSize
        this.mrp = mrp
        this.purchasePrice = purchasePrice
        this.manufactureDate = manufactureDate
        this.expiryDate = expiryDate
        this.barCode = barCode
        this.hSNCode = hSNCode
        this.gst = gst
        this.userId = userId
        this.modifiedDate = modifiedDate
        this.status = status
        this.flag = flag
        this.articleCode = articleCode
        this.remarks = remarks
        this.createdBy = createdBy
        this.irqid = irqid
        this.brandName = brandName
        this.prodNameTypVarient = prodNameTypVarient
        this.releaseFrom = releaseFrom
        this.combo = combo
        this.onlineSalesUOM = onlineSalesUOM
        this.onlineSalesUOMFactor = onlineSalesUOMFactor
        this.productOrigin = productOrigin
        this.itemType = itemType
        this.airEnabled = airEnabled
        this.courierable = courierable
        this.requiredQty=requiredQty
        this.doctorName=doctorName
        this.doctorSpecialty=doctorSpecialty
        this.images = images
        this.description = description
        this.site = site
        this.employeeDetailsResponse = employeeDetailsResponse
    }

    constructor()

    fun withDcIdOrSiteid(dcIdOrSiteid: String?): DrugRequest {
        this.dcIdOrSiteid = dcIdOrSiteid
        return this
    }

    fun withCategory(category: String?): DrugRequest {
        this.category = category
        return this
    }

    fun withItemName(itemName: String?): DrugRequest {
        this.itemName = itemName
        return this
    }

    fun withBatch(batch: String?): DrugRequest {
        this.batch = batch
        return this
    }

    fun withPackSize(packSize: String?): DrugRequest {
        this.packSize = packSize
        return this
    }

    fun withMrp(mrp: String?): DrugRequest {
        this.mrp = mrp
        return this
    }

    fun withPurchasePrice(purchasePrice: String?): DrugRequest {
        this.purchasePrice = purchasePrice
        return this
    }

    fun withManufactureDate(manufactureDate: String?): DrugRequest {
        this.manufactureDate = manufactureDate
        return this
    }

    fun withExpiryDate(expiryDate: String?): DrugRequest {
        this.expiryDate = expiryDate
        return this
    }

    fun withBarCode(barCode: String?): DrugRequest {
        this.barCode = barCode
        return this
    }

    fun withHSNCode(hSNCode: String?): DrugRequest {
        this.hSNCode = hSNCode
        return this
    }

    fun withGst(gst: String?): DrugRequest {
        this.gst = gst
        return this
    }

    fun withUserId(userId: String?): DrugRequest {
        this.userId = userId
        return this
    }

    fun withModifiedDate(modifiedDate: String?): DrugRequest {
        this.modifiedDate = modifiedDate
        return this
    }

    fun withStatus(status: String?): DrugRequest {
        this.status = status
        return this
    }

    fun withFlag(flag: String?): DrugRequest {
        this.flag = flag
        return this
    }

    fun withArticleCode(articleCode: String?): DrugRequest {
        this.articleCode = articleCode
        return this
    }

    fun withRemarks(remarks: String?): DrugRequest {
        this.remarks = remarks
        return this
    }

    fun withCreatedBy(createdBy: String?): DrugRequest {
        this.createdBy = createdBy
        return this
    }

    fun withIrqid(irqid: String?): DrugRequest {
        this.irqid = irqid
        return this
    }

    fun withBrandName(brandName: String?): DrugRequest {
        this.brandName = brandName
        return this
    }

    fun withProdNameTypVarient(prodNameTypVarient: String?): DrugRequest {
        this.prodNameTypVarient = prodNameTypVarient
        return this
    }

    fun withReleaseFrom(releaseFrom: String?): DrugRequest {
        this.releaseFrom = releaseFrom
        return this
    }

    fun withCombo(combo: String?): DrugRequest {
        this.combo = combo
        return this
    }

    fun withOnlineSalesUOM(onlineSalesUOM: String?): DrugRequest {
        this.onlineSalesUOM = onlineSalesUOM
        return this
    }

    fun withOnlineSalesUOMFactor(onlineSalesUOMFactor: String?): DrugRequest {
        this.onlineSalesUOMFactor = onlineSalesUOMFactor
        return this
    }

    fun withProductOrigin(productOrigin: String?): DrugRequest {
        this.productOrigin = productOrigin
        return this
    }

    fun withItemType(itemType: String?): DrugRequest {
        this.itemType = itemType
        return this
    }

    fun withAirEnabled(airEnabled: String?): DrugRequest {
        this.airEnabled = airEnabled
        return this
    }

    fun withCourierable(courierable: String?): DrugRequest {
        this.courierable = courierable
        return this
    }

    fun withRequiredQty(requiredQty: String?): DrugRequest {
        this.requiredQty = requiredQty
        return this
    }

    fun withDoctorName(doctorName: String?): DrugRequest {
        this.doctorName = doctorName
        return this
    }

    fun withDoctorSpecialty(doctorSpecialty: String?): DrugRequest {
        this.doctorSpecialty = doctorSpecialty
        return this
    }



    fun withImages(images: List<Image>?): DrugRequest {
        this.images = images
        return this
    }

    public class Image: Serializable {


        @SerializedName("ImageTypeName")
        @Expose
        var imageTypeName: String? = null

        @SerializedName("ImgData")
        @Expose
        var imgData: String? = null

        @SerializedName("ImgTitle")
        @Expose
        var imgTitle: String? = null

        @SerializedName("ImgType")
        @Expose
        var imgType: String? = null

        @SerializedName("Imglength")
        @Expose
        var imglength: String? = null

        var imageURL: String? = null

        constructor(
            imageTypeName: String?,
            imgData: String?,
            imgTitle: String?,
            imgType: String?,
            imglength: String?,
            imgUrl: String?
        ) {
            this.imageTypeName = imageTypeName
            this.imgData = imgData
            this.imgTitle = imgTitle
            this.imgType = imgType
            this.imglength = imglength
            this.imageURL = imgUrl
        }

        fun withImageTypeName(imageTypeName: String?): Image {
            this.imageTypeName = imageTypeName
            return this
        }

        fun withImgData(imgData: String?): Image {
            this.imgData = imgData
            return this
        }

        fun withImgTitle(imgTitle: String?): Image {
            this.imgTitle = imgTitle
            return this
        }

        fun withImgType(imgType: String?): Image {
            this.imgType = imgType
            return this
        }

        fun withImglength(imglength: String?): Image {
            this.imglength = imglength
            return this
        }

    }


}