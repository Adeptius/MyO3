package ua.adeptius.myo3.model.persons;


import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import ua.adeptius.myo3.utils.Utilits;

public class Person {

    public Person(String json) {
        try {
            //Preparing json because it shit
            System.out.println(json);

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
            current = Double.parseDouble(allInfo.get("current").toString());
            currency = allInfo.get("currency").toString();
            email = allInfo.get("email").toString();
            stopsum = Double.parseDouble(allInfo.get("stopsum").toString());
            age = Integer.parseInt(allInfo.get("age").toString());
            yur = allInfo.get("yur").toString().equals("1");
            vip = allInfo.get("vip").toString().equals("1");

            //Phones
            String phonesJson = allInfo.get("phones").toString();
            phonesJson = phonesJson.substring(1, phonesJson.length()-1);
            String[] splitted = Utilits.splitJson(phonesJson);
            for (String s : splitted) {
                phones.add(new Phone(s));
            }

            // Address
            String addressJson = allInfo.get("address").toString();
            this.address = new Address(addressJson);

            // Mailing
            String mailingsJson = allInfo.get("mailings").toString();
            mailingsJson = mailingsJson.substring(1, mailingsJson.length()-1);
            splitted = Utilits.splitJson(mailingsJson);
            for (String s : splitted) {
                mailing.add(new Mailing(s));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int id;
    private String surname;
    private String name;
    private String lastname;
    private String card;
    private double current;
    private String currency;
    private String email;
    private double stopsum;
    private int age;
    private boolean yur;
    private boolean vip;
    private ArrayList<Phone> phones = new ArrayList<>();
    private ArrayList<Mailing> mailing = new ArrayList<>();
    private Address address;



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

    public String getLastname() {
        return lastname;
    }

    public String getCard() {
        return card;
    }

    public double getCurrent() {
        return current;
    }

    public String getCurrency() {
        return currency;
    }

    public String getEmail() {
        return email;
    }

    public double getStopsum() {
        return stopsum;
    }

    public int getAge() {
        return age;
    }

    public boolean isYur() {
        return yur;
    }

    public boolean isVip() {
        return vip;
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }

    public ArrayList<Mailing> getMailing() {
        return mailing;
    }

    public Address getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return "NewPerson{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", card='" + card + '\'' +
                ", current=" + current +
                ", currency='" + currency + '\'' +
                ", email='" + email + '\'' +
                ", stopsum=" + stopsum +
                ", age=" + age +
                ", yur=" + yur +
                ", vip=" + vip +
                ", phones=" + phones +
                ", mailing=" + mailing +
                ", address=" + address +
                '}';
    }
}
