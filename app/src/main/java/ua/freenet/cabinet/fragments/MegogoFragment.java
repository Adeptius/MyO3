package ua.freenet.cabinet.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.GetInfo;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.ChannelMegogo;
import ua.freenet.cabinet.model.MegogoPts;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;


public class MegogoFragment extends HelperFragment {

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
    }

    @Override
    void doInBackground() throws Exception {
        try {
            allChannelMegogos = DbCache.getMegogoMainChannels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            filmBox = DbCache.getMegogoFilmBoxChannels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            viasat = DbCache.getMegogoViasatChannels();
        } catch (Exception e) {
            e.printStackTrace();
        }

        megogoPts = DbCache.getMegogoPts();
        for (int i = megogoPts.size() - 1; i >= 0; i--) {
            if (megogoPts.get(i).isSubscribe())
                activeSubscribe = megogoPts.get(i).getName();
        }

        if (!"".equals(activeSubscribe)) {
            activationLink = GetInfo.getMegogoActivationLink();
        }
        sortChannels();
        prepareViews();
    }

    @Override
    void processIfOk() {
        showAndAnimatePreparedViews();
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

    private void prepareViews() {
        View perevagyLayout = LayoutInflater.from(context).inflate(R.layout.item_megogo_perevagy, null);
        LinearLayout forHardware = (LinearLayout) perevagyLayout.findViewById(R.id.layToAddHardware);
        insertHardWareRequirementsToLayout(forHardware, "megogo");
        perevagyLayout.setVisibility(View.GONE);
        preparedViews.add(perevagyLayout);

        if (!"".equals(activeSubscribe)) {
            View mainLayoutMegogo = LayoutInflater.from(context).inflate(R.layout.item_megogo_main, null);
            mainLayoutMegogo.setVisibility(View.GONE);
            TextView nameField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_name);
            nameField.setText("Підключена послуга");
            TextView listField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_list);
            String sb = "Якщо ви ще не зареєструвались на MEGOGO - зробіть це." +
                    "Також можете встановити додаток на свій пристрій";
            listField.setText(sb);
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
                        openInBrowser("https://play.google.com/store/apps/details?id=" + appPackageName);
                    }
                }
            });
            mainLayoutMegogo.setPadding(0, 0, 0, 50);

            preparedViews.add(mainLayoutMegogo);
        }

        for (final MegogoPts megogoPt : megogoPts) {
            if (megogoPt.getName().equals("Телевидение MEGOGO.NET (оптимальный за 0грн БАНДЛ) - 0 грн."))
                continue;

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
                        removeAllViewsAndCollapse(channelList);
                        showButton.setText("Канали");
                    } else {
                        if (name.equals("Пакет легкий")) {
                            showChannels(channelList, light, showButton);
                        } else if (name.equals("Пакет оптимальний")) {
                            showChannels(channelList, optimal, showButton);
                        } else if (name.equals("Пакет максимальний")) {
                            showChannels(channelList, maximum, showButton);
                        } else if (name.equals("Додатковий пакет FilmBox")) {
                            showChannels(channelList, filmBox, showButton);
                        } else if (name.equals("Додатковий пакет Viasat Premium")) {
                            showChannels(channelList, viasat, showButton);
                        }
                    }
                }
            });
            preparedViews.add(mainLayoutMegogo);
        }
        TextView textView = new TextView(context);
        textView.setText(R.string.megogo_additional);
        textView.setPadding(40, 30, 40, 40);
        textView.setVisibility(View.GONE);
        preparedViews.add(textView);
    }

    private HashMap<LinearLayout, Integer> imagesLoaded = new HashMap<>(); // счетчик загруженных картинок по каждому контейнеру

    private void showChannels(final LinearLayout container, final List<ChannelMegogo> chanels, final Button button) {
        container.setVisibility(View.GONE); // Скрываем контейнер

        final TextView coment = new TextView(context);
        coment.setTextColor(COLOR_GREEN);
        coment.setTextSize(16);
        coment.setTypeface(null, Typeface.BOLD);
        coment.setVisibility(View.GONE);
        if (chanels.equals(optimal)) {
            coment.setText("Всі канали з пакету \"Легкий\" та плюс наступні:");
            container.addView(coment);
        } else if (chanels.equals(maximum)) {
            coment.setText("Всі канали з пакету \"Оптимальний\" та плюс наступні:");
            container.addView(coment);
        }

        imagesLoaded.put(container, 0);

        int column = 4;
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
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            imagesLoaded.put(container, imagesLoaded.get(container) + 1);
                            return false;// Увеличиваем счетчик обработанных картинок при ошибке
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imagesLoaded.put(container, imagesLoaded.get(container) + 1);
                            return false;// Увеличиваем счетчик обработанных картинок при успехе
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter()
                    .override(dim, dim)
                    .into(imageView);
        }

        button.setText("Завантаження...");
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    int inSleep = 0;
                    while (imagesLoaded.get(container) < chanels.size()) {
                        Thread.sleep(100);
                        inSleep = inSleep + 100;
                    } // в цикле ждём когда загрузятся все картинки
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        expand(container); // всё загружено! показываем анимацию
                        coment.setVisibility(View.VISIBLE);
                        button.setText("Сховати");
                    }
                });
            }
        });
    }

    private void showMessageGoToMegogoSite() {
        String message = "Зараз ви перейдете на сайт MEGOGO. Натисніть там \"Регистрация нового аккаунта\" " +
                "та створіть собі обліковий запис";
        new MyAlertDialogBuilder(context)
                .setTitleText("Реєстрація")
                .setMessage(message)
                .setPositiveButtonWithRunnableForHandler("Перейти", new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(activationLink));
                        startActivity(i);
                    }
                }).createAndShow();
    }

    private void moreInfo(final ChannelMegogo channelMegogo) {
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

                    Thread.sleep(200);
                    int currentX = ((LinearLayout) imageView.getParent()).getWidth();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new MyAlertDialogBuilder(context)
                .setTitleTextWithWhiteBackground(channelMegogo.getTitle())
                .setView(layout)
                .createAndShow();
    }


    private void askActivate(final MegogoPts megogoPt) {
        String textMessage;
        if (!"".equals(activeSubscribe) && !megogoPt.getName().contains("пакет")) {
            textMessage = "У вас вже активовано: " + activeSubscribe + "\nПослугу буде змінено на " + megogoPt.getName();
        } else {
            textMessage = megogoPt.getName() + " буде активовано. \nВартість: " + megogoPt.getMonth() + "грн/міс";
        }

        new MyAlertDialogBuilder(context)
                .setTitleText("Активувати?")
                .setMessage(textMessage)
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        activate(megogoPt);
                    }
                }).createAndShow();
    }

    private void activate(final MegogoPts megogoPt) {
        progressDialogShow();
        if (SendInfo.activateMegogo(megogoPt.getId())) {
            DbCache.markMegogoPtsOld();
            DbCache.markServicesOld();
            try {
                Thread.sleep(TIME_TO_WAIT_BEFORE_UPDATE);
            } catch (InterruptedException ignored) {
            }
            progressDialogStopAndShowMessage("10 хвилин активація..", mainLayout);
            goTo(new TarifFragment());
        } else {
            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
        }
    }

    private void askDeActivate(final MegogoPts megogoPt) {
        new MyAlertDialogBuilder(context)
                .setTitleText("Відключити?")
                .setMessage(megogoPt.getName() + " буде відключено.")
                .setPositiveButtonWithRunnableForExecutor("Так", new Runnable() {
                    @Override
                    public void run() {
                        deActivate(megogoPt);
                    }
                }).createAndShow();
    }

    private void deActivate(final MegogoPts megogoPt) {
        progressDialogShow();
        if (SendInfo.deActivateMegogo(megogoPt.getId())) {
            DbCache.markMountlyFeeOld();
            DbCache.markMegogoPtsOld();
            DbCache.markServicesOld();
            try {
                Thread.sleep(TIME_TO_WAIT_BEFORE_UPDATE);
            } catch (InterruptedException ignored) {
            }
            progressDialogStopAndShowMessage("Відключення..", mainLayout);
            goTo(new TarifFragment());
        } else {
            progressDialogStopAndShowMessage("Трапилась помилка", mainLayout);
        }
    }
}
