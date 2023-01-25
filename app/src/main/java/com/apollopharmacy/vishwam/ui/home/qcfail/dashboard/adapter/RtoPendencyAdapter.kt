package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.RtoPendencyLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.DecimalFormat
import java.util.function.Predicate

class RtoPendencyAdapter(
    val mContext: Context,
    val mCallBack: QcDashBoardCallback,
    val designation: String,
    var dashboardHistoryList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,
    var dashboardHierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy>,


    ) :
    RecyclerView.Adapter<RtoPendencyAdapter.ViewHolder>() {
    var rtoManagerAdapter: RtoManagerAdapter? = null
    var rtoSitesAdapter: RtoSitesAdapter? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val dashboardSiteBinding: RtoPendencyLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.rto_pendency_layout,
                parent,
                false
            )
        return ViewHolder(dashboardSiteBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val items = dashboardHistoryList.get(position)




        rtoManagerAdapter?.notifyDataSetChanged()





        if (dashboardHierarchyList.isNotEmpty()) {
            for (i in dashboardHierarchyList.indices) {
                if (dashboardHierarchyList.get(i).designation.equals(dashboardHistoryList.get(
                        position).designation) && dashboardHierarchyList.get(i).employeId.equals(
                        dashboardHistoryList.get(position).empid)
                ) {

                    var item = Getqcfailpendinghistoryforhierarchy()
                    for (i in dashboardHierarchyList) {
                        if (i.designation!!.equals(dashboardHistoryList.get(position).designation)) {
                            item = i

                        }
                    }


                        rtoManagerAdapter = RtoManagerAdapter(mContext,
                            mCallBack,
                            dashboardHistoryList,designation,
                            dashboardHierarchyList,
                            item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)
                        holder.dashboardSiteBinding.managerRecyclerView.adapter = rtoManagerAdapter


                    rtoSitesAdapter = RtoSitesAdapter(mContext, mCallBack,
                        item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)

                    holder.dashboardSiteBinding.gmsitesRecyclerView.adapter = rtoSitesAdapter
                }
            }


        }









        holder.dashboardSiteBinding.generalmanagerArrow.setOnClickListener {
            var isContain:Boolean
            val predicate =
                Predicate { dashboardHierarchyList: Getqcfailpendinghistoryforhierarchy ->
                    dashboardHierarchyList.employeId.equals(items.empid)
                }


            isContain=dashboardHierarchyList.stream().anyMatch(predicate)
            if (isContain){
                dashboardHistoryList[position].setisClick(true)

                mCallBack.notify(position, false)

            }
            else{
                dashboardHistoryList[position].setisClick(true)
                items.designation?.let { it1 ->
                    items.empid?.let { it2 ->
                        mCallBack.onClick(position, it1,
                            it2, true)
                    }
                }
            }




        }

        holder.dashboardSiteBinding.closeArrow.setOnClickListener {
            dashboardHistoryList[position].setisClick(false)
            mCallBack.notify(position, false)

        }





        if (dashboardHistoryList[position].isClick) {


            holder.dashboardSiteBinding.gmsitesRecyclerView.visibility = View.VISIBLE

            holder.dashboardSiteBinding.closeArrow.visibility = View.VISIBLE

            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.GONE
            holder.dashboardSiteBinding.managerRecyclerView.visibility = View.VISIBLE
            rtoManagerAdapter?.notifyDataSetChanged()
        } else {
            holder.dashboardSiteBinding.closeArrow.visibility = View.GONE
            holder.dashboardSiteBinding.gmsitesRecyclerView.visibility = View.GONE

            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.VISIBLE
            holder.dashboardSiteBinding.managerRecyclerView.visibility = View.GONE

        }






        if (items.designation?.replace(" ", "").equals("GENERALMANAGER", true)) {
            if (com.apollopharmacy.vishwam.data.Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        "")
                    .equals("GENERALMANAGER", true)
            ) {
                holder.dashboardSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)
                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE
            }


        } else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
            if (com.apollopharmacy.vishwam.data.Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        "")
                    .equals("MANAGER", true)
            ) {
                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

                holder.dashboardSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)

                holder.dashboardSiteBinding.logo.setImageResource(R.drawable.qc_manager)
                holder.dashboardSiteBinding.generalmanagerLayout.setBackgroundColor(Color.parseColor(
                    "#636fc1"))
                holder.dashboardSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#7e88c7"))
            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

            }


        } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
            if (com.apollopharmacy.vishwam.data.Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        "")
                    .equals("EXECUTIVE", true)
            ) {
                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

                holder.dashboardSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)

                holder.dashboardSiteBinding.logo.setImageResource(R.drawable.qc_executive)
                holder.dashboardSiteBinding.generalmanagerLayout.setBackgroundColor(Color.parseColor(
                    "#f4a841"))
                holder.dashboardSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#f6b968"))
            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

            }

        }

        holder.dashboardSiteBinding.rtocounts.setText(items.rtocount.toString())
        holder.dashboardSiteBinding.rtovalues.setText(DecimalFormat("#,###.00").format(items.rtoamount).toString())
        holder.dashboardSiteBinding.rrrtocounts.setText(items.rrtocount.toString())
        holder.dashboardSiteBinding.rrrtovalues.setText(DecimalFormat("#,###.00").format(items.rrtoamount).toString())


    }


    override fun getItemCount(): Int {
        return dashboardHistoryList.size
    }

    class ViewHolder(val dashboardSiteBinding: RtoPendencyLayoutBinding) :
        RecyclerView.ViewHolder(dashboardSiteBinding.root)
}



