package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterGetStoreDetailsBinding
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyCallback
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyFragment

class GetStoreDetailsAdapter(
    private var context: Context?,
    private var storeDetails: MutableList<StoreDetailsModelResponse.Row>,
    private var  newSurveyFragment: NewSurveyFragment,
    private var  newSurveyCallback: NewSurveyCallback
) : RecyclerView.Adapter<GetStoreDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterGetStoreDetailsBinding: AdapterGetStoreDetailsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_get_store_details,
                parent,
                false
            )
        return ViewHolder(adapterGetStoreDetailsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val siteIdList = storeDetails.get(position)
        holder.adapterGetStoreDetailsBinding.storeId.text = siteIdList.site
        holder.adapterGetStoreDetailsBinding.address.text = siteIdList.city + siteIdList.city
//        holder.adapterGetStoreDetailsBinding.cardViewStore.setOnClickListener {
//            newSurveyCallback.onClickCardView( holder.adapterGetStoreDetailsBinding.address.text.toString());
//        }
    }

    override fun getItemCount(): Int {
        return storeDetails.size
    }


    class ViewHolder(val adapterGetStoreDetailsBinding: AdapterGetStoreDetailsBinding) :
        RecyclerView.ViewHolder(adapterGetStoreDetailsBinding.root)
}