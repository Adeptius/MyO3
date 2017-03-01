package ua.freenet.cabinet.model;


import java.util.ArrayList;
import java.util.List;

import ua.freenet.cabinet.utils.Utilits;

public class Zayavleniya {

    public static List<Zayava> getAllZayava() {
        List<Zayava> list = new ArrayList<>();
        list.add(new Zayava(
                "Додаткова IP адреса",
                MailType.DOP_IP,
                "Для організації роботи серверів паралельно з домашньою лінією. Або для підключення додаткового ПК без роутера(потрібен свіч).",
                30,
                "щомісячно",
                "Замовити",
                "Коли надати адресу?"
        ));

        list.add(new Zayava(
                "Реальна IP адреса",
                MailType.REAL_IP,
                "Для серверів, VPN, відеонагляду та PlayStation Network.",
                30,
                "щомісячно",
                "Замовити",
                "Коли надати адресу?"
        ));

        list.add(new Zayava(
                "Відключення реальної IP-адреси",
                MailType.DISABLE_REAL_IP,
                "У випадку, якщо вона вам більше не потрібна.",
                0,
                "Безкоштовно",
                "Відключити",
                "Вкажіть дату відключення"
        ));

        list.add(new Zayava(
                "Створення поштової скриньки",
                MailType.CREATE_EMAIL,
                "Поштова скринька на @freenet.com.ua. Має велике обмеження: обсяг 30 мегабайтів. На випадок, якщо вона вам реально потрібна. В інших випадках краще google mail.",
                0,
                "Безкоштовно",
                "Замовити",
                ""
        ));
        list.add(new Zayava(
                "Зміна IP адреси",
                MailType.CHANGE_IP,
                "Зміна IP адреси на випадок, якщо така необхідна.",
                90,
                "одноразово",
                "Замовити",
                "Вкажіть дату зміни"
        ));

        list.add(new Zayava(
                "Зміна тарифного пакету",
                MailType.CHANGE_TARIF,
                "На деякі акційні тарифи можно перейти тільки по заяві.",
                0,
                "Залежить від умов",
                "Замовити",
                "Вкажіть дату зміни"
        ));
        list.add(new Zayava(
                "Переоформлення угоди",
                MailType.CHANGE_DEAL,
                "Якщо ви переїзджаєте на нову адресу, і там також підключились до нашого інтернету - ми вам перенесемо ваш рахунок на нову угоду.",
                0,
                "Безкоштовно",
                "Відправити",
                "Вкажіть дату переоформлення"
        ));
        list.add(new Zayava(
                "Помилкова проплата",
                MailType.WRONG_PAY,
                "Ми допоможем вам повернути ваші кошти.",
                0,
                "Безкоштовно",
                "Відправити",
                "Вкажіть дату помилкової проплати"
        ));
        list.add(new Zayava(
                "Призупинення послуг",
                MailType.STOP_TARIF,
                "Призупинка, якщо вам недоступно це в розділі підключених послуг.",
                0,
                "Безкоштовно",
                "Відправити",
                "Вкажіть початкову дату призупинки"
        ));
        return list;
    }


    /**
     * Задание шапки
     */
    public static String getHeader(Person person, String passSer, String passNum, String phoneNum, String email) {
        return "Директору ТОВ «Фрінет»" +
                "\n" + "Фролову В.І." + "\n" +
                "\n" + "Абонента " + person.getLastname() +
                " " + person.getName() +
                " " + person.getSurname() +
                "\n" + "паспорт: серія " + passSer + " № " + passNum +
                "\n" + "номер договору " + person.getCard() +
                "\n" + "телефон: " + phoneNum +
                "\n" + "e-mail: " + email +
                "\n" +
                "\n                         ЗАЯВА";
    }

    public static String getFooter() {
        return  "\n" +
                "\n" + "\n" + Utilits.getUkrDateNow() +
                "\n" + "_____________________________________________________" +
                "\n" +
                "\n" + "Заповнюється співробітником компанії:" +
                "\n" +
                "\n" + "Заяву прийняв_________________" + "             «___»____________201__р." +
                "\n" + "\n" + "Послугу активовано";
    }

    public static String realIP(String startDate, Zayava zayava) {
        return  "\n" +
                "\n" + "Прошу з " + startDate + " надати мені реальну IP адресу." +
                "\n" +
                "\n" + "З вартістю послуги в " + zayava.getPrice() + "грн/" + zayava.getPriceType() + " ознайомлений(на).";
    }


    public static String dopIP(String startDate, Zayava zayava) {
        return  "\n" +
                "\n" + "Прошу з " + startDate + " надати мені додаткову IP адресу." +
                "\n" +
                "\n" + "З вартістю послуги в " + zayava.getPrice() + "грн/" + zayava.getPriceType() + " ознайомлений(на).";
    }

    public static String realIPOff(String startDate) {
        return  "\n" +
                "\n" + "Прошу з " + startDate + " відключити  мені реальну IP адресу." +
                "\n";
    }

    public static String changeIP(String startDate, Zayava zayava) {
        return  "\n" +
                "\n" + "Прошу з " + startDate + " змінити мою IP адресу." +
                "\n" +
                "\n" + "З вартістю послуги в " + zayava.getPrice() + "грн/" + zayava.getPriceType() + " ознайомлений(на).";
    }


    public static String createEmail(String login, String password) {
        return  "\n" +
                "\n" + "Прошу з " + Utilits.getUkrDateNow() + " надати електронну поштову скриньку" +
                "\n" +
                "\n" + "Логін " + login + "@freenet.com.ua пароль " + password;

    }

    public static String changeTarif(String startDate, String currentTarif, String newTarif) {
        return  "\n" +
                "\n" + "Прошу з " + startDate + " змінити тарифний пакет " +
                "\nЗ " + currentTarif +
                "\nНа " + newTarif +
                "\n" +
                "\n" +
                "\nЗ умовами надання послуги «Зміна тарифного пакету» ознайомлений/на." +
                "\n" +
                "\n" + Utilits.getUkrDateNow() +
                "\n" +
                "\n" + "_____________________________________________________" +
                "\n" +
                "\n" + "Заповнюється співробітником компанії:" +
                "\n" +
                "\n" + "Заяву прийняв_________________" + "             «___»____________201__р." +
                "\n" +
                "\n" + "Пакет змінено" +
                "\n" +
                "\n" + "Виставлено в чергу на зміну пакету";
    }


    public static String changeDial(String date, String dogovor, String newAdress, String newDogovor, int age) {
        return  "\n" +
                "\n" + "Прошу з " + date + " переоформити договір № " + dogovor +
                " в зв’язку з переїздом на іншу адресу:\n" +
                newAdress +
                "\n" +
                "\nПозитивний залишок на рахунку перенести на договір " + newDogovor +
                "\n" +
                "\nЗберегти історію користування послугами ООО “Фрінет” за " + age + " місяців" +
                "\n" +
                "\n" +
                "\n" + Utilits.getUkrDateNow() +
                "\n" +
                "\n" + "_____________________________________________________" +
                "\n" +
                "\n" + "Заповнюється співробітником компанії:" +
                "\n" +
                "\n" + "Заяву прийняв_________________" + "             «___»____________201__р." +
                "\n" +
                "\n" + "Договір закрито" +
                "\n" +
                "\n" + "Залишок перенесено";
    }

    public static String wrongPay(String date, String wrongPayedSum, String wrongDogovor) {
        return  "\n" +
                "\n" + "Я помилково сплатив(ла) " + date +
                "\nсуму " + wrongPayedSum + " грн на договір № " + wrongDogovor +
                "\n" +
                "\nПрошу перенести цю сплату на мій договір." +
                "\n" +
                "\nКопія квитанції додається." +
                "\n" +
                "\n" +
                "\n" + Utilits.getUkrDateNow() +
                "\n" +
                "\n" + "_____________________________________________________" +
                "\n" +
                "\n" + "Заповнюється співробітником компанії:" +
                "\n" +
                "\n" + "Заяву прийняв_________________" + "             «___»____________201__р.";
    }

    public static String stopInternet(String startDate, String endDate, String dog) {
        return  "\n" +
                "\n" + "Прошу призупинити надання послуг доступу до Інтернету за договором " + dog +
                "\nЗ " + startDate + " по " + endDate +
                "\n" +
                "\n" +
                "\n" +
                "\n" + Utilits.getUkrDateNow() +
                "\n" +
                "\n" + "_____________________________________________________" +
                "\n" +
                "\n" + "Заповнюється співробітником компанії:" +
                "\n" +
                "\n" + "Заяву прийняв_________________" + "             «___»____________201__р." +
                "\nВ черзі на призупинення" +
                "\nПризупинено" +
                "\nВ черзі на активацію" +
                "\nПодовження Призупинення послуги";
    }
}
