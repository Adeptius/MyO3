package ua.freenet.cabinet.model;


import org.json.JSONObject;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.*;

public class Servise {

    private int id;
    private int type;
    private int serviceCost;
    private int costForCustomer;
    private String pay_type_name;
    private boolean is_allow_change;
    private boolean is_allow_suspend;
    private String newName;
    private String dateWillChange;
    private boolean isStopped;
    private boolean isActivatingNow;
    private boolean haveDiscount;
    private int discount;


    private String discountTo;

    public Servise(String json) {
//        Gson allGSon = new Gson();
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            id = allInfo.getInt("id");
            type = allInfo.getInt("type");
            serviceCost = (int) allInfo.getDouble("month");
            pay_type_name = allInfo.getString("pay_type_name");
            if ("!-Приостановлен".equals(pay_type_name)) {
                isStopped = true;
                pay_type_name = "Призупинено";
            }
            is_allow_change = allInfo.getBoolean("is_allow_change");
            is_allow_suspend = allInfo.getBoolean("is_allow_suspend");

            try{
                String s = allInfo.getString("discounts");
                s = s.substring(1, s.length()-1);
                JSONObject disc = new JSONObject(s);
                int discount = disc.getInt("amount");
                String date = disc.getString("eDate");
                date = date.substring(0, date.indexOf(" "));
                this.discount = discount;
                haveDiscount = true;
                discountTo = date;
            }catch (Exception ignored){}

            if (haveDiscount){ // тут вообще можно брать "money" из json
                double del100 = (double) serviceCost / 100;
                double discMultiplier = 100-discount;
                double exactlyCost = del100 * discMultiplier;
                costForCustomer = (int) Math.round(exactlyCost);
            }else {
                costForCustomer = serviceCost;
            }

            // На время теста: беру инфу с биллинга
            costForCustomer = (int) allInfo.getDouble("money");

            try {
                String s = allInfo.getString("chprices");
                s = s.substring(1, s.length() - 1);
                JSONObject details = new JSONObject(s.trim());
                newName = details.getString("newName");
                dateWillChange = details.getString("dchange");
            } catch (Exception ignored) {
                newName = "";
                dateWillChange = "";
            }

            if (!"".equals(dateWillChange)){
                int year = parseInt(dateWillChange.substring(0,4));
                int month = parseInt(dateWillChange.substring(5,7));
                int day = parseInt(dateWillChange.substring(8,10));
                int hour = parseInt(dateWillChange.substring(11,13));
                int minute = parseInt(dateWillChange.substring(14,16));
                @SuppressWarnings("deprecation")
                Date date = new Date(year-1900,month-1,day,hour,minute);
                long timeEnable = date.getTime();
                long timeCurrent = new Date().getTime();
                int minutesToEnable = (int) (timeEnable-timeCurrent)/1000/60;

                if ("Призупинено".equals(pay_type_name) && minutesToEnable < 3 && minutesToEnable > -58 ) {
                    isActivatingNow = true;
                }
            }

            try{
                if (!allInfo.getString("login").equals("") && type==3){
                    pay_type_name = allInfo.getString("login") + "@freenet.com.ua";
                }

            }catch (Exception ignored){}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMyServiceName() {
        String name = getPay_type_name();
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
        if (name.startsWith("!-")) {
            name = name.substring(name.indexOf("!-") + 2);
        }

        try{
            Matcher regexMatcher = Pattern.compile("\\d{1,3}грн").matcher(name);
            regexMatcher.find();
            String s = regexMatcher.group();
            name = name.replaceAll(s,"");
        }catch (Exception e){

        }


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
        name = name.replaceAll("бесплатный", "безкоштовний");
        name = name.replaceAll("Почтовый ящик 30Мб", "Обсяг 30мб");

        name = name.trim();

        return name;
    }

    public String getMyTypeName() {
        if (getType() == 5) return "Доступ до інтернету";
        if (getType() == 7 && getMyServiceName().contains("Гарантований")) return "Сервісні послуги";
        if (getType() == 7) return "Надання адреси";
        if (getType() == 15) return "Телебачення MEGOGO";
        if (getType() == 13 && pay_type_name.contains("аренды")) return "Оренда обладнання";
        if (getType() == 13) return "Телебачення OLL.TV";
        if (getType() == 14) return "Телебачення Divan.TV";
        if (getType() == 9) return "Антивірус";
        if (getType() == 3) return "Поштова скринька";
        return "";
    }

    public String getComent() {
        String coment = "";

        String newNameWithOutDate = "";
        if (newName.startsWith("20")) {
            newNameWithOutDate = newName.substring(newName.indexOf(" ") + 1);
        }

        String date = "";
        if (!"".equals(dateWillChange)) {
            try {
                date = dateWillChange.substring(0, dateWillChange.indexOf(" ")) + " ";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("!-Удалить услугу".equals(newName)){
            coment += "Послугу буде видалено " + date;
        }else if ("!-Приостановлен".equals(newName)){
            coment += "Послугу буде призупинено " + date;
        }else if(isStopped && !"".equals(newName)){
            coment += "Послуга буде відновлена " + date;
        }else if (!"".equals(newName)){
            coment += "Послугу буде змінено " + date +" на "
                    + (!"".equals(newNameWithOutDate)? newNameWithOutDate : newName);
        }
        return coment;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean is_allow_change() {
        return is_allow_change;
    }

    public boolean isActivatingNow() {
        return isActivatingNow;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public String getPay_type_name() {
        return pay_type_name;
    }

    public boolean is_allow_suspend() {
        return is_allow_suspend;
    }

    public boolean isHaveDiscount() {
        return haveDiscount;
    }

    public int getDiscount() {
        return discount;
    }

    public String getDiscountTo() {
        return discountTo;
    }

    public int getCostForCustomer() {
        return costForCustomer;
    }

    public String getNewName() {
        return newName;
    }

    public String getDateWillChange() {
        return dateWillChange;
    }
}
