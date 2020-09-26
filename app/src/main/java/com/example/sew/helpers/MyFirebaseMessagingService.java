package com.example.sew.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.TextureView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.SplashActivity;
import com.example.sew.models.ContentType;
import com.example.sew.models.PushNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import ir.zadak.zadaknotify.notification.ZadakNotification;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    PushNotification pushNotification;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {

            String title = remoteMessage.getData().get("title");
            int notificationId = 0;
            if (!TextUtils.isEmpty(remoteMessage.getData().get("pId")))
                notificationId = remoteMessage.getData().get("pId").hashCode();
             pushNotification = new PushNotification(new JSONObject(remoteMessage.getData().get("body")));
            if (!TextUtils.isEmpty(pushNotification.getImageShayari().getContentId())) {
                generatePush(title, "", pushNotification.getImageShayari().getImageUrl(), true, notificationId);
            } else if (!TextUtils.isEmpty(pushNotification.getRemembering().getPoetId())) {
                generatePush(pushNotification.getRemembering().getDayType(), pushNotification.getRemembering().getPoetName(), pushNotification.getRemembering().getImageUrl(), false, notificationId);
            } else if (!TextUtils.isEmpty(pushNotification.getSherOfTheDay().getContentId())) {
                generatePush(title, pushNotification.getSherOfTheDay().getTitle(), notificationId);
            } else if (!TextUtils.isEmpty(pushNotification.getWordOfTheDay().getDictionaryId())) {
                generatePush(title, pushNotification.getWordOfTheDay().getWord_En(), notificationId);
            } else if (!TextUtils.isEmpty(pushNotification.getEvent().getIsEvent())) {
                generatePush(pushNotification.getEvent().getEventTitle(), pushNotification.getEvent().getEventDescription(), pushNotification.getEvent().getImageUrl(), false, notificationId);
            } else if (!TextUtils.isEmpty(pushNotification.getGeneral().getTitle())) {
                generatePush(pushNotification.getGeneral().getTitle(), pushNotification.getGeneral().getContent(),pushNotification.getGeneral().getImageURL(),false, notificationId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void generatePush(String title, String subTitle, int notificationId) {
        generatePush(title, subTitle, "", false, notificationId);
    }

    private void generatePush(String title, String subTitle, String imageUrl, boolean showLargeImage, int notificationId) {
        Context mContext = MyApplication.getContext();
        new ShowPush().showPush(mContext, title, subTitle, SplashActivity.getInstance(MyApplication.getContext(),pushNotification), imageUrl, showLargeImage, notificationId);
    }

    public class ShowPush {
        Bitmap largeIcon;
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                largeIcon = bitmap;
                mBuilder.setLargeIcon(largeIcon);
             //   if (showLargeImage)
                    mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon(null));
                notificationManager.notify(notificationId, mBuilder.build());
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        void showPush(Context context, String title, String body, Intent intent, String imageUrl, boolean showLargeImage, int notificationId) {
            showNotification(context, title, body, intent, imageUrl, showLargeImage, notificationId);
        }

        NotificationManager notificationManager;
        NotificationCompat.Builder mBuilder;
        int notificationId;
        boolean showLargeImage;

        void showNotification(Context context, String title, String body, Intent intent, String imageUrl, boolean showLargeImage, int notificationId) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "channel-01";
            String channelName = "Rekhta.org";
            this.notificationId = notificationId;
            this.showLargeImage = showLargeImage;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            int min = 65;
            int max = 80;
            Random r = new Random();
            int randomNumber = r.nextInt(max - min + 1) + min;
            mBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.rtxt)
                    .setContentTitle(title)
                    .setContentText(body);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(intent);
           // PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(randomNumber, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setAutoCancel(true);// Add this Line By Anuj
            mBuilder.setContentIntent(resultPendingIntent);
            if (!TextUtils.isEmpty(imageUrl)) {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(() -> Picasso.get().load(imageUrl).into(target));
            } else
                notificationManager.notify(notificationId, mBuilder.build());

        }
    }

    @Override
    public void onNewToken(String s) {
        MyService.setFcmToken(s);
        super.onNewToken(s);
    }
}
