package ua.adeptius.myo3.model;


import org.json.JSONObject;

public class FriendInvite {

    public FriendInvite(String json) {
        try{
            JSONObject jobj = new JSONObject(json);
            this.fio = jobj.getString("FIO");
            this.adress = jobj.getString("address");
            this.status = jobj.getString("status");
            this.phone = jobj.getString("mobilePhone");
            this.date = jobj.getString("date");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String fio;
    private String adress;
    private String status;
    private String phone;
    private String date;



    public String getFio() {
        return fio;
    }

    public String getAdress() {
        return adress;
    }

    public String getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }
}
