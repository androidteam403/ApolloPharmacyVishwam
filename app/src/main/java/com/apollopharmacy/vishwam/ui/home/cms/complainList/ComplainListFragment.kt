package com.apollopharmacy.vishwam.ui.home.cms.complainList

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.FragmentComplaintsBinding
import com.apollopharmacy.vishwam.databinding.ViewComplaintItemBinding
import com.apollopharmacy.vishwam.databinding.ViewImageShowBinding
import com.apollopharmacy.vishwam.databinding.ViewOrderStatusBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.MainActivity.isSuperAdmin
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.util.*
import com.apollopharmacy.vishwam.util.Utils.getDateDifference
import com.bumptech.glide.Glide
import java.util.*

class ComplainListFragment : BaseFragment<ComplainListViewModel, FragmentComplaintsBinding>(),
    ImageClickListener, ComplaintListCalendarDialog.DateSelected {

    var isFromDateSelected: Boolean = false

    lateinit var storeData: LoginDetails.StoreData

    // var TicketHistorydata:ArrayList<NewTicketHistoryResponse.Row>()


    override val layoutRes: Int
        get() = R.layout.fragment_complaints

    lateinit var adapter: ApproveRecyclerView
    lateinit var userData: LoginDetails

    override fun retrieveViewModel(): ComplainListViewModel {
        return ViewModelProvider(this).get(ComplainListViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setup() {
        viewBinding.viewmodel = viewModel
        val siteId = Preferences.getSiteId()
        userData = LoginRepo.getProfile()!!
        viewBinding.fromDateText.setText(Utils.getCurrentDate())
        viewBinding.toDateText.setText(Utils.getCurrentDate())

        viewBinding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClick()
        })


        if (NetworkUtil.isNetworkConnected(requireContext())) {

            // var tempfromDate = viewBinding.fromDateText.text.toString()
            // var temptoDate = viewBinding.toDateText.text.toString()

            var fromDate = Utils.getticketlistfiltersdate(viewBinding.fromDateText.text.toString())
            var toDate = Utils.getticketlistfiltersdate(viewBinding.toDateText.text.toString())

            /* var format1: SimpleDateFormat? = null
             format1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
             var date: Date? = null
             date = format1.parse(fromDate)
             fromDate=format1.format(date)
             date=format1.parse(toDate)
             toDate=format1.format(date)*/


//            if (siteId.isEmpty()) {
//                Toast.makeText(
//                    requireContext(),
//                    resources.getString(R.string.label_check_site),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//            else {
                Utlis.showLoading(requireContext())
                if (isSuperAdmin) {
                    /* viewModel.getComplainListOrder(
                         RequestComplainList(
                             "",
                             Utils.getOneWeekBackDate(),
                             Utils.getCurrentDate(),
                             userData.EMPID
                         )
                     )*/


                    viewModel.getNewticketlist(
                        RequestComplainList(
                            //"11002",
                            Preferences.getSiteId(),
                            // viewBinding.fromDateText.text.toString(),
                            // viewBinding.toDateText.text.toString(),
                            fromDate,
                            toDate,
                            userData.EMPID
                        )
                    )
                    /*  viewModel.getNewticketHistory(
                          RequestTicketHistory(
                              1,
                              100,
                              "B7452223034D6B13977B10F7FCD638E1"
                          )
                      )*/
                } else {
                    /* viewModel.getComplainListOrder(
                         RequestComplainList(
                             siteId,
                             Utils.getOneWeekBackDate(),
                             Utils.getCurrentDate(),
                             ""
                         )
                     )*/

                    viewModel.getNewticketlist(
                        RequestComplainList(
                            // "11002",
                            Preferences.getSiteId(),
                            // viewBinding.fromDateText.text.toString(),
                            //  viewBinding.toDateText.text.toString(),
                            fromDate,
                            toDate,
                            userData.EMPID
                        )
                    )

                    /* viewModel.getNewticketHistory(
                         RequestTicketHistory(
                             1,
                             100,
                           "B7452223034D6B13977B10F7FCD638E1"
                         )
                     )*/
                }
         //   }
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        //newticketlist..........
        viewModel.newcomplainLiveData.observe(viewLifecycleOwner, Observer {
            Utlis.hideLoading()
            if (viewBinding.pullToRefresh.isRefreshing) {
                viewBinding.pullToRefresh.isRefreshing = false
            }
            if (it.size == 0) {
                viewBinding.recyclerViewApproved.visibility = View.GONE
                viewBinding.emptyList.visibility = View.VISIBLE
            } else {
                viewBinding.emptyList.visibility = View.GONE
                viewBinding.recyclerViewApproved.visibility = View.VISIBLE

                /*var newticketlist=ArrayList<ResponseNewTicketlist.Row>()
                newticketlist=it
                for(item in newticketlist)
                {
                    //var tickethistory=ResponseNewTicketlist.NewTicketHistoryResponse
                    item.Tickethistory=
                }*/

                adapter = ApproveRecyclerView(it, this)
                viewBinding.recyclerViewApproved.adapter = adapter
            }
        })

        //tickethistory api response...............................................................
        viewModel.newtickethistoryLiveData.observe(viewLifecycleOwner, Observer {
            Utlis.hideLoading()
            // if (it.size == 0) {
            //  } else {

            var ticketHistory: ArrayList<ResponseNewTicketlist.NewTicketHistoryResponse.Row>
            ticketHistory = it.data.listData.rows
            for (item in ticketHistory) {
                var uid = item.ticket.uid
                var itemPos: Int = -1
                for (ticketrow in adapter.orderData) {
                    itemPos++
                    if (uid.equals(ticketrow.uid)) {
                        item.status = ticketrow.status.name

                        adapter.orderData[itemPos].Tickethistory = it
                        // ticketrow.Tickethistory.data.listData.rows = it
                        /* if (ticketrow.Tickethistory.data.listData.rows.size > 0) {
                             for (statusitem in ticketrow.Tickethistory.data.listData.rows) {
                                 statusitem.status.name = ticketrow.status.name
                             }
                         }*/

                        break
                    }

                }
            }
            //  adapter.notifyAdapter()
            adapter.notifyDataSetChanged()
        })


        /* viewModel.complainLiveData.observe(viewLifecycleOwner, Observer {
             Utlis.hideLoading()
             if (it.size == 0) {
                 viewBinding.recyclerViewApproved.visibility = View.GONE
                 viewBinding.emptyList.visibility = View.VISIBLE
             } else {
                 viewBinding.emptyList.visibility = View.GONE
                 viewBinding.recyclerViewApproved.visibility = View.VISIBLE
                 adapter = ApproveRecyclerView(it, this)
                 viewBinding.recyclerViewApproved.adapter = adapter
             }
         })*/
        viewModel.command.observe(viewLifecycleOwner, Observer {
            Utlis.hideLoading()
            when (it) {
                is CmsCommand.ShowToast -> {
                    viewBinding.emptyList.visibility = View.VISIBLE
                }
            }
        })

        viewBinding.fromDateText.setOnClickListener {
            isFromDateSelected = true
            openDateDialog()
        }

        viewBinding.toDateText.setOnClickListener {
            isFromDateSelected = false
            openDateDialog()
        }

        viewBinding.submit.setOnClickListener {
            submitClick()
        }
    }

    fun submitClick(){
        if (NetworkUtil.isNetworkConnected(requireContext())) {
//                if (siteId.isEmpty()) {
//                    Toast.makeText(
//                        requireContext(),
//                        resources.getString(R.string.label_check_site),
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                } else {
            //  val fromDate:String?=null
            //  val toDate:String?=null
            var fromDate = viewBinding.fromDateText.text.toString()
            var toDate = viewBinding.toDateText.text.toString()
            //var format1 : SimpleDateFormat? = null
            /*format1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var date: Date? = null
            date = format1!!.parse(fromDate)
            fromDate= format1!!.format(date)
            date= format1!!.parse(toDate)
            toDate= format1!!.format(date)*/

            if (getDateDifference(fromDate, toDate) > 0) {
                if(!viewBinding.pullToRefresh.isRefreshing)
                    Utlis.showLoading(requireContext())

                if (isSuperAdmin) {
                    /* viewModel.getComplainListOrder(
                         RequestComplainList(
                             "",
                             viewBinding.fromDateText.text.toString(),
                             viewBinding.toDateText.text.toString(),
                             userData.EMPID
                         )
                     )*/

                    viewModel.getNewticketlist(
                        RequestComplainList(
                            //"11002",
                            Preferences.getSiteId(),
                            Utils.getticketlistfiltersdate(viewBinding.fromDateText.text.toString()),
                            Utils.getticketlistfiltersdate(viewBinding.toDateText.text.toString()),

                            userData.EMPID
                        )
                    )

                } else {
                    /* viewModel.getComplainListOrder(
                         RequestComplainList(
                             siteId,
                             viewBinding.fromDateText.text.toString(),
                             viewBinding.toDateText.text.toString(),
                             ""
                         )
                     )*/

                    viewModel.getNewticketlist(
                        RequestComplainList(
                            //"11002",
                            Preferences.getSiteId(),
                            Utils.getticketlistfiltersdate(viewBinding.fromDateText.text.toString()),
                            Utils.getticketlistfiltersdate(viewBinding.toDateText.text.toString()),
                            //fromDate,
                            // toDate,
                            userData.EMPID
                        )
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.label_check_dates),
                    Toast.LENGTH_SHORT
                ).show()
            }
            //  }
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(viewBinding.fromDateText.text.toString(),false,viewBinding.fromDateText.text.toString())
            }.show(childFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(viewBinding.toDateText.text.toString(),true,viewBinding.fromDateText.text.toString())
            }.show(childFragmentManager, "")
        }
    }

    /* class ApproveRecyclerView(
         var orderData: List<CmsResposeV2.DataItem>,
         val imageClickListener: ImageClickListener,
     ) :
         SimpleRecyclerView<ViewComplaintItemBinding, CmsResposeV2.DataItem>(
             orderData,
             R.layout.view_complaint_item
         ) {
         var orderItemsId = ArrayList<String>()

         @SuppressLint("SetTextI18n")
         override fun bindItems(
             binding: ViewComplaintItemBinding,
             items: CmsResposeV2.DataItem,
             position: Int,
         ) {
             binding.ticketNumber.text =
                 context.resources?.getString(R.string.label_complaint_ticket_number) + " " + items.ticketNo
             binding.regDate.text =
                 context.resources?.getString(R.string.label_registered_date) + " " + Utlis.cmsComplaintDateFormat(
                     items.regtime
                 )

             binding.staffNameText.setText(items.staffName)
             binding.departmentName.text = items.problemType
             binding.problemSinceText.text = Utils.getCustomDate(items.problemSince)
             binding.complainDetails.text =
                 items.problemDrescription?.trim()?.replace("\\s+".toRegex(), " ")

             binding.pendingLayout.setOnClickListener {
                 imageClickListener.onComplaintItemClick(position, orderData)
             }
             if (items.isExpanded) {
                 binding.extraData.visibility = View.VISIBLE
                 binding.arrow.visibility = View.GONE
                 binding.arrowClose.visibility = View.VISIBLE
             } else {
                 binding.extraData.visibility = View.GONE
                 binding.arrow.visibility = View.VISIBLE
                 binding.arrowClose.visibility = View.GONE
             }

             val remarkList = ArrayList<CmsResposeV2.TrackingDetailsItem>()
             remarkList.add(
                 CmsResposeV2.TrackingDetailsItem(
                     items.staffName,
                     items.regtime,
                     "",
                     "OPEN",
                     items.staffName,
                     ""
                 )
             )

             if (remarkList.isNullOrEmpty()) binding.remarksLayout.visibility =
                 View.GONE
             else binding.remarksLayout.visibility = View.VISIBLE

             if (orderData[position].trackingDetails.size > 0) {
                 for (item in orderData[position].trackingDetails) {
                     if (!item.status.equals("")) {
                         remarkList.add(item)
                     }
                 }
             }

             binding.remarksRecycleView.adapter = RemarkAdapter(remarkList)

             if (orderData[position].imageUrl.isNullOrEmpty()) binding.attacheImages.visibility =
                 View.GONE
             else binding.attacheImages.visibility = View.VISIBLE
             binding.addedImagesRecyclerView.adapter =
                 ImageRecyclerView(orderData[position].imageUrl, imageClickListener)

             if (items.statusDrescription.equals("New")) {
                 binding.cmpStatus.text =
                     context.resources?.getString(R.string.label_status) + " OPEN"
             } else {
                 binding.cmpStatus.text =
                     context.resources?.getString(R.string.label_status) + " " + items.statusDrescription
             }
 //            binding.cmpStatus.text =
 //                context.resources?.getString(R.string.label_status) + " " + remarkList.get(
 //                    remarkList.size - 1
 //                ).status?.toUpperCase()

             val complaintStatus = remarkList.get(
                 remarkList.size - 1
             ).status
             if (complaintStatus?.toUpperCase().equals("CLOSED") || complaintStatus?.toUpperCase()
                     .equals("ACCEPT")
             ) {
                 binding.closedDate.visibility = View.VISIBLE
                 binding.closedDate.text = "Closed Date: " + Utlis.cmsComplaintDateFormat(
                     remarkList.get(
                         remarkList.size - 1
                     ).dttime
                 )
             } else {
                 binding.closedDate.visibility = View.GONE
             }
         }

         fun notifyAdapter(userList: List<CmsResposeV2.DataItem>) {
             this.orderData = userList
             notifyDataSetChanged()
         }

         class RemarkAdapter(val remarkList: ArrayList<CmsResposeV2.TrackingDetailsItem>) :
             RecyclerView.Adapter<RemarkAdapter.ViewHolder>() {
             override fun onCreateViewHolder(
                 parent: ViewGroup,
                 viewType: Int,
             ): ViewHolder {
                 val view = DataBindingUtil.inflate(
                     LayoutInflater.from(parent.context),
                     R.layout.view_order_status,
                     parent,
                     false
                 ) as ViewOrderStatusBinding
                 return ViewHolder(view)
             }

             override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                 holder.bind(remarkList[position])
                 if (position == (remarkList.size - 1)) {
                     holder.remarkBinding.viewCreated.visibility = View.GONE
                     holder.remarkBinding.viewHoldProcess.visibility = View.GONE
                     holder.remarkBinding.viewProcess.visibility = View.GONE
                     holder.remarkBinding.viewClosed.visibility = View.GONE
                     holder.remarkBinding.viewLOne.visibility = View.GONE
                     holder.remarkBinding.viewLTwo.visibility = View.GONE
                     holder.remarkBinding.viewLThree.visibility = View.GONE
                     holder.remarkBinding.viewReopenRequest.visibility = View.GONE
                     holder.remarkBinding.viewAcknowledged.visibility = View.GONE
                     holder.remarkBinding.viewReopenedForProcess.visibility = View.GONE
                     holder.remarkBinding.viewReOpen.visibility = View.GONE
                     holder.remarkBinding.viewAccept.visibility = View.GONE
                 }
             }

             override fun getItemCount(): Int {
                 return remarkList.size
             }

             inner class ViewHolder(val remarkBinding: ViewOrderStatusBinding) :
                 RecyclerView.ViewHolder(remarkBinding.root) {
                 fun bind(remarks: CmsResposeV2.TrackingDetailsItem) {
                     if (remarks.status?.toUpperCase().equals("OPEN")) {
                         remarkBinding.createdLayout.visibility = View.VISIBLE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.createdPerson.setText(
                             "Created by " + remarks.processingStaffName
                         )
                         remarkBinding.createdDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                     } else if (remarks.status?.toUpperCase().equals("HOLD AND PROCESSE")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.VISIBLE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.holdProcessPerson.setText(
                             "Hold and Processed by " + remarks.processingStaffName
                         )
                         remarkBinding.holdProcessDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.holdProcessStatus.visibility = View.GONE
                         else {
                             remarkBinding.holdProcessStatus.visibility = View.VISIBLE
                             remarkBinding.holdProcessStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("PROCESSING")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.VISIBLE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.processPerson.setText(
                             "Processed by " + remarks.processingStaffName
                         )
                         remarkBinding.processDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.processStatus.visibility = View.GONE
                         else {
                             remarkBinding.processStatus.visibility = View.VISIBLE
                             remarkBinding.processStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("CLOSED")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.VISIBLE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.closedPerson.setText(
                             "Closed by " + remarks.processingStaffName
                         )
                         remarkBinding.closedDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.closedStatus.visibility = View.GONE
                         else {
                             remarkBinding.closedStatus.visibility = View.VISIBLE
                             remarkBinding.closedStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("L1 ESCALATION")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.VISIBLE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.lOnePerson.setText(
                             "L1 Escalated by " + remarks.processingStaffName
                         )
                         remarkBinding.lOneDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.lOneStatus.visibility = View.GONE
                         else {
                             remarkBinding.lOneStatus.visibility = View.VISIBLE
                             remarkBinding.lOneStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("L2 ESCALATION")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.VISIBLE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.lTwoPerson.setText(
                             "L2 Escalated by " + remarks.processingStaffName
                         )
                         remarkBinding.lTwoDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.lTwoStatus.visibility = View.GONE
                         else {
                             remarkBinding.lTwoStatus.visibility = View.VISIBLE
                             remarkBinding.lTwoStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("L3 ESCALATION")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.VISIBLE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.lThreePerson.setText(
                             "L3 Escalated by " + remarks.processingStaffName
                         )
                         remarkBinding.lThreeDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.lThreeStatus.visibility = View.GONE
                         else {
                             remarkBinding.lThreeStatus.visibility = View.VISIBLE
                             remarkBinding.lThreeStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("REOPEN REQUEST")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.VISIBLE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.reopenRequestPerson.setText(
                             "Reopened by " + remarks.processingStaffName
                         )
                         remarkBinding.reopenRequestDate.setText(
                             "on " + Utlis.convertCmsDate(
                                 remarks.dttime
                             )
                         )
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.reopenRequestStatus.visibility = View.GONE
                         else {
                             remarkBinding.reopenRequestStatus.visibility = View.VISIBLE
                             remarkBinding.reopenRequestStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("ACKNOWLEDGED")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.VISIBLE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.acknowledgedPerson.setText(
                             "Acknowledged by " + remarks.processingStaffName
                         )
                         remarkBinding.acknowledgedDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.acknowledgedStatus.visibility = View.GONE
                         else {
                             remarkBinding.acknowledgedStatus.visibility = View.VISIBLE
                             remarkBinding.acknowledgedStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("REOPENED FOR REPROCESS")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.VISIBLE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.reopenedForProcessPerson.setText(
                             "Reopened for Reprocess by " + remarks.processingStaffName
                         )
                         remarkBinding.reopenedForProcessDate.setText(
                             "on " + Utlis.convertCmsDate(
                                 remarks.dttime
                             )
                         )
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.reopenedForProcessStatus.visibility = View.GONE
                         else {
                             remarkBinding.reopenedForProcessStatus.visibility = View.VISIBLE
                             remarkBinding.reopenedForProcessStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("RE-OPEN")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.VISIBLE
                         remarkBinding.acceptLayout.visibility = View.GONE

                         remarkBinding.reOpenPerson.setText(
                             "Reopened by " + remarks.processingStaffName
                         )
                         remarkBinding.reOpenDate.setText(
                             "on " + Utlis.convertCmsDate(
                                 remarks.dttime
                             )
                         )
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.reOpenStatus.visibility = View.GONE
                         else {
                             remarkBinding.reOpenStatus.visibility = View.VISIBLE
                             remarkBinding.reOpenStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     } else if (remarks.status?.toUpperCase().equals("ACCEPT")) {
                         remarkBinding.createdLayout.visibility = View.GONE
                         remarkBinding.holdProcessLayout.visibility = View.GONE
                         remarkBinding.processLayout.visibility = View.GONE
                         remarkBinding.closedLayout.visibility = View.GONE
                         remarkBinding.lOneLayout.visibility = View.GONE
                         remarkBinding.lTwoLayout.visibility = View.GONE
                         remarkBinding.lThreeLayout.visibility = View.GONE
                         remarkBinding.reopenRequestLayout.visibility = View.GONE
                         remarkBinding.acknowledgedLayout.visibility = View.GONE
                         remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                         remarkBinding.reOpenLayout.visibility = View.GONE
                         remarkBinding.acceptLayout.visibility = View.VISIBLE

                         remarkBinding.acceptPerson.setText(
                             "Accepted by " + remarks.processingStaffName
                         )
                         remarkBinding.acceptDate.setText(
                             "on " + Utlis.convertCmsDate(
                                 remarks.dttime
                             )
                         )
                         if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.acceptStatus.visibility = View.GONE
                         else {
                             remarkBinding.acceptStatus.visibility = View.VISIBLE
                             remarkBinding.acceptStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }
                     }
                 }
             }
         }
     }

     //Image Recyclerview
     class ImageRecyclerView(
         var orderData: ArrayList<CmsResposeV2.ImageUrl>,
         val imageClickListener: ImageClickListener,
     ) :
         SimpleRecyclerView<ViewImageShowBinding, CmsResposeV2.ImageUrl>(
             orderData,
             R.layout.view_image_show
         ) {
         override fun bindItems(
             binding: ViewImageShowBinding,
             items: CmsResposeV2.ImageUrl,
             position: Int,
         ) {
             Glide.with(context).load(items.url).placeholder(R.drawable.thumbnail_image)
                 .into(binding.image)
             binding.image.setOnClickListener {
                 imageClickListener.onItemClick(position, items.url)
             }
         }
     }

     override fun onItemClick(position: Int, imagePath: String) {
         PhotoPopupWindow(context, R.layout.layout_image_fullview, view, imagePath, null)
     }

     override fun onComplaintItemClick(position: Int, orderData: List<CmsResposeV2.DataItem>) {
         var curStatus: Boolean = orderData.get(position).isExpanded
         orderData.forEach {
             it.isExpanded = false
         }
         orderData.get(position).isExpanded = !curStatus
         adapter.notifyAdapter(orderData)
     }

     override fun selectedDateTo(dateSelected: String, showingDate: String) {
         if (isFromDateSelected) {
             isFromDateSelected = false
             viewBinding.fromDateText.setText(showingDate)
         } else {
             viewBinding.toDateText.setText(showingDate)
         }
     }
 }

 interface ImageClickListener {
     fun onItemClick(position: Int, imagePath: String)

     fun onComplaintItemClick(position: Int, orderData: List<CmsResposeV2.DataItem>)
 }*/


    //new ticket list data recycler views..............
    class ApproveRecyclerView(
        var orderData: ArrayList<ResponseNewTicketlist.Row>,
        val imageClickListener: ImageClickListener,

        ) :
        SimpleRecyclerView<ViewComplaintItemBinding, ResponseNewTicketlist.Row>(
            orderData,
            R.layout.view_complaint_item,
        ) {
        var orderItemsId = ArrayList<String>()
        var tickethistory = ArrayList<NewTicketHistoryResponse.Row>()

        //var historyitem : NewTicketHistoryResponse.Row()
        var tickethistoryresponsenew = ArrayList<NewTicketHistoryResponse.Row>()

        @SuppressLint("SetTextI18n")
        override fun bindItems(
            binding: ViewComplaintItemBinding,
            items: ResponseNewTicketlist.Row,
            position: Int,

            ) {
            binding.ticketNumber.text =
                context.resources?.getString(R.string.label_complaint_ticket_number) + " " + items.ticket_id
            binding.regDate.text =
                context.resources?.getString(R.string.label_registered_date) + " " + items.created_time?.let {
                    Utlis.cmsComplaintDateFormat(
                        it
                    )
                }

            lateinit var userData: LoginDetails
            userData = LoginRepo.getProfile()!!

//            lateinit var StoreData: StoreData
//            StoreData = LoginRepo.getStoreData()!!
            if(items.created_id.last_name != null)
                binding.staffNameText.text = items.created_id.first_name + " "+ items.created_id.last_name
            else
                binding.staffNameText.text = items.created_id.first_name
            binding.departmentName.text = items.department.name
            binding.problemSinceText.text = Utils.getCustomDate(items.created_time)
            binding.complainDetails.text =
                items.description?.trim()?.replace("\\s+".toRegex(), " ")

            binding.siteid.text = items.site.site+"-"+items.site.store_name
//            binding.siteName.text = items.site.store_name



            binding.pendingLayout.setOnClickListener {
                imageClickListener.onComplaintItemClick(position, orderData)

            }
            if (items.isExpanded) {

                binding.extraData.visibility = View.VISIBLE
                binding.arrow.visibility = View.GONE
                binding.arrowClose.visibility = View.VISIBLE

                /*   tickethistoryresponsenew=
                       orderData[position].uid?.let { imageClickListener.gettickethistory(it) }!!;*/

            } else {
                binding.extraData.visibility = View.GONE
                binding.arrow.visibility = View.VISIBLE
                binding.arrowClose.visibility = View.GONE
            }

            /*   val remarkList = ArrayList<ResponseNewTicketlist.Row>()
               remarkList.add(
                   ResponseNewTicketlist.Row(
                       items.uid,
                       items.description,
                       items.priority,
                       items.ticket_id,
                       items.parent,
                       items.cluster,
                       items.created_id,
                       items.category,
                       items.department,
                       items.region,
                       items.reason,
                       items.level,
                       items.status,
                       items.site,
                       items.subcategory,
                       items.user,
                       items.created_time,
                       items.modified_time,
                       items.closed_date,
                       items.problem_images,
                       items.isExpanded,
                       items.Tickethistory,

                   )
               )*/

//        if(Utils.historytransferredarray.size > 0)
//          { tickethistoryresponsenew=Utils.historytransferredarray
//              for(item in tickethistoryresponsenew)
//              {
//                  //var statu
//                  item.status.name= orderData[position].status.name
//
//              }
//          }

            if (items.Tickethistory == null) {
                binding.remarksLayout.visibility = View.GONE
            } else {
                if (items.Tickethistory.data.listData.rows.isNullOrEmpty()) binding.remarksLayout.visibility =
                    View.GONE
                else binding.remarksLayout.visibility = View.VISIBLE
                binding.remarksRecycleView.adapter =
                    RemarkAdapter(items.Tickethistory.data.listData.rows)
            }


            var problemimages = ArrayList<ResponseNewTicketlist.Image>()
            if (orderData[position].problem_images == null) {
            } else {
                problemimages = orderData[position].problem_images!!.images
            }

            binding.attacheImages.visibility = View.VISIBLE

            /* binding.addedImagesRecyclerView.adapter =
                ImageRecyclerView(problemimages, imageClickListener)*/

            /* if (problemimages.isNullOrEmpty()) binding.attacheImages.visibility =
                 View.GONE
             else binding.attacheImages.visibility = View.VISIBLE*/

            if (problemimages.isNullOrEmpty()) {
            } else {
                binding.addedImagesRecyclerView.adapter =
                    ImageRecyclerView(problemimages, imageClickListener)
            }

            binding.cmpStatus.text =
                " " + items.status.name
            binding.cmpStatus.setTextColor(Color.parseColor(items.status.background_color))
            /*if (items.status.name.equals("New")) {
                binding.cmpStatus.text =
                    context.resources?.getString(R.string.label_status) + " New"
            } else {
                binding.cmpStatus.text =
                    context.resources?.getString(R.string.label_status) + " " + items.description
            }*/
//            binding.cmpStatus.text =
//                context.resources?.getString(R.string.label_status) + " " + remarkList.get(
//                    remarkList.size - 1
//                ).status?.toUpperCase()

            val complaintStatus = orderData[position].status.name
            if (complaintStatus?.toUpperCase().equals("Closed") || complaintStatus?.toUpperCase()
                    .equals("In Progress")
            ) {
                binding.closedDate.visibility = View.VISIBLE
                binding.closedDate.text = "Closed Date: " + orderData[position].closed_date

            } else {
                binding.closedDate.visibility = View.GONE
            }
        }

        fun notifyAdapter(userList: ArrayList<ResponseNewTicketlist.Row>) {
            this.orderData = userList
            notifyDataSetChanged()
        }

        class RemarkAdapter(val remarkList: ArrayList<ResponseNewTicketlist.NewTicketHistoryResponse.Row>) :
            RecyclerView.Adapter<RemarkAdapter.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int,
            ): ViewHolder {
                val view = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.view_order_status,
                    parent,
                    false
                ) as ViewOrderStatusBinding
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(remarkList[position])
                if (position == (remarkList.size - 1)) {
                    holder.remarkBinding.viewCreated.visibility = View.GONE
                    holder.remarkBinding.viewHoldProcess.visibility = View.GONE
                    holder.remarkBinding.viewProcess.visibility = View.GONE
                    holder.remarkBinding.viewClosed.visibility = View.GONE
                    holder.remarkBinding.viewLOne.visibility = View.GONE
                    holder.remarkBinding.viewLTwo.visibility = View.GONE
                    holder.remarkBinding.viewLThree.visibility = View.GONE
                    holder.remarkBinding.viewReopenRequest.visibility = View.GONE
                    holder.remarkBinding.viewAcknowledged.visibility = View.GONE
                    holder.remarkBinding.viewReopenedForProcess.visibility = View.GONE
                    holder.remarkBinding.viewReOpen.visibility = View.GONE
                    holder.remarkBinding.viewAccept.visibility = View.GONE
                }
            }

            override fun getItemCount(): Int {
                return remarkList.size
            }

            inner class ViewHolder(val remarkBinding: ViewOrderStatusBinding) :
                RecyclerView.ViewHolder(remarkBinding.root) {
                fun bind(remarks: ResponseNewTicketlist.NewTicketHistoryResponse.Row) {
                    // remarks.status?.name?: "In PROGRESS"
                    if (remarks.status?.toUpperCase().equals("NEW")) {
                        remarkBinding.createdLayout.visibility = View.VISIBLE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.GONE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.createdPerson.text = remarks.description
                        remarkBinding.createdDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }
                    } else if (remarks.status?.toUpperCase().equals("IN PROGRESS")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.VISIBLE
                        remarkBinding.closedLayout.visibility = View.GONE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.processPerson.text = remarks.description
                        remarkBinding.processDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }
                        /* if (remarks.remarks.isNullOrEmpty())
                             remarkBinding.processStatus.visibility = View.GONE
                         else {
                             remarkBinding.processStatus.visibility = View.VISIBLE
                             remarkBinding.processStatus.setText(
                                 "Remarks : " + remarks.remarks?.trim()
                                     ?.replace("\\s+".toRegex(), " ")
                             )
                         }*/
                    } else if (remarks.status?.toUpperCase().equals("CLOSED")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }
                        /*if (remarks.remarks.isNullOrEmpty())
                            remarkBinding.closedStatus.visibility = View.GONE
                        else {
                            remarkBinding.closedStatus.visibility = View.VISIBLE
                            remarkBinding.closedStatus.setText(
                                "Remarks : " + remarks.remarks?.trim()
                                    ?.replace("\\s+".toRegex(), " ")
                            )
                        }*/
                    } else if (remarks.status?.toUpperCase().equals("RESOLVED")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }
                        /*if (remarks.remarks.isNullOrEmpty())
                            remarkBinding.closedStatus.visibility = View.GONE
                        else {
                            remarkBinding.closedStatus.visibility = View.VISIBLE
                            remarkBinding.closedStatus.setText(
                                "Remarks : " + remarks.remarks?.trim()
                                    ?.replace("\\s+".toRegex(), " ")
                            )
                        }*/
                    } else if (remarks.status?.toUpperCase().equals("REOPEN")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }
                        /*if (remarks.remarks.isNullOrEmpty())
                            remarkBinding.closedStatus.visibility = View.GONE
                        else {
                            remarkBinding.closedStatus.visibility = View.VISIBLE
                            remarkBinding.closedStatus.setText(
                                "Remarks : " + remarks.remarks?.trim()
                                    ?.replace("\\s+".toRegex(), " ")
                            )
                        }*/
                    } else if (remarks.status?.toUpperCase().equals("REJECTED")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }


                        /*if (remarks.remarks.isNullOrEmpty())
                            remarkBinding.closedStatus.visibility = View.GONE
                        else {
                            remarkBinding.closedStatus.visibility = View.VISIBLE
                            remarkBinding.closedStatus.setText(
                                "Remarks : " + remarks.remarks?.trim()
                                    ?.replace("\\s+".toRegex(), " ")
                            )
                        }*/
                    } else if (remarks.status?.toUpperCase().equals("ON HOLD")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.VISIBLE
                        remarkBinding.lOneLayout.visibility = View.GONE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.closedPerson.text = remarks.description
                        remarkBinding.closedDate.text = "on " + remarks.created_time?.let {
                            Utlis.convertCmsDate(it)
                        }


                        /*if (remarks.remarks.isNullOrEmpty())
                            remarkBinding.closedStatus.visibility = View.GONE
                        else {
                            remarkBinding.closedStatus.visibility = View.VISIBLE
                            remarkBinding.closedStatus.setText(
                                "Remarks : " + remarks.remarks?.trim()
                                    ?.replace("\\s+".toRegex(), " ")
                            )
                        }*/
                    }

                    /*else if (remarks.status?.toUpperCase().equals("L1 ESCALATION")) {
                        remarkBinding.createdLayout.visibility = View.GONE
                        remarkBinding.holdProcessLayout.visibility = View.GONE
                        remarkBinding.processLayout.visibility = View.GONE
                        remarkBinding.closedLayout.visibility = View.GONE
                        remarkBinding.lOneLayout.visibility = View.VISIBLE
                        remarkBinding.lTwoLayout.visibility = View.GONE
                        remarkBinding.lThreeLayout.visibility = View.GONE
                        remarkBinding.reopenRequestLayout.visibility = View.GONE
                        remarkBinding.acknowledgedLayout.visibility = View.GONE
                        remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                        remarkBinding.reOpenLayout.visibility = View.GONE
                        remarkBinding.acceptLayout.visibility = View.GONE

                        remarkBinding.lOnePerson.setText(
                            "L1 Escalated by " + remarks.processingStaffName
                        )
                        remarkBinding.lOneDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                        if (remarks.remarks.isNullOrEmpty())
                            remarkBinding.lOneStatus.visibility = View.GONE
                        else {
                            remarkBinding.lOneStatus.visibility = View.VISIBLE
                            remarkBinding.lOneStatus.setText(
                                "Remarks : " + remarks.remarks?.trim()
                                    ?.replace("\\s+".toRegex(), " ")
                            )
                        }
                    } *//*else if (remarks.status?.toUpperCase().equals("L2 ESCALATION")) {
                    remarkBinding.createdLayout.visibility = View.GONE
                    remarkBinding.holdProcessLayout.visibility = View.GONE
                    remarkBinding.processLayout.visibility = View.GONE
                    remarkBinding.closedLayout.visibility = View.GONE
                    remarkBinding.lOneLayout.visibility = View.GONE
                    remarkBinding.lTwoLayout.visibility = View.VISIBLE
                    remarkBinding.lThreeLayout.visibility = View.GONE
                    remarkBinding.reopenRequestLayout.visibility = View.GONE
                    remarkBinding.acknowledgedLayout.visibility = View.GONE
                    remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                    remarkBinding.reOpenLayout.visibility = View.GONE
                    remarkBinding.acceptLayout.visibility = View.GONE

                    remarkBinding.lTwoPerson.setText(
                        "L2 Escalated by " + remarks.processingStaffName
                    )
                    remarkBinding.lTwoDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                    if (remarks.remarks.isNullOrEmpty())
                        remarkBinding.lTwoStatus.visibility = View.GONE
                    else {
                        remarkBinding.lTwoStatus.visibility = View.VISIBLE
                        remarkBinding.lTwoStatus.setText(
                            "Remarks : " + remarks.remarks?.trim()
                                ?.replace("\\s+".toRegex(), " ")
                        )
                    }
                } *//*else if (remarks.status?.toUpperCase().equals("L3 ESCALATION")) {
                    remarkBinding.createdLayout.visibility = View.GONE
                    remarkBinding.holdProcessLayout.visibility = View.GONE
                    remarkBinding.processLayout.visibility = View.GONE
                    remarkBinding.closedLayout.visibility = View.GONE
                    remarkBinding.lOneLayout.visibility = View.GONE
                    remarkBinding.lTwoLayout.visibility = View.GONE
                    remarkBinding.lThreeLayout.visibility = View.VISIBLE
                    remarkBinding.reopenRequestLayout.visibility = View.GONE
                    remarkBinding.acknowledgedLayout.visibility = View.GONE
                    remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                    remarkBinding.reOpenLayout.visibility = View.GONE
                    remarkBinding.acceptLayout.visibility = View.GONE

                    remarkBinding.lThreePerson.setText(
                        "L3 Escalated by " + remarks.processingStaffName
                    )
                    remarkBinding.lThreeDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                    if (remarks.remarks.isNullOrEmpty())
                        remarkBinding.lThreeStatus.visibility = View.GONE
                    else {
                        remarkBinding.lThreeStatus.visibility = View.VISIBLE
                        remarkBinding.lThreeStatus.setText(
                            "Remarks : " + remarks.remarks?.trim()
                                ?.replace("\\s+".toRegex(), " ")
                        )
                    }
                }*//* else if (remarks.status?.toUpperCase().equals("REOPEN REQUEST")) {
                    remarkBinding.createdLayout.visibility = View.GONE
                    remarkBinding.holdProcessLayout.visibility = View.GONE
                    remarkBinding.processLayout.visibility = View.GONE
                    remarkBinding.closedLayout.visibility = View.GONE
                    remarkBinding.lOneLayout.visibility = View.GONE
                    remarkBinding.lTwoLayout.visibility = View.GONE
                    remarkBinding.lThreeLayout.visibility = View.GONE
                    remarkBinding.reopenRequestLayout.visibility = View.VISIBLE
                    remarkBinding.acknowledgedLayout.visibility = View.GONE
                    remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                    remarkBinding.reOpenLayout.visibility = View.GONE
                    remarkBinding.acceptLayout.visibility = View.GONE

                    remarkBinding.reopenRequestPerson.setText(
                        "Reopened by " + remarks.processingStaffName
                    )
                    remarkBinding.reopenRequestDate.setText(
                        "on " + Utlis.convertCmsDate(
                            remarks.dttime
                        )
                    )
                    if (remarks.remarks.isNullOrEmpty())
                        remarkBinding.reopenRequestStatus.visibility = View.GONE
                    else {
                        remarkBinding.reopenRequestStatus.visibility = View.VISIBLE
                        remarkBinding.reopenRequestStatus.setText(
                            "Remarks : " + remarks.remarks?.trim()
                                ?.replace("\\s+".toRegex(), " ")
                        )
                    }
                } *//*else if (remarks.status?.toUpperCase().equals("ACKNOWLEDGED")) {
                    remarkBinding.createdLayout.visibility = View.GONE
                    remarkBinding.holdProcessLayout.visibility = View.GONE
                    remarkBinding.processLayout.visibility = View.GONE
                    remarkBinding.closedLayout.visibility = View.GONE
                    remarkBinding.lOneLayout.visibility = View.GONE
                    remarkBinding.lTwoLayout.visibility = View.GONE
                    remarkBinding.lThreeLayout.visibility = View.GONE
                    remarkBinding.reopenRequestLayout.visibility = View.GONE
                    remarkBinding.acknowledgedLayout.visibility = View.VISIBLE
                    remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                    remarkBinding.reOpenLayout.visibility = View.GONE
                    remarkBinding.acceptLayout.visibility = View.GONE

                    remarkBinding.acknowledgedPerson.setText(
                        "Acknowledged by " + remarks.processingStaffName
                    )
                    remarkBinding.acknowledgedDate.setText("on " + Utlis.convertCmsDate(remarks.dttime))
                    if (remarks.remarks.isNullOrEmpty())
                        remarkBinding.acknowledgedStatus.visibility = View.GONE
                    else {
                        remarkBinding.acknowledgedStatus.visibility = View.VISIBLE
                        remarkBinding.acknowledgedStatus.setText(
                            "Remarks : " + remarks.remarks?.trim()
                                ?.replace("\\s+".toRegex(), " ")
                        )
                    }
                }*//* else if (remarks.status?.toUpperCase().equals("REOPENED FOR REPROCESS")) {
                    remarkBinding.createdLayout.visibility = View.GONE
                    remarkBinding.holdProcessLayout.visibility = View.GONE
                    remarkBinding.processLayout.visibility = View.GONE
                    remarkBinding.closedLayout.visibility = View.GONE
                    remarkBinding.lOneLayout.visibility = View.GONE
                    remarkBinding.lTwoLayout.visibility = View.GONE
                    remarkBinding.lThreeLayout.visibility = View.GONE
                    remarkBinding.reopenRequestLayout.visibility = View.GONE
                    remarkBinding.acknowledgedLayout.visibility = View.GONE
                    remarkBinding.reopenedForProcessLayout.visibility = View.VISIBLE
                    remarkBinding.reOpenLayout.visibility = View.GONE
                    remarkBinding.acceptLayout.visibility = View.GONE

                    remarkBinding.reopenedForProcessPerson.setText(
                        "Reopened for Reprocess by " + remarks.processingStaffName
                    )
                    remarkBinding.reopenedForProcessDate.setText(
                        "on " + Utlis.convertCmsDate(
                            remarks.dttime
                        )
                    )
                    if (remarks.remarks.isNullOrEmpty())
                        remarkBinding.reopenedForProcessStatus.visibility = View.GONE
                    else {
                        remarkBinding.reopenedForProcessStatus.visibility = View.VISIBLE
                        remarkBinding.reopenedForProcessStatus.setText(
                            "Remarks : " + remarks.remarks?.trim()
                                ?.replace("\\s+".toRegex(), " ")
                        )
                    }
                } *//*else if (remarks.status?.toUpperCase().equals("RE-OPEN")) {
                    remarkBinding.createdLayout.visibility = View.GONE
                    remarkBinding.holdProcessLayout.visibility = View.GONE
                    remarkBinding.processLayout.visibility = View.GONE
                    remarkBinding.closedLayout.visibility = View.GONE
                    remarkBinding.lOneLayout.visibility = View.GONE
                    remarkBinding.lTwoLayout.visibility = View.GONE
                    remarkBinding.lThreeLayout.visibility = View.GONE
                    remarkBinding.reopenRequestLayout.visibility = View.GONE
                    remarkBinding.acknowledgedLayout.visibility = View.GONE
                    remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                    remarkBinding.reOpenLayout.visibility = View.VISIBLE
                    remarkBinding.acceptLayout.visibility = View.GONE

                    remarkBinding.reOpenPerson.setText(
                        "Reopened by " + remarks.processingStaffName
                    )
                    remarkBinding.reOpenDate.setText(
                        "on " + Utlis.convertCmsDate(
                            remarks.dttime
                        )
                    )
                    if (remarks.remarks.isNullOrEmpty())
                        remarkBinding.reOpenStatus.visibility = View.GONE
                    else {
                        remarkBinding.reOpenStatus.visibility = View.VISIBLE
                        remarkBinding.reOpenStatus.setText(
                            "Remarks : " + remarks.remarks?.trim()
                                ?.replace("\\s+".toRegex(), " ")
                        )
                    }
                }*//* else if (remarks.status?.toUpperCase().equals("ACCEPT")) {
                    remarkBinding.createdLayout.visibility = View.GONE
                    remarkBinding.holdProcessLayout.visibility = View.GONE
                    remarkBinding.processLayout.visibility = View.GONE
                    remarkBinding.closedLayout.visibility = View.GONE
                    remarkBinding.lOneLayout.visibility = View.GONE
                    remarkBinding.lTwoLayout.visibility = View.GONE
                    remarkBinding.lThreeLayout.visibility = View.GONE
                    remarkBinding.reopenRequestLayout.visibility = View.GONE
                    remarkBinding.acknowledgedLayout.visibility = View.GONE
                    remarkBinding.reopenedForProcessLayout.visibility = View.GONE
                    remarkBinding.reOpenLayout.visibility = View.GONE
                    remarkBinding.acceptLayout.visibility = View.VISIBLE

                    remarkBinding.acceptPerson.setText(
                        "Accepted by " + remarks.processingStaffName
                    )
                    remarkBinding.acceptDate.setText(
                        "on " + Utlis.convertCmsDate(
                            remarks.dttime
                        )
                    )
                    if (remarks.remarks.isNullOrEmpty())
                        remarkBinding.acceptStatus.visibility = View.GONE
                    else {
                        remarkBinding.acceptStatus.visibility = View.VISIBLE
                        remarkBinding.acceptStatus.setText(
                            "Remarks : " + remarks.remarks?.trim()
                                ?.replace("\\s+".toRegex(), " ")
                        )
                    }
                }*/
                }
            }
        }
    }

    //Image Recyclerview
    class ImageRecyclerView(
        var orderData: ArrayList<ResponseNewTicketlist.Image>,
        val imageClickListener: ImageClickListener,
    ) :
        SimpleRecyclerView<ViewImageShowBinding, ResponseNewTicketlist.Image>(
            orderData,
            R.layout.view_image_show
        ) {
        override fun bindItems(
            binding: ViewImageShowBinding,
            items: ResponseNewTicketlist.Image,
            position: Int,
        ) {
            Glide.with(context).load(items.url).placeholder(R.drawable.thumbnail_image)
                .into(binding.image)
            binding.image.setOnClickListener {
                items.url?.let { it1 -> imageClickListener.onItemClick(position, it1) }
            }
        }
    }

    override fun onItemClick(position: Int, imagePath: String) {
        PhotoPopupWindow(context, R.layout.layout_image_fullview, view, imagePath, null)
    }
    /* override  fun gettickethistory(uid:String):ArrayList<NewTicketHistoryResponse.Row>
    {
        var newtickethistory=ArrayList<NewTicketHistoryResponse.Row>()
        newtickethistory= viewModel.gettickethistory(1,100,uid)
         return newtickethistory;

    }*/

    fun calltickethistory(uid: String) {
        Utlis.showLoading(requireContext())
        val tickethistory = ArrayList<NewTicketHistoryResponse.Row>()
        viewModel.getNewticketHistory(
            RequestTicketHistory(
                1,
                50,
                uid
            )
        )
    }

    override fun onComplaintItemClick(
        position: Int,
        orderData: ArrayList<ResponseNewTicketlist.Row>,
    ) {

        // orderData[position].uid?.let { calltickethistory(it) }
        var curStatus: Boolean = orderData.get(position).isExpanded
        orderData.forEach {
            it.isExpanded = false
        }

        orderData.get(position).isExpanded = !curStatus
        // orderData[position].uid?.let { gettickethistory(it)}
        orderData[position].uid?.let { calltickethistory(it) }


        // adapter.notifyAdapter(orderData)


    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            viewBinding.fromDateText.setText(showingDate)
            val fromDate = viewBinding.fromDateText.text.toString()
            val toDate = viewBinding.toDateText.text.toString()
            if (getDateDifference(fromDate, toDate) == 0) {
                viewBinding.toDateText.setText(Utils.getCurrentDate())
            }
        } else {
            viewBinding.toDateText.setText(showingDate)
        }
    }
}

interface ImageClickListener {
    fun onItemClick(position: Int, imagePath: String)

    fun onComplaintItemClick(position: Int, orderData: ArrayList<ResponseNewTicketlist.Row>)

    //  fun gettickethistory(uid:String):ArrayList<NewTicketHistoryResponse.Row>

    // fun calltickethistory(uid:String?);
    // abstract fun callticketHistory(uid: String?)
}