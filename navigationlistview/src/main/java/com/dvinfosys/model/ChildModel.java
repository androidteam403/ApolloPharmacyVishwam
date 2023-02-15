package com.dvinfosys.model;

public class ChildModel {
    String title;
    boolean isSelected;
    int bgColor;
    int icon;

    public ChildModel(String title, int icon){
        this.title = title;
        this.icon=icon;
    }

    public ChildModel(String title, boolean isSelected){
        this.title = title;
        this.isSelected = isSelected;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
