package com.apollopharmacy.vishwam.util.firebase;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.ui.home.MainActivity;
import com.apollopharmacy.vishwam.ui.rider.dashboard.DashboardFragment;
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.OrderDeliveryActivity;
import com.apollopharmacy.vishwam.ui.rider.reports.ReportsActivity;
import com.apollopharmacy.vishwam.ui.rider.trackmap.TrackMapActivity;
import com.apollopharmacy.vishwam.util.Utils;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class OreoNotification extends ContextWrapper {

    private static final String CHANNEL_ID = "Fcm Test";
    private static final String CHANNEL_NAME = "Fcm Test";
    private NotificationManager notificationManager;
    private Context context;

    public OreoNotification(Context base) {
        super(base);
        this.context = base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Fcm Test channel for app test FCM");
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel.setShowBadge(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }


    @SuppressLint("ResourceAsColor")
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent, Uri soundUri, String icon, @NonNull RemoteMessage remoteMessage) {
        riderNotification(remoteMessage);


        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_apollo)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setTicker("Fcm Test")
                .setNumber(10)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setContentInfo("Info");
    }

    private void riderNotification(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            try {
                if (remoteMessage.getData() != null && remoteMessage.getData().get("uid") != null) {
                    Utils.isMyOrdersListApiCall = true;
                    if (remoteMessage.getData().get("notification_type").equals("ORDER_CANCELLED")) {
                        if (Utils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_cancelled", true);
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals("TrackMapActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, TrackMapActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_cancelled", true);
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals("ReportsActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, ReportsActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_cancelled", true);
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                    } else if (remoteMessage.getData().get("notification_type").equals("ORDER_SHIFTED")) {
                        if (Utils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals(MainActivity.class.getSimpleName())) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals("TrackMapActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, TrackMapActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals("ReportsActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, ReportsActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("order_shifted", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                    } else if (remoteMessage.getData().get("notification_type").equals("ORDER_ASSIGNED")) {
                        if (!getSessionManager().getNotificationStatus())
                            Utils.NOTIFICATIONS_COUNT = 0;
                        Utils.NOTIFICATIONS_COUNT = Utils.NOTIFICATIONS_COUNT + 1;

                        if (getSessionManager().getAsignedOrderUid() != null) {
                            List<String> orderUidList = getSessionManager().getAsignedOrderUid();
                            orderUidList.add(remoteMessage.getData().get("uid"));
                            getSessionManager().setAsignedOrderUid(orderUidList);
                        } else {
                            List<String> orderUidList = new ArrayList<>();
                            orderUidList.add(remoteMessage.getData().get("uid"));
                            getSessionManager().setAsignedOrderUid(orderUidList);
                        }
                        if (Utils.CURRENT_SCREEN.equals(MainActivity.class.getSimpleName())) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("ORDER_ASSIGNED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.postDelayed(() -> {
                            MainActivity.notificationDotVisibility(true);
                            ReportsActivity.notificationDotVisibility(true);
                            OrderDeliveryActivity.notificationDotVisibility(true);
                            DashboardFragment.newOrderViewVisibility(true);
                            getSessionManager().setNotificationStatus(true);
                            MediaPlayer mp = MediaPlayer.create(context, R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 1000);

                        handler.postDelayed(() -> {
                            MediaPlayer mp = MediaPlayer.create(context, R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 2000);

                        handler.postDelayed(() -> {
                            MediaPlayer mp = MediaPlayer.create(context, R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 3000);
                    } else if (remoteMessage.getData().get("notification_type").equals("COMPLAINT_RESOLVED")) {
                        if (Utils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals(MainActivity.class.getSimpleName())) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals("TrackMapActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, TrackMapActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        } else if (Utils.CURRENT_SCREEN.equals("ReportsActivity")) {
                            String orderNumber = remoteMessage.getData().get("uid");
                            Intent intent = new Intent(this, ReportsActivity.class);
                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("COMPLAINT_RESOLVED", true);
                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
                            intent.putExtra("order_uid", orderNumber);
                            startActivity(intent);
                        }
                    }


//                    if (remoteMessage.getData().get("order_status") != null) {
//                        if (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_cancelled", true);
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, TrackMapActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_cancelled", true);
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("ReportsActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, ReportsActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_cancelled", true);
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        }
//                    } else if (Objects.requireNonNull(remoteMessage.getNotification().getBody()).contains("assigned to another rider")) {
//                        if (CommonUtils.CURRENT_SCREEN.equals("OrderDeliveryActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, OrderDeliveryActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals(NavigationActivity.class.getSimpleName())) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, NavigationActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("TrackMapActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, TrackMapActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        } else if (CommonUtils.CURRENT_SCREEN.equals("ReportsActivity")) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, ReportsActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("order_shifted", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        }
//
//
////                        SnackbarLayoutBinding snackbarLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.snackbar_layout, null, false);
////                        Snackbar.make(snackbarLayoutBinding.getRoot(), remoteMessage.getNotification().toString(), Snackbar.LENGTH_LONG).show();
//                    } else {
//                        if (!getSessionManager().getNotificationStatus())
//                            CommonUtils.NOTIFICATIONS_COUNT = 0;
//                        CommonUtils.NOTIFICATIONS_COUNT = CommonUtils.NOTIFICATIONS_COUNT + 1;
//
//                        if (getSessionManager().getAsignedOrderUid() != null) {
//                            List<String> orderUidList = getSessionManager().getAsignedOrderUid();
//                            orderUidList.add(remoteMessage.getData().get("uid"));
//                            getSessionManager().setAsignedOrderUid(orderUidList);
//                        } else {
//                            List<String> orderUidList = new ArrayList<>();
//                            orderUidList.add(remoteMessage.getData().get("uid"));
//                            getSessionManager().setAsignedOrderUid(orderUidList);
//                        }
//                        if (CommonUtils.CURRENT_SCREEN.equals(NavigationActivity.class.getSimpleName())) {
//                            String orderNumber = remoteMessage.getData().get("uid");
//                            Intent intent = new Intent(this, NavigationActivity.class);
//                            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES |
//                                    Intent.FLAG_ACTIVITY_NEW_TASK |
//                                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.putExtra("ORDER_ASSIGNED", true);
//                            intent.putExtra("NOTIFICATION", remoteMessage.getNotification().getBody());
//                            intent.putExtra("order_uid", orderNumber);
//                            startActivity(intent);
//                        }
//                        Handler handler = new Handler(Looper.getMainLooper());
//
//                        handler.postDelayed(() -> {
//                            NavigationActivity.notificationDotVisibility(true);
//                            ReportsActivity.notificationDotVisibility(true);
//                            OrderDeliveryActivity.notificationDotVisibility(true);
//                            DashboardFragment.newOrderViewVisibility(true);
//                            getSessionManager().setNotificationStatus(true);
//                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
//                            mp.start();
//                        }, 1000);
//
//                        handler.postDelayed(() -> {
//                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
//                            mp.start();
//                        }, 2000);
//
//                        handler.postDelayed(() -> {
//                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
//                            mp.start();
//                        }, 3000);
//                    }
                }
            } catch (Exception e) {
                System.out.println("MyFirebaseMessagingSerice:::::::::" + e.getMessage());
            }
        }
    }

    public SessionManager getSessionManager() {
        return new SessionManager(context);
    }
}
