package ua.adeptius.myo3.model.persons;


import org.json.JSONException;
import org.json.JSONObject;

public class Phone {

    public Phone(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            id = Integer.parseInt(allInfo.get("id").toString());
            pId = Integer.parseInt(allInfo.get("pId").toString());
            phone = allInfo.get("phone").toString();
            comment = allInfo.get("comment").toString();
            lang = Integer.parseInt(allInfo.get("lang").toString());
            phonems = allInfo.get("phonems").toString();
            valid = Integer.parseInt(allInfo.get("valid").toString());
            modifyDate = allInfo.get("modifyDate").toString();
            smsInform = Integer.parseInt(allInfo.get("smsInform").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int id;
    private int pId;
    private String phone;
    private String comment;
    private int lang;
    private String phonems;
    private int valid;
    private String modifyDate;
    private int smsInform;

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