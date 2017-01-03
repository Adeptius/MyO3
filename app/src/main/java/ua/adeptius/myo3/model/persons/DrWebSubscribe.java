package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrWebSubscribe {

    private String login;
    private String sid;
    private String payType;
    private String newprice;
    private String dchange;
    private String name;
    private String android_url;
    private String url;

    public DrWebSubscribe(String json) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            this.login = jsonObject.getString("login");
            this.sid = jsonObject.getString("sid");
            this.payType = jsonObject.getString("payType");
            this.newprice = jsonObject.getString("newprice");
            this.dchange = jsonObject.getString("dchange");
            this.name = jsonObject.getString("name");
            this.url = jsonObject.getString("url");
            this.android_url = jsonObject.getString("android_url");
            if (android_url.equals("")) android_url = null;
            if (dchange.equals("null")) dchange = null;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(this);
    }


    public String getLogin() {
        return login;
    }

    public String getSid() {
        return sid;
    }

    public String getPayType() {
        return payType;
    }

    public String getNewprice() {
        return newprice;
    }

    public String getDchange() {
        return dchange;
    }

    public String getName() {
        return name;
    }

    public String getMyName(){
        String name = this.name;
        try{
            Matcher regexMatcher = Pattern.compile("[ ]?[-]?[ ]?\\d{1,3}[ ]?грн[.]?").matcher(name);
            regexMatcher.find();
            name = name.replaceAll(regexMatcher.group(), "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return name;
    }

    public String getMyCost(){
        String cost = this.name;
        try{
            Matcher regexMatcher = Pattern.compile("\\d{1,3}").matcher(cost);
            regexMatcher.find();
            cost = regexMatcher.group();
        }catch (Exception e) {
        e.printStackTrace();
        }
        return cost;
    }


    @Override
    public String toString() {
        return "DrWebSubscribe{" +
                "login='" + login + '\'' +
                ", sid='" + sid + '\'' +
                ", payType='" + payType + '\'' +
                ", newprice='" + newprice + '\'' +
                ", dchange='" + dchange + '\'' +
                ", name='" + name + '\'' +
                ", android_url='" + android_url + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getAndroid_url() {
        return android_url;
    }

    public String getUrl() {
        return url;
    }
}
