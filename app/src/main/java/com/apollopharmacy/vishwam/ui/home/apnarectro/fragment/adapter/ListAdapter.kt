package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterListApnaBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.PreRectroCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListRes
import com.apollopharmacy.vishwam.util.Utlis
import java.text.SimpleDateFormat
import java.util.Collections


class ListAdapter(
    private var fragmentList: List<List<GetStorePendingAndApprovedListRes.Get>>,
    private var context: Context,
    private var preRectroCallback: PreRectroCallback,
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var retroStore: String? = null
    var approvedDate: String? = null
    var partiallyApprovedBy: String? = null
    var partiallyApprovedDate: String? = null
    var reshootBy: String? = null
    var reshootDate: String? = null
    var hierarchialStatus: String? = null
    var firstStageStatus: String? = null
    var secondStageStatus: String? = null
    var thirdStageStatus: String? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterListApnaBinding: AdapterListApnaBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_list_apna,
                parent,
                false
            )
        return ViewHolder(adapterListApnaBinding)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = fragmentList.get(position)


        for (i in item.indices) {
            if (item.get(i).stage.equals("PRE-RETRO")) {
                retroStore = item.get(i).store
                holder.adapterListApnaBinding.firstStageStatus.text = item.get(i).status
                holder.adapterListApnaBinding.transactionId.text = item.get(i).retroid
                holder.adapterListApnaBinding.storeId.text = item.get(i).store.split("-").get(0)
                holder.adapterListApnaBinding.storeName.text = item.get(i).store.split("-").get(1)
                holder.adapterListApnaBinding.uploadedBy.text = item.get(i).uploadedBy
                val strDate = item.get(i).uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat =
                    SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.adapterListApnaBinding.uploadedOn.text =
                    dateNewFormat.toString()
                if (item.get(i).hierarchystatus.toString()
                        .contains("AWAITING") || item.get(i).status.equals("Reshoot")
                ) {
                    holder.adapterListApnaBinding.preRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                    if (item.get(i).status.equals("Reshoot")){
                        holder.adapterListApnaBinding.preRetroStatus.text =item.get(i).status

                    }else{
                        if (item.get(i).hierarchystatus.toString().toLowerCase().contains("executive")) {
                            holder.adapterListApnaBinding.preRetroStatus.text = "Pending At Executive"
                        }
                        else if (item.get(i).hierarchystatus.toString().equals("AWAITING APPROVAL FROM MANAGER")){
                            holder.adapterListApnaBinding.preRetroStatus.text = "Pending At Manager"

                        }
                        else if (item.get(i).hierarchystatus.toString().replace(" ","").contains("GENERAL")){
                            holder.adapterListApnaBinding.preRetroStatus.text = "Pending At GM"

                        }
                        else if (item.get(i).hierarchystatus.toString().toLowerCase().contains("ceo")){
                            holder.adapterListApnaBinding.preRetroStatus.text = "Pending At CEO"

                        }

                    }
                    holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
                    holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(
                        context.getColor(
                            R.color.light_pink_for_reshoot_pending
                        )
                    )
                    holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(
                        context.getDrawable(
                            R.drawable.clock_small
                        )
                    )
                } else if (item.get(i).hierarchystatus.toString().contains("APPROVED")) {
                    holder.adapterListApnaBinding.preRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.preRetroStatus.text = item.get(i).status
                    holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.greenn))
                    holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(
                        context.getColor(
                            R.color.green_bg_for_approved
                        )
                    )
                    holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(
                        context.getDrawable(
                            R.drawable.greenright
                        )
                    )
                } else if (item.get(i).status.equals("")) {
                    holder.adapterListApnaBinding.preRectroText.setTextColor(context.getColor(R.color.ash_color_for_apna))
                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.preRetroStatus.text = "---"
                    holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.ashh))
                    holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(
                        context.getColor(
                            R.color.grey_bg_for_no_status
                        )
                    )
                    holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(
                        context.getDrawable(
                            R.drawable.clock_small
                        )
                    )
                }
            } else if (item.get(i).stage.equals("POST-RETRO")) {
                holder.adapterListApnaBinding.secondstageStatus.text = item.get(i).status

                if (item.get(i).status.equals("Pending At User")||item.get(i).hierarchystatus.toString().contains("AWAITING") || item.get(i).status.contains("Reshoot")) {

                    holder.adapterListApnaBinding.postRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                    if (item.get(i).status.equals("Reshoot")){
                        holder.adapterListApnaBinding.postRetroStatus.text =item.get(i).status

                    }
                    else if (item.get(i).status.equals("Pending At User")){
                        holder.adapterListApnaBinding.postRetroStatus.text =item.get(i).status

                    }
                    else{
                        if (item.get(i).hierarchystatus.toString().toLowerCase().contains("executive")) {
                            holder.adapterListApnaBinding.postRetroStatus.text = "Pending At Executive"
                        }
                        else if (item.get(i).hierarchystatus.toString().equals("AWAITING APPROVAL FROM MANAGER")){
                            holder.adapterListApnaBinding.postRetroStatus.text = "Pending At Manager"

                        }
                        else if (item.get(i).hierarchystatus.toString().toLowerCase().contains("general")){
                            holder.adapterListApnaBinding.postRetroStatus.text = "Pending At GM"

                        }
                        else if (item.get(i).hierarchystatus.toString().toLowerCase().contains("ceo")){
                            holder.adapterListApnaBinding.postRetroStatus.text = "Pending At CEO"

                        }
                    }
                    holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
                    holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(
                        context.getColor(
                            R.color.light_pink_for_reshoot_pending
                        )
                    )
                    holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(
                        context.getDrawable(
                            R.drawable.clock_small
                        )
                    )
                } else if (item.get(i).hierarchystatus.toString().contains("APPROVED")) {
                    holder.adapterListApnaBinding.postRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.postRetroStatus.text = item.get(i).status
                    holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.greenn))
                    holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(
                        context.getColor(
                            R.color.green_bg_for_approved
                        )
                    )
                    holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(
                        context.getDrawable(
                            R.drawable.greenright
                        )
                    )
                } else if (item.get(i).status.contains("")) {
                    holder.adapterListApnaBinding.postRectroText.setTextColor(context.getColor(R.color.ash_color_for_apna))
                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.postRetroStatus.text = "---"
                    holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.ashh))
                    holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(
                        context.getColor(
                            R.color.grey_bg_for_no_status
                        )
                    )
                    holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(
                        context.getDrawable(
                            R.drawable.clock_small
                        )
                    )
                }
            } else if (item.get(i).stage.equals("AFTER-COMPLETION")) {
                holder.adapterListApnaBinding.thirdstagestatus.text =item.get(i).status

                holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                if (item.get(i).status.equals("Pending At User")||item.get(i).hierarchystatus.toString().contains("AWAITING") || item.get(i).status.contains("Reshoot")) {

                    holder.adapterListApnaBinding.afterCompletionText.setTextColor(
                        context.getColor(
                            R.color.black
                        )
                    )
                    if (item.get(i).status.equals("Reshoot")){
                        holder.adapterListApnaBinding.afterCompleteionStatus.text =item.get(i).status

                    }
                    else if (item.get(i).status.equals("Pending At User")){
                        holder.adapterListApnaBinding.afterCompleteionStatus.text =item.get(i).status

                    }
                    else{
                        if (item.get(i).hierarchystatus.toString().toLowerCase().contains("executive")) {
                            holder.adapterListApnaBinding.afterCompleteionStatus.text = "Pending At Executive"
                        }
                        else if (item.get(i).hierarchystatus.toString().equals("AWAITING APPROVAL FROM MANAGER")){
                            holder.adapterListApnaBinding.afterCompleteionStatus.text = "Pending At Manager"

                        }
                        else if (item.get(i).hierarchystatus.toString().contains("GENERAL")){
                            holder.adapterListApnaBinding.afterCompleteionStatus.text = "Pending At GM"

                        }
                        else if (item.get(i).hierarchystatus.toString().toLowerCase().contains("ceo")){
                            holder.adapterListApnaBinding.afterCompleteionStatus.text = "Pending At CEO"

                        }
                    }
                    holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(
                        context.getColor(
                            R.color.pending_reshoot_color
                        )
                    )
                    holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(
                        context.getColor(R.color.light_pink_for_reshoot_pending)
                    )
                    holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(
                        context.getDrawable(R.drawable.clock_small)
                    )
                } else if (item.get(i).hierarchystatus.toString().contains("APPROVED")) {
                    holder.adapterListApnaBinding.afterCompletionText.setTextColor(
                        context.getColor(
                            R.color.black
                        )
                    )
                    holder.adapterListApnaBinding.afterCompleteionStatus.text = item.get(i).status
                    holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )
                    holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(
                        context.getColor(R.color.green_bg_for_approved)
                    )
                    holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(
                        context.getDrawable(R.drawable.greenright)
                    )

                    holder.adapterListApnaBinding.approvedBy.setText(item.get(i).ceoApprovedBy.toString())
                    holder.adapterListApnaBinding.approvedOn.setText(
                        Utlis.convertRetroDate(
                            item.get(
                                i
                            ).ceoApprovedDate.toString()
                        )
                    )

                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.VISIBLE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.VISIBLE
                    holder.adapterListApnaBinding.uploadedBy.text = item.get(i).uploadedBy
                } else if (item.get(i).status.equals("")) {
                    holder.adapterListApnaBinding.afterCompletionText.setTextColor(
                        context.getColor(
                            R.color.ash_color_for_apna
                        )
                    )
                    holder.adapterListApnaBinding.approvedByLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility = View.GONE
                    holder.adapterListApnaBinding.afterCompleteionStatus.text = "---"
                    holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(
                        context.getColor(
                            R.color.ashh
                        )
                    )
                    holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(
                        context.getColor(R.color.grey_bg_for_no_status)
                    )
                    holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(
                        context.getDrawable(R.drawable.clock_small)
                    )
                }
            }

        }




        holder.adapterListApnaBinding.preRetroStageLayout.setOnClickListener {
            if (holder.adapterListApnaBinding.firstStageStatus.text.toString().contains("Pending") || holder.adapterListApnaBinding.preRetroStatus.text.toString()
                    .equals("---")
            ) {

                preRectroCallback.onClickPreRetrPending(
                    "isPreRetroStage",
                    holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "newUploadStage",
                    holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate, "PRE-RETRO"
                )
            } else if (holder.adapterListApnaBinding.firstStageStatus.text.toString().equals("Approved")
            ) {

                preRectroCallback.onClickPreRetrPending(
                    "isPreRetroStage",
                    holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "approvedStage",
                    holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootDate,
                    reshootBy, "PRE-RETRO"
                )
            } else if (holder.adapterListApnaBinding.firstStageStatus.text.toString().equals("Reshoot")
            ) {
                preRectroCallback.onClickPreRetrPending(
                    "isPreRetroStage",
                    holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "reshootStage",
                    holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootDate,
                    reshootBy, "PRE-RETRO"
                )
            }

        }

        holder.adapterListApnaBinding.postRetroStageLayout.setOnClickListener {
            if (holder.adapterListApnaBinding.firstStageStatus.text.toString() == "Approved" && holder.adapterListApnaBinding.secondstageStatus.text.toString()!!.contains("Pending")
            )//doubt about status name
            {

                preRectroCallback.onClickPostRetroPending(
                    "isPostRetroStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "newUploadStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate, "POST-RETRO"
                )
            } else if (holder.adapterListApnaBinding.secondstageStatus.text.toString()
                    .equals("Approved")
            ) {


                preRectroCallback.onClickPostRetroPending(
                    "isPostRetroStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "approvedStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate, "POST-RETRO"
                )
            } else if (holder.adapterListApnaBinding.secondstageStatus.text.toString()
                    .equals("Reshoot")
            ) {
                preRectroCallback.onClickPostRetroPending(
                    "isPostRetroStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "reshootStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate, "POST-RETRO"
                )
            }
        }

        holder.adapterListApnaBinding.afterCompletionStageLayout.setOnClickListener {
            if (holder.adapterListApnaBinding.postRetroStatus.text.toString().equals("Approved") && holder.adapterListApnaBinding.thirdstagestatus.text.toString()!!.contains("Pending"))//doubt about status name
            {
                preRectroCallback.onClickPostRetroPending(
                    "isAfterCompletionStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "newUploadStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate, "AFTER-COMPLETION"
                )
            } else if (holder.adapterListApnaBinding.thirdstagestatus.text.toString()
                    .equals("Approved")
            ) {
                preRectroCallback.onClickPostRetroPending(
                    "isAfterCompletionStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "approvedStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate, "AFTER-COMPLETION"
                )
            } else if (holder.adapterListApnaBinding.thirdstagestatus.text.toString()
                    .equals("Reshoot")
            ) {
                preRectroCallback.onClickPostRetroPending(
                    "isAfterCompletionStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    retroStore!!,
                    "reshootStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate, "AFTER-COMPLETION"
                )
            }
//           if(holder.adapterListApnaBinding.postRetroStatus.text.toString().equals("Approved")){

//          }
        }

    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(val adapterListApnaBinding: AdapterListApnaBinding) :
        RecyclerView.ViewHolder(adapterListApnaBinding.root)


}