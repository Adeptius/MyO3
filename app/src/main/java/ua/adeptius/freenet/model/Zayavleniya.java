package ua.adeptius.freenet.model;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ua.adeptius.freenet.utils.Utilits;

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

//        list.add(new Zayava(
//                "Доступ до SMTP серверу",
//                MailType.SMTP,
//                "Отримання доступу до нашого серверу вихідної пошти, якщо у вашої пошти свого немає. Працює тільки у нашій мережі. Послуга доступна тільки абонентам з реальними IP. На всякий випадок: google, ukr.net, mail.ru, i.ua мають свої сервери",
//                0,
//                "Безкоштовно",
//                "Замовити"
//        ));
//        list.add(new Zayava(
//                "Компенсація абонентської плати",
//                MailType.COMPENSATION,
//                "Повернення коштів на ваш рахунок по причині відсутності інтернету, або некористування, якщо ви забули завчасно призупинити послуги. Будь-ласка, не пишіть по одній заяві кожного разу, по можливості, почекайте і відправте заяву за декілька днів. Дякуємо.",
//                0,
//                "Безкоштовно",
//                "Відправити"
//        ));
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
        sb.append("\n                         ЗАЯВА");
        return sb.toString();
    }

    public static String getFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("\n").append(Utilits.getUkrDateNow());
        sb.append("\n").append("_____________________________________________________");
        sb.append("\n");
        sb.append("\n").append("Заповнюється співробітником компанії:");
        sb.append("\n");
        sb.append("\n").append("Заяву прийняв_________________").append("             «___»____________201__р.");
        sb.append("\n").append("\n").append("Послугу активовано");
        return sb.toString();
    }

    public static String realIP(String startDate, Zayava zayava) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу з ").append(startDate).append(" надати мені реальну IP адресу.");
        sb.append("\n");
        sb.append("\n").append("З вартістю послуги в " + zayava.getPrice() + "грн/" + zayava.getPriceType() + " ознайомлений(на).");
        return sb.toString();
    }


    public static String dopIP(String startDate, Zayava zayava) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу з ").append(startDate).append(" надати мені додаткову IP адресу.");
        sb.append("\n");
        sb.append("\n").append("З вартістю послуги в " + zayava.getPrice() + "грн/" + zayava.getPriceType() + " ознайомлений(на).");
        return sb.toString();
    }

    public static String realIPOff(String startDate, Zayava zayava) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу з ").append(startDate).append(" відключити  мені реальну IP адресу.");
        sb.append("\n");
        return sb.toString();
    }

    public static String changeIP(String startDate, Zayava zayava) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу з ").append(startDate).append(" змінити мою IP адресу.");
        sb.append("\n");
        sb.append("\n").append("З вартістю послуги в " + zayava.getPrice() + "грн/" + zayava.getPriceType() + " ознайомлений(на).");
        return sb.toString();
    }


    public static String createEmail(String login, String password) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу з ").append(Utilits.getUkrDateNow()).append(" надати електронну поштову скриньку");
        sb.append("\n");
        sb.append("\n").append("Логін " + login + "@freenet.com.ua пароль " + password);
        return sb.toString();

    }

    public static String changeTarif(String startDate, String currentTarif, String newTarif) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу з ").append(startDate).append(" змінити тарифний пакет ");
        sb.append("\nЗ ").append(currentTarif);
        sb.append("\nНа ").append(newTarif);
        sb.append("\n");
        sb.append("\n");
        sb.append("\nЗ умовами надання послуги «Зміна тарифного пакету» ознайомлений/на.");
        sb.append("\n");
        sb.append("\n" + Utilits.getUkrDateNow());
        sb.append("\n");
        sb.append("\n").append("_____________________________________________________");
        sb.append("\n");
        sb.append("\n").append("Заповнюється співробітником компанії:");
        sb.append("\n");
        sb.append("\n").append("Заяву прийняв_________________").append("             «___»____________201__р.");
        sb.append("\n");
        sb.append("\n").append("Пакет змінено");
        sb.append("\n");
        sb.append("\n").append("Виставлено в чергу на зміну пакету");

        return sb.toString();
    }


    public static String changeDial(String date, String dogovor, String newAdress, String newDogovor, int age) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу з ").append(date).append(" переоформити договір № ").append(dogovor);
        sb.append(" в зв’язку з переїздом на іншу адресу:\n");
        sb.append(newAdress);
        sb.append("\n");
        sb.append("\nПозитивний залишок на рахунку перенести на договір ").append(newDogovor);
        sb.append("\n");
        sb.append("\nЗберегти історію користування послугами ООО “Фрінет” за ").append(age).append(" місяців");
        sb.append("\n");
        sb.append("\n");
        sb.append("\n" + Utilits.getUkrDateNow());
        sb.append("\n");
        sb.append("\n").append("_____________________________________________________");
        sb.append("\n");
        sb.append("\n").append("Заповнюється співробітником компанії:");
        sb.append("\n");
        sb.append("\n").append("Заяву прийняв_________________").append("             «___»____________201__р.");
        sb.append("\n");
        sb.append("\n").append("Договір закрито");
        sb.append("\n");
        sb.append("\n").append("Залишок перенесено");

        return sb.toString();
    }


    public static String wrongPay(String date, String wrongPayedSum, String wrongDogovor) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Я помилково сплатив(ла) ").append(date);
        sb.append("\nсуму ").append(wrongPayedSum).append(" грн на договір № ").append(wrongDogovor);
        sb.append("\n");
        sb.append("\nПрошу перенести цю сплату на мій договір.");
        sb.append("\n");
        sb.append("\nКопія квитанції додається.");
        sb.append("\n");
        sb.append("\n");
        sb.append("\n" + Utilits.getUkrDateNow());
        sb.append("\n");
        sb.append("\n").append("_____________________________________________________");
        sb.append("\n");
        sb.append("\n").append("Заповнюється співробітником компанії:");
        sb.append("\n");
        sb.append("\n").append("Заяву прийняв_________________").append("             «___»____________201__р.");
        return sb.toString();
    }

    public static String stopInternet(String startDate, String endDate, String dog) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n").append("Прошу призупинити надання послуг доступу до Інтернету за договором ").append(dog);
        sb.append("\nЗ ").append(startDate).append(" по ").append(endDate);
        sb.append("\n");
        sb.append("\n");
        sb.append("\n");
        sb.append("\n" + Utilits.getUkrDateNow());
        sb.append("\n");
        sb.append("\n").append("_____________________________________________________");
        sb.append("\n");
        sb.append("\n").append("Заповнюється співробітником компанії:");
        sb.append("\n");
        sb.append("\n").append("Заяву прийняв_________________").append("             «___»____________201__р.");
        sb.append("\nВ черзі на призупинення");
        sb.append("\nПризупинено");
        sb.append("\nВ черзі на активацію");
        sb.append("\nПодовження Призупинення послуги");
        return sb.toString();
    }
}
