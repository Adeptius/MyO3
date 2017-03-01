package ua.freenet.cabinet.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.News;

public class NewsFragment extends BaseFragment {

    @Override
    void setAllSettings() {
        titleText = "Новини та акції";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.background_news6;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {

    }

    @Override
    void doInBackground() throws Exception {

        String city = DbCache.getPerson().getAddress().getCityNameUa();

        String newsUrl = DbCache.getUrlOfCityNews(city);
        String acciiurl = DbCache.getUrlOfCityAccii(city);

        List<News> newses = getAllNews(acciiurl, newsUrl);
        sortByDate(newses);
        newses = subList(newses, 10);
        prepareNews(newses);
    }


    @Override
    void processIfOk() {
    }

    private void prepareNews(List<News> newses) {
        for (int i = 0; i < newses.size(); i++) {
            final News news = newses.get(i);

            final View itemView = LayoutInflater.from(context).inflate(R.layout.item_piece_of_news, null);

            final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView_news);
            TextView newsTitle = (TextView) itemView.findViewById(R.id.text_news_title);
            TextView comentText = (TextView) itemView.findViewById(R.id.text_news_comment);

            loadImageForNews(news, imageView);
            newsTitle.setText(news.getTitle());
            comentText.setText(news.getComment());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = news.getUrl();
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    mainLayout.removeView(progressBar);
                    mainLayout.addView(itemView);
                    itemView.startAnimation(AnimationUtils.loadAnimation(context,
                            R.anim.main_screen_trans));

                }
            });
        }
    }

    private void loadImageForNews(News news, ImageView imageView) {
        try {
            String url = getHiResImg(news.getUrl());
            if (url == null) {
                url = news.getImgUrl();
            } // если по ссылке не найден hi-res image - берём low-res
            url = url.replaceAll("http:","https:");
            URL newurl = new URL(url);

            InputStream stream = newurl.openConnection().getInputStream();
            final Bitmap loadedBitMap = BitmapFactory.decodeStream(stream);

            double y = loadedBitMap.getHeight();
            double x = loadedBitMap.getWidth();

            if (x < 200) return;

            int currentX = (int) (mainLayout.getWidth() * 0.9D);
            double ratio = y / x;

            imageView.getLayoutParams().height = (int) (currentX * ratio);
            imageView.setImageBitmap(loadedBitMap);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

    }

    private String getHiResImg(String url) throws Exception {
        Document doc2 = Jsoup.connect(url).get();
        Elements images = doc2.getElementsByTag("img");
        for (Element image : images) {
            if (image.toString().contains("/content/images/") ) {
                return "http://o3.ua" + image.attributes().get("src");
            }
        }
        return null;
    }

    private List<News> getAllNews(String urlAccii, String urlnews) throws Exception {
        Document doc = Jsoup.connect(urlAccii).get();
        Elements post = doc.body().getElementsByClass("post");

        List<News> newses = new ArrayList<>();
        for (Element element : post) {
            String href = element.getElementsByTag("figure").first()
                    .getElementsByTag("a").first()
                    .attributes().get("href");
            String imageUrl = element.getElementsByTag("figure").first()
                    .getElementsByTag("a").first()
                    .getElementsByTag("img").first()
                    .attributes().get("src");
            String date = element.getElementsByTag("div").first()
                    .getElementsByTag("time").first().html();
            String title2 = element.getElementsByTag("div").first()
                    .getElementsByTag("h3").first()
                    .getElementsByTag("a").first().html();
            String text;
            try {
                text = element.getElementsByTag("div").first()
                        .getElementsByTag("p").first().html();
            } catch (NullPointerException e) {
                text = "";
            }

            News news = new News(title2,text,href,imageUrl,convertDateToNumbers(date));
            newses.add(news);
        }

        doc = Jsoup.connect(urlnews).get();
        post = doc.body().getElementsByClass("post");

        for (Element element : post) {
            String href = element.getElementsByTag("figure").first()
                    .getElementsByTag("a").first()
                    .attributes().get("href");
            String imageUrl = element.getElementsByTag("figure").first()
                    .getElementsByTag("a").first()
                    .getElementsByTag("img").first()
                    .attributes().get("src");
            String date = element.getElementsByTag("div").first()
                    .getElementsByTag("time").first().html();
            String title2 = element.getElementsByTag("div").first()
                    .getElementsByTag("h3").first()
                    .getElementsByTag("a").first().html();
            String text;
            try {
                text = element.getElementsByTag("div").first()
                        .getElementsByTag("p").first().html();
            } catch (NullPointerException e) {
                text = "";
            }

            News news = new News(title2, text, href, imageUrl, convertDateToNumbers(date));
            newses.add(news);
        }
        return newses;
    }

    private List<News> subList(List<News> newses, int i) {
        List<News> newNews = new ArrayList<>();
        for (News newse : newses) {
            newNews.add(newse);
            if (newNews.size() == i) return newNews;
        }
        return newNews;
    }

    private static void sortByDate(List<News> newses) {
        Collections.sort(newses, new Comparator<News>() {
            @Override
            public int compare(News o1, News o2) {
                return o2.getNumberedDate().compareTo(o1.getNumberedDate());
            }
        });
    }

    private static String convertDateToNumbers(String date) {
        date = date.toLowerCase();
        String day = date.substring(0, 2);
        String year = date.substring(
                date.indexOf(" 20") + 1,
                date.indexOf(" 20") + 5
        );
        String month = "";
        if (date.contains("січня")) month = "01";
        if (date.contains("лютого")) month = "02";
        if (date.contains("березня")) month = "03";
        if (date.contains("квітня")) month = "04";
        if (date.contains("травня")) month = "05";
        if (date.contains("червня")) month = "06";
        if (date.contains("липня")) month = "07";
        if (date.contains("серпня")) month = "08";
        if (date.contains("вересня")) month = "09";
        if (date.contains("жовтня")) month = "10";
        if (date.contains("листопада")) month = "11";
        if (date.contains("грудня")) month = "12";

        if (date.contains("января")) month = "01";
        if (date.contains("февраля")) month = "02";
        if (date.contains("марта")) month = "03";
        if (date.contains("апреля")) month = "04";
        if (date.contains("мая")) month = "05";
        if (date.contains("июня")) month = "06";
        if (date.contains("июля")) month = "07";
        if (date.contains("августа")) month = "08";
        if (date.contains("сентября")) month = "09";
        if (date.contains("октября")) month = "10";
        if (date.contains("ноября")) month = "11";
        if (date.contains("декабря")) month = "12";
        return year + month + day;
    }
}

