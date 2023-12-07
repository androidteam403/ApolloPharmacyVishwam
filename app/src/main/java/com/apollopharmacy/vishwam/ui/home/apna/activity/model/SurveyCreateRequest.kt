package com.apollopharmacy.vishwam.ui.home.apna.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SurveyCreateRequest : Serializable {
    @SerializedName("dimension_type")
    @Expose
    var dimensionType: DimensionType? = null

    @SerializedName("parking")
    @Expose
    var parking: Parking? = null

    @SerializedName("surveyed_on")
    @Expose
    var surveyed_on: String? = null

    @SerializedName("toilets_availability")
    @Expose
    var toiletsAvailability: ToiletsAvailability? = null

    @SerializedName("employee_id")
    @Expose
    var employeeId: String? = null

    @SerializedName("neighboring_store")
    @Expose
    var neighboringStore: List<NeighboringStore>? = null

    @SerializedName("nsp_prov")
    @Expose
    var networkServiceProvider: List<NetworkServiceProvider>? = null

    @SerializedName("isp_prov")
    @Expose
    var internetServiceProvider: List<InternetServiceProvider>? = null

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

    @SerializedName("region")
    @Expose
    var region: Region? = null

//    @SerializedName("state")
//    @Expose
//    var state: State? = null
//
//    @SerializedName("city")
//    @Expose
//    var city: City? = null

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

//    var dimensionType1: String? = null

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
    var totalArea: Double? = null

    @SerializedName("shop_address")
    @Expose
    var shopAddress: String? = null

    @SerializedName("shop_no")
    @Expose
    var shopNo: String? = null

    @SerializedName("building_age")
    @Expose
    var buildingAge: String? = null

    @SerializedName("bldg_age_in_month")
    @Expose
    var bldgAgeInMonth: String? = null


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

    @SerializedName("eo_site_id")
    @Expose
    var eoSiteId: String? = null

    @SerializedName("eo_site_name")
    @Expose
    var eoSiteName: String? = null

    @SerializedName("extng_outlet_age")
    @Expose
    var extngOutletAge: Float? = null

    @SerializedName("extng_outlet_age_in_month")
    @Expose
    var extng_outlet_age_in_month: Float? = null

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

    @SerializedName("occupation")
    @Expose
    var occupation: String? = null

    @SerializedName("service_class")
    @Expose
    var serviceClass: Float? = null

    @SerializedName("business_class")
    @Expose
    var businessClass: Float? = null

    @SerializedName("traffic_generator")
    @Expose
    var trafficGenerator: List<TrafficGenerator>? = null

    var location2: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("apollo_employee")
    @Expose
    var apolloEmployee: ApolloEmployee? = null

    class ApolloEmployee : Serializable {
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("label")
        @Expose
        var label: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("icon")
        @Expose
        var icon: String? = null

        @SerializedName("other")
        @Expose
        var other: String? = null

        @SerializedName("isDefault")
        @Expose
        var isDefault: String? = null
    }

    class DimensionType : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        var name: String? = null
    }

    class Parking : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class ToiletsAvailability : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class NetworkServiceProvider : Serializable {
        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    class InternetServiceProvider : Serializable {
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class NeighboringStore : Serializable {
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
        var sales: Double? = null

        @SerializedName("sqft")
        @Expose
        var sqft: Double? = null

        class Location : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            var name: String? = null
        }
    }

    class Chemist : Serializable {
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

        class Organised : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }

        class Unorganised : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }
    }

    class Apartment : Serializable {
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

        class Type : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }
    }

    class Hospital : Serializable {
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

        class Speciality : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null
        }
    }

    class SiteImageMb : Serializable {
        @SerializedName("images")
        @Expose
        var images: List<Image>? = null

        class Image : Serializable {
            @SerializedName("url")
            @Expose
            var url: String? = null
        }
    }

    class VideoMb : Serializable {
        @SerializedName("video")
        @Expose
        var video: List<Video>? = null

        class Video : Serializable {
            @SerializedName("url")
            @Expose
            var url: String? = null
        }
    }

    class Region : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        var name: String? = null
    }

    class State : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class City : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }

    class TrafficGenerator : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        var name: String? = null
    }

    class TrafficStreetType : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null
    }
}