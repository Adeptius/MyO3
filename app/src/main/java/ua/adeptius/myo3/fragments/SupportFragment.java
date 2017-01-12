package ua.adeptius.myo3.fragments;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.City;
import ua.adeptius.myo3.model.Person;

public class SupportFragment  extends BaseFragment {

    private Person person;
    private City city;
    private String sityNameUa;

    @Override
    void setAllSettings() {
        titleText = "Підтримка";
        descriptionText = "";
        fragmentId = R.layout.fragment_support;
        titleImage = R.drawable.background_support;
        layoutId = R.id.support_main;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        person = GetInfo.getPersonInfo();
        sityNameUa = person.getAddress().getCityNameUa();
        String url = GetInfo.getUrlOfCity(sityNameUa);
        city = GetInfo.getCityPhones(url);
        System.out.println(city);
    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }


    private void draw() {
        LinearLayout layForContacts = getLayout(R.id.layout_for_contacts);

        if (sityNameUa.equals("Київ")){
            View layoutCallCentre = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView callCentreName = (TextView) layoutCallCentre.findViewById(R.id.centre_name);
            callCentreName.setText("Відділ підключень");
            TextView callCentreDesc = (TextView) layoutCallCentre.findViewById(R.id.centre_desc);
            callCentreDesc.setText("Телефонуйте, якщо бажаєте підключитись, або дізнатись про діючи акції");
            TextView callCentreWork = (TextView) layoutCallCentre.findViewById(R.id.text_work_time);
            callCentreWork.setText("З 09 до 20 щоденно");
            Button callCentreButton = (Button) layoutCallCentre.findViewById(R.id.button);
            if (city.getSupport().size() == 1){
                String code = city.getCall().get(0);
                code = code.substring(0, code.indexOf(" "));
                callCentreButton.setText("Зателефонувати " + code);
            }else {
                callCentreButton.setText("Зателефонувати");
            }
            layForContacts.addView(layoutCallCentre);

            View layoutAbon = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView abonName = (TextView) layoutAbon.findViewById(R.id.centre_name);
            abonName.setText("Відділ по розрахунку з абонентами");
            TextView abonDesc = (TextView) layoutAbon.findViewById(R.id.centre_desc);
            abonDesc.setText("Питання підключення послуг, та фінансового характеру вирішуються тут");
            TextView abonWork = (TextView) layoutAbon.findViewById(R.id.text_work_time);
            abonWork.setText("Пн-Пт з 09 до 20, Сб з 10 до 18");
            Button abonButton = (Button) layoutAbon.findViewById(R.id.button);
            if (city.getSupport().size() == 1){
                String code = city.getAbon().get(0);
                code = code.substring(0, code.indexOf(" "));
                abonButton.setText("Зателефонувати " + code);
            }else {
                abonButton.setText("Зателефонувати");
            }
            layForContacts.addView(layoutAbon);
        }else {
            View layoutCallAndAbon = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView callAndAbonName = (TextView) layoutCallAndAbon.findViewById(R.id.centre_name);
            callAndAbonName.setText("Відділ підключень та розрахунку");
            TextView callAndAbonDesc = (TextView) layoutCallAndAbon.findViewById(R.id.centre_desc);
            callAndAbonDesc.setText("Телефонуйте, якщо ви бажаєте оформити підключення, дізнатись про акції, або вирішити питання фінансового характеру");
            TextView callAndAbonWork = (TextView) layoutCallAndAbon.findViewById(R.id.text_work_time);
            callAndAbonWork.setText(city.getTime());
            Button callAndAbonButton = (Button) layoutCallAndAbon.findViewById(R.id.button);
            if (city.getSupport().size() == 1){
                String code = city.getAbon().get(0);
                code = code.substring(0, code.indexOf(" "));
                callAndAbonButton.setText("Зателефонувати " + code);
            }else {
                callAndAbonButton.setText("Зателефонувати");
            }
            layForContacts.addView(layoutCallAndAbon);
        }


        View layoutSupport = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
        TextView supportName = (TextView) layoutSupport.findViewById(R.id.centre_name);
        supportName.setText("Технічна підтримка");
        TextView supportDesc = (TextView) layoutSupport.findViewById(R.id.centre_desc);
        supportDesc.setText("Служба цілодобової технічної підтримки");
        TextView supportWork = (TextView) layoutSupport.findViewById(R.id.text_work_time);
        supportWork.setText("Цілодобово!");
        Button supportButton = (Button) layoutSupport.findViewById(R.id.button);
        if (city.getSupport().size() == 1){
            String code = city.getSupport().get(0);
            code = code.substring(0, code.indexOf(" "));
            supportButton.setText("Зателефонувати " + code);
        }else {
            supportButton.setText("Зателефонувати");
        }
        layForContacts.addView(layoutSupport);

        if (sityNameUa.equals("Київ")){

        }else {

        }





    }


    @Override
    public void onClick(View v) {

    }
}

