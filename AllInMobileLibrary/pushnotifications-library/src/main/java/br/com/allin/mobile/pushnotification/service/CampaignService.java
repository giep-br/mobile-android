package br.com.allin.mobile.pushnotification.service;

import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.TemplateTask;

/**
 * Service class for campaign
 */
public class CampaignService {
    private OnRequest onRequest;

    public CampaignService(OnRequest onRequest) {
        this.onRequest = onRequest;
    }

    public void getTemplate(int idCampaign) {
        new TemplateTask(idCampaign, onRequest).execute();
    }
}
