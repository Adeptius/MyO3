package ua.freenet.cabinet.fragments;


import android.graphics.Typeface;
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

import java.util.HashMap;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.ChannelOllTv;


public class OllTvFragment  extends HelperFragment implements View.OnClickListener {

    private Button buttonStart;
    private Button buttonOptimal;
    private Button buttonPremium;
    private LinearLayout listStart;
    private LinearLayout listOptimal;
    private LinearLayout listPremium;
    private List<ChannelOllTv> start;
    private List<ChannelOllTv> optimal;
    private List<ChannelOllTv> premium;


    @Override
    void setAllSettings() {
        titleText = "";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.background_oll_tv;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        start = DbCache.getOllTvStartChannels();
        optimal = DbCache.getOllTvOptimalChannels();
        premium = DbCache.getOllTvPremiumChannels();
        deleteSecondFromFirst(premium, optimal);
        deleteSecondFromFirst(optimal, start);
    }

    private void deleteSecondFromFirst(List<ChannelOllTv> second, List<ChannelOllTv> first) {
        for (ChannelOllTv channelOllTv : first) {
            if (second.contains(channelOllTv)){
                second.remove(channelOllTv);
            }
        }
    }

    @Override
    void processIfOk() {
        draw();
        hideAllViewsInMainScreen();
        animateScreen();
    }

    private void draw() {
        View perevagyLayout = LayoutInflater.from(context).inflate(R.layout.item_olltv_perevagy, null);
        LinearLayout forHardware = (LinearLayout) perevagyLayout.findViewById(R.id.layForHardware);
        mainLayout.addView(perevagyLayout);

        addHardWareRequirementsToLayout(forHardware,"oll");

        View allChanels = LayoutInflater.from(context).inflate(R.layout.item_olltv_tarif, null);
        mainLayout.addView(allChanels);

        View allPackets = LayoutInflater.from(context).inflate(R.layout.item_olltv_packets, null);
        mainLayout.addView(allPackets);

        buttonStart = (Button) allChanels.findViewById(R.id.olltv_button_start);
        buttonOptimal = (Button) allChanels.findViewById(R.id.olltv_button_optimal);
        buttonPremium = (Button) allChanels.findViewById(R.id.olltv_button_premium);

        listStart = (LinearLayout) allChanels.findViewById(R.id.olltv_list_start);
        listOptimal = (LinearLayout) allChanels.findViewById(R.id.olltv_list_optimal);
        listPremium = (LinearLayout) allChanels.findViewById(R.id.olltv_list_premium);

        buttonStart.setOnClickListener(this);
        buttonOptimal.setOnClickListener(this);
        buttonPremium.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonStart)) {
            if (listStart.getChildCount() > 0) {
                removeAllViewsAndCollapse(listStart);
                buttonStart.setText("Список каналів");
            } else {
                drawIcons(listStart, start, buttonStart);
            }
        } else if (v.equals(buttonOptimal)) {
            if (listOptimal.getChildCount() > 0) {
                removeAllViewsAndCollapse(listOptimal);
                buttonOptimal.setText("Список каналів");
            } else {
                drawIcons(listOptimal, optimal, buttonOptimal);
            }
        } else if (v.equals(buttonPremium)) {
            if (listPremium.getChildCount() > 0) {
                removeAllViewsAndCollapse(listPremium);
                buttonPremium.setText("Список каналів");
            } else {
                drawIcons(listPremium, premium, buttonPremium);
            }
        }
    }

    private HashMap<LinearLayout, Integer> imagesLoaded = new HashMap<>(); // счетчик загруженных картинок по каждому контейнеру

    private void drawIcons(final LinearLayout container, final List<ChannelOllTv> ollTvList, final Button button) {
        container.setVisibility(View.GONE); // Скрываем контейнер

        final TextView coment = new TextView(context);
        coment.setTextColor(COLOR_GREEN);
        coment.setTextSize(16);
        coment.setTypeface(null, Typeface.BOLD);
        coment.setVisibility(View.GONE);
        if (container.equals(listOptimal)){
            coment.setText("Всі канали з пакету \"Стартовий\" та плюс наступні:");
            container.addView(coment);
        }else if (container.equals(listPremium)){
            coment.setText("Всі канали з пакету \"Оптимальний\" та плюс наступні:");
            container.addView(coment);
        }

        imagesLoaded.put(container,0);

        int column = 4;
        LinearLayout layout = null;

        button.setText("Завантаження...");
        for (final ChannelOllTv channel : ollTvList) {
            if (layout == null || layout.getChildCount() == column) {
                layout = new LinearLayout(context);
                container.addView(layout, MATCH_WRAP);
            }
            ImageView imageView = new ImageView(context);
            layout.addView(imageView, WRAP_WRAP);

            int widht = container.getWidth();

            int dim = widht / column;

            Glide.with(this)
                    .load(channel.getIconUrl())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            imagesLoaded.put(container,imagesLoaded.get(container)+1);
                            return false;// Увеличиваем счетчик обработанных картинок при ошибке
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imagesLoaded.put(container,imagesLoaded.get(container)+1);
                            return false;// Увеличиваем счетчик обработанных картинок при успехе
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter()
                    .override(dim, dim)
                    .into(imageView);
        }

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    int inSleep = 0;
                    while (imagesLoaded.get(container)<ollTvList.size()){
                        Thread.sleep(100);
                        inSleep = inSleep + 100;
                    } // в цикле ждём когда загрузятся все картинки
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        coment.setVisibility(View.VISIBLE);
                        expand(container); // всё загружено! показываем анимацию
                        button.setText("Сховати");
                    }
                });
            }
        });


    }
}
