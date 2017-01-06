package ua.adeptius.myo3.model;


import org.json.JSONObject;

public class MegogoPts {

    private String id;
    private String megogoPayTypeId;
    private String month;
    private String name;
    private String serType;
    private boolean subscribe;
    private String description;

    public MegogoPts(String json) {
        try{
            JSONObject j = new JSONObject(json);
            setId(j.getString("id"));
            setMegogoPayTypeId(j.getString("megogoPayTypeId"));
            setMonth(j.getString("month"));
            setName(j.getString("name"));
            setSerType(j.getString("serType"));
            setSubscribe(j.getString("subscribe").equals("1"));
        }catch (Exception e){
            setId("");
            setMegogoPayTypeId("");
            setMonth("");
            setName("Помилка");
            setDescription("Помилка обробки данних. Повідомте, будь-ласка, про це.");
            setSerType("");
            setSubscribe(false);
        }
    }

    public void setName(String name) {
        if (name.equals("MEGOGO Легкая")) {
            name = "Підписка легка";
            description = "Національні телеканали" +
                    "\nІнтерактивні канали MEGOGO" +
                    "\nВідключення всієї реклами на MEGOGO";

        } else if (name.equals("MEGOGO Оптимальная")) {
            name = "Підписка оптимальна";
            description = "Всі канали \"Легка\"\n + Колекція кращих фільмів\n++Безкоштовно перші 30 днів";

        } else if (name.equals("MEGOGO Максимальная")) {
            name = "Підписка максимальна";
            description = "Всі канали \"Легка\" та \"Оптимальна\"\n + Закордонні телеканали";

        } else if (name.equals("MEGOGO Viasat Premium")) {
            name = "Додатковий пакет Viasat Premium";
            description = "2 телеканали TV1000 з гарячими хітами кінопрокату від найкрупніших голлівудських кіностудій.";

        } else if (name.equals("MEGOGO FilmBox")) {
            name = "Додатковий пакет FilmBox";
            description = "Кіно та серіали, мода, музика, бойові мистецтва та " +
                    "познавальні передачі (3 канали в HD якості).";
        }
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setMegogoPayTypeId(String megogoPayTypeId) {
        this.megogoPayTypeId = megogoPayTypeId;
    }

    public void setMonth(String month) {
        this.month = month;
    }



    public void setSerType(String serType) {
        this.serType = serType;
    }


    public String getId() {
        return id;
    }

    public String getMegogoPayTypeId() {
        return megogoPayTypeId;
    }

    public String getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    public String getSerType() {
        return serType;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    @Override
    public String toString() {
        return "MegogoPts{" +
                "id='" + id + '\'' +
                ", megogoPayTypeId='" + megogoPayTypeId + '\'' +
                ", month='" + month + '\'' +
                ", name='" + name + '\'' +
                ", serType='" + serType + '\'' +
                ", subscribe=" + subscribe +
                ", description='" + description + '\'' +
                '}';
    }
}
