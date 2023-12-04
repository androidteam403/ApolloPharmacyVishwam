package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class EmployeeDetailsResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("zcServerDateTime")
    @Expose
    var zcServerDateTime: String? = null

    @SerializedName("zcServerIp")
    @Expose
    var zcServerIp: String? = null

    @SerializedName("zcServerHost")
    @Expose
    var zcServerHost: String? = null

    class Data : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("upload_swach")
        @Expose
        var uploadSwach: UploadSwach? = null

        @SerializedName("upload_apna_retro")
        @Expose
        var uploadApnaRetro: UploadApnaRetro? = null

        @SerializedName("new_drug_request")
        @Expose
        var newDrugRequest: NewDrugRequest? = null

        @SerializedName("enable_marketing_dashboard")
        @Expose
        var enableMarketingDashboard: EnableMarketingDashboard? = null


        /* "enable_marketing_dashboard": {
             "uid": "No",
             "name": "No",
             "other": {
                 "color": null
             },
             "icon": null
         }*/

        @SerializedName("email")
        @Expose
        var email: String? = null


        @SerializedName("role")
        @Expose
        var role: Role? = null

        class Role : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("code")
            @Expose
            var code: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

        }


        @SerializedName("site")
        @Expose
        var site: Site? = null


        class Site : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("site")
            @Expose
            var site: String? = null

            @SerializedName("store_name")
            @Expose
            var storeName: String? = null

            @SerializedName("dc_code")
            @Expose
            var dcCode: DcCode? = null

            class DcCode : Serializable {
                @SerializedName("uid")
                @Expose
                var uid: String? = null

                @SerializedName("code")
                @Expose
                var code: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null


            }

            @SerializedName("state")
            @Expose
            var state: State? = null

            class State : Serializable {
                @SerializedName("uid")
                @Expose
                var uid: String? = null

                @SerializedName("code")
                @Expose
                var code: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null


            }
        }


        @SerializedName("swacch_default_site")
        @Expose
        var swacchDefaultSite: SwacchDefaultSite? = null

        @SerializedName("assign_to_me_any_level")
        @Expose
        var assign_to_me_any_level: AssignToMeAnyLevel? = null

        @SerializedName("department")
        @Expose
        var department: Department? = null

        @SerializedName("champs_admin")
        @Expose
        var champs_admin: ChampsAdmin? = null


        class UploadSwach : Serializable {
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
            var icon: Any? = null

            class Other : Serializable {
                @SerializedName("color")
                @Expose
                var color: Any? = null


            }
        }

        class UploadApnaRetro : Serializable {
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
            var icon: Any? = null

            class Other : Serializable {
                @SerializedName("color")
                @Expose
                var color: Any? = null


            }
        }


        class NewDrugRequest : Serializable {
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
            var icon: Any? = null

            class Other : Serializable {
                @SerializedName("color")
                @Expose
                var color: Any? = null


            }
        }


        class EnableMarketingDashboard {
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

        }

        class Other {
            @SerializedName("color")
            @Expose
            var color: String? = null

        }

        class SwacchDefaultSite : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("site")
            @Expose
            var site: String? = null
        }

        class AssignToMeAnyLevel : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("name_1")
            @Expose
            var name_1: String? = null

            @SerializedName("icon")
            @Expose
            var icon: String? = null
        }

        class ChampsAdmin : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null
        }

        class Department : Serializable {
            @SerializedName("uid")
            @Expose
            var uid: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("code")
            @Expose
            var code: String? = null
        }
    }
}
