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
import com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.PreRectroFragment

class ListAdapter(private  var fragmentList: ArrayList<PreRectroFragment.AdapterList>,
private var context: Context,
private var preRectroCallback: PreRectroCallback) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {



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
        holder.adapterListApnaBinding.transactionId.text=item.transactionId
        holder.adapterListApnaBinding.storeId.text=item.storeId
        holder.adapterListApnaBinding.storeName.text=item.storeName
        holder.adapterListApnaBinding.uploadedBy.text=item.uploadedBy
        holder.adapterListApnaBinding.uploadedOn.text=item.uploadedOn
        holder.adapterListApnaBinding.transactionId.text=item.transactionId

        if(item.overAlStatus.equals("COMPLETE")){
            holder.adapterListApnaBinding.approvedByLayout.visibility= View.VISIBLE
            holder.adapterListApnaBinding.approvedOnLayout.visibility= View.VISIBLE
            holder.adapterListApnaBinding.approvedOn.text = item.approvedOn
            holder.adapterListApnaBinding.approvedBy.text = item.approvedBy
        }else{
            holder.adapterListApnaBinding.approvedByLayout.visibility= View.GONE
            holder.adapterListApnaBinding.approvedOnLayout.visibility= View.GONE
        }

        if(item.preRetostatus.equals("PENDING") || item.preRetostatus.equals("RE-SHOOT")){
            holder.adapterListApnaBinding.preRetroStatus.text=item.preRetostatus
            holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
            holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(context.getColor(R.color.light_pink_for_reshoot_pending))
            holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
        }else if(item.preRetostatus.equals("APPROVED")|| item.preRetostatus.equals("COMPLETED")){
            holder.adapterListApnaBinding.preRetroStatus.text=item.preRetostatus
            holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.greenn))
            holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(context.getColor(R.color.green_bg_for_approved))
            holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.greenright))
        }else{
            holder.adapterListApnaBinding.preRetroStatus.text=item.preRetostatus
            holder.adapterListApnaBinding.preRetroStatus.setTextColor(context.getColor(R.color.ashh))
            holder.adapterListApnaBinding.preRetroStageLayout.setBackgroundColor(context.getColor(R.color.grey_bg_for_no_status))
            holder.adapterListApnaBinding.preRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.ash_tick))
        }

        if(item.postRetostatus.equals("PENDING") || item.postRetostatus.equals("RE-SHOOT")){
            holder.adapterListApnaBinding.postRetroStatus.text=item.postRetostatus
            holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
            holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(context.getColor(R.color.light_pink_for_reshoot_pending))
            holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
        }else if(item.postRetostatus.equals("APPROVED")|| item.postRetostatus.equals("COMPLETED")){
            holder.adapterListApnaBinding.postRetroStatus.text=item.postRetostatus
            holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.greenn))
            holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(context.getColor(R.color.green_bg_for_approved))
            holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.greenright))
        }else{
            holder.adapterListApnaBinding.postRetroStatus.text=item.postRetostatus
            holder.adapterListApnaBinding.postRetroStatus.setTextColor(context.getColor(R.color.ashh))
            holder.adapterListApnaBinding.postRetroStageLayout.setBackgroundColor(context.getColor(R.color.grey_bg_for_no_status))
            holder.adapterListApnaBinding.postRetroStatusImage.setImageDrawable(context.getDrawable(R.drawable.ash_tick))
        }

        if(item.afterCompletionstatus.equals("PENDING") || item.afterCompletionstatus.equals("RE-SHOOT")){
            holder.adapterListApnaBinding.afterCompleteionStatus.text=item.afterCompletionstatus
            holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(context.getColor(R.color.pending_reshoot_color))
            holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(context.getColor(R.color.light_pink_for_reshoot_pending))
            holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(context.getDrawable(R.drawable.clock_small))
        }else if(item.afterCompletionstatus.equals("APPROVED")|| item.afterCompletionstatus.equals("COMPLETED")){
            holder.adapterListApnaBinding.afterCompleteionStatus.text=item.afterCompletionstatus
            holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(context.getColor(R.color.greenn))
            holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(context.getColor(R.color.green_bg_for_approved))
            holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(context.getDrawable(R.drawable.greenright))
        }else{
            holder.adapterListApnaBinding.afterCompleteionStatus.text=item.afterCompletionstatus
            holder.adapterListApnaBinding.afterCompleteionStatus.setTextColor(context.getColor(R.color.ashh))
            holder.adapterListApnaBinding.afterCompletionStageLayout.setBackgroundColor(context.getColor(R.color.grey_bg_for_no_status))
            holder.adapterListApnaBinding.afterCompletionStatusImage.setImageDrawable(context.getDrawable(R.drawable.ash_tick))
        }

        holder.adapterListApnaBinding.cardview.setOnClickListener {
            if(item.preRetostatus.equals("PENDING")){
                preRectroCallback.onClickCardView()
            }
        }
        holder.adapterListApnaBinding.preRetroStageLayout.setOnClickListener {
            preRectroCallback.onClickPreRetroPending("isPreRetroStage")
        }

        holder.adapterListApnaBinding.postRetroStageLayout.setOnClickListener {
//            if(item.postRetostatus.equals("PENDING")){
                preRectroCallback.onClickPostRetroPending("isPostRetroStage",  fragmentList.get(position).postRetostatus)
//            }
        }

        holder.adapterListApnaBinding.afterCompletionStageLayout.setOnClickListener {
           if(!item.afterCompletionstatus.equals("---")){
            preRectroCallback.onClickPostRetroPending("isAfterCompletionStage", item.postRetostatus)
          }
        }


    }

    override fun getItemCount(): Int {
       return fragmentList.size
    }

    class ViewHolder(val adapterListApnaBinding: AdapterListApnaBinding) :
        RecyclerView.ViewHolder(adapterListApnaBinding.root)


}