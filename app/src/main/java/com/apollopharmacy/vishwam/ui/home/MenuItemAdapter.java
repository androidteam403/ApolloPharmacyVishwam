package com.apollopharmacy.vishwam.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
    ArrayList<MenuModel> greetingMenuModel=new ArrayList<>();
    ArrayList<MenuModel> cashMenuModel=new ArrayList<>();
    ArrayList<MenuModel> sensingMenuModel=new ArrayList<>();
    ArrayList<MenuModel> attendencemenuModel;
    ArrayList<MenuModel> retroQrMenuModel=new ArrayList<>();
    ArrayList<MenuModel> cmsMenuModel;
    ArrayList<MenuModel> discountMenu;
    ArrayList<MenuModel> drugMenu;
    ArrayList<MenuModel> qcMenu;
    ArrayList<MenuModel> swachMenu;

    ArrayList<MenuModel> monitoringMenu;
    ArrayList<MenuModel> champsMenu;
    ArrayList<MenuModel> planogramMenu;
    ArrayList<MenuModel> apnStoreMenu;
    ArrayList<MenuModel> apnaMenu;
    public MenuItemAdapter(Context context, HomeFragmentCallback callback, ArrayList<MenuModel> menuModel,ArrayList<MenuModel> attendencemenuModel,ArrayList<MenuModel> cmsMenuModel,ArrayList<MenuModel> discountMenuModel,ArrayList<MenuModel> drugRequestMenuModel,ArrayList<MenuModel> omsQcMenuModel,ArrayList<MenuModel> swachhMenuModel,ArrayList<MenuModel> monitoringMenuModel,ArrayList<MenuModel> champsMenuModel, ArrayList<MenuModel> planogramMenuModel,ArrayList<MenuModel> apnaRetroMenuModel,ArrayList<MenuModel> apnaMenuModel) {
        this.context = context;
        this.callback = callback;
        this.menuModel = menuModel;
        this.attendencemenuModel=attendencemenuModel;
        this.cmsMenuModel=cmsMenuModel;
        discountMenu=discountMenuModel;
        drugMenu=drugRequestMenuModel;
        qcMenu=omsQcMenuModel;
        swachMenu=swachhMenuModel;
        monitoringMenu=monitoringMenuModel;
        champsMenu=champsMenuModel;
        planogramMenu=planogramMenuModel;
        apnStoreMenu=apnaRetroMenuModel;
        apnaMenu=apnaMenuModel;

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
        if (menuModel.get(position).getMenuName().equalsIgnoreCase("Greetings to Chairman")) {
           greetingMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.name.setText("Greetings" + "\n" + "to Chairman");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), greetingMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Cash Deposit")) {
            cashMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.name.setText("Cash" + "\n" + "Deposit");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), cashMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Apollo Sensing")) {
            sensingMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.name.setText("Apollo" + "\n" + "Sensing");
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), sensingMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Retro QR")) {
            retroQrMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.name.setText("Retro" + "\n" + "QR");
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), retroQrMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Attendance")) {
            holder.menuItemBinding.name.setText("Attendance" + "\n" + "Management");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), attendencemenuModel);
            });
        }
        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Complaint List")) {
            holder.menuItemBinding.name.setText("CMS");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), cmsMenuModel);
            });
        }

        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Pending")) {
            holder.menuItemBinding.name.setText("Discount");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), discountMenu);
            });
        }
        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("New Drug List")) {
            holder.menuItemBinding.name.setText("Raise New" + "\n" + "Drug Request");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), drugMenu);
            });
        }
       else if (menuModel.get(position).getMenuName().equalsIgnoreCase("OutStanding")) {
            holder.menuItemBinding.name.setText("OMC QC");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), qcMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("List")) {
            holder.menuItemBinding.name.setText("SWACHH");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), swachMenu);
            });
        }
        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Upload")) {
            holder.menuItemBinding.name.setText("SWACHH");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), swachMenu);
            });
        }

       else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Dashboard")) {
            holder.menuItemBinding.name.setText("Monitoring");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), monitoringMenu);
            });
        }
        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Champs Survey")) {
            holder.menuItemBinding.name.setText("Champs");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), champsMenu);
            });
        }

        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Planogram Evaluation")) {
            holder.menuItemBinding.name.setText("Planogram");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), planogramMenu);
            });
        }
        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Approval")) {
            holder.menuItemBinding.name.setText("APNA Store");
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), apnStoreMenu);
            });
        }
        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Creation")) {
            holder.menuItemBinding.name.setText("APNA Store");
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), apnStoreMenu);
            });
        }
        else if (menuModel.get(position).getMenuName().equalsIgnoreCase("APNA")) {
            holder.menuItemBinding.name.setText("APNA");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), apnaMenu);
            });
        }


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
