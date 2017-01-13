package ua.adeptius.myo3.model;



public class Zayava {

    private String name;
    private MailType type;
    private String coment;
    private int price;
    private String priceType;
    private String buttonName;

    public Zayava(String name, MailType type, String coment, int price, String priceType, String buttonName) {
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

    public void setType(MailType type) {
        this.type = type;
    }

    public String getComent() {
        return coment;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
}
