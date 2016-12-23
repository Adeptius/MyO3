package ua.adeptius.myo3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.model.persons.Mailing;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.utils.Utilits;

public class Test {

    static String session;


    public static void main(String[] args) throws Exception {
        Test test = new Test();
        session = Web.getPhpSession("02514521", "5351301");
        String json = getJsonFromUrl("https://my.o3.ua/ajax/persons");

        Person person = new Person(json);
        for (Object o : person.getPhones()) {
            System.out.println(o);
        }
        System.out.println(person.getAddress());
        for (Mailing mailing : person.getMailing()) {
            System.out.println(mailing);
        }
    }


    public static String getJsonFromUrl(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Cookie", "PHPSESSID=" + session);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = in.readLine();
        result = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(result);
        Utilits.networkLog("Получен Json: " + result);
        return result;
    }

}
