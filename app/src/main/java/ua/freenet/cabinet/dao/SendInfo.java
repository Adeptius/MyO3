package ua.freenet.cabinet.dao;



import org.json.JSONObject;

import java.util.HashMap;

import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.model.Phone;
import ua.freenet.cabinet.utils.Utilits;

public class SendInfo {


    public static String changeEmailPassword(String newPass, String sid) {
        Utilits.networkLog("Запрос изменения пароля:");
        HashMap<String, String> map = new HashMap<>();
        map.put("new_password", newPass);
        map.put("confirm_password", newPass);
        map.put("s_id", sid);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/save_mail_password", map, false);
            response = new JSONObject(response).getString("message");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Трапилась помилка. Можливо інтернет відсутній.";
    }

    public static String rememberPassword(String card){
        Utilits.networkLog("Запрос восстановления пароля договора : "+card);
        HashMap<String, String> map = new HashMap<>();
        map.put("card", card);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/remember", map, false);
            if (response.contains("\"success\":true")) return "Пароль відправлено на вашу пошту";
            if (response.contains("невірний номер договору")) return "Номер угоди невірний, або ви не вказали його в особистому кабінеті раніше.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Трапилась помилка. Можливо інтернет відсутній.";
    }

    public static boolean activateBonusProgram() {
        Utilits.networkLog("Запрос активации бонусной программы");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/bonus_confirm",
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean spendBonuses(HashMap<String, String> map) {
        Utilits.networkLog("Запрос траты бонусов");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/pay_bonus", map, false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String bringNewFriend(HashMap<String, String> map) {
        Utilits.networkLog("Запрос на приведи друга");
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/add_refer", map, false);
            if (response.contains("успішно створена")) return "Заявка створена";
            if (response.contains("Така заявка існує!")) return "Заявка по цьому номеру вже існує.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Трапилась помилка..";
    }

    public static boolean deActivateMegogo(String id) {
        Utilits.networkLog("Запрос деактивации megogo " + id);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/megogo/unsubscribe/" + id,
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean activateMegogo(String id) {
        Utilits.networkLog("Запрос активации megogo " + id);
        try {
            String response = Web.sendPost("https://my.o3.ua/ajax/megogo/subscribe/" + id,
                    new HashMap<String, String>(), false);
            if (response.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
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
        if (number.length()==10)number = "+38"+ number;
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