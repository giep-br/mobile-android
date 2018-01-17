package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for logout request
 */
public class LogoutTask extends BaseTask<String> {
    public LogoutTask(Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);
    }

    @Override
    public String getUrl() {
        return RouteConstants.DEVICE_LOGOUT;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            data.put(HttpBodyConstants.DEVICE_TOKEN, AlliNPush.getInstance().getDeviceId());
            data.put(HttpBodyConstants.USER_EMAIL, AlliNPush.getInstance().getUserEmail());

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
