package ua.freenet.cabinet.model;


import org.json.JSONObject;

public class MegogoPts {

    private String id;
    private String month;
    private String name;
    private String originalName;
    private boolean subscribe;
    private String description;

    public MegogoPts(String json) {
        try{
            JSONObject j = new JSONObject(json);
            id = j.getString("id");
            month = j.getString("month");
            setName(j.getString("name"));
            subscribe = j.getString("subscribe").equals("1");
        }catch (Exception e){
            id = "";
            month = "";
            name = "Помилка";
            description = "Помилка обробки данних. Повідомте, будь-ласка, про це.";
            subscribe = false;
        }
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setName(String name) {
        originalName = name;
        if (name.equals("MEGOGO Легкая")) {
            name = "Пакет легкий";
            description = "Національні телеканали" +
                    "\nІнтерактивні канали MEGOGO" +
                    "\nВідключення всієї реклами на MEGOGO";

        } else if (name.equals("MEGOGO Оптимальная")) {
            name = "Пакет оптимальний";
            description = "Національні телеканали" +
            "\nІнтерактивні канали MEGOGO" +
                    "\nВідключення всієї реклами на MEGOGO"+
            "\nКолекція кращих фільмів" +
            "\n+Безкоштовно перші 30 днів";

        } else if (name.equals("MEGOGO Максимальная")) {
            name = "Пакет максимальний";
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
                    "пізнавальні передачі (3 канали в HD якості).";
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
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
