package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

public class Operation {

    private String date;
    private double money;
    private String note;
    private double saldo;
    //    private boolean bonus;


    public Operation(String json) {
        try {
            JSONObject jobject = new JSONObject(json);
            this.date = jobject.getString("date");
            this.note = jobject.getString("note");
            this.money = jobject.getDouble("money");
//            this.bonus = jobject.getBoolean("bonus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

    public String getMyNote() {
        String note = getNote();
        if (note.startsWith("20")){
            note = note.substring(note.indexOf(" ")+1);
        }
        return note;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getMoney() {
        return money;
    }

}
