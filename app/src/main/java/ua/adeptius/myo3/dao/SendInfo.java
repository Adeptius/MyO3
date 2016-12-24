package ua.adeptius.myo3.dao;


import java.util.HashMap;

import ua.adeptius.myo3.model.persons.Phone;

public class SendInfo {


    public static boolean changeEmail(String email) throws Exception{
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        String response = Web.sendPost("https://my.o3.ua/ajax/email/save", map);
        if (response.contains("Email успішно збережений!")) return true;
        return false;
    }

    public static boolean changeSmsNumber(String number, Phone phone) throws Exception{
        HashMap<String, String> map = new HashMap<>();
        map.put("comment",   phone.getComment());
        map.put("id",        ""+phone.getId());
        map.put("lang",      ""+phone.getLang());
        map.put("modifyDate", phone.getModifyDate());
        map.put("pId",       ""+phone.getpId());
        map.put("phone",     number);
        map.put("phonems",   phone.getPhonems());
        map.put("smsInform", "1");
        map.put("valid",     ""+phone.getValid());
        String response = Web.sendPost("https://my.o3.ua/ajax/phone/save", map);
        if (response.contains("Номер успішно збережений!")) return true;
        return false;
    }



}
