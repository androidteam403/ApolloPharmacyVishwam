package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.DialogLocationListBinding
import com.apollopharmacy.vishwam.databinding.DialogTrainersEmailBinding
import com.apollopharmacy.vishwam.databinding.TrainersEmailItemBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.RegionListResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsCallback
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.model.TrainersEmailIdResponse

class TrainersEmailAdapterForDialog(
    private val applicationContext: Context,
    private var emailList: ArrayList<TrainersEmailIdResponse.Data.ListData.Row.TrainerEmail>,
    private val surveyDetailsCallback: SurveyDetailsCallback,
    private val trainerEmailList: ArrayList<String>
) : RecyclerView.Adapter<TrainersEmailAdapterForDialog.ViewHolder>()  {


    class ViewHolder(val trainersEmailItemBinding: TrainersEmailItemBinding) :
        RecyclerView.ViewHolder(trainersEmailItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val trainersEmailItemBinding = DataBindingUtil.inflate<TrainersEmailItemBinding>(
            LayoutInflater.from(applicationContext),
            R.layout.trainers_email_item,
            parent,
            false
        )
        return TrainersEmailAdapterForDialog.ViewHolder(trainersEmailItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.trainersEmailItemBinding.itemNameTrainer.text = emailList[position].email
        if(trainerEmailList.contains(emailList[position].email)){
            holder.trainersEmailItemBinding.itemNameTrainer.isChecked=true
        }else{
            holder.trainersEmailItemBinding.itemNameTrainer.isChecked=false
        }
        var selectedList = ArrayList<String>()
        holder.trainersEmailItemBinding.itemNameTrainer.setOnClickListener {
            if(holder.trainersEmailItemBinding.itemNameTrainer.isChecked){
                holder.trainersEmailItemBinding.itemNameTrainer.isChecked=true
                selectedList.add(emailList[position].email)

            }else{
                holder.trainersEmailItemBinding.itemNameTrainer.isChecked=false
                selectedList.remove(emailList[position].email)
                surveyDetailsCallback.removeEmail(emailList[position].email)
            }

            surveyDetailsCallback.updateTrainersList(selectedList)
        }
        }

    override fun getItemCount(): Int {
        return emailList.size
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    val storeArrayList: ArrayList<TrainersEmailIdResponse.Data.ListData.Row.TrainerEmail> = emailList
    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {

                    emailList = storeArrayList!!
                } else {
                    var filteredList = ArrayList<TrainersEmailIdResponse.Data.ListData.Row.TrainerEmail>()
                    for (row in storeArrayList!! ) {
                        if (row.email?.contains(charString)!!) {
                            filteredList.add(row)
                        }
                    }
                    emailList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = emailList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
//                siteData =
//                    filterResults.values as ArrayList<StoreListItem>
//                notifyDataSetChanged()
                if (emailList != null && !emailList.isEmpty()) {
                    emailList =
                        filterResults.values as ArrayList<TrainersEmailIdResponse.Data.ListData.Row.TrainerEmail>
                    try {
                        surveyDetailsCallback.noOrdersFound(emailList.size)
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.e("FullfilmentAdapter", e.message!!)
                    }
                } else {
                    surveyDetailsCallback.noOrdersFound(0)
                    notifyDataSetChanged()
                }
            }
        }
    }


}