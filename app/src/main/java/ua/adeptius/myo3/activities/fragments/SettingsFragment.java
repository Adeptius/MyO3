package ua.adeptius.myo3.activities.fragments;

import android.view.View;

import ua.adeptius.myo3.R;


public class SettingsFragment extends BaseFragment {

    @Override
    void setAllSettings() {
        titleText = "";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.background_main1;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {


        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        draw();
        animateScreen();
    }

    private void draw() {

    }

    @Override
    void processIfOk() {

    }

    @Override
    public void onClick(View v) {

    }
}
