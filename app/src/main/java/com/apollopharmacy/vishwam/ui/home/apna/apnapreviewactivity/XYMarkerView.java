package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollopharmacy.vishwam.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class XYMarkerView extends MarkerView {

    private final TextView tvContent;
    private LinearLayout linearLayout;
    private final ValueFormatter xAxisValueFormatter;

    private final DecimalFormat format;

    public XYMarkerView(Context context, ValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_apna);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent_apna);
        linearLayout = findViewById(R.id.layout_marker);
        format = new DecimalFormat("###.0");
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

//        tvContent.setText(String.format("Name: %s, Value: %s", xAxisValueFormatter.getFormattedValue(e.getX()), format.format(e.getY())));
        if (e.getData() != null) {
            linearLayout.setVisibility(View.VISIBLE);
//            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(String.format(e.getData().toString() + ": %s", format.format(e.getY())));
        } else {
            linearLayout.setVisibility(View.VISIBLE);
//            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(String.format("Value: %s", format.format(e.getY())));
        }
//       Handler ha = new Handler();
//        ha.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tvContent.setVisibility(View.GONE);
//                linearLayout.setVisibility(View.GONE);
//            }
//        },1000);




      super.refreshContent(e, highlight);
    }

   public void onAndroidTurn(Entry e){
       if (e.getData() != null) {
           tvContent.setVisibility(View.VISIBLE);
           tvContent.setText(String.format(e.getData().toString() + ": %s", format.format(e.getY())));
       } else {
           tvContent.setVisibility(View.VISIBLE);
           tvContent.setText(String.format("Value: %s", format.format(e.getY())));
       }

       tvContent.postDelayed(new Runnable() {
           @Override
           public void run() {
               tvContent.setVisibility(View.GONE);
           }
       }, 3000);
    }
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
