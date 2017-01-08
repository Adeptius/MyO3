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
        System.out.println(this);
    }

    public String getImage() {

        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailableIn() {
        return availableIn;
    }

    public String getAvailableOn() {
        return availableOn;
    }

}
