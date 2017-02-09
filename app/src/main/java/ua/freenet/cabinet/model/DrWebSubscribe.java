package ua.freenet.cabinet.model;


import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrWebSubscribe {

    private String sid;
    private String dchange;
    private String name;
    private String android_url;
    private String url;

    public DrWebSubscribe(String json) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            this.sid = jsonObject.getString("sid");
            this.dchange = jsonObject.getString("dchange");
            this.name = jsonObject.getString("name");
            this.url = jsonObject.getString("url");
            this.android_url = jsonObject.getString("android_url");
            if (android_url.equals("")) android_url = null;
            if (dchange.equals("null")) dchange = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getSid() {
        return sid;
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

    public String getAndroid_url() {
        return android_url;
    }

    public String getUrl() {
        return url;
    }
}
