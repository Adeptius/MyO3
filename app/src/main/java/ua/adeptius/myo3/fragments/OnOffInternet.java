package ua.adeptius.myo3.fragments;


import android.view.View;
import android.widget.Button;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;

public class OnOffInternet extends BaseFragment {

    private boolean internetIsActive;
    private boolean worldIsActive;

    @Override
    void setAllSettings() {
        titleText = "Вимикання інтернету";
        descriptionText = "Вимикання інтернету у цьому меню не впливає на абонентську плату";
        fragmentId = R.layout.fragment_on_off_internet;
        titleImage = R.drawable.background_main1;
        layoutId = R.id.main_on_off;
    }

    @Override
    void init() {


        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        Boolean[] status = GetInfo.getInternetSwitches();
        internetIsActive = status[0];
        worldIsActive = status[1];
    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
        updateTitle();
    }

    private void draw() {
        Button worldButton = getButton(R.id.world_button);
        Button internetButton = getButton(R.id.internet_button);

        if (internetIsActive){
            internetButton.setText("Вимкнути");
        }else {
            internetButton.setText("Увімкнути");
        }

        if (worldIsActive){
            worldButton.setText("Вимкнути");
        }else {
            worldButton.setText("Увімкнути");
        }
        worldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (SendInfo.internetSwitchWorld()) {
                            makeSimpleSnackBar("Виконано.", mainLayout);
                            try {Thread.sleep(500);} catch (InterruptedException ignored) {}
                            reloadFragment();
                        } else {
                            makeSimpleSnackBar("Трапилась помилка.", mainLayout);
                        }
                    }
                });
            }
        });

        internetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (SendInfo.internetSwitchAll()) {
                            makeSimpleSnackBar("Виконано.", mainLayout);
                            try {Thread.sleep(500);} catch (InterruptedException ignored) {}
                            reloadFragment();
                        } else {
                            makeSimpleSnackBar("Трапилась помилка.", mainLayout);
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onClick(View v) {

    }
}
