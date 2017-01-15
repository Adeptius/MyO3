package ua.adeptius.myo3.model;


import org.json.JSONObject;

public class MegogoPts {

    private String id;
    private String month;
    private String name;
    private boolean subscribe;
    private String description;

    public MegogoPts(String json) {
        try{
            JSONObject j = new JSONObject(json);
            id = j.getString("id");
            month = j.getString("month");
            name = j.getString("name");
            subscribe = j.getString("subscribe").equals("1");
        }catch (Exception e){
            id = "";
            month = "";
            name = "Помилка";
            description = "Помилка обробки данних. Повідомте, будь-ласка, про це.";
            subscribe = false;
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
            description = "Національні телеканали" +
            "\nІнтерактивні канали MEGOGO" +
                    "\nВідключення всієї реклами на MEGOGO"+
            "\nКолекція кращих фільмів" +
            "\n+Безкоштовно перші 30 днів";

        } else if (name.equals("MEGOGO Максимальная")) {
            name = "Підписка максимальна";
            description = "Національні телеканали" +
                    "\nІнтерактивні канали MEGOGO" +
                    "\nВідключення всієї реклами на MEGOGO"+
                    "\nКолекція кращих фільмів" +
                    "\nЗакордонні канали";

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

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    public boolean isSubscribe() {
        return subscribe;
    }


}
