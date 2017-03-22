package ua.freenet.cabinet.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.activities.MainActivity;
import ua.freenet.cabinet.activities.PdfActivity;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.GetInfo;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.BonusServiceSpending;
import ua.freenet.cabinet.model.PdfDocument;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.model.PdfDocument.*;

public class BonusFragment extends HelperFragment {

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
    }

    @Override
    void doInBackground() throws Exception {
        boolean[] boo = DbCache.getBonusesStatus();
        signedPublicCard = boo[0];
        confirmedBonus = boo[1];
        if (signedPublicCard && confirmedBonus) {
            bonuses = GetInfo.getCountOfBonuses();
            serviceSpendings = GetInfo.getBonusesSpending();
        }
        prepareViews();
    }

    @Override
    void processIfOk() {
        showAndAnimatePreparedViews();
    }

    private void prepareViews() {
        if (signedPublicCard && confirmedBonus) { // если всё подписано
            View layout = LayoutInflater.from(context).inflate(R.layout.item_bonus_main, null);
            preparedViews.add(layout);

            final Button hideButton = (Button) layout.findViewById(R.id.button_hide);
            final LinearLayout hideLayout = (LinearLayout) layout.findViewById(R.id.hide_layout);
            hideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, PdfActivity.class);
//                    startActivity(intent);
                    if (layIsHidden) {
                        hideButton.setText("Сховати");
                        expand(hideLayout);
                        layIsHidden = false;
                    } else {
                        hideButton.setText("Як нараховуються бонуси?");
                        collapse(hideLayout);
                        layIsHidden = true;
                    }
                }
            });

            TextView bonusesText = (TextView) layout.findViewById(R.id.text_bonuses);
            bonusesText.setText(String.valueOf(bonuses));

            TextView textHowTo = (TextView) layout.findViewById(R.id.text_how_to);
            String sb = "Нарахування БОНУСІВ відбувається при розрахунку за придбання або оплату послуг компанії, враховуючи те, скільки часу ви наш абонент." +
                    "\n6 місяців - нарахування 2% бонусів від внесеної абонплати." +
                    "\n13 місяців - 4%" +
                    "\n19 місяців - 6%" +
                    "\n25 місяців - 8%" +
                    "\n31 місяць та більше - 10%" +
                    "\n" +
                    "\nБОНУСИ нараховуються при своєчасному внесенні абонплати (до першого числа, щоб не допускати заборгованість)." +
                    "\n" +
                    "\nБонуси нараховуються моментально." +
                    "\n" +
                    "\nНакопичені БОНУСИ не можуть бути переведені в грошовий еквівалент чи видані Учаснику готівковими коштами." +
                    "\n" +
                    "\nОдин БОНУС дорівнює 1 одній гривні.";
            textHowTo.setText(sb);

            for (final BonusServiceSpending serviceSpending : serviceSpendings) {
                if (serviceSpending.getNote().startsWith("!-Приостановлен")){
                    continue;
                }
                View spendingLayout = LayoutInflater.from(context).inflate(R.layout.item_bonus_service, null);
                spendingLayout.setVisibility(View.GONE);
                Button payButton = (Button) spendingLayout.findViewById(R.id.button_pay);


                TextView cost = (TextView) spendingLayout.findViewById(R.id.cost);
                String money = String.valueOf(Math.abs(serviceSpending.getMoney()));
                cost.setText(money);

                TextView name = (TextView) spendingLayout.findViewById(R.id.name);
                name.setText(serviceSpending.getNote());


                TextView coment = (TextView) spendingLayout.findViewById(R.id.coment);
                coment.setText("Вже сплачено бонусами: " + serviceSpending.getBonus() + " грн");
                if (serviceSpending.getBonus() == 0) {
                    coment.setVisibility(View.GONE);
                }

                int canPay = Math.abs(serviceSpending.getMoney()) - serviceSpending.getBonus();
                if (bonuses < canPay) {
                    canPay = bonuses;
                }
                final int finalCanPay = canPay;
                payButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askHowMuch(finalCanPay, serviceSpending);
                    }
                });
                if (bonuses == 0) payButton.setVisibility(View.GONE);
                preparedViews.add(spendingLayout);
            }
        } else if (signedPublicCard) { // если подписано, но бонусы не подключены
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
                    openInBrowser("http://o3.ua/content/files/pravila_bonusnoi_sistemi.pdf");
//                    Intent intent = new Intent(context, PdfActivity.class);
//                    startActivity(intent);
                }
            });
            preparedViews.add(activateLayout);

        } else if (!confirmedBonus) { // если договор не подписан.
            View iditeVCoaLayout = LayoutInflater.from(context).inflate(R.layout.item_bonus_idite_v_coa, null);

            Button button = (Button) iditeVCoaLayout.findViewById(R.id.button_show_dial);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openInBrowser("http://o3.ua/content/files/publichnyj__dogovir.pdf");
                }
            });
            preparedViews.add(iditeVCoaLayout);
        }
    }

    private void askActivate() {
        new MyAlertDialogBuilder(context)
                .setTitleText("Активація")
                .setMessage("Ви погоджуєтесь з правилами?")
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        if (SendInfo.activateBonusProgram()) {
                            DbCache.markBonusesStatusOld();
                            progressDialogWaitStopShowMessageReload("Хвилинку, триває активація!", mainLayout);
                        } else {
                            progressDialogWaitStopShowMessageReload("Трапилась помилка", mainLayout);
                        }
                    }
                }).createAndShow();
    }

    public void askHowMuch(final int canPay, final BonusServiceSpending spending) {
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

        final MyAlertDialogBuilder dialog = new MyAlertDialogBuilder(context);
        dialog.setView(vlayout)
                .createShowAndSetPositiveForExecutor("Сплатити", new Runnable() {
                    @Override
                    public void run() {
                        int shoosenPay = Integer.parseInt(text.getText().toString());
                        if (shoosenPay > canPay || shoosenPay < 1) {
                            makeSimpleSnackBar("Ця сумма неправильна", vlayout);
                        } else { // если сумма правильна
                            final HashMap<String, String> map = new HashMap<>();
                            map.put("bonus", String.valueOf(shoosenPay));

                            if (!spending.getP_id().equals("")){
                                map.put("p_id", spending.getP_id());
                            }else{
                                map.put("s_id", spending.getS_id());
                            }
                            progressDialogShow();
                            if (SendInfo.spendBonuses(map)) { // отправляю запрос
                                DbCache.markPersonOld();
                                DbCache.markCountOfBonusesOld();
                                DbCache.markBonusesSpendingOld();
                                progressDialogWaitStopShowMessageReload("Сплачено!", vlayout);
                                dialog.closeWithHandler();
                            } else { // если вернулась неудача
                                dialog.closeWithHandler();
                                progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                            }
                        }
                    }
                });
    }
}
