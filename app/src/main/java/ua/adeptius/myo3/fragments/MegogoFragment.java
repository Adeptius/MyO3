package ua.adeptius.myo3.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.ChannelMegogo;
import ua.adeptius.myo3.model.MegogoPts;
import ua.adeptius.myo3.utils.Utilits;

//TODO переваги
public class MegogoFragment extends BaseFragment {

    private List<ChannelMegogo> allChannelMegogos = new ArrayList<>();
    private List<ChannelMegogo> light = new ArrayList<>();
    private List<ChannelMegogo> optimal = new ArrayList<>();
    private List<ChannelMegogo> maximum = new ArrayList<>();
    private List<ChannelMegogo> filmBox = new ArrayList<>();
    private List<ChannelMegogo> viasat = new ArrayList<>();
    private String activeSubscribe = "";
    private String activationLink = "";
    private List<MegogoPts> megogoPts;

    @Override
    void setAllSettings() {
        titleText = "";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.background_megogo11;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        try {
            allChannelMegogos = GetInfo.getMegogoChannels("http://megogo.net/ru/tv/channels/8701-light-tv-online");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            filmBox = GetInfo.getMegogoChannels("http://megogo.net/ru/tv/channels/2691-filmbox-tv-online");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            viasat = GetInfo.getMegogoChannels("http://megogo.net/ru/tv/channels/2701-tv1000premium-tv-online");
        } catch (Exception e) {
            e.printStackTrace();
        }

        megogoPts = GetInfo.getMegogoPts();
        for (int i = megogoPts.size() - 1; i >= 0; i--) {
            if (megogoPts.get(i).isSubscribe())
                activeSubscribe = megogoPts.get(i).getName();
        }

        if (!"".equals(activeSubscribe)) {
            activationLink = GetInfo.getMegogoActivationLink();
        }
        sortChannels();
    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }

    private void sortChannels() {
        for (ChannelMegogo channelMegogo : allChannelMegogos) {
            if (channelMegogo.getAvailableIn().equals("Легка")) {
                light.add(channelMegogo);
            } else if (channelMegogo.getAvailableIn().equals("Оптимальна")) {
                optimal.add(channelMegogo);
            } else if (channelMegogo.getAvailableIn().equals("Максимальна")) {
                maximum.add(channelMegogo);
            }
        }
    }

    private void draw() {
        View perevagyLayout = LayoutInflater.from(context).inflate(R.layout.item_megogo_perevagy, null);
        perevagyLayout.setVisibility(View.GONE);
        mainLayout.addView(perevagyLayout);

        if (!"".equals(activeSubscribe)) {
            View mainLayoutMegogo = LayoutInflater.from(context).inflate(R.layout.item_megogo_main, null);
            mainLayoutMegogo.setVisibility(View.GONE);
            TextView nameField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_name);
            nameField.setText("Підключена послуга");
            TextView listField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_list);
            StringBuilder sb = new StringBuilder();
            sb.append("Якщо ви ще не зареєструвались на MEGOGO - зробіть це.");
            sb.append("Також можете встановити додаток на свій пристрій");
            listField.setText(sb.toString());
            Button activateButton = (Button) mainLayoutMegogo.findViewById(R.id.megogo_activate_button);
            activateButton.setText("Реєстрація");
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageGoToMegogoSite();
                }
            });
            Button showButton = (Button) mainLayoutMegogo.findViewById(R.id.show_button);
            showButton.setText("Додаток");
            showButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = "com.megogo.application"; // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            mainLayoutMegogo.setPadding(0,0,0,50);

            mainLayout.addView(mainLayoutMegogo);
        }

        for (final MegogoPts megogoPt : megogoPts) {
            if (megogoPt.getName().equals("Телевидение MEGOGO.NET (оптимальный за 0грн БАНДЛ) - 0 грн."))
                continue;

            String id = megogoPt.getId();
            final String name = megogoPt.getName();
            String cost = megogoPt.getMonth();
            String description = megogoPt.getDescription();
            boolean subscribed = megogoPt.isSubscribe();

            View mainLayoutMegogo = LayoutInflater.from(context).inflate(R.layout.item_megogo_main, null);
            mainLayoutMegogo.setVisibility(View.GONE);
            TextView nameField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_name);
            TextView listField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_list);

            Button activateButton = (Button) mainLayoutMegogo.findViewById(R.id.megogo_activate_button);

            final Button showButton = (Button) mainLayoutMegogo.findViewById(R.id.show_button);

            final LinearLayout channelList = (LinearLayout) mainLayoutMegogo.findViewById(R.id.list_of_chanels);

            nameField.setText(name + " (" + cost + "грн/міс)");

            listField.setText(description);

            if (subscribed) {
                nameField.setTextColor(COLOR_GREEN);
                nameField.setText(name + " (підключено)");
                activateButton.setText("Відключити");
                activateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askDeActivate(megogoPt);
                    }
                });
            } else {
                if ("".equals(activeSubscribe)) {
                    activateButton.setText("Підключити");
                } else {
                    activateButton.setText("Перейти");
                    if (megogoPt.getName().contains("пакет")) {
                        activateButton.setText("Підключити");
                    }
                }

                activateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askActivate(megogoPt);
                    }
                });
            }


            showButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (channelList.getChildCount() > 0) {
                        channelList.removeAllViews();
                        if (name.equals("Підписка легка")) {
                            showButton.setText("Канали");
                        } else if (name.equals("Підписка оптимальна")) {
                            showButton.setText("Канали");
                        } else if (name.equals("Підписка максимальна")) {
                            showButton.setText("Канали");
                        } else if (name.equals("Додатковий пакет FilmBox")) {
                            showButton.setText("Канали");
                        } else if (name.equals("Додатковий пакет Viasat Premium")) {
                            showButton.setText("Канали");
                        }
                    } else {
                        showButton.setText("Сховати");
                        if (name.equals("Підписка легка")) {
                            showChannels(channelList, light);
                        } else if (name.equals("Підписка оптимальна")) {
                            showChannels(channelList, optimal);
                        } else if (name.equals("Підписка максимальна")) {
                            showChannels(channelList, maximum);
                        } else if (name.equals("Додатковий пакет FilmBox")) {
                            showChannels(channelList, filmBox);
                        } else if (name.equals("Додатковий пакет Viasat Premium")) {
                            showChannels(channelList, viasat);
                        }
                    }
                }
            });
            mainLayout.addView(mainLayoutMegogo);
        }
        addAdditionalText();
    }

    private void showChannels(LinearLayout container, List<ChannelMegogo> chanels) {
        TextView coment = new TextView(context);
        coment.setTextColor(COLOR_GREEN);
        coment.setTextSize(16);
        coment.setTypeface(null, Typeface.BOLD);
        if (chanels.equals(optimal)){
            coment.setText("Всі канали з легкої підписки та плюс наступні:");
            container.addView(coment);
        }else if (chanels.equals(maximum)){
            coment.setText("Всі канали з оптимальної підписки та плюс наступні:");
            container.addView(coment);
        }

        int column = Utilits.calculateColums(this);
        LinearLayout layout = null;

        for (final ChannelMegogo channelMegogo : chanels) {
            if (layout == null || layout.getChildCount() == column) {
                layout = new LinearLayout(context);
                container.addView(layout, MATCH_WRAP);
            }
            ImageView imageView = new ImageView(context);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreInfo(channelMegogo);
                }
            });
            layout.addView(imageView, WRAP_WRAP);

            int widht = container.getWidth();

            int dim = widht / column;

            Glide.with(this)
                    .load(channelMegogo.getIconUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter()
                    .override(dim, dim)
                    .into(imageView);

        }
    }

    private void showMessageGoToMegogoSite() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Реєстрація");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText("Зараз ви перейдете на сайт MEGOGO. Натисніть там \"Регистрация нового аккаунта\" " +
                "та створіть собі обліковий запис");
        builder.setView(textLayout);
        builder.setPositiveButton("Перейти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(activationLink));
                startActivity(i);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void moreInfo(final ChannelMegogo channelMegogo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView textView = new TextView(context);
        textView.setText(channelMegogo.getTitle());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(24);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(COLOR_BLUE);
        textView.setBackgroundColor(Color.WHITE);

        final View layout = LayoutInflater.from(context).inflate(R.layout.item_megogo_details, null);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
        final TextView description = (TextView) layout.findViewById(R.id.text_description);

        description.setText(channelMegogo.getDescription());
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = channelMegogo.getIconUrl();
                    URL newurl = new URL(url);
                    final Bitmap loadedBitMap = BitmapFactory
                            .decodeStream(newurl.openConnection().getInputStream());

                    double y = loadedBitMap.getHeight();
                    double x = loadedBitMap.getWidth();

            int currentX = ((LinearLayout) imageView.getParent()).getWidth();
//                    int currentX = 500;
                    double ratio = y / x;
                    final int needY = (int) (currentX * ratio);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.getLayoutParams().height = needY;
                            imageView.setImageBitmap(loadedBitMap);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        builder.setCustomTitle(textView);
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addAdditionalText() {
        TextView textView = new TextView(context);
        textView.setText(R.string.megogo_additional);
        textView.setPadding(40, 30, 40, 40);
        textView.setVisibility(View.GONE);
        mainLayout.addView(textView);
    }


    private void askActivate(final MegogoPts megogoPt) {
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
        String textMessage = "";
        if (!"".equals(activeSubscribe) && !megogoPt.getName().contains("пакет")) {
            textMessage = "У вас вже активовано: " + activeSubscribe + "\nПослугу буде змінено на " + megogoPt.getName();
        } else {
            textMessage = megogoPt.getName() + " буде активовано. \nВартість: " + megogoPt.getMonth() + "грн/міс";
        }
        text.setText(textMessage);
        builder.setView(textLayout);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                activate(megogoPt);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void activate(final MegogoPts megogoPt) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                if (SendInfo.activateMegogo(megogoPt.getId())) {
                    makeSimpleSnackBar("10 хвилин активація..", mainLayout);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                    goTo(new TarifFragment());
                } else {
                    makeSimpleSnackBar("Трапилась помилка", mainLayout);
                }
            }
        });
    }

    private void askDeActivate(final MegogoPts megogoPt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        TextView titleView = new TextView(context);
        titleView.setText("Відключити?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(megogoPt.getName() + " буде відключено.");
        builder.setView(textLayout);
        builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                deActivate(megogoPt);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deActivate(final MegogoPts megogoPt) {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                if (SendInfo.deActivateMegogo(megogoPt.getId())) {
                    makeSimpleSnackBar("Відключення..", mainLayout);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                    goTo(new TarifFragment());
                } else {
                    makeSimpleSnackBar("Трапилась помилка", mainLayout);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
