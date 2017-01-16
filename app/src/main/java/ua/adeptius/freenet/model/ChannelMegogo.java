package ua.adeptius.freenet.model;

import org.json.JSONObject;

public class ChannelMegogo {

    public ChannelMegogo(String json, String iconUrl) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            this.title = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(jsonObject.getString("title"));
            this.description = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(jsonObject.getString("description"));
            this.iconUrl = "http://" + iconUrl.substring(25, iconUrl.length()-2);

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
    private String title;
    private String iconUrl;
    private String availableIn;

    public String getDescription() {
        return description;
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
}
