package br.com.allin.mobile.pushnotification.gcm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.allin.mobile.pushnotification.AllInApplication;
import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.Notification;

/**
 * IntentService that it will be executed when a notification was received.
 */
public class AllInGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (!data.isEmpty()) {
            readContentFromNotification(data);
        }
    }

    private void readContentFromNotification(Bundle data) {
        final String subject = data.getString(Notification.SUBJECT);
        final String description = data.getString(Notification.DESCRIPTION);
        final String action = data.getString(Notification.ACTION);

        if (!Util.isNullOrClear(action)) {
            if (AllInPush.getInstance().getContext() instanceof AllInApplication) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        AllInPush.getInstance().getAlliNApplication().onAction(action);
                    }
                }.sendEmptyMessage(0);
            }
        } else if (!Util.isNullOrClear(subject) && !Util.isNullOrClear(description)) {
            AllInGcmNotification.showNotification(this, subject, description, data);
        }
    }
}
