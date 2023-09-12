package com.apollopharmacy.vishwam.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.LayoutMenuItemBinding;
import com.apollopharmacy.vishwam.ui.home.home.HomeFragmentCallback;

import java.util.ArrayList;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private Context context;
    HomeFragmentCallback callback;
    ArrayList<MenuModel> menuModel;

    public MenuItemAdapter(Context context, HomeFragmentCallback callback, ArrayList<MenuModel> menuModel) {
        this.context = context;
        this.callback = callback;
        this.menuModel = menuModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutMenuItemBinding menuItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.layout_menu_item,
                parent,
                false
        );
        return new ViewHolder(menuItemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (menuModel.get(position).getMenuName().equalsIgnoreCase("QcDashboard")) {
            holder.menuItemBinding.name.setText("Dashboard");
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("QcApproved")) {
            holder.menuItemBinding.name.setText("Approved");
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("QcRejected")) {
            holder.menuItemBinding.name.setText("Rejected");
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Approval List")) {
            holder.menuItemBinding.name.setText("Approval\nList");
        }
        else {
            holder.menuItemBinding.name.setText(menuModel.get(position).getMenuName());
        }
        holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

        holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
            callback.onClickMenuItem(menuModel.get(position).getMenuName(), menuModel);
        });
    }

    @Override
    public int getItemCount() {
        return menuModel.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutMenuItemBinding menuItemBinding;

        public ViewHolder(@NonNull LayoutMenuItemBinding menuItemBinding) {
            super(menuItemBinding.getRoot());
            this.menuItemBinding = menuItemBinding;
        }
    }
}
