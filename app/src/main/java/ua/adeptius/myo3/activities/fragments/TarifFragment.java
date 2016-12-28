package ua.adeptius.myo3.activities.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.persons.Servise;


public class TarifFragment extends BaseFragment {

    private LinearLayout mainLayout;
    private List<Servise> services;



    @Override
    void doInBackground() throws Exception {
        services = GetInfo.getServises();
    }

    @Override
    void processIfOk() {
        drawAllServises(services);
    }

    private void drawAllServises(List<Servise> services) {
        for (Servise service : services) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_tarif_item, null);
            mainLayout.addView(itemView);

            TextView serviceName = (TextView) itemView.findViewById(R.id.service_name);
            serviceName.setText(service.getMyServiceName());

            TextView serviceCost = (TextView) itemView.findViewById(R.id.service_cost);
            serviceCost.setText(String.valueOf(service.getMonth()));

            Button changeButton = (Button) itemView.findViewById(R.id.service_change_button);
            if (!service.is_allow_change()){
                changeButton.setVisibility(View.GONE);
            }

            Button stopButton = (Button) itemView.findViewById(R.id.service_stop_button);
            if (!service.is_allow_suspend()){
                stopButton.setVisibility(View.GONE);
            }

            TextView serviceTypeName = (TextView) itemView.findViewById(R.id.service_type);
            serviceTypeName.setText(service.getMyTypeName());




        }
    }

    @Override
    void processIfFail() {

    }

    @Override
    void init() {
        titleText = "Тарифи";
        descriptionText = "Ваші підключені тарифи та послуги";
        mainLayout = (LinearLayout) baseView.findViewById(R.id.scroll_view_balance);
        startBackgroundTask();
    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_balance;
    }

    @Override
    int setLayoutId() {
        return R.id.scroll_view_balance;
    }

    @Override
    public void onClick(View v) {

    }
}
