package ua.adeptius.myo3.model;



public class Zayava {

    private String name;
    private MailType type;
    private String coment;
    private int price;
    private String priceType;
    private String buttonName;

    Zayava(String name, MailType type, String coment, int price, String priceType, String buttonName) {
        this.name = name;
        this.type = type;
        this.coment = coment;
        this.price = price;
        this.priceType = priceType;
        this.buttonName = buttonName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MailType getType() {
        return type;
    }

    public String getComent() {
        return coment;
    }

    public String getButtonName() {
        return buttonName;
    }

    public int getPrice() {
        return price;
    }

    public String getPriceType() {
        return priceType;
    }

}
