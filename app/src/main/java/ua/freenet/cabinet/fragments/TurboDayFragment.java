package ua.freenet.cabinet.fragments;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

import static ua.freenet.cabinet.utils.Utilits.doTwoSymb;

public class TurboDayFragment extends HelperFragment {


    private List<String> statistics;
    private Person person;

    @Override
    void setAllSettings() {
        titleText = "Турбо день";
        descriptionText = "Платна послуга збільшення швидкості до 100мбіт";
        fragmentId = R.layout.fragment_free_and_turbo_day;
        titleImage = R.drawable.background_speed2;
        layoutId = R.id.main_for_free_and_turbo_day;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }


    @Override
    void doInBackground() throws Exception {
        statistics = DbCache.getTurboDayStatistics();
        person = DbCache.getPerson();
    }

    @Override
    void processIfOk() {
        drawScreen();
        animateScreen();
    }

    private void drawScreen() {
        Button activateButton = getButton(R.id.button_activate_free_and_turbo_day);
        TextView firstText = getTextView(R.id.first_text);
        TextView textStatisticsTitle = getTextView(R.id.text_statistics_title);
        TextView daysLeft = getTextView(R.id.days_left);
        daysLeft.setVisibility(View.GONE);
        LinearLayout layout = (LinearLayout) mainLayout.findViewById(R.id.layout_for_statistics);


        firstText.setText("Послуга діє з 09:00 до 17:00 години обраного дня та коштує 2,4 грн за день.");

        if (person.getCurrent() < 2.5){
            activateButton.setText("Недостатньо коштів");
            activateButton.setClickable(false);
        }else {
            activateButton.setText("Обрати дату активації");
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askStartDate();
                }
            });
        }

        Collections.reverse(statistics);

        if (!statistics.isEmpty()) {
            textStatisticsTitle.setVisibility(View.VISIBLE);
            for (String statistic : statistics) {
                TextView textView = new TextView(context);
                textView.setText(statistic);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.addView(textView, MATCH_WRAP);
            }
        }
        activateButton.setVisibility(View.VISIBLE);
    }

    private void askStartDate() {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        new MyAlertDialogBuilder(context)
                .setTitleText("Дата початку:")
                .setView(datePicker)
                .setPositiveButtonWithRunnableForHandler("Вибрано", new Runnable() {
                    @Override
                    public void run() {
                        askEndDate(datePicker);
                    }
                }).createAndShow();
    }

    private void askEndDate(final DatePicker start) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        new MyAlertDialogBuilder(context)
                .setTitleText("Дата закінчення:")
                .setView(datePicker)
                .setPositiveButtonWithRunnableForHandler("Вибрано", new Runnable() {
                    @Override
                    public void run() {
                        confirmDate(start, datePicker);
                    }
                }).createAndShow();
    }

    private void confirmDate(final DatePicker start, final DatePicker end) {
        int startYear = start.getYear();
        int startMonth = start.getMonth() + 1;
        int startDay = start.getDayOfMonth();
        int endYear = end.getYear();
        int endMonth = end.getMonth() + 1;
        int endDay = end.getDayOfMonth();

        final String startDate = startYear + "-" + doTwoSymb(startMonth) + "-" + doTwoSymb(startDay);
        final String endDate = endYear + "-" + doTwoSymb(endMonth) + "-" + doTwoSymb(endDay);

        new MyAlertDialogBuilder(context)
                .setTitleText("Все вірно?")
                .setMessage("Послуга замовляється на період\nз: " + startDate + "\nпо: " + endDate)
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        if (SendInfo.activateTurboDay(startDate, endDate)) {
                            DbCache.markTurboDayStatisticsOld();
                            progressDialogWaitStopShowMessageReload("Послуга активована.", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Неправильний термін", mainLayout);
                        }
                    }
                }).createAndShow();
    }
}
