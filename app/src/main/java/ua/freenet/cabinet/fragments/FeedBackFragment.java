package ua.freenet.cabinet.fragments;


import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.model.Phone;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;
import ua.freenet.cabinet.utils.Utilits;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;


public class FeedBackFragment extends HelperFragment {



    private Person person;
    public static String htmlMailBody = "";

    @Override
    void setAllSettings() {
        titleText = "Напишіть нам";
        descriptionText = "";
        titleImage = R.drawable.background_feedback4;
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {
    }

    @Override
    void doInBackground() throws Exception {
        person = DbCache.getPerson();
    }

    @Override
    void processIfOk() {
        prepareViews();
        showAndAnimatePreparedViews();
    }

    private void prepareViews() {
        View feedbackMainView = LayoutInflater.from(context).inflate(R.layout.item_feedback, null);

        final String technical = "Технічне питання";
        final String finance = "Фінансове питання";
        final String callCentre = "Питання по підключенню";
        final String akciiAndTarifs = "Акції та тарифи";

        final String technicalEmail = "support@o3.ua";
        final String financeEmail = "abon_otdel@o3.ua";
        final String callCentreEmail = "sales@o3.ua";

        String[] list = new String[]{technical, finance, callCentre, akciiAndTarifs};
        Spinner spinner = (Spinner) feedbackMainView.findViewById(R.id.feedback_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final String[] choicedTemathic = {technicalEmail};
        final String[] subject = {""};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = adapter.getItem(position);
                if (technical.equals(selected)) {
                    choicedTemathic[0] = technicalEmail;
                    subject[0] = "Технічне звернення абонента";
                } else if (finance.equals(selected)) {
                    choicedTemathic[0] = financeEmail;
                    subject[0] = "Фінансове звернення абонента";
                } else if (callCentre.equals(selected)) {
                    choicedTemathic[0] = callCentreEmail;
                    subject[0] = "Звернення по підключенню. Абонент";
                }else if (akciiAndTarifs.equals(selected)) {
                    choicedTemathic[0] = financeEmail;
                    subject[0] = "Звернення по акціям та тарифам. Абонент";
                }
                System.out.println(choicedTemathic[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Контактный email
        final EditText emailText = (EditText) feedbackMainView.findViewById(R.id.email_text);
        emailText.setText(person.getEmail());

        // Контактный телефон
        final EditText phoneText = (EditText) feedbackMainView.findViewById(R.id.phone_text);
        String phone = "";
        if (phone.equals("")) {
            List<Phone> phones = person.getPhones();
            if (phones != null && phones.size() > 1) {
                phone = phones.get(0).getPhone();
            }
        }
        if (phone.startsWith("+38")) {
            phone = phone.substring(3);
        }
        phoneText.setText(phone);

        // Описание обращения
        final EditText summaryText = (EditText) feedbackMainView.findViewById(R.id.summary_text);
        summaryText.setText(Utilits.savedFeedBackText);
        // Сохранение текста
        summaryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utilits.savedFeedBackText = s.toString();
            }
        });

        final Button sendButton = (Button) feedbackMainView.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailText.getText().toString();
                final String choicedPhone = phoneText.getText().toString();
                final String abonDogovor = person.getCard();
                final String completeSubject = subject[0] + " " + abonDogovor;

                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        sendEmail(choicedTemathic[0], abonDogovor, email, choicedPhone,
                                summaryText.getText().toString(), completeSubject, summaryText);
                    }
                });
            }
        });

        preparedViews.add(feedbackMainView);
    }

    private void sendEmail(@NonNull String recipient, @NonNull String abonDogovor,
                           @NonNull String abonEmail, @NonNull String abonPhone,
                           @NonNull String abonMessage, @NonNull String subject, final EditText summaryText) {
        //TODO если не введён ни один контакт - то сообщение
        if (abonEmail.equals("") && abonPhone.equals("")) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    new MyAlertDialogBuilder(context)
                            .setMessage("Вкажіть, будь ласка, хоча б один контакт")
                            .setNegativeButtonForClose("Ввести")
                            .createAndShow();
                }
            });
            return;
        }
        // Врубаем прогресс бар, что пошла отправка
        progressDialogShow();

        try {
            // Чтение HTML шаблона из файла
            InputStream raw = context.getAssets().open("myMail.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(raw, "UTF8"));
            StringBuilder builder = new StringBuilder(4300);
            while (reader.ready()) {
                builder.append(reader.readLine());
            }
            String htmlMessage = builder.toString();
            // Вставка данных в шаблон
            htmlMessage = htmlMessage.replaceAll("ABON_DOGOVOR", abonDogovor);
            htmlMessage = htmlMessage.replaceAll("ABON_EMAIL", abonEmail);
            htmlMessage = htmlMessage.replaceAll("ABON_PHONE", abonPhone);
            htmlMessage = htmlMessage.replaceAll("ABON_MESSAGE", abonMessage);

            //Подготовка письма
            //Тема письма


            // Логин и пароль от почты рассылки Gmail
            String username = "my@o3.ua";
            String password = "YwPFltknU8ez0m";

            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", true);
            props.put("mail.smtp.host", "mail.o3.ua");
            props.put("mail.smtp.user", username);
            props.put("mail.smtp.password", password);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", true);

            Session session = Session.getInstance(props, null);
            MimeMessage message = new MimeMessage(session);

            // Create the email addresses involved
            InternetAddress from = new InternetAddress(username);
            message.setSubject(subject);
            message.setFrom(from);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart("alternative");

            // Create your text message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlMessage, "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

            // Associate multi-part with message
            message.setContent(multipart);

            // Отправка сообщения
            Transport transport = session.getTransport("smtp");
            transport.connect("mail.o3.ua", username, password);
            System.out.println("Transport: " + transport.toString());
            transport.sendMessage(message, message.getAllRecipients());

            showResult("Відправлено!", "Будь ласка, зачекайте, ми з Вами обов'язково зв'яжемося по пошті або телефону");
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    summaryText.setText("");
                    Utilits.savedFeedBackText = "";
                }
            });

        }catch (MessagingException e){
            e.printStackTrace();
            showResult("Помилка","Не вдалось відправити звернення. Спробуйте ще раз.");
        }catch (IOException e) {
            e.printStackTrace();
            showResult("Помилка", "Не вдалось відправити звернення. Можливо немає зв'язку.");
        }finally {
            hideProgressDialog();
        }
    }

    private void showResult(final String header, final String message){
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                new MyAlertDialogBuilder(context)
                        .setTitleText(header)
                        .setMessage(message)
                        .setNegativeButtonForClose("Зрозуміло")
                        .createAndShow();
            }
        });
    }
}

