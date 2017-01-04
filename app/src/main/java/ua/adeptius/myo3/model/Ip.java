package ua.adeptius.myo3.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ip {
    private String ip;
    private String gateway;
    private String mask;
    private String dns1 = "193.24.25.1";
    private String dns2 = "193.24.25.250";

    public Ip(String json) {
        try {
            JSONObject jobject = new JSONObject(json);
            this.setIp(jobject.get("ip").toString());
            this.setMask(jobject.get("mask").toString());
            this.setGateway(jobject.get("gateway").toString());
        } catch (JSONException e) {
            e.printStackTrace();
            this.setIp("");
            this.setMask("");
            this.setGateway("");
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public void setMask(String mask) {
        this.mask = mask;
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
        return dns1;
    }

    public String getDns2() {
        return dns2;
    }
}
