package ua.adeptius.myo3.dao;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Document;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.document.html.form.FormElement;
import com.gistlabs.mechanize.document.html.form.Forms;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.adeptius.myo3.model.ChannelMegogo;
import ua.adeptius.myo3.utils.Settings;
import ua.adeptius.myo3.utils.Utilits;

public class Web {

    private static String sessionId;
    private static long sessionCreatedTime;

    public static String getSessionId() throws Exception {
        if (sessionId == null) {
            setSessionId(getPhpSession());
            return getSessionId();
        } else {
            long currentTime = new GregorianCalendar().getTimeInMillis();
            long sessionTime = currentTime - sessionCreatedTime;
            int minutes = (int) sessionTime / 1000 / 60;
            if (minutes > 30) {
                setSessionId(getPhpSession());
                return getSessionId();
            } else {
                return sessionId;
            }
        }
    }

    private static void setSessionId(String sessionID) {
        sessionId = sessionID;
        sessionCreatedTime = new GregorianCalendar().getTimeInMillis();
    }

//    private static String getPhpSession() throws Exception {
//        MechanizeAgent agent = new MechanizeAgent();
//        Document page = agent.get("https://my.o3.ua/login");
////        Form form = page.form("/login_check");
//        Forms forms = page.forms();
//
//        Form form = forms.get(0);
//        form.get("_username").set(Settings.getCurrentLogin());
//        form.get("_password").set(Settings.getCurrentPassword());
//        Resource response = form.submit();
//        if ("Redirecting to https://my.o3.ua/".equals(response.getTitle()) ||
//                "Особистий кабінет".equals(response.getTitle())) {
//            String session = agent.cookies().getAll().get(0).getValue();
//            agent = null;
//            page = null;
//            response = null;
//            form = null;
//            return session;
//        } else {
//            throw new Exception();
//        }
//    }

    public static String getPhpSession() throws Exception {
        URL obj = new URL("https://my.o3.ua/login");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String session1 = getSessionFromHeaders(con.getHeaderFields());
        System.out.println("Session 1 = " + session1);

        obj = new URL("https://my.o3.ua/login_check");
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.8,ru;q=0.6,uk;q=0.4");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Content-Length", "53");
        con.setRequestProperty("Host", "my.o3.ua");
        con.setRequestProperty("Origin", "https://my.o3.ua");
        con.setRequestProperty("Referer", "https://my.o3.ua/login");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        con.setRequestProperty("Cookie", "PHPSESSID=" + session1);
        String urlParameters = "_target_path=%2F&_username=02514521&_password=5351301";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        String session2 = getSessionFromHeaders(con.getHeaderFields());
        System.out.println("Session 2 = " + session2);
        return session2;
    }

    public static String getSessionFromHeaders(Map<String, List<String>> map) throws Exception {

        List<String> list = map.get("Set-Cookie");
        String result = list.get(0);
        result = result.substring(result.indexOf("PHPSESSID=")+10, result.indexOf(";"));
        return result;
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


}
