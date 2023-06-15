package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterGetstorepersonhistoryBinding
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelResponse
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class GetStorePersonAdapter(
    private var getStorePersonlist: ArrayList<GetStorePersonHistoryodelResponse.Get>?,
    private var callBackInterface: getStoreHistory
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface getStoreHistory {
        fun onClickStatus(
            position: Int,
            swachhid: String?,
            status: String?,
            approvedDate: String?,
            storeId: String?,
            uploadedDate: String?,
            reshootDate: String?,
            partiallyApprovedDate: String?
        )

        fun onClickReview(swachhid: String?, storeId: String?)
    }

    companion object {
        private const val VIEW_TYPE_DATA = 0;
        const val VIEW_TYPE_PROGRESS = 1;
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = getStorePersonlist?.get(position)
        //if data is load, returns PROGRESSBAR viewtype.
        return if (viewtype?.swachhid.isNullOrEmpty()) {
            VIEW_TYPE_PROGRESS
        } else VIEW_TYPE_DATA

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATA -> {//inflates row layout
                val adapterGetStorePersonHistoryBinding: AdapterGetstorepersonhistoryBinding=
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.adapter_getstorepersonhistory,
                        parent,
                        false
                    )
                return DataViewHolder(adapterGetStorePersonHistoryBinding)
            }
            VIEW_TYPE_PROGRESS -> {//inflates progressbar layout
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.progressbar, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different View type")
        }



//
//        return when (viewType) {
//           VIEW_TYPE_DATA -> {//inflates row layout
//               val view = LayoutInflater.from(parent.context)
//                   .inflate(R.layout.adapter_getstorepersonhistory, parent, false)
//
//               ViewHolder(view)
//            }
//            VIEW_TYPE_PROGRESS -> {//inflates progressbar layout
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.progressbar, parent, false)
//               ViewHolder(view)
//            }
//            else -> throw IllegalArgumentException("Different View type")
//        }
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.adapter_getstorepersonhistory, parent, false)
//
//        return ViewHolder(view)


    }







    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {





        if (holder is DataViewHolder) {
            holder.bind(getItem(position))
        }




    }


    inner class DataViewHolder(val adapterGetStorePersonHistoryBinding: AdapterGetstorepersonhistoryBinding) :
        RecyclerView.ViewHolder(adapterGetStorePersonHistoryBinding.root) {
        fun bind(items: GetStorePersonHistoryodelResponse.Get) {
            bindItems(adapterGetStorePersonHistoryBinding, items, adapterPosition)
        }
    }


    fun getData(): ArrayList<GetStorePersonHistoryodelResponse.Get>? {
        return getStorePersonlist
    }

    fun getItem(position: Int): GetStorePersonHistoryodelResponse.Get {
        return getStorePersonlist!![position]
    }


    override fun getItemCount(): Int {
        return getStorePersonlist?.size!!
    }
    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun bindItems(
        binding: AdapterGetstorepersonhistoryBinding,
        items: GetStorePersonHistoryodelResponse.Get,
        position: Int,

        ) {
        val getStorePerson = getStorePersonlist?.get(position)


        binding.swachidgetStore.text=getStorePerson?.swachhid
        var upperCaseUploadedBy: String = getStorePersonlist?.get(position)?.uploadedBy!!

        getStorePersonlist?.get(position)?.uploadedBy= upperCaseUploadedBy.toUpperCase()

//        getStorePersonlist?.get(position).empName= empName
//        val onDetails: TextView = itemView.findViewById(R.id.date_details)
//        val byDetails: TextView = itemView.findViewById(R.id.by_details)
//        val approvedLayout: LinearLayout = itemView.findViewById(R.id.green_approved_layot)
//        val reshootLayout: LinearLayout = itemView.findViewById(R.id.re_shoot_layout)
//        val pendingpLayout: LinearLayout = itemView.findViewById(R.id.pending_layout)
//        val notUpdatedLayout: LinearLayout = itemView.findViewById(R.id.not_updated_layout)
//        val overAllLayout: LinearLayout = itemView.findViewById(R.id.overallLayout)
//        val partiallyApproved: LinearLayout = itemView.findViewById(R.id.partially_approved_layout)
//        val threeDots: LinearLayout = itemView.findViewById(R.id.three_dots)
//        val swachId: TextView = itemView.findViewById(R.id.swachidgetStore)
//        val empName: TextView = itemView.findViewById(R.id.empNamegetStore)
//        val reviewLayoutAccepted: RelativeLayout = itemView.findViewById(R.id.reviewButtonApproved)
//        val reviewLayoutPending: RelativeLayout = itemView.findViewById(R.id.reviewButtonPending)
//        val reviewLayoutReshoot: RelativeLayout = itemView.findViewById(R.id.reviewButtonReshoot)
//        val reviewLayoutPartial: RelativeLayout = itemView.findViewById(R.id.reviewButtonpartial)

        if (getStorePersonlist?.get(position)?.status.equals("Approved")) {
            binding.greenApprovedLayot.visibility = View.VISIBLE
            binding.pendingLayout.visibility=View.GONE
           binding.partiallyApprovedLayout.visibility=View.GONE
            binding.reShootLayout.visibility=View.GONE
            if (getStorePerson?.uploadedDate == "") {
                binding.dateDetails.text = "--"
            } else {


                val strDate = getStorePerson?.uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                binding.dateDetails.text = dateNewFormat.toString()




                binding.dateDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                binding.byDetails.text = "--"
            } else {
                binding.byDetails.text = getStorePerson?.uploadedBy
//                holder.empName.text = " - " + empname
            }
        }
        else if (getStorePersonlist?.get(position)?.status.equals("Pending")) {
            binding.greenApprovedLayot.visibility = View.GONE
            binding.pendingLayout.visibility = View.VISIBLE
            binding.partiallyApprovedLayout.visibility=View.GONE
            binding.reShootLayout.visibility=View.GONE
            if (getStorePerson?.uploadedDate == "") {
                binding.dateDetails.text = "--"
            } else {

                val strDate = getStorePerson?.uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                binding.dateDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                binding.byDetails.text = "--"
            } else {
                binding.byDetails.text = getStorePerson?.uploadedBy
//                holder.empName.text = " - " +empname
            }


        }
        else if (getStorePersonlist?.get(position)?.status.equals("Reshoot")) {
            binding.greenApprovedLayot.visibility = View.GONE
            binding.pendingLayout.visibility = View.GONE
            binding.partiallyApprovedLayout.visibility=View.GONE
            binding.reShootLayout.visibility=View.VISIBLE
            if (getStorePerson?.uploadedDate == "") {
                binding.dateDetails.text = "--"
            } else {

                val strDate =  getStorePerson?.uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                binding.dateDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                binding.byDetails.text = "--"
            } else {
                binding.byDetails.text = getStorePerson?.uploadedBy
//                holder.empName.text = " - " +empname
            }
        }
//        else if (getStorePersonlist?.get(position)?.status.equals("NOT UPDATED")) {
//            holder.notUpdatedLayout.visibility = View.VISIBLE
//        }
        else if (getStorePersonlist?.get(position)?.status.equals("Partially Approved")) {
            binding.partiallyApprovedLayout.visibility = View.VISIBLE
            binding.greenApprovedLayot.visibility = View.GONE
            binding.pendingLayout.visibility = View.GONE
            binding.reShootLayout.visibility=View.GONE

            if (getStorePerson?.uploadedDate == "") {
                binding.dateDetails.text = "--"
            } else {
                val strDate =  getStorePerson?.uploadedDate
                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
                binding.dateDetails.text = dateNewFormat.toString()
            }

            if (getStorePerson?.uploadedBy == "") {
                binding.byDetails.text = "--"
            } else {
                binding.byDetails.text = getStorePerson?.uploadedBy
//                holder.empName.text = " - " +empname
            }
        }

        binding.overallLayout.setOnClickListener {
            callBackInterface.onClickStatus(
                position,
                getStorePerson?.swachhid,
                getStorePerson?.status,
                getStorePerson?.approvedDate,
                getStorePerson?.storeId,
                getStorePerson?.uploadedDate,
                getStorePerson?.reshootDate,
                getStorePerson?.partiallyApprovedDate,
            )
        }


        binding.reviewButtonApproved.setOnClickListener {
            callBackInterface.onClickReview( getStorePerson?.swachhid,  getStorePerson?.storeId )
        }
//        binding.reviewButtonPending.setOnClickListener {
//            callBackInterface.onClickReview(getStorePerson?.swachhid, getStorePerson?.storeId)
//        }
//        binding.reviewButtonReshoot.setOnClickListener {
//            callBackInterface.onClickReview(getStorePerson?.swachhid, getStorePerson?.storeId)
//        }
//        binding.reviewButtonpartial.setOnClickListener {
//            callBackInterface.onClickReview(getStorePerson?.swachhid, getStorePerson?.storeId)
//        }

    }


//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val onDetails: TextView = itemView.findViewById(R.id.date_details)
//        val byDetails: TextView = itemView.findViewById(R.id.by_details)
//        val approvedLayout: LinearLayout = itemView.findViewById(R.id.green_approved_layot)
//        val reshootLayout: LinearLayout = itemView.findViewById(R.id.re_shoot_layout)
//        val pendingpLayout: LinearLayout = itemView.findViewById(R.id.pending_layout)
//        val notUpdatedLayout: LinearLayout = itemView.findViewById(R.id.not_updated_layout)
//        val overAllLayout: LinearLayout = itemView.findViewById(R.id.overallLayout)
//        val partiallyApproved: LinearLayout = itemView.findViewById(R.id.partially_approved_layout)
//        val threeDots: LinearLayout = itemView.findViewById(R.id.three_dots)
//        val swachId: TextView = itemView.findViewById(R.id.swachidgetStore)
//        val empName: TextView = itemView.findViewById(R.id.empNamegetStore)
//        val reviewLayoutAccepted: RelativeLayout = itemView.findViewById(R.id.reviewButtonApproved)
//        val reviewLayoutPending: RelativeLayout = itemView.findViewById(R.id.reviewButtonPending)
//        val reviewLayoutReshoot: RelativeLayout = itemView.findViewById(R.id.reviewButtonReshoot)
//        val reviewLayoutPartial: RelativeLayout = itemView.findViewById(R.id.reviewButtonpartial)
////
//
//
//
//    }
}