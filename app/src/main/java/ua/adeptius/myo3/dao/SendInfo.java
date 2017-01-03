package ua.adeptius.myo3.dao;


import java.util.HashMap;

import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.persons.Phone;
import ua.adeptius.myo3.utils.Utilits;

public class SendInfo {

    public static boolean deactivateDrWeb(String id) {
        Utilits.networkLog("Запрос отключения drweb " + id);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/dr_web/unsubscribe/" + id,
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean activateDrWeb(String version) {
        Utilits.networkLog("Запрос подключения DrWeb подписки " + version);
        String url = "https://my.o3.ua/ajax/dr_web/subscribe/";
        if (version.equals("classic"))url += "AV";
        else if (version.equals("standart"))url += "AVS";
        else if (version.equals("premium"))url += "AVSPC";
        else if (version.equals("mobile"))url += "MOBILE";
        else return false;
        try {
            String response = Web.sendPost(url, new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean internetSwitchWorld() {
        Utilits.networkLog("Запрос отключения зарубежа");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/internet/switch/world",
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean internetSwitchAll() {
        Utilits.networkLog("Запрос отключения всего интернета");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/internet/switch/all",
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean deActivateGarantedService() {
        Utilits.networkLog("Запрос отключения гарантированного сервиса");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/guaranteed_service/disable",
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean activateGarantedService() {
        Utilits.networkLog("Запрос активации гарантированного сервиса");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/guaranteed_service/enable",
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean reActivateCredit() {
        Utilits.networkLog("Запрос восстановления кредита доверия");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/restore_credit_trust",
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean activateCredit() {
        Utilits.networkLog("Запрос активации кредита доверия");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/activate_credit_trust",
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean activateTurboDay(String startDate, String endDate) {
        Utilits.networkLog("Запрос активации турбо дня");
        HashMap<String, String> map = new HashMap<>();
        map.put("s_date", startDate);
        map.put("e_date", endDate);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/activate_turbo_day", map, false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean activateFreeDay(HashMap<String, String> map) {
        Utilits.networkLog("Запрос активации фридэя");
        try {
            String s = Web.sendPost("https://my.o3.ua/ajax/activate_free_day", map, false);
            if (s.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean changeTarif(HashMap<String, String> map) {
        Utilits.networkLog("Запрос смены тарифа");
        try {
            String s = Web.sendPost("https://my.o3.ua/ajax/set_chprice",map, false);
            if (s.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean startService(String startDate) {
        Utilits.networkLog("Запрос восстановления сервиса");
        HashMap<String, String> map = new HashMap<>();
        map.put("r_date", startDate);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/renew", map, false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean stopService(String startDate, String endDate) {
        Utilits.networkLog("Запрос приостановки сервиса");
        HashMap<String, String> map = new HashMap<>();
        map.put("s_date", startDate);
        map.put("e_date", endDate);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/suspend", map, false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void changeMailings(int id) {
        Utilits.networkLog("Запрос смены рассылки");
        HashMap<String, String> map = new HashMap<>();
        map.put("mailing_id", String.valueOf(id));
        try {
            Web.sendPost("https://my.o3.ua/ajax/mailing/subscribe", map, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean changeEmail(String email) throws Exception{
        Utilits.networkLog("Запрос смены имэйла");
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        String response = Web.sendPost("https://my.o3.ua/ajax/email/save", map, true);
        if (response.contains("Email успішно збережений!")) return true;
        return false;
    }

    public static boolean changePassword(String newPassword) throws Exception{
        Utilits.networkLog("Запрос смены пароля");
        HashMap<String, String> map = new HashMap<>();
        map.put("old_password", Settings.getCurrentPassword());
        map.put("new_password", newPassword);
        map.put("confirm_password", newPassword);
        String response = Web.sendPost("https://my.o3.ua/ajax/password/save", map, true);
        if (response.contains("\"success\":true")) return true;
        return false;
    }

    public static boolean changeSmsNumber(String number, Phone phone) throws Exception{
        Utilits.networkLog("Запрос смены смс номера для рассылки");
        HashMap<String, String> map = new HashMap<>();
        if (phone==null){
            map.put("id","null");
            map.put("phone", number);
        }else {
            map.put("comment",   phone.getComment());
            map.put("id",        ""+phone.getId());
            map.put("lang",      ""+phone.getLang());
            map.put("modifyDate", phone.getModifyDate());
            map.put("pId",       ""+phone.getpId());
            map.put("phone",     number);
            map.put("phonems",   phone.getPhonems());
            map.put("smsInform", "1");
            map.put("valid",     ""+phone.getValid());
        }
        String response = Web.sendPost("https://my.o3.ua/ajax/phone/save", map, true);
        if (response.contains("Номер успішно збережений!")) return true;
        return false;
    }
}
