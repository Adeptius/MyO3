package ua.freenet.cabinet.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
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

public class DrWebFragment extends BaseFragment {

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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Відключити послугу?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText("Послуга буде вимкнена в останній день місяця.");
        builder.setView(textLayout);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EXECUTOR.submit(new Runnable() {
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
                });
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showActivateMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Оберіть підписку:");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        titleView.setBackgroundColor(Color.WHITE);
        builder.setCustomTitle(titleView);

        View layout = LayoutInflater.from(context).inflate(R.layout.item_dr_web_choise, null);
        ImageView classic = (ImageView) layout.findViewById(R.id.activate_classic);
        ImageView standart = (ImageView) layout.findViewById(R.id.activate_standart);
        ImageView premium = (ImageView) layout.findViewById(R.id.activate_premium);
        ImageView mobile = (ImageView) layout.findViewById(R.id.activate_mobile);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.show();
        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("classic");
                dialog.dismiss();
            }
        });
        standart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("standart");
                dialog.dismiss();
            }
        });
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("premium");
                dialog.dismiss();
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activate("mobile");
                dialog.dismiss();
            }
        });
    }

    private void activate(final String version) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        String s = "Підключити версію ";
        if (version.equals("classic")) s += "класік";
        else if (version.equals("standart")) s += "стандарт";
        else if (version.equals("premium")) s += "преміум";
        else if (version.equals("mobile")) s += "мобільний";
        s += "?";
        titleView.setText(s);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EXECUTOR.submit(new Runnable() {
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
                });
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

    }
}
