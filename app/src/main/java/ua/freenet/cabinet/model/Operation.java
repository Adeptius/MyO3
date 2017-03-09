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
