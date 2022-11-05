package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterRatingReviewBinding
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse
import java.text.SimpleDateFormat

class RatingReviewAdapter(
    val context: Context,
    val remarksList: List<GetImageUrlModelResponse.Remark>?,
    val userDesignation: String?
) : RecyclerView.Adapter<RatingReviewAdapter.ViewHolder>()  {


    var ratingbar: RatingBar? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterRatingReviewBinding: AdapterRatingReviewBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_rating_review,
                parent,
                false
            )
        return ViewHolder(adapterRatingReviewBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var imageUrls = remarksList?.get(position)

        if(userDesignation!=null && userDesignation.equals("MANAGER")||userDesignation.equals("GENERAL MANAGER")||userDesignation.equals("CEO"))
        {
            holder.adapterRatingReviewBinding.apprReviewedByText.setText("Reviewed By: ")
            holder.adapterRatingReviewBinding.apprReviewedDateText.setText("Reviewed Date: ")
        }
        if(imageUrls?.remarks!=null && imageUrls?.remarks!=""){
            holder.adapterRatingReviewBinding.comments.text=imageUrls?.remarks
        }else{
            holder.adapterRatingReviewBinding.comments.text="--"
        }


        if(imageUrls?.userid!=null && imageUrls?.userid!=""){
            var upperCaseApprovedBy: String = imageUrls?.userid!!

            remarksList?.get(position)!!.userid= upperCaseApprovedBy.toUpperCase()
            holder.adapterRatingReviewBinding.userId.text=imageUrls?.userid
        }else{
            holder.adapterRatingReviewBinding.userId.text="--"
        }




        if (imageUrls?.createdDate != "") {
            val strDate = imageUrls?.createdDate
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            val date = dateFormat.parse(strDate)
            val dateNewFormat =
                SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)

            holder.adapterRatingReviewBinding.createdOn.text= dateNewFormat.toString()

        }else{
            holder.adapterRatingReviewBinding.createdOn.text="--"
        }

        ratingbar = holder.adapterRatingReviewBinding.ratingBar
        ratingbar!!.setRating(imageUrls!!.rating!!.toFloat())
//        Toast.makeText(context,""+imageUrls.rating, Toast.LENGTH_SHORT).show()
//        holder.adapterRatingReviewBinding.ratingBar.rating= imageUrls!!.rating!!.toFloat()


    }

    override fun getItemCount(): Int {
      return  remarksList!!.size
    }

    class ViewHolder(val adapterRatingReviewBinding: AdapterRatingReviewBinding ) :
        RecyclerView.ViewHolder(adapterRatingReviewBinding.root)


}