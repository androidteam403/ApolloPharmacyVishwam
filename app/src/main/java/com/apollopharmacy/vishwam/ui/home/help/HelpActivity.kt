package com.apollopharmacy.vishwam.ui.home.help

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityHelpBinding
import com.apollopharmacy.vishwam.ui.home.help.adapter.ModulesAdapter

class HelpActivity : AppCompatActivity(), HelpActivityCallback {
    private lateinit var activityHelpBinding: ActivityHelpBinding
    private lateinit var helpActivityViewModel: HelpActivityViewModel
    private lateinit var modulesAdapter: ModulesAdapter
    private var modulesArrayList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHelpBinding =
            DataBindingUtil.setContentView(this@HelpActivity, R.layout.activity_help)
        helpActivityViewModel = ViewModelProvider(this)[HelpActivityViewModel::class.java]
        activityHelpBinding.callback = this@HelpActivity
        setup()
    }

    private fun setup() {
        modulesArrayList.add("Apna Retro")
        modulesArrayList.add("Apna Survey")
        modulesArrayList.add("Champs")
        modulesArrayList.add("Complaints")
        modulesArrayList.add("Discount")
        modulesArrayList.add("New Drug")
        modulesArrayList.add("OMS QC")
        modulesArrayList.add("Sensing")
        modulesArrayList.add("Swach")



        modulesAdapter =
            ModulesAdapter(this, modulesArrayList, this)
        activityHelpBinding.modulesRecyclerView.setLayoutManager(
            LinearLayoutManager(this)
        )
        activityHelpBinding.modulesRecyclerView.setAdapter(
            modulesAdapter
        )

        searchByModule()


    }

    private fun searchByModule() {
        activityHelpBinding.searchViewH.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changing.
                val newText = s.toString()
                // Do something with the new text.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has changed.
                val text = s.toString()
                if (!text.isNullOrEmpty()) {
                    activityHelpBinding.closeIconH.visibility = View.VISIBLE
                    activityHelpBinding.searchIconH.visibility = View.GONE
                } else {
                    activityHelpBinding.closeIconH.visibility = View.GONE
                    activityHelpBinding.searchIconH.visibility = View.VISIBLE
                }
                activityHelpBinding.closeIconH.setOnClickListener {
                    activityHelpBinding.searchViewH.setText("")
                }
//                if (!text.isNullOrEmpty() && text.length > 0) {
                if (modulesAdapter != null) {
                    modulesAdapter!!.getFilter()!!.filter(text)
                }
//                }
            }
        })
    }

    override fun onclickBackArrow() {
        super.onBackPressed()
    }

    override fun noModuleFound(size: Int) {
        if (size > 0) {
            activityHelpBinding.noModuleFoundLayout.visibility = View.GONE
            activityHelpBinding.modulesRecyclerView.visibility = View.VISIBLE
        } else {
            activityHelpBinding.noModuleFoundLayout.visibility = View.VISIBLE
            activityHelpBinding.modulesRecyclerView.visibility = View.GONE
        }
    }
}