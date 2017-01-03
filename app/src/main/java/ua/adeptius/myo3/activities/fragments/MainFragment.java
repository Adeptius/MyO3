package ua.adeptius.myo3.activities.fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Mailing;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.model.persons.Phone;


public class MainFragment extends BaseFragment {

    private TextView pib, contractNumber, city, street, house, room, age, money, smsInfo,
            email, password, fee;
    private CheckBox newsCheckBox, worksCheckBox, akciiCheckBox;
    private ImageView editPass, editSms, editEmail;
    private boolean creditEnabled;
    private Person person;
    private List<Ip> ips;
    private String mountlyFee;

    @Override
    void init() {
        titleText = "Головна";
        descriptionText = "Тут відображається основна інформація по вашій угоді";
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
        person = GetInfo.getPersonInfo();
        ips = GetInfo.getIP();
        mountlyFee = GetInfo.getMountlyFeefromLK();
        creditEnabled = GetInfo.getCreditStatus().startsWith("20");
    }

    @Override
    void processIfOk() {
        setPersonData(person, ips, mountlyFee);
        animateScreen();
    }

    private void setPersonData(Person person, List<Ip> ips, String mountlyFee) {
        setTitle(titleText, person.getUkrName() +
                ", тут відображається основна інформація по вашій угоді");
        pib.setText(person.getLastname() + " " + person.getName() + " " + person.getSurname());
        contractNumber.setText(person.getCard());
        city.setText(person.getAddress().getCityNameUa());
        street.setText(person.getAddress().getStrNameUa());
        house.setText(person.getAddress().gethName());
        room.setText(person.getAddress().getAddressFlatName());
        age.setText(person.getAge() + " місяців");
        String many = String.valueOf(person.getCurrent());
        many = many.length() > 4 ? many.substring(0, 4) : many;
        money.setText(many + " грн");
        fee.setText(mountlyFee + " грн");

        String phoneNumber = "";
        for (Phone phone : person.getPhones()) {
            if (phone.getSmsInform() == 1) {
                phoneNumber = phone.getPhone();
                phoneNumber = phoneNumber.replaceAll("\\+38", "");
            }
        }
        smsInfo.setText(phoneNumber);
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
        showWarningIfInternetInactive();
    }

    private void showWarningIfInternetInactive() {
        if (person.getStopsum()>person.getCurrent() && !creditEnabled){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setCancelable(true);
            TextView titleView = new TextView(context);
            titleView.setText("Інтернет не активний");
            titleView.setGravity(Gravity.CENTER);
            titleView.setTextSize(24);
            titleView.setTypeface(null, Typeface.BOLD);
            titleView.setTextColor(COLOR_BLUE);
            builder.setCustomTitle(titleView);
            View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
            TextView text = (TextView) textLayout.findViewById(R.id.text);
            StringBuilder sb = new StringBuilder();
            sb.append("На вашому рахунку недостатньо коштів.\n");
            sb.append("Ваша абонплата: ").append(mountlyFee).append(" грн.\n");
            String notAnoth = String.valueOf(Math.abs(person.getCurrent()));
            if (notAnoth.contains(".")) notAnoth = notAnoth.substring(0, notAnoth.indexOf(".")+2);
            sb.append("На рахунку не вистачило: ").append(notAnoth).append(" грн для оплати цього місяця.\n");
            sb.append("Перейти до перегляду історії проплат?");
            text.setText(sb.toString());
            builder.setView(textLayout);
            builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    FragmentManager fm = getFragmentManager();
                    try {
                        fm.beginTransaction().replace(R.id.content_frame, BalanceFragment.class.newInstance()).commit();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected int setFragmentId() {
        return R.layout.fragment_main;
    }

    @Override
    protected int setLayoutId() {
        return R.id.scroll_view_main;
    }

    @Override
    public void onClick(View view) {
        System.out.println(view.getClass());
        System.out.println(view);
        if (view.equals(newsCheckBox)) {
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    SendInfo.changeMailings(2);
                    try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                    reloadFragment();
                }
            });
        } else if (view.equals(worksCheckBox)) {
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    SendInfo.changeMailings(5);
                    try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                    reloadFragment();
                }
            });
        } else if (view.equals(akciiCheckBox)) {
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    SendInfo.changeMailings(6);
                    try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                    reloadFragment();
                }
            });
        } else if (view.equals(editPass)) {
            changePassword(view);
        } else if (view.equals(editSms)) {
            changeSmsNumber(view);
        } else if (view.equals(editEmail)) {
            changeEmail(view);
        } else if (view.equals(password)) {
            password.setText(Settings.getCurrentPassword());
        }
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

    private void changeSmsNumber(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(context);
        textView.setText("  +380");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(18);
        final EditText text = new EditText(context);
        linearLayout.addView(textView, WRAP_WRAP);
        linearLayout.addView(text, MATCH_WRAP);
        text.setCursorVisible(true);
        text.hasFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setCancelable(true);
        builder.setView(linearLayout);
        builder.setMessage("Введіть новий номер телефону:");
        builder.setPositiveButton("Змінити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Phone phone = null;
                            for (Phone phone1 : person.getPhones()) {
                                if (phone1.getSmsInform() == 1) phone = phone1;
                            }
                            if (SendInfo.changeSmsNumber("+380" + text.getText(), phone)) {
                                makeSimpleSnackBar("Номер змінено", view);
                                try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                                reloadFragment();
                            } else
                                makeSimpleSnackBar("Помилка. Номер невірний.", view);
                        } catch (Exception e) {
                            e.printStackTrace();
                            makeSimpleSnackBar("Помилка. Нема з'єднання.", view);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeEmail(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText text = new EditText(context);
        text.setCursorVisible(true);
        text.hasFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setCancelable(true);
        builder.setView(text);
        builder.setMessage("Введіть новий email:");
        builder.setPositiveButton("Змінити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (SendInfo.changeEmail(text.getText().toString())) {
                                makeSimpleSnackBar("Email змінено", view);
                                try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                                reloadFragment();
                            } else
                                makeSimpleSnackBar("Помилка. Email невірний.", view);
                        } catch (Exception e) {
                            e.printStackTrace();
                            makeSimpleSnackBar("Помилка. Нема з'єднання.", view);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changePassword(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText text = new EditText(context);
        text.setCursorVisible(true);
        text.hasFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setCancelable(true);
        builder.setView(text);
        builder.setMessage("Введіть новий пароль (мінімум 6 символів та одна буква з цифрою):");
        builder.setPositiveButton("Змінити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (SendInfo.changePassword(text.getText().toString())) {
                                makeSimpleSnackBar("Пароль змінено", view);
                                Settings.setCurrentPassword(text.getText().toString());
                                try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                                reloadFragment();
                            } else
                                makeSimpleSnackBar("Помилка. Пароль невірний.", view);
                        } catch (Exception e) {
                            e.printStackTrace();
                            makeSimpleSnackBar("Помилка. Нема з'єднання.", view);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
