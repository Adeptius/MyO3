package ua.adeptius.myo3.model.persons;


import org.json.JSONObject;

public class AvailableTarif {

    private String id;
    private String name;
//    private String month;
//    private String bundle;

    public AvailableTarif(String json) {
        try {
            JSONObject jobject = new JSONObject(json);
            this.id = jobject.getString("id");
            this.name = jobject.getString("name");
//            this.month = jobject.get("month").toString();
//            this.bundle = jobject.get("bundle").toString();
        } catch (Exception e) {

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getMonth() {
//        return month;
//    }
//
//    public void setMonth(String month) {
//        this.month = month;
//    }
//
//    public String getBundle() {
//        return bundle;
//    }
//
//    public void setBundle(String bundle) {
//        this.bundle = bundle;
//    }
}
