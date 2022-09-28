package com.apollopharmacy.vishwam.ui.home.qcfail.approved

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcList
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.PendingFragment
import com.apollopharmacy.vishwam.ui.login.Command


class ApprovedFragment : BaseFragment<QcApprovedViewModel, FragmentApprovedQcBinding>(),
    QcImagesListner,
    QcFilterFragment.QcFilterClicked {

    lateinit var adapter: ApprovedRecycleView
    private var isBulkChecked: Boolean = false

    val names = ArrayList<QcList>();


    override val layoutRes: Int
        get() = R.layout.fragment_approved_qc

    override fun retrieveViewModel(): QcApprovedViewModel {
        return ViewModelProvider(this).get(QcApprovedViewModel::class.java)
    }

    override fun setup() {
        var name = QcList()
        name.setIschecked(false)
        names.add(name)
        var name1 = QcList()
        name1.setIschecked(false)
        names.add(name1)
        adapter = ApprovedRecycleView(names, this)
        viewBinding.recyclerViewPending.adapter = adapter


        viewBinding.filter.setOnClickListener {
            viewModel.filterClicked()

        }
        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is Command.ShowButtonSheet -> {
                    var dialog = command.fragment.newInstance()
                    dialog.arguments = command.arguments
                    dialog.setTargetFragment(this, 0)
                    activity?.supportFragmentManager?.let { dialog.show(it, "") }
                }
                is Command.ShowToast -> {
                    hideLoading()
                    if (command.message.equals("no data found.please check empid")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.recyclerViewPending.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    class ApprovedRecycleView(
        var orderData: ArrayList<QcList>,
        val imageClicklistner: QcImagesListner,

        ) :
        SimpleRecyclerView<QcApprovedlayoutBinding, QcList>(
            orderData,
            R.layout.qc_approvedlayout
        ) {


        override fun bindItems(
            binding: QcApprovedlayoutBinding,
            items: QcList,
            position: Int,
        ) {
            lateinit var orderadapter: ApproveOrderDetailsRecycleView
            val names = ArrayList<String>();
            names.add("a")

            orderadapter = ApproveOrderDetailsRecycleView(names)
            binding.recyclerView.adapter = orderadapter
            var ischecked: Boolean = false


            if (!items.isIschecked) {
                binding.checkBox.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
            } else {
                binding.checkBox.setImageResource(R.drawable.ic_circle_tick)
            }
            binding.checkBoxLayout.setOnClickListener {

                imageClicklistner.isChecked(orderData, position)

//
//                if (!ischecked) {
//                    ischecked = true
//                    binding.checkBox.setImageResource(R.drawable.icon_item_checked)
//                } else {
//                    binding.checkBox.setImageResource(R.drawable.icon_item_unchecked)
//                    ischecked = false
//                }


            }


            binding.arrow.setOnClickListener {
                binding.arrowClose.visibility = View.VISIBLE
                binding.arrow.visibility = View.GONE
                binding.extraData.visibility = View.VISIBLE


            }

            binding.arrowClose.setOnClickListener {
                binding.arrowClose.visibility = View.GONE
                binding.arrow.visibility = View.VISIBLE
                binding.extraData.visibility = View.GONE


            }

        }
    }


    class ApproveOrderDetailsRecycleView(
        var orderData: ArrayList<String>,

        ) :
        SimpleRecyclerView<QcApprovedorderdetailsBinding, String>(
            orderData,
            R.layout.qc_approvedorderdetails
        ) {
        override fun bindItems(
            binding: QcApprovedorderdetailsBinding,
            items: String,
            position: Int,
        ) {


        }
    }

    override fun clickedApply(
        selectedData: String,
        data: ArrayList<String>,
        tag: Int,
        toDate: String,
    ) {

    }

    override fun isChecked(array: ArrayList<QcList>, position: Int) {
        if (array[position].isIschecked) {
            names[position].setIschecked(false)
        } else {
            names[position].setIschecked(true)
        }
        adapter.notifyDataSetChanged()
    }


}

interface QcImagesListner {


    fun isChecked(array: ArrayList<QcList>, position: Int)
}


