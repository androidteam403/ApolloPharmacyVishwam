package com.apollopharmacy.vishwam.ui.home.planogram.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlanogramDetailsListResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    public class AllBrandsDisplay : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class CategoryType : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Chiller : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("grouped_product_wise")
        @Expose
        var groupedProductWise: GroupedProductWise__1? = null

        @SerializedName("no_gaps")
        @Expose
        var noGaps: NoGaps? = null

        @SerializedName("shelf_facing")
        @Expose
        var shelfFacing: ShelfFacing__1? = null

        @SerializedName("type")
        @Expose
        var type: Type__3? = null
    }

    public class Cleanliness : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class CondTagOffTalkers : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class CorrectOfferTalkers : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class CustomerFacing : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Data : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("areas_to_focus_on")
        @Expose
        var areasToFocusOn: String? = null

        @SerializedName("branch_name")
        @Expose
        var branchName: String? = null

        @SerializedName("categories_to_focus_on")
        @Expose
        var categoriesToFocusOn: String? = null

        @SerializedName("site")
        @Expose
        var site: Site? = null

        @SerializedName("overall_score")
        @Expose
        var overallScore: Any? = null

        @SerializedName("date")
        @Expose
        var date: String? = null

        @SerializedName("offers_gondola")
        @Expose
        var offersGondola: List<OffersGondola>? = null

        @SerializedName("diaper_podium")
        @Expose
        var diaperPodium: List<DiaperPodium>? = null

        @SerializedName("peghooks_display")
        @Expose
        var peghooksDisplay: List<PeghooksDisplay>? = null

        @SerializedName("chiller")
        @Expose
        var chiller: List<Chiller>? = null

        @SerializedName("value_deals_bin")
        @Expose
        var valueDealsBin: List<ValueDealsBin>? = null

        @SerializedName("posters")
        @Expose
        var posters: List<Poster>? = null

        @SerializedName("planogram_survey")
        @Expose
        var planogramSurvey: List<PlanogramSurvey>? = null
    }

    public class DiaperPodium : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("all_brands_display")
        @Expose
        var allBrandsDisplay: AllBrandsDisplay? = null

        @SerializedName("offer_tent_card")
        @Expose
        var offerTentCard: OfferTentCard? = null

        @SerializedName("type")
        @Expose
        var type: Type__1? = null
    }

    public class EmptyShelvesRefill : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Fifo : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class FillUptoTop : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class GroupedProductWise : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class GroupedProductWise__1 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class GroupedProductWise__2 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class LftRightFrntBck : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class MonthlyOffers : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class NoGaps : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class NoUnauthOldPosters : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class OfferTentCard : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class OffersGondola : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("correct_offer_talkers")
        @Expose
        var correctOfferTalkers: CorrectOfferTalkers? = null

        @SerializedName("shelf_facing")
        @Expose
        var shelfFacing: ShelfFacing? = null

        @SerializedName("type")
        @Expose
        var type: Type? = null
    }

    public class PeghooksDisplay : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("type")
        @Expose
        var type: Type__2? = null

        @SerializedName("grouped_product_wise")
        @Expose
        var groupedProductWise: GroupedProductWise? = null

        @SerializedName("customer_facing")
        @Expose
        var customerFacing: CustomerFacing? = null
    }

    public class PlanogramSurvey : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("category_type")
        @Expose
        var categoryType: CategoryType? = null

        @SerializedName("cleanliness")
        @Expose
        var cleanliness: Cleanliness? = null

        @SerializedName("clean_hint_text")
        @Expose
        var cleanHintText: String? = null

        @SerializedName("cond_tag_off_talkers")
        @Expose
        var condTagOffTalkers: CondTagOffTalkers? = null

        @SerializedName("cond_tag_talkers_hint")
        @Expose
        var condTagTalkersHint: String? = null

        @SerializedName("empty_shelves_refill")
        @Expose
        var emptyShelvesRefill: EmptyShelvesRefill? = null

        @SerializedName("empty_shel_ref_hint")
        @Expose
        var emptyShelRefHint: String? = null

        @SerializedName("fifo")
        @Expose
        var fifo: Fifo? = null

        @SerializedName("fifo_hint_text")
        @Expose
        var fifoHintText: String? = null

        @SerializedName("lft_right_frnt_bck_hint")
        @Expose
        var lftRightFrntBckHint: String? = null

        @SerializedName("lft_right_frnt_bck")
        @Expose
        var lftRightFrntBck: LftRightFrntBck? = null

        @SerializedName("shelf_facing")
        @Expose
        var shelfFacing: ShelfFacing__2? = null

        @SerializedName("shelf_facing_hint")
        @Expose
        var shelfFacingHint: String? = null

        @SerializedName("type")
        @Expose
        var type: Type__6? = null

        @SerializedName("subcat_flow_hint")
        @Expose
        var subcatFlowHint: String? = null

        @SerializedName("subcat_flow")
        @Expose
        var subcatFlow: SubcatFlow? = null

        @SerializedName("shelf_strip_hint")
        @Expose
        var shelfStripHint: String? = null

        @SerializedName("shelf_strips")
        @Expose
        var shelfStrips: ShelfStrips? = null

        @SerializedName("score")
        @Expose
        var score: Any? = null
    }

    public class Poster : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("monthly_offers")
        @Expose
        var monthlyOffers: MonthlyOffers? = null

        @SerializedName("no_unauth_old_posters")
        @Expose
        var noUnauthOldPosters: NoUnauthOldPosters? = null

        @SerializedName("smart_save_offer")
        @Expose
        var smartSaveOffer: SmartSaveOffer? = null

        @SerializedName("type")
        @Expose
        var type: Type__5? = null
    }

    public class ShelfFacing : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class ShelfFacing__1 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class ShelfFacing__2 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class ShelfStrips : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Site : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("site")
        @Expose
        var site: String? = null
    }

    public class SmartSaveOffer : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class SubcatFlow : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Type : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Type__1 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Type__2 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Type__3 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Type__4 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Type__5 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class Type__6 : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
    }

    public class ValueDealsBin : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("fill_upto_top")
        @Expose
        var fillUptoTop: FillUptoTop? = null

        @SerializedName("grouped_product_wise")
        @Expose
        var groupedProductWise: GroupedProductWise__2? = null

        @SerializedName("type")
        @Expose
        var type: Type__4? = null
    }
}