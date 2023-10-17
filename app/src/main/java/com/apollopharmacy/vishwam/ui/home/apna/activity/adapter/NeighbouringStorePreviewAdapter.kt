package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.LayoutNeighbouringStorePreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import com.apollopharmacy.vishwam.util.Utils
import java.text.DecimalFormat

class NeighbouringStorePreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.NeighboringStore>,
) : RecyclerView.Adapter<NeighbouringStorePreviewAdapter.ViewHolder>() {
    class ViewHolder(val layoutNeighbouringStorePreviewBinding: LayoutNeighbouringStorePreviewBinding) :
        RecyclerView.ViewHolder(layoutNeighbouringStorePreviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutNeighbouringStorePreviewBinding =
            DataBindingUtil.inflate<LayoutNeighbouringStorePreviewBinding>(
                LayoutInflater.from(mContext),
                R.layout.layout_neighbouring_store_preview,
                parent,
                false
            )
        return ViewHolder(layoutNeighbouringStorePreviewBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data[position].location!!.name != null) {
            holder.layoutNeighbouringStorePreviewBinding.neighborLocation.setText(data[position].location!!.name)
        } else {
            holder.layoutNeighbouringStorePreviewBinding.neighborLocation.setText("-")
        }
        if (data[position].store != null) {
            holder.layoutNeighbouringStorePreviewBinding.store.setText(data[position].store)
        } else {
            holder.layoutNeighbouringStorePreviewBinding.store.setText("-")
        }
        if (data[position].rent != null) {
            holder.layoutNeighbouringStorePreviewBinding.rent.setText(
                "\u20B9" + DecimalFormat("##,##,##0", Utils.symbols).format(
                    data[position].rent!!.toLong()
                )
            )
        } else {
            holder.layoutNeighbouringStorePreviewBinding.rent.setText("-")
        }
        if (data[position].sales != null) {
            var salesFromResponse = DecimalFormat("###.0##", Utils.symbols).format(data[position].sales)
            var sales = ""
            var salesSplit =
                "${salesFromResponse}".split(
                    "."
                )
            if (salesSplit[1].toInt() > 0) {
                sales =
                    DecimalFormat("##,##,###.0##", Utils.symbols).format(salesFromResponse.toDouble())
            } else {
                sales =
                    DecimalFormat("##,##,###", Utils.symbols).format(salesFromResponse.toDouble())
            }
            holder.layoutNeighbouringStorePreviewBinding.sales.setText(sales)//data.get(position).sales!!.toString()


//            val df = DecimalFormat("##.###").format(data[position].sales!!)
//            val a = DecimalFormat(data[position].sales!!.toString())
//            holder.layoutNeighbouringStorePreviewBinding.sales.setText(df.toString())//data.get(position).sales!!.toString()
//            holder.layoutNeighbouringStorePreviewBinding.sales.setText("\u20B9" + DecimalFormat("##,##,##0").format(
//                data[position].sales!!.toLong()))
        } else {
            holder.layoutNeighbouringStorePreviewBinding.sales.setText("-")
        }
        if (data[position].sqft != null) {

            var sqftFromResponse = DecimalFormat("###.0##", Utils.symbols).format(data[position].sqft)
            var sqft = ""
            var sqftSplit =
                "${sqftFromResponse}".split(
                    "."
                )
            if (sqftSplit[1].toInt() > 0) {
                sqft =
                    DecimalFormat("##,##,###.0##", Utils.symbols).format(sqftFromResponse.toDouble())
            } else {
                sqft =
                    DecimalFormat("##,##,###", Utils.symbols).format(sqftFromResponse.toDouble())
            }
            holder.layoutNeighbouringStorePreviewBinding.sqft.setText(sqft)

            /* val df = DecimalFormat("##.###").format(data[position].sqft!!)
             holder.layoutNeighbouringStorePreviewBinding.sqft.setText(df)*/
        } else {
            holder.layoutNeighbouringStorePreviewBinding.sqft.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}