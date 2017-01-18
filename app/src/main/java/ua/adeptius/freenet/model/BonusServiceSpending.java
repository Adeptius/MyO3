package ua.adeptius.freenet.model;


import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BonusServiceSpending {

    private int bonus;
    private String date;
    private int money;
    private String note;
    private String s_id;

    public BonusServiceSpending(String json) {
        try{
            JSONObject j = new JSONObject(json);
            bonus = j.getInt("bonus");
            date = j.getString("date");
            money = j.getInt("money");
            note = j.getString("note");
            s_id = j.getString("s_id");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getBonus() {
        return bonus;
    }

    public String getDate() {
        return date;
    }

    public int getMoney() {
        return money;
    }


    // Copy/pasted from service
    public String getNote() {
        String name = note;
        if (name.contains("Телевидение OLL.TV")){
            name = name.replaceAll("Телевидение OLL.TV \\(","Тариф ");
            if (name.contains("грн.")) {
                name = name.substring(0, name.lastIndexOf("-")-2);
            }
        }
        if (name.contains("Телевидение DIVAN.TV")){
            name = name.replaceAll("Телевидение DIVAN.TV \\(","Тариф ");
            if (name.contains("грн.")) {
                name = name.substring(0, name.lastIndexOf(")")-1);
            }
        }
        if (name.contains("грн.")) {
            name = name.substring(0, name.lastIndexOf("-"));
        }
        if (name.contains("-") && name.contains("грн")) {
            name = name.substring(0, name.lastIndexOf("-"));
        }
        if (name.startsWith("20")) {
            name = name.substring(name.indexOf(" ") + 1);
        }

        try{
            Matcher regexMatcher = Pattern.compile("\\d{1,3}грн").matcher(name);
            if (regexMatcher.find()){
                String s = regexMatcher.group();
                name = name.replaceAll(s,"");
            }
        }catch (Exception ignored){}

        name = name.replaceAll("Мбит", "Мбіт");
        name = name.replaceAll("Безлимитный", "Безліміт");
        name = name.replaceAll("Реальный", "Реальний");
        name = name.replaceAll("Телевидение MEGOGO.NET \\(", "Тариф ");
        name = name.replaceAll("оптимальный\\)", "оптимальний");
        name = name.replaceAll("Оптимальный\\)", "оптимальний");
        name = name.replaceAll("\\(Промо 1-аренда stb\\)", "Медіаплеєр MAG");
        name = name.replaceAll("Детский\\)", "Дитячий");
        name = name.replaceAll("звезды", "зірки");
        name = name.replaceAll("Гарантированный сервис", "Гарантований сервіс");
        name = name.replaceAll("MEGOGO Легкая", "Підписка легка");
        name = name.replaceAll("MEGOGO Оптимальная", "Підписка оптимальна");
        name = name.replaceAll("MEGOGO Максимальная", "Підписка максимальна");
        name = name.replaceAll("MEGOGO Viasat Premium", "Пакет Viasat Premium");
        name = name.replaceAll("MEGOGO FilmBox", "Пакет FilmBox");
        return name;
    }

    public String getS_id() {
        return s_id;
    }
}