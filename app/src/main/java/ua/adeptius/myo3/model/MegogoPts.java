package ua.adeptius.myo3.model;


import org.json.JSONObject;

public class MegogoPts {

    public MegogoPts(String json) {
        try{
            JSONObject j = new JSONObject(json);
            this.id = j.getString("id");
            this.megogoPayTypeId = j.getString("megogoPayTypeId");
            this.month = j.getString("month");
            this.name = j.getString("name");
            this.serType = j.getString("serType");
            this.subscribe = j.getString("subscribe");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String id;
    private String megogoPayTypeId;
    private String month;
    private String name;
    private String serType;
    private String subscribe;


    public String getId() {
        return id;
    }

    public String getMegogoPayTypeId() {
        return megogoPayTypeId;
    }

    public String getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    public String getSerType() {
        return serType;
    }

    public String getSubscribe() {
        return subscribe;
    }

    @Override
    public String toString() {
        return "MegogoPts{" +
                "id='" + id + '\'' +
                ", megogoPayTypeId='" + megogoPayTypeId + '\'' +
                ", month='" + month + '\'' +
                ", name='" + name + '\'' +
                ", serType='" + serType + '\'' +
                ", subscribe='" + subscribe + '\'' +
                '}';
    }
}
