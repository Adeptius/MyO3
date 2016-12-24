package ua.adeptius.myo3.model.persons;


import org.json.JSONException;
import org.json.JSONObject;

public class Mailing {

    public Mailing(String json) {
        try {
            JSONObject allInfo = new JSONObject(json.trim());
            id = Integer.parseInt(allInfo.get("id").toString());
            title = allInfo.get("title").toString();
            subscribe = Boolean.parseBoolean(allInfo.get("subscribe").toString());
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

    @Override
    public String toString() {
        return "Mailing{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subscribe=" + subscribe +
                '}';
    }
}
