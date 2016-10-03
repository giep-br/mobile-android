package br.com.allin.mobile.pushnotification.service;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.exception.WebServiceException;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.interfaces.OnInvoke;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

abstract class BaseService<T> extends AsyncTask<Void, Void, Object> implements OnInvoke<T> {
    protected Context context;
    protected OnRequest onRequest;
    protected RequestType requestType;
    protected boolean withCache;

    public BaseService(Context context, RequestType requestType,
                          boolean withCache, OnRequest onRequest) {
        this.onRequest = onRequest;
        this.context = context;
        this.requestType = requestType;
        this.withCache = withCache;
    }

    @Override
    public JSONObject getData() {
        return null;
    }

    @Override
    public String[] getParams() {
        return null;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            if (requestType == RequestType.GET) {
                return HttpManager.get(context, this.getUrl(), this.getParams());
            } else {
                return HttpManager.post(context,
                        this.getUrl(), this.getData(), this.getParams(), this.withCache);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);

        if (object instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) object;

            if (!responseEntity.isSuccess()) {
                if (onRequest != null) {
                    onRequest.onError(
                            new WebServiceException(responseEntity.getMessage()));
                }
            } else {
                if (onRequest != null) {
                    onRequest.onFinish(this.onSuccess(responseEntity));
                }
            }
        } else {
            if (onRequest != null) {
                onRequest.onError(
                        new WebServiceException(String.valueOf(object)));
            }
        }
    }
}
