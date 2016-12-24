package ua.adeptius.myo3.dao;


import java.util.HashMap;

public class SendInfo {


    public static boolean changeEmail(String email) throws Exception{
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        String response = Web.sendPost("https://my.o3.ua/ajax/email/save", map);
        if (response.contains("Email успішно збережений!")) return true;
        return false;
    }
}
