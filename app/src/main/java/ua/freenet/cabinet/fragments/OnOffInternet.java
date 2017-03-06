package ua.freenet.cabinet.fragments;


import android.view.View;
import android.widget.Button;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;

public class OnOffInternet extends HelperFragment {

    private boolean internetIsActive;
    private boolean worldIsActive;

    @Override
    void setAllSettings() {
        titleText = "Вимикання інтернету";
        descriptionText = "Вимикання інтернету у цьому меню не скасовує абонентську плату";
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
        Boolean[] status = DbCache.getInternetSwitches();
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
                        progressDialogShow();
                        if (SendInfo.internetSwitchWorld()) {
                            DbCache.markInternetSwitchesOld();
                         progressDialogWaitStopShowMessageReload("Виконано.", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка.", mainLayout);
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
                        progressDialogShow();
                        if (SendInfo.internetSwitchAll()) {
                            DbCache.markInternetSwitchesOld();
                            progressDialogWaitStopShowMessageReload("Виконано.", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка.", mainLayout);
                        }
                    }
                });
            }
        });
    }
}
