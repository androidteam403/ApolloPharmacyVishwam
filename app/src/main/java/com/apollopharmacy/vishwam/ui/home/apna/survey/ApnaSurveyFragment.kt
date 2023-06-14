package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.cms.RequestComplainList
import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyActivity
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ComplainListFragment
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.adapter.PendingApprovedListAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),
    ApnaSurveyCallback {
    var surveytResponseList = ArrayList<SurveyListResponse.Row>()
    lateinit var displayedItems: ArrayList<SurveyListResponse.Row>
    lateinit var layoutManager: LinearLayoutManager
    var isScrolling: Boolean = false
    var startIndex: Int = 0
    var startPageNo: Int = 1
    private var isFirstTime: Boolean = true
    var isApnaListThereFirstTime: Boolean = true
    lateinit var responseData: SurveyListResponse
    private var isLoadingApproved: Boolean = false

    var itemsPerPage: Int = 10
    var handler: Handler = Handler()

    var adapter: ApnaSurveyAdapter? = null
    val APNA_NEW_SURVEY_ACTIVITY_VALUE: Int? = 1000
    override val layoutRes: Int
        get() = R.layout.fragment_apna_survey

    override fun retrieveViewModel(): ApnaSurveylViewModel {
        return ViewModelProvider(this).get(ApnaSurveylViewModel::class.java)
    }

    override fun setup() {
        Log.i("TAG", "setup: ")
//        showLoading()
        callAPI(1)
        MainActivity.mInstance.plusIconApna.setOnClickListener {
            requireActivity().startActivityForResult(
                ApnaNewSurveyActivity().getStartIntent(
                    requireContext()
                ), APNA_NEW_SURVEY_ACTIVITY_VALUE!!
            )
        }
        viewBinding.pullToRefreshApproved.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClick()
        })
        viewBinding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString()
                val filteredList = ArrayList<SurveyListResponse.Row?>()
                for (i in surveyList.indices) {
                    if (inputText.isEmpty()) {
                        filteredList.clear()
                        filteredList.addAll(surveyList)
                    } else {
                        if (surveyList.get(i).id.toString().contains(inputText, true)) {
                            filteredList.add(surveyList.get(i))
                        }
                    }
                }
                if (filteredList.size < 1) {
                    viewBinding.recyclerViewapproval.visibility = View.GONE
                    viewBinding.noListFound.visibility = View.VISIBLE
                } else {
                    viewBinding.recyclerViewapproval.visibility = View.VISIBLE
                    viewBinding.noListFound.visibility = View.GONE
                }
                adapter!!.filter(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        addScrollerListener()

    }


    override fun onClick(position: Int, surveyListResponse: SurveyListResponse.Row) {
        val i = Intent(activity, ApnaPreviewActivity::class.java)
        i.putExtra("regionList", surveyListResponse)
//        startActivityForResult(i, 210)
        startActivity(i)
    }

    public var surveyList = ArrayList<SurveyListResponse.Row>()
    var surveyListLoad = ArrayList<SurveyListResponse.Row?>()
    private var isLoading = false
    private var isLastRecord = false
    private var page = 1
    private val pageSize = 3
    override fun onSuccessgetSurveyDetails(surveyListResponse: SurveyListResponse) {
        Utlis.hideLoading()
        if (viewBinding.pullToRefreshApproved.isRefreshing) {
            viewBinding.pullToRefreshApproved.isRefreshing = false
        }
        if (surveyListResponse.data!!.listData!!.rows!!.size == 0) {
            isLoadMoreAvailable = false
            if (isLoading) {
                adapter!!.getData().removeAt(adapter!!.getData().size - 1)
                var listSize = adapter!!.getData().size
                adapter!!.notifyItemRemoved(listSize)
//                    adapter.getData().addAll(it.data.listData.rows)
                adapter!!.notifyDataSetChanged()
                isLoading = false
            } else {
                viewBinding.recyclerViewapproval.visibility = View.GONE
                viewBinding.noListFound.visibility = View.VISIBLE
            }
        } else {
            if (surveyListResponse.data!!.listData!!.rows!!.size <5) {
                isLoadMoreAvailable = false
            }
            responseData = surveyListResponse

            if (isApnaListThereFirstTime) {
                isApnaListThereFirstTime = false
                Preferences.setResponseSurveylist(Gson().toJson(responseData))
            }
            viewBinding.noListFound.visibility = View.GONE
            viewBinding.recyclerViewapproval.visibility = View.VISIBLE
            if (isLoading) {
                adapter!!.getData().removeAt(adapter!!.getData().size - 1)
                var listSize = adapter!!.getData().size
                adapter!!.notifyItemRemoved(listSize)
                adapter!!.getData().addAll(surveyListResponse.data!!.listData!!.rows!!)
                adapter!!.notifyDataSetChanged()
                isLoading = false
            } else {
                adapter = ApnaSurveyAdapter(
                    requireContext(),
                    surveyListResponse.data!!.listData!!.rows!! as java.util.ArrayList<SurveyListResponse.Row?>,this
                )
                layoutManager = LinearLayoutManager(context)
                viewBinding.recyclerViewapproval.layoutManager = layoutManager
                viewBinding.recyclerViewapproval.adapter = adapter
            }
        }
        addScrollerListener()
    }

    override fun onFailureGetSurveyDetails(message: String) {
        hideLoading()
        viewBinding.recyclerViewapproval.visibility = View.GONE
        viewBinding.noListFound.visibility = View.VISIBLE
    }

    override fun onFailuregetSurveyDetails(surveyListResponse: Any) {
    }

    private fun initAdapter() {
        adapter = ApnaSurveyAdapter(
            requireContext(),
            surveyListLoad,
            this,
        )
        viewBinding.recyclerViewapproval.adapter = adapter

    }
    fun submitClick() {
        if (!viewBinding.pullToRefreshApproved.isRefreshing)
            Utlis.showLoading(requireContext())

        callAPI( 1)

    }
    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        viewBinding.recyclerViewapproval.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isFirstTime) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter!!.getData().size - 1) {
                        loadMore()
                    }
                }
            }
        })
    }

    var isLoadMoreAvailable = true
    private fun loadMore() {
        //notify adapter using Handler.post() or RecyclerView.post()
        handler.post(Runnable {
            if (isLoadMoreAvailable) {
                isLoading = true
                val newdata = SurveyListResponse.Row()
                adapter!!.getData().add(newdata)
                adapter!!.notifyItemInserted(adapter!!.getData().size - 1)
                callAPI(responseData.data!!.listData!!.page!! + 1)
            }
        })
    }

    fun callAPI(page: Int) {
        if (page == 1) {
            isApnaListThereFirstTime = true
        } else {
            isApnaListThereFirstTime = false
        }
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            isFirstTime = false

            if (!isLoading) Utlis.showLoading(requireContext())

            viewModel.getApnaSurveyList(this,page.toString(),"2")

        } else {
            Toast.makeText(requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT).show()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == APNA_NEW_SURVEY_ACTIVITY_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                showLoading()
                viewModel.getApnaSurveyList(this, "1", "5")
            }
        }
    }
}