package ua.freenet.cabinet.fragments;

import android.content.DialogInterface;
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

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.GetInfo;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.AvailableTarif;
import ua.freenet.cabinet.model.Servise;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

import static ua.freenet.cabinet.utils.Utilits.doTwoSymb;


public class TarifFragment extends HelperFragment {

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
            serviceCost.setText(String.valueOf(service.getServiceCost()));

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
            } else if (service.getMyTypeName().contains("Антивірус")) {
                goToButton.setVisibility(View.VISIBLE);
                goToButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goTo(new DrWebFragment());
                    }
                });
            }

            if (service.getMyTypeName().equals("Поштова скринька")) {
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
                discountView.setText(
                        "Знижка "
                                + service.getDiscount()
                                + "% ("
                                + service.getCostForCustomer()
                                + " грн) до "
                                + service.getDiscountTo());
            }
        }
    }

    private void changeTarif(final Servise servise) {
        progressDialogShow();
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
                            new MyAlertDialogBuilder(context)
                                    .setTitleText("Оберіть тариф:")
                                    .setItems(tarifs, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int item) {
                                            proceedShoosenTarif(availableTarifs.get(item).getId(),
                                                    availableTarifs.get(item).getName(), servise);
                                            dialog.dismiss();
                                        }
                                    }).createAndShow();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hideProgressDialog();
                }
            }
        });
    }

    private void proceedShoosenTarif(final String id, String name, final Servise servise) {
        String willcost = "";
        try {
//            Matcher regexMatcher = Pattern.compile("\\d{1,4}[ ]грн").matcher(servise.getPay_type_name());
//            regexMatcher.find();
//            String s = regexMatcher.group();
//            int currentMoney = Integer.parseInt(s.substring(0, s.indexOf(" ")));
            int currentMoney = servise.getServiceCost();

            Matcher regexMatcher = Pattern.compile("\\d{1,4}[ ]грн").matcher(name);
            regexMatcher.find();
            String s = regexMatcher.group();
            int willBeMoney = Integer.parseInt(s.substring(0, s.indexOf(" ")));

            if (currentMoney > willBeMoney) {
                willcost = "Ви бажаєте перейти на більш дешевий тариф. " +
                        "З вашого рахунку, зараз, буде одноразово знято 10 грн.";
            } else if (currentMoney < willBeMoney) {
                willcost = "Ви бажаєте перейти на більш дорогий тариф. " +
                        "Вам буде надана знижка 20% на 2 місяці.";
            }
            willcost += "\nНовий тариф буде активовано з першого числа наступного місяця";
        } catch (Exception ignored) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Протягом цього місяця ви не зможете призупиняти послугу, або її змінити.\n");
        if (!"".equals(willcost)) {
            sb.append(willcost);
        }

        new MyAlertDialogBuilder(context)
                .setTitleText("Ви обрали:")
                .setMessage(sb.toString())
                .setPositiveButtonWithRunnableForExecutor("Згоден", new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("pt_new", id);
                        map.put("service_id", String.valueOf(servise.getId()));
                        progressDialogShow();
                        if (SendInfo.changeTarif(map)) {
                            DbCache.markMountlyFeeOld();
                            DbCache.markServicesOld();
                            progressDialogWaitStopShowMessageReload("Тариф змінено", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
                        }
                    }
                }).createAndShow();
    }


    private void stopTheService() {
        String sb = "1. Поки послуга призупинена - абонентська плата за неї не знімається.\n" +
                "2. Призупиняти можно 6 разів на рік.\n" +
                "3. Призупинити можно не раніше ніж з завтра, та на строк від 10 днів до 6 місяців.\n" +
                "4. Не хвилюйтеся - відновити достроково ви зможете у будь-який момент.\n";

        new MyAlertDialogBuilder(context)
                .setTitleText("Умови")
                .setMessage(sb)
                .setPositiveButtonWithRunnableForHandler("Зрозуміло", new Runnable() {
                    @Override
                    public void run() {
                        askStartDate();
                    }
                }).createAndShow();
    }


    private void askStartDate() {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);

        final MyAlertDialogBuilder builder = new MyAlertDialogBuilder(context);
        builder.setTitleText("Призупинити з:")
                .setView(datePicker)
                .createShowAndSetPositiveForHandler("Вибрано", new Runnable() {
                    @Override
                    public void run() {
                        if (!isShoosenFutureDay(datePicker)) {
                            makeSimpleSnackBar("Сьогодні або вчора вибрати не можливо", datePicker);
                        } else {
                            askEndDate(getStringedDate(datePicker));
                            builder.close();
                        }
                    }
                });
    }

    private void askEndDate(final String startDate) {
        final View datepickerLayout = LayoutInflater.from(context).inflate(R.layout.item_datepicker, null);
        final DatePicker datePicker = (DatePicker) datepickerLayout.findViewById(R.id.datePicker);

        new MyAlertDialogBuilder(context)
                .setTitleText("Призупинити до:")
                .setView(datePicker)
                .setPositiveButtonWithRunnableForHandler("Вибрано", new Runnable() {
                    @Override
                    public void run() {
                        confirmDate(startDate, getStringedDate(datePicker));
                    }
                }).createAndShow();
    }

    private void confirmDate(final String startDate, final String endDate) {
        new MyAlertDialogBuilder(context)
                .setTitleText("Все вірно?")
                .setMessage("Послуга призупиняється\nз: " + startDate + "\nпо: " + endDate)
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        progressDialogShow();
                        if (SendInfo.stopService(startDate, endDate)) {
                            DbCache.markServicesOld();
                            progressDialogWaitStopShowMessageReload("Успішно", mainLayout);
                        } else {
                            progressDialogStopAndShowMessage("Невдало. Можливо ви вказали строк менше 10 днів, або зник інтернет.", mainLayout);
                        }
                    }
                }).createAndShow();
    }

    private void changePasswordForEmail(final String id) {
        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_change_mail, null);
        final EditText text = (EditText) textLayout.findViewById(R.id.text_new_password);

        new MyAlertDialogBuilder(context)
                .setView(textLayout)
                .setTitleText("Зміна паролю")
                .setPositiveButtonWithRunnableForExecutor("Готово", new Runnable() {
                    @Override
                    public void run() {
                        String result = SendInfo.changeEmailPassword(text.getText().toString(), id);
                        makeSimpleSnackBar(result, mainLayout);
                    }
                }).createAndShow();
    }

    private void startTheService() {
        new MyAlertDialogBuilder(context)
                .setTitleText("Відновити зараз?")
                .setMessage("Це займе від пари хвилин до години.")
                .setPositiveButtonWithRunnableForHandler("Так", new Runnable() {
                    @Override
                    public void run() {
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
                }).createAndShow();
    }


    public static boolean isShoosenFutureDay(DatePicker datePicker) {
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

        return !(y && m && d);
    }

    public static boolean isShoosenTodayOrFuture(DatePicker datePicker) {
        Calendar calendar = new GregorianCalendar();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;

        boolean y = currentYear >= year;
        boolean m = currentMonth >= month;
        boolean d = currentDay >= day;

        return !(y && m && d);
    }

    private String getStringedDate(DatePicker datePicker) {
        String year = String.valueOf(datePicker.getYear());
        String month = String.valueOf(datePicker.getMonth() + 1);
        String day = String.valueOf(datePicker.getDayOfMonth());
        month = month.length() == 1 ? "0" + month : month;
        day = day.length() == 1 ? "0" + day : day;
        return year + "-" + month + "-" + day;
    }
}
