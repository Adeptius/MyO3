package ua.adeptius.freenet.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ip {
    private String ip;
    private String gateway;
    private String mask;

    public Ip(String json) {
        try {
            JSONObject jobject = new JSONObject(json);
            ip = jobject.getString("ip");
            mask = jobject.getString("mask");
            gateway = jobject.getString("gateway");
        } catch (JSONException e) {
            e.printStackTrace();
            ip = "";
            mask = "";
            gateway = "";
        }
    }

    public String getIp() {
        return ip;
    }

    public String getGateway() {
        return gateway;
    }

    public String getMask() {
        return mask;
    }

    public String getDns1() {
        return "193.24.25.1";
    }

    public String getDns2() {
        return "193.24.25.250";
    }
}
