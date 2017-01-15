package ua.adeptius.myo3.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.DbCache;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.BonusServiceSpending;
import ua.adeptius.myo3.utils.Utilits;

public class BonusFragment extends BaseFragment {

    private int bonuses;
    private boolean layIsHidden = true;
    private boolean signedPublicCard;
    private boolean confirmedBonus;

    private List<BonusServiceSpending> serviceSpendings;


    @Override
    void setAllSettings() {
        titleText = "Бонуси";
        descriptionText = "Система нарахування бонусів по програмі лояльності";
        titleImage = R.drawable.background_main1;
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        boolean[] boo = DbCache.getBonusesStatus();
        signedPublicCard = boo[0];
        confirmedBonus = boo[1];
        if (signedPublicCard && confirmedBonus){
            bonuses = DbCache.getCountOfBonuses();
            serviceSpendings = DbCache.getBonusesSpending();
        }
    }

    @Override
    void processIfOk() {
        draw();
        hideAllViewsInMainScreen();
        animateScreen();
    }

    private void draw() {
        if (signedPublicCard && confirmedBonus){ // если всё подписано
            View layout = LayoutInflater.from(context).inflate(R.layout.item_bonus_main, null);
            mainLayout.addView(layout);

            final Button hideButton = (Button) layout.findViewById(R.id.button_hide);
            final LinearLayout hideLayout = (LinearLayout) layout.findViewById(R.id.hide_layout);
            hideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layIsHidden) {
                        hideButton.setText("Сховати");
                        hideLayout.setVisibility(View.VISIBLE);
                        layIsHidden = false;
                    } else {
                        hideButton.setText("Як нараховуються бонуси?");
                        hideLayout.setVisibility(View.GONE);
                        layIsHidden = true;
                    }
                }
            });

            TextView bonusesText = (TextView) layout.findViewById(R.id.text_bonuses);
            bonusesText.setText(String.valueOf(bonuses));

            TextView textHowTo = (TextView) layout.findViewById(R.id.text_how_to);
            StringBuilder sb = new StringBuilder();
            sb.append("Нарахування БОНУСІВ відбувається при розрахунку за придбання або оплату послуг компанії, враховуючи те, скільки часу ви наш абонент.")
                    .append("\n6-12 місяців - нарахування 2% бонусів від внесеної абонплати.")
                    .append("\n6 місяців - 4%")
                    .append("\n19 місяців - 6%")
                    .append("\n25 місяців - 8%")
                    .append("\n31 місяць та більше - 10%")
                    .append("\n")
                    .append("\nБОНУСИ нараховуються при своєчасному внесенні абонплати (до першого числа, щоб не допускати заборгованість).")
                    .append("\n")
                    .append("\nБонуси нараховуються моментально.")
                    .append("\n")
                    .append("\nНакопичені БОНУСИ не можуть бути переведені в грошовий еквівалент чи видані Учаснику готівковими коштами.")
                    .append("\n")
                    .append("\nОдин БОНУС дорівнює 1 одній гривні.");
            textHowTo.setText(sb.toString());

            for (final BonusServiceSpending serviceSpending : serviceSpendings) {
                View spendingLayout = LayoutInflater.from(context).inflate(R.layout.item_bonus_service, null);
                spendingLayout.setVisibility(View.GONE);
                Button payButton = (Button) spendingLayout.findViewById(R.id.button_pay);
                TextView cost = (TextView) spendingLayout.findViewById(R.id.cost);
                TextView name = (TextView) spendingLayout.findViewById(R.id.name);
                TextView coment = (TextView) spendingLayout.findViewById(R.id.coment);

                String money = String.valueOf(serviceSpending.getMoney());

                cost.setText(money);
                name.setText(serviceSpending.getNote());
                coment.setText("Вже сплачено бонусами: " + serviceSpending.getBonus() + " грн");

                int canPay = Math.abs(serviceSpending.getMoney()) - serviceSpending.getBonus();
                if (bonuses < canPay) {
                    canPay = bonuses;
                }
                final int finalCanPay = canPay;
                payButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askHowMuch(finalCanPay, serviceSpending.getS_id());
                    }
                });
                if (bonuses == 0) payButton.setVisibility(View.GONE);
                mainLayout.addView(spendingLayout);
            }
        }else if (signedPublicCard && !confirmedBonus) { // если подписано, но бонусы не подключены
            View activateLayout = LayoutInflater.from(context).inflate(R.layout.item_bonus_activate, null);

            Button activateButton = (Button) activateLayout.findViewById(R.id.button_activate);
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askActivate();
                }
            });

            Button fullRules = (Button) activateLayout.findViewById(R.id.button_rules);
            fullRules.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "http://o3.ua/content/files/pravila_bonusnoi_sistemi.pdf";
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            mainLayout.addView(activateLayout);


        }else if (!signedPublicCard && !confirmedBonus){ // если договор не подписан.
            View iditeVCoaLayout = LayoutInflater.from(context).inflate(R.layout.item_bonus_idite_v_coa, null);

            Button button = (Button) iditeVCoaLayout.findViewById(R.id.button_show_dial);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "http://o3.ua/content/files/publichnyj__dogovir.pdf";
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            mainLayout.addView(iditeVCoaLayout);
        }
    }

    private void askActivate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        TextView titleView = new TextView(context);
        titleView.setText("Активація");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText("Ви погоджуєтесь з правилами?");
        builder.setView(textLayout);

        builder.setCustomTitle(titleView);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (SendInfo.activateBonusProgram()) {
                            makeSimpleSnackBar("Хвилинку, триває активація!", mainLayout);
                            try {Thread.sleep(5000);} catch (InterruptedException ignored) {}
                            DbCache.markBonusesStatusOld();
                            reloadFragment();
                        } else {
                            makeSimpleSnackBar("Трапилась помилка", mainLayout);
                            try {Thread.sleep(TIME_TO_WAIT_BEFORE_UPDATE);} catch (InterruptedException ignored) {}
                            reloadFragment();
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

    }

    public void askHowMuch(final int canPay, final String id) {
        final LinearLayout vlayout = new LinearLayout(context);
        vlayout.setOrientation(LinearLayout.VERTICAL);
        vlayout.setPadding(5, 5, 5, 5);

        TextView first = new TextView(context);
        first.setText("Ви можете використати " + canPay + " бонусів");
        first.setTypeface(null, Typeface.BOLD);
        first.setTextSize(18);
        vlayout.addView(first);

        LinearLayout hlayout = new LinearLayout(context);
        hlayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView before = new TextView(context);
        before.setText("Сплатити бонусами");
        before.setTypeface(null, Typeface.BOLD);
        before.setTextSize(18);

        TextView after = new TextView(context);
        after.setText("грн");
        after.setTypeface(null, Typeface.BOLD);
        after.setTextSize(18);

        final EditText text = new EditText(context);
        text.setWidth((int) Utilits.dpToPixel(50, context));
        text.setCursorVisible(true);
        text.setText(String.valueOf(canPay));
        text.hasFocus();

        hlayout.addView(before);
        hlayout.addView(text);
        hlayout.addView(after);
        vlayout.addView(hlayout);

        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setView(vlayout)
                .setPositiveButton("Сплатити", null) //Set to null. We override the onclick
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int shoosenPay = Integer.parseInt(text.getText().toString());
                        if (shoosenPay > canPay || shoosenPay < 1) {
                            makeSimpleSnackBar("Ця сумма неправильна", vlayout);
                        } else { // если сумма правильна
                            final HashMap<String, String> map = new HashMap<>();
                            map.put("bonus", String.valueOf(shoosenPay));
                            map.put("s_id", id);
                            map.put("p_id", "");

                            EXECUTOR.submit(new Runnable() {
                                @Override
                                public void run() {
                                    if (SendInfo.spendBonuses(map)){ // отправляю запрос

                                        makeSimpleSnackBar("Сплачено!", vlayout);
                                        try{Thread.sleep(TIME_TO_WAIT_BEFORE_UPDATE);}catch (Exception ignored){}
                                        DbCache.markCountOfBonusesOld();
                                        DbCache.markBonusesSpendingOld();
                                        HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        });
                                        reloadFragment();
                                    }else { // если вернулась неудача
                                        HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        });
                                        makeSimpleSnackBar("Трапилась помилка", mainLayout);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.show();
    }
}
