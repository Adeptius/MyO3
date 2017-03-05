package ua.freenet.cabinet.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.Ip;
import ua.freenet.cabinet.model.Mailing;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.model.Phone;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;
import ua.freenet.cabinet.utils.Settings;


public class MainFragment extends BaseFragment implements View.OnClickListener {

    private TextView pib, contractNumber, city, street, house, room, age, money, smsInfo,
            email, password, fee;
    private CheckBox newsCheckBox, worksCheckBox, akciiCheckBox;
    private ImageView editPass, editSms, editEmail;
    private Person person;
    private List<Ip> ips;
    private String mountlyFee;


    @Override
    void setAllSettings() {
        titleText = "Головна";
        descriptionText = "Тут відображається основна інформація по вашій угоді";
        fragmentId = R.layout.fragment_main;
        titleImage = R.drawable.background_main1;
        layoutId = R.id.scroll_view_main;
    }


    @Override
    void init() {
        pib = getTextView(R.id.pib);
        contractNumber = getTextView(R.id.contractNumber);
        city = getTextView(R.id.city);
        street = getTextView(R.id.street);
        house = getTextView(R.id.house);
        room = getTextView(R.id.room);
        age = getTextView(R.id.age);
        fee = getTextView(R.id.fee);
        money = getTextView(R.id.money);
        smsInfo = getTextView(R.id.sms_info);
        email = getTextView(R.id.email);
        password = getTextView(R.id.password);
        newsCheckBox = getCheckBox(R.id.checkBoxNews);
        worksCheckBox = getCheckBox(R.id.checkBoxworks);
        akciiCheckBox = getCheckBox(R.id.checkBoxAction);
        editPass = getImageView(R.id.imageView_edit_password);
        editSms = getImageView(R.id.imageView_edit_sms);
        editEmail = getImageView(R.id.imageView_edit_email);
        editPass.setOnClickListener(this);
        editSms.setOnClickListener(this);
        editEmail.setOnClickListener(this);
        newsCheckBox.setOnClickListener(this);
        worksCheckBox.setOnClickListener(this);
        akciiCheckBox.setOnClickListener(this);
        password.setOnClickListener(this);
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        person = DbCache.getPerson();
        ips = DbCache.getIps();
        mountlyFee = DbCache.getMountlyFeefromLK();
    }

    @Override
    void processIfOk() {
        setPersonData(person, ips, mountlyFee);
        animateScreen();
    }

    private void setPersonData(Person person, List<Ip> ips, String mountlyFee) {
        descriptionText = person.getUkrName() + ", тут відображається основна інформація по вашій угоді";
        updateTitle();
        pib.setText(person.getLastname() + " " + person.getName() + " " + person.getSurname());
        contractNumber.setText(person.getCard());
        city.setText(person.getAddress().getCityNameUa());
        street.setText(person.getAddress().getStrNameUa());
        house.setText(person.getAddress().gethName());
        room.setText(person.getAddress().getAddressFlatName());
        age.setText(person.getAge() + " місяців");
        String many = String.valueOf(person.getCurrent());
        int pos = many.indexOf(".") + 2;
        boolean cont = many.contains(".");
        int len = many.length();

        if (cont && len >= pos) {
            many = many.substring(0, many.indexOf(".") + 2);
        }

        if (person.getCurrent() > 0) {
            money.setTextColor(COLOR_GREEN);
        } else if (person.getCurrent() < person.getStopsum()) {
            money.setTextColor(COLOR_RED);
        }
        money.setText(many + " грн");
        money.setOnClickListener(this);
        fee.setText(mountlyFee + " грн");
        smsInfo.setText(person.getPhoneWithSms());
        email.setText(person.getEmail());
        password.setText("Показати");
        for (Mailing mailing : person.getMailing()) {
            if (mailing.getTitle().equals("Новости"))
                newsCheckBox.setChecked(mailing.isSubscribe());
            if (mailing.getTitle().equals("Плановые работы"))
                worksCheckBox.setChecked(mailing.isSubscribe());
            if (mailing.getTitle().equals("Акции"))
                akciiCheckBox.setChecked(mailing.isSubscribe());
        }
        showIps(ips);
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                showWarningIfNewAbon();
                showWarningIfInternetInactive();
                showMessageOfTheDay();
            }
        });
    }


    private void showWarningIfNewAbon() {
        if (person.getAge() < 1 && person.getStopsum() < 10) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    String message = "Шановний абонент!\n" +
                            "Нагадуємо, що першу оплату необхідно внести у повному обсязі " +
                            "незалежно від стану вашого балансу.\n" +
                            "Будь ласка, поповніть рахунок на " + (Integer.parseInt(mountlyFee) + 1) + " грн. " +
                            "(1 грн за підключення плюс ваш тариф " + mountlyFee + " грн)";

                    new MyAlertDialogBuilder(context)
                            .setTitleText("Перша сплата")
                            .setMessageText(message)
                            .setPositiveButtonForClose("Гаразд")
                            .createAndShow();
                }
            });
        }
    }


    private void showWarningIfInternetInactive() {
        if (person.getStopsum() > person.getCurrent()) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("На вашому рахунку недостатньо коштів.\n");
                    sb.append("Ваша абонплата: ").append(mountlyFee).append(" грн.\n");
                    String notAnoth = String.valueOf(Math.abs(person.getCurrent()));
                    if (notAnoth.contains("."))
                        notAnoth = notAnoth.substring(0, notAnoth.indexOf(".") + 2);
                    sb.append("На рахунку не вистачило: ").append(notAnoth).append(" грн для оплати цього місяця.\n");
                    sb.append("Перейти до перегляду історії проплат?");

                    new MyAlertDialogBuilder(context)
                            .setTitleText("Інтернет не активний")
                            .setMessageText(sb.toString())
                            .setNegativeButtonForClose("Ні")
                            .setPositiveButtonWithRunnableForExecutor("Так",
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            goTo(new BalanceFragment());
                                        }
                                    }
                            ).createAndShow();
                }
            });
        }
    }

    private void showMessageOfTheDay() {
        try {
            URL url = new URL(DocumentFragment.URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String s;
            final StringBuilder stringBuilder = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                s = s.replace("\\n", "\n");
                stringBuilder.append(s);
            }

            if (!"".equals(stringBuilder.toString())) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(stringBuilder.toString());
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(newsCheckBox)) {
            tumblerMailing(2);
        } else if (view.equals(worksCheckBox)) {
            tumblerMailing(5);
        } else if (view.equals(akciiCheckBox)) {
            tumblerMailing(6);
        } else if (view.equals(editPass)) {
            changePassword();
        } else if (view.equals(editSms)) {
            changeSmsNumber();
        } else if (view.equals(editEmail)) {
            changeEmail();
        } else if (view.equals(password)) {
            password.setText(Settings.getCurrentPassword());
        } else if (view.equals(money)) {
            goTo(new BalanceFragment());
        }
    }

    private void tumblerMailing(final int mailing) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                progressDialogShow();
                SendInfo.changeMailings(mailing);
                DbCache.markPersonOld();
                progressDialogWaitStopShowMessageReload("Збережено", newsCheckBox);
            }
        });
    }

    public void showIps(List<Ip> ips) {
        LinearLayout layout = (LinearLayout) baseView.findViewById(R.id.network_settings); // getting lay from array that not include in main lay
        int count = layout.getChildCount();
        for (int i = count - 1; i > 0; i--) {
            layout.removeViewAt(i);
        }

        for (int i = 0; i < ips.size(); i++) {
            Ip ip = ips.get(i);
            String number = i == 0 ? "" : " " + (i + 1);

            LinearLayout ipLayout = new LinearLayout(context);
            LinearLayout maskLayout = new LinearLayout(context);
            LinearLayout gatewayLayout = new LinearLayout(context);
            ipLayout.setOrientation(LinearLayout.HORIZONTAL);
            maskLayout.setOrientation(LinearLayout.HORIZONTAL);
            gatewayLayout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(ipLayout, MATCH_WRAP);
            layout.addView(maskLayout, MATCH_WRAP);
            layout.addView(gatewayLayout, MATCH_WRAP);

            TextView ipTitle = new TextView(context);
            TextView maskTitle = new TextView(context);
            TextView gatewayTitle = new TextView(context);
            ipTitle.setText("IP" + number + ":");
            maskTitle.setText("Маска" + number + ":");
            gatewayTitle.setText("Шлюз" + number + ":");
            ipTitle.setTextSize(18);
            maskTitle.setTextSize(18);
            gatewayTitle.setTextSize(18);
            ipTitle.setTypeface(null, Typeface.BOLD);
            maskTitle.setTypeface(null, Typeface.BOLD);
            gatewayTitle.setTypeface(null, Typeface.BOLD);

            TextView ipValue = new TextView(context);
            TextView maskValue = new TextView(context);
            TextView gatewayValue = new TextView(context);
            ipValue.setText(ip.getIp());
            maskValue.setText(ip.getMask());
            gatewayValue.setText(ip.getGateway());
            ipValue.setTextSize(18);
            maskValue.setTextSize(18);
            gatewayValue.setTextSize(18);

            ipLayout.addView(ipTitle, MATCH_WRAP_WEIGHT150);
            ipLayout.addView(ipValue, MATCH_WRAP_WEIGHT1);
            maskLayout.addView(maskTitle, MATCH_WRAP_WEIGHT150);
            maskLayout.addView(maskValue, MATCH_WRAP_WEIGHT1);
            gatewayLayout.addView(gatewayTitle, MATCH_WRAP_WEIGHT150);
            gatewayLayout.addView(gatewayValue, MATCH_WRAP_WEIGHT1);


            LinearLayout separator = new LinearLayout(context);
            separator.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(separator, MATCH_WRAP);
            TextView separatorText = new TextView(context);
            separatorText.setText("");
            separator.addView(separatorText);
        }
        LinearLayout dns1Layout = new LinearLayout(context);
        LinearLayout dns2Layout = new LinearLayout(context);
        layout.addView(dns1Layout);
        layout.addView(dns2Layout);

        TextView dns1Title = new TextView(context);
        TextView dns2Title = new TextView(context);
        dns1Title.setText("DNS перший:");
        dns2Title.setText("DNS другий:");
        dns1Title.setTextSize(18);
        dns2Title.setTextSize(18);
        dns1Title.setTypeface(null, Typeface.BOLD);
        dns2Title.setTypeface(null, Typeface.BOLD);

        TextView dns1Value = new TextView(context);
        TextView dns2Value = new TextView(context);
        dns1Value.setText(ips.get(0).getDns1());
        dns2Value.setText(ips.get(0).getDns2());
        dns1Value.setTextSize(18);
        dns2Value.setTextSize(18);

        dns1Layout.addView(dns1Title, MATCH_WRAP_WEIGHT150);
        dns1Layout.addView(dns1Value, MATCH_WRAP_WEIGHT1);
        dns2Layout.addView(dns2Title, MATCH_WRAP_WEIGHT150);
        dns2Layout.addView(dns2Value, MATCH_WRAP_WEIGHT1);
    }

    private void changeSmsNumber() {
        final LinearLayout messageLayout = new LinearLayout(context);
        messageLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(context);
        textView.setText("  +380");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(18);
        final EditText text = new EditText(context);
        messageLayout.addView(textView, WRAP_WRAP);
        messageLayout.addView(text, MATCH_WRAP);
        text.setCursorVisible(true);
        text.hasFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        final MyAlertDialogBuilder builder = new MyAlertDialogBuilder(context);
        builder.setTitleText("Новий номер")
                .setView(messageLayout)
                .createShowAndSetPositiveForExecutor("Зберегти", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        try {
                            Phone phone = null;
                            for (Phone phone1 : person.getPhones()) {
                                if (phone1.getSmsInform() == 1) phone = phone1;
                            }
                            if (SendInfo.changeSmsNumber(text.getText().toString().trim(), phone)) {
                                DbCache.markPersonOld();
                                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                                progressDialogWaitStopShowMessageReload("Номер змінено", messageLayout);
                                builder.close();
                            } else
                                progressDialogStopAndShowMessage("Помилка. Номер невірний.", messageLayout);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialogStopAndShowMessage("Помилка. Нема з'єднання.", messageLayout);
                        }
                    }
                });
    }

    private void changeEmail() {
        final EditText text = new EditText(context);
        text.setCursorVisible(true);
        text.hasFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        final MyAlertDialogBuilder builder = new MyAlertDialogBuilder(context);
        builder.setTitleText("Новий email")
                .setView(text)
                .createShowAndSetPositiveForExecutor("Зберегти", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        try {
                            if (SendInfo.changeEmail(text.getText().toString().trim())) {
                                DbCache.markPersonOld();
                                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                                progressDialogWaitStopShowMessageReload("Email змінено", text);
                                builder.close();
                            } else
                                progressDialogStopAndShowMessage("Помилка. Email невірний.", text);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialogStopAndShowMessage("Помилка. Нема з'єднання.", text);
                        }
                    }
                });
    }

    private void changePassword() {
        final EditText text = new EditText(context);
        text.setCursorVisible(true);
        text.hasFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        final MyAlertDialogBuilder builder = new MyAlertDialogBuilder(context);
        builder.setView(text)
                .setTitleText("Новий пароль")
                .setMessage("Пароль має містити мінімум 6 символів та одну букву з цифрою.")
                .createShowAndSetPositiveForExecutor("Зберегти", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        try {
                            if (SendInfo.changePassword(text.getText().toString().trim())) {
                                DbCache.markPersonOld();
                                Settings.setCurrentPassword(text.getText().toString().trim());
                                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                                progressDialogWaitStopShowMessageReload("Пароль змінено", text);
                                builder.close();
                            } else
                                progressDialogStopAndShowMessage("Помилка. Пароль невірний.", text);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialogStopAndShowMessage("Помилка. Нема з'єднання.", text);
                        }
                    }
                });
    }
}
