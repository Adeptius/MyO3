package ua.freenet.cabinet.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.HashMap;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.GetInfo;
import ua.freenet.cabinet.model.ChannelDivan;
import ua.freenet.cabinet.model.ChannelDivanDetails;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;


public class DivanTvFragment extends HelperFragment implements View.OnClickListener {

    private Button buttonStart;
    private Button buttonOptimal;
    private Button buttonPrestige;
    private Button buttonVip;
    private Button buttonFilm;
    private Button buttonEnglish;
    private Button buttonKid;
    private Button buttonTv1000;
    private Button buttonNight;
    private Button buttonViasat;
    private LinearLayout listStart;
    private LinearLayout listOptimal;
    private LinearLayout listPrestige;
    private LinearLayout listVip;
    private LinearLayout listFilm;
    private LinearLayout listEnglish;
    private LinearLayout listKid;
    private LinearLayout listTv1000;
    private LinearLayout listNight;
    private LinearLayout listViasat;

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
        LinearLayout forHardware = (LinearLayout) itemView.findViewById(R.id.layForHardware);
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


        View allChanels = LayoutInflater.from(context).inflate(R.layout.item_divantv_tarifs, null);
        buttonStart = (Button) allChanels.findViewById(R.id.divan_button_start);
        buttonOptimal = (Button) allChanels.findViewById(R.id.divan_button_optimal);
        buttonPrestige = (Button) allChanels.findViewById(R.id.divan_button_prestige);
        buttonVip = (Button) allChanels.findViewById(R.id.divan_button_vip);
        listStart = (LinearLayout) allChanels.findViewById(R.id.divan_list_start);
        listOptimal = (LinearLayout) allChanels.findViewById(R.id.divan_list_optimal);
        listPrestige = (LinearLayout) allChanels.findViewById(R.id.divan_list_prestige);
        listVip = (LinearLayout) allChanels.findViewById(R.id.divan_list_vip);

        View allPackets = LayoutInflater.from(context).inflate(R.layout.item_divan_packets, null);
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

        itemView.setVisibility(View.INVISIBLE);
        allChanels.setVisibility(View.INVISIBLE);
        allPackets.setVisibility(View.INVISIBLE);
        mainLayout.addView(itemView);
        mainLayout.addView(allChanels);
        mainLayout.addView(allPackets);
        addHardWareRequirementsToLayout(forHardware,"divan");
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

    private HashMap<LinearLayout, Integer> imagesLoaded = new HashMap<>(); // счетчик загруженных картинок по каждому контейнеру


    private void drawIcons(final LinearLayout container, final List<ChannelDivan> divanList, final Button button) {
        container.setVisibility(View.GONE); // Скрываем контейнер

        int column = 4;

        LinearLayout layout = null;
        imagesLoaded.put(container, 0);

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

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    int inSleep = 0;
                    while (imagesLoaded.get(container) < divanList.size()) {
                        System.out.println("imagesLoaded.get(container) " + imagesLoaded.get(container));
                        System.out.println("divanList.size() " + divanList.size());
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
                        button.setText("Сховати");
                    }
                });
            }
        });
    }

    private void showInfoAboutChanel(final ChannelDivan channelDivan) {

        final View layout = LayoutInflater.from(context).inflate(R.layout.item_divan_chanel_detail, null);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
        final TextView description = (TextView) layout.findViewById(R.id.text_description);
        final TextView packets = (TextView) layout.findViewById(R.id.text_packets);
        final TextView showOn = (TextView) layout.findViewById(R.id.text_show_on);
        final TextView error = (TextView) layout.findViewById(R.id.text_error);
        final LinearLayout lin = (LinearLayout) layout.findViewById(R.id.lin);
        final LinearLayout loading = (LinearLayout) layout.findViewById(R.id.loading_layout);

        new MyAlertDialogBuilder(context)
                .setView(layout)
                .createAndShow();

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

                        int currentX = ((LinearLayout) imageView.getParent()).getWidth();
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

    private void showList(final LinearLayout layout, final Button button, final String url) {
        button.setText("Завантаження...");
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<ChannelDivan> divanList = DbCache.getDivanChanels(url);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            drawIcons(layout, divanList, button);
                        }
                    });
                } catch (Exception e) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            button.setText("Список каналів");
                            makeSimpleSnackBar("Помилка завантаження", button);
                        }
                    });
                }
            }
        });
    }

    protected void removeAllViewsAndCollapseDivan(View view, Button button) {
        removeAllViewsAndCollapse(view);
        button.setText("Список каналів");
    }

    @Override
    public void onClick(View v) {
        if (v.equals(buttonStart)) {
            if (listStart.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listStart, buttonStart);
            } else {
                showList(listStart, buttonStart, "https://divan.tv/tariffs/channels/?tariff_id=37&devices=all&tariff_name=%D0%A1%D1%82%D0%B0%D1%80%D1%82%D0%BE%D0%B2%D1%8B%D0%B9");
            }
        } else if (v.equals(buttonOptimal)) {
            if (listOptimal.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listOptimal, buttonOptimal);
            } else {
                showList(listOptimal, buttonOptimal, "https://divan.tv/tariffs/channels/?tariff_id=130&devices=all&tariff_name=%D0%9E%D0%BF%D1%82%D0%B8%D0%BC%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9");
            }
        } else if (v.equals(buttonPrestige)) {
            if (listPrestige.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listPrestige, buttonPrestige);
            } else {
                showList(listPrestige, buttonPrestige, "https://divan.tv/tariffs/channels/?tariff_id=13&devices=all&tariff_name=%D0%9F%D1%80%D0%B5%D1%81%D1%82%D0%B8%D0%B6%D0%BD%D1%8B%D0%B9");
            }
        } else if (v.equals(buttonVip)) {
            if (listVip.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listVip, buttonVip);
            } else {
                showList(listVip, buttonVip, "https://divan.tv/tariffs/channels/?tariff_id=99&devices=all&tariff_name=VIP");
            }
        } else if (v.equals(buttonFilm)) {
            if (listFilm.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listFilm, buttonFilm);
            } else {
                showList(listFilm, buttonFilm, "https://divan.tv/tariffs/channels/?tariff_id=38&devices=all&tariff_name=%D0%A4%D0%B8%D0%BB%D1%8C%D0%BC%D0%BE%D0%B2%D1%8B%D0%B9");
            }
        } else if (v.equals(buttonEnglish)) {
            if (listEnglish.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listEnglish, buttonEnglish);
            } else {
                showList(listEnglish, buttonEnglish, "https://divan.tv/tariffs/channels/?tariff_id=12&devices=all&tariff_name=In+English");
            }
        } else if (v.equals(buttonKid)) {
            if (listKid.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listKid, buttonKid);
            } else {
                showList(listKid, buttonKid, "https://divan.tv/tariffs/channels/?tariff_id=23&devices=all&tariff_name=%D0%94%D0%B5%D1%82%D1%81%D0%BA%D0%B8%D0%B9");
            }
        } else if (v.equals(buttonTv1000)) {
            if (listTv1000.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listTv1000, buttonTv1000);
            } else {
                showList(listTv1000, buttonTv1000, "https://divan.tv/tariffs/channels/?tariff_id=206&devices=all&tariff_name=TV1000+%D0%9F%D0%BB%D1%8E%D1%81");
            }
        } else if (v.equals(buttonNight)) {
            if (listNight.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listNight, buttonNight);
            } else {
                showList(listNight, buttonNight, "https://divan.tv/tariffs/channels/?tariff_id=320&devices=all&tariff_name=%D0%9D%D0%BE%D1%87%D0%BD%D0%BE%D0%B9");
            }
        } else if (v.equals(buttonViasat)) {
            if (listViasat.getChildCount() > 0) {
                removeAllViewsAndCollapseDivan(listViasat, buttonViasat);
            } else {
                showList(listViasat, buttonViasat, "https://divan.tv/tariffs/channels/?tariff_id=198&devices=all&tariff_name=VIASAT+Premium");
            }
        }
    }
}
