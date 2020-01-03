/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appynitty.manmad_npp.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.appynitty.ghantagaditracker.activity.DashboardActivity;
import com.appynitty.ghantagaditracker.controller.Notification;
import com.appynitty.ghantagaditracker.utils.AUtils;
import com.appynitty.ghantagaditracker.utils.DatabaseHelper;
import com.appynitty.manmad_npp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Inside   onMessageReceived");

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        Log.d(TAG, "TITLE : " + remoteMessage.getData().get("title"));
        Log.d(TAG, "MESSAGE : " + remoteMessage.getData().get("message"));
        Log.d(TAG, "BODY : " + remoteMessage.getData().get("body"));

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.insertNotification(remoteMessage.getData().get("message"), AUtils.getNotificationDateTime(), Notification.STATUS_UNREAD);

        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("body"));
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody, String type) {

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AUtils.FCM_NOTI, true);
        intent.putExtra(AUtils.FCM_NOTI_TYPE, type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.notify);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelId = "mChannel_DEMO";
        CharSequence channelName = getResources().getString(R.string.app_name);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getPackageName())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
//                .setContentText(messageBody)// single line message
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody)) // Multi line message
                .setTicker(getString(R.string.app_name))
                .setColor(Color.parseColor("#FFA726"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(Bitmap.createScaledBitmap(largeIconBitmap, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setChannelId(channelId);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));

        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of l_notification */, notificationBuilder.build());
        }
    }
}