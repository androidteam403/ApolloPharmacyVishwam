package com.apollopharmacy.vishwam.ui.home;

import java.util.ArrayList;
import java.util.List;

public class MenuModel {
    private String menuName;

    private String prioritySubmodule;
    private int menuIcon;
    private boolean isSelected;

    private ArrayList<MenuModel> menuModelList;

    public MenuModel(String menuName, int menuIcon, boolean isSelected, ArrayList<MenuModel> menuModelList, String prioritySubmodule) {
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.isSelected = isSelected;
        this.menuModelList = menuModelList;
        this.prioritySubmodule = prioritySubmodule;
    }

    public MenuModel() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPrioritySubmodule() {
        return prioritySubmodule;
    }

    public void setPrioritySubmodule(String prioritySubmodule) {
        this.prioritySubmodule = prioritySubmodule;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<MenuModel> getMenuModelList() {
        return menuModelList;
    }

    public void setMenuModelList(ArrayList<MenuModel> menuModelList) {
        this.menuModelList = menuModelList;
    }
}
