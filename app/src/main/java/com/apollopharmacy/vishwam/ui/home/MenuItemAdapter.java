package com.apollopharmacy.vishwam.ui.home;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.menuItemBinding.name.setText(menuModel.get(position).getMenuName());
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
