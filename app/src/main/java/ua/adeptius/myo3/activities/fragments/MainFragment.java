package ua.adeptius.myo3.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Person;


public class MainFragment extends Fragment {

    TextView textView;
    public static final Handler HANDLER = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("init MainFragment");
        View v = inflater.inflate(R.layout.fragment_main,container,false);
        textView = (TextView) v.findViewById(R.id.textView2);
        textView.setText("before main");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Settings.setSessionID(Web.getPhpSession("02514521", "5351301"));
                    final Ip ip = GetInfo.getIP();
                    final Person person = GetInfo.getPersonInfo();
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(person.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return v;
    }
}
