package ua.adeptius.myo3.model;



public class ChannelDivanDetails {

    private String image;
    private String name;
    private String description;
    private String availableIn;
    private String availableOn;

    public ChannelDivanDetails(String image, String name, String description, String availableIn, String availableOn) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.availableIn = availableIn;
        this.availableOn = availableOn;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        String description = this.description;
        description = description.replaceAll("<strong>","");
        description = description.replaceAll("</strong>","");
        return description;
    }

    public String getAvailableIn() {
        String available = this.availableIn;
        available = available.replaceAll("Детский","Дитячий");
        available = available.replaceAll("Престижный","Престижний");
        available = available.replaceAll("Стартовый","Стартовий");
        available = available.replaceAll("Оптимальный","Оптимальний");
        return available;
    }

    public String getAvailableOn() {
        String available = this.availableOn;
        available = available.replaceAll("телевизоре","телевізорі");
        available = available.replaceAll("сайте","сайті");
        available = available.replaceAll("планшете / смартфоне","мобільних пристроях");
        return available;
    }

}
