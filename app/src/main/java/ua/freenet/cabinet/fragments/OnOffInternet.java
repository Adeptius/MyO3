package ua.freenet.cabinet.fragments;


import android.view.View;
import android.widget.Button;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

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
        final Button worldButton = getButton(R.id.world_button);
        final Button internetButton = getButton(R.id.internet_button);

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
                if (worldButton.getText().equals("Вимкнути")){
                    new MyAlertDialogBuilder(context)
                            .setTitleText("Увага!")
                            .setMessage("Просте вимикання не скасовує абонентську плату.\nДля збереження коштів призупиніть послугу.")
                            .setPositiveButtonWithRunnableForHandler("Вимкнути", new Runnable() {
                                @Override
                                public void run() {
                                    switchWorld();
                                }
                            })
                            .setNegativeButtonWithRunnableForHandler("Призупинити", new Runnable() {
                                @Override
                                public void run() {
                                    goTo(new TarifFragment());
                                }
                            })
                            .createAndShow();
                }else {
                    switchWorld();
                }
            }
        });

        internetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internetButton.getText().equals("Вимкнути")){
                    new MyAlertDialogBuilder(context)
                            .setTitleText("Увага!")
                            .setMessage("Просте вимикання не скасовує абонентську плату.\nДля збереження коштів призупиніть послугу.")
                            .setPositiveButtonWithRunnableForHandler("Вимкнути", new Runnable() {
                                @Override
                                public void run() {
                                    switchAll();
                                }
                            })
                            .setNegativeButtonWithRunnableForHandler("Призупинити", new Runnable() {
                                @Override
                                public void run() {
                                    goTo(new TarifFragment());
                                }
                            })
                            .createAndShow();
                }else {
                    switchAll();
                }
            }
        });
    }

    private void switchWorld(){
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

    private void switchAll(){
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
}
