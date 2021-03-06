package ua.freenet.cabinet.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Address {

    private boolean privat;
    private String addressFlatName;
    private String strNameUa;
    private String hName;
    private String cityNameUa;

    Address(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            addressFlatName = allInfo.getString("addressFlatName");
            strNameUa = allInfo.getString("strNameUa");
            hName = allInfo.getString("hName");
            cityNameUa = allInfo.getString("cityNameUa");
            privat = allInfo.getString("privat").equals("1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isPrivat() {
        return privat;
    }

    public String getAddressFlatName() {
        if (addressFlatName==null){
            return "";
        }
        return addressFlatName;
    }

    public String getStrNameUa() {
        if (strNameUa==null){
            return "";
        }
        return strNameUa;
    }

    public String gethName() {
        if (hName==null){
            return "";
        }
        return hName;
    }

    public String getCityNameUa() {
        String cityNameUa = this.cityNameUa;
        if (cityNameUa==null){
            return "";
        }
        cityNameUa = cityNameUa.replaceAll("Дніпропетровськ","Дніпро");
        return cityNameUa;
    }
}
