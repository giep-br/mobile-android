package br.com.allin.mobile.pushnotification.service;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;

/**
 * The service class for GCM (Google Cloud MessageConstants) has the responsibility
 * of the sender's ID number generator for Google properly send notification according to the record
 */
public class GCMService extends AsyncTask<Void, Void, String> {
    private ConfigurationEntity configurationEntity;
    private DeviceEntity deviceEntity;

    GCMService(DeviceEntity deviceEntity, ConfigurationEntity configurationEntity) {
        this.configurationEntity = configurationEntity;
        this.deviceEntity = deviceEntity;
    }

    @Override
    protected String doInBackground(Void... params) {
        InstanceID instanceID = InstanceID.getInstance(AlliNPush.getInstance().getContext());

        String token = null;

        for (int attempts = 0; attempts < 3 &&
                (token == null || TextUtils.isEmpty(token)); attempts++) {
            try {
                token = instanceID.getToken(configurationEntity.getSenderId(),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        }

        Context context = AlliNPush.getInstance().getContext();
        String senderId = configurationEntity.getSenderId();

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        sharedPreferencesManager.storeData(PreferencesConstants.KEY_DEVICE_ID, token);
        sharedPreferencesManager.storeData(PreferencesConstants.KEY_PROJECT_ID, senderId);

        return token;
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        if (token != null && !TextUtils.isEmpty(token)) {
            deviceEntity.setDeviceId(token);

            AlliNPush.getInstance().sendDeviceInfo(deviceEntity);
        }
    }
}
