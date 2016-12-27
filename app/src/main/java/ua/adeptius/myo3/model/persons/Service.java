package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

public class Service {

    public Service(String json) {
        try{
            JSONObject allInfo = new JSONObject(json.trim());
            this.date = allInfo.get("date").toString();
            this.money = Double.parseDouble(allInfo.get("money").toString());
            this.note = allInfo.get("note").toString();
            this.bonus = Boolean.parseBoolean(allInfo.get("bonus").toString());
            if (allInfo.has("s_id")){
                this.s_id = Integer.parseInt(allInfo.get("s_id").toString());
            }else {
                this.p_id = Integer.parseInt(allInfo.get("p_id").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String date;
    private double money;
    private String note;
    private boolean bonus;
    private int s_id;
    private int p_id;


}
