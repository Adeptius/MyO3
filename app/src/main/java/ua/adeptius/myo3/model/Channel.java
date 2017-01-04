package ua.adeptius.myo3.model;

import org.json.JSONObject;

public class Channel {

    public Channel(String json, String iconUrl, String category) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            this.genre = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(jsonObject.getString("genre"));
            this.title = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(jsonObject.getString("title"));
            this.description = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(jsonObject.getString("description"));
            this.iconUrl = iconUrl.substring(25, iconUrl.length()-2);
            this.category = category;

            String available = "";
            try {
                available = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(jsonObject.getString("availableReason"));
            }catch (Exception ignored){}
            if (available.equals("")){
                this.availableIn = "Легка";
            }else if (available.contains("Оптимальная")){
                this.availableIn = "Оптимальна";
            }else if (available.contains("Максимальная")){
                this.availableIn = "Максимальна";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String description;
    private String genre;
    private String title;
    private String iconUrl;
    private String availableIn;
    private String category;

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getAvailableIn() {
        return availableIn;
    }

    public String getCategory() {
        return category;
    }
}
