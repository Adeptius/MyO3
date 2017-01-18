package ua.adeptius.freenet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.adeptius.freenet.R;
import ua.adeptius.freenet.dao.DbCache;
import ua.adeptius.freenet.dao.GetInfo;
import ua.adeptius.freenet.dao.SendInfo;
import ua.adeptius.freenet.model.AvailableTarif;
import ua.adeptius.freenet.model.Servise;

import static ua.adeptius.freenet.utils.Utilits.doTwoSymb;


public class TarifFragment extends BaseFragment {

    private List<Servise> services;

    @Override
    void setAllSettings() {
        titleText = "Підключені послуги";
        descriptionText = "Керуйте вашими послугами звідси";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.background_tarifs;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {

    }

    @Override
    void doInBackground() throws Exception {
        services = DbCache.getServises();
    }

    @Override
    void processIfOk() {
        prepareServises(services);
        hideAllViewsInMainScreen();
        animateScreen();
    }

    private void prepareServises(List<Servise> services) {
        for (final Servise service : services) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_tarif, null);
            mainLayout.addView(itemView);

            TextView serviceTypeName = (TextView) itemView.findViewById(R.id.service_type);
            serviceTypeName.setText(service.getMyTypeName());
            serviceTypeName.setTextColor(COLOR_BLUE);

            TextView serviceName = (TextView) itemView.findViewById(R.id.service_name);
            serviceName.setText(service.getMyServiceName());


            TextView serviceComent = (TextView) itemView.findViewById(R.id.service_coment);
            if ("".equals(service.getComent())) {
                serviceComent.setVisibility(View.GONE);
            } else {
                serviceComent.setText(service.getComent());
            }

            TextView serviceCost = (TextView) itemView.findViewById(R.id.service_cost);
            serviceCost.setText(String.valueOf(service.getMonth()));

            Button goToButton = (Button) itemView.findViewById(R.id.button_go_to);
            goToButton.setVisibility(View.GONE);
            if (service.getMyTypeName().contains("MEGOGO")) {
                goToButton.setVisibility(View.VISIBLE);
                goToButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       goTo(new MegogoFragment());
                    }
                });
            }else if (service.getMyTypeName().contains("Антивірус")){
                goToButton.setVisibility(View.VISIBLE);
                goToButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goTo(new DrWebFragment());
                    }
                });
            }

            if (service.getMyTypeName().equals("Поштова скринька")){
                goToButton.setVisibility(View.VISIBLE);
                goToButton.setText("Змінити пароль");
                goToButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePasswordForEmail(String.valueOf(service.getId()));
                    }
                });
            }

            Button changeButton = (Button) itemView.findViewById(R.id.service_change_button);
            if (!service.is_allow_change()) {
                changeButton.setVisibility(View.GONE);
            } else {
                changeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeTarif(service);
                    }
                });
            }

            Button stopButton = (Button) itemView.findViewById(R.id.service_stop_button);
            if (!service.is_allow_suspend()) {
                stopButton.setVisibility(View.GONE);
            } else {
                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopTheService();
                    }
                });
            }

            Button startButton = (Button) itemView.findViewById(R.id.service_renew_button);
            if (service.isStopped()) {
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startTheService();
                    }
                });
            } else {
                startButton.setVisibility(View.GONE);
            }

            ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.service_progress_bar);
            if (service.isActivatingNow()) {
                progressBar.setVisibility(View.VISIBLE);
                serviceComent.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                changeButton.setVisibility(View.GONE);
                serviceComent.setText("Триває активація");
            }

            TextView discountView = (TextView) itemView.findViewById(R.id.discount_text);
            if (service.isHaveDiscount() && service.getDiscount() != 0) {
                discountView.setVisibility(View.VISIBLE);
                discountView.setText("Знижка " + service.getDiscount() + "% до " + service.getDiscountTo());
            }
        }
    }

    private void changeTarif(final Servise servise) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<AvailableTarif> availableTarifs = GetInfo
                            .getAvailableTarifs(String.valueOf(servise.getId()));
                    for (AvailableTarif availableTarif : availableTarifs) {
                        System.out.println(availableTarif);
                    }

                    final String[] tarifs = new String[availableTarifs.size()];
                    for (int i = 0; i < tarifs.length; i++) {
                        String name = availableTarifs.get(i).getName();
                        if (name.startsWith("20")) {
                            name = name.substring(name.indexOf(" ") + 1);
                        }
                        tarifs[i] = name;
                    }

                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                            TextView titleView = new TextView(context);
                            titleView.setText("Оберіть тариф:");
                            titleView.setGravity(Gravity.CENTER);
                            titleView.setTextSize(24);
                            titleView.setTypeface(null, Typeface.BOLD);
                            titleView.setTextColor(COLOR_BLUE);
                            builder.setCustomTitle(titleView);
                            builder.setItems(tarifs, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    proceedShoosenTarif(availableTarifs.get(item).getId(),
                                            availableTarifs.get(item).getName(),
                                            servise);
                                    dialog.dismiss();
                                }
                            });
                            builder.setCancelable(true);
                            builder.show();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void proceedShoosenTarif(final String id, String name, final Servise servise){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        String willcost = "";
        try {
            Matcher regexMatcher = Pattern.compile("\\d{1,4}[ ]грн").matcher(servise.getPay_type_name());
            regexMatcher.find();
            String s = regexMatcher.group();
            int currentMoney = Integer.parseInt(s.substring(0, s.indexOf(" ")));

            regexMatcher = Pattern.compile("\\d{1,4}[ ]грн").matcher(name);
            regexMatcher.find();
            s = regexMatcher.group();
            int willBeMoney = Integer.parseInt(s.substring(0, s.indexOf(" ")));

            if (currentMoney > willBeMoney){
                willcost = "Ви бажаєте перейти на більш дешевий тариф. " +
                        "З вашого рахунку, зараз, буде одноразово знято 10 грн.";
            }else if(currentMoney < willBeMoney){
                willcost = "Ви бажаєте перейти на більш дорогий тариф. " +
                        "Вам буде надана знижка 20% на 2 місяці.";
            }
            willcost += "\n Новий тариф буде активовано з першого числа наступного місяця";
        }catch (Exception ignored){}

        TextView titleView = new TextView(context);
        titleView.setText("Ви обрали:");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);

        StringBuilder sb = new StringBuilder();
        sb.append(name + "\n");
        sb.append("Протягом цього місяця ви не зможете призупиняти послугу, або її змінити.\n");
        sb.append("Зміна тарифного плану відбудеться 1-го числа наступного місяця.\n");
        if(!"".equals(willcost)){
            sb.append(willcost);
        }

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(sb.toString());
        builder.setView(textLayout);

        builder.setCustomTitle(titleView);
        builder.setPositiveButton("Згоден", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("pt_new", id);
                        map.put("service_id",String.valueOf(servise.getId()));
                        progressDialogShow();
                        if (SendInfo.changeTarif(map)) {
                            DbCache.markMountlyFeeOld();
                            DbCache.markServicesOld();
                            progressDialogWaitStopShowMessageReload("Тариф змінено", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void stopTheService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        TextView titleView = new TextView(context);
        titleView.setText("Умови");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);

        StringBuilder sb = new StringBuilder();
        sb.append("1. Поки послуга призупинена - абонентська плата за неї не знімається.\n");
        sb.append("2. Призупиняти можно 6 разів на рік.\n");
        sb.append("3. Призупинити можно не раніше ніж з завтра, та на строк від 10 днів до 6 місяців.\n");
        sb.append("4. Не хвилюйтеся - відновити достроково ви зможете у будь-який момент.\n");

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(sb.toString());
        builder.setView(textLayout);

        builder.setPositiveButton("Зрозуміло", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                askStartDate();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void askStartDate() {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        builder.setCancelable(true);
        builder.setView(datePicker);

        TextView titleText = new TextView(context);
        titleText.setText("Призупинити з:");
        titleText.setGravity(Gravity.CENTER);
        titleText.setTextSize(24);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setTextColor(COLOR_BLUE);

        builder.setCustomTitle(titleText);
        builder.setView(datePicker);
        builder.setPositiveButton("Вибрано", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (!isRightChoice(datePicker)) {
                    makeSimpleSnackBar("Сьогодні або вчора вибрати не можливо", mainLayout);
                } else {
                    askEndDate(getStringedDate(datePicker));
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void askEndDate(final String startDate) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        builder.setCancelable(true);
        builder.setView(datePicker);

        TextView textView = new TextView(context);
        textView.setText("Призупинити до:");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(24);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(COLOR_BLUE);

        builder.setCustomTitle(textView);
        builder.setPositiveButton("Вибрано", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                confirmDate(startDate, getStringedDate(datePicker));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void confirmDate(final String startDate, final String endDate) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
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

        StringBuilder sb = new StringBuilder();
        sb.append("Послуга призупиняється\n");
        sb.append("з: " + startDate);
        sb.append("\nпо: " + endDate);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
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
                        progressDialogShow();
                        if (SendInfo.stopService(startDate, endDate)) {
                            DbCache.markServicesOld();
                            progressDialogWaitStopShowMessageReload("Успішно", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Невдало", mainLayout);
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changePasswordForEmail(final String id) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);
        builder.setCancelable(true);
        builder.setView(datePicker);

        TextView titleView = new TextView(context);
        titleView.setText("Зміна паролю");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);


        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_change_mail, null);
        final EditText text = (EditText) textLayout.findViewById(R.id.text_new_password);
        builder.setView(textLayout);
        builder.setCustomTitle(titleView);
        builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        String result = SendInfo.changeEmailPassword(text.getText().toString(),id);
                        makeSimpleSnackBar(result, mainLayout);
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startTheService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        TextView titleView = new TextView(context);
        titleView.setText("Відновити зараз?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);

        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText("Це займе від пари хвилин до години.");
        builder.setView(textLayout);

        builder.setCustomTitle(titleView);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
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
                final String query = year + "-" + doTwoSymb(month) + "-" + doTwoSymb(day)
                        + " " + doTwoSymb(hour) + ":" + doTwoSymb(minute);
                progressDialogShow();
                EXECUTOR.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (SendInfo.startService(query)) {
                            DbCache.markServicesOld();
                            progressDialogWaitStopShowMessageReload("Послуга відновлена. Зачекайте.", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Невдалось. Спробуйте ще раз.", mainLayout);
                        }
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean isRightChoice(DatePicker datePicker) {
        Calendar calendar = new GregorianCalendar();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        boolean y = currentYear >= year;
        boolean m = currentMonth >= month;
        boolean d = currentDay >= day;

        if (y && m && d) {
            return false;
        }
        return true;
    }

    private String getStringedDate(DatePicker datePicker) {
        String year = String.valueOf(datePicker.getYear());
        String month = String.valueOf(datePicker.getMonth() + 1);
        String day = String.valueOf(datePicker.getDayOfMonth());
        month = month.length() == 1 ? "0" + month : month;
        day = day.length() == 1 ? "0" + day : day;
        return year + "-" + month + "-" + day;
    }

    @Override
    public void onClick(View v) {

    }
}
