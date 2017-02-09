package ua.freenet.cabinet.model;

public class ChannelDivan {

    public ChannelDivan(String id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    private String id;
    private String name;
    private String iconUrl;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
