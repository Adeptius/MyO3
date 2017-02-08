package ua.adeptius.freenet.model;



public class Zayava {

    private String name;
    private MailType type;
    private String coment;
    private int price;
    private String priceType;
    private String buttonName;
    private String headerText;

    Zayava(String name, MailType type, String coment, int price, String priceType, String buttonName, String headerText) {
        this.name = name;
        this.type = type;
        this.coment = coment;
        this.price = price;
        this.priceType = priceType;
        this.buttonName = buttonName;
        this.headerText = headerText;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getName() {
        return name;
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
