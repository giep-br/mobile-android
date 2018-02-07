package br.com.allin.mobile.pushnotification.entity.allin;

import android.database.Cursor;
import android.os.Bundle;

import br.com.allin.mobile.pushnotification.constants.MessageConstant;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageEntity {
    private int id;
    private String idSend;
    private String subject;
    private String description;
    private String idCampaign;
    private String idLogin;
    private String urlScheme;
    private String action;
    private String date;
    private String urlTransactional;
    private String urlCampaign;
    private boolean read;

    public MessageEntity(int id, String idSend, String subject, String description,
                         String idCampaign, String idLogin, String urlScheme,
                         String action, String date, String urlCampaign, String urlTransactional) {
        this.id = id;
        this.idSend = idSend;
        this.subject = subject;
        this.description = description;
        this.idCampaign = idCampaign;
        this.idLogin = idLogin;
        this.urlScheme = urlScheme;
        this.action = action;
        this.date = date;
        this.urlTransactional = urlTransactional;
        this.urlCampaign = urlCampaign;

        updateNullValues();
    }

    public MessageEntity(int id, String idSend, String subject, String description,
                         String idCampaign, String idLogin, String urlScheme,
                         String action, String date, String urlCampaign,
                         String urlTransactional, boolean read) {
        this.id = id;
        this.idSend = idSend;
        this.subject = subject;
        this.description = description;
        this.idCampaign = idCampaign;
        this.idLogin = idLogin;
        this.urlScheme = urlScheme;
        this.action = action;
        this.date = date;
        this.urlTransactional = urlTransactional;
        this.urlCampaign = urlCampaign;
        this.read = read;

        updateNullValues();
    }

    public MessageEntity(Cursor cursor) {
        this.id = getCursorValue(cursor, MessageConstant.DB_FIELD_ID, 0);
        this.idSend = getCursorValue(cursor, MessageConstant.DB_FIELD_ID_SEND, "");
        this.subject = getCursorValue(cursor, MessageConstant.DB_FIELD_SUBJECT, "");
        this.description = getCursorValue(cursor, MessageConstant.DB_FIELD_DESCRIPTION, "");
        this.idCampaign = getCursorValue(cursor, MessageConstant.DB_FIELD_ID_CAMPAIGN, "");
        this.idLogin = getCursorValue(cursor, MessageConstant.DB_FIELD_ID_LOGIN, "");
        this.urlScheme = getCursorValue(cursor, MessageConstant.DB_FIELD_URL_SCHEME, "");
        this.action = getCursorValue(cursor, MessageConstant.DB_FIELD_ACTION, "");
        this.date = getCursorValue(cursor, MessageConstant.DB_FIELD_DATE_NOTIFICATION, "");
        this.urlTransactional = getCursorValue(cursor, MessageConstant.DB_FIELD_URL_TRANSACTIONAL, "");
        this.urlCampaign = getCursorValue(cursor, MessageConstant.DB_FIELD_URL_CAMPAIGN, "");
        this.read = getCursorValue(cursor, MessageConstant.DB_FIELD_READ, false);

        updateNullValues();
    }

    public <T> T getCursorValue(Cursor cursor, String key, Object valueDefault) {
        int index = cursor.getColumnIndex(key);
        Object object = null;

        if (index >= 0) {
            if (valueDefault instanceof String) {
                object = cursor.getString(index);
            } else if (valueDefault instanceof Integer) {
                object = cursor.getInt(index);
            }
        }

        return (T) (object == null ? valueDefault : object);
    }

    public MessageEntity(Bundle bundle) {
        this.id = bundle.getInt(MessageConstant.DB_FIELD_ID);
        this.idSend = bundle.getString(MessageConstant.DB_FIELD_ID_SEND);
        this.subject = bundle.getString(MessageConstant.DB_FIELD_SUBJECT);
        this.description = bundle.getString(MessageConstant.DB_FIELD_DESCRIPTION);
        this.idCampaign = bundle.getString(MessageConstant.DB_FIELD_ID_CAMPAIGN);
        this.idLogin = bundle.getString(MessageConstant.DB_FIELD_ID_LOGIN);
        this.urlScheme = bundle.getString(MessageConstant.DB_FIELD_URL_SCHEME);
        this.action = bundle.getString(MessageConstant.DB_FIELD_ACTION);
        this.date = bundle.getString(MessageConstant.DB_FIELD_DATE_NOTIFICATION);
        this.urlTransactional = bundle.getString(MessageConstant.DB_FIELD_URL_TRANSACTIONAL);
        this.urlCampaign = bundle.getString(MessageConstant.DB_FIELD_URL_CAMPAIGN);
        this.read = bundle.getInt(MessageConstant.DB_FIELD_READ) == 1;

        updateNullValues();
    }

    private void updateNullValues() {
        if (idSend == null) {
            idSend = "";
        }

        if (subject == null) {
            subject = "";
        }

        if (description == null) {
            description = "";
        }

        if (idCampaign == null) {
            idCampaign = "";
        }

        if (idLogin == null) {
            idLogin = "";
        }

        if (urlScheme == null) {
            urlScheme = "";
        }

        if (action == null) {
            action = "";
        }

        if (date == null) {
            date = "";
        }

        if (urlTransactional == null) {
            urlTransactional = "";
        }

        if (urlCampaign == null) {
            urlCampaign = "";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdSend() {
        return idSend;
    }

    public void setIdSend(String idSend) {
        this.idSend = idSend;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdCampaign() {
        return idCampaign;
    }

    public void setIdCampaign(String idCampaign) {
        this.idCampaign = idCampaign;
    }

    public String getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(String idLogin) {
        this.idLogin = idLogin;
    }

    public String getUrlScheme() {
        return urlScheme;
    }

    public void setUrlScheme(String urlScheme) {
        this.urlScheme = urlScheme;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlTransactional() {
        return urlTransactional;
    }

    public void setUrlTransactional(String urlTransactional) {
        this.urlTransactional = urlTransactional;
    }

    public String getUrlCampaign() {
        return urlCampaign;
    }

    public void setUrlCampaign(String urlCampaign) {
        this.urlCampaign = urlCampaign;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}