package ua.adeptius.myo3.activities.fragments;

import android.view.View;
import android.widget.LinearLayout;

import ua.adeptius.myo3.R;


public class TarifFragment extends BaseFragment {

    LinearLayout mainLayout;

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
        titleText = "Тарифи";
        descriptionText = "Ваші підключені тарифи та послуги";
        mainLayout = (LinearLayout) baseView.findViewById(R.id.scroll_view_news);
        startBackgroundTask();
    }

    @Override
    int setFragmentId() {
        return 0;
    }

    @Override
    int setLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }
}
