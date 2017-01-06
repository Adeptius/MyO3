package ua.adeptius.myo3.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.Channel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class DivanTvFragment extends BaseFragment {


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
//        getSite();
        draw();
    }



    @Override
    void processIfOk() {
        animateScreen();
    }

    private void draw() {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_piece_of_news, null);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView_news);
        TextView newsTitle = (TextView) itemView.findViewById(R.id.text_news_title);
        TextView comentText = (TextView) itemView.findViewById(R.id.text_news_comment);
        loadImageForNews(imageView, R.drawable.divan_tv_news1);
        newsTitle.setText("Ваші улюблені ТВ-канали. Та навіть більше.");
        comentText.setText("");
        mainLayout.addView(itemView);





    }

    private void loadImageForNews(ImageView imageView, int drawableId) {
        try {
            final Bitmap loadedBitMap = BitmapFactory
                    .decodeResource(getResources(), drawableId);
//            view.setImageBitmap(loadedBitMap);
//            view.setScaleType(ImageView.ScaleType.CENTER_CROP);

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




    private void getSite() throws Exception {

        Document doc = Jsoup.connect("https://divan.tv/tariffs/channels/?tariff_id=99&devices=all&tariff_name=VIP").get();
        Element ul = doc.getElementsByClass("ci-channels").first();
        Elements lis = ul.getElementsByTag("li");
        List<Channel> channels = new ArrayList<>();
        for (Element li : lis) {
            Element firstDiv = li.getElementsByTag("div").first();
            String id = firstDiv.attr("data-id");
            Element secondDiv = firstDiv.getElementsByClass("image").first();

            Element img = secondDiv.getElementsByTag("img").first();
            String name = img.attr("alt");
            String imageUrl = img.attr("src");

            Channel channel = new Channel(id, name, imageUrl);
            channels.add(channel);
        }

        for (Channel channel : channels) {
            System.out.println(channel.getName());
        }
        System.out.println("CHANNELS SIZE"+channels.size());
    }


    @Override
    public void onClick(View v) {

    }
}
