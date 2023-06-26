package com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.UserListForSubworkflowResponse


class UsersListforSubworkflowSpinnerAdapter(
    var context: Context,
    var rowsList: ArrayList<UserListForSubworkflowResponse.Rows>,
) : BaseAdapter() {


    override fun getCount(): Int {
        return rowsList.size
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflter = LayoutInflater.from(context)
        val view: View = inflter.inflate(R.layout.sprinner_user_list_subworkflow, null)
        val names = view.findViewById<TextView>(R.id.user)
        var row = rowsList.get(position)
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
        names.setText(userName)
        return view
    }
}