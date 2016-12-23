package ua.adeptius.myo3.dao;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Document;
import com.gistlabs.mechanize.document.html.form.Form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.exceptions.CantGetSessionIdException;
import ua.adeptius.myo3.utils.Utilits;

public class Web {

    public static String getJsonFromUrl(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Cookie", "PHPSESSID=" + Settings.getSessionID());
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = in.readLine();
        result = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(result);
        Utilits.networkLog("Получен Json: " + result);
        return result;
    }

    public static String getPhpSession(String login, String password) throws CantGetSessionIdException {
        MechanizeAgent agent = new MechanizeAgent();
        Document page = agent.get("https://my.o3.ua/login");
        Form form = page.form("login_form");
        form.get("_username").set(login);
        form.get("_password").set(password);
        Resource response = form.submit();
        System.out.println("response.getTitle() " + response.getTitle());
        if ("Redirecting to https://my.o3.ua/".equals(response.getTitle()) ||
                "Особистий кабінет".equals(response.getTitle())) {
            return agent.cookies().getAll().get(0).getValue();
        } else {
            throw new CantGetSessionIdException();
        }
    }

    public static boolean isLoggedNow(String sessionId) throws Exception {
        String result = getJsonFromUrl("https://my.o3.ua/ajax/persons");
        if (result.equals("<!DOCTYPE html>")) {
            return false;
        } else if (result.startsWith("{\"id\":")) {
            return true;
        }
        return false;
    }


}
