package com.apollopharmacy.vishwam.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apollopharmacy.vishwam.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PopUpWIndow extends PopupWindow {


    View view;
    Context mContext;
    PhotoView photoView;
    ProgressBar loading;
    TextView textView, imagePositionTextView;

    ViewGroup parent;
    private static PhotoPopupWindow instance = null;

    public PopUpWIndow(Context ctx, int layout, View v, String imageUrl, Bitmap bitmap, String category, int imagePosition) {
        super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_imageview, null), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(5.0f);
        }
        this.mContext = ctx;
        this.view = getContentView();
        ImageButton closeButton = this.view.findViewById(R.id.ib_close);
        setOutsideTouchable(true);

        setFocusable(true);
        closeButton.setOnClickListener(view -> dismiss());
        //---------Begin customising this popup--------------------

        photoView = view.findViewById(R.id.image);
        loading = view.findViewById(R.id.loading);
        textView = view.findViewById(R.id.category_naePop);
        imagePositionTextView = view.findViewById(R.id.count_category);

        textView.setText(category);
        int addOne = 1;
        int myInt = imagePosition+addOne;

//        int myInt = Integer.parseInt(String.valueOf(imagePosition).addOne);
      imagePositionTextView.setText(String.valueOf(myInt));

        photoView.setMaximumScale(6);
        parent = (ViewGroup) photoView.getParent();

        if (bitmap != null) {
            loading.setVisibility(View.GONE);
            photoView.setImageBitmap(bitmap);
        } else {
            loading.setIndeterminate(true);
            loading.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(imageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.thumbnail_image).error(R.drawable.thumbnail_image))
                    .into(photoView);
            showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }


//    public PhotoPopupWindow(@NotNull Context context, int layoutImageFullview, @NotNull View view, @NotNull String toString, @Nullable Void nothing, @Nullable String categoryname) {
//        textView.setText(categoryname);
//    }
}

