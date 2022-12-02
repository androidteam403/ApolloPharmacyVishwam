package com.apollopharmacy.vishwam.dialog

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.*
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DialogAcknowledgementBinding
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Feedback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Rating
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.TicketResolveCloseModel
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.ui.home.cms.registration.RegistrationViewModel
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis.convertCmsDate

class AcknowledgementDialog : DialogFragment() {

    lateinit var viewBinding: DialogAcknowledgementBinding
    lateinit var viewModel: RegistrationViewModel
    val TAG = "AcknowledgementDialog"

    var cmsloginresponse: ResponseCMSLogin.Data?=null
    var ticketstatusapiresponse:ResponseTicktResolvedapi.Data?=null
    var ticketratingapiresponse: ResponseticketRatingApi.Data?=null

    var ratingvalue:String?=null

    var ratingduid:String?=null

    var token:String?=null

    var closingstatus:Boolean=false
    lateinit var datanew: ResponseTicktResolvedapi.Data
    var ticketCloseApi:Boolean=false
    protected var mProgressDialog: ProgressDialog? = null
    companion object {
        const val KEY_DATA_ACK = "acknowledgement"
    }

    init {
    }

    fun generateParsedData(data: ArrayList<DataItem>, tag: String): Bundle {
        return Bundle().apply {
            putSerializable(SiteDialog.KEY_DATA, data)
        }
    }

     fun generateParsedDataNew(data: ResponseTicktResolvedapi.Data, tag: String): Bundle {
        // ticketstatusapiresponse=data
        return Bundle().apply {
            putSerializable(SiteDialog.KEY_DATA, data)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.setLayout(
            0.95.toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(false)
        viewBinding = DialogAcknowledgementBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(requireActivity())[RegistrationViewModel::class.java]
        viewBinding.viewModel = viewModel
       // val data = arguments?.getSerializable(SiteDialog.KEY_DATA) as ArrayList<DataItem>
        datanew = arguments?.getSerializable(SiteDialog.KEY_DATA) as ResponseTicktResolvedapi.Data
       // Utils.printMessage(TAG, "Acknowledgement Data :: " + data.toString())
      /*  viewBinding.ticketNo.text =
            context?.resources?.getString(R.string.label_complaint_ticket_number) + "  ${data[0].ticketNo}"
        viewBinding.regDate.text =
            context?.resources?.getString(R.string.label_registered_date) + "  ${convertCmsDate(data[0].regtime.toString())}"
        if (data[0].closeTime.toString().isEmpty()) {
            viewBinding.closeDate.visibility = View.GONE
        } else {
            viewBinding.closeDate.visibility = View.VISIBLE
            viewBinding.closeDate.text =
                context?.resources?.getString(R.string.label_close_date) + "  ${convertCmsDate(data[0].closeTime.toString())}"
        }*/
        viewBinding.dilogaClose.setOnClickListener { dismiss() }

          viewBinding.ticketNo.text = "${datanew.ticket_id}"
        if(datanew.ticket_created_time != null)
        {
        viewBinding.regDate.text ="${convertCmsDate(datanew.ticket_created_time.toString())}"
    }
        /*if (data[0].closeTime.toString().isEmpty()) {
            viewBinding.closeDate.visibility = View.GONE
        } else {
            viewBinding.closeDate.visibility = View.VISIBLE
            viewBinding.closeDate.text =
                context?.resources?.getString(R.string.label_close_date) + "  ${convertCmsDate(data[0].closeTime.toString())}"
        }*/

        viewModel.getTicketRatingApi()



        viewModel.cmsloginapiresponse.observe(viewLifecycleOwner, {
            cmsloginresponse=it.data;
            token=it.data.token
            if (ticketCloseApi)
                ticketCloseAPI()
            else
                ticketReOpenApi()
//            viewModel.getTicketRatingApi()
           // var cmsloginresponse: ResponseCMSLogin
           // cmsloginresponse=it


        })

        viewModel.cmsticketRatingresponse.observe(viewLifecycleOwner,{
            //var cmsticketratingresponse:ResponseticketRatingApi
          //  cmsticketratingresponse=it
            ticketratingapiresponse=it.data;


        })

        viewModel.cmsticketclosingapiresponse.observe(viewLifecycleOwner,{
            if(closingstatus) {
                closingstatus=false
                hideLoading()
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                dismiss()
            }
           // viewModel.cmsticketclosingapiresponse.removeObserver(viewLifecycleOwner,{})
        })


      /*  viewBinding.problemDesc.text =
            context?.resources?.getString(R.string.label_prob_description) + "  ${data[0].problemDrescription}"*/
          viewBinding.problemDesc.text = datanew.ticket_reason_name
        viewBinding.textHead.text = context?.resources?.getString(R.string.label_acknowledgement)
        viewBinding.accept.setOnClickListener {
            closingstatus=true
            for(rows in ticketratingapiresponse?.listData?.rows!!)
            {
                if(rows.value.equals(ratingvalue))
                { ratingduid=rows.uid
                    break
                }

            }

            if (checkValidation(true)) {
                ticketCloseApi = true
                showLoading()
                val userData = LoginRepo.getProfile()

                var cmsLogin = RequestCMSLogin()
                // cmsLogin.appUserName = "APL49365"
                cmsLogin.appUserName =userData!!.EMPID
                cmsLogin.appPassword = LoginRepo.getPassword()
//                viewModel.getCMSLoginApi(cmsLogin)
                ticketCloseAPI()


               /* viewModel.submitRequestOfAcknowledgment(
                    SubmitAcknowledge(
                       // data[0].ticketNo,
                         datanew.ticket_id,
                        viewBinding.remark.text.toString().trim(),
                        "ACCEPT",
                        viewBinding.smileRating.rating.toString(), userData!!.EMPID
                    )
                )*/
            }
        }
        viewBinding.smileRating.setOnRatingSelectedListener { level, reselected ->
            Utils.printMessage(TAG, "Smile Rating :: " + level.toString())
            ratingvalue=level.toString()

        }
        viewBinding.reject.setOnClickListener {
            closingstatus=true
            for(rows in ticketratingapiresponse?.listData?.rows!!)
            {
                if(rows.value.equals(ratingvalue))
                { ratingduid=rows.uid
                    break
                }

            }

            if (checkValidation(false)) {
                ticketCloseApi =false
                showLoading()
                val userData = LoginRepo.getProfile()

                var cmsLogin = RequestCMSLogin()
                // cmsLogin.appUserName = "APL49365"
                cmsLogin.appUserName =userData!!.EMPID
                cmsLogin.appPassword = LoginRepo.getPassword()
//                viewModel.getCMSLoginApi(cmsLogin)
                    ticketReOpenApi()
                /*viewModel.submitRequestOfAcknowledgment(
                    SubmitAcknowledge(
                      //  data[0].ticketNo,
                          datanew.ticket_id,
                        viewBinding.remark.text.toString().trim(),
                        "RE-OPEN",
                        viewBinding.smileRating.rating.toString(), userData!!.EMPID
                    )
                )*/
            }
        }





        viewModel.command.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CmsCommand.ShowToast -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
                is CmsCommand.InVisibleLayout -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        })
        return viewBinding.root
    }

    fun checkValidation(isValidateRate: Boolean): Boolean {
        if (viewBinding.remark.text.toString().isEmpty()) {
            Toast.makeText(
                requireContext(),
                context?.resources?.getString(R.string.label_enter_remark),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (isValidateRate && viewBinding.smileRating.rating == 0) {
            Toast.makeText(
                requireContext(),
                context?.resources?.getString(R.string.label_rate_complaint),
                Toast.LENGTH_SHORT
            )
                .show()
            return false
        }
        return true
    }

    fun ticketCloseAPI(){
//        viewModel.getTicketclosingApi(
//            token,
//            RequestClosedticketApi(
//                RequestClosedticketApi.Feedback(RequestClosedticketApi.Rating(ratingduid)),
//                comment = viewBinding.remark.text.toString().trim(),
//                uid = datanew.ticket_uid,
//                RequestClosedticketApi.Status(uid = "52E2C8F5C204B5BD03DF3A73EB096484",code = "solved"),
//                RequestClosedticketApi.Action(uid = "9370BDBD701E49BA59A9418CA849AB22",code = "close"),
//                RequestClosedticketApi.Level(datanew.ticket_level_uid),
//                ticket_id = datanew.ticket_id,
//                RequestClosedticketApi.User(uid = datanew.ticket_user_uid),
//                action_name = "Closed",
//                RequestClosedticketApi.SessionUser(uid =cmsloginresponse?.uid,name = cmsloginresponse?.name,login_unique = cmsloginresponse?.login_unique),
//                RequestClosedticketApi.Site(uid =datanew.ticket_site_uid)
//            )
//
//        )
        val userData = LoginRepo.getProfile()

        val inventoryAcceptrejectModel = TicketResolveCloseModel(
            "close",
            viewBinding.remark.text.toString().trim(),
            userData!!.EMPID,
            "closed",
            datanew.ticket_id!!,
            Feedback(Rating(ratingduid!!))
        )
        viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
    }

    fun ticketReOpenApi(){
        if(viewBinding.remark.text.toString().trim().isNotEmpty()) {
//            viewModel.getTicketclosingApi(
//                token,
//                RequestClosedticketApi(
//                    RequestClosedticketApi.Feedback(RequestClosedticketApi.Rating(ratingduid)),
//                    comment = viewBinding.remark.text.toString().trim(),
//                    uid = datanew.ticket_uid,
//                    RequestClosedticketApi.Status(uid = "52E2C8F5C204B5BD03DF3A73EB096484",
//                        code = "solved"),
//                    RequestClosedticketApi.Action(uid = "C4EB6C6A46A6E5C449C548281B68AE0B",
//                        code = "reopen"),
//                    RequestClosedticketApi.Level(datanew.ticket_level_uid),
//                    ticket_id = datanew.ticket_id,
//                    RequestClosedticketApi.User(uid = datanew.ticket_user_uid),
//                    action_name = "Reopened",
//                    RequestClosedticketApi.SessionUser(uid = cmsloginresponse?.uid,
//                        name = cmsloginresponse?.name,
//                        login_unique = cmsloginresponse?.login_unique),
//                    RequestClosedticketApi.Site(uid = datanew.ticket_site_uid)
//                )
//            )
            val userData = LoginRepo.getProfile()
            val inventoryAcceptrejectModel = TicketResolveCloseModel(
                "reopen",
                viewBinding.remark.text.toString().trim(),
                userData!!.EMPID,
                "reopened",
                datanew.ticket_id!!,
                Feedback(Rating(ratingduid!!))
            )
            viewModel.actionTicketResolveClose(inventoryAcceptrejectModel)
        }
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = Utils.showLoadingDialog(this.context)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.cancel()
        }
    }
}