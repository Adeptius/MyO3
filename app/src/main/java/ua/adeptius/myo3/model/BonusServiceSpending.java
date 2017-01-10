package ua.adeptius.myo3.model;


import org.json.JSONObject;

public class BonusServiceSpending {

    public BonusServiceSpending(String json) {
        try{
            JSONObject j = new JSONObject(json);
            this.bonus = j.getInt("bonus");
            this.date = j.getString("date");
            this.money = j.getInt("money");
            this.note = j.getString("note");
            this.s_id = j.getString("s_id");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int bonus;
    private String date;
    private int money;
    private String note;
    private String s_id;

    public int getBonus() {
        return bonus;
    }

    public String getDate() {
        return date;
    }

    public int getMoney() {
        return money;
    }

    public String getNote() {
        return note;
    }

    public String getS_id() {
        return s_id;
    }
}
