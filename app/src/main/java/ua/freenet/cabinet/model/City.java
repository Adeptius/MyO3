package ua.freenet.cabinet.model;


import java.util.List;

public class City {

    private List<String> support;
    private List<String> abon;
    private List<String> call;
    private String time;
    private String reason;
    private String adress;
    private String coordinates;


    public List<String> getSupport() {
        return support;
    }

    public void setSupport(List<String> support) {
        this.support = support;
    }

    public List<String> getAbon() {
        return abon;
    }

    public void setAbon(List<String> abon) {
        this.abon = abon;
    }

    public List<String> getCall() {
        return call;
    }

    public void setCall(List<String> call) {
        this.call = call;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "City{" +
                "support=" + support +
                ", abon=" + abon +
                ", call=" + call +
                ", time='" + time + '\'' +
                ", reason='" + reason + '\'' +
                ", adress='" + adress + '\'' +
                ", coordinates='" + coordinates + '\'' +
                '}';
    }
}
