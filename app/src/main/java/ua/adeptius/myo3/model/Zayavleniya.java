package ua.adeptius.myo3.model;


import ua.adeptius.myo3.utils.Utilits;

public class Zayavleniya {

    public static String getHeader(Person person, String passSer, String passNum, String phoneNum, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("Директору ТОВ «Фрінет»");
        sb.append("\n").append("Фролову В.І.").append("\n");
        sb.append("\n").append("Абонента ").append(person.getLastname())
                .append(" ").append(person.getName())
                .append(" ").append(person.getSurname());
        sb.append("\n").append("паспорт: серія ").append(passSer).append(" № ").append(passNum);
        sb.append("\n").append("номер договору ").append(person.getCard());
        sb.append("\n").append("телефон: ").append(phoneNum);
        sb.append("\n").append("e-mail: ").append(email);
        sb.append("\n");
        sb.append("\n               ЗАЯВА");
        sb.append("\n");


        return sb.toString();
    }

    public static String getFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("\n").append(Utilits.getUkrDateNow());
        sb.append("\n").append("_____________________________________________________");
        sb.append("\n");
        sb.append("\n").append("Заповнюється співробітником компанії:");
        sb.append("\n");
        sb.append("\n").append("Заяву прийняв_________________").append("             «___»____________201__р.");
        sb.append("\n").append("\n").append("Послугу активовано");
        return sb.toString();
    }

    public static String realIP(String startDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("Прошу з ").append(startDate).append(" надати мені реальну IP адресу.");
        sb.append("\n");
        sb.append("\n").append("З вартістю послуги в 30грн/щомісячно ознайомлений(на).");
        return sb.toString();
    }

}
