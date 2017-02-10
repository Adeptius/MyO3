package ua.freenet.cabinet.model;


import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import ua.freenet.cabinet.utils.Utilits;

public class Person {

    private double stopsum;
    private int id;
    private String surname;
    private String name;
    private String lastname;
    private String card;
    private double current;
    private String email;
    private int age;
    private ArrayList<Phone> phones = new ArrayList<>();
    private ArrayList<Mailing> mailing = new ArrayList<>();
    private Address address;
    private boolean yur;
    private boolean vip;

    public Person(String json) {
        try {
            //Preparing json because it shit

            json = json.replaceAll("\"mailings\":\\{", "\"mailings\":[");
            json = json.replaceAll("\\},\"phones\"", "],\"phones\"");
            json = json.replaceAll("\"1\":","").replaceAll("\"2\":","").replaceAll("\"3\":","");
            json = json.replaceAll("\"4\":","").replaceAll("\"5\":","").replaceAll("\"6\":","");

            // Parse basic info
            JSONObject allInfo = new JSONObject(json.trim());
            id = Integer.parseInt(allInfo.get("id").toString());
            surname = allInfo.get("surname").toString();
            name = allInfo.get("name").toString();
            lastname = allInfo.get("lastname").toString();
            card = allInfo.get("card").toString();
            String money = allInfo.getString("current");
            if (money.contains("E-")){
                current = 0;
            }else if (money.length() > 7){
                current = Double.parseDouble(money.substring(0,7));
            }else {
                current = Double.parseDouble(money);
            }
            email = allInfo.get("email").toString();
            if (email==null) email = "";
            age = Integer.parseInt(allInfo.get("age").toString());

            //Phones
            String phonesJson = allInfo.get("phones").toString();
            phonesJson = phonesJson.substring(1, phonesJson.length()-1);
            String[] splitted = Utilits.splitJson(phonesJson);
            for (String s : splitted) {
                if (!s.equals("")) {
                    phones.add(new Phone(s));
                }
            }

            // Address
            String addressJson = allInfo.get("address").toString();
            address = new Address(addressJson);

            // Mailing
            String mailingsJson = allInfo.get("mailings").toString();
            mailingsJson = mailingsJson.substring(1, mailingsJson.length()-1);
            splitted = Utilits.splitJson(mailingsJson);
            for (String s : splitted) {
                mailing.add(new Mailing(s));
            }

            stopsum = allInfo.getDouble("stopsum");


            yur = allInfo.getString("yur").equals("1");
            vip = allInfo.getString("vip").equals("1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isVip() {
        return vip;
    }

    public boolean isYur() {
        return yur;
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getUkrName() {
        String ukrName = name;
        ukrName = ukrName.replace("Владимир","Володимир");
        ukrName = ukrName.replace("Игорь","Ігор");

        return ukrName;
    }

    public Address getAddress() {
        return address;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCard() {
        return card;
    }

    public String getEmail() {
        if ("null".equals(email)){
            return "";
        }
        return email;
    }

    public String getPhoneWithSms() {
        String phoneNumber = "";
        for (Phone phone : getPhones()) {
            if (phone.getSmsInform() == 1) {
                phoneNumber = phone.getPhone();
                phoneNumber = phoneNumber.replaceAll("\\+38", "");
            }
        }
        return phoneNumber;
    }

    public double getCurrent() {
        return current;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }

    public ArrayList<Mailing> getMailing() {
        return mailing;
    }

    public double getStopsum() {
        return stopsum;
    }
}
