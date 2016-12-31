package ua.adeptius.myo3.activities.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.utils.Utilits;


public class CreditFragment extends BaseFragment {

    private HashMap<String, String> map;
    Button activateButton;

    @Override
    void init() {
        titleText = "Кредит довіри";
        descriptionText = "Безкоштовна послуга, яка вмикає інтернет строком на 5 діб";
//        mainLayout = (LinearLayout) baseView.findViewById(R.id.credit_main_layout);
        activateButton = getButton(R.id.activate_button);
        activateButton.setVisibility(View.GONE);
    }

    @Override
    void doInBackground() throws Exception {
        map = GetInfo.getCreditStatus();
    }

    @Override
    void processIfOk() {
        draw();
    }

    private void draw() {
        TextView textView = getTextView(R.id.text_title);
        StringBuilder sb = new StringBuilder();
        sb.append("Будь-ласка, погасіть заборгованість у строк п'яти діб, щоб мати змогу і надалі");
        sb.append(" користуватися цією послугою.");
        textView.setText(sb.toString());

        TextView textDetails = getTextView(R.id.text_view_for_details);
        sb = new StringBuilder();
        sb.append("Якщо кредит довіри недоступний - ви можете його відновити оплативши одноразово 30 грн");
        sb.append("\n\nПри підключенні послуги \"Гарантований сервіс\" - " +
                "строк послуги збільшується до 10 днів");
        textDetails.setText(sb.toString());

        TextView activeText = getTextView(R.id.active_left);

        activateButton.setVisibility(View.VISIBLE);
        if(map.get("active").startsWith("20")){
            activateButton.setText("Послуга вже активна");
            activateButton.setClickable(false);
            try{
                Date date = Utilits.getDate(map.get("active"));
                long timeEnable = date.getTime();
                long timeCurrent = new Date().getTime();
                int hoursLeft = (int) (timeEnable-timeCurrent)/1000/60/60 % 24;
                int daysLeft = (int) (timeEnable-timeCurrent)/1000/60/60/24;
                activeText.setText("Залишилось " + daysLeft + " дні, " + hoursLeft + " годин" );
                activeText.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activate();
                }
            });
        }

        if (map.get("allow").equals("false")){
            activateButton.setText("не доступно");
            activateButton.setClickable(false);

        }


    }



    private void activate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Активувати?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(context).inflate(R.layout.alert_builder_message, null);
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

    private void activateNow() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                if (SendInfo.activateCredit()) {
                    makeSimpleSnackBar("10 хвилин активація..", mainLayout);
                } else {
                    makeSimpleSnackBar("Трапилась помилка", mainLayout);
                }
            }
        });
    }


    @Override
    void processIfFail() {

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
