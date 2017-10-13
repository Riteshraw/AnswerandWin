package com.nubiz.answerandwin.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nubiz.answerandwin.util.Utils;

/**
 * Created by admin on 05-Oct-17.
 */

public class NotificatoinActionService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras().getString("response").equals("yes"))
            Utils.showToast(this, "yes");
        else if (intent.getExtras().getString("response").equals("cancel"))
            Utils.showToast(this, "cancel");
        else
            Utils.showToast(this, "no");

        int notificationId = intent.getIntExtra("notifyID", -1);
        if (notificationId != -1) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
