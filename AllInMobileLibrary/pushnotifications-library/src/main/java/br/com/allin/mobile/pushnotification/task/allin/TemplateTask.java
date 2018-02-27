package br.com.allin.mobile.pushnotification.task.allin;

import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.constants.RouteConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Thread for template campaign request
 */
public class TemplateTask extends BaseTask<String> {
    private int id;

    public TemplateTask(int id, OnRequest onRequest) {
        super(RequestType.GET, false, onRequest);

        this.id = id;
    }

    @Override
    public String[] getParams() {
        return new String[] { String.valueOf(id) };
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + RouteConstant.CAMPAIGN;
    }

    @Override
    public String onSuccess(AIResponse AIResponse) {
        return  AIResponse.getMessage();
    }
}
