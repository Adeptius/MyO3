package ua.adeptius.myo3.activities.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;

import static ua.adeptius.myo3.utils.Utilits.doTwoSymb;

public class TurboDayFragment extends BaseFragment {


    private List<String> statistics;

    @Override
    void init() {
        titleText = "Турбо день";
        descriptionText = "Платна послуга збільшення швидкості до 100мбіт";
//        mainLayout = (LinearLayout) baseView.findViewById(R.id.main_for_free_and_turbo_day);
    }

    @Override
    void doInBackground() throws Exception {
        statistics = GetInfo.getTurboDayStatistics();
    }

    @Override
    void processIfOk() {
        drawScreen();
    }

    private void drawScreen() {
        Button activateButton = getButton(R.id.button_activate_free_and_turbo_day);
        TextView firstText = getTextView(R.id.first_text);
        TextView textStatisticsTitle = getTextView(R.id.text_statistics_title);
        LinearLayout layout = (LinearLayout) mainLayout.findViewById(R.id.layout_for_statistics);

        firstText.setText("Послуга діє з 09:00 до 17:00 години обраного дня та коштує 2,4 грн за день.");

        activateButton.setText("Обрати дату активації");

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askStartDate();
            }
        });

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
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.fragment_tarif_datepicker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        builder.setCancelable(true);
        builder.setView(datePicker);

        TextView titleText = new TextView(context);
        titleText.setText("Дата початку:");
        titleText.setGravity(Gravity.CENTER);
        titleText.setTextSize(24);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setTextColor(COLOR_BLUE);

        builder.setCustomTitle(titleText);
        builder.setView(datePicker);
        builder.setPositiveButton("Вибрано", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                askEndDate(datePicker);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void askEndDate(final DatePicker start) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.fragment_tarif_datepicker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        builder.setCancelable(true);
        builder.setView(datePicker);

        TextView textView = new TextView(context);
        textView.setText("Дата закінчення:");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(24);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(COLOR_BLUE);

        builder.setCustomTitle(textView);
        builder.setPositiveButton("Вибрано", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                confirmDate(start, datePicker);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void confirmDate(final DatePicker start, final DatePicker end) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.fragment_tarif_datepicker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        builder.setCancelable(true);
        builder.setView(datePicker);

        TextView titleView = new TextView(context);
        titleView.setText("Все вірно?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);

        int startYear = start.getYear();
        int startMonth = start.getMonth() + 1;
        int startDay = start.getDayOfMonth();
        int endYear = end.getYear();
        int endMonth = end.getMonth() + 1;
        int endDay = end.getDayOfMonth();

        final String startDate = startYear + "-" + doTwoSymb(startMonth) + "-" + doTwoSymb(startDay);
        final String endDate = endYear + "-" + doTwoSymb(endMonth) + "-" + doTwoSymb(endDay);

        StringBuilder sb = new StringBuilder();
        sb.append("Послуга замовляється на період\n");
        sb.append("з: " + startDate);
        sb.append("\nпо: " + endDate);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.alert_builder_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(sb.toString());
        builder.setView(textLayout);

        builder.setCustomTitle(titleView);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (SendInfo.activateTurboDay(startDate, endDate)) {
                            makeSimpleSnackBar("Послуга активована.", mainLayout);
                        } else {
                            makeSimpleSnackBar("Неправильний термін", mainLayout);
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


//    public void activating(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setCancelable(true);
//        TextView titleView = new TextView(context);
//        titleView.setText("Активувати?");
//        titleView.setGravity(Gravity.CENTER);
//        titleView.setTextSize(24);
//        titleView.setTypeface(null, Typeface.BOLD);
//        titleView.setTextColor(COLOR_BLUE);
//        builder.setCustomTitle(titleView);
//        View textLayout = LayoutInflater.from(context).inflate(R.layout.alert_builder_message, null);
//        TextView text = (TextView) textLayout.findViewById(R.id.text);
//        text.setText("Послуга активується до 10 хвилин.");
//        builder.setView(textLayout);
//        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialog, int which) {
//                activateNow();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

//    public void activateNow(){
//        Calendar calendar = new GregorianCalendar();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE) + 3;
//        if (minute > 59) {
//            hour++;
//            minute = minute - 60;
//        }
//        final String date = year + "-" + doTwoSymb(month) + "-" + doTwoSymb(day)
//                + " " + doTwoSymb(hour) + ":" + doTwoSymb(minute);
//
//        final HashMap<String, String> map = new HashMap<>();
//        map.put("date", date);
//        map.put("days", "1");
//
//        EXECUTOR.submit(new Runnable() {
//            @Override
//            public void run() {
//                if (SendInfo.activateFreeDay(map)) {
//                    makeSimpleSnackBar("10 хвилин активація..", mainLayout);
//                } else {
//                    makeSimpleSnackBar("Послуга вже активна.", mainLayout);
//                }
//            }
//        });
//
//    }

    @Override
    void processIfFail() {

    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_free_and_turbo_day;
    }

    @Override
    int setLayoutId() {
        return R.id.main_for_free_and_turbo_day;
    }

    @Override
    public void onClick(View v) {

    }
}
