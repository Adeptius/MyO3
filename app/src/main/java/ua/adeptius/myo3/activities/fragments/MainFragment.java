package ua.adeptius.myo3.activities.fragments;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.activities.MainActivity;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Mailing;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.model.persons.Phone;


public class MainFragment extends BaseFragment {

    private TextView pib, contractNumber, city, street, house, room, age, money, smsInfo, email, password;
    private CheckBox newsCheckBox, worksCheckBox, akciiCheckBox;

    @Override
    void doWork() {
        String titleText = "Головна";
        String descriptionText = "Тут відображається основна інформація по вашій угоді";
        MainActivity.titleTextView.setText(titleText);
        MainActivity.descriptionTextView.setText(descriptionText);
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


        new Thread(() -> {
            try {
                Settings.setSessionID(Web.getPhpSession("02514521", "5351301"));
                final Ip ip = GetInfo.getIP();
                final Person person = GetInfo.getPersonInfo();
                setData(person);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setData(Person person) {
        HANDLER.post(() -> {
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
            String pass = "";
            for (int i = 0; i < Settings.getCurrentPassword().length(); i++) {
                pass += "*";
            }
            password.setText(pass);
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

        });
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

    }
}
