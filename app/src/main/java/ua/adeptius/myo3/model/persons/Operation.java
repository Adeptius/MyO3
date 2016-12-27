package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

public class Operation {


    public Operation(String json) {
        try {
            JSONObject jobject = new JSONObject(json);
            this.date = jobject.get("date").toString();
            this.bonus = Boolean.parseBoolean(jobject.get("bonus").toString());
            this.note = jobject.get("note").toString();
            this.money = Double.parseDouble(jobject.get("money").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String date;
    private boolean bonus;
    private String note;
    private double money;
    private double saldo;

    public String getTextSaldo() {
        String result = String.valueOf(saldo);
        if (result.contains(".")) {
            int dot = result.indexOf(".");
            result = result.substring(0, dot + 2);
        }
        return result;
    }

    public String getTextMoney() {
        String result = String.valueOf(money);
        try{
            int dot = result.indexOf(".");
            if(result.length()>(dot+2)){
                result = result.substring(0, dot+3);
            }
        }catch (Exception ignored){}

        if (result.contains(".0")) {
            result = result.replaceAll("\\.0","");
        }
        return result;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "date='" + date + '\'' +
                ", note='" + note + '\'' +
                ", money=" + money +
                ", saldo=" + saldo +
                '}';
    }
}
