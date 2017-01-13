package ua.adeptius.myo3.fragments;

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
import android.widget.TextView;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.MailType;
import ua.adeptius.myo3.model.Person;
import ua.adeptius.myo3.model.Phone;
import ua.adeptius.myo3.model.Zayavleniya;
import ua.adeptius.myo3.utils.Settings;
import ua.adeptius.myo3.utils.Utilits;

import static ua.adeptius.myo3.model.MailType.ACTIVATION_INTERNET;
import static ua.adeptius.myo3.model.MailType.CHANGE_DEAL;
import static ua.adeptius.myo3.model.MailType.CHANGE_IP;
import static ua.adeptius.myo3.model.MailType.CHANGE_TARIF;
import static ua.adeptius.myo3.model.MailType.COMPENSATION;
import static ua.adeptius.myo3.model.MailType.CREATE_EMAIL;
import static ua.adeptius.myo3.model.MailType.DISABLE_REAL_IP;
import static ua.adeptius.myo3.model.MailType.DOP_IP;
import static ua.adeptius.myo3.model.MailType.REAL_IP;
import static ua.adeptius.myo3.model.MailType.STOP_INTERNET;
import static ua.adeptius.myo3.model.MailType.WRONG_PAY;


public class DocumentFragment extends BaseFragment {

    private Person person;
    private String header;
    private String footer;

    @Override
    void setAllSettings() {
        titleText = "Заяви";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
        titleImage = R.drawable.background_main1;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        person = GetInfo.getPersonInfo();
        footer = Zayavleniya.getFooter();
    }

    @Override
    void processIfOk() {
        draw();
        hideAllViewsInMainScreen();
        animateScreen();
    }

    private void draw() {
        askAgree(REAL_IP, "Вартість послуги 30 грн/щомісячно.");


    }

    private void askAgree(final MailType type, String agree){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        TextView titleView = new TextView(context);
        titleView.setText("Погодження");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(agree);
        builder.setView(textLayout);

        builder.setPositiveButton("Далі", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                askHeaderData(type);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void askHeaderData(final MailType type) {
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


                            if (type == CREATE_EMAIL || type == CHANGE_IP || type == COMPENSATION || type == WRONG_PAY) {
                                showWithoutDate(type);
                            }else askDate(type);

                        } else {
                            makeSimpleSnackBar("Заповніть всі поля", headerLayout);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void askDate(final MailType type){
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker_zayava, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        Button todayButton = (Button)  datepickerLayout.findViewById(R.id.button_today);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String today = Utilits.getUkrDateNow();
                showWithDate(type, today);
            }
        });
        builder.setCancelable(true);
        builder.setView(datePicker);

        TextView titleText = new TextView(context);
        titleText.setText("Коли нам обробити заяву?");
        titleText.setGravity(Gravity.CENTER);
        titleText.setTextSize(24);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setTextColor(COLOR_BLUE);

        builder.setCustomTitle(titleText);
        builder.setView(datePicker);
        builder.setPositiveButton("Далі", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String date = Utilits.parseUkrTime(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth()
                );
                showWithDate(type, date);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showWithDate(MailType type, String date){
        if (type == REAL_IP) {

        } else if (type == DISABLE_REAL_IP) {

        } else if (type == DOP_IP) {

        } else if (type == ACTIVATION_INTERNET) {

        } else if (type == CHANGE_DEAL) {

        } else if (type == STOP_INTERNET) {

        } else if (type == CHANGE_TARIF) {

        }
    }


    private void showWithoutDate(MailType type) {
        if (type == CREATE_EMAIL){

        } else if (type == CHANGE_IP){

        } else if (type == COMPENSATION){

        } else if (type == WRONG_PAY) {

        }
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
                try {
                    Phone phone = null;
                    for (Phone phone1 : person.getPhones()) {
                        if (phone1.getSmsInform() == 1) phone = phone1;
                    }
                    if (SendInfo.changeSmsNumber(phone1, phone)) {
                        makeSimpleSnackBar("Номер змінено", viewForSnackBar);
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                button.setVisibility(View.GONE);
                            }
                        });
                    } else
                        makeSimpleSnackBar("Помилка. Номер невірний.", viewForSnackBar);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            button.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    makeSimpleSnackBar("Помилка. Нема з'єднання.", viewForSnackBar);
                }
            }
        });

    }

    private void saveNewEmail(final Button button, final String email, final View viewForSnackBar) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (SendInfo.changeEmail(email)) {
                        makeSimpleSnackBar("Email змінено", viewForSnackBar);
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                button.setVisibility(View.GONE);
                            }
                        });
                    } else
                        makeSimpleSnackBar("Помилка. Email невірний.", viewForSnackBar);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            button.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    makeSimpleSnackBar("Помилка. Нема з'єднання.", viewForSnackBar);
                }
            }
        });
    }

    private void sendEmail(String recipient, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", recipient, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject + "Угода " + person.getCard());
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
