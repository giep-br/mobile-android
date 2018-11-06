package br.com.allin.mobile.pushnotification.gcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.ActionConstants;
import br.com.allin.mobile.pushnotification.constants.NotificationConstants;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.entity.MessageEntity;
import br.com.allin.mobile.pushnotification.http.DownloadImage;

/**
 * Class that provides the notification of receipt of a push GCM.
 */
class AllInGcmNotification {
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;

    AllInGcmNotification(Context context) {
        this.context = context;
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    /**
     * Create a standard notification with title and text, sending additional parameters from a @code {Bundle}.
     *
     * @param extras ParametersConstants to be included in the notification.
     */
    void showNotification(final Bundle extras) {
        if (extras == null) {
            return;
        }

        this.context.registerReceiver(new BroadcastNotification(), new IntentFilter(BroadcastNotification.ACTION));

        dealScheme(extras);

        String image = extras.getString(NotificationConstants.IMAGE);

        if (Util.isNullOrClear(image)) {
            showNotification(null, extras);
        } else {
            new DownloadImage(image, new DownloadImage.OnDownloadCompleted() {
                @Override
                public void onCompleted(Bitmap bitmap) {
                    showNotification(bitmap, extras);
                }

                @Override
                public void onError() {
                    showNotification(null, extras);
                }
            }).execute();
        }
    }

    private void dealScheme(Bundle extras) {
        String scheme = extras.getString(NotificationConstants.URL_SCHEME);

        if (scheme != null && scheme.trim().length() > 0) {
            try {
                scheme = URLDecoder.decode(scheme, "UTF-8");
            } catch (Exception e) {
                Log.e(AllInGcmNotification.class.toString(), "ERRO IN DECODE URL");
            } finally {
                if (scheme.contains("##id_push##")) {
                    scheme = scheme.replace("##id_push##",
                            Util.md5(AllInPush.getInstance().getDeviceId()));
                }

                extras.putString(NotificationConstants.URL_SCHEME, scheme);
            }
        }
    }

    private void showNotification(Bitmap bitmap, Bundle extras) {
        final String title = extras.getString(NotificationConstants.SUBJECT);
        final String content = extras.getString(NotificationConstants.DESCRIPTION);

        if (title == null || title.trim().length() == 0 || content == null || content.trim().length() == 0) {
            return;
        }

        String backgroundColor = sharedPreferencesManager.getData(PreferencesConstants.KEY_BACKGROUND_NOTIFICATION, null);
        int whiteIcon = sharedPreferencesManager.getData(PreferencesConstants.KEY_WHITE_ICON_NOTIFICATION, 0);
        int icon = sharedPreferencesManager.getData(PreferencesConstants.KEY_ICON_NOTIFICATION, 0);
        long idMessage = AllInPush.getInstance().addMessage(new MessageEntity(extras));

        extras.putLong(NotificationConstants.ID, idMessage);

        Intent intent = new Intent();
        intent.setAction(BroadcastNotification.ACTION);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder
                notificationCompatBuilder = new NotificationCompat.Builder(context, "notify_001");

        if (icon == 0) {
            notificationCompatBuilder
                .setSmallIcon(whiteIcon != 0 ? whiteIcon : getNotificationIcon(context));
        } else {
            notificationCompatBuilder
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setSmallIcon(whiteIcon);
        }

        notificationCompatBuilder
            .setColor(backgroundColor != null ? Color.parseColor(backgroundColor) : Color.TRANSPARENT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setGroupSummary(true)
            .setGroup("messages")
            .setContentIntent(pendingIntent)
            .setContentText(content)
            .setContentTitle(title)
            .setAutoCancel(true);

        if (bitmap != null) {
            notificationCompatBuilder.setStyle(
                new NotificationCompat.BigPictureStyle().bigPicture(bitmap)
            );
        } else {
            notificationCompatBuilder.setStyle(
                new NotificationCompat.BigTextStyle().bigText(content)
            );
        }

        addActions(context, notificationCompatBuilder, extras);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel " + idMessage, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            notificationManager.createNotificationChannel(channel);
        }


        if (notificationManager != null) {
            notificationManager.notify((int) idMessage, notificationCompatBuilder.build());
        }
    }

    private void addActions(Context context,
                            NotificationCompat.Builder notificationBuilder, Bundle extras) {
        String actions = extras.getString(NotificationConstants.ACTIONS);

        if (actions != null && !TextUtils.isEmpty(actions)) {
            try {
                JSONArray jsonArray = new JSONArray(extras.getString(NotificationConstants.ACTIONS));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String action = jsonObject.getString(ActionConstants.ACTION);
                    String text = jsonObject.getString(ActionConstants.TEXT);

                    Intent intentAction = new Intent();
                    intentAction.setAction(BroadcastNotification.ACTION);
                    intentAction.putExtra(ActionConstants.class.toString(), action);

                    PendingIntent pendingIntent = PendingIntent
                            .getBroadcast(context, i, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);

                    notificationBuilder.addAction(0, text, pendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the id of the application of the default icon.
     *
     * @param context Application context.
     *
     * @return Application icon id.
     */
    private int getNotificationIcon(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        PackageManager packageManager = context.getPackageManager();

        int iconResource = 0;

        try {
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            iconResource = applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return iconResource;

    }

}
