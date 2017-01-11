package ua.adeptius.myo3.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Address {

    //    private String strName;
//    private int hId;
//    private int segNo;
//    private int strId;
//    private String hNote;
//    private int cscSegId;
    //    private String cityName;
//    private int postCode;
    private boolean privat;
    private String addressFlatName;
    private String strNameUa;
    private String hName;
    private String cityNameUa;



    public Address(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            addressFlatName = allInfo.getString("addressFlatName");
            strNameUa = allInfo.getString("strNameUa");
            hName = allInfo.getString("hName");
            cityNameUa = allInfo.getString("cityNameUa");
            privat = allInfo.getString("privat").equals("1");
//            cityName = allInfo.get("cityName").toString();
//            hId = Integer.parseInt(allInfo.get("hId").toString());
//            strName = allInfo.get("strName").toString();
//            strId = Integer.parseInt(allInfo.get("strId").toString());
//            segNo = Integer.parseInt(allInfo.get("segNo").toString());
//            hNote = allInfo.get("hNote").toString();
//            cscSegId = Integer.parseInt(allInfo.get("cscSegId").toString());
//            postCode = Integer.parseInt(allInfo.get("postCode").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isPrivat() {
        return privat;
    }
    public String getAddressFlatName() {
        return addressFlatName;
    }

    public String getStrNameUa() {
        return strNameUa;
    }

    public String gethName() {
        return hName;
    }

    public String getCityNameUa() {
        String cityNameUa = this.cityNameUa;
        cityNameUa = cityNameUa.replaceAll("Дніпропетровськ","Дніпро");
        return cityNameUa;
    }

}
