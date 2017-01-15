package ua.adeptius.myo3.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ip {
    private String ip;
    private String gateway;
    private String mask;

    public Ip(String json) {
        try {
            JSONObject jobject = new JSONObject(json);
            this.ip = jobject.getString("ip");
            this.mask = jobject.getString("mask");
            this.gateway = jobject.getString("gateway");
        } catch (JSONException e) {
            e.printStackTrace();
            this.ip = "";
            this.mask = "";
            this.gateway = "";
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
