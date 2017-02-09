package ua.freenet.cabinet.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Phone {

    private int id;
    private int pId;
    private String phone;
    private String comment;
    private int lang;
    private String phonems;
    private int valid;
    private String modifyDate;
    private int smsInform;

    public Phone(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            id = allInfo.getInt("id");
            pId = allInfo.getInt("pId");
            phone = allInfo.getString("phone");
            comment = allInfo.getString("comment");
            lang = allInfo.getInt("lang");
            phonems = allInfo.getString("phonems");
            valid = allInfo.getInt("valid");
            modifyDate = allInfo.getString("modifyDate");
            smsInform = allInfo.getInt("smsInform");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int getId() {
        return id;
    }

    public int getpId() {
        return pId;
    }

    public String getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }

    public int getLang() {
        return lang;
    }

    public String getPhonems() {
        return phonems;
    }

    public int getValid() {
        return valid;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public int getSmsInform() {
        return smsInform;
    }

}