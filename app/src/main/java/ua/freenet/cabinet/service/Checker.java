package ua.freenet.cabinet.service;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.activities.LoginActivity;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.MegogoPts;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.model.Servise;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.utils.Utilits.*;

class Checker extends Thread {

    private NotificationManager nm;
    private Context context;


    Checker(NotificationManager nm, Context context) {
        this.nm = nm;
        this.context = context;
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void run() {
        while (true) {
            try {
                long random = (long) (Math.random() * 36000000);
//                System.out.println("random" + random);
                int seconds = (int) random / 1000;
//                System.out.println("seconds" + seconds);
                int sumMinutes = seconds / 60;
//                System.out.println("sumMinutes" + sumMinutes);
                int hours = sumMinutes / 60;
//                System.out.println("hours" + hours);
                int minutes;
                if (hours == 0) {
                    minutes = sumMinutes;
                } else {
                    minutes = sumMinutes % (hours * 60);
                }
//                System.out.println("minutes" + minutes);
                log("Засыпаю на " + hours + " часов, " + minutes + " минут");
//                Thread.sleep();// 46800000
                Thread.sleep(random);// 46800000
            } catch (InterruptedException ignored) {
            }
            if (isLogged()) {
                try {
                    if (isItNormalTimeToCheckFutureMonth()) {
                        checkMoneyForNextMonth();
                    } else if (isThatStartOfMonth()) {
                        checkMoneyForCurrentMonth();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log("Не залогинено");
            }
        }
    }

    private void checkMoneyForCurrentMonth() throws Exception {
        Person person = DbCache.getPerson();
        if (person.getStopsum() > person.getCurrent()) {
            int notAnoth = (int) Math.round(Math.abs(person.getCurrent()));
//            NotificationsHelper helper = new NotificationsHelper();
            NotificationsHelper.init(context);
            NotificationsHelper.createNotificationFirstMonth(notAnoth, LoginActivity.class, R.mipmap.ic_launcher);
            Settings.setServiceCheckedDay(new GregorianCalendar().get(Calendar.DAY_OF_MONTH));
        } else { // если баланс ок - дальше проверять не нужно
            Settings.setMonthPaydCurrentMonth(new GregorianCalendar().get(Calendar.MONTH));
        }
    }


    private boolean isLogged() {
        boolean logNotNull = !Settings.getCurrentLogin().equals("");
        boolean passNotNull = !Settings.getCurrentPassword().equals("");
        return logNotNull && passNotNull;
    }

    private boolean checkMoneyForNextMonth() throws Exception {
//        double amonth = Double.parseDouble(DbCache.getMountlyFeefromLK());
        double haveMoney = DbCache.getPerson().getCurrent();
//        log("денег " + haveMoney + " абонплата " + amonth);

        List<Servise> servises = DbCache.getServises();

        List<String> willChanged = new ArrayList<>();
        int summaryCost = 0;

        for (Servise servise : servises) {
            if (isThatDateIsNextMonth(servise.getDateWillChange())) { // если услуга меняется
                if (servise.getNewName().contains("Удалить")) {// если её удалят
                    willChanged.add(servise.getMyServiceName() + " відключиться.");
                } else { // если она меняется на другую
                    summaryCost += getPrice(servise.getNewName());
                    willChanged.add(servise.getMyServiceName() + " зміниться на " + servise.getNewName());
                }
            } else if (servise.isHaveDiscount()) { // если есть скидка
                if (isThatDateIsNextMonth(servise.getDiscountTo())) {// и она кончается в след месяце
                    summaryCost += servise.getServiceCost();
                    willChanged.add("Закінчиться знижка " + servise.getDiscount() + "% на " + servise.getMyServiceName());
                } else { // и она пока не кончается
                    summaryCost += servise.getCostForCustomer();
                }
            } else { // если ничего с тарифом не произойдёт
                summaryCost += servise.getCostForCustomer();
            }
        }

        if (summaryCost != 0 && summaryCost > haveMoney && summaryCost < 5000 && summaryCost > -5000) {
            // если абоненту не хватает денег и у него тариф не бесплатный и не произошло ошибки парсинга стоимости тарифа
            StringBuilder sb = new StringBuilder();
            if (willChanged.size() == 0) { // если ни один тариф не меняется.
                sb.append("Ваша абонентська плата становить ").append(summaryCost).append(" грн\n");
            } else { // если будут изменения в тарифе
                sb.append("З наступного місяця\n");
                for (String s : willChanged) {
                    sb.append(s).append("\n");
                }
                sb.append("Абонентська плата становитиме ").append(summaryCost).append(" грн\n");
            }
            sb.append("На Вашому рахунку зараз ").append((int) Math.round(haveMoney)).append(" грн\n");
            sb.append("Для того, щоб інтернет працював у наступному місяці - не вистачає ");
            int notAnoth = (int) Math.round(summaryCost - haveMoney);
            sb.append(notAnoth).append(" грн.\n");

            System.out.println(sb.toString());


//            NotificationsHelper helper = new NotificationsHelper();
            NotificationsHelper.init(context);
            NotificationsHelper.createNotification(notAnoth, sb.toString(), LoginActivity.class, R.mipmap.ic_launcher);
        } else { // Если всё в порядке - то больше в этом месяце проверять не нужно
            Settings.setMonthPaydFutureMonth(new GregorianCalendar().get(Calendar.MONTH));
        }
        Settings.setServiceCheckedDay(new GregorianCalendar().get(Calendar.DAY_OF_MONTH));
        return false;
    }

    private static boolean isItNormalTimeToCheckFutureMonth() {
        Utilits.log("Проверяем задолженность на следующий месяц");
        Calendar calendar = new GregorianCalendar();
        int currentMonth = calendar.get(Calendar.MONTH);

        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int left = days - currentDay;
        if (left > 4) {
            Utilits.log("Рано проверять: до конца месяца еще " + left + " дней");
            return false;
        }

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours < 9) {
            Utilits.log("Слишком раннее время");
            return false;
        }
        if (hours > 22) {
            Utilits.log("Слишком позднее время");
            return false;
        }

        if (Settings.getServiceCheckedDay() == currentDay) {
            Utilits.log("Сегодня уже проверяли баланс");
            return false; // если сегодня уже проверяли
        }

        if (Settings.getMonthPaydFutureMonth() == currentMonth) {
            Utilits.log("Уже проплачено. Больше в этом месяце проверять не нужно");
            return false;
        }

        return true;
    }

    private boolean isThatStartOfMonth() {
        Utilits.log("Проверяем задолженность в начале месяца");
        Calendar calendar = new GregorianCalendar();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (currentDay > 3) {
            Utilits.log("Сейчас не начало месяца");
            return false;
        }

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours < 9) {
            Utilits.log("Слишком раннее время");
            return false;
        }
        if (hours > 21) {
            Utilits.log("Слишком позднее время");
            return false;
        }

        if (Settings.getServiceCheckedDay() == currentDay) {
            Utilits.log("Сегодня уже проверяли баланс");
            return false; // если сегодня уже проверяли
        }

        if (Settings.getMonthPaydCurrentMonth() == currentMonth) {
            Utilits.log("Уже проплачено. Больше в этом месяце проверять не нужно");
            return false;
        }
        return true;
    }

    private static boolean isThatDateIsNextMonth(String date) {
        if (date.contains(" ")) {
            date = date.substring(0, date.indexOf(" "));
        }
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        month++;
        if (month == 13) {
            month = 1;
            year++;
        }
        String monthText = month < 10 ? "0" + month : "" + month;
        String result = year + "-" + monthText + "-01";
        return result.equals(date);
    }

    private static int getPrice(String name) throws Exception {
        if (name.contains("MEGOGO")) {
            List<MegogoPts> megogoPts = DbCache.getMegogoPts();
            for (MegogoPts megogoPt : megogoPts) {
                if (megogoPt.getOriginalName().equals(name)) {
                    return Integer.parseInt(megogoPt.getMonth());
                }
            }
        } else {
            try {
                Matcher regexMatcher = Pattern.compile("\\d{1,4}[ ]?грн").matcher(name);
                if (regexMatcher.find()) {
                    String s = regexMatcher.group();
                    if (s.contains(" ")) {
                        s = s.substring(0, s.indexOf(" ")).trim();
                    }
                    return Integer.parseInt(s.trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -9999999;
    }
}
