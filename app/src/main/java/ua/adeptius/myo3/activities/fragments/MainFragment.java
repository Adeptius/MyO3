package ua.adeptius.myo3.activities.fragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.activities.MainActivity;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Mailing;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.model.persons.Phone;


public class MainFragment extends BaseFragment {

    private TextView pib, contractNumber, city, street, house, room, age, money, smsInfo,
            email, password, textip, mask, gateway, dns1, dns2;
    private CheckBox newsCheckBox, worksCheckBox, akciiCheckBox;
    private ImageView editPass, editSms, editEmail;
    private Ip ip;
    private Person person;

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
        money = getTextView(R.id.money);
        smsInfo = getTextView(R.id.sms_info);
        email = getTextView(R.id.email);
        password = getTextView(R.id.password);
        newsCheckBox = getCheckBox(R.id.checkBoxNews);
        worksCheckBox = getCheckBox(R.id.checkBoxworks);
        akciiCheckBox = getCheckBox(R.id.checkBoxAction);
        textip = getTextView(R.id.text_ip);
        mask = getTextView(R.id.text_mask);
        gateway = getTextView(R.id.text_gateway);
        dns1 = getTextView(R.id.text_dns1);
        dns2 = getTextView(R.id.text_dns2);
        editPass = getImageView(R.id.imageView_edit_password);
        editSms = getImageView(R.id.imageView_edit_sms);
        editEmail = getImageView(R.id.imageView_edit_email);
        editPass.setOnClickListener(this);
        editSms.setOnClickListener(this);
        editEmail.setOnClickListener(this);
        newsCheckBox.setOnClickListener(this);
        worksCheckBox.setOnClickListener(this);
        akciiCheckBox.setOnClickListener(this);
        startBackgroundTask();
    }

    @Override
    void doInBackground() throws Exception {
        ip = GetInfo.getIP();
        person = GetInfo.getPersonInfo();
    }

    @Override
    void processIfOk() {
        setPersonData(person, ip);
    }

    @Override
    void processIfFail() {

    }

    private void setPersonData(Person person, Ip ip) {
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

        String phoneNumber = "";
        for (Phone phone : person.getPhones()) {
            if (phone.getSmsInform() == 1) phoneNumber = phone.getPhone();
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
        newsCheckBox.setClickable(false);
        worksCheckBox.setClickable(false);
        akciiCheckBox.setClickable(false);

        textip.setText(ip.getIp());
        mask.setText(ip.getMask());
        gateway.setText(ip.getGateway());
        dns1.setText(ip.getDns1());
        dns2.setText(ip.getDns2());
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
        if (view.equals(newsCheckBox)) {

        } else if (view.equals(worksCheckBox)) {

        } else if (view.equals(akciiCheckBox)) {

        } else if (view.equals(editPass)) {

        } else if (view.equals(editSms)) {
            changeSmsNumber(view);
        } else if (view.equals(editEmail)) {
            changeEmail(view);
        } else if (view.equals(password)) {
            password.setText(Settings.getCurrentPassword());
        }
    }

    public void makeSimpleSnackBar(String message, View text){
        HANDLER.post(() -> Snackbar
                .make(text, message, Snackbar.LENGTH_LONG).show());
    }

    private void changeSmsNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        EditText text = new EditText(context);
        text.setCursorVisible(true);
        text.hasFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setCancelable(true);
        builder.setView(text);
        builder.setMessage("Введіть новий номер телефону:");
        builder.setPositiveButton("Змінити", (dialog, which) -> {
            imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Phone phone = null;
                        for (Phone phone1 : person.getPhones()) {
                            if (phone1.getSmsInform()==1) phone = phone1;
                        }
                        if (SendInfo.changeSmsNumber(text.getText().toString(), phone)) {
                            makeSimpleSnackBar("Номер змінено", view);
                        }else
                            makeSimpleSnackBar("Помилка. Номер невірний.", view);
                    } catch (Exception e) {
                        e.printStackTrace();
                        makeSimpleSnackBar("Помилка. Нема з'єднання.", view);
                    }
                    dialog.dismiss();
                }
            }).start();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeEmail(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        EditText text = new EditText(context);
        text.setCursorVisible(true);
        text.hasFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setCancelable(true);
        builder.setView(text);
        builder.setMessage("Введіть новий email:");
        builder.setPositiveButton("Змінити", (dialog, which) -> {
            imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
            EXECUTOR.submit(() -> {
                try {
                    if (SendInfo.changeEmail(text.getText().toString())) {
                        makeSimpleSnackBar("Email змінено", view);
                    }else
                        makeSimpleSnackBar("Помилка. Email невірний.", view);
                } catch (Exception e) {
                    e.printStackTrace();
                    makeSimpleSnackBar("Помилка. Нема з'єднання.", view);
                }
                dialog.dismiss();
            });
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
