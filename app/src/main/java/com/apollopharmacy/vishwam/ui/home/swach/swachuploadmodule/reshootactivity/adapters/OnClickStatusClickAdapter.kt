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
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.reshootactivity.adapters.ImagesCardViewAdapterRes
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse

class OnClickStatusClickAdapter(
    private var categoryList: List<GetImageUrlModelResponse.Category>?,
    private var callbackInterface: ImagesCardViewAdapterRes.CallbackInterface,
    private var callbackInterfaceOnClick: CallbackInterfaceOnClick,
) : RecyclerView.Adapter<OnClickStatusClickAdapter.ViewHolder>() {
    private lateinit var imagesCardViewAdapter: ImagesCardViewAdapterRes

    var reshootAgainCount: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_reshootactivity, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var catergoryResponse = categoryList?.get(position)

        holder.catergoryName.text = catergoryResponse?.categoryname


        var isApproved = true
        var isReshoot = true
        var isPending = true
        for (i in catergoryResponse?.imageUrls!!){
            if (i.status.equals("0")){
                isApproved = false
                isReshoot = false
            }else if (i.status.equals("1")){
                isPending = false
                isReshoot = false
            }else if (i.status.equals("2")){
                isApproved = false
                isPending = false
            }
        }
        if (isApproved){
            holder.approvedLayout.visibility = View.VISIBLE
            holder.reshootLayout.visibility = View.GONE
            holder.pendingLayout.visibility=View.GONE
            holder.partialLayout.visibility = View.GONE
        }else if (isReshoot){
            holder.reshootLayout.visibility = View.VISIBLE
            holder.approvedLayout.visibility = View.GONE
            holder.pendingLayout.visibility=View.GONE
            holder.partialLayout.visibility = View.GONE
        }else if (isPending){
            holder.pendingLayout.visibility=View.VISIBLE
            holder.reshootLayout.visibility = View.GONE
            holder.approvedLayout.visibility = View.GONE
            holder.partialLayout.visibility = View.GONE
        }else{
            holder.partialLayout.visibility = View.VISIBLE
            holder.pendingLayout.visibility=View.GONE
            holder.reshootLayout.visibility = View.GONE
            holder.approvedLayout.visibility = View.GONE
        }
//
//
//        if (allStatusGood) {
//            holder.approvedLayout.visibility = View.VISIBLE
//        } else if(isPending) {
//            holder.pendingLayout.visibility=View.VISIBLE
//        }else if(isReshoot){
//            holder.reshootLayout.visibility = View.VISIBLE
//        }else if(isPartial){
//            holder.partialLayout.visibility = View.VISIBLE
//        }








        imagesCardViewAdapter =
            ImagesCardViewAdapterRes(position, catergoryResponse?.imageUrls, callbackInterface,categoryList)
        holder.imageRecyclerView.layoutManager = LinearLayoutManager(
            ViswamApp.context,
            LinearLayoutManager.HORIZONTAL, false
        )
        holder.imageRecyclerView.adapter = imagesCardViewAdapter


    }

    override fun getItemCount(): Int {
        return categoryList?.size!!
    }


    interface CallbackInterfaceOnClick {

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catergoryName: TextView = itemView.findViewById(R.id.categoryNameSwachRes)
        val imageRecyclerView: RecyclerView =
            itemView.findViewById(R.id.uploadlayoutrecyclerviewRes)
        val approvedLayout: LinearLayout = itemView.findViewById(R.id.approved_layoutRes)
        val reshootLayout: LinearLayout = itemView.findViewById(R.id.reshoot_layoutRes)
        val pendingLayout: LinearLayout = itemView.findViewById(R.id.pending_layoutRes)
        val partialLayout: LinearLayout = itemView.findViewById(R.id.partially_approved_res)
    }

}