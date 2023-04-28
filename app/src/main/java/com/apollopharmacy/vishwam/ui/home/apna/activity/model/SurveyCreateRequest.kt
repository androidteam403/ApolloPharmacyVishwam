package com.apollopharmacy.vishwam.ui.home.apna.activity.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest.ToiletsAvailability

class SurveyCreateRequest {
    @SerializedName("dimension_type")
    @Expose
    var dimensionType: DimensionType? = null

    @SerializedName("parking")
    @Expose
    var parking: Parking? = null

    @SerializedName("toilets_availability")
    @Expose
    var toiletsAvailability: ToiletsAvailability? = null

    @SerializedName("employee_id")
    @Expose
    var employeeId: String? = null

    @SerializedName("neighboring_store")
    @Expose
    var neighboringStore: List<NeighboringStore>? = null

    @SerializedName("chemist")
    @Expose
    var chemist: List<Chemist>? = null

    @SerializedName("apartments")
    @Expose
    var apartments: List<Apartment>? = null

    @SerializedName("hospitals")
    @Expose
    var hospitals: List<Hospital>? = null

    @SerializedName("site_image_mb")
    @Expose
    var siteImageMb: SiteImageMb? = null

    @SerializedName("video_mb")
    @Expose
    var videoMb: VideoMb? = null

    @SerializedName("location")
    @Expose
    var location: Location__1? = null

    @SerializedName("state")
    @Expose
    var state: State? = null

    @SerializedName("city")
    @Expose
    var city: City? = null

    @SerializedName("pincode")
    @Expose
    var pincode: String? = null

    @SerializedName("landmarks")
    @Expose
    var landmarks: String? = null

    @SerializedName("lat")
    @Expose
    var lat: String? = null

    @SerializedName("long")
    @Expose
    var long: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("length")
    @Expose
    var length: String? = null

    @SerializedName("width")
    @Expose
    var width: String? = null

    @SerializedName("ceiling_height")
    @Expose
    var ceilingHeight: String? = null

    @SerializedName("total_area")
    @Expose
    var totalArea: Float? = null

    @SerializedName("building_age")
    @Expose
    var buildingAge: String? = null

    @SerializedName("expected_rent")
    @Expose
    var expectedRent: Int? = null

    @SerializedName("security_deposit")
    @Expose
    var securityDeposit: Int? = null

    @SerializedName("traffic_street_type")
    @Expose
    var trafficStreetType: TrafficStreetType? = null

    @SerializedName("morning_from")
    @Expose
    var morningFrom: String? = null

    @SerializedName("morning_to")
    @Expose
    var morningTo: String? = null

    @SerializedName("evening_from")
    @Expose
    var eveningFrom: String? = null

    @SerializedName("evening_to")
    @Expose
    var eveningTo: String? = null

    @SerializedName("traffic_patterns")
    @Expose
    var trafficPatterns: String? = null

    @SerializedName("extng_outlet_name")
    @Expose
    var extngOutletName: String? = null

    @SerializedName("extng_outlet_age")
    @Expose
    var extngOutletAge: Float? = null

    @SerializedName("cs_pharma")
    @Expose
    var csPharma: Float? = null

    @SerializedName("cs_fmcg")
    @Expose
    var csFmcg: Float? = null

    @SerializedName("cs_surgicals")
    @Expose
    var csSurgicals: Float? = null

    @SerializedName("area_discount")
    @Expose
    var areaDiscount: Float? = null

    @SerializedName("local_disbts_comments")
    @Expose
    var localDisbtsComments: String? = null

    @SerializedName("service_class")
    @Expose
    var serviceClass: Float? = null

    @SerializedName("business_class")
    @Expose
    var businessClass: Float? = null

    @SerializedName("traffic_generator")
    @Expose
    var trafficGenerator: List<TrafficGenerator>? = null

    class DimensionType {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class Parking {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class ToiletsAvailability {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class NeighboringStore {
        @SerializedName("location")
        @Expose
        var location: Location? = null

        @SerializedName("store")
        @Expose
        var store: String? = null

        @SerializedName("rent")
        @Expose
        var rent: Int? = null

        @SerializedName("sales")
        @Expose
        var sales: Float? = null

        @SerializedName("sqft")
        @Expose
        var sqft: Float? = null

        class Location {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }
    }

    class Chemist {
        @SerializedName("chemist")
        @Expose
        var chemist: String? = null

        @SerializedName("organised")
        @Expose
        var organised: Organised? = null

        @SerializedName("org_avg_sale")
        @Expose
        var orgAvgSale: Int? = null

        @SerializedName("unorganised")
        @Expose
        var unorganised: Unorganised? = null

        @SerializedName("unorg_avg_sale")
        @Expose
        var unorgAvgSale: Int? = null

        class Organised {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }

        class Unorganised {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }
    }

    class Apartment {
        @SerializedName("type")
        @Expose
        var type: Type? = null

        @SerializedName("apartments")
        @Expose
        var apartments: String? = null

        @SerializedName("no_houses")
        @Expose
        var noHouses: String? = null

        @SerializedName("distance")
        @Expose
        var distance: Int? = null

        class Type {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }
    }

    class Hospital {
        @SerializedName("speciality")
        @Expose
        var speciality: Speciality? = null

        @SerializedName("hospitals")
        @Expose
        var hospitals: String? = null

        @SerializedName("beds")
        @Expose
        var beds: String? = null

        @SerializedName("no_opd")
        @Expose
        var noOpd: String? = null

        @SerializedName("occupancy")
        @Expose
        var occupancy: String? = null

        class Speciality {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }
    }

    class SiteImageMb {
        @SerializedName("images")
        @Expose
        var images: List<Image>? = null

        class Image {
            @SerializedName("url")
            @Expose
            var url: String? = null
        }
    }

    class VideoMb {
        @SerializedName("video")
        @Expose
        var video: List<Video>? = null

        class Video {
            @SerializedName("url")
            @Expose
            var url: String? = null
        }
    }

    class Location__1 {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class State {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class City {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class TrafficGenerator {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class TrafficStreetType {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }
}