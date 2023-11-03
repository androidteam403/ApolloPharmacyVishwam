package com.apollopharmacy.vishwam.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.LayoutMenuItemBinding;
import com.apollopharmacy.vishwam.ui.home.home.HomeFragmentCallback;

import java.util.ArrayList;
import java.util.Locale;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> implements Filterable {
    private Context context;
    HomeFragmentCallback callback;
    ArrayList<MenuModel> menuModel;
    ArrayList<MenuModel> greetingMenuModel = new ArrayList<>();
    ArrayList<MenuModel> cashMenuModel = new ArrayList<>();
    ArrayList<MenuModel> sensingMenuModel = new ArrayList<>();
    ArrayList<MenuModel> attendencemenuModel;
    ArrayList<MenuModel> retroQrMenuModel = new ArrayList<>();
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

    private ArrayList<MenuModel> menuModelList = new ArrayList<>();
    private ArrayList<MenuModel> filteredList = new ArrayList<>();

    public MenuItemAdapter(Context context, HomeFragmentCallback callback, ArrayList<MenuModel> menuModel, ArrayList<MenuModel> attendencemenuModel, ArrayList<MenuModel> cmsMenuModel, ArrayList<MenuModel> discountMenuModel, ArrayList<MenuModel> drugRequestMenuModel, ArrayList<MenuModel> omsQcMenuModel, ArrayList<MenuModel> swachhMenuModel, ArrayList<MenuModel> monitoringMenuModel, ArrayList<MenuModel> champsMenuModel, ArrayList<MenuModel> planogramMenuModel, ArrayList<MenuModel> apnaRetroMenuModel, ArrayList<MenuModel> apnaMenuModel) {
        this.context = context;
        this.callback = callback;
        this.menuModel = menuModel;
        this.menuModelList = menuModel;
        this.attendencemenuModel = attendencemenuModel;
        this.cmsMenuModel = cmsMenuModel;
        discountMenu = discountMenuModel;
        drugMenu = drugRequestMenuModel;
        qcMenu = omsQcMenuModel;
        swachMenu = swachhMenuModel;
        monitoringMenu = monitoringMenuModel;
        champsMenu = champsMenuModel;
        planogramMenu = planogramMenuModel;
        apnStoreMenu = apnaRetroMenuModel;
        apnaMenu = apnaMenuModel;

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
        MenuModel menuModel1 = menuModel.get(position);
        holder.menuItemBinding.name.setText(menuModel1.getMenuName());
        holder.menuItemBinding.icon.setImageResource(menuModel1.getMenuIcon());
        holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
            if (menuModel1.getMenuModelList() != null)
                callback.onClickMenuItem(menuModel1.getPrioritySubmodule(), menuModel1.getMenuModelList());
            else
                callback.onClickMenuItem(menuModel1.getPrioritySubmodule(), new ArrayList<MenuModel>());
        });
        /*if (menuModel.get(position).getMenuName().equalsIgnoreCase("Greetings")) {
            greetingMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.name.setText("Greetings");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), greetingMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Cash Deposit")) {
            cashMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.name.setText("Cash Deposit");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), cashMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Sensing")) {
            sensingMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.name.setText("Sensing");
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), sensingMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Retro QR")) {
            retroQrMenuModel.add(menuModel.get(position));
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());
            holder.menuItemBinding.name.setText("Retro QR");
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), retroQrMenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Attendance")) {
            holder.menuItemBinding.name.setText("Attendance");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), attendencemenuModel);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Complaints")) {
            holder.menuItemBinding.name.setText("Complaints");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                if (cmsMenuModel != null){
                    if (cmsMenuModel.size() > 0){
                        callback.onClickMenuItem("Complaints", cmsMenuModel);
                    }else {

                    }
                }

            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Pending")) {
            holder.menuItemBinding.name.setText("Discount");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), discountMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("New Drug List")) {
            holder.menuItemBinding.name.setText("Raise New" + "\n" + "Drug Request");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), drugMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Out Standing")) {
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
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Upload")) {
            holder.menuItemBinding.name.setText("SWACHH");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), swachMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Monitoring")) {
            holder.menuItemBinding.name.setText("Monitoring");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), monitoringMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Champs Survey")) {
            holder.menuItemBinding.name.setText("Champs");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), champsMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Planogram Evaluation")) {
            holder.menuItemBinding.name.setText("Planogram");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), planogramMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Approval")) {
            holder.menuItemBinding.name.setText("APNA Store");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), apnStoreMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("Creation")) {
            holder.menuItemBinding.name.setText("APNA Store");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());
            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), apnStoreMenu);
            });
        } else if (menuModel.get(position).getMenuName().equalsIgnoreCase("APNA")) {
            holder.menuItemBinding.name.setText("APNA");
            holder.menuItemBinding.icon.setImageResource(menuModel.get(position).getMenuIcon());

            holder.menuItemBinding.menuLayout.setOnClickListener(v -> {
                callback.onClickMenuItem(menuModel.get(position).getMenuName(), apnaMenu);
            });
        }*/


    }

    @Override
    public int getItemCount() {
        return menuModel.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    menuModel = menuModelList;
                } else {
                    filteredList.clear();
                    for (MenuModel row : menuModelList) {
                        if (!filteredList.contains(row)) {
                            if (row.getMenuName().toLowerCase(Locale.ENGLISH).contains(charString.toLowerCase(Locale.ENGLISH))) {
                                filteredList.add(row);
                            } else {
                                if (row.getMenuModelList() != null) {
//                                boolean isItemContains = false;
                                    for (MenuModel innerRow : row.getMenuModelList()) {
                                        if (innerRow.getMenuName().toLowerCase(Locale.ENGLISH).contains(charString.toLowerCase(Locale.ENGLISH))) {
                                            filteredList.add(row);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                       /* if (!filteredList.contains(row) && (row.getRefno().toLowerCase().contains(charString.toLowerCase()) || row.getOverallOrderStatus().toLowerCase().contains(charString))) {
                            filteredList.add(row);
                        }*/

                    }
                    menuModel = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = menuModel;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (menuModel != null && !menuModel.isEmpty()) {
                    menuModel = (ArrayList<MenuModel>) filterResults.values;
                    try {
                        callback.noModuleFound(menuModel.size());
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("MenuItemAdapter", e.getMessage());
                    }
                } else {
                    callback.noModuleFound(0);
                    notifyDataSetChanged();
                }
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutMenuItemBinding menuItemBinding;

        public ViewHolder(@NonNull LayoutMenuItemBinding menuItemBinding) {
            super(menuItemBinding.getRoot());
            this.menuItemBinding = menuItemBinding;
        }
    }
}
