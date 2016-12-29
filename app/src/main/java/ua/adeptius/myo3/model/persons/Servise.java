package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Servise {

    private String typeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOld() {
        return old;
    }

    public void setOld(int old) {
        this.old = old;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type_name() {
        return pay_type_name;
    }

    public void setPay_type_name(String pay_type_name) {
        this.pay_type_name = pay_type_name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDomen() {
        return domen;
    }

    public void setDomen(String domen) {
        this.domen = domen;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getChprices() {
        return chprices;
    }

    public void setChprices(String chprices) {
        this.chprices = chprices;
    }

    public boolean is_allow_change() {
        return is_allow_change;
    }

    public void setIs_allow_change(boolean is_allow_change) {
        this.is_allow_change = is_allow_change;
    }

    public boolean is_allow_suspend() {
        return is_allow_suspend;
    }

    public void setIs_allow_suspend(boolean is_allow_suspend) {
        this.is_allow_suspend = is_allow_suspend;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public Servise(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            this.id = Integer.parseInt(allInfo.get("id").toString());
            this.old = Integer.parseInt(allInfo.get("old").toString());
            this.sid = Integer.parseInt(allInfo.get("sid").toString());
            this.type = Integer.parseInt(allInfo.get("type").toString());
            this.money = Double.parseDouble(allInfo.get("money").toString());
            this.start_date = allInfo.get("start_date").toString();
            this.month = Integer.parseInt(allInfo.get("month").toString());
            this.pay_type = Integer.parseInt(allInfo.get("pay_type").toString());
            this.pay_type_name = allInfo.get("pay_type_name").toString();
            if ("!-Приостановлен".equals(pay_type_name)) {
                isStopped = true;
                this.pay_type_name = "Призупинено";
            }
            this.login = allInfo.get("login").toString();
            this.domen = allInfo.get("domen").toString();
            this.end_date = allInfo.get("end_date").toString();
            this.chprices = allInfo.get("chprices").toString();
            this.is_allow_change = Boolean.parseBoolean(allInfo.get("is_allow_change").toString());
            this.is_allow_suspend = Boolean.parseBoolean(allInfo.get("is_allow_suspend").toString());
            this.discounts = allInfo.get("discounts").toString();

            try {
                String s = allInfo.get("chprices").toString();
                s = s.substring(1, s.length() - 1);
                JSONObject details = new JSONObject(s.trim());
                this.newName = details.get("newName").toString();
                this.dateWillChange = details.get("dchange").toString();
            } catch (Exception ignored) {
                this.newName = "";
                this.dateWillChange = "";
            }

            if (!"".equals(dateWillChange)){
                int year = Integer.parseInt(dateWillChange.substring(0,4));
                int month = Integer.parseInt(dateWillChange.substring(5,7));
                int day = Integer.parseInt(dateWillChange.substring(8,10));
                int hour = Integer.parseInt(dateWillChange.substring(11,13));
                int minute = Integer.parseInt(dateWillChange.substring(14,16));
                Date date = new Date(year-1900,month-1,day,hour,minute);
                System.out.println(date);
                long timeEnable = date.getTime();
                long timeCurrent = new Date().getTime();
                int minutesToEnable = (int) (timeEnable-timeCurrent)/1000/60;
                System.out.println("timeEnable "+timeEnable);
                System.out.println("timeCurrent "+timeCurrent);
                System.out.println("minutesToEnable "+minutesToEnable);

                System.out.println(pay_type_name);
                System.out.println("условие" + "!-Приостановлен".equals(pay_type_name));
                if ("Призупинено".equals(pay_type_name) && minutesToEnable < 3 && minutesToEnable > -58 ) {
                    isActivatingNow = true;
                    System.out.println(isActivatingNow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActivatingNow() {
        return isActivatingNow;
    }

    public void setActivatingNow(boolean activatingNow) {
        isActivatingNow = activatingNow;
    }

    private int id;
    private int old;
    private int sid;
    private int type;
    private double money;
    private String start_date;
    private int month;
    private int pay_type;
    private String pay_type_name;
    private String login;
    private String domen;
    private String end_date;
    private String chprices;
    private boolean is_allow_change;
    private boolean is_allow_suspend;
    private String discounts;
    private String newName;
    private String dateWillChange;
    private boolean isStopped;
    private boolean isActivatingNow;

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public String getMyServiceName() {
        String name = getPay_type_name();
        if (name.contains("грн.")) {
            name = name.substring(0, name.lastIndexOf("-"));
        }
        if (name.contains("грн")) {
            name = name.substring(0, name.lastIndexOf("-"));
        }
        if (name.startsWith("20")) {
            name = name.substring(name.indexOf(" ") + 1);
        }

        name = name.replaceAll("Мбит", "Мбіт");
        name = name.replaceAll("Безлимитный", "Безліміт");
        name = name.replaceAll("Реальный", "Реальний");
        name = name.replaceAll("Телевидение MEGOGO.NET \\(", "Тариф ");
        name = name.replaceAll("оптимальный\\)", "оптимальний");
        return name;
    }

    public String getMyTypeName() {
        if (getType() == 5) return "Доступ до інтернету";
        if (getType() == 7) return "Надання адреси";
        if (getType() == 15) return "Телебачення MEGOGO";
        return typeName;
    }

    public String getComent() {
        String coment = "";

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
            coment += "Послугу буде змінено на " + newName;
        }
        return coment;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getDateWillChange() {
        return dateWillChange;
    }

    public void setDateWillChange(String dateWillChange) {
        this.dateWillChange = dateWillChange;
    }


}
