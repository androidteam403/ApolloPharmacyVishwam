package com.apollopharmacy.vishwam.ui.home.help.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterHelpModuleBinding
import com.apollopharmacy.vishwam.ui.home.help.HelpActivityCallback
import com.apollopharmacy.vishwam.ui.home.help.model.HelpResponseModel

class ModulesAdapter(
    private var context: Context,
    private var modulesArrayList: List<HelpResponseModel.Data.ListData.Row>,
    private var helpActivityCallback: HelpActivityCallback,
) : RecyclerView.Adapter<ModulesAdapter.ViewHolder>() {
    private val filteredList = java.util.ArrayList<HelpResponseModel.Data.ListData.Row>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterHelpModuleBinding: AdapterHelpModuleBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_help_module,
                parent,
                false
            )
        return ViewHolder(adapterHelpModuleBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var modulesList = modulesArrayList.get(position)

        holder.adapterHelpModuleBinding.moduleName.text = modulesArrayList.get(position).title
//        val htmlString: String =
//            modulesArrayList.get(position).description!!
//        val spannedDescription: Spanned = Html.fromHtml(
//            htmlString,
//            Html.FROM_HTML_MODE_COMPACT
//        ) // Use Html.FROM_HTML_MODE_LEGACY for older versions
        holder.adapterHelpModuleBinding.description.text=modulesArrayList.get(position).description!!

//        holder.adapterHelpModuleBinding.moduleName.setPaintFlags(holder.adapterHelpModuleBinding.moduleName.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        var layoutIsShowing=false
        holder.adapterHelpModuleBinding.relativeLayout.setOnClickListener {
            if(layoutIsShowing){
                holder.adapterHelpModuleBinding.descriptionHelpLayout.visibility= View.GONE
                layoutIsShowing=false
                holder.adapterHelpModuleBinding.plusIcon.visibility=View.VISIBLE
                holder.adapterHelpModuleBinding.minusIcon.visibility=View.GONE
            }else{
                layoutIsShowing=true
                holder.adapterHelpModuleBinding.plusIcon.visibility=View.GONE
                holder.adapterHelpModuleBinding.minusIcon.visibility=View.VISIBLE
                holder.adapterHelpModuleBinding.descriptionHelpLayout.visibility= View.VISIBLE
            }

        }
    }

    override fun getItemCount(): Int {
        return modulesArrayList!!.size
    }
    val menuModelList: List<HelpResponseModel.Data.ListData.Row> = modulesArrayList
    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    modulesArrayList = menuModelList!!
                } else {
                    filteredList.clear()
                    for (row in menuModelList!!) {
                        if (!filteredList.contains(row)) {
                            if (row.title.toString().lowercase().contains(charString.lowercase())) {
                                filteredList.add(row)
                            } else {
                                if (row!= null) {
//                                boolean isItemContains = false;
                                    for (innerRow in row.title.toString()) {
                                        if (innerRow.lowercase()
                                                .contains(charString.lowercase())
                                        ) {
                                            filteredList.add(row)
                                            break
                                        }
                                    }
                                }
                            }
                        }
                        /* if (!filteredList.contains(row) && (row.getRefno().toLowerCase().contains(charString.toLowerCase()) || row.getOverallOrderStatus().toLowerCase().contains(charString))) {
                            filteredList.add(row);
                        }*/
                    }
                    modulesArrayList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = modulesArrayList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (modulesArrayList != null && !modulesArrayList.isEmpty()) {
                    modulesArrayList = filterResults.values as List<HelpResponseModel.Data.ListData.Row>
                    try {
                        helpActivityCallback.noModuleFound(modulesArrayList.size)
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.e("MenuItemAdapter", e.message!!)
                    }
                } else {
                    helpActivityCallback.noModuleFound(0)
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ViewHolder(val adapterHelpModuleBinding: AdapterHelpModuleBinding) :
        RecyclerView.ViewHolder(adapterHelpModuleBinding.root)
}