package ua.adeptius.myo3.activities.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;

import static ua.adeptius.myo3.R.id.activate_garant_button;
import static ua.adeptius.myo3.R.id.text_title;
import static ua.adeptius.myo3.R.id.text_view_for_details;

public class GarantServiceFragment extends BaseFragment {

    private String serviseStatus;
    private boolean privatHouse;

    @Override
    void init() {
        titleText = "Гарантований сервіс";
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        serviseStatus = GetInfo.isGarantedServiceEnabled();
        privatHouse = GetInfo.getPersonInfo().getAddress().isPrivat();
        prepareScreen();
    }

    private void prepareScreen(){
        TextView titleText = getTextView(text_title);
        StringBuilder sb = new StringBuilder();
        sb.append("Безкоштовний виїзд майстра для:\n");
        sb.append("1. Заміни конекторів, діагностики.\n");
        sb.append("2. Подовження кабелю, або виготовлення пачкорду (до 10м).\n");
        sb.append("3. Налаштування мережевого з'єднання.\n");
        if (privatHouse){
            sb.append("4. Безкоштовна заміна оптичного медіаконвертеру.\n");
        }
        sb.append("\nА також:\n");
        sb.append("Кредит довіри 10 днів.\n");
        sb.append("Безкоштовний перехід на більш дешевий тарифний план.");
        titleText.setText(sb.toString());

        Button activateButton = getButton(activate_garant_button);
        if ("enabled".equals(serviseStatus)){
            activateButton.setText("Відключити послугу");
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deActivateService();
                }
            });
        }else if ("disabled".equals(serviseStatus)){
            activateButton.setText("Підключити послугу");
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activateService();
                }
            });
        }else if ("enabling".equals(serviseStatus)){
            activateButton.setClickable(false);
            activateButton.setText("Послуга підключається");
        }else if ("disabling".equals(serviseStatus)){
            activateButton.setClickable(false);
            activateButton.setText("Послуга відключається");
        }else if (serviseStatus.startsWith("20")){
            activateButton.setClickable(false);
            activateButton.setText("Відключення " + serviseStatus);
        }
        TextView elseInfo = getTextView(text_view_for_details);
        elseInfo.setText(getString(R.string.garant_servise_else_info));
    }

    @Override
    void processIfOk() {
        if (privatHouse){
            descriptionText = "Послуга вартістю 30 грн/міс, яка гарантує вам якісний та дешевий сервіс";
        }else {
            descriptionText = "Послуга вартістю 10 грн/міс, яка гарантує вам якісний та дешевий сервіс";
        }
        setTitle(titleText, descriptionText);

        animateScreen();
    }

    private void deActivateService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Відключити послугу?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(context).inflate(R.layout.alert_builder_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        StringBuilder sb = new StringBuilder();
        sb.append("Увага! Якщо ви забажаєте підключити послугу повторно, менш ніж як через 12 ");
        sb.append("місяців - то така активація буде коштувати ");
        if (privatHouse){
            sb.append("100 грн разово");
        }else {
            sb.append("50 грн разово");
        }
        text.setText(sb.toString());
        builder.setView(textLayout);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (SendInfo.deActivateGarantedService()) {
                            makeSimpleSnackBar("Послуга відключається", mainLayout);
                            reloadFragment();
                        } else {
                            makeSimpleSnackBar("Трапилась помилка", mainLayout);
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void activateService(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Підключити послугу?");
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
                        if (SendInfo.activateGarantedService()) {
                            makeSimpleSnackBar("Послуга підключається", mainLayout);
                            reloadFragment();
                        } else {
                            makeSimpleSnackBar("Трапилась помилка", mainLayout);
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_garant_service;
    }

    @Override
    int setLayoutId() {
        return R.id.main_content_garant_service;
    }

    @Override
    public void onClick(View v) {

    }
}
