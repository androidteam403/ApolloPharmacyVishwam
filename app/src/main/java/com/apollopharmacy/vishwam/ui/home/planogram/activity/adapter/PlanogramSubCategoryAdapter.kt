package com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPlanogramSubcategoryBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramActivityCallback
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramDetailsListResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse
import com.tooltip.Tooltip

class PlanogramSubCategoryAdapter(
    var questionsList: ArrayList<PlanogramSurveyQuestionsListResponse.Questions>,
    var applicationContext: Context,
    var planogramCateoryCallback: PlanogramActivityCallback,
    var categoryName: String?,
    var detailsListResponse: PlanogramDetailsListResponse,
    var categoryPosition: Int,
) : RecyclerView.Adapter<PlanogramSubCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterPlanogramSubCategoryBinding: AdapterPlanogramSubcategoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_planogram_subcategory,
                parent,
                false
            )
        return ViewHolder(adapterPlanogramSubCategoryBinding)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var question = questionsList.get(position)


        holder.adapterPlanogramSubCategoryBinding.question.text = question.name
        questionsList.get(position).categoryName = categoryName




        if (detailsListResponse.data != null) {
            if (detailsListResponse.data != null) {
                if (detailsListResponse.data!!.diaperPodium != null) {

                    for (j in detailsListResponse.data!!.diaperPodium!!.indices) {

                        if (categoryName.equals("Diaper podium")) {
                            if (question.name.equals("All brands displayed") && detailsListResponse.data!!.diaperPodium!!.get(
                                    j
                                ).allBrandsDisplay!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.diaperPodium!!.get(j).allBrandsDisplay!!.uid
                            }
                            if (question.name.equals("Offer Tent card") && detailsListResponse.data!!.diaperPodium!!.get(
                                    j
                                ).offerTentCard!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.diaperPodium!!.get(j).offerTentCard!!.uid
                            }
                        }


                    }

                }
                if (detailsListResponse.data!!.valueDealsBin != null) {

                    for (j in detailsListResponse.data!!.valueDealsBin!!.indices) {

                        if (categoryName.equals("Value deals bin")) {
                            if (question.name.equals("Grouped product wise") && detailsListResponse.data!!.valueDealsBin!!.get(
                                    j
                                ).groupedProductWise!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.valueDealsBin!!.get(j).groupedProductWise!!.uid
                            }
                            if (question.name.equals("Filled up to top") && detailsListResponse.data!!.valueDealsBin!!.get(
                                    j
                                ).fillUptoTop!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.diaperPodium!!.get(j).offerTentCard!!.uid
                            }
                        }


                    }

                }
                if (detailsListResponse.data!!.posters != null) {

                    for (j in detailsListResponse.data!!.posters!!.indices) {

                        if (categoryName.equals("POSTERS")) {
                            if (question.name!!.contains("Monthly") && detailsListResponse.data!!.posters!!.get(
                                    j
                                ).monthlyOffers!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.posters!!.get(j).monthlyOffers!!.uid
                            }
                            if (question.name!!.contains("Smart Saver") && detailsListResponse.data!!.posters!!.get(
                                    j
                                ).smartSaveOffer!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.posters!!.get(j).smartSaveOffer!!.uid
                            }
                            if (question.name!!.contains("No") && detailsListResponse.data!!.posters!!.get(
                                    j
                                ).noUnauthOldPosters!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.posters!!.get(j).noUnauthOldPosters!!.uid
                            }
                        }


                    }

                }
                if (detailsListResponse.data!!.peghooksDisplay != null) {

                    for (j in detailsListResponse.data!!.peghooksDisplay!!.indices) {

                        if (categoryName.equals("Peghooks display")) {
                            if (question.name.equals("Grouped product wise") && detailsListResponse.data!!.peghooksDisplay!!.get(
                                    j
                                ).groupedProductWise!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.peghooksDisplay!!.get(j).groupedProductWise!!.uid
                            }
                            if (question.name.equals("Customer facing") && detailsListResponse.data!!.peghooksDisplay!!.get(
                                    j
                                ).customerFacing!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.peghooksDisplay!!.get(j).customerFacing!!.uid
                            }
                        }


                    }

                }
                if (detailsListResponse.data!!.offersGondola != null) {

                    for (j in detailsListResponse.data!!.offersGondola!!.indices) {

                        if (categoryName.equals("Offers gondola")) {
                            if (question.name.equals("Shelf facing") && detailsListResponse.data!!.offersGondola!!.get(
                                    j
                                ).shelfFacing!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.offersGondola!!.get(j).shelfFacing!!.uid
                            }
                            if (question.name.equals("Correct offer talkers") && detailsListResponse.data!!.offersGondola!!.get(
                                    j
                                ).correctOfferTalkers!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.offersGondola!!.get(j).correctOfferTalkers!!.uid
                            }
                        }


                    }

                }
                if (detailsListResponse.data!!.chiller != null) {

                    for (j in detailsListResponse.data!!.chiller!!.indices) {

                        if (categoryName.equals("Chiller")) {
                            if (question.name!!.contains("Shelf facing") && detailsListResponse.data!!.chiller!!.get(
                                    j
                                ).shelfFacing!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.chiller!!.get(j).shelfFacing!!.uid
                            }
                            if (question.name!!.contains("Grouped product wise") && detailsListResponse.data!!.chiller!!.get(
                                    j
                                ).groupedProductWise!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.chiller!!.get(j).groupedProductWise!!.uid
                            }
                            if (question.name!!.contains("No gaps") && detailsListResponse.data!!.chiller!!.get(
                                    j
                                ).noGaps!!.uid != null
                            ) {
                                question.value =
                                    detailsListResponse.data!!.chiller!!.get(j).noGaps!!.uid
                            }
                        }


                    }

                }

            }

            for (i in detailsListResponse.data!!.planogramSurvey!!.indices) {


                if (detailsListResponse.data!!.planogramSurvey!!.get(i).categoryType!!.uid != null) {
                    if (categoryName!!.lowercase().replace(" ", "").equals(
                            detailsListResponse.data!!.planogramSurvey!!.get(i).name!!.lowercase()
                                .replace(" ", "")
                        )
                    ) {

                        if (question.name.equals("Cleanliness") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).cleanliness!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).cleanliness!!.uid
                        }
                        if (question.name.equals("Subcategory Flow") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).subcatFlow!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).subcatFlow!!.uid
                        }
                        if (question.name!!.contains("Left to right") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).lftRightFrntBck!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).lftRightFrntBck!!.uid
                        }
                        if (question.name.equals("FIFO") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).fifo!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).fifo!!.uid
                        }
                        if (question.name.equals("Empty shelves refilling") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).emptyShelvesRefill!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).emptyShelvesRefill!!.uid
                        }
                        if (question.name.equals("Shelf facing") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).shelfFacing!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).shelfFacing!!.uid
                        }
                        if (question.name!!.contains("Condition tags") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).condTagOffTalkers!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).condTagOffTalkers!!.uid
                        }
                        if (question.name.equals("Shelf strips") && detailsListResponse.data!!.planogramSurvey!!.get(
                                i
                            ).shelfStrips!!.uid != null
                        ) {
                            question.value =
                                detailsListResponse.data!!.planogramSurvey!!.get(i).shelfStrips!!.uid
                        }
                    }

                }

            }


            holder.adapterPlanogramSubCategoryBinding.naLayout.isEnabled = false

            holder.adapterPlanogramSubCategoryBinding.noLayout.isEnabled = false
            holder.adapterPlanogramSubCategoryBinding.yesLayout.isEnabled = false

        } else {
            holder.adapterPlanogramSubCategoryBinding.naLayout.isEnabled = true
            holder.adapterPlanogramSubCategoryBinding.noLayout.isEnabled = true
            holder.adapterPlanogramSubCategoryBinding.yesLayout.isEnabled = true
        }

        if (!question.value.isNullOrEmpty()) {
            if (questionsList.get(position).value.equals("NA")) {
                holder.adapterPlanogramSubCategoryBinding.progressName.text = "NA"

                holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_na_no_plano)
                holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_na_planogram)
                holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
                holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                    applicationContext.resources.getColor(R.color.dark_gray)
                )

            } else if (questionsList.get(position).value.equals("NO") || questionsList.get(position).value.equals(
                    "N"
                )
            ) {
                holder.adapterPlanogramSubCategoryBinding.progressName.text = "NO"

                holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_no_planogram)//
                holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                    applicationContext.getDrawable(R.drawable.no_bg_plano)
                holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
                holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                    applicationContext.resources.getColor(R.color.red_no_plano)
                )
            } else if (questionsList.get(position).value.equals("YES") || questionsList.get(position).value.equals(
                    "Y"
                )
            ) {
                holder.adapterPlanogramSubCategoryBinding.progressName.text = "YES"

                holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_plano)
                holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_no_stroke_plano)
                holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                    applicationContext.getDrawable(R.drawable.bg_yes_layout_plano)
                holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                    applicationContext.resources.getColor(R.color.greenY)
                )

            }
        }

        holder.adapterPlanogramSubCategoryBinding.infoButton.setOnClickListener {
            Tooltip.Builder(holder.adapterPlanogramSubCategoryBinding.infoButton, R.style.Tooltip)
                .setCancelable(true)
                .setDismissOnClick(false)
                .setCornerRadius(20f)
                .setText(question.hintText)
                .build().show()
        }

        holder.adapterPlanogramSubCategoryBinding.naLayout.setOnClickListener {
            holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                applicationContext.getDrawable(R.drawable.bg_na_no_plano)
            holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                applicationContext.getDrawable(R.drawable.bg_na_planogram)
            holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
            holder.adapterPlanogramSubCategoryBinding.progressName.text = "NA"
            holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                applicationContext.resources.getColor(
                    R.color.dark_gray
                )
            )
            questionsList.get(position).value = "NA"
            planogramCateoryCallback.checkAreasToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.countValues(questionsList)
            planogramCateoryCallback.checkCategoriesToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.caluclateScore()
        }
        holder.adapterPlanogramSubCategoryBinding.noLayout.setOnClickListener {
            holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                applicationContext.getDrawable(R.drawable.bg_no_planogram)//
            holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                applicationContext.getDrawable(R.drawable.no_bg_plano)
            holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_end_stroke)
            holder.adapterPlanogramSubCategoryBinding.progressName.text = "NO"
            holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                applicationContext.resources.getColor(
                    R.color.red_no_plano
                )
            )
            questionsList.get(position).value = "N"
            planogramCateoryCallback.checkAreasToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.countValues(questionsList)

            planogramCateoryCallback.checkCategoriesToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.caluclateScore()
        }
        holder.adapterPlanogramSubCategoryBinding.yesLayout.setOnClickListener {
            holder.adapterPlanogramSubCategoryBinding.naLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_plano)
            holder.adapterPlanogramSubCategoryBinding.noLayout.background =
                applicationContext.getDrawable(R.drawable.bg_no_stroke_plano)
            holder.adapterPlanogramSubCategoryBinding.yesLayout.background =
                applicationContext.getDrawable(R.drawable.bg_yes_layout_plano)
            holder.adapterPlanogramSubCategoryBinding.progressName.text = "YES"
            holder.adapterPlanogramSubCategoryBinding.progressName.setTextColor(
                applicationContext.resources.getColor(
                    R.color.greenY
                )
            )
            questionsList.get(position).value = "Y"
            planogramCateoryCallback.checkAreasToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.countValues(questionsList)

            planogramCateoryCallback.checkCategoriesToFocusOn(questionsList, categoryPosition)
            planogramCateoryCallback.caluclateScore()
        }
    }

    override fun getItemCount(): Int {
        return questionsList.size//data.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class ViewHolder(val adapterPlanogramSubCategoryBinding: AdapterPlanogramSubcategoryBinding) :
        RecyclerView.ViewHolder(adapterPlanogramSubCategoryBinding.root)
}

