package ua.freenet.cabinet.model;


import org.json.JSONObject;

import ua.freenet.cabinet.dao.DbCache;

public class Operation {

    private String date;
    private double money;
    private String note;


    public Operation(String json) {
        try {
            JSONObject jobject = new JSONObject(json);
            this.date = jobject.getString("date");
            this.note = jobject.getString("note");
            this.money = jobject.getDouble("money");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String getTextSaldo() {
//        String result = String.valueOf(saldo);
//        if (result.contains(".")) {
//            int dot = result.indexOf(".");
//            result = result.substring(0, dot + 2);
//        }
//        return result;
//    }
//
//    public String getTextMoney() {
//        String result = String.valueOf(money);
//        try{
//            int dot = result.indexOf(".");
//            if(result.length()>(dot+2)){
//                result = result.substring(0, dot+3);
//            }
//        }catch (Exception ignored){}
//
//        if (result.contains(".0")) {
//            result = result.substring(0, result.indexOf(".0"));
//        }
//        if (result.contains(".")){
//            int a = result.indexOf(".");
//            if (result.length()>a+2){
//                result = result.substring(0, a+2);
//            }
//        }
//        if (result.equals("-0")){
//            result = "0";
//        }
//        return result;
//    }
//
//    public String getMyNote() {
//        String note = getNote();
//        if (note.startsWith("20")){
//            note = note.substring(note.indexOf(" ")+1);
//        }
//        return note;
//    }

    public String getNote() {
        note = note.replaceAll("\\(Промо 1-аренда stb\\)","Оренда приставки");
        note = note.replaceAll(" - тариф аренды","");
        note = note.replaceAll("Телевидение ","");
        if (note.startsWith("20")) {
            note = note.substring(note.indexOf(" ") + 1);
        }
        if (note.contains("грн")){
            note = note.substring(0, note.indexOf("грн")+3);
        }

        return note;
    }

    public String getDate() {
        return date;
    }

    static void calculateForAbon(Person person){
        person.setCurrent((Math.random()*1000)-500);
        DbCache.person = person;
    }

    public double getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "date='" + date + '\'' +
                ", money=" + money +
                ", note='" + note + '\'' +
                '}';
    }
}
