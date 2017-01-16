package ua.adeptius.freenet.model;



public enum MailType {
    REAL_IP("Видача реальної IP адреси.", "support@o3.ua"),
    DISABLE_REAL_IP("Відключення реальної IP адреси.", "support@o3.ua"),
    DOP_IP("Видача додаткової IP адреси.", "support@o3.ua"),
    CREATE_EMAIL("Створення поштової скриньки.", "support@o3.ua"),
//    SMTP("Доступ до SMTP серверу", "support@o3.ua"),
    CHANGE_IP("Зміна IP адреси.", "support@o3.ua"),
//    COMPENSATION("Компенсація абонентської плати.", "support@o3.ua"),
//    CHANGE_DEAL("Переоформлення угоди.", "abon_otdel@o3.ua"),
//    WRONG_PAY("Помилкова проплата.", "abon_otdel@o3.ua"),
    CHANGE_TARIF("Зміна тарифу.", "abon_otdel@o3.ua");


    MailType(String subject, String email) {
        this.subject = subject;
        this.email = email;
    }

    private String subject;
    private String email;

    public String getSubject() {
        return subject;
    }

    public String getEmail() {
        return email;
    }
}
