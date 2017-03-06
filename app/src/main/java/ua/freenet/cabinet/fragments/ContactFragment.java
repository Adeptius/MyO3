package ua.freenet.cabinet.fragments;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.City;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

public class ContactFragment extends HelperFragment {

    private Person person;
    private City city;
    private String sityNameUa;

    @Override
    void setAllSettings() {
        titleText = "Контакти";
        descriptionText = "";
        titleImage = R.drawable.background_support;
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        person = DbCache.getPerson();
        sityNameUa = person.getAddress().getCityNameUa();
        String url = DbCache.getUrlOfCityPhones(sityNameUa);
        city = DbCache.getCityPhones(url);
    }

    @Override
    void processIfOk() {
        draw();
        hideAllViewsInMainScreen();
        animateScreen();
    }


    private void draw() {
//        LinearLayout layForContacts = getLayout(R.id.layout_for_contacts);
        LinearLayout layForContacts = mainLayout;

        if (sityNameUa.equals("Київ")){
            // Добавляем контакт Киевского колл центра
            View layoutCallCentre = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView callCentreName = (TextView) layoutCallCentre.findViewById(R.id.centre_name);
            callCentreName.setText("Відділ підключень");
            TextView callCentreDesc = (TextView) layoutCallCentre.findViewById(R.id.centre_desc);
            callCentreDesc.setText("Телефонуйте, якщо бажаєте оформити підключення, або дізнатись про діючи акції");
            TextView callCentreWork = (TextView) layoutCallCentre.findViewById(R.id.text_work_time);
            callCentreWork.setText("З 09 до 20 щоденно");
            Button callCentreButton = (Button) layoutCallCentre.findViewById(R.id.button);
            callCentreButton.setText("Зателефонувати");
            callCentreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call(city.getCall());
                }
            });
            Button emailCallCentreButton = (Button) layoutCallCentre.findViewById(R.id.button_email);
            emailCallCentreButton.setText("написати листа");
            emailCallCentreButton.setVisibility(View.VISIBLE);
            emailCallCentreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail("sales@o3.ua");
                }
            });
            layForContacts.addView(layoutCallCentre);

            // Добавляем контакт Киевского абон отдела
            View layoutAbon = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView abonName = (TextView) layoutAbon.findViewById(R.id.centre_name);
            abonName.setText("Відділ по розрахунку з абонентами");
            TextView abonDesc = (TextView) layoutAbon.findViewById(R.id.centre_desc);
            abonDesc.setText("Питання підключення послуг, та фінансового характеру вирішуються тут");
            TextView abonWork = (TextView) layoutAbon.findViewById(R.id.text_work_time);
            abonWork.setText("Пн-Пт з 09 до 20, Сб з 10 до 18");
            Button abonButton = (Button) layoutAbon.findViewById(R.id.button);
            abonButton.setText("Зателефонувати");
            abonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call(city.getAbon());
                }
            });
            Button emailAbonButton = (Button) layoutAbon.findViewById(R.id.button_email);
            emailAbonButton.setText("написати листа");
            emailAbonButton.setVisibility(View.VISIBLE);
            emailAbonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail("abon_otdel@o3.ua");
                }
            });
            layForContacts.addView(layoutAbon);
        }else { // Добавляем контакт НЕ Киевского колл центра
            View layoutCallAndAbon = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView callAndAbonName = (TextView) layoutCallAndAbon.findViewById(R.id.centre_name);
            callAndAbonName.setText("Відділ підключень та розрахунку");
            TextView callAndAbonDesc = (TextView) layoutCallAndAbon.findViewById(R.id.centre_desc);
            callAndAbonDesc.setText("Телефонуйте, якщо ви бажаєте оформити підключення, дізнатись про акції, або вирішити питання фінансового характеру");
            TextView callAndAbonWork = (TextView) layoutCallAndAbon.findViewById(R.id.text_work_time);
            callAndAbonWork.setText(city.getTime());
            Button callAndAbonButton = (Button) layoutCallAndAbon.findViewById(R.id.button);
            callAndAbonButton.setText("Зателефонувати");
            callAndAbonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call(city.getAbon());
                }
            });
            Button emailCallAndAbonButton = (Button) layoutCallAndAbon.findViewById(R.id.button_email);
            emailCallAndAbonButton.setText("написати листа");
            emailCallAndAbonButton.setVisibility(View.VISIBLE);
            emailCallAndAbonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail("abon_otdel@o3.ua");
                }
            });
            layForContacts.addView(layoutCallAndAbon);
        }


        // добавляем саппорт
        View layoutSupport = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
        TextView supportName = (TextView) layoutSupport.findViewById(R.id.centre_name);
        supportName.setText("Технічна підтримка");
        TextView supportDesc = (TextView) layoutSupport.findViewById(R.id.centre_desc);
        supportDesc.setText("Служба цілодобової технічної підтримки");
        TextView supportWork = (TextView) layoutSupport.findViewById(R.id.text_work_time);
        supportWork.setText("Цілодобово!");
        Button supportButton = (Button) layoutSupport.findViewById(R.id.button);
        supportButton.setText("Зателефонувати");
        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(city.getSupport());
            }
        });
        Button emailSupportButton = (Button) layoutSupport.findViewById(R.id.button_email);
        emailSupportButton.setText("написати листа");
        emailSupportButton.setVisibility(View.VISIBLE);
        emailSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail("support@o3.ua");
            }
        });
        layForContacts.addView(layoutSupport);

        if (sityNameUa.equals("Київ")){// добавляем Киевские ЦОА
            // Закревского
            View layoutZakrevskogo = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView zakrevskogoName = (TextView) layoutZakrevskogo.findViewById(R.id.centre_name);
            zakrevskogoName.setText("Центр обслуговування абонентів");
            TextView zakrevskogoDesc = (TextView) layoutZakrevskogo.findViewById(R.id.centre_desc);
            zakrevskogoDesc.setText("Закревського 22\nОбробка вхідних дзвінків а також прийом абонентів у ЦОА");
            TextView zakrevskogoWork = (TextView) layoutZakrevskogo.findViewById(R.id.text_work_time);
            zakrevskogoWork.setText("Пн-Пт з 09 до 20, Сб з 10 до 18");
            Button zakrevskogoButton = (Button) layoutZakrevskogo.findViewById(R.id.button);
            zakrevskogoButton.setText("Карта");
            zakrevskogoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCoordinatesOnMap("50.505783 30.618057");
                }
            });
            layForContacts.addView(layoutZakrevskogo);

            // Драгоманова
            View layoutDragomanova = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView dragomanovaName = (TextView) layoutDragomanova.findViewById(R.id.centre_name);
            dragomanovaName.setText("Центр обслуговування абонентів");
            TextView dragomanovaDesc = (TextView) layoutDragomanova.findViewById(R.id.centre_desc);
            dragomanovaDesc.setText("Драгоманова 17\nОбробка вхідних дзвінків а також прийом абонентів у ЦОА");
            TextView dragomanovaWork = (TextView) layoutDragomanova.findViewById(R.id.text_work_time);
            dragomanovaWork.setText("Пн-Пт з 09 до 20, Сб з 10 до 18");
            Button dragomanovaButton = (Button) layoutDragomanova.findViewById(R.id.button);
            dragomanovaButton.setText("Карта");
            dragomanovaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCoordinatesOnMap("50.4090463 30.6394796");
                }
            });
            layForContacts.addView(layoutDragomanova);


        }else {// добавляем НЕ Киевские ЦОА
            View layoutRegionCoa = LayoutInflater.from(context).inflate(R.layout.item_support_contact, null);
            TextView regionCoaName = (TextView) layoutRegionCoa.findViewById(R.id.centre_name);
            regionCoaName.setText("Центр обслуговування абонентів");
            TextView regionCoaDesc = (TextView) layoutRegionCoa.findViewById(R.id.centre_desc);
            regionCoaDesc.setText(city.getAdress()+ "\n" + city.getReason());
            TextView regionCoaWork = (TextView) layoutRegionCoa.findViewById(R.id.text_work_time);
            regionCoaWork.setText(city.getTime());
            Button regionCoaButton = (Button) layoutRegionCoa.findViewById(R.id.button);
            regionCoaButton.setText("Карта");
            regionCoaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCoordinatesOnMap(city.getCoordinates());
                }
            });
            layForContacts.addView(layoutRegionCoa);
        }
    }

    private void call(final List<String> phones){
        if (phones.size() == 1) {
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phones.get(0)));
            startActivity(intent1);
        } else {
            String[] cloneForPhones = new String[phones.size()];
            for (int i = 0; i < cloneForPhones.length; i++) {
                cloneForPhones[i] = phones.get(i).trim();
            }

            new MyAlertDialogBuilder(context)
                    .setTitleText("Оберіть номер")
                    .setItems(cloneForPhones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String number = phones.get(which);
                            number = number.replaceAll("\\(","");
                            number = number.replaceAll("\\)","");
                            number = number.replaceAll(" ","");
                            number = number.replaceAll("-","");
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + number));
                            startActivity(intent1);
                            dialog.dismiss();
                        }
                    }).createAndShow();
        }
    }

    private void sendEmail(String recipient){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",recipient, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Договір " + person.getCard());
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void showCoordinatesOnMap(String coordinates){
        try{
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=" + coordinates));
        startActivity(intent);
        }catch (ActivityNotFoundException e){
            new MyAlertDialogBuilder(context)
                    .setTitleText("Помилка")
                    .setMessage("На пристрої не знайдено встановлених мап")
                    .setPositiveButtonForClose("ОК")
                    .createAndShow();
        }
    }
}

