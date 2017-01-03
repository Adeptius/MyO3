package ua.adeptius.myo3.activities.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.utils.Utilits;


public class CreditFragment extends BaseFragment {

    private String creditStatus;
    Button activateButton;
    private boolean garantServiceEnabled;

    @Override
    void init() {
        titleText = "Кредит довіри";
        activateButton = getButton(R.id.activate_button);
//        activateButton.setVisibility(View.GONE);
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        creditStatus = GetInfo.getCreditStatus();
        garantServiceEnabled = GetInfo.isGarantedServiceEnabled().equals("enabled");
    }

    @Override
    void processIfOk() {
        if (garantServiceEnabled) {
            descriptionText = "Безкоштовна послуга, яка вмикає інтернет строком на 10 діб";
        } else {
            descriptionText = "Безкоштовна послуга, яка вмикає інтернет строком на 5 діб";
        }
        setTitle(titleText, descriptionText);
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
                if (SendInfo.reActivateCredit()) {
                    makeSimpleSnackBar("Зачекайте, послуга відновлюється", mainLayout);
                    try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                    reloadFragment();
                } else {
                    makeSimpleSnackBar("Трапилась помилка. Можливо недостатньо коштів", mainLayout);
                }
            }
        });
    }

    private void activateNow() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                if (SendInfo.activateCredit()) {
                    makeSimpleSnackBar("10 хвилин активація..", mainLayout);
                    try {Thread.sleep(300);} catch (InterruptedException ignored) {}
                    reloadFragment();
                } else {
                    makeSimpleSnackBar("Трапилась помилка", mainLayout);
                }
            }
        });
    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_credit;
    }

    @Override
    int setLayoutId() {
        return R.id.credit_main_layout;
    }

    @Override
    public void onClick(View v) {

    }
}
