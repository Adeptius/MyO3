package ua.adeptius.myo3.dao;


import java.io.IOException;
import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Person;

public class GetInfo {

    public static Person getPersonInfo() throws Exception{
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/persons");
        return new Person(json);
    }

    public static Ip getIP() throws IOException {
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/network_settings");
        return new Ip(json);
    }


}
