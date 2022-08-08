package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse

class OnClickStatusClickAdapter(private var categoryList: List<GetImageUrlModelResponse.Category>?, private var callbackInterface: ImagesCardViewAdapterRes.CallbackInterface) : RecyclerView.Adapter<OnClickStatusClickAdapter.ViewHolder>() {
    private lateinit var imagesCardViewAdapter: ImagesCardViewAdapterRes


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_reshootactivity, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var catergoryResponse = categoryList?.get(position)

        holder.catergoryName.text = catergoryResponse?.categoryname

        imagesCardViewAdapter =
            ImagesCardViewAdapterRes(position, catergoryResponse?.imageUrls, callbackInterface)
        holder.imageRecyclerView.layoutManager = LinearLayoutManager(
            ViswamApp.context,
            LinearLayoutManager.HORIZONTAL, false)
        holder.imageRecyclerView.adapter = imagesCardViewAdapter


//        for (i in categoryList?.get(position)?.imageUrls!!.indices){
//            if(categoryList!!.get(position).imageUrls!!.get(i).status.equals("1")){
//                var allGood : Boolean = true
//            }else if(categoryList!!.get(position).imageUrls!!.get(i).status.equals("2")){
//                var reShoot : Boolean = true
//            }else if(categoryList!!.get(position).imageUrls!!.get(i).status.equals("3")){
//                var partiallyApproved : Boolean = true
//            }
//        }

    }

    override fun getItemCount(): Int {
        return categoryList?.size!!
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catergoryName :TextView = itemView.findViewById(R.id.categoryNameSwachRes)
        val imageRecyclerView: RecyclerView = itemView.findViewById(R.id.uploadlayoutrecyclerviewRes)
        val approvedLayout : LinearLayout = itemView.findViewById(R.id.approved_layoutRes)
        val reshootLayout : LinearLayout = itemView.findViewById(R.id.reshoot_layoutRes)
        val partialLayout : LinearLayout = itemView.findViewById(R.id.partially_approved_res)
    }

}