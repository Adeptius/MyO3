package ua.adeptius.myo3.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.ChannelDivan;
import ua.adeptius.myo3.model.ChannelDivanDetails;
import ua.adeptius.myo3.model.ChannelMegogo;

import static ua.adeptius.myo3.utils.Utilits.doTwoSymb;


//TODO як замовити послугу
public class DivanTvFragment extends BaseFragment {

    Button buttonStart;
    Button buttonOptimal;
    Button buttonPrestige;
    Button buttonVip;
    Button buttonFilm;
    Button buttonEnglish;
    Button buttonKid;
    Button buttonTv1000;
    Button buttonNight;
    Button buttonViasat;
    LinearLayout listStart;
    LinearLayout listOptimal;
    LinearLayout listPrestige;
    LinearLayout listVip;
    LinearLayout listFilm;
    LinearLayout listEnglish;
    LinearLayout listKid;
    LinearLayout listTv1000;
    LinearLayout listNight;
    LinearLayout listViasat;

    @Override
    void setAllSettings() {
        titleText = "";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.divantv_logo3;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();

    }

    @Override
    void doInBackground() throws Exception {

    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }

    private void draw() {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_divantv_news, null);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView_news);
        TextView newsTitle = (TextView) itemView.findViewById(R.id.text_news_title);
        TextView comentText = (TextView) itemView.findViewById(R.id.text_news_comment);
        loadImageForNews(imageView, R.drawable.divan_tv_news1);
        newsTitle.setText("Ваші улюблені ТВ-канали. Та навіть більше.");
        comentText.setText("Враховуючи ваші смаки, ми подбали про те, щоб ви були задоволені.");
        ImageView imageView2 = (ImageView) itemView.findViewById(R.id.imageView_news2);
        TextView newsTitle2 = (TextView) itemView.findViewById(R.id.text_news_title2);
        TextView comentText2 = (TextView) itemView.findViewById(R.id.text_news_comment2);
        loadImageForNews(imageView2, R.drawable.divan_tv_news2);
        newsTitle2.setText("Дивіться де завгодно!");
        comentText2.setText("Доступно на більшості пристроїв та працює всюди, де є інтернет.");
        ImageView imageView3 = (ImageView) itemView.findViewById(R.id.imageView_news3);
        TextView newsTitle3 = (TextView) itemView.findViewById(R.id.text_news_title3);
        TextView comentText3 = (TextView) itemView.findViewById(R.id.text_news_comment3);
        loadImageForNews(imageView3, R.drawable.divan_tv_news3);
        newsTitle3.setText("Шоу не роспочнеться без вас!");
        comentText3.setText("Ви самі обираєте що і коли дивитися.");
        mainLayout.addView(itemView);

        View allChanels = LayoutInflater.from(context).inflate(R.layout.item_divantv_tarifs, null);
        mainLayout.addView(allChanels);

        buttonStart = (Button) allChanels.findViewById(R.id.divan_button_start);
        buttonOptimal = (Button) allChanels.findViewById(R.id.divan_button_optimal);
        buttonPrestige = (Button) allChanels.findViewById(R.id.divan_button_prestige);
        buttonVip = (Button) allChanels.findViewById(R.id.divan_button_vip);
        listStart = (LinearLayout) allChanels.findViewById(R.id.divan_list_start);
        listOptimal = (LinearLayout) allChanels.findViewById(R.id.divan_list_optimal);
        listPrestige = (LinearLayout) allChanels.findViewById(R.id.divan_list_prestige);
        listVip = (LinearLayout) allChanels.findViewById(R.id.divan_list_vip);

        View allPackets = LayoutInflater.from(context).inflate(R.layout.item_divan_packets, null);
        mainLayout.addView(allPackets);

        buttonFilm = (Button) allPackets.findViewById(R.id.divan_button_film);
        buttonEnglish = (Button) allPackets.findViewById(R.id.divan_button_english);
        buttonKid = (Button) allPackets.findViewById(R.id.divan_button_kid);
        buttonTv1000 = (Button) allPackets.findViewById(R.id.divan_button_tv1000);
        buttonNight = (Button) allPackets.findViewById(R.id.divan_button_night);
        buttonViasat = (Button) allPackets.findViewById(R.id.divan_button_viasat);
        listFilm = (LinearLayout) allPackets.findViewById(R.id.divan_list_film);
        listEnglish = (LinearLayout) allPackets.findViewById(R.id.divan_list_english);
        listKid = (LinearLayout) allPackets.findViewById(R.id.divan_list_kid);
        listTv1000 = (LinearLayout) allPackets.findViewById(R.id.divan_list_tv1000);
        listNight = (LinearLayout) allPackets.findViewById(R.id.divan_list_night);
        listViasat = (LinearLayout) allPackets.findViewById(R.id.divan_list_viasat);

        buttonStart.setOnClickListener(this);
        buttonOptimal.setOnClickListener(this);
        buttonPrestige.setOnClickListener(this);
        buttonVip.setOnClickListener(this);
        buttonFilm.setOnClickListener(this);
        buttonEnglish.setOnClickListener(this);
        buttonKid.setOnClickListener(this);
        buttonTv1000.setOnClickListener(this);
        buttonNight.setOnClickListener(this);
        buttonViasat.setOnClickListener(this);
    }

    private void loadImageForNews(ImageView imageView, int drawableId) {
        try {
            final Bitmap loadedBitMap = BitmapFactory
                    .decodeResource(getResources(), drawableId);
            double y = loadedBitMap.getHeight();
            double x = loadedBitMap.getWidth();

            int currentX = (int) (SCREEN_WIDTH * 0.9D);
            double ratio = y / x;
            int needY = (int) (currentX * ratio);

            imageView.getLayoutParams().height = needY;
            imageView.setImageBitmap(loadedBitMap);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void drawIcons(final LinearLayout container, List<ChannelDivan> divanList) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;

        int column = 3;
        if (dpi > 600) {
            column = 3;
        } else if (dpi > 450) {
            column = 4;
        } else if (dpi > 300) {
            column = 5;
        } else if (dpi > 150) {
            column = 6;
        }

        LinearLayout layout = null;

        for (final ChannelDivan channel : divanList) {
            if (layout == null || layout.getChildCount() == column) {
                layout = new LinearLayout(context);
                container.addView(layout, MATCH_WRAP);
            }
            ImageView imageView = new ImageView(context);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInfoAboutChanel(channel);
                }
            });
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

    private void showInfoAboutChanel(final ChannelDivan channelDivan) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        final TextView titleView = new TextView(context);
        titleView.setText(channelDivan.getName());
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        titleView.setBackgroundColor(Color.WHITE);
        builder.setCustomTitle(titleView);

        final View layout = LayoutInflater.from(context).inflate(R.layout.item_divan_chanel_detail, null);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
        final TextView description = (TextView) layout.findViewById(R.id.text_description);
        final TextView packets = (TextView) layout.findViewById(R.id.text_packets);
        final TextView showOn = (TextView) layout.findViewById(R.id.text_show_on);
        final TextView error = (TextView) layout.findViewById(R.id.text_error);
        final LinearLayout lin = (LinearLayout) layout.findViewById(R.id.lin);
        final LinearLayout loading = (LinearLayout) layout.findViewById(R.id.loading_layout);



        builder.setView(layout);

        builder.setCustomTitle(titleView);

        AlertDialog dialog = builder.create();
        dialog.show();
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final ChannelDivanDetails details = GetInfo.getDivanDetails(channelDivan.getId());
                    try {
                        String url = details.getImage();
                        URL newurl = new URL(url);
                        final Bitmap loadedBitMap = BitmapFactory
                                .decodeStream(newurl.openConnection().getInputStream());

                        double y = loadedBitMap.getHeight();
                        double x = loadedBitMap.getWidth();

                        int currentX = ((LinearLayout)imageView.getParent()).getWidth();
                        double ratio = y / x;
                        final int needY = (int) (currentX * ratio);

                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);
                                imageView.getLayoutParams().height = needY;
                                imageView.setImageBitmap(loadedBitMap);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            description.setText(details.getDescription());
                            packets.setText(details.getAvailableIn());
                            showOn.setText(details.getAvailableOn());
                        }
                    });
                } catch (Exception e) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            error.setVisibility(View.VISIBLE);
                        }
                    });
                } finally {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void showList(final LinearLayout layout, final String url) {
        final ProgressBar progressBar = new ProgressBar(context);
        layout.addView(progressBar);

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<ChannelDivan> divanList = GetInfo.getDivanChanels(url);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            drawIcons(layout, divanList);
                        }
                    });

                } catch (Exception e) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = new TextView(context);
                            textView.setText("Трапилась помилка");
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextSize(18);
                            layout.addView(textView);
                        }
                    });

                } finally {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            layout.removeView(progressBar);
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonStart)) {
            if (listStart.getChildCount() > 0) {
                listStart.removeAllViews();
                buttonStart.setText("Список каналів");
            } else {
                showList(listStart, "https://divan.tv/tariffs/channels/?tariff_id=37&devices=all&tariff_name=%D0%A1%D1%82%D0%B0%D1%80%D1%82%D0%BE%D0%B2%D1%8B%D0%B9");
                buttonStart.setText("Сховати");
            }
        } else if (v.equals(buttonOptimal)) {
            if (listOptimal.getChildCount() > 0) {
                listOptimal.removeAllViews();
                buttonOptimal.setText("Список каналів");
            } else {
                showList(listOptimal, "https://divan.tv/tariffs/channels/?tariff_id=130&devices=all&tariff_name=%D0%9E%D0%BF%D1%82%D0%B8%D0%BC%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9");
                buttonOptimal.setText("Сховати");
            }
        } else if (v.equals(buttonPrestige)) {
            if (listPrestige.getChildCount() > 0) {
                listPrestige.removeAllViews();
                buttonPrestige.setText("Список каналів");
            } else {
                showList(listPrestige, "https://divan.tv/tariffs/channels/?tariff_id=13&devices=all&tariff_name=%D0%9F%D1%80%D0%B5%D1%81%D1%82%D0%B8%D0%B6%D0%BD%D1%8B%D0%B9");
                buttonPrestige.setText("Сховати");
            }
        } else if (v.equals(buttonVip)) {
            if (listVip.getChildCount() > 0) {
                listVip.removeAllViews();
                buttonVip.setText("Список каналів");
            } else {
                showList(listVip, "https://divan.tv/tariffs/channels/?tariff_id=99&devices=all&tariff_name=VIP");
                buttonVip.setText("Сховати");
            }
        } else if (v.equals(buttonFilm)) {
            if (listFilm.getChildCount() > 0) {
                listFilm.removeAllViews();
                buttonFilm.setText("Список каналів");
            } else {
                showList(listFilm, "https://divan.tv/tariffs/channels/?tariff_id=38&devices=all&tariff_name=%D0%A4%D0%B8%D0%BB%D1%8C%D0%BC%D0%BE%D0%B2%D1%8B%D0%B9");
                buttonFilm.setText("Сховати");
            }
        } else if (v.equals(buttonEnglish)) {
            if (listEnglish.getChildCount() > 0) {
                listEnglish.removeAllViews();
                buttonEnglish.setText("Список каналів");
            } else {
                showList(listEnglish, "https://divan.tv/tariffs/channels/?tariff_id=12&devices=all&tariff_name=In+English");
                buttonEnglish.setText("Сховати");
            }
        } else if (v.equals(buttonKid)) {
            if (listKid.getChildCount() > 0) {
                listKid.removeAllViews();
                buttonKid.setText("Список каналів");
            } else {
                showList(listKid, "https://divan.tv/tariffs/channels/?tariff_id=23&devices=all&tariff_name=%D0%94%D0%B5%D1%82%D1%81%D0%BA%D0%B8%D0%B9");
                buttonKid.setText("Сховати");
            }
        } else if (v.equals(buttonTv1000)) {
            if (listTv1000.getChildCount() > 0) {
                listTv1000.removeAllViews();
                buttonTv1000.setText("Список каналів");
            } else {
                showList(listTv1000, "https://divan.tv/tariffs/channels/?tariff_id=206&devices=all&tariff_name=TV1000+%D0%9F%D0%BB%D1%8E%D1%81");
                buttonTv1000.setText("Сховати");
            }
        } else if (v.equals(buttonNight)) {
            if (listNight.getChildCount() > 0) {
                listNight.removeAllViews();
                buttonNight.setText("Список каналів");
            } else {
                showList(listNight, "https://divan.tv/tariffs/channels/?tariff_id=320&devices=all&tariff_name=%D0%9D%D0%BE%D1%87%D0%BD%D0%BE%D0%B9");
                buttonNight.setText("Сховати");
            }
        } else if (v.equals(buttonViasat)) {
            if (listViasat.getChildCount() > 0) {
                listViasat.removeAllViews();
                buttonViasat.setText("Список каналів");
            } else {
                showList(listViasat, "https://divan.tv/tariffs/channels/?tariff_id=198&devices=all&tariff_name=VIASAT+Premium");
                buttonViasat.setText("Сховати");
            }
        }
    }
}