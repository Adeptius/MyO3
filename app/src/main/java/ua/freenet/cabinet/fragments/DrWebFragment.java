package ua.freenet.cabinet.fragments;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.DrWebSubscribe;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

public class DrWebFragment extends HelperFragment {

    List<DrWebSubscribe> subscribes;

    @Override
    void setAllSettings() {
        titleText = "";
        descriptionText = "";
        fragmentId = R.layout.fragment_dr_web;
        titleImage = R.drawable.background_drweb2;
        layoutId = R.id.main_dr_web;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        subscribes = DbCache.getDrWebServices();
    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }

    private void draw() {
        Button activateButton = getButton(R.id.button_activate_antivirus);
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivateMessage();
            }
        });

        if (subscribes != null) {
            for (final DrWebSubscribe subscribe : subscribes) {
                View drWebLayout = LayoutInflater.from(context).inflate(R.layout.item_drweb, null);


                TextView cost = (TextView) drWebLayout.findViewById(R.id.dr_web_cost);
                cost.setText(subscribe.getMyCost());

                TextView name = (TextView) drWebLayout.findViewById(R.id.drweb_name);
                name.setText(subscribe.getMyName());

                if (subscribe.getDchange() != null && !subscribe.getDchange().equals("")) {
                    TextView coment = (TextView) drWebLayout.findViewById(R.id.service_coment);
                    coment.setVisibility(View.VISIBLE);
                    coment.setText("Буде вимкнено " + subscribe.getDchange()
                            .substring(0, subscribe.getDchange().indexOf(" ")));
                }


                Button installWindows = (Button) drWebLayout.findViewById(R.id.install_windows);
                installWindows.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, subscribe.getUrl());
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });

                Button installAndroid = (Button) drWebLayout.findViewById(R.id.install_android);
                if (subscribe.getAndroid_url() == null) {
                    installAndroid.setVisibility(View.GONE);
                } else {
                    installAndroid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            String url = subscribe.getAndroid_url();
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                }

                Button disable = (Button) drWebLayout.findViewById(R.id.disable_dr_web);
                disable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deativate(subscribe.getSid());
                    }
                });


                drWebLayout.setVisibility(View.GONE);
                mainLayout.addView(drWebLayout, 0);
            }
        }
    }

    private void deativate(final String sid) {
        new MyAlertDialogBuilder(context)
                .setTitleText("Відключити послугу?")
                .setMessage("Послуга буде вимкнена в останній день місяця.")
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        if (SendInfo.deactivateDrWeb(sid)) {
                            DbCache.markMountlyFeeOld();
                            DbCache.markDrWebServicesOld();
                            progressDialogWaitStopShowMessageReload("Буде відключено!", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                        }
                    }
                }).createAndShow();
    }

    private void showActivateMessage() {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_dr_web_choise, null);
        ImageView classic = (ImageView) layout.findViewById(R.id.activate_classic);
        ImageView standart = (ImageView) layout.findViewById(R.id.activate_standart);
        ImageView premium = (ImageView) layout.findViewById(R.id.activate_premium);
        ImageView mobile = (ImageView) layout.findViewById(R.id.activate_mobile);

        final MyAlertDialogBuilder dialog = new MyAlertDialogBuilder(context)
                .setTitleTextWithWhiteBackground("Оберіть підписку:")
                .setView(layout)
                .createAndShow();

        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("classic");
                dialog.close();
            }
        });
        standart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("standart");
                dialog.close();
            }
        });
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("premium");
                dialog.close();
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("mobile");
                dialog.close();
            }
        });
    }

    private void activate(final String version) {
        String s = "Підключити версію ";
        if (version.equals("classic")) s += "класік";
        else if (version.equals("standart")) s += "стандарт";
        else if (version.equals("premium")) s += "преміум";
        else if (version.equals("mobile")) s += "мобільний";
        s += "?";

        new MyAlertDialogBuilder(context)
                .setTitleText(s)
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        if (SendInfo.activateDrWeb(version)) {
                            DbCache.markMountlyFeeOld();
                            DbCache.markDrWebServicesOld();
                            progressDialogWaitStopShowMessageReload("Підключено!", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                        }
                    }
                }).createAndShow();
    }
}
