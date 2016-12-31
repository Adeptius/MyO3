package ua.adeptius.myo3.activities.fragments;


import android.view.View;
import android.widget.LinearLayout;

import ua.adeptius.myo3.R;

public class GarantServiceFragment extends BaseFragment {


    @Override
    void doInBackground() throws Exception {

    }

    @Override
    void processIfOk() {

    }

    @Override
    void processIfFail() {

    }

    @Override
    void init() {
        titleText = "Кредит довіри";
        descriptionText = "Безкоштовна послуга, яка вмикає інтернет строком на 5 діб";
//        mainLayout = (LinearLayout) baseView.findViewById(R.id.main_content_garant_service);

    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_garant_service;
    }

    @Override
    int setLayoutId() {
        return R.id.main_content_garant_service;
    }

    @Override
    public void onClick(View v) {

    }
}
