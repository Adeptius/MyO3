package ua.freenet.cabinet.fragments;

import android.view.View;

import ua.freenet.cabinet.R;


public class SettingsFragment extends BaseFragment {

    @Override
    void setAllSettings() {
        titleText = "";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
        titleImage = R.drawable.background_main1;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {

    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }

    private void draw() {

    }

    @Override
    public void onClick(View v) {

    }
}
