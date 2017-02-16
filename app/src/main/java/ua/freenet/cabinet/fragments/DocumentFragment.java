package ua.freenet.cabinet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.GetInfo;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.AvailableTarif;
import ua.freenet.cabinet.model.MailType;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.model.Phone;
import ua.freenet.cabinet.model.Servise;
import ua.freenet.cabinet.model.Zayava;
import ua.freenet.cabinet.model.Zayavleniya;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.model.MailType.CHANGE_DEAL;
import static ua.freenet.cabinet.model.MailType.CHANGE_IP;
import static ua.freenet.cabinet.model.MailType.CHANGE_TARIF;
import static ua.freenet.cabinet.model.MailType.CREATE_EMAIL;
import static ua.freenet.cabinet.model.MailType.DISABLE_REAL_IP;
import static ua.freenet.cabinet.model.MailType.DOP_IP;
import static ua.freenet.cabinet.model.MailType.REAL_IP;
import static ua.freenet.cabinet.model.MailType.STOP_TARIF;
import static ua.freenet.cabinet.model.MailType.WRONG_PAY;


public class DocumentFragment extends BaseFragment {

    private Person person;
    private String header;
    private String footer;
    private static final int CAMERA_REQUEST = 1888;
    private String wrongPayMessage = "";

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
            askAgree(type, zayava, "Зміна тарифу. \nДо речі: подивіться, чи зможете ви самостійно перейти на інший тариф у розділі \"Підключені послуги\"");
        } else if (type == CREATE_EMAIL) {
            askAgree(type, zayava, "Створення поштової скриньки. Безкоштовно надається тільки одна скринька. \nУВАГА!\n У разі відключення від мережі \"Фрінет\" скринька буде автоматично видалена!");
        } else if (type == CHANGE_IP) {
            askAgree(type, zayava, checkMoneyMessage(zayava));
        } else if (type == CHANGE_DEAL) {
            askAgree(type, zayava, "Ми раді, що ви залишитесь з нами!");
        } else if (type == STOP_TARIF) {
            askAgree(type, zayava, "Призупинка можлива на строк не менш ніж 10 днів");
        } else if (type == WRONG_PAY) {
            askAgree(type, zayava, "Перед продовженням сфотографуйте ваш чек (квитанцію), будь ласка.");
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
        if (itsMailToSupport) {
            datePicker.setMinDate(new GregorianCalendar().getTimeInMillis());
        } else if (type != MailType.WRONG_PAY) { // если это неправильная оплата, ограничений в выборе даты быть не должно.
            datePicker.setMinDate(new GregorianCalendar().getTimeInMillis() + 86400000);
        }

        TextView titleText = new TextView(context);
        titleText.setText(zayava.getHeaderText());
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
                        if (type == MailType.WRONG_PAY) {// если это неправильная оплата, ограничений в выборе даты быть не должно.
                            String date = Utilits.parseUkrTime(
                                    datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth()
                            );
                            showWithDate(type, date, zayava);
                            dialog.dismiss();
                        } else if (itsMailToSupport) {
                            if (TarifFragment.isShoosenTodayOrFuture(datePicker)) {
                                String date = Utilits.parseUkrTime(
                                        datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth()
                                );
                                showWithDate(type, date, zayava);
                                dialog.dismiss();
                            } else {
                                makeSimpleSnackBar("Минуле не можно вибирати", datePicker);
                            }
                        } else {
                            if (TarifFragment.isShoosenFutureDay(datePicker)) {
                                String date = Utilits.parseUkrTime(
                                        datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth()
                                );
                                showWithDate(type, date, zayava);
                                dialog.dismiss();
                            } else {
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
            sendEmail(type, Zayavleniya.realIP(date, zayava), true, null);
        } else if (type == DISABLE_REAL_IP) {
            sendEmail(type, Zayavleniya.realIPOff(date, zayava), true, null);
        } else if (type == DOP_IP) {
            sendEmail(type, Zayavleniya.dopIP(date, zayava), true, null);
        } else if (type == CHANGE_DEAL) {
            showChangeDialQuestions(type, date);
        } else if (type == CHANGE_TARIF) {
            showTarifChoise(type, date);
        } else if (type == CHANGE_IP) {
            sendEmail(type, Zayavleniya.changeIP(date, zayava), true, null);
        } else if (type == WRONG_PAY) {
            proceedAskWrongPay(date);
        } else if (type == STOP_TARIF) {
            askEndDate(date);
        }
    }

    private void askEndDate(final String startDate) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker_zayava, null);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);

        TextView titleText = new TextView(context);
        titleText.setText("Вкажіть кінцеву дату призупинки");
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
                        String endDate = Utilits.parseUkrTime(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );
                        String message = Zayavleniya.stopInternet(startDate, endDate, person.getCard());
                        sendEmail(STOP_TARIF, message, false, null);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void proceedAskWrongPay(final String date) {
        View tarifLayout = LayoutInflater.from(context).inflate(R.layout.alert_item_wrong_pay, null);
        final EditText dogovor = (EditText) tarifLayout.findViewById(R.id.edit_dog);
        final EditText suma = (EditText) tarifLayout.findViewById(R.id.edit_sum);
        dogovor.setInputType(InputType.TYPE_CLASS_NUMBER);
        suma.setInputType(InputType.TYPE_CLASS_NUMBER);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setView(tarifLayout)
                .setPositiveButton("Обрати файл", null)
                .setCancelable(true)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sum = suma.getText().toString();
                        String dog = dogovor.getText().toString();
                        wrongPayMessage = Zayavleniya.wrongPay(date, sum, dog);
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void showWithoutDate(MailType type, Zayava zayava) {
        if (type == CREATE_EMAIL) {
            askEmailWantedName(type, zayava);
        }
    }


    private void showChangeDialQuestions(final MailType type, final String date) {
        View tarifLayout = LayoutInflater.from(context).inflate(R.layout.alert_item_change_dial, null);
        final EditText newDog = (EditText) tarifLayout.findViewById(R.id.edit_new_dog);
        final EditText newAddress = (EditText) tarifLayout.findViewById(R.id.edit_new_adress);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setView(tarifLayout)
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
                        String adr = newAddress.getText().toString();
                        String dog = newDog.getText().toString();
                        String message = Zayavleniya.changeDial(date, person.getCard(), adr, dog, person.getAge());
                        sendEmail(type, message, false, null);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }


    private void showTarifChoise(final MailType type, final String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        View tarifLayout = LayoutInflater.from(context).inflate(R.layout.alert_documents_change_tarif, null);
        final EditText textOldTarif = (EditText) tarifLayout.findViewById(R.id.edit_tarif_old);
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
                                                false, null);
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
                        password.getText().toString()), true, null);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendEmail(final MailType type, final String message, final boolean useStandartFooter, final Uri bitmapUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(new StringBuilder()
                .append("Зараз у вас відкриється ваша поштова програма. ")
                .append("\nМожете ознайомитись з текстом заяви, та потім просто натисніть \"Відправити\". ")
                .append("\nМи намагаємось відповідати якомога швидше. ")
                .append("\nВи обов'язково отримаєте відповідь протягом дня.")
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
                if (bitmapUri != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                }
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

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImage = imageReturnedIntent.getData();
        sendEmail(WRONG_PAY, wrongPayMessage, false, selectedImage);
    }
}
