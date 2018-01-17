package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.DefaultListConstants;
import br.com.allin.mobile.pushnotification.constants.ParametersConstants;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.DeviceTask;
import br.com.allin.mobile.pushnotification.task.EmailTask;
import br.com.allin.mobile.pushnotification.task.ListTask;
import br.com.allin.mobile.pushnotification.task.LogoutTask;

/**
 * Service class for device information and configuration
 */
public class DeviceService {
    private Context context;
    private OnRequest onRequest;

    public DeviceService(Context context) {
        this.context = context;
        this.onRequest = null;
    }

    public DeviceService(Context context, OnRequest onRequest) {
        this.context = context;
        this.onRequest = onRequest;
    }

    public void sendDevice(final DeviceEntity deviceEntity) {
        new DeviceTask(deviceEntity, context, new OnRequest() {
            @Override
            public void onFinish(Object value) {
                String pushId = AlliNPush.getInstance().getDeviceId();
                Map<String, String> map = new HashMap<>();
                map.put(DefaultListConstants.ID_PUSH, Util.md5(pushId));
                map.put(DefaultListConstants.PUSH_ID, pushId);
                map.put(DefaultListConstants.PLATAFORMA, ParametersConstants.ANDROID);

                AlliNPush.getInstance().sendList(DefaultListConstants.LISTA_PADRAO, map);
            }

            @Override
            public void onError(Exception exception) {
                if (onRequest != null) {
                    onRequest.onError(exception);
                }
            }
        }).execute();
    }

    public void logout() {
        new LogoutTask(context, onRequest).execute();
    }

    public void sendList(String nameList, Map<String, String> columnsAndValues) {
        new ListTask(nameList, columnsAndValues, context, onRequest).execute();
    }

    public void updateEmail(String email) {
        new EmailTask(email, context, onRequest).execute();
    }

    public String getDeviceToken() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);

        return sharedPreferencesManager.getData(PreferencesConstants.KEY_DEVICE_ID, null);
    }

    public String getUserEmail() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);

        return sharedPreferencesManager.getData(PreferencesConstants.KEY_USER_EMAIL, null);
    }

    public DeviceEntity getDeviceInfos(String senderId) {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);

        String deviceId = sharedPreferencesManager.getData(PreferencesConstants.KEY_DEVICE_ID, null);
        Integer registeredVersion = sharedPreferencesManager.getData(PreferencesConstants.KEY_APPVERSION, 1);
        String sharedProjectId = sharedPreferencesManager.getData(PreferencesConstants.KEY_PROJECT_ID, null);

        if (Util.isNullOrClear(deviceId)) {
            return null;
        }

        return new DeviceEntity(deviceId,
                registeredVersion != Util.getAppVersion(context) || !senderId.equals(sharedProjectId));
    }
}
