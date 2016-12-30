package ua.adeptius.myo3.dao;


import java.util.HashMap;

import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.persons.Phone;

public class SendInfo {



    public static boolean activateTurboDay(String startDate, String endDate) {
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
        try {
            String s = Web.sendPost("https://my.o3.ua/ajax/activate_free_day", map, false);
            if (s.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean changeTarif(HashMap<String, String> map) {
        try {
            String s = Web.sendPost("https://my.o3.ua/ajax/set_chprice",map, false);
            if (s.contains("\"success\":true")) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean startService(String startDate) {
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
        HashMap<String, String> map = new HashMap<>();
        map.put("mailing_id", String.valueOf(id));
        try {
            Web.sendPost("https://my.o3.ua/ajax/mailing/subscribe", map, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean changeEmail(String email) throws Exception{
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        String response = Web.sendPost("https://my.o3.ua/ajax/email/save", map, true);
        if (response.contains("Email успішно збережений!")) return true;
        return false;
    }

    public static boolean changePassword(String newPassword) throws Exception{
        HashMap<String, String> map = new HashMap<>();
        map.put("old_password", Settings.getCurrentPassword());
        map.put("new_password", newPassword);
        map.put("confirm_password", newPassword);
        String response = Web.sendPost("https://my.o3.ua/ajax/password/save", map, true);
        if (response.contains("\"success\":true")) return true;
        return false;
    }

    public static boolean changeSmsNumber(String number, Phone phone) throws Exception{
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
