package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.databinding.FragmentAttachmentsBinding
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ComplainListFragment
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ImageClickListener
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.callback.AttachmentsCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.viewmodel.AttachmentsFragmentViewModel
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.CmsTicketRequest
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.SubworkflowConfigDetailsResponse
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketData
import com.apollopharmacy.vishwam.util.PhotoPopupWindow
import com.bumptech.glide.Glide
import java.util.ArrayList

class AttachmentsFragment(private var orderDataWp: ResponseNewTicketlist.Row, private var imageClickListener: ImageClickListener, private var position: Int) :  BaseFragment<AttachmentsFragmentViewModel, FragmentAttachmentsBinding>(),
    ImageClickListener, AttachmentsCallback {
    var isHavingProblemImages =false

    override val layoutRes: Int
        get() = R.layout.fragment_attachments

    override fun retrieveViewModel(): AttachmentsFragmentViewModel {
        return ViewModelProvider(this).get(AttachmentsFragmentViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        if (orderDataWp.ticketDetailsResponse?.data?.problem_images != null && orderDataWp.ticketDetailsResponse?.data?.problem_images?.images != null
            && orderDataWp.ticketDetailsResponse?.data?.problem_images?.images!!.size>0) {
            isHavingProblemImages=true
        }else if (orderDataWp != null && orderDataWp.ticketDetailsResponse != null
            && orderDataWp.ticketDetailsResponse!!.data != null && orderDataWp.ticketDetailsResponse!!.data.ticket_inventory != null
            && orderDataWp.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item.size > 0
        ){
            isHavingProblemImages=false
        }

            if(!isHavingProblemImages){
                if (orderDataWp != null && orderDataWp.ticketDetailsResponse != null
                    && orderDataWp.ticketDetailsResponse!!.data != null && orderDataWp.ticketDetailsResponse!!.data.ticket_inventory != null
                    && orderDataWp.ticketDetailsResponse!!.data.ticket_inventory.ticket_inventory_item.size > 0
                ) {
                    if (!orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.isNullOrEmpty()) {
                        viewBinding.inventoryImagesLayout.visibility = View.VISIBLE
                        viewBinding.emptyList.visibility = View.GONE
                        Glide.with(ViswamApp.context)
                            .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob)
                            .placeholder(R.drawable.thumbnail_image).into(viewBinding.frontImgView)
                        viewBinding.frontImgView.setOnClickListener {
                            orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.let { it1 ->
                                imageClickListener.onItemClick(position, it1)
                            }
                        }
                    } else {
                        viewBinding.frontImgLabel.visibility = View.GONE
                        viewBinding.frontImgView.visibility = View.GONE
                    }
                    if (!orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob.isNullOrEmpty()) {
                        Glide.with(ViswamApp.context)
                            .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob)
                            .placeholder(R.drawable.thumbnail_image).into(viewBinding.backImgView)
                        viewBinding.backImgView.setOnClickListener {
                            orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob.let { it1 ->
                                imageClickListener.onItemClick(position, it1)
                            }
                        }
                    } else {
                        viewBinding.backImgLabel.visibility = View.GONE
                        viewBinding.backImgView.visibility = View.GONE
                    }
                    if (!orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob.isNullOrEmpty()) {
                        Glide.with(ViswamApp.context)
                            .load(orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob)
                            .placeholder(R.drawable.thumbnail_image).into(viewBinding.otherImgView)
                        viewBinding.otherImgView.setOnClickListener {
                            orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob.let { it1 ->
                                imageClickListener.onItemClick(position, it1)
                            }
                        }
                        viewBinding.otherImgBg.visibility=View.VISIBLE
                        viewBinding.otherImgLabel.visibility = View.VISIBLE
                        viewBinding.otherImgView.visibility = View.VISIBLE
                    } else {
                        viewBinding.otherImgBg.visibility=View.GONE
                        viewBinding.otherImgLabel.visibility = View.GONE
                        viewBinding.otherImgView.visibility = View.GONE
                    }
                    viewBinding.inventoryImagesLayout.visibility = View.VISIBLE
                    viewBinding.emptyList.visibility = View.GONE

                } else {
                    viewBinding.inventoryImagesLayout.visibility = View.GONE
                    viewBinding.emptyList.visibility = View.VISIBLE
                }
            }else{
                var problemimages = ArrayList<ResponseNewTicketlist.Image>()
                if (orderDataWp.ticketDetailsResponse?.data?.problem_images != null && orderDataWp.ticketDetailsResponse?.data?.problem_images?.images != null) {
                    problemimages =
                        orderDataWp.ticketDetailsResponse?.data?.problem_images!!.images
                    viewBinding.addedImagesRecyclerView.adapter =
                        ComplainListFragment.ImageRecyclerView(problemimages, imageClickListener)
                    viewBinding.attacheImages.visibility = View.VISIBLE
                    viewBinding.emptyListProblemImages.visibility = View.GONE
                    viewBinding.emptyList.visibility = View.GONE
                    viewBinding.inventoryImagesLayout.visibility = View.GONE
                } else {
                    viewBinding.attacheImages.visibility = View.GONE
                    viewBinding.inventoryImagesLayout.visibility = View.GONE
                    viewBinding.emptyListProblemImages.visibility = View.VISIBLE
                }
            }


    }



    override fun onItemClick(position: Int, imagePath: String) {
        PhotoPopupWindow(context, R.layout.layout_image_fullview, view, imagePath, null)
    }

    override fun onComplaintItemClick(
        position: Int,
        orderData: ArrayList<ResponseNewTicketlist.Row>,
        get: ResponseNewTicketlist.Row,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickForwardToFinance(
        data: CmsTicketRequest,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickCCAccept(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickCCReject(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickInventoryAccept(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickInventoryReject(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickForwardToManager(data: ResponseNewTicketlist.Row) {
        TODO("Not yet implemented")
    }

    override fun onClickForwardChangeManager(
        data: ResponseNewTicketlist.Row,
        position: Int,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickTicketResolve(
        data: ResponseNewTicketlist.Row,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {
        Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show()
    }

    override fun onClickTicketClose(
        data: ResponseNewTicketlist.Row,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickTicketReopen(
        data: ResponseNewTicketlist.Row,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickSubWorkflowAccept(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickSubWorkflowReject(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
    ) {

    }

    override fun onClickAction(
        data: TicketData,
        responseList: ArrayList<ResponseNewTicketlist.Row>,
        position: Int,
        row: SubworkflowConfigDetailsResponse.Rows,
    ) {

    }

    override fun onClickPreviewIconBackOther(url: String?, view: View) {
        if(!url.isNullOrEmpty()){
            PhotoPopupWindow(context, R.layout.layout_image_fullview, view, url, null)
        }
    }

    override fun onClickPreviewIconFront() {
        orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].front_img_blob.let { it1 ->
//            imageClickListener.onItemClick(position, it1)
            PhotoPopupWindow(context, R.layout.layout_image_fullview, view, it1, null)
        }
    }

    override fun onClickPreviewIconBack() {
        orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].back_img_blob.let { it1 ->
//            imageClickListener.onItemClick(position, it1)
            PhotoPopupWindow(context, R.layout.layout_image_fullview, view, it1, null)
        }
    }

    override fun onClickPreviewIconOther() {
        orderDataWp.ticketDetailsResponse?.data?.ticket_inventory!!.ticket_inventory_item[0].other_img_blob.let { it1 ->
//            imageClickListener.onItemClick(position, it1)
            PhotoPopupWindow(context, R.layout.layout_image_fullview, view, it1, null)
        }
    }


}