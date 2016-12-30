package ua.adeptius.myo3.model.persons;


import org.json.JSONException;
import org.json.JSONObject;

public class Address {

//    private String strName;
//    private int hId;
//    private int segNo;
//    private int strId;
//    private String hNote;
//    private int cscSegId;
//    private int privat;
//    private String cityName;
//    private int postCode;
    private String addressFlatName;
    private String strNameUa;
    private String hName;
    private String cityNameUa;

    public Address(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            addressFlatName = allInfo.get("addressFlatName").toString();
            strNameUa = allInfo.get("strNameUa").toString();
            hName = allInfo.get("hName").toString();
            cityNameUa = allInfo.get("cityNameUa").toString();
//            cityName = allInfo.get("cityName").toString();
//            hId = Integer.parseInt(allInfo.get("hId").toString());
//            strName = allInfo.get("strName").toString();
//            strId = Integer.parseInt(allInfo.get("strId").toString());
//            segNo = Integer.parseInt(allInfo.get("segNo").toString());
//            hNote = allInfo.get("hNote").toString();
//            cscSegId = Integer.parseInt(allInfo.get("cscSegId").toString());
//            privat = Integer.parseInt(allInfo.get("privat").toString());
//            postCode = Integer.parseInt(allInfo.get("postCode").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        return cityNameUa;
    }
//    public String getStrName() {
//        return strName;
//    }
//
//    public int gethId() {
//        return hId;
//    }
//
//    public int getSegNo() {
//        return segNo;
//    }
//
//    public int getStrId() {
//        return strId;
//    }
//
//    public String gethNote() {
//        return hNote;
//    }
//
//    public int getCscSegId() {
//        return cscSegId;
//    }
//
//    public int getPrivat() {
//        return privat;
//    }
//
//    public String getCityName() {
//        return cityName;
//    }
//
//
//    public int getPostCode() {
//        return postCode;
//    }
}
