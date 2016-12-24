package ua.adeptius.myo3.model.persons;


import org.json.JSONException;
import org.json.JSONObject;

public class Address {

    public Address(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            addressFlatName = allInfo.get("addressFlatName").toString();
            strName = allInfo.get("strName").toString();
            strNameUa = allInfo.get("strNameUa").toString();
            hId = Integer.parseInt(allInfo.get("hId").toString());
            segNo = Integer.parseInt(allInfo.get("segNo").toString());
            strId = Integer.parseInt(allInfo.get("strId").toString());
            hName = allInfo.get("hName").toString();
            hNote = allInfo.get("hNote").toString();
            cscSegId = Integer.parseInt(allInfo.get("cscSegId").toString());
            privat = Integer.parseInt(allInfo.get("privat").toString());
            cityName = allInfo.get("cityName").toString();
            cityNameUa = allInfo.get("cityNameUa").toString();
            postCode = Integer.parseInt(allInfo.get("postCode").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String addressFlatName;
    private String strName;
    private String strNameUa;
    private int hId;
    private int segNo;
    private int strId;
    private String hName;
    private String hNote;
    private int cscSegId;
    private int privat;
    private String cityName;
    private String cityNameUa;
    private int postCode;

    public String getAddressFlatName() {
        return addressFlatName;
    }

    public String getStrName() {
        return strName;
    }

    public String getStrNameUa() {
        return strNameUa;
    }

    public int gethId() {
        return hId;
    }

    public int getSegNo() {
        return segNo;
    }

    public int getStrId() {
        return strId;
    }

    public String gethName() {
        return hName;
    }

    public String gethNote() {
        return hNote;
    }

    public int getCscSegId() {
        return cscSegId;
    }

    public int getPrivat() {
        return privat;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityNameUa() {
        return cityNameUa;
    }

    public int getPostCode() {
        return postCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressFlatName='" + addressFlatName + '\'' +
                ", strName='" + strName + '\'' +
                ", strNameUa='" + strNameUa + '\'' +
                ", hId=" + hId +
                ", segNo=" + segNo +
                ", strId=" + strId +
                ", hName='" + hName + '\'' +
                ", hNote='" + hNote + '\'' +
                ", cscSegId=" + cscSegId +
                ", privat=" + privat +
                ", cityName='" + cityName + '\'' +
                ", cityNameUa='" + cityNameUa + '\'' +
                ", postCode=" + postCode +
                '}';
    }
}
