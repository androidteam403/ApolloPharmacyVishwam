package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.cms.ReasonDept
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.data.model.cms.ResponseticketRatingApi
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DialogCustomAlertBinding
import com.apollopharmacy.vishwam.databinding.DialogUserListBinding
import com.apollopharmacy.vishwam.databinding.FragmentHistory2Binding
import com.apollopharmacy.vishwam.dialog.OnTransactionSearchManagerListnier
import com.apollopharmacy.vishwam.dialog.SearchManagerDialog
import com.apollopharmacy.vishwam.dialog.model.SubmitticketDialog
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ComplainListFragment
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.ComplaintsListDetailsCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.callback.HistoryCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.model.SubworkFlowAssignedtoMeRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.model.SubworkFlowAssignedtoMeResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.viewmodel.HistoryFragmentViewModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter.SubworkflowConfigDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter.UserListAdapter
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.*
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.hsalf.smilerating.SmileRating

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment(
    private var orderDataWp: ResponseNewTicketlist.Row,
    private var orderData: java.util.ArrayList<ResponseNewTicketlist.Row>,
    private var position: Int,
    private val imageClickListener: HistoryCallback,
    private var isFromApprovalList: Boolean,
    private var complaintsListDetailsCallback: ComplaintsListDetailsCallback,
) : BaseFragment<HistoryFragmentViewModel, FragmentHistory2Binding>(), HistoryCallback,
    SubmitticketDialog.AbstractDialogFinanceClickListner, OnTransactionSearchManagerListnier {
    var resList = java.util.ArrayList<ResponseNewTicketlist.Row>()
    var pos: Int = 0
    lateinit var userData: LoginDetails
    var frwdMngrPos: Int = 0
    var resListMnger = java.util.ArrayList<ResponseNewTicketlist.Row>()
    var ticketratingapiresponse: ResponseticketRatingApi.Data? = null
    lateinit var userListDialog: Dialog
    var usersList: java.util.ArrayList<UserListForSubworkflowResponse.Rows>? = null
    lateinit var userListAdapter: UserListAdapter
    var userForsubworkflow = UserListForSubworkflowResponse.Rows()


    override val layoutRes: Int
        get() = R.layout.fragment_history2

    override fun retrieveViewModel(): HistoryFragmentViewModel {
        return ViewModelProvider(this).get(HistoryFragmentViewModel::class.java)
    }

    override fun setup() {
//        viewBinding.callback=this
        viewModel.getTicketRatingApi()
//                if (isApprovalListFragment) {
        if (orderDataWp.Tickethistory == null) {
            viewBinding.remarksLayout.visibility = View.GONE
        } else {
            if (orderDataWp.Tickethistory!!.data.listData.rows.isNullOrEmpty()) viewBinding.remarksLayout.visibility =
                View.GONE
            else viewBinding.remarksLayout.visibility = View.VISIBLE
            viewBinding.remarksRecycleView.adapter =
                ComplainListFragment.ApproveRecyclerView.RemarkAdapter(orderDataWp.Tickethistory!!.data.listData.rows)
        }
        userData = LoginRepo.getProfile()!!

        var empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()
        var managerUid: String? = null
        var employeeDetailsResponse: EmployeeDetailsResponse? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(
                empDetailsResponse, EmployeeDetailsResponse::class.java
            )

        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        onCLickAssignedToMe()
//        if (arguments?.getBoolean("isFromApprovalList") == true) {
//            Utlis.showLoading(requireContext())
//            viewModel.getSubworkFlowConfigDetails(
//                this, pos, orderDataWp
//            )
//        }
//assigned_to_me_layout
        if (isFromApprovalList) {
            if (orderDataWp.subworkflowConfigDetailsResponse != null && orderDataWp.subworkflowConfigDetailsResponse!!.data != null && orderDataWp.subworkflowConfigDetailsResponse!!.data!!.listData != null && orderDataWp.subworkflowConfigDetailsResponse!!.data!!.listData!!.rows != null && orderDataWp.subworkflowConfigDetailsResponse!!.data!!.listData!!.rows!!.size > 0
            ) {
                var subworkflowConfigDetailsAdapter = SubworkflowConfigDetailsAdapter(
                    ViswamApp.context,
                    imageClickListener,
                    orderDataWp.subworkflowConfigDetailsResponse!!.data!!.listData!!.rows!!,
                    orderDataWp.ticketDetailsResponse!!.data,
                    orderData,
                    position
                )
                viewBinding.subworkflowConfigDetailsListLayout.visibility = View.VISIBLE
                viewBinding.inventoryActionLayout.visibility = View.GONE

                var layoutManager =
                    LinearLayoutManager(ViswamApp.context, LinearLayoutManager.HORIZONTAL, false)
                //attaches LinearLayoutManager with RecyclerView
                viewBinding.subworkflowConfigDetailsListRecyclerview.layoutManager = layoutManager
                viewBinding.subworkflowConfigDetailsListRecyclerview.adapter =
                    subworkflowConfigDetailsAdapter
            } else {
                viewBinding.subworkflowConfigDetailsListLayout.visibility = View.GONE
                viewBinding.inventoryActionLayout.visibility = View.GONE
            }


            /*  //Subworkflow action details adapter.....
              if (orderData != null
                  && orderData.get(position) != null
                  && orderData.get(position).ticket_subworkflow_history != null
                  && orderData.get(position).ticket_subworkflow_history!!.size > 0
              ) {
                  if (orderData.get(position).ticketSubworkflowInfo != null
                      && orderData.get(position).ticketSubworkflowInfo!!.subworkflow_action != null
                      && orderData.get(position).ticketSubworkflowInfo!!.subworkflow_action!!.action != null
                  ) {
                      binding.subworkflowAction.text =
                          "${orderData.get(position).ticketSubworkflowInfo!!.subworkflow_action!!.action}"
                  }
                  var subworkflowActionDetailsAdapter = SubworkflowActionDetailsAdapter(
                      context,
                      orderData.get(position).ticket_subworkflow_history!!
                  )
                  binding.subworkflowDetailsHistoryLayout.visibility = View.VISIBLE
                  var layoutManager =
                      LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                  binding.subworkflowActionDetailsRecyclerview.layoutManager = layoutManager
                  binding.subworkflowActionDetailsRecyclerview.adapter =
                      subworkflowActionDetailsAdapter
              } else {
                  binding.subworkflowDetailsHistoryLayout.visibility = View.GONE
              }*/
        } else {
            viewBinding.subworkflowConfigDetailsListLayout.visibility = View.GONE
        }

        if (orderDataWp != null) {
            if (employeeDetailsResponse?.data!!.department!!.code.equals("FN")) {
                if (orderDataWp.ticketDetailsResponse!!.data.department.code.equals("IT") && orderDataWp.ticketDetailsResponse!!.data.category.code.equals(
                        "pos"
                    ) && orderDataWp.ticketDetailsResponse!!.data.reason.code.equals("asb_not_completed") && orderDataWp.ticketDetailsResponse!!.data!!.user!!.uid != null && orderDataWp.ticketDetailsResponse!!.data!!.user!!.uid!!.equals(
                        employeeDetailsResponse?.data!!.uid
                    ) && orderDataWp.ticketDetailsResponse!!.data.ticket_it.status.uid.equals(
                        "forwarded_to_fin"
                    ) && orderDataWp.status!!.code.equals("inprogress") && employeeDetailsResponse?.data!!.department!!.code.equals(
                        "FN"
                    ) && orderDataWp.ticketDetailsResponse!!.data.reason.sub_workflow.uid.equals(
                        "Yes"
                    )
                ) {

                    viewBinding.ccAcceptrejectLayout.visibility = View.VISIBLE;
                    viewBinding.acceptBtn.setOnClickListener {
                        imageClickListener.onClickCCAccept(
                            orderDataWp.ticketDetailsResponse!!.data, orderData, position, this
                        )
                    }
                    viewBinding.rejectBtn.setOnClickListener {
                        imageClickListener.onClickCCReject(
                            orderDataWp.ticketDetailsResponse!!.data, orderData, position, this
                        )
                    }
                } else {
                    viewBinding.ccAcceptrejectLayout.visibility = View.GONE
                }
            }
//
        }

        if (orderDataWp.ticketDetailsResponse?.data?.ticket_it?.tid?.uid != null) {
//            viewBinding.creditCardDetailsLayout.visibility = View.VISIBLE
////                binding.ccReason.text = " ${items.creditCardTSDetails?.data?.reason!!.name }"
////                binding.ccExecutive.text = " ${items.creditCardTSDetails?.data?.executive!!.first_name }"
////                binding.ccManager.text = items.creditCardTSDetails?.data?.manager!!.first_name
//            viewBinding.ccTid.text = " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.tid.tid}"
//            viewBinding.billNumber.text =
//                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.bill_number}"
//            viewBinding.transactionNumber.text =
//                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.transaction_id}"
//            viewBinding.approvalCode.text =
//                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.approval_code}"
//            viewBinding.billAmount.text =
//                "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.bill_amount}"


//                department?.code == 'IT' && category.code == 'pos'  && reason.code == 'asb_not_completed'
//                        && user?.uid == session user.uid && ticket_it?.status?.uid == 'forwarded_to_fin' && status?.code == 'inprogress'
//                && sessionuser.department.code == 'FN' && reason?.sub_workflow?.uid ==  'Yes'


            if (((orderDataWp.status!!.code.equals("new") || orderDataWp.status!!.code.equals("reopen")) && orderDataWp.ticketDetailsResponse!!.data.executive.uid.equals(
                    employeeDetailsResponse?.data!!.uid
                ) && orderDataWp.ticketDetailsResponse!!.data.reason.sub_workflow.uid.equals(
                    "Yes"
                ) && checkResonDepot(
                    orderDataWp.ticketDetailsResponse!!.data.reason.reason_dept,
                    employeeDetailsResponse
                ))
            ) {
                if (orderDataWp.ticketDetailsResponse!!.data.ticket_it.status.uid != null && (orderDataWp.ticketDetailsResponse!!.data.ticket_it.status.uid.equals(
                        "approved"
                    ) || orderDataWp.ticketDetailsResponse!!.data.ticket_it.status.uid.equals(
                        "rejected"
                    ))
                ) {
                    viewBinding.ccActionLayout.visibility = View.GONE
                } else {
                    if (orderDataWp.ticketDetailsResponse!!.data.ticket_it.status.uid == null) {
                        //Change to visible if required
                        viewBinding.ccActionLayout.visibility = View.GONE
//                            binding.ccActionLayout.visibility = View.VISIBLE
//                        binding.acceptBtn.setOnClickListener {
//
//
//
//
//
//
////                            imageClickListener.onClickForwardToFinance(cmsTicketRequest)
//
//                            imageClickListener.onClickCCAccept(items.ticketDetailsResponse!!.data)
//                        }
                        viewBinding.frwdToFinance.setOnClickListener {

                            val cmsTicketRequest = CmsTicketRequest()
                            val cms = CmsTicketRequest.Ticket()
                            cmsTicketRequest.uid =
                                orderDataWp.ticketDetailsResponse!!.data.ticket_it.uid
                            cms.uid = orderDataWp.ticketDetailsResponse!!.data.uid
                            cmsTicketRequest.ticket = cms


//


                            imageClickListener.onClickForwardToFinance(
                                cmsTicketRequest, orderData, position, this
                            )
//                            imageClickListener.onClickCCReject(items.ticketDetailsResponse!!.data)
                        }
                    } else {
                        viewBinding.ccActionLayout.visibility = View.GONE
                    }
                }
            } else {
                viewBinding.ccActionLayout.visibility = View.GONE
            }
        } else {
//            viewBinding.creditCardDetailsLayout.visibility = View.GONE
            viewBinding.ccActionLayout.visibility = View.GONE
        }

        var isDonthaveInventory = false
        if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.uid != null && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item.isNotEmpty()) {
//            viewBinding.inventoryDetailsLayout.visibility = View.VISIBLE
////                binding.articleCode.text =
////                    "${items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item[0].item_code}"
////              if (items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item)
//            viewBinding.articleName.text =
//                "${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_name}"
//            viewBinding.batchNumber.text =
//                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].batch_no}"
//            viewBinding.barcode.text =
//                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].barcode
//                    ?: "--"
//            viewBinding.expairyDate.text =
//                Utlis.convertCmsExparyDate(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].expiry_date)
//
//            viewBinding.purchaseRate.text =
//                "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].purchase_rate}"
//            if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req")) {
//                viewBinding.oldMrpLabel.text = "MRP : "
//                viewBinding.oldMrp.text =
//                    "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].mrp}"
//                viewBinding.newMrp.visibility = View.GONE
//                viewBinding.newMrpLabel.visibility = View.GONE
//            } else {
//                viewBinding.oldMrp.text =
//                    "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].old_mrp}"
//                viewBinding.newMrp.text =
//                    "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].new_mrp}"
//            }
//                if(items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.first_name == null) {
//                    binding.manager.text =
//                        " ${items.inventoryDetailsModel?.data?.site?.manager?.first_name}"
//                }else {
//                    binding.manager.text =
//                        " ${items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.first_name}"
//                }
//                binding.category.text =
//                    " ${items.inventoryDetailsModel?.data?.category!!.name}"
//                binding.subCategory.text =
//                    " ${items.inventoryDetailsModel?.data?.subcategory!!.name}"
//                binding.reason.text =
//                    " ${items.inventoryDetailsModel?.data?.reason!!.name}"

            if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.uid == null) {
                managerUid = orderDataWp.ticketDetailsResponse?.data?.site?.manager?.uid!!
            } else {
                managerUid =
                    orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].manager.uid
            }

            if (orderDataWp.status!!.code.equals("inprogress") || orderDataWp.status!!.code.equals("reopen")) {
                if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("mrp_cr") && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid != null && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid.equals(
                        "forward"
                    ) && managerUid.equals(employeeDetailsResponse?.data!!.uid)
                ) {
                    if (!isFromApprovalList) {
                        viewBinding.inventoryActionLayout.visibility = View.VISIBLE
                    } else {
                        viewBinding.inventoryActionLayout.visibility = View.GONE
                    }
//                    viewBinding.inventoryActionLayout.visibility = View.VISIBLE
                    viewBinding.inventoryRejectBtn.visibility = View.VISIBLE
                    viewBinding.inventoryForwardManagerBtn.visibility = View.GONE
                    viewBinding.inventoryChangeForwardBtn.visibility = View.VISIBLE
                    viewBinding.inventoryAcceptBtn.text = "Approve"
                } else if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req") && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid == null && employeeDetailsResponse?.data!!.uid.equals(
                        orderDataWp.ticketDetailsResponse?.data?.user!!.uid
                    )
                ) {
                    if (!isFromApprovalList) {
                        viewBinding.inventoryActionLayout.visibility = View.VISIBLE
                    } else {
                        viewBinding.inventoryActionLayout.visibility = View.GONE
                    }
//                    viewBinding.inventoryActionLayout.visibility = View.VISIBLE
                    viewBinding.inventoryRejectBtn.visibility = View.VISIBLE
                    viewBinding.inventoryForwardManagerBtn.visibility = View.GONE
                    viewBinding.inventoryChangeForwardBtn.visibility = View.GONE
                    viewBinding.inventoryAcceptBtn.text = "Approve"
                }
                viewBinding.inventoryAcceptBtn.setOnClickListener {
                    imageClickListener.onClickInventoryAccept(
                        orderDataWp,
                        position,
                        orderData,
                        this
                    )
                }
                viewBinding.inventoryRejectBtn.setOnClickListener {
                    imageClickListener.onClickInventoryReject(
                        orderDataWp,
                        position,
                        orderData,
                        this
                    )
                }
                viewBinding.inventoryForwardManagerBtn.setOnClickListener {
                    imageClickListener.onClickForwardToManager(orderDataWp, this)
                }
                viewBinding.inventoryChangeForwardBtn.setOnClickListener {
                    imageClickListener.onClickForwardChangeManager(
                        orderDataWp,
                        position,
                        orderData,
                        this
                    )
                }
            } else {
                viewBinding.inventoryActionLayout.visibility = View.GONE
            }
//            viewBinding.inventoryImagesLayout.visibility = View.VISIBLE
//            if (!orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.isNullOrEmpty()) {
//                Glide.with(ViswamApp.context)
//                    .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob)
//                    .placeholder(R.drawable.thumbnail_image).into(viewBinding.frontImgView)
//                viewBinding.frontImgView.setOnClickListener {
//                    orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.let { it1 ->
//                        imageClickListener.onItemClick(position, it1)
//                    }
//                }
//            } else {
//                viewBinding.frontImgLabel.visibility = View.GONE
//                viewBinding.frontImgView.visibility = View.GONE
//            }
//            if (!orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob.isNullOrEmpty()) {
//                Glide.with(ViswamApp.context)
//                    .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob)
//                    .placeholder(R.drawable.thumbnail_image).into(viewBinding.backImgView)
//                viewBinding.backImgView.setOnClickListener {
//                    orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob.let { it1 ->
//                        imageClickListener.onItemClick(position, it1)
//                    }
//                }
//            } else {
//                viewBinding.backImgLabel.visibility = View.GONE
//                viewBinding.backImgView.visibility = View.GONE
//            }
//            if (!orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob.isNullOrEmpty()) {
//                Glide.with(ViswamApp.context)
//                    .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob)
//                    .placeholder(R.drawable.thumbnail_image).into(viewBinding.otherImgView)
//                viewBinding.otherImgView.setOnClickListener {
//                    orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob.let { it1 ->
//                        imageClickListener.onItemClick(position, it1)
//                    }
//                }
//                viewBinding.otherImgLabel.visibility = View.VISIBLE
//                viewBinding.otherImgView.visibility = View.VISIBLE
//            } else {
//                viewBinding.otherImgLabel.visibility = View.GONE
//                viewBinding.otherImgView.visibility = View.GONE
//            }
        } else {
            isDonthaveInventory = true
//            viewBinding.inventoryDetailsLayout.visibility = View.GONE
            viewBinding.inventoryActionLayout.visibility = View.GONE
//            viewBinding.inventoryImagesLayout.visibility = View.GONE
        }

        if (orderDataWp.status?.code.isNullOrEmpty()) {
        } else {

            if (orderDataWp.status!!.code.equals("solved") && employeeDetailsResponse?.data!!.uid.equals(
                    orderDataWp.created_id!!.uid
                )
            ) {
                viewBinding.ticketResolveBtn.visibility = View.VISIBLE
                if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("mrp_cr") || orderDataWp.ticketDetailsResponse?.data?.category?.code.equals(
                        "new_batch_req"
                    )
                ) {
                    viewBinding.ticketResolveBtn.visibility = View.GONE
                }
                if (orderDataWp.ticketDetailsResponse != null) {
                    if (orderDataWp.ticketDetailsResponse!!.data!!.reason!!.allow_manual_ticket_closure!!.uid == null
                        || orderDataWp.ticketDetailsResponse!!.data!!.reason!!.allow_manual_ticket_closure!!.uid == "Yes"
                        || orderDataWp.ticketDetailsResponse!!.data!!.override_ticket_closure == true
                        || orderDataWp.ticketDetailsResponse!!.data!!.override_ticket_closure == null
                    ) {
                        viewBinding.ticketCloseBtn.visibility = View.VISIBLE
                    } else {
                        viewBinding.ticketCloseBtn.visibility = View.GONE
                    }

                    if (orderDataWp.ticketDetailsResponse!!.data!!.reason!!.allow_manual_ticket_closure!!.uid == null
                        || orderDataWp.ticketDetailsResponse!!.data!!.reason!!.allow_manual_ticket_closure!!.uid == "Yes"
                    ) {
                        viewBinding.ticketResolveBtn.visibility = View.VISIBLE
                        if (orderDataWp.have_subworkflow != null) {
                            if (orderDataWp.have_subworkflow == true) {
                                viewBinding.ticketResolveBtn.visibility = View.GONE
                            }
                        }

                    } else {
                        viewBinding.ticketResolveBtn.visibility = View.GONE
                    }
                }





                viewBinding.ticketCloseBtn.setOnClickListener {
                    imageClickListener.onClickTicketClose(orderDataWp, orderData, position, this)
                }
                viewBinding.ticketActionLayout.visibility = View.VISIBLE
                viewBinding.ticketResolveBtn.text = "Reopen"
                viewBinding.ticketResolveBtn.setOnClickListener {
                    imageClickListener.onClickTicketReopen(orderDataWp, orderData, position, this)
                }
            } else if ((orderDataWp.status!!.code.equals("inprogress") || orderDataWp.status!!.code.equals(
                    "reopened"
                )) && employeeDetailsResponse?.data!!.uid.equals(
                    orderDataWp.ticketDetailsResponse?.data?.user?.uid
                )
            ) {
                if (orderDataWp.ticketDetailsResponse != null && (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals(
                        "mrp_cr"
                    ) || orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req"))
                ) {
                    viewBinding.ticketResolveBtn.visibility = View.GONE
                    viewBinding.ticketActionLayout.visibility = View.GONE
                } else if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request?.uid != null && orderDataWp.ticketDetailsResponse?.data?.category?.code.equals(
                        "new_drug_req"
                    )
                ) {
                    viewBinding.ticketResolveBtn.visibility = View.GONE
                    viewBinding.ticketActionLayout.visibility = View.GONE
                }
//                    else if (items.status!!.code.equals("inprogress")
//                        && items.ticketDetailsResponse!!.data.department.code.equals(
//                            "IT") && items.ticketDetailsResponse!!.data.category.code.equals("pos")
//                    ) {
//                        binding.ticketResolveBtn.visibility = View.GONE
//                        binding.ticketActionLayout.visibility = View.GONE
//                    }

//                    else  if (employeeDetailsResponse?.data!!.department!!.code.equals(
//                            "FN")
//                    ) {
//                        if (items.ticketDetailsResponse!!.data.department.code.equals("IT") && items.ticketDetailsResponse!!.data.category.code.equals(
//                                "pos") && items.ticketDetailsResponse!!.data.reason.code.equals("asb_not_completed")
//                            && items.ticketDetailsResponse!!.data.user.uid.equals(
//                                employeeDetailsResponse?.data!!.uid)
//                            && items.status!!.code.equals("inprogress")
//                            && items.ticketDetailsResponse!!.data.reason.sub_workflow.uid.equals("Yes")
//                        ) {
//
//                            binding.ticketResolveBtn.visibility = View.GONE
//                            binding.ticketActionLayout.visibility = View.GONE
//                        }
//                        else {
//                            binding.ticketResolveBtn.visibility = View.VISIBLE
//                            binding.ticketCloseBtn.visibility = View.GONE
//                            binding.ticketResolveBtn.text = "Resolve"
//                            binding.ticketResolveBtn.setOnClickListener {
//                                imageClickListener.onClickTicketResolve(items)
//                            }
//                            binding.ticketActionLayout.visibility = View.VISIBLE
//                        }
//
//                        }

                else {
                    viewBinding.ticketResolveBtn.visibility = View.VISIBLE
                    viewBinding.ticketCloseBtn.visibility = View.GONE
                    viewBinding.ticketResolveBtn.text = "Resolve"
                    viewBinding.ticketResolveBtn.setOnClickListener {

//                            items.Tickethistory!!.data.listData.rows[0].description=items.Tickethistory!!.data.listData.rows[0].description.replace("Accepted","Resolved")
                        imageClickListener.onClickTicketResolveHis(
                            orderDataWp,
                            orderData,
                            position,
                            this
                        )
                    }
                    viewBinding.ticketActionLayout.visibility = View.VISIBLE
                }
            } else {
                viewBinding.ticketActionLayout.visibility = View.GONE
            }
        }

        viewModel.cmsTicketResponseList.observe(viewLifecycleOwner) {
            hideLoading()
            if (it.success!!) {

                SubmitticketDialog().apply {
                    arguments = SubmitticketDialog().generateParsedData(it)
                }.show(childFragmentManager, "")
            }


        }

        viewModel.command.observe(viewLifecycleOwner, Observer {
            Utlis.hideLoading()
            when (it) {
//                is CmsCommand.VisibleLayout -> {
//                    viewBinding.emptyList.visibility = View.VISIBLE
//                }

                is HistoryFragmentViewModel.CmsCommand.ShowToast -> {
                    hideLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is HistoryFragmentViewModel.CmsCommand.RefreshPageOnSuccess -> {
                    hideLoading()
//                    submitClick()
                }

                else -> {
                }
            }
        })

        viewModel.cmsticketRatingresponse.observe(viewLifecycleOwner) {
//            Utlis.hideLoading()
//            ticketratingapiresponse = it.data;
//            callAPI(1)
//            if (!Preferences.getResponseNewTicketlist().isEmpty()) {
//                if (Config.COMPLAINTLISTFETCHEDTIME != null) {
//                    if (diffTime(Config.COMPLAINTLISTFETCHEDTIME!!,
//                            Calendar.getInstance().getTime()) < 6
//                    ) {
//                        isTicketListThereFirstTime = false
//                        viewModel.setTicketListFromSheredPeff()
//                    } else {
//                        Utlis.hideLoading()
//                        ticketratingapiresponse = it.data;
//                        callAPI(1)
//                    }
//                } else {
//                    Utlis.hideLoading()
//                    ticketratingapiresponse = it.data;
//                    callAPI(1)
//                }
//            } else {
            Utlis.hideLoading()
            ticketratingapiresponse = it.data;
//            callAPI(1)
//            }
        }


        if (orderDataWp!!.ticketDetailsResponse!!.data!!.subworkflow_access == 1
            && employeeDetailsResponse!!.data!!.assign_to_me_any_level!!.uid.equals("Yes")
            && orderDataWp != null
            && orderDataWp!!.ticketDetailsResponse != null
            && orderDataWp!!.ticketDetailsResponse!!.data != null
            && orderDataWp!!.ticketDetailsResponse!!.data!!.ticketSubworkflowInfo != null
            && orderDataWp!!.ticketDetailsResponse!!.data!!.ticketSubworkflowInfo!!.assigned_to != null
            && orderDataWp!!.ticketDetailsResponse!!.data!!.ticketSubworkflowInfo!!.assigned_to!!.uid != null
            && !employeeDetailsResponse!!.data!!.uid.equals(orderDataWp!!.ticketDetailsResponse!!.data!!.ticketSubworkflowInfo!!.assigned_to!!.uid)
            && !orderDataWp!!.ticketSubworkflowInfo!!.subworkflow_action!!.code.equals("change_forward_manager")
            && !orderDataWp!!.ticketSubworkflowInfo!!.subworkflow_action!!.code.equals("forward_to_manager")
            && !orderDataWp!!.ticketSubworkflowInfo!!.subworkflow_action!!.code.equals("closed")
            && !orderDataWp!!.ticketSubworkflowInfo!!.subworkflow_action!!.code.equals("rejected")
        ) {
            viewBinding.assignedToMeLayout.visibility = View.VISIBLE
            viewBinding.subworkflowConfigDetailsListLayout.visibility = View.GONE
        } else {
            if (!orderDataWp!!.status!!.code.equals("solved")
                && !orderDataWp!!.status!!.code.equals("closed")
                && !orderDataWp!!.status!!.code.equals("rejected")
            ) {
                viewBinding.assignedToMeLayout.visibility = View.GONE
                viewBinding.subworkflowConfigDetailsListLayout.visibility = View.VISIBLE
            }else{
                viewBinding.assignedToMeLayout.visibility = View.GONE
                viewBinding.subworkflowConfigDetailsListLayout.visibility = View.GONE
            }
        }
    }


    override fun onClickForwardToFinance(
        data: CmsTicketRequest,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        showLoading()

        resList = responseList
        pos = position
        viewModel.cmsTicketStatusUpdate(data)
    }


    override fun onClickCCAccept(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Accept"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val ccAcceptRejectModel = CCAcceptRejectModel()
                ccAcceptRejectModel.setAction("resolve")
                ccAcceptRejectModel.setComment(remark.text.toString())
                ccAcceptRejectModel.setEmployee_id(userData.EMPID)
                ccAcceptRejectModel.setPos_status("approve")
                ccAcceptRejectModel.setStatus("inprogress")
                ccAcceptRejectModel.setTicket_id(data.ticket_id)
                val ticket_it = CCAcceptRejectModel.Ticket_it()
                ticket_it.setUid("approved")
                ccAcceptRejectModel.setTicket_it(ticket_it)
                ccAcceptRejectModel.setUid(data.uid)
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "solved"
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionCCAccept(ccAcceptRejectModel, 0, complaintsListDetailsCallback)

//                adapter.notifyDataSetChanged()

//                \"background_color\":\"#047604\",\"code\":\"solved\",\"name\":\"Resolved\",\"text_color\":\"#FFFFFF\
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onClickCCReject(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reject"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val ccAcceptRejectModel = CCAcceptRejectModel()
                ccAcceptRejectModel.setAction(null)
                ccAcceptRejectModel.setComment(remark.text.toString())
                ccAcceptRejectModel.setEmployee_id(userData.EMPID)
                ccAcceptRejectModel.setPos_status(null)
                ccAcceptRejectModel.setStatus("reject")
                ccAcceptRejectModel.setTicket_id(data.ticket_id)
                val ticket_it = CCAcceptRejectModel.Ticket_it()
                ticket_it.setUid("rejected")
                ccAcceptRejectModel.setTicket_it(ticket_it)
                ccAcceptRejectModel.setUid(data.uid)
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "rejected"
                responseList.get(position).status!!.name = "Rejected"
                responseList.get(position).status!!.background_color = "#ed001c"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionCCAccept(ccAcceptRejectModel, 0, complaintsListDetailsCallback)

//                adapter.notifyDataSetChanged()


//                "background_color\":\"#ed001c\",\"code\":\"rejected\",\"name\":\"Rejected\",\"text_color\":\"#FFFFFF\"}
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickInventoryAccept(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Accept"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = InventoryAcceptrejectModel(
                    null,
                    remark.text.toString(),
                    ItemStatus("approved"),
                    data.site!!.site!!,
                    data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    "inprogress",
                    data.ticketDetailsResponse!!.data.ticket_id,
                    "resolve"
                )
                var workFlowUpdateModel = WorkFlowUpdateModel(
                    Action("97A318ACE84930236386DB1A70944825", "resolve"),
                    "Resolved",
                    Department(
                        data.ticketDetailsResponse?.data?.department?.uid,
                        data.ticketDetailsResponse?.data?.department?.code
                    ),
                    Level(
                        data.ticketDetailsResponse?.data?.level?.uid,
                        data.ticketDetailsResponse?.data?.level?.code
                    ),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(
                        data.ticketDetailsResponse?.data?.user?.first_name,
                        data.ticketDetailsResponse?.data?.user?.uid
                    ),
                    "Yes",
                    "manager"
                )
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "solved"
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionInventoryAcceptReject(
                    inventoryAcceptrejectModel,
                    workFlowUpdateModel,
                    0,
                    complaintsListDetailsCallback
                )
//                adapter.notifyDataSetChanged()


//                \"background_color\":\"#047604\",\"code\":\"solved\",\"name\":\"Resolved\",\"text_color\":\"#FFFFFF\
//                viewModel.actionInventoryAcceptReject(inventoryAcceptrejectModel,
//                    workFlowUpdateModel,
//                    0)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onClickInventoryReject(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reject"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = InventoryAcceptrejectModel(
                    remark.text.toString(),
                    null,
                    ItemStatus("rejected"),
                    data.site!!.site!!,
                    data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    "reject",
                    data.ticketDetailsResponse!!.data.ticket_id,
                    null

                )
                var workFlowUpdateModel = WorkFlowUpdateModel(
                    Action("52E2C8F5C204B5BD03DF3A73EB096484", "reject"),
                    "Resolved",
                    Department(
                        data.ticketDetailsResponse?.data?.department?.uid,
                        data.ticketDetailsResponse?.data?.department?.code
                    ),
                    Level(
                        data.ticketDetailsResponse?.data?.level?.uid,
                        data.ticketDetailsResponse?.data?.level?.code
                    ),
                    NextLevel("64D9D9BE4A621E9C13A2C73404646655"),
                    null,
                    Site(data.site?.uid),
                    "",
                    Status(data.status?.code, data.status?.uid),
                    data.ticket_id,
                    TicketOwner(data.ticketDetailsResponse?.data?.user?.uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    User(
                        data.ticketDetailsResponse?.data?.user?.first_name,
                        data.ticketDetailsResponse?.data?.user?.uid
                    ),
                    "Yes",
                    "manager"
                )
                responseList.get(position).isExpanded = false
                responseList.get(position).status!!.code = "rejected"
                responseList.get(position).status!!.name = "Rejected"
                responseList.get(position).status!!.background_color = "#ed001c"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                viewModel.actionInventoryAcceptReject(
                    inventoryAcceptrejectModel,
                    workFlowUpdateModel,
                    0,
                    complaintsListDetailsCallback
                )
//                adapter.notifyDataSetChanged()

//                background_color\":\"#ed001c\",\"code\":\"rejected\",\"name\":\"Rejected\",\"text_color\":\"#FFFFFF

            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onClickForwardToManager(
        data: ResponseNewTicketlist.Row,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Forward to Manager"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
                showLoading()
                var forwardToManagerModel = ForwardToManagerModel(
                    remark.text.toString(),
                    Manager(""),//data.inventoryDetailsModel!!.data.ticket_inventory.ticket_inventory_item[0].manager.uid
                    FMTicket(data.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid),
                    data.ticketDetailsResponse!!.data.ticket_inventory.uid,
                    userData.EMPID
                )
                viewModel.actionForwardToManager(
                    forwardToManagerModel,
                    0,
                    complaintsListDetailsCallback
                )
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private lateinit var selectedInventeryTicket: ResponseNewTicketlist.Row
    override fun onClickForwardChangeManager(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        historyCallback: HistoryCallback,
    ) {
        resListMnger = responseList
        frwdMngrPos = pos
        selectedInventeryTicket = data
        viewModel.getManagersLiveData.observe(viewLifecycleOwner, Observer {
            SearchManagerDialog(it).apply { }.show(childFragmentManager, "")
        })
        viewModel.getManagers(data.site?.uid!!)
    }


    override fun onClickTicketClose(
        data: ResponseNewTicketlist.Row,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        var ratingduid: String = ""
        val rating = dialog.findViewById(R.id.smile_rating) as SmileRating
        rating.visibility = View.VISIBLE
        rating.setOnRatingSelectedListener { level, reselected ->
            for (rows in ticketratingapiresponse?.listData?.rows!!) {
                if (rows.value.equals(level.toString())) {
                    ratingduid = rows.uid!!
                    break
                }
            }
        }
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Close"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else if (ratingduid.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    context?.resources?.getString(R.string.label_rate_complaint),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "close",
                    remark.text.toString(),
                    userData.EMPID,
                    "closed",
                    data.ticket_id!!,
                    Feedback(Rating(ratingduid))
                )

                responseList.get(position).status!!.name = "Closed"
                responseList.get(position).status!!.background_color = "#00ba00"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                responseList.get(position).status!!.code = "closed"

                data.isExpanded = false
                viewModel.actionTicketResolveClose(
                    inventoryAcceptrejectModel,
                    historyCallback,
                    complaintsListDetailsCallback
                )
//                adapter.notifyDataSetChanged()

//                "background_color\":\"#00ba00\",\"code\":\"closed\",\"name\":\"Closed\",\"text_color\":\"#FFFFFF\
//                viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onClickTicketReopen(
        data: ResponseNewTicketlist.Row,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Reopen"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "reopen",
                    remark.text.toString(),
                    userData.EMPID,
                    "reopened",
                    data.ticket_id!!,
                    null
                )
                responseList.get(position).status!!.name = "Reopen"
                responseList.get(position).status!!.background_color = "#f57200"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                responseList.get(position).status!!.code = "reopened"

                data.isExpanded = false
                viewModel.actionTicketResolveClose(
                    inventoryAcceptrejectModel,
                    historyCallback,
                    complaintsListDetailsCallback
                )

//                adapter.notifyDataSetChanged()
//                \"background_color\":\"#f57200\",\"code\":\"reopened\",\"name\":\"Reopen\",\"text_color\":\"#FFFFFF\"},
//                data.status.
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    //commented in list fragment

//    override fun onClickSubWorkflowAccept(
//        data: TicketData,
//        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
//        position: Int,
//        historyCallback: HistoryCallback
//    ) {
//        var subWorkflowAcceptRequest = SubWorkflowAcceptRequest()
//        subWorkflowAcceptRequest.uid = data.uid
//        subWorkflowAcceptRequest.employee_id = Preferences.getValidatedEmpId()
//        Utlis.showLoading(requireContext())
//        viewModel.subWorkflowAcceptApiCAll(subWorkflowAcceptRequest, this)
//
//    }
//// commented in list fragment
//    override fun onClickSubWorkflowReject(
//        data: TicketData,
//        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
//        position: Int,
//    ) {
//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_subworkflow_reject)
//        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
//        ticketNo.text = data.ticket_id
//        val dismissDialog = dialog.findViewById(R.id.diloga_close) as ImageView
//        dismissDialog.setOnClickListener {
//            dialog.dismiss()
//        }
//        val submit = dialog.findViewById(R.id.submit) as AppCompatButton
//        submit.setOnClickListener {
//            dialog.dismiss()
//            Utlis.showLoading(requireContext())
//            var subWorkflowRejectRequest = SubWorkflowRejectRequest()
//            val commentInput = dialog.findViewById(R.id.remark) as AppCompatEditText
//            subWorkflowRejectRequest.comment = commentInput.text.toString()
//            subWorkflowRejectRequest.uid = data.uid
//            subWorkflowRejectRequest.status = "reject"
//            subWorkflowRejectRequest.employee_id = Preferences.getValidatedEmpId()
//            subWorkflowRejectRequest.ticket_id = data.ticket_id
//            viewModel.subWorkflowRejectApiCall(subWorkflowRejectRequest, this)
//        }
//        val cancel = dialog.findViewById(R.id.cancel) as AppCompatButton
//        cancel.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }

    override fun onClickAction(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
        historyCallback: HistoryCallback,
    ) {
        if (row.action!!.code.equals("forward") && row.assignToUser!!.uid!!.equals("Yes")) {
            if (NetworkUtil.isNetworkConnected(requireContext())) {
                showLoading()
                viewModel.userlistForSubworkflowApiCall(
                    this,
                    data, responseList, position, row
                )
            }
        } else {
            showActionPopup(data, responseList, position, row, null)
        }
    }

    override fun onSuccessBack(complaintsListDetailsCallback: ComplaintsListDetailsCallback) {
        complaintsListDetailsCallback.onClickBack()
    }

    override fun onSuccessSubworkflowAssignedtoMeApiCall(subworkFlowAssignedtoMeResponse: SubworkFlowAssignedtoMeResponse) {
        Utlis.hideLoading()
        Toast.makeText(
            requireContext(),
            subworkFlowAssignedtoMeResponse.message,
            Toast.LENGTH_SHORT
        ).show()
        requireActivity().finish()
    }

    fun doActionAssignToMe() {
        Utlis.showLoading(requireContext())
        var subworkFlowAssignedtoMeRequest = SubworkFlowAssignedtoMeRequest()
        subworkFlowAssignedtoMeRequest.employee_id = Preferences.getValidatedEmpId()
        subworkFlowAssignedtoMeRequest.uid = orderDataWp.ticketDetailsResponse!!.data!!.uid
        subworkFlowAssignedtoMeRequest.subworkflow_seq_order =
            orderDataWp.ticketSubworkflowInfo!!.subworkflow_seq_order
        subworkFlowAssignedtoMeRequest.subworkflow_step_order =
            orderDataWp.ticketSubworkflowInfo!!.subworkflow_step_order
        subworkFlowAssignedtoMeRequest.reason = orderDataWp.reason!!.uid
        viewModel.subworkflowAssignedtoMeApiCall(
            this@HistoryFragment,
            subworkFlowAssignedtoMeRequest
        )
    }

    override fun onSuccessSubWorkflowAcceptApiCall() {

    }

    override fun onSuccessSubWorkflowRejectApiCall() {

    }

    override fun onSuccessUsersListforSubworkflow(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
        userListForSubworkflowResponse: UserListForSubworkflowResponse?,
        historyCallback: HistoryCallback,
    ) {
        hideLoading()
        showActionPopup(data, responseList, position, row, userListForSubworkflowResponse)
    }

    override fun onSuccessActionUpdate(
        ticketSubworkflowActionUpdateResponse: TicketSubworkflowActionUpdateResponse?,
        row: SubworkflowConfigDetailsResponse.Rows,
        remark: String,
        data1: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        hideLoading()
        responseList.get(position).isExpanded = false

        if (ticketSubworkflowActionUpdateResponse != null && ticketSubworkflowActionUpdateResponse.success!! && ticketSubworkflowActionUpdateResponse.data != null && ticketSubworkflowActionUpdateResponse.data!!.status != null && ticketSubworkflowActionUpdateResponse.data!!.status != null) {
            responseList.get(position).status!!.code =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.code!! //"solved"
            responseList.get(position).status!!.name =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.name!! //"Resolved"
            responseList.get(position).status!!.background_color =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.background_color //"#047604"
            responseList.get(position).status!!.text_color =
                ticketSubworkflowActionUpdateResponse.data!!.status!!.text_color!! //"#FFFFFF"
        }
        if (ticketSubworkflowActionUpdateResponse != null && ticketSubworkflowActionUpdateResponse.success!!
            && ticketSubworkflowActionUpdateResponse.data!! != null && ticketSubworkflowActionUpdateResponse.data!!.ticketSubworkflowInfo != null
        ) {
            responseList.get(position).ticketSubworkflowInfo =
                ticketSubworkflowActionUpdateResponse.data!!.ticketSubworkflowInfo!!
        }
        if (ticketSubworkflowActionUpdateResponse != null && ticketSubworkflowActionUpdateResponse.success!!
            && ticketSubworkflowActionUpdateResponse.data!! != null && ticketSubworkflowActionUpdateResponse.data!!.ticket_subworkflow_history != null
        ) {
            responseList.get(position).ticket_subworkflow_history =
                ticketSubworkflowActionUpdateResponse.data!!.ticket_subworkflow_history!!
        }
        orderData = responseList
        historyCallback.onSuccessBack(complaintsListDetailsCallback)
//        adapter.notifyItemChanged(position)
    }

    override fun onSuccessSubworkflowConfigDetailsApi(
        responseSubworkflowConfigDetailsResponse: SubworkflowConfigDetailsResponse,
        position: Int,
    ) {
        Utlis.hideLoading()
        orderDataWp.subworkflowConfigDetailsResponse =
            responseSubworkflowConfigDetailsResponse
//       var orderData: ArrayList<ResponseNewTicketlist.Row> = adapter.getData()
//
//        orderData.get(position).subworkflowConfigDetailsResponse
//        adapter.setData()
//        adapter.notifyItemChanged(position)
    }

    override fun onFailureSubworkflowConfigDetailsApi(message: String) {
        Utlis.hideLoading()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun checkResonDepot(
        reasonDept: List<ReasonDept>,
        employeeDetailsResponse: EmployeeDetailsResponse,
    ): Boolean {
        for (data in reasonDept) {
            if (data.department.uid.equals(employeeDetailsResponse.data?.department?.uid)) {
                return true
            }
        }
        return false
    }

    override fun onClickTicketResolveHis(
        data: ResponseNewTicketlist.Row,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        historyCallback: HistoryCallback,
    ) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason!!.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Resolve"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
//                showLoading()
                val inventoryAcceptrejectModel = TicketResolveCloseModel(
                    "resolve",
                    remark.text.toString(),
                    userData.EMPID,
                    "solved",
                    data.ticket_id!!,
                    null
                )
                responseList.get(position).status!!.name = "Resolved"
                responseList.get(position).status!!.background_color = "#047604"
                responseList.get(position).status!!.text_color = "#FFFFFF"
                responseList.get(position).status!!.code = "solved"

                data.isExpanded = false

//                adapter.notifyDataSetChanged()
                viewModel.actionTicketResolveClose(
                    inventoryAcceptrejectModel,
                    historyCallback,
                    complaintsListDetailsCallback
                )
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

//    override fun onClickTicketResolveHistory() {
//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_comment)
//        val body = dialog.findViewById(R.id.textHead) as TextView
//        body.text = "Ticket Details"
//        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
//        ticketNo.text = data.ticket_id
//        val regDate = dialog.findViewById(R.id.regDate) as TextView
//        regDate.text = Utlis.convertCmsDate(data.created_time!!)
//        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
//        problemDesc.text = data.reason!!.name
//        val remark = dialog.findViewById(R.id.remark) as EditText
//        val yesBtn = dialog.findViewById(R.id.submit) as Button
//        yesBtn.text = "Resolve"
//        val noBtn = dialog.findViewById(R.id.reject) as Button
//        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
//        dialogClose.setOnClickListener { dialog.dismiss() }
//        yesBtn.setOnClickListener {
//            if (remark.text.toString().isEmpty()) {
//                remark.error = "Please enter comment"
//                remark.requestFocus()
//            } else {
//                dialog.dismiss()
////                showLoading()
//                val inventoryAcceptrejectModel = TicketResolveCloseModel(
//                    "resolve",
//                    remark.text.toString(),
//                    userData.EMPID,
//                    "solved",
//                    data.ticket_id!!,
//                    null
//                )
//                responseList.get(position).status!!.name = "Resolved"
//                responseList.get(position).status!!.background_color = "#047604"
//                responseList.get(position).status!!.text_color = "#FFFFFF"
//                responseList.get(position).status!!.code = "solved"
//
//                data.isExpanded = false
//
////                adapter.notifyDataSetChanged()
//                viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
//            }
//        }
//        noBtn.setOnClickListener { dialog.dismiss() }
//        dialog.show()
//    }

    fun showActionPopup(
        data: TicketData,
        responseList: java.util.ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
        userListForSubworkflowResponse: UserListForSubworkflowResponse?,
    ) {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = data.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(data.created_time)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = data.reason.name
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        val userSelect = dialog.findViewById(R.id.user_list_selected_item) as EditText

        yesBtn.text = row.action!!.name!!
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        var userDropdownLayout =
            dialog.findViewById(R.id.user_dropdown_layout) as LinearLayout
//        var userForsubworkflow = UserListForSubworkflowResponse.Rows()
        if ((row.action!!.code.equals("forward") || row.action!!.code.equals("change_forward_manager")) && row.assignToUser!!.uid!!.equals(
                "Yes"
            )
        ) {
            if (userListForSubworkflowResponse != null
                && userListForSubworkflowResponse.success!!
                && userListForSubworkflowResponse.data != null
                && userListForSubworkflowResponse.data!!.listData != null
                && userListForSubworkflowResponse.data!!.listData!!.rows != null
                && userListForSubworkflowResponse.data!!.listData!!.rows!!.size > 0
            ) {
                userDropdownLayout.visibility = View.VISIBLE
                usersList = userListForSubworkflowResponse.data!!.listData!!.rows!!

                var userListSelectedItem =
                    dialog.findViewById(R.id.user_list_selected_item) as EditText
                userListSelectedItem.setOnClickListener {
                    userListDialog = Dialog(requireContext())
                    val dialogUserListBinding =
                        DataBindingUtil.inflate<DialogUserListBinding>(
                            LayoutInflater.from(requireContext()),
                            R.layout.dialog_user_list,
                            null,
                            false
                        )
                    userListDialog.setContentView(dialogUserListBinding.root)
                    userListDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    userListDialog.setCancelable(false)
                    dialogUserListBinding.closeDialog.setOnClickListener {
                        userListDialog.dismiss()
                    }
//                    userListAdapter = UserListAdapter(
//                        this,
//                        requireContext(),
//                        usersList!!, userSelect
//                    )
//                    dialogUserListBinding.locationRcv.adapter = userListAdapter
//                    dialogUserListBinding.locationRcv.layoutManager =
//                        LinearLayoutManager(requireContext())

                    dialogUserListBinding.searchLocationListText.addTextChangedListener(object :
                        TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int,
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int,
                        ) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            filterUserList(s.toString(), dialogUserListBinding)
                        }
                    })
                    userListDialog.show()
                }
                /*  val spinner = dialog.findViewById(R.id.user_list_for_subworkflow_spinner) as Spinner
                  var usersListforSubworkflowSpinnerAdapter =
                      UsersListforSubworkflowSpinnerAdapter(
                          requireContext(),
                          userListForSubworkflowResponse.data!!.listData!!.rows!!
                      )
                  spinner.adapter = usersListforSubworkflowSpinnerAdapter
                  spinner.setSelection(0)
                  spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                      override fun onItemSelected(
                          parent: AdapterView<*>?,
                          view: View,
                          positionDropDown: Int,
                          id: Long,
                      ) {
                          userForsubworkflow =
                              userListForSubworkflowResponse.data!!.listData!!.rows!!.get(
                                  positionDropDown
                              )
                      }

                      override fun onNothingSelected(parent: AdapterView<*>?) {}
                  })*/
            } else {
                userDropdownLayout.visibility = View.GONE
            }
        } else {
            userDropdownLayout.visibility = View.GONE
        }

        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else if ((row.action!!.code.equals("forward") || row.action!!.code.equals("change_forward_manager")) && row.assignToUser!!.uid!!.equals(
                    "Yes"
                ) && userSelect.text.toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "Please select user", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                if (NetworkUtil.isNetworkConnected(requireContext())) {
                    Utlis.showLoading(requireContext())
                    var ticketSubworkflowActionUpdateRequest =
                        TicketSubworkflowActionUpdateRequest()
                    ticketSubworkflowActionUpdateRequest.uid = responseList.get(position).uid
                    ticketSubworkflowActionUpdateRequest.comment = "${remark.text.toString()}"
                    ticketSubworkflowActionUpdateRequest.employee_id =
                        "${Preferences.getValidatedEmpId()}"//"RH75774748" //"SE35674"
                    // "SE35674"//${Preferences.getValidatedEmpId()}
                    ticketSubworkflowActionUpdateRequest.items_uid =
                        data.ticket_inventory!!.ticket_inventory_item!!.get(0).uid
                    if (row.action!!.code != null && row.action!!.code.equals("forward_to_manager")) {
                        ticketSubworkflowActionUpdateRequest.items_uid =
                            data.ticket_inventory!!.ticket_inventory_item!!.get(0).uid
                        var manager = TicketSubworkflowActionUpdateRequest.Manager()
                        manager.uid = data.manager!!.uid
                        ticketSubworkflowActionUpdateRequest.manager = manager
                        var ticket = TicketSubworkflowActionUpdateRequest.Ticket()
                        ticket.uid = responseList.get(position).uid
                        ticketSubworkflowActionUpdateRequest.ticket = ticket
                    }

                    if (row.action!!.code != null && row.action!!.code.equals("change_forward_manager") && row.assignToUser!!.uid!!.equals(
                            "Yes"
                        )
                    ) {
                        ticketSubworkflowActionUpdateRequest.items_uid =
                            data.ticket_inventory!!.ticket_inventory_item!!.get(0).uid
                        var manager = TicketSubworkflowActionUpdateRequest.Manager()
                        manager.uid = userForsubworkflow!!.uid
                        ticketSubworkflowActionUpdateRequest.manager = manager
                        var ticket = TicketSubworkflowActionUpdateRequest.Ticket()
                        ticket.uid = responseList.get(position).uid
                        ticketSubworkflowActionUpdateRequest.ticket = ticket

                        var oldManager = TicketSubworkflowActionUpdateRequest.OldManager()
                        oldManager.uid = data.manager!!.uid
                        ticketSubworkflowActionUpdateRequest.old_manager = oldManager

                        var toUser = TicketSubworkflowActionUpdateRequest.ToUser()
                        toUser.uid = userForsubworkflow.uid
                        toUser.firstName = userForsubworkflow.firstName
                        toUser.middleName = userForsubworkflow.middleName
                        toUser.lastName = userForsubworkflow.lastName
                        toUser.loginUnique = userForsubworkflow.loginUnique
                        var role = TicketSubworkflowActionUpdateRequest.Role()
                        role.uid = userForsubworkflow.role!!.uid
                        role.code = userForsubworkflow.role!!.code
                        role.name = userForsubworkflow.role!!.name
                        toUser.role = role
                        var level = TicketSubworkflowActionUpdateRequest.Level()
                        level.uid = userForsubworkflow.level!!.uid
                        level.name = userForsubworkflow.level!!.name
                        toUser.level = level
                        ticketSubworkflowActionUpdateRequest.toUser = toUser


                        var site = TicketSubworkflowActionUpdateRequest.Site()
                        site.uid = data.site.uid
                        site.site = data.site.site
                        ticketSubworkflowActionUpdateRequest.site = site
                    }

                    var subworkflow = TicketSubworkflowActionUpdateRequest.Subworkflow()
                    subworkflow.uid = row.uid!!
                    ticketSubworkflowActionUpdateRequest.subworkflow = subworkflow

                    if (row.action!!.code.equals("forward") && row.assignToUser!!.uid!!.equals("Yes")) {
                        var toUser = TicketSubworkflowActionUpdateRequest.ToUser()
                        toUser.uid = userForsubworkflow.uid
                        toUser.firstName = userForsubworkflow.firstName
                        toUser.middleName = userForsubworkflow.middleName
                        toUser.lastName = userForsubworkflow.lastName
                        toUser.loginUnique = userForsubworkflow.loginUnique
                        var role = TicketSubworkflowActionUpdateRequest.Role()
                        role.uid = userForsubworkflow.role!!.uid
                        role.code = userForsubworkflow.role!!.code
                        role.name = userForsubworkflow.role!!.name
                        toUser.role = role
                        var level = TicketSubworkflowActionUpdateRequest.Level()
                        level.uid = userForsubworkflow.level!!.uid
                        level.name = userForsubworkflow.level!!.name
                        toUser.level = level
                        ticketSubworkflowActionUpdateRequest.toUser = toUser
                    }

                    viewModel.actionUpdateApiCall(
                        this,
                        ticketSubworkflowActionUpdateRequest,
                        row, remark.text.toString(),
                        data,
                        responseList,
                        position,
                    )
                }
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun confirmsavetheticket() {

    }


    private fun filterUserList(
        searchText: String,
        dialogUserListBinding: DialogUserListBinding?,
    ) {
        val filteredList = java.util.ArrayList<UserListForSubworkflowResponse.Rows>()
        for (i in usersList!!.indices) {
            if (searchText.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(usersList!!)
            } else {
                var userName = ""
                if (usersList!![i].firstName != null) {
                    userName = "${usersList!![i].firstName}"
                }
                if (usersList!![i].middleName != null) {
                    userName = "$userName ${usersList!![i].middleName}"
                }
                if (usersList!![i].lastName != null) {
                    userName = "$userName ${usersList!![i].lastName}"
                }
                if (usersList!![i].loginUnique != null) {
                    userName = "$userName (${usersList!![i].loginUnique})"
                }

                if (userName!!.contains(searchText, true)) {
                    filteredList.add(usersList!![i])
                }
            }
        }
        if (filteredList.size < 1) {
            dialogUserListBinding!!.locationRcv.visibility = View.GONE
            dialogUserListBinding.locationAvailable.visibility = View.VISIBLE
        } else {
            dialogUserListBinding!!.locationRcv.visibility = View.VISIBLE
            dialogUserListBinding.locationAvailable.visibility = View.GONE
        }
        userListAdapter.filter(filteredList)
    }

    override fun onSelectedManager(data: Row) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment)
        val body = dialog.findViewById(R.id.textHead) as TextView
        body.text = "Ticket Details"
        val ticketNo = dialog.findViewById(R.id.ticketNo) as TextView
        ticketNo.text = selectedInventeryTicket.ticket_id
        val regDate = dialog.findViewById(R.id.regDate) as TextView
        regDate.text = Utlis.convertCmsDate(selectedInventeryTicket.created_time!!)
        val problemDesc = dialog.findViewById(R.id.problemDesc) as TextView
        problemDesc.text = selectedInventeryTicket.ticketDetailsResponse?.data?.description
        val remark = dialog.findViewById(R.id.remark) as EditText
        val yesBtn = dialog.findViewById(R.id.submit) as Button
        yesBtn.text = "Forward to Manager"
        val noBtn = dialog.findViewById(R.id.reject) as Button
        val dialogClose = dialog.findViewById(R.id.diloga_close) as ImageView
        dialogClose.setOnClickListener { dialog.dismiss() }
        yesBtn.setOnClickListener {
            if (remark.text.toString().isEmpty()) {
                remark.error = "Please enter comment"
                remark.requestFocus()
            } else {
                dialog.dismiss()
                showLoading()
                var request = ChangeManagerRequest(
                    remark.text.toString(),
                    ChangeManager(data.uid),
                    OldManager(selectedInventeryTicket.ticketDetailsResponse!!.data.site.uid),
                    ChangeSite(
                        selectedInventeryTicket.ticketDetailsResponse!!.data.site.site,
                        selectedInventeryTicket.ticketDetailsResponse!!.data.site.uid
                    ),
                    selectedInventeryTicket.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item[0].uid,
                    userData.EMPID,
                    CCTicket(selectedInventeryTicket.ticketDetailsResponse!!.data.uid)
                )
                var actionRequest = ChangeManagerRequest(
                    remark.text.toString(),
                    ChangeManager(data.uid),
                    OldManager(selectedInventeryTicket.ticketDetailsResponse!!.data.site.manager.uid),
                    ChangeSite(
                        selectedInventeryTicket.ticketDetailsResponse!!.data.site.site,
                        selectedInventeryTicket.ticketDetailsResponse!!.data.site.uid
                    ),
                    selectedInventeryTicket.ticketDetailsResponse!!.data.uid,
                    userData.EMPID,
                    CCTicket(selectedInventeryTicket.ticketDetailsResponse!!.data.uid)
                )
                viewModel.actionChangeForwardToManager(
                    actionRequest,
                    request,
                    0,
                    complaintsListDetailsCallback
                )

                if (resListMnger != null) {
                    resListMnger.get(frwdMngrPos).isExpanded = false
//                    adapter.notifyDataSetChanged()
                }
            }
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    fun onCLickAssignedToMe() {
        viewBinding.assignedToMe.setOnClickListener {
            var dialog = Dialog(requireContext())
            var dialogCustomAlertBinding: DialogCustomAlertBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_custom_alert,
                null,
                false
            )
            dialog.setContentView(dialogCustomAlertBinding.root)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogCustomAlertBinding.message.text =
                "${orderDataWp!!.ticketDetailsResponse!!.data!!.ticket_id} is assigned to ${orderDataWp!!.ticketDetailsResponse!!.data!!.ticketSubworkflowInfo!!.assigned_to!!.first_name}. Are you sure you want to assign yourself?"
            dialogCustomAlertBinding.cancelButton.setOnClickListener {
                dialog.dismiss()
            }
            dialogCustomAlertBinding.okBtn.setOnClickListener {
                doActionAssignToMe()
            }
            dialog.show()
        }
    }
}