package com.apollopharmacy.vishwam.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.SubmenuItemRowBinding;

import java.util.ArrayList;

public class SubmenuAdapter extends RecyclerView.Adapter<SubmenuAdapter.ViewHolder> {
    Context context;
    ArrayList<MenuModel> submenus;
    MainActivityCallback callback;

    public SubmenuAdapter(Context context, ArrayList<MenuModel> submenus, MainActivityCallback callback) {
        this.context = context;
        this.submenus = submenus;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubmenuItemRowBinding submenuItemRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.submenu_item_row, parent, false);
        return new ViewHolder(submenuItemRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.submenuItemRowBinding.menuName.setText(submenus.get(position).getMenuName());
        holder.submenuItemRowBinding.menuIcon.setImageResource(submenus.get(position).getMenuIcon());
        holder.submenuItemRowBinding.menu.setOnClickListener(v -> {
            callback.onClickSubmenuItem(submenus.get(position).getMenuName(), submenus, position);
        });
    }

    @Override
    public int getItemCount() {
        return submenus.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SubmenuItemRowBinding submenuItemRowBinding;

        public ViewHolder(@NonNull SubmenuItemRowBinding submenuItemRowBinding) {
            super(submenuItemRowBinding.getRoot());
            this.submenuItemRowBinding = submenuItemRowBinding;
        }
    }
}
