package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.RtoPendencyLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.NumberFormat
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

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
    var dashBoardList = ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()





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
        val userData = LoginRepo.getProfile()

        if (Preferences.getAppLevelDesignationQCFail()
                .replace(
                    " ",
                    ""
                )
                .equals("GENERALMANAGER", true)
        ) {
            holder.dashboardSiteBinding.cardView.strokeColor = ContextCompat.getColor(mContext, R.color.qcGmColor)

        }
        else if (Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        ""
                    )
                    .equals("MANAGER", true)
            ) {
            holder.dashboardSiteBinding.cardView.strokeColor = ContextCompat.getColor(mContext, R.color.qcManagerColor)

        }
        else  if (Preferences.getAppLevelDesignationQCFail()
                .replace(
                    " ",
                    ""
                )
                .equals("EXECUTIVE", true)
        ) {
            holder.dashboardSiteBinding.cardView.strokeColor = ContextCompat.getColor(mContext, R.color.qcexecutiveColor)

        }


        rtoManagerAdapter?.notifyDataSetChanged()





        if (dashboardHierarchyList.isNotEmpty()) {
            for (i in dashboardHierarchyList.indices) {
                if (dashboardHierarchyList.get(i).designation.equals(
                        dashboardHistoryList.get(
                            position
                        ).designation
                    ) && dashboardHierarchyList.get(i).employeId.equals(
                        dashboardHistoryList.get(position).empid
                    )
                ) {

                    var item = Getqcfailpendinghistoryforhierarchy()
                    for (i in dashboardHierarchyList) {
                        if (i.employeId!!.equals(dashboardHistoryList.get(position).empid)) {
                            item = i

                        }
                    }

                    dashBoardList= item.pendingcount!!.stream().filter { x -> x.rtocount !=0 || x.rrtocount!=0} // or `Objects::nonNull`
                        .collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>

                    dashBoardList.sortWith { o1: Getqcfailpendinghistoryforhierarchy.Pendingcount, o2: Getqcfailpendinghistoryforhierarchy.Pendingcount ->
                        o2.rtoamount!!.compareTo(
                            o1.rtoamount!!
                        )
                    }
                    if(items.designation.equals("EXECUTIVE", true)
                    ) {
                        rtoSitesAdapter = RtoSitesAdapter(
                            mContext,
                            dashBoardList, mCallBack,
                        )

                        holder.dashboardSiteBinding.gmsitesRecyclerView.adapter = rtoSitesAdapter

                        rtoSitesAdapter!!.notifyDataSetChanged()
                    }else{
                        rtoManagerAdapter = RtoManagerAdapter(
                            mContext,
                            mCallBack,
                            dashboardHistoryList, designation,
                            dashboardHierarchyList,
                            item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                        )
                        holder.dashboardSiteBinding.managerRecyclerView.adapter = rtoManagerAdapter

                    }






                }
            }


        }









        holder.dashboardSiteBinding.generalmanagerArrow.setOnClickListener {
            var isContain: Boolean
            val predicate =
                Predicate { dashboardHierarchyList: Getqcfailpendinghistoryforhierarchy ->
                    dashboardHierarchyList.employeId.equals(items.empid)
                }


            isContain = dashboardHierarchyList.stream().anyMatch(predicate)
            if (isContain) {
                dashboardHistoryList[position].setisClick(true)

                mCallBack.notify(position, false)

            } else {
                dashboardHistoryList[position].setisClick(true)
                items.designation?.let { it1 ->
                    items.empid?.let { it2 ->
                        mCallBack.onClick(
                            position, it1,
                            it2, true
                        )
                    }
                }
            }
            notifyDataSetChanged()

        }

        holder.dashboardSiteBinding.closeArrow.setOnClickListener {
            dashboardHistoryList[position].setisClick(false)
            mCallBack.notify(position, false)
            notifyDataSetChanged()

        }





        if (dashboardHistoryList[position].isClick) {


            holder.dashboardSiteBinding.gmsitesRecyclerView.visibility = View.VISIBLE
            if (Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        ""
                    )
                    .equals("EXECUTIVE", true)
            ) {
                holder.dashboardSiteBinding.rtoHeader.visibility=View.VISIBLE

            }else{
                holder.dashboardSiteBinding.rtoHeader.visibility=View.GONE

            }

            holder.dashboardSiteBinding.closeArrow.visibility = View.VISIBLE

            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.GONE
            holder.dashboardSiteBinding.managerRecyclerView.visibility = View.VISIBLE
            rtoManagerAdapter?.notifyDataSetChanged()
        }
        else {
            holder.dashboardSiteBinding.closeArrow.visibility = View.GONE
            holder.dashboardSiteBinding.gmsitesRecyclerView.visibility = View.GONE
            holder.dashboardSiteBinding.rtoHeader.visibility=View.GONE

            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.VISIBLE
            holder.dashboardSiteBinding.managerRecyclerView.visibility = View.GONE

        }


        if (items.designation?.replace(" ", "").equals("GENERALMANAGER", true)) {
            if (Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        ""
                    )
                    .equals("GENERALMANAGER", true)
            ) {


                holder.dashboardSiteBinding.generalmanagerLayout.setBackgroundColor(
                    Color.parseColor(
                        "#00acae"
                    )
                )
                holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(
                    Color.parseColor(
                        "#00acae"
                    )
                )
                if (userData != null) {
                    holder.dashboardSiteBinding.gmEmpname.setText(items.empid+ " ("+ userData.EMPNAME+")" + "\n" + items.designation)

                }
                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE
            }


        }
        else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
            if (Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        ""
                    )
                    .equals("MANAGER", true)
            ) {
                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE


                if (userData != null) {
                    holder.dashboardSiteBinding.gmEmpname.setText(items.empid+ " ("+ userData.EMPNAME+")" + "\n" + items.designation)

                }
                holder.dashboardSiteBinding.generalmanagerLayout.setBackgroundColor(
                    Color.parseColor(
                        "#606db3"
                    )
                )
                holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(
                    Color.parseColor(
                        "#606db3"
                    )
                )
            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

            }


        }
        else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
            if (Preferences.getAppLevelDesignationQCFail()
                    .replace(
                        " ",
                        ""
                    )
                    .equals("EXECUTIVE", true)
            ) {
                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE
// Assuming holder.dashboardSiteBinding.cardView is your CardView

                if (userData != null) {
                    holder.dashboardSiteBinding.gmEmpname.setText(items.empid+ " ("+ userData.EMPNAME+")" + "\n" + items.designation)

                }
                holder.dashboardSiteBinding.generalmanagerLayout.setBackgroundColor(
                    Color.parseColor(
                        "#d48a2b"
                    )
                )
                holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(
                    Color.parseColor(
                        "#d48a2b"
                    )
                )
            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

            }

        }







        holder.dashboardSiteBinding.sumOfRtValues.setText(NumberFormat.getNumberInstance(Locale.US).format(items.rtoamount!! + items.rrtoamount!!).toString())

        holder.dashboardSiteBinding.rtCount.setText(items.rtocount.toString())
        holder.dashboardSiteBinding.rtovalue.setText(NumberFormat.getNumberInstance(Locale.US).format(items.rtoamount).toString())
        holder.dashboardSiteBinding.rrtoCount.setText(items.rrtocount.toString())
        holder.dashboardSiteBinding.rrtovalue.setText(NumberFormat.getNumberInstance(Locale.US).format(items.rrtoamount).toString())

    }

    override fun getItemCount(): Int {
        return dashboardHistoryList.size
    }

    class ViewHolder(val dashboardSiteBinding: RtoPendencyLayoutBinding) :
        RecyclerView.ViewHolder(dashboardSiteBinding.root)



}



