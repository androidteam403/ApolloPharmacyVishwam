package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterListApnaBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.PreRectroCallback
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListRes
import java.text.SimpleDateFormat

class ListAdapter(private var fragmentList: List<List<GetStorePendingAndApprovedListRes.Get>>,
                  private var context: Context,
                  private var preRectroCallback: PreRectroCallback) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var approvedby:String?=null
    var approvedDate:String?=null
    var partiallyApprovedBy:String?=null
    var partiallyApprovedDate:String?=null
    var reshootBy:String?=null
    var reshootDate:String?=null
    var hierarchialStatus:String?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterListApnaBinding: AdapterListApnaBinding=
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
        val item  = fragmentList.get(position)


        for(i in item.indices){
            if(item.get(i).stage.equals("PRE-RETRO")){
                holder.adapterListApnaBinding.transactionId.text=item.get(i).retroid
                holder.adapterListApnaBinding.storeId.text=item.get(i).store




//                holder.adapterListApnaBinding.storeName.text=fragmentList.get(position).get(i).store
                holder.adapterListApnaBinding.uploadedBy.text=item.get(i).uploadedBy
                val strDate = item.get(i).uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat =
                    SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                holder.adapterListApnaBinding.uploadedOn.text=
                    dateNewFormat.toString()
                if(item.get(i).status.equals("Pending") ||item.get(i).status.equals("Reshoot")){
                    holder.adapterListApnaBinding.preRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.preRetroStatus.text=item.get(i).status
                    holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
                    holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(context.getColor(R.color.light_pink_for_reshoot_pending))
                    holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
                }else if(item.get(i).status.equals("Approved")){
                    holder.adapterListApnaBinding.preRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.preRetroStatus.text=item.get(i).status
                    holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.greenn))
                    holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(context.getColor(R.color.green_bg_for_approved))
                    holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.greenright))
                }else if(item.get(i).status.equals("")){
                    holder.adapterListApnaBinding.preRectroText.setTextColor(context.getColor(R.color.ash_color_for_apna))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.preRetroStatus.text="---"
                    holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.ashh))
                    holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(context.getColor(R.color.grey_bg_for_no_status))
                    holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
                }
            }else if(item.get(i).stage.equals("POST-RETRO")){


                if(item.get(i).status.contains("Pending") || item.get(i).status.contains("Reshoot")){
                    holder.adapterListApnaBinding.postRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.postRetroStatus.text=item.get(i).status
                    holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
                    holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(context.getColor(R.color.light_pink_for_reshoot_pending))
                    holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
                }else if(item.get(i).status.contains("Approved")){
                    holder.adapterListApnaBinding.postRectroText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.postRetroStatus.text=item.get(i).status
                    holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.greenn))
                    holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(context.getColor(R.color.green_bg_for_approved))
                    holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.greenright))
                }else if(item.get(i).status.contains("")){
                    holder.adapterListApnaBinding.postRectroText.setTextColor(context.getColor(R.color.ash_color_for_apna))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.postRetroStatus.text="---"
                    holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.ashh))
                    holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(context.getColor(R.color.grey_bg_for_no_status))
                    holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
                }
            }else if(item.get(i).stage.equals("AFTER-COMPLETION")){


                holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                if(item.get(i).status.contains("Pending") || item.get(i).status.equals("Reshoot")){
                    holder.adapterListApnaBinding.afterCompletionText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.afterCompleteionStatus.text=item.get(i).status
                    holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
                    holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(context.getColor(R.color.light_pink_for_reshoot_pending))
                    holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
                }else if(item.get(i).status.equals("Approved")){
                    holder.adapterListApnaBinding.afterCompletionText.setTextColor(context.getColor(R.color.black))
                    holder.adapterListApnaBinding.afterCompleteionStatus.text=item.get(i).status
                    holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(context.getColor(R.color.greenn))
                    holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(context.getColor(R.color.green_bg_for_approved))
                    holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(context.getDrawable(R.drawable.greenright))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.VISIBLE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.VISIBLE
                    holder.adapterListApnaBinding.uploadedBy.text=item.get(i).uploadedBy
                                   }else if(item.get(i).status.equals("")){
                    holder.adapterListApnaBinding.afterCompletionText.setTextColor(context.getColor(R.color.ash_color_for_apna))
                    holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
                    holder.adapterListApnaBinding.afterCompleteionStatus.text="---"
                    holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(context.getColor(R.color.ashh))
                    holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(context.getColor(R.color.grey_bg_for_no_status))
                    holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
                }
            }

        }










//        holder.adapterListApnaBinding.cardview.setOnClickListener {
//            if( holder.adapterListApnaBinding.preRetroStatus.text.toString().equals("Pending")){
//                preRectroCallback.onClickCardView()
//            }
//        }
        holder.adapterListApnaBinding.preRetroStageLayout.setOnClickListener {
            if(holder.adapterListApnaBinding.preRetroStatus.text.toString().equals("Pending")||holder.adapterListApnaBinding.preRetroStatus.text.toString().equals("---")){

                preRectroCallback.onClickPreRetrPending("isPreRetroStage", holder.adapterListApnaBinding.postRetroStatus.text.toString(), holder.adapterListApnaBinding.transactionId.text.toString(), holder.adapterListApnaBinding.uploadedOn.text.toString(), holder.adapterListApnaBinding.uploadedBy.text.toString(), holder.adapterListApnaBinding.storeId.text.toString(), "newUploadStage", holder.adapterListApnaBinding.preRetroStatus.text.toString(), approvedDate, partiallyApprovedBy, partiallyApprovedDate, reshootBy, reshootDate)
            }else if(holder.adapterListApnaBinding.preRetroStatus.text.toString().equals("Approved")){

                preRectroCallback.onClickPreRetrPending(
                    "isPreRetroStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    holder.adapterListApnaBinding.storeId.text.toString(),
                    "approvedStage",
                    holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootDate,
                    reshootBy
                )
            } else if(holder.adapterListApnaBinding.preRetroStatus.text.toString().equals("Reshoot")){
               preRectroCallback.onClickPreRetrPending(
                   "isPreRetroStage",
                   holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                   holder.adapterListApnaBinding.transactionId.text.toString(),
                   holder.adapterListApnaBinding.uploadedOn.text.toString(),
                   holder.adapterListApnaBinding.uploadedBy.text.toString(),
                   holder.adapterListApnaBinding.storeId.text.toString(),
                   "reshootStage",
                   holder.adapterListApnaBinding.preRetroStatus.text.toString(),
                   approvedDate,
                   partiallyApprovedBy,
                   partiallyApprovedDate,
                   reshootDate,
                   reshootBy
               )
            }

        }

        holder.adapterListApnaBinding.postRetroStageLayout.setOnClickListener {
            if(holder.adapterListApnaBinding.preRetroStatus.text.toString() == "Approved" && holder.adapterListApnaBinding.postRetroStatus.text.toString().contains("Pending"))//doubt about status name
            {

                preRectroCallback.onClickPostRetroPending("isPostRetroStage",  holder.adapterListApnaBinding.postRetroStatus.text.toString(), holder.adapterListApnaBinding.transactionId.text.toString(),holder.adapterListApnaBinding.uploadedOn.text.toString(), holder.adapterListApnaBinding.uploadedBy.text.toString(), holder.adapterListApnaBinding.storeId.text.toString(),"newUploadStage", holder.adapterListApnaBinding.postRetroStatus.text.toString(), approvedDate, partiallyApprovedBy, partiallyApprovedDate, reshootBy, reshootDate)
            } else if(holder.adapterListApnaBinding.postRetroStatus.text.toString().equals("Approved")){


                preRectroCallback.onClickPostRetroPending(
                    "isPostRetroStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    holder.adapterListApnaBinding.storeId.text.toString(),
                    "approvedStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate
                )
          } else if(holder.adapterListApnaBinding.postRetroStatus.text.toString().equals("Reshoot")){
                preRectroCallback.onClickPostRetroPending(
                    "isPostRetroStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    holder.adapterListApnaBinding.storeId.text.toString(),
                    "reshootStage",
                    holder.adapterListApnaBinding.postRetroStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate
                )
            }
        }

        holder.adapterListApnaBinding.afterCompletionStageLayout.setOnClickListener {
            if(holder.adapterListApnaBinding.postRetroStatus.text.toString().equals("Approved")&& holder.adapterListApnaBinding.afterCompleteionStatus.text.toString().contains("Pending"))//doubt about status name
            {
                preRectroCallback.onClickPostRetroPending(
                    "isAfterCompletionStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    holder.adapterListApnaBinding.storeId.text.toString(),
                    "newUploadStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate
                )
            }
            else if(holder.adapterListApnaBinding.afterCompleteionStatus.text.toString().equals("Approved")){
                preRectroCallback.onClickPostRetroPending(
                    "isAfterCompletionStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    holder.adapterListApnaBinding.storeId.text.toString(),
                    "approvedStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate
                )}
            else if(holder.adapterListApnaBinding.afterCompleteionStatus.text.toString().equals("Reshoot")){
                preRectroCallback.onClickPostRetroPending(
                    "isAfterCompletionStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    holder.adapterListApnaBinding.transactionId.text.toString(),
                    holder.adapterListApnaBinding.uploadedOn.text.toString(),
                    holder.adapterListApnaBinding.uploadedBy.text.toString(),
                    holder.adapterListApnaBinding.storeId.text.toString(),
                    "reshootStage",
                    holder.adapterListApnaBinding.afterCompleteionStatus.text.toString(),
                    approvedDate,
                    partiallyApprovedBy,
                    partiallyApprovedDate,
                    reshootBy,
                    reshootDate
                ) }
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