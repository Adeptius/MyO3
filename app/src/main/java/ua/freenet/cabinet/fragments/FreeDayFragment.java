package ua.freenet.cabinet.fragments;


import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.Servise;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

import static ua.freenet.cabinet.utils.Utilits.doTwoSymb;

public class FreeDayFragment extends BaseFragment {

    private int numberOfFreeDays;
    private int availableFreeDays;
    private List<String> statistics;
    private List<Servise> services;


    @Override
    void setAllSettings() {
        titleText = "Вільний день";
        descriptionText = "Безкоштовна послуга збільшення швидкості до 100мбіт";
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
        Map<String, Integer> map = DbCache.getFreeDayInfo();
        numberOfFreeDays = map.get("daysTotal");
        availableFreeDays = map.get("daysLeft");
        statistics = DbCache.getFreeDayStatistics();
        services = DbCache.getServises();

    }

    @Override
    void processIfOk() {
        drawScreen();
        animateScreen();
    }

    private void drawScreen() {
        Button activateButton = getButton(R.id.button_activate_free_and_turbo_day);

        TextView firstText = getTextView(R.id.first_text);
        TextView activeText = getTextView(R.id.active_text);
        TextView textDaysLeft = getTextView(R.id.days_left);
        TextView textStatisticsTitle = getTextView(R.id.text_statistics_title);
        LinearLayout layout = (LinearLayout) mainLayout.findViewById(R.id.layout_for_statistics);

        StringBuilder mainMessage = new StringBuilder();
        mainMessage.append("Ви з нами ");
        if (numberOfFreeDays == 0) {
            mainMessage.append("менше року, нажаль, поки що, послуга вам не доступна. Залишайтесь з нами та отримуйте більше бонусів.");
        } else if (numberOfFreeDays == 1) {
            mainMessage.append("один рік. Вам надається один день у місяць без обмежень швидкості.");
        } else if (numberOfFreeDays == 2) {
            mainMessage.append("два роки. Вам надається два дні у місяць без обмежень швидкості.");
        } else if (numberOfFreeDays == 3) {
            mainMessage.append("три роки. Вам надається три дня у місяць без обмежень швидкості.");
        } else if (numberOfFreeDays == 4) {
            mainMessage.append("чотири роки. Вам надається чотири дні у місяць без обмежень швидкості.");
        } else if (numberOfFreeDays == 5) {
            mainMessage.append("більше п'яти років. Вам надається п'ять днів у місяць без обмежень швидкості.");
        }
        if (!(numberOfFreeDays == 0)) {
            mainMessage.append("\nПослуга діє 24 години.");
        }

        StringBuilder leftMessage = new StringBuilder();
        if (availableFreeDays == 0) {
            if (!(numberOfFreeDays == 0)) {
                leftMessage.append("Ви вже використали всі вільні дні у цьому місяці.");
            }
        } else {
            activateButton.setVisibility(View.VISIBLE);
        }
        if (availableFreeDays == 1) {
            leftMessage.append("Вам доступен ще один вільний день.");
        } else if (availableFreeDays == 2) {
            leftMessage.append("Вам доступно ще два вільних дні.");
        } else if (availableFreeDays == 3) {
            leftMessage.append("Вам доступно ще три вільних дні.");
        } else if (availableFreeDays == 4) {
            leftMessage.append("Вам доступно ще чотири вільних дні.");
        } else if (availableFreeDays == 5) {
            leftMessage.append("Вам доступно ще п'ять вільних днів.");
        }

        boolean stopped = false;
        for (Servise service : services) {
            if (service.getType() == 5) {
                stopped = service.isStopped();
            }
        }

        firstText.setText(mainMessage.toString());
        textDaysLeft.setText(leftMessage.toString());

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activating();
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
        if (statistics.size() > 0) {
            String statistic = statistics.get(0);
            statistic = statistic.substring(statistic.indexOf("до ") + 3);
            if (isFreeDayIsActive(statistic)) {
                activateButton.setClickable(false);
                activateButton.setText("Послуга вже активована");
                try {
                    Date date = getDate(statistic + ":00");
                    long timeEnable = date.getTime();
                    long timeCurrent = new Date().getTime();
                    int hoursLeft = (int) (timeEnable - timeCurrent) / 1000 / 60 / 60;
                    activeText.setText("Залишилось " + hoursLeft + " годин");
                    activeText.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (stopped) {
            activateButton.setText("Інтернет призупинений");
            activateButton.setClickable(false);
        }
    }

    public static Date getDate(String date) {
        // string in format dd-mm-yyyy hh-mm-ss
        int year = Integer.parseInt(date.substring(6, 10));
        int month = Integer.parseInt(date.substring(3, 5));
        int day = Integer.parseInt(date.substring(0, 2));
        int hour = Integer.parseInt(date.substring(11, 13));
        int minute = Integer.parseInt(date.substring(14, 16));
        int seconds = Integer.parseInt(date.substring(17, 19));
        @SuppressWarnings("deprecation")
        Date date2 = new Date(year - 1900, month - 1, day, hour, minute, seconds);
        return date2;
    }

    public boolean isFreeDayIsActive(String date) {
        System.out.println(date);
        int year = Integer.parseInt(date.substring(6, 10));
        int month = Integer.parseInt(date.substring(3, 5));
        int day = Integer.parseInt(date.substring(0, 2));
        int hour = Integer.parseInt(date.substring(11, 13));
        int minute = Integer.parseInt(date.substring(14, 16));
        @SuppressWarnings("deprecation")
        Date date2 = new Date(year - 1900, month - 1, day, hour, minute);
        long timeActive = date2.getTime();
        long timeNow = new Date().getTime();
        return timeNow < timeActive;
    }

    public void activating() {
        new MyAlertDialogBuilder(context)
                .setTitleText("Активувати?")
                .setMessage("Послуга активується до 10 хвилин.")
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        activateNow();
                    }
                }).createAndShow();
    }

    public void activateNow() {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE) + 3;
        if (minute > 59) {
            hour++;
            minute = minute - 60;
        }
        final String date = year + "-" + doTwoSymb(month) + "-" + doTwoSymb(day)
                + " " + doTwoSymb(hour) + ":" + doTwoSymb(minute);

        final HashMap<String, String> map = new HashMap<>();
        map.put("date", date);
        map.put("days", "1");
        progressDialogShow();
        if (SendInfo.activateFreeDay(map)) {
            DbCache.markFreeDayInfoOld();
            DbCache.markFreeDayStatisticsOld();
            progressDialogWaitStopShowMessageReload("10 хвилин активація..", mainLayout);
        } else {
            progressDialogStopAndShowMessage("Послуга вже активна.", mainLayout);
        }
    }
}
