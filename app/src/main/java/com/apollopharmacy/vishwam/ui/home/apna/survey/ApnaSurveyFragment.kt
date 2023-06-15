package com.apollopharmacy.vishwam.ui.home.apna.survey

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentApnaSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyActivity
import com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.ApnaPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.adapter.ApnaSurveyAdapter


class ApnaSurveyFragment() : BaseFragment<ApnaSurveylViewModel, FragmentApnaSurveyBinding>(),
    ApnaSurveyCallback {
    var surveyListResponse = ArrayList<SurveyListResponse.Row>()
    lateinit var displayedItems: ArrayList<SurveyListResponse.Row>
    lateinit var layoutManager: LinearLayoutManager
    var isScrolling: Boolean = false
    var startIndex: Int = 0
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
        showLoading()
        viewModel.getApnaSurveyList(this)
        MainActivity.mInstance.plusIconApna.setOnClickListener {
            requireActivity().startActivityForResult(ApnaNewSurveyActivity().getStartIntent(
                requireContext()), APNA_NEW_SURVEY_ACTIVITY_VALUE!!)
        }
//        viewBinding.next.setOnClickListener {
//            if (startIndex + itemsPerPage < surveyListResponse.size) {
//                startIndex = startIndex + itemsPerPage
//                displayedItems = getData()
//                adapter!!.setData(getData())
//            } else {
//                Toast.makeText(requireContext(), "No Next Record Found", Toast.LENGTH_SHORT).show()
//            }
//        }
//        viewBinding.previous.setOnClickListener {
//            if (startIndex >= itemsPerPage) {
//                startIndex = startIndex - itemsPerPage
//                displayedItems = getData()
//                adapter!!.setData(getData())
//            } else {
//                Toast.makeText(requireContext(), "No Previous Record Found", Toast.LENGTH_SHORT).show()
//            }
//        }
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

//        viewBinding.recyclerViewapproval.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                isScrolling = true
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (isScrolling && (layoutManager.findLastCompletelyVisibleItemPosition() + 1 == getData().size)) {
//                    isScrolling = false
//                    loadMoreData()
//                }
//            }
//        })
    }

//    private fun loadMoreData() {
//        handler.post(Runnable {
//            if (startIndex + itemsPerPage < surveyListResponse.size) {
//                startIndex = startIndex + itemsPerPage
//                displayedItems = getData()
//                adapter!!.getData().addAll(displayedItems)
//                adapter!!.notifyDataSetChanged()
//                isScrolling = false
//            }
//        })
//    }

//    override fun onStart() {
//        super.onStart()
//        showLoading()
//        viewModel.getApnaSurveyList(this)
//    }

    override fun onClick(position: Int, surveyListResponse: SurveyListResponse.Row) {
        val i = Intent(activity, ApnaPreviewActivity::class.java)
        i.putExtra("regionList", surveyListResponse)
//        startActivityForResult(i, 210)
        startActivity(i)
    }

    var surveyList = ArrayList<SurveyListResponse.Row>()
    var surveyListLoad = ArrayList<SurveyListResponse.Row?>()
    private var isLoading = false
    private var isLastRecord = false
    private var page = 1
    private val pageSize = 3
    override fun onSuccessgetSurveyDetails(surveyListResponse: SurveyListResponse) {
        hideLoading()
        if (surveyListResponse != null && surveyListResponse.data!! != null && surveyListResponse.data!!.listData != null && surveyListResponse.data!!.listData!!.rows != null && surveyListResponse.data!!.listData!!.rows!!.size > 0) {
            page = 1
            surveyList =
                surveyListResponse.data!!.listData!!.rows as ArrayList<SurveyListResponse.Row>
            for (i in 0 until surveyList.size) {
                surveyListLoad.add(surveyList.get(i))
                if (i == (page * pageSize) - 1) {
                    break
                }
            }
            page++
            if (surveyListLoad != null && surveyListLoad.size > 0) {
                val mLayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(requireContext())
                viewBinding.recyclerViewapproval.setLayoutManager(mLayoutManager)
                viewBinding.recyclerViewapproval.setItemAnimator(DefaultItemAnimator())
                viewBinding.noListFound.setVisibility(View.GONE);
                viewBinding.recyclerViewapproval.setVisibility(View.VISIBLE)
                initAdapter()
                initScrollListener()
            } else {
                viewBinding.recyclerViewapproval.setVisibility(View.GONE)
                viewBinding.noListFound.setVisibility(View.VISIBLE);
            }
        } else {
            viewBinding.recyclerViewapproval.setVisibility(View.GONE)
            viewBinding.noListFound.setVisibility(View.VISIBLE);
        }


//        surveyListResponse = value.data!!.listData!!.rows as ArrayList<SurveyListResponse.Row>
//        layoutManager = LinearLayoutManager(requireContext())
//        displayedItems = getData()
//        adapter = ApnaSurveyAdapter(requireContext(),
//            displayedItems, this)
////        adapter = ApnaSurveyAdapter(requireContext(),
////            value.data!!.listData!!.rows as ArrayList<SurveyListResponse.Row>, this)
//        viewBinding.recyclerViewapproval.adapter = adapter
//        viewBinding.recyclerViewapproval.layoutManager = layoutManager
    }

    override fun onFailureGetSurveyDetails(message: String) {
        hideLoading()
        viewBinding.recyclerViewapproval.visibility = View.GONE
        viewBinding.noListFound.visibility = View.VISIBLE
    }

    override fun onFailuregetSurveyDetails(surveyListResponse: Any) {
        TODO("Not yet implemented")
    }

    private fun initAdapter() {
        adapter = ApnaSurveyAdapter(
            requireContext(),
            surveyListLoad,
            this,
        )
        viewBinding.recyclerViewapproval.adapter = adapter
    }

    private fun initScrollListener() {
        viewBinding.recyclerViewapproval.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == surveyListLoad.size - 1) {
                        //bottom of list!
                        if (!isLastRecord) {
                            isLoading = true
                            loadMore()
                        }
                    }
                }
            }
        })
    }

    private fun loadMore() {
        if (surveyListLoad.size < surveyList.size) {
            surveyListLoad.add(null)
            adapter!!.notifyItemInserted(surveyListLoad.size - 1)
            val logindetailListLoadTemp = ArrayList<SurveyListResponse.Row>()
            for (i in surveyListLoad.size - 1 until surveyList.size) {
                logindetailListLoadTemp.add(surveyList.get(i))
                if (i == (page * pageSize)
                    - 1
                ) {
                    break
                }
            }
            page++
            surveyListLoad.removeAt(surveyListLoad.size - 1)
            val scrollPosition: Int = surveyListLoad.size
            adapter!!.notifyItemRemoved(scrollPosition)
            val currentSize = scrollPosition
            val nextLimit = 10
            surveyListLoad.addAll(logindetailListLoadTemp)
            adapter!!.notifyDataSetChanged()
            isLoading = false
            if (surveyListLoad.size == surveyList.size) {
                isLastRecord = true
            }
        } else if (surveyListLoad.size == surveyList.size) {
            isLastRecord = true
        }
    }

//    private fun getData(): ArrayList<SurveyListResponse.Row> {
//        val endIndex = minOf(startIndex + itemsPerPage, surveyListResponse.size)
//        return ArrayList(surveyListResponse.subList(startIndex, endIndex))
//    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == APNA_NEW_SURVEY_ACTIVITY_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                showLoading()
                viewModel.getApnaSurveyList(this)
            }
        }
    }
}