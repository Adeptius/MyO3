package ua.adeptius.myo3.activities.fragments;

import android.view.View;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Person;


public class MainFragment extends BaseFragment {

    TextView pib, contractNumber, adress, age, money;

    @Override
    void doWork() {
        pib = (TextView) baseView.findViewById(R.id.pib);
        contractNumber = (TextView) baseView.findViewById(R.id.contractNumber);
        adress = (TextView) baseView.findViewById(R.id.adress);
        age = (TextView) baseView.findViewById(R.id.age);
        money = (TextView) baseView.findViewById(R.id.money);

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
    private void setData(Person person){
        HANDLER.post(() -> {
            pib.setText(person.getLastname() + " " + person.getName() + " " + person.getSurname());
            contractNumber.setText(person.getCard());
            String addre = person.getAddress().getCityNameUa() + " " +
                    person.getAddress().getStrNameUa() + " " +
                    person.getAddress().gethName() + " кв. " +
                    person.getAddress().getAddressFlatName();
                    adress.setText(addre);
            age.setText(person.getAge() + " місяців");
            String many = String.valueOf(person.getCurrent());
            many = many.length() > 4 ? many.substring(0,4) : many;
            money.setText(many + " грн");





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
