package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.databinding.FragmentDetailsBinding
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.viewmodel.DetailsFragmentViewModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.checkResonDepot
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CmsTicketRequest
import com.apollopharmacy.vishwam.util.Utlis
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment(
    private val orderDataWp: ResponseNewTicketlist.Row,
) : BaseFragment<DetailsFragmentViewModel, FragmentDetailsBinding>() {

    override fun retrieveViewModel(): DetailsFragmentViewModel {
        return ViewModelProvider(this).get(DetailsFragmentViewModel::class.java)
    }

    override val layoutRes: Int
        get() = R.layout.fragment_details

    @SuppressLint("SetTextI18n")
    override fun setup() {
        viewBinding.staffNameText.text =
            orderDataWp.ticketDetailsResponse?.data?.created_id?.first_name + (if (orderDataWp.ticketDetailsResponse?.data?.created_id?.middle_name != null) " " + orderDataWp.ticketDetailsResponse?.data?.created_id?.middle_name else "") + (if (orderDataWp.ticketDetailsResponse?.data?.created_id?.last_name != null) " " + orderDataWp.ticketDetailsResponse?.data?.created_id?.last_name else "") + " (" + orderDataWp.ticketDetailsResponse?.data?.created_id?.login_unique + ")"
        viewBinding.departmentName.text = orderDataWp.ticketDetailsResponse?.data?.department?.name
        viewBinding.problemSinceText.text = orderDataWp.created_time?.let {
            Utlis.convertCmsDate(it)
        }

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
        var isDonthaveInventory = false
        if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.uid != null && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item.isNotEmpty()) {
            viewBinding.inventoryDetailsLayout.visibility = View.VISIBLE
//                binding.articleCode.text =
//                    "${items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item[0].item_code}"
//              if (items.inventoryDetailsModel?.data?.ticket_inventory!!.ticket_inventory_item)
            if(!orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_name.isNullOrEmpty()){
                viewBinding.articleName.text =
                    "${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_name}"
            }else{
                viewBinding.articleName.text="--"
            }

            viewBinding.batchNumber.text =
                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].batch_no}"
            viewBinding.barcode.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].barcode
                    ?: "--"
            viewBinding.expairyDate.text =
                Utlis.convertCmsExparyDate(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].expiry_date)

            viewBinding.purchaseRate.text =
                "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].purchase_rate}"
            if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req")) {
                viewBinding.oldMrpLabel.text = "MRP : "
                viewBinding.oldMrp.text =
                    "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].mrp}"
                viewBinding.newMrp.visibility = View.GONE
                viewBinding.newMrpLabel.visibility = View.GONE
            } else {
                viewBinding.oldMrp.text =
                    "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].old_mrp}"
                viewBinding.newMrp.text =
                    "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].new_mrp}"
            }
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

//            if (orderDataWp.status!!.code.equals("inprogress") || orderDataWp.status!!.code.equals("reopen")) {
//                if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("mrp_cr") && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid != null && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid.equals(
//                        "forward"
//                    ) && managerUid.equals(employeeDetailsResponse?.data!!.uid)
//                ) {
//                    viewBinding.inventoryActionLayout.visibility = View.VISIBLE
//                    viewBinding.inventoryRejectBtn.visibility = View.VISIBLE
//                    viewBinding.inventoryForwardManagerBtn.visibility = View.GONE
//                    viewBinding.inventoryChangeForwardBtn.visibility = View.VISIBLE
//                    viewBinding.inventoryAcceptBtn.text = "Approve"
//                } else if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req") && orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].item_status.uid == null && employeeDetailsResponse?.data!!.uid.equals(
//                        orderDataWp.ticketDetailsResponse?.data?.user!!.uid
//                    )
//                ) {
//                    viewBinding.inventoryActionLayout.visibility = View.VISIBLE
//                    viewBinding.inventoryRejectBtn.visibility = View.VISIBLE
//                    viewBinding.inventoryForwardManagerBtn.visibility = View.GONE
//                    viewBinding.inventoryChangeForwardBtn.visibility = View.GONE
//                    viewBinding.inventoryAcceptBtn.text = "Approve"
//                }
//                viewBinding.inventoryAcceptBtn.setOnClickListener {
//                    imageClickListener.onClickInventoryAccept(orderDataWp, position, orderData)
//                }
//                viewBinding.inventoryRejectBtn.setOnClickListener {
//                    imageClickListener.onClickInventoryReject(orderDataWp, position, orderData)
//                }
//                viewBinding.inventoryForwardManagerBtn.setOnClickListener {
//                    imageClickListener.onClickForwardToManager(orderDataWp)
//                }
//                viewBinding.inventoryChangeForwardBtn.setOnClickListener {
//                    imageClickListener.onClickForwardChangeManager(orderDataWp, position, orderData)
//                }
//            } else {
//                viewBinding.inventoryActionLayout.visibility = View.GONE
//            }
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
            viewBinding.inventoryDetailsLayout.visibility = View.GONE
//            viewBinding.inventoryActionLayout.visibility = View.GONE
//            viewBinding.inventoryImagesLayout.visibility = View.GONE
        }



        orderDataWp.created_id?.first_name + (if (orderDataWp.created_id?.middle_name != null) " " + orderDataWp.created_id?.middle_name else "") + (if (orderDataWp.created_id?.last_name != null) " " + orderDataWp.created_id?.last_name else "") + " (" + orderDataWp.created_id?.login_unique + ")"
//            if(items.department?.code.equals("IT")){
        if (!TextUtils.isEmpty(orderDataWp.ticketDetailsResponse?.data?.executive?.first_name)) {
            viewBinding.itTicketExecutive.text =
                orderDataWp.ticketDetailsResponse?.data?.executive?.first_name + (if (orderDataWp.ticketDetailsResponse?.data?.executive?.middle_name != null) " " + orderDataWp.ticketDetailsResponse?.data?.executive?.middle_name else "") + (if (orderDataWp.ticketDetailsResponse?.data?.executive?.last_name != null) " " + orderDataWp.ticketDetailsResponse?.data?.executive?.last_name else "") + " (" + orderDataWp.ticketDetailsResponse?.data?.executive?.login_unique + ")"
            viewBinding.itTicketExecutiveLayout.visibility = View.VISIBLE
        } else {
            viewBinding.itTicketExecutiveLayout.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(orderDataWp.ticketDetailsResponse?.data?.manager?.first_name)) {
            viewBinding.itTicketManager.text =
                orderDataWp.ticketDetailsResponse?.data?.manager?.first_name + (if (orderDataWp.ticketDetailsResponse?.data?.manager?.middle_name != null) " " + orderDataWp.ticketDetailsResponse?.data?.manager?.middle_name else "") + (if (orderDataWp.ticketDetailsResponse?.data?.manager?.last_name != null) " " + orderDataWp.ticketDetailsResponse?.data?.manager?.last_name else "") + " (" + orderDataWp.ticketDetailsResponse?.data?.manager?.login_unique + ")"
            viewBinding.itTicketManagerLayout.visibility = View.VISIBLE
        } else {
            viewBinding.itTicketManagerLayout.visibility = View.GONE
        }


//            }else{
//                binding.itTicketExecutiveLayout.visibility = View.GONE
//                binding.itTicketManagerLayout.visibility = View.GONE
//            }
        if (orderDataWp.ticketDetailsResponse?.data?.ticket_it?.tid?.uid != null) {
            viewBinding.creditCardDetailsLayout.visibility = View.VISIBLE
//                binding.ccReason.text = " ${items.creditCardTSDetails?.data?.reason!!.name }"
//                binding.ccExecutive.text = " ${items.creditCardTSDetails?.data?.executive!!.first_name }"
//                binding.ccManager.text = items.creditCardTSDetails?.data?.manager!!.first_name
            viewBinding.ccTid.text = " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.tid.tid}"
            viewBinding.billNumber.text =
                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.bill_number}"
            viewBinding.transactionNumber.text =
                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.transaction_id}"
            viewBinding.approvalCode.text =
                " ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.approval_code}"
            viewBinding.billAmount.text =
                "₹ ${orderDataWp.ticketDetailsResponse?.data?.ticket_it!!.bill_amount}"


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
//                    viewBinding.ccActionLayout.visibility = View.GONE
                } else {
                    if (orderDataWp.ticketDetailsResponse!!.data.ticket_it.status.uid == null) {
                        //Change to visible if required
//                        viewBinding.ccActionLayout.visibility = View.GONE
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
//                        viewBinding.frwdToFinance.setOnClickListener {
//
//                            val cmsTicketRequest = CmsTicketRequest()
//                            val cms = CmsTicketRequest.Ticket()
//                            cmsTicketRequest.uid =
//                                orderDataWp.ticketDetailsResponse!!.data.ticket_it.uid
//                            cms.uid = orderDataWp.ticketDetailsResponse!!.data.uid
//                            cmsTicketRequest.ticket = cms
//
//
////
//
//
//                            imageClickListener.onClickForwardToFinance(
//                                cmsTicketRequest, orderData, position
//                            )
////                            imageClickListener.onClickCCReject(items.ticketDetailsResponse!!.data)
//                        }
                    } else {
//                        viewBinding.ccActionLayout.visibility = View.GONE
                    }
                }
            } else {
//                viewBinding.ccActionLayout.visibility = View.GONE
            }
        }
        else {
            viewBinding.creditCardDetailsLayout.visibility = View.GONE
//            viewBinding.ccActionLayout.visibility = View.GONE
        }

        if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request?.uid != null) {
            viewBinding.drugLayout.drugDetailsLayout.visibility = View.VISIBLE
//                binding.drugLayout.drugBarcode.text = items.ticket_inventory.drug_request.barcode ?: "--"
            viewBinding.drugLayout.drugItemNumber.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.item_name
                    ?: "--"
//                binding.drugLayout.drugItemDetailsNumber.text = items.subcategory?.name ?: "--"
            viewBinding.drugLayout.drugPackSize.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.pack_size.toString()
                    ?: "--"
            viewBinding.drugLayout.drugMrp.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.mrp.toString()
                    ?: "--"
//                binding.drugLayout.drugPurchasePrice.text =
//                    items.ticket_inventory.drug_request.purchase_price.toString() ?: "--"
//                binding.drugLayout.drugRemarks.text = items.ticket_inventory.drug_request.remarks ?: "--"
            viewBinding.drugLayout.drugBatchNo.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.batch_no
                    ?: "--"


            //From - made changes by naveen//
            viewBinding.drugLayout.itemType.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.item_type.name
            viewBinding.drugLayout.requiredQty.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.required_quantity.toString()
            viewBinding.drugLayout.doctorName.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.doctors_name
            viewBinding.drugLayout.doctorSpeciality.text =
                orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.drug_request.doctor_specialty.name

            //To - made changes by naveen//


//                binding.drugLayout.drugManufactuing.text = if(items.ticket_inventory.drug_request?.manufacturing_date == null ){ "--"} else { Utlis.convertCmsExparyDate(items.ticket_inventory.drug_request.manufacturing_date)}
//                binding.drugLayout.drugExpairy.text =  if(items.ticket_inventory.drug_request?.expiry_date == null ){ "--"} else { Utlis.convertCmsExparyDate(items.ticket_inventory.drug_request.expiry_date)}
//                binding.drugLayout.drugHsn.text = items.ticket_inventory.drug_request.hsn_code ?: "--"
//                binding.drugLayout.drugGst.text = items.ticket_inventory.drug_request.gst ?: "--"
//                binding.drugLayout.drugReference.text = items.ticket_inventory.drug_request.reference_no ?: "--"

//            if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.front_mb != null) {
//                Glide.with(ViswamApp.context)
//                    .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.front_mb)
//                    .placeholder(R.drawable.thumbnail_image).into(viewBinding.frontImgView)
//                viewBinding.frontImgView.setOnClickListener {
//                    orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.front_mb.let { it1 ->
//                        imageClickListener.onItemClick(position, it1)
//                    }
//                }
//            } else {
//                viewBinding.frontImgLabel.visibility = View.GONE
//                viewBinding.frontImgView.visibility = View.GONE
//            }
//            if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request?.back_mb != null) {
//                Glide.with(ViswamApp.context)
//                    .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.back_mb)
//                    .placeholder(R.drawable.thumbnail_image).into(viewBinding.backImgView)
//                viewBinding.backImgView.setOnClickListener {
//                    orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request!!.back_mb.let { it1 ->
//                        imageClickListener.onItemClick(position, it1)
//                    }
//                }
//            } else {
//                viewBinding.backImgLabel.visibility = View.GONE
//                viewBinding.backImgView.visibility = View.GONE
//            }
//            viewBinding.inventoryImagesLayout.visibility = View.VISIBLE
        } else {
            viewBinding.drugLayout.drugDetailsLayout.visibility = View.GONE

        }
        viewBinding.complainDetails.text =
            orderDataWp.ticketDetailsResponse?.data?.description?.trim()
                ?.replace("\\s+".toRegex(), " ") ?: "--"
//        if (orderDataWp.status?.code.isNullOrEmpty()) {
//        }
//        else {
//
//            if (orderDataWp.status!!.code.equals("solved") && employeeDetailsResponse?.data!!.uid.equals(
//                    orderDataWp.created_id!!.uid
//                )
//            ) {
//                viewBinding.ticketResolveBtn.visibility = View.VISIBLE
//                if (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("mrp_cr") || items.ticketDetailsResponse?.data?.category?.code.equals(
//                        "new_batch_req"
//                    )
//                ) {
//                    viewBinding.ticketResolveBtn.visibility = View.GONE
//                }
//                if (orderDataWp.ticketDetailsResponse != null) {
//                    if (orderDataWp.ticketDetailsResponse!!.data!!.reason!!.allow_manual_ticket_closure!!.uid == null
//                        || orderDataWp.ticketDetailsResponse!!.data!!.reason!!.allow_manual_ticket_closure!!.uid == "Yes"
//                    ) {
//                        viewBinding.ticketCloseBtn.visibility = View.VISIBLE
//                    } else {
//                        viewBinding.ticketCloseBtn.visibility = View.GONE
//                    }
//                }
//
//
//
//
//
//                viewBinding.ticketCloseBtn.setOnClickListener {
//                    imageClickListener.onClickTicketClose(items, orderData, position)
//                }
//                viewBinding.ticketActionLayout.visibility = View.VISIBLE
//                viewBinding.ticketResolveBtn.text = "Reopen"
//                viewBinding.ticketResolveBtn.setOnClickListener {
//                    imageClickListener.onClickTicketReopen(items, orderData, position)
//                }
//            } else if ((orderDataWp.status!!.code.equals("inprogress") || orderDataWp.status!!.code.equals("reopened")) && employeeDetailsResponse?.data!!.uid.equals(
//                    orderDataWp.ticketDetailsResponse?.data?.user?.uid
//                )
//            ) {
//                if (orderDataWp.ticketDetailsResponse != null && (orderDataWp.ticketDetailsResponse?.data?.category?.code.equals(
//                        "mrp_cr"
//                    ) || orderDataWp.ticketDetailsResponse?.data?.category?.code.equals("new_batch_req"))
//                ) {
//                    viewBinding.ticketResolveBtn.visibility = View.GONE
//                    viewBinding.ticketActionLayout.visibility = View.GONE
//                } else if (orderDataWp.ticketDetailsResponse?.data?.ticket_inventory?.drug_request?.uid != null && orderDataWp.ticketDetailsResponse?.data?.category?.code.equals(
//                        "new_drug_req"
//                    )
//                ) {
//                    viewBinding.ticketResolveBtn.visibility = View.GONE
//                    viewBinding.ticketActionLayout.visibility = View.GONE
//                }
////                    else if (items.status!!.code.equals("inprogress")
////                        && items.ticketDetailsResponse!!.data.department.code.equals(
////                            "IT") && items.ticketDetailsResponse!!.data.category.code.equals("pos")
////                    ) {
////                        binding.ticketResolveBtn.visibility = View.GONE
////                        binding.ticketActionLayout.visibility = View.GONE
////                    }
//
////                    else  if (employeeDetailsResponse?.data!!.department!!.code.equals(
////                            "FN")
////                    ) {
////                        if (items.ticketDetailsResponse!!.data.department.code.equals("IT") && items.ticketDetailsResponse!!.data.category.code.equals(
////                                "pos") && items.ticketDetailsResponse!!.data.reason.code.equals("asb_not_completed")
////                            && items.ticketDetailsResponse!!.data.user.uid.equals(
////                                employeeDetailsResponse?.data!!.uid)
////                            && items.status!!.code.equals("inprogress")
////                            && items.ticketDetailsResponse!!.data.reason.sub_workflow.uid.equals("Yes")
////                        ) {
////
////                            binding.ticketResolveBtn.visibility = View.GONE
////                            binding.ticketActionLayout.visibility = View.GONE
////                        }
////                        else {
////                            binding.ticketResolveBtn.visibility = View.VISIBLE
////                            binding.ticketCloseBtn.visibility = View.GONE
////                            binding.ticketResolveBtn.text = "Resolve"
////                            binding.ticketResolveBtn.setOnClickListener {
////                                imageClickListener.onClickTicketResolve(items)
////                            }
////                            binding.ticketActionLayout.visibility = View.VISIBLE
////                        }
////
////                        }
//
//                else {
//                    viewBinding.ticketResolveBtn.visibility = View.VISIBLE
//                    viewBinding.ticketCloseBtn.visibility = View.GONE
//                    viewBinding.ticketResolveBtn.text = "Resolve"
//                    viewBinding.ticketResolveBtn.setOnClickListener {
//
////                            items.Tickethistory!!.data.listData.rows[0].description=items.Tickethistory!!.data.listData.rows[0].description.replace("Accepted","Resolved")
//                        imageClickListener.onClickTicketResolve(items, orderData, position)
//                    }
//                    viewBinding.ticketActionLayout.visibility = View.VISIBLE
//                }
//            } else {
//                viewBinding.ticketActionLayout.visibility = View.GONE
//            }
//        }


        /*    // soubworkflow manual
                    if (items!!.have_subworkflow != null) {
                        if (items!!.have_subworkflow == true) {
                            if (items!!.is_subworkflow_completed == false) {
                                binding.ticketResolveBtn.visibility = View.GONE
                            }
                            if (isApprovalListFragment) {
                                if (items!!.is_subworkflow_completed == true) {
                                    binding.subWorkflowAcceptrejectLayout.visibility = View.GONE
                                } else {
                                    binding.subWorkflowAcceptrejectLayout.visibility = View.VISIBLE
                                    binding.subWorkflowAcceptBtn.setOnClickListener {
                                        imageClickListener.onClickSubWorkflowAccept(
                                            items.ticketDetailsResponse!!.data, orderData, position
                                        )
                                    }

                                    binding.subWorkflowRejectBtn.setOnClickListener {
                                        imageClickListener.onClickSubWorkflowReject(
                                            items.ticketDetailsResponse!!.data, orderData, position
                                        )
                                    }

                                }
                            } else {
                                binding.subWorkflowAcceptrejectLayout.visibility = View.GONE
                            }
                        } else {
                            binding.subWorkflowAcceptrejectLayout.visibility = View.GONE
                        }
                    } else {
                        binding.subWorkflowAcceptrejectLayout.visibility = View.GONE
                    }
        */



        if (orderDataWp.site?.uid == null) {
            viewBinding.siteidLable.text = "Ticket type: "
            viewBinding.siteid.text = "Self"
            viewBinding.regionLayout.visibility = View.VISIBLE
            viewBinding.locationLayout.visibility = View.VISIBLE
            viewBinding.region.text = orderDataWp.ticketDetailsResponse?.data?.region?.name
            viewBinding.location.text = orderDataWp.ticketDetailsResponse?.data?.location?.name
        } else {
            viewBinding.siteid.text = orderDataWp.site?.site + "-" + orderDataWp.site?.store_name
        }
        viewBinding.ticketCategory.text = orderDataWp.ticketDetailsResponse?.data?.category?.name
//        if (items.ticket_inventory?.drug_request?.uid != null) {
//            viewBinding.subCategoryLayout.visibility = View.GONE
//            viewBinding.regionLayout.visibility = View.GONE
//        } else {
            viewBinding.subCategory.text =
                orderDataWp.ticketDetailsResponse?.data?.subcategory?.name
            viewBinding.region.text = orderDataWp.ticketDetailsResponse?.data?.reason?.name
            viewBinding.subCategoryLayout.visibility = View.VISIBLE
            viewBinding.regionLayout.visibility = View.VISIBLE
//        }
//        binding.siteName.text = items.site.store_name

//
//        if (isApprovalListFragment) {
//            if (orderDataWp.subworkflowConfigDetailsResponse != null && orderDataWp.subworkflowConfigDetailsResponse!!.data != null && orderDataWp.subworkflowConfigDetailsResponse!!.data!!.listData != null && orderDataWp.subworkflowConfigDetailsResponse!!.data!!.listData!!.rows != null && orderData!!.get(
//                    position
//                ).subworkflowConfigDetailsResponse!!.data!!.listData!!.rows!!.size > 0
//            ) {
//                var subworkflowConfigDetailsAdapter = SubworkflowConfigDetailsAdapter(
//                    ViswamApp.context,
//                    imageClickListener,
//                    orderData!!.get(position).subworkflowConfigDetailsResponse!!.data!!.listData!!.rows!!,
//                    items.ticketDetailsResponse!!.data,
//                    orderData,
//                    position
//                )
//                viewBinding.subworkflowConfigDetailsListLayout.visibility = View.VISIBLE
//
//                var layoutManager =
//                    LinearLayoutManager(ViswamApp.context, LinearLayoutManager.HORIZONTAL, false)
//                //attaches LinearLayoutManager with RecyclerView
//                viewBinding.subworkflowConfigDetailsListRecyclerview.layoutManager = layoutManager
//                viewBinding.subworkflowConfigDetailsListRecyclerview.adapter =
//                    subworkflowConfigDetailsAdapter
//            } else {
//                viewBinding.subworkflowConfigDetailsListLayout.visibility = View.GONE
//            }
//
//
//            /*  //Subworkflow action details adapter.....
//              if (orderData != null
//                  && orderData.get(position) != null
//                  && orderData.get(position).ticket_subworkflow_history != null
//                  && orderData.get(position).ticket_subworkflow_history!!.size > 0
//              ) {
//                  if (orderData.get(position).ticketSubworkflowInfo != null
//                      && orderData.get(position).ticketSubworkflowInfo!!.subworkflow_action != null
//                      && orderData.get(position).ticketSubworkflowInfo!!.subworkflow_action!!.action != null
//                  ) {
//                      binding.subworkflowAction.text =
//                      binding.subworkflowAction.text =
//                          "${orderData.get(position).ticketSubworkflowInfo!!.subworkflow_action!!.action}"
//                  }
//                  var subworkflowActionDetailsAdapter = SubworkflowActionDetailsAdapter(
//                      context,
//                      orderData.get(position).ticket_subworkflow_history!!
//                  )
//                  binding.subworkflowDetailsHistoryLayout.visibility = View.VISIBLE
//                  var layoutManager =
//                      LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//                  binding.subworkflowActionDetailsRecyclerview.layoutManager = layoutManager
//                  binding.subworkflowActionDetailsRecyclerview.adapter =
//                      subworkflowActionDetailsAdapter
//              } else {
//                  binding.subworkflowDetailsHistoryLayout.visibility = View.GONE
//              }*/
//        }
    }


}