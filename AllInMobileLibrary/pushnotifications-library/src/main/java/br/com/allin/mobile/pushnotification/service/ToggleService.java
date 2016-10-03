package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Parameters;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class ToggleService extends BaseService<String> {
    private boolean enable;

    public ToggleService(boolean enable,
                         Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        this.enable = enable;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();
            data.put(HttpBody.DEVICE_TOKEN, AllInPush.getInstance().getDeviceId());
            data.put(HttpBody.PLATFORM, Parameters.ANDROID);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String getUrl() {
        return enable ? Route.DEVICE_ENABLE : Route.DEVICE_DISABLE;
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
