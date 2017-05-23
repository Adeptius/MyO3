package ua.freenet.cabinet.dao;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.HashMap;
import ua.freenet.cabinet.utils.Utilits;

public class Web {

    public static String sessionId;
    private static long sessionCreatedTime;

    public static void markSessionIsOld() {
        sessionId = null;
    }

    private static String getSessionId() throws Exception {
        if (sessionId == null) {
            setSessionId(GetInfo.getPhpSession());
            return getSessionId();
        } else {
            long currentTime = new GregorianCalendar().getTimeInMillis();
            long sessionTime = currentTime - sessionCreatedTime;
            int minutes = (int) sessionTime / 1000 / 60;
            if (minutes > 30) {
                setSessionId(GetInfo.getPhpSession());
                return getSessionId();
            } else {
                return sessionId;
            }
        }
    }

    static void setSessionId(String sessionID) {
        sessionId = sessionID;
        sessionCreatedTime = new GregorianCalendar().getTimeInMillis();
    }

    public static String getJsonFromUrl(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Cookie", "PHPSESSID=" + getSessionId());
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = in.readLine();
        result = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(result);
        result = fixJson(result);
        Utilits.networkLog("Получен Json: " + result);
        in.close();
        return result;
    }

    public static String sendPost(String url, HashMap<String, String> jSonQuery, boolean itsJson) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Cookie", "PHPSESSID=" + getSessionId());
        String urlParameters = "";
        if (itsJson) {
            con.setRequestProperty("Content-Type", "application/json");
            urlParameters = new JSONObject(jSonQuery).toString();
        } else {
            Object[] keys = jSonQuery.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                String key = (String) keys[i];
                urlParameters += key + "=" + jSonQuery.get(key);
                if (!(i == keys.length - 1)) urlParameters += "&";
            }
        }
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        Utilits.networkLog("Передаю параметры: " + urlParameters);
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = in.readLine();
        result = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(result);
        Utilits.networkLog("Ответ: " + result);
        in.close();
        return result;
    }

    private static String fixJson(String json){
        // 00015018 0756594  "hNote":""","cscSegId":2    "hNote":"","cscSegId":2
        json = json.replaceAll(":\"\"\",", ":\"\",");

        // 561200788 5126383 "hNote":"[добавлен как частный дом]""","cscSegId": 117,
        json = json.replaceAll("\"\"\",", "\",");

        // "month":85,"bundle":2822},null]
        json = json.replaceAll(",null", "");

        json = json.replaceAll("частный дом]\"\"", "частный дом]\"");


        return json;
    }
}
