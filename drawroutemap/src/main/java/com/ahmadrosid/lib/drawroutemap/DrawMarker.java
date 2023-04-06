package com.ahmadrosid.lib.drawroutemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */
public class DrawMarker {

    public static DrawMarker INSTANCE;

    Marker marker = null;

    public static DrawMarker getInstance(Context context) {
        INSTANCE = new DrawMarker(context);
        return INSTANCE;
    }

    private Context context;

    DrawMarker(Context context) {
        this.context = context;
    }

    public void draw(GoogleMap googleMap, LatLng location, int resDrawable, String title, int load, HashMap<Integer, Marker> hashMap) {
        try {
            Drawable circleDrawable = ContextCompat.getDrawable(context, resDrawable);
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

            if (load != 0 && hashMap.containsKey(1)) {
                Marker marker = hashMap.get(load);
                marker.setPosition(location);// Update your marker
            } else {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(title)
                        .icon(markerIcon));

                hashMap.put(load, marker);
            }
        } catch (Exception e) {
            System.out.println("DrawMarker================================= class" + e.getMessage());
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}