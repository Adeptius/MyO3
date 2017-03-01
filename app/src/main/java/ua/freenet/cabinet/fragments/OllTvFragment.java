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

import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.ChannelOllTv;
import ua.freenet.cabinet.utils.Utilits;


public class OllTvFragment  extends BaseFragment {

    private Button buttonStart;
    private Button buttonOptimal;
    private Button buttonPremium;
    private LinearLayout listStart;
    private LinearLayout listOptimal;
    private LinearLayout listPremium;
    private List<ChannelOllTv> start;
    private List<ChannelOllTv> optimal;
    private List<ChannelOllTv> premium;
    private boolean hardwareIsHidden = true;

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
        mainLayout.addView(perevagyLayout);

        final View hardware = LayoutInflater.from(context).inflate(R.layout.item_hardware, null);
        final Button hideButton = (Button) hardware.findViewById(R.id.button_hide);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAndShowHardware(hardware, hideButton);
            }
        });
        mainLayout.addView(hardware);

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
                listStart.removeAllViews();
                buttonStart.setText("Список каналів");
            } else {
                drawIcons(listStart, start);
                buttonStart.setText("Сховати");
            }
        } else if (v.equals(buttonOptimal)) {
            if (listOptimal.getChildCount() > 0) {
                listOptimal.removeAllViews();
                buttonOptimal.setText("Список каналів");
            } else {
                drawIcons(listOptimal, optimal);
                buttonOptimal.setText("Сховати");
            }
        } else if (v.equals(buttonPremium)) {
            if (listPremium.getChildCount() > 0) {
                listPremium.removeAllViews();
                buttonPremium.setText("Список каналів");
            } else {
                drawIcons(listPremium, premium);
                buttonPremium.setText("Сховати");
            }
        }
    }

    private void drawIcons(final LinearLayout container, List<ChannelOllTv> ollTvList) {
        TextView coment = new TextView(context);
        coment.setTextColor(COLOR_GREEN);
        coment.setTextSize(16);
        coment.setTypeface(null, Typeface.BOLD);
        if (container.equals(listOptimal)){
            coment.setText("Всі канали з пакету \"Стартовий\" та плюс наступні:");
            container.addView(coment);
        }else if (container.equals(listPremium)){
            coment.setText("Всі канали з пакету \"Оптимальний\" та плюс наступні:");
            container.addView(coment);
        }

        int column = 4;
        LinearLayout layout = null;

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
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .fitCenter()
                    .override(dim, dim)
                    .into(imageView);
        }
    }



    private void hideAndShowHardware(View hardware, Button hideButton) {
        LinearLayout hideLayout = (LinearLayout) hardware.findViewById(R.id.lay_hide);
        Button downloadButton = (Button) hardware.findViewById(R.id.button_download);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "tv.oll.app";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        if (hardwareIsHidden){
            hideLayout.setVisibility(View.VISIBLE);
            hideButton.setText("Сховати");
            hardwareIsHidden = false;
        }else {
            hardwareIsHidden = true;
            hideButton.setText("Показати необхідне обладнання");
            hideLayout.setVisibility(View.GONE);
        }
    }
}
