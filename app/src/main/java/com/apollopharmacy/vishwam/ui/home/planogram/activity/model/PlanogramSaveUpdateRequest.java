package com.apollopharmacy.vishwam.ui.home.planogram.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



    public class PlanogramSaveUpdateRequest implements Serializable {

        @SerializedName("areas_to_focus_on")
        @Expose
        private String areasToFocusOn;
        @SerializedName("branch_name")
        @Expose
        private String branchName;
        @SerializedName("categories_to_focus_on")
        @Expose
        private String categoriesToFocusOn;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("overall_score")
        @Expose
        private String overallScore;
        @SerializedName("planogram_survey")
        @Expose
        private List<PlanogramSurvey> planogramSurvey;
        @SerializedName("site_id")
        @Expose
        private String siteId;

        @SerializedName("employee_id")
        @Expose
        private String employeeId;
        @SerializedName("chiller")
        @Expose
        private List<Chiller> chiller;
        @SerializedName("diaper_podium")
        @Expose
        private List<DiaperPodium> diaperPodium;
        @SerializedName("offers_gondola")
        @Expose
        private List<OffersGondola> offersGondola;
        @SerializedName("peghooks_display")
        @Expose
        private List<PeghooksDisplay> peghooksDisplay;
        @SerializedName("posters")
        @Expose
        private List<Poster> posters;
        @SerializedName("value_deals_bin")
        @Expose
        private List<ValueDealsBin> valueDealsBin;

        public String getAreasToFocusOn() {
            return areasToFocusOn;
        }

        public void setAreasToFocusOn(String areasToFocusOn) {
            this.areasToFocusOn = areasToFocusOn;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getCategoriesToFocusOn() {
            return categoriesToFocusOn;
        }

        public void setCategoriesToFocusOn(String categoriesToFocusOn) {
            this.categoriesToFocusOn = categoriesToFocusOn;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getOverallScore() {
            return overallScore;
        }

        public void setOverallScore(String overallScore) {
            this.overallScore = overallScore;
        }

        public List<PlanogramSurvey> getPlanogramSurvey() {
            return planogramSurvey;
        }

        public void setPlanogramSurvey(List<PlanogramSurvey> planogramSurvey) {
            this.planogramSurvey = planogramSurvey;
        }

        public class PlanogramSurvey implements Serializable {

            @SerializedName("all_brands_display")
            @Expose
            private AllBrandsDisplay allBrandsDisplay;
            @SerializedName("category_type")
            @Expose
            private CategoryType categoryType;
            @SerializedName("cleanliness")
            @Expose
            private Cleanliness cleanliness;
            @SerializedName("clean_hint_text")
            @Expose
            private String cleanHintText;
            @SerializedName("cond_tag_off_talkers")
            @Expose
            private CondTagOffTalkers condTagOffTalkers;
            @SerializedName("cond_tag_talkers_hint")
            @Expose
            private String condTagTalkersHint;
            @SerializedName("correct_offer_talkers")
            @Expose
            private CorrectOfferTalkers correctOfferTalkers;
            @SerializedName("customer_facing")
            @Expose
            private CustomerFacing customerFacing;
            @SerializedName("empty_shelves_refill")
            @Expose
            private EmptyShelvesRefill emptyShelvesRefill;
            @SerializedName("empty_shel_ref_hint")
            @Expose
            private String emptyShelRefHint;
            @SerializedName("fifo")
            @Expose
            private Fifo fifo;
            @SerializedName("fifo_hint_text")
            @Expose
            private String fifoHintText;
            @SerializedName("fill_upto_top")
            @Expose
            private FillUptoTop fillUptoTop;
            @SerializedName("grouped_product_wise")
            @Expose
            private GroupedProductWise groupedProductWise;
            @SerializedName("lft_right_frnt_bck_hint")
            @Expose
            private String lftRightFrntBckHint;
            @SerializedName("lft_right_frnt_bck")
            @Expose
            private LftRightFrntBck lftRightFrntBck;
            @SerializedName("monthly_offers")
            @Expose
            private MonthlyOffers monthlyOffers;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("no_gaps")
            @Expose
            private NoGaps noGaps;
            @SerializedName("no_unauth_old_posters")
            @Expose
            private NoUnauthOldPosters noUnauthOldPosters;
            @SerializedName("offer_tent_card")
            @Expose
            private OfferTentCard offerTentCard;
            @SerializedName("score")
            @Expose
            private String score;
            @SerializedName("shelf_facing")
            @Expose
            private ShelfFacing shelfFacing;
            @SerializedName("shelf_facing_hint")
            @Expose
            private String shelfFacingHint;
            @SerializedName("shelf_strips")
            @Expose
            private ShelfStrips shelfStrips;
            @SerializedName("shelf_strip_hint")
            @Expose
            private String shelfStripHint;
            @SerializedName("smart_save_offer")
            @Expose
            private SmartSaveOffer smartSaveOffer;
            @SerializedName("subcat_flow")
            @Expose
            private SubcatFlow subcatFlow;
            @SerializedName("subcat_flow_hint")
            @Expose
            private String subcatFlowHint;
            @SerializedName("type")
            @Expose
            private Type type;
            private final static long serialVersionUID = 2697863466995842263L;

            public AllBrandsDisplay getAllBrandsDisplay() {
                return allBrandsDisplay;
            }

            public void setAllBrandsDisplay(AllBrandsDisplay allBrandsDisplay) {
                this.allBrandsDisplay = allBrandsDisplay;
            }

            public class AllBrandsDisplay implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 2570648679180539053L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }


            public CategoryType getCategoryType() {
                return categoryType;
            }

            public void setCategoryType(CategoryType categoryType) {
                this.categoryType = categoryType;
            }

            public class CategoryType implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public Cleanliness getCleanliness() {
                return cleanliness;
            }

            public void setCleanliness(Cleanliness cleanliness) {
                this.cleanliness = cleanliness;
            }

            public class Cleanliness implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getCleanHintText() {
                return cleanHintText;
            }

            public void setCleanHintText(String cleanHintText) {
                this.cleanHintText = cleanHintText;
            }

            public CondTagOffTalkers getCondTagOffTalkers() {
                return condTagOffTalkers;
            }

            public void setCondTagOffTalkers(CondTagOffTalkers condTagOffTalkers) {
                this.condTagOffTalkers = condTagOffTalkers;
            }

            public class CondTagOffTalkers implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getCondTagTalkersHint() {
                return condTagTalkersHint;
            }

            public void setCondTagTalkersHint(String condTagTalkersHint) {
                this.condTagTalkersHint = condTagTalkersHint;
            }

            public CorrectOfferTalkers getCorrectOfferTalkers() {
                return correctOfferTalkers;
            }

            public void setCorrectOfferTalkers(CorrectOfferTalkers correctOfferTalkers) {
                this.correctOfferTalkers = correctOfferTalkers;
            }

            public class CorrectOfferTalkers implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public CustomerFacing getCustomerFacing() {
                return customerFacing;
            }

            public void setCustomerFacing(CustomerFacing customerFacing) {
                this.customerFacing = customerFacing;
            }

            public class CustomerFacing implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public EmptyShelvesRefill getEmptyShelvesRefill() {
                return emptyShelvesRefill;
            }

            public void setEmptyShelvesRefill(EmptyShelvesRefill emptyShelvesRefill) {
                this.emptyShelvesRefill = emptyShelvesRefill;
            }

            public class EmptyShelvesRefill implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -7762315910863383214L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getEmptyShelRefHint() {
                return emptyShelRefHint;
            }

            public void setEmptyShelRefHint(String emptyShelRefHint) {
                this.emptyShelRefHint = emptyShelRefHint;
            }

            public Fifo getFifo() {
                return fifo;
            }

            public void setFifo(Fifo fifo) {
                this.fifo = fifo;
            }

            public class Fifo implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -7481614130799781206L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getFifoHintText() {
                return fifoHintText;
            }

            public void setFifoHintText(String fifoHintText) {
                this.fifoHintText = fifoHintText;
            }

            public FillUptoTop getFillUptoTop() {
                return fillUptoTop;
            }

            public void setFillUptoTop(FillUptoTop fillUptoTop) {
                this.fillUptoTop = fillUptoTop;
            }

            public class FillUptoTop implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -6742508634982076974L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public GroupedProductWise getGroupedProductWise() {
                return groupedProductWise;
            }

            public void setGroupedProductWise(GroupedProductWise groupedProductWise) {
                this.groupedProductWise = groupedProductWise;
            }

            public class GroupedProductWise implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -1315521125853084379L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getLftRightFrntBckHint() {
                return lftRightFrntBckHint;
            }

            public void setLftRightFrntBckHint(String lftRightFrntBckHint) {
                this.lftRightFrntBckHint = lftRightFrntBckHint;
            }


            public LftRightFrntBck getLftRightFrntBck() {
                return lftRightFrntBck;
            }

            public void setLftRightFrntBck(LftRightFrntBck lftRightFrntBck) {
                this.lftRightFrntBck = lftRightFrntBck;
            }

            public class LftRightFrntBck implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;


                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public MonthlyOffers getMonthlyOffers() {
                return monthlyOffers;
            }

            public void setMonthlyOffers(MonthlyOffers monthlyOffers) {
                this.monthlyOffers = monthlyOffers;
            }

            public class MonthlyOffers implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -2282796447573691381L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public NoGaps getNoGaps() {
                return noGaps;
            }

            public void setNoGaps(NoGaps noGaps) {
                this.noGaps = noGaps;
            }

            public class NoGaps implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -7724432698303690682L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public NoUnauthOldPosters getNoUnauthOldPosters() {
                return noUnauthOldPosters;
            }

            public void setNoUnauthOldPosters(NoUnauthOldPosters noUnauthOldPosters) {
                this.noUnauthOldPosters = noUnauthOldPosters;
            }

            public class NoUnauthOldPosters implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -6378271898887681116L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public OfferTentCard getOfferTentCard() {
                return offerTentCard;
            }

            public void setOfferTentCard(OfferTentCard offerTentCard) {
                this.offerTentCard = offerTentCard;
            }

            public class OfferTentCard implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 6638238577403389976L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public ShelfFacing getShelfFacing() {
                return shelfFacing;
            }

            public void setShelfFacing(ShelfFacing shelfFacing) {
                this.shelfFacing = shelfFacing;
            }


            public class ShelfFacing implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -1255587416116969622L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getShelfFacingHint() {
                return shelfFacingHint;
            }

            public void setShelfFacingHint(String shelfFacingHint) {
                this.shelfFacingHint = shelfFacingHint;
            }

            public ShelfStrips getShelfStrips() {
                return shelfStrips;
            }

            public void setShelfStrips(ShelfStrips shelfStrips) {
                this.shelfStrips = shelfStrips;
            }

            public class ShelfStrips implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -6926918039715770131L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }


            public String getShelfStripHint() {
                return shelfStripHint;
            }

            public void setShelfStripHint(String shelfStripHint) {
                this.shelfStripHint = shelfStripHint;
            }

            public SmartSaveOffer getSmartSaveOffer() {
                return smartSaveOffer;
            }

            public void setSmartSaveOffer(SmartSaveOffer smartSaveOffer) {
                this.smartSaveOffer = smartSaveOffer;
            }

            public class SmartSaveOffer implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 1730410281096232548L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public SubcatFlow getSubcatFlow() {
                return subcatFlow;
            }

            public void setSubcatFlow(SubcatFlow subcatFlow) {
                this.subcatFlow = subcatFlow;
            }

            public class SubcatFlow implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -1396683717710120407L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getSubcatFlowHint() {
                return subcatFlowHint;
            }

            public void setSubcatFlowHint(String subcatFlowHint) {
                this.subcatFlowHint = subcatFlowHint;
            }

            public Type getType() {
                return type;
            }

            public void setType(Type type) {
                this.type = type;
            }

            public class Type implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -7449111070759652309L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

        }

        public String getSiteId() {
            return siteId;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public List<Chiller> getChiller() {
            return chiller;
        }

        public void setChiller(List<Chiller> chiller) {
            this.chiller = chiller;
        }

        public class Chiller implements Serializable {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("grouped_product_wise")
            @Expose
            private GroupedProductWise__1 groupedProductWise;
            @SerializedName("no_gaps")
            @Expose
            private NoGaps__1 noGaps;
            @SerializedName("shelf_facing")
            @Expose
            private ShelfFacing__1 shelfFacing;
            @SerializedName("type")
            @Expose
            private Type__1 type;
            private final static long serialVersionUID = 2281798940110586743L;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public GroupedProductWise__1 getGroupedProductWise() {
                return groupedProductWise;
            }

            public void setGroupedProductWise(GroupedProductWise__1 groupedProductWise) {
                this.groupedProductWise = groupedProductWise;
            }

            public class GroupedProductWise__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 2011500070819109501L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public NoGaps__1 getNoGaps() {
                return noGaps;
            }

            public void setNoGaps(NoGaps__1 noGaps) {
                this.noGaps = noGaps;
            }

            public class NoGaps__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -5069649096033542106L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public ShelfFacing__1 getShelfFacing() {
                return shelfFacing;
            }

            public void setShelfFacing(ShelfFacing__1 shelfFacing) {
                this.shelfFacing = shelfFacing;
            }

            public class ShelfFacing__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -2923426285114011276L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public Type__1 getType() {
                return type;
            }

            public void setType(Type__1 type) {
                this.type = type;
            }

            public class Type__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -2863992215191987359L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

        }


        public List<DiaperPodium> getDiaperPodium() {
            return diaperPodium;
        }

        public void setDiaperPodium(List<DiaperPodium> diaperPodium) {
            this.diaperPodium = diaperPodium;
        }

        public class DiaperPodium implements Serializable {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("all_brands_display")
            @Expose
            private AllBrandsDisplay__1 allBrandsDisplay;
            @SerializedName("offer_tent_card")
            @Expose
            private OfferTentCard__1 offerTentCard;
            @SerializedName("type")
            @Expose
            private Type__2 type;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public AllBrandsDisplay__1 getAllBrandsDisplay() {
                return allBrandsDisplay;
            }

            public void setAllBrandsDisplay(AllBrandsDisplay__1 allBrandsDisplay) {
                this.allBrandsDisplay = allBrandsDisplay;
            }

            public class AllBrandsDisplay__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -4515007102406852690L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public OfferTentCard__1 getOfferTentCard() {
                return offerTentCard;
            }

            public void setOfferTentCard(OfferTentCard__1 offerTentCard) {
                this.offerTentCard = offerTentCard;
            }

            public class OfferTentCard__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 8510080101142869558L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public Type__2 getType() {
                return type;
            }

            public void setType(Type__2 type) {
                this.type = type;
            }

            public class Type__2 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -4188624632435376460L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

        }

        public List<OffersGondola> getOffersGondola() {
            return offersGondola;
        }

        public void setOffersGondola(List<OffersGondola> offersGondola) {
            this.offersGondola = offersGondola;
        }

        public class OffersGondola implements Serializable {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("correct_offer_talkers")
            @Expose
            private CorrectOfferTalkers__1 correctOfferTalkers;
            @SerializedName("shelf_facing")
            @Expose
            private ShelfFacing__2 shelfFacing;
            @SerializedName("type")
            @Expose
            private Type__3 type;
            private final static long serialVersionUID = 4314102866467791804L;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public CorrectOfferTalkers__1 getCorrectOfferTalkers() {
                return correctOfferTalkers;
            }

            public void setCorrectOfferTalkers(CorrectOfferTalkers__1 correctOfferTalkers) {
                this.correctOfferTalkers = correctOfferTalkers;
            }

            public class CorrectOfferTalkers__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public ShelfFacing__2 getShelfFacing() {
                return shelfFacing;
            }

            public void setShelfFacing(ShelfFacing__2 shelfFacing) {
                this.shelfFacing = shelfFacing;
            }

            public class ShelfFacing__2 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -5410631810784732851L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public Type__3 getType() {
                return type;
            }

            public void setType(Type__3 type) {
                this.type = type;
            }

            public class Type__3 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 2293325051063013420L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

        }

        public List<PeghooksDisplay> getPeghooksDisplay() {
            return peghooksDisplay;
        }

        public void setPeghooksDisplay(List<PeghooksDisplay> peghooksDisplay) {
            this.peghooksDisplay = peghooksDisplay;
        }

        public class PeghooksDisplay implements Serializable {

            @SerializedName("customer_facing")
            @Expose
            private CustomerFacing__1 customerFacing;
            @SerializedName("grouped_product_wise")
            @Expose
            private GroupedProductWise__2 groupedProductWise;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("type")
            @Expose
            private Type__4 type;
            private final static long serialVersionUID = 3329497376599524698L;

            public CustomerFacing__1 getCustomerFacing() {
                return customerFacing;
            }

            public void setCustomerFacing(CustomerFacing__1 customerFacing) {
                this.customerFacing = customerFacing;
            }

            public class CustomerFacing__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public GroupedProductWise__2 getGroupedProductWise() {
                return groupedProductWise;
            }

            public void setGroupedProductWise(GroupedProductWise__2 groupedProductWise) {
                this.groupedProductWise = groupedProductWise;
            }

            public class GroupedProductWise__2 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -1957906388070437896L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Type__4 getType() {
                return type;
            }

            public void setType(Type__4 type) {
                this.type = type;
            }

            public class Type__4 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 3148075634230166595L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

        }


        public List<Poster> getPosters() {
            return posters;
        }

        public void setPosters(List<Poster> posters) {
            this.posters = posters;
        }

        public class Poster implements Serializable {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("monthly_offers")
            @Expose
            private MonthlyOffers__1 monthlyOffers;
            @SerializedName("no_unauth_old_posters")
            @Expose
            private NoUnauthOldPosters__1 noUnauthOldPosters;
            @SerializedName("smart_save_offer")
            @Expose
            private SmartSaveOffer__1 smartSaveOffer;
            @SerializedName("type")
            @Expose
            private Type__5 type;
            private final static long serialVersionUID = 4148668950729934485L;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public MonthlyOffers__1 getMonthlyOffers() {
                return monthlyOffers;
            }

            public void setMonthlyOffers(MonthlyOffers__1 monthlyOffers) {
                this.monthlyOffers = monthlyOffers;
            }

            public class MonthlyOffers__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 4235044556290215528L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public NoUnauthOldPosters__1 getNoUnauthOldPosters() {
                return noUnauthOldPosters;
            }

            public void setNoUnauthOldPosters(NoUnauthOldPosters__1 noUnauthOldPosters) {
                this.noUnauthOldPosters = noUnauthOldPosters;
            }

            public class NoUnauthOldPosters__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 8493118743633489922L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public SmartSaveOffer__1 getSmartSaveOffer() {
                return smartSaveOffer;
            }

            public void setSmartSaveOffer(SmartSaveOffer__1 smartSaveOffer) {
                this.smartSaveOffer = smartSaveOffer;
            }

            public class SmartSaveOffer__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 9065870235346398478L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public Type__5 getType() {
                return type;
            }

            public void setType(Type__5 type) {
                this.type = type;
            }

            public class Type__5 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 8526793089995573943L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

        }

        public List<ValueDealsBin> getValueDealsBin() {
            return valueDealsBin;
        }

        public void setValueDealsBin(List<ValueDealsBin> valueDealsBin) {
            this.valueDealsBin = valueDealsBin;
        }

        public class ValueDealsBin implements Serializable {

            @SerializedName("fill_upto_top")
            @Expose
            private FillUptoTop__1 fillUptoTop;
            @SerializedName("grouped_product_wise")
            @Expose
            private GroupedProductWise__3 groupedProductWise;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("type")
            @Expose
            private Type__6 type;
            private final static long serialVersionUID = -3103225271019566084L;

            public FillUptoTop__1 getFillUptoTop() {
                return fillUptoTop;
            }

            public void setFillUptoTop(FillUptoTop__1 fillUptoTop) {
                this.fillUptoTop = fillUptoTop;
            }

            public class FillUptoTop__1 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = 6436688956273113302L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public GroupedProductWise__3 getGroupedProductWise() {
                return groupedProductWise;
            }

            public void setGroupedProductWise(GroupedProductWise__3 groupedProductWise) {
                this.groupedProductWise = groupedProductWise;
            }

            public class GroupedProductWise__3 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -5141408135674861716L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Type__6 getType() {
                return type;
            }

            public void setType(Type__6 type) {
                this.type = type;
            }

            public class Type__6 implements Serializable {

                @SerializedName("uid")
                @Expose
                private String uid;
                private final static long serialVersionUID = -658334955938096767L;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

            }

        }

    }



