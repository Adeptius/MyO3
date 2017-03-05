package ua.freenet.cabinet.fragments;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

import static ua.freenet.cabinet.R.id.activate_garant_button;
import static ua.freenet.cabinet.R.id.text_title;
import static ua.freenet.cabinet.R.id.text_view_for_details;

public class GarantServiceFragment extends BaseFragment {

    private String serviseStatus;
    private boolean privatHouse;
    private double money;

    @Override
    void setAllSettings() {
        titleText = "Гарантований сервіс";
        descriptionText = "";
        fragmentId = R.layout.fragment_garant_service;
        titleImage = R.drawable.background_garant2;
        layoutId = R.id.main_content_garant_service;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        serviseStatus = DbCache.garantedServiceStatus();
        privatHouse = DbCache.getPerson().getAddress().isPrivat();
        money = DbCache.getPerson().getCurrent();
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
            if (privatHouse&&money<30 || !privatHouse&&money<10){
                activateButton.setText("Недостатньо коштів");
                activateButton.setClickable(false);
            }else {
                activateButton.setText("Підключити послугу");
                activateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activateService();
                    }
                });
            }
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
        updateTitle();
        animateScreen();
    }

    private void deActivateService() {
        StringBuilder sb = new StringBuilder();
        sb.append("Увага! Якщо ви забажаєте підключити послугу повторно, менш ніж як через 12 ");
        sb.append("місяців - то така активація буде коштувати ");
        if (privatHouse){
            sb.append("100 грн разово");
        }else {
            sb.append("50 грн разово");
        }

        new MyAlertDialogBuilder(context)
                .setTitleText("Відключити послугу?")
                .setMessage(sb.toString())
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        if (SendInfo.deActivateGarantedService()) {
                            DbCache.markMountlyFeeOld();
                            DbCache.markGarantedServiceStatusOld();
                            progressDialogWaitStopShowMessageReload("Послуга відключається", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                        }
                    }
                }).createAndShow();
    }

    private void activateService(){
        new MyAlertDialogBuilder(context)
                .setTitleText("Підключити послугу?")
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        if (SendInfo.activateGarantedService()) {
                            DbCache.markMountlyFeeOld();
                            DbCache.markGarantedServiceStatusOld();
                            progressDialogWaitStopShowMessageReload("Послуга підключається", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                        }
                    }
                }).createAndShow();
    }
}
