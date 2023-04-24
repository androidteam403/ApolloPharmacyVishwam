package com.apollopharmacy.vishwam.ui.home.apna.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SurveyDetailsList : Serializable {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("success")
    var success = false

    @SerializedName("data")
    var data: Data? = null

    inner class Apartment : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("apartments")
        @Expose
        var apartments: String? = null

        @SerializedName("no_houses")
        @Expose
        var noHouses: Int? = null

        @SerializedName("type")
        @Expose
        var type: Type? = null

        @SerializedName("distance")
        @Expose
        var distance: Int? = null
        fun withUid(uid: String?): Apartment {
            this.uid = uid
            return this
        }

        fun withApartments(apartments: String?): Apartment {
            this.apartments = apartments
            return this
        }

        fun withNoHouses(noHouses: Int?): Apartment {
            this.noHouses = noHouses
            return this
        }

        fun withType(type: Type?): Apartment {
            this.type = type
            return this
        }

        fun withDistance(distance: Int?): Apartment {
            this.distance = distance
            return this
        }
    }

    inner class Chemist : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("chemist")
        @Expose
        var chemist: String? = null

        @SerializedName("organised")
        @Expose
        var organised: Organised? = null

        @SerializedName("unorganised")
        @Expose
        var unorganised: Unorganised? = null

        @SerializedName("unorg_avg_sale")
        @Expose
        var unorgAvgSale: Int? = null

        @SerializedName("org_avg_sale")
        @Expose
        var orgAvgSale: Int? = null
        fun withUid(uid: String?): Chemist {
            this.uid = uid
            return this
        }

        fun withChemist(chemist: String?): Chemist {
            this.chemist = chemist
            return this
        }

        fun withOrganised(organised: Organised?): Chemist {
            this.organised = organised
            return this
        }

        fun withUnorganised(unorganised: Unorganised?): Chemist {
            this.unorganised = unorganised
            return this
        }

        fun withUnorgAvgSale(unorgAvgSale: Int?): Chemist {
            this.unorgAvgSale = unorgAvgSale
            return this
        }

        fun withOrgAvgSale(orgAvgSale: Int?): Chemist {
            this.orgAvgSale = orgAvgSale
            return this
        }
    }

    inner class City : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withUid(uid: String?): City {
            this.uid = uid
            return this
        }

        fun withName(name: String?): City {
            this.name = name
            return this
        }
    }

    inner class CreatedId : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("first_name")
        @Expose
        var firstName: String? = null

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null
        fun withUid(uid: String?): CreatedId {
            this.uid = uid
            return this
        }

        fun withFirstName(firstName: String?): CreatedId {
            this.firstName = firstName
            return this
        }

        fun withLastName(lastName: String?): CreatedId {
            this.lastName = lastName
            return this
        }
    }

    inner class Data : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("address")
        @Expose
        var address: String? = null

        @SerializedName("dimension_type")
        @Expose
        var dimensionType: DimensionType? = null

        @SerializedName("extng_outlet_name")
        @Expose
        var extngOutletName: String? = null

        @SerializedName("lat")
        @Expose
        var lat: String? = null

        @SerializedName("local_disbts_comments")
        @Expose
        var localDisbtsComments: String? = null

        @SerializedName("long")
        @Expose
        var long: String? = null

        @SerializedName("landmarks")
        @Expose
        var landmarks: String? = null

        @SerializedName("parking")
        @Expose
        var parking: Parking? = null

        @SerializedName("pincode")
        @Expose
        var pincode: Int? = null

        @SerializedName("status")
        @Expose
        var status: Status? = null

        @SerializedName("survey_id")
        @Expose
        var surveyId: String? = null

        @SerializedName("traffic_patterns")
        @Expose
        var trafficPatterns: String? = null

        @SerializedName("traffic_street_type")
        @Expose
        var trafficStreetType: TrafficStreetType? = null

        @SerializedName("created_id")
        @Expose
        var createdId: CreatedId? = null

        @SerializedName("modified_id")
        @Expose
        var modifiedId: ModifiedId? = null

        @SerializedName("city")
        @Expose
        var city: City? = null

        @SerializedName("state")
        @Expose
        var state: State? = null

        @SerializedName("location")
        @Expose
        var location: Location? = null

        @SerializedName("business_class")
        @Expose
        var businessClass: Double? = null

        @SerializedName("site_image")
        @Expose
        var siteImage: List<Any>? = null

        @SerializedName("building_age")
        @Expose
        var buildingAge: Int? = null

        @SerializedName("morning_from")
        @Expose
        var morningFrom: String? = null

        @SerializedName("cs_pharma")
        @Expose
        var csPharma: Double? = null

        @SerializedName("cs_surgicals")
        @Expose
        var csSurgicals: Double? = null

        @SerializedName("width")
        @Expose
        var width: Double? = null

        @SerializedName("evening_to")
        @Expose
        var eveningTo: String? = null

        @SerializedName("video")
        @Expose
        var video: List<Any>? = null

        @SerializedName("security_deposit")
        @Expose
        var securityDeposit: Int? = null

        @SerializedName("length")
        @Expose
        var length: Double? = null

        @SerializedName("modified_time")
        @Expose
        var modifiedTime: String? = null

        @SerializedName("total_area")
        @Expose
        var totalArea: Double? = null

        @SerializedName("morning_to")
        @Expose
        var morningTo: String? = null

        @SerializedName("created_time")
        @Expose
        var createdTime: String? = null

        @SerializedName("area_discount")
        @Expose
        var areaDiscount: Double? = null

        @SerializedName("evening_from")
        @Expose
        var eveningFrom: String? = null

        @SerializedName("ceiling_height")
        @Expose
        var ceilingHeight: Double? = null

        @SerializedName("extng_outlet_age")
        @Expose
        var extngOutletAge: Double? = null

        @SerializedName("site_image_mb")
        @Expose
        var siteImageMb: SiteImageMb? = null

        @SerializedName("cs_fmcg")
        @Expose
        var csFmcg: Double? = null

        @SerializedName("expected_rent")
        @Expose
        var expectedRent: Int? = null

        @SerializedName("service_class")
        @Expose
        var serviceClass: Double? = null

        @SerializedName("video_mb")
        @Expose
        var videoMb: VideoMb? = null

        @SerializedName("traffic_generator")
        @Expose
        var trafficGenerator: List<TrafficGenerator>? = null

        @SerializedName("neighboring_store")
        @Expose
        var neighboringStore: List<NeighboringStore>? = null

        @SerializedName("apartments")
        @Expose
        var apartments: List<Apartment>? = null

        @SerializedName("chemist")
        @Expose
        var chemist: List<Chemist>? = null

        @SerializedName("hospitals")
        @Expose
        var hospitals: List<Hospital>? = null
        fun withUid(uid: String?): Data {
            this.uid = uid
            return this
        }

        fun withAddress(address: String?): Data {
            this.address = address
            return this
        }

        fun withDimensionType(dimensionType: DimensionType?): Data {
            this.dimensionType = dimensionType
            return this
        }

        fun withExtngOutletName(extngOutletName: String?): Data {
            this.extngOutletName = extngOutletName
            return this
        }

        fun withLat(lat: String?): Data {
            this.lat = lat
            return this
        }

        fun withLocalDisbtsComments(localDisbtsComments: String?): Data {
            this.localDisbtsComments = localDisbtsComments
            return this
        }

        fun withLong(_long: String?): Data {
            long = _long
            return this
        }

        fun withLandmarks(landmarks: String?): Data {
            this.landmarks = landmarks
            return this
        }

        fun withParking(parking: Parking?): Data {
            this.parking = parking
            return this
        }

        fun withPincode(pincode: Int?): Data {
            this.pincode = pincode
            return this
        }

        fun withStatus(status: Status?): Data {
            this.status = status
            return this
        }

        fun withSurveyId(surveyId: String?): Data {
            this.surveyId = surveyId
            return this
        }

        fun withTrafficPatterns(trafficPatterns: String?): Data {
            this.trafficPatterns = trafficPatterns
            return this
        }

        fun withTrafficStreetType(trafficStreetType: TrafficStreetType?): Data {
            this.trafficStreetType = trafficStreetType
            return this
        }

        fun withCreatedId(createdId: CreatedId?): Data {
            this.createdId = createdId
            return this
        }

        fun withModifiedId(modifiedId: ModifiedId?): Data {
            this.modifiedId = modifiedId
            return this
        }

        fun withCity(city: City?): Data {
            this.city = city
            return this
        }

        fun withState(state: State?): Data {
            this.state = state
            return this
        }

        fun withLocation(location: Location?): Data {
            this.location = location
            return this
        }

        fun withBusinessClass(businessClass: Double?): Data {
            this.businessClass = businessClass
            return this
        }

        fun withSiteImage(siteImage: List<Any>?): Data {
            this.siteImage = siteImage
            return this
        }

        fun withBuildingAge(buildingAge: Int?): Data {
            this.buildingAge = buildingAge
            return this
        }

        fun withMorningFrom(morningFrom: String?): Data {
            this.morningFrom = morningFrom
            return this
        }

        fun withCsPharma(csPharma: Double?): Data {
            this.csPharma = csPharma
            return this
        }

        fun withCsSurgicals(csSurgicals: Double?): Data {
            this.csSurgicals = csSurgicals
            return this
        }

        fun withWidth(width: Double?): Data {
            this.width = width
            return this
        }

        fun withEveningTo(eveningTo: String?): Data {
            this.eveningTo = eveningTo
            return this
        }

        fun withVideo(video: List<Any>?): Data {
            this.video = video
            return this
        }

        fun withSecurityDeposit(securityDeposit: Int?): Data {
            this.securityDeposit = securityDeposit
            return this
        }

        fun withLength(length: Double?): Data {
            this.length = length
            return this
        }

        fun withModifiedTime(modifiedTime: String?): Data {
            this.modifiedTime = modifiedTime
            return this
        }

        fun withTotalArea(totalArea: Double?): Data {
            this.totalArea = totalArea
            return this
        }

        fun withMorningTo(morningTo: String?): Data {
            this.morningTo = morningTo
            return this
        }

        fun withCreatedTime(createdTime: String?): Data {
            this.createdTime = createdTime
            return this
        }

        fun withAreaDiscount(areaDiscount: Double?): Data {
            this.areaDiscount = areaDiscount
            return this
        }

        fun withEveningFrom(eveningFrom: String?): Data {
            this.eveningFrom = eveningFrom
            return this
        }

        fun withCeilingHeight(ceilingHeight: Double?): Data {
            this.ceilingHeight = ceilingHeight
            return this
        }

        fun withExtngOutletAge(extngOutletAge: Double?): Data {
            this.extngOutletAge = extngOutletAge
            return this
        }

        fun withSiteImageMb(siteImageMb: SiteImageMb?): Data {
            this.siteImageMb = siteImageMb
            return this
        }

        fun withCsFmcg(csFmcg: Double?): Data {
            this.csFmcg = csFmcg
            return this
        }

        fun withExpectedRent(expectedRent: Int?): Data {
            this.expectedRent = expectedRent
            return this
        }

        fun withServiceClass(serviceClass: Double?): Data {
            this.serviceClass = serviceClass
            return this
        }

        fun withVideoMb(videoMb: VideoMb?): Data {
            this.videoMb = videoMb
            return this
        }

        fun withTrafficGenerator(trafficGenerator: List<TrafficGenerator>?): Data {
            this.trafficGenerator = trafficGenerator
            return this
        }

        fun withNeighboringStore(neighboringStore: List<NeighboringStore>?): Data {
            this.neighboringStore = neighboringStore
            return this
        }

        fun withApartments(apartments: List<Apartment>?): Data {
            this.apartments = apartments
            return this
        }

        fun withChemist(chemist: List<Chemist>?): Data {
            this.chemist = chemist
            return this
        }

        fun withHospitals(hospitals: List<Hospital>?): Data {
            this.hospitals = hospitals
            return this
        }
    }

    inner class DimensionType : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): DimensionType {
            this.uid = uid
            return this
        }
    }

    inner class Hospital : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("beds")
        @Expose
        var beds: Int? = null

        @SerializedName("hospitals")
        @Expose
        var hospitals: String? = null

        @SerializedName("no_opd")
        @Expose
        var noOpd: Int? = null

        @SerializedName("occupancy")
        @Expose
        var occupancy: Int? = null

        @SerializedName("speciality")
        @Expose
        var speciality: Speciality? = null
        fun withUid(uid: String?): Hospital {
            this.uid = uid
            return this
        }

        fun withBeds(beds: Int?): Hospital {
            this.beds = beds
            return this
        }

        fun withHospitals(hospitals: String?): Hospital {
            this.hospitals = hospitals
            return this
        }

        fun withNoOpd(noOpd: Int?): Hospital {
            this.noOpd = noOpd
            return this
        }

        fun withOccupancy(occupancy: Int?): Hospital {
            this.occupancy = occupancy
            return this
        }

        fun withSpeciality(speciality: Speciality?): Hospital {
            this.speciality = speciality
            return this
        }
    }

    inner class Image : Serializable {
        @SerializedName("url")
        @Expose
        var url: String? = null
        fun withUrl(url: String?): Image {
            this.url = url
            return this
        }
    }

    inner class Image__1 : Serializable {
        @SerializedName("url")
        @Expose
        var url: String? = null
        fun withUrl(url: String?): Image__1 {
            this.url = url
            return this
        }
    }

    inner class Location : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: Any? = null
        fun withUid(uid: Any?): Location {
            this.uid = uid
            return this
        }
        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withName(name: String?): Location {
            this.name = name
            return this
        }
    }

    inner class Location__1 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): Location__1 {
            this.uid = uid
            return this
        }
        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withName(name: String?): Location__1 {
            this.name = name
            return this
        }
    }

    inner class ModifiedId : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("first_name")
        @Expose
        var firstName: String? = null

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null
        fun withUid(uid: String?): ModifiedId {
            this.uid = uid
            return this
        }

        fun withFirstName(firstName: String?): ModifiedId {
            this.firstName = firstName
            return this
        }

        fun withLastName(lastName: String?): ModifiedId {
            this.lastName = lastName
            return this
        }
    }

    class NeighboringStore: Serializable{
        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("location")
        var location: Location? = null

        @SerializedName("store")
        var store: String? = null

        @SerializedName("rent")
        var rent = 0

        @SerializedName("sales")
        var sales = 0.0

        @SerializedName("sqft")
        var sqft = 0.0
    }
    inner class Organised : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): Organised {
            this.uid = uid
            return this
        }
    }

    inner class Other : Serializable {
        @SerializedName("color")
        @Expose
        var color: String? = null
        fun withColor(color: String?): Other {
            this.color = color
            return this
        }
    }

    inner class Parking : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): Parking {
            this.uid = uid
            return this
        }
    }

    inner class SiteImageMb : Serializable {
        @SerializedName("images")
        @Expose
        var images: List<Image>? = null
        fun withImages(images: List<Image>?): SiteImageMb {
            this.images = images
            return this
        }
    }

    inner class Speciality : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): Speciality {
            this.uid = uid
            return this
        }
    }

    inner class State : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withUid(uid: String?): State {
            this.uid = uid
            return this
        }

        fun withName(name: String?): State {
            this.name = name
            return this
        }
    }

    inner class Status : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("other")
        @Expose
        var other: Other? = null

        @SerializedName("icon")
        @Expose
        var icon: String? = null
        fun withUid(uid: String?): Status {
            this.uid = uid
            return this
        }

        fun withName(name: String?): Status {
            this.name = name
            return this
        }

        fun withOther(other: Other?): Status {
            this.other = other
            return this
        }

        fun withIcon(icon: String?): Status {
            this.icon = icon
            return this
        }
    }

    inner class TrafficGenerator : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): TrafficGenerator {
            this.uid = uid
            return this
        }
    }

    inner class TrafficStreetType : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): TrafficStreetType {
            this.uid = uid
            return this
        }
    }

    inner class Type : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): Type {
            this.uid = uid
            return this
        }
    }

    inner class Unorganised : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
        fun withUid(uid: String?): Unorganised {
            this.uid = uid
            return this
        }
    }

    inner class VideoMb : Serializable {
        @SerializedName("images")
        @Expose
        var images: List<Image__1>? = null
        fun withImages(images: List<Image__1>?): VideoMb {
            this.images = images
            return this
        }
    }
}