package com.apollopharmacy.vishwam.util.firebase;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.ui.home.MainActivity;
import com.apollopharmacy.vishwam.ui.rider.dashboard.DashboardFragment;
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager;
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.OrderDeliveryActivity;
import com.apollopharmacy.vishwam.ui.rider.reports.ReportsActivity;
import com.apollopharmacy.vishwam.ui.rider.trackmap.TrackMapActivity;
import com.apollopharmacy.vishwam.util.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "Bestmarts";
    private static final String CHANNEL_NAME = "Bestmarts";
    private Context mContext;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotification1(remoteMessage);
        } else {
            sendNotification(remoteMessage);
        }
    }

    @SuppressLint("LongLogTag")
    private void sendNotification(RemoteMessage remoteMessage) {
        if (!isAppIsInBackground(getApplicationContext())) {
            //foreground app
            Utils.printMessage("remoteMessage foreground", remoteMessage.getData().toString());
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            int pendingFlags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
            } else {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            }


            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0 /* Request code */, resultIntent,
                    pendingFlags);
//                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.logo_apollo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setNumber(10)
                    .setTicker("Bestmarts")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("Info");
            notificationManager.notify(1, notificationBuilder.build());
            riderNotification(remoteMessage);
        } else {
            Utils.printMessage("remoteMessage background", remoteMessage.getData().toString());
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String body = data.get("body");
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            int pendingFlags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
            } else {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0 /* Request code */, resultIntent,
                    pendingFlags);
//                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.logo_apollo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setNumber(10)
                    .setTicker("Bestmarts")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("Info");
            notificationManager.notify(1, notificationBuilder.build());
            riderNotification(remoteMessage);
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    @SuppressLint("NewApi")
    private void sendNotification1(RemoteMessage remoteMessage) {
        if (!isAppIsInBackground(getApplicationContext())) {
            //foreground app
            Utils.printMessage("remoteMessage", remoteMessage.getData().toString());
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            int pendingFlags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
            } else {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0 /* Request code */, resultIntent,
                    pendingFlags);
//                    PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultsound, String.valueOf(R.drawable.logo), remoteMessage);

            int i = 0;
            oreoNotification.getManager().notify(i, builder.build());
        } else {
            Utils.printMessage("remoteMessage", remoteMessage.getData().toString());
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            int pendingFlags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
            } else {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0 /* Request code */, resultIntent,
                    pendingFlags);
//                    PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultsound, String.valueOf(R.drawable.logo), remoteMessage);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int i = 0;
            oreoNotification.getManager().notify(i, builder.build());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Utils.printMessage("NEW_TOKEN = = == = = =", s);
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
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 1000);

                        handler.postDelayed(() -> {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
                            mp.start();
                        }, 2000);

                        handler.postDelayed(() -> {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_sound); // sound is inside res/raw/mysound
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
        return new SessionManager(this);
    }
}