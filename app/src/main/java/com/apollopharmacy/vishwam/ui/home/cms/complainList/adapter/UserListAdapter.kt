package com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ComplaintListFragmentCallback
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.UserListForSubworkflowResponse

class UserListAdapter(
    var mCallback: ComplaintListFragmentCallback,
    var mContext: Context,
    var usersList: ArrayList<UserListForSubworkflowResponse.Rows>, var userSelect: EditText,
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    class ViewHolder(val viewItemRowBinding: ViewItemRowBinding) :
        RecyclerView.ViewHolder(viewItemRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItemRowBinding = DataBindingUtil.inflate<ViewItemRowBinding>(
            LayoutInflater.from(mContext),
            R.layout.view_item_row,
            parent,
            false
        )
        return ViewHolder(viewItemRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var row = usersList[position];
        var userName = ""
        if (row.firstName != null) {
            userName = "${row.firstName}"
        }
        if (row.middleName != null) {
            userName = "$userName ${row.middleName}"
        }
        if (row.lastName != null) {
            userName = "$userName ${row.lastName}"
        }
        if (row.loginUnique != null) {
            userName = "$userName (${row.loginUnique})"
        }
        holder.viewItemRowBinding.itemName.text = userName
        holder.viewItemRowBinding.itemName.setOnClickListener {
            mCallback.onSelectUserListItem(row, userSelect)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    fun filter(filteredList: ArrayList<UserListForSubworkflowResponse.Rows>) {
        this.usersList = filteredList
        notifyDataSetChanged()
    }
}