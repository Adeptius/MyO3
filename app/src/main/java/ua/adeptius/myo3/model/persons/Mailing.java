package ua.adeptius.myo3.model.persons;


import org.json.JSONException;
import org.json.JSONObject;

public class Mailing {

    public Mailing(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            id = allInfo.getInt("id");
            title = allInfo.getString("title");
            subscribe = allInfo.getBoolean("subscribe");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    private int id;
    private String title;
    private boolean subscribe;

}
