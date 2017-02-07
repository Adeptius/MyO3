package ua.adeptius.freenet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.adeptius.freenet.R;
import ua.adeptius.freenet.dao.DbCache;
import ua.adeptius.freenet.dao.GetInfo;
import ua.adeptius.freenet.dao.SendInfo;
import ua.adeptius.freenet.model.AvailableTarif;
import ua.adeptius.freenet.model.MailType;
import ua.adeptius.freenet.model.Person;
import ua.adeptius.freenet.model.Phone;
import ua.adeptius.freenet.model.Servise;
import ua.adeptius.freenet.model.Zayava;
import ua.adeptius.freenet.model.Zayavleniya;
import ua.adeptius.freenet.utils.Settings;
import ua.adeptius.freenet.utils.Utilits;

//import static ua.adeptius.myo3.model.MailType.CHANGE_DEAL;
import static ua.adeptius.freenet.model.MailType.CHANGE_IP;
import static ua.adeptius.freenet.model.MailType.CHANGE_TARIF;
//import static ua.adeptius.myo3.model.MailType.COMPENSATION;
import static ua.adeptius.freenet.model.MailType.CREATE_EMAIL;
import static ua.adeptius.freenet.model.MailType.DISABLE_REAL_IP;
import static ua.adeptius.freenet.model.MailType.DOP_IP;
import static ua.adeptius.freenet.model.MailType.REAL_IP;
//import static ua.adeptius.myo3.model.MailType.SMTP;
//import static ua.adeptius.myo3.model.MailType.WRONG_PAY;


public class DocumentFragment extends BaseFragment {

    private Person person;
    private String header;
    private String footer;

    @Override
    void setAllSettings() {
        titleText = "Заяви";
        descriptionText = "Просто та зручно";
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
        titleImage = R.drawable.background_documents;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        person = DbCache.getPerson();
        footer = Zayavleniya.getFooter();
    }

    @Override
    void processIfOk() {
        draw();
        hideAllViewsInMainScreen();
        animateScreen();
    }

    private void draw() {
        List<Zayava> list = Zayavleniya.getAllZayava();
        for (final Zayava zayava : list) {
            View zayavaLayout = LayoutInflater.from(context).inflate(R.layout.item_document_zayava, null);
            TextView zayavaName = (TextView) zayavaLayout.findViewById(R.id.zayava_name);
            TextView zayavaComent = (TextView) zayavaLayout.findViewById(R.id.zayava_coment);
            TextView zayavaCost = (TextView) zayavaLayout.findViewById(R.id.zayava_cost);
            TextView zayavaCostType = (TextView) zayavaLayout.findViewById(R.id.zayava_cost_type);
            Button zayavaButton = (Button) zayavaLayout.findViewById(R.id.zayava_button);

            zayavaName.setText(zayava.getName());
            zayavaComent.setText(zayava.getComent());
            if (zayava.getPrice() == 0) {
                zayavaCost.setText(zayava.getPriceType());
                zayavaCost.setTextSize(20);
                zayavaCostType.setVisibility(View.GONE);
            } else {
                zayavaCost.setText(String.valueOf(zayava.getPrice()));
                zayavaCostType.setText(zayava.getPriceType());
            }
            zayavaButton.setText(zayava.getButtonName());
            zayavaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonWasClicked(zayava);
                }
            });
            mainLayout.addView(zayavaLayout);
        }
    }

    private String checkMoneyMessage(Zayava zayava) {
        String message = "Вартість послуги складає " + zayava.getPrice() + " грн " + zayava.getPriceType().toLowerCase() + ".";
        int willCostIfNow;
        message += "\nУ вас зараз " + (int) person.getCurrent() + " грн";
        if (zayava.getPriceType().equals("щомісячно")) {
            willCostIfNow = Utilits.calculateDaysCostLeft(zayava.getPrice()) + 1;
            message += "\nЯкщо активувати прямо зараз - з вашого рахунку буде знято ";
            message += willCostIfNow + " грн. ";
        } else {
            willCostIfNow = zayava.getPrice();
            message += "\nЯкщо активувати прямо зараз - ";
        }

        int moneyWillLeft = (int) person.getCurrent() - willCostIfNow;
        if (moneyWillLeft < 0) {
            message += "на рахунку не вистачить " + Math.abs(moneyWillLeft) + " грн. Послуги припиняться.";
            message += "\nРекомендуємо спочатку поповнити рахунок.";
        } else {
            message += "на рахунку залишиться " + moneyWillLeft + " грн. Інтернет працюватиме.";
        }

        message += "\nДо речі: зараз ви зможете вибрати будь-який інший день активації.";
        return message;
    }

    private void buttonWasClicked(Zayava zayava) {
        MailType type = zayava.getType();
        if (type == REAL_IP) {
            askAgree(type, zayava, checkMoneyMessage(zayava));
        } else if (type == DISABLE_REAL_IP) {
            askAgree(type, zayava, "Відключення реальної IP. Ми замінимо її на внутрішню.");
        } else if (type == DOP_IP) {
            askAgree(type, zayava, checkMoneyMessage(zayava));
        } else if (type == CHANGE_TARIF) {
            askAgree(type, zayava, "Зміна тарифу. \nМайте на увазі: перехід можливий не раніше ніж з завтрішнього дня. \nДо речі: подивіться, чи зможете ви самостійно перейти на інший тариф у розділі \"Підключені послуги\"");
        } else if (type == CREATE_EMAIL) {
            askAgree(type, zayava, "Створення поштової скриньки. Безкоштовно надається тільки одна скринька. \nУВАГА!\n У разі відключення від мережі \"Фрінет\" скринька буде автоматично видалена!");
        } else if (type == CHANGE_IP) {
            askAgree(type, zayava, checkMoneyMessage(zayava));
//        } else if (type == CHANGE_DEAL) {
//            askAgree(type, zayava, "Ми раді, що ви залишитесь з нами!");
//        } else if (type == COMPENSATION){
//            askAgree(type, zayava, "Нам шкода, що Вам доводиться відправляти цю заяву. Ми зробили все, щоб це у вас зайняло якнайменше часу.");
//        } else if (type == WRONG_PAY) {
//            askAgree(type, zayava, "Не хвилюйтесь, ми допоможемо повернути ваші кошти.");
//        } else if (type == SMTP) {
//            askAgree(type, zayava, "Послуга доступна тільки абонентам з реальними IP. До вашого відома: поштовий сервер relay2@freenet.com.ua без перевірки логіна та пароля (аутенфікації)");
        }
    }

    private void askAgree(final MailType type, final Zayava zayava, String agree) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(agree);
        builder.setView(textLayout);

        builder.setPositiveButton("Далі", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                askHeaderData(type, zayava);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void askHeaderData(final MailType type, final Zayava zayava) {
        final View headerLayout = LayoutInflater.from(context).inflate(R.layout.item_documents_header_data, null);

        // Обработка телефона
        final EditText phoneEdit = (EditText) headerLayout.findViewById(R.id.edit_phone);
        final Button phoneButton = (Button) headerLayout.findViewById(R.id.button_phone);
        phoneEdit.setText(person.getPhoneWithSms());
        isValidPhone(phoneEdit);
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = phoneEdit.getText().toString();
                if (isValidPhone(phoneEdit) && !person.getPhoneWithSms().equals(phone)) {
                    phoneButton.setVisibility(View.VISIBLE);
                } else {
                    phoneButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPhone(phoneButton, phoneEdit.getText().toString(), headerLayout);
            }
        });

        // Обработка email
        final EditText emailEdit = (EditText) headerLayout.findViewById(R.id.edit_email);
        final Button emailButton = (Button) headerLayout.findViewById(R.id.button_email);
        emailEdit.setText(person.getEmail());
        isValidEmail(emailEdit);
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = emailEdit.getText().toString();
                if (isValidEmail(emailEdit) && !email.equals(person.getEmail())) {
                    emailButton.setVisibility(View.VISIBLE);
                } else {
                    emailButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewEmail(emailButton, emailEdit.getText().toString(), headerLayout);
            }
        });

        // Поле Серии паспорта
        final EditText passSerEdit = (EditText) headerLayout.findViewById(R.id.edit_pass_ser);
        passSerEdit.setText(Settings.getCurrentPassportSerial());
        if (passSerEdit.getText().toString().equals("")) {
            passSerEdit.setText("Серія");
            passSerEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && passSerEdit.getText().toString().equals("Серія")) {
                        passSerEdit.setText("");
                    }
                }
            });
        }
        isValidSerial(passSerEdit);
        passSerEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ser = passSerEdit.getText().toString();
                if (isValidSerial(passSerEdit)) {
                    Settings.setCurrentPassportSerial(ser);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Поле Номера паспорта
        final EditText passNumEdit = (EditText) headerLayout.findViewById(R.id.edit_pass_num);
        passNumEdit.setText(Settings.getCurrentPassportNumber());
        if (passNumEdit.getText().toString().equals("")) {
            passNumEdit.setText("Номер");
            passNumEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && passNumEdit.getText().toString().equals("Номер")) {
                        passNumEdit.setText("");
                    }
                }
            });
        }
        isValidPassNum(passNumEdit);
        passNumEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ser = passNumEdit.getText().toString();
                if (isValidPassNum(passNumEdit)) {
                    Settings.setCurrentPassportNumber(ser);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setView(headerLayout)
                .setPositiveButton("Далі", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean phone = phoneEdit.getCurrentTextColor() == COLOR_GREEN;
                        boolean email = emailEdit.getCurrentTextColor() == COLOR_GREEN;
                        boolean ser = passSerEdit.getCurrentTextColor() == COLOR_GREEN;
                        boolean num = passNumEdit.getCurrentTextColor() == COLOR_GREEN;

                        if (phone && email && ser && num) {
                            dialog.dismiss();
                            header = Zayavleniya.getHeader(
                                    person,
                                    passSerEdit.getText().toString(),
                                    passNumEdit.getText().toString(),
                                    phoneEdit.getText().toString(),
                                    emailEdit.getText().toString()
                            );

                            if (type == CREATE_EMAIL) {
                                showWithoutDate(type, zayava);
                            } else askDate(type, zayava);

                        } else {
                            makeSimpleSnackBar("Заповніть правильно всі поля, будь-ласка", headerLayout);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void askDate(final MailType type, final Zayava zayava) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker_zayava, null);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        final boolean itsMailToSupport = type.getEmail().equals("support@o3.ua");
        if (itsMailToSupport){
            datePicker.setMinDate(new GregorianCalendar().getTimeInMillis());
        }else {
            datePicker.setMinDate(new GregorianCalendar().getTimeInMillis()+86400000);
        }

        TextView titleText = new TextView(context);
        titleText.setText("Вкажіть дату зміни");
        titleText.setGravity(Gravity.CENTER);
        titleText.setTextSize(24);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setTextColor(COLOR_GREEN);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setView(datepickerLayout)
                .setCustomTitle(titleText)
                .setPositiveButton("Далі", null)
                .setCancelable(true)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (itsMailToSupport){
                            if (TarifFragment.isShoosenTodayOrFuture(datePicker)){
                                String date = Utilits.parseUkrTime(
                                        datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth()
                                );
                                showWithDate(type, date, zayava);
                                dialog.dismiss();
                            }else {
                                makeSimpleSnackBar("Минуле не можно вибирати", datePicker);
                            }
                        }else {
                            if (TarifFragment.isShoosenFutureDay(datePicker)){
                                String date = Utilits.parseUkrTime(
                                        datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth()
                                );
                                showWithDate(type, date, zayava);
                                dialog.dismiss();
                            }else {
                                makeSimpleSnackBar("Сьогодні або вчора не можно вибирати", datePicker);
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }


    private void showWithDate(MailType type, String date, Zayava zayava) {
        if (type == REAL_IP) {
            sendEmail(type, Zayavleniya.realIP(date, zayava), true);
        } else if (type == DISABLE_REAL_IP) {
            sendEmail(type, Zayavleniya.realIPOff(date, zayava), true);
        } else if (type == DOP_IP) {
            sendEmail(type, Zayavleniya.dopIP(date, zayava), true);
//        } else if (type == CHANGE_DEAL) {

        } else if (type == CHANGE_TARIF) {
            showTarifChoise(type, date);
        } else if (type == CHANGE_IP) {
            sendEmail(type, Zayavleniya.changeIP(date, zayava), true);
        }
    }

    private void showTarifChoise(final MailType type, final String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        View tarifLayout = LayoutInflater.from(context).inflate(R.layout.alert_documents_change_tarif, null);
        final EditText textOldTarif = (EditText) tarifLayout.findViewById(R.id.edit_tarif_old);
        final EditText textNewTarif = (EditText) tarifLayout.findViewById(R.id.edit_tarif_new);
        final LinearLayout lin = (LinearLayout) tarifLayout.findViewById(R.id.lay_for_tarifs);
        builder.setView(tarifLayout);
        final AlertDialog dialog = builder.create();
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                progressDialogShow();
                try {
                    String serviseFound = "";
                    String serviceId = "";
                    List<Servise> services = DbCache.getServises();

                    for (Servise service : services) {
                        if (service.getType() == 5) {
                            serviseFound = service.getPay_type_name();
                            serviceId = String.valueOf(service.getId());
                        }
                    }
                    final List<AvailableTarif> availableTarifs = GetInfo.getAvailableTarifs(serviceId);

                    final String oldTarif = serviseFound;
                    hideProgressDialog();
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            textOldTarif.setText(oldTarif);
                            for (final AvailableTarif availableTarif : availableTarifs) {
                                View layForNewTarif = LayoutInflater.from(context).inflate(R.layout.alert_item_tarif_choice, null);
                                TextView name = (TextView) layForNewTarif.findViewById(R.id.text_name);
                                name.setText(availableTarif.getName());
                                name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sendEmail(type, Zayavleniya.changeTarif(date,
                                                textOldTarif.getText().toString(),
                                                availableTarif.getName().toString()),
                                                false);
                                        dialog.dismiss();
                                    }
                                });
                                lin.addView(layForNewTarif);
                            }
                        }
                    });
                } catch (Exception ignored) {
                }
            }
        });


        dialog.show();
    }



    private void showWithoutDate(MailType type, Zayava zayava) {
        if (type == CREATE_EMAIL) {
            askEmailWantedName(type, zayava);
        }
//        } else if (type == COMPENSATION){
//        } else if (type == WRONG_PAY) {
//        } else if (type == SMTP) {//
//        }
    }


    private void askEmailWantedName(final MailType type, Zayava zayava) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        View emailLayout = LayoutInflater.from(context).inflate(R.layout.item_document_ask_email, null);
        final TextView login = (TextView) emailLayout.findViewById(R.id.text_login);
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (login.getText().toString().equals("Логін")) {
                    login.setText("");
                }
            }
        });
        final TextView password = (TextView) emailLayout.findViewById(R.id.text_password);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (password.getText().toString().equals("Пароль")) {
                    password.setText("");
                }
            }
        });
        builder.setView(emailLayout);
        builder.setPositiveButton("Далі", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                sendEmail(type, Zayavleniya.createEmail(
                        login.getText().toString(),
                        password.getText().toString()), true);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendEmail(final MailType type, final String message, final boolean useStandartFooter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(new StringBuilder()
                .append("Зараз у вас відкриється ваша поштова програма. ")
                .append("\nМожете ознайомитись з текстом заяви, та потім просто натисніть \"Відправити\". ")
                .append("\nМи намагаємось відповідати якомога швидше. ")
                .append("\nВи обов'язково отримаєте відповідь протягом дня")
                .append("\nДякуємо за розуміння!").toString());
        builder.setView(textLayout);

        builder.setPositiveButton("Далі", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", type.getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, type.getSubject() + " Угода " + person.getCard());
                String message2;
                if (useStandartFooter) {
                    message2 = header + message + footer;
                } else {
                    message2 = header + message;
                }

                emailIntent.putExtra(Intent.EXTRA_TEXT, message2);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

    }

    private boolean isValidPhone(EditText editText) {
        String phone = editText.getText().toString();
        Matcher regexMatcher = Pattern.compile("^\\d{10}$").matcher(phone);
        boolean valid = regexMatcher.find();
        setFieldIsValid(editText, valid);
        return valid;
    }

    private boolean isValidEmail(EditText editText) {
        String email = editText.getText().toString();
        Matcher regexMatcher = Pattern.compile(".+[@].+[.].+").matcher(email);
        boolean valid = regexMatcher.find() && !email.contains(" ");
        setFieldIsValid(editText, valid);
        return valid;
    }

    private boolean isValidSerial(EditText editText) {
        String serial = editText.getText().toString();
        Matcher regexMatcher = Pattern.compile("^[а-я|А-Я]{2}$").matcher(serial);
        boolean valid = regexMatcher.find();
        setFieldIsValid(editText, valid);
        return valid;
    }

    private boolean isValidPassNum(EditText editText) {
        String passNum = editText.getText().toString();
        Matcher regexMatcher = Pattern.compile("^\\d{6}$").matcher(passNum);
        boolean valid = regexMatcher.find();
        setFieldIsValid(editText, valid);
        return valid;
    }

    private void setFieldIsValid(EditText editText, boolean valid) {
        if (valid) {
            editText.setTextColor(COLOR_GREEN);
        } else {
            editText.setTextColor(COLOR_RED);
        }
    }

    private void saveNewPhone(final Button button, final String phone1, final View viewForSnackBar) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                progressDialogShow();
                try {
                    Phone phone = null;
                    for (Phone phone1 : person.getPhones()) {
                        if (phone1.getSmsInform() == 1) phone = phone1;
                    }
                    if (SendInfo.changeSmsNumber(phone1, phone)) {
                        progressDialogStopAndShowMessage("Номер збережено", viewForSnackBar);
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                button.setVisibility(View.GONE);
                            }
                        });
                    } else
                        progressDialogStopAndShowMessage("Помилка. Номер невірний.", viewForSnackBar);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            button.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialogStopAndShowMessage("Помилка. Нема з'єднання.", viewForSnackBar);
                }
            }
        });
    }

    private void saveNewEmail(final Button button, final String email, final View viewForSnackBar) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                progressDialogShow();
                try {
                    if (SendInfo.changeEmail(email)) {
                        progressDialogStopAndShowMessage("Email збережено", viewForSnackBar);
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                button.setVisibility(View.GONE);
                            }
                        });
                    } else
                        progressDialogStopAndShowMessage("Помилка. Email невірний.", viewForSnackBar);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            button.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialogStopAndShowMessage("Помилка. Нема з'єднання.", viewForSnackBar);
                }
            }
        });
    }


}
