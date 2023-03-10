package com.apollopharmacy.vishwam.ui.rider.takeneworder;

public interface OnItemClickListener {
    void onItemClick(int position, boolean isSelected);

    void onDecrementClick(int position);

    void onIncrementClick(int position);
}
