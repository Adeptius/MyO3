package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

public class ServiceHZ {

    public ServiceHZ(String json) {
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }
}