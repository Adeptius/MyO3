package ua.adeptius.myo3.model;


import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.adeptius.myo3.utils.Utilits;

public class Zayavleniya {

    public static List<Zayava> getAllZayava() {
        List<Zayava> list = new ArrayList<>();
        list.add(new Zayava(
                "Додаткова IP адреса",
                MailType.DOP_IP,
                "Для організації роботи серверів паралельно з домашньою лінією. Або для підключення додаткового ПК без роутера(необхіден свіч).",
                30,
                "щомісячно",
                "Замовити"
        ));

        list.add(new Zayava(
                "Реальна IP адреса",
                MailType.REAL_IP,
                "Для серверів, VPN, відеонагляду та PlayStation Network.",
                30,
                "щомісячно",
                "Замовити"
        ));

        list.add(new Zayava(
                "Відключення реальної IP-адреси",
                MailType.DISABLE_REAL_IP,
                "У випадку, якщо вона вам більше не потрібна.",
                0,
                "Безкоштовно",
                "Відключити"
        ));

        list.add(new Zayava(
                "Створення поштової скриньки",
                MailType.CREATE_EMAIL,
                "Поштова скринька на @freenet.com.ua. Має велике обмеження: обсяг 30 мегабайтів. На випадок, якщо вона вам реально потрібна. В інших випадках краще google mail.",
                0,
                "Безкоштовно",
                "Замовити"
        ));
        list.add(new Zayava(
                "Зміна IP адреси",
                MailType.CHANGE_IP,
                "Зміна IP адреси на випадок, якщо така необхідна.",
                90,
                "одноразово",
                "Замовити"
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
                "Замовити"
        ));
        list.add(new Zayava(
                "Помилкова проплата",
                MailType.WRONG_PAY,
                "Якщо ви випадково проплатили інтернет на іншу угоду, та у вас є чек - ми вам допоможемо.",
                0,
                "Безкоштовно",
                "Відправити"
        ));
        list.add(new Zayava(
                "Переоформлення угоди",
                MailType.CHANGE_DEAL,
                "Якщо ви переїзджаєте на нову адресу, і там також підключились до нашого інтернету - ми вам перенесемо ваш рахунок на нову угоду.",
                0,
                "Безкоштовно",
                "Відправити"
        ));
        return list;
    }

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


//    public static String compens(String reason) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("\nна компенсацію абонентської плати");
//        sb.append("\n").append(reason);
//        sb.append("\n");
//        sb.append("\n").append(Utilits.getUkrDateNow());
//        return sb.toString();
//    }


}
