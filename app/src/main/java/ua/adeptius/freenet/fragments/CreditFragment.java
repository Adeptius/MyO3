package ua.adeptius.freenet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import ua.adeptius.freenet.R;
import ua.adeptius.freenet.dao.DbCache;
import ua.adeptius.freenet.dao.SendInfo;
import ua.adeptius.freenet.utils.Utilits;


public class CreditFragment extends BaseFragment {

    private String creditStatus;
    Button activateButton;
    private boolean garantServiceEnabled;

    @Override
    void setAllSettings() {
        titleText = "Кредит довіри";
        descriptionText = "";
        fragmentId = R.layout.fragment_credit;
        titleImage = R.drawable.background_dovira3;
        layoutId = R.id.credit_main_layout;
    }

    @Override
    void init() {
        activateButton = getButton(R.id.activate_button);
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        creditStatus = DbCache.getCreditStatus();
        garantServiceEnabled = DbCache.garantedServiceStatus().equals("enabled");
    }

    @Override
    void processIfOk() {
        int creditDays = 5;
        if (garantServiceEnabled) {
            creditDays = 10;
        }
        descriptionText = "Безкоштовна послуга, яка вмикає інтернет строком на "+creditDays+" діб при заборгованості";
        updateTitle();
        draw();
        animateScreen();
    }

    private void draw() {
        TextView textView = getTextView(R.id.text_title);
        StringBuilder sb = new StringBuilder();
        sb.append("Будь-ласка, погасіть заборгованість у строк ");
        if (garantServiceEnabled) {
            sb.append("десяти");
        } else {
            sb.append("п'яти");
        }
        sb.append(" діб, щоб мати змогу і надалі користуватися цією послугою.");

        textView.setText(sb);

        TextView textDetails = getTextView(R.id.text_view_for_details);
        textDetails.setText(getString(R.string.credit_details));

        TextView activeText = getTextView(R.id.active_left);
        TextView ifCreditLoss = getTextView(R.id.if_credit_loss);

        activateButton.setVisibility(View.VISIBLE);
        if (creditStatus.startsWith("20")) {
            activateButton.setText("Послуга вже активна");
            activateButton.setClickable(false);
            try {
                Date date = Utilits.getDate(creditStatus);
                long timeEnable = date.getTime();
                long timeCurrent = new Date().getTime();
                int hoursLeft = (int) (timeEnable - timeCurrent) / 1000 / 60 / 60 % 24;
                int daysLeft = (int) (timeEnable - timeCurrent) / 1000 / 60 / 60 / 24;
                activeText.setText("Залишилось " + daysLeft + " дні, " + hoursLeft + " годин");
                activeText.setVisibility(View.VISIBLE);
                if (garantServiceEnabled){
                    if (daysLeft==9 && hoursLeft > 22)
                        ifCreditLoss.setText("Якщо доступ не з'явився через 10 хвилин - перезавантажте роутер");
                }else {
                    if (daysLeft==4 && hoursLeft > 22)
                        ifCreditLoss.setText("Якщо доступ не з'явився через 10 хвилин - перезавантажте роутер");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("restoring".equals(creditStatus)) {
            activateButton.setVisibility(View.VISIBLE);
            activateButton.setClickable(false);
            activateButton.setText("Відновення...");
        } else if ("disabled".equals(creditStatus)) {
            getTextView(R.id.text_title).setVisibility(View.GONE);
            activateButton.setText("Відновити кредит довіри");
            ifCreditLoss.setVisibility(View.VISIBLE);
            ifCreditLoss.setText("Нажаль, кредит довіри недоступний. Можливо ви раніше не оплатили " +
                    "своєчасно. Проте ви можете його відновити.");
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReActivateMessage();
                }
            });
        } else {
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activate();
                }
            });
        }
    }


    private void showReActivateMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Відновити?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText("Вартість відновлення кредиту довіри - 30 грн. Кошти мають бути на рахунку.");
        builder.setView(textLayout);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                reActivateNow();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void activate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Активувати?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText("Послуга активується до 10 хвилин.");
        builder.setView(textLayout);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                activateNow();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void reActivateNow() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                progressDialogShow();
                if (SendInfo.reActivateCredit()) {
                    DbCache.markCreditStatusOld();
                    progressDialogWaitStopShowMessageReload("Зачекайте, послуга відновлюється", mainLayout);
                } else {
                    progressDialogStopAndShowMessage("Трапилась помилка. Можливо недостатньо коштів", mainLayout);
                }
            }
        });
    }

    private void activateNow() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                progressDialogShow();
                if (SendInfo.activateCredit()) {
                    DbCache.markCreditStatusOld();
                    progressDialogWaitStopShowMessageReload("10 хвилин активація..", mainLayout);
                } else {
                    progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}