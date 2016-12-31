package ua.adeptius.myo3.activities.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.activities.MainActivity;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Mailing;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.model.persons.Phone;

//TODO поле имэйл не влазит полностью
public class MainFragment extends BaseFragment {

    private TextView pib, contractNumber, city, street, house, room, age, money, smsInfo,
            email, password, fee;
    private CheckBox newsCheckBox, worksCheckBox, akciiCheckBox;
    private ImageView editPass, editSms, editEmail;
    private List<Ip> ips;
    private Person person;
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
        ips = GetInfo.getIP();
        person = GetInfo.getPersonInfo();
        mountlyFee = GetInfo.getMountlyFee();
    }

    @Override
    void processIfOk() {
        setPersonData(person, ips, mountlyFee);
        animateScreen();
    }

    @Override
    void processIfFail() {

    }

    private void setPersonData(Person person, List<Ip> ips, String mountlyFee) {
        MainActivity.descriptionTextView.setText(
                person.getUkrName() + ", тут відображається основна інформація по вашій угоді");
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
                    reloadFragment();
                }
            });
        } else if (view.equals(worksCheckBox)) {
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    SendInfo.changeMailings(5);
                    reloadFragment();
                }
            });
        } else if (view.equals(akciiCheckBox)) {
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    SendInfo.changeMailings(6);
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
