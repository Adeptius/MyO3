package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

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

    public Servise(String json){
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
            this.login = allInfo.get("login").toString();
            this.domen = allInfo.get("domen").toString();
            this.end_date = allInfo.get("end_date").toString();
            this.chprices = allInfo.get("chprices").toString();
            this.is_allow_change = Boolean.parseBoolean(allInfo.get("is_allow_change").toString());
            this.is_allow_suspend = Boolean.parseBoolean(allInfo.get("is_allow_suspend").toString());
            this.discounts = allInfo.get("discounts").toString();
        }catch (Exception e){
            e.printStackTrace();
        }


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


    public String getMyServiceName() {
        String name = getPay_type_name();
        if (name.contains("грн.")){
            name = name.substring(0,name.indexOf("-"));
        }
        if (name.contains("грн")){
            name = name.substring(0,name.indexOf("-"));
        }
        name = name.replaceAll("Мбит","Мбіт");
        name = name.replaceAll("Реальный","Реальний");
        return name;
    }

    public String getMyTypeName() {
        if (getType() == 5) return "Доступ до інтернету";
        if (getType() == 7) return "Надання адреси";
        return typeName;
    }
}
